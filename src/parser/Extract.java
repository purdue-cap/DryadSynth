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
        SygusProblem problem = extractor.createProblem();

		System.out.println("Synth type:" + problem.problemType.toString());


		if (problem.problemType == SygusProblem.ProbType.GENERAL) {
			System.out.println("Global symbol types:");
			for (String name: problem.glbSybTypeTbl.keySet()) {
				System.out.println(name + "  " + problem.glbSybTypeTbl.get(name).toString());
			}
		}
		System.out.println("Synth requests:");
		for(String name : problem.requests.keySet()) {
			if (problem.problemType == SygusProblem.ProbType.GENERAL) {
				System.out.println("Grammar Infos:");
				SygusProblem.CFG cfg = problem.cfgs.get(name);
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
			FuncDecl func = problem.requests.get(name);
			System.out.println("Name:" + func.getName());
			System.out.println("Argument types:" + Arrays.toString(func.getDomain()));
			System.out.println("Argument names:" + Arrays.toString(problem.requestArgs.get(name)));
			System.out.println("Used argument names:" + Arrays.toString(problem.requestUsedArgs.get(name)));
			System.out.println("Return type is " + func.getRange().getName());
		}

		System.out.println("Defined variables:");
		for(Expr expr: problem.vars.values()) {
			System.out.println("Name:" + expr.toString() + " Type:" + expr.getSort().toString());
		}

		System.out.println("Defined functions:");
		for(DefinedFunc func: problem.funcs.values()) {
			System.out.println("Name:" + func.getName() + " Definition:" + func.toString());
		}

		System.out.println("Constraints:");
		for (Expr expr: problem.constraints) {
			System.out.println(expr);
		}

		System.out.println("Final Constraints:");
		System.out.println(problem.finalConstraint);

		if (!problem.candidate.isEmpty()) {
			System.out.println("Possible candidates:");
			for (String name : problem.candidate.keySet()) {
				System.out.println(name + " : " + problem.candidate.get(name).getDef().toString());
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
