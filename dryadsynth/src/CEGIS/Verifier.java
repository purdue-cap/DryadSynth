import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class Verifier {
    protected Context ctx;
    public Solver s;

    public Verifier(Context context) {
        this.ctx = context;
        this.s = ctx.mkSolver();
    }

    public Status verify(Map<String, Expr> functions, SygusProblem problem) {

        Expr spec = problem.finalConstraint;
        for (String name : problem.names) {
            FuncDecl f = problem.rdcdRequests.get(name);
            Expr[] args = problem.requestUsedArgs.get(name);
            Expr def = functions.get(name);
            DefinedFunc df = new DefinedFunc(ctx, args, def);
            spec = df.rewrite(spec, f);
        }
        
        s.push();
        s.add(ctx.mkNot((BoolExpr)spec));

        Status status = s.check();
        s.pop();

        return status;
    }

    public Model getLastModel() {
        return this.s.getModel();
    }

}