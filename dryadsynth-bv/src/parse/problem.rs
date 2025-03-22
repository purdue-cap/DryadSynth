use derive_more::DebugCustom;
use pest::Span;

use crate::enumerate::expr::OwnedExpr;

use super::{Error, new_custom_error_span, sexpr::SExpr, cfg::Cfg};

#[derive(Clone, DebugCustom)]
#[debug(fmt = "synth-fun {} {:?}", name, args)]
pub struct SynthProblem<'i> {
    pub name: &'i str,
    pub args: Vec<&'i str>,
    pub cfg: Cfg<'i>,
    pub span: Span<'i>,
    pub description: String,
    pub chatgpt_exprs: Vec<OwnedExpr>,
    pub bits: usize,
}

impl<'i> SynthProblem<'i> {
    pub fn parse<'a>(sexpr: &'a SExpr<'i>) -> Result<Self, Error> {
        if let SExpr::List(l, span) = sexpr {
            for e in l {
                let e: & SExpr<'i> = e;
                if let Some(("synth-fun", [SExpr::Id(name, span), args, ret, _, cfg] | [SExpr::Id(name, span), args, ret, cfg])) = e.call() {
                    let bits = if let Some([_, _, SExpr::Id(v, span)]) | Some([_, SExpr::Id(v, span)]) = ret.list() {
                        v.parse::<usize>().map_err(|_| new_custom_error_span("Unknown return value".into(), span.clone()))?
                    } else {
                        return Err(new_custom_error_span("Unknown return value".into(), ret.span().clone()));
                    };
                    if let Some(args) = args.list() {
                        let args: Vec<&'i str> = args.iter().map(|x| x.call().map(|y| y.0).ok_or(new_custom_error_span("SynthProblem: Expecting (<argname> <type>).".into(), x.span().clone()))).collect::<Result<_, _>>()?;
                        return Ok(SynthProblem{name, args,  cfg: Cfg::parse(cfg)? , span: span.clone(), description: "".into(), chatgpt_exprs: Vec::new(), bits});
                    } else { return Err(new_custom_error_span("SynthProblem: Expecting Argument list".into(), args.span().clone())); }
                }
            }
            Err(new_custom_error_span("SynthProblem: Expecting a `synth-fun`.".into(), span.clone()))
        } else { panic!("Toplevel should be a list: {sexpr}") }
    }
    pub fn arg(&self, name: &str) -> Option<usize> {
        self.args.iter().enumerate().find(|x| *x.1 == name).map(|x| x.0)
    }
}

#[cfg(test)]
mod tests {
    use crate::parse::{SExpr, SynthProblem};

    #[test]
    fn test_problem_parse() {
        let e = SExpr::parse("(synth-fun f ((x bool) (y bool)) ()
        (
            (Start bool ((bvand a b) (bvand a c) ) )
        ))").unwrap();
        let cfg = SynthProblem::parse(&e).unwrap();
        println!("{:?}", cfg);
    }
}
