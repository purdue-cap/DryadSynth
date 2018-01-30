import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class SSI {
    private Context ctx;
    private SygusExtractor extractor;
    private Logger logger;
    private int numCore;
    private Expr[] callCache;
    private Expr funcExpr;
    private Set<Expr> compared;

    private Tactic simp;

    public DefinedFunc results[];

    public class SSIException extends Exception {}

    public SSI(Context ctx, SygusExtractor extractor, Logger logger, int numCore) {
        this.ctx = ctx;
        this.extractor = extractor;
        this.logger = logger;
        this.numCore = numCore;

        this.compared = new HashSet<Expr>();
        // ctx-solver-simplify would significantly reduce the speed for 
        // constructITE() in large test cases
        // so we only do symbol based simplification here
        this.simp = ctx.repeat(ctx.then(
                    ctx.mkTactic("simplify"),
                    ctx.mkTactic("ctx-simplify")
                    ), 8);
    }
    public void run() throws SSIException {
        if (extractor.names.size() > 1) {
            throw new SSIException();
        }
        String name = extractor.names.get(0);
        logger.info("Scanning constraints.");
        this.scanExpr(extractor.finalConstraint);
        for (Expr e : compared) {
            logger.info("Candidate: " + e.toString());
        }

        this.funcExpr = extractor.rdcdRequests.get(name).apply(this.callCache);

        logger.info("Constructing result...");
        Expr def = this.constructITE().simplify();
        
        // Check result correctness
        //logger.info("Checking result correctness...");
        //Expr checkExpr = extractor.finalConstraint.substitute(this.funcExpr, def);
        //Solver s = ctx.mkSolver();
        //s.add(ctx.mkNot((BoolExpr)checkExpr));
        //Status sat = s.check();
        //if (sat == Status.UNSATISFIABLE) {
        //    logger.info("Result is correct.");
        //} else {
        //    logger.severe("Result incorrect!");
        //}

        Expr defUsedArgs[] = extractor.requestUsedArgs.get(name);
        Expr defArgs[] = extractor.requestArgs.get(name);

        def = def.substitute(this.callCache, defUsedArgs);
        DefinedFunc result = new DefinedFunc(ctx, name, defArgs, def);
        this.results = new DefinedFunc[]{result };
    }

    private Expr constructITE() {
        if (this.compared.isEmpty()) {
            return ctx.mkInt(0);
        }
        Expr cond = extractor.finalConstraint;
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
    
    private void scanExpr(Expr expr) throws SSIException {
        if (expr.isConst()) {
            return;
        }
        if (expr.isApp()) {
            Expr[] args = expr.getArgs();
            // Push in not expressions.
            if (expr.isNot()) {
                Expr inner = args[0];
                Expr[] innerArgs = inner.getArgs();
                if (inner.isNot()) {
                    inner = innerArgs[0];
                    this.scanExpr(inner);
                    return;
                }
                if (inner.isAnd()) {
                    List<Expr> terms= new ArrayList<Expr>();
                    for (Expr term: innerArgs) {
                        terms.add(ctx.mkNot((BoolExpr)term));
                    }
                    this.scanExpr(ctx.mkOr(terms.toArray(new BoolExpr[terms.size()])));
                    return;
                }
                if (inner.isOr()) {
                    List<Expr> terms= new ArrayList<Expr>();
                    for (Expr term: innerArgs) {
                        terms.add(ctx.mkNot((BoolExpr)term));
                    }
                    this.scanExpr(ctx.mkAnd(terms.toArray(new BoolExpr[terms.size()])));
                    return;
                }
                if (inner.isImplies()) {
                    this.scanExpr(ctx.mkNot(ctx.mkOr(ctx.mkNot((BoolExpr)innerArgs[0]), (BoolExpr)innerArgs[1])));
                    return;
                }
                if (inner.isITE()) {
                    this.scanExpr(ctx.mkNot(ctx.mkAnd(
                                    ctx.mkOr(ctx.mkNot((BoolExpr)innerArgs[0]), (BoolExpr)innerArgs[1]),
                                    ctx.mkOr((BoolExpr)innerArgs[0], (BoolExpr)innerArgs[2])
                                    )));
                    return;
                }
                if (inner.isGE()) {
                    this.scanExpr(ctx.mkLt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                    return;
                }
                if (inner.isGT()) {
                    this.scanExpr(ctx.mkLe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                    return;
                }
                if (inner.isLT()) {
                    this.scanExpr(ctx.mkGe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                    return;
                }
                if (inner.isLE()) {
                    this.scanExpr(ctx.mkGt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                    return;
                }
                if (inner.isEq()) {
                    this.scanExpr(ctx.mkOr(
                                ctx.mkLt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]), 
                                ctx.mkGt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1])
                                ));
                    return;
                }
            }
            if (expr.isLE()|| expr.isGE()|| expr.isLT()|| expr.isGT()|| expr.isEq()) {
                Expr left = args[0];
                Expr right = args[1];
                if (left.isApp() && extractor.rdcdRequests.values().contains(left.getFuncDecl())) {
                    if (expr.isGE()|| expr.isLE()|| expr.isEq()) {
                        this.compared.add(right);
                    } else if (expr.isGT()) {
                        this.compared.add(ctx.mkAdd((ArithExpr)right, ctx.mkInt(1)));
                    } else if (expr.isLT()) {
                        this.compared.add(ctx.mkSub((ArithExpr)right, ctx.mkInt(1)));
                    }
                    this.callCache = left.getArgs();
                } else if (right.isApp() && extractor.rdcdRequests.values().contains(right.getFuncDecl()))  {
                    if (expr.isGE()|| expr.isLE()|| expr.isEq()) {
                        this.compared.add(left);
                    } else if (expr.isGT()) {
                        this.compared.add(ctx.mkSub((ArithExpr)left, ctx.mkInt(1)));
                    } else if (expr.isLT()) {
                        this.compared.add(ctx.mkAdd((ArithExpr)left, ctx.mkInt(1)));
                    }
                    this.callCache = right.getArgs();
                }
            }
            // Handling arguments.
            for (Expr arg: args) {
                this.scanExpr(arg);
            }
        }
    }
}
