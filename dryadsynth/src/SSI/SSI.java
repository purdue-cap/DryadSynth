import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class SSI extends Thread {
    protected Context ctx;
    protected SygusProblem problem;
    protected Logger logger;
    protected int numCore;
    // Current name that is being worked on
    protected String name;
    // Partial constraint that have only current function calls
    protected Expr pushedConstr;
    private Expr partConstr;
    private Expr[] callCache;
    private Expr funcExpr;
    private Set<Expr> compared;
    // Partial results stored for further use
    private Map<String, DefinedFunc> partResults;

    protected Tactic simp;

    public DefinedFunc results[];

    public class SSIException extends Exception {}

    public SSI(Context ctx, SygusProblem problem, Logger logger, int numCore) {
        this.ctx = ctx;
        this.problem = problem;
        this.logger = logger;
        this.numCore = numCore;

        this.compared = new HashSet<Expr>();
        this.partResults = new HashMap<String, DefinedFunc>();
        // ctx-solver-simplify would significantly reduce the speed for
        // constructITE() in large test cases
        // so we only do symbol based simplification here
        this.simp = ctx.repeat(ctx.then(
                    ctx.mkTactic("simplify"),
                    ctx.mkTactic("ctx-simplify")
                    ), 8);
    }
    public Expr getDef() throws SSIException {
        logger.info("Initializing SSI algorithm state.");
        this.compared.clear();
        // Initialize callcache to defined call args
        // If later called, would be overridden, if not, would kick in as fail-safe
        this.callCache = problem.requestUsedArgs.get(name);

        logger.info("Reducing constraint to partial constraint.");
        this.partConstr = this.reduceConstr(this.pushedConstr);
        logger.info("Partial constraint: " + this.partConstr.toString());

        logger.info("Scanning constraints.");
        this.scanExpr(this.normalizeExpr(this.partConstr));
        for (Expr e : compared) {
            logger.info("Candidate: " + e.toString());
        }

        this.funcExpr = problem.rdcdRequests.get(name).apply(this.callCache);

        logger.info("Constructing result...");
        Expr def = this.constructITE().simplify();
        logger.info("Constructed result for " + name + " :" + def.toString());

        return def;
    }

    public void run(){
        logger.info("Pushing in Nots in the constraint");
        try {
            this.pushedConstr = pushInNots(problem.finalConstraint);
        } catch(SSIException e) {
            this.results = null;
            return;
        }
        logger.info("Pushed constraint: " + this.pushedConstr.toString());

        this.results = new DefinedFunc[problem.names.size()];
        int i = 0;
        for (String funcName: problem.names) {
            this.name = funcName;
            logger.info("Working on func: " + funcName);
            Expr def;
            try {
                def = this.getDef();
            } catch(SSIException e) {
                this.results = null;
                return;
            }

            Expr defUsedArgs[] = problem.requestUsedArgs.get(name);
            Expr defArgs[] = problem.requestArgs.get(name);

            def = def.substitute(this.callCache, defUsedArgs);
            DefinedFunc interResult = new DefinedFunc(ctx, name, defUsedArgs, def);
            DefinedFunc result = new DefinedFunc(ctx, name, defArgs, def);
            this.partResults.put(name, interResult);
            this.results[i] = result;
            i++;
        }

    }

    // This function rewrites all currently solved functions with their solutions,
    // then removes all non-current unsolved functions' occurence.
    private Expr reduceConstr(Expr orig) throws SSIException{
        // Rewrites all currently solved functions
        Expr rewritten = orig;
        for (String funcName : this.partResults.keySet()){
            DefinedFunc func = this.partResults.get(funcName);
            FuncDecl decl = problem.rdcdRequests.get(funcName);
            rewritten = func.rewrite(rewritten, decl);
        }
        return killNonCurrent(rewritten).simplify();
    }

    private Expr killNonCurrent(Expr orig) throws SSIException{
        // For logical combinations,
        if (orig.isAnd() || orig.isOr()) {
            Expr[] args = new Expr[orig.getArgs().length];
            FuncDecl decl = orig.getFuncDecl();
            int i = 0;
            for (Expr arg : orig.getArgs()) {
                args[i] = killNonCurrent(arg);
                i++;
            }
            return decl.apply(args);
        }
        // For atomic comparisons
        if (orig.isGE() || orig.isGT() || orig.isLE() || orig.isLT() || orig.isEq()) {
            if (hasNonCurrent(orig)) {
                return ctx.mkTrue();
            } else {
                return orig;
            }
        }
        throw new SSIException();
    }

    // Determine if an atomic comparison has non current function calls
    private boolean hasNonCurrent(Expr expr) {
        if (expr.isApp()) {
            FuncDecl decl = expr.getFuncDecl();
            String funcName = decl.getName().toString();
            if (problem.names.contains(funcName) &&
                !funcName.equals(this.name)) {
                    return true;
            }
            boolean result = false;
            for (Expr arg : expr.getArgs()) {
                result = result || hasNonCurrent(arg);
            }
            return result;
        }
        return false;
    }

    // This function rewrites the expression so that the function calls
    // on an atomic formula only appears at right side
    private Expr normalizeExpr(Expr orig) throws SSIException {
        Params p = ctx.mkParams();
        p.add("arith_lhs", true);
        if (orig.isApp()) {
            Expr[] args = orig.getArgs();
            if (orig.isGE() || orig.isLT()|| orig.isGT() || orig.isLE() || orig.isEq()) {
                logger.info("Atom: " + orig.toString());
                Expr normed = orig.simplify(p);
                Expr left = normed.getArgs()[0];
                Expr right = normed.getArgs()[1];
                Expr funcTerm = null;
                List<Expr> remainingTerms = new ArrayList<Expr>();
                Expr[] leftArgs;
                if (left.isAdd()) {
                    leftArgs = left.getArgs();
                } else {
                    leftArgs = new Expr[] {left};
                }
                for (Expr expr: leftArgs) {
                    if (expr.isMul()) {
                        Expr inner = expr.getArgs()[1];
                        if (inner.isApp() && problem.rdcdRequests.values().contains(inner.getFuncDecl())) {
                            if (funcTerm != null) {
                                throw new SSIException();
                            }
                            funcTerm = expr;
                        } else {
                            remainingTerms.add(expr);
                        }

                    } else {
                        if (expr.isApp() && problem.rdcdRequests.values().contains(expr.getFuncDecl())) {
                            if (funcTerm != null) {
                                throw new SSIException();
                            }
                            funcTerm = expr;
                        } else {
                            remainingTerms.add(expr);
                        }
                    }
                }
                if (funcTerm == null) {
                    return orig;
                }
                right = ctx.mkMul(ctx.mkInt(-1), (ArithExpr)right);
                remainingTerms.add(right);
                funcTerm = ctx.mkMul(ctx.mkInt(-1), (ArithExpr)funcTerm);
                normed = normed.update(new Expr[] {ctx.mkAdd(
                            remainingTerms.toArray(new ArithExpr[remainingTerms.size()])
                            ).simplify(), funcTerm.simplify() });
                logger.info("Normalized:" + normed.toString());
                return normed;
            }
            List<Expr> newArgs = new ArrayList<Expr>();
            for (Expr arg: args) {
                newArgs.add(this.normalizeExpr(arg));
            }
            return orig.update(newArgs.toArray(new Expr[newArgs.size()]));
        }
        return orig;
    }

    private Expr constructITE() {
        if (this.compared.isEmpty()) {
            return ctx.mkInt(0);
        }
        Expr cond = this.partConstr;
        Expr cand = (Expr)this.compared.toArray()[0];
        this.compared.remove(cand);
        cond = cond.substitute(this.funcExpr, cand);
        cond = SygusFormatter.elimITE(ctx, cond);

        // Do simplification of conditions
        // This simplification does not change anything in most cases
        // but since non-solver simplifications are cheap
        // We're doing it anyway
        Goal g = ctx.mkGoal(false, false, false);
        g.add((BoolExpr)cond);
        cond = simp.apply(g).getSubgoals()[0].AsBoolExpr();

        return ctx.mkITE((BoolExpr)cond, cand, constructITE());
    }

    // Cache for pushInNots' DP algorithm
    private Map<Expr, Expr> pushInNotsCache = new HashMap<Expr, Expr>();
    // This function pushes in not expressions to completely elminate them.
    protected Expr pushInNots(Expr expr) throws SSIException {
        if (pushInNotsCache.containsKey(expr)) {
            return pushInNotsCache.get(expr);
        }
        if (expr.isConst() || expr.isIntNum() || expr.isTrue() || expr.isFalse()) {
            pushInNotsCache.put(expr, expr);
            return expr;
        }
        if (expr.isNot()) {
            Expr inner = expr.getArgs()[0];
            Expr[] innerArgs = inner.getArgs();
            Expr pushed;
            if (inner.isNot()) {
                inner = innerArgs[0];
                pushed = pushInNots(inner);
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            if (inner.isAnd()) {
                List<Expr> terms= new ArrayList<Expr>();
                for (Expr term: innerArgs) {
                    terms.add(ctx.mkNot((BoolExpr)term));
                }
                pushed = pushInNots(ctx.mkOr(terms.toArray(new BoolExpr[terms.size()])));
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            if (inner.isOr()) {
                List<Expr> terms= new ArrayList<Expr>();
                for (Expr term: innerArgs) {
                    terms.add(ctx.mkNot((BoolExpr)term));
                }
                pushed = pushInNots(ctx.mkAnd(terms.toArray(new BoolExpr[terms.size()])));
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            if (inner.isGE()) {
                pushed = pushInNots(ctx.mkLt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            if (inner.isGT()) {
                pushed = pushInNots(ctx.mkLe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            if (inner.isLT()) {
                pushed = pushInNots(ctx.mkGe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            if (inner.isLE()) {
                pushed = pushInNots(ctx.mkGt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            if (inner.isEq()) {
                pushed = pushInNots(ctx.mkOr(
                            ctx.mkLt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]),
                            ctx.mkGt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1])
                            ));
                pushInNotsCache.put(expr, pushed);
                return pushed;
            }
            Expr innerPushed = pushInNots(inner);
            pushed = pushInNots(ctx.mkNot((BoolExpr)innerPushed));
            pushInNotsCache.put(expr, pushed);
            return pushed;
        }
        // Implies and ITEs with bool sort has implicit nots and must be pushed in as well
        if (expr.isImplies())  {
            Expr[] args = expr.getArgs();
            Expr pushed = pushInNots(ctx.mkOr(ctx.mkNot((BoolExpr)args[0]), (BoolExpr)args[1]));
            pushInNotsCache.put(expr, pushed);
            return pushed;
        }
        if (expr.isITE() && expr.isBool()) {
            Expr[] args = expr.getArgs();
            Expr pushed = pushInNots(ctx.mkAnd(
                                ctx.mkOr(ctx.mkNot((BoolExpr)args[0]), (BoolExpr)args[1]),
                                ctx.mkOr((BoolExpr)args[0], (BoolExpr)args[2])));
            pushInNotsCache.put(expr, pushed);
            return pushed;
        }

        if (expr.isApp()){
            FuncDecl decl = expr.getFuncDecl();
            Expr[] args = new Expr[expr.getArgs().length];
            int i = 0;
            for (Expr arg : expr.getArgs()) {
                args[i] = pushInNots(arg);
                i++;
            }
            Expr pushed = decl.apply(args);
            pushInNotsCache.put(expr, pushed);
            return pushed;
        }
        throw new SSIException();
    }

    private void scanExpr(Expr expr) throws SSIException {
        if (expr.isConst()) {
            return;
        }
        if (expr.isApp()) {
            Expr[] args = expr.getArgs();
            if (expr.isLE()|| expr.isGE()|| expr.isLT()|| expr.isGT()|| expr.isEq()) {
                Expr left = args[0];
                Expr right = args[1];
                if (right.isApp() && problem.rdcdRequests.values().contains(right.getFuncDecl()))  {
                    if (expr.isGE()|| expr.isLE()|| expr.isEq()) {
                        this.compared.add(left);
                    } else if (expr.isGT()) {
                        this.compared.add(ctx.mkSub((ArithExpr)left, ctx.mkInt(1)));
                    } else if (expr.isLT()) {
                        this.compared.add(ctx.mkAdd((ArithExpr)left, ctx.mkInt(1)));
                    }
                    this.callCache = right.getArgs();
                } else if (right.isMul()) {
                    Expr inner = right.getArgs()[1];
                    if(inner.isApp() && problem.rdcdRequests.values().contains(inner.getFuncDecl())) {
                        if (right.getArgs()[0].equals(ctx.mkInt(-1))) {
                            if (expr.isGE()|| expr.isLE()|| expr.isEq()) {
                                this.compared.add(ctx.mkMul(ctx.mkInt(-1), (ArithExpr)left).simplify());
                            } else if (expr.isGT()) {
                                this.compared.add(ctx.mkMul(ctx.mkInt(-1), ctx.mkSub((ArithExpr)left, ctx.mkInt(1))).simplify());
                            } else if (expr.isLT()) {
                                this.compared.add(ctx.mkMul(ctx.mkInt(-1), ctx.mkAdd((ArithExpr)left, ctx.mkInt(1))).simplify());
                            }
                        this.callCache = inner.getArgs();
                        } else {
                            throw new SSIException();
                        }
                    }
                }
            }
            // Handling arguments.
            for (Expr arg: args) {
                this.scanExpr(arg);
            }
        }
    }
}
