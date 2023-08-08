pub type Bits = Box<[u128]>;

pub trait BoxSliceExt {
    fn from_bit_iter(t: impl Iterator<Item = usize>, len: usize) -> Self;
    fn zeros(len: usize) -> Self;
    fn count_ones(&self) -> u32;
    fn conjunction_assign(&mut self, other: &Self);
    fn union_assign(&mut self, other: &Self);
    fn difference_assign(&mut self, other: &Self);
    fn subset(&self, other: &Self) -> bool;
    
}
fn ceildiv(a: usize, b: usize) -> usize {
    (a + b - 1) / b
}

impl BoxSliceExt for Box<[u128]> {
    fn from_bit_iter(t: impl Iterator<Item = usize>, len: usize) -> Self {
        let mut vec: Vec<u128> = vec![0u128; ceildiv(len, u128::BITS as usize)];
        for i in t {
            vec[i / u128::BITS as usize] |= 1 << (i as u32 % u128::BITS);
        }
        vec.into_boxed_slice()
    }
    fn zeros(len: usize) -> Self {
        (0..ceildiv(len, u128::BITS as usize)).map(|_| 0u128).collect()
    }

    fn count_ones(&self) -> u32 {
        self.iter().map(|x| x.count_ones()).sum()
    }
    
    fn conjunction_assign(&mut self, other: &Self) {
        self.iter_mut().zip(other.iter()).for_each(|(i, j)| *i &= j);
    }
    fn union_assign(&mut self, other: &Self) {
        self.iter_mut().zip(other.iter()).for_each(|(i, j)| *i |= j);
    }
    
    fn difference_assign(&mut self, other: &Self) {
        self.iter_mut().zip(other.iter()).for_each(|(i, j)| *i &= !j);
    }

    fn subset(&self, other: &Self) -> bool {
        other.iter().zip(self.iter()).all(|(i, j)| i & j == *j)
    }
}

pub fn boxed_ones(size: usize) -> Box<[u128]> {
    let l = ceildiv(size, u128::BITS as usize);
    let rem = size as u32 % u128::BITS;
    (0..l).map(|i| if i + 1 == l { (1 << rem) - 1 } else { u128::MAX }).collect()
}