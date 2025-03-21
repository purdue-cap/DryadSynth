import java.util.*;
import com.microsoft.z3.*;

public class UITest {
    public static Context ctx;
    public static void main(String[] args) {
        ctx = new Context();
        ArithExpr x = ctx.mkIntConst("x");
        ArithExpr y = ctx.mkIntConst("y");
        FuncDecl rqst = ctx.mkFuncDecl("max2", new Sort[]{ctx.mkIntSort(), ctx.mkIntSort()}, ctx.mkIntSort());
        ArithExpr appliedRqst = (ArithExpr)rqst.apply(x, y);
        BoolExpr spec = ctx.mkAnd(
            ctx.mkOr(ctx.mkEq(appliedRqst, x), ctx.mkEq(appliedRqst, y)),
            ctx.mkGe(appliedRqst, x),
            ctx.mkGe(appliedRqst, y)
        );
        DefinedFunc cand = new DefinedFunc(ctx, "max2", new Expr[]{x, y}, x);
        Expr candDef = cand.getDef();
        DefinedFunc newCand = new DefinedFunc(ctx, "max2", cand.getArgs(), ctx.mkITE(ctx.mkGe((ArithExpr)candDef, appliedRqst), candDef, appliedRqst));
        System.out.println("Spec: " + spec.toString());
        System.out.println("Cand: " + cand.toString());
        System.out.println("newCand: " + newCand.toString());
        BoolExpr checkSAT = ctx.mkImplies(spec, (BoolExpr)newCand.rewrite(spec, rqst));
        System.out.println("Check SAT against: " + checkSAT.toString());
        Solver s = ctx.mkSolver();
        s.add(ctx.mkNot(checkSAT));
        Status sts = s.check();
        System.out.println("Check Result: " + sts.toString());
    }

}
