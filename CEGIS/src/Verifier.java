import java.util.*;
import com.microsoft.z3.*;

public class Verifier {

	public Solver s;
	private Context ctx;
	private int numVar;
	private int numFunc;
	private IntExpr[] var;
	//private IntExpr[] counterExample;

	public Verifier(Context ctx, int numVar, int numFunc, IntExpr[] var) {
		this.numVar = numVar;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.var = var;
	}

	/*public IntExpr[] decode(Model model) {
		IntExpr[] counterExample = new IntExpr[numVar];

		for (int i = 0; i < numVar; i++) {
			counterExample[i] = (IntExpr) model.evaluate(var[i], false);
		}

		return counterExample;
	}*/

	public BoolExpr maxProp(ArithExpr[] functions) {
		//BoolExpr maxProp = ctx.mkOr(ctx.mkAnd(ctx.mkGe(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[0]))
		//	, ctx.mkAnd(ctx.mkLt(var[0], var[1]), ctx.mkEq(ctx.mkApp(eval, var[0], var[1], ctx.mkInt(0), ctx.mkInt(0)), var[1])));

		BoolExpr maxProp = ctx.mkAnd(ctx.mkGe(functions[0], var[0]), ctx.mkGe(functions[0], var[1])
			, ctx.mkOr(ctx.mkEq(functions[0], var[0]), ctx.mkEq(functions[0], var[1])));

		return maxProp;
	}

	/*public int verify(ArithExpr[] functions) {

		int verifierFlag = 256;
		BoolExpr spec = maxProp(functions);

		s.add(ctx.mkNot(spec));

		if (s.check() == Status.UNSATISFIABLE) {
			verifierFlag = 1;
		} else if (s.check() == Status.UNKNOWN) {
			verifierFlag = 2;
		}else if (s.check() == Status.SATISFIABLE) {
			verifierFlag = 0;
            //IntExpr[] counterExample = decode(s.getModel());
		}

		return verifierFlag;

	}*/

	public Status verify(ArithExpr[] functions) {

		BoolExpr spec = maxProp(functions);

		s.push();
		s.add(ctx.mkNot(spec));
		System.out.println("Verifying... Formula: ");
		System.out.println(s);


		Status status = s.check();
		s.pop();
		return status;

	}

}