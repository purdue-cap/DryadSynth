import java.util.*;
import com.microsoft.z3.*;

public class SimpTest {
    public static void main(String[] args) {
        Context ctx = new Context();
        ArithExpr x = (ArithExpr)ctx.mkConst("x", ctx.mkIntSort());
        ArithExpr y = (ArithExpr)ctx.mkConst("y", ctx.mkIntSort());
        ArithExpr z = (ArithExpr)ctx.mkConst("z", ctx.mkIntSort());
        ArithExpr xp = (ArithExpr)ctx.mkConst("x!", ctx.mkIntSort());
        ArithExpr yp = (ArithExpr)ctx.mkConst("y!", ctx.mkIntSort());
        ArithExpr zp = (ArithExpr)ctx.mkConst("z!", ctx.mkIntSort());
        // QE Test here
        ArithExpr k = (ArithExpr)ctx.mkConst("k", ctx.mkIntSort());

        Expr e = ctx.mkAnd(
                ctx.mkLe(x, ctx.mkInt(2)),
                ctx.mkLe(ctx.mkSub(x, k), ctx.mkInt(1)),
                ctx.mkGe(ctx.mkSub(x, k), ctx.mkInt(-1)),
                ctx.mkGe(k, ctx.mkInt(0))
                );
        
        Quantifier q = ctx.mkExists(
               new Expr[] {k},
               e,
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );
        System.out.println(q);

        Tactic qe = ctx.mkTactic("qe");
        Tactic sim = ctx.mkTactic("ctx-solver-simplify");
        Goal g = ctx.mkGoal(false, false, false);
        g.add(q);
        Expr qFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        System.out.println(qFree);


        e = ctx.mkAnd(
                ctx.mkGe(ctx.mkSub(x, ctx.mkMul(k, ctx.mkInt(2))), ctx.mkInt(-2)),
                ctx.mkEq(ctx.mkSub(x, ctx.mkMul(k, ctx.mkInt(2))), ctx.mkInt(0)),
                ctx.mkGe(k, ctx.mkInt(0))
                );

        q = ctx.mkExists(
               new Expr[] {k},
               e,
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );
        System.out.println(q);

        g = ctx.mkGoal(false, false, false);
        g.add(q);
        qFree = qe.apply(g).getSubgoals()[0].AsBoolExpr();
        System.out.println(qFree);

        e = ctx.mkAnd(
                ctx.mkGe(ctx.mkSub(x, ctx.mkMul(k, ctx.mkInt(3))), ctx.mkInt(-2)),
                ctx.mkGe(ctx.mkSub(x, ctx.mkMul(k, ctx.mkInt(3))), ctx.mkInt(0)),
                ctx.mkLe(ctx.mkSub(x, ctx.mkMul(k, ctx.mkInt(3))), ctx.mkInt(1)),
                ctx.mkGe(ctx.mkSub(y, ctx.mkMul(k, ctx.mkInt(1))), ctx.mkInt(1)),
                ctx.mkGe(k, ctx.mkInt(0))
                );

        q = ctx.mkExists(
               new Expr[] {k},
               e,
               0,
               new Pattern[] {},
               new Expr[] {},
               ctx.mkSymbol(""),
               ctx.mkSymbol("")
                );
        BoolExpr eq = ctx.mkOr(q, ctx.mkEq(x, ctx.mkInt(-10)));
        System.out.println(eq);

        g = ctx.mkGoal(false, false, false);
        g.add(q);
        g = qe.apply(g).getSubgoals()[0];
        g = sim.apply(g).getSubgoals()[0];
        qFree = g.AsBoolExpr();
        System.out.println(qFree);

        e = ctx.mkGe(ctx.mkAdd(x, ctx.mkAdd(y,z)), ctx.mkInt(0));
        g = ctx.mkGoal(false, false, false);
        g.add((BoolExpr)e);
        g = sim.apply(g).getSubgoals()[0];
        System.out.println(g.AsBoolExpr());
        System.out.println(ctx.SimplifyHelp());
    }

}
