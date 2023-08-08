use std::{simd::{LaneCount, SupportedLaneCount}, collections::hash_map::Entry, mem::size_of};
use std::simd::SimdPartialEq;

use crate::{generate_rules_matching, info, search::filter::Filter};

use super::{config::{Config, Rule}, expr::Expr, Bv, algo::dynamic_usage_for_capacity};
use bumpalo::Bump;
// use fnv::FnvHashMap as HashMap;
use std::collections::HashMap;


pub trait FilterOwn<'a, const N: usize, A: super::Algo<'a, N>> : FnMut(&mut A, Expr<'a>, Bv<N>) ->  Result<(), ()> {}
impl<'a, const N: usize, A: super::Algo<'a, N>, T: FnMut(&mut A, Expr<'a>, Bv<N>) ->  Result<(), ()>> FilterOwn<'a, N, A> for T {}

pub struct Algo<'a, const N: usize>   {
    config: Config<N>,
    exprs: Vec<Vec<(&'a Expr<'a>, Bv<N>)>>,
    pub map: HashMap<Bv<N>, Option<&'a Expr<'a>> >,
    args: Vec<Bv<N>>,
    dag_size: bool,
    bump: &'a Bump,
    mem_size_limit: usize,
    atom_shift: bool,
    counter: usize,
    counter2: usize,
    
}


impl<'a, const N: usize> Algo<'a, N>  {
    
    
    pub fn new(config: Config<N>, args: Vec<Bv<N>>, output: Bv<N>, dag_size: bool, bump: &'a Bump, mem_size_limit: usize) -> Self {
        let mut map = HashMap::with_capacity_and_hasher(2000000, Default::default());
        map.insert(output, None);
        Algo { config, exprs: vec![Vec::new()], map, dag_size, args, bump, mem_size_limit, counter: 0, atom_shift : true, counter2: 0}
    }
    pub fn run_size(&mut self, size: usize, mut f: impl Filter<'a, N, Self>) ->  Result<(), ()> {
        // These three fields can not be changed.
        let selfexprs = unsafe { you_can::borrow_unchecked(&self.exprs) };
        let config = unsafe { you_can::borrow_unchecked(&self.config) };
        let args = unsafe { you_can::borrow_unchecked(&self.args) };
        let atom_shift = self.atom_shift;

        if selfexprs.len() > size {
            for (e, v) in selfexprs[size].iter() {
                f(self, e, v.clone())?;
            }
            Ok(())
        } else {
            for rule in config.iter() {
                let algo = &mut *self;
                macro_rules! generate_op {
                    (op1, $e:expr, $v:expr) => {
                        algo.run_size_dyn(size - 1, Box::new(|algo, e1, v1: Bv<N>| {
                            let v = $v(v1);
                            algo.try_insert($e(&e1), v, &mut f)
                        }))?;
                    };
                    (op1_constant, $e:expr, $v:expr, $c:expr) => {
                        algo.run_size_dyn(size - 1, Box::new(|algo, e1, v1: Bv<N>| {
                            let cv = Bv([$c; N].into());
                            let v = $v(v1, cv);
                            let c = algo.bump.alloc(Expr::Const($c));
                            algo.try_insert($e(e1, c), v, &mut f)
                        }))?;
                    };
                    (op2_sym, $e:expr, $v:expr) => {
                        for i in 1..=(size - 1).div_floor(2) {
                            algo.run_size_dyn(i, Box::new(|algo, e1, v1: Bv<N>| {
                                algo.run_size_dyn(size - 1 - i, Box::new(|algo, e2, v2: Bv<N>| {
                                    let v = $v(v1, v2);
                                    algo.try_insert($e(e1, e2), v, &mut f)
                                }))
                            }))?;
                        }
                        if size > 3 && algo.dag_size {
                            let stop = algo.run_size_dyn(size - 2, Box::new(|algo, e1, v1: Bv<N>| {
                                e1.for_each_subexpr(&mut |e2, v2| {
                                    let v = $v(v1, v2);
                                    algo.try_insert($e(e1, e2), v, &mut f)
                                }, &*args)?;
                                let v = $v(v1, v1);
                                algo.try_insert($e(e1, e1), v, &mut f)
                            }))?;
                        }
                    };
                    (op2, $e:expr, $v:expr) => {
                        if rule.is_shift() && atom_shift {
                            if size > 1 {
                                algo.run_size_dyn(size - 1, Box::new(|algo, e1, v1: Bv<N>| {
                                    algo.run_size_dyn(1, Box::new(|algo, e2, v2: Bv<N>| {
                                        let v = $v(v1, v2);
                                        algo.try_insert($e(e1, e2), v, &mut f)
                                    }))
                                }))?;
                            }
                            continue;
                        }
                        if size > 2 {
                            for i in 1..(size - 2) {
                                algo.run_size_dyn(size - 2 - i, Box::new(|algo, e1, v1: Bv<N>| {
                                    algo.run_size_dyn(i, Box::new(|algo, e2, v2: Bv<N>| {
                                        let v = $v(v1, v2);
                                        algo.try_insert($e(e1, e2), v, &mut f)
                                    }))
                                }))?;
                            }
                        }
                        if size > 3 && algo.dag_size {
                            algo.run_size_dyn(size - 2, Box::new(|algo, e1, v1: Bv<N>| {
                                e1.for_each_subexpr(&mut |e2, v2| {
                                    let v = $v(v1, v2);
                                    algo.try_insert($e(e1, e2), v, &mut f)?;
                                    let v = $v(v2, v1);
                                    algo.try_insert($e(e2, e1), v, &mut f)
                                }, &*args)?;
                                let v = $v(v1, v1);
                                algo.try_insert($e(e1, e1), v, &mut f)
                            }))?;
                        }
                    };
                    (op2_nonzero, $e:expr, $v:expr) => {
                        if size > 3 {
                            for i in 1..(size - 3) {
                                algo.run_size_dyn(i, Box::new(|algo, e1, v1: Bv<N>| {
                                    algo.run_size_dyn(size - 3 - i, Box::new(|algo, e2, v2: Bv<N>| {
                                        if v2.any_eq(&Bv([0; N])) { return Ok(()); }
                                        let v = $v(v1, v2);
                                        algo.try_insert($e(e1, e2), v, &mut f)
                                    }))
                                }))?;
                            }
                        }
                        if size > 5 && algo.dag_size {
                            algo.run_size_dyn(size - 5, Box::new(|algo, e1, v1: Bv<N>| {
                                e1.for_each_subexpr(&mut |e2, v2| {
                                    if v2.any_eq(&Bv([0; N])) { return Ok(()); }
                                    let v = $v(v1, v2);
                                    algo.try_insert($e(e1, e2), v, &mut f)
                                }, &*args)?;
                                if v1.any_eq(&Bv([0; N])) { return Ok(()); }
                                let v = $v(v1, v1);
                                algo.try_insert($e(e1, e1), v, &mut f)
                            }))?;
                        }
                    };
                    (var, $id:expr) => {
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
                            algo.try_insert(expr.clone(), expr.eval_bv(&*args), &mut f)?;
                        }
                    };
                    (ite) => {};
                }
                generate_rules_matching!(rule);
            }
            Ok(())
        }
    }
    
    pub fn run_size_dyn(&mut self, size: usize, mut f: Box<dyn Filter<'a, N, Self> + '_>) -> Result<(), ()> {
        self.run_size(size, f)
    }
    pub fn run_until(&mut self, mut f: impl Filter<'a, N, Self>) -> Result<(), ()> {
        for size in self.exprs.len()..self.mem_size_limit {
            let mut expr : Vec<(&'a Expr<'a>, Bv<N>)> = Vec::new();
            self.run_size(size, #[inline] |algo, e, v| {
                f(algo, e, v)?;
                expr.push((algo.bump.alloc(e.clone()), v));
                Ok(())
            })?;
            self.exprs.push(expr);
        }
        assert!(self.exprs.len() == self.mem_size_limit);
        info!("Exceed mem_size_limit.");
        for size in self.mem_size_limit..usize::MAX {
            self.run_size(size, #[inline] |algo, e, v| f(algo, e, v))?;
        }
        Ok(())
    }
    pub fn try_insert(&mut self, e: Expr<'a>, v: Bv<N>, mut f: impl Filter<'a, N, Self>) -> Result<(), ()> {
        self.counter2 += 1;
        match self.map.entry(v) {
            Entry::Vacant(entry) => {
                self.counter += 1;
                if self.mem_size_limit > self.exprs.len() {
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

}


impl<'a, const N: usize> crate::enumerate::Algo<'a, N> for Algo<'a, N>  {
    fn len(&self) -> usize {
        self.exprs.iter().map(|x| x.len()).sum()
    }
    fn count(&self) -> usize {
        self.counter
    }
    fn count2(&self) -> usize {
        self.counter2
    }
    fn size(&self) -> usize {
        self.exprs.len()
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




