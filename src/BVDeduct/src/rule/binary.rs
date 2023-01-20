use std::cmp::max;

use itertools::Itertools;

use crate::{bv::{Bv, Hint, expr::BvExpr, Indices}, algo::{Algo, State}};

use super::{Rule, BvRule};


#[derive(Clone)]
pub struct AddRule<'a>{
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>, 
    pub val: i16, 
    pub left: Bv<'a>
}

impl<'a> AddRule<'a> {
    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.add { return; }
        let mut weight = 0.0;
        let value = algo.ctx.collect(hint.zip(**eval).map(|(i, t, m, x)| {
            let tlz = u64::BITS - (t & m).leading_zeros();
            let first_diff_bit = u64::BITS - ((t ^ x) & m).leading_zeros();
            if first_diff_bit == 0 { return (i, 0); }
            let diff_part = unsafe { 1_u64.unchecked_shl(first_diff_bit).unchecked_sub(1) };
            let x_diff = x & diff_part;
            let t_diff = if (1 << (first_diff_bit - 1)) & x != 0 { (t | m) & diff_part} else { (t & m) & diff_part };
            let sub = unsafe {t_diff.unchecked_sub(x_diff)};
            let sub_lz = u64::BITS - sub.leading_zeros();
            if 1.0 - sub_lz as f32 / tlz as f32 > 0.7 && sub > 0 {
                weight = f32::max(weight, 1.0 - sub_lz as f32 / tlz as f32);
            }
            (i, sub)
        }));

        let hint = Hint::new(hint.indices, value, *algo.ctx.allone());
        let rule = AddRule { parent: state.node, hint, val, left: eval.clone() };
        algo.push(state.derive(algo.ctx.alloc(rule.into()), weight * if state.weight < 0.7 { state.weight } else { 1.0 }, false, false))
    }
}


impl<'a> Rule<'a> for AddRule<'a> {
    fn is_deductive(&self) -> bool { false }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>, value: &Bv<'a>) -> crate::bv::Bv<'a> { 
        ctx.alloc_bv(value.iter().zip(self.left.iter()).map(|(v1, v2)| unsafe { v1.unchecked_add(*v2) }))
    }

    fn build_expr(&self,left:Option<crate::bv::expr::BvExpr>,right:crate::bv::expr::BvExpr) -> crate::bv::expr::BvExpr {
        let l = if self.val == 0 { left.unwrap_or(BvExpr::Symbol(0)) } else { BvExpr::Symbol(self.val) };
        BvExpr::Add(Box::new(l), Box::new(right))
    }
    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> std::fmt::Debug for AddRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "+ {}", self.val)
    }
}
#[derive(Clone)]
pub struct XorRule<'a>{
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>, 
    pub val: i16, 
    pub left: Bv<'a>
}

impl<'a> XorRule<'a> {
    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.xor { return; }
        let mut weight = 0.0;
        let value = algo.ctx.collect(hint.zip(**eval).map(|(i, t, m, x)| {
            let mut t_bits = u64::BITS - (t & m).leading_zeros();
            let mut d_bits = if t_bits == u64::BITS {
                t_bits = u64::BITS - (t & m).leading_ones();
                u64::BITS - ((t ^ x) & m).leading_ones()
            } else { u64::BITS - ((t ^ x) & m).leading_zeros() };

            if d_bits > 0 && (t_bits > d_bits + 3) && (t & !x) & m != 0 && (!t & x) & m != 0 {
                weight = f32::max(weight, 0.5 + 0.5 * (t_bits - d_bits) as f32 / t_bits as f32);
            }
            (i, t ^ x)
        }));
        if weight < 0.7 && state.node.is_not() {
            return;
        }

        let hint = Hint::new(hint.indices, value, *hint.mask);
        let rule = XorRule { parent: state.node, hint, val, left: eval.clone() };
        algo.push(state.derive(algo.ctx.alloc(rule.into()), weight * if state.weight < 0.7 { state.weight } else { 1.0 }, false, false))
    }
}

impl<'a> Rule<'a> for XorRule<'a> {
    fn is_deductive(&self) -> bool { false }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>, value: &Bv<'a>) -> crate::bv::Bv<'a> { 
        ctx.alloc_bv(value.iter().zip(self.left.iter()).map(|(v1, v2)| unsafe { v1 ^ v2 }))
    }

    fn build_expr(&self,left:Option<BvExpr>,right:BvExpr) -> BvExpr {
        let l = if self.val == 0 { left.unwrap_or(BvExpr::Symbol(0)) } else { BvExpr::Symbol(self.val) };
        BvExpr::Xor(Box::new(l), Box::new(right))
    }

    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> std::fmt::Debug for XorRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "^ {}", self.val)
    }
}
#[derive(Clone)]
pub struct AndRule<'a>{
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>, 
    pub val: i16, 
    pub left: Bv<'a>,
    pub deductive: bool
}

impl<'a> Rule<'a> for AndRule<'a> {
    fn is_deductive(&self) -> bool { self.deductive }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>, value: &Bv<'a>) -> crate::bv::Bv<'a> { 
        ctx.alloc_bv(value.iter().zip(self.left.iter()).map(|(v1, v2)| unsafe { v1 & v2 }))
    }

    fn build_expr(&self,left:Option<BvExpr>,right:BvExpr) -> BvExpr {
        let l = if self.val == 0 { left.unwrap_or(BvExpr::Symbol(0)) } else { BvExpr::Symbol(self.val) };
        BvExpr::And(Box::new(l), Box::new(right))
    }
    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> std::fmt::Debug for AndRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "& {}", self.val)
    }
}

impl<'a> AndRule<'a> {
    pub fn and_weight(t: u64, m: u64, x: u64) -> Option<f32> {
        
        let tvalid = (t & m).count_ones();
        let upvalid = (t & !x & m).count_ones();
        if upvalid == 0 && (!t & x & m) != 0 && !x & m != 0 {
            Some(1.0)
        } else if 2 * upvalid < tvalid && (!t & x & m) != 0 && !x & m != 0  {
            let mut percentage = (!x & m).count_ones() as f32 / m.count_ones() as f32;
            let w = (1.0 - (2 * upvalid) as f32 / tvalid as f32) * f32::min(percentage * 2.0, 1.0);
            Some(w)
        } else { None }
    }
    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.and { return; }
        let mut weight = 0.0;
        let (indices, mask) = algo.ctx.collect_with_indices(hint.zip(**eval).flat_map(|(i, t, m, x)| {
            if let Some(w) = Self::and_weight(t, m, x) {
                if w > 0.6 {
                    weight = f32::max(weight, w);
                    Some((i, m & x))
                } else { None }
            } else { None }
            // let tvalid = (t & m).count_ones();
            // let upvalid = (t & !x & m).count_ones();
            // if upvalid == 0 && (!t & x & m) != 0 && !x & m != 0 {
            //     weight = 1.0;
            //     Some((i, m & x))
            // } else if 2 * upvalid < tvalid && (!t & x & m) != 0 && !x & m != 0  {
            //     let w = 1.0 - (2 * upvalid) as f32 / tvalid as f32 * (!x & m).count_ones() as f32 / m.count_ones() as f32;
            //     if w > 0.6 {
            //         weight = f32::max(weight, w);
            //         Some((i, m & x))
            //     } else { None }
            // } else { None }
        }));
        let deductive = weight > 0.9999;
        let hint = Hint::new(indices, *hint.value, mask);
        let rule = AndRule { parent: state.node, hint, val, left: eval.clone(), deductive };
        algo.push(state.derive(algo.ctx.alloc(rule.into()), weight, true, false));
    }
}

#[derive(Clone)]
pub struct OrRule<'a>{
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>, 
    pub val: i16, 
    pub left: Bv<'a>,
    pub deductive: bool
}

impl<'a> Rule<'a> for OrRule<'a> {
    fn is_deductive(&self) -> bool { self.deductive }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>, value: &Bv<'a>) -> crate::bv::Bv<'a> { 
        ctx.alloc_bv(value.iter().zip(self.left.iter()).map(|(v1, v2)| unsafe { v1 | v2 }))
    }

    fn build_expr(&self,left:Option<BvExpr>,right:BvExpr) -> BvExpr {
        let l = if self.val == 0 { left.unwrap_or(BvExpr::Symbol(0)) } else { BvExpr::Symbol(self.val) };
        BvExpr::Or(Box::new(l), Box::new(right))
    }
    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> std::fmt::Debug for OrRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "| {}", self.val)
    }
}

impl<'a> OrRule<'a> {
    pub fn or_weight(t: u64, m: u64, x: u64) -> Option<f32> {
        let tvalid = (t & m).count_ones();
        let upvalid = (!t & x & m).count_ones();
        if upvalid == 0 && (t & !x & m) != 0 && x & m != 0 {
            Some(1.0)
        } else if 2 * upvalid < tvalid && (t & !x & m) != 0 && x & m != 0 {
            let percentage = (x & m).count_ones() as f32 / m.count_ones() as f32;
            let w = (1.0 - (2 * upvalid) as f32 / tvalid as f32) * f32::min(percentage * 2.0, 1.0);
            Some(w)
        } else { None }
    }

    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.or { return; }
        let mut weight = 0.0;
        let (indices, mask) = algo.ctx.collect_with_indices(hint.zip(**eval).flat_map(|(i, t, m, x)| {
            if let Some(w) = Self::or_weight(t, m, x) {
                if w > 0.6 {
                    weight = f32::max(weight, w);
                    Some((i, m & !x))
                } else { None }
            } else { None }
        }));
        let deductive = weight > 0.9999;
        let hint = Hint::new(indices, *hint.value, mask);
        let rule = OrRule { parent: state.node, hint, val, left: eval.clone(), deductive };
        algo.push(state.derive(algo.ctx.alloc(rule.into()), weight, true, false))
    }
}

#[derive(Clone)]
pub struct MulRule<'a>{
    pub parent: &'a BvRule<'a>,
    pub hint: Hint<'a>, 
    pub val: i16, 
    pub left: Bv<'a>,
    pub deductive: bool
}

impl<'a> Rule<'a> for MulRule<'a> {
    fn is_deductive(&self) -> bool { self.deductive }

    fn hint(&self) -> &Hint<'a> { &self.hint }

    fn evaluate(&self,ctx: &crate::bv::Context<'a>, value: &Bv<'a>) -> crate::bv::Bv<'a> { 
        ctx.alloc_bv(value.iter().zip(self.left.iter()).map(|(v1, v2)| unsafe { v1.unchecked_mul(*v2) }))
    }

    fn build_expr(&self,left:Option<BvExpr>,right:BvExpr) -> BvExpr {
        let l = if self.val == 0 { left.unwrap_or(BvExpr::Symbol(0)) } else { BvExpr::Symbol(self.val) };
        BvExpr::Mul(Box::new(l), Box::new(right))
    }
    fn parent(&self) ->  &'a super::BvRule<'a> { self.parent }
}

impl<'a> std::fmt::Debug for MulRule<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "* {}", self.val)
    }
}

impl<'a> MulRule<'a> {
    pub fn derive_from(algo: &mut Algo<'a, '_>, state: & State<'a>, hint: Hint<'a>, val: i16, eval: &Bv<'a>) {
        if !algo.conf.mul { return; }
        let mut weight = 0.0;
        let value = algo.ctx.collect(hint.zip(**eval).map(|(i, t, m, x)| {
            if x != 0 && (t & m) % x == 0 {
                weight = f32::max(weight, 1.0);
            }
            (i, if x == 0 {0} else {(t & m).div_floor(x) })
        }));
        let deductive = weight > 0.9999;
        let hint = Hint::new(hint.indices, value, &algo.ctx.allone());
        let rule = MulRule { parent: state.node, hint, val, left: eval.clone(), deductive };
        algo.push(state.derive(algo.ctx.alloc(rule.into()), weight, true, false))
    }
}