import java.util.*;
import com.microsoft.z3.*;

public class ASTRWTest {
    public static void main(String [] args) {
        ASTGeneral a = new ASTGeneral("+", new ASTGeneral("x"), new ASTGeneral("y"));
        System.out.println(a.toString());
        System.out.println((new ASTGeneral(a)).toString());
        System.out.println(a.hashCode());
        ASTGeneral b = new ASTGeneral("-", new ASTGeneral("x"), new ASTGeneral("y"));
        System.out.println(b.toString());
        ASTGeneral c = new ASTGeneral("*", a, b);
        System.out.println(c.toString());
        ASTGeneral d = c.substitute(new ASTGeneral("x"), new ASTGeneral("x1"));
        System.out.println(c.toString());
        System.out.println(d.toString());
        ASTGeneral e = d.update(new ASTGeneral[] {new ASTGeneral("m1"), new ASTGeneral("m2")});
        System.out.println(e.toString());
        ASTGeneral d1 = c.substitute(new ASTGeneral[]{new ASTGeneral("x"), new ASTGeneral("y")},
                                     new ASTGeneral[]{new ASTGeneral("x1"), new ASTGeneral("y1")});
        System.out.println(d1.toString());

        Context ctx = new Context();
        IntExpr x = ctx.mkIntConst("x");
        IntExpr y = ctx.mkIntConst("y");
        DefinedFunc f = new DefinedFunc(ctx, "f", new Expr[]{x, y}, ctx.mkAdd(x, y), new String[]{"x", "y"}, new ASTGeneral(a));
        System.out.println(f);
        ASTGeneral aT = f.apply(a, b);
        System.out.println(aT);
        ASTGeneral expr = new ASTGeneral("*", new ASTGeneral("f", new ASTGeneral("a"), new ASTGeneral("b")), new ASTGeneral("f", new ASTGeneral("c"), new ASTGeneral("d")));
        System.out.println(expr);
        System.out.println(f.rewrite(expr));
    }
}
