use pest::{Parser, iterators::Pair};


pub use pest::Span;
pub use pest::Position;

pub type Error = pest::error::Error<Rule>;

pub fn new_custom_error_span<'i>(msg: String, span: Span<'i>) -> Error {
    Error::new_from_span(pest::error::ErrorVariant::CustomError { message: msg }, span)
}
pub fn new_costom_error_pos<'i>(msg: String, pos: Position<'i>) -> Error {
    Error::new_from_pos(pest::error::ErrorVariant::CustomError { message: msg }, pos)
}

#[derive(Clone)]
pub enum SExpr<'i> {
    Id(&'i str, Span<'i>),
    Const(&'i str, Span<'i>),
    List(Vec<SExpr<'i>>, Span<'i>),
}

impl<'i> SExpr<'i> {
    pub fn call(&self) -> Option<(&'i str, &[SExpr<'i>])> {
        match self {
            SExpr::List(vec, _) => {
                match vec.first().unwrap() {
                    SExpr::Id(n, _) => {
                        if let Ok(r) = (&vec[1..]).try_into() {
                            Some((n, r))
                        } else { None }
                    } _ => None
                }
            } _ => None,
        }
    }
    
    pub fn list(&self) -> Option<&[SExpr<'i>]> {
        match self {
            SExpr::List(vec, _) => {
                if let Ok(r) = (&vec[..]).try_into() {
                    Some(r)
                } else { None }
            } _ => None,
        }
    }
    pub fn span(&self) -> & Span<'i> {
        match self {
            SExpr::Id(_, s) => s,
            SExpr::Const(_, s) => s,
            SExpr::List(_, s) => s,
        }
    }
    pub fn len(&self) -> usize {
        match self {
            SExpr::Id(_, _) => 0,
            SExpr::Const(_, _) => 0,
            SExpr::List(v, _) => v.len(),
        }
    }
}

impl<'i> std::fmt::Display for SExpr<'i> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        use join_lazy_fmt::Join;
        match self {
            SExpr::Id(i, _) => write!(f, "{}", i),
            SExpr::Const(i, _) => write!(f, "{}", i),
            SExpr::List(v, _) => write!(f, "({})", " ".join(v))
        }
    }
}

impl<'i> std::fmt::Debug for SExpr<'i> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        std::fmt::Display::fmt(&self, f)
    }
}

impl<'i> SExpr<'i> {
    pub fn parse(input: &'i str) -> Result<SExpr<'i>, Error> {
        let mut pairs = SExprParser::parse(Rule::exprs, input)?.into_iter();
        if let Some(x) = pairs.next() {
            if x.as_span().end() < input.len() {
                return Err(new_costom_error_pos("Unrecongized remaining input".into(), x.as_span().end_pos()));
            }
            Ok(Self::construct(x))
        } else {
            panic!("Parse Failed.");
        }
    }
    fn construct(pair: Pair<'i, Rule>) -> SExpr<'i> {
        let span = pair.as_span();
        let pairs = pair.into_inner();
        if let Some(first) = pairs.peek() {
            match first.as_rule() {
                Rule::id => { return SExpr::Id(first.as_str(), first.as_span()); }
                Rule::iliteral => { return SExpr::Const(first.as_str(), first.as_span()); }
                _ => (),
            }
        }
        let vec: Vec<_> = pairs.into_iter().map(|x| {
            assert!(x.as_rule() == Rule::expr);
            Self::construct(x)
        }).collect();
        SExpr::List(vec, span)
    }
}


#[derive(pest_derive::Parser)]
#[grammar = "src/parse/sexpr.pest"]
struct SExprParser;


#[cfg(test)]
mod tests {
    use super::SExpr;

    #[test]
    fn parse_test() {
        println!("{}", SExpr::parse("(alpha beta) (delta)").unwrap());
    }
}



