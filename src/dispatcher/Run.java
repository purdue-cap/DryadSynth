import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.microsoft.z3.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class Run {
	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();
		int numCore = Runtime.getRuntime().availableProcessors();
		int minFinite = 20;
		int minInfinite = 5;
		int formattingBound = 65535;
		boolean maxsmtFlag = false;

		if (args.length > 1) {
			if (args[1].equals("-m")) {
				maxsmtFlag = true;
				if (args.length == 3)  {
					numCore = Integer.parseInt(args[2]);
				}
				if (args.length == 4) {
					minFinite = Integer.parseInt(args[2]);
					minInfinite = Integer.parseInt(args[3]);
				}
				if (args.length >= 5) {
					numCore = Integer.parseInt(args[2]);
					minFinite = Integer.parseInt(args[3]);
					minInfinite = Integer.parseInt(args[4]);
				}
			} else {
				if (args.length == 2)  {
					numCore = Integer.parseInt(args[1]);
				}
				if (args.length == 3) {
					minFinite = Integer.parseInt(args[1]);
					minInfinite = Integer.parseInt(args[2]);
				}
				if (args.length >= 4) {
					numCore = Integer.parseInt(args[1]);
					minFinite = Integer.parseInt(args[2]);
					minInfinite = Integer.parseInt(args[3]);
				}
			}
		}

		// if (args.length == 2)  {
		// 	numCore = Integer.parseInt(args[1]);
		// }
		// if (args.length == 3) {
		// 	minFinite = Integer.parseInt(args[1]);
		// 	minInfinite = Integer.parseInt(args[2]);
		// }
		// if (args.length >= 4) {
		// 	numCore = Integer.parseInt(args[1]);
		// 	minFinite = Integer.parseInt(args[2]);
		// 	minInfinite = Integer.parseInt(args[3]);
		// }

		// ANTLRFileStream is deprecated as of antlr 4.7, use it with antlr 4.5 only
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);

		Logger logger = Logger.getLogger("main");
		logger.setUseParentHandlers(false);
		FileHandler handler = new FileHandler("log.main.txt", false);
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
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
		dispatcher.prescreen();
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
