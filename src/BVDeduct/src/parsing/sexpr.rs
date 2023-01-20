use std::{path::Path, fs, error::Error, str::FromStr};

use bumpalo::collections::vec;
use itertools::{free, Itertools};
use nom::{IResult, combinator::map};

use crate::algo::AlgoConf;

use super::Function;

#[derive(Clone, PartialEq, Eq)]
pub enum SExpr {
    Symbol(String),
    Constant(u64),
    List(Vec<SExpr>),
}


mod sexpr_parsing {
    use nom::{character::{is_alphanumeric}, IResult, bytes::complete::{take_while1, tag, take_while}, combinator::{map_res, map}, sequence::{preceded, delimited, separated_pair, tuple, terminated}, multi::separated_list0, branch::alt};

    use super::SExpr;

    pub fn whitespace0(input: &str) -> IResult<&str, &str> {
        take_while(|x: char| x.is_whitespace())(input)
    }
    pub fn whitespace1(input: &str) -> IResult<&str, &str> {
        take_while1(|x: char| x.is_whitespace())(input)
    }

    pub fn is_symbol_character(c: char) -> bool {
        match c {
            'A'..='Z' => true,
            'a'..='z' => true,
            '0'..='9' => true,
            '_' => true,
            '-' => true,
            '=' => true,
            _ => false,
        }
    }
    fn u64_hex(input: &str) -> Result<u64, std::num::ParseIntError> {
        u64::from_str_radix(input, 16)
    }
    fn u64_binary(input: &str) -> Result<u64, std::num::ParseIntError> {
        u64::from_str_radix(input, 2)
    }
    fn u64_decimal(input: &str) -> Result<u64, std::num::ParseIntError> {
        u64::from_str_radix(input, 10)
    }
    
    pub fn symbol(input: &str) -> IResult<&str, String> {
        map(take_while1(is_symbol_character), |x: &str| x.into() )(input)
    }
    
    pub fn constant(input: &str) -> IResult<&str, u64> {
        alt((
            preceded(tag("#x"), map_res(take_while1(|x: char| x.is_digit(16)), u64_hex)),
            preceded(tag("#d"), map_res(take_while1(|x: char| x.is_digit(10)), u64_decimal)),
            preceded(tag("#b"), map_res(take_while1(|x: char| x.is_digit(2)), u64_binary)),
        ))(input)
    }
    
    pub fn sexpr(input: &str) -> IResult<&str, SExpr> { 
        alt((
            map(symbol, |x| SExpr::Symbol(x)),
            map(constant, |x| SExpr::Constant(x)),
            map(apply, |x| SExpr::List(x)),
        ))(input)
    }

    pub fn sexpr_vec(input: &str) -> IResult<&str, Vec<SExpr>> {
        separated_list0(whitespace1, sexpr)(input)
    }

    pub fn apply(input: &str) -> IResult<&str, Vec<SExpr>> {
        delimited(terminated(tag("("), whitespace0), 
            map(sexpr_vec, |vec| vec)
        , preceded(whitespace0, tag(")"))) (input)
    }
}

type VecIter = <Vec<SExpr> as IntoIterator>::IntoIter;

impl SExpr {
    pub fn parse(input: &str) -> IResult<&str, Self> {
        map(sexpr_parsing::sexpr_vec, |x| SExpr::List(x))(input)
    }
    
    pub fn is_call(&self, name: &str) -> bool {
        match self {
            SExpr::List(vec) if vec.len() > 0 => {
                match &vec[0] {
                    SExpr::Symbol(s) if s == name => true,
                    _ => false,
                }
            }
            _ => false,
        }
    }

    pub fn to_call(self) -> Option<(String, Vec<SExpr>)> {
        match self {
            SExpr::List(mut vec) if vec.len() >= 1 => 
                vec.remove(0).to_symbol().map(|x| (x, vec)),
            _ => None,
        }
    }
    pub fn to_list(self) -> Option<Vec<SExpr>> {
        match self {
            SExpr::List(vec) => Some(vec),
            _ => None,
        }
    }
    pub fn to_symbol(self) -> Option<String> {
        match self {
            SExpr::Symbol(name) => Some(name),
            _ => None,
        }
    }

    pub fn to_const(self) -> Option<u64> {
        match self {
            SExpr::Constant(name) => Some(name),
            _ => None,
        }
    }
    pub fn match_call<const N: usize>(self, name: &str) -> Option<[SExpr; N]> {
        match self {
            SExpr::List(mut vec) if vec.len() > 0 => {
                let op = vec.remove(0);
                // if vec.len() != N { panic!("{name} have {} arguments, instead of {N}", vec.len())}
                match op {
                    SExpr::Symbol(n) if n == name => {
                        vec.try_into().ok()
                    }
                    _ => None
                }
            }
            _ => None,
        }
    }
    pub fn replace(self, env: &[(String, SExpr)]) -> SExpr {
        match self {
            SExpr::Symbol(s) => {
                if let Some(a) = env.iter().find(|x| x.0 == s) {
                    a.1.clone()
                } else {
                    SExpr::Symbol(s)
                }
            }
            SExpr::Constant(_) => self,
            SExpr::List(v) => SExpr::List(v.into_iter().map(|x| x.replace(env)).collect()),
        }
    }
    pub fn replace_func(self, funcs: &[Function]) -> SExpr {
        match self {
            SExpr::List(mut vec) if vec.len() > 0 => {
                match &vec[0] {
                    Self::Symbol(op) => {
                        if let Some(f) = funcs.iter().find(|f| f.name == *op) {
                            vec.remove(0);
                            let env = f.args.iter().cloned().zip(vec.into_iter()).collect_vec();
                            f.expr.clone().replace(&*env)
                        } else {
                            SExpr::List(vec.into_iter().map(|x| x.replace_func(funcs)).collect())
                        }
                    }
                    _ => SExpr::List(vec.into_iter().map(|x| x.replace_func(funcs)).collect()),
                }
            }
            _ => self,
        }
    }

}


impl std::fmt::Display for SExpr {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            Self::Symbol(arg0) => write!(f, "{arg0}"),
            Self::Constant(arg0) => write!(f, "{arg0:#x}"),
            Self::List(arg0) => write!(f, "({})", arg0.iter().join(" ")),
        }
    }
}

impl std::fmt::Debug for SExpr {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", self)
    }
}

