// Automatically generated file
#ifndef __MODEL_PARAMS_HPP_
#define __MODEL_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct model_params {
  params_ref const & p;
  params_ref g;
  model_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("model")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("partial", CPK_BOOL, "enable/disable partial function interpretations", "false","model");
    d.insert("v1", CPK_BOOL, "use Z3 version 1.x pretty printer", "false","model");
    d.insert("v2", CPK_BOOL, "use Z3 version 2.x (x <= 16) pretty printer", "false","model");
    d.insert("compact", CPK_BOOL, "try to compact function graph (i.e., function interpretations that are lookup tables)", "false","model");
  }
  /*
     REG_MODULE_PARAMS('model', 'model_params::collect_param_descrs')
  */
  bool partial() const { return p.get_bool("partial", g, false); }
  bool v1() const { return p.get_bool("v1", g, false); }
  bool v2() const { return p.get_bool("v2", g, false); }
  bool compact() const { return p.get_bool("compact", g, false); }
};
#endif
