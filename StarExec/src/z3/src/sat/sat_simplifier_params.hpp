// Automatically generated file
#ifndef __SAT_SIMPLIFIER_PARAMS_HPP_
#define __SAT_SIMPLIFIER_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct sat_simplifier_params {
  params_ref const & p;
  params_ref g;
  sat_simplifier_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("sat")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("elim_blocked_clauses", CPK_BOOL, "eliminate blocked clauses", "false","sat");
    d.insert("elim_blocked_clauses_at", CPK_UINT, "eliminate blocked clauses only once at the given simplification round", "2","sat");
    d.insert("blocked_clause_limit", CPK_UINT, "maximum number of literals visited during blocked clause elimination", "100000000","sat");
    d.insert("resolution", CPK_BOOL, "eliminate boolean variables using resolution", "true","sat");
    d.insert("resolution.limit", CPK_UINT, "approx. maximum number of literals visited during variable elimination", "500000000","sat");
    d.insert("resolution.occ_cutoff", CPK_UINT, "first cutoff (on number of positive/negative occurrences) for Boolean variable elimination", "10","sat");
    d.insert("resolution.occ_cutoff_range1", CPK_UINT, "second cutoff (number of positive/negative occurrences) for Boolean variable elimination, for problems containing less than res_cls_cutoff1 clauses", "8","sat");
    d.insert("resolution.occ_cutoff_range2", CPK_UINT, "second cutoff (number of positive/negative occurrences) for Boolean variable elimination, for problems containing more than res_cls_cutoff1 and less than res_cls_cutoff2", "5","sat");
    d.insert("resolution.occ_cutoff_range3", CPK_UINT, "second cutoff (number of positive/negative occurrences) for Boolean variable elimination, for problems containing more than res_cls_cutoff2", "3","sat");
    d.insert("resolution.lit_cutoff_range1", CPK_UINT, "second cutoff (total number of literals) for Boolean variable elimination, for problems containing less than res_cls_cutoff1 clauses", "700","sat");
    d.insert("resolution.lit_cutoff_range2", CPK_UINT, "second cutoff (total number of literals) for Boolean variable elimination, for problems containing more than res_cls_cutoff1 and less than res_cls_cutoff2", "400","sat");
    d.insert("resolution.lit_cutoff_range3", CPK_UINT, "second cutoff (total number of literals) for Boolean variable elimination, for problems containing more than res_cls_cutoff2", "300","sat");
    d.insert("resolution.cls_cutoff1", CPK_UINT, "limit1 - total number of problems clauses for the second cutoff of Boolean variable elimination", "100000000","sat");
    d.insert("resolution.cls_cutoff2", CPK_UINT, "limit2 - total number of problems clauses for the second cutoff of Boolean variable elimination", "700000000","sat");
    d.insert("elim_vars", CPK_BOOL, "enable variable elimination during simplification", "true","sat");
    d.insert("subsumption", CPK_BOOL, "eliminate subsumed clauses", "true","sat");
    d.insert("subsumption.limit", CPK_UINT, "approx. maximum number of literals visited during subsumption (and subsumption resolution)", "100000000","sat");
  }
  /*
     REG_MODULE_PARAMS('sat', 'sat_simplifier_params::collect_param_descrs')
  */
  bool elim_blocked_clauses() const { return p.get_bool("elim_blocked_clauses", g, false); }
  unsigned elim_blocked_clauses_at() const { return p.get_uint("elim_blocked_clauses_at", g, 2u); }
  unsigned blocked_clause_limit() const { return p.get_uint("blocked_clause_limit", g, 100000000u); }
  bool resolution() const { return p.get_bool("resolution", g, true); }
  unsigned resolution_limit() const { return p.get_uint("resolution.limit", g, 500000000u); }
  unsigned resolution_occ_cutoff() const { return p.get_uint("resolution.occ_cutoff", g, 10u); }
  unsigned resolution_occ_cutoff_range1() const { return p.get_uint("resolution.occ_cutoff_range1", g, 8u); }
  unsigned resolution_occ_cutoff_range2() const { return p.get_uint("resolution.occ_cutoff_range2", g, 5u); }
  unsigned resolution_occ_cutoff_range3() const { return p.get_uint("resolution.occ_cutoff_range3", g, 3u); }
  unsigned resolution_lit_cutoff_range1() const { return p.get_uint("resolution.lit_cutoff_range1", g, 700u); }
  unsigned resolution_lit_cutoff_range2() const { return p.get_uint("resolution.lit_cutoff_range2", g, 400u); }
  unsigned resolution_lit_cutoff_range3() const { return p.get_uint("resolution.lit_cutoff_range3", g, 300u); }
  unsigned resolution_cls_cutoff1() const { return p.get_uint("resolution.cls_cutoff1", g, 100000000u); }
  unsigned resolution_cls_cutoff2() const { return p.get_uint("resolution.cls_cutoff2", g, 700000000u); }
  bool elim_vars() const { return p.get_bool("elim_vars", g, true); }
  bool subsumption() const { return p.get_bool("subsumption", g, true); }
  unsigned subsumption_limit() const { return p.get_uint("subsumption.limit", g, 100000000u); }
};
#endif
