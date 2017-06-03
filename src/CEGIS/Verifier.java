import java.util.*;
import com.microsoft.z3.*;

public class Verifier {

	public Solver s;
	private Context ctx;
	private String returnType;
	private int numVar;
	private int numV;
	private int numFunc;
	private IntExpr[] var;
	private BoolExpr finalConstraint;
	private SygusExtractor extractor;

	public Verifier(Context ctx, String returnType, int numVar, int numV, int numFunc, IntExpr[] var, SygusExtractor extractor) {
		this.numVar = numVar;
		this.numV = numV;
		this.numFunc = numFunc;
		this.ctx = ctx;
		this.returnType = returnType;
		this.s = ctx.mkSolver();
		this.var = var;
		this.finalConstraint = extractor.finalConstraint;;
		this.extractor = extractor;
	}

	public Status verify(Expr[] functions) {

		Expr spec = finalConstraint;

		IntExpr[] vars = new IntExpr[numV];
		if (returnType.equals("INV")) {
			for (int i = 0; i < numV; i++) {
				vars[i] = var[2*i];
			}
		} else {
			System.arraycopy(var, 0, vars, 0, numV);
		}

		int i = 0;
		for (FuncDecl f : extractor.requests.values()) {
			DefinedFunc df = new DefinedFunc(ctx, vars, functions[i]);
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