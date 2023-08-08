from ast import Tuple
from dataclasses import dataclass, field
import os
from textwrap import dedent
from typing import Dict, List


@dataclass
class Op:
    name: str
    arity: int
    nz: bool
    comm: bool
    sym: str
    typ: str

    def lname(self):
        return self.name.lower()
    def smtname(self):
        return "bv" + self.name.lower()
    def debug(self):
        if self.arity == 1:
            return f'debug(fmt = "{self.sym}{{:?}}", _0)'
        else:
            inner = " ".join("{:?}" for _ in range(self.arity))
            return f'debug(fmt = "({self.sym} {inner})", {", ".join(f"_{i}" for i in range(self.arity)) })'
    def display(self):
        if self.name == "Ite":
            return 'display(fmt = "(ite (= {} #x0000000000000000) {} {})", _0, _1, _2)'
        inner = " ".join("{}" for _ in range(self.arity))
        return f'display(fmt = "({self.smtname()} {inner})", {", ".join(f"_{i}" for i in range(self.arity)) })' 
    def arm(self, rec_type: str):
        return (self.name, [rec_type] * self.arity, [self.debug(), self.display()])

op1 = [
    Op("Not", 1, False, False, "!", "op1"),
    Op("Neg", 1, False, False, "~", "op1"),
]

op2_sym = [
    Op("Add", 2, False, True, "+", "op2_sym"),
    Op("Xor", 2, False, True, "^", "op2_sym"),
    Op("And", 2, False, True, "&", "op2_sym"),
    Op("Or", 2, False, True, "|", "op2_sym"),
    Op("Mul", 2, False, True, "*", "op2_sym"),
]
op2 = [
    Op("Sub", 2, False, False, "-", "op2"),
]
    
shift = [
    Op("LShr", 2, False, False, ">>", "shift"),
    Op("AShr", 2, False, False, ">>>", "shift"),
    Op("Shl", 2, False, False, "<<", "shift"),
]

divisions = [
    Op("UDiv", 2, True, False, "/", "divisions"),
    Op("URem", 2, True, False, "%", "divisions"),
    Op("SDiv", 2, True, False, "s/", "divisions"),
    Op("SRem", 2, True, False, "s%", "divisions"),
]

operators = [*op1, *op2_sym, *op2, *shift, *divisions]

ite = Op("Ite", 3, False, False, "ite", "ite")


@dataclass
class MacroRules:
    name: str
    d: Dict[str, any] = field(default_factory=dict) 

    @staticmethod
    def wrap(name: str, inner, input = ""):
        return MacroRules(name, { input : inner })

    def __str__(self):
        s = "\n".join(f"({a}) => {{ {b} }};" for (a, b) in self.d.items())
        return f"""
        #[macro_export]
        macro_rules! {self.name} {{
            {s}
        }}
        """

@dataclass
class Enum:
    name: str
    derives: List[str]
    d: Dict[str, List[str]] = field(default_factory=dict) 
    labels: Dict[str, List[str]] = field(default_factory=dict) 

    def add(self, n: str, b: List[str], c: List[str]):
        self.d[n] = b
        self.labels[n] = c
    @staticmethod
    def arms(name: str, l: List[Tuple]) -> 'Enum':
        result = Enum(name)
        for p in l:
            result.add(*p)
        return result


    def __str__(self):
        def extract_derives(s):
            return "\n".join([ f"#[{s}]" for s in self.labels.get(s, [])])        
        variants =  ",\n".join( f'{extract_derives(a)} {a}({", ".join(b)})' for (a, b) in self.d.items() )
        return f"""
        #[derive({", ".join(self.derives)})]
        pub enum {self.name} {{
            {variants}
        }}
        """

def expr(mname, name, rec_type, l = [], owned = False):
    expr = Enum(name, ['DebugCustom', 'Display', 'PartialEq', 'Eq', 'Hash'] + l)

    for op in operators:
        expr.add(*op.arm(rec_type))
    if owned:
        expr.add(*ite.arm(rec_type))
    expr.add('Var', ['usize'], ['debug(fmt = "v{:?}", _0)', 'display(fmt = "v{:?}", _0)'])
    expr.add('Const', ['u64'], ['debug(fmt = "{:#x}", _0)', 'display(fmt = "#x{:016x}", _0)'])
    
    return MacroRules.wrap(mname, str(expr))

def op_of():
    return MacroRules("op_of", { op.name: f"crate::enumerate::op::{op.lname()}" for op in operators + [ite]})

def bv_op_of():
    return MacroRules("bv_op_of", { op.name: f"crate::enumerate::op::bv::{op.lname()}" for op in operators})

def smt_name_of():
    return MacroRules("smt_name_of", { op.name: f'"{op.smtname()}"' for op in operators + [ite]})
def symbol_of():
    return MacroRules("symbol_of", { op.name: f'"{op.sym}"' for op in operators + [ite]})

def all_ops():
    return MacroRules("all_ops", {
        "" : ";\n".join(f"foreach_op!({op.name})" for op in operators),
        "type" : ";\n".join(f"foreach_op!({op.typ},{op.name})" for op in operators),
        "arity" : ";\n".join(f"foreach_op!({op.arity},{op.name})" for op in operators),
        "arity=1" : ";\n".join(f"foreach_op!({op.name})" for op in operators if op.arity == 1),
        "arity=2" : ";\n".join(f"foreach_op!({op.name})" for op in operators if op.arity == 2),
        "arity nonzero" : ";\n".join(f"foreach_op!({op.arity}, {str(op.nz).lower()}, {op.name})" for op in operators),
    })


impls = [
    expr("enum_expr", "Expr<'a>", "&'a Expr<'a>", ["Clone"], True),
    expr("enum_ownedexpr", "OwnedExpr", "Box<OwnedExpr>", ["Clone"], True),
    op_of(),
    bv_op_of(),
    smt_name_of(),
    symbol_of(),
    all_ops(),
]


macro_rs = "./macros.rs"
print(macro_rs)
with open(macro_rs, "w") as f:
    for impl in impls:
        f.write(str(impl) + "\n\n")

os.system(f"rustfmt {macro_rs}")

