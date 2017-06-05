// Automatically generated file
#ifndef __OPT_PARAMS_HPP_
#define __OPT_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct opt_params {
  params_ref const & p;
  params_ref g;
  opt_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("opt")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("optsmt_engine", CPK_SYMBOL, "select optimization engine: 'basic', 'farkas', 'symba'", "basic","opt");
    d.insert("maxsat_engine", CPK_SYMBOL, "select engine for maxsat: 'core_maxsat', 'wmax', 'maxres', 'pd-maxres'", "maxres","opt");
    d.insert("priority", CPK_SYMBOL, "select how to priortize objectives: 'lex' (lexicographic), 'pareto', or 'box'", "lex","opt");
    d.insert("dump_benchmarks", CPK_BOOL, "dump benchmarks for profiling", "false","opt");
    d.insert("print_model", CPK_BOOL, "display model for satisfiable constraints", "false","opt");
    d.insert("enable_sls", CPK_BOOL, "enable SLS tuning during weighted maxsast", "false","opt");
    d.insert("enable_sat", CPK_BOOL, "enable the new SAT core for propositional constraints", "true","opt");
    d.insert("elim_01", CPK_BOOL, "eliminate 01 variables", "true","opt");
    d.insert("pp.neat", CPK_BOOL, "use neat (as opposed to less readable, but faster) pretty printer when displaying context", "true","opt");
    d.insert("pb.compile_equality", CPK_BOOL, "compile arithmetical equalities into pseudo-Boolean equality (instead of two inequalites)", "false","opt");
    d.insert("maxres.hill_climb", CPK_BOOL, "give preference for large weight cores", "true","opt");
    d.insert("maxres.add_upper_bound_block", CPK_BOOL, "restict upper bound with constraint", "false","opt");
    d.insert("maxres.max_num_cores", CPK_UINT, "maximal number of cores per round", "4294967295","opt");
    d.insert("maxres.max_core_size", CPK_UINT, "break batch of generated cores if size reaches this number", "3","opt");
    d.insert("maxres.maximize_assignment", CPK_BOOL, "find an MSS/MCS to improve current assignment", "false","opt");
    d.insert("maxres.max_correction_set_size", CPK_UINT, "allow generating correction set constraints up to maximal size", "3","opt");
    d.insert("maxres.wmax", CPK_BOOL, "use weighted theory solver to constrain upper bounds", "false","opt");
    d.insert("maxres.pivot_on_correction_set", CPK_BOOL, "reduce soft constraints if the current correction set is smaller than current core", "true","opt");
  }
  /*
     REG_MODULE_PARAMS('opt', 'opt_params::collect_param_descrs')
     REG_MODULE_DESCRIPTION('opt', 'optimization parameters')
  */
  symbol optsmt_engine() const { return p.get_sym("optsmt_engine", g, symbol("basic")); }
  symbol maxsat_engine() const { return p.get_sym("maxsat_engine", g, symbol("maxres")); }
  symbol priority() const { return p.get_sym("priority", g, symbol("lex")); }
  bool dump_benchmarks() const { return p.get_bool("dump_benchmarks", g, false); }
  bool print_model() const { return p.get_bool("print_model", g, false); }
  bool enable_sls() const { return p.get_bool("enable_sls", g, false); }
  bool enable_sat() const { return p.get_bool("enable_sat", g, true); }
  bool elim_01() const { return p.get_bool("elim_01", g, true); }
  bool pp_neat() const { return p.get_bool("pp.neat", g, true); }
  bool pb_compile_equality() const { return p.get_bool("pb.compile_equality", g, false); }
  bool maxres_hill_climb() const { return p.get_bool("maxres.hill_climb", g, true); }
  bool maxres_add_upper_bound_block() const { return p.get_bool("maxres.add_upper_bound_block", g, false); }
  unsigned maxres_max_num_cores() const { return p.get_uint("maxres.max_num_cores", g, 4294967295u); }
  unsigned maxres_max_core_size() const { return p.get_uint("maxres.max_core_size", g, 3u); }
  bool maxres_maximize_assignment() const { return p.get_bool("maxres.maximize_assignment", g, false); }
  unsigned maxres_max_correction_set_size() const { return p.get_uint("maxres.max_correction_set_size", g, 3u); }
  bool maxres_wmax() const { return p.get_bool("maxres.wmax", g, false); }
  bool maxres_pivot_on_correction_set() const { return p.get_bool("maxres.pivot_on_correction_set", g, true); }
};
#endif
