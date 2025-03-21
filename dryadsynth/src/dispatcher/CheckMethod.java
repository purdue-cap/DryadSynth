import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.microsoft.z3.*;
import java.util.logging.Logger;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class CheckMethod {
	public static void main(String[] args) throws Exception {

		// ANTLRFileStream is deprecated as of antlr 4.7, use it with antlr 4.5 only
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);
		Logger logger = Logger.getLogger("main");

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

		logger.info("Final Constraints:");
		logger.info(extractor.finalConstraint.toString());

		SygusDispatcher dispatcher = new SygusDispatcher(ctx, extractor);
		dispatcher.setNumCore(1);
		dispatcher.prescreen();
		logger.info("Chosen method:");
		logger.info(dispatcher.getMethod().toString());
	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}
