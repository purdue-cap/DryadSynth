

use std::cmp::{max, min};


use crate::{bv::{Hint, Context, Bv, Indices}, algo::{Algo, State}};

use super::{Rule, BvRule, NotRule, OrRule, AndRule};
#[derive(Clone)]
pub struct RootRule<'a> {
    hint: Hint<'a>
}

impl<'a> RootRule<'a> {
    pub fn new(hint: Hint<'a>) -> Self {
        Self { hint}
    }
}

impl<'a> Rule<'a> for RootRule<'a> {
    fn is_deductive(&self) -> bool { true }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self, ctx: &Context<'a>, value: &Bv<'a>) -> Bv<'a> { value.clone() }

    fn build_expr(&self,left:Option<crate::bv::expr::BvExpr>,right:crate::bv::expr::BvExpr) -> crate::bv::expr::BvExpr {
        panic!("RootRule should be handled differently.")
    }
    
    fn parent(&self) ->  &'a BvRule<'a> { panic!("No parent for RootRule.") }

}

#[derive(Clone)]
pub struct AcceptRule<'a> {
    pub(crate) parent: &'a BvRule<'a>,
    pub(crate) hint: Hint<'a>,
    pub(crate) val: i16,
    pub(crate) subtree_parent: &'a BvRule<'a>,
    pub(crate) deductive: bool,
    pub(crate) eval: Bv<'a>,
}

impl<'a> AcceptRule<'a> {
    pub fn derive_from<'b>(algo: &mut Algo<'a, 'b>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>, must_deductive: bool) {
        let mut weight = 0.0;
        for (i, t, m, x) in hint.zip(**eval) {
            let lz = u64::BITS - (t & m).leading_zeros();
            let dlz = u64::BITS - ((t ^ x) & m).leading_zeros();
            if lz == 0 {
                if dlz == 0 { weight = 1.0; }
            } else if 1.0 - (dlz as f32 / lz as f32) > 0.7 {
                weight = f32::max(weight, 1.0 - (dlz as f32 / lz as f32));
            }
        }
        let (finished, rule) = AcceptRule::new(algo.ctx, state.node, val, weight > 0.999, eval);
        if !finished && (!must_deductive || weight > 0.999){
            algo.push(state.derive(algo.ctx.alloc(rule), weight, false, val == 0));
        } else if weight > 0.999 {
            algo.add_solution(state.derive(algo.ctx.alloc(rule), weight, false, val == 0));
        }
    }
    pub fn derive_from_shr<'b>(algo: &mut Algo<'a, 'b>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        let mut weight = 0.0;
        for (i, t, m, x) in hint.zip(**eval) {
            let tvalid = (t & m).count_ones();
            if tvalid == 0 {
                if (t ^ x) & m == 0 { weight = 1.0 }
            } else if (t & !x & m).count_ones() < (!t & x & m).count_ones() {
                let upvalid = (t & !x & m).count_ones();
                let mut percentage = (!x & m).count_ones() as f32 / m.count_ones() as f32;
                let w = (1.0 - (2 * upvalid) as f32 / tvalid as f32) * f32::min(percentage * 2.0, 1.0);
                if w > 0.5 { weight = f32::max(weight, w); }
            } else {
                let upvalid = (!t & x & m).count_ones();
                let percentage = (x & m).count_ones() as f32 / m.count_ones() as f32;
                let w = (1.0 - (2 * upvalid) as f32 / tvalid as f32) * f32::min(percentage * 2.0, 1.0);
                if w > 0.5 { weight = f32::max(weight, w); }
            }
            
        }
        let (finished, rule) = AcceptRule::new(algo.ctx, state.node, val, weight > 0.999, eval);
        if !finished {
            algo.push(state.derive(algo.ctx.alloc(rule), weight, false, val == 0));
        } else if weight > 0.999 {
            algo.add_solution(state.derive(algo.ctx.alloc(rule), weight, false, val == 0));
        }
    }
    
    pub fn new(ctx: &Context<'a>, l: &'a BvRule<'a>, val: i16, deductive: bool, eval: &Bv<'a>) -> (bool, BvRule<'a>) {
        // if let BvRule::RootRule(_) = l {
            // Self{ parent: l,  val, subtree_parent: l, deductive, eval: eval.clone(), hint: l.hint().with_indices(eval.indices)}.into()
        let mut subtree_parent = l;
        let mut value = eval.clone();
        loop {
            match &subtree_parent {
                BvRule::AcceptRule(AcceptRule { subtree_parent: subtree, .. }) => {
                    if let BvRule::RootRule(_) = subtree  { break}  else { subtree_parent = subtree.parent(); }
                }
                BvRule::RootRule(_) => break,
                node if node.is_deductive() => {
                    value = subtree_parent.evaluate(ctx, &value);
                    subtree_parent = subtree_parent.parent();
                }
                _ => break,
            }
        }
        if let BvRule::RootRule(_) = subtree_parent {
            (true, Self { parent: l, val, subtree_parent, deductive, eval: value, hint: subtree_parent.hint().clone()}.into())
        } else {
            (false, Self { parent: l, val, subtree_parent, deductive, eval: subtree_parent.evaluate(ctx, &value), hint: subtree_parent.parent().hint().clone()}.into())
        }
    }
}

impl<'a> std::fmt::Debug for RootRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "Root")
    }
}
impl<'a> std::fmt::Debug for AcceptRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "= {}", self.val)
    }
}

impl<'a> Rule<'a> for AcceptRule<'a> {
    fn parent(&self) -> &'a BvRule<'a> { self.parent }
    fn is_deductive(&self) -> bool { self.deductive }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self, ctx: &Context<'a>, value: &Bv<'a>) -> Bv<'a> { self.eval.clone() }

    fn build_expr(&self,left:Option<crate::bv::expr::BvExpr>,right:crate::bv::expr::BvExpr) -> crate::bv::expr::BvExpr {
        panic!("AcceptRule should be handled differently.")
    }
}

