use std::{cell::RefCell, borrow::{Borrow, BorrowMut}};

use bumpalo::Bump;

use crate::{bv::expr::BvExpr, utils::BoxSliceExt, utils::{self}, debg, parsing::sexpr::SExpr};




pub enum SubProblem<'a> {
    Unsolved(Bits, f32),
    Accept(usize),
    Ite{ expr: usize, entropy: f32, t: SubProb<'a>, f: SubProb<'a> }
}

impl<'a> SubProblem<'a> {
    #[inline]
    pub fn add_subproblems(&self, subproblem: &mut Vec<(SubProb<'a>, bool)>) {
        if let SubProblem::Ite { expr, entropy, t, f } = self {
            subproblem.push((f, true));
            subproblem.push((t, true));
        }
    }
}

pub type Bits = Box<[u128]>;
pub type SubProb<'a> = &'a RefCell<SubProblem<'a>>;

pub struct TreeLearning<'a> {
    pub size: usize,
    root: SubProb<'a>,
    subproblems: Vec<SubProb<'a>>,
    pub conditions: Vec<(BvExpr, Bits)>,
    pub options: Vec<(BvExpr, Bits)>,
    pub bump: &'a Bump,
}

pub enum SelectResult {
    Accept(usize),
    Ite(usize, f32, (Bits, f32), (Bits, f32)),
    // Failed,
}


impl<'a> TreeLearning<'a> {
    // pub fn split_infomation(bits: Bits) -> f32 {

    // }
    pub fn new_in(size: usize, options: Vec<(BvExpr, Bits)>, bump: &'a Bump) -> Self {
        let mut this = Self {
            size,
            root: bump.alloc(RefCell::new(SubProblem::Unsolved(utils::boxed_ones(size), 0.0))),
            subproblems: Vec::new(),
            conditions: Vec::new(),
            options: options,
            bump,
        };
        let root_entro = this.entropy(& utils::boxed_ones(size));
        if let SubProblem::Unsolved(a, entropy) = &mut *this.root.borrow_mut() {
            *entropy = root_entro;
        }
        this.subproblems.push(this.root);
        this
    }

    #[inline]
    pub fn entropy(&self, bits: &Bits) -> f32 {
        
        let mut vec: Vec<_> = self.options.iter().enumerate().map(|(i, b)| {
            let mut res = b.1.clone();
            res.conjunction_assign(&*bits);
            (i, res.count_ones(), res)
        }).collect();
        vec.sort_by_key(|a| u32::MAX - a.1);

        let total = bits.count_ones();
        let mut rest = bits.clone();
        let mut rest_count = rest.count_ones();
        let mut res = 0.0;
        for (_, _, b) in vec {
            rest.difference_assign(&b);
            let count = rest_count - rest.count_ones();
            let p = count as f32 / total as f32;
            if p > 0.0 {
                res += - p * p.log2();
            }
            rest_count = rest.count_ones();
        }
        res
    }
    
    pub fn cond_entropy(&self, bits: &Bits, condition: &Bits) -> (f32, (Bits, f32), (Bits, f32)) {
        let total = bits.count_ones();
        let mut and_bits = bits.clone();
        and_bits.conjunction_assign(&condition);
        let and_entro = self.entropy(&and_bits);
        let and_count = and_bits.count_ones();
        let mut diff_bits = bits.clone();
        diff_bits.difference_assign(&condition);
        let diff_entro = self.entropy(&diff_bits);
        let diff_count = diff_bits.count_ones();
        if and_count == 0 || diff_count == 0 {
            (1e10, (and_bits, and_entro), (diff_bits, diff_entro))
        } else {
            (
                (and_entro * and_count as f32 + diff_entro * diff_count as f32) / total as f32,
                (and_bits, and_entro), (diff_bits, diff_entro)
            )
        }
    }
    
    #[inline]
    pub fn select(&self, unsolved: &SubProblem<'a>) -> SelectResult {
        if let SubProblem::Unsolved(bits, entro) = unsolved {
            if *entro <= 0.0001 {
                if let Some((i, _)) = self.options.iter().enumerate().find(|(_, x)| bits.subset(&x.1) ) {
                    return SelectResult::Accept(i)
                }
            }
            let (i, (centro, tb, fb)) = self.conditions.iter().enumerate()
                .map(|(i, (e, cb))| {
                    let ce = self.cond_entropy(bits, cb);
                    (i, ce)
                })
                .min_by(|a, b| a.1.0.partial_cmp(&b.1.0).unwrap())
                .expect("At least have one condition.");
            SelectResult::Ite(i, centro, tb, fb)
        } else { panic!("last should be unsolved.") }
    }

    pub fn run(&mut self) {
        let mut counter = 0;
        while let Some(last) = self.subproblems.pop() {
            // if counter >= 1 {break;}

            counter += 1;
            let sel = self.select(&*last.borrow());
            match sel {
                SelectResult::Accept(i) => {
                    *last.borrow_mut() = SubProblem::Accept(i);
                }
                SelectResult::Ite(expr, entropy, t, f) => {
                    let tb = self.bump.alloc(SubProblem::Unsolved(t.0, t.1).into());
                    let fb = self.bump.alloc(SubProblem::Unsolved(f.0, f.1).into());
                    self.subproblems.push(fb);
                    self.subproblems.push(tb);
                    *last.borrow_mut() = SubProblem::Ite{ expr, entropy, t: tb, f: fb };
                }
            }
        }
    }

    fn fmt_recursive(&self, f: &mut std::fmt::Formatter<'_>, node: SubProb<'a>, indent: &mut String) -> std::fmt::Result {
        match &*node.borrow() {
            SubProblem::Unsolved(bits, entropy) => 
                writeln!(f, "{indent}?? {} {:x?}", entropy, bits),
            SubProblem::Accept(i) => 
                writeln!(f, "{indent}{:?}", self.options[*i].0),
            SubProblem::Ite { expr, entropy, t: tb, f: fb } => {
                writeln!(f, "{indent}ite {:?} {:x?}", self.conditions[*expr].0, self.conditions[*expr].1)?;
                indent.push_str("  ");
                self.fmt_recursive(f, tb, indent)?;
                self.fmt_recursive(f, fb, indent)?;
                indent.pop(); indent.pop();
                Ok(())
            }
        }
    }
    fn size_recursive(&self, node: SubProb<'a>) -> usize {
        match &*node.borrow() {
            SubProblem::Unsolved(bits, entropy) => 1,
            SubProblem::Accept(i) => 1,
            SubProblem::Ite { expr, entropy, t: tb, f: fb } => 1 + self.size_recursive(tb) + self.size_recursive(fb),
        }
    }
    fn cover_recursive(&self, node: SubProb<'a>) -> Bits {
        match &*node.borrow() {
            SubProblem::Unsolved(bits, entropy) => bits.clone(),
            SubProblem::Accept(i) => self.options[*i].1.clone(),
            SubProblem::Ite { expr, entropy, t: tb, f: fb } => {
                let mut t = self.cover_recursive(tb);
                let mut f = self.cover_recursive(fb);
                let bits = self.conditions[*expr].1.clone();
                t.conjunction_assign(&bits);
                f.difference_assign(&bits);
                t.union_assign(&f);
                t
            }
        }
    }
    fn bvexpr_recursizve(&self, node: SubProb<'a>) -> BvExpr {
        match &*node.borrow() {
            SubProblem::Unsolved(bits, entropy) => panic!("Still subproblem remain."),
            SubProblem::Accept(i) => self.options[*i].0.clone(),
            SubProblem::Ite { expr, entropy, t: tb, f: fb } => {
                let t = self.bvexpr_recursizve(tb);
                let f = self.bvexpr_recursizve(fb);
                let cond = self.conditions[*expr].0.clone();
                BvExpr::Ite(cond.into(), t.into(), f.into())
            }
        }
    }
    pub fn bvexpr(&self) -> BvExpr {
        self.bvexpr_recursizve(self.root)
    }
    
    pub fn result_size(&self) -> usize {
        self.size_recursive(self.root)
    }
}

impl<'a> std::fmt::Debug for TreeLearning<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        self.fmt_recursive(f, self.root, &mut "".into())
    }
}






