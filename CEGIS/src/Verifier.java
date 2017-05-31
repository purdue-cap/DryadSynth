import java.util.*;
import com.microsoft.z3.*;

public class Verifier {

	public Solver s;
	private Context ctx;
	private int numVar;
	private int numFunc;
	private IntExpr[] var;
	private BoolExpr finalConstraint;
	private SygusExtractor extractor;

	public Verifier(Context ctx, int numVar, int numFunc, IntExpr[] var, SygusExtractor extractor) {
		this.numVar = numVar;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.var = var;
		this.finalConstraint = extractor.finalConstraint;;
		this.extractor = extractor;
	}

	/*public BoolExpr max2Prop(ArithExpr[] functions) {
		//BoolExpr max2Prop = ctx.mkOr(ctx.mkAnd(ctx.mkGe(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[0]))
		//	, ctx.mkAnd(ctx.mkLt(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[1])));

		BoolExpr max2Prop = ctx.mkAnd(ctx.mkGe(functions[0], var[0]), ctx.mkGe(functions[0], var[1])
			, ctx.mkOr(ctx.mkEq(functions[0], var[0]), ctx.mkEq(functions[0], var[1])));

		return max2Prop;
	}

	public BoolExpr max3Prop(ArithExpr[] functions) {
		//BoolExpr max2Prop = ctx.mkOr(ctx.mkAnd(ctx.mkGe(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[0]))
		//	, ctx.mkAnd(ctx.mkLt(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[1])));

		BoolExpr max3Prop = ctx.mkAnd(ctx.mkGe(functions[0], var[0]), ctx.mkGe(functions[0], var[1]), ctx.mkGe(functions[0], var[2])
			, ctx.mkOr(ctx.mkEq(functions[0], var[0]), ctx.mkEq(functions[0], var[1]), ctx.mkEq(functions[0], var[2])));

		return max3Prop;
	}*/

	public Status verify(ArithExpr[] functions) {

		Expr spec = finalConstraint;

		int i = 0;
		for (FuncDecl f : extractor.requests.values()) {
			DefinedFunc df = new DefinedFunc(ctx, var, functions[i]);
			spec = df.rewrite(spec, f);
			i = i + 1;
		}

		int j = 0;
		for(Expr expr: extractor.vars.values()) {
			spec = 	spec.substitute(expr, var[j]);
			j = j + 1;
		}

		//System.out.println("Specification : ");
		//System.out.println(ctx.mkNot((BoolExpr)spec));

		//BoolExpr spec = max2Prop(functions);
		//BoolExpr spec = max3Prop(functions);

		s.push();
		s.add(ctx.mkNot((BoolExpr)spec));
		//System.out.println("Verifying... Formula: ");
		//System.out.println(s);

		Status status = s.check();
		s.pop();
		return status;
		//return Status.UNSATISFIABLE;

	}

}