

use bumpalo::Bump;


pub mod expr;

#[derive(Clone, Copy, PartialEq, Eq)]
pub struct Indices<'a>(pub &'a [usize]);

impl<'a> Indices<'a> {
    pub fn subset<'b>(&self, other: &Indices<'b>) -> bool {
        let mut iter1 = self.iter();
        let mut iter2 = other.iter();
        let mut last = if let Some(a) = iter2.next() { a } else { return self.len() == 0; };
        while let Some(i1) = iter1.next() {
            while i1 > last {
                last = if let Some(a) = iter2.next() { a } else { return false; };
            }
            if i1 < last { return false; }
        }
        true
    }
    #[inline(always)]
    pub fn intersect<'b: 'a>(&self, other: Indices<'b>) -> impl Iterator<Item = usize> + 'a {
        let mut iter = other.iter().cloned();
        let mut peekvalue = iter.next().unwrap_or(usize::MAX);
        self.iter().flat_map(move |x| {
            while peekvalue < *x {
                peekvalue = iter.next().unwrap_or(usize::MAX);
            }
            if peekvalue == *x {
                Some(*x)
            } else { None }
        })
    }
}

impl<'a> std::ops::Deref for Indices<'a> {
    type Target = &'a [usize];

    #[inline(always)]
    fn deref(&self) -> &Self::Target { &self.0 }
}

impl<'a> From<&'a [usize]> for Indices<'a> {
    fn from(value: &'a [usize]) -> Self { Indices(value) }
}

#[derive(Clone, Debug)]
pub struct Bv<'a> (pub &'a [u64]);

impl<'a> Bv<'a> {
    pub fn new(indices: Indices<'a>, value: &'a [u64]) -> Self {
        Self(value)
    }
    #[inline(always)]
    pub fn map(&self, ctx: & Context<'a>, mut f: impl FnMut(u64) -> u64) -> Self {
        Self(ctx.alloc_iter(self.0.iter().cloned().map(f)))
    }
}

impl<'a> std::ops::Index<usize> for Bv<'a> {
    type Output = u64;

    #[inline(always)]
    fn index(&self, index: usize) -> &Self::Output {
        debug_assert!(index < self.0.len());
        unsafe { self.0.get_unchecked(index) } 
    }
}
impl<'a> std::ops::Deref for Bv<'a> {
    type Target = &'a [u64];

    #[inline(always)]
    fn deref(&self) -> &Self::Target { &self.0 }
}

impl<'a> From<&'a [u64]> for Bv<'a> {
    fn from(value: &'a [u64]) -> Self { Bv(value) }
}

#[derive(Clone)]
pub struct Hint<'a> {
    pub indices: Indices<'a>,
    pub value: Bv<'a>,
    pub mask: Bv<'a>
}

impl<'a> Hint<'a> {
    #[inline(always)]
    pub fn iter(&self) -> impl ExactSizeIterator<Item = (usize, u64, u64)> + '_ {
        self.indices.iter().map(|&i| (i, self.value[i as usize], self.mask[i as usize]))
    }

    #[inline(always)]
    pub fn zip(&self, bv: &'a [u64]) -> impl Iterator<Item = (usize, u64, u64, u64)> + '_ {
        self.indices.iter().map(|&i| (i, self.value[i as usize], self.mask[i as usize], bv[i as usize]))
    }

    #[inline(always)]
    pub fn intersect_zip<'b: 'a>(&self, inds: Indices<'b>, bv: &'a [u64]) -> impl Iterator<Item = (usize, u64, u64, u64)> + '_ {
        self.indices.intersect(inds).map(|i| (i, self.value[i as usize], self.mask[i as usize], bv[i as usize]))
    }

}

impl<'a> std::fmt::Debug for Hint<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        if self.indices.len() < 30 {
            write!(f, "[")?;
            for (j, i) in self.indices.iter().enumerate() {
                let comma = if j == 0 { "" } else { ", " };
                write!(f, "{comma}{} {:#x} {:#x}", i, self.value[*i as usize] & self.mask[*i as usize], self.value[*i as usize] | !self.mask[*i as usize])?;
            }
            write!(f, "]")
        } else {
            write!(f, "")
        }
    }
}

impl<'a> Hint<'a> {
    pub fn new(indices: Indices<'a>, value: &'a [u64], mask: &'a [u64]) -> Self {
        Self { indices, value: value.into(), mask: mask.into()}
    }
}


pub use bumpalo::collections::Vec as BVec;

pub struct Context<'a>{
    pub len: usize,
    pub variables: Vec<Bv<'a>>,
    pub consts: Vec<Bv<'a>>,
    pub bump: &'a Bump,
    pub allexamples: Indices<'a>,
    pub noexamples: Indices<'a>,
    pub allzero: &'a [u64],
    pub allmax: &'a [u64],
}

impl<'a> Context<'a> {
    pub fn new(bump: &'a Bump, len: usize) -> Self {
        let allzero : &[u64] = bump.alloc_slice_fill_default(len);
        let allmax : &[u64] = bump.alloc_slice_fill_with(len, |_| u64::MAX);
        let allexamples = Indices(bump.alloc_slice_fill_with(len, |i| i ));
        let noexamples = Indices(bump.alloc([]));
        Self {
            len,
            variables: Vec::new(),
            consts: Vec::new(),
            bump,
            allexamples, noexamples, allzero, allmax
        }
    }
    #[inline(always)]
    pub fn alloc<T>(&self, a: T) -> &'a T {
        self.bump.alloc(a)
    }
    #[inline(always)]
    pub fn alloc_iter<T>(&self, mut iter: impl ExactSizeIterator<Item = T>) -> &'a [T] {
        self.bump.alloc_slice_fill_iter(iter)
    }
    #[inline(always)]
    pub fn alloc_bv(&self, mut iter: impl ExactSizeIterator<Item = u64>) -> Bv<'a> {
        debug_assert!(iter.len() == self.len);
        Bv(self.bump.alloc_slice_fill_iter(iter))
    }
    #[inline(always)]
    pub fn alloc_zeros(&self) -> &'a mut [u64] {
        self.bump.alloc_slice_fill_default::<u64>(self.len)
    }
    #[inline(always)]
    pub fn collect(&self, a1: impl Iterator<Item = (usize, u64)>) -> &'a [u64] {
        let mut res = self.alloc_zeros();
        for (i, v) in a1 {
            res[i] = v;
        }
        res
    }
    #[inline(always)]
    pub fn collect2(&self, a1: impl Iterator<Item = (usize, u64, u64)>) -> (&'a [u64], &'a [u64]) {
        let mut res1 = self.alloc_zeros();
        let mut res2 = self.alloc_zeros();
        for (i, v1, v2) in a1 {
            res1[i] = v1;
            res2[i] = v2;
        }
        (res1, res2)
    }
    #[inline(always)]
    pub fn collect3(&self, a1: impl Iterator<Item = (usize, u64, u64, u64)>) -> (&'a [u64], &'a [u64], &'a [u64]) {
        let mut res1 = self.alloc_zeros();
        let mut res2 = self.alloc_zeros();
        let mut res3 = self.alloc_zeros();
        for (i, v1, v2, v3) in a1 {
            res1[i] = v1;
            res2[i] = v2;
            res3[i] = v3;
        }
        (res1, res2, res3)
    }
    #[inline(always)]
    pub fn collect_with_indices(&self, a1: impl Iterator<Item = (usize, u64)>) -> (Indices<'a>, &'a [u64]) {
        let mut res = self.alloc_zeros();
        let mut ind = self.alloc_vec();
        for (i, v) in a1 {
            res[i] = v;
            ind.push(i);
        }
        (ind.into_bump_slice().into(), res)
    }
    #[inline(always)]
    pub fn collect_with_indices2(&self, a1: impl Iterator<Item = (usize, u64, u64)>) -> (Indices<'a>, &'a [u64], &'a [u64]) {
        let mut res1 = self.alloc_zeros();
        let mut res2 = self.alloc_zeros();
        let mut ind = self.alloc_vec();
        for (i, v1, v2) in a1 {
            res1[i] = v1;
            res2[i] = v2;
            ind.push(i);
        }
        (ind.into_bump_slice().into(), res1, res2)
    }
    #[inline(always)]
    pub fn collect_with_indices3(&self, a1: impl Iterator<Item = (usize, u64, u64, u64)>) -> (Indices<'a>, &'a [u64], &'a [u64], &'a [u64]) {
        let mut res1 = self.alloc_zeros();
        let mut res2 = self.alloc_zeros();
        let mut res3 = self.alloc_zeros();
        let mut ind = self.alloc_vec();
        for (i, v1, v2, v3) in a1 {
            res1[i] = v1;
            res2[i] = v2;
            res3[i] = v3;
            ind.push(i);
        }
        (Indices(ind.into_bump_slice()), res1, res2, res3)
    }
    #[inline(always)]
    pub fn alloc_vec<T>(&self) -> BVec<'a, T> {
        BVec::new_in(self.bump)
    }
    #[inline(always)]
    pub fn alloc_indices(&self, vec: Vec<usize>) -> Indices<'a> {
        Indices(self.alloc_iter(vec.into_iter()))
    }
    #[inline(always)]
    pub fn alloc_iter_unsized<T>(&self, mut iter: impl Iterator<Item = T>) -> &'a [T] {
        let vec = BVec::from_iter_in(iter, self.bump);
        vec.into_bump_slice()
    }
    pub fn lookup(&self, idx: i16) -> &Bv<'a> {
        if idx > 0 {
            &self.variables[(idx - 1) as usize]
        } else if idx < 0 {
            &self.consts[(- idx - 1) as usize]
        } else {
            panic!("should not happen")
        }
    }
    pub fn allzero(&self) -> Bv<'a> {
        Bv(self.allzero)
    }
    pub fn allone(&self) -> Bv<'a> {
        Bv(self.allmax)
    }
    pub fn new_var(&mut self, mut iter: impl ExactSizeIterator<Item = u64>) -> i16 {
        self.variables.push(Bv::new(self.allexamples, self.alloc_iter(iter)));
        self.variables.len() as i16
    }
    pub fn new_const(&mut self, value: u64) -> i16 {
        self.consts.push(Bv::new(self.allexamples, self.bump.alloc_slice_fill_copy(self.len, value)));
        -(self.consts.len() as i16)
    }
    pub fn variables(&self) -> impl Iterator<Item= (i16, &Bv<'a>)> {
        self.variables.iter().enumerate().map(|(i, v)| ((i + 1) as i16, v))
    }
    pub fn consts(&self) -> impl Iterator<Item= (i16, &Bv<'a>)> {
        self.consts.iter().enumerate().map(|(i, v)| (-((i + 1) as i16), v))
    }
    pub fn no_hint(&self) -> Hint<'a> {
        Hint::new(Indices(self.alloc([])), self.alloc([]), self.alloc([]))
    }
}
