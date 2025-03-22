use std::{collections::hash_map::Entry, mem::size_of, time::Instant};

use crate::{generate_rules_matching, search::filter::Filter, debg};

use super::{config::{Config, Rule}, expr::Expr, Bv, algo::dynamic_usage_for_capacity, store::Store, utils::BitSetU64Ext};
use bumpalo::Bump;
// use fnv::FnvHashMap as HashMap;
use std::collections::HashMap;
use itertools::Itertools;

#[derive(Debug, PartialEq, Eq, Clone, Copy)]
struct Point{
    n: usize,
    k: usize,
}

impl Point {
    pub fn new(n: usize, k: usize) -> Self {
        Self{ n, k }
    }
    pub fn fcond(self) -> bool {
        self.n >=  self.k && self.n > 0 && self.k > 0
    }
    pub fn frange(self, other: Point) -> bool {
        self.fcond() && self.flt(other)
    }
    pub fn frangeeq(self, other: Point) -> bool {
        self.fcond() && self.fle(other)
    }
    pub fn f(self) -> usize {
        self.n - self.k
    }
    fn iter(f: usize, maxk: usize) -> impl Iterator<Item = Point> {
        (1..=maxk).map(move |k| Point::fromf(f, k))
    }
    fn flt(self, other: Self) -> bool {
        self.f() < other.f() || self.f() == other.f() && self.k < other.k
    }
    fn fle(self, other: Self) -> bool {
        self.f() < other.f() || self.f() == other.f() && self.k <= other.k
    }
    pub fn fromf(f: usize, k: usize) -> Self {
        Self{k, n: f + k }
    }
}

pub trait FilterOwn<'a, const N: usize, A: super::Algo<'a, N>> : FnMut(&mut A, Expr<'a>, Bv<N>) ->  Result<(), ()> {}
impl<'a, const N: usize, A: super::Algo<'a, N>, T: FnMut(&mut A, Expr<'a>, Bv<N>) ->  Result<(), ()>> FilterOwn<'a, N, A> for T {}

pub struct Algo<'a, const N: usize>   {
    atom_rules: Config<N>,
    rules: Config<N>,
    exprs: Vec<Store<'a, N>>,
    pub map: HashMap<Bv<N>, Option<&'a Expr<'a>> >,
    args: Vec<Bv<N>>,
    dag_size: bool,
    bump: &'a Bump,
    mem_size_limit: usize,
    counter: usize,
    searched_counter: usize,
    cur_point: Point,
    vars: u64,
    atom_shift: bool
}


impl<'a, const N: usize> Algo<'a, N>  {
    
    
    pub fn new(config: Config<N>, args: Vec<Bv<N>>, output: Bv<N>, dag_size: bool, bump: &'a Bump, mem_size_limit: usize) -> Self {
        let (atom_rules, rules) : (Vec<_>, _) = config.0.into_iter().partition(|x| x.is_atom());
        let mut map = HashMap::with_capacity_and_hasher(2000000, Default::default());
        map.insert(output, None);
        
        let vars = u64::from_bits(atom_rules.iter().map(|x| if let Rule::Var(_) = x { true } else { false }));
        println!("Atoms: {:?} {:x}", atom_rules, vars);

        let store = Store::<'a, N>::new((1 << atom_rules.len()) - 1);

        let exprs = (0..Point::fromf(mem_size_limit, atom_rules.len()).n).map(|_| store.clone()).collect();
        Algo { atom_rules: atom_rules.into(), rules: rules.into(), exprs, map, dag_size, args, bump, mem_size_limit, counter: 0, cur_point: Point::new(0, 0), searched_counter: 0, vars, atom_shift: true}
    }
    pub fn mem_limit_exceeded(&self) -> bool { self.cur_point.f() >= self.mem_size_limit }
    pub fn run_size(&mut self, size: usize, subset: u64, mut f: impl Filter<'a, N, Self>) ->  Result<(), ()> {
        // debg!("{} {:x}", size, subset);
        // These three fields can not be changed.
        let selfexprs = unsafe { you_can::borrow_unchecked(&self.exprs) };
        let rules = unsafe { you_can::borrow_unchecked(&self.rules) };
        let atom_rules = unsafe { you_can::borrow_unchecked(&self.atom_rules) };
        let p = Point::new(size, subset.count_ones() as usize);
        let atom_shift = self.atom_shift;
        if !p.frangeeq(self.cur_point) || (size > 1 && subset & self.vars == 0) || size == 0 {
            Ok(())
        } else if p != self.cur_point && p.f() < self.mem_size_limit {
            for (e, v) in selfexprs[size].set(subset).iter() {
                f(self, e, v.clone())?;
            }
            Ok(())
        } else {
            let enabled_atom_rules = atom_rules.iter().zip(subset.bit_iter()).flat_map(|(r, p)| if p { Some(r.clone()) } else { None }).collect_vec();
            for rule in rules.iter().chain(enabled_atom_rules.iter()) {
                
                let algo = &mut *self;
                macro_rules! generate_op {
                    (op1, $e:expr, $v:expr) => {
                        algo.op1(size, subset, $e, $v, &mut f)?;
                    };
                    (op1_constant, $e:expr, $v:expr, $c:expr) => {
                        algo.op1_constant(size, subset, $e, $v, $c, &mut f)?;
                    };
                    (op2_sym, $e:expr, $v:expr) => {
                        algo.op2_sym(size, subset, $e, $v, &mut f)?;
                    };
                    (op2, $e:expr, $v:expr) => {
                        if rule.is_shift() && atom_shift {
                            algo.op2_atom(size, subset, $e, $v, &mut f)?;
                        } else {
                            algo.op2(size, subset, $e, $v, &mut f)?;
                        }
                    };
                    (op2_nonzero, $e:expr, $v:expr) => {
                        algo.op2_nonzero(size, subset, $e, $v, &mut f)?;
                    };
                    (var, $id:expr) => {
                        let args = unsafe { you_can::borrow_unchecked(&algo.args) };
                        if size == 1 {
                            let v = args[*$id];
                            algo.try_insert(Expr::Var(*$id), v, &mut f)?;
                        }
                    };
                    (const, $v:expr) => {
                        if size == 1 {
                            let v = Bv([*$v; N].into());
                            algo.try_insert(Expr::Const(*$v), v, &mut f)?;
                        }
                    };
                    (chatgpt, $e:expr, $v:expr) => {
                        if size == *$v {
                            let expr = Expr::from_owned(algo.bump, $e);
                            algo.try_insert(expr.clone(), expr.eval_bv(&*algo.args), &mut f)?;
                        }
                    };
                    (ite) => {};
                }
                generate_rules_matching!(rule);
            }
            Ok(())
        }
    }
    
    pub fn run_size_dyn(&mut self, size: usize, subset: u64, f: Box<dyn Filter<'a, N, Self> + '_>) -> Result<(), ()> {
        self.run_size(size, subset, f)
    }

    pub fn run_fill(&mut self, size: usize, subset: u64, mut f: impl Filter<'a, N, Self>) -> Result<(), ()> {
        if !self.mem_limit_exceeded() {
            let mut expr : Vec<(&'a Expr<'a>, Bv<N>)> = Vec::new();
            self.run_size(size, subset, #[inline] |algo, e, v| {
                f(algo, e, v)?;
                expr.push((algo.bump.alloc(e.clone()), v));
                Ok(())
            })?;
            self.exprs[size].append(subset, expr);
        } else {
            self.run_size(size, subset, #[inline] |algo, e, v| f(algo, e, v))?;
        }
        Ok(())
    }
    pub fn run_until(&mut self, mut callback: impl Filter<'a, N, Self>) -> Result<(), ()> {
        for f in 0..usize::MAX {
            for p in Point::iter(f, self.atom_rules.len()) {
                self.cur_point = p;
                let counter1 = self.searched_counter;
                let counter2 = self.counter;
                let timer = Instant::now();
                
                for bitset in (self.atom_rules.len() as u64).comb_iter(p.k as u64) {
                    self.run_fill(p.n, bitset, &mut callback)?;
                }
                debg!("(n: {}, k: {}) => ({}, {}, {}ms)", p.n, p.k, self.searched_counter - counter1, self.counter - counter2, timer.elapsed().as_millis());
            }
        }
        Ok(())
    }

    pub fn try_insert(&mut self, e: Expr<'a>, v: Bv<N>, mut f: impl Filter<'a, N, Self>) -> Result<(), ()> {
        self.searched_counter += 1;
        let mem_limit_exceeded = self.mem_limit_exceeded();
        match self.map.entry(v) {
            Entry::Vacant(entry) => {
                self.counter += 1;
                if !mem_limit_exceeded {
                    let e : &'a Expr<'a> = self.bump.alloc(e);
                    entry.insert(Some(e));
                    f(self, e, v)
                } else {
                    let e = unsafe { std::mem::transmute::<&Expr<'a>, &'a Expr<'a>>(&e) };
                    f(self, e, v)
                }
            }
            Entry::Occupied(entry) => {
                if entry.get().is_none() {
                    let e = unsafe { std::mem::transmute::<&Expr<'a>, &'a Expr<'a>>(&e) };
                    f(self, e, v)
                } else { Ok(()) }
            }
        }
    }
    #[inline(always)]
    pub fn op1_constant(&mut self, size: usize, subset: u64, fe: fn(&'a Expr<'a>, &'a Expr<'a>) -> Expr<'a>, fv: fn(Bv<N>, Bv<N>) -> Bv<N>, constant: u64, mut f: &mut impl Filter<'a, N, Self>) -> Result<(), ()> {
        self.run_size_dyn(size - 1, subset, Box::new(|algo, e1, v1: Bv<N>| {
            let cv = Bv([constant; N].into());
            let v = fv(v1, cv);
            let c = algo.bump.alloc(Expr::Const(constant));
            algo.try_insert(fe(e1, c), v, &mut f)
        }))?;
        Ok(())
    }
    #[inline(always)]
    pub fn op2_atom(&mut self, size: usize, subset: u64, fe: fn(&'a Expr<'a>, &'a Expr<'a>) -> Expr<'a>, fv: fn(Bv<N>, Bv<N>) -> Bv<N>, mut f: &mut impl Filter<'a, N, Self>) -> Result<(), ()> {
        let algo = self;
        if size <= 1 { return Ok(())}
        for s2 in subset.one_iter() {
            for s1 in [subset & !s2, subset] {
                algo.run_size_dyn(1, s2, Box::new(|algo, e2, v2: Bv<N>| {
                    // let op_size = if size + 1 == 2 * s1.count_ones() as usize { 2 } else { 1 };
                    algo.run_size_dyn(size - 1, s1, Box::new(|algo, e1, v1: Bv<N>| {
                        if !e1.is_shift() {
                            let v = fv(v1, v2);
                            algo.try_insert(fe(e1, e2), v, &mut f)
                        } else { Ok(()) }
                    }))
                }))?;
            }
        }
        Ok(())
    }
    #[inline(always)]
    pub fn op2_sym(&mut self, size: usize, subset: u64, fe: fn(&'a Expr<'a>, &'a Expr<'a>) -> Expr<'a>, fv: fn(Bv<N>, Bv<N>) -> Bv<N>, mut f: &mut impl Filter<'a, N, Self>) -> Result<(), ()> {
        let args = unsafe { you_can::borrow_unchecked(&self.args) };
        let algo = self;
        for (s1, s2) in subset.split_iter() {
            if s1 > s2 { continue; }
            for i in 1..(size - 1) {
                algo.run_size_dyn(size - 1 - i, s1, Box::new(|algo, e1, v1: Bv<N>| {
                    algo.run_size_dyn(i, s2, Box::new(|algo, e2, v2: Bv<N>| {
                        let v = fv(v1, v2);
                        algo.try_insert(fe(e1, e2), v, &mut f)
                    }))
                }))?;
            }
        }
        if size > 3 && algo.dag_size {
            let stop = algo.run_size_dyn(size - 2, subset, Box::new(|algo, e1, v1: Bv<N>| {
                e1.for_each_subexpr(&mut |e2, v2| {
                    let v = fv(v1, v2);
                    algo.try_insert(fe(e1, e2), v, &mut f)
                }, &*args)?;
                let v = fv(v1, v1);
                algo.try_insert(fe(e1, e1), v, &mut f)
            }))?;
        }
        Ok(())
    }
    #[inline(always)]
    pub fn op2(&mut self, size: usize, subset: u64, fe: fn(&'a Expr<'a>, &'a Expr<'a>) -> Expr<'a>, fv: fn(Bv<N>, Bv<N>) -> Bv<N>, mut f: &mut impl Filter<'a, N, Self>) -> Result<(), ()> {
        let args = unsafe { you_can::borrow_unchecked(&self.args) };
        let algo = self;
        for (s1, s2) in subset.split_iter() {
            for i in 1..(size - 1) {
                algo.run_size_dyn(size - 1 - i, s1, Box::new(|algo, e1, v1: Bv<N>| {
                    algo.run_size_dyn(i, s2, Box::new(|algo, e2, v2: Bv<N>| {
                        let v = fv(v1, v2);
                        algo.try_insert(fe(e1, e2), v, &mut f)
                    }))
                }))?;
            }
        }
        if size > 3 && algo.dag_size {
            algo.run_size_dyn(size - 2, subset, Box::new(|algo, e1, v1: Bv<N>| {
                e1.for_each_subexpr(&mut |e2, v2| {
                    let v = fv(v1, v2);
                    algo.try_insert(fe(e1, e2), v, &mut f)?;
                    let v = fv(v2, v1);
                    algo.try_insert(fe(e2, e1), v, &mut f)
                }, &*args)?;
                let v = fv(v1, v1);
                algo.try_insert(fe(e1, e1), v, &mut f)
            }))?;
        }
        Ok(())
    }
    #[inline(always)]
    pub fn op2_nonzero(&mut self, size: usize, subset: u64, fe: fn(&'a Expr<'a>, &'a Expr<'a>) -> Expr<'a>, fv: fn(Bv<N>, Bv<N>) -> Bv<N>, mut f: &mut impl Filter<'a, N, Self>) -> Result<(), ()> {
        let args = unsafe { you_can::borrow_unchecked(&self.args) };
        let algo = self;
        if size > 3 {
            for (s1, s2) in subset.split_iter() {
                for i in 1..(size - 3) {
                    algo.run_size_dyn(i, s1, Box::new(|algo, e1, v1: Bv<N>| {
                        algo.run_size_dyn(size - 1 - i, s2, Box::new(|algo, e2, v2: Bv<N>| {
                            if v2.any_eq(&Bv([0; N])) { return Ok(()); }
                            let v = fv(v1, v2);
                            algo.try_insert(fe(e1, e2), v, &mut f)
                        }))
                    }))?;
                }
            }
        }
        if size > 4 && algo.dag_size {
            algo.run_size_dyn(size - 4, subset, Box::new(|algo, e1, v1: Bv<N>| {
                e1.for_each_subexpr(&mut |e2, v2| {
                    if v2.any_eq(&Bv([0; N])) { return Ok(()); }
                    let v = fv(v1, v2);
                    algo.try_insert(fe(e1, e2), v, &mut f)
                }, &*args)?;
                if v1.any_eq(&Bv([0; N])) { return Ok(()); }
                let v = fv(v1, v1);
                algo.try_insert(fe(e1, e1), v, &mut f)
            }))?;
        }
        Ok(())
    }
    #[inline(always)]
    pub fn op1(&mut self, size: usize, subset: u64, fe: fn(&'a Expr<'a>) -> Expr<'a>, fv: fn(Bv<N>) -> Bv<N>, mut f: &mut impl Filter<'a, N, Self>) -> Result<(), ()> {
        self.run_size_dyn(size - 1, subset, Box::new(|algo, e1, v1: Bv<N>| {
            let v = fv(v1);
            algo.try_insert(fe(&e1), v, &mut f)
        }))?;
        Ok(())
    }

}


impl<'a, const N: usize> crate::enumerate::Algo<'a, N> for Algo<'a, N>  {
    fn len(&self) -> usize {
        self.exprs.iter().map(|x| x.len()).sum()
    }
    fn count(&self) -> usize {
        self.searched_counter
    }
    fn count2(&self) -> usize { 0 }
    fn size(&self) -> usize {
        self.cur_point.f()
    }
    fn map_size(&self) -> usize {
        dynamic_usage_for_capacity::<Bv<N>, Option<&'a Expr<'a>>>(self.map.capacity())
    }
    fn expr_size(&self) -> usize {
        self.len() * size_of::<(&'a Expr<'a>, Bv<N>)>()
    }
    fn map(&self) -> &HashMap<Bv<N>, Option<&'a Expr<'a>>> {
        &self.map
    }
}




