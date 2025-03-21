use std::simd::{LaneCount, SupportedLaneCount, prelude::SimdPartialEq};

use bumpalo::Bump;

use crate::{enumerate::{expr::{Expr, OwnedExpr}, Bv}, solutions::Solutions, info, oexpr};

#[derive(Debug)]
pub enum SubExpr<'a, const N: usize>   {
    And(Expr<'a>, Bv<N> ),
    Or(Expr<'a>, Bv<N> ),
}

pub struct CombineRules<'a ,const N: usize>  {
    mask: Bv<N>,
    output: Bv<N>,
    subexprs: Vec<SubExpr<'a, N>>,
}

impl<'a, const N: usize> CombineRules<'a, N>

{
    pub fn new(output: &Bv<N>) -> Self {
        Self { mask: Bv::ONES, subexprs: Vec::new(), output: output.clone()}
    }
    
    pub fn collect_expr(&self, expr: &'a Expr<'a>, mut value: Bv<N>, bump: &'a Bump) -> (&'a Expr<'a>, usize) {
        let mut res: &'a Expr<'a> = expr;
        let mut size = 0;
        for sexpr in self.subexprs.iter().rev() {
            match sexpr {
                SubExpr::And(e, first) => {
                    if value & *first != value {
                        let e : &'a Expr<'a> = bump.alloc(e.clone());
                        res = bump.alloc(Expr::And(e, res));
                        value = value & *first;
                        size += 1;
                    }
                }
                SubExpr::Or(e, first) => {
                    if value | *first != value {
                        let e : &'a Expr<'a> = bump.alloc(e.clone());
                        res = bump.alloc(Expr::Or(e, res));
                        value = value | *first;
                        size += 1;
                    }
                }
            }
        }
        assert!(value == self.output, "{:?} {value:?} {:?}", res, self.output);
        (res, size)
    }
    #[inline(always)]
    pub fn test(&mut self, expr: & Expr<'a>, v: &Bv<N>, sol: &Solutions) -> Option<(&'a Expr<'a>, Bump)> {
        let first = v;
        // AND
        let and_cond = first.imples(&self.output);
        let and_count = (self.mask & *first).count();
        let and_cnstr = self.mask.count() - and_count > 2 * (N as u32);
        if and_cond && and_cnstr && 3 * first.count_zeros() > self.output.count_zeros() && (N >= sol.pbecstr.len() || sol.pbecstr.test_and(expr)) {
            self.subexprs.push(SubExpr::And(expr.clone(), !self.mask | *first));
            self.mask = self.mask & *first;
            // println!("{:?} and {:?} {}", expr, first.count_zeros() as f32 / self.output.count_zeros() as f32, self.mask.count());
        }
        // OR
        let or_cond = self.output.imples(&first);
        let or_count = (self.mask & !*first).count();
        let or_cnstr = self.mask.count() - or_count > 2 * (N as u32);
        if or_cond && or_cnstr && 3 * first.count() > self.output.count() && (N >= sol.pbecstr.len() || sol.pbecstr.test_or(expr)) {
            self.subexprs.push(SubExpr::Or(expr.clone(), self.mask & *first));
            self.mask = self.mask & !*first;
            // println!("{:?} or {:?} {}", expr, first.count() as f32 / self.output.count() as f32, self.mask.count());
        }

        if self.mask & (*first ^ self.output) == Bv::ZEROS {
            let bump = Bump::new();
            let b: &'a Bump = unsafe { std::mem::transmute(&bump) };
            let (c, size) = self.collect_expr(b.alloc(expr.clone()), first.clone(), b);
            let c : &'a Expr<'a> = c;
            // println!("!!! {:?} {}", expr, size);
            if size > 4 { return None; }
            return Some((c, bump));
        }
        None
    }
}










