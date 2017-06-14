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

		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);

		Logger logger = Logger.getLogger("main");
		logger.setUseParentHandlers(false);
		FileHandler handler = new FileHandler("log.main.txt", false);
		handler.setFormatter(new SimpleFormatter());
		logger.addHandler(handler);
		Thread mainThread = Thread.currentThread();
		int numCore = Runtime.getRuntime().availableProcessors();

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

		Producer1D pdc1d = new Producer1D();
		Cegis[] threads = new Cegis[numCore];
		for (int i = 0; i < numCore; i++) {
			Logger threadLogger = Logger.getLogger("main.thread" + i);
			threadLogger.setUseParentHandlers(false);
			FileHandler threadHandler = new FileHandler("log.thread." + i + ".txt", false);
			threadHandler.setFormatter(new SimpleFormatter());
			threadLogger.addHandler(threadHandler);
			threads[i] = new Cegis(extractor, pdc1d, mainThread, threadLogger);
			threads[i].start();
		}

		DefinedFunc[] results = new DefinedFunc[0];
		boolean flag = true;
		while (flag) {
			synchronized(mainThread) {
				mainThread.wait();
			}
			for (Cegis thread : threads) {
				if (thread.results != null) {
					results = thread.results;
					flag = false;
					break;
				}
			}
		}

		for (DefinedFunc df: results) {
			System.out.println(df);
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
