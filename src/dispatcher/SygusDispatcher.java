import java.util.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import com.microsoft.z3.*;

public class SygusDispatcher {
    public enum SolveMethod {
        PRESCREENED, CEGIS, SSI, SSICOMM, AT, GENERALDNC
    }
    SolveMethod method = SolveMethod.CEGIS;
    Context z3ctx;
    SygusExtractor extractor;
    SygusProblem problem;

    Logger logger;
    int numCore;
    int iterLimit = 0;
    int minFinite = 20;
    int minInfinite = 5;
    boolean maxsmtFlag = false;
    boolean enforceCEGIS = false;
    boolean enforceFHCEGIS = false;
    boolean enableITCEGIS = false;
    boolean heightsOnly = false;
    Thread mainThread;
    Thread [] threads = null;
    Map<String, Expr[]> callCache = null;
    Set<String> funcCalled = null;
    CEGISEnv env = null;
    DnCEnv dncEnv = null;
    Expr dncBaseExpr = null;
    Expr[] dncBaseArgs = null;

    AT preparedAT;
    Thread [] fallbackCEGIS = null;

    boolean nosolution = false;

    SygusDispatcher(Context z3ctx, SygusExtractor extractor) {
        this.z3ctx = z3ctx;
        this.extractor = extractor;
        this.problem = extractor.createProblem();
        this.logger = Logger.getLogger("main");
        this.numCore = Runtime.getRuntime().availableProcessors();
        this.mainThread = Thread.currentThread();
    }

    public void setNumCore(int cores) {
        this.numCore = cores;
    }

    public void setIterLimit(int iterLimit) {
        this.iterLimit = iterLimit;
    }

    public void setMinFinite(int min) {
        this.minFinite = min;
    }

    public void setMinInfinite(int min) {
        this.minInfinite = min;
    }

    public void setMaxSMTFlag(boolean maxsmt) {
        this.maxsmtFlag = maxsmt;
    }

    public SolveMethod getMethod(){
        return method;
    }

    public void setEnforceCEGIS(boolean enforce) {
        this.enforceCEGIS = enforce;
    }

    public void setEnforceFHCEGIS(boolean fhcegis) {
        this.enforceFHCEGIS = fhcegis;
    }

    public void setEnableITCEGIS(boolean enable) {
        this.enableITCEGIS = enable;
    }

    public void setHeightsOnly(boolean flag) {
        this.heightsOnly = flag;
    }

    public void prescreen() {
        if (this.enforceCEGIS) {
            logger.info("Enforcing CEGIS algorithms, skipping prescreen.");
            this.method = SolveMethod.CEGIS;
            return;
        }
        boolean checkResult = this.checkGeneral();
        if (checkResult) {
            checkResult = this.checkDnC();
            if (checkResult) {
                logger.info("General Divide and Conquer Algorithm applicable, applying");
                this.method = SolveMethod.GENERALDNC;
                return;
            }
            logger.info("General SyGuS problem detected, using CEGIS.");
            this.method = SolveMethod.CEGIS;
            return;
        }
        logger.info("Checking candidates generated from parsing.");
        checkResult = this.validateCandidates();
        if (checkResult) {
            logger.info("Parsed candidates are valid.");
            this.method = SolveMethod.PRESCREENED;
            return;
        }
        checkResult = this.checkSSIComm();
        if (checkResult) {
            logger.info("SSIComm detected, using SSI-Commutativity algorithm");
            this.method = SolveMethod.SSICOMM;
            return;
        }
        checkResult = this.checkSSI();
        if (checkResult) {
            logger.info("SSI Detected, using SSI algorithm");
            this.method = SolveMethod.SSI;
            return;
        }
        checkResult = this.checkAT();
        if (checkResult) {
            logger.info("AT detected, using AT algorithm");
            this.method = SolveMethod.AT;
            return;
        }

        return;
    }

    public void initAlgorithm() throws Exception{
        if (this.method == SolveMethod.PRESCREENED) {
            logger.info("Taking parsed candidates, skipping algorithm initialization.");
            return;
        }

        logger.info("Initializing CEGIS algorithm as prepared fallback.");
        env = new CEGISEnv();
        env.problem = problem;
        env.minFinite = minFinite;
        env.minInfinite = minInfinite;
        env.maxsmtFlag = maxsmtFlag;
        env.enforceFHCEGIS = enforceFHCEGIS;
        fallbackCEGIS = new Thread[numCore];
        env.pdc1D = new Producer1D();
        env.pdc1D.heightsOnly = heightsOnly;
        env.feedType = CEGISEnv.FeedType.HEIGHTONLY;
        if (numCore > 1) {
            for (int i = 0; i < numCore; i++) {
                Logger threadLogger = Logger.getLogger("main.thread" + i);
                threadLogger.setUseParentHandlers(false);
                FileHandler threadHandler = new FileHandler("log.thread." + i + ".txt", false);
                threadHandler.setFormatter(new SimpleFormatter());
                threadLogger.addHandler(threadHandler);
                if (enableITCEGIS) {
                    fallbackCEGIS[i] = new ITCegis(env, threadLogger);
                } else {
                    fallbackCEGIS[i] = new Cegis(env, threadLogger);
                }
                ((Cegis)fallbackCEGIS[i]).iterLimit = this.iterLimit;
            }
        } else {
            if (enableITCEGIS) {
                fallbackCEGIS[0] = new ITCegis(z3ctx, env, logger);
            } else {
                fallbackCEGIS[0] = new Cegis(z3ctx, env, logger);
            }
            ((Cegis)fallbackCEGIS[0]).iterLimit = this.iterLimit;
        }

        if (this.method == SolveMethod.GENERALDNC) {
            logger.info("Initializing DnC General CEGIS.");
            dncEnv = new DnCEnv();
            dncEnv.problem = new DnCProblem(problem);
            dncEnv.minFinite = minFinite;
            dncEnv.minInfinite = minInfinite;
            dncEnv.maxsmtFlag = maxsmtFlag;
            threads = new Thread[numCore];
            dncEnv.pdc1D = new Producer1D();
            dncEnv.feedType = CEGISEnv.FeedType.HEIGHTONLY;
            dncEnv.dncBaseExpr = this.dncBaseExpr;
            dncEnv.dncBaseArgs = this.dncBaseArgs;
            dncEnv.scanSubExprs();
            logger.info("Scanned subexprs:");
            for (Expr expr : dncEnv.dncPblms.keySet()) {
                logger.info(expr.toString());
            }
            if (numCore > 1) {
                for (int i = 0; i < numCore; i++) {
                    Logger threadLogger = Logger.getLogger("main.thread" + i);
                    threadLogger.setUseParentHandlers(false);
                    FileHandler threadHandler = new FileHandler("log.thread." + i + ".txt", false);
                    threadHandler.setFormatter(new SimpleFormatter());
                    threadLogger.addHandler(threadHandler);
                    threads[i] = new DnCegis(dncEnv, threadLogger);
                    ((Cegis)threads[i]).iterLimit = this.iterLimit;
                }
            } else {
                threads[0] = new DnCegis(z3ctx, dncEnv, logger);
                ((Cegis)threads[0]).iterLimit = this.iterLimit;
            }
        }

        if (this.method == SolveMethod.CEGIS) {
            if (this.enforceCEGIS) {
                logger.info("Enforcing CEGIS.");
            } else {
                logger.info("No decidable fragment found, fallback to CEGIS.");
            }
            threads = fallbackCEGIS;
            return;
        }

        if (this.method == SolveMethod.SSICOMM) {
            logger.info("Initializing SSI-Commu algorithms.");
            // Single thread only algorithm, ignoring numCore settings.
            threads = new Thread[1];
            threads[0] = new SSICommu(z3ctx, problem, logger, numCore);
            return;
        }

        if (this.method == SolveMethod.SSI) {
            logger.info("Initializing SSI algorithms.");
            // Single thread only algorithm, ignoring numCore settings.
            threads = new Thread[1];
            threads[0] = new SSI(z3ctx, problem, logger, numCore);
            return;
        }

        if (this.method == SolveMethod.AT) {
            logger.info("Initializing AT algorithms.");
            // Single thread only algorithm, ignoring numCore settings.
            threads = new Thread[1];
            threads[0] = preparedAT;
            return;
        }

    }

    public DefinedFunc[] runAlgorithm() throws Exception{
        if (this.method == SolveMethod.PRESCREENED) {
            logger.info("Outputing parsed candidates as results.");
            List<DefinedFunc> resList = new ArrayList<DefinedFunc>();
            for (String name : problem.candidate.keySet()) {
                resList.add(problem.candidate.get(name).replaceName(name));
            }
            return resList.toArray(new DefinedFunc[resList.size()]);
        }

        DefinedFunc[] results = null;
        if (this.method == SolveMethod.GENERALDNC) {
            logger.info("Starting DnC General CEGIS.");
            if (numCore > 1) {
                for (int i = 0; i < numCore; i++) {
                    threads[i].start();
                }
        		while (results == null) {
                    synchronized(dncEnv) {
                        dncEnv.wait();
                    }
        			for (Thread thread : threads) {
                        DnCegis cegis = (DnCegis)thread;
        				if (cegis.results != null) {
                            results = cegis.results;
        				}
        			}
                    if (dncEnv.runningThreads.get() == 0) {
                        return results;
                    }
        		}
            } else {
                threads[0].run();
                results = ((Cegis)threads[0]).results;
            }
        }

        if (this.method == SolveMethod.SSI) {
            logger.info("Starting SSI algorithms.");
            threads[0].run();
            results = ((SSI)threads[0]).results;
        }

        if (this.method == SolveMethod.SSICOMM) {
            logger.info("Starting SSI-Comm algorithms.");
            threads[0].run();
            results = ((SSICommu)threads[0]).results;
        }

        if (this.method == SolveMethod.AT){
            logger.info("Starting AT algorithms.");
            threads[0].run();
            results = ((AT)threads[0]).results;
        }

        if (this.method == SolveMethod.CEGIS || (results == null) && (iterLimit == 0)) {
            logger.info("Starting fallback CEGIS algorithms execution.");
            if (numCore > 1) {
                for (int i = 0; i < numCore; i++) {
                    fallbackCEGIS[i].start();
                }
        		while (results == null) {
                    synchronized(env) {
                        env.wait();
                    }
                    int resultHeight = 0;
        			for (Thread thread : fallbackCEGIS) {
                        Cegis cegis = (Cegis)thread;
        				if (cegis.results != null) {
                            results = cegis.results;
                            resultHeight = cegis.resultHeight;
                            if (cegis.nosolution) {
                                nosolution = true;
                            }
        				}
        			}
                    if (env.runningThreads.get() == 0) {
                        return results;
                    }
                    if (env.checkITOnly){
                        for (Thread thread : fallbackCEGIS) {
                            Cegis cegis = (Cegis)thread;
                            cegis.running = false;
                        }
                        return results;
                    }
                    if (heightsOnly) {
                        System.out.println("resultHeight:" + new Integer(resultHeight).toString());
                        return null;
                    }
        		}
            } else {
                int resultHeight;
                fallbackCEGIS[0].run();
                results = ((Cegis)fallbackCEGIS[0]).results;
                resultHeight = ((Cegis)fallbackCEGIS[0]).resultHeight;
                if (((Cegis)fallbackCEGIS[0]).nosolution) {
                    nosolution = true;
                }
                if (heightsOnly) {
                    System.out.println("resultHeight:" + new Integer(resultHeight).toString());
                    return null;
                }
            }
        }
        return results;

    }

    public boolean checkTmplt() throws Exception {
        boolean oldCEGIS = this.enforceCEGIS;
        boolean oldITCEGIS = this.enableITCEGIS;
        this.initAlgorithm();
        this.env.checkITOnly = true;
        this.runAlgorithm();
        this.enforceCEGIS = oldCEGIS;
        this.enableITCEGIS = oldITCEGIS;
        return this.env.tmpltApplied;
    }

    boolean checkGeneral() {
        return problem.isGeneral;
    }

    boolean validateCandidates() {
        Solver solver = z3ctx.mkSolver();
        Expr spec = problem.finalConstraint;
        for (String name : problem.candidate.keySet()) {
			FuncDecl f = problem.rdcdRequests.get(name);
			Expr[] args = problem.requestUsedArgs.get(name);
			DefinedFunc df = problem.candidate.get(name).replaceArgs(args);
			spec = df.rewrite(spec, f);
        }
        solver.push();
        solver.add(z3ctx.mkNot((BoolExpr)spec));
        Status status = solver.check();
        solver.pop();
        return status == Status.UNSATISFIABLE;


    }

    boolean checkSSIComm() {
        if (problem.problemType != SygusProblem.ProbType.CLIA) {
            return false;
        }
        boolean flag = false;
        List<BoolExpr> remainingConstrs = new ArrayList<BoolExpr>();
        for (Expr constr: problem.constraints) {
            if (isCommConstr(constr)) {
                flag = true;
            } else {
                remainingConstrs.add((BoolExpr)constr);
            }
        }
        if (!flag) {
            return false;
        }
        Expr spec = z3ctx.mkAnd(remainingConstrs.toArray(new BoolExpr[remainingConstrs.size()]));
        this.callCache = new HashMap<String, Expr[]>();
        this.funcCalled = new HashSet<String>();
        return isSSI(spec);
    }

    boolean checkSSI(){
        if (problem.problemType != SygusProblem.ProbType.CLIA) {
            return false;
        }
        Expr spec = problem.finalConstraint;
        this.callCache = new HashMap<String, Expr[]>();
        this.funcCalled = new HashSet<String>();
        return isSSI(spec);
    }

    boolean isCommConstr(Expr expr) {
        if (!expr.isEq()) {
            return false;
        }
        Expr left = expr.getArgs()[0];
        Expr right = expr.getArgs()[1];
        if (!(left.isApp() && right.isApp())) {
            return false;
        }
        if (!(left.getFuncDecl().equals(right.getFuncDecl()))) {
            return false;
        }
        if (left.getFuncDecl().getDomain().length != 2) {
            return false;
        }
        Expr arg1 = left.getArgs()[0];
        Expr arg2 = left.getArgs()[1];
        if (!(arg1.equals(right.getArgs()[1]) &&
              arg2.equals(right.getArgs()[0]))) {
            return false;
        }
        return true;
    }

    boolean isSSI(Expr expr) {
        boolean result = true;
        if (expr.isApp()) {
            Expr[] args = expr.getArgs();
            FuncDecl exprFunc = expr.getFuncDecl();
            String funcName = exprFunc.getName().toString();
            if (problem.names.contains(funcName) &&
                exprFunc.equals(problem.rdcdRequests.get(funcName)))  {
                // For SSI, one atomic expression should have only one particular function
                if (funcCalled.contains(funcName)) {
                    return false;
                }
                if (!z3ctx.getIntSort().equals(exprFunc.getRange())) {
                    return false;
                }
                if (this.callCache.keySet().contains(funcName)) {
                    if (!Arrays.equals(args, this.callCache.get(funcName))) {
                        return false;
                    }
                } else {
                    this.callCache.put(funcName, args);
                }
                funcCalled.add(funcName);
            }
            for (Expr arg: args) {
                result = result && isSSI(arg);
            }
            // After atomic expression evalutation, reset the funcCalled set
            if (expr.isGE() || expr.isLT()|| expr.isGT() || expr.isLE() || expr.isEq()) {
                funcCalled.clear();
            }
        }
        return result;
    }

    boolean checkAT() {
        if (problem.problemType != SygusProblem.ProbType.INV) {
            return false;
        }
        this.preparedAT = new AT(z3ctx, problem, logger);
        this.preparedAT.init();
        if (this.preparedAT.transfunc != null) {
            Set<Region> regions = this.preparedAT.transfunc.getRegions();
            for (Region r1: regions) {
                for (Region r2: regions) {
                    if (r1 == r2) {
                        continue;
                    }
                    BoolExpr intersec = z3ctx.mkAnd((BoolExpr)r1.toExpr(), (BoolExpr)r2.toExpr());
                    Solver s = z3ctx.mkSolver();
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

    boolean checkDnC() {
        if (problem.problemType != SygusProblem.ProbType.GENERAL) {
            return false;
        }
        if (problem.requests.size() > 1) {
            return false;
        }
        FuncDecl target = problem.requests.get(problem.names.get(0));
        if (problem.constraints.size() > 1){
            return false;
        }
        BoolExpr spec = problem.constraints.get(0);
        if (!spec.isEq()) {
            return false;
        }
        Expr left = spec.getArgs()[0];
        Expr right = spec.getArgs()[1];
        if (left.getFuncDecl().equals(target) &&
                isConcrete(right)) {
            this.dncBaseExpr = right;
            this.dncBaseArgs = left.getArgs();
            return true;
        }
        if (right.getFuncDecl().equals(target) &&
                isConcrete(left)) {
            this.dncBaseExpr = left;
            this.dncBaseArgs = right.getArgs();
            return true;
        }
        return false;
    }

    boolean isConcrete(Expr expr) {
        if (expr.isNumeral()) {
            return true;
        }
        if (expr.isConst()) {
            String varName = expr.toString();
            if (problem.vars.containsKey(varName)) {
                return true;
            }
            return false;
        }
        if (expr.isApp()) {
            FuncDecl decl = expr.getFuncDecl();
            String funcName = decl.getName().toString();
            if (OpDispatcher.internalOps.contains(funcName) || problem.funcs.containsKey(funcName)) {
                boolean result = true;
                for (int i = 0; i < expr.getNumArgs(); i++){
                    result = result && isConcrete(expr.getArgs()[i]);
                }
                return result;
            }
        }
        return false;
    }
}
