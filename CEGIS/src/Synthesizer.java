import java.util.*;
import java.io.*;
import com.microsoft.z3.*;

public class Synthesizer {

	public Solver s;
	private Context ctx;
	private int numVar;
	private int numFunc;
	private HashSet<IntExpr[]> counterExamples;

	public int heightBound;
	public int bound;
	public Expand e;

	public Synthesizer(Context ctx, int numVar, int numFunc, HashSet<IntExpr[]> counterExamples) {
		this.numVar = numVar;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.counterExamples = counterExamples;

		if (counterExamples.size() > 3) {
			this.heightBound = 3;
		} else {
			this.heightBound = counterExamples.size() + 1;
		}
		//this.heightBound = counterExamples.size() + 1;
		this.bound = (int)Math.pow(2, heightBound) - 1;
		this.e = new Expand(bound, ctx, numVar, numFunc);
	}

	public void checkAndGetModel() {
		System.out.println(s);
		if (s.check() == Status.UNSATISFIABLE) {
			System.out.println("Unsatisfiable!");
		} else if (s.check() == Status.UNKNOWN) {
			System.out.println("Unknown!");
		}else if (s.check() == Status.SATISFIABLE) {
			System.out.println("Satisfiable! Here is the model:");
            System.out.println(s.getModel());
		}
	}

	/*public BoolExpr maxProp(FuncDecl eval, IntExpr a, IntExpr b) {
		BoolExpr maxProp = ctx.mkOr(ctx.mkAnd(ctx.mkGe(a, b), ctx.mkEq(ctx.mkApp(eval, a, b, ctx.mkInt(0), ctx.mkInt(0)), a))
			, ctx.mkAnd(ctx.mkLt(a, b), ctx.mkEq(ctx.mkApp(eval, a, b, ctx.mkInt(0), ctx.mkInt(0)), b)));

		return maxProp;
	}*/

	public BoolExpr max(FuncDecl eval, IntExpr[] cntrExmp) {
		//BoolExpr maxProp = ctx.mkOr(ctx.mkAnd(ctx.mkGe(cntrExmp[0], cntrExmp[1]), ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0]))
		//	, ctx.mkAnd(ctx.mkLt(cntrExmp[0], cntrExmp[1]), ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])));

		BoolExpr maxProp = ctx.mkAnd(ctx.mkGe((ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0])
			, ctx.mkGe((ArithExpr)ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])
			, ctx.mkOr(ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[0])
				, ctx.mkEq(ctx.mkApp(eval, cntrExmp[0], cntrExmp[1], ctx.mkInt(0), ctx.mkInt(0)), cntrExmp[1])));

		return maxProp;
	}

	//public Status synthesis(HashSet<IntExpr[]> counterExamples) {
	public Status synthesis() {

		//int heightBound = counterExamples.size() + 1;
		//int bound = (int)Math.pow(2, heightBound) - 1;

		//Expand e = new Expand(bound, ctx, numVar, numFunc);

		//BoolExpr maxProp1 = maxProp(e.eval, ctx.mkInt(1), ctx.mkInt(2));
		//BoolExpr maxProp2 = maxProp(e.eval, ctx.mkInt(2), ctx.mkInt(1));
	
		/*s.push();
		BoolExpr p = e.expandValid();
		for (IntExpr[] params : counterExamples) {
			p = ctx.mkAnd(p, e.expandEval(params));
			p = ctx.mkAnd(p, max(e.eval, params));
		}
		//p = ctx.mkAnd(p, maxProp1, maxProp2);

		s.add(p);

		try {
		PrintWriter writer1 = new PrintWriter("s3.txt", "UTF-8");
		writer1.println(s);
		writer1.close();
		} catch (IOException e) {
   			System.out.println("Print out error");
		}

		s.pop();*/

		s.push();

		BoolExpr q = ctx.mkAnd(e.expandValid(), e.expandCoefficient());
		//BoolExpr q = e.expandValid();
		for (IntExpr[] params : counterExamples) {
			q = ctx.mkAnd(q, e.expandEval(params));
			q = ctx.mkAnd(q, max(e.eval, params));
		}
		//p = ctx.mkAnd(p, maxProp1, maxProp2);

		s.add(q);

		/*try {
		PrintWriter writer2 = new PrintWriter("s4.txt", "UTF-8");
		writer2.println(s);
		writer2.close();
		} catch (IOException e) {
   			System.out.println("Print out error");
		}*/

		System.out.println("Synthesizing... Formula: ");
		System.out.println(s);
		
		/*if (s.check() == Status.UNSATISFIABLE) {
			System.out.println("Unsatisfiable!");
		} else if (s.check() == Status.UNKNOWN) {
			System.out.println("Unknown!");
		}else if (s.check() == Status.SATISFIABLE) {
			System.out.println("Satisfiable! Here is the model:");
			System.out.println(s.getModel());

			BoolExpr[][] valid = e.getValid();
			IntExpr[][][] coefficient = e.getCoefficients();

			IntExpr variable1 = ctx.mkIntConst("var1");
			IntExpr variable2 = ctx.mkIntConst("var2");
			IntExpr[] vars = { variable1, variable2 }; 
            SynthDecoder decoder = new SynthDecoder(ctx, s.getModel(), valid, coefficient, bound, numVar, numFunc);
            //decoder.generateFunction(vars);

            ArithExpr[] function = decoder.generateFunction(vars);
			for (int i = 0; i < numFunc; i++) {
				System.out.println(function[i]);
			}
		}*/

		Status sts = s.check();
		s.pop();
		System.out.println("Synthesis done.");
		return sts;

	}

}
