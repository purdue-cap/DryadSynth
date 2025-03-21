use std::{ops::{Div, Shl, Shr, Neg, Not}, simd::{LaneCount, SupportedLaneCount}};

use bumpalo::Bump;
use derive_more::{DebugCustom, Display};
use either::Either;
use thiserror::Error;
// use z3::{ast::{BV, Ast}, Symbol};

use crate::{parse::{SExpr, self, literals, new_custom_error_span}, bv_op_of, all_ops, enum_expr, enum_ownedexpr, smt_name_of, enumerate::op};

use super::{Bv};

enum_expr!();

#[derive(Error, Debug)]
#[error("divided by zero")]
pub struct DivideByZero;

impl<'a> Expr<'a> {
    pub fn eval(&self, args: &[u64]) -> Result<u64, DivideByZero> {
        macro_rules! foreach_op {
            (1, false, $op: ident) => {
                if let Expr::$op(e1) = self {
                    return Ok(crate::op_of![$op](e1.eval(args)?));
                }
            };
            (2, false, $op: ident) => {
                if let Expr::$op(e1, e2) = self {
                    return Ok(crate::op_of![$op](e1.eval(args)?, e2.eval(args)?));
                }
            };
            (2, true, $op: ident) => {
                if let Expr::$op(e1, e2) = self {
                    return crate::op_of![$op](e1.eval(args)?, e2.eval(args)?);
                }
            };
        }
        if let Expr::Var(a) = self { return Ok(args[*a]);}
        if let Expr::Const(a) = self { return Ok(*a);}
        if let Expr::Ite(a, b, c) = self { return Ok(op::ite(a.eval(args)?, b.eval(args)?, c.eval(args)?)); }
        all_ops!(arity nonzero);
        panic!("should not reach here. {:?}", self);
    }
    pub fn eval_bv<const N: usize>(&self, args: &[Bv<N>]) -> Bv<N> {
        macro_rules! foreach_op {
            (1, $op: ident) => {
                if let Expr::$op(e1) = self {
                    return crate::bv_op_of![$op](e1.eval_bv(args));
                }
            };
            (2, $op: ident) => {
                if let Expr::$op(e1, e2) = self {
                    return crate::bv_op_of![$op](e1.eval_bv(args), e2.eval_bv(args));
                }
            };
        }
        if let Expr::Var(a) = self { return args[*a];}
        if let Expr::Const(a) = self { return Bv([*a; N].into()); }
        if let Expr::Ite(a, b, c) = self { return op::bv::ite(a.eval_bv(args), b.eval_bv(args), c.eval_bv(args)); }
        all_ops!(arity);
        panic!("should not reach here. {:?}", self);
    }
    pub fn is_shift(&self) -> bool {
        match self {
            Expr::LShr(_, _) => true,
            Expr::AShr(_, _) => true,
            Expr::Shl(_, _) => true,
            _ => false
        }
    }
    pub fn from_owned(bump: &'a Bump, expr: &OwnedExpr) -> &'a Expr<'a> {
        macro_rules! foreach_op {
            (1, $op:ident) => {
                if let OwnedExpr::$op(a) = expr { return bump.alloc(Expr::$op(Self::from_owned(bump, a)))}
            };
            (2, $op:ident) => {
                if let OwnedExpr::$op(a, b) = expr { return bump.alloc(Expr::$op(Self::from_owned(bump, a), Self::from_owned(bump, b))); }
            };
        }
        if let OwnedExpr::Var(i) = expr { return bump.alloc(Expr::Var(*i)) }
        if let OwnedExpr::Const(c) = expr { return bump.alloc(Expr::Const(*c)) }
        if let OwnedExpr::Ite(a, b, c) = expr { return bump.alloc(Expr::Ite(Self::from_owned(bump, a), Self::from_owned(bump, b), Self::from_owned(bump, c))) }
        all_ops!(arity);
        panic!();
    }
    pub fn for_each_subexpr<const N: usize>(&'a self, f: &mut impl FnMut(&'a Expr, Bv<N>) -> Result<(), ()>, args: &[Bv<N>]) -> Result<Option<Bv<N>>, ()>  {
        macro_rules! foreach_op {
            (1, false, $op: ident) => {
                if let Expr::$op(e1) = self {
                    let v1 = e1.for_each_subexpr(f, args)?;
                    let v1 = if let Some(v) = v1 { v } else { return Ok(None)};
                    f(e1, v1)?;
                    return Ok(Some(bv_op_of![$op](v1)))
                }
            };
            (2, false, $op:ident) => {{
                if let Expr::$op(e1, e2) = self {
                    let v1 = e1.for_each_subexpr(f, args)?;
                    let v1 = if let Some(v) = v1 { v } else { return Ok(None)};
                    f(e1, v1)?;
                    let v2 = e2.for_each_subexpr(f, args)?;
                    let v2 = if let Some(v) = v2 { v } else { return Ok(None)};
                    f(e2, v2)?;
                    let v = bv_op_of![$op](v1, v2);
                    return Ok(Some(v));
                }
            }};
            (2, true, $op:ident) => {{
                if let Expr::$op(e1, e2) = self {
                    let v1 = e1.for_each_subexpr(f, args)?;
                    let v1 = if let Some(v) = v1 { v } else { return Ok(None)};
                    f(e1, v1)?;
                    let v2 = e2.for_each_subexpr(f, args)?;
                    let v2 = if let Some(v) = v2 { v } else { return Ok(None)};
                    f(e2, v2)?;
                    if v2.any_eq(&Bv::ZEROS) { return Ok(None)}
                    let v = bv_op_of![$op](v1, v2);
                    return Ok(Some(v));
                }
            }};
        }
        if let Expr::Var(i) = self {
            return Ok(Some(args[*i].clone()));
        }
        if let Expr::Const(c) = self { return Ok(Some(Bv([*c; N].into()))) }
        all_ops!(arity nonzero);
        return Ok(None)
    }
}

enum_ownedexpr!();

impl OwnedExpr {
    pub fn equal(&self, expr: &Expr) -> bool {
        self == &expr.into()
    }
    pub fn from_sexpr<'a>(sexpr: &SExpr<'a>, env: &[&'a str]) -> parse::Result<Self> {
        if let SExpr::Const(c, span) = sexpr {
            let a = literals::u64_constant(c, span.clone())?;
            return Ok(Self::Const(a));
        } else if let SExpr::Id(v, span) = sexpr {
            if let Some((i, _)) = env.iter().enumerate().find(|(i, x)| x == &v) {
                return Ok(Self::Var(i));
            } else { return Err(new_custom_error_span("Unrecongnized Variable".into(), sexpr.span().clone())); }
        }
        
        macro_rules! operator {
            ($bvn:pat, $n:ident, $($a:ident),* ) => {
                if let Some(($bvn, [$( $a ),*])) = sexpr.call() {
                    return Ok(Self::$n( $(Self::from_sexpr($a, env)?.into()),* ))
                }
            };
        }
        macro_rules! foreach_op {
            (1, $op:ident) => { operator!(smt_name_of![$op], $op, a1); };
            (2, $op:ident) => { operator!(smt_name_of![$op], $op, a1, a2); };
        }
        
        all_ops!(arity);
        operator!("=", Xor, a1, a2);
        operator!("ite", Ite, a1, a2, a3);
        return Err(new_custom_error_span("Unrecongnized Expression".into(), sexpr.span().clone()))
    }
    
    pub fn for_each_subexpr(&self, f: &mut impl FnMut(&OwnedExpr) -> ()) -> ()  {
        f(self);
        macro_rules! foreach_op {
            (1, $op:ident) => {
                if let OwnedExpr::$op(e1) = self {
                    e1.for_each_subexpr(f);
                    return;
                }
            };
            (2, $op:ident) => {{
                if let OwnedExpr::$op(e1, e2) = self {
                    e1.for_each_subexpr(f);
                    e2.for_each_subexpr(f);
                    return;
                }
            }};
        }
        if let OwnedExpr::Var(i) = self { return () }
        if let OwnedExpr::Const(c) = self { return () }
        if let OwnedExpr::Ite(a, b, c) = self { return () }
        all_ops!(arity);
        panic!();
    }
    pub fn size(&self) -> usize  {
        macro_rules! foreach_op {
            (1, $op:ident) => {
                if let OwnedExpr::$op(a) = self { return 1 + a.size(); }
            };
            (2, $op:ident) => {
                if let OwnedExpr::$op(a, b) = self { return 1 + a.size() + b.size(); }
            };
        }
        if let OwnedExpr::Var(_) = self { return 1; }
        if let OwnedExpr::Const(_) = self { return 1; }
        if let OwnedExpr::Ite(a, b, c) = self { return 1 + a.size() + b.size() + c.size() }
        all_ops!(arity);
        panic!();
    }
}

impl<'a> From<&Expr<'a>> for OwnedExpr {
    fn from(value: &Expr<'a>) -> Self {
        if let Expr::Var(i) = value { return OwnedExpr::Var(*i); }
        if let Expr::Const(c) = value { return OwnedExpr::Const(*c); }
        if let Expr::Ite(e1, e2, e3) = value { return OwnedExpr::Ite(Box::new((*e1).into()), Box::new((*e2).into()), Box::new((*e3).into()),); }
        macro_rules! foreach_op {
            (1, $op:ident) => {
                if let Expr::$op(e1) = value { return OwnedExpr::$op(Box::new((*e1).into())); }
            };
            (2, $op:ident) => {
                if let Expr::$op(e1, e2) = value { return OwnedExpr::$op(Box::new((*e1).into()), Box::new((*e2).into())); }
            }
        }
        all_ops!(arity);
        panic!("shoud not reach here");
    }
}


#[macro_export]
macro_rules! oexpr {
    ($v:literal) => { OwnedExpr::Var($v as usize) };
    ([$v:expr]) => { OwnedExpr::Const($v as u64) };
    ($v:ident) => { $v.clone() };
    (! $a1:tt) => { OwnedExpr::Not(Box::new(oexpr![$a1])) };
    (~ $a1:tt) => { OwnedExpr::Neg(Box::new(oexpr![$a1])) };
    (+ $a1:tt $a2:tt) => { OwnedExpr::Add(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (^ $a1:tt $a2:tt) => { OwnedExpr::Xor(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (& $a1:tt $a2:tt) => { OwnedExpr::And(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (| $a1:tt $a2:tt) => { OwnedExpr::Or(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (- $a1:tt $a2:tt) => { OwnedExpr::Sub(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (* $a1:tt $a2:tt) => { OwnedExpr::Mul(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (/ $a1:tt $a2:tt) => { OwnedExpr::UDiv(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (>> $a1:tt $a2:tt) => { OwnedExpr::LShr(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (<< $a1:tt $a2:tt) => { OwnedExpr::Shl(Box::new(oexpr![$a1]), Box::new(oexpr![$a2])) };
    (ite $a1:tt $a2:tt $a3:tt) => { OwnedExpr::Ite(Box::new(oexpr![$a1]), Box::new(oexpr![$a2]), Box::new(oexpr![$a3])) };
    ( ($( $inner:tt )*) ) => { oexpr!($($inner)*) };
}

#[cfg(test)]
mod tests {
    #[test]
    fn test1() {
        use super::OwnedExpr;
        println!("{:?}", oexpr!{+ [1] [2]} );
    }
}


impl OwnedExpr {
    pub fn eval(&self, args: &[u64]) -> Result<u64, DivideByZero> {
        macro_rules! foreach_op {
            (1, false, $op: ident) => {
                if let OwnedExpr::$op(e1) = self {
                    return Ok(crate::op_of![$op](e1.eval(args)?));
                }
            };
            (2, false, $op: ident) => {
                if let OwnedExpr::$op(e1, e2) = self {
                    return Ok(crate::op_of![$op](e1.eval(args)?, e2.eval(args)?));
                }
            };
            (2, true, $op: ident) => {
                if let OwnedExpr::$op(e1, e2) = self {
                    return crate::op_of![$op](e1.eval(args)?, e2.eval(args)?);
                }
            };
        }
        if let OwnedExpr::Var(a) = self { return Ok(args[*a]);}
        if let OwnedExpr::Const(a) = self { return Ok(*a);}
        if let OwnedExpr::Ite(e1, e2, e3) = self { return Ok(op::ite(e1.eval(args)?, e2.eval(args)?, e3.eval(args)?));}
        all_ops!(arity nonzero);
        panic!("should not reach here.");
    }
    pub fn eval_bv<const N: usize>(&self, args: &[Bv<N>]) -> Bv<N> {
        macro_rules! foreach_op {
            (1, $op: ident) => {
                if let OwnedExpr::$op(e1) = self {
                    return crate::bv_op_of![$op](e1.eval_bv(args));
                }
            };
            (2, $op: ident) => {
                if let OwnedExpr::$op(e1, e2) = self {
                    return crate::bv_op_of![$op](e1.eval_bv(args), e2.eval_bv(args));
                }
            };
        }
        if let OwnedExpr::Var(a) = self { return args[*a];}
        if let OwnedExpr::Const(a) = self { return Bv([*a; N].into()); }
        if let OwnedExpr::Ite(e1, e2, e3) = self { return op::bv::ite(e1.eval_bv(args), e2.eval_bv(args), e3.eval_bv(args)); }
        all_ops!(arity);
        panic!("Should not reach here.")
    }
}