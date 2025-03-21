use std::{future::Future, time::Duration, sync::mpsc::{self, RecvTimeoutError}, thread};

use extension_trait::extension_trait;
use futures::FutureExt;
use itertools::Itertools;
use tokio::task::JoinHandle;

use crate::log;

pub struct BitIter(u64);

impl Iterator for BitIter {
    type Item = bool;

    fn next(&mut self) -> Option<Self::Item> {
        if self.0 == 0 {
            None
        } else {
            let b = self.0 & 1;
            self.0 = self.0 >> 1;
            if b == 0 { Some(false) } else { Some(true) }
        }
    }
}
pub struct OneIter(u64);

impl Iterator for OneIter {
    type Item = u64;

    fn next(&mut self) -> Option<Self::Item> {
        let a = self.0.trailing_zeros();
        if a == 64 {
            None
        } else {
            self.0 = self.0 & !(1 << a);
            Some(1 << a)
        }
    }
}
pub struct OnePosIter(u64);

impl Iterator for OnePosIter {
    type Item = u32;

    fn next(&mut self) -> Option<Self::Item> {
        let a = self.0.trailing_zeros();
        self.0 = self.0 & !(1 << a);
        if a == 64 { None } else { Some(a) }
    }
}

pub struct TSubsetIter(u64, u64);

impl TSubsetIter {
    pub fn new(i: u64) -> Self {
        Self(i, 0)
    }
}
pub struct SubsetIter(u64, u64);

impl SubsetIter {
    pub fn new(i: u64) -> Self {
        Self(i, 0)
    }
}

impl Iterator for TSubsetIter {
    type Item = u64;

    fn next(&mut self) -> Option<Self::Item> {
        for i in self.0.one_iter() {
            if i & self.1 == 0 {
                self.1 = self.1 & !(i - 1) | i;
                if self.1 == self.0 {
                    return None;
                }
                return Some(self.1);
            }
            
        }
        
        return None;
    }
}
impl Iterator for SubsetIter {
    type Item = u64;

    fn next(&mut self) -> Option<Self::Item> {
        for i in self.0.one_iter() {
            if i & self.1 == 0 {
                self.1 = self.1 & !(i - 1) | i;
                return Some(self.1);
            }
        }
        return None;
    }
}

pub struct CombIter(u64);

impl Iterator for CombIter {
    type Item = u64;

    fn next(&mut self) -> Option<Self::Item> {
        let cur = self.0;
        let to = self.0.trailing_ones();
        let noto = self.0 & !((1 << to) - 1);
        let fo = noto.trailing_zeros();
        if fo == 64 {
            if self.0 != 0 {
                self.0 = 0;
                return Some(cur);
            } else { return None; }
        }
        self.0 = self.0 & !u64::bits(0, fo + 1) | u64::bits(fo - to - 1, fo);
        Some(cur)
    }
}

impl CombIter {
    pub fn new(p: u32, n: u32) -> Self {
        assert!(n >= p);
        Self(u64::bits(n - p, n))
    }
}





#[extension_trait]
pub impl BitSetU64Ext for u64 {
    fn from_bits(mut iter: impl Iterator<Item = bool>) -> u64 {
        let mut result = 0;
        for (i, p) in iter.enumerate() {
            if p { result |= 1 << i; }
        }
        result
    }
    fn bit_iter(self) -> BitIter {
        BitIter(self)
    }
    fn one_iter(self) -> OneIter {
        OneIter(self)
    }
    fn one_pos_iter(self) -> OnePosIter {
        OnePosIter(self)
    }
    fn tsubset_iter(self) -> TSubsetIter {
        TSubsetIter::new(self)
    }
    fn subset_iter(self) -> SubsetIter {
        SubsetIter::new(self)
    }
    fn split_iter(self) -> impl Iterator<Item=(u64, u64)> {
        self.subset_iter().flat_map(move |i|{
            i.subset_iter().chain([0u64].into_iter()).map(move |j| {
                (i, (self & !i) | j)
            })
        })
    }
    fn pos_of_subset(self, other: u64) -> u64 {
        let mut res = 0u64;
        for (i, p) in self.one_iter().enumerate() {
            if p & other != 0 {
                res |= 1 << i;
            }
        }
        return res;
    }
    fn subset_of_pos(self, other: u64) -> u64 {
        let mut res = 0u64;
        for (i, p) in self.one_iter().zip(other.bit_iter()) {
            if p { res |= i }
        }
        return res;
    }
    fn bits(a: u32, b: u32) -> Self {
        ((1 << b) - 1) & !((1 << a) - 1)
    }
    fn fact(self) -> u64 {
        let mut f = 1;
        for i in 1..=self {
            f *= i;
        }
        f
    }
    fn combs(self, r: u64) -> u64 {
        self.fact() / (r.fact() * (self - r).fact())
    }    
    fn comb_iter(self, r: u64) -> CombIter {
        CombIter::new(r as u32, self as u32)
    }    
}

#[cfg(test)]
mod tests {
    use itertools::Itertools;

    use super::BitSetU64Ext;

    #[test]
    fn test_a() {
        for p in [7u64, 9, 13, 53897] {
            let v = p.tsubset_iter().collect_vec();
            println!("{:?}", v);
            assert!(v.len() == (1 << p.count_ones()) - 2);
        }
        for p in [7u64, 9, 13, 53897] {
            let v = p.subset_iter().collect_vec();
            println!("{:?}", v);
            assert!(v.len() == (1 << p.count_ones()) - 1);
        }
        
        let a = 0x17.split_iter().collect_vec();
        println!("{:?}", a);
        
    }
    #[test]
    fn test_b() {
        for (r, n) in [(2u64, 6u64), (7, 12), (8, 15) ] {
            let v = n.comb_iter(r).collect_vec();
            assert_eq!(v.len(), n.combs(r) as usize);
        }
    }
}

pub fn run_with_timeout<T: Send + 'static>(duration: u64, f: impl FnOnce() -> T + Send + 'static) -> Option<T> {
    let (tx, rx) = mpsc::channel();
    let l = log::log_level();
    let _ = thread::spawn(move || {
        log::set_log_level(l);
        let result = f();
        match tx.send(result) {
            Ok(()) => {} // everything good
            Err(_) => {} // we have been released, don't panic
        }
    });

    match rx.recv_timeout(Duration::from_secs(duration)) {
        Ok(result) => Some(result),
        Err(RecvTimeoutError::Timeout) => None,
        Err(RecvTimeoutError::Disconnected) => unreachable!(),
    }
}
pub fn spawn<T: Send + 'static>(f: impl FnOnce() -> T + Send + 'static) -> JoinHandle<T> {
    let l = log::log_level();
    tokio::task::spawn_blocking(move ||{ 
        log::set_log_level(l);
        f()
    })
}