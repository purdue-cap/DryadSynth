
use std::{array, cell::UnsafeCell};

use derive_more::DebugCustom;

use crate::{enumerate::expr::OwnedExpr, info};

use super::{new_custom_error_span, Error, SExpr};

#[derive(Clone, DebugCustom)]
#[debug(fmt = "define-fun {} {:?}", name, args)]
pub struct DefineFun {
    pub name: &'static str,
    pub args: Vec<&'static str>,
    pub expr: OwnedExpr,
}

pub struct Environ(UnsafeCell<[Vec<DefineFun>; 3]>);

impl Environ {
    pub fn new() -> Self {
        Environ(array::from_fn(|_| Vec::new()).into())
    }

    pub fn inner(&self) -> &mut [Vec<DefineFun>; 3] {
        unsafe { self.0.get().as_mut().unwrap() }
    }


    pub fn parse<'a>(&self, sexpr: &'a SExpr<'static>) -> Result<(), Error> {
        if let SExpr::List(l, span) = sexpr {
            for e in l {
                let e: & SExpr<'static> = e;
                if let Some(("define-fun", [SExpr::Id(name, span), args, ret, expr])) = e.call() {
                    if let Some(args) = args.list() {
                        let args: Vec<&'static str> = args.iter().map(|x| x.call().map(|y| y.0).ok_or(new_custom_error_span("DefineFun: Expecting (<argname> <type>).".into(), x.span().clone()))).collect::<Result<_, _>>()?;
                        if args.len() > 2 { return Err(new_custom_error_span("DefineFun: Number of args must be <= 2".into(), span.clone())); }

                        let expr = OwnedExpr::from_sexpr(&expr, args.as_slice())?;
                        info!("DefineFun: {} {:?} -> {:?}", name, args, expr);
                        self.inner()[args.len()].push(DefineFun { name: *name, args, expr })
                    } else { return Err(new_custom_error_span("DefineFun: Expecting Argument list".into(), args.span().clone())); }
                }
            }
            Ok(())
        } else { panic!("Toplevel should be a list: {sexpr}") }
    }

    pub fn lookup(&self, name: &str, nargs: usize) -> Option<usize> {
        self.inner()[nargs].iter().enumerate().find(|(_, p)| p.name == name).map(|(a,_)| a)
    }
    pub fn apply(&self, id: usize, nargs: usize) -> &OwnedExpr {
        &self.inner()[nargs][id].expr
    }
    pub fn name(&self, id: usize, nargs: usize) -> &'static str {
        &self.inner()[nargs][id].name
    }
}

unsafe impl Sync for Environ {}

lazy_static::lazy_static!(
    pub static ref ENV: Environ = Environ::new();
);

