import java.util.*;
import com.microsoft.z3.*;

public class LSTest {
    public static Context ctx;
    public static Expr alter(Expr v) {
        return ctx.mkIntConst(v.toString() + "!alt");
    }
    public static Expr alter(Expr e, Expr[] v) {
        Expr result = e;
        for (Expr c : v) {
            Expr alt = alter(c);
            result = result.substitute(c, alt);
        }
        return result;
    }
    public static void main(String[] args) {
        ctx = new Context();
        ArithExpr x = ctx.mkIntConst("x");
        ArithExpr xp = ctx.mkIntConst("x'");
        ArithExpr y = ctx.mkIntConst("y");
        ArithExpr yp = ctx.mkIntConst("y'");
        BoolExpr eq1 = ctx.mkEq(ctx.mkAdd(ctx.mkSub(xp, x), ctx.mkSub(yp, y)), ctx.mkInt(2));
        BoolExpr eq2 = ctx.mkEq(ctx.mkSub(ctx.mkSub(xp, x), ctx.mkSub(yp, y)), ctx.mkInt(0));
        ArithExpr cx = ctx.mkIntConst("cx");
        ArithExpr cy = ctx.mkIntConst("cy");
        BoolExpr ast1 = ctx.mkEq(xp, ctx.mkAdd(x, cx));
        BoolExpr ast2 = ctx.mkEq(yp, ctx.mkAdd(y, cy));

        System.out.println("Eq1:" + eq1.toString());
        System.out.println("Eq2:" + eq2.toString());

        Solver s = ctx.mkSolver();
        s.push();
        s.add(eq1);
        s.add(eq2);
        s.add(ast1);
        s.add(ast2);
        s.add((BoolExpr)alter(eq1, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add((BoolExpr)alter(eq2, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add((BoolExpr)alter(ast1, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add((BoolExpr)alter(ast2, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add(ctx.mkNot(ctx.mkEq(cx, alter(cx))));
        s.add(ctx.mkNot(ctx.mkEq(cy, alter(cy))));
        System.out.println(s.check().toString());
        s.pop();

        s.push();
        s.add(eq1);
        s.add(eq2);
        s.add(ast1);
        s.add(ast2);
        System.out.println(s.check().toString());
        Model m = s.getModel();
        System.out.println("cx :" + m.eval(cx, false).toString());
        System.out.println("cy :" + m.eval(cy, false).toString());
        s.pop();

        s.push();
        s.add(eq1);
        s.add(eq2);
        s.add(ctx.mkNot(ctx.mkAnd((BoolExpr)ast1.substitute(cx, m.eval(cx, false)),
                                  (BoolExpr)ast2.substitute(cy, m.eval(cy, false)))));
        System.out.println(s.check().toString());
        s.pop();

        eq1 = ctx.mkEq(yp, ctx.mkAdd(x, y));
        eq2 = ctx.mkEq(xp, ctx.mkSub(x, ctx.mkInt(1)));

        System.out.println("Eq1:" + eq1.toString());
        System.out.println("Eq2:" + eq2.toString());

        s.push();
        s.add(eq1);
        s.add(eq2);
        s.add(ast1);
        s.add(ast2);
        s.add((BoolExpr)alter(eq1, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add((BoolExpr)alter(eq2, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add((BoolExpr)alter(ast1, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add((BoolExpr)alter(ast2, new Expr[]{x, y, xp, yp, cx, cy}));
        s.add(ctx.mkNot(ctx.mkEq(cx, alter(cx))));
        s.add(ctx.mkNot(ctx.mkEq(cy, alter(cy))));
        System.out.println(s.check().toString());
        s.pop();

        s.push();
        s.add(eq1);
        s.add(eq2);
        s.add(ast1);
        s.add(ast2);
        System.out.println(s.check().toString());
        m = s.getModel();
        System.out.println("cx :" + m.eval(cx, false).toString());
        System.out.println("cy :" + m.eval(cy, false).toString());
        s.pop();

        s.push();
        s.add(eq1);
        s.add(eq2);
        s.add(ctx.mkNot(ctx.mkAnd((BoolExpr)ast1.substitute(cx, m.eval(cx, false)),
                                  (BoolExpr)ast2.substitute(cy, m.eval(cy, false)))));
        System.out.println(s.check().toString());
        s.pop();
    }

}
