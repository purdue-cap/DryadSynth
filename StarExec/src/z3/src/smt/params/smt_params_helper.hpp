// Automatically generated file
#ifndef __SMT_PARAMS_HELPER_HPP_
#define __SMT_PARAMS_HELPER_HPP_
#include"params.h"
#include"gparams.h"
struct smt_params_helper {
  params_ref const & p;
  params_ref g;
  smt_params_helper(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("smt")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("auto_config", CPK_BOOL, "automatically configure solver", "true","smt");
    d.insert("logic", CPK_SYMBOL, "logic used to setup the SMT solver", "","smt");
    d.insert("random_seed", CPK_UINT, "random seed for the smt solver", "0","smt");
    d.insert("relevancy", CPK_UINT, "relevancy propagation heuristic: 0 - disabled, 1 - relevancy is tracked by only affects quantifier instantiation, 2 - relevancy is tracked, and an atom is only asserted if it is relevant", "2","smt");
    d.insert("macro_finder", CPK_BOOL, "try to find universally quantified formulas that can be viewed as macros", "false","smt");
    d.insert("ematching", CPK_BOOL, "E-Matching based quantifier instantiation", "true","smt");
    d.insert("phase_selection", CPK_UINT, "phase selection heuristic: 0 - always false, 1 - always true, 2 - phase caching, 3 - phase caching conservative, 4 - phase caching conservative 2, 5 - random, 6 - number of occurrences", "3","smt");
    d.insert("restart_strategy", CPK_UINT, "0 - geometric, 1 - inner-outer-geometric, 2 - luby, 3 - fixed, 4 - arithmetic", "1","smt");
    d.insert("restart_factor", CPK_DOUBLE, "when using geometric (or inner-outer-geometric) progression of restarts, it specifies the constant used to multiply the currect restart threshold", "1.1","smt");
    d.insert("case_split", CPK_UINT, "0 - case split based on variable activity, 1 - similar to 0, but delay case splits created during the search, 2 - similar to 0, but cache the relevancy, 3 - case split based on relevancy (structural splitting), 4 - case split on relevancy and activity, 5 - case split on relevancy and current goal", "1","smt");
    d.insert("delay_units", CPK_BOOL, "if true then z3 will not restart when a unit clause is learned", "false","smt");
    d.insert("delay_units_threshold", CPK_UINT, "maximum number of learned unit clauses before restarting, ignored if delay_units is false", "32","smt");
    d.insert("pull_nested_quantifiers", CPK_BOOL, "pull nested quantifiers", "false","smt");
    d.insert("refine_inj_axioms", CPK_BOOL, "refine injectivity axioms", "true","smt");
    d.insert("timeout", CPK_UINT, "timeout (in milliseconds) (0 means immediate timeout)", "4294967295","smt");
    d.insert("rlimit", CPK_UINT, "resource limit (0 means no limit)", "0","smt");
    d.insert("max_conflicts", CPK_UINT, "maximum number of conflicts before giving up.", "4294967295","smt");
    d.insert("mbqi", CPK_BOOL, "model based quantifier instantiation (MBQI)", "true","smt");
    d.insert("mbqi.max_cexs", CPK_UINT, "initial maximal number of counterexamples used in MBQI, each counterexample generates a quantifier instantiation", "1","smt");
    d.insert("mbqi.max_cexs_incr", CPK_UINT, "increment for MBQI_MAX_CEXS, the increment is performed after each round of MBQI", "0","smt");
    d.insert("mbqi.max_iterations", CPK_UINT, "maximum number of rounds of MBQI", "1000","smt");
    d.insert("mbqi.trace", CPK_BOOL, "generate tracing messages for Model Based Quantifier Instantiation (MBQI). It will display a message before every round of MBQI, and the quantifiers that were not satisfied", "false","smt");
    d.insert("mbqi.force_template", CPK_UINT, "some quantifiers can be used as templates for building interpretations for functions. Z3 uses heuristics to decide whether a quantifier will be used as a template or not. Quantifiers with weight >= mbqi.force_template are forced to be used as a template", "10","smt");
    d.insert("mbqi.id", CPK_STRING, "Only use model-based instantiation for quantifiers with id's beginning with string", "","smt");
    d.insert("qi.profile", CPK_BOOL, "profile quantifier instantiation", "false","smt");
    d.insert("qi.profile_freq", CPK_UINT, "how frequent results are reported by qi.profile", "4294967295","smt");
    d.insert("qi.max_instances", CPK_UINT, "maximum number of quantifier instantiations", "4294967295","smt");
    d.insert("qi.eager_threshold", CPK_DOUBLE, "threshold for eager quantifier instantiation", "10.0","smt");
    d.insert("qi.lazy_threshold", CPK_DOUBLE, "threshold for lazy quantifier instantiation", "20.0","smt");
    d.insert("qi.cost", CPK_STRING, "expression specifying what is the cost of a given quantifier instantiation", "(+ weight generation)","smt");
    d.insert("qi.max_multi_patterns", CPK_UINT, "specify the number of extra multi patterns", "0","smt");
    d.insert("bv.reflect", CPK_BOOL, "create enode for every bit-vector term", "true","smt");
    d.insert("bv.enable_int2bv", CPK_BOOL, "enable support for int2bv and bv2int operators", "true","smt");
    d.insert("arith.random_initial_value", CPK_BOOL, "use random initial values in the simplex-based procedure for linear arithmetic", "false","smt");
    d.insert("arith.solver", CPK_UINT, "arithmetic solver: 0 - no solver, 1 - bellman-ford based solver (diff. logic only), 2 - simplex based solver, 3 - floyd-warshall based solver (diff. logic only) and no theory combination", "2","smt");
    d.insert("arith.nl", CPK_BOOL, "(incomplete) nonlinear arithmetic support based on Groebner basis and interval propagation", "true","smt");
    d.insert("arith.nl.gb", CPK_BOOL, "groebner Basis computation, this option is ignored when arith.nl=false", "true","smt");
    d.insert("arith.nl.branching", CPK_BOOL, "branching on integer variables in non linear clusters", "true","smt");
    d.insert("arith.nl.rounds", CPK_UINT, "threshold for number of (nested) final checks for non linear arithmetic", "1024","smt");
    d.insert("arith.euclidean_solver", CPK_BOOL, "eucliean solver for linear integer arithmetic", "false","smt");
    d.insert("arith.propagate_eqs", CPK_BOOL, "propagate (cheap) equalities", "true","smt");
    d.insert("arith.propagation_mode", CPK_UINT, "0 - no propagation, 1 - propagate existing literals, 2 - refine bounds", "2","smt");
    d.insert("arith.branch_cut_ratio", CPK_UINT, "branch/cut ratio for linear integer arithmetic", "2","smt");
    d.insert("arith.int_eq_branch", CPK_BOOL, "branching using derived integer equations", "false","smt");
    d.insert("arith.ignore_int", CPK_BOOL, "treat integer variables as real", "false","smt");
    d.insert("arith.dump_lemmas", CPK_BOOL, "dump arithmetic theory lemmas to files", "false","smt");
    d.insert("arith.greatest_error_pivot", CPK_BOOL, "Pivoting strategy", "false","smt");
    d.insert("pb.conflict_frequency", CPK_UINT, "conflict frequency for Pseudo-Boolean theory", "1000","smt");
    d.insert("pb.learn_complements", CPK_BOOL, "learn complement literals for Pseudo-Boolean theory", "true","smt");
    d.insert("pb.enable_compilation", CPK_BOOL, "enable compilation into sorting circuits for Pseudo-Boolean", "true","smt");
    d.insert("pb.enable_simplex", CPK_BOOL, "enable simplex to check rational feasibility", "false","smt");
    d.insert("array.weak", CPK_BOOL, "weak array theory", "false","smt");
    d.insert("array.extensional", CPK_BOOL, "extensional array theory", "true","smt");
    d.insert("dack", CPK_UINT, "0 - disable dynamic ackermannization, 1 - expand Leibniz's axiom if a congruence is the root of a conflict, 2 - expand Leibniz's axiom if a congruence is used during conflict resolution", "1","smt");
    d.insert("dack.eq", CPK_BOOL, "enable dynamic ackermannization for transtivity of equalities", "false","smt");
    d.insert("dack.factor", CPK_DOUBLE, "number of instance per conflict", "0.1","smt");
    d.insert("dack.gc", CPK_UINT, "Dynamic ackermannization garbage collection frequency (per conflict)", "2000","smt");
    d.insert("dack.gc_inv_decay", CPK_DOUBLE, "Dynamic ackermannization garbage collection decay", "0.8","smt");
    d.insert("dack.threshold", CPK_UINT, " number of times the congruence rule must be used before Leibniz's axiom is expanded", "10","smt");
    d.insert("core.validate", CPK_BOOL, "validate unsat core produced by SMT context", "false","smt");
    d.insert("core.minimize", CPK_BOOL, "minimize unsat core produced by SMT context", "false","smt");
  }
  /*
     REG_MODULE_PARAMS('smt', 'smt_params_helper::collect_param_descrs')
     REG_MODULE_DESCRIPTION('smt', 'smt solver based on lazy smt')
  */
  bool auto_config() const { return p.get_bool("auto_config", g, true); }
  symbol logic() const { return p.get_sym("logic", g, symbol("")); }
  unsigned random_seed() const { return p.get_uint("random_seed", g, 0u); }
  unsigned relevancy() const { return p.get_uint("relevancy", g, 2u); }
  bool macro_finder() const { return p.get_bool("macro_finder", g, false); }
  bool ematching() const { return p.get_bool("ematching", g, true); }
  unsigned phase_selection() const { return p.get_uint("phase_selection", g, 3u); }
  unsigned restart_strategy() const { return p.get_uint("restart_strategy", g, 1u); }
  double restart_factor() const { return p.get_double("restart_factor", g, 1.1); }
  unsigned case_split() const { return p.get_uint("case_split", g, 1u); }
  bool delay_units() const { return p.get_bool("delay_units", g, false); }
  unsigned delay_units_threshold() const { return p.get_uint("delay_units_threshold", g, 32u); }
  bool pull_nested_quantifiers() const { return p.get_bool("pull_nested_quantifiers", g, false); }
  bool refine_inj_axioms() const { return p.get_bool("refine_inj_axioms", g, true); }
  unsigned timeout() const { return p.get_uint("timeout", g, 4294967295u); }
  unsigned rlimit() const { return p.get_uint("rlimit", g, 0u); }
  unsigned max_conflicts() const { return p.get_uint("max_conflicts", g, 4294967295u); }
  bool mbqi() const { return p.get_bool("mbqi", g, true); }
  unsigned mbqi_max_cexs() const { return p.get_uint("mbqi.max_cexs", g, 1u); }
  unsigned mbqi_max_cexs_incr() const { return p.get_uint("mbqi.max_cexs_incr", g, 0u); }
  unsigned mbqi_max_iterations() const { return p.get_uint("mbqi.max_iterations", g, 1000u); }
  bool mbqi_trace() const { return p.get_bool("mbqi.trace", g, false); }
  unsigned mbqi_force_template() const { return p.get_uint("mbqi.force_template", g, 10u); }
  char const * mbqi_id() const { return p.get_str("mbqi.id", g, ""); }
  bool qi_profile() const { return p.get_bool("qi.profile", g, false); }
  unsigned qi_profile_freq() const { return p.get_uint("qi.profile_freq", g, 4294967295u); }
  unsigned qi_max_instances() const { return p.get_uint("qi.max_instances", g, 4294967295u); }
  double qi_eager_threshold() const { return p.get_double("qi.eager_threshold", g, 10.0); }
  double qi_lazy_threshold() const { return p.get_double("qi.lazy_threshold", g, 20.0); }
  char const * qi_cost() const { return p.get_str("qi.cost", g, "(+ weight generation)"); }
  unsigned qi_max_multi_patterns() const { return p.get_uint("qi.max_multi_patterns", g, 0u); }
  bool bv_reflect() const { return p.get_bool("bv.reflect", g, true); }
  bool bv_enable_int2bv() const { return p.get_bool("bv.enable_int2bv", g, true); }
  bool arith_random_initial_value() const { return p.get_bool("arith.random_initial_value", g, false); }
  unsigned arith_solver() const { return p.get_uint("arith.solver", g, 2u); }
  bool arith_nl() const { return p.get_bool("arith.nl", g, true); }
  bool arith_nl_gb() const { return p.get_bool("arith.nl.gb", g, true); }
  bool arith_nl_branching() const { return p.get_bool("arith.nl.branching", g, true); }
  unsigned arith_nl_rounds() const { return p.get_uint("arith.nl.rounds", g, 1024u); }
  bool arith_euclidean_solver() const { return p.get_bool("arith.euclidean_solver", g, false); }
  bool arith_propagate_eqs() const { return p.get_bool("arith.propagate_eqs", g, true); }
  unsigned arith_propagation_mode() const { return p.get_uint("arith.propagation_mode", g, 2u); }
  unsigned arith_branch_cut_ratio() const { return p.get_uint("arith.branch_cut_ratio", g, 2u); }
  bool arith_int_eq_branch() const { return p.get_bool("arith.int_eq_branch", g, false); }
  bool arith_ignore_int() const { return p.get_bool("arith.ignore_int", g, false); }
  bool arith_dump_lemmas() const { return p.get_bool("arith.dump_lemmas", g, false); }
  bool arith_greatest_error_pivot() const { return p.get_bool("arith.greatest_error_pivot", g, false); }
  unsigned pb_conflict_frequency() const { return p.get_uint("pb.conflict_frequency", g, 1000u); }
  bool pb_learn_complements() const { return p.get_bool("pb.learn_complements", g, true); }
  bool pb_enable_compilation() const { return p.get_bool("pb.enable_compilation", g, true); }
  bool pb_enable_simplex() const { return p.get_bool("pb.enable_simplex", g, false); }
  bool array_weak() const { return p.get_bool("array.weak", g, false); }
  bool array_extensional() const { return p.get_bool("array.extensional", g, true); }
  unsigned dack() const { return p.get_uint("dack", g, 1u); }
  bool dack_eq() const { return p.get_bool("dack.eq", g, false); }
  double dack_factor() const { return p.get_double("dack.factor", g, 0.1); }
  unsigned dack_gc() const { return p.get_uint("dack.gc", g, 2000u); }
  double dack_gc_inv_decay() const { return p.get_double("dack.gc_inv_decay", g, 0.8); }
  unsigned dack_threshold() const { return p.get_uint("dack.threshold", g, 10u); }
  bool core_validate() const { return p.get_bool("core.validate", g, false); }
  bool core_minimize() const { return p.get_bool("core.minimize", g, false); }
};
#endif
