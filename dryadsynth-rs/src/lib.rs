//! Public API crate for DryadSynth (Rust front‑end + FFI)

use libc::malloc;
use std::slice;

pub mod sygus_capnp {
    include!(concat!(env!("OUT_DIR"), "/sygus_capnp.rs"));
}

pub mod sl2capnp;
//mod dispatcher;


// .sl-> capnp -> every solvers -> back the solution capnp bytes
pub fn synthesize(sl_text: &str) -> anyhow::Result<Vec<u8>> {
    let cap = sl2capnp::encode_file_text(sl_text)?;
    // TODO: change to actual solvers
    Ok(cap)
}


 #[unsafe(no_mangle)]
pub extern "C" fn dryadsynth_solve(
    ptr: *const u8,
    len: usize,
    out_len: *mut usize,
) -> *mut u8 {
    let input = unsafe { slice::from_raw_parts(ptr, len) };
    match std::str::from_utf8(input)
        .ok()
        .and_then(|t| synthesize(t).ok())
    {
        Some(res) => unsafe {
            *out_len = res.len();
            let out_ptr = malloc(res.len()) as *mut u8;
            if out_ptr.is_null() { return std::ptr::null_mut(); }
            std::ptr::copy_nonoverlapping(res.as_ptr(), out_ptr, res.len());
            out_ptr
        },
        None => std::ptr::null_mut(),
    }
}
