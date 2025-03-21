import com.microsoft.z3.*;
import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class CNFTest {
    public static void main(String [] args) {
        Context ctx = new Context();
        ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
        FuncDecl f = ctx.mkFuncDecl("f", new Sort[]{ctx.mkIntSort(), ctx.mkIntSort()}, ctx.mkIntSort());
        BoolExpr x = (BoolExpr)ctx.mkConst("x", ctx.mkBoolSort());
        BoolExpr y = (BoolExpr)ctx.mkConst("y", ctx.mkBoolSort());
        BoolExpr z = (BoolExpr)ctx.mkConst("z", ctx.mkBoolSort());
        BoolExpr a = (BoolExpr)ctx.mkConst("a", ctx.mkBoolSort());
        BoolExpr b = (BoolExpr)ctx.mkConst("b", ctx.mkBoolSort());
        BoolExpr c = (BoolExpr)ctx.mkConst("c", ctx.mkBoolSort());

        IntExpr p = (IntExpr)ctx.mkConst("p", ctx.mkIntSort());
        IntExpr q = (IntExpr)ctx.mkConst("q", ctx.mkIntSort());
        // BoolExpr spec = ctx.mkOr(
        //                     ctx.mkGe(
        //                         (IntExpr)f.apply(a, b),
        //                         c
        //                         ),
        //                     ctx.mkAnd(
        //                         x,
        //                         ctx.mkOr(
        //                             y,
        //                             ctx.mkGe(
        //                                 a,
        //                                 c
        //                                 )
        //                             )
        //                         ),
        //                     ctx.mkEq(
        //                         z,
        //                         x
        //                         )
        //                     );

        Expr spec = ctx.mkOr(
                (BoolExpr)ctx.mkITE(
                   x,
                   y,
                   z
                  ),
                (BoolExpr)ctx.mkITE(
                   a,
                   b,
                   c
                  )
                );

        Expr and = ctx.mkAnd(x, y, z, a);
        System.out.println("and length: " + and.getArgs().length);
        Expr or = ctx.mkOr(ctx.mkAnd(x, y), ctx.mkAnd(a, b), c);
        System.out.println("or length: " + or.getArgs().length);
        System.out.println("x length: " + x.getArgs().length);

        System.out.println("Specification: " + spec);
        System.out.println();

        // Tactic convertCNF = ctx.mkTactic("tseitin-cnf");
        // Goal g = ctx.mkGoal(false, false, false);
        // g.add(spec);
        // BoolExpr result = convertCNF.apply(g).getSubgoals()[0].AsBoolExpr();
        // System.out.println("tseitin-cnf: Converted spec: " + result);

        Transf trans = new Transf(null, ctx);
        Expr eliminate = trans.eliminateImpEq(spec);
        Expr pushneg = trans.pushNegIn(eliminate);
        System.out.println("After eliminating:");
        System.out.println(eliminate);
        System.out.println();
        System.out.println("After pushing:");
        System.out.println(pushneg);
        System.out.println();

        // if (pushneg.isOr()) {
        //     System.out.println("pushneg.isOr()");
        //     System.out.println("pushneg or len: " + pushneg.getArgs().length);
        //     for (Expr e : pushneg.getArgs()) {
        //         System.out.println("pushneg arg: " + e);
        //         Expr transfed = trans.convertToCNF(e);
        //         System.out.println("Converted arg: " + transfed);
        //         System.out.println("Converted arg: " + transfed.getArgs().length);
        //         for (Expr ee : transfed.getArgs()) {
        //             System.out.println("Converted arg's arg: " + ee);
        //         }
        //     }
        // }

        Expr cnf = trans.convertToCNF(spec);
        System.out.println("After converting:");
        System.out.println(cnf);
        System.out.println();
        System.out.println("Simplified:");
        System.out.println(cnf.simplify());
        System.out.println("Simplified args length:");
        System.out.println(cnf.simplify().getArgs().length);

        System.out.println("f DeclKindind: " + f.getDeclKind());    // Z3_OP_UNINTERPRETED
        FuncDecl specfunc = spec.getFuncDecl();
        System.out.println("specfunc DeclKindind: " + specfunc.getDeclKind());      // Z3_OP_OR
        Expr withFunc = ctx.mkOr(ctx.mkGe((ArithExpr)f.apply(p, q), ctx.mkInt(0)), x);
        FuncDecl withfunc = withFunc.getFuncDecl();
        System.out.println("withfunc DeclKindind: " + withfunc.getDeclKind());      // Z3_OP_OR
        FuncDecl cons = x.getFuncDecl();
        System.out.println("cons DeclKindind: " + cons.getDeclKind());      // Z3_OP_UNINTERPRETED
        System.out.println("x isConst: " + x.isConst());      // true
        Expr funcapply = f.apply(p, q);
        FuncDecl app = funcapply.getFuncDecl();
        System.out.println("app DeclKindind: " + app.getDeclKind());      // Z3_OP_UNINTERPRETED
        System.out.println("funcapply isConst: " + funcapply.isConst());      // false
        FuncDecl t = ctx.mkTrue().getFuncDecl();
        System.out.println("true DeclKindind: " + t.getDeclKind());      // Z3_OP_TRUE
        System.out.println("true isConst: " + ctx.mkTrue().isConst());      // true
        FuncDecl one = ctx.mkInt(1).getFuncDecl();
        System.out.println("one DeclKindind: " + one.getDeclKind());      // Z3_OP_ANUM
        System.out.println("one isConst: " + ctx.mkInt(1).isConst());      // false

    }

}