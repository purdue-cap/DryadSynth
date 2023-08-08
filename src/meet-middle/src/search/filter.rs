
use std::{simd::{LaneCount, SupportedLaneCount, SimdPartialEq}, intrinsics::size_of, mem::MaybeUninit, time::Instant};

use bumpalo::Bump;
use separator::Separatable;
use serde::{Deserialize, Serialize};


use crate::{solutions::Solutions, deductive::{combine::CombineRules, reverse::ReverseRule}, enumerate::{Bv, expr::{Expr, OwnedExpr}, Algo}, info, oexpr};

pub trait Filter<'a, const N: usize, A: Algo<'a, N>> : FnMut(&mut A, &'a Expr<'a>, Bv<N>) ->  Result<(), ()> {}
impl<'a, const N: usize, A: Algo<'a, N>, T: FnMut(&mut A, &'a Expr<'a>, Bv<N>) ->  Result<(), ()>> Filter<'a, N, A> for T {}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct FilterConfig {
    pub stop_at_new_sol: bool,
    pub count_limit: usize,
    pub count_limit_solved: usize,
    pub time_limit: usize,
    pub deductive_combine: bool,
    pub deductive_reverse: bool,
}

impl FilterConfig {
    pub const fn cond_default() -> Self {
        Self { stop_at_new_sol: false, count_limit: usize::MAX, count_limit_solved: usize::MAX, deductive_combine: false, deductive_reverse: false, time_limit: usize::MAX }
    }
    pub const fn expr_default() -> Self {
        Self { stop_at_new_sol: true, count_limit: usize::MAX, count_limit_solved: 100000, deductive_combine: true, deductive_reverse: true, time_limit: usize::MAX }
    }
    pub const fn improve_default() -> Self {
        Self { stop_at_new_sol: true, count_limit: usize::MAX, count_limit_solved: 0, deductive_combine: true, deductive_reverse: true, time_limit: usize::MAX }
    }
    
    pub fn state<'a ,const N: usize>(&self, args: Vec<Bv<N>>, out: &Bv<N>, bump: &'a Bump, partial_solution: bool) -> FilterState<'a, N>  {
        use OwnedExpr::*;
        FilterState {
            comb_rules: self.deductive_combine.then(|| CombineRules::new(out)),
            reverse_rules: self.deductive_reverse.then(|| ReverseRule::new()),
            output: out.clone(),
            solved: false,
            bump,
            args: args.clone(),
            time_limit: self.time_limit,
            stop_at_new_sol: self.stop_at_new_sol,
            count_limit: self.count_limit,
            count_limit_solved: self.count_limit_solved,
            partial_solution,
            start_time: Instant::now(),
        }
    }
}

pub struct FilterState<'a ,const N: usize>  {
    stop_at_new_sol: bool,
    count_limit: usize,
    count_limit_solved: usize,
    comb_rules: Option<CombineRules<'a, N>>,
    reverse_rules: Option<ReverseRule>,
    time_limit: usize,
    args: Vec<Bv<N>>,
    output: Bv<N>,
    pub solved: bool,
    bump: &'a Bump,
    partial_solution: bool,
    start_time: Instant,
}

impl<'a, const N: usize> FilterState<'a, N> {
    #[inline(always)]
    pub fn filter(&mut self, algo: &impl Algo<'a,N>, e: &Expr<'a>, v: Bv<N>, sol: &mut Solutions) -> Result<(), ()> {
        if v.any_eq(&self.output) {
            if self.partial_solution {
                if sol.add_solution(e) {
                    if self.stop_at_new_sol { return Err(()); }
                } else if v == self.output {
                    if !self.solved { info!("Sample is solved at {:?}", e); }
                    self.solved = true;
                }
            } else {
                if algo.count() < 1000 && sol.add_solution(e) {
                    if !self.solved { info!("Sample is solved at {:?}", e); }
                    if self.stop_at_new_sol { return Err(()); }
                }
                if v == self.output {
                    if sol.add_solution(e) { if self.stop_at_new_sol { return Err(()); } }
                    if !self.solved { info!("Sample is solved at {:?}", e); }
                    self.solved = true;
                }
            }
            
        }
        if let Some(comb_rules) = &mut self.comb_rules {
            if let Some((e, bump)) = comb_rules.test(e, &v, &sol) {
                if sol.add_solution(e) {
                    if self.stop_at_new_sol { return Err(()); }
                }
                self.solved = true;
                drop(bump);
            }
        }
        if let Some(reverse_rules) = &mut self.reverse_rules {
            if let Some(e) = reverse_rules.reverse(algo, e, &v, &self.output, & self.bump) {
                if sol.add_solution(e) {
                    if self.stop_at_new_sol { return Err(()); }
                }
                self.solved = true;
            }
        }
        if algo.count() % 10000 == 0 && self.start_time.elapsed().as_millis() as usize >= self.time_limit {
            info!("Sample exceed time limit. {} remaining {} {}", sol.remaining(), algo.count(), self.time_limit);
            return Err(());
        } else if algo.count() > self.count_limit || self.solved && algo.count() > self.count_limit_solved {
            info!("Sample exceed limit. {} remaining {}", sol.remaining(), algo.count());
            return Err(());
        } else if algo.count() % 1000000 == 0 {
            let bump_size: usize = unsafe{ self.bump.iter_allocated_chunks_raw().map(|x| x.1).sum() };
            info!("Searching Size {} at Count {} {}, Estimate size: {} {} {} expr: {:?}", algo.size().separated_string(), algo.count().separated_string(), algo.len().separated_string(), algo.map_size().separated_string(), algo.expr_size().separated_string(), bump_size.separated_string(), e);
        }
        Ok(())
    }
}


