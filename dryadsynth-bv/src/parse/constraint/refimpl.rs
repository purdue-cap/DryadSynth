use derive_more::DebugCustom;
use rand::{rngs::StdRng, seq::SliceRandom};

use crate::{enumerate::expr::OwnedExpr, parse::{SExpr, self, new_custom_error_span}};

use super::PbeConstraint;




#[derive(DebugCustom, Clone)]
#[debug(fmt = "{:?}", ref_impl)]
pub struct RefImplConstraint {
    pub args: usize,
    pub ref_impl: OwnedExpr,
}

impl RefImplConstraint {
    pub fn parse(sexpr: & SExpr) -> Result<Option<Self>, parse::Error> {
        for e in sexpr.list().expect("Toplevel should be a list.") {
            if let Some(("define-fun", [name, args, _, body])) = e.call() {
                let l = args.list().ok_or(new_custom_error_span("Expecting list of (id type) in arg".into(), args.span().clone()))?;
                let mut vargs = Vec::new();
                for e in l {
                    if let Some([SExpr::Id(x, _), _]) = e.list() {
                        vargs.push(*x);
                    } else { return Err(new_custom_error_span("Expecting list of (id type) in arg".into(), args.span().clone()));}
                }
                let e = OwnedExpr::from_sexpr(body, &*vargs)?;
                return Ok(Some(Self { ref_impl: e, args: vargs.len() }))
            }
        }
        Ok(None)
    }
    pub fn generate_pbe(&self, count: usize, arity: usize, rng: &mut StdRng) -> PbeConstraint {
        let mut res = PbeConstraint::new(arity);
        for _ in 0..count {
            let mask = [u64::MAX, u64::MAX, u64::MAX, u64::MAX, 0xFF, 0xFFFFFFFFFFFF, 0xFFFFFF0000FFFFFF ].choose(rng).unwrap();
            res.random_example(rng, *mask, &self.ref_impl, arity)
        }
        res
    }
}




