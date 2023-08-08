use std::{collections::{HashMap, HashSet}, hash::BuildHasherDefault};

use byteorder::{LittleEndian, ReadBytesExt};


#[allow(missing_copy_implementations)]
pub struct FnvHasher(u64);

impl Default for FnvHasher {

    #[inline]
    fn default() -> FnvHasher {
        FnvHasher(0xcbf29ce484222325)
    }
}

impl FnvHasher {
    /// Create an FNV hasher starting with a state corresponding
    /// to the hash `key`.
    #[inline]
    pub fn with_key(key: u64) -> FnvHasher {
        FnvHasher(key)
    }
}

impl std::hash::Hasher for FnvHasher {
    #[inline]
    fn finish(&self) -> u64 {
        self.0
    }

    #[inline]
    fn write(&mut self, bytes: &[u8]) {
        let FnvHasher(mut hash) = *self;
        let bytes: &[u64] = bytemuck::try_cast_slice(bytes).unwrap();
        for byte in bytes.iter() {
            hash = hash.wrapping_mul(byte + 0x88886ffdb344693);
        }

        *self = FnvHasher(hash);
    }
}

pub type FnvBuildHasher = BuildHasherDefault<FnvHasher>;
// pub type FnvHashMap<K, V> = HashMap<K, V, FnvBuildHasher>;

pub type FnvHashSet<T> = HashSet<T, FnvBuildHasher>;