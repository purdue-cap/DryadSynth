use enum_dispatch::enum_dispatch;

use crate::bv::{Bv, Context, Hint};

pub use self::basic::{AcceptRule, RootRule};
pub use self::binary::MulRule;
pub use self::binary::{AddRule, AndRule, OrRule, XorRule};
pub use self::shift::{NotRule, ShlRule, ShrRule};
use crate::bv::expr::BvExpr;

#[enum_dispatch(BvRule)]
pub trait Rule<'a> {
    fn parent(&self) -> &'a BvRule<'a>;
    fn is_deductive(&self) -> bool;
    fn hint(&self) -> &Hint<'a>;
    fn evaluate(&self, ctx: &Context<'a>, value: &Bv<'a>) -> Bv<'a>;
    fn build_expr(&self, left: Option<BvExpr>, right: BvExpr) -> BvExpr;
}

mod basic;
mod binary;
mod shift;

// #[derive(Clone)] pub struct RuleList<'a> { pub stack: Vec<BvRule<'a>>, pub deductive: bool }

pub enum SubProblem<'a> {
    Lower(Hint<'a>),
    Upper(Hint<'a>, Bv<'a>, &'a BvRule<'a>, &'a BvRule<'a>),
}

impl<'a> BvRule<'a> {
    // pub fn new(hint: Hint<'a>) -> Self {
    // Self { stack: vec![BvRule::RootRule(RootRule::new(hint))], deductive: true }
    // }
    pub fn subproblem(&self) -> SubProblem<'a> {
        let last = self;
        match last {
            BvRule::AcceptRule(AcceptRule {
                eval,
                hint,
                subtree_parent,
                ..
            }) => {
                SubProblem::Upper(hint.clone(), eval.clone(), subtree_parent.parent(), subtree_parent)
            }
            rule => SubProblem::Lower(rule.hint().clone()),
        }
    }
    pub fn to_vec(&'a self) -> Vec<&'a BvRule<'a>> {
        let mut rule = self;
        let mut result = Vec::new();
        loop {
            result.push(rule);
            if let BvRule::RootRule(_) = rule {
                result.reverse();
                break result;
            }
            rule = rule.parent();
        }
    }
    pub fn to_expr(&'a self) -> BvExpr {
        Self::to_expr_inner(None, &mut self.to_vec().into_iter())
    }
    fn to_expr_inner<'b>(existing: Option<BvExpr>, iter: &mut impl Iterator<Item = &'b BvRule<'a>>) -> BvExpr
        where 'a: 'b, {
        match iter.next() {
            Some(BvRule::RootRule(_)) => Self::to_expr_inner(None, iter),
            Some(BvRule::AcceptRule(AcceptRule { val, .. })) => {
                if *val == 0 {
                    existing.unwrap_or(BvExpr::Symbol(0))
                } else {
                    BvExpr::Symbol(*val)
                }
            }
            Some(rule) => {
                if rule.is_deductive() {
                    rule.build_expr(existing, Self::to_expr_inner(None, iter))
                } else {
                    let this = rule.build_expr(existing, Self::to_expr_inner(None, iter));
                    Self::to_expr_inner(Some(this), iter)
                }
            }
            None => existing.unwrap_or(BvExpr::Symbol(0)),
        }
    }
}

impl<'a> std::fmt::LowerExp for BvRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for r in self.parent().to_vec() {
            match r.subproblem() {
                SubProblem::Lower(hint) => {
                    let i = hint.indices.0[0];
                    writeln!(f, "L {r:?} [{}] {:#16x} ~ {:#16x}", i, hint.value[i] & hint.mask[i], hint.value[i] | ! hint.mask[i])?;
                }                    
                SubProblem::Upper(hint, eval, _, _) => {
                    let i = hint.indices.0[0];
                    writeln!(f, "U {r:?} [{}] {:#16x} => {:#16x} ~ {:#16x}", i, eval[i], hint.value[i] & hint.mask[i], hint.value[i] | ! hint.mask[i])?;
                }
            }
        }
        write!(f, "")
    }
}

impl<'a> std::fmt::Display for BvRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "{:?}", self.to_expr())
    }
}

#[derive(Clone)]
#[enum_dispatch]
pub enum BvRule<'a> {
    AcceptRule(AcceptRule<'a>),
    RootRule(RootRule<'a>),
    AndRule(AndRule<'a>),
    OrRule(OrRule<'a>),
    AddRule(AddRule<'a>),
    XorRule(XorRule<'a>),
    ShlRule(ShlRule<'a>),
    MulRule(MulRule<'a>),
    ShrRule(ShrRule<'a>),
    NotRule(NotRule<'a>),
}

impl<'a> std::fmt::Debug for BvRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            BvRule::AcceptRule(inner) => inner.fmt(f),
            BvRule::RootRule(inner) => inner.fmt(f),
            BvRule::AndRule(inner) => inner.fmt(f),
            BvRule::OrRule(inner) => inner.fmt(f),
            BvRule::AddRule(inner) => inner.fmt(f),
            BvRule::XorRule(inner) => inner.fmt(f),
            BvRule::ShlRule(inner) => inner.fmt(f),
            BvRule::ShrRule(inner) => inner.fmt(f),
            BvRule::MulRule(inner) => inner.fmt(f),
            BvRule::NotRule(inner) => inner.fmt(f),
        }
    }
}

impl<'a> BvRule<'a> {
    #[inline(always)]
    pub fn is_shr(&self) -> Option<u16> {
        match self {
            BvRule::ShrRule(r) => Some(r.shift),
            _ => None,
        }
    }
    #[inline(always)]
    pub fn is_not(&self) -> bool {
        match self {
            BvRule::NotRule(_) => true,
            _ => false,
        }
    }
    #[inline(always)]
    pub fn is_and(&self) -> bool {
        match self {
            BvRule::AndRule(_) => true,
            _ => false,
        }
    }
    pub fn is_root(&self) -> bool {
        match self {
            BvRule::RootRule(_) => true,
            _ => false,
        }
    }
}
