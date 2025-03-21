use thiserror::Error;

pub mod sexpr;
pub mod cfg;
pub mod constraint;
pub mod literals;
mod problem;
pub mod deffun;

pub use sexpr::SExpr;
pub use sexpr::Error;
pub type Result<T> = std::result::Result<T, Error>;
pub use sexpr::new_costom_error_pos;
pub use sexpr::new_custom_error_span;
pub use problem:: SynthProblem;
pub use constraint::PbeConstraint;

pub fn parse_pbe<'i>(input: &'i str) -> Result<(SynthProblem<'i>, PbeConstraint)> {
    let sexpr = SExpr::parse(input)?;
    let problem = SynthProblem::parse(&sexpr)?;
    let constraint = PbeConstraint::parse(problem.name, problem.args.len(), &sexpr)?;
    Ok((problem, constraint))
}
