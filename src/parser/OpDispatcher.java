import java.util.*;
import com.microsoft.z3.*;

public class OpDispatcher {
    private Context z3ctx;
    private Map<String, FuncDecl> requests;
    private Map<String, DefinedFunc> funcs;

    // For grammar parsing
    // Internal expressions that might be used in grammars
    public static final String[] internalOpsArray = new String[] {
        "+","-","*","/","and","or","not","<",">","=","<=",">=","=>","ite", "div", "mod"
    };
    public static final Set<String> internalOps = new HashSet<String>(Arrays.asList(internalOpsArray));

    public OpDispatcher(Context ctx, Map<String, FuncDecl> requests, Map<String, DefinedFunc> funcs) {
        this.z3ctx = ctx;
        this.requests = requests;
        this.funcs = funcs;
    }
    public Expr dispatch(String name, Expr[] args) {
        return dispatch(name, args, false, false);
    }

    public Expr dispatch(String name, Expr[] args, boolean definedOnly, boolean doNotInterp) {
        if (name.equals("+")) {
            return z3ctx.mkAdd(Arrays.copyOf(args, args.length, ArithExpr[].class));
        }
        if (name.equals("-")) {
            return z3ctx.mkSub(Arrays.copyOf(args, args.length, ArithExpr[].class));
        }
        if (name.equals("*")) {
            return z3ctx.mkMul(Arrays.copyOf(args, args.length, ArithExpr[].class));
        }
        if (name.equals("/")) {
            return z3ctx.mkDiv((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("and")) {
            return z3ctx.mkAnd(Arrays.copyOf(args, args.length, BoolExpr[].class));
        }
        if (name.equals("or")) {
            return z3ctx.mkOr(Arrays.copyOf(args, args.length, BoolExpr[].class));
        }
        if (name.equals("not")) {
            return z3ctx.mkNot((BoolExpr)args[0]);
        }
        if (name.equals(">")) {
            return z3ctx.mkGt((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals(">=")) {
            return z3ctx.mkGe((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("<")) {
            return z3ctx.mkLt((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("<=")) {
            return z3ctx.mkLe((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("=")) {
            return z3ctx.mkEq(args[0], args[1]);
        }
        if (name.equals("=>")) {
            return z3ctx.mkImplies((BoolExpr)args[0], (BoolExpr)args[1]);
        }
        if (name.equals("ite")) {
            return z3ctx.mkITE((BoolExpr)args[0], args[1], args[2]);
        }
        if (name.equals("div")) {
            return z3ctx.mkDiv((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("mod")) {
            return z3ctx.mkMod((IntExpr)args[0], (IntExpr)args[1]);
        }
        DefinedFunc df = funcs.get(name);
        if (df != null) {
            if (doNotInterp) {
                return df.applyUninterp(args);
            } else {
                return df.apply(args);
            }
        }
        if (definedOnly) {
            return null;
        }
        FuncDecl f = requests.get(name);
        if (f != null) {
            return z3ctx.mkApp(f, args);
        }
        return null;
    }
}
