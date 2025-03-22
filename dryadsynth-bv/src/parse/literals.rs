use nom::{IResult, bytes::complete::{take_while1, tag}, combinator::map_res, sequence::preceded, branch::alt};
use pest::Span;

use super::{Error, new_custom_error_span};


fn u64_hex(input: &str) -> Result<u64, std::num::ParseIntError> {
    u64::from_str_radix(input, 16)
}
fn u64_binary(input: &str) -> Result<u64, std::num::ParseIntError> {
    u64::from_str_radix(input, 2)
}
fn u64_decimal(input: &str) -> Result<u64, std::num::ParseIntError> {
    u64::from_str_radix(input, 10)
}

pub fn u64_constant_no_error<'i>(input: &str) -> u64 {
    let res : IResult<&str, u64> = alt((
        preceded(tag("#x"), map_res(take_while1(|x: char| x.is_digit(16)), u64_hex)),
        preceded(tag("#d"), map_res(take_while1(|x: char| x.is_digit(10)), u64_decimal)),
        preceded(tag("#b"), map_res(take_while1(|x: char| x.is_digit(2)), u64_binary)),
    ))(input);
    res.expect("expecting a constant.").1
}
pub fn u64_constant_python<'i>(input: &str) -> Option<u64> {
    let res : IResult<&str, u64> = alt((
        preceded(tag("0x"), map_res(take_while1(|x: char| x.is_digit(16)), u64_hex)),
        preceded(tag("0b"), map_res(take_while1(|x: char| x.is_digit(2)), u64_binary)),
        map_res(take_while1(|x: char| x.is_digit(10)), u64_decimal),
    ))(input);
    res.ok().map(|(x, y)| y)
}

pub fn u64_constant<'i>(input: &str, span: Span<'i>) -> Result<u64, Error> {
    let res : IResult<&str, u64> = alt((
        preceded(tag("#x"), map_res(take_while1(|x: char| x.is_digit(16)), u64_hex)),
        preceded(tag("#d"), map_res(take_while1(|x: char| x.is_digit(10)), u64_decimal)),
        preceded(tag("#b"), map_res(take_while1(|x: char| x.is_digit(2)), u64_binary)),
    ))(input);
    match res {
        Ok((s, a)) => {
            if s.len() == 0 { Ok(a) }
            else { Err(new_custom_error_span("Unrecongnized Constant.".into(), span)) }
        }
        Err(_) => Err(new_custom_error_span("Unrecongnized Constant.".into(), span))
    }
}

#[cfg(test)]
mod tests {
    use super::u64_constant_python;

    #[test]
    fn test1() {
        assert_eq!(u64_constant_python("0x01"), Some(1));
        assert_eq!(u64_constant_python("1"), Some(1));
        assert_eq!(u64_constant_python("011000"), Some(11000));
        assert_eq!(u64_constant_python("000011000"), Some(11000));
        assert_eq!(u64_constant_python("0b00001"), Some(1));
    }
}