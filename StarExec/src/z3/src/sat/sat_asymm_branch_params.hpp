// Automatically generated file
#ifndef __SAT_ASYMM_BRANCH_PARAMS_HPP_
#define __SAT_ASYMM_BRANCH_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct sat_asymm_branch_params {
  params_ref const & p;
  params_ref g;
  sat_asymm_branch_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("sat")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("asymm_branch", CPK_BOOL, "asymmetric branching", "true","sat");
    d.insert("asymm_branch.rounds", CPK_UINT, "maximum number of rounds of asymmetric branching", "32","sat");
    d.insert("asymm_branch.limit", CPK_UINT, "approx. maximum number of literals visited during asymmetric branching", "100000000","sat");
  }
  /*
     REG_MODULE_PARAMS('sat', 'sat_asymm_branch_params::collect_param_descrs')
  */
  bool asymm_branch() const { return p.get_bool("asymm_branch", g, true); }
  unsigned asymm_branch_rounds() const { return p.get_uint("asymm_branch.rounds", g, 32u); }
  unsigned asymm_branch_limit() const { return p.get_uint("asymm_branch.limit", g, 100000000u); }
};
#endif
