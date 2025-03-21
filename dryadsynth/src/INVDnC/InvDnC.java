import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class InvDnC extends Thread {

    private Context ctx;
    private SygusProblem problem;
    private Logger logger;
    private int iterLimit;
    private SygusDispatcher.DnCType dnctype = SygusDispatcher.DnCType.RECPOST;

    String name;
    Expr pre;
    Expr trans;
    Expr post;
    List<Set<Expr>> relation = new ArrayList<Set<Expr>>();
    List<DefinedFunc[]> subresults = new ArrayList<DefinedFunc[]>();
    // SygusProblem[] invdncProblem = null;

    public DefinedFunc[] results;

    public CEGISEnv syncenv;

    public InvDnC (Context ctx, SygusProblem problem, Logger logger, SygusDispatcher.DnCType type, int iterLimit) {
        this.ctx = ctx;
        this.problem = problem;
        this.logger = logger;
        this.dnctype = type;
        this.iterLimit = iterLimit;
    }

    public InvDnC (Context ctx, Logger logger, CEGISEnv syncenv, SygusDispatcher.DnCType type, int iterLimit, SygusProblem problem) {
        this.ctx = ctx;
        this.syncenv = syncenv;
        // this.problem = this.syncenv.problem.translate(this.ctx);
        this.problem = problem.translate(this.ctx);
        this.logger = logger;
        this.dnctype = type;
        this.iterLimit = iterLimit;
    }

    public void setEnv(CEGISEnv syncenv) {
        this.syncenv = syncenv;
    }

    public void init() {
        // There should be only one invariant to synthesize
        name = problem.names.get(0);
        Set<Set<Expr>> varrelation = problem.varsRelation.get(name);
        for (Set<Expr> set : varrelation) {
            relation.add(set);
        }
        // invdncProblem = new SygusProblem[relation.size()];
        pre = problem.invConstraints.get(name)[0].getDef();
        trans = problem.invConstraints.get(name)[1].getDef();
        post = problem.invConstraints.get(name)[2].getDef();
    }

    public void run () {
        syncenv.runningThreads.incrementAndGet();
        results = solve();
        synchronized(syncenv) {
            syncenv.notify();
        }
        syncenv.runningThreads.decrementAndGet();
    }

    DefinedFunc[] solve() {
        for (int k = 0; k < relation.size(); k++) {
            // generate subproblems
            // if possible, set a timeout for QE
            SygusProblem kprob = genSubproblem(k);
            if (kprob == null) {
                continue;
            }
            // solve subproblems
            DefinedFunc[] subrslt = solveSubproblem(kprob);
            if (subrslt != null) {
                if (this.dnctype == SygusDispatcher.DnCType.RCCRSS) {
                    logger.info("DnCType.RCCRSS found a solution for subproblem, return.");
                    return subrslt;
                } else {
                    subresults.add(subrslt);
                }
            }
        }
        // then combine the result
        DefinedFunc[] combined = subresults.get(0);
        for (int i = 1; i < subresults.size(); i++) {
            DefinedFunc[] subrslt = subresults.get(i);
            for (int j = 0; j < combined.length; j++) {
                    Expr combinedSolution;
                    if (this.dnctype == SygusDispatcher.DnCType.RECPOST) {
                        combinedSolution = ctx.mkAnd((BoolExpr)combined[j].getDef().translate(ctx), 
                            (BoolExpr)subrslt[j].getDef().translate(ctx));
                    } else {       // if (this.dnctype == DnCType.CROSSPRE)
                        combinedSolution = ctx.mkOr((BoolExpr)combined[j].getDef().translate(ctx), 
                            (BoolExpr)subrslt[j].getDef().translate(ctx));
                    }
                    combined[j] = combined[j].replaceDef(combinedSolution);
            }
        }
        return combined;
    }

    DefinedFunc[] solveSubproblem(SygusProblem kProb) {
        if (kProb == null) {
            logger.info("Null subproblem.");
            return null;
        }
        // try AT first
        AT at = new AT(ctx, kProb, logger);
        at.init();
        boolean checkAT = at.checkAT();
        if (checkAT) {
            logger.info("AT found for subproblem.");
            at.solve();
            return at.results;
        } else {
            // then initialize CEGIS
            logger.info("Initializing CEGIS algorithm as prepared fallback.");
            // maybe no need to create an env every time
            CEGISEnv env = new CEGISEnv();
            env.original = kProb;
            env.problem = new SygusProblem(kProb);
            if (syncenv.enforceFHCEGIS) {
                env.problem.finalConstraint = SygusDispatcher.getIndFinalConstraint(kProb, ctx);
            }
            env.minFinite = syncenv.minFinite;
            env.minInfinite = syncenv.minInfinite;
            env.eqBound = syncenv.eqBound;
            env.maxsmtFlag = syncenv.maxsmtFlag;
            env.enforceFHCEGIS = syncenv.enforceFHCEGIS;
            env.pdc1D = new Producer1D();
            // env.pdc1D.heightsOnly = syncenv.heightsOnly;
            // Use EuSolver in place of CEGIS algorithm
            if (!env.EUSolverPath.isEmpty()) {
                // CEGIS algorithm should be considered as All-in-one
                env.feedType = CEGISEnv.FeedType.ALLINONE;
            } else {
                env.feedType = CEGISEnv.FeedType.HEIGHTONLY;
            }
            Cegis cegis = new Cegis(env, logger);
            cegis.iterLimit = this.iterLimit;
            // running cegis
            cegis.run();
            if (cegis.nosolution || cegis.results == null) {
                logger.info("No solution for this sub-problem.");
                return null;
            } else {
                return cegis.results;
            }
        }
    }

    SygusProblem genSubproblem(int k) {
        // construct the k-th subproblem for INV DnC
        logger.info("Start to generate the " + k + "-th sub-problem.");
        Set<Expr> exprset = relation.get(k);
        Set<Expr> otherset = new HashSet<Expr>();
        for (Expr e : problem.requestArgs.get(name)) {
            otherset.add(e);
        }
        otherset.removeAll(exprset);
        Expr[] args = exprset.toArray(new Expr[exprset.size()]);
        logger.info("Class: " + Arrays.toString(args));
        Expr[] otherargs = otherset.toArray(new Expr[otherset.size()]);
        Tactic qe = ctx.mkTactic("qe");
        Goal g = ctx.mkGoal(false, false, false);
        Quantifier withArg;
        Quantifier withoutArg;

        // pre first
        if (this.dnctype == SygusDispatcher.DnCType.RECPOST || this.dnctype == SygusDispatcher.DnCType.RCCRSS) {
            withoutArg = ctx.mkExists(
                otherargs, pre, 0, new Pattern[] {}, new Expr[] {}, ctx.mkSymbol(""), ctx.mkSymbol(""));
        } else {
            withoutArg = ctx.mkForall(
                otherargs, pre, 0, new Pattern[] {}, new Expr[] {}, ctx.mkSymbol(""), ctx.mkSymbol(""));
        }
        g.add(withoutArg);
        Expr prewoargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("Pre withoutargQF: " + prewoargQF.toString());

        // then post
        if (this.dnctype == SygusDispatcher.DnCType.RECPOST) {
            withoutArg = ctx.mkExists(
                otherargs, post, 0, new Pattern[] {}, new Expr[] {}, ctx.mkSymbol(""), ctx.mkSymbol(""));
        } else if (this.dnctype == SygusDispatcher.DnCType.CROSSPRE || this.dnctype == SygusDispatcher.DnCType.RCCRSS) {
            withoutArg = ctx.mkForall(
                otherargs, post, 0, new Pattern[] {}, new Expr[] {}, ctx.mkSymbol(""), ctx.mkSymbol(""));
        }
        g.reset();
        g.add(withoutArg);
        Expr postwoargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("Post withoutargQF: " + postwoargQF.toString());

        // check if pre => post
        Solver solver = ctx.mkSolver();
        BoolExpr preipost = ctx.mkImplies((BoolExpr)prewoargQF, (BoolExpr)postwoargQF);
        solver.add(ctx.mkNot(preipost));
        Status status = solver.check();
        if (status != Status.UNSATISFIABLE) {
            logger.info("Pre can not imply post. Discard this subproblem.");
            return null;
        }

        // finallly trans
        Expr[] primedArgs = new Expr[args.length];
        for (int i = 0; i < args.length; i++) {
            String varname = args[i].toString() + "!";
            primedArgs[i] = problem.vars.get(varname); // ctx.mkConst(varname + "!", args[i].getSort());
        }
        Expr[] primedOtherargs = new Expr[otherargs.length];
        for (int i = 0; i < otherargs.length; i++) {
            String varname = otherargs[i].toString() + "!";
            primedOtherargs[i] = problem.vars.get(varname); // ctx.mkConst(varname + "!", otherargs[i].getSort());
        }
        Expr[] argswprime = new Expr[args.length * 2];
        System.arraycopy(args, 0, argswprime, 0, args.length);
        System.arraycopy(primedArgs, 0, argswprime, args.length, primedArgs.length);
        Expr[] otherargswprime = new Expr[otherargs.length * 2];
        System.arraycopy(otherargs, 0, otherargswprime, 0, otherargs.length);
        System.arraycopy(primedOtherargs, 0, otherargswprime, otherargs.length, primedOtherargs.length);
        if (this.dnctype == SygusDispatcher.DnCType.RECPOST || this.dnctype == SygusDispatcher.DnCType.RCCRSS) {
            withoutArg = ctx.mkExists(
                otherargswprime, trans, 0, new Pattern[] {}, new Expr[] {}, ctx.mkSymbol(""), ctx.mkSymbol(""));
        } else if (this.dnctype == SygusDispatcher.DnCType.CROSSPRE) {
            withoutArg = ctx.mkForall(
                otherargswprime, trans, 0, new Pattern[] {}, new Expr[] {}, ctx.mkSymbol(""), ctx.mkSymbol(""));
        }
        g.reset();
        g.add(withoutArg);
        Expr transwoargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("Trans withoutargQF: " + transwoargQF.toString());

        SygusProblem kProb = problem.createINVSubProblem(args, argswprime, prewoargQF, transwoargQF, postwoargQF);

        logger.info(k + "-th problem finalConstraint: " + kProb.finalConstraint.toString());

        return kProb;
    }

}
