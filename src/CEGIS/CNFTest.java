import com.microsoft.z3.*;
import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class CNFTest {
    public static void main(String [] args) {
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();
        Model model = null;
        ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
        FuncDecl f = ctx.mkFuncDecl("f", new Sort[]{ctx.mkIntSort(), ctx.mkIntSort()}, ctx.mkIntSort());
        Expr x = ctx.mkConst("x", ctx.mkIntSort());
        Expr y = ctx.mkConst("y", ctx.mkIntSort());
        Expr z = ctx.mkConst("z", ctx.mkIntSort());
        Expr a = ctx.mkConst("a", ctx.mkIntSort());
        Expr b = ctx.mkConst("b", ctx.mkIntSort());
        Expr c = ctx.mkConst("c", ctx.mkIntSort());
        BoolExpr spec = ctx.mkOr(ctx.mkGe((IntExpr)f.apply(x, y), (ArithExpr)x),
                            ctx.mkAnd(ctx.mkEq(a, b)
                                , ctx.mkOr(ctx.mkGe((ArithExpr)b, (ArithExpr)c)
                                    , ctx.mkGe((ArithExpr)a, (ArithExpr)c))),
                            ctx.mkEq(z, x));
        System.out.println("Specification: " + spec);
        System.out.println();

        Tactic convertCNF = ctx.mkTactic("tseitin-cnf");
        Goal g = ctx.mkGoal(false, false, false);
        g.add(spec);
        BoolExpr result = convertCNF.apply(g).getSubgoals()[0].AsBoolExpr();
        System.out.println("tseitin-cnf: Converted spec: " + result);

    }

}