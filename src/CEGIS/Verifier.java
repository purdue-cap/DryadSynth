import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class Verifier {

	private Context ctx;
	public Solver s;
	private SygusExtractor extractor;
	private Logger logger;

	public Verifier(Context ctx, SygusExtractor extractor, Logger logger) {
		this.ctx = ctx;
		this.s = ctx.mkSolver();
		this.extractor = extractor;
		this.logger = logger;
	}

	public Status verify(Map<String, Expr> functions) {

		Expr spec = extractor.finalConstraint;

		for (String name : extractor.names) {
			FuncDecl f = extractor.rdcdRequests.get(name);
			Expr[] args = extractor.requestUsedArgs.get(name);
			Expr def = functions.get(name);
			DefinedFunc df = new DefinedFunc(ctx, args, def);
			spec = df.rewrite(spec, f);
		}

		// int j = 0;
		// for(Expr expr: extractor.vars.values()) {
		// 	spec = 	spec.substitute(expr, var[j]);
		// 	j = j + 1;
		// }

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
