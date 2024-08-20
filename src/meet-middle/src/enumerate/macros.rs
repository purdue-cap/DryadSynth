
#[macro_export]
macro_rules! enum_expr {
    () => {
        #[derive(DebugCustom, Display, PartialEq, Eq, Hash, Clone)]
        pub enum Expr<'a> {
            #[debug(fmt = "!{:?}", _0)]
            #[display(fmt = "(bvnot {})", _0)]
            Not(&'a Expr<'a>),
            #[debug(fmt = "~{:?}", _0)]
            #[display(fmt = "(bvneg {})", _0)]
            Neg(&'a Expr<'a>),
            #[debug(fmt = "f10{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(0, 1)", _0)]
            Fop1_0(&'a Expr<'a>),
            #[debug(fmt = "f11{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(1, 1)", _0)]
            Fop1_1(&'a Expr<'a>),
            #[debug(fmt = "f12{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(2, 1)", _0)]
            Fop1_2(&'a Expr<'a>),
            #[debug(fmt = "f13{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(3, 1)", _0)]
            Fop1_3(&'a Expr<'a>),
            #[debug(fmt = "f14{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(4, 1)", _0)]
            Fop1_4(&'a Expr<'a>),
            #[debug(fmt = "f15{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(5, 1)", _0)]
            Fop1_5(&'a Expr<'a>),
            #[debug(fmt = "f16{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(6, 1)", _0)]
            Fop1_6(&'a Expr<'a>),
            #[debug(fmt = "f17{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(7, 1)", _0)]
            Fop1_7(&'a Expr<'a>),
            #[debug(fmt = "(+ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvadd {} {})", _0, _1)]
            Add(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(^ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvxor {} {})", _0, _1)]
            Xor(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(& {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvand {} {})", _0, _1)]
            And(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(| {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvor {} {})", _0, _1)]
            Or(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(* {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvmul {} {})", _0, _1)]
            Mul(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(- {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvsub {} {})", _0, _1)]
            Sub(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f20 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(0, 2)", _0, _1)]
            Fop2_0(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f21 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(1, 2)", _0, _1)]
            Fop2_1(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f22 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(2, 2)", _0, _1)]
            Fop2_2(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f23 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(3, 2)", _0, _1)]
            Fop2_3(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f24 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(4, 2)", _0, _1)]
            Fop2_4(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f25 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(5, 2)", _0, _1)]
            Fop2_5(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f26 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(6, 2)", _0, _1)]
            Fop2_6(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(f27 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(7, 2)", _0, _1)]
            Fop2_7(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(>> {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvlshr {} {})", _0, _1)]
            LShr(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(>>> {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvashr {} {})", _0, _1)]
            AShr(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(<< {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvshl {} {})", _0, _1)]
            Shl(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(/ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvudiv {} {})", _0, _1)]
            UDiv(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(% {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvurem {} {})", _0, _1)]
            URem(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(s/ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvsdiv {} {})", _0, _1)]
            SDiv(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(s% {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvsrem {} {})", _0, _1)]
            SRem(&'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "(ite {:?} {:?} {:?})", _0, _1, _2)]
            #[display(fmt = "(ite (= {} #x0000000000000000) {} {})", _0, _1, _2)]
            Ite(&'a Expr<'a>, &'a Expr<'a>, &'a Expr<'a>),
            #[debug(fmt = "v{:?}", _0)]
            #[display(fmt = "v{:?}", _0)]
            Var(usize),
            #[debug(fmt = "{:#x}", _0)]
            #[display(fmt = "#x{:016x}", _0)]
            Const(u64),
        }
    };
}

#[macro_export]
macro_rules! enum_ownedexpr {
    () => {
        #[derive(DebugCustom, Display, PartialEq, Eq, Hash, Clone)]
        pub enum OwnedExpr {
            #[debug(fmt = "!{:?}", _0)]
            #[display(fmt = "(bvnot {})", _0)]
            Not(Box<OwnedExpr>),
            #[debug(fmt = "~{:?}", _0)]
            #[display(fmt = "(bvneg {})", _0)]
            Neg(Box<OwnedExpr>),
            #[debug(fmt = "f10{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(0, 1)", _0)]
            Fop1_0(Box<OwnedExpr>),
            #[debug(fmt = "f11{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(1, 1)", _0)]
            Fop1_1(Box<OwnedExpr>),
            #[debug(fmt = "f12{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(2, 1)", _0)]
            Fop1_2(Box<OwnedExpr>),
            #[debug(fmt = "f13{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(3, 1)", _0)]
            Fop1_3(Box<OwnedExpr>),
            #[debug(fmt = "f14{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(4, 1)", _0)]
            Fop1_4(Box<OwnedExpr>),
            #[debug(fmt = "f15{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(5, 1)", _0)]
            Fop1_5(Box<OwnedExpr>),
            #[debug(fmt = "f16{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(6, 1)", _0)]
            Fop1_6(Box<OwnedExpr>),
            #[debug(fmt = "f17{:?}", _0)]
            #[display(fmt = "({} {})", "crate::parse::deffun::ENV.name(7, 1)", _0)]
            Fop1_7(Box<OwnedExpr>),
            #[debug(fmt = "(+ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvadd {} {})", _0, _1)]
            Add(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(^ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvxor {} {})", _0, _1)]
            Xor(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(& {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvand {} {})", _0, _1)]
            And(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(| {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvor {} {})", _0, _1)]
            Or(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(* {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvmul {} {})", _0, _1)]
            Mul(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(- {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvsub {} {})", _0, _1)]
            Sub(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f20 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(0, 2)", _0, _1)]
            Fop2_0(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f21 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(1, 2)", _0, _1)]
            Fop2_1(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f22 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(2, 2)", _0, _1)]
            Fop2_2(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f23 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(3, 2)", _0, _1)]
            Fop2_3(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f24 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(4, 2)", _0, _1)]
            Fop2_4(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f25 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(5, 2)", _0, _1)]
            Fop2_5(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f26 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(6, 2)", _0, _1)]
            Fop2_6(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(f27 {:?} {:?})", _0, _1)]
            #[display(fmt = "({} {} {})", "crate::parse::deffun::ENV.name(7, 2)", _0, _1)]
            Fop2_7(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(>> {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvlshr {} {})", _0, _1)]
            LShr(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(>>> {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvashr {} {})", _0, _1)]
            AShr(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(<< {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvshl {} {})", _0, _1)]
            Shl(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(/ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvudiv {} {})", _0, _1)]
            UDiv(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(% {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvurem {} {})", _0, _1)]
            URem(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(s/ {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvsdiv {} {})", _0, _1)]
            SDiv(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(s% {:?} {:?})", _0, _1)]
            #[display(fmt = "(bvsrem {} {})", _0, _1)]
            SRem(Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "(ite {:?} {:?} {:?})", _0, _1, _2)]
            #[display(fmt = "(ite (= {} #x0000000000000000) {} {})", _0, _1, _2)]
            Ite(Box<OwnedExpr>, Box<OwnedExpr>, Box<OwnedExpr>),
            #[debug(fmt = "v{:?}", _0)]
            #[display(fmt = "v{:?}", _0)]
            Var(usize),
            #[debug(fmt = "{:#x}", _0)]
            #[display(fmt = "#x{:016x}", _0)]
            Const(u64),
        }
    };
}

#[macro_export]
macro_rules! op_of {
    (Not) => {
        crate::enumerate::op::not
    };
    (Neg) => {
        crate::enumerate::op::neg
    };
    (Fop1_0) => {
        crate::enumerate::op::fop1::<0>
    };
    (Fop1_1) => {
        crate::enumerate::op::fop1::<1>
    };
    (Fop1_2) => {
        crate::enumerate::op::fop1::<2>
    };
    (Fop1_3) => {
        crate::enumerate::op::fop1::<3>
    };
    (Fop1_4) => {
        crate::enumerate::op::fop1::<4>
    };
    (Fop1_5) => {
        crate::enumerate::op::fop1::<5>
    };
    (Fop1_6) => {
        crate::enumerate::op::fop1::<6>
    };
    (Fop1_7) => {
        crate::enumerate::op::fop1::<7>
    };
    (Add) => {
        crate::enumerate::op::add
    };
    (Xor) => {
        crate::enumerate::op::xor
    };
    (And) => {
        crate::enumerate::op::and
    };
    (Or) => {
        crate::enumerate::op::or
    };
    (Mul) => {
        crate::enumerate::op::mul
    };
    (Sub) => {
        crate::enumerate::op::sub
    };
    (Fop2_0) => {
        crate::enumerate::op::fop2::<0>
    };
    (Fop2_1) => {
        crate::enumerate::op::fop2::<1>
    };
    (Fop2_2) => {
        crate::enumerate::op::fop2::<2>
    };
    (Fop2_3) => {
        crate::enumerate::op::fop2::<3>
    };
    (Fop2_4) => {
        crate::enumerate::op::fop2::<4>
    };
    (Fop2_5) => {
        crate::enumerate::op::fop2::<5>
    };
    (Fop2_6) => {
        crate::enumerate::op::fop2::<6>
    };
    (Fop2_7) => {
        crate::enumerate::op::fop2::<7>
    };
    (LShr) => {
        crate::enumerate::op::lshr
    };
    (AShr) => {
        crate::enumerate::op::ashr
    };
    (Shl) => {
        crate::enumerate::op::shl
    };
    (UDiv) => {
        crate::enumerate::op::udiv
    };
    (URem) => {
        crate::enumerate::op::urem
    };
    (SDiv) => {
        crate::enumerate::op::sdiv
    };
    (SRem) => {
        crate::enumerate::op::srem
    };
    (Ite) => {
        crate::enumerate::op::ite
    };
}

#[macro_export]
macro_rules! bv_op_of {
    (Not) => {
        crate::enumerate::op::bv::not
    };
    (Neg) => {
        crate::enumerate::op::bv::neg
    };
    (Fop1_0) => {
        crate::enumerate::op::bv::fop1::<0, N>
    };
    (Fop1_1) => {
        crate::enumerate::op::bv::fop1::<1, N>
    };
    (Fop1_2) => {
        crate::enumerate::op::bv::fop1::<2, N>
    };
    (Fop1_3) => {
        crate::enumerate::op::bv::fop1::<3, N>
    };
    (Fop1_4) => {
        crate::enumerate::op::bv::fop1::<4, N>
    };
    (Fop1_5) => {
        crate::enumerate::op::bv::fop1::<5, N>
    };
    (Fop1_6) => {
        crate::enumerate::op::bv::fop1::<6, N>
    };
    (Fop1_7) => {
        crate::enumerate::op::bv::fop1::<7, N>
    };
    (Add) => {
        crate::enumerate::op::bv::add
    };
    (Xor) => {
        crate::enumerate::op::bv::xor
    };
    (And) => {
        crate::enumerate::op::bv::and
    };
    (Or) => {
        crate::enumerate::op::bv::or
    };
    (Mul) => {
        crate::enumerate::op::bv::mul
    };
    (Sub) => {
        crate::enumerate::op::bv::sub
    };
    (Fop2_0) => {
        crate::enumerate::op::bv::fop2::<0, N>
    };
    (Fop2_1) => {
        crate::enumerate::op::bv::fop2::<1, N>
    };
    (Fop2_2) => {
        crate::enumerate::op::bv::fop2::<2, N>
    };
    (Fop2_3) => {
        crate::enumerate::op::bv::fop2::<3, N>
    };
    (Fop2_4) => {
        crate::enumerate::op::bv::fop2::<4, N>
    };
    (Fop2_5) => {
        crate::enumerate::op::bv::fop2::<5, N>
    };
    (Fop2_6) => {
        crate::enumerate::op::bv::fop2::<6, N>
    };
    (Fop2_7) => {
        crate::enumerate::op::bv::fop2::<7, N>
    };
    (LShr) => {
        crate::enumerate::op::bv::lshr
    };
    (AShr) => {
        crate::enumerate::op::bv::ashr
    };
    (Shl) => {
        crate::enumerate::op::bv::shl
    };
    (UDiv) => {
        crate::enumerate::op::bv::udiv
    };
    (URem) => {
        crate::enumerate::op::bv::urem
    };
    (SDiv) => {
        crate::enumerate::op::bv::sdiv
    };
    (SRem) => {
        crate::enumerate::op::bv::srem
    };
}

#[macro_export]
macro_rules! smt_name_of {
    (Not) => {
        "bvnot"
    };
    (Neg) => {
        "bvneg"
    };
    (Fop1_0) => {
        "bvfop1_0"
    };
    (Fop1_1) => {
        "bvfop1_1"
    };
    (Fop1_2) => {
        "bvfop1_2"
    };
    (Fop1_3) => {
        "bvfop1_3"
    };
    (Fop1_4) => {
        "bvfop1_4"
    };
    (Fop1_5) => {
        "bvfop1_5"
    };
    (Fop1_6) => {
        "bvfop1_6"
    };
    (Fop1_7) => {
        "bvfop1_7"
    };
    (Add) => {
        "bvadd"
    };
    (Xor) => {
        "bvxor"
    };
    (And) => {
        "bvand"
    };
    (Or) => {
        "bvor"
    };
    (Mul) => {
        "bvmul"
    };
    (Sub) => {
        "bvsub"
    };
    (Fop2_0) => {
        "bvfop2_0"
    };
    (Fop2_1) => {
        "bvfop2_1"
    };
    (Fop2_2) => {
        "bvfop2_2"
    };
    (Fop2_3) => {
        "bvfop2_3"
    };
    (Fop2_4) => {
        "bvfop2_4"
    };
    (Fop2_5) => {
        "bvfop2_5"
    };
    (Fop2_6) => {
        "bvfop2_6"
    };
    (Fop2_7) => {
        "bvfop2_7"
    };
    (LShr) => {
        "bvlshr"
    };
    (AShr) => {
        "bvashr"
    };
    (Shl) => {
        "bvshl"
    };
    (UDiv) => {
        "bvudiv"
    };
    (URem) => {
        "bvurem"
    };
    (SDiv) => {
        "bvsdiv"
    };
    (SRem) => {
        "bvsrem"
    };
    (Ite) => {
        "bvite"
    };
}

#[macro_export]
macro_rules! symbol_of {
    (Not) => {
        "!"
    };
    (Neg) => {
        "~"
    };
    (Fop1_0) => {
        "f10"
    };
    (Fop1_1) => {
        "f11"
    };
    (Fop1_2) => {
        "f12"
    };
    (Fop1_3) => {
        "f13"
    };
    (Fop1_4) => {
        "f14"
    };
    (Fop1_5) => {
        "f15"
    };
    (Fop1_6) => {
        "f16"
    };
    (Fop1_7) => {
        "f17"
    };
    (Add) => {
        "+"
    };
    (Xor) => {
        "^"
    };
    (And) => {
        "&"
    };
    (Or) => {
        "|"
    };
    (Mul) => {
        "*"
    };
    (Sub) => {
        "-"
    };
    (Fop2_0) => {
        "f20"
    };
    (Fop2_1) => {
        "f21"
    };
    (Fop2_2) => {
        "f22"
    };
    (Fop2_3) => {
        "f23"
    };
    (Fop2_4) => {
        "f24"
    };
    (Fop2_5) => {
        "f25"
    };
    (Fop2_6) => {
        "f26"
    };
    (Fop2_7) => {
        "f27"
    };
    (LShr) => {
        ">>"
    };
    (AShr) => {
        ">>>"
    };
    (Shl) => {
        "<<"
    };
    (UDiv) => {
        "/"
    };
    (URem) => {
        "%"
    };
    (SDiv) => {
        "s/"
    };
    (SRem) => {
        "s%"
    };
    (Ite) => {
        "ite"
    };
}

#[macro_export]
macro_rules! all_ops {
    () => {
        foreach_op!(Not);
        foreach_op!(Neg);
        foreach_op!(Fop1_0);
        foreach_op!(Fop1_1);
        foreach_op!(Fop1_2);
        foreach_op!(Fop1_3);
        foreach_op!(Fop1_4);
        foreach_op!(Fop1_5);
        foreach_op!(Fop1_6);
        foreach_op!(Fop1_7);
        foreach_op!(Add);
        foreach_op!(Xor);
        foreach_op!(And);
        foreach_op!(Or);
        foreach_op!(Mul);
        foreach_op!(Sub);
        foreach_op!(Fop2_0);
        foreach_op!(Fop2_1);
        foreach_op!(Fop2_2);
        foreach_op!(Fop2_3);
        foreach_op!(Fop2_4);
        foreach_op!(Fop2_5);
        foreach_op!(Fop2_6);
        foreach_op!(Fop2_7);
        foreach_op!(LShr);
        foreach_op!(AShr);
        foreach_op!(Shl);
        foreach_op!(UDiv);
        foreach_op!(URem);
        foreach_op!(SDiv);
        foreach_op!(SRem)
    };
    (type) => {
        foreach_op!(op1, Not);
        foreach_op!(op1, Neg);
        foreach_op!(op1, Fop1_0);
        foreach_op!(op1, Fop1_1);
        foreach_op!(op1, Fop1_2);
        foreach_op!(op1, Fop1_3);
        foreach_op!(op1, Fop1_4);
        foreach_op!(op1, Fop1_5);
        foreach_op!(op1, Fop1_6);
        foreach_op!(op1, Fop1_7);
        foreach_op!(op2_sym, Add);
        foreach_op!(op2_sym, Xor);
        foreach_op!(op2_sym, And);
        foreach_op!(op2_sym, Or);
        foreach_op!(op2_sym, Mul);
        foreach_op!(op2, Sub);
        foreach_op!(op2, Fop2_0);
        foreach_op!(op2, Fop2_1);
        foreach_op!(op2, Fop2_2);
        foreach_op!(op2, Fop2_3);
        foreach_op!(op2, Fop2_4);
        foreach_op!(op2, Fop2_5);
        foreach_op!(op2, Fop2_6);
        foreach_op!(op2, Fop2_7);
        foreach_op!(shift, LShr);
        foreach_op!(shift, AShr);
        foreach_op!(shift, Shl);
        foreach_op!(divisions, UDiv);
        foreach_op!(divisions, URem);
        foreach_op!(divisions, SDiv);
        foreach_op!(divisions, SRem)
    };
    (arity) => {
        foreach_op!(1, Not);
        foreach_op!(1, Neg);
        foreach_op!(1, Fop1_0);
        foreach_op!(1, Fop1_1);
        foreach_op!(1, Fop1_2);
        foreach_op!(1, Fop1_3);
        foreach_op!(1, Fop1_4);
        foreach_op!(1, Fop1_5);
        foreach_op!(1, Fop1_6);
        foreach_op!(1, Fop1_7);
        foreach_op!(2, Add);
        foreach_op!(2, Xor);
        foreach_op!(2, And);
        foreach_op!(2, Or);
        foreach_op!(2, Mul);
        foreach_op!(2, Sub);
        foreach_op!(2, Fop2_0);
        foreach_op!(2, Fop2_1);
        foreach_op!(2, Fop2_2);
        foreach_op!(2, Fop2_3);
        foreach_op!(2, Fop2_4);
        foreach_op!(2, Fop2_5);
        foreach_op!(2, Fop2_6);
        foreach_op!(2, Fop2_7);
        foreach_op!(2, LShr);
        foreach_op!(2, AShr);
        foreach_op!(2, Shl);
        foreach_op!(2, UDiv);
        foreach_op!(2, URem);
        foreach_op!(2, SDiv);
        foreach_op!(2, SRem)
    };
    (arity=1) => {
        foreach_op!(Not);
        foreach_op!(Neg);
        foreach_op!(Fop1_0);
        foreach_op!(Fop1_1);
        foreach_op!(Fop1_2);
        foreach_op!(Fop1_3);
        foreach_op!(Fop1_4);
        foreach_op!(Fop1_5);
        foreach_op!(Fop1_6);
        foreach_op!(Fop1_7)
    };
    (arity=2) => {
        foreach_op!(Add);
        foreach_op!(Xor);
        foreach_op!(And);
        foreach_op!(Or);
        foreach_op!(Mul);
        foreach_op!(Sub);
        foreach_op!(Fop2_0);
        foreach_op!(Fop2_1);
        foreach_op!(Fop2_2);
        foreach_op!(Fop2_3);
        foreach_op!(Fop2_4);
        foreach_op!(Fop2_5);
        foreach_op!(Fop2_6);
        foreach_op!(Fop2_7);
        foreach_op!(LShr);
        foreach_op!(AShr);
        foreach_op!(Shl);
        foreach_op!(UDiv);
        foreach_op!(URem);
        foreach_op!(SDiv);
        foreach_op!(SRem)
    };
    (arity nonzero) => {
        foreach_op!(1, false, Not);
        foreach_op!(1, false, Neg);
        foreach_op!(1, false, Fop1_0);
        foreach_op!(1, false, Fop1_1);
        foreach_op!(1, false, Fop1_2);
        foreach_op!(1, false, Fop1_3);
        foreach_op!(1, false, Fop1_4);
        foreach_op!(1, false, Fop1_5);
        foreach_op!(1, false, Fop1_6);
        foreach_op!(1, false, Fop1_7);
        foreach_op!(2, false, Add);
        foreach_op!(2, false, Xor);
        foreach_op!(2, false, And);
        foreach_op!(2, false, Or);
        foreach_op!(2, false, Mul);
        foreach_op!(2, false, Sub);
        foreach_op!(2, true, Fop2_0);
        foreach_op!(2, true, Fop2_1);
        foreach_op!(2, true, Fop2_2);
        foreach_op!(2, true, Fop2_3);
        foreach_op!(2, true, Fop2_4);
        foreach_op!(2, true, Fop2_5);
        foreach_op!(2, true, Fop2_6);
        foreach_op!(2, true, Fop2_7);
        foreach_op!(2, false, LShr);
        foreach_op!(2, false, AShr);
        foreach_op!(2, false, Shl);
        foreach_op!(2, true, UDiv);
        foreach_op!(2, true, URem);
        foreach_op!(2, true, SDiv);
        foreach_op!(2, true, SRem)
    };
}
