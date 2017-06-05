// Automatically generated file
#ifndef __REWRITER_PARAMS_HPP_
#define __REWRITER_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct rewriter_params {
  params_ref const & p;
  params_ref g;
  rewriter_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("rewriter")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("max_memory", CPK_UINT, "maximum amount of memory in megabytes", "4294967295","rewriter");
    d.insert("max_steps", CPK_UINT, "maximum number of steps", "4294967295","rewriter");
    d.insert("flat", CPK_BOOL, "create nary applications for and,or,+,*,bvadd,bvmul,bvand,bvor,bvxor", "true","rewriter");
    d.insert("push_ite_arith", CPK_BOOL, "push if-then-else over arithmetic terms.", "false","rewriter");
    d.insert("push_ite_bv", CPK_BOOL, "push if-then-else over bit-vector terms.", "false","rewriter");
    d.insert("pull_cheap_ite", CPK_BOOL, "pull if-then-else terms when cheap.", "false","rewriter");
    d.insert("bv_ineq_consistency_test_max", CPK_UINT, "max size of conjunctions on which to perform consistency test based on inequalities on bitvectors.", "0","rewriter");
    d.insert("cache_all", CPK_BOOL, "cache all intermediate results.", "false","rewriter");
  }
  /*
     REG_MODULE_PARAMS('rewriter', 'rewriter_params::collect_param_descrs')
     REG_MODULE_DESCRIPTION('rewriter', 'new formula simplification module used in the tactic framework, and new solvers')
  */
  unsigned max_memory() const { return p.get_uint("max_memory", g, 4294967295u); }
  unsigned max_steps() const { return p.get_uint("max_steps", g, 4294967295u); }
  bool flat() const { return p.get_bool("flat", g, true); }
  bool push_ite_arith() const { return p.get_bool("push_ite_arith", g, false); }
  bool push_ite_bv() const { return p.get_bool("push_ite_bv", g, false); }
  bool pull_cheap_ite() const { return p.get_bool("pull_cheap_ite", g, false); }
  unsigned bv_ineq_consistency_test_max() const { return p.get_uint("bv_ineq_consistency_test_max", g, 0u); }
  bool cache_all() const { return p.get_bool("cache_all", g, false); }
};
#endif
