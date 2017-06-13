import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class Run {
	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();

		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);

		Logger logger = Logger.getLogger("main");
		logger.setUseParentHandlers(false);
		Thread mainThread = Thread.currentThread();
		int numCore = Runtime.getRuntime().availableProcessors();

		HashMap<String, String> cfg = new HashMap<String, String>();
		cfg.put("model", "true");
		Context ctx = new Context(cfg);
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

		logger.info("Final Constraints:");
		logger.info(extractor.finalConstraint.toString());

		Producer1D pdc1d = new Producer1D();
		Cegis[] threads = new Cegis[numCore];
		for (int i = 0; i < numCore; i++) {
			Logger threadLogger = Logger.getLogger("main.thread" + i);
			threads[i] = new Cegis(extractor, pdc1d, mainThread, threadLogger);
			threads[i].start();
		}

		DefinedFunc[] results = new DefinedFunc[0];
		boolean flag = true;
		while (flag) {
			mainThread.wait();
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
		logger.info("Algorithm Runtime: " + estimatedTime);

		for (Cegis thread : threads) {
			thread.running = false;
			thread.join();
		}

		estimatedTime = System.currentTimeMillis() - startTime;
		logger.info("Total Runtime: " + estimatedTime);

	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}
