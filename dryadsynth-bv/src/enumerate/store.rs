
use super::{Bv, expr::Expr};



#[derive(Clone)]
pub struct Store<'a, const N: usize>(Vec<Vec<(&'a Expr<'a>, Bv<N>)>>);

impl<'a, const N: usize> Store<'a, N> {
    pub fn new(mask: u64) -> Self {
        let r = 0..(1 << mask.count_ones());
        Self(r.map(|_| Vec::new()).collect())
    }

    pub fn set(&self, s: u64) -> &Vec<(&'a Expr<'a>, Bv<N>)> {
        &self.0[s as usize]
    }
    pub fn append(&mut self, s: u64, r : Vec<(&'a Expr<'a>, Bv<N>)>) {
        self.0[s as usize] = r;
    }
    pub fn len(&self) -> usize {
        self.0.iter().map(|x| x.len()).sum()
    }
    

}





