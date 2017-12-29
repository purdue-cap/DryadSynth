import java.util.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import com.microsoft.z3.*;

public class SygusDispatcher {
    public enum SolveMethod {
        PRESCREENED, CEGIS, SININV
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

    // For legacy SININV codes
    // Todo: Refine SININV codes to fit the design pattern here
    // The method shall create worker thread objects in initAlgorithm
    // And shall run these threads and wait for results in runAlgorithm
    NewMethod newMethod;
    NewMutiWay newMutiWay;

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
        checkResult = this.checkSinInv();
        if (checkResult) {
            logger.info("Single Invocation detected, using SinInv method.");
            this.method = SolveMethod.SININV;
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

        if (this.method == SolveMethod.SININV) {
            logger.info("Initializing single invocation algorithms.");
            logger.info("Using legacy one-shot codes, no initialization is performanced.");
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

        if (this.method == SolveMethod.SININV) {
            logger.info("Starting single invocation algorithms execution.");
            logger.info("Using legacy codes, execution is one-shot.");
            Map<String, Expr> resultMap;
            if (numCore > 1) {
                newMutiWay = new NewMutiWay(this.z3ctx, this.extractor, this.logger, this.numCore);
                resultMap = newMutiWay.results;
            } else {
                newMethod = new NewMethod(this.z3ctx, this.extractor, this.logger);
                resultMap = newMethod.results;
            }

            logger.info("Results gathered, changing arguments.");
            int i = 0;
            DefinedFunc[] results = new DefinedFunc[resultMap.size()];
            for (String name : extractor.names) {
                Expr def = resultMap.get(name);
                def = def.substitute(this.callCache.get(name), extractor.requestUsedArgs.get(name));
                results[i] = new DefinedFunc(z3ctx, name, extractor.requestArgs.get(name), def);
                logger.info("Done, Synthesized function " + name);
                i = i + 1;
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

    boolean checkSinInv(){
        Expr spec = extractor.finalConstraint;
        this.callCache = new HashMap<String, Expr[]>();
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(spec);

        while (!todo.empty()) {
            Expr expr = todo.pop();
            if (expr.isApp()) {
                Expr[] args = expr.getArgs();
                for (Expr arg: args) {
                    todo.push(arg);
                }
                FuncDecl exprFunc = expr.getFuncDecl();
                String funcName = exprFunc.getName().toString();
                if (extractor.names.contains(funcName) &&
                    exprFunc.equals(extractor.rdcdRequests.get(funcName)))  {
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
                }
            }
        }
        return true;
    }
}
