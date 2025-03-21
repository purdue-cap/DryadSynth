use ahash::{HashMap, HashMapExt};
use itertools::Itertools;
use thiserror::Error;
use tree_sitter::{Node, Parser, Language};

use crate::{enumerate::expr::OwnedExpr, all_ops, parse::literals::{u64_constant_no_error, u64_constant_python}};

#[derive(Error, Debug)]
#[error("Parsing Error")]
pub struct ParsingError;

extern "C" { fn tree_sitter_python() -> Language; }
extern "C" { fn tree_sitter_markdown() -> Language; }

pub struct Env<'a> {
    pub maps: HashMap<&'a str, OwnedExpr>,
    var: usize,
    text: &'a str,
}

fn children<'a>(node: &tree_sitter::Node<'a>) -> Vec<Node<'a>> {
    node.named_children(&mut node.walk()).filter(|x| x.kind() != "ERROR").collect_vec()
}

impl<'a> std::fmt::Debug for Env<'a> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "{:?}", self.maps)
    }
}

impl<'a> Env<'a> {
    fn text(&self, node: &tree_sitter::Node) -> &'a str {
        node.utf8_text(self.text.as_bytes()).unwrap()
    }
    pub fn get_table(self) -> HashMap<String, OwnedExpr> {
        self.maps.into_iter().map(|(a,b)| (a.into(), b)).collect()
    }
    pub fn parse(&mut self, node: &tree_sitter::Node) -> Result<OwnedExpr, ParsingError> {
        let cursor = &mut node.walk();
        match node.kind() {
            "module" => children(node).iter().map(|x| self.parse(x)).last().ok_or(ParsingError)?,
            "expression_statement" => self.parse(children(node).last().ok_or(ParsingError)?),
            "assignment" => {
                let left = node.children_by_field_name("left", cursor).next().ok_or(ParsingError)?;
                let right = node.children_by_field_name("right", cursor).next().ok_or(ParsingError)?;
                let value = self.parse(&right)?;
                self.maps.insert(self.text(&left), value.clone());
                Ok(value)
            }
            "augmented_assignment" => {
                let left = node.children_by_field_name("left", cursor).next().ok_or(ParsingError)?;
                let operator = node.children_by_field_name("operator", cursor).next().ok_or(ParsingError)?;
                let mut op = self.text(&operator);
                let right = node.children_by_field_name("right", cursor).next().ok_or(ParsingError)?;
                if op == "//" { op = "/"; }
                let op = &op[0..op.len() - 1];
                macro_rules! foreach_op {
                    ($name: ident) => {
                        if op == crate::symbol_of!($name) {
                            let value = OwnedExpr::$name(self.parse(&left)?.into(), self.parse(&right)?.into());
                            self.maps.insert(self.text(&left), value.clone());
                            return Ok(value);
                        }};}
                all_ops!(arity = 2);
                Err(ParsingError)
            }
            "identifier" => {
                if !self.maps.contains_key(self.text(&node)) {
                    let v = OwnedExpr::Var(self.var);
                    self.var += 1;
                    self.maps.insert(self.text(&node), v.clone());
                    Ok(v)
                } else {
                    Ok(self.maps[self.text(node)].clone())
                }
            }
            "binary_operator" => {
                let left = node.children_by_field_name("left", cursor).next().ok_or(ParsingError)?;
                let operator = node.children_by_field_name("operator", cursor).next().ok_or(ParsingError)?;
                let mut op = self.text(&operator);
                let right = node.children_by_field_name("right", cursor).next().ok_or(ParsingError)?;
                if op == "//" { op = "/"; }
                macro_rules! foreach_op {
                    ($name: ident) => {
                      if op == crate::symbol_of!($name) {
                        return Ok(OwnedExpr::$name(self.parse(&left)?.into(), self.parse(&right)?.into()));
                      }};}
                all_ops!(arity = 2);
                Err(ParsingError)
            }
            "parenthesized_expression" => {
                self.parse(children(node).first().ok_or(ParsingError)?)
            }
            "unary_operator" => {
                let operator = node.children_by_field_name("operator", cursor).next().ok_or(ParsingError)?;
                let argument = node.children_by_field_name("argument", cursor).next().ok_or(ParsingError)?;
                let op = self.text(&operator);
                macro_rules! foreach_op {
                    ($name: ident) => {
                      if op == crate::symbol_of!($name) {
                        return Ok(OwnedExpr::$name(self.parse(&argument)?.into())); }};}
                all_ops!(arity = 1);
                Err(ParsingError)
            }
            "integer" => {
                let value = u64_constant_python(self.text(node)).ok_or(ParsingError)?;
                Ok(OwnedExpr::Const(value))
            }
            "arrow_bracket" => {
                let v = OwnedExpr::Var(self.var);
                self.var += 1;
                Ok(v)
            }
            "ERROR" => {
                for n in node.children(cursor) {
                    if let Ok(e) = self.parse(&n) {
                        return Ok(e)
                    }
                }
                Err(ParsingError)
            }
            _ => { Err(ParsingError) }
        }
    }
    pub fn parse_md(&mut self, node: &tree_sitter::Node) -> Result<(), ParsingError> {
        let mut pyparser = Parser::new();
        let py = unsafe { tree_sitter_python() };
        pyparser.set_language(py).unwrap();
        let cursor = &mut node.walk();
        for ch in node.children(cursor) {
            match ch.kind() {
                "fenced_code_block" | "code_span" => {
                    let n = ch.child(0).ok_or(ParsingError)?;
                    let orig_text = self.text;
                    let text = n.utf8_text(orig_text.as_bytes()).unwrap();
                    let tree = pyparser.parse(text, None).unwrap();
                    self.text = text;
                    let res = self.parse(&tree.root_node())?;
                    self.maps.insert("last", res);
                    self.text = orig_text;
                },
                "text" => {
                    let orig_text = self.text;
                    let text = ch.utf8_text(orig_text.as_bytes()).unwrap();
                    if text.contains("=") {
                        let tree = pyparser.parse(text, None).unwrap();
                        self.text = text;
                        let res = self.parse(&tree.root_node())?;
                        self.maps.insert("last", res);
                        self.text = orig_text;
                    }
                }
                _ => {
                    self.parse_md(&ch)?;
                }
            }
        }
        Ok(())
    }
    pub fn parse_response(text: &'a str, nargs: usize) -> Result<Env, ParsingError> {
        let mut mdparser = Parser::new();
        let md = unsafe { tree_sitter_markdown() };
        mdparser.set_language(md).unwrap();
        let mut pyparser = Parser::new();
        let py = unsafe { tree_sitter_python() };
        pyparser.set_language(py).unwrap();
        
        let tree = mdparser.parse(text, None).unwrap();
        let root_node = tree.root_node();
        let mut cursor = root_node.walk();
        let mut env = Env { maps: HashMap::new(), var: 0, text };
        if let Ok(r) = env.parse_md(&root_node) {
            if env.var <= nargs {
                return Ok(env);
            }
        }
        Err(ParsingError)
    }
}






