
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
        foreach_op!(Add);
        foreach_op!(Xor);
        foreach_op!(And);
        foreach_op!(Or);
        foreach_op!(Mul);
        foreach_op!(Sub);
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
        foreach_op!(op2_sym, Add);
        foreach_op!(op2_sym, Xor);
        foreach_op!(op2_sym, And);
        foreach_op!(op2_sym, Or);
        foreach_op!(op2_sym, Mul);
        foreach_op!(op2, Sub);
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
        foreach_op!(2, Add);
        foreach_op!(2, Xor);
        foreach_op!(2, And);
        foreach_op!(2, Or);
        foreach_op!(2, Mul);
        foreach_op!(2, Sub);
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
        foreach_op!(Neg)
    };
    (arity=2) => {
        foreach_op!(Add);
        foreach_op!(Xor);
        foreach_op!(And);
        foreach_op!(Or);
        foreach_op!(Mul);
        foreach_op!(Sub);
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
        foreach_op!(2, false, Add);
        foreach_op!(2, false, Xor);
        foreach_op!(2, false, And);
        foreach_op!(2, false, Or);
        foreach_op!(2, false, Mul);
        foreach_op!(2, false, Sub);
        foreach_op!(2, false, LShr);
        foreach_op!(2, false, AShr);
        foreach_op!(2, false, Shl);
        foreach_op!(2, true, UDiv);
        foreach_op!(2, true, URem);
        foreach_op!(2, true, SDiv);
        foreach_op!(2, true, SRem)
    };
}
