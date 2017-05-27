import java.util.*;
import com.microsoft.z3.*;

public class Verifier {

	public Solver s;
	private Context ctx;
	private int numVar;
	private int numFunc;
	private IntExpr[] v;
	private IntExpr[][] var;

	public Verifier(Context ctx, int numVar, int numFunc, IntExpr[][] var) {
	//public Verifier(Context ctx, int numVar, int numFunc) {
		this.numVar = numVar;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.var = var;

		v = new IntExpr[numVar];
		for (int i = 0; i < numVar; i++) {
			v[i] = ctx.mkIntConst("v" + i);
		}

	}

	public BoolExpr max2Prop(ArithExpr[] functions) {
		//BoolExpr max2Prop = ctx.mkOr(ctx.mkAnd(ctx.mkGe(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[0]))
		//	, ctx.mkAnd(ctx.mkLt(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[1])));

		BoolExpr max2Prop = ctx.mkAnd(ctx.mkGe(functions[0], v[0]), ctx.mkGe(functions[0], v[1])
			, ctx.mkOr(ctx.mkEq(functions[0], v[0]), ctx.mkEq(functions[0], v[1])));

		return max2Prop;
	}

	public BoolExpr max3Prop(ArithExpr[] functions) {
		//BoolExpr max2Prop = ctx.mkOr(ctx.mkAnd(ctx.mkGe(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[0]))
		//	, ctx.mkAnd(ctx.mkLt(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[1])));

		BoolExpr max3Prop = ctx.mkAnd(ctx.mkGe(functions[0], v[0]), ctx.mkGe(functions[0], v[1]), ctx.mkGe(functions[0], v[2])
			, ctx.mkOr(ctx.mkEq(functions[0], v[0]), ctx.mkEq(functions[0], v[1]), ctx.mkEq(functions[0], v[2])));

		return max3Prop;
	}

	public BoolExpr polynomial3Prop(ArithExpr[] functions) {

		BoolExpr polynomial3Prop = ctx.mkEq(ctx.mkAdd(functions[0], functions[1]), ctx.mkSub(ctx.mkAdd(v[0], v[1]), v[0]));

		return polynomial3Prop;
	}

	public Status verify(ArithExpr[] functions) {

		//BoolExpr spec = max2Prop(functions);
		//BoolExpr spec = max3Prop(functions);
		BoolExpr spec = polynomial3Prop(functions);

		s.push();

		//for max2
		//BoolExpr init = ctx.mkAnd(ctx.mkEq(v[0], var[0][0]), ctx.mkEq(v[1], var[0][1]));

		//for max3
		//BoolExpr init = ctx.mkAnd(ctx.mkEq(v[0], var[0][0]), ctx.mkEq(v[1], var[0][1]), ctx.mkEq(v[2], var[0][2]));

		//for polynomial3Prop
		BoolExpr init = ctx.mkAnd(ctx.mkEq(v[0], var[0][0]), ctx.mkEq(v[1], var[0][1])
			, ctx.mkEq(v[1], var[1][0]), ctx.mkEq(v[0], var[1][1])
			);
		
		s.add(init);
		s.add(ctx.mkNot(spec));
		//System.out.println("Verifying... Formula: ");
		//System.out.println(s);

		Status status = s.check();
		s.pop();
		return status;
		//return Status.SATISFIABLE;

	}

}