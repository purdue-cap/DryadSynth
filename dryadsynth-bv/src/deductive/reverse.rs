
use bumpalo::Bump;

use crate::enumerate::{Algo, Bv, expr::Expr};

pub struct ReverseRule {
    pub add: bool,
    pub xor: bool,
}

impl ReverseRule {
    pub fn new() -> Self {
        Self { add: true, xor: true }
    }

    #[inline(always)]
    pub fn reverse<'a, const N: usize>(&self, algo: &impl Algo<'a, N>, e: &Expr<'a>, v: &Bv<N>, output: &Bv<N>, bump: &'a Bump) -> Option<&'a Expr<'a>>  {
        if self.add {
            if let Some(Some(p)) = algo.map().get(&(*output - *v).into()) {
                return Some(bump.alloc(Expr::Add(bump.alloc(e.clone()), p)))
            }
        }
        if self.xor {
            if let Some(Some(p)) = algo.map().get(&(*output ^ *v).into()) {
                return Some(bump.alloc(Expr::Xor(bump.alloc(e.clone()), p)))
            }
        }
        None
    }
}