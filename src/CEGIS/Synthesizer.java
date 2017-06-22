import java.util.*;
import java.io.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class Synthesizer {

	public Solver s;
	private Context ctx;
	private Set<Expr[]> counterExamples;
	private SygusExtractor extractor;
	private Logger logger;

	public int heightBound;
	public int bound;
	public Expand e;

	public Synthesizer(Context ctx, Set<Expr[]> counterExamples, int heightBound, SygusExtractor extractor, Logger logger) {
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.counterExamples = counterExamples;
		this.extractor = extractor;
		this.logger = logger;
		this.heightBound = heightBound;
		this.bound = (int)Math.pow(2, heightBound) - 1;
		this.e = new Expand(bound, ctx, extractor);
	}

	public Status synthesis(int condBound) {

		s.push();

		Expr spec = extractor.finalConstraint;

		BoolExpr q = e.expandCoefficient(condBound);

		int k = 0;
		for (String name : extractor.names) {
			FuncDecl f = extractor.rdcdRequests.get(name);
			Expr[] var = extractor.requestUsedArgs.get(name);
			Expr eval = e.generateEval(k, 0);
			DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
			spec = definedfunc.rewrite(spec, f);
			k = k + 1;
		}

		for (Expr[] params : counterExamples) {
			BoolExpr expandSpec = (BoolExpr) spec.substitute(extractor.vars.values().toArray(new Expr[extractor.vars.size()]), params);
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
