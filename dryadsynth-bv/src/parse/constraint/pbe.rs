use std::simd::{LaneCount, SupportedLaneCount};
use itertools::Itertools;
use rand::{rngs::StdRng, Rng};
use sdset::SetBuf;
use smallvec::smallvec;

use derive_more::DebugCustom;
use smallvec::SmallVec;

use crate::enumerate::{Bv, expr::{Expr, OwnedExpr}};

use super::super::{Error, sexpr::SExpr, new_costom_error_pos, new_custom_error_span, literals};



#[derive(DebugCustom, Clone)]
#[debug(fmt = "{:#x?} -> {:#x?}", input, output)]
pub struct PbeConstraint {
    input: Vec<Vec<u64>>,
    output: Vec<u64>
}

impl PbeConstraint {
    pub fn new(args: usize) -> Self {
        Self{ input: (0..args).map(|_| Vec::new()).collect(), output: Vec::new() }
    }
    pub fn len(&self) -> usize {
        self.output.len()
    }
    pub fn args_count(&self) -> usize {
        self.input.len()
    }
    pub fn parse(func: &str, arity: usize, sexpr: &SExpr) -> Result<Self, Error> {
        let mut input: Vec<Vec<u64>> = (0..arity).map(|_| Vec::new()).collect();
        let mut output = Vec::new();
        for e in sexpr.list().expect("Toplevel should be a list.") {
            if let Some(("constraint", [value])) = e.call() {
                if let Some(("=", [l, r])) = value.call() {
                    if let Some((name, args)) = l.call() {
                        if name == func && args.len() == arity {
                            for ((i, arg), vec) in args.iter().enumerate().zip(input.iter_mut()) {
                                if let SExpr::Const(s, span) = arg {
                                    let a = literals::u64_constant(s, span.clone())?;
                                    vec.push(a);
                                } else {return Err(new_custom_error_span(format!("PBEConstraint: Expecting Constant for input. {}", arg).into(), r.span().clone()))}
                            }
                            
                            if let SExpr::Const(s, span) = r {
                                let a = literals::u64_constant(s, span.clone())?;
                                output.push(a);
                            } else {return Err(new_custom_error_span("PBEConstraint: Expecting Constant for output.".into(), r.span().clone()))}
                        } else {} // {return Err(new_custom_error_span("PBEConstraint: Function Name/Arity not match.".into(), l.span().clone()))}
                    } else {return Err(new_custom_error_span("PBEConstraint: Expecting Input Examples.".into(), l.span().clone()));}
                } else {return Err(new_custom_error_span("PBEConstraint: Expecting Input-Output Examples (= (<func> <arg1> ...) <output>).".into(), value.span().clone()));}
            }
        }
        Ok(PbeConstraint { input, output })
    }
    
    pub fn bv<const N: usize>(&self) -> (Vec<Bv<N>>, Bv<N>)  {
        (self.input.iter().map(|x| Bv::from(x.iter().cloned()) ).collect(), Bv::from(self.output.iter().cloned()))
    }
    pub fn bv_from_slice<const N: usize>(&self, iter: &[usize; N]) -> (Vec<Bv<N>>, Bv<N>)  {
        (self.input.iter().map(|x| Bv(iter.map(|i| x[i]).into()) ).collect(), Bv(iter.map(|i| self.output[i]).into()))
    }
    pub fn args(&self, i: usize) -> SmallVec<[u64; 4]> {
        self.input.iter().map(|x| x[i]).collect()
    }
    pub fn test(&self, expr: &Expr) -> sdset::SetBuf<usize> {
        SetBuf::new_unchecked((0..self.len()).into_iter().flat_map(|i| {
            if expr.eval(self.args(i).as_slice()).map(|x| x == self.output[i]).unwrap_or(false) {
                Some(i)
            } else { None }
        }).collect())
    }
    pub fn test_owned(&self, expr: &OwnedExpr) -> sdset::SetBuf<usize> {
        SetBuf::new_unchecked((0..self.len()).into_iter().flat_map(|i| {
            if expr.eval(self.args(i).as_slice()).map(|x| x == self.output[i]).unwrap_or(false) {
                Some(i)
            } else { None }
        }).collect())
    }
    pub fn test_and(&self, expr: &Expr) -> bool {
        (0..self.len()).into_iter().all(|i| {
            expr.eval(self.args(i).as_slice()).map(|x| self.output[i] & !x == 0).unwrap_or(false)
        })
    }
    pub fn test_or(&self, expr: &Expr) -> bool {
        (0..self.len()).into_iter().all(|i| {
            expr.eval(self.args(i).as_slice()).map(|x| !self.output[i] & x == 0).unwrap_or(false)
        })
    }

    pub fn test_on(&self, expr: &Expr, iter: impl Iterator<Item= usize>) -> sdset::SetBuf<usize> {
        SetBuf::new_unchecked(iter.into_iter().flat_map(#[inline(always)] |i| {
            if expr.eval(self.args(i).as_slice()).map(|x| x == self.output[i]).unwrap_or(false) {
                Some(i)
            } else { None }
        }).collect())
    }
    pub fn set_output(mut self, value: u64) -> Self {
        self.output.iter_mut().for_each(|x| *x = value);
        self
    }
    pub fn add_example(&mut self, input: &[u64], output: u64) {
        self.input.iter_mut().zip(input.iter()).for_each(|(x, p)| x.push(*p));
        self.output.push(output)
    }
    pub fn random_example(&mut self, rng: &mut StdRng, mask: u64, expr: &OwnedExpr, len: usize) {
        loop {
            let mut args = (0..len).map(|_| rng.gen::<u64>() & mask).collect_vec();
            if let Ok(a) = expr.eval(&*args) {
                self.add_example(&args, a);
                break;
            }
        }
    }
    pub fn fmt(&self, arity: usize) -> String {
        let iter = (0..self.output.len()).map(|x| ((0..arity).map(|i| self.input[i][x]).collect_vec(), self.output[x]));
        let mut siter = iter.map(|(v, o)| format!("({}) -> 0b{:064b}", v.iter().map(|j| format!("0b{:064b}", j)).join(", "), o));
        format!("Here are some examples:\n{}", siter.join("\n"))
    }
}

#[cfg(test)]
mod tests {
    use crate::parse::sexpr::SExpr;

    use super::PbeConstraint;

    #[test]
    fn test_constraint() {
        let e = SExpr::parse(" (constraint (= (f #x1 #x2) #x3))
            (constraint (= (f #x1 #x2) #x3))
        ").unwrap();
        let contraint = PbeConstraint::parse(&"f", 2, &e).unwrap();
        println!("{:?}", contraint);
    }
}