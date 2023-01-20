
use std::{cmp::{Ordering, min}, collections::BinaryHeap};

use crate::{rule::{BvRule, SubProblem, AcceptRule, Rule, NotRule, XorRule, AddRule, AndRule, OrRule, ShlRule, ShrRule, RootRule, MulRule}, bv::{Context, Indices, Bv, Hint}, debg, debg2, info, utils::SortedIter};

const BEST_SEARCH_LIMIT : usize = 10000;

pub struct State<'a> {
    pub weight: f32,
    accumulative_weight: f32,
    step: usize,
    timestemp: usize,
    size: usize,
    pub node: &'a BvRule<'a>
}

impl<'a> PartialEq for State<'a> {
    #[inline(always)]
    fn eq(&self, other: &Self) -> bool {
        std::ptr::eq(self.node, other.node)
    }
}

impl <'a> Eq for State<'a> {}

impl<'a> Ord for State<'a> {
    #[inline(always)]
    fn cmp(&self, other: &Self) -> Ordering {
            if self.evaluate() < other.evaluate() {
                Ordering::Greater
            } else {
                Ordering::Less
            }
    }
}

impl<'a> State<'a> {
    pub fn evaluate(&self) -> usize {
        min(self.step, 8 * self.size)
    }
    #[inline(always)]
    pub fn derive(&self, rule: &'a BvRule<'a>, weight: f32, accumulative: bool, is_upper_accept: bool) -> State<'a> {
        let weight1 = weight * self.accumulative_weight * (1.0 - 1.0 / (10 * rule.hint().indices.len()) as f32);
        let weight2 = if weight1 >= 0.7 { weight1 } else {0.0};
        debg2!("   {rule:?} {weight} {weight2}");
        let step_ratio = if self.step > 30 { 40.0 / self.step.saturating_sub(35) as f32 } else { 8.0 };
        State {
            weight: weight2,
            accumulative_weight: {
                self.accumulative_weight * (if accumulative { weight } else { 1.0 })
            },
            timestemp: self.timestemp,
            step: self.step + if is_upper_accept && weight > 0.99 { 0 } else { 4 + ((1.0 - weight) * 12.0) as usize },
            size: self.size + if is_upper_accept { 0 } else { 1 },
            node: rule,
        }
    }
}

impl<'a> PartialOrd for State<'a> {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

#[derive(Clone, Copy, PartialEq, Eq)]
pub enum FilterType {
    OnlyUnsolved,
    OnlyBetterSolution,
    OnlyDifferentSolution,
    None,
}
#[derive(Debug, Clone)]
pub struct AlgoConf {
    pub varibles: Vec<usize>,
    pub constants: Vec<u64>,
    pub add: bool,
    pub xor: bool,
    pub and: bool,
    pub or: bool,
    pub shr: bool,
    pub shl: bool,
    pub not: bool,
    pub udiv: bool,
    pub mul: bool,
    pub ite: bool,
}

impl AlgoConf {
    pub fn new() -> Self {
        AlgoConf { varibles: Vec::new(), constants: Vec::new(), add: false, xor: false, and: false, or: false, shr: false, shl: false, not: false, ite: false, mul: false, udiv: false }
    }
}

pub struct Algo<'a, 'b> {
    heap: BinaryHeap<State<'a>>,
    pub ctx: &'b Context<'a>,
    pub unsolved: Vec<usize>,
    pub solution: Vec<(&'a BvRule<'a>, Vec<usize>)>,
    pub target: Bv<'a>,
    pub counter: usize,
    pub is_cond_search: bool,
    pub filter: FilterType,
    pub conf: AlgoConf,
}

impl<'a, 'b> Algo<'a, 'b> {
    pub fn new(ctx: &'b Context<'a>, target: Bv<'a>, is_cond_search: bool, conf: AlgoConf) -> Self {
        let mut this = Self {
            heap: BinaryHeap::new(),
            ctx,
            unsolved: Vec::from(ctx.allexamples.0),
            solution: Vec::new(),
            target: target.clone(),
            counter: 0,
            is_cond_search,
            filter: FilterType::OnlyDifferentSolution,
            conf
        };
        this.heap.push(State {
            weight: 1.0,
            accumulative_weight: 1.0,
            step: 0,
            timestemp: 0,
            size: 0,
            node: this.ctx.alloc(RootRule::new(Hint::new(this.ctx.allexamples, target.0, this.ctx.allmax)).into()),
        });
        this
    }
    #[inline]
    pub fn run(&mut self, limit: usize) {
        while self.counter <= limit {
            if self.unsolved.len() == 0 && self.filter == FilterType::OnlyUnsolved { break; }
            if let Some(state) = self.heap.pop() {
                self.derive(state);
                self.counter += 1;
            } else { break; }
        }
    }
    #[inline]
    pub fn next(&mut self, limit: usize) -> Option<&(&'a BvRule<'a>, Vec<usize>)> {
        let start_timestemp = self.heap.peek().unwrap().timestemp;
        while self.unsolved.len() > 0 && self.counter <= limit {
            if let Some(state) = self.heap.pop() {
                if state.timestemp > start_timestemp {
                    return Some(self.solution.last().unwrap())
                }
                self.derive(state);
                self.counter += 1;
            } else { break; }
        }
        None
    }
    pub fn push(&mut self, state: State<'a>) {
        self.heap.push(state);
    }
    pub fn add_solution(&mut self, state: State<'a>) {
        if let BvRule::AcceptRule(AcceptRule { eval, .. }) = state.node {
            let solved : Vec<_> = (0..self.ctx.len).filter(|&i| eval[i] == self.target[i]).collect();
            let cond = self.result_filter(&solved);
            if cond {
                if self.filter != FilterType::OnlyUnsolved { self.retain_unsolved(solved.as_slice()); }
                info!("{}: Partial Solution Found: {}, {:?} solved at {}", self.counter, state.node, solved.len(), state.evaluate());
                self.solution.push((state.node, solved));
            }
        }
    }
    fn retain_hint(&mut self, hint: & Hint<'a>) -> bool {
        unsafe {
            let hint = hint as *const Hint<'a> as *mut Hint<'a>;
            let new_indices = self.ctx.alloc_iter_unsized((*hint).indices.intersect(Indices(self.unsolved.as_slice())));
            if new_indices.len() == 0 { return false; }
            (*hint).indices = new_indices.into();
        }
        true
    }
    fn retain_unsolved(&mut self, solved: &[usize]) -> usize {
        let mut solved_iter = unsafe { SortedIter::from_unchecked(solved.iter().cloned()) } ;
        let old_len = self.unsolved.len();
        self.unsolved.retain(|i| !solved_iter.next_contains(*i));
        old_len.saturating_sub(self.unsolved.len())
    }
    fn better_solution(&self, set: Indices) -> bool {
        for sol in self.solution.iter() {
            if set.subset(&Indices(sol.1.as_slice())) {
                return false;
            }
        }
        true
    }
    fn new_solution(&self, set: Indices) -> bool {
        for sol in self.solution.iter() {
            if set == Indices(sol.1.as_slice()) {
                return false;
            }
        }
        true
    }
    #[inline]
    fn hint_filter(&mut self, mut state: &mut State<'a>) -> bool {
        match self.filter {
            FilterType::OnlyUnsolved => self.retain_hint(state.node.hint()),
            FilterType::OnlyBetterSolution => self.better_solution(state.node.hint().indices),
            FilterType::OnlyDifferentSolution => self.new_solution(state.node.hint().indices),
            FilterType::None => true,
        }
    }
    #[inline]
    fn result_filter(&mut self, solved: &Vec<usize>) -> bool{
        match self.filter {
            FilterType::OnlyUnsolved => self.retain_unsolved(solved.as_slice()) > 0,
            FilterType::OnlyBetterSolution => self.better_solution(Indices(solved.as_slice())),
            FilterType::OnlyDifferentSolution => self.new_solution(Indices(solved.as_slice())),
            FilterType::None => true,
        }
    }
    fn derive(&mut self, mut state: State<'a>) {
        if state.timestemp > self.solution.len() {
            if !self.hint_filter(&mut state) {
                // debg!("{}X {}", self.counter, state.node);
                return;
            }
            state.timestemp = self.solution.len();
        } 
        // if state.size > 10 { return; }
        match state.node.subproblem() {
            SubProblem::Lower(hint) => {
                // debg!("{}L {} {:?} {} {}", self.counter, state.node, hint, state.step, state.weight);
                let mut consts = self.ctx.consts();
                if !(self.is_cond_search && state.node.is_and()) {
                    for (val, eval) in consts {
                        AcceptRule::derive_from(self, &state, hint.clone(), val, eval, true);
                    }
                }
                for (val, eval) in self.ctx.variables() {
                    if state.node.is_shr().is_some() {
                        AcceptRule::derive_from_shr(self, &state, hint.clone(), val, eval);
                        NotRule::derive_from(self, &state, hint.clone(), val, eval);
                    } else {
                        if !state.node.is_not() {
                            NotRule::derive_from(self, &state, hint.clone(), val, eval); 
                            AndRule::derive_from(self, &state, hint.clone(), val, eval);
                            OrRule::derive_from(self, &state, hint.clone(), val, eval);
                        }
                        AcceptRule::derive_from(self, &state, hint.clone(), val, eval, false);
                    }
                    XorRule::derive_from(self, &state, hint.clone(), val, eval);
                    AddRule::derive_from(self, &state, hint.clone(), val, eval);
                    MulRule::derive_from(self, &state, hint.clone(), val, eval);
                    ShlRule::derive_from(self, &state, hint.clone(), val, eval);
                    ShrRule::derive_from(self, &state, hint.clone(), val, eval);
                }
            }
            SubProblem::Upper(hint, ref eval, parent, left) => {
                // debg!("{}U {} {:?} {} {}", self.counter, state.node, hint, state.step, state.weight);
                if parent.is_shr().is_some() {
                    AcceptRule::derive_from_shr(self, &state, hint.clone(), 0, eval);
                } else {
                    if !parent.is_not() {
                        NotRule::derive_from(self, &state, hint.clone(), 0, eval);
                        AndRule::derive_from(self, &state, hint.clone(), 0, eval);
                        OrRule::derive_from(self, &state, hint.clone(), 0, eval);
                    }
                    AcceptRule::derive_from(self, &state, hint.clone(), 0, eval, false);
                }
                if !left.is_shr().is_some() {
                    XorRule::derive_from(self, &state, hint.clone(), 0, eval);
                }
                AddRule::derive_from(self, &state, hint.clone(), 0, eval);
                MulRule::derive_from(self, &state, hint.clone(), 0, eval);
            }
        }

    }
}

// impl<'a> Iterator for Algo<'a> {
//     type Item = &'a RuleNode<'a>;

//     fn next(&mut self) -> Option<Self::Item> {
//         loop{
//             if let Some(State { weight, prev, rule }) = self.heap.pop() {
//                 if let Some(node) = RuleNode::cons(rule, prev, &self.ctx) {
//                     let node = self.ctx.alloc(node);
//                     debg!("{node} - {weight}");
//                     debg2!("{node:?}");
//                     if node.complete() { break Some(node); }
//                     self.add(node, weight);
//                 } else { break Some(prev); }
//             } else { break None; }
//         }
//     }
// }

// #[cfg(test)]
// mod tests {
//     use bumpalo::Bump;

//     use crate::{bv::{BinaryOp, Context, Hint}, algo::Algo, rule};

//     use crate::rule::{RuleNode, Rule};

//     #[test]
//     fn test1() {
//         let bump = Bump::new();
//         let mut ctx = Context::new(&bump, 1);
//         let x = ctx.new_var([1].into_iter());
//         let out = ctx.alloc_slice([3].into_iter());
//         let lower = vec![rule!(+ 1), rule!(| 1), rule!(& 1), rule!(^ 1), rule!(id 1)];
//         let upper = vec![rule!(+ 0), rule!(| 0), rule!(& 0), rule!(^ 0), rule!(id 0)];
//         let mut algo = Algo::new(ctx, lower, upper, out);
//         eprintln!("asdf");
//         println!("{:?}", algo.next());
//         // println!("{:?}", algo.take(1).map(|x| x.to_expr()).collect::<Vec<_>>());
//     }
// }