import com.microsoft.z3.*;
import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class UFTest {
    public static void main(String [] args) {
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();
        Model model = null;
        ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
        FuncDecl f = ctx.mkFuncDecl("f", new Sort[]{ctx.mkIntSort(), ctx.mkIntSort()}, ctx.mkIntSort());
        Expr x = ctx.mkConst("x", ctx.mkIntSort());
        Expr y = ctx.mkConst("y", ctx.mkIntSort());
        Expr spec = ctx.mkAnd(ctx.mkGe((IntExpr)f.apply(x, y), (ArithExpr)x),
                                ctx.mkGe((IntExpr)f.apply(x, y), (ArithExpr)y),
                                ctx.mkEq((IntExpr)f.apply(x, y), x),
                                ctx.mkEq((IntExpr)f.apply(x, y), y));
        System.out.println("Specification: " + spec);
        System.out.println();

        Expr[] arguments = new Expr[]{x, y};
        Sort[] domain = f.getDomain();
        Sort range = f.getRange();
        FuncDecl uf0 = ctx.mkFuncDecl("uf0", domain, range);
        FuncDecl uf1 = ctx.mkFuncDecl("uf1", domain, range);
        FuncDecl sub = ctx.mkFuncDecl("sub", new Sort[]{ctx.mkIntSort(), ctx.mkIntSort()}, ctx.mkBoolSort());
        Expr body = ctx.mkITE((BoolExpr)sub.apply(arguments), uf0.apply(arguments), uf1.apply(arguments));
        DefinedFunc definedfunc = new DefinedFunc(ctx, arguments, body);
        BoolExpr newSpec = (BoolExpr)definedfunc.rewrite(spec, f);
        System.out.println("Transformed spec: " + newSpec);
        System.out.println();

        solver.push();
        solver.add(ctx.mkNot((BoolExpr)newSpec));
        System.out.println("Input to solver with newSpec: " + solver);
        System.out.println();
        solver.pop();

        solver.add(ctx.mkNot((BoolExpr)spec));
        System.out.println("Input to solver with spec: " + solver);
        Status result = solver.check();
        if (result == Status.SATISFIABLE) {
            model = solver.getModel();
            System.out.println("SAT! Model :" + model);
            FuncInterp intrprt_f = model.getFuncInterp(f);
            System.out.println("f Interpretation: " + intrprt_f.toString());
            System.out.println("f Intrprt getArity: " + intrprt_f.getArity());
            System.out.println("f Intrprt getElse: " + intrprt_f.getElse());
            System.out.println("f Intrprt getNumEntries: " + intrprt_f.getNumEntries());
            FuncInterp.Entry[] entries = intrprt_f.getEntries();
            int i = 0;
            for (FuncInterp.Entry entry : entries) {
                Expr[] entryArgs = entry.getArgs();
                Expr value = entry.getValue();
                System.out.println("Entry: " + i);
                System.out.println("Entry args: " + Arrays.toString(entryArgs));
                System.out.println("Entry value: " + value);
                i = i + 1;
            }

            Expr defn_f = interpFunc(ctx, intrprt_f, new Expr[]{x, y});
            System.out.println("Definition of f in CE: " + defn_f);

            DefinedFunc func = new DefinedFunc(ctx, "f", new Expr[]{x, y}, defn_f);
            Expr rewritten = (BoolExpr)func.rewrite(spec, f);
            System.out.println("Rewritten with defn: " + rewritten);
        }

    }

    public static Expr interpFunc(Context ctx, FuncInterp interp, Expr[] args) {
        Expr elseCase = interp.getElse();
        Expr defn = elseCase;
        FuncInterp.Entry[] entries = interp.getEntries();
        for (FuncInterp.Entry entry : entries) {
            Expr[] arguments = entry.getArgs();
            Expr value = entry.getValue();
            if (args.length != arguments.length) {
                System.out.println("Arguments Error: Length of arguments does not match. Existing.");
                System.exit(1);
            }
            Expr condition = ctx.mkTrue();
            for (int i = 0; i < args.length; i++) {
                condition = ctx.mkAnd((BoolExpr)condition, ctx.mkEq(args[i], arguments[i]));
            }
            defn = ctx.mkITE((BoolExpr)condition, value, defn);
        }
        return defn.simplify();
        // return defn;
    }

}