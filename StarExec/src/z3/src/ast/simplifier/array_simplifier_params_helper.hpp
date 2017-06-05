// Automatically generated file
#ifndef __ARRAY_SIMPLIFIER_PARAMS_HELPER_HPP_
#define __ARRAY_SIMPLIFIER_PARAMS_HELPER_HPP_
#include"params.h"
#include"gparams.h"
struct array_simplifier_params_helper {
  params_ref const & p;
  params_ref g;
  array_simplifier_params_helper(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("old_simplify")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("array.canonize", CPK_BOOL, "normalize array terms into normal form during simplification", "false","old_simplify");
    d.insert("array.simplify", CPK_BOOL, "enable/disable array simplifications", "true","old_simplify");
  }
  /*
     REG_MODULE_PARAMS('old_simplify', 'array_simplifier_params_helper::collect_param_descrs')
  */
  bool array_canonize() const { return p.get_bool("array.canonize", g, false); }
  bool array_simplify() const { return p.get_bool("array.simplify", g, true); }
};
#endif
