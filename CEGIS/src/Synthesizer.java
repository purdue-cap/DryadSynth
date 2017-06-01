import java.util.*;
import java.io.*;
import com.microsoft.z3.*;

public class Synthesizer {

	public Solver s;
	private Context ctx;
	private int numVar;
	private int numV;
	private int numFunc;
	private HashSet<IntExpr[]> counterExamples;
	private SygusExtractor extractor;

	public int heightBound;
	public int bound;
	public Expand e;

	public Synthesizer(Context ctx, int numVar, int numV, int numFunc, HashSet<IntExpr[]> counterExamples, int heightBound, SygusExtractor extractor) {
		this.numVar = numVar;
		this.numV = numV;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.counterExamples = counterExamples;
		this.extractor = extractor;
		this.heightBound = heightBound;
		this.bound = (int)Math.pow(2, heightBound) - 1;
		this.e = new Expand(bound, ctx, numV, numFunc);
	}

	/*public BoolExpr max2(FuncDecl eval, IntExpr[] cntrExmp) {
		//BoolExpr maxProp = ctx.mkOr(ctx.mkAnd(ctx.mkGe(cntrExmp[0], cntrExmp[1]), ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0]))
		//	, ctx.mkAnd(ctx.mkLt(cntrExmp[0], cntrExmp[1]), ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])));

		BoolExpr max2Prop = ctx.mkAnd(ctx.mkGe((ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0])
			, ctx.mkGe((ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])
			, ctx.mkOr(ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0])
				, ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])));

		return max2Prop;
	}

	public BoolExpr max3(FuncDecl eval, IntExpr[] cntrExmp) {

		BoolExpr max3Prop = ctx.mkAnd(ctx.mkGe((ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], cntrExmp[2], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0])
			, ctx.mkGe((ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], cntrExmp[2], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])
			, ctx.mkGe((ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], cntrExmp[2], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[2])
			, ctx.mkOr(ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], cntrExmp[2], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0])
				, ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], cntrExmp[2], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])
				, ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], cntrExmp[2], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[2])));

		return max3Prop;
	}*/

	public Status synthesis() {

		s.push();

		Expr spec = extractor.finalConstraint;

		BoolExpr q = ctx.mkAnd(e.expandCoefficient());

		IntExpr[] variables = new IntExpr[numVar];
		for (int i = 0; i < numVar; i++) {
			variables[i] = ctx.mkIntConst("variables" + i);
		}

		IntExpr[] var = new IntExpr[numV];
		System.arraycopy(variables, 0, var, 0, numV);

		int k = 0;
		for (FuncDecl f : extractor.requests.values()) {
			Expr eval = e.generateEval(k, var, 0);
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