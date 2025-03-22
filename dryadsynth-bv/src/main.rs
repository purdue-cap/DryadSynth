#![feature(portable_simd)]
#![feature(const_trait_impl)]
#![feature(local_key_cell_methods)]
#![feature(map_try_insert)]
#![feature(unchecked_math)]
#![feature(int_roundings)]
#![feature(slice_pattern)]
#![feature(core_intrinsics)]
#![feature(return_position_impl_trait_in_trait)]
#![feature(inherent_associated_types)]

use std::{fs, env, sync::mpsc, thread};

use clap::Parser;
use enumerate::expr::OwnedExpr;
use figment::{Figment, providers::{Toml, Format, Serialized}};
use itertools::Itertools;
use openai::set_key;
use parse::{constraint::RefImplConstraint, deffun, PbeConstraint};


use crate::{parse::{SExpr, SynthProblem}, search::{search::SearchConfig}};
extern crate pest;
extern crate pest_derive;

mod enumerate;
mod deductive;
mod log;
mod cegis;
mod parse;
mod solutions;
mod tree_learning;
mod search;
pub mod hash;
pub mod chatgpt;

#[derive(Debug, Parser)] // requires `derive` feature
#[command(name = "bvmiddle")]
struct Cli {
    #[arg(short, long, action = clap::ArgAction::Count)]
    verbose: u8,
    path: String,
    #[arg(short, long)]
    config: Option<String>,
}

// pub fn solve_pbe(problem: SynthProblem, pbecstr: PbeConstraint) -> Self


fn main_inner() -> Result<OwnedExpr, Box<dyn std::error::Error>> {
    let args = Cli::parse();
    log::set_log_level(args.verbose + 2);

    let input = fs::read_to_string(args.path)?;
    
    let mut description = String::new();
    for a in input.split("\n") {
        let b = a.trim();
        if b.starts_with(";") {
            description.push_str(&b[1..]);
        } else { break }
    }
    
    let i: &'static str = unsafe { you_can::borrow_unchecked(&input) };
    let sexpr = SExpr::parse(&i)?;

    deffun::ENV.parse(&sexpr)?;

    let mut problem = SynthProblem::parse(&sexpr)?;
    problem.description = description;
    let pbecstr = PbeConstraint::parse(problem.name, problem.args.len(), &sexpr)?;
    if pbecstr.len() > 0 {
        let fig = Figment::new().merge(Serialized::defaults(SearchConfig::default_pbe()));
        let fig = if let Some(path) = args.config { fig.merge(Toml::file(path)) } else { fig };
        let mut config : SearchConfig = fig.extract()?;
        config.is_pbe = true;

        let result = config.search(&problem, &pbecstr, PbeConstraint::new(problem.args.len()))?;
        Ok(result)
    } else if let Some(refimpl) = RefImplConstraint::parse(&sexpr)? {
        let fig = Figment::new().merge(Serialized::defaults(SearchConfig::default_refimpl()));
        let fig = if let Some(path) = args.config { fig.merge(Toml::file(path)) } else { fig };
        let mut config : SearchConfig = fig.extract()?;
        if let Ok(a) = env::var("OPENAI_API_KEY") {
            set_key(a);
        } else {
            config.chatgpt = false;
        }
        if config.chatgpt && problem.description != "" {
            let l = log::log_level();
            let (sender, receiver) = mpsc::channel();
            let sd = sender.clone();
            let _ = thread::spawn({
                let (mut config, problem, refimpl) = (config.clone(), problem.clone(), refimpl.clone());
                move || { sd.send(config.cegis_loop(&problem, &refimpl)) }
            });
            let _ = thread::spawn(move || {
                log::set_log_level(l);
                let pbe = refimpl.generate_pbe(3, problem.args.len(), &mut config.rng());
                info!("Use ChatGPT to guide the search");
                let result = chatgpt::ask(& problem, pbe.clone(), config.gpt_version.clone());
                for r in result.iter() {
                    if let Some(last) = r.get("last") {
                        if pbe.test_owned(last).len() >= 3 {
                            if config.cegis_check(&problem, &refimpl, last) {
                                sender.send(Ok(last.clone())).unwrap();
                                return;
                            }
                        }
                    }
                }
                let map = chatgpt::collect_subexpr(result);
                let v = map.into_iter().flat_map(|(e, i)| if i >= 4 && e.size() < 10 { Some(e) } else { None }).collect_vec();
                if v.len() > 0 {
                    info!("Use ChatGPT to guide the search: {:?}", v);
                    problem.chatgpt_exprs = v;
                    sender.send(config.cegis_loop(&problem, &refimpl)).unwrap();
                }
            });
            let a = receiver.recv().unwrap()?;
            Ok(a)
        } else {
            let result = config.cegis_loop(&problem, &refimpl)?;
            Ok(result)
        }
    } else { panic!() }
}

#[cfg(feature = "dhat-heap")]
#[global_allocator]
static ALLOC: dhat::Alloc = dhat::Alloc;

pub fn main() -> Result<(), Box<dyn std::error::Error>> {
    #[cfg(feature = "dhat-heap")]
    let _profiler = dhat::Profiler::new_heap();
    
    
    println!("{}", main_inner()?);
    Ok(())
}