import java.util.*;
import java.io.*;
import com.microsoft.z3.*;

public class Synthesizer {

	public Solver s;
	private Context ctx;
	private String returnType;
	private int numVar;
	private int numV;
	private int numFunc;
	private HashSet<IntExpr[]> counterExamples;
	private SygusExtractor extractor;

	public int heightBound;
	public int bound;
	public Expand e;

	public Synthesizer(Context ctx, String returnType, int numVar, int numV, int numFunc, HashSet<IntExpr[]> counterExamples, int heightBound, SygusExtractor extractor) {
		this.ctx = ctx;
		this.returnType = returnType;
		this.numVar = numVar;
		this.numV = numV;
		this.numFunc = numFunc;
		this.s = ctx.mkSolver();
		this.counterExamples = counterExamples;
		this.extractor = extractor;
		this.heightBound = heightBound;
		this.bound = (int)Math.pow(2, heightBound) - 1;
		this.e = new Expand(bound, ctx, numV, numFunc);
	}

	public Status synthesis(int condBound) {

		s.push();

		Expr spec = extractor.finalConstraint;

		BoolExpr q = ctx.mkAnd(e.expandCoefficient(condBound, returnType));

		IntExpr[] variables = new IntExpr[numVar];
		for (int i = 0; i < numVar; i++) {
			variables[i] = ctx.mkIntConst("variables" + i);
		}

		IntExpr[] var = new IntExpr[numV];
		if (returnType.equals("INV")) {
			for (int i = 0; i < numV; i++) {
				var[i] = variables[2*i];
			}
		} else {
			System.arraycopy(variables, 0, var, 0, numV);
		}

		int k = 0;
		for (FuncDecl f : extractor.requests.values()) {
			Expr eval = e.generateEval(k, var, 0, returnType);
			DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
			spec = definedfunc.rewrite(spec, f);
			k = k + 1;
		}

		int j = 0;
		for(Expr expr: extractor.vars.values()) {
			spec = 	spec.substitute(expr, variables[j]);
			j = j + 1;
		}

		for (IntExpr[] params : counterExamples) {
			BoolExpr expandSpec = (BoolExpr) spec.substitute(variables, params);
			q = ctx.mkAnd(q, expandSpec);
		}

		s.add(q);

		/*try {
		PrintWriter writer2 = new PrintWriter("s.smt2", "UTF-8");
		writer2.println("(set-logic QF_UFLIA)");
		writer2.println("(set-option :produce-models true)");
		writer2.println(s);
		writer2.println("(check-sat)");
		writer2.println("(get-model)");
		writer2.close();
		} catch (IOException e) {
   			System.out.println("Print out error");
		}*/

		//System.out.println("Synthesizing... Formula: ");
		//System.out.println(s);
		
		Status sts = s.check();
		s.pop();
		return sts;

	}

}