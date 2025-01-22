use std::{simd::{SupportedLaneCount, LaneCount}, cmp::min};

use bumpalo::Bump;
use itertools::Itertools;
use rand::rngs::StdRng;
use serde::{Deserialize, Serialize};

use crate::{parse::{SynthProblem, PbeConstraint, self}, solutions::Solutions, info, enumerate::{config::{Config, Rule}, algo::Algo, algo2, Algo as AAlgo, algo3, algo4}, deductive::{combine::CombineRules, reverse::ReverseRule}};

use super::filter::FilterConfig;



#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct SampleConfig {
    pub sample: usize,
    pub cover_limit: usize,
    pub partial_solution: bool,
    pub dag_size: bool,
    pub atom_diversity_order: bool,
    pub atom_shift: bool,
    pub filter: FilterConfig,
    pub mem_size_limit: usize,
}

impl SampleConfig {
    pub const fn cond_default() -> Self {
        Self { sample: 16, cover_limit: 8, partial_solution: true, filter: FilterConfig::cond_default(), dag_size: true, mem_size_limit: usize::MAX, atom_diversity_order: false, atom_shift: true }
    }
    pub const fn expr_default() -> Self {
        Self { sample: 8, cover_limit: 13, partial_solution: true, filter: FilterConfig::expr_default(), dag_size: true, mem_size_limit: usize::MAX, atom_diversity_order: false, atom_shift: true}
    }
    pub const fn expr_default_ni() -> Self {
        Self { sample: 64, cover_limit: 13, partial_solution: true, filter: FilterConfig::expr_default(), dag_size: true, mem_size_limit: usize::MAX, atom_diversity_order: false, atom_shift: true}
    }
    pub const fn improve_default() -> Self {
        Self { sample: 8, cover_limit: 13, partial_solution: true, filter: FilterConfig::improve_default(), dag_size: true, mem_size_limit: usize::MAX, atom_diversity_order: false, atom_shift: true}
    }

    pub fn search_inner<const N: usize>(&self, sol: &mut Solutions, problem: &SynthProblem, rng: &mut StdRng) -> parse::Result<bool> {
        let (args, out) = sol.sample::<N, _>(rng);
        info!("Sampling with: {:x?} -> {:x?}. {}", args, out, sol.pbecstr.len());
        let mut bump = Bump::new();
        let mut filter = self.filter.state(args.clone(), &out, &bump, self.partial_solution);
        let mut config = Config::from_problem(problem, self.partial_solution)?;
        let consts = config.iter().flat_map(|x| if let Rule::Const(c) = x { Some(c) } else { None }).collect_vec();
        // if self.atom_shift {
        //     config.0 = config.0.iter().flat_map(|x| {
        //         match x {
        //             Rule::Shl => consts.iter().map(|x| Rule::ShlC(**x)).collect_vec(),
        //             Rule::LShr => consts.iter().map(|x| Rule::LShrC(**x)).collect_vec(),
        //             Rule::AShr => consts.iter().map(|x| Rule::AShrC(**x)).collect_vec(),
        //             _ => vec![x.clone()],
        //         }
        //     }).collect_vec();
        // }

        if self.mem_size_limit == usize::MAX {
            let mut algo = Algo::new(config, args, out.clone(), self.dag_size, &bump);
            algo.run_until(|algo, e, v| filter.filter(algo, e, v, sol));
            let result = !filter.solved;
            info!("Searched for {} {} {} Expressions.", algo.len(), algo.count(), algo.count2());
            Ok(result)
        } else if self.atom_diversity_order {
            if self.atom_shift {
                let mut algo = algo4::Algo::new(config, args, out.clone(), self.dag_size, &bump, self.mem_size_limit);
                algo.run_until(|algo, e, v| filter.filter(algo, e, v, sol));
                let result = !filter.solved;
                info!("Searched for {} Expressions.", algo.count());
                Ok(result)
            } else {
                let mut algo = algo3::Algo::new(config, args, out.clone(), self.dag_size, &bump, self.mem_size_limit);
                algo.run_until(|algo, e, v| filter.filter(algo, e, v, sol));
                let result = !filter.solved;
                info!("Searched for {} Expressions.", algo.count());
                Ok(result)
            }
        } else {
            let mut algo = algo2::Algo::new(config, args, out.clone(), self.dag_size, &bump, self.mem_size_limit);
            algo.run_until(|algo, e, v| filter.filter(algo, e, v, sol));
            let result = !filter.solved;
            info!("Searched for {} Expressions.", algo.count());
            Ok(result)
        }
    }
    #[inline(always)]
    pub fn search(&self, sol: &mut Solutions, problem: &SynthProblem, rng: &mut StdRng) -> parse::Result<bool> {
        sol.set_cover_limit(if self.cover_limit == 0 {1} else {sol.pbecstr.len() / self.cover_limit});
        let arr = [1, 2, 4, 8, 16, 32, 48, 64, 80, 1024];
        let sample = arr[arr.binary_search(&min(self.sample, sol.pbecstr.len())).unwrap_or_else(|a| a)];
        match sample {
            1 => self.search_inner::<1>(sol, problem, rng),
            2 => self.search_inner::<2>(sol, problem, rng),
            4 => self.search_inner::<4>(sol, problem, rng),
            8 => self.search_inner::<8>(sol, problem, rng),
            16 => self.search_inner::<16>(sol, problem, rng),
            32 => self.search_inner::<32>(sol, problem, rng),
            48 => self.search_inner::<48>(sol, problem, rng),
            64 => self.search_inner::<64>(sol, problem, rng),
            80 => self.search_inner::<80>(sol, problem, rng),
            1024 => self.search_inner::<1024>(sol, problem, rng),
            _ => panic!("Unexpected sample count: {}. Expected numbers are 1, 2, 4, 8, 16, 32, 64.", sample)
        }
    }
}



