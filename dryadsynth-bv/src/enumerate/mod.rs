use std::array;
use iter_fixed::{IntoIteratorFixed, IteratorFixed};
// use fnv::FnvHashMap as HashMap;
use std::collections::HashMap;

use derive_more::{From, Deref, DebugCustom};

use self::expr::Expr;

pub mod expr;
pub mod config;
pub mod algo;
pub mod algo2;
pub mod algo3;
pub mod algo4;
pub mod macros;
pub mod op;
pub mod utils;
pub mod store;

#[repr(C, align(32))]
#[derive(From, Deref, DebugCustom, Clone, Copy, PartialEq, Eq, Hash)]
#[debug(fmt = "{:x}", *_0)]
pub struct Bv<const N: usize> (pub [u64; N]);

impl<const N: usize> Bv<N> {
    pub fn into_iter_fixed(self) -> IteratorFixed<array::IntoIter<u64, N>, N> {
        self.0.into_iter_fixed()
    }
}

impl<const N: usize> std::ops::Not for Bv<N> {
    type Output = Bv<N>;

    fn not(self) -> Self::Output {
        op::bv::not(self)
    }
}
impl<const N: usize> std::ops::BitAnd for Bv<N> {
    type Output = Bv<N>;

    fn bitand(self, rhs: Self) -> Self::Output {
        op::bv::and(self, rhs)
    }
}
impl<const N: usize> std::ops::BitOr for Bv<N> {
    type Output = Bv<N>;

    fn bitor(self, rhs: Self) -> Self::Output {
        op::bv::or(self, rhs)
    }
}
impl<const N: usize> std::ops::BitXor for Bv<N> {
    type Output = Bv<N>;

    fn bitxor(self, rhs: Self) -> Self::Output {
        op::bv::xor(self, rhs)
    }
}
impl<const N: usize> std::ops::Sub for Bv<N> {
    type Output = Bv<N>;

    fn sub(self, rhs: Self) -> Self::Output {
        op::bv::sub(self, rhs)
    }
}

impl<const N: usize> Bv<N>  {
    pub const ONES : Self = Bv([u64::MAX; N]);
    pub const ZEROS : Self = Bv([u64::MIN; N]);
    pub fn to_int(self) -> [i64; N] {
        self.map(|p| p as i64)
    }
    pub fn from(iter: impl Iterator<Item=u64>) -> Self {
        let mut vec: Vec<_> = iter.take(N).collect();
        vec.resize(N, 0);
        let a : [u64; N] = vec.try_into().unwrap();
        Self(a.into())
    }
    pub fn count(&self) -> u32 {
        self.0.iter().map(|x| x.count_ones()).sum()
    }
    pub fn count_zeros(&self) -> u32 {
        self.0.iter().map(|x| x.count_zeros()).sum()
    }
    
    pub fn imples(&self, other: &Self) -> bool {
        self.iter().zip(other.iter()).all(|(a, b)| !a & b == 0)
    }
    
    pub fn any_eq(&self, other: &Self) -> bool {
        self.iter().zip(other.iter()).any(|(a, b)| a == b)
    }
}



#[cfg(test)]
mod tests {
    use std::simd::Simd;

    #[test]
    fn test_unchecked() {
        let a : Simd<_, 2> = [i64::MIN; 2].into();
        println!("{:?}", a + a);
    }
}


pub trait Algo<'a, const N: usize> {
    fn len(&self) -> usize;
    fn count(&self) -> usize;
    fn count2(&self) -> usize;
    fn size(&self) -> usize;
    fn map_size(&self) -> usize;
    fn expr_size(&self) -> usize;
    fn map(&self) -> & HashMap<Bv<N>, Option<&'a Expr<'a>>>;
}