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
        oParser.acceptsAll(Arrays.asList("t", "threads"), "Numbers of parallel threads to use")
            .withRequiredArg().ofType(Integer.class).defaultsTo(Runtime.getRuntime().availableProcessors());
        oParser.acceptsAll(Arrays.asList("f", "minFinite"), "Timeout of finite region search, in minutes")
            .withRequiredArg().ofType(Integer.class).defaultsTo(20);
        oParser.acceptsAll(Arrays.asList("i", "minInfinite"), "Timeout of infinite region search, in minutes")
            .withRequiredArg().ofType(Integer.class).defaultsTo(5);
        oParser.acceptsAll(Arrays.asList("b", "formattingBound"), "Bound of output size to used string formatting instead of parser formatting")
            .withRequiredArg().ofType(Integer.class).defaultsTo(65535);
        oParser.acceptsAll(Arrays.asList("C", "CEGISOnly"), "Run synthesiszer in CEGIS mode only, disable all decidable fragments");
        oParser.acceptsAll(Arrays.asList("M", "modeCheckOnly"), "Run mode check to determine fragment of the problem only, skipping all synthesis");
        oParser.acceptsAll(Arrays.asList("v", "verbose"), "Enable verbose output of logs to stdout");
        oParser.nonOptions("SyGuS benchmark file to process");
        OptionSet options = oParser.parse(args);
        if (options.has("h")) {
            oParser.printHelpOn(System.out);
            return;
        }

		int numCore = (Integer)options.valuesOf("t").get(0);
		int minFinite = (Integer)options.valuesOf("f").get(0);
		int minInfinite = (Integer)options.valuesOf("i").get(0);
		int formattingBound = (Integer)options.valuesOf("b").get(0);
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
		ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
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

		SygusDispatcher dispatcher = new SygusDispatcher(ctx, extractor);
		dispatcher.setNumCore(numCore);
		dispatcher.setMinFinite(minFinite);
		dispatcher.setMinInfinite(minInfinite);
		dispatcher.setMaxSMTFlag(maxsmtFlag);
        dispatcher.setEnforceCEGIS(options.has("C"));
		dispatcher.prescreen();
        if (options.has("M")) {
            System.out.println(dispatcher.getMethod().toString());
            return;
        }
		dispatcher.initAlgorithm();
		DefinedFunc[] results = dispatcher.runAlgorithm();


		// ANTLRInputStream is deprecated as of antlr 4.7, use it with antlr 4.5 only
		ANTLRInputStream resultBuffer;
		SygusFormatter formatter = new SygusFormatter();
		for (DefinedFunc df: results) {
			String rawResult = df.toString();
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
