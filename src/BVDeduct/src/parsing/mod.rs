use crate::algo::AlgoConf;

use self::sexpr::SExpr;


pub mod sexpr;
#[derive(Debug)]
pub struct NonTerminal {
    name: String,
    pub rules: Vec<SExpr>,
}

impl NonTerminal {
    pub fn from(expr: SExpr) -> Self {
        let (name, args) = expr.to_call().expect("Expecting name of nonterminal");
        let [_, list] : [SExpr; 2] = args.try_into().expect("Unexpected number of arguments.");
        let rules = list.to_list().expect("Expecting rules list.");
        NonTerminal { name, rules }
    }
    pub fn replace_func(self, funcs: &[Function]) -> Self {
        Self { name: self.name, rules: self.rules.into_iter().map(|x| x.replace_func(funcs)).collect() }
    }
}
#[derive(Debug)]
pub struct Function {
    name: String,
    args: Vec<String>,
    expr: SExpr,
}

impl Function {
    pub fn from(expr: SExpr) -> Option<Function> {
        expr.match_call("define-fun").and_then(|[a1, a2, _, a4]| {
            let name = a1.to_symbol().expect("Expecting a identifier for define-fun.");
            let args = a2.to_list().expect("Expecting argument list.").into_iter().map(|x| x.to_call().expect("Expecting argument.").0).collect();
            let expr = a4;
            Some(Function { name, args, expr })
        })
    }
}
#[derive(Debug)]
pub struct PBEProblem {
    nonterminals: Vec<NonTerminal>,
    pub input: Vec<(String, Vec<u64>)>,
    pub output: Vec<u64>,
    start_nonterminal: usize,
}

impl PBEProblem {
    pub fn from(file: SExpr) -> PBEProblem {
        let l = file.to_list().expect("Expect a file.");
        let mut funcs: Vec<Function> = Vec::new();
        let mut name = String::from("");
        let mut input: Vec<(String, Vec<u64>)> = Vec::new();
        let mut output: Vec<u64> = Vec::new();
        let mut nonterminals: Vec<NonTerminal> = Vec::new();
        let mut start_nonterminal = 0;
        for stmt in l {
            if stmt.is_call("define-fun") {
                if let Some(f) = Function::from(stmt) {
                    funcs.push(f);
                }
            } else if stmt.is_call("constraint") {
                let [constraint] = stmt.match_call("constraint").unwrap();
                let [i, o] = constraint.match_call("=").expect("Expecting an example.");
                let (n, args) = i.to_call().expect("Expecting an example.");
                if n != name { panic!("Expecting an example."); }
                
                for ((_, vec), value) in input.iter_mut().zip(args.into_iter()) {
                    vec.push(value.to_const().expect("Expecting an example."));
                }
                output.push(o.to_const().expect("Expecting an example."));
            } else if stmt.is_call("synth-fun") {
                let [n, args, _, _, nts] = stmt.match_call("synth-fun").expect("Synth-fun have 5 arguments.");
                name = n.to_symbol().expect("Expecting an identifier.");
                input = args.to_list().expect("Expecting argument list.").into_iter().map(|x| (x.to_call().expect("Expecting an argument").0, Vec::new())).collect();
                nonterminals = nts.to_list().expect("Expecting nonterminal grammar list.").into_iter().map(NonTerminal::from).map(|x| x.replace_func(&*funcs)).collect();
            }
        }
        if let Some(nt) = nonterminals.iter().enumerate().find(|x| x.1.name == "Start") {
            start_nonterminal = nt.0;
        } else { panic!("Need a Start nonterminal."); }
        PBEProblem { nonterminals, input, output, start_nonterminal }
    }
    pub fn match_call<'b, 'a: 'b>(&'b self, expr: &'a SExpr, name: &str) -> Vec<&'_ [SExpr]> {
        match expr {
            SExpr::Symbol(sym) => {
                if let Some(x) = self.nonterminals.iter().find(|x| &x.name == sym) {
                    x.rules.iter().flat_map(|p| self.match_call(p, name)).collect()
                } else { Vec::with_capacity(0) }
            }
            SExpr::Constant(_) => Vec::with_capacity(0),
            SExpr::List(vec) => {
                if let SExpr::Symbol(x) = &vec[0] {
                    if x == name {
                        vec![&vec[1..vec.len()]]
                    } else { Vec::with_capacity(0) }
                } else { Vec::with_capacity(0) }
            }
        }
    }
    pub fn match_variable<'b, 'a: 'b>(&'b self, expr: &'a SExpr) -> Vec<usize> {
        match expr {
            SExpr::Symbol(sym) => {
                if let Some(x) = self.nonterminals.iter().find(|x| &x.name == sym) {
                    x.rules.iter().flat_map(|p| self.match_variable(p)).collect()
                } else if let Some(v) = self.input.iter().enumerate().find(|x| &x.1.0 == sym) {
                    vec![v.0]
                } else { Vec::with_capacity(0) }
            }
            SExpr::Constant(_) => Vec::with_capacity(0),
            SExpr::List(vec) => {
                Vec::with_capacity(0)
            }
        }
    }
    pub fn match_consts<'b, 'a: 'b>(&'b self, expr: &'a SExpr) -> Vec<u64> {
        match expr {
            SExpr::Symbol(sym) => {
                if let Some(x) = self.nonterminals.iter().find(|x| &x.name == sym) {
                    x.rules.iter().flat_map(|p| self.match_consts(p)).collect()
                } else { Vec::with_capacity(0) }
            }
            SExpr::Constant(v) => vec![*v],
            SExpr::List(vec) => {
                Vec::with_capacity(0)
            }
        }
    }
    pub fn algo_conf(&self) -> AlgoConf {
        let mut conf = AlgoConf::new();
        let start = SExpr::Symbol("Start".into());

        conf.varibles = self.match_variable(&start);
        conf.constants = self.match_consts(&start);

        for args in self.match_call(&start, "ite") {
            match args {
                [cond, t, f] if t == &start && f == &start => {
                    for args in self.match_call(cond, "=") {
                        match args {
                            [s, one] if s == &start => {
                                conf.ite = true;
                            } _ => (),
                        }
                    }
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvadd") {
            match args {
                [a1, a2] if a1 == &start && a2 == &start => {
                    conf.add = true;
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvor") {
            match args {
                [a1, a2] if a1 == &start && a2 == &start => {
                    conf.or = true;
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvand") {
            match args {
                [a1, a2] if a1 == &start && a2 == &start => {
                    conf.and = true;
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvxor") {
            match args {
                [a1, a2] if a1 == &start && a2 == &start => {
                    conf.xor = true;
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvmul") {
            match args {
                [a1, a2] if a1 == &start && a2 == &start => {
                    conf.mul = true;
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvudiv") {
            match args {
                [a1, a2] if a1 == &start && a2 == &start => {
                    conf.udiv = true;
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvlshr") {
            match args {
                [a1, a2] if a1 == &start => {
                    if self.match_consts(a2).contains(&1) {
                        conf.shr = true;
                    }
                } _ => (),
            }
        }
        for args in self.match_call(&start, "bvshl") {
            match args {
                [a1, a2] if a1 == &start => {
                    if self.match_consts(a2).contains(&1) {
                        conf.shl = true;
                    }
                } _ => (),
            }
        }

        for args in self.match_call(&start, "bvnot") {
            match args {
                [a1] if a1 == &start => {
                    conf.not = true;
                } _ => (),
            }
        }
        conf
    }
}

#[cfg(test)]
mod tests {
    use std::fs::read_to_string;

    use super::{PBEProblem, sexpr::SExpr};

    #[test]
    fn test_parse() {
        let input = read_to_string("test.sl").unwrap();
        let (rest, result) = SExpr::parse(&*input).unwrap();
        let problem = PBEProblem::from(result);
        println!("{problem:?}");
        println!("{:?}", problem.algo_conf());
    }
}
