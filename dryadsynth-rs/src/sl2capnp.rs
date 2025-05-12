use anyhow::Result;
use capnp::{message::Builder, serialize_packed};
use sygus_parser::ast::{SyGuSFile, SyGuSCmd};

pub fn encode_file_text(src: &str) -> Result<Vec<u8>> {
    // parsing to AST
    let ast = SyGuSFile::from_str(src)
        .map_err(|e| anyhow::anyhow!("parse error: {e}"))?;

    // build capnp
    use crate::sygus_capnp::sygus_problem;
    let mut msg = Builder::new_default();
    {
        let mut root = msg.init_root::<sygus_problem::Builder>();

        // set-logic
        if let Some(SyGuSCmd::SetLogic(log)) =
            ast.cmds.iter().find(|c| matches!(c, SyGuSCmd::SetLogic(_)))
        {
            root.set_logic(log.as_str().into());
        }

        // synth-fun
        let synths: Vec<_> = ast.cmds.iter().filter_map(|c| {
            if let SyGuSCmd::SynthFun(name, params, ret_sort, grammar) = c {
                Some((name, params, ret_sort, grammar))
            } else {
                None
            }
        }).collect();

        let mut funs = root.reborrow().init_functions(synths.len() as u32);
        for (i, (name, params, ret_sort, grammar_opt)) in synths.iter().enumerate() {
            let mut fd = funs.reborrow().get(i as u32);
            fd.set_name(name.as_str().into());
            fd.set_ret_type(format!("{ret_sort:?}").as_str().into());

            // Parameters
            let mut ps = fd.reborrow().init_params(params.len() as u32);
            for (j, p) in params.iter().enumerate() {
                let mut v = ps.reborrow().get(j as u32);
                v.set_name(p.name.as_str().into());
                v.set_sort(format!("{:?}", p.sort).as_str().into());
            }

            // grammar
            let gram = grammar_opt
                .as_ref()
                .map(|g| format!("{g:?}"))
                .unwrap_or_default();
            fd.set_grammar(gram.as_str().into());
        }

        // Constraints
        let cons: Vec<_> = ast.cmds.iter().filter_map(|c| {
            if let SyGuSCmd::Constraint(e) = c { Some(e) } else { None }
        }).collect();
        let mut cs = root.reborrow().init_constraints(cons.len() as u32);
        for (i, e) in cons.iter().enumerate() {
            cs.reborrow()
              .get(i as u32)
              .set_expr(format!("{e:?}").as_str().into());
        }
    }

    let mut buf = Vec::new();
    serialize_packed::write_message(&mut buf, &msg)?;
    Ok(buf)
}
