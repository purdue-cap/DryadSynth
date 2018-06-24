import java.util.*;
import com.microsoft.z3.*;

public class GeneralTest{
    public static void main(String [] args) throws Exception {
		HashMap<String, String> cfg = new HashMap<String, String>();
		cfg.put("model", "true");
		Context ctx = new Context(cfg);
        ArithExpr x = ctx.mkIntConst("x");
        BoolExpr b = ctx.mkEq(ctx.mkSub(x, ctx.mkInt(1)), ctx.mkInt(0));
        System.out.println(b.toString());
        Solver s = ctx.mkSolver();
        s.add(b);
        Status sts = s.check();
        assert sts == Status.SATISFIABLE;
        Model m = s.getModel();
        System.out.println(m.toString());
        Expr e = m.evaluate(x, true);
        assert e.isIntNum();
        System.out.println(((IntNum)e).getInt());
    }
}
