#![feature(local_key_cell_methods)]
#![feature(int_roundings)]
#![feature(unchecked_math)]
use std::{
    collections::{BTreeMap, BTreeSet},
    fs::{File, self},
    io::{self, BufRead},
    path::Path,
};

use algo::{FilterType, AlgoConf};
use bumpalo::Bump;
use bv::{expr::BvExpr, Context, Indices};
use clap::{command, Parser};
use cond::{TreeLearning, Bits};
use itertools::Itertools;
use regex::Regex;
mod cond;
mod parsing;

use crate::{algo::Algo, utils::BoxSliceExt, parsing::{PBEProblem, sexpr::SExpr}};

fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>>
where
    P: AsRef<Path>,
{
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}

mod algo;
mod bv;
mod log;
mod rule;
mod utils;

#[derive(Parser, Debug)]
#[command(
    author = "Yuantian Ding",
    version = "0.0.0",
    about = "a Sygus bitvec solver"
)]
struct Args {
    #[arg(short, long, action = clap::ArgAction::Count)]
    verbose: u8,
    path: String,
}

fn read_constraint(path: String) -> Vec<(u64, u64)> {
    let hexliteral = Regex::new(r"#x([0-9a-fA-F]+)").unwrap();
    let mut io = Vec::new();
    if let Ok(lines) = read_lines(path) {
        for line in lines {
            if let Ok(line) = line {
                if line.starts_with("(constraint ") {
                    let mut iter = hexliteral.captures_iter(line.as_str());
                    let i = u64::from_str_radix(&iter.next().unwrap()[1], 16).unwrap();
                    let o = u64::from_str_radix(&iter.next().unwrap()[1], 16).unwrap();
                    io.push((i, o))
                }
            }
        }
    }
    io
}

fn run_algorithm(problem: &PBEProblem, indices: Vec<usize>) -> Vec<BvExpr> {
   
    let bump = Bump::new();
    let conf = problem.algo_conf();
    let mut ctx = Context::new(&bump, indices.len());
    for k in &conf.varibles {
        ctx.new_var(indices.iter().map(|i| problem.input[*k].1[*i]));
    }
    let out = ctx.alloc_bv(indices.iter().map(|i| problem.output[*i]));
    for v in &conf.constants {
        let _ = ctx.new_const(*v);
    }

    let mut state = Algo::new(&ctx, out, false, conf);
    state.filter = FilterType::OnlyBetterSolution;
    state.run(10000);
    state.filter = FilterType::OnlyUnsolved;
    state.run(usize::MAX);
    
    state.solution.iter().map(|(rule, _)| rule.to_expr()).collect()
}

fn search_conditions(problem: &PBEProblem) -> Vec<(BvExpr, Vec<usize>)> {
   
    let bump = Bump::new();
    let conf = problem.algo_conf();
    let mut ctx = Context::new(&bump, problem.output.len());
    for k in &conf.varibles {
        ctx.new_var(problem.input[*k].1.iter().cloned());
    }
    let out = ctx.alloc_bv(problem.output.iter().map(|_| 1));
    for v in &conf.constants {
        let _ = ctx.new_const(0);
    }

    let mut state = Algo::new(&ctx, out, true, conf);
    state.filter = FilterType::OnlyDifferentSolution;
    state.run(10000);
    state.solution.into_iter().map(|(rule, v)| (rule.to_expr(), v)).collect()
    
}

fn tree_learning(options: Vec<(BvExpr, Bits)>, conditions: Vec<(BvExpr, Bits)>, size: usize) -> BvExpr {
    let bump = Bump::new();
    let mut tl = TreeLearning::new_in(size, options, &bump);
    tl.conditions = conditions;
    tl.run();
    tl.bvexpr()
    // println!("{:?}", tl.result_size());
}

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let args = Args::parse();
    log::set_log_level(args.verbose + 2);
    let input = fs::read_to_string(args.path)?;
    let (_, sexpr) = SExpr::parse(&*input).expect("Error parsing: not s-expression.");
    let problem = PBEProblem::from(sexpr);
    let conf = problem.algo_conf();
    // println!("{:?}", conf);
    let nexamples = problem.output.len();
    let mut solutions: Vec<(BvExpr, Vec<usize>)> = Vec::new();
    let mut unsolved: BTreeSet<_> = (0..nexamples).collect();
    let mut counter = 0;
    while !unsolved.is_empty() {
      counter += 1;
      info!("Attempt {}, {} examples remain", counter, unsolved.len());
        for expr in run_algorithm(& problem, unsolved.iter().cloned().take(35).collect()) {
            let solved: Vec<_> = (0..nexamples).flat_map(|i| {
                    if expr.eval(&conf.varibles.iter().map(|k| problem.input[*k].1[i]).collect(), &conf.constants) == problem.output[i] {
                        unsolved.remove(&i);
                        Some(i)
                    } else { None }
                }).collect();
            solutions.retain(|sol| !Indices(sol.1.as_slice()).subset(&Indices(solved.as_slice())) );
            solutions.push((expr, solved));
        }
    }

    info!("Searching Conditions");
    let conds = search_conditions(& problem);

    // for (expr, cases) in solutions.iter() {
    //     println!("expression {:?} covers {} examples.", expr, cases.len());
    // }

    let options: Vec<_> = solutions.into_iter().map(|(a, b)| (a, Bits::from_bit_iter(b.into_iter(), nexamples)) ).collect();
    let mut conds: Vec<_> = conds.into_iter().map(|(a, b)| (a, Bits::from_bit_iter(b.into_iter(), nexamples))).collect();
    
    if conf.and && conf.shl {
        if let Some((p, _)) = conf.constants.iter().enumerate().find(|x| *x.1 == 1) {
            for v in &conf.varibles {
                for shift in 0..64 {
                    use BvExpr::*;
                    let expr = And(Symbol(-((p + 1) as i16)).into(), Shr(Symbol((v + 1) as i16).into(), shift).into());
                    let cover = (0..nexamples).flat_map(|i| {
                        if expr.eval(&conf.varibles.iter().map(|k| problem.input[*k].1[i]).collect(), &conf.constants) == 1 {
                            Some(i)
                        } else { None }
                    });
                    let cover = Box::from_bit_iter(cover, nexamples);
                    conds.push((expr, cover));
                }
            }
        }
    }
    // for (expr, cases) in conds.iter() {
    //     println!("condition {:?} covers {:x?}.", expr, cases);
    // }

    let expr = tree_learning(options, conds, nexamples);
    let result = expr.to_sexpr(&conf.varibles.iter().map(|k| &*problem.input[*k].0).collect_vec(), &conf.constants);
    println!("{}", result);
    Ok(())
}
