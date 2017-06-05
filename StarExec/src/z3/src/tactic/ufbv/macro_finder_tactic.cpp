/*++
Copyright (c) 2012 Microsoft Corporation

Module Name:

    macro_finder_tactic.cpp

Abstract:

    Macro finder

Author:

    Christoph (cwinter) 2012-10-26

Notes:

--*/
#include"tactical.h"
#include"simplifier.h"
#include"basic_simplifier_plugin.h"
#include"arith_simplifier_plugin.h"
#include"bv_simplifier_plugin.h"
#include"macro_manager.h"
#include"macro_finder.h"
#include"extension_model_converter.h"
#include"macro_finder_tactic.h"

class macro_finder_tactic : public tactic {    

    struct imp {
        ast_manager & m_manager;
        bool m_elim_and;

        imp(ast_manager & m, params_ref const & p) : 
            m_manager(m),
            m_elim_and(false) {
            updt_params(p);
        }
        
        ast_manager & m() const { return m_manager; }
        
        
        void operator()(goal_ref const & g, 
                        goal_ref_buffer & result, 
                        model_converter_ref & mc, 
                        proof_converter_ref & pc,
                        expr_dependency_ref & core) {
            SASSERT(g->is_well_sorted());
            mc = 0; pc = 0; core = 0;
            tactic_report report("macro-finder", *g);
            fail_if_unsat_core_generation("macro-finder", g);

            bool produce_proofs = g->proofs_enabled();

            simplifier simp(m_manager);
            basic_simplifier_plugin * bsimp = alloc(basic_simplifier_plugin, m_manager);
            bsimp->set_eliminate_and(m_elim_and);
            simp.register_plugin(bsimp);
            arith_simplifier_params a_params;
            arith_simplifier_plugin * asimp = alloc(arith_simplifier_plugin, m_manager, *bsimp, a_params);
            simp.register_plugin(asimp);
            bv_simplifier_params bv_params;
            bv_simplifier_plugin * bvsimp = alloc(bv_simplifier_plugin, m_manager, *bsimp, bv_params);
            simp.register_plugin(bvsimp);
                
            macro_manager mm(m_manager, simp);
            macro_finder mf(m_manager, mm);
            
            expr_ref_vector forms(m_manager), new_forms(m_manager);
            proof_ref_vector proofs(m_manager), new_proofs(m_manager);            
            unsigned   size = g->size();
            for (unsigned idx = 0; idx < size; idx++) {
                forms.push_back(g->form(idx));
                proofs.push_back(g->pr(idx));                
            }

            mf(forms.size(), forms.c_ptr(), proofs.c_ptr(), new_forms, new_proofs);
        
            g->reset();
            for (unsigned i = 0; i < new_forms.size(); i++)
                g->assert_expr(new_forms.get(i), produce_proofs ? new_proofs.get(i) : 0, 0);

            extension_model_converter * evmc = alloc(extension_model_converter, mm.get_manager());
            unsigned num = mm.get_num_macros();
            for (unsigned i = 0; i < num; i++) {
                expr_ref f_interp(mm.get_manager());
                func_decl * f = mm.get_macro_interpretation(i, f_interp);
                evmc->insert(f, f_interp);
            }
            mc = evmc;
                        
            g->inc_depth();
            result.push_back(g.get());
            TRACE("macro-finder", g->display(tout););
            SASSERT(g->is_well_sorted());
        }

        void updt_params(params_ref const & p) {
            m_elim_and = p.get_bool("elim_and", false);
        }
    };

    imp *      m_imp;
    params_ref m_params;    
public:
    macro_finder_tactic(ast_manager & m, params_ref const & p):
        m_params(p) {
        m_imp = alloc(imp, m, p);
    }

    virtual tactic * translate(ast_manager & m) {
        return alloc(macro_finder_tactic, m, m_params);
    }
        
    virtual ~macro_finder_tactic() {
        dealloc(m_imp);
    }

    virtual void updt_params(params_ref const & p) {
        m_params = p;
        m_imp->updt_params(p);
    }

    virtual void collect_param_descrs(param_descrs & r) {
        insert_max_memory(r);
        insert_produce_models(r);
        insert_produce_proofs(r);
        r.insert("elim_and", CPK_BOOL, "(default: false) eliminate conjunctions during (internal) calls to the simplifier.");
    }
    
    virtual void operator()(goal_ref const & in, 
                            goal_ref_buffer & result, 
                            model_converter_ref & mc, 
                            proof_converter_ref & pc,
                            expr_dependency_ref & core) {
        (*m_imp)(in, result, mc, pc, core);
    }
    
    virtual void cleanup() {
        ast_manager & m = m_imp->m();
        imp * d = alloc(imp, m, m_params);
        std::swap(d, m_imp);        
        dealloc(d);
    }


};

tactic * mk_macro_finder_tactic(ast_manager & m, params_ref const & p) {
    return alloc(macro_finder_tactic, m, p);
}
