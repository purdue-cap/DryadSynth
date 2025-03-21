

use core::slice::SlicePattern;
use std::{simd::{LaneCount, SupportedLaneCount}, sync::Arc, time, thread};

use bumpalo::Bump;
use derive_more::{From, DebugCustom, Deref};
use itertools::Itertools;
use rand::{seq::IteratorRandom, Rng};
use sdset::{SetBuf, duo::OpBuilder, SetOperation};
use smallvec::SmallVec;
use spin::mutex::Mutex;

use crate::{enumerate::{expr::{OwnedExpr, Expr}, Bv}, parse::PbeConstraint, tree_learning::{Bits, self}, info, warn};
use crate::tree_learning::bits::BoxSliceExt;

pub type CondBuffer = Arc<Mutex<Vec<(OwnedExpr, Bits)>>>;

pub struct Solutions<'a> {
    pub unsolved: SetBuf<usize>,
    pub solved: Vec<(OwnedExpr, SetBuf<usize>)>,
    pub pbecstr: &'a PbeConstraint,
    pub check_dups: bool,
    pub sample_all: bool,
    pub check_tree_learning: Option<(CondBuffer, usize)>,
    pub cover_limit: usize,
    pub additional_check: PbeConstraint,
    pub cond_buffer: Option<CondBuffer>, 
    pub wait_cond: usize,
}

impl<'a> Solutions<'a> {
    pub fn new(cstr: &'a PbeConstraint, check_dups: bool, sample_all: bool) -> Self {
        let unsolved = sdset::SetBuf::new_unchecked((0..cstr.len()).collect());
        let solved = Vec::new();
        Self { unsolved, solved, pbecstr: cstr, cover_limit: 1, check_dups, sample_all, check_tree_learning: None, additional_check: PbeConstraint::new(cstr.args_count()), cond_buffer: None , wait_cond: 0}
    }
    pub fn add_solution(&mut self, sol: & Expr<'_>, counter: usize) -> bool {
        let set = self.pbecstr.test(sol);
        if set.len() == 0 { 
            warn!("{:?}", sol);
        }
        if set.len() >= self.cover_limit || set.len() >= self.unsolved.len() {
            if self.additional_check.len() > 0 {
                if self.additional_check.test(sol).len() < self.additional_check.len() {
                    return false;
                }
            }
            if self.check_dups {
                for (_, existed) in self.solved.iter() {
                    let diff: SetBuf<usize> = OpBuilder::new(set.as_set(), existed.as_set()).difference().into_set_buf();
                    if diff.len() == 0 { return false; }
                }
                self.unsolved = OpBuilder::new(self.unsolved.as_set(), set.as_set()).difference().into_set_buf();
                info!("Partial Solution Found: {:?}, when searching {}th expression, {} solved, {} remaining.", sol, counter, set.len(), self.remaining());
            } else {
                if set.len() == self.pbecstr.len() { return false; }
            }
            if let Some(buf) = &self.cond_buffer {
                let mut guard = buf.lock();
                let l = &self.solved[guard.len()..];
                let len = self.pbecstr.len();
                let mut a = l.iter().map(|(x, y)| (x.clone(), Bits::from_bit_iter(y.iter().cloned(), len))).collect_vec();
                guard.append(&mut a)
            }
            if set.len() == self.pbecstr.len() {
                self.solved.push((sol.into(), set));
                return true;
            }
            self.solved.push((sol.into(), set));
            self.tree_learning()
        } else { false }
    }
    pub fn add_solution2<const N: usize>(&mut self, sol: & Expr<'_>, args: &[Bv<N>], v: & Bv<N>) -> bool  {
        let set = self.pbecstr.test(sol);
        if set.len() == 0 { 
            let out2 = Bv::<N>::from((0..N).map(|i| args.iter().map(|v| v[i]).collect_vec()).map(|v| sol.eval(v.as_slice()).unwrap_or(0)));
            warn!("{:?} {:?} {:?}", sol, out2, v);
        }
        if set.len() >= self.cover_limit {
            if self.check_dups {
                for (_, existed) in self.solved.iter() {
                    let diff: SetBuf<usize> = OpBuilder::new(set.as_set(), existed.as_set()).difference().into_set_buf();
                    if diff.len() == 0 { return false; }
                }
                self.unsolved = OpBuilder::new(self.unsolved.as_set(), set.as_set()).difference().into_set_buf();
                info!("Partial Solution Found: {:?}, {} solved, {} remaining", sol, set.len(), self.remaining());
            } else {
                if set.len() == self.pbecstr.len() { return false; }
            }
            self.solved.push((sol.into(), set));
            self.tree_learning()
        } else { false }
    }
    pub fn add_solution_unchecked(&mut self, sol: & Expr<'_>) -> bool {
        let set = self.pbecstr.test(sol);
        if set.len() >= self.cover_limit {
            self.unsolved = OpBuilder::new(self.unsolved.as_set(), set.as_set()).difference().into_set_buf();
            self.solved.push((sol.into(), set));
            true
        } else { false }
    }
    pub fn sample_unsolved<const N: usize, R: Rng>(&self, rng: &mut R) -> (Vec<Bv<N>>, Bv<N>)  {
        let mut rand_index = self.unsolved.iter().cloned().choose_multiple(rng, N);
        rand_index.resize(N, 0);
        self.pbecstr.bv_from_slice(&rand_index.try_into().unwrap())
    }
    pub fn sample_all<const N: usize, R: Rng>(&self, rng: &mut R) -> (Vec<Bv<N>>, Bv<N>)  {
        let mut rand_index = (0..self.pbecstr.len()).into_iter().choose_multiple(rng, N);
        rand_index.resize(N, 0);
        self.pbecstr.bv_from_slice(&rand_index.try_into().unwrap())
    }
    pub fn sample<const N: usize, R: Rng>(&self, rng: &mut R) -> (Vec<Bv<N>>, Bv<N>)  {
        if self.sample_all { self.sample_all(rng) } else { self.sample_unsolved(rng) } 
    }
    pub fn remaining(&self) -> usize {
        self.unsolved.len()
    }
    pub fn set_cover_limit(&mut self, value: usize) {
        if value < self.pbecstr.len() {
            self.cover_limit = value;
        } else {
            self.cover_limit = self.pbecstr.len();
        }
    }
    pub fn to_bits(&self) -> Vec<(OwnedExpr, Bits)> {
        let len = self.pbecstr.len();
        self.solved.iter().map(|(x, y)| (x.clone(), Bits::from_bit_iter(y.iter().cloned(), len))).collect()
    }
    #[inline(always)]
    pub fn tree_learning(&mut self) -> bool {
        if let Some((conds, limit)) = &self.check_tree_learning {
            if self.unsolved.len() > 0 { return false; }
            if self.wait_cond > 0 {
                thread::sleep(time::Duration::from_millis(self.wait_cond as u64));
                self.wait_cond = 0;
            }
            // info!("Learning Tree");
            let guard = conds.lock();
            let bump = Bump::new();
            if guard.len() == 0 { return false; }
            let tl = tree_learning::tree_learning(self.to_bits(), &guard[..], self.pbecstr.len(), &bump, *limit);
            // info!("Decition Tree: {} {}", tl.subproblems.len(), tl.solved);
            // if self.cover_limit * 2 < self.pbecstr.len() { self.cover_limit *= 2; }
            tl.solved
        } else { true }
    }
    pub fn get_tree(&self) -> OwnedExpr {
        let (conds, limit) = self.check_tree_learning.as_ref().unwrap();
        let guard = conds.lock();
        let bump = Bump::new();
        let tl = tree_learning::tree_learning(self.to_bits(), &guard[..], self.pbecstr.len(), &bump, *limit);
        tl.bvexpr()
    }
}
