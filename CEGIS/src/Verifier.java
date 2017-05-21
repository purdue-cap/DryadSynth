import java.util.*;
import com.microsoft.z3.*;

public class Verifier {

	public Solver s;
	private Context ctx;
	private int numVar;
	private int numFunc;
	private IntExpr[] var;

	public Verifier(Context ctx, int numVar, int numFunc, IntExpr[] var) {
		this.numVar = numVar;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.var = var;
	}

	public BoolExpr max2Prop(ArithExpr[] functions) {
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
	}

	public Status verify(ArithExpr[] functions) {

		//BoolExpr spec = max2Prop(functions);
		BoolExpr spec = max3Prop(functions);

		s.push();
		s.add(ctx.mkNot(spec));
		//System.out.println("Verifying... Formula: ");
		//System.out.println(s);

		Status status = s.check();
		s.pop();
		return status;

	}

}