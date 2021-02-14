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

public class Run {
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
		oParser.acceptsAll(Arrays.asList("g", "grammarVersion"), "grammar version")
            .withRequiredArg().ofType(Integer.class).defaultsTo(0);
        oParser.acceptsAll(Arrays.asList("i", "minInfinite"), "Timeout of infinite region search, in minutes")
            .withRequiredArg().ofType(Integer.class).defaultsTo(5);
        oParser.acceptsAll(Arrays.asList("b", "formattingBound"), "Bound of output size to used string formatting instead of parser formatting")
            .withRequiredArg().ofType(Integer.class).defaultsTo(65535);
        oParser.acceptsAll(Arrays.asList("eq", "EqBound"), "Bound of height for equations in branch condition")
			.withRequiredArg().ofType(Integer.class).defaultsTo(0);
		oParser.acceptsAll(Arrays.asList("E", "EUSolver"), "EUSolver entry point command, set to use EUSolver as CEGIS algorithm, set to empty string to disable.")
			.withRequiredArg().ofType(String.class).defaultsTo("");
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
		int grammar_version = (Integer)options.valuesOf("g").get(0);
		boolean maxsmtFlag = options.has("m");
		String EUSolverPath = (String)options.valuesOf("E").get(0);

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
		if(grammar_version == 2){
			try{
				tree = parser.start();
				logger.info("Accepted");
			} catch(Exception ex) {
				logger.info("Not Accepted");
				return;
			}
		}
		else if(grammar_version == 1){
			try{
				ANTLRFileStream input_1 = new ANTLRFileStream(fn);
				SygusV1Lexer lexer_1 = new SygusV1Lexer(input_1);
				CommonTokenStream tokens_1 = new CommonTokenStream(lexer_1);
				SygusV1Parser parser_1 = new SygusV1Parser(tokens_1);
				grammar_version = 1;
				tree = parser_1.start();
				logger.info("Accepted, Please ignore the previous grammar error");
			} catch(Exception ex_1){
				logger.info("Not Accepted");
				return;
			}
		}
		else{
			try{
				tree = parser.start();
				grammar_version = 2;
				logger.info("Accepted");
			} catch(Exception ex) {
				try{
					ANTLRFileStream input_1 = new ANTLRFileStream(fn);
					SygusV1Lexer lexer_1 = new SygusV1Lexer(input_1);
					CommonTokenStream tokens_1 = new CommonTokenStream(lexer_1);
					SygusV1Parser parser_1 = new SygusV1Parser(tokens_1);
					grammar_version = 1;
					tree = parser_1.start();
					logger.info("Accepted, Please ignore the previous grammar error");
				} catch(Exception ex_1){
					logger.info("Not Accepted");
					return;
				}
			}
		}
		
		ParseTreeWalker walker = new ParseTreeWalker();
		SygusExtractor extractorV2 = new SygusExtractor(ctx);;
		SygusDispatcher dispatcher;
		SygusExtractorV1 extractorV1 = new SygusExtractorV1(ctx);;
		if(grammar_version == 2){
			walker.walk(extractorV2, tree);
			logger.info("Final Constraints:" + extractorV2.finalConstraint.toString());
			dispatcher = new SygusDispatcher(ctx, extractorV2);
		}
		else{
			walker.walk(extractorV1, tree);
			logger.info("Final Constraints:" + extractorV1.finalConstraint.toString());
			dispatcher = new SygusDispatcher(ctx, extractorV1);
		}
		

		
		dispatcher.setNumCore(numCore);
		dispatcher.setIterLimit(iterLimit);
		dispatcher.setMinFinite(minFinite);
		dispatcher.setMinInfinite(minInfinite);
		dispatcher.setMaxSMTFlag(maxsmtFlag);
		dispatcher.setEUSolverPath(EUSolverPath);
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
		// System.exit(0);
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
			if (extractorV2.isGeneral||extractorV1.isGeneral) {
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
		logger.info("Runtime: " + estimatedTime);

		System.exit(0);

	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}
