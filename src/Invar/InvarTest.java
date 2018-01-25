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
        ArithExpr n = (ArithExpr)ctx.mkConst("n", ctx.mkIntSort());
        ArithExpr c = (ArithExpr)ctx.mkConst("c", ctx.mkIntSort());
        ArithExpr sn = (ArithExpr)ctx.mkConst("sn", ctx.mkIntSort());
        ArithExpr size = (ArithExpr)ctx.mkConst("size", ctx.mkIntSort());

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

        case "cggmp-new":
        // cggmp-new
        // Trans: (j >= i & i' = i + 2 & j' = j - 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("i", i);
        vars.put("j", j);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1 , 1}, 0);
        tr.addMap(r, new int[] {2, -1});
        // Pre: i = 1 & j = 20
        pre = ctx.mkAnd(
                ctx.mkEq(i, ctx.mkInt(1)),
                ctx.mkEq(j, ctx.mkInt(20))
                );
        // Post: !(j < i & !(j = 13))
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLt(j, i),
                    ctx.mkNot(ctx.mkEq(j, ctx.mkInt(13)))
                    )
                );
        test(name, tr, pre, post, vars);
        break;

        case "dec":
        // dec 
        // Trans: (x > 0 & x' = x - 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {1}, -1);
        tr.addMap(r, new int[] {-1});
        // Pre: x = 100
        pre = ctx.mkAnd(
                ctx.mkEq(x, ctx.mkInt(100))
                );
        // Post: !(x <= 0 & !x = 0)
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLe(x, ctx.mkInt(0)),
                    ctx.mkNot(ctx.mkEq(x, ctx.mkInt(0)))
                    )
                );
        test(name, tr, pre, post, vars);
        break;

        case "dec-new":
        // dec-new 
        // Trans: (x > 0 & x' = x - 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {1}, -1);
        tr.addMap(r, new int[] {-1});
        // Pre: x = 10000
        pre = ctx.mkAnd(
                ctx.mkEq(x, ctx.mkInt(10000))
                );
        // Post: !(x <= 0 & !x = 0)
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLe(x, ctx.mkInt(0)),
                    ctx.mkNot(ctx.mkEq(x, ctx.mkInt(0)))
                    )
                );
        test(name, tr, pre, post, vars);
        break;
        
        case "dec_simpl":
        // dec_simpl 
        // Trans: (x > 0 & x' = x - 1 & n' = n)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("n", n);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {1, 0}, -1);
        tr.addMap(r, new int[] {-1, 0});
        // Pre: x = n
        pre = ctx.mkAnd(
                ctx.mkEq(x, n)
                );
        // Post: !(x <= 0 & !x = 0 & n >= 0)
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLe(x, ctx.mkInt(0)),
                    ctx.mkNot(ctx.mkEq(x, ctx.mkInt(0))),
                    ctx.mkGe(n, ctx.mkInt(0))
                    )
                );
        test(name, tr, pre, post, vars);
        break;

        case "ex7":
        // ex7 
        // Trans: ( i < y & i' = i + 1 & y' = y & x' = x)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("y", y);
        vars.put("i", i);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {0, 1, -1}, -1);
        tr.addMap(r, new int[] {0, 0, 1});
        // Pre: i = 0 & x >= 0 & y >= 0 & x >= y
        pre = ctx.mkAnd(
                ctx.mkEq(i, ctx.mkInt(0)),
                ctx.mkGe(x, ctx.mkInt(0)),
                ctx.mkGe(y, ctx.mkInt(0)),
                ctx.mkGe(x, y)
                );
        // Post: !(i < y & (i >= x | 0 > i))
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLt(i, y),
                    ctx.mkOr(ctx.mkGe(i, x),
                        ctx.mkLt(i, ctx.mkInt(0)))
                    )
                );
        test(name, tr, pre, post, vars);
        break;

        case "ex23":
        // ex23 
        // Trans: ( c < 36 & z' = z + 1 & y' = y & c' = c + 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("y", y);
        vars.put("z", z);
        vars.put("c", c);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {0, 0, -1}, 35);
        tr.addMap(r, new int[] {0, 1, 1});
        // Pre: c = 0 & y >= 0 & y <= 127 & z = 36 * y
        pre = ctx.mkAnd(
                ctx.mkEq(c, ctx.mkInt(0)),
                ctx.mkGe(y, ctx.mkInt(0)),
                ctx.mkLe(y, ctx.mkInt(127)),
                ctx.mkEq(z, ctx.mkMul(y, ctx.mkInt(36)))
                );
        // Post: !(c < 36 & (z < 0 | z >= 4608))
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLt(c, ctx.mkInt(36)),
                    ctx.mkOr(ctx.mkLt(z, ctx.mkInt(0)),
                        ctx.mkGe(z, ctx.mkInt(4608)))
                    )
                );
        test(name, tr, pre, post, vars);
        break;

        case "hola_add":
        // hola_add
        // Trans: ( x > 0 & n' = n & x' = x - 1 & y' = y + 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("y", y);
        vars.put("n", n);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {1, 0, 0}, -1);
        tr.addMap(r, new int[] {-1, 1, 0});
        // Pre: n >= 0 & x = n & y = 0
        pre = ctx.mkAnd(
                ctx.mkEq(x, n),
                ctx.mkGe(n, ctx.mkInt(0)),
                ctx.mkEq(y, ctx.mkInt(0))
                );
        // Post: (x > 0 | n = x + y)
        post = ctx.mkOr( ctx.mkGt(x, ctx.mkInt(0)), 
                ctx.mkEq(n, ctx.mkAdd(x, y)));
        test(name, tr, pre, post, vars);
        break;

        case "hola_countud":
        // hola_countud
        // Trans: ( x > 0 & n' = n & x' = x - 1 & y' = y + 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("y", y);
        vars.put("n", n);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {1, 0, 0}, -1);
        tr.addMap(r, new int[] {-1, 1, 0});
        // Pre: n >= 0 & x = n & y = 0
        pre = ctx.mkAnd(
                ctx.mkEq(x, n),
                ctx.mkGe(n, ctx.mkInt(0)),
                ctx.mkEq(y, ctx.mkInt(0))
                );
        // Post: (x > 0 | y = n)
        post = ctx.mkOr( ctx.mkGt(x, ctx.mkInt(0)), 
                ctx.mkEq(n, y));
        test(name, tr, pre, post, vars);
        break;

        case "inc":
        // inc 
        // Trans: (x < 100 & x' = x + 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1}, 99);
        tr.addMap(r, new int[] {1});
        // Pre: x = 0
        pre = ctx.mkAnd(
                ctx.mkEq(x, ctx.mkInt(0))
                );
        // Post: (x = 100 | !x >= 100)
        post = ctx.mkOr(
                    ctx.mkEq(x, ctx.mkInt(100)),
                    ctx.mkNot(ctx.mkGe(x, ctx.mkInt(100)))
                    );
        test(name, tr, pre, post, vars);
        break;

        case "inc_simp":
        // inc_simp 
        // Trans: (x < n & x' = x + 1 & n' = n)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("n", n);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1, 1}, -1);
        tr.addMap(r, new int[] {1, 0});
        // Pre: x = 0
        pre = ctx.mkAnd(
                ctx.mkEq(x, ctx.mkInt(0))
                );
        // Post: (x = n | n < 0 | !x >= n)
        post = ctx.mkOr(
                    ctx.mkEq(x, n),
                    ctx.mkLt(n, ctx.mkInt(0)),
                    ctx.mkNot(ctx.mkGe(x, n))
                    );
        test(name, tr, pre, post, vars);
        break;

        case "sum1":
        // sum1
        // Trans: (i <= n & sn' = sn + 1 & n' = n & i' = i + 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("i", i);
        vars.put("n", n);
        vars.put("sn", sn);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1, 1, 0}, 0);
        tr.addMap(r, new int[] {1, 0, 1});
        // Pre: sn = 0 & i = 1
        pre = ctx.mkAnd(
                ctx.mkEq(i, ctx.mkInt(1)),
                ctx.mkEq(sn, ctx.mkInt(0))
                );
        // Post: (i <= n | sn = n | sn = 0)
        post = ctx.mkOr(
                    ctx.mkLe(i, n),
                    ctx.mkEq(sn, n),
                    ctx.mkEq(sn, ctx.mkInt(0))
                    );
        test(name, tr, pre, post, vars);
        break;

        case "sum3":
        // sum3
        // Trans: (x' = x + 1 & sn' = sn + 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("sn", sn);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {0, 0}, 1);
        tr.addMap(r, new int[] {1, 1});
        // Pre: sn = 0 & x = 0
        pre = ctx.mkAnd(
                ctx.mkEq(x, ctx.mkInt(0)),
                ctx.mkEq(sn, ctx.mkInt(0))
                );
        // Post: (sn = x | sn = -1)
        post = ctx.mkOr(
                    ctx.mkEq(sn, x),
                    ctx.mkEq(sn, ctx.mkInt(-1))
                    );
        test(name, tr, pre, post, vars);
        break;

        case "sum4":
        // sum4
        // Trans: (i <= 8 & sn' = sn + 1 & i' = i + 1)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("i", i);
        vars.put("sn", sn);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1, 0}, 8);
        tr.addMap(r, new int[] {1, 1});
        // Pre: sn = 0 & i = 1
        pre = ctx.mkAnd(
                ctx.mkEq(i, ctx.mkInt(1)),
                ctx.mkEq(sn, ctx.mkInt(0))
                );
        // Post: (i <= 8 | sn = 8 | sn = 0)
        post = ctx.mkOr(
                    ctx.mkLe(i, ctx.mkInt(8)),
                    ctx.mkEq(sn, ctx.mkInt(8)),
                    ctx.mkEq(sn, ctx.mkInt(0))
                    );
        test(name, tr, pre, post, vars);
        break;


        case "sum4_simp":
        // sum4_simp
        // Trans: (i <= size & sn' = sn + 1 & i' = i + 1 & size' = size)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("i", i);
        vars.put("sn", sn);
        vars.put("size", size);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1, 0, 1}, 0);
        tr.addMap(r, new int[] {1, 1, 0});
        // Pre: sn = 0 & i = 1
        pre = ctx.mkAnd(
                ctx.mkEq(i, ctx.mkInt(1)),
                ctx.mkEq(sn, ctx.mkInt(0))
                );
        // Post: (i <= size | sn = size | sn = 0)
        post = ctx.mkOr(
                    ctx.mkLe(i, size),
                    ctx.mkEq(sn, size),
                    ctx.mkEq(sn, ctx.mkInt(0))
                    );
        test(name, tr, pre, post, vars);
        break;
        
        case "tacas":
        // tacas
        // Trans: (x > 0 & x' = x - 1 & y' = y - 1 & i' = i & j' = j) | (x < 0 & x' = x - 1 & y' = y - 1 & i' = i & j' = j)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("y", y);
        vars.put("i", i);
        vars.put("j", j);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {1, 0, 0, 0}, -1);
        tr.addMap(r, new int[] {-1, -1, 0, 0});
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1, 0, 0, 0}, -1);
        tr.addMap(r, new int[] {-1, -1, 0, 0});
        // Pre: i = x & j = y
        pre = ctx.mkAnd(
                ctx.mkEq(i, x),
                ctx.mkEq(j, y)
                );
        // Post: (! x = 0 | ! i = j | y = 0)
        post = ctx.mkOr(
                ctx.mkNot(ctx.mkEq(x, ctx.mkInt(0))),
                ctx.mkNot(ctx.mkEq(i, j)),
                ctx.mkEq(y, ctx.mkInt(0))
                    );
        test(name, tr, pre, post, vars);
        break;

        case "w1":
        // w1
        // Trans: (x < n & x' = x + 1 & n' = n)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("n", n);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {-1, 1}, -1);
        tr.addMap(r, new int[] {1, 0});
        // Pre: x = 0 & n >= 0
        pre = ctx.mkAnd(
                ctx.mkEq(x, ctx.mkInt(0)),
                ctx.mkGe(n, ctx.mkInt(0))
                );
        // Post: (! x >= n | x = n)
        post = ctx.mkOr(
                ctx.mkNot(ctx.mkGe(x, n)),
                ctx.mkEq(x, n)
                    );
        test(name, tr, pre, post, vars);
        break;

        case "dec_simpl-new":
        // dec_simpl-new
        // Trans: (x > 1 & x' = x - 1 & n' = n)
        vars = new LinkedHashMap<String, Expr>();
        vars.put("x", x);
        vars.put("n", n);
        tr = new Transf(vars, ctx);
        r = new Region(vars, ctx);
        r.addCond(new int[] {1, 0}, -2);
        tr.addMap(r, new int[] {-1, 0});
        // Pre: x = n
        pre = ctx.mkAnd(
                ctx.mkEq(x, n)
                );
        // Post: !(x <= 1 & !x = 1 & n >= 0)
        post = ctx.mkNot(
                ctx.mkAnd(
                    ctx.mkLe(x, ctx.mkInt(1)),
                    ctx.mkNot(ctx.mkEq(x, ctx.mkInt(1))),
                    ctx.mkGe(n, ctx.mkInt(0))
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
