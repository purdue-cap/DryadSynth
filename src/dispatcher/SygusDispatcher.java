import java.util.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import com.microsoft.z3.*;

public class SygusDispatcher {
    public enum SolveMethod {
        PRESCREENED, CEGIS, SSI, SSICOMM, AT
    }
    SolveMethod method = SolveMethod.CEGIS;
    Context z3ctx;
    SygusExtractor extractor;
    Logger logger;
    int numCore;
    int minFinite = 20;
    int minInfinite = 5;
    Thread mainThread;
    Thread [] threads = null;
    Map<String, Expr[]> callCache = null;
    boolean hasFunc = false;

    AT preparedAT;

    SygusDispatcher(Context z3ctx, SygusExtractor extractor) {
        this.z3ctx = z3ctx;
        this.extractor = extractor;
        this.logger = Logger.getLogger("main");
        this.numCore = Runtime.getRuntime().availableProcessors();
        this.mainThread = Thread.currentThread();
    }

    public void setNumCore(int cores) {
        this.numCore = cores;
    }

    public void setMinFinite(int min) {
        this.minFinite = min;
    }

    public void setMinInfinite(int min) {
        this.minInfinite = min;
    }

    public SolveMethod getMethod(){
        return method;
    }

    public void prescreen() {
        logger.info("Checking candidates generated from parsing.");
        boolean checkResult = this.validateCandidates();
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

        if (this.method == SolveMethod.CEGIS) {
            logger.info("Initializing default CEGIS algorithms.");
    		Producer1D pdc1d = new Producer1D();
    		threads = new Thread[numCore];
            if (numCore > 1) {
        		for (int i = 0; i < numCore; i++) {
        			Logger threadLogger = Logger.getLogger("main.thread" + i);
        			threadLogger.setUseParentHandlers(false);
        			FileHandler threadHandler = new FileHandler("log.thread." + i + ".txt", false);
        			threadHandler.setFormatter(new SimpleFormatter());
        			threadLogger.addHandler(threadHandler);
        			threads[i] = new Cegis(extractor, pdc1d, mainThread, threadLogger, minFinite, minInfinite);
        		}
            } else {
    			threads[0] = new Cegis(extractor, pdc1d, mainThread, logger, minFinite, minInfinite);
            }
            return;
        }

        if (this.method == SolveMethod.SSICOMM) {
            logger.info("Initializing SSI-Commu algorithms.");
            // Single thread only algorithm, ignoring numCore settings.
            threads = new Thread[1];
            threads[0] = new SSICommu(z3ctx, extractor, logger, numCore);
            return;
        }

        if (this.method == SolveMethod.SSI) {
            logger.info("Initializing SSI algorithms.");
            // Single thread only algorithm, ignoring numCore settings.
            threads = new Thread[1];
            threads[0] = new SSI(z3ctx, extractor, logger, numCore);
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
            for (String name : extractor.candidate.keySet()) {
                resList.add(extractor.candidate.get(name).replaceName(name));
            }
            return resList.toArray(new DefinedFunc[resList.size()]);
        }
        if (this.method == SolveMethod.CEGIS) {
            logger.info("Starting default CEGIS algorithms execution.");
            if (numCore > 1) {
                for (int i = 0; i < numCore; i++) {
                    threads[i].start();
                }
        		while (true) {
        			synchronized(mainThread) {
        				mainThread.wait();
        			}
        			for (Thread thread : threads) {
                        Cegis cegis = (Cegis)thread;
        				if (cegis.results != null) {
                            return cegis.results;
        				}
        			}
        		}
            } else {
                threads[0].run();
                return ((Cegis)threads[0]).results;
            }
        }

        if (this.method == SolveMethod.SSI) {
            logger.info("Starting SSI algorithms.");
            threads[0].run();
            DefinedFunc[] results = ((SSI)threads[0]).results;
            if (results == null) {
                throw(new Exception());
            }
            return results;
        }

        if (this.method == SolveMethod.SSICOMM) {
            logger.info("Starting SSI-Comm algorithms.");
            threads[0].run();
            DefinedFunc[] results = ((SSICommu)threads[0]).results;
            if (results == null) {
                throw(new Exception());
            }
            return results;
        }

        if (this.method == SolveMethod.AT){
            logger.info("Starting AT algorithms.");
            threads[0].run();
            DefinedFunc[] results = ((AT)threads[0]).results;
            if (results == null) {
                throw(new Exception());
            }
            return results;
        }
        return null;

    }

    boolean validateCandidates() {
        Solver solver = z3ctx.mkSolver();
        Expr spec = extractor.finalConstraint;
        for (String name : extractor.candidate.keySet()) {
			FuncDecl f = extractor.rdcdRequests.get(name);
			Expr[] args = extractor.requestUsedArgs.get(name);
			DefinedFunc df = extractor.candidate.get(name).replaceArgs(args);
			spec = df.rewrite(spec, f);
        }
        solver.push();
        solver.add(z3ctx.mkNot((BoolExpr)spec));
        Status status = solver.check();
        solver.pop();
        return status == Status.UNSATISFIABLE;


    }

    boolean checkSSIComm() {
        if (extractor.problemType != SygusExtractor.ProbType.CLIA) {
            return false;
        }
        boolean flag = false;
        List<BoolExpr> remainingConstrs = new ArrayList<BoolExpr>();
        for (Expr constr: extractor.constraints) {
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
        return isSSI(spec);
    }

    boolean checkSSI(){
        if (extractor.problemType != SygusExtractor.ProbType.CLIA) {
            return false;
        }
        Expr spec = extractor.finalConstraint;
        this.callCache = new HashMap<String, Expr[]>();
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
            if (extractor.names.contains(funcName) &&
                exprFunc.equals(extractor.rdcdRequests.get(funcName)))  {
                // For SSI, one atomic expression should have only one function
                if (hasFunc) {
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
                hasFunc = true;
            }
            for (Expr arg: args) {
                result = result && isSSI(arg);
            }
            // After atomic expression evalutation, reset the hasFunc flag
            if (expr.isGE() || expr.isLT()|| expr.isGT() || expr.isLE() || expr.isEq()) {
                hasFunc = false;
            }
        }
        return result;
    }

    boolean checkAT() {
        if (extractor.problemType != SygusExtractor.ProbType.INV) {
            return false;
        }
        this.preparedAT = new AT(z3ctx, extractor, logger);
        this.preparedAT.init();
        return false;
    }
}
