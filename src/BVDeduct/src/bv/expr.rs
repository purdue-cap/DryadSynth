use crate::parsing::sexpr::SExpr;

use super::Context;

#[derive(Clone)]
pub enum BvExpr {
    Symbol(i16),
    Not(Box<BvExpr>),
    Add(Box<BvExpr>, Box<BvExpr>),
    Xor(Box<BvExpr>, Box<BvExpr>),
    And(Box<BvExpr>, Box<BvExpr>),
    Or(Box<BvExpr>, Box<BvExpr>),
    Shl(Box<BvExpr>, u16),
    Shr(Box<BvExpr>, u16),
    Mul(Box<BvExpr>, Box<BvExpr>),
    Div(Box<BvExpr>, Box<BvExpr>),
    Ite(Box<BvExpr>, Box<BvExpr>, Box<BvExpr>),
}

impl std::fmt::Debug for BvExpr {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            Self::Symbol(0) => write!(f, "?"),
            Self::Symbol(arg0) => write!(f, "{}", arg0),
            Self::Not(arg0) => write!(f, "!{:?}", arg0),
            Self::Add(arg0, arg1) => write!(f, "(+ {:?} {:?})", arg0, arg1),
            Self::Xor(arg0, arg1) => write!(f, "(^ {:?} {:?})", arg0, arg1),
            Self::And(arg0, arg1) => write!(f, "(& {:?} {:?})", arg0, arg1),
            Self::Or(arg0, arg1) => write!(f, "(| {:?} {:?})", arg0, arg1),
            Self::Mul(arg0, arg1) => write!(f, "(* {:?} {:?})", arg0, arg1),
            Self::Div(arg0, arg1) => write!(f, "(/ {:?} {:?})", arg0, arg1),
            Self::Shl(arg0, arg1) => write!(f, "(<<{} {:?})", arg1, arg0),
            Self::Shr(arg0, arg1) => write!(f, "(>>{} {:?})", arg1, arg0),
            Self::Ite(arg0, arg1, arg2) => write!(f, "(ite {:?} {:?} {:?})", arg0, arg1, arg2),
        }
    }
}

impl BvExpr {
    pub fn eval(&self, var: &Vec<u64>, consts: &Vec<u64>) -> u64 {
        match self {
            Self::Symbol(arg0) if *arg0 > 0 => var[(arg0 - 1) as usize],
            Self::Symbol(arg0) if *arg0 < 0 => consts[(- arg0 - 1) as usize],
            Self::Symbol(_) => panic!("unfinished expression"),
            Self::Not(arg0) => !arg0.eval(var, consts),
            Self::Add(arg0, arg1) => unsafe {arg0.eval(var, consts).unchecked_add(arg1.eval(var, consts)) },
            Self::Xor(arg0, arg1) => arg0.eval(var, consts) ^ arg1.eval(var, consts),
            Self::And(arg0, arg1) => arg0.eval(var, consts) & arg1.eval(var, consts),
            Self::Or(arg0, arg1) => arg0.eval(var, consts) | arg1.eval(var, consts),
            Self::Mul(arg0, arg1) => unsafe {arg0.eval(var, consts).unchecked_mul(arg1.eval(var, consts))},
            Self::Div(arg0, arg1) => arg0.eval(var, consts).div_floor(arg1.eval(var, consts)),
            Self::Shl(arg0, arg1) => arg0.eval(var, consts) << arg1,
            Self::Shr(arg0, arg1) => arg0.eval(var, consts) >> arg1,
            Self::Ite(arg0, arg1, arg2) => if arg0.eval(var, consts) == 1 { arg1.eval(var, consts) } else { arg2.eval(var, consts) },
        }
    }
    pub fn to_sexpr(&self, var: &[&str], consts: &[u64]) -> SExpr {
        match self {
            Self::Symbol(arg0) if *arg0 > 0 => SExpr::Symbol(var[(arg0 - 1) as usize].into()),
            Self::Symbol(arg0) if *arg0 < 0 => SExpr::Constant(consts[(- arg0 - 1) as usize]),
            Self::Symbol(_) => panic!("unfinished expression"),
            Self::Not(arg0) => SExpr::List(vec![SExpr::Symbol("bvnot".into()), arg0.to_sexpr(var, consts)]),
            Self::Add(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvadd".into()), arg0.to_sexpr(var, consts), arg1.to_sexpr(var, consts)]),
            Self::Xor(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvxor".into()), arg0.to_sexpr(var, consts), arg1.to_sexpr(var, consts)]),
            Self::And(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvand".into()), arg0.to_sexpr(var, consts), arg1.to_sexpr(var, consts)]),
            Self::Mul(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvmul".into()), arg0.to_sexpr(var, consts), arg1.to_sexpr(var, consts)]),
            Self::Div(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvudiv".into()), arg0.to_sexpr(var, consts), arg1.to_sexpr(var, consts)]),
            Self::Or(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvor".into()), arg0.to_sexpr(var, consts), arg1.to_sexpr(var, consts)]),
            Self::Shl(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvshl".into()), arg0.to_sexpr(var, consts), SExpr::Constant(*arg1 as u64)]),
            Self::Shr(arg0, arg1) => SExpr::List(vec![SExpr::Symbol("bvlshr".into()), arg0.to_sexpr(var, consts), SExpr::Constant(*arg1 as u64)]),
            Self::Ite(arg0, arg1, arg2) => {
                let cond = SExpr::List(vec![SExpr::Symbol("=".into()), arg0.to_sexpr(var, consts), SExpr::Constant(1)]);
                SExpr::List(vec![SExpr::Symbol("ite".into()), cond, arg1.to_sexpr(var, consts), arg2.to_sexpr(var, consts)])
            }
        }
    }
}