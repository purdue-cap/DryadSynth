import java.util.*;
import com.microsoft.z3.*;

public class InvarTest {
    public static Context ctx;
    public static void main(String[] args) {
        ctx = new Context();

        // Var declarations
        ArithExpr x = (ArithExpr)ctx.mkConst("x", ctx.mkIntSort());
        ArithExpr y = (ArithExpr)ctx.mkConst("y", ctx.mkIntSort());
        ArithExpr z = (ArithExpr)ctx.mkConst("z", ctx.mkIntSort());
        ArithExpr i = (ArithExpr)ctx.mkConst("i", ctx.mkIntSort());
        ArithExpr j = (ArithExpr)ctx.mkConst("j", ctx.mkIntSort());

        Map<String, Expr> vars;
        Transf tr;
        Region r;
        Expr pre;
        Expr post;

        String name = args[0];

        switch(name) {

        case "minor1":
        // Minor test 1
        // Trans: (x' = x + 2 & x < 1 ) | (x ' = x + 1 & x >= 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[]{ -1}, 0);
        tr.addMap(r, new int[] {2});
        r = new Region(vars, ctx);
        r.addCond(new int[] {1} , -1);
        tr.addMap(r, new int[] {1});
        // Pre: -3 <= x <= -2
        pre = ctx.mkAnd(
                ctx.mkGe(x, ctx.mkInt(-3)),
                ctx.mkLe(x, ctx.mkInt(-2))
                );
        // Post: True
        post = ctx.mkTrue();
        test(name, tr, pre, post, vars);
        break;

        case "minor2":
        // Minor test 2
        // Trans: (x ' = x - 1 & y' = y + 2 & x - y < 2) | ( y' = y + 1 & x - y
        // >= 2 )
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("y", y);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1 , 1}, 1);
        tr.addMap(r, new int[] {-1, 2});
        r = new Region(vars, ctx);
        r.addCond(new int[] {1, -1}, -2);
        tr.addMap(r, new int[] {0, 1});
        // Pre: y = -3 & 0 <= x <= 1
        pre = ctx.mkAnd(ctx.mkEq(y, ctx.mkInt(-3)),
                ctx.mkLe(ctx.mkInt(0), x),
                ctx.mkLe(x, ctx.mkInt(1)));
        // Post: True
        post = ctx.mkTrue();
        test(name, tr, pre, post, vars);
        break;

        case "cegar1":
        // Cegar1
        // Trans: (x' = x + 2 & y' = y + 2)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("y", y);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {0 , 0}, 1);
        tr.addMap(r, new int[] {2, 2});
        // Pre: 0 <= x <= 2 & 0 <= y <= 2
        pre = ctx.mkAnd(
                ctx.mkLe(ctx.mkInt(0), x),
                ctx.mkLe(x, ctx.mkInt(2)),
                ctx.mkLe(ctx.mkInt(0), y),
                ctx.mkLe(y, ctx.mkInt(2))
                );
        // Post: !(x = 4 & y = 0)
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkEq(x, ctx.mkInt(4)),
                    ctx.mkEq(y, ctx.mkInt(0))
                    )
                );
        test(name, tr, pre, post, vars);
        break;
        
        case "cegar1-new":
        // Cegar1-new
        // Trans: (x' = x + 10 & y' = y + 10)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("y", y);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {0 , 0}, 1);
        tr.addMap(r, new int[] {10, 10});
        // Pre: 0 <= x <= 10 & 0 <= y <= 10
        pre = ctx.mkAnd(
                ctx.mkLe(ctx.mkInt(0), x),
                ctx.mkLe(x, ctx.mkInt(10)),
                ctx.mkLe(ctx.mkInt(0), y),
                ctx.mkLe(y, ctx.mkInt(10))
                );
        // Post: !(x = 20 & y = 0)
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkEq(x, ctx.mkInt(20)),
                    ctx.mkEq(y, ctx.mkInt(0))
                    )
                );
        test(name, tr, pre, post, vars);
        break;


        case "cggmp":
        // cggmp
        // Trans: (j >= i & i' = i + 2 & j' = j - 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("i", i);
        vars.put("j", j);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1 , 1}, 0);
        tr.addMap(r, new int[] {2, -1});
        // Pre: i = 1 & j = 10
        pre = ctx.mkAnd(
                ctx.mkEq(i, ctx.mkInt(1)),
                ctx.mkEq(j, ctx.mkInt(10))
                );
        // Post: !(j < i & !(j = 6))
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLt(j, i),
                    ctx.mkNot(ctx.mkEq(j, ctx.mkInt(6)))
                    )
                );
        test(name, tr, pre, post, vars);
        break;


        }

    }
    
    public static void test(String name, Transf t, Expr pre, Expr post, Map<String, Expr> vars) {
        System.out.println("Running algorithm on: " + name);
        System.out.println("Transf expr:");
        System.out.println(t.toExpr());
        System.out.println("Pre expr:");
        System.out.println(pre);
        t.kExtend();
        long startTime = System.currentTimeMillis();
        Expr inv = t.run(pre);
        System.out.println("Run " + t.lastRunIterCount + " iterations.");
        System.out.println("Runtime:" + (System.currentTimeMillis() - startTime));
        System.out.println("Result:");
        System.out.println(inv);
        System.out.println("Checking if invariant is valid.");
        BoolExpr e1 = ctx.mkImplies((BoolExpr)pre, (BoolExpr)inv);
        Expr invp = inv;
        for (Expr var : vars.values()) {
            invp = invp.substitute(var,
                    ctx.mkConst(var.toString() + "!", ctx.mkIntSort()));
        }
        BoolExpr e2 = ctx.mkImplies(ctx.mkAnd(
                    (BoolExpr)inv, (BoolExpr)t.toExpr()
                    ), (BoolExpr)invp);
        BoolExpr e3 = ctx.mkImplies((BoolExpr)inv, (BoolExpr)post);
        Solver s = ctx.mkSolver();
        s.add(ctx.mkNot(ctx.mkAnd(e1, e2, e3)));
        Status r = s.check();
        if ( r == Status.UNSATISFIABLE ) {
            System.out.println("Valid.");
        } else {
            System.out.println("Not Valid, status: " + r.toString());
        }
    }

}
