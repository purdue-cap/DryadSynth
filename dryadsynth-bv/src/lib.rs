// // dryadsynth-bv/src/lib.rs

// use anyhow::Result;
// use capnp::{message::ReaderOptions, serialize_packed};
// use std::io::Cursor;
// use enumerate::expr::OwnedExpr;
// use parse::{SExpr, deffun, constraint::RefImplConstraint, PbeConstraint, SynthProblem}
// use crate::search::search::SearchConfig

// pub mod sygus_capnp {
//     include!(concat!(env!("OUT_DIR"), "/sygus_capnp.rs"));
// }

// // Receive capnp encode SyGuS question input and return a capnp solution
// pub fn solve_bytes(input: &[u8]) -> Result<Vec<u8>> {
//     // decode capnp to rust
//     let mut reader = serialize_packed::read_message(
//         &mut Cursor::new(input),
//         ReaderOptions::new(),
//     )?;
//     let problem = reader.getroot::<sygus_capnp::sygus_problem::Reader>()?;
//     let logic = problem.get_logic()?;
//     let mut sl = format!("(set-logic {})\n", logic);

// }