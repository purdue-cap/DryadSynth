// Automatically generated file
#ifndef __INTERP_PARAMS_HPP_
#define __INTERP_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct interp_params {
  params_ref const & p;
  params_ref g;
  interp_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("interp")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("profile", CPK_BOOL, "(INTERP) profile interpolation", "false","interp");
    d.insert("check", CPK_BOOL, "(INTERP) check interpolants", "false","interp");
  }
  /*
     REG_MODULE_PARAMS('interp', 'interp_params::collect_param_descrs')
     REG_MODULE_DESCRIPTION('interp', 'interpolation parameters')
  */
  bool profile() const { return p.get_bool("profile", g, false); }
  bool check() const { return p.get_bool("check", g, false); }
};
#endif
