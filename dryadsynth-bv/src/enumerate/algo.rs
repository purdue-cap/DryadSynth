use std::{intrinsics::size_of, alloc::Layout};
// use std::collections::HashMap;
// use nohash_hasher::{BuildNoHashHasher, NoHashHasher};
// // use fnv::FnvHashMap as HashMap;
use std::collections::HashMap;
// use hash_hasher::HashedMap as HashMap;
use bumpalo::Bump;
// use rustc_hash::FxHashMap as HashMap;
use itertools::Itertools;
use crate::generate_rules_matching;

// use ahash::AHashMap as HashMap;
use super::{Bv, expr::Expr, config::{Config, Rule}};

pub struct Algo<'a, const N: usize>   {
    config: Config<N>,
    exprs: Vec<Vec<(&'a Expr<'a>, Bv<N>)>>,
    pub map: HashMap<Bv<N>, Option<&'a Expr<'a>> >,
    args: Vec<Bv<N>>,
    dag_size: bool,
    bump: &'a Bump,
    counter2: usize,
}

impl<'a, const N: usize> crate::enumerate::Algo<'a, N> for Algo<'a, N>  {
    fn len(&self) -> usize {
        self.exprs.iter().map(|x| x.len()).sum()
    }
    fn count(&self) -> usize {
        self.map.len()
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


impl<'a, const N: usize> Algo<'a, N>  {
    pub fn new(config: Config<N>, args: Vec<Bv<N>>, output: Bv<N>, dag_size: bool, bump: &'a Bump) -> Self {
        let mut map = HashMap::with_capacity_and_hasher(2000000, Default::default());
        map.insert(output, None);
        Algo { config, exprs: vec![Vec::new()], map, dag_size, args, bump, counter2: 0 }
    }

    pub fn run_until(&mut self, mut f: impl FnMut(&Self, &Expr<'a>, Bv<N>) -> Result<(), ()>) -> Result<(), ()> {
        
        
        // let xh = oexpr!(>> 0 [32]);
        // let yh = oexpr!(>> 1 [32]);
        // let xl = oexpr!(& 0 [0xFFFFFFFF]);
        // let yl = oexpr!(& 1 [0xFFFFFFFF]);
        // let ll = oexpr!(* xl yl);
        // let hl = oexpr!(* xh yl);
        // let lh = oexpr!(* xl yh);
        // let hh = oexpr!(* xh yh);
        // let left = oexpr!(>> (+ hl (>> (* xl 1) [32])) [32]);
        // let right = oexpr!(+ ll (>> (+ lh (>> ll [32])) [32]));
        let args = self.args.clone();
        let bump = self.bump;
        loop {
            let mut exprs = Vec::new();
            let size = self.exprs.len();
            for rule in self.config.iter() {
                macro_rules! insert {
                    ($e:expr, $v:expr) => {
                        // if left.eval_bv(&*args) == $v {
                        //     println!("left {}", $e);
                        // } else if right.eval_bv(&*args) == $v {
                        //     println!("right {}", $e);
                        // }
                        self.counter2 += 1;
                        match self.map.try_insert($v, Some($e)) {
                            Ok(_) => { let e : &'a Expr<'a> = $e; f(self, e, $v)?; exprs.push((e, $v)); }
                            Err(err) => { if err.entry.get().is_none() { f(self, $e, $v)?; } }
                        }
                    };
                    ($e:expr, $v:expr, $map:expr, Option) => {
                        match $map.try_insert($v, Some($e)) {
                            Ok(_) => { let e : &'a Expr<'a> = $e; f(self, e, $v)?; exprs.push((e, $v)); }
                            Err(err) => { if err.entry.get().is_none() { f(self, $e, $v)?; } }
                        }
                    };
                }
                macro_rules! generate_op {
                    (op1, $e:expr, $v:expr) => {
                        let last = &self.exprs[size - 1];
                        for (e1, v1) in last {
                            let v = $v(*v1);
                            insert!(bump.alloc($e(e1)), v);
                        }
                    };
                    (op1_constant, $e:expr, $v:expr, $c:expr) => {
                        let last = &self.exprs[size - 1];
                        for (e1, v1) in last {
                            let cv = Bv([$c; N].into());
                            let v = $v(*v1, cv);
                            insert!(bump.alloc($e(e1, bump.alloc(Expr::Const($c)))), v);
                        }
                    };
                    (op2_sym, $e:expr, $v:expr) => {
                        for i in 1..(size - 1).div_ceil(2) {
                            for (e1, v1) in self.exprs[i].iter() {
                                for (e2, v2) in self.exprs[size - 1 - i].iter() {
                                    let v = $v(*v1, *v2);
                                    insert!(bump.alloc($e(e1, e2)), v);
                                }
                            }
                        }
                        if (size - 1) % 2 == 0 {
                            for ((e1, v1), (e2, v2)) in self.exprs[(size - 1) / 2].iter().combinations() {
                                let v = $v(*v1, *v2);
                                insert!(bump.alloc($e(e1, e2)), v);
                            }
                            for (e1, v1) in self.exprs[(size - 1) / 2].iter() {
                                let v = $v(*v1, *v1);
                                insert!(bump.alloc($e(e1, e1)), v);
                            }
                        }
                        if size > 3 && self.dag_size {
                            for (e1, v1) in self.exprs[size - 2].iter() {
                                let e1: &'a Expr<'a> = *e1;
                                let selfmap = unsafe { you_can::borrow_unchecked(&mut self.map) };
                                e1.for_each_subexpr(&mut |e2, v2| {
                                    let v = $v(*v1, v2);
                                    insert!(bump.alloc($e(e1, e2)), v, selfmap, Option);
                                    Ok(())
                                }, &*args)?;
                                let v = $v(*v1, *v1);
                                insert!(bump.alloc($e(e1, e1)), v);
                            }
                        }
                    };
                    (op2, $e:expr, $v:expr) => {
                        if rule.is_shift() {
                            let l = if *rule == Rule::LShr { 1 } else if *rule == Rule::Shl { 2 } else { 3 };
                            if size > l {
                                for (e2, v2) in self.exprs[1].iter() {
                                    if let Expr::Var(_) | Expr::Const(_) = e2 {
                                        for (e1, v1) in self.exprs[size - l].iter() {
                                            let v = $v(*v1, *v2);
                                            insert!(bump.alloc($e(e1, e2)), v);
                                        }
                                    }
                                }
                            }
                            continue;
                        }
                        if size > 2 {
                            for i in 1..(size - 2) {
                                for (e2, v2) in self.exprs[size - 2 - i].iter() {
                                    for (e1, v1) in self.exprs[i].iter() {
                                        let v = $v(*v1, *v2);
                                        insert!(bump.alloc($e(e1, e2)), v);
                                    }
                                }
                            }
                        }
                        if size > 3 && self.dag_size {
                            for (e1, v1) in self.exprs[size - 2].iter() {
                                let e1: &'a Expr<'a> = *e1;
                                let selfmap = unsafe { you_can::borrow_unchecked(&mut self.map) };
                                e1.for_each_subexpr(&mut |e2, v2| {
                                    let v = $v(*v1, v2);
                                    insert!(bump.alloc($e(e1, e2)), v, selfmap, Option);
                                    let v = $v(v2, *v1);
                                    insert!(bump.alloc($e(e2, e1)), v, selfmap, Option);
                                    Ok(())
                                }, &*args)?;
                                let v = $v(*v1, *v1);
                                insert!(bump.alloc($e(e1, e1)), v);
                            }
                        }
                    };
                    (op2_nonzero, $e:expr, $v:expr) => {
                        let l = if (Rule::SDiv == *rule) || (Rule::URem == *rule) || (Rule::SRem == *rule) { 4 } else { 1 };
                        if size > l {
                            for i in 1..(size - l) {
                                for (e1, v1) in self.exprs[i].iter() {
                                    for (e2, v2) in self.exprs[size - l - i].iter() {
                                        if v2.any_eq(& Bv::<N>::ZEROS) { continue; }
                                        let v = $v(*v1, *v2);
                                        insert!(bump.alloc($e(e1, e2)), v);
                                    }
                                }
                            }
                        }
                        if size > 5 && self.dag_size {
                            for (e1, v1) in self.exprs[size - 4].iter() {
                                let e1: &'a Expr<'a> = *e1;
                                let selfmap = unsafe { you_can::borrow_unchecked(&mut self.map) };
                                e1.for_each_subexpr(&mut |e2, v2| {
                                    if v2.any_eq(& Bv::<N>::ZEROS) { return Ok(()); }
                                    let v = $v(*v1, v2);
                                    insert!(bump.alloc($e(e1, e2)), v, selfmap, Option);
                                    Ok(())
                                }, &*args)?;
                                if v1.any_eq(& Bv::<N>::ZEROS) { continue }
                                let v = $v(*v1, *v1);
                                insert!(bump.alloc($e(e1, e1)), v);
                            }
                        }
                    };
                    (var, $id:expr) => {
                        if size == 1 {
                            let v = self.args[*$id];
                            insert!(bump.alloc(Expr::Var(*$id)), v);
                        }
                    };
                    (chatgpt, $e:expr, $v:expr) => {
                        if size == *$v {
                            let expr = Expr::from_owned(bump, $e);
                            insert!(expr, expr.eval_bv(&*args));
                        }
                    };
                    (const, $v:expr) => {
                        if size == 1 {
                            let v = Bv([*$v; N].into());
                            insert!(bump.alloc(Expr::Const(*$v)), v);
                        }
                    };
                    (ite) => {};
                }
                generate_rules_matching!(rule);
            }
            self.exprs.push(exprs);
        }
    }
}


pub fn dynamic_usage_for_capacity<K, V>(cap: usize) -> usize {
    // The bucket calculation is sourced from here:
    //   https://github.com/rust-lang/hashbrown/blob/dbd6dbe30a4076c0ea65ca5bd57036c27f3cc7c9/src/raw/mod.rs#L187-L216
    //
    // hashbrown's RawTable::buckets is not accessible via the std HashMap, and
    // HashMap::capacity is a lower bound. However, hashbrown has an invariant that
    // the number of buckets is a power of two, so usually we'll calculate the correct
    // memory usage, and occasionally we'll undercount by around a factor of two.
    let width: usize = std::mem::size_of::<core::arch::x86_64::__m128i>();
    let buckets = {
        if cap < 8 {
            return if cap < 4 { 4 } else { 8 };
        }
        let adjusted_cap = (cap * 8) / 7;
        adjusted_cap.next_power_of_two()
    };

    // The memory usage calculation is sourced from here:
    //   https://github.com/rust-lang/hashbrown/blob/dbd6dbe30a4076c0ea65ca5bd57036c27f3cc7c9/src/raw/mod.rs#L240-L265

    let layout = Layout::new::<(K, V)>();
    let size = layout.size();
    let ctrl_align = usize::max(layout.align(), width);

    let ctrl_offset = (size * buckets + ctrl_align - 1) & !(ctrl_align - 1);
    ctrl_offset + buckets + width
}