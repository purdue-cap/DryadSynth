// Automatically generated file
#ifndef __POLY_REWRITER_PARAMS_HPP_
#define __POLY_REWRITER_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct poly_rewriter_params {
  params_ref const & p;
  params_ref g;
  poly_rewriter_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("rewriter")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("som", CPK_BOOL, "put polynomials in som-of-monomials form", "false","rewriter");
    d.insert("som_blowup", CPK_UINT, "maximum number of monomials generated when putting a polynomial in sum-of-monomials normal form", "4294967295","rewriter");
    d.insert("hoist_mul", CPK_BOOL, "hoist multiplication over summation to minimize number of multiplications", "false","rewriter");
    d.insert("hoist_cmul", CPK_BOOL, "hoist constant multiplication over summation to minimize number of multiplications", "false","rewriter");
    d.insert("flat", CPK_BOOL, "create nary applications for and,or,+,*,bvadd,bvmul,bvand,bvor,bvxor", "true","rewriter");
  }
  /*
     REG_MODULE_PARAMS('rewriter', 'poly_rewriter_params::collect_param_descrs')
  */
  bool som() const { return p.get_bool("som", g, false); }
  unsigned som_blowup() const { return p.get_uint("som_blowup", g, 4294967295u); }
  bool hoist_mul() const { return p.get_bool("hoist_mul", g, false); }
  bool hoist_cmul() const { return p.get_bool("hoist_cmul", g, false); }
  bool flat() const { return p.get_bool("flat", g, true); }
};
#endif
