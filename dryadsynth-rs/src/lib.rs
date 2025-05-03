//! Public API crate for DryadSynth

pub mod sygus_capnp {
    include!(concat!(env!("OUT_DIR"), "/sygus_capnp.rs"));
}
pub mod sl2capnp;
pub use sl2capnp::encode_file;
//the solution with export
pub fn solve(input: &[u8]) -> anyhow::Result<Vec<u8>> {
    // TODO: call dryadsynth-bv, just return now
    Ok(input.to_vec())
}
