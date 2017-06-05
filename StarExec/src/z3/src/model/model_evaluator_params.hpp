// Automatically generated file
#ifndef __MODEL_EVALUATOR_PARAMS_HPP_
#define __MODEL_EVALUATOR_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct model_evaluator_params {
  params_ref const & p;
  params_ref g;
  model_evaluator_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("model_evaluator")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("max_memory", CPK_UINT, "maximum amount of memory in megabytes", "4294967295","model_evaluator");
    d.insert("max_steps", CPK_UINT, "maximum number of steps", "4294967295","model_evaluator");
    d.insert("completion", CPK_BOOL, "assigns an interptetation to symbols that do not have one in the current model, when evaluating expressions in the current model", "false","model_evaluator");
    d.insert("cache", CPK_BOOL, "cache intermediate results in the model evaluator", "true","model_evaluator");
  }
  /*
     REG_MODULE_PARAMS('model_evaluator', 'model_evaluator_params::collect_param_descrs')
  */
  unsigned max_memory() const { return p.get_uint("max_memory", g, 4294967295u); }
  unsigned max_steps() const { return p.get_uint("max_steps", g, 4294967295u); }
  bool completion() const { return p.get_bool("completion", g, false); }
  bool cache() const { return p.get_bool("cache", g, true); }
};
#endif
