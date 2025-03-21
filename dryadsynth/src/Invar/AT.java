import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class AT extends Thread {

    private Context ctx;
    private SygusProblem problem;
    private Logger logger;

    public Expr trans;
    public Transf transfunc;
    public Expr pre;
    public Expr post;
    public Map<String, Expr> vars;
    public Expr[] argParas;

    public DefinedFunc[] results;

    public CEGISEnv env;

    public AT(Context ctx, SygusProblem problem, Logger logger) {
        this.ctx = ctx;
        this.problem = problem;
        this.logger = logger;
    }

    public AT(Context ctx, Logger logger, CEGISEnv env) {
        this.ctx = ctx;
        this.env = env;
        this.problem = this.env.problem.translate(this.ctx);
        this.logger = logger;
    }

    public void setEnv(CEGISEnv env) {
        this.env = env;
    }

    public void init() {

        vars = new LinkedHashMap<String, Expr>();

        Map<String, DefinedFunc[]> invConstraints = problem.invConstraints;
        logger.info("map size" + invConstraints.size());

        DefinedFunc[] invFunc = invConstraints.entrySet().iterator().next().getValue();
        if (invFunc.length != 3) {
            this.results = null;
            return;
        } else {
            pre = invFunc[0].getDef();
            logger.info("Pre");
            logger.info(pre.toString());
            trans = invFunc[1].getDef();
            logger.info("Trans");
            logger.info(trans.toString());
            post = invFunc[2].getDef();
            logger.info("Post");
            logger.info(post.toString());
        }

        Map<String, Expr[]> requestArgs = problem.requestArgs;
        argParas = requestArgs.entrySet().iterator().next().getValue();

        Map<String, Expr[]> requestUsedArgs = problem.requestSyntaxUsedArgs;
        Expr[] usedVars = requestUsedArgs.entrySet().iterator().next().getValue();
        for (Expr var: usedVars) {
            vars.put(var.toString(), var);
        }

        this.transfunc = Transf.fromTransfFormula(trans, vars, ctx);
        if (this.transfunc != null) {
            logger.info("Parsed tranf:" + this.transfunc.toString());
        }
    }

    public boolean checkAT() {
        if (problem.problemType != SygusProblem.ProbType.INV) {
            return false;
        }
        if (this.transfunc != null) {
            Set<Region> regions = this.transfunc.getRegions();
            for (Region r1: regions) {
                for (Region r2: regions) {
                    if (r1 == r2) {
                        continue;
                    }
                    BoolExpr intersec = ctx.mkAnd((BoolExpr)r1.toExpr(), (BoolExpr)r2.toExpr());
                    Solver s = ctx.mkSolver();
                    s.add(intersec);
                    Status r = s.check();
                    if (r != Status.UNSATISFIABLE) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void run () {
        env.runningThreads.incrementAndGet();
        this.solve();
        synchronized(env) {
            env.notify();
        }
        env.runningThreads.decrementAndGet();
    }

    public void solve() {
        this.evaluate(transfunc, pre, post, vars, argParas);
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

    public void evaluate(Transf t, Expr pre, Expr post, Map<String, Expr> vars, Expr[] argParas) {
        logger.info("Running algorithm on: ");
        logger.info("Transf expr:");
        logger.info(t.toExpr().toString());
        logger.info("Pre expr:");
        logger.info(pre.toString());
        logger.info("Post expr:");
        logger.info(post.toString());
        t.kExtend();
        long startTime = System.currentTimeMillis();
        Expr inv = t.run(pre);
        logger.info("Run " + t.lastRunIterCount + " iterations.");
        logger.info("Runtime:" + (System.currentTimeMillis() - startTime));
        logger.info("Result:");
        logger.info(inv.toString());

        inv = post_processing(inv);
        Tactic simp = ctx.repeat(ctx.then(ctx.mkTactic("simplify"), ctx.mkTactic("ctx-simplify"), ctx.mkTactic("ctx-solver-simplify")), 8);
        Goal g = ctx.mkGoal(false, false, false);
        g.add((BoolExpr)inv);
        inv = simp.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("After post processing:");
        logger.info(inv.toString());

        if (argParas == null) {
            argParas = vars.values().toArray(new Expr[vars.size()]);
        }
        DefinedFunc df = new DefinedFunc(ctx, problem.names.get(0), argParas, inv);
        logger.info("Checking if invariant is valid.");
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
            logger.info("Valid results");
            this.results = new DefinedFunc[] {df};
        } else {
            this.results = null;
            logger.severe("Invalid results for AT algorithm");
        }
    }

}
