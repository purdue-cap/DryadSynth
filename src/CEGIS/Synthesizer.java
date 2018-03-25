import java.util.*;
import java.io.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class Synthesizer {

	public Solver s;
	public Optimize optimize;
	private Context ctx;
	private Set<Expr[]> counterExamples;
	private SygusExtractor extractor;
	private Logger logger;

	public int heightBound;
	public int bound;
	public Expand e;

	public Model m = null;

	public Synthesizer(Context ctx, Set<Expr[]> counterExamples, int heightBound, SygusExtractor extractor, Logger logger) {
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.optimize = ctx.mkOptimize();
		this.counterExamples = counterExamples;
		this.extractor = extractor;
		this.logger = logger;
		this.heightBound = heightBound;
		this.bound = (int)Math.pow(2, heightBound) - 1;
		this.e = new Expand(bound, ctx, extractor);
	}

	public Model getLastModel() {
		return m;
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

		if (sts == Status.SATISFIABLE) {
			m = s.getModel();
		}
		
		s.pop();
		return sts;

	}

	public BoolExpr setMax(IntExpr c, IntExpr absolute) {
		BoolExpr setMax;
		IntExpr minusc = (IntExpr)ctx.mkSub(ctx.mkInt(0), c);

		setMax = ctx.mkOr(ctx.mkAnd(ctx.mkGe(c, minusc), ctx.mkEq(c, absolute))
			, ctx.mkAnd(ctx.mkGt(minusc, c), ctx.mkEq(minusc, absolute)));

		return setMax;
	}

	public Status synthesisWithSMT() {
		optimize.Push();

		Expr spec = extractor.finalConstraint;

		BoolExpr q = ctx.mkTrue();

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

		optimize.Add(q);

		int numFunc = extractor.names.size();
		IntExpr[][][] c = e.getCoefficients();
		IntExpr[][][] absolute = new IntExpr[numFunc][bound][0];

		for (int l = 0; l < numFunc; l++) {
			for (int j = 0; j < bound; j++) {
				String name = extractor.names.get(l);
				int argCount = extractor.requestUsedArgs.get(name).length;
				absolute[l][j] = new IntExpr[argCount + 1];
				for (int i = 0; i < argCount + 1; i++) {
					absolute[l][j][i] = ctx.mkIntConst("f" + l + "_absolute_c" + j + "_" + i);
					optimize.Add(setMax(c[l][j][i], absolute[l][j][i]));
					Optimize.Handle minimize = optimize.MkMinimize(absolute[l][j][i]);
				}
			}	
		}

		Status sts = optimize.Check();

		if (sts == Status.SATISFIABLE) {
			m = optimize.getModel();
		}
		
		optimize.Pop();
		return sts;
	}

}
