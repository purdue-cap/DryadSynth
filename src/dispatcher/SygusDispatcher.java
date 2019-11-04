import java.util.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
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
    int eqBound = 0;
    boolean maxsmtFlag = false;
    boolean enforceCEGIS = false;
    boolean enforceFHCEGIS = false;
    boolean enableITCEGIS = false;
    boolean heightsOnly = false;
    boolean methodOnly = false;
    Thread mainThread;
    Thread [] threads = null;
    Map<String, Expr[]> callCache = null;
    Set<String> funcCalled = null;
    CEGISEnv env = null;
    DnCEnv dncEnv = null;
    Expr dncBaseExpr = null;
    Expr[] dncBaseArgs = null;
    SygusProblem[] invdncProblem = null;
    boolean invDnC = false;

    AT preparedAT;
    Thread [] fallbackCEGIS = null;

    boolean nosolution = false;
    public enum ConvertMethod {
        FULLCLIA, ADDSUB, ITEWOSUB, ITEWOADDSUB, CONSTONLY, TENSONLY, NONE
    }
    public enum CoeffRange {
        ADDONLY, ADDSUB, TENSEQUAL, TENS, NONE
    }
    Map<String, CoeffRange> coeffRange;
    ConvertMethod converted = ConvertMethod.NONE;
    String[] resultStr = null;

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

    public void setEqBound(int eqbound) {
        this.eqBound = eqbound;
    }

    public void setMaxSMTFlag(boolean maxsmt) {
        this.maxsmtFlag = maxsmt;
    }

    public SolveMethod getMethod(){
        return method;
    }

    public ConvertMethod getConverted(){
        return converted;
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

    public void setMethodOnly(boolean checkmethod) {
        this.methodOnly = checkmethod;
    }

    public void prescreenGeneral() {
        // first check if a General track problem can be convert to CLIA or not
        if (!this.checkGeneral()) {
            return;
        }
        if (this.isFullCLIA()) {
            // TODO: distinguish clia & inv
            problem.problemType = SygusProblem.ProbType.CLIA;
            problem.isGeneral = false;
            this.converted = ConvertMethod.FULLCLIA;
            logger.info("FULLCLIA detected.");
            return;
        }
        if (this.isCLIAITEOnly()) {
            // TODO: distinguish clia & inv
            problem.problemType = SygusProblem.ProbType.CLIA;
            problem.isGeneral = false;
            this.converted = ConvertMethod.ITEWOSUB;
            logger.info("ITEWOSUB detected.");
            return;
        }
        if (this.isITEOnlyNoAddSub()) {
            // TODO: distinguish clia & inv
            problem.problemType = SygusProblem.ProbType.CLIA;
            problem.isGeneral = false;
            this.converted = ConvertMethod.ITEWOADDSUB;
            logger.info("ITEWOADDSUB detected.");
            return;
        }
        if (this.isConstantOnly()) {
            // for constant int functions only
            problem.problemType = SygusProblem.ProbType.CLIA;
            problem.isGeneral = false;
            this.converted = ConvertMethod.CONSTONLY;
            logger.info("CONSTONLY detected.");
            // should enforce cegis only,
            // the functions do not have any argument, they can pass SSI checking
            // but should not be solved by SSI
            this.enforceCEGIS = true;
            return;
        }
        coeffRange = new LinkedHashMap<String, CoeffRange>();
        if (this.isAddSubOnly(coeffRange)) {
            problem.problemType = SygusProblem.ProbType.CLIA;
            problem.isGeneral = false;
            // store coeffRange in problem, so that it can be used in cegis
            problem.coeffRange = coeffRange;
            this.converted = ConvertMethod.ADDSUB;
            // The result of SSI may not meet the grammar requirements,
            // so enforce the solving method to CEGIS
            this.enforceCEGIS = true;
            // fix height to 1, so single thread should be enough
            this.numCore = 1;
            logger.info("ADDSUB detected. Setting numCore to 1.");
            return;
        }
        coeffRange = new LinkedHashMap<String, CoeffRange>();
        if (this.isTensOnly(coeffRange)) {
            problem.problemType = SygusProblem.ProbType.CLIA;
            problem.isGeneral = false;
            problem.coeffRange = coeffRange;
            this.converted = ConvertMethod.TENSONLY;
            logger.info("TENSONLY detected.");
            for (String name : problem.names) {
                logger.info("func: " + name + ". coeffRange: " + coeffRange.get(name));
            }
            this.enforceCEGIS = true;
            // should use equation, as the grammar allows that
            this.eqBound = 20;
            // unexpected simplification for expr.simplify(), have to use single thread
            this.numCore = 1;
            return;
        }
        return;
    }

    public void prescreen() {
        // first check if a General track problem can be convert to CLIA or not
        prescreenGeneral();
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
        checkResult = this.checkINVDnC();
        if (checkResult) {
            logger.info("Invariant Divide and Conquer Algorithm applicable, applying");
            this.invDnC = true;
            // this.problem = invdncProblem[1];
            // System.exit(0);
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

    public void init() throws Exception {
        // postpone prescreen and initialization of INV DnC problems
        if (!this.invDnC) {
            initAlgorithm();
        }
    }

    public void initAlgorithm() throws Exception{
        if (this.method == SolveMethod.PRESCREENED) {
            logger.info("Taking parsed candidates, skipping algorithm initialization.");
            return;
        }

        logger.info("Initializing CEGIS algorithm as prepared fallback.");
        env = new CEGISEnv();
        env.original = problem;
        // env.original = new SygusProblem(problem);
        env.problem = new SygusProblem(problem);
        boolean isINV = true;   // CLIA benchmarks do not synthesize boolean functions
        for(String name : problem.names) {
            FuncDecl func = problem.rdcdRequests.get(name);
            if (!func.getRange().toString().equals("Bool")) {
                isINV = false;
            }
        }
        if (isINV && !problem.isGeneral && enforceFHCEGIS) {
            env.problem.finalConstraint = getIndFinalConstraint(problem);
        }
        env.minFinite = minFinite;
        env.minInfinite = minInfinite;
        env.eqBound = eqBound;
        env.maxsmtFlag = maxsmtFlag;
        env.enforceFHCEGIS = enforceFHCEGIS;
        fallbackCEGIS = new Thread[numCore];
        env.pdc1D = new Producer1D();
        env.pdc1D.heightsOnly = heightsOnly;
        env.feedType = CEGISEnv.FeedType.HEIGHTONLY;

        if (this.converted == ConvertMethod.ADDSUB) {
            // should fix height to 1, so set env feedtype to FIXED
            env.feedType = CEGISEnv.FeedType.FIXED;
            env.fixedHeight = 1;
            logger.info("ADDSUB detected. Setting cegis fixedHeight to 1.");
        }

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
            // threads = new Thread[1];
            // threads[0] = preparedAT;

            if (numCore > 1) {
                threads = fallbackCEGIS;
                Context atctx = new Context();
                // atctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
                preparedAT = new AT(atctx, logger, env);
                preparedAT.init();
                threads[0] = preparedAT;
            } else {
                threads = new Thread[1];
                preparedAT.setEnv(env);
                threads[0] = preparedAT;
            }

            return;
        }

    }

    public DefinedFunc[] run() throws Exception {
        if (!this.invDnC) {
            return runAlgorithm();
        } else {
            DefinedFunc[] combined = new DefinedFunc[problem.requests.size()];
            DefinedFunc[] subResults = null;

            for (int i = 0; i < invdncProblem.length; i++) {
                logger.info("Start solving subproblem " + i);
                this.problem = invdncProblem[i];
                // prescreen to check if the subproblem is AT applicable or not
                if (this.checkAT()) {
                    logger.info("AT detected for subproblem " + i + ", using AT algorithm");
                    this.method = SolveMethod.AT;
                } else {
                    this.method = SolveMethod.CEGIS;
                }
                // initial and run the algorithm on the subproblem
                initAlgorithm();
                subResults = runAlgorithm();
                // combine the solutions
                if (!this.nosolution && subResults != null) {
                    for (int j = 0; j < combined.length; j++) {
                        if (combined[j] != null) {
                            Expr combinedSolution = z3ctx.mkAnd((BoolExpr)combined[j].getDef(), 
                                    (BoolExpr)subResults[j].getDef());
                            combined[j] = combined[j].replaceDef(combinedSolution);
                        } else {
                            combined[j] = new DefinedFunc(subResults[j].ctx, subResults[j].getName(), subResults[j].getArgs(), subResults[j].getDef());
                        } 
                    }
                    logger.info("Combine solution for subproblem " + i);
                } else {
                    logger.info("No solution for subproblem " + i);
                }
                // reset nosolution
                this.nosolution = false;
            }
            // TODO: set final no solution

            return combined;
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
            // logger.info("Starting AT algorithms.");
            // threads[0].run();
            // results = ((AT)threads[0]).results;

            if (numCore > 1) {
                for (int i = 0; i < numCore; i++) {
                    threads[i].start();
                }
                while (results == null) {
                    synchronized(env) {
                        env.wait();
                    }
                    int resultHeight = 0;
                    AT at = (AT)threads[0];
                    if (at.results != null) {
                        results = at.results;
                        logger.info("AT got results.");
                        if (methodOnly) {
                            System.out.println("AT");
                        }
                    } 
                    for (int i = 1; i < numCore; i++) {
                        Cegis cegis = (Cegis)threads[i];
                        if (cegis.results != null) {
                            results = cegis.results;
                            resultHeight = cegis.resultHeight;
                            if (cegis.nosolution) {
                                nosolution = true;
                            }
                            if (methodOnly) {
                                System.out.println("CEGIS");
                            }
                        }
                    }
                    if (env.runningThreads.get() == 0) {
                        return results;
                    }
                    if (env.checkITOnly){
                        for (int i = 1; i < numCore; i++) {
                            Cegis cegis = (Cegis)threads[i];
                            cegis.running = false;
                        }
                        return results;
                    }
                    if (methodOnly) {
                        return null;
                    }
                    if (heightsOnly) {
                        System.out.println("resultHeight:" + new Integer(resultHeight).toString());
                        return null;
                    }
                }
            } else {
                logger.info("Starting AT algorithms.");
                if (methodOnly) {
                    System.out.println("AT");
                }
                threads[0].run();
                results = ((AT)threads[0]).results;
            }

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

    boolean iteConverted = true;
    public void postFormatting(DefinedFunc[] results) {
        if (this.converted == ConvertMethod.NONE) {
            logger.info("Nothing to format.");
            return;
        }
        if (this.converted == ConvertMethod.FULLCLIA || this.converted == ConvertMethod.ADDSUB) {
            logger.info("Formatting FullCLIA problem to fit in the grammar.");
            resultStr = new String[results.length];
            for (int i = 0; i < results.length; i++) {
                DefinedFunc result = results[i];
                Expr def = result.getDef();
                logger.info("Function: " + result.getName() + " def: " + def.toString() + ".");
                Expr transdef = def.translate(z3ctx);
                Map<Expr, String> cache = new LinkedHashMap<Expr, String>();
                Expr arg0 = result.getArgs()[0].translate(z3ctx);
                Expr newdef = formatFullCLIA(transdef, cache, arg0);
                logger.info("Function: " + result.getName() + ". Done formatting.");
                resultStr[i] = getResultStr(result.getName(), cache.get(transdef));
            }
            return;
        }
        if (this.converted == ConvertMethod.TENSONLY) {
            logger.info("Formatting TENSONLY problem to fit in the grammar.");
            resultStr = new String[results.length];
            for (int i = 0; i < results.length; i++) {
                DefinedFunc result = results[i];
                Expr def = result.getDef();
                logger.info("Function: " + result.getName() + " def: " + def.toString() + ".");
                Expr transdef = def.translate(z3ctx);

                transdef = timesTen(transdef);
                // resultStr[i] = transdef.toString();
                // System.exit(0);
                logger.info("After timesTen: " + transdef.toString());
                Map<Expr, String> cache = new LinkedHashMap<Expr, String>();
                Expr arg0 = result.getArgs()[0].translate(z3ctx);
                Expr newdef = formatFullCLIA(transdef, cache, arg0);
                logger.info("Function: " + result.getName() + ". Done formatting.");
                resultStr[i] = getResultStr(result.getName(), cache.get(transdef));
            }
            return;
        }
        if (this.converted == ConvertMethod.ITEWOSUB) {
            logger.info("Formatting ITEwoSUB problem to fit in the grammar.");
            resultStr = new String[results.length];
            for (int i = 0; i < results.length; i++) {
                DefinedFunc result = results[i];
                Expr def = result.getDef();
                logger.info("Function: " + result.getName() + " def: " + def.toString() + ".");
                Expr transdef = def.translate(z3ctx);
                logger.info("Function: " + result.getName() + " transdef: " + transdef.toString() + ".");
                // first push negation in
                Transf trans = new Transf(null, z3ctx);
                transdef = trans.pushNegIn(transdef);
                logger.info("Function: " + result.getName() + " pushNegIn def: " + transdef.toString() + ".");
                // then eliminate and or not
                int counter = 0;
                while (iteConverted) {
                    iteConverted = false;
                    transdef = convertToITE(transdef);
                }
                logger.info("Function: " + result.getName() + " iteConverted def: " + transdef.toString() + ".");
                // Solver testSolver = z3ctx.mkSolver();
                // testSolver.add(z3ctx.mkNot(z3ctx.mkEq(def.translate(z3ctx), transdef)));
                // Status testresult = testSolver.check();
                // logger.info("Function: " + result.getName() + " testresult: " + testresult.toInt());
                // System.exit(0);
                Map<Expr, String> cache = new LinkedHashMap<Expr, String>();
                Expr arg0 = result.getArgs()[0].translate(z3ctx);
                Expr newdef = formatFullCLIA(transdef, cache, arg0);
                logger.info("Function: " + result.getName() + ". Done formatting.");
                resultStr[i] = getResultStr(result.getName(), cache.get(transdef));
            }
            return;
        }
        if (this.converted == ConvertMethod.ITEWOADDSUB) {
            logger.info("Formatting ITEwoSUB problem to fit in the grammar.");
            resultStr = new String[results.length];
            for (int i = 0; i < results.length; i++) {
                DefinedFunc result = results[i];
                Expr def = result.getDef();
                logger.info("Function: " + result.getName() + " def: " + def.toString() + ".");
                Expr transdef = def.translate(z3ctx);
                logger.info("Function: " + result.getName() + " transdef: " + transdef.toString() + ".");
                // first push negation in
                Transf trans = new Transf(null, z3ctx);
                transdef = trans.pushNegIn(transdef);
                logger.info("Function: " + result.getName() + " pushNegIn def: " + transdef.toString() + ".");
                // then eliminate and or not
                int counter = 0;
                while (iteConverted) {
                    iteConverted = false;
                    transdef = convertToITE(transdef);
                }
                logger.info("Function: " + result.getName() + " iteConverted def: " + transdef.toString() + ".");
                Map<Expr, String> cache = new LinkedHashMap<Expr, String>();
                Expr arg0 = result.getArgs()[0].translate(z3ctx);
                Expr newdef = formatFullCLIA(transdef, cache, arg0);
                logger.info("Function: " + result.getName() + ". Done formatting.");
                resultStr[i] = getResultStr(result.getName(), cache.get(transdef));
            }
            return;
        }
        return;
    }

    public BoolExpr getIndFinalConstraint(SygusProblem origProblem) {
        BoolExpr newFinal = origProblem.finalConstraint;
        if (origProblem.invConstraints != null) {
            Map<String, DefinedFunc[]> invConstr = origProblem.invConstraints;
            newFinal = z3ctx.mkTrue();
            for (String key : invConstr.keySet()) {
                DefinedFunc pre = invConstr.get(key)[0];
                DefinedFunc trans = invConstr.get(key)[1];
                DefinedFunc post = invConstr.get(key)[2];
                // logger.info("pre: " + pre.getDef());
                // logger.info("trans: " + trans.getDef());
                // logger.info("post: " + post.getDef());
                FuncDecl inv = origProblem.requests.get(key);
                Expr[] vars = origProblem.requestArgs.get(key);
                Expr invapp = inv.apply(vars);
                Expr[] primedArgs = new Expr[vars.length];
                for (int i = 0; i < vars.length; i++) {
                    String name = vars[i].toString();
                    primedArgs[i] = z3ctx.mkConst(name + "!", vars[i].getSort());
                }
                BoolExpr inductive = z3ctx.mkImplies(z3ctx.mkAnd((BoolExpr)trans.getDef(),
                                            (BoolExpr)invapp),
                                    (BoolExpr)inv.apply(primedArgs));
                // logger.info("inductive before rewriting: " + inductive);
                Expr newDef = z3ctx.mkAnd((BoolExpr)post.getDef(), z3ctx.mkOr((BoolExpr)pre.getDef(), (BoolExpr)invapp));
                // logger.info("new inv with template: " + newDef);
                DefinedFunc newinv = new DefinedFunc(z3ctx, key, vars, newDef);
                inductive = (BoolExpr)newinv.rewrite(inductive, inv);
                // logger.info("inductive after rewriting: " + inductive);
                FuncDecl rdcdInv = origProblem.rdcdRequests.get(key);
                Expr[] rdcdVars = origProblem.requestUsedArgs.get(key);
                DefinedFunc df = new DefinedFunc(z3ctx, key, vars, rdcdInv.apply(rdcdVars));
                inductive = (BoolExpr)df.rewrite(inductive, inv);
                // logger.info("inductive with rdcd inv: " + inductive);
                if (invConstr.size() > 1) {
                    newFinal = z3ctx.mkAnd(newFinal, inductive);
                } else if (invConstr.size() == 1) {
                    newFinal = inductive;
                }
            }
        }
        return (BoolExpr)newFinal;
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

    boolean checkINVDnC() {
        if (problem.problemType != SygusProblem.ProbType.INV) {
            return false;
        }
        if (extractor.invConstraints.size() > 1) {
            return false;
        }
        for (String name : extractor.invConstraints.keySet()) {
            Set<Set<Expr>> relation = extractor.varsRelation.get(name);
            logger.info("Num of classes: " + relation.size());
            if (relation.size() <= 1) {
                logger.info("Only 1 class, no need to do divide and conquer.");
                return false;
            }
            invdncProblem = new SygusProblem[relation.size()];
            Expr pre = extractor.invConstraints.get(name)[0].getDef();
            Expr trans = extractor.invConstraints.get(name)[1].getDef();
            Expr post = extractor.invConstraints.get(name)[2].getDef();

            int k = 0;
            for(Set<Expr> exprset : relation) {
                Set<Expr> otherset = new HashSet<Expr>();
                for (Expr e : extractor.requestArgs.get(name)) {
                    otherset.add(e);
                }
                otherset.removeAll(exprset);
                Expr[] args = exprset.toArray(new Expr[exprset.size()]);
                logger.info("Class: " + Arrays.toString(args));
                Expr[] otherargs = otherset.toArray(new Expr[otherset.size()]);
                Tactic qe = z3ctx.mkTactic("qe");
                Goal g = z3ctx.mkGoal(false, false, false);
                Solver solver = z3ctx.mkSolver();
                Status status;

                // check pre first
                Quantifier withArg = z3ctx.mkExists(
                    args, pre, 0, new Pattern[] {}, new Expr[] {}, z3ctx.mkSymbol(""), z3ctx.mkSymbol(""));
                Quantifier withoutArg = z3ctx.mkExists(
                    otherargs, pre, 0, new Pattern[] {}, new Expr[] {}, z3ctx.mkSymbol(""), z3ctx.mkSymbol(""));
                g.add(withArg);
                Expr prewargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
                // logger.info("Pre withargQF: " + wargQF.toString());
                g.reset();
                g.add(withoutArg);
                Expr prewoargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
                // logger.info("Pre withoutargQF: " + woargQF.toString());
                BoolExpr preqf = z3ctx.mkAnd((BoolExpr)prewargQF, (BoolExpr)prewoargQF);
                // pre => exist x. pre /\ exist y. pre
                solver.add(z3ctx.mkNot(z3ctx.mkImplies((BoolExpr)pre, preqf)));
                status = solver.check();
                // logger.info("Pre Status: " + status);
                if (status != Status.UNSATISFIABLE) {
                    return false;
                }

                // check trans then
                Expr[] primedArgs = new Expr[args.length];
                for (int i = 0; i < args.length; i++) {
                    String varname = args[i].toString() + "!";
                    primedArgs[i] = extractor.vars.get(varname); // z3ctx.mkConst(varname + "!", args[i].getSort());
                }
                Expr[] primedOtherargs = new Expr[otherargs.length];
                for (int i = 0; i < otherargs.length; i++) {
                    String varname = otherargs[i].toString() + "!";
                    primedOtherargs[i] = extractor.vars.get(varname); // z3ctx.mkConst(varname + "!", otherargs[i].getSort());
                }
                Expr[] argswprime = new Expr[args.length * 2];
                System.arraycopy(args, 0, argswprime, 0, args.length);
                System.arraycopy(primedArgs, 0, argswprime, args.length, primedArgs.length);
                Expr[] otherargswprime = new Expr[otherargs.length * 2];
                System.arraycopy(otherargs, 0, otherargswprime, 0, otherargs.length);
                System.arraycopy(primedOtherargs, 0, otherargswprime, otherargs.length, primedOtherargs.length);
                withArg = z3ctx.mkExists(
                    argswprime, trans, 0, new Pattern[] {}, new Expr[] {}, z3ctx.mkSymbol(""), z3ctx.mkSymbol(""));
                withoutArg = z3ctx.mkExists(
                    otherargswprime, trans, 0, new Pattern[] {}, new Expr[] {}, z3ctx.mkSymbol(""), z3ctx.mkSymbol(""));
                g.reset();
                g.add(withArg);
                Expr transwargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
                // logger.info("Trans withargQF: " + transwargQF.toString());
                g.reset();
                g.add(withoutArg);
                Expr transwoargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
                // logger.info("Trans withoutargQF: " + transwoargQF.toString());
                BoolExpr transqf = z3ctx.mkAnd((BoolExpr)transwargQF, (BoolExpr)transwoargQF);
                // trans => exist x, x'. trans /\ exist y, y'. trans
                solver.reset();
                solver.add(z3ctx.mkNot(z3ctx.mkImplies((BoolExpr)trans, transqf)));
                status = solver.check();
                // logger.info("Post Status: " + status);
                if (status != Status.UNSATISFIABLE) {
                    return false;
                }

                // finally check post
                withArg = z3ctx.mkForall(
                    args, post, 0, new Pattern[] {}, new Expr[] {}, z3ctx.mkSymbol(""), z3ctx.mkSymbol(""));
                withoutArg = z3ctx.mkForall(
                    otherargs, post, 0, new Pattern[] {}, new Expr[] {}, z3ctx.mkSymbol(""), z3ctx.mkSymbol(""));
                g.reset();
                g.add(withArg);
                Expr postwargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
                // logger.info("Post withargQF: " + postwargQF.toString());
                g.reset();
                g.add(withoutArg);
                Expr postwoargQF = qe.apply(g).getSubgoals()[0].AsBoolExpr();
                // logger.info("Post withoutargQF: " + postwoargQF.toString());
                BoolExpr postqf = z3ctx.mkAnd((BoolExpr)postwargQF, (BoolExpr)postwoargQF);
                // forall x. post \/ forall y. post => post     (test with /\ before, but /\ might be too strong
                solver.reset();
                solver.add(z3ctx.mkNot(z3ctx.mkImplies(postqf, (BoolExpr)post)));
                status = solver.check();
                // logger.info("Post Status: " + status);
                if (status != Status.UNSATISFIABLE) {
                    return false;
                }

                invdncProblem[k] = extractor.createINVSubProblem(args, argswprime, prewoargQF, transwoargQF, postwoargQF);
                logger.info(k + "-th problem finalConstraint: " + invdncProblem[k].finalConstraint.toString());
                k = k + 1;
            }
        }
        return true;
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

    boolean isFullCLIA() {
        boolean result = true;
        // to check if a General track problem is actually a CLIA problem
        if (problem.problemType != SygusProblem.ProbType.GENERAL) {
            return false;
        }
        Map<String, SygusProblem.CFG> cfgs = problem.cfgs;
        for (String name : problem.names) {
            SygusProblem.CFG cfg = cfgs.get(name);
            result = result && isCLIA(name, cfg);
        }
        return result;
    }

    boolean isCLIA(String funcname, SygusProblem.CFG cfg) {
        // recognize the full CLIA grammar, like the one in max2

        // Int expressions
        boolean containsArgs = containsInputArgs(funcname, cfg);
        boolean containtsZero = false;
        boolean containsOne = false;
        boolean containsAdd = false;
        boolean containsMinus = false;
        boolean containsITE = false;

        // Bool expressions
        boolean containsAnd = false;
        boolean containsOr = false;
        boolean containsNot = false;
        boolean containsLe = false;
        boolean containsEq = false;
        boolean containsGe = false;

        boolean containsLt = false;
        boolean containsGt = false;

        Map<String, List<String[]>>  grammarRules = cfg.grammarRules;
        Map<String, Sort> grammarSybSort = cfg.grammarSybSort;

        if (grammarSybSort.size() != 2 || 
            !(grammarSybSort.containsValue(z3ctx.getIntSort()) 
                && grammarSybSort.containsValue(z3ctx.getBoolSort()))) {
            return false;
        }

        String intName = null;
        String boolName = null;

        for (String nonTermName : grammarRules.keySet()) {
            Sort sort = grammarSybSort.get(nonTermName);
            if (sort == z3ctx.getIntSort()) {
                intName = nonTermName;
            }
            if (sort == z3ctx.getBoolSort()) {
                boolName = nonTermName;
            }
        }

        List<String[]> intRuleLists = grammarRules.get(intName);
        for (String[] rules : intRuleLists) {
            if (rules.length == 1) {
                // 0 1
                if (rules[0].equals("0")) {
                    containtsZero = true;
                }
                if (rules[0].equals("1")) {
                    containsOne = true;
                }
            }
            if (rules.length == 3) {
                // + -
                if (rules[0].equals("+") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsAdd = true;
                }
                if (rules[0].equals("-") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsMinus = true;
                }
            }
            if (rules.length == 4) {
                // ite
                if (rules[0].equals("ite") && rules[1].equals(boolName) && rules[2].equals(intName) && rules[3].equals(intName)) {
                    containsITE = true;
                }
            }
        }

        List<String[]> boolRuleLists = grammarRules.get(boolName);
        for (String[] rules : boolRuleLists) {
            if (rules.length == 2) {
                // not
                if (rules[0].equals("not") && rules[1].equals(boolName)) {
                    containsNot = true;
                }
            }
            if (rules.length == 3) {
                if (rules[0].equals("and") && rules[1].equals(boolName) && rules[2].equals(boolName)) {
                    // and
                    containsAnd = true;
                }
                if (rules[0].equals("or") && rules[1].equals(boolName) && rules[2].equals(boolName)) {
                    // or
                    containsOr = true;
                }
                if (rules[0].equals("<=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // le
                    containsLe = true;
                }
                if (rules[0].equals("=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // eq
                    containsEq = true;
                }
                if (rules[0].equals(">=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // ge
                    containsGe = true;
                }
                if (rules[0].equals("<") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // lt
                    containsLt = true;
                }
                if (rules[0].equals(">") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // gt
                    containsGt = true;
                }
            }
        }

        if (containsArgs && containtsZero && containsOne && containsAdd && containsMinus && containsITE
            && containsAnd && containsOr && containsNot && containsLe && containsEq && containsGe) {
            return true;
        }

        // if the grammar does not match the conditions above, return false
        return false;
    }

    boolean isAddSubOnly(Map<String, CoeffRange> coeffRange) {
        boolean result = true;
        // to check if a General track problem is actually a CLIA problem
        if (problem.problemType != SygusProblem.ProbType.GENERAL) {
            return false;
        }
        Map<String, SygusProblem.CFG> cfgs = problem.cfgs;
        for (String name : problem.names) {
            SygusProblem.CFG cfg = cfgs.get(name);
            result = result && isAddSub(name, cfg, coeffRange);
        }
        return result;
    }

    boolean isAddSub(String funcname, SygusProblem.CFG cfg, Map<String, CoeffRange> coeffRange) {
        // recognize the Int only grammar, like the one in eightfuncs

        // Int expressions
        boolean containsArgs = containsInputArgs(funcname, cfg);
        boolean containsAdd = false;
        boolean containsMinus = false;

        Map<String, List<String[]>>  grammarRules = cfg.grammarRules;
        Map<String, Sort> grammarSybSort = cfg.grammarSybSort;

        if (grammarSybSort.size() != 1 || 
            !(grammarSybSort.containsValue(z3ctx.getIntSort()))) {
            coeffRange.put(funcname, CoeffRange.NONE);
            return false;
        }

        String intName = null;

        for (String nonTermName : grammarRules.keySet()) {
            intName = nonTermName;
        }

        int argNum = problem.requestArgs.get(funcname).length;
        List<String[]> intRuleLists = grammarRules.get(intName);

        if (intRuleLists.size() != argNum + 2 && intRuleLists.size() != argNum + 1) {
            // the grammar should only contain function parameters and "+" or "minus"
            coeffRange.put(funcname, CoeffRange.NONE);
            return false;
        }

        for (String[] rules : intRuleLists) {
            if (rules.length == 3) {
                // + -
                if (rules[0].equals("+") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsAdd = true;
                }
                if (rules[0].equals("-") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsMinus = true;
                }
            }
        }

        if (containsArgs && intRuleLists.size() == argNum + 2 && containsAdd && containsMinus) {
            coeffRange.put(funcname, CoeffRange.ADDSUB);
            return true;
        }
        if (containsArgs && intRuleLists.size() == argNum + 1 && containsAdd) {
            coeffRange.put(funcname, CoeffRange.ADDONLY);
            return true;
        }

        // if the grammar does not match the conditions above, return false
        coeffRange.put(funcname, CoeffRange.NONE);
        return false;
    }

    boolean isCLIAITEOnly() {
        boolean result = true;
        // to check if a General track problem is actually a CLIA problem
        if (problem.problemType != SygusProblem.ProbType.GENERAL) {
            return false;
        }
        Map<String, SygusProblem.CFG> cfgs = problem.cfgs;
        for (String name : problem.names) {
            SygusProblem.CFG cfg = cfgs.get(name);
            result = result && isITEOnly(name, cfg);
        }
        return result;
    }

    boolean isITEOnly(String funcname, SygusProblem.CFG cfg) {
        // recognize the partial CLIA grammar, no and or not sub, like the one in array_sum_2_5

        // Int expressions
        boolean containsArgs = containsInputArgs(funcname, cfg);
        boolean containtsZero = false;
        boolean containsOne = false;
        boolean containsAdd = false;
        boolean containsMinus = false;
        boolean containsITE = false;

        // Bool expressions
        boolean containsAnd = false;
        boolean containsOr = false;
        boolean containsNot = false;
        boolean containsLe = false;
        boolean containsEq = false;
        boolean containsGe = false;

        boolean containsLt = false;
        boolean containsGt = false;

        Map<String, List<String[]>>  grammarRules = cfg.grammarRules;
        Map<String, Sort> grammarSybSort = cfg.grammarSybSort;

        if (grammarSybSort.size() != 2 || 
            !(grammarSybSort.containsValue(z3ctx.getIntSort()) 
                && grammarSybSort.containsValue(z3ctx.getBoolSort()))) {
            return false;
        }

        String intName = null;
        String boolName = null;

        for (String nonTermName : grammarRules.keySet()) {
            Sort sort = grammarSybSort.get(nonTermName);
            if (sort == z3ctx.getIntSort()) {
                intName = nonTermName;
            }
            if (sort == z3ctx.getBoolSort()) {
                boolName = nonTermName;
            }
        }

        List<String[]> intRuleLists = grammarRules.get(intName);
        for (String[] rules : intRuleLists) {
            if (rules.length == 1) {
                // 0 1
                if (rules[0].equals("0")) {
                    containtsZero = true;
                }
                if (rules[0].equals("1")) {
                    containsOne = true;
                }
            }
            if (rules.length == 3) {
                // + -
                if (rules[0].equals("+") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsAdd = true;
                }
                if (rules[0].equals("-") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsMinus = true;
                }
            }
            if (rules.length == 4) {
                // ite
                if (rules[0].equals("ite") && rules[1].equals(boolName) && rules[2].equals(intName) && rules[3].equals(intName)) {
                    containsITE = true;
                }
            }
        }

        List<String[]> boolRuleLists = grammarRules.get(boolName);
        for (String[] rules : boolRuleLists) {
            if (rules.length == 2) {
                // not
                if (rules[0].equals("not") && rules[1].equals(boolName)) {
                    containsNot = true;
                }
            }
            if (rules.length == 3) {
                if (rules[0].equals("and") && rules[1].equals(boolName) && rules[2].equals(boolName)) {
                    // and
                    containsAnd = true;
                }
                if (rules[0].equals("or") && rules[1].equals(boolName) && rules[2].equals(boolName)) {
                    // or
                    containsOr = true;
                }
                if (rules[0].equals("<=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // le
                    containsLe = true;
                }
                if (rules[0].equals("=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // eq
                    containsEq = true;
                }
                if (rules[0].equals(">=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // ge
                    containsGe = true;
                }
                if (rules[0].equals("<") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // lt
                    containsLt = true;
                }
                if (rules[0].equals(">") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // gt
                    containsGt = true;
                }
            }
        }

        if (containsArgs && containtsZero && containsOne && containsAdd && !containsMinus && containsITE
            && !containsAnd && !containsOr && !containsNot  && !containsEq
            && containsLe && containsGe && containsLt && containsGt) {
            return true;
        }

        // if the grammar does not match the conditions above, return false
        return false;
    }

    boolean isITEOnlyNoAddSub() {
        boolean result = true;
        // to check if a General track problem is actually a CLIA problem
        if (problem.problemType != SygusProblem.ProbType.GENERAL) {
            return false;
        }
        Map<String, SygusProblem.CFG> cfgs = problem.cfgs;
        for (String name : problem.names) {
            SygusProblem.CFG cfg = cfgs.get(name);
            result = result && isITENoAddSub(name, cfg);
        }
        return result;
    }

    boolean isITENoAddSub(String funcname, SygusProblem.CFG cfg) {
        // recognize the partial CLIA grammar, no and or not sub, like the one in array_sum_2_5

        // Int expressions
        boolean containsArgs = containsInputArgs(funcname, cfg);
        boolean containtsZero = false;
        boolean containsOne = false;
        boolean containsAdd = false;
        boolean containsMinus = false;
        boolean containsITE = false;

        // Bool expressions
        boolean containsAnd = false;
        boolean containsOr = false;
        boolean containsNot = false;
        boolean containsLe = false;
        boolean containsEq = false;
        boolean containsGe = false;

        boolean containsLt = false;
        boolean containsGt = false;

        Map<String, List<String[]>>  grammarRules = cfg.grammarRules;
        Map<String, Sort> grammarSybSort = cfg.grammarSybSort;

        if (grammarSybSort.size() != 2 || 
            !(grammarSybSort.containsValue(z3ctx.getIntSort()) 
                && grammarSybSort.containsValue(z3ctx.getBoolSort()))) {
            return false;
        }

        String intName = null;
        String boolName = null;

        for (String nonTermName : grammarRules.keySet()) {
            Sort sort = grammarSybSort.get(nonTermName);
            if (sort == z3ctx.getIntSort()) {
                intName = nonTermName;
            }
            if (sort == z3ctx.getBoolSort()) {
                boolName = nonTermName;
            }
        }

        List<String[]> intRuleLists = grammarRules.get(intName);
        for (String[] rules : intRuleLists) {
            if (rules.length == 1) {
                // 0 1
                if (rules[0].equals("0")) {
                    containtsZero = true;
                }
                if (rules[0].equals("1")) {
                    containsOne = true;
                }
            }
            if (rules.length == 3) {
                // + -
                if (rules[0].equals("+") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsAdd = true;
                }
                if (rules[0].equals("-") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsMinus = true;
                }
            }
            if (rules.length == 4) {
                // ite
                if (rules[0].equals("ite") && rules[1].equals(boolName) && rules[2].equals(intName) && rules[3].equals(intName)) {
                    containsITE = true;
                }
            }
        }

        List<String[]> boolRuleLists = grammarRules.get(boolName);
        for (String[] rules : boolRuleLists) {
            if (rules.length == 2) {
                // not
                if (rules[0].equals("not") && rules[1].equals(boolName)) {
                    containsNot = true;
                }
            }
            if (rules.length == 3) {
                if (rules[0].equals("and") && rules[1].equals(boolName) && rules[2].equals(boolName)) {
                    // and
                    containsAnd = true;
                }
                if (rules[0].equals("or") && rules[1].equals(boolName) && rules[2].equals(boolName)) {
                    // or
                    containsOr = true;
                }
                if (rules[0].equals("<=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // le
                    containsLe = true;
                }
                if (rules[0].equals("=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // eq
                    containsEq = true;
                }
                if (rules[0].equals(">=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // ge
                    containsGe = true;
                }
                if (rules[0].equals("<") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // lt
                    containsLt = true;
                }
                if (rules[0].equals(">") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // gt
                    containsGt = true;
                }
            }
        }

        if (containsArgs && containtsZero && containsOne && !containsAdd && !containsMinus && containsITE
            && !containsAnd && !containsOr && !containsNot  && !containsEq
            && containsLe && containsGe && containsLt && containsGt) {
            return true;
        }

        // if the grammar does not match the conditions above, return false
        return false;
    }

    boolean isConstantOnly() {
        boolean result = true;
        // to check if a General track problem is actually synthesizing constant Int functions, basically "General_plus10.sl"
        if (problem.problemType != SygusProblem.ProbType.GENERAL) {
            return false;
        }
        Map<String, SygusProblem.CFG> cfgs = problem.cfgs;
        for (String name : problem.names) {
            SygusProblem.CFG cfg = cfgs.get(name);
            result = result && isConstantInt(name, cfg);
        }
        return result;
    }

    boolean isConstantInt(String funcname, SygusProblem.CFG cfg) {
        Map<String, List<String[]>>  grammarRules = cfg.grammarRules;
        Map<String, Sort> grammarSybSort = cfg.grammarSybSort;

        if (grammarSybSort.size() != 1 || 
            !grammarSybSort.containsValue(z3ctx.getIntSort())) {
            return false;
        }
        if (grammarRules.size() != 1) {
            return false;
        }
        for (String name : grammarRules.keySet()) {
            List<String[]> intRuleLists = grammarRules.get(name);
            if (intRuleLists.size() != 1) {
                return false;
            }
            String[] rules = intRuleLists.get(0);
            if (rules.length == 1 && rules[0].equals("ConstantInt")) {
                return true;
            }
        }
        return false;
    }

    boolean isTensOnly(Map<String, CoeffRange> coeffRange) {
        boolean result = true;
        // to check if a General track problem is synthesizing functions that only have 0 10 20 30 40 50 60 70 80 90 100
        if (problem.problemType != SygusProblem.ProbType.GENERAL) {
            return false;
        }
        Map<String, SygusProblem.CFG> cfgs = problem.cfgs;
        for (String name : problem.names) {
            SygusProblem.CFG cfg = cfgs.get(name);
            result = result && isTens(name, cfg, coeffRange);
        }
        return result;
    }

    boolean isTens(String funcname, SygusProblem.CFG cfg, Map<String, CoeffRange> coeffRange) {
        // recognize the grammar like the one in "s0_d0.sl"

        boolean containsArgs = containsInputArgs(funcname, cfg);
        boolean containtsTens = false;
        boolean containsAdd = false;
        boolean containsMinus = false;
        boolean containsITE = false;

        // Bool expressions
        boolean containsLe = false;
        boolean containsEq = false;
        boolean containsGe = false;

        Map<String, List<String[]>>  grammarRules = cfg.grammarRules;
        Map<String, Sort> grammarSybSort = cfg.grammarSybSort;

        if (grammarSybSort.size() != 2 || 
            !(grammarSybSort.containsValue(z3ctx.getIntSort()) 
                && grammarSybSort.containsValue(z3ctx.getBoolSort()))) {
            return false;
        }

        String intName = null;
        String boolName = null;

        for (String nonTermName : grammarRules.keySet()) {
            Sort sort = grammarSybSort.get(nonTermName);
            if (sort == z3ctx.getIntSort()) {
                intName = nonTermName;
            }
            if (sort == z3ctx.getBoolSort()) {
                boolName = nonTermName;
            }
        }

        List<String> tens = new ArrayList<String>();
        for (int i = 0; i < 101; i+=10) {
            tens.add(Integer.toString(i));
        }
        List<String[]> intRuleLists = grammarRules.get(intName);
        for (String[] rules : intRuleLists) {
            if (rules.length == 1) {
                // 0 1
                if (tens.contains(rules[0])) {
                    tens.remove(rules[0]);
                }
            }
            if (rules.length == 3) {
                // + -
                if (rules[0].equals("+") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsAdd = true;
                }
                if (rules[0].equals("-") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    containsMinus = true;
                }
            }
            if (rules.length == 4) {
                // ite
                if (rules[0].equals("ite") && rules[1].equals(boolName) && rules[2].equals(intName) && rules[3].equals(intName)) {
                    containsITE = true;
                }
            }
        }

        List<String[]> boolRuleLists = grammarRules.get(boolName);
        for (String[] rules : boolRuleLists) {
            if (rules.length == 3) {
                if (rules[0].equals("<=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // le
                    containsLe = true;
                }
                if (rules[0].equals("=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // eq
                    containsEq = true;
                }
                if (rules[0].equals(">=") && rules[1].equals(intName) && rules[2].equals(intName)) {
                    // ge
                    containsGe = true;
                }
            }
        }

        if (containsArgs && tens.isEmpty() && containsAdd && containsMinus && containsITE && containsEq && !containsLe && !containsGe) {
            coeffRange.put(funcname, CoeffRange.TENSEQUAL);
            return true;
        }
        if (containsArgs && tens.isEmpty() && containsAdd && containsMinus && containsITE && containsEq && containsLe && containsGe) {
            coeffRange.put(funcname, CoeffRange.TENS);
            return true;
        }

        // if the grammar does not match the conditions above, return false
        return false;
    }

    boolean containsInputArgs(String funcname, SygusProblem.CFG cfg) {
        boolean result = true;
        Map<String, List<String[]>>  grammarRules = cfg.grammarRules;
        Expr[] args = problem.requestArgs.get(funcname);

        for (Expr arg : args) {
            boolean containsArg = false;
            for (String currentSymbol : grammarRules.keySet()) {
                List<String[]> ruleLists = grammarRules.get(currentSymbol);
                for (String[] rules : ruleLists) {
                    String rule = rules[0];
                    // System.out.println("current rule: " + rule);
                    if (rule.equals(arg.toString())) {
                        // System.out.println("print arg: " + arg.toString());
                        containsArg = true;
                        break;
                    }
                }
            }
            result = result && containsArg;
        }
        return result;
    }

    Expr timesTen(Expr orig) {
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(orig);
        Map<Expr, Expr> cache = new HashMap<Expr, Expr>();

        boolean visited;
        Expr expr, newExpr, body;
        Expr [] args, newArgsArray;
        List<Expr> newArgs = new ArrayList<Expr>();
        FuncDecl exprFunc;
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
                    boolean isequal = false;
                    todo.pop();
                    newArgsArray = newArgs.toArray(new Expr[newArgs.size()]);
                    if (expr.isEq()) {
                        for (int i = 0; i < newArgsArray.length; i++) {
                            // System.out.println("EQ expr: " + expr.toString() + ". Arg: " + newArgsArray[i].toString());
                            if (newArgsArray[i].isIntNum()) {
                                int num = Integer.parseInt(newArgsArray[i].toString());
                                newArgsArray[i] = z3ctx.mkInt(num * 10);
                            } else {
                                newArgsArray[i] = z3ctx.mkMul((ArithExpr)newArgsArray[i], z3ctx.mkInt(10));
                            }
                            // System.out.println("EQ expr: " + expr.toString() + ". New arg: " + newArgsArray[i].toString());
                        }
                        newExpr = expr.update(newArgsArray);
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

    Expr formatFullCLIA(Expr expr, Map<Expr, String> cache, Expr arg0) {
        if (expr.isConst()) {
            String conststr = expr.toString();
            cache.put(expr, conststr);
            return expr;
        }
        if (expr.isIntNum()) {
            if (this.converted == ConvertMethod.TENSONLY || this.converted == ConvertMethod.ITEWOADDSUB) {
                cache.put(expr, expr.toString());
                return expr;
            }
            int num = Integer.parseInt(expr.toString());
            String conststr = expr.toString();
            if (num >= 2) {
                Expr plus = z3ctx.mkInt(1);
                String plusstr = "1";
                for (int j = 1; j < num; j++) {
                    plus = z3ctx.mkAdd((ArithExpr)plus, z3ctx.mkInt(1));
                    plusstr =  "(+ " + plusstr + " 1)";
                }
                conststr = plusstr;
            }
            if (num < 0) {
                Expr minus = z3ctx.mkInt(0);
                String minusstr = "0";
                if (this.converted == ConvertMethod.ADDSUB) {
                    minusstr = "(- " + arg0.toString() + " " + arg0.toString() + ")";
                }
                for (int j = 0; j < (0 - num); j++) {
                    minus = z3ctx.mkSub((ArithExpr)minus, z3ctx.mkInt(1));
                    minusstr =  "(- " + minusstr + " 1)";
                }
                conststr = minusstr;
            }
            if (num == 0) {
                if (this.converted == ConvertMethod.ADDSUB) {
                    conststr = "(- " + arg0.toString() + " " + arg0.toString() + ")";
                }
            }
            cache.put(expr, conststr);
            return expr;
        }
        if (expr.isApp()) {
            Expr[] args = expr.getArgs();
            Expr[] newArgs = new Expr[args.length];
            for (int i = 0; i < args.length; i++) {
                newArgs[i] = formatFullCLIA(args[i], cache, arg0);
            }
            if (expr.isAnd()) {
                // may have more than 2 conjuctions
                Expr and = newArgs[0];
                String andstr = cache.get(args[0]);
                for (int i = 1; i < args.length; i++) {
                    and = z3ctx.mkAnd((BoolExpr)and, (BoolExpr)newArgs[i]);
                    andstr = "(and " + andstr + " " + cache.get(args[i]) + ")";
                }
                cache.put(expr, andstr);
                return and;
            }
            if (expr.isOr()) {
                // may have more than 2 disjuctions
                Expr or = newArgs[0];
                String orstr = cache.get(args[0]);
                for (int i = 1; i < args.length; i++) {
                    or = z3ctx.mkOr((BoolExpr)or, (BoolExpr)newArgs[i]);
                    orstr = "(or " + orstr + " " + cache.get(args[i]) + ")";
                }
                cache.put(expr, orstr);
                return or;
            }
            if (expr.isNot()) {
                String notstr = "(not " + cache.get(args[0]) + ")";
                cache.put(expr, notstr);
                return z3ctx.mkNot((BoolExpr)newArgs[0]);
            }
            if (expr.isMul()) {
                // since CLIA, one arg is int number, the other one is variable
                int num = 0;
                Expr arg = null;
                if (args[0].isIntNum()) {
                    num = Integer.parseInt(args[0].toString());
                    arg = newArgs[1];
                } else {
                    arg = newArgs[0];
                    num = Integer.parseInt(args[1].toString());
                }
                if (num == 0) {
                    String zero = "0";
                    if (this.converted == ConvertMethod.ADDSUB) {
                        zero = "(- " + arg0.toString() + " " + arg0.toString() + ")";
                    }
                    cache.put(expr, zero);
                    return z3ctx.mkInt(0);
                }
                if (num > 0) {
                    Expr add = arg;
                    String addstr = cache.get(arg);
                    for (int i = 1; i < num; i++) {
                        add = z3ctx.mkAdd((ArithExpr)add, (ArithExpr)arg);
                        addstr = "(+ " + addstr + " " + cache.get(arg) + ")";
                    }
                    cache.put(expr, addstr);
                    return add;
                }
                if (num < 0) {
                    Expr sub = z3ctx.mkInt(0);
                    String substr = "0";
                    if (this.converted == ConvertMethod.ADDSUB) {
                        substr = "(- " + arg0.toString() + " " + arg0.toString() + ")";
                    } 
                    for (int i = 0; i < (0 - num); i++) {
                        sub = z3ctx.mkSub((ArithExpr)sub, (ArithExpr)arg);
                        substr = "(- " + substr + " " + cache.get(arg) + ")";
                    }
                    cache.put(expr, substr);
                    return sub;
                }
            }
            if (expr.isAdd()) {
                Expr add = newArgs[0];
                String addstr = cache.get(args[0]);
                for (int i = 1; i < args.length; i++) {
                    add = z3ctx.mkAdd((ArithExpr)add, (ArithExpr)newArgs[i]);
                    addstr = "(+ " + addstr + " " + cache.get(args[i]) + ")";
                    // logger.info("add arg: " + cache.get(args[i]));
                }
                cache.put(expr, addstr);
                return add;
            }
            if (expr.isSub()) {
                Expr sub = newArgs[0];
                String substr = cache.get(args[0]);
                for (int i = 1; i < args.length; i++) {
                    sub = z3ctx.mkSub((ArithExpr)sub, (ArithExpr)newArgs[i]);
                    substr = "(- " + substr + " " + cache.get(args[i]) + ")";
                }
                cache.put(expr, substr);
                return sub;
            }
            if (expr.isITE()) {
                String itestr = "(ite " + cache.get(args[0]) + " " + cache.get(args[1]) + " " + cache.get(args[2]) + ")";
                cache.put(expr, itestr);
                return z3ctx.mkITE((BoolExpr)newArgs[0], newArgs[1], newArgs[2]);
            }
            if (expr.isGE()) {
                String gestr = "(>= " + cache.get(args[0]) + " " + cache.get(args[1]) + ")";
                cache.put(expr, gestr);
                return z3ctx.mkGe((ArithExpr)newArgs[0], (ArithExpr)newArgs[1]);
            }
            if (expr.isLE()) {
                String lestr = "(<= " + cache.get(args[0]) + " " + cache.get(args[1]) + ")";
                cache.put(expr, lestr);
                return z3ctx.mkLe((ArithExpr)newArgs[0], (ArithExpr)newArgs[1]);
            }
            if (expr.isGT()) {
                String gestr = "(> " + cache.get(args[0]) + " " + cache.get(args[1]) + ")";
                cache.put(expr, gestr);
                return z3ctx.mkGt((ArithExpr)newArgs[0], (ArithExpr)newArgs[1]);
            }
            if (expr.isLT()) {
                String lestr = "(< " + cache.get(args[0]) + " " + cache.get(args[1]) + ")";
                cache.put(expr, lestr);
                return z3ctx.mkLt((ArithExpr)newArgs[0], (ArithExpr)newArgs[1]);
            }
            if (expr.isEq()) {
                String eqstr = "(= " + cache.get(args[0]) + " " + cache.get(args[1]) + ")";
                cache.put(expr, eqstr);
                return z3ctx.mkEq(newArgs[0], newArgs[1]);
            }
        }
        // return null as error message
        logger.info("expr reach the end: " + expr.toString());
        return null;
    }
    public int getVal(Expr e)
    {
    	if(e.isGE()|e.isLT()|e.isLE()|e.isGT())
    		return 1;
    	else if(e.isNot())
    	{
    		logger.info("WARNING!NOT APPEAR");
    		return 2;
    	}
    	else if(e.isEq())
    		return 4;
    	else if(e.isAnd()|e.isOr())
    		return 2+e.getArgs().length;
    	else
    	{
    		logger.info("ERROR"+e.toString());
    		return -1;
    	}
    }
    class Mycomp implements Comparator<Expr>{
    	@Override
    	public int compare(Expr e1,Expr e2){
    		return getVal(e2)-getVal(e1);
    	}
    }
    Expr minimizeITE(Expr expr,Solver s){
    	
    	Expr[] args = expr.getArgs();
    	Expr cond = args[0];
    	s.push();
    	s.add((BoolExpr)cond);
    	Status status = s.check();
    	if(status == Status.UNSATISFIABLE){
    		s.pop();
    		
    		if(args[2].isITE()){
    			s.push();
    			s.add((BoolExpr)z3ctx.mkNot((BoolExpr)cond));
    			Expr retexp = minimizeITE(args[2],s);
    			s.pop();
    			return retexp;
    		}
    		else
    			return args[2];
    	}
    	s.pop();

    	s.push();
    	s.add((BoolExpr)z3ctx.mkNot((BoolExpr)cond));
    	status = s.check();
    	if(status == Status.UNSATISFIABLE){
    		s.pop();
    		
    		if(args[1].isITE()){
    			s.push();
    			s.add((BoolExpr)cond);
    			Expr retexp = minimizeITE(args[1],s);
    			s.pop();
    			return retexp;
    		}
    		else
    			return args[1];
    	}
    	s.pop();

    	Expr retexp1,retexp2;
    	if(args[1].isITE()){
    		s.push();
    		s.add((BoolExpr)cond);
    		retexp1 = minimizeITE(args[1],s);
    		s.pop();
    	}
    	else
    		retexp1 = args[1];

    	if(args[2].isITE()){
    		s.push();
    		s.add((BoolExpr)z3ctx.mkNot((BoolExpr)cond));
    		retexp2 = minimizeITE(args[2],s);
    		s.pop();
    	}
    	else
    		retexp2 = args[2];
    	return z3ctx.mkITE((BoolExpr)cond,retexp1,retexp2);
    }
    Expr convertToITE(Expr expr) {
        // eliminate and or not in ite's boolean condition
        if (expr.isConst()) {
            return expr;
        }
        if (expr.isIntNum()) {
            return expr;
        }
        if (expr.isNot()) {
            Expr inner = expr.getArgs()[0];
            Expr[] innerArgs = inner.getArgs();
            if (inner.isGE()) {
            	return z3ctx.mkLt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]);
            }
            if (inner.isLE()) {
            	return z3ctx.mkGt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]);
            }
            if (inner.isGT()) {
            	return z3ctx.mkLe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]);
            }
            if (inner.isLT()) {
            	return z3ctx.mkGe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]);
            }
            // return null as error message
            return null;
        }
        if (expr.isITE()) {
            Expr[] args = expr.getArgs();
            Expr[] convertedArgs = new Expr[args.length];
            for (int i = 0; i < args.length; i++) {
                convertedArgs[i] = convertToITE(args[i]);
            }
            Expr[] innerArgs = convertedArgs[0].getArgs();
            if (convertedArgs[0].isAnd()) {
                iteConverted = true;
                Comparator cmp = new Mycomp();
                Arrays.sort(innerArgs,cmp);
                Expr ite = z3ctx.mkITE((BoolExpr)innerArgs[0], convertedArgs[1], convertedArgs[2]);
                for (int i = 1; i < innerArgs.length; i++) {
                    ite = z3ctx.mkITE((BoolExpr)innerArgs[i], ite, convertedArgs[2]);
                }
                logger.info("HERE1"+ite.toString());
                Solver s = z3ctx.mkSolver();
                Expr finalite = minimizeITE(ite,s);
                logger.info("HERE2"+finalite.toString());
                //return ite;
                return finalite;
            }
            if (convertedArgs[0].isOr()) {
                iteConverted = true;
                // for (int i = 0; i < innerArgs.length; i++) {
                //     logger.info("beforesort " + innerArgs[i].toString());
                // }
                Comparator cmp = new Mycomp();
                Arrays.sort(innerArgs,cmp);
                Expr ite = z3ctx.mkITE((BoolExpr)innerArgs[0], convertedArgs[1], convertedArgs[2]);
                for (int i = 1; i < innerArgs.length; i++) {
                    // logger.info("or in ite: " + i);
                    ite = z3ctx.mkITE((BoolExpr)innerArgs[i], convertedArgs[1], ite);
                }
                logger.info("HERE1"+ite.toString());
                Solver s = z3ctx.mkSolver();
                Expr finalite = minimizeITE(ite,s);
                logger.info("HERE2"+finalite.toString());
                //return ite;
                return finalite;
            }
            return z3ctx.mkITE((BoolExpr)convertedArgs[0], convertedArgs[1], convertedArgs[2]);
        }
        if (expr.isAnd()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(convertToITE(e));
            }
            return z3ctx.mkAnd(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (expr.isOr()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(convertToITE(e));
            }
            return z3ctx.mkOr(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (expr.isMul()) {
            return expr;
        }
        if (expr.isAdd()) {
            return expr;
        }
        if (expr.isSub()) {
            return expr;
        }
        if (expr.isGE()) {
            // return z3ctx.mkGe((ArithExpr)convertToITE(innerArgs[0]), (ArithExpr)convertToITE(innerArgs[1]));
            return expr;
        }
        if (expr.isLE()) {
            // return z3ctx.mkLe((ArithExpr)convertToITE(innerArgs[0]), (ArithExpr)convertToITE(innerArgs[1]));
            return expr;
        }
        if (expr.isGT()) {
            // return z3ctx.mkGt((ArithExpr)convertToITE(innerArgs[0]), (ArithExpr)convertToITE(innerArgs[1]));
            return expr;
        }
        if (expr.isLT()) {
            // return z3ctx.mkLt((ArithExpr)convertToITE(innerArgs[0]), (ArithExpr)convertToITE(innerArgs[1]));
            return expr;
        }
        if (expr.isEq()) {
            // return z3ctx.mkGe((ArithExpr)convertToITE(innerArgs[0]), (ArithExpr)convertToITE(innerArgs[1]));
            Expr[] args = expr.getArgs();
            Expr arg0 = args[0];
            Expr arg1 = args[1];
            return z3ctx.mkAnd(z3ctx.mkGe((ArithExpr)arg0, (ArithExpr)arg1), z3ctx.mkLe((ArithExpr)arg0, (ArithExpr)arg1));
        }
        // return null as error message
        logger.info("expr reach the end: " + expr.toString());
        return null;
    }

    String getResultStr(String name, String defstr) {
        String type = problem.requests.get(name).getRange().toString();
        Expr[] args = problem.requestArgs.get(name);
        String arglist = "";
        if (args.length < 0) {
            return null;
        }
        if (args.length == 0) {
            arglist = "()";
        }
        if (args.length == 1) {
            String arg = args[0].toString();
            String sort = args[0].getSort().toString();
            arglist = "((" + arg + " " + sort + "))";
        }
        if (args.length > 1) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i].toString();
                String sort = args[i].getSort().toString();
                if (i == 0) {
                    arglist = "(" + arg + " " + sort + ")";
                } else {
                    arglist = arglist + " " + "(" + arg + " " + sort + ")";
                }
            }
            arglist = "(" + arglist + ")";
        }
        String output = "(define-fun " + name + " " + arglist + " " + type + " " + defstr + ")";
        return output;
    }

}
