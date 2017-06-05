// Automatically generated file
#ifndef __ACKERMANNIZATION_PARAMS_HPP_
#define __ACKERMANNIZATION_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct ackermannization_params {
  params_ref const & p;
  params_ref g;
  ackermannization_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("ackermannization")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("eager", CPK_BOOL, "eagerly instantiate all congruence rules", "true","ackermannization");
  }
  /*
     REG_MODULE_PARAMS('ackermannization', 'ackermannization_params::collect_param_descrs')
     REG_MODULE_DESCRIPTION('ackermannization', 'solving UF via ackermannization')
  */
  bool eager() const { return p.get_bool("eager", g, true); }
};
#endif
