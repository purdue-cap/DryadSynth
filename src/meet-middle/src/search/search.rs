use std::{cmp::min, thread, sync::Arc};

use bumpalo::Bump;
use futures::executor::block_on;
use rand::{thread_rng, seq::SliceRandom};
use rsmt2::SmtConf;
use serde::{Deserialize, Serialize};
use spin::Mutex;
use tokio::task::block_in_place;

use crate::{parse::{SynthProblem, PbeConstraint, self, constraint::RefImplConstraint}, solutions::Solutions, enumerate::expr::{Expr, OwnedExpr}, tree_learning::{TreeLearning, self}, info, cegis::CegisState, log};

use super::sample::SampleConfig;


#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct SearchConfig {
    pub ar_ratio: usize,
    pub ite_limit: usize,
    pub no_ite: bool,
    pub smt_solver: String,
    pub random_example: usize,
    pub additional_check: usize,
    pub chatgpt: bool,
    pub gpt_version: String,
    pub cond_search: SampleConfig,
    pub expr_search: SampleConfig,
    pub improve_search: SampleConfig,
    pub is_pbe: bool,
}


impl SearchConfig {
    pub fn default_pbe() -> Self {
        Self { random_example: 1, additional_check: 0, ar_ratio: 2, ite_limit: 100000, smt_solver: "bitwuzla".into(), no_ite: false, chatgpt: false, cond_search: SampleConfig::cond_default(), expr_search: SampleConfig::expr_default(), improve_search: SampleConfig::improve_default(), gpt_version: "gpt-3.5-turbo-0301".into(), is_pbe: false}
    }
    pub fn default_refimpl() -> Self {
        Self { random_example: 50, additional_check: 0, ar_ratio: 2, ite_limit: 100000, smt_solver: "bitwuzla".into(), no_ite: true, chatgpt: true, cond_search: SampleConfig::cond_default(), expr_search: SampleConfig::expr_default_ni(), improve_search: SampleConfig::improve_default(), gpt_version: "gpt-3.5-turbo-0301".into(), is_pbe: false}
    }
    pub fn search(&mut self, problem: & SynthProblem<'static>, pbecstr: &PbeConstraint, additional_example: PbeConstraint) -> parse::Result<OwnedExpr> {
        let mut no_ite = self.no_ite;
        if self.ite_limit == 1 || pbecstr.len() <= 1{
            no_ite = true;
        }
        let cond = Arc::new(Mutex::new(Vec::new()));
        if self.no_ite {
            self.cond_search.filter.count_limit = 10000;
        }
        thread::spawn({
            let problem = problem.clone();
            let pbecstr = pbecstr.clone();
            let cond_search = self.cond_search.clone();
            let cond = cond.clone();
            let l = log::log_level();
            move || {
                // log::set_log_level(l);
                let cond_cstr = &pbecstr.clone().set_output(0);
                let mut conds = Solutions::new(cond_cstr, false, true);
                conds.cond_buffer = Some(cond);
                let mut rng = rand::thread_rng();
                let problem = problem;
                
                info!("Searching Conditions.");
                for shift in 0..64 {
                    use Expr::*;
                    for i in 0..problem.args.len() {
                        conds.add_solution(&And(&LShr(&Var(i), &Const(shift)), &Const(1)));
                    }
                }
                cond_search.search(&mut conds, &problem, &mut rng).unwrap();
                
                info!("{} Condition Searched.", conds.solved.len())
        }});
        

        let mut rng = rand::thread_rng();
        
        info!("Searching Expressions.");
        let mut exprs = Solutions::new(pbecstr, true, false);
        exprs.additional_check = additional_example;
        
        if self.no_ite {
            self.expr_search.cover_limit = pbecstr.len();
            self.expr_search.partial_solution = false;
            exprs.sample_all = true;
            if exprs.pbecstr.len() > 5 && !self.is_pbe {
                exprs.wait_cond = 300;
            }
            exprs.check_tree_learning = Some((cond.clone(), min(self.ite_limit, 7)));
        }

        while exprs.remaining() > 0 {
            if !self.expr_search.search(&mut exprs, problem, &mut rng)? {
                self.expr_search.filter.count_limit_solved *= self.ar_ratio;
            }
        }
        exprs.sample_all = true;
        exprs.check_tree_learning = Some((cond, self.ite_limit));
        if exprs.tree_learning() { return Ok(exprs.get_tree()); }

        if !no_ite {
            info!("Improving Expressions.");
            self.improve_search.cover_limit = pbecstr.len() / ((self.ite_limit + 1) / 2 - 1);
            self.improve_search.filter.count_limit_solved += self.expr_search.filter.count_limit_solved;
            while !self.improve_search.search(&mut exprs, problem, &mut rng)? {
                self.improve_search.filter.count_limit_solved *= self.ar_ratio;
            }
        }
        
        Ok(exprs.get_tree())
    }
    
    // #[inline(always)]
    // pub fn tree_learing(exprs: &Solutions, conds: &Solutions, pbe: &PbeConstraint, size_limit: usize) -> Option<OwnedExpr> {
    //     let bump = Bump::new();
    //     let tl = tree_learning::tree_learning(exprs.to_bits(), conds.to_bits(), pbe.len(), &bump, size_limit);
    //     if tl.result_size() < size_limit { Some(tl.bvexpr()) } else { None }
    // }
    
    pub fn cegis_loop(&mut self, problem: &SynthProblem<'static>, refimpl: &RefImplConstraint) -> parse::Result<OwnedExpr> {
        info!("Starting CEGIS Loop.");
        let mut cegis = CegisState::new(problem.args.len(), refimpl.clone(), self.get_smt_conf());
        let no_additional_check = PbeConstraint::new(problem.args.len());
        let mut additional_check = PbeConstraint::new(problem.args.len());
        {
            let mut rng = thread_rng();
            for _ in 0..self.additional_check {
                let mask = [u64::MAX, u64::MAX, u64::MAX, u64::MAX, 0xFF, 0xFFFFFFFFFFFF, 0xFFFFFF0000FFFFFF ].choose(&mut rng).unwrap();
                additional_check.random_example(&mut rng, *mask, &refimpl.ref_impl, problem.args.len())
            }
            for _ in 0..std::cmp::max(self.random_example, 1) {
                let mask = [u64::MAX, u64::MAX, u64::MAX, u64::MAX, 0xFF, 0xFFFFFFFFFFFF, 0xFFFFFF0000FFFFFF ].choose(&mut rng).unwrap();
                cegis.examples.random_example(&mut rng, *mask, &refimpl.ref_impl, problem.args.len());
            }
        }

        let mut result = self.search(&problem, &cegis.examples, no_additional_check.clone())?;
        while !cegis.verify(&result) {
            result = self.search(&problem, &cegis.examples, if cegis.examples.len() > 8 { additional_check.clone() } else { no_additional_check.clone() } )?;
        }
        Ok(result)
    }
    pub fn cegis_check<'a>(&mut self, problem: &SynthProblem<'a>, refimpl: &RefImplConstraint, expr: &OwnedExpr) -> bool {
        let mut cegis = CegisState::new(problem.args.len(), refimpl.clone(), self.get_smt_conf());
        cegis.verify(expr)
    }
    pub fn get_smt_conf<'a>(&self) -> SmtConf {
        match &*self.smt_solver {
            "z3" => SmtConf::default_z3(),
            "bitwuzla" => {
                let mut conf = SmtConf::yices_2("bitwuzla");
                conf.option("-i");
                conf.option("--smt2");
                conf.option("--model-gen");
                conf
            }
            s => panic!("Unrecongized Solver: {}", s),
        }
    }
    
}
unsafe impl Send for SearchConfig {}