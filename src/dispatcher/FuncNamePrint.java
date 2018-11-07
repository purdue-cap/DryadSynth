import java.util.*;
import com.microsoft.z3.*;

public class FuncNamePrint {
    public static void printName(Expr expr) {
        System.out.println(expr.toString() + " : " + expr.getFuncDecl().getName().toString());
    }
    public static void main(String[] args) {
        Context ctx = new Context();
        ArithExpr x = ctx.mkIntConst("x");
        ArithExpr y = ctx.mkIntConst("y");
        ArithExpr z = ctx.mkIntConst("z");
        BoolExpr p = ctx.mkBoolConst("p");
        BoolExpr q = ctx.mkBoolConst("q");
        BoolExpr l = ctx.mkBoolConst("l");
        printName(ctx.mkAdd(x ,y));
        printName(ctx.mkSub(x, y));
        printName(ctx.mkMul(x, y));
        printName(ctx.mkDiv(x, y));
        printName(ctx.mkAnd(p, q));
        printName(ctx.mkOr(p, q));
        printName(ctx.mkNot(p));
        printName(ctx.mkGt(x, y));
        printName(ctx.mkGe(x, y));
        printName(ctx.mkLt(x, y));
        printName(ctx.mkLe(x, y));
        printName(ctx.mkEq(x, y));
        printName(ctx.mkImplies(p, q));
        printName(ctx.mkITE(p, x, y));
        printName(ctx.mkMod((IntExpr)x, (IntExpr)y));
    }

}
