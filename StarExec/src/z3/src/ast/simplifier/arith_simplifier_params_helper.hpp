// Automatically generated file
#ifndef __ARITH_SIMPLIFIER_PARAMS_HELPER_HPP_
#define __ARITH_SIMPLIFIER_PARAMS_HELPER_HPP_
#include"params.h"
#include"gparams.h"
struct arith_simplifier_params_helper {
  params_ref const & p;
  params_ref g;
  arith_simplifier_params_helper(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("old_simplify")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("arith.expand_eqs", CPK_BOOL, "expand equalities into two inequalities", "false","old_simplify");
    d.insert("arith.process_all_eqs", CPK_BOOL, "put all equations in the form (= t c), where c is a numeral", "false","old_simplify");
  }
  /*
     REG_MODULE_PARAMS('old_simplify', 'arith_simplifier_params_helper::collect_param_descrs')
     REG_MODULE_DESCRIPTION('old_simplify', 'old simplification (stack) still used in the smt module')
  */
  bool arith_expand_eqs() const { return p.get_bool("arith.expand_eqs", g, false); }
  bool arith_process_all_eqs() const { return p.get_bool("arith.process_all_eqs", g, false); }
};
#endif
