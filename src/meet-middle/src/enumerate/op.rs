
use crate::parse::deffun;

use super::expr::DivideByZero;

#[inline(always)]
pub fn not(v1: u64) -> u64 { !v1 }
#[inline(always)]
pub fn neg(v1: u64) -> u64 { unsafe { (0i64.unchecked_sub(v1 as i64)) as u64 } }
#[inline(always)]
pub fn add(v1: u64, v2: u64) -> u64 { unsafe { v1.unchecked_add(v2) } }
#[inline(always)]
pub fn mul(v1: u64, v2: u64) -> u64 { unsafe { v1.unchecked_mul(v2) } }
#[inline(always)]
pub fn sub(v1: u64, v2: u64) -> u64 { unsafe { v1.unchecked_sub(v2) } }
#[inline(always)]
pub fn xor(v1: u64, v2: u64) -> u64 { v1 ^ v2 }
#[inline(always)]
pub fn and(v1: u64, v2: u64) -> u64 { v1 & v2 }
#[inline(always)]
pub fn or(v1: u64, v2: u64) -> u64 { v1 | v2 }
#[inline(always)]
pub fn udiv(v1: u64, v2: u64) -> Result<u64, DivideByZero> { v1.checked_div(v2).ok_or(DivideByZero) }
#[inline(always)]
pub fn urem(v1: u64, v2: u64) -> Result<u64, DivideByZero> { v1.checked_rem(v2).ok_or(DivideByZero) }

#[inline(always)]
pub fn lshr(this: u64, that: u64) -> u64 {
    if that >= 64 { 0 } else { this >> that }
}
#[inline(always)]
pub fn ashr(this: u64, that: u64) -> u64 {
    if that >= 64 { 0 } else { ((this as i64) >> (that as i64)) as u64 }
}
#[inline(always)]
pub fn shl(this: u64, that: u64) -> u64 {
    if that >= 64 { 0 } else { this << that }
}
#[inline(always)]
pub fn sdiv(this: u64, that: u64) -> Result<u64, DivideByZero>{
    if that == 0 { return Err(DivideByZero); }
    Ok((this as i64).overflowing_div(that as i64).0 as u64)
}
#[inline(always)]
pub fn srem(this: u64, that: u64) -> Result<u64, DivideByZero>{
    if that == 0 { return Err(DivideByZero); }
    Ok((this as i64).overflowing_rem(that as i64).0 as u64)
}

#[inline(always)]
pub fn fop1<const I: usize>(a: u64) -> u64 {
    deffun::ENV.apply(I, 1).eval(&[a]).unwrap()
}

#[inline(always)]
pub fn fop2<const I: usize>(a: u64, b: u64) -> Result<u64, DivideByZero>{
    deffun::ENV.apply(I, 2).eval(&[a, b])
}

#[inline(always)]
pub fn ite(v1: u64, v2: u64, v3: u64) -> u64{
    if v1 == 0 { v2 } else { v3 }
}

pub mod bv {
    use std::{simd::{LaneCount, SupportedLaneCount}, ops::{Not, Neg}};

    use iter_fixed::IteratorFixed;
    use itertools::izip;

    use crate::{enumerate::Bv, parse::deffun};


    #[inline(always)] pub fn not<const N : usize>(v1: Bv<N>) -> Bv<N> { v1.map(|a| super::not(a)).into() }
    #[inline(always)] pub fn neg<const N : usize>(v1: Bv<N>) -> Bv<N> { v1.map(|a| super::neg(a)).into() }
    #[inline(always)] pub fn add<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::add(a, b)).collect()) }
    #[inline(always)] pub fn xor<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::xor(a,b)).collect()) }
    #[inline(always)] pub fn and<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::and(a,b)).collect()) }
    #[inline(always)] pub fn or<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::or(a,b)).collect()) }
    #[inline(always)] pub fn mul<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::mul(a,b)).collect()) }
    #[inline(always)] pub fn sub<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::sub(a,b)).collect()) }
    #[inline(always)] pub fn lshr<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::lshr(a,b)).collect()) }
    #[inline(always)] pub fn shl<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::shl(a,b)).collect()) }
    #[inline(always)] pub fn udiv<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::udiv(a,b).unwrap()).collect()) }
    #[inline(always)] pub fn sdiv<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::sdiv(a,b).unwrap()).collect()) }
    #[inline(always)] pub fn urem<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::urem(a,b).unwrap()).collect()) }
    #[inline(always)] pub fn srem<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::srem(a,b).unwrap()).collect()) }
    #[inline(always)] pub fn ashr<const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).map(|(a,b)| super::ashr(a,b)).collect()) }
    #[inline(always)] pub fn ite<const N : usize>(v1: Bv<N>, v2: Bv<N>, v3: Bv<N>) -> Bv<N>  { Bv(v1.into_iter_fixed().zip(v2.into_iter_fixed()).zip(v3.into_iter_fixed()).map(|((a,b), c)| super::ite(a,b,c)).collect()) }

    #[inline(always)]
    pub fn fop1<const I: usize, const N : usize>(v1: Bv<N>) -> Bv<N> {
        deffun::ENV.apply(I, 1).eval_bv(&[v1])
    }

    #[inline(always)]
    pub fn fop2<const I: usize, const N : usize>(v1: Bv<N>, v2: Bv<N>) -> Bv<N> {
        deffun::ENV.apply(I, 2).eval_bv(&[v1, v2])
    }



}