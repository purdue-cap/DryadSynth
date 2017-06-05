// Automatically generated file
#ifndef __BV_SIMPLIFIER_PARAMS_HELPER_HPP_
#define __BV_SIMPLIFIER_PARAMS_HELPER_HPP_
#include"params.h"
#include"gparams.h"
struct bv_simplifier_params_helper {
  params_ref const & p;
  params_ref g;
  bv_simplifier_params_helper(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("old_simplify")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("bv.bv2int_distribute", CPK_BOOL, "if true, then int2bv is distributed over arithmetical operators", "true","old_simplify");
  }
  /*
     REG_MODULE_PARAMS('old_simplify', 'bv_simplifier_params_helper::collect_param_descrs')
  */
  bool bv_bv2int_distribute() const { return p.get_bool("bv.bv2int_distribute", g, true); }
};
#endif
