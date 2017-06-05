/*++
Copyright (c) 2016 Microsoft Corporation

Module Name:

    bounded_int2bv_solver.cpp

Abstract:

    This solver identifies bounded integers and rewrites them to bit-vectors.

Author:

    Nikolaj Bjorner (nbjorner) 2016-10-23

Notes:

--*/

#include "bounded_int2bv_solver.h"
#include "solver_na2as.h"
#include "tactic.h"
#include "pb2bv_rewriter.h"
#include "filter_model_converter.h"
#include "extension_model_converter.h"
#include "ast_pp.h"
#include "model_smt2_pp.h"
#include "bound_manager.h"
#include "bv2int_rewriter.h"
#include "expr_safe_replace.h"
#include "bv_decl_plugin.h"
#include "arith_decl_plugin.h"

class bounded_int2bv_solver : public solver_na2as {
    ast_manager&     m;
    params_ref       m_params;
    bv_util          m_bv;
    arith_util       m_arith;
    expr_ref_vector  m_assertions;
    ref<solver>      m_solver;
    ptr_vector<bound_manager> m_bounds;
    func_decl_ref_vector  m_bv_fns;
    func_decl_ref_vector  m_int_fns;
    unsigned_vector       m_bv_fns_lim;
    obj_map<func_decl, func_decl*> m_int2bv;
    obj_map<func_decl, func_decl*> m_bv2int;
    obj_map<func_decl, rational>   m_bv2offset;
    bv2int_rewriter_ctx   m_rewriter_ctx;
    bv2int_rewriter_star  m_rewriter;

public:

    bounded_int2bv_solver(ast_manager& m, params_ref const& p, solver* s):
        solver_na2as(m),
        m(m),
        m_params(p),
        m_bv(m),
        m_arith(m),
        m_assertions(m),
        m_solver(s),
        m_bv_fns(m),
        m_int_fns(m),
        m_rewriter_ctx(m, p),
        m_rewriter(m, m_rewriter_ctx)
    {
        m_bounds.push_back(alloc(bound_manager, m));
    }

    virtual ~bounded_int2bv_solver() {
        while (!m_bounds.empty()) {
            dealloc(m_bounds.back());
            m_bounds.pop_back();
        }
    }

    virtual solver* translate(ast_manager& m, params_ref const& p) {
        return alloc(bounded_int2bv_solver, m, p, m_solver->translate(m, p));
    }

    virtual void assert_expr(expr * t) {
        m_assertions.push_back(t);
    }

    virtual void push_core() {
        flush_assertions();
        m_solver->push();
        m_bv_fns_lim.push_back(m_bv_fns.size());
        m_bounds.push_back(alloc(bound_manager, m));
    }

    virtual void pop_core(unsigned n) {
        m_assertions.reset();
        m_solver->pop(n);

        if (n > 0) {
            SASSERT(n <= m_bv_fns_lim.size());
            unsigned new_sz = m_bv_fns_lim.size() - n;
            unsigned lim = m_bv_fns_lim[new_sz];
            for (unsigned i = m_int_fns.size(); i > lim; ) {
                --i;
                m_int2bv.erase(m_int_fns[i].get());
                m_bv2int.erase(m_bv_fns[i].get());
                m_bv2offset.erase(m_bv_fns[i].get());
            }
            m_bv_fns_lim.resize(new_sz);
            m_bv_fns.resize(lim);
            m_int_fns.resize(lim);
        }

        while (n > 0) {
            dealloc(m_bounds.back());
            m_bounds.pop_back();
            --n;
        }
    }

    virtual lbool check_sat_core(unsigned num_assumptions, expr * const * assumptions) {
        flush_assertions();
        return m_solver->check_sat(num_assumptions, assumptions);
    }

    virtual void updt_params(params_ref const & p) { m_solver->updt_params(p);  }
    virtual void collect_param_descrs(param_descrs & r) { m_solver->collect_param_descrs(r); }
    virtual void set_produce_models(bool f) { m_solver->set_produce_models(f); }
    virtual void set_progress_callback(progress_callback * callback) { m_solver->set_progress_callback(callback);  }
    virtual void collect_statistics(statistics & st) const { m_solver->collect_statistics(st); }
    virtual void get_unsat_core(ptr_vector<expr> & r) { m_solver->get_unsat_core(r); }
    virtual void get_model(model_ref & mdl) {
        m_solver->get_model(mdl);
        if (mdl) {
            extend_model(mdl);
            filter_model(mdl);
        }
    }
    virtual proof * get_proof() { return m_solver->get_proof(); }
    virtual std::string reason_unknown() const { return m_solver->reason_unknown(); }
    virtual void set_reason_unknown(char const* msg) { m_solver->set_reason_unknown(msg); }
    virtual void get_labels(svector<symbol> & r) { m_solver->get_labels(r); }
    virtual ast_manager& get_manager() const { return m;  }
    virtual lbool find_mutexes(expr_ref_vector const& vars, vector<expr_ref_vector>& mutexes) { return m_solver->find_mutexes(vars, mutexes); }
    virtual lbool get_consequences_core(expr_ref_vector const& asms, expr_ref_vector const& vars, expr_ref_vector& consequences) {
        flush_assertions();
        expr_ref_vector bvars(m);
        for (unsigned i = 0; i < vars.size(); ++i) {
            expr* v = vars[i];
            func_decl* f;
            rational offset;
            if (is_app(v) && is_uninterp_const(v) && m_int2bv.find(to_app(v)->get_decl(), f)) {
                bvars.push_back(m.mk_const(f));
            }
            else {
                bvars.push_back(v);
            }
        }
        lbool r = m_solver->get_consequences(asms, bvars, consequences);

        // translate bit-vector consequences back to integer values
        for (unsigned i = 0; i < consequences.size(); ++i) {
            expr* a, *b, *u, *v;
            func_decl* f;
            rational num;
            unsigned bvsize;
            rational offset;
            VERIFY(m.is_implies(consequences[i].get(), a, b));
            if (m.is_eq(b, u, v) && is_uninterp_const(u) && m_bv2int.find(to_app(u)->get_decl(), f) && m_bv.is_numeral(v, num, bvsize)) {
                SASSERT(num.is_unsigned());
                expr_ref head(m);
                VERIFY (m_bv2offset.find(to_app(u)->get_decl(), offset));
                // f + offset == num
                // f == num - offset
                head = m.mk_eq(m.mk_const(f), m_arith.mk_numeral(num + offset, true));
                consequences[i] = m.mk_implies(a, head);
            }
        }
        return r;

    }

private:

    void filter_model(model_ref& mdl) const {
        if (m_bv_fns.empty()) {
            return;
        }
        filter_model_converter filter(m);
        for (unsigned i = 0; i < m_bv_fns.size(); ++i) {
            filter.insert(m_bv_fns[i]);
        }
        filter(mdl, 0);
    }

    void extend_model(model_ref& mdl) {
        extension_model_converter ext(m);
        obj_map<func_decl, func_decl*>::iterator it = m_int2bv.begin(), end = m_int2bv.end();
        for (; it != end; ++it) {
            rational offset;
            VERIFY (m_bv2offset.find(it->m_value, offset));
            expr_ref value(m_bv.mk_bv2int(m.mk_const(it->m_value)), m);
            if (!offset.is_zero()) {
                value = m_arith.mk_add(value, m_arith.mk_numeral(offset, true));
            }
            TRACE("int2bv", tout << mk_pp(it->m_key, m) << " " << value << "\n";);
            ext.insert(it->m_key, value);
        }
        ext(mdl, 0);
    }

    void accumulate_sub(expr_safe_replace& sub) {
        for (unsigned i = 0; i < m_bounds.size(); ++i) {
            accumulate_sub(sub, *m_bounds[i]);
        }
    }

    void accumulate_sub(expr_safe_replace& sub, bound_manager& bm) {
        bound_manager::iterator it = bm.begin(), end = bm.end();
        for (; it != end; ++it) {
            expr* e = *it;
            rational lo, hi;
            bool s1, s2;
            SASSERT(is_uninterp_const(e));
            func_decl* f = to_app(e)->get_decl();

            if (bm.has_lower(e, lo, s1) && bm.has_upper(e, hi, s2) && lo <= hi && !s1 && !s2) {
                func_decl* fbv;
                rational offset;
                if (!m_int2bv.find(f, fbv)) {
                    rational n = hi - lo + rational::one();
                    unsigned num_bits = get_num_bits(n);
                    expr_ref b(m);
                    b = m.mk_fresh_const("b", m_bv.mk_sort(num_bits));
                    fbv = to_app(b)->get_decl();
                    offset = lo;
                    m_int2bv.insert(f, fbv);
                    m_bv2int.insert(fbv, f);
                    m_bv2offset.insert(fbv, offset);
                    m_bv_fns.push_back(fbv);
                    m_int_fns.push_back(f);
                    unsigned shift;
                    if (!offset.is_zero() && !n.is_power_of_two(shift)) {
                        m_assertions.push_back(m_bv.mk_ule(b, m_bv.mk_numeral(n-rational::one(), num_bits)));
                    }
                }
                else {
                    VERIFY(m_bv2offset.find(fbv, offset));
                }
                expr_ref t(m.mk_const(fbv), m);
                t = m_bv.mk_bv2int(t);
                if (!offset.is_zero()) {
                    t = m_arith.mk_add(t, m_arith.mk_numeral(offset, true));
                }
                TRACE("pb", tout << lo << " <= " << hi << " offset: " << offset << "\n"; tout << mk_pp(e, m) << " |-> " << t << "\n";);
                sub.insert(e, t);
            }
            else {
                IF_VERBOSE(1,
                           verbose_stream() << "unprocessed entry: " << mk_pp(e, m) << "\n";
                           if (bm.has_lower(e, lo, s1)) {
                               verbose_stream() << "lower: " << lo << " " << s1 << "\n";
                           }
                           if (bm.has_upper(e, hi, s2)) {
                               verbose_stream() << "upper: " << hi << " " << s2 << "\n";
                           });
            }
        }
    }

    unsigned get_num_bits(rational const& k) {
        SASSERT(!k.is_neg());
        SASSERT(k.is_int());
        rational two(2);
        rational bound(1);
        unsigned num_bits = 1;
        while (bound <= k) {
            ++num_bits;
            bound *= two;
        }
        return num_bits;
    }

    void flush_assertions() {
        bound_manager& bm = *m_bounds.back();
        for (unsigned i = 0; i < m_assertions.size(); ++i) {
            bm(m_assertions[i].get());
        }
        expr_safe_replace sub(m);
        accumulate_sub(sub);
        proof_ref proof(m);
        expr_ref fml1(m), fml2(m);
        if (sub.empty()) {
            m_solver->assert_expr(m_assertions);
        }
        else {
            for (unsigned i = 0; i < m_assertions.size(); ++i) {
                sub(m_assertions[i].get(), fml1);
                m_rewriter(fml1, fml2, proof);
                if (m.canceled()) {
                    m_rewriter.reset();
                    return;
                }
                m_solver->assert_expr(fml2);
                TRACE("int2bv", tout << fml2 << "\n";);
            }
        }
        m_assertions.reset();
        m_rewriter.reset();
    }
};

solver * mk_bounded_int2bv_solver(ast_manager & m, params_ref const & p, solver* s) {
    return alloc(bounded_int2bv_solver, m, p, s);
}
