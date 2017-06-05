// Automatically generated file
#ifndef __SAT_PARAMS_HPP_
#define __SAT_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct sat_params {
  params_ref const & p;
  params_ref g;
  sat_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("sat")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("max_memory", CPK_UINT, "maximum amount of memory in megabytes", "4294967295","sat");
    d.insert("phase", CPK_SYMBOL, "phase selection strategy: always_false, always_true, caching, random", "caching","sat");
    d.insert("phase.caching.on", CPK_UINT, "phase caching on period (in number of conflicts)", "400","sat");
    d.insert("phase.caching.off", CPK_UINT, "phase caching off period (in number of conflicts)", "100","sat");
    d.insert("restart", CPK_SYMBOL, "restart strategy: luby or geometric", "luby","sat");
    d.insert("restart.initial", CPK_UINT, "initial restart (number of conflicts)", "100","sat");
    d.insert("restart.factor", CPK_DOUBLE, "restart increment factor for geometric strategy", "1.5","sat");
    d.insert("random_freq", CPK_DOUBLE, "frequency of random case splits", "0.01","sat");
    d.insert("random_seed", CPK_UINT, "random seed", "0","sat");
    d.insert("burst_search", CPK_UINT, "number of conflicts before first global simplification", "100","sat");
    d.insert("max_conflicts", CPK_UINT, "maximum number of conflicts", "4294967295","sat");
    d.insert("gc", CPK_SYMBOL, "garbage collection strategy: psm, glue, glue_psm, dyn_psm", "glue_psm","sat");
    d.insert("gc.initial", CPK_UINT, "learned clauses garbage collection frequence", "20000","sat");
    d.insert("gc.increment", CPK_UINT, "increment to the garbage collection threshold", "500","sat");
    d.insert("gc.small_lbd", CPK_UINT, "learned clauses with small LBD are never deleted (only used in dyn_psm)", "3","sat");
    d.insert("gc.k", CPK_UINT, "learned clauses that are inactive for k gc rounds are permanently deleted (only used in dyn_psm)", "7","sat");
    d.insert("minimize_lemmas", CPK_BOOL, "minimize learned clauses", "true","sat");
    d.insert("dyn_sub_res", CPK_BOOL, "dynamic subsumption resolution for minimizing learned clauses", "true","sat");
    d.insert("core.minimize", CPK_BOOL, "minimize computed core", "false","sat");
    d.insert("core.minimize_partial", CPK_BOOL, "apply partial (cheap) core minimization", "false","sat");
    d.insert("optimize_model", CPK_BOOL, "enable optimization of soft constraints", "false","sat");
    d.insert("bcd", CPK_BOOL, "enable blocked clause decomposition for equality extraction", "false","sat");
    d.insert("dimacs.core", CPK_BOOL, "extract core from DIMACS benchmarks", "false","sat");
  }
  /*
     REG_MODULE_PARAMS('sat', 'sat_params::collect_param_descrs')
     REG_MODULE_DESCRIPTION('sat', 'propositional SAT solver')
  */
  unsigned max_memory() const { return p.get_uint("max_memory", g, 4294967295u); }
  symbol phase() const { return p.get_sym("phase", g, symbol("caching")); }
  unsigned phase_caching_on() const { return p.get_uint("phase.caching.on", g, 400u); }
  unsigned phase_caching_off() const { return p.get_uint("phase.caching.off", g, 100u); }
  symbol restart() const { return p.get_sym("restart", g, symbol("luby")); }
  unsigned restart_initial() const { return p.get_uint("restart.initial", g, 100u); }
  double restart_factor() const { return p.get_double("restart.factor", g, 1.5); }
  double random_freq() const { return p.get_double("random_freq", g, 0.01); }
  unsigned random_seed() const { return p.get_uint("random_seed", g, 0u); }
  unsigned burst_search() const { return p.get_uint("burst_search", g, 100u); }
  unsigned max_conflicts() const { return p.get_uint("max_conflicts", g, 4294967295u); }
  symbol gc() const { return p.get_sym("gc", g, symbol("glue_psm")); }
  unsigned gc_initial() const { return p.get_uint("gc.initial", g, 20000u); }
  unsigned gc_increment() const { return p.get_uint("gc.increment", g, 500u); }
  unsigned gc_small_lbd() const { return p.get_uint("gc.small_lbd", g, 3u); }
  unsigned gc_k() const { return p.get_uint("gc.k", g, 7u); }
  bool minimize_lemmas() const { return p.get_bool("minimize_lemmas", g, true); }
  bool dyn_sub_res() const { return p.get_bool("dyn_sub_res", g, true); }
  bool core_minimize() const { return p.get_bool("core.minimize", g, false); }
  bool core_minimize_partial() const { return p.get_bool("core.minimize_partial", g, false); }
  bool optimize_model() const { return p.get_bool("optimize_model", g, false); }
  bool bcd() const { return p.get_bool("bcd", g, false); }
  bool dimacs_core() const { return p.get_bool("dimacs.core", g, false); }
};
#endif
