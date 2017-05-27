import java.util.*;
import java.io.*;
import com.microsoft.z3.*;

public class Synthesizer {

	public Solver s;
	private Context ctx;
	private int numVar;
	private int numFunc;
	private HashSet<IntExpr[][]> counterExamples;

	public int heightBound;
	public int bound;
	public Expand e;

	public Synthesizer(Context ctx, int numVar, int numFunc, HashSet<IntExpr[][]> counterExamples, int heightBound) {
		this.numVar = numVar;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.counterExamples = counterExamples;

		/*if (counterExamples.size() > 3) {
			this.heightBound = 3;
		} else {
			this.heightBound = counterExamples.size() + 1;
		}*/
		//this.heightBound = counterExamples.size() + 1;
		this.heightBound = heightBound;
		this.bound = (int)Math.pow(2, heightBound) - 1;
		this.e = new Expand(bound, ctx, numVar, numFunc);
	}

	/*public BoolExpr maxProp(FuncDecl eval, IntExpr a, IntExpr b) {
		BoolExpr maxProp = ctx.mkOr(ctx.mkAnd(ctx.mkGe(a, b), ctx.mkEq(ctx.mkApp(eval, a, b, ctx.mkInt(0), ctx.mkInt(0)), a))
			, ctx.mkAnd(ctx.mkLt(a, b), ctx.mkEq(ctx.mkApp(eval, a, b, ctx.mkInt(0), ctx.mkInt(0)), b)));

		return maxProp;
	}*/

	public BoolExpr max2(FuncDecl eval, IntExpr[] cntrExmp) {
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
	}

	public BoolExpr polynomial3(FuncDecl eval, IntExpr[] cntrExmp) {

		ArithExpr fun1 = (ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0));
		ArithExpr fun2 = (ArithExpr)ctx.mkApp(eval, cntrExmp[1], cntrExmp[0], ctx.mkInt(0), ctx.mkInt(1));

		BoolExpr polynomial3Prop = ctx.mkEq(ctx.mkAdd(fun1, fun2), ctx.mkSub(ctx.mkAdd(cntrExmp[0], cntrExmp[1]), cntrExmp[0]));

		return polynomial3Prop;
	}

	public Status synthesis() {

		s.push();

		BoolExpr q = ctx.mkAnd(e.expandValid(), e.expandCoefficient());
		//BoolExpr q = e.expandValid();
		for (IntExpr[][] params : counterExamples) {
			for (int k = 0; k < numFunc; k++) {
				q = ctx.mkAnd(q, e.expandEval(k, params[k]));
			}			
			//q = ctx.mkAnd(q, max2(e.eval, params));
			//q = ctx.mkAnd(q, max3(e.eval, params));
			q = ctx.mkAnd(q, polynomial3(e.eval, params[0]));
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
		//System.out.println("Synthesis done.");
		return sts;

	}

}
