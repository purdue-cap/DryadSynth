// Automatically generated file.
#include"gparams.h"
#include"ackermannization_params.hpp"
#include"ackermannize_bv_tactic_params.hpp"
#include"fpa2bv_rewriter_params.hpp"
#include"nnf.h"
#include"nnf_params.hpp"
#include"pattern_inference_params_helper.hpp"
#include"pp_params.hpp"
#include"arith_rewriter_params.hpp"
#include"array_rewriter_params.hpp"
#include"bool_rewriter_params.hpp"
#include"bv_rewriter_params.hpp"
#include"fpa_rewriter_params.hpp"
#include"poly_rewriter_params.hpp"
#include"rewriter_params.hpp"
#include"arith_simplifier_params_helper.hpp"
#include"array_simplifier_params_helper.hpp"
#include"bv_simplifier_params_helper.hpp"
#include"context_params.h"
#include"interp_params.hpp"
#include"algebraic_params.hpp"
#include"rcf_params.hpp"
#include"model_evaluator_params.hpp"
#include"model_params.hpp"
#include"fixedpoint_params.hpp"
#include"nlsat_params.hpp"
#include"opt_params.hpp"
#include"parser_params.hpp"
#include"sat_asymm_branch_params.hpp"
#include"sat_params.hpp"
#include"sat_scc_params.hpp"
#include"sat_simplifier_params.hpp"
#include"smt_params_helper.hpp"
#include"combined_solver_params.hpp"
#include"sls_params.hpp"
#include"qfufbv_tactic_params.hpp"
#include"env_params.h"
void gparams_register_modules() {
{ param_descrs d; context_params::collect_param_descrs(d); gparams::register_global(d); }
{ param_descrs d; env_params::collect_param_descrs(d); gparams::register_global(d); }
{ param_descrs * d = alloc(param_descrs); ackermannization_params::collect_param_descrs(*d); gparams::register_module("ackermannization", d); }
{ param_descrs * d = alloc(param_descrs); ackermannize_bv_tactic_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); fpa2bv_rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); nnf::get_param_descrs(*d); gparams::register_module("nnf", d); }
{ param_descrs * d = alloc(param_descrs); nnf_params::collect_param_descrs(*d); gparams::register_module("nnf", d); }
{ param_descrs * d = alloc(param_descrs); pattern_inference_params_helper::collect_param_descrs(*d); gparams::register_module("pi", d); }
{ param_descrs * d = alloc(param_descrs); pp_params::collect_param_descrs(*d); gparams::register_module("pp", d); }
{ param_descrs * d = alloc(param_descrs); arith_rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); array_rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); bool_rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); bv_rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); fpa_rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); poly_rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); rewriter_params::collect_param_descrs(*d); gparams::register_module("rewriter", d); }
{ param_descrs * d = alloc(param_descrs); arith_simplifier_params_helper::collect_param_descrs(*d); gparams::register_module("old_simplify", d); }
{ param_descrs * d = alloc(param_descrs); array_simplifier_params_helper::collect_param_descrs(*d); gparams::register_module("old_simplify", d); }
{ param_descrs * d = alloc(param_descrs); bv_simplifier_params_helper::collect_param_descrs(*d); gparams::register_module("old_simplify", d); }
{ param_descrs * d = alloc(param_descrs); interp_params::collect_param_descrs(*d); gparams::register_module("interp", d); }
{ param_descrs * d = alloc(param_descrs); algebraic_params::collect_param_descrs(*d); gparams::register_module("algebraic", d); }
{ param_descrs * d = alloc(param_descrs); rcf_params::collect_param_descrs(*d); gparams::register_module("rcf", d); }
{ param_descrs * d = alloc(param_descrs); model_evaluator_params::collect_param_descrs(*d); gparams::register_module("model_evaluator", d); }
{ param_descrs * d = alloc(param_descrs); model_params::collect_param_descrs(*d); gparams::register_module("model", d); }
{ param_descrs * d = alloc(param_descrs); fixedpoint_params::collect_param_descrs(*d); gparams::register_module("fixedpoint", d); }
{ param_descrs * d = alloc(param_descrs); nlsat_params::collect_param_descrs(*d); gparams::register_module("nlsat", d); }
{ param_descrs * d = alloc(param_descrs); opt_params::collect_param_descrs(*d); gparams::register_module("opt", d); }
{ param_descrs * d = alloc(param_descrs); parser_params::collect_param_descrs(*d); gparams::register_module("parser", d); }
{ param_descrs * d = alloc(param_descrs); sat_asymm_branch_params::collect_param_descrs(*d); gparams::register_module("sat", d); }
{ param_descrs * d = alloc(param_descrs); sat_params::collect_param_descrs(*d); gparams::register_module("sat", d); }
{ param_descrs * d = alloc(param_descrs); sat_scc_params::collect_param_descrs(*d); gparams::register_module("sat", d); }
{ param_descrs * d = alloc(param_descrs); sat_simplifier_params::collect_param_descrs(*d); gparams::register_module("sat", d); }
{ param_descrs * d = alloc(param_descrs); smt_params_helper::collect_param_descrs(*d); gparams::register_module("smt", d); }
{ param_descrs * d = alloc(param_descrs); combined_solver_params::collect_param_descrs(*d); gparams::register_module("combined_solver", d); }
{ param_descrs * d = alloc(param_descrs); sls_params::collect_param_descrs(*d); gparams::register_module("sls", d); }
{ param_descrs * d = alloc(param_descrs); qfufbv_tactic_params::collect_param_descrs(*d); gparams::register_module("ackermannization", d); }
gparams::register_module_descr("ackermannization", "solving UF via ackermannization");
gparams::register_module_descr("nnf", "negation normal form");
gparams::register_module_descr("pi", "pattern inference (heuristics) for universal formulas (without annotation)");
gparams::register_module_descr("pp", "pretty printer");
gparams::register_module_descr("rewriter", "new formula simplification module used in the tactic framework, and new solvers");
gparams::register_module_descr("old_simplify", "old simplification (stack) still used in the smt module");
gparams::register_module_descr("interp", "interpolation parameters");
gparams::register_module_descr("algebraic", "real algebraic number package");
gparams::register_module_descr("rcf", "real closed fields");
gparams::register_module_descr("fixedpoint", "fixedpoint parameters");
gparams::register_module_descr("nlsat", "nonlinear solver");
gparams::register_module_descr("opt", "optimization parameters");
gparams::register_module_descr("sat", "propositional SAT solver");
gparams::register_module_descr("smt", "smt solver based on lazy smt");
gparams::register_module_descr("combined_solver", "combines two solvers: non-incremental (solver1) and incremental (solver2)");
gparams::register_module_descr("sls", "Experimental Stochastic Local Search Solver (for QFBV only).");
gparams::register_module_descr("ackermannization", "tactics based on solving UF-theories via ackermannization (see also ackr module)");
}
