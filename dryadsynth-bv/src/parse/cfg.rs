

use derive_more::{From, Deref};

use super::{sexpr::{SExpr, Error}, new_custom_error_span};
use derive_more::DebugCustom;
use join_lazy_fmt::Join;
#[derive(DebugCustom, Clone)]
#[debug(fmt = "{} -> {}", _0, "\" | \".join(_1)")]
pub struct NonTerminal<'i>(pub &'i str, pub Vec<SExpr<'i>>);

impl<'i> NonTerminal<'i> {
    pub fn parse(sexpr: &SExpr<'i>) -> Result<Self, Error> {
        if let Some((name, [ty, options])) = sexpr.call() {
            if let SExpr::List(l, _) = options {
                Ok(NonTerminal(name, l.to_vec()))
            } else {
                Err(new_custom_error_span("Expecting list of expression for a non-terminal.".into(), sexpr.span().clone()))
            }
        } else {
            Err(new_custom_error_span("Expecting (<nonterminal name> <type> <expression-list>)".into(), sexpr.span().clone()))
        }
    }
}



#[derive(From, DebugCustom, Clone, Deref)]
#[debug(fmt = "{:?}", _0)]
pub struct Cfg<'i>(Vec<NonTerminal<'i>>);

impl<'i> Cfg<'i> {
    pub fn parse(sexpr: & SExpr<'i>) -> Result<Self, Error> {
        if let SExpr::List(l, _) = sexpr {
            let vec : Vec<_> = l.iter().map(NonTerminal::parse).collect::<Result<_, _>>()?;
            Ok(vec.into())
        } else {
            Err(new_custom_error_span("Expecting nonterminals list.".into(), sexpr.span().clone()))
        }
    }
    
    pub fn nt(&self, name: &str) -> Option<usize> {
        self.iter().enumerate().find(|x| x.1.0 == name).map(|x| x.0)
    }
}

#[cfg(test)]
mod tests {
    use crate::parse::{sexpr::SExpr, cfg::Cfg};

    #[test]
    fn test() {
        let e = SExpr::parse("(Start bool ((bvand a b) (bvand a c) ) )").unwrap();
        let cfg = Cfg::parse(&e).unwrap();
        println!("{:?}", cfg);

    }
}
