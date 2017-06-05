// Automatically generated file
#ifndef __FIXEDPOINT_PARAMS_HPP_
#define __FIXEDPOINT_PARAMS_HPP_
#include"params.h"
#include"gparams.h"
struct fixedpoint_params {
  params_ref const & p;
  params_ref g;
  fixedpoint_params(params_ref const & _p = params_ref::get_empty()):
     p(_p), g(gparams::get_module("fixedpoint")) {}
  static void collect_param_descrs(param_descrs & d) {
    d.insert("timeout", CPK_UINT, "set timeout", "4294967295","fixedpoint");
    d.insert("engine", CPK_SYMBOL, "Select: auto-config, datalog, duality, pdr, bmc", "auto-config","fixedpoint");
    d.insert("datalog.default_table", CPK_SYMBOL, "default table implementation: sparse, hashtable, bitvector, interval", "sparse","fixedpoint");
    d.insert("datalog.default_relation", CPK_SYMBOL, "default relation implementation: external_relation, pentagon", "pentagon","fixedpoint");
    d.insert("datalog.generate_explanations", CPK_BOOL, "produce explanations for produced facts when using the datalog engine", "false","fixedpoint");
    d.insert("datalog.use_map_names", CPK_BOOL, "use names from map files when displaying tuples", "true","fixedpoint");
    d.insert("datalog.magic_sets_for_queries", CPK_BOOL, "magic set transformation will be used for queries", "false","fixedpoint");
    d.insert("datalog.explanations_on_relation_level", CPK_BOOL, "if true, explanations are generated as history of each relation, rather than per fact (generate_explanations must be set to true for this option to have any effect)", "false","fixedpoint");
    d.insert("datalog.unbound_compressor", CPK_BOOL, "auxiliary relations will be introduced to avoid unbound variables in rule heads", "true","fixedpoint");
    d.insert("datalog.similarity_compressor", CPK_BOOL, "rules that differ only in values of constants will be merged into a single rule", "true","fixedpoint");
    d.insert("datalog.similarity_compressor_threshold", CPK_UINT, "if similarity_compressor is on, this value determines how many similar rules there must be in order for them to be merged", "11","fixedpoint");
    d.insert("datalog.all_or_nothing_deltas", CPK_BOOL, "compile rules so that it is enough for the delta relation in union and widening operations to determine only whether the updated relation was modified or not", "false","fixedpoint");
    d.insert("datalog.compile_with_widening", CPK_BOOL, "widening will be used to compile recursive rules", "false","fixedpoint");
    d.insert("datalog.default_table_checked", CPK_BOOL, "if true, the detault table will be default_table inside a wrapper that checks that its results are the same as of default_table_checker table", "false","fixedpoint");
    d.insert("datalog.default_table_checker", CPK_SYMBOL, "see default_table_checked", "null","fixedpoint");
    d.insert("datalog.check_relation", CPK_SYMBOL, "name of default relation to check. operations on the default relation will be verified using SMT solving", "null","fixedpoint");
    d.insert("datalog.initial_restart_timeout", CPK_UINT, "length of saturation run before the first restart (in ms), zero means no restarts", "0","fixedpoint");
    d.insert("datalog.output_profile", CPK_BOOL, "determines whether profile information should be output when outputting Datalog rules or instructions", "false","fixedpoint");
    d.insert("datalog.print.tuples", CPK_BOOL, "determines whether tuples for output predicates should be output", "true","fixedpoint");
    d.insert("datalog.profile_timeout_milliseconds", CPK_UINT, "instructions and rules that took less than the threshold will not be printed when printed the instruction/rule list", "0","fixedpoint");
    d.insert("datalog.dbg_fpr_nonempty_relation_signature", CPK_BOOL, "if true, finite_product_relation will attempt to avoid creating inner relation with empty signature by putting in half of the table columns, if it would have been empty otherwise", "false","fixedpoint");
    d.insert("duality.full_expand", CPK_BOOL, "Fully expand derivation trees", "false","fixedpoint");
    d.insert("duality.no_conj", CPK_BOOL, "No forced covering (conjectures)", "false","fixedpoint");
    d.insert("duality.feasible_edges", CPK_BOOL, "Don't expand definitley infeasible edges", "true","fixedpoint");
    d.insert("duality.use_underapprox", CPK_BOOL, "Use underapproximations", "false","fixedpoint");
    d.insert("duality.stratified_inlining", CPK_BOOL, "Use stratified inlining", "false","fixedpoint");
    d.insert("duality.recursion_bound", CPK_UINT, "Recursion bound for stratified inlining", "4294967295","fixedpoint");
    d.insert("duality.profile", CPK_BOOL, "profile run time", "false","fixedpoint");
    d.insert("duality.mbqi", CPK_BOOL, "use model-based quantifier instantiation", "true","fixedpoint");
    d.insert("duality.batch_expand", CPK_BOOL, "use batch expansion", "false","fixedpoint");
    d.insert("duality.conjecture_file", CPK_STRING, "save conjectures to file", "","fixedpoint");
    d.insert("pdr.bfs_model_search", CPK_BOOL, "use BFS strategy for expanding model search", "true","fixedpoint");
    d.insert("pdr.farkas", CPK_BOOL, "use lemma generator based on Farkas (for linear real arithmetic)", "true","fixedpoint");
    d.insert("generate_proof_trace", CPK_BOOL, "trace for 'sat' answer as proof object", "false","fixedpoint");
    d.insert("pdr.flexible_trace", CPK_BOOL, "allow PDR generate long counter-examples by extending candidate trace within search area", "false","fixedpoint");
    d.insert("pdr.use_model_generalizer", CPK_BOOL, "use model for backwards propagation (instead of symbolic simulation)", "false","fixedpoint");
    d.insert("pdr.validate_result", CPK_BOOL, "validate result (by proof checking or model checking)", "false","fixedpoint");
    d.insert("pdr.simplify_formulas_pre", CPK_BOOL, "simplify derived formulas before inductive propagation", "false","fixedpoint");
    d.insert("pdr.simplify_formulas_post", CPK_BOOL, "simplify derived formulas after inductive propagation", "false","fixedpoint");
    d.insert("pdr.use_multicore_generalizer", CPK_BOOL, "extract multiple cores for blocking states", "false","fixedpoint");
    d.insert("pdr.use_inductive_generalizer", CPK_BOOL, "generalize lemmas using induction strengthening", "true","fixedpoint");
    d.insert("pdr.use_arith_inductive_generalizer", CPK_BOOL, "generalize lemmas using arithmetic heuristics for induction strengthening", "false","fixedpoint");
    d.insert("pdr.use_convex_closure_generalizer", CPK_BOOL, "generalize using convex closures of lemmas", "false","fixedpoint");
    d.insert("pdr.use_convex_interior_generalizer", CPK_BOOL, "generalize using convex interiors of lemmas", "false","fixedpoint");
    d.insert("pdr.cache_mode", CPK_UINT, "use no (0), symbolic (1) or explicit cache (2) for model search", "0","fixedpoint");
    d.insert("pdr.inductive_reachability_check", CPK_BOOL, "assume negation of the cube on the previous level when checking for reachability (not only during cube weakening)", "false","fixedpoint");
    d.insert("pdr.max_num_contexts", CPK_UINT, "maximal number of contexts to create", "500","fixedpoint");
    d.insert("pdr.try_minimize_core", CPK_BOOL, "try to reduce core size (before inductive minimization)", "false","fixedpoint");
    d.insert("pdr.utvpi", CPK_BOOL, "Enable UTVPI strategy", "true","fixedpoint");
    d.insert("print_fixedpoint_extensions", CPK_BOOL, "use SMT-LIB2 fixedpoint extensions, instead of pure SMT2, when printing rules", "true","fixedpoint");
    d.insert("print_low_level_smt2", CPK_BOOL, "use (faster) low-level SMT2 printer (the printer is scalable but the result may not be as readable)", "false","fixedpoint");
    d.insert("print_with_variable_declarations", CPK_BOOL, "use variable declarations when displaying rules (instead of attempting to use original names)", "true","fixedpoint");
    d.insert("print_answer", CPK_BOOL, "print answer instance(s) to query", "false","fixedpoint");
    d.insert("print_certificate", CPK_BOOL, "print certificate for reachability or non-reachability", "false","fixedpoint");
    d.insert("print_boogie_certificate", CPK_BOOL, "print certificate for reachability or non-reachability using a format understood by Boogie", "false","fixedpoint");
    d.insert("print_statistics", CPK_BOOL, "print statistics", "false","fixedpoint");
    d.insert("print_aig", CPK_SYMBOL, "Dump clauses in AIG text format (AAG) to the given file name", "","fixedpoint");
    d.insert("tab.selection", CPK_SYMBOL, "selection method for tabular strategy: weight (default), first, var-use", "weight","fixedpoint");
    d.insert("xform.bit_blast", CPK_BOOL, "bit-blast bit-vectors", "false","fixedpoint");
    d.insert("xform.magic", CPK_BOOL, "perform symbolic magic set transformation", "false","fixedpoint");
    d.insert("xform.scale", CPK_BOOL, "add scaling variable to linear real arithemtic clauses", "false","fixedpoint");
    d.insert("xform.inline_linear", CPK_BOOL, "try linear inlining method", "true","fixedpoint");
    d.insert("xform.inline_eager", CPK_BOOL, "try eager inlining of rules", "true","fixedpoint");
    d.insert("xform.inline_linear_branch", CPK_BOOL, "try linear inlining method with potential expansion", "false","fixedpoint");
    d.insert("xform.compress_unbound", CPK_BOOL, "compress tails with unbound variables", "true","fixedpoint");
    d.insert("xform.fix_unbound_vars", CPK_BOOL, "fix unbound variables in tail", "false","fixedpoint");
    d.insert("xform.unfold_rules", CPK_UINT, "unfold rules statically using iterative squarring", "0","fixedpoint");
    d.insert("xform.slice", CPK_BOOL, "simplify clause set using slicing", "true","fixedpoint");
    d.insert("xform.karr", CPK_BOOL, "Add linear invariants to clauses using Karr's method", "false","fixedpoint");
    d.insert("xform.quantify_arrays", CPK_BOOL, "create quantified Horn clauses from clauses with arrays", "false","fixedpoint");
    d.insert("xform.instantiate_quantifiers", CPK_BOOL, "instantiate quantified Horn clauses using E-matching heuristic", "false","fixedpoint");
    d.insert("xform.coalesce_rules", CPK_BOOL, "coalesce rules", "false","fixedpoint");
    d.insert("xform.coi", CPK_BOOL, "use cone of influence simplificaiton", "true","fixedpoint");
    d.insert("duality.enable_restarts", CPK_BOOL, "DUALITY: enable restarts", "false","fixedpoint");
  }
  /*
     REG_MODULE_PARAMS('fixedpoint', 'fixedpoint_params::collect_param_descrs')
     REG_MODULE_DESCRIPTION('fixedpoint', 'fixedpoint parameters')
  */
  unsigned timeout() const { return p.get_uint("timeout", g, 4294967295u); }
  symbol engine() const { return p.get_sym("engine", g, symbol("auto-config")); }
  symbol datalog_default_table() const { return p.get_sym("datalog.default_table", g, symbol("sparse")); }
  symbol datalog_default_relation() const { return p.get_sym("datalog.default_relation", g, symbol("pentagon")); }
  bool datalog_generate_explanations() const { return p.get_bool("datalog.generate_explanations", g, false); }
  bool datalog_use_map_names() const { return p.get_bool("datalog.use_map_names", g, true); }
  bool datalog_magic_sets_for_queries() const { return p.get_bool("datalog.magic_sets_for_queries", g, false); }
  bool datalog_explanations_on_relation_level() const { return p.get_bool("datalog.explanations_on_relation_level", g, false); }
  bool datalog_unbound_compressor() const { return p.get_bool("datalog.unbound_compressor", g, true); }
  bool datalog_similarity_compressor() const { return p.get_bool("datalog.similarity_compressor", g, true); }
  unsigned datalog_similarity_compressor_threshold() const { return p.get_uint("datalog.similarity_compressor_threshold", g, 11u); }
  bool datalog_all_or_nothing_deltas() const { return p.get_bool("datalog.all_or_nothing_deltas", g, false); }
  bool datalog_compile_with_widening() const { return p.get_bool("datalog.compile_with_widening", g, false); }
  bool datalog_default_table_checked() const { return p.get_bool("datalog.default_table_checked", g, false); }
  symbol datalog_default_table_checker() const { return p.get_sym("datalog.default_table_checker", g, symbol("null")); }
  symbol datalog_check_relation() const { return p.get_sym("datalog.check_relation", g, symbol("null")); }
  unsigned datalog_initial_restart_timeout() const { return p.get_uint("datalog.initial_restart_timeout", g, 0u); }
  bool datalog_output_profile() const { return p.get_bool("datalog.output_profile", g, false); }
  bool datalog_print_tuples() const { return p.get_bool("datalog.print.tuples", g, true); }
  unsigned datalog_profile_timeout_milliseconds() const { return p.get_uint("datalog.profile_timeout_milliseconds", g, 0u); }
  bool datalog_dbg_fpr_nonempty_relation_signature() const { return p.get_bool("datalog.dbg_fpr_nonempty_relation_signature", g, false); }
  bool duality_full_expand() const { return p.get_bool("duality.full_expand", g, false); }
  bool duality_no_conj() const { return p.get_bool("duality.no_conj", g, false); }
  bool duality_feasible_edges() const { return p.get_bool("duality.feasible_edges", g, true); }
  bool duality_use_underapprox() const { return p.get_bool("duality.use_underapprox", g, false); }
  bool duality_stratified_inlining() const { return p.get_bool("duality.stratified_inlining", g, false); }
  unsigned duality_recursion_bound() const { return p.get_uint("duality.recursion_bound", g, 4294967295u); }
  bool duality_profile() const { return p.get_bool("duality.profile", g, false); }
  bool duality_mbqi() const { return p.get_bool("duality.mbqi", g, true); }
  bool duality_batch_expand() const { return p.get_bool("duality.batch_expand", g, false); }
  char const * duality_conjecture_file() const { return p.get_str("duality.conjecture_file", g, ""); }
  bool pdr_bfs_model_search() const { return p.get_bool("pdr.bfs_model_search", g, true); }
  bool pdr_farkas() const { return p.get_bool("pdr.farkas", g, true); }
  bool generate_proof_trace() const { return p.get_bool("generate_proof_trace", g, false); }
  bool pdr_flexible_trace() const { return p.get_bool("pdr.flexible_trace", g, false); }
  bool pdr_use_model_generalizer() const { return p.get_bool("pdr.use_model_generalizer", g, false); }
  bool pdr_validate_result() const { return p.get_bool("pdr.validate_result", g, false); }
  bool pdr_simplify_formulas_pre() const { return p.get_bool("pdr.simplify_formulas_pre", g, false); }
  bool pdr_simplify_formulas_post() const { return p.get_bool("pdr.simplify_formulas_post", g, false); }
  bool pdr_use_multicore_generalizer() const { return p.get_bool("pdr.use_multicore_generalizer", g, false); }
  bool pdr_use_inductive_generalizer() const { return p.get_bool("pdr.use_inductive_generalizer", g, true); }
  bool pdr_use_arith_inductive_generalizer() const { return p.get_bool("pdr.use_arith_inductive_generalizer", g, false); }
  bool pdr_use_convex_closure_generalizer() const { return p.get_bool("pdr.use_convex_closure_generalizer", g, false); }
  bool pdr_use_convex_interior_generalizer() const { return p.get_bool("pdr.use_convex_interior_generalizer", g, false); }
  unsigned pdr_cache_mode() const { return p.get_uint("pdr.cache_mode", g, 0u); }
  bool pdr_inductive_reachability_check() const { return p.get_bool("pdr.inductive_reachability_check", g, false); }
  unsigned pdr_max_num_contexts() const { return p.get_uint("pdr.max_num_contexts", g, 500u); }
  bool pdr_try_minimize_core() const { return p.get_bool("pdr.try_minimize_core", g, false); }
  bool pdr_utvpi() const { return p.get_bool("pdr.utvpi", g, true); }
  bool print_fixedpoint_extensions() const { return p.get_bool("print_fixedpoint_extensions", g, true); }
  bool print_low_level_smt2() const { return p.get_bool("print_low_level_smt2", g, false); }
  bool print_with_variable_declarations() const { return p.get_bool("print_with_variable_declarations", g, true); }
  bool print_answer() const { return p.get_bool("print_answer", g, false); }
  bool print_certificate() const { return p.get_bool("print_certificate", g, false); }
  bool print_boogie_certificate() const { return p.get_bool("print_boogie_certificate", g, false); }
  bool print_statistics() const { return p.get_bool("print_statistics", g, false); }
  symbol print_aig() const { return p.get_sym("print_aig", g, symbol("")); }
  symbol tab_selection() const { return p.get_sym("tab.selection", g, symbol("weight")); }
  bool xform_bit_blast() const { return p.get_bool("xform.bit_blast", g, false); }
  bool xform_magic() const { return p.get_bool("xform.magic", g, false); }
  bool xform_scale() const { return p.get_bool("xform.scale", g, false); }
  bool xform_inline_linear() const { return p.get_bool("xform.inline_linear", g, true); }
  bool xform_inline_eager() const { return p.get_bool("xform.inline_eager", g, true); }
  bool xform_inline_linear_branch() const { return p.get_bool("xform.inline_linear_branch", g, false); }
  bool xform_compress_unbound() const { return p.get_bool("xform.compress_unbound", g, true); }
  bool xform_fix_unbound_vars() const { return p.get_bool("xform.fix_unbound_vars", g, false); }
  unsigned xform_unfold_rules() const { return p.get_uint("xform.unfold_rules", g, 0u); }
  bool xform_slice() const { return p.get_bool("xform.slice", g, true); }
  bool xform_karr() const { return p.get_bool("xform.karr", g, false); }
  bool xform_quantify_arrays() const { return p.get_bool("xform.quantify_arrays", g, false); }
  bool xform_instantiate_quantifiers() const { return p.get_bool("xform.instantiate_quantifiers", g, false); }
  bool xform_coalesce_rules() const { return p.get_bool("xform.coalesce_rules", g, false); }
  bool xform_coi() const { return p.get_bool("xform.coi", g, true); }
  bool duality_enable_restarts() const { return p.get_bool("duality.enable_restarts", g, false); }
};
#endif
