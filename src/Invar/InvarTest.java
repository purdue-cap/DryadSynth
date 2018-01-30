import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class InvarTest {

    private Context ctx;
    private SygusExtractor extractor;
    private Expr trans;

    public class InvarException extends Exception {}

    public InvarTest(Context ctx, SygusExtractor extractor) {
        this.ctx = ctx;
        this.extractor = extractor;
    }

    public void run() throws InvarException {

        Expr pre, post;
        Map<String, Expr> vars = new LinkedHashMap<String, Expr>();
        Expr[] argParas;

        Map<String, DefinedFunc[]> invConstraints = extractor.invConstraints;
        //System.out.println("map size" + invConstraints.size());

        DefinedFunc[] invFunc = invConstraints.entrySet().iterator().next().getValue();
        if (invFunc.length != 3) {
            throw new InvarException();
        } else {
            pre = invFunc[0].getDef();
            // System.out.println("Pre");
            // System.out.println();
            // System.out.println(pre);
            // System.out.println();
            trans = invFunc[1].getDef();
            // System.out.println("Trans");
            // System.out.println();
            // System.out.println(trans);
            // System.out.println();
            post = invFunc[2].getDef();
            // System.out.println("Post");
            // System.out.println();
            // System.out.println(post);
            // System.out.println();
        }

        Map<String, Expr[]> requestArgs = extractor.requestArgs;
        argParas = requestArgs.entrySet().iterator().next().getValue();

        Map<String, Expr[]> requestUsedArgs = extractor.requestUsedArgs;
        Expr[] usedVars = requestUsedArgs.entrySet().iterator().next().getValue();
        for (Expr var: usedVars) {
            vars.put(var.toString(), var);
        }

        Transf tr = new Transf(vars, ctx);
        Transf transfunc = tr.fromTransfFormula(trans, vars, ctx);
        
        test(transfunc, pre, post, vars, argParas);
    }
    

    public Expr post_processing(Expr orig) {
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(orig);
        Map<Expr, Expr> cache = new HashMap<Expr, Expr>();

        boolean visited;
        Expr expr, newExpr, body;
        Expr [] args, newArgsArray;
        List<Expr> newArgs = new ArrayList<Expr>();
        while (!todo.empty()) {
            expr = todo.peek();
            if (expr.isConst()) {
                todo.pop();
                cache.put(expr, expr);
            } else if (expr.isApp()) {
                visited = true;
                newArgs.clear();
                args = expr.getArgs();
                for (Expr arg: args) {
                    if (!cache.containsKey(arg)) {
                        todo.push(arg);
                        visited = false;
                    } else {
                        newArgs.add(cache.get(arg));
                    }
                }
                if (visited) {
                    boolean mod = false;
                    todo.pop();
                    newArgsArray = newArgs.toArray(new Expr[newArgs.size()]);
                    if(expr.isEq()) {
                        for (Expr arg: newArgsArray) {
                            if (arg.isConst()) {
                                mod = mod;
                            } else if (arg.isModulus()) {
                                mod = true;
                            }
                        }
                    }
                    if(mod) {
                        newExpr = ctx.mkTrue();
                    } else {
                        newExpr = expr.update(newArgsArray);
                    }
                    cache.put(expr, newExpr);
                }
            } else if(expr.isQuantifier()) {
                body = ((Quantifier)expr).getBody();
                if (cache.containsKey(body)) {
                    todo.pop();
                    newExpr = expr.update(new Expr[]{ cache.get(body) });
                    cache.put(expr, newExpr);
                } else {
                    todo.push(body);
                }
            } else {
                todo.pop();
                cache.put(expr, expr);
            }
        }
        return cache.get(orig);
    }
    
    public void test(Transf t, Expr pre, Expr post, Map<String, Expr> vars, Expr[] argParas) {
        //System.out.println("Running algorithm on: " + name);
        System.out.println("Transf expr:");
        System.out.println(t.toExpr());
        System.out.println("Pre expr:");
        System.out.println(pre);
        System.out.println("Post expr:");
        System.out.println(post);
        t.kExtend();
        long startTime = System.currentTimeMillis();
        Expr inv = t.run(pre);
        System.out.println("Run " + t.lastRunIterCount + " iterations.");
        System.out.println("Runtime:" + (System.currentTimeMillis() - startTime));
        System.out.println("Result:");
        System.out.println(inv);

        inv = post_processing(inv);
        Tactic simp = ctx.repeat(ctx.then(ctx.mkTactic("simplify"), ctx.mkTactic("ctx-simplify"), ctx.mkTactic("ctx-solver-simplify")), 8);
        Goal g = ctx.mkGoal(false, false, false);
        g.add((BoolExpr)inv);
        inv = simp.apply(g).getSubgoals()[0].AsBoolExpr();
        //System.out.println("After post processing:");
        //System.out.println(inv);

        if (argParas == null) {
            argParas = vars.values().toArray(new Expr[vars.size()]);
        }
        DefinedFunc df = new DefinedFunc(ctx, "inv-f", argParas, inv);
        String rawResult = df.toString();
        rawResult = rawResult.replaceAll("\\(\\s*-\\s+(\\d+)\\s*\\)", "-$1");
        rawResult = rawResult.replaceAll("\\s+", " ");
        System.out.println(rawResult);
        System.out.println("Checking if invariant is valid.");
        BoolExpr e1 = ctx.mkImplies((BoolExpr)pre, (BoolExpr)inv);
        Expr invp = inv;
        for (Expr var : vars.values()) {
           invp = invp.substitute(var,
                   ctx.mkConst(var.toString() + "!", ctx.mkIntSort()));
        }
        BoolExpr e2 = ctx.mkImplies(ctx.mkAnd(
                   (BoolExpr)inv, (BoolExpr)this.trans
                   ), (BoolExpr)invp);
        BoolExpr e3 = ctx.mkImplies((BoolExpr)inv, (BoolExpr)post);
        Solver s = ctx.mkSolver();
        s.add(ctx.mkNot(ctx.mkAnd(e1, e2, e3)));
        Status r = s.check();
        if ( r == Status.UNSATISFIABLE ) {
           System.out.println("Valid.");
        } else {
           System.out.println("Not Valid, status: " + r.toString());
        }
    }

}
