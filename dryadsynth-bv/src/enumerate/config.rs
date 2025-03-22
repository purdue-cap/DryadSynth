


use crate::parse::{self, cfg::NonTerminal, deffun, literals, new_custom_error_span, SExpr, SynthProblem};

use super::expr::OwnedExpr;
use derive_more::{From, Deref};
type NTid = usize;



#[derive(Debug, Clone, PartialEq, Eq)]
pub enum Rule<const N: usize>  {
    Var(usize),
    Const(u64),
    Fop1(usize),
    Fop2(usize),
    ChatGPT(OwnedExpr, usize),
    LShrC(u64),
    AShrC(u64),
    ShlC(u64),
    Not, Neg, Add, Xor, And, Or, Sub, Mul, UDiv, LShr, Shl, SDiv, URem, SRem, AShr, Ite
}

impl<const N: usize> Rule<N> {
    pub fn is_atom(&self) -> bool {
        match self {
            Rule::Var(_) | Rule::Const(_) => true,
            _ => false,
        }
    }
    pub fn is_shift(&self) -> bool {
        match self {
            Rule::Shl | Rule::AShr | Rule::LShr => true,
            _ => false,
        }
    }
}

#[macro_export]
macro_rules! generate_rules_matching {
    ($rules:expr) => {
        match $rules {
            Rule::Not => {
                generate_op!(op1, |e1| Expr::Not(e1), $crate::bv_op_of!(Not));
            }
            Rule::Neg => {
                generate_op!(op1, |e1| Expr::Neg(e1), $crate::bv_op_of!(Neg));
            }
            Rule::LShrC(v) => {
                generate_op!(op1_constant, |e1, e2| Expr::LShr(e1, e2), $crate::bv_op_of!(LShr), *v);
            }
            Rule::AShrC(v) => {
                generate_op!(op1_constant, |e1, e2| Expr::AShr(e1, e2), $crate::bv_op_of!(AShr), *v);
            }
            Rule::ChatGPT(e, v) => {
                generate_op!(chatgpt, e, v);
            }
            Rule::ShlC(v) => {
                generate_op!(op1_constant, |e1, e2| Expr::Shl(e1, e2), $crate::bv_op_of!(Shl), *v);
            }
            Rule::Add => {
                generate_op!(op2_sym, |e1, e2| Expr::Add(e1, e2), $crate::bv_op_of!(Add));
            }
            Rule::Xor => {
                generate_op!(op2_sym, |e1, e2| Expr::Xor(e1, e2), $crate::bv_op_of!(Xor));
            }
            Rule::And => {
                generate_op!(op2_sym, |e1, e2| Expr::And(e1, e2), $crate::bv_op_of!(And));
            }
            Rule::Or => {
                generate_op!(op2_sym, |e1, e2| Expr::Or(e1, e2), $crate::bv_op_of!(Or));
            }
            Rule::Mul => {
                generate_op!(op2_sym, |e1, e2| Expr::Mul(e1, e2), $crate::bv_op_of!(Mul));
            }
            Rule::Sub => {
                generate_op!(op2, |e1, e2| Expr::Sub(e1, e2), $crate::bv_op_of!(Sub));
            }
            Rule::UDiv => {
                generate_op!(op2_nonzero, |e1, e2| Expr::UDiv(e1, e2), $crate::bv_op_of!(UDiv));
            }
            Rule::LShr => {
                generate_op!(op2, |e1, e2| Expr::LShr(e1, e2), $crate::bv_op_of!(LShr));
            }
            Rule::Shl => {
                generate_op!(op2, |e1, e2| Expr::Shl(e1, e2), $crate::bv_op_of!(Shl));
            }
            Rule::SDiv => {
                generate_op!(op2_nonzero, |e1, e2| Expr::SDiv(e1, e2), $crate::bv_op_of!(SDiv));
            }
            Rule::URem => {
                generate_op!(op2_nonzero, |e1, e2| Expr::URem(e1, e2), $crate::bv_op_of!(URem));
            }
            Rule::SRem => {
                generate_op!(op2_nonzero, |e1, e2| Expr::SRem(e1, e2), $crate::bv_op_of!(SRem));
            }
            Rule::AShr => {
                generate_op!(op2, |e1, e2| Expr::AShr(e1, e2), $crate::bv_op_of!(AShr));
            }
            Rule::Var(id) => {
                generate_op!(var, id);
            }
            Rule::Fop1(id) => {
                match id {
                    0 => { generate_op!(op1, |e1| Expr::Fop1_0(e1), $crate::bv_op_of!(Fop1_0)); }
                    1 => { generate_op!(op1, |e1| Expr::Fop1_1(e1), $crate::bv_op_of!(Fop1_1)); }
                    2 => { generate_op!(op1, |e1| Expr::Fop1_2(e1), $crate::bv_op_of!(Fop1_2)); }
                    3 => { generate_op!(op1, |e1| Expr::Fop1_3(e1), $crate::bv_op_of!(Fop1_3)); }
                    4 => { generate_op!(op1, |e1| Expr::Fop1_4(e1), $crate::bv_op_of!(Fop1_4)); }
                    5 => { generate_op!(op1, |e1| Expr::Fop1_5(e1), $crate::bv_op_of!(Fop1_5)); }
                    6 => { generate_op!(op1, |e1| Expr::Fop1_6(e1), $crate::bv_op_of!(Fop1_6)); }
                    7 => { generate_op!(op1, |e1| Expr::Fop1_7(e1), $crate::bv_op_of!(Fop1_7)); }
                    _ => panic!(),
                }
            }
            Rule::Fop2(id) => {
                match id {
                    0 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_0(e1, e2), $crate::bv_op_of!(Fop2_0)); }
                    1 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_1(e1, e2), $crate::bv_op_of!(Fop2_1)); }
                    2 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_2(e1, e2), $crate::bv_op_of!(Fop2_2)); }
                    3 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_3(e1, e2), $crate::bv_op_of!(Fop2_3)); }
                    4 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_4(e1, e2), $crate::bv_op_of!(Fop2_4)); }
                    5 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_5(e1, e2), $crate::bv_op_of!(Fop2_5)); }
                    6 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_6(e1, e2), $crate::bv_op_of!(Fop2_6)); }
                    7 => { generate_op!(op2_nonzero, |e1, e2| Expr::Fop2_7(e1, e2), $crate::bv_op_of!(Fop2_7)); }
                    _ => panic!(),
                }
            }
            Rule::Const(value) => {
                generate_op!(const, value);
            }
            Rule::Ite => {
                generate_op!(ite);
            }
        }
    };
}


#[derive(From, Debug, Deref, Clone)]
pub struct Config<const N: usize>(pub Vec<Rule<N>>) ;

impl<const N : usize> Config<N>  {
    pub fn from_problem(problem: &SynthProblem<'_>, partial_solution: bool) -> parse::Result<Self> {
        if let Some(NonTerminal(name, rules)) = problem.cfg.first() {
            let mut res = Vec::<Rule<N>>::new();
            for e in problem.chatgpt_exprs.iter() {
                res.push(Rule::ChatGPT(e.clone(), 1))
            }
            for r in rules {
                if let SExpr::Id(v, _) = r {
                    let id = problem.arg(v).ok_or(new_custom_error_span("Enumeration: Unknowned variable.".into(), r.span().clone()))?;
                    res.push(Rule::Var(id));
                } else if let SExpr::Const(v, span) = r {
                    let v = literals::u64_constant(v, span.clone())?;
                    res.push(Rule::Const(v));
                } else if let Some(("bvnot", [SExpr::Id(nt1, sp1)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    res.push(Rule::Not);
                } else if let Some(("bvneg", [SExpr::Id(nt1, sp1)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    res.push(Rule::Neg);
                } else if let Some(("bvlshr", [SExpr::Id(nt1, sp1), SExpr::Const(v, span)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    let v = literals::u64_constant(v, span.clone())?;
                } else if let Some(("ehad", [SExpr::Id(nt1, sp1)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    res.push(Rule::LShrC(1));
                } else if let Some(("arba", [SExpr::Id(nt1, sp1)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    res.push(Rule::LShrC(4));
                } else if let Some(("shesh", [SExpr::Id(nt1, sp1)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    res.push(Rule::LShrC(16));
                } else if let Some(("bvshl", [SExpr::Id(nt1, sp1), SExpr::Const(v, span)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    let v = literals::u64_constant(v, span.clone())?;
                    res.push(Rule::ShlC(v));
                } else if let Some(("smol", [SExpr::Id(nt1, sp1), SExpr::Const(v, span)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    res.push(Rule::ShlC(1));
                } else if let Some(("bvadd", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::Add);
                } else if let Some(("bvxor", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::Xor);
                } else if let Some(("bvand", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::And);
                } else if let Some(("bvor", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::Or);
                } else if let Some(("bvsub", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::Sub);
                } else if let Some(("bvmul", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::Mul);
                } else if let Some(("bvudiv", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::UDiv);
                } else if let Some(("bvsdiv", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::SDiv);
                } else if let Some(("bvurem", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::URem);
                } else if let Some(("bvsrem", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::SRem);
                } else if let Some(("bvashr", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::AShr);
                } else if let Some(("bvlshr", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::LShr);
                } else if let Some(("bvshl", [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    res.push(Rule::Shl);
                } else if let Some((fop1, [SExpr::Id(nt1, sp1)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if let Some(a) = deffun::ENV.lookup(fop1, 1) {
                        res.push(Rule::Fop1(a));
                    } else {
                        return Err(new_custom_error_span("Unknown Symbol.".into(), sp1.clone()))
                    }
                } else if let Some((fop2, [SExpr::Id(nt1, sp1), SExpr::Id(nt2, sp2)])) = r.call() {
                    if nt1 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp1.clone())) }
                    if nt2 != name { return Err(new_custom_error_span("Enumeration: Only Start Non-Terminal is accepted.".into(), sp2.clone())) }
                    if let Some(a) = deffun::ENV.lookup(fop2, 2) {
                        res.push(Rule::Fop2(a));
                    } else {
                        return Err(new_custom_error_span("Unknown Symbol.".into(), sp1.clone()))
                    }
                } else if let Some(("ite", _)) = r.call() {
                    res.push(Rule::Ite);
                }
            }
            Ok(res.into())
        } else { Err(new_custom_error_span("Enumeration: Need a Start Non-Terminal.".into(), problem.span.clone())) }
    }
        
}
