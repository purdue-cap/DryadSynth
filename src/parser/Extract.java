import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import com.microsoft.z3.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class Extract {
	public static void main(String[] args) throws Exception {
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);
		Context z3ctx = new Context();
		z3ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);

		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);

		ParseTree tree;
		try{
			tree = parser.start();
			System.out.println("Accepted");
		} catch(Exception ex) {
			System.out.println("Not Accepted");
			return;
		}

		ParseTreeWalker walker = new ParseTreeWalker();
		SygusExtractor extractor = new SygusExtractor(z3ctx);
		walker.walk(extractor, tree);

		System.out.println("Synth type:" + extractor.problemType.toString());


		if (extractor.problemType == SygusExtractor.ProbType.GENERAL) {
			System.out.println("Global symbol types:");
			for (String name: extractor.glbSybTypeTbl.keySet()) {
				System.out.println(name + "  " + extractor.glbSybTypeTbl.get(name).toString());
			}
		}
		System.out.println("Synth requests:");
		for(String name : extractor.requests.keySet()) {
			if (extractor.problemType == SygusExtractor.ProbType.GENERAL) {
				System.out.println("Grammar Infos:");
				SygusExtractor.CFG cfg = extractor.cfgs.get(name);
				for (String sybName: cfg.grammarSybSort.keySet()) {
					System.out.println("Symbol name:" + sybName + " Type:" + cfg.grammarSybSort.get(sybName).toString());
					System.out.println(sybName + " := ");
					for (String[] repr: cfg.grammarRules.get(sybName)) {
						if (repr.length == 1) {
							System.out.println("| " + repr[0]);
						} else {
							System.out.println("| (" + String.join(" ", repr) + ")");
						}
					}
				}
				System.out.println("Symbol types:");
				for (String sybName: cfg.sybTypeTbl.keySet()) {
					System.out.println(sybName + "  " + cfg.sybTypeTbl.get(sybName).toString());
				}
			}
			FuncDecl func = extractor.requests.get(name);
			System.out.println("Name:" + func.getName());
			System.out.println("Argument types:" + Arrays.toString(func.getDomain()));
			System.out.println("Argument names:" + Arrays.toString(extractor.requestArgs.get(name)));
			System.out.println("Used argument names:" + Arrays.toString(extractor.requestUsedArgs.get(name)));
			System.out.println("Return type is " + func.getRange().getName());
		}

		System.out.println("Defined variables:");
		for(Expr expr: extractor.vars.values()) {
			System.out.println("Name:" + expr.toString() + " Type:" + expr.getSort().toString());
		}

		System.out.println("Defined functions:");
		for(DefinedFunc func: extractor.funcs.values()) {
			System.out.println("Name:" + func.getName() + " Definition:" + func.toString());
		}

		System.out.println("Constraints:");
		for (Expr expr: extractor.constraints) {
			System.out.println(expr);
		}

		System.out.println("Final Constraints:");
		System.out.println(extractor.finalConstraint);

		if (!extractor.candidate.isEmpty()) {
			System.out.println("Possible candidates:");
			for (String name : extractor.candidate.keySet()) {
				System.out.println(name + " : " + extractor.candidate.get(name).getDef().toString());
			}
		}
	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}
