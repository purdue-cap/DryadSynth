
use std::hash::Hash;

use ahash::HashMap;
use openai::chat::{ChatCompletionMessage, ChatCompletionMessageRole::{System, self, User}, ChatCompletion};
use rand::{thread_rng, seq::SliceRandom};
use tree_sitter::{Parser, Language};

use crate::{parse::{PbeConstraint, SynthProblem, constraint::RefImplConstraint}, enumerate::expr::OwnedExpr};

use self::parsing::Env;

pub mod parsing;

extern "C" { fn tree_sitter_python() -> Language; }

pub async fn ask_chatgpt(system: String, text: String, count: usize, nargs: usize, gpt_version: String) -> Vec<HashMap<String, OwnedExpr>> {
    let smsg = ChatCompletionMessage { role: System, content: Some(system), name: None, function_call: None, tool_call_id: None, tool_calls: None };
    let umsg = ChatCompletionMessage { role: User, content: Some(text.clone()), name: None, function_call: None, tool_call_id: None, tool_calls: None };
    let mut res = Vec::new();
    loop {
        // println!("{}", text);
        let result = ChatCompletion::builder(gpt_version.as_str(), vec![smsg.clone(), umsg.clone()]).temperature(0.5).n(count as u8).max_tokens(3000 as u64).create().await;
        if let Ok(a) = result {
            for x in a.choices {
                let response = x.message.content.unwrap();
                // println!("-----------------------");
                // println!("{}", response);
                if let Ok(n) = Env::parse_response(&response, nargs) {
                    // if let Some(last) = n.maps.get("last") {
                    //     println!("{:?}", last);
                    // }
                    res.push(n.get_table());
                }
            }
            break;
        } 
        match result {
            Err(e) => println!("{:?}", e),
            // Ok(Err(e)) => println!("{:?}", e),
            _ => (),
        }
    };
    res
}


#[tokio::main]
pub async fn ask(problem: &SynthProblem<'_>, pbe: PbeConstraint, gpt_version: String) -> Vec<HashMap<String, OwnedExpr>> {
    let system = "You are a helpful assistant helping user synthesizing bit-vector programs. Please give results in steps.";
    let user = format!("Give me some useful subexpressions in each steps to {}. Assume all integers are {} bits.\n\n{}", problem.description.to_lowercase(), problem.bits, pbe.fmt(problem.args.len()));
    // println!("{}", user);
    ask_chatgpt(system.into(), user, 20, problem.args.len(), gpt_version).await
}

pub fn collect_subexpr(envs: Vec<HashMap<String, OwnedExpr>>) -> HashMap<OwnedExpr, usize> {
    let mut map = HashMap::with_hasher(Default::default());
    for e in envs.iter() {
        for (a, b) in e.iter() {
            if a != "last" {
                if !map.contains_key(b) {
                    map.insert(b.clone(), 0);
                }
                map.insert(b.clone(), map[b] + 1);
            }
        }
    }
    map
}



