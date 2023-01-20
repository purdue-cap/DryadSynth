use std::{ops::Not, cmp::{max, min}};

use crate::{bv::{Hint, Bv, expr::BvExpr, Context}, algo::{Algo, State}};

use super::{Rule, BvRule};




#[derive(Clone)]
pub struct ShlRule<'a>{
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>, 
    pub shift: u16,
}

impl<'a> Rule<'a> for ShlRule<'a> {
    fn is_deductive(&self) -> bool { false }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>, value: & Bv<'a>) -> crate::bv::Bv<'a> { 
        value.map(ctx, |u| u << self.shift)
    }

    fn build_expr(&self, _ : Option<crate::bv::expr::BvExpr>,right:crate::bv::expr::BvExpr) -> crate::bv::expr::BvExpr {
        BvExpr::Shl(Box::new(right), self.shift)
    }
    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> std::fmt::Debug for ShlRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "<< {}", self.shift)
    }
}

impl<'a> ShrRule<'a> {
    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.shr { return; }
        let last_shift = state.node.is_shr().unwrap_or(64);
        let mut shift1 = Vec::new();
        let mut shift4 = Vec::new();
        let mut shift16 = Vec::new();
        let mut deductive4 = false;
        let mut deductive16 = false;
        
        for (i, t, m, x) in hint.zip(**eval) {
            let tlz = (t & m).leading_zeros();
            let max_shift = tlz.saturating_sub(x.leading_zeros());
            let min_shift = (t | !m).leading_zeros().saturating_sub(x.leading_zeros());
            if tlz >= 15 {
                if min_shift >= 16 { deductive16 = true; }
                shift16.push(i);
            }
            if tlz >= 3 && min_shift < 16 {
                if min_shift >= 4 { deductive4 = true; }
                shift4.push(i);
            }
            if tlz >= 1 && min_shift < 4 { shift1.push(i); }
        };
    
        
        if shift16.len() > 0 && 16 <= last_shift {
            let (value, mask) = algo.ctx.collect2(shift16.iter().map(|&i| (i, hint.value[i] << 16, hint.mask[i] << 16)));
            let indices = algo.ctx.alloc_indices(shift16);
            let weight = 0.8 * (indices.len() as f32 / hint.indices.len() as f32);
            let hint = Hint::new(indices, value, mask);
            let rule = ShrRule { parent: state.node, hint, shift: 16 };
            algo.push(state.derive(algo.ctx.alloc(rule.into()), if deductive16 { 1.0 } else { weight }, false, false))
        }
        if shift4.len() > 0 && 4 <= last_shift {
            let (value, mask) = algo.ctx.collect2(shift4.iter().map(|&i| (i, hint.value[i] << 4, hint.mask[i] << 4)));
            let indices = algo.ctx.alloc_indices(shift4);
            let weight = 0.8 * (indices.len() as f32 / hint.indices.len() as f32);
            let hint = Hint::new(indices, value, mask);
            let rule = ShrRule { parent: state.node, hint, shift: 4 };
            algo.push(state.derive(algo.ctx.alloc(rule.into()), if deductive4 { 1.0 } else { weight }, false, false))
        }
        if shift1.len() > 0 && 1 <= last_shift {
            let (value, mask) = algo.ctx.collect2(shift1.iter().map(|&i| (i, hint.value[i] << 1, hint.mask[i] << 1)));
            let indices = algo.ctx.alloc_indices(shift1);
            let weight = 0.8 * (indices.len() as f32 / hint.indices.len() as f32) ;
            let hint = Hint::new(indices, value, mask);
            let rule = ShrRule { parent: state.node, hint, shift: 1 };
            algo.push(state.derive(algo.ctx.alloc(rule.into()), weight, false, false))
        }
    }
}
#[derive(Clone)]
pub struct ShrRule<'a>{
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>, 
    pub shift: u16,
}

impl<'a> Rule<'a> for ShrRule<'a> {
    fn is_deductive(&self) -> bool { false }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>, value: & Bv<'a>) -> crate::bv::Bv<'a> { 
        value.map(ctx, |u| u >> self.shift)
    }

    fn build_expr(&self, _ : Option<crate::bv::expr::BvExpr>,right:crate::bv::expr::BvExpr) -> crate::bv::expr::BvExpr {
        BvExpr::Shr(Box::new(right), self.shift)
    }
    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> ShlRule<'a> {
    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.shl { return; }
        
        let (indices, value, mask) = algo.ctx.collect_with_indices2(hint.zip(**eval).flat_map(|(i, t, m, x)| {
            let max_shift = (t & m).trailing_zeros().saturating_sub(x.trailing_zeros());
            if max_shift >= 1 {
                Some((i, t >> 1, m >> 1))
            } else { None }
        }));
        
        if indices.len() > 0 {
            let hint = Hint::new(indices, value, mask);
            let rule = ShlRule { parent: state.node, hint, shift: 1 };
            algo.push(state.derive(algo.ctx.alloc(rule.into()), 0.0, false, false))
        }
    }
}

impl<'a> std::fmt::Debug for ShrRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, ">> {}", self.shift)
    }
}

#[derive(Clone)]
pub struct NotRule<'a> {
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>,
    pub deductive: bool,
}

impl<'a> NotRule<'a> {
    pub fn new_deductive(ctx : & Context<'a>, parent: &'a BvRule<'a>) -> NotRule<'a> {
        Self {
            parent,
            hint: ctx.no_hint(),
            deductive: true,
        }
    }
}

impl<'a> Rule<'a> for NotRule<'a> {
    fn is_deductive(&self) -> bool { self.deductive }

    fn hint(&self) ->  &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>,value: &Bv<'a>) -> Bv<'a> {
        ctx.alloc_bv(value.iter().map(|v1| !v1))
    }

    fn build_expr(&self, left:Option<BvExpr>,right:BvExpr) -> BvExpr {
        BvExpr::Not(Box::new(right))
    }
    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> std::fmt::Debug for NotRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "! ?")
    }
}


impl<'a> NotRule<'a> {
    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.not { return; }
        let mut max_1_bits = 0;
        let value = algo.ctx.collect(hint.zip(**eval).flat_map(|(i, t, m, x)| {
            let bits = unsafe { (t | !m).leading_ones().unchecked_sub(m.leading_zeros()) };
            max_1_bits = u32::max(bits, max_1_bits);
            Some((i, !t))
        }));
        
        let hint = Hint::new(hint.indices, value, *hint.mask);
        let rule = NotRule { parent: state.node, hint, deductive: true };
        algo.push(state.derive(algo.ctx.alloc(rule.into()), if max_1_bits >= 4 { 0.7 + 0.3 * min(max_1_bits - 4, 32) as f32 / 32.0 } else { 0.0 }, false, false))
    }
}