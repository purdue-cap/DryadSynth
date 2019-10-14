import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.microsoft.z3.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class QETest {
    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        OptionParser oParser = new OptionParser();
        oParser.acceptsAll(Arrays.asList("m", "maxSAT"), "Enable maxSAT");
        oParser.acceptsAll(Arrays.asList("h", "?", "help"), "Print help");
        oParser.acceptsAll(Arrays.asList("H", "heightsOnly"), "Only outputs entered heights and result heights for CEGIS algorithms (Would output vector length bound values in CEGIS for General track)");
        oParser.acceptsAll(Arrays.asList("t", "threads"), "Numbers of parallel threads to use")
            .withRequiredArg().ofType(Integer.class).defaultsTo(Runtime.getRuntime().availableProcessors());
        oParser.acceptsAll(Arrays.asList("l", "iterLimit"), "Limit of iterations per thread for CEGIS algorithm (0 means no limit)")
            .withRequiredArg().ofType(Integer.class).defaultsTo(0);
        oParser.acceptsAll(Arrays.asList("f", "minFinite"), "Timeout of finite region search, in minutes")
            .withRequiredArg().ofType(Integer.class).defaultsTo(20);
        oParser.acceptsAll(Arrays.asList("i", "minInfinite"), "Timeout of infinite region search, in minutes")
            .withRequiredArg().ofType(Integer.class).defaultsTo(5);
        oParser.acceptsAll(Arrays.asList("b", "formattingBound"), "Bound of output size to used string formatting instead of parser formatting")
            .withRequiredArg().ofType(Integer.class).defaultsTo(65535);
        oParser.acceptsAll(Arrays.asList("eq", "EqBound"), "Bound of height for equations in branch condition")
            .withRequiredArg().ofType(Integer.class).defaultsTo(0);
        oParser.acceptsAll(Arrays.asList("C", "CEGISOnly"), "Run synthesiszer in CEGIS mode only, disable all decidable fragments");
        oParser.acceptsAll(Arrays.asList("F", "FHCEGIS"), "Enable Fixed Height CEGIS algorithm for INV benchamrks.");
        oParser.acceptsAll(Arrays.asList("I", "ITCEGIS"), "Enable Inductive Template in CEGIS algorithms");
        oParser.acceptsAll(Arrays.asList("M", "modeCheckOnly"), "Run mode check to determine fragment of the problem only, skipping all synthesis");
        oParser.acceptsAll(Arrays.asList("v", "verbose"), "Enable verbose output of logs to stdout");
        oParser.nonOptions("SyGuS benchmark file to process");
        OptionSet options = oParser.parse(args);
        if (options.has("h")) {
            oParser.printHelpOn(System.out);
            return;
        }

        int numCore = (Integer)options.valuesOf("t").get(0);
        int iterLimit = (Integer)options.valuesOf("l").get(0);
        int minFinite = (Integer)options.valuesOf("f").get(0);
        int minInfinite = (Integer)options.valuesOf("i").get(0);
        int formattingBound = (Integer)options.valuesOf("b").get(0);
        int eqBound = (Integer)options.valuesOf("eq").get(0);
        boolean maxsmtFlag = options.has("m");

        if (options.nonOptionArguments().size() < 1) {
            System.out.println("Missing input file!");
            oParser.printHelpOn(System.out);
            return;
        }

        String fn = (String)options.nonOptionArguments().get(0);

        // ANTLRFileStream is deprecated as of antlr 4.7, use it with antlr 4.5 only
        ANTLRFileStream input = new ANTLRFileStream(fn);
        SygusLexer lexer = new SygusLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SygusParser parser = new SygusParser(tokens);

        Logger logger = Logger.getLogger("main");
        if (!options.has("v")) {
            logger.setUseParentHandlers(false);
            FileHandler handler = new FileHandler("log.main.txt", false);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        }
        logger.info(String.format("Using %d threads", numCore));
        logger.info(String.format("Using finite coeffBound timeout %d mins and infinite coeffBound timeout %d mins", minFinite, minInfinite));

        HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        Context ctx = new Context(cfg);
        // ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
        //Context z3ctx = new Context();

        ANTLRErrorStrategy es = new CustomErrorStrategy();
        parser.setErrorHandler(es);

        ParseTree tree;
        try{
            tree = parser.start();
            logger.info("Accepted");
        } catch(Exception ex) {
            logger.info("Not Accepted");
            return;
        }

        ParseTreeWalker walker = new ParseTreeWalker();
        SygusExtractor extractor = new SygusExtractor(ctx);
        walker.walk(extractor, tree);

        logger.info("Final Constraints:" + extractor.finalConstraint.toString());

        BoolExpr finalConstraint = extractor.finalConstraint;

        Map<String, DefinedFunc[]> invConstraints = extractor.invConstraints;
        DefinedFunc[] funcs = new DefinedFunc[3];

        for(String key : invConstraints.keySet()) {
            funcs = invConstraints.get(key);
            for (int i = 0; i < 3; i++) {
                logger.info("invConstraints " + i + " : " + funcs[i].getDef().toString());
            }
        }

        Map<String, Expr[]> requestArgs = extractor.requestArgs;
        Expr[] argList = new Expr[17];;

        for(String key: requestArgs.keySet()){
            argList = requestArgs.get(key);
            for(int i = 0; i < argList.length; i++){
                logger.info("arg " + i + " : " + argList[i].toString());
            }
        }

        Quantifier preWithX = ctx.mkExists(
               new Expr[] {argList[5], argList[13], argList[14], argList[15], argList[16]},
               funcs[0].getDef(),
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );

        Quantifier preWithoutX = ctx.mkExists(
               new Expr[] {argList[0], argList[1], argList[2], argList[3], argList[4]
                        , argList[6], argList[7], argList[8], argList[9], argList[10]
                        , argList[11], argList[12]},
               funcs[0].getDef(),
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );

        // logger.info("preWithX: " + preWithX.toString());
        // logger.info("preWithoutX: " + preWithoutX.toString());

        Tactic qe = ctx.mkTactic("qe");
        Goal g = ctx.mkGoal(false, false, false);
        g.add(preWithX);
        Expr preWithXQFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("preWithXQFree: " + preWithXQFree.toString());

        g.reset();
        g.add(preWithoutX);
        Expr preWithoutXQFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("preWithoutXQFree: " + preWithoutXQFree.toString());

        BoolExpr and = ctx.mkAnd((BoolExpr)preWithXQFree, (BoolExpr)preWithoutXQFree);

        Solver solver = ctx.mkSolver();
        solver.add(ctx.mkNot(ctx.mkImplies((BoolExpr)funcs[0].getDef(), and)));
        Status status = solver.check();

        logger.info("Pre Status: " + status);



        Quantifier postWithX = ctx.mkForall(
               new Expr[] {argList[5], argList[13], argList[14], argList[15], argList[16]},
               funcs[2].getDef(),
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );

        Quantifier postWithoutX = ctx.mkForall(
               new Expr[] {argList[0], argList[1], argList[2], argList[3], argList[4]
                        , argList[6], argList[7], argList[8], argList[9], argList[10]
                        , argList[11], argList[12]},
               funcs[2].getDef(),
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );

        logger.info("postWithX: " + postWithX.toString());
        logger.info("postWithoutX: " + postWithoutX.toString());

        g.reset();
        g.add(postWithX);
        Expr postWithXQFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("postWithXQFree: " + postWithXQFree.simplify().toString());

        g.reset();
        g.add(postWithoutX);
        Expr postWithoutXQFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("postWithoutXQFree: " + postWithoutXQFree.simplify().toString());

        and = ctx.mkAnd((BoolExpr)postWithXQFree, (BoolExpr)postWithoutXQFree);

        solver.reset();
        solver.add(ctx.mkNot(ctx.mkImplies(and, (BoolExpr)funcs[2].getDef())));
        status = solver.check();

        logger.info("Post Status: " + status);



        Expr[] primedArgs = new Expr[argList.length];
        for (int i = 0; i < argList.length; i++) {
            String name = argList[i].toString();
            primedArgs[i] = ctx.mkConst(name + "!", argList[i].getSort());
        }

        Quantifier transWithX = ctx.mkExists(
               new Expr[] {argList[5], argList[13], argList[14], argList[15], argList[16]
                        , primedArgs[5], primedArgs[13], primedArgs[14], primedArgs[15], primedArgs[16]},
               funcs[1].getDef(),
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );

        Quantifier transWithoutX = ctx.mkExists(
               new Expr[] {argList[0], argList[1], argList[2], argList[3], argList[4]
                        , argList[6], argList[7], argList[8], argList[9], argList[10]
                        , argList[11], argList[12]
                        , primedArgs[0], primedArgs[1], primedArgs[2], primedArgs[3], primedArgs[4]
                        , primedArgs[6], primedArgs[7], primedArgs[8], primedArgs[9], primedArgs[10]
                        , primedArgs[11], primedArgs[12]},
               funcs[1].getDef(),
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );

        logger.info("transWithX: " + transWithX.toString());
        logger.info("transWithoutX: " + transWithoutX.toString());

        g.reset();
        g.add(transWithX);
        Expr transWithXQFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("transWithXQFree: " + transWithXQFree.simplify().toString());

        g.reset();
        g.add(transWithoutX);
        Expr transWithoutXQFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        logger.info("transWithoutXQFree: " + transWithoutXQFree.simplify().toString());

        and = ctx.mkAnd((BoolExpr)transWithXQFree, (BoolExpr)transWithoutXQFree);

        solver.reset();
        solver.add(ctx.mkNot(ctx.mkImplies((BoolExpr)funcs[1].getDef(), and)));
        status = solver.check();

        logger.info("Trans Status: " + status);

        /*SygusDispatcher dispatcher = new SygusDispatcher(ctx, extractor);
        dispatcher.setNumCore(numCore);
        dispatcher.setIterLimit(iterLimit);
        dispatcher.setMinFinite(minFinite);
        dispatcher.setMinInfinite(minInfinite);
        dispatcher.setMaxSMTFlag(maxsmtFlag);
        dispatcher.setEqBound(eqBound);
        dispatcher.setEnforceCEGIS(options.has("C"));
        dispatcher.setEnforceFHCEGIS(options.has("F"));
        dispatcher.setEnableITCEGIS(options.has("I"));
        dispatcher.setHeightsOnly(options.has("H"));
        dispatcher.prescreen();
        if (options.has("M")) {
            dispatcher.setMethodOnly(options.has("M"));
            if (dispatcher.problem.problemType != SygusProblem.ProbType.INV) {
                // for INV problems, need to solve them to see which method is used.
                SygusDispatcher.SolveMethod method = dispatcher.getMethod();
                SygusDispatcher.ConvertMethod converted = dispatcher.getConverted();
                if (converted == SygusDispatcher.ConvertMethod.NONE) {
                    System.out.println(method.toString());
                } else {
                    System.out.println("Converted");
                }
                if (method == SygusDispatcher.SolveMethod.CEGIS && options.has("I")) {
                    if (dispatcher.checkTmplt()) {
                        System.out.println("TMPLT:TRUE");
                    } else {
                        System.out.println("TMPLT:FALSE");
                    }
                }
                return;
            }
        }
        dispatcher.initAlgorithm();
        DefinedFunc[] results = dispatcher.runAlgorithm();
        dispatcher.postFormatting(results);
        if (results == null) {
            System.exit(1);
        }
        if (dispatcher.nosolution) {
            System.out.println("No solution.");
            System.exit(0);
        }
        if (dispatcher.resultStr != null) {
            String[] strs = dispatcher.resultStr;
            for (String str : strs) {
                System.out.println(str);
            }
            System.exit(0);
        }


        // ANTLRInputStream is deprecated as of antlr 4.7, use it with antlr 4.5 only
        ANTLRInputStream resultBuffer;
        SygusFormatter formatter = new SygusFormatter();
        for (DefinedFunc df: results) {
            String rawResult;
            if (extractor.isGeneral) {
                rawResult = df.toString();
            } else {
                rawResult = df.toString(true);
            }
            if (rawResult.length() <= 65535) {
                resultBuffer = new ANTLRInputStream(rawResult);
                lexer = new SygusLexer(resultBuffer);
                tokens = new CommonTokenStream(lexer);
                parser = new SygusParser(tokens);
                System.out.println(formatter.visit(parser.start()));
            } else {
                // When output size is too large, run regexp replace instead
                rawResult = rawResult.replaceAll("\\(\\s*-\\s+(\\d+)\\s*\\)", "-$1");
                rawResult = rawResult.replaceAll("\\s+", " ");
                System.out.println(rawResult);
            }
        }

        long estimatedTime = System.currentTimeMillis() - startTime;
        logger.info("Runtime: " + estimatedTime);*/

        System.exit(0);

    }
}

class CustomErrorStrategy extends DefaultErrorStrategy{
    @Override
    public void reportError(Parser recognizer, RecognitionException e){
        throw e;
    }
}
