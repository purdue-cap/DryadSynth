import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.microsoft.z3.*;
import java.util.logging.Logger;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class GeneralTest {
	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();

		// ANTLRFileStream is deprecated as of antlr 4.7, use it with antlr 4.5 only
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);
		Logger logger = Logger.getLogger("main");

		HashMap<String, String> config = new HashMap<String, String>();
		config.put("model", "true");
		Context ctx = new Context(config);
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

		if (extractor.problemType == SygusExtractor.ProbType.GENERAL) {
			String out = new String("Global symbol types:\n");
			for (String name: extractor.glbSybTypeTbl.keySet()) {
				out += (name + "  " + extractor.glbSybTypeTbl.get(name).toString() + "\n");
			}
            logger.info(out);
		}
		logger.info("Synth requests:");
		for(String name : extractor.requests.keySet()) {
			if (extractor.problemType == SygusExtractor.ProbType.GENERAL) {
				String out = new String("Grammar Infos:\n");
				SygusExtractor.CFG cfg = extractor.cfgs.get(name);
				for (String sybName: cfg.grammarSybSort.keySet()) {
					out += ("Symbol name:" + sybName + " Type:" + cfg.grammarSybSort.get(sybName).toString() + "\n");
					out += (sybName + " := " + "\n");
					for (String[] repr: cfg.grammarRules.get(sybName)) {
						if (repr.length == 1) {
							out += ("| " + repr[0] + "\n");
						} else {
							out += ("| (" + String.join(" ", repr) + ")\n");
						}
					}
				}
				out += ("Symbol types:\n");
				for (String sybName: cfg.sybTypeTbl.keySet()) {
					out += (sybName + "  " + cfg.sybTypeTbl.get(sybName).toString() + "\n");
				}
                logger.info(out);
			}
			FuncDecl func = extractor.requests.get(name);
            String out = new String();
			out += ("Name:" + func.getName() + "\n");
			out += ("Argument types:" + Arrays.toString(func.getDomain()) + "\n");
			out += ("Argument names:" + Arrays.toString(extractor.requestArgs.get(name)) + "\n");
			out += ("Used argument names:" + Arrays.toString(extractor.requestUsedArgs.get(name)) + "\n");
			out += ("Return type is " + func.getRange().getName() + "\n");
            logger.info(out);
		}

        Expand ex = new Expand(ctx, extractor);
        Integer len = new Integer(1);
        for (; len < 4; len ++) {
            ex.setVectorBound(len);
            if (!ex.isInterpretableNow()){
                logger.info("Invalid on length " + len.toString());
                continue;
            }
            logger.info("Valid-" + len.toString() + ": " + ex.validPredicate(0).toString());
            logger.info("Intep-" + len.toString() + ": " + ex.interpretGeneral(0).toString());
        }

		logger.info("Final Constraints:");
		logger.info(extractor.finalConstraint.toString());

		long estimatedTime = System.currentTimeMillis() - startTime;
		logger.info("Runtime: " + estimatedTime);


	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}
