import java.util.*;
import java.math.BigInteger;
import com.microsoft.z3.*;

public class OpDispatcher {
    private Context z3ctx;
    public Map<String, FuncDecl> requests;
    public Map<String, DefinedFunc> funcs;

    // For grammar parsing
    // Internal expressions that might be used in grammars
    public static final String[] internalOpsArray = new String[] {
        "+","-","*","/","and","or","not","<",">","=","<=",">=","=>","ite", "if", "div", "mod"
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
        if (name.equals("if")) {
            return z3ctx.mkITE((BoolExpr)args[0], args[1], args[2]);
        }
        if (name.equals("div")) {
            return z3ctx.mkDiv((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("mod")) {
            return z3ctx.mkMod((IntExpr)args[0], (IntExpr)args[1]);
        }
        // bitvec operators
        if (name.equals("bvadd")) {
            return z3ctx.mkBVAdd((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvsub")) {
            return z3ctx.mkBVSub((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvneg")) {
            return z3ctx.mkBVNeg((BitVecExpr)args[0]);
        }
        if (name.equals("bvmul")) {
            return z3ctx.mkBVMul((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvurem")) {
            return z3ctx.mkBVURem((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvudiv")) {
            return z3ctx.mkBVUDiv((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvsdiv")) {
            return z3ctx.mkBVSDiv((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvsrem")) {
            return z3ctx.mkBVSRem((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvsmod")) {
            return z3ctx.mkBVSMod((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvshl")) {
            return z3ctx.mkBVSHL((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvlshr")) {
            return z3ctx.mkBVLSHR((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvashr")) {
            return z3ctx.mkBVASHR((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvor")) {
            return z3ctx.mkBVOR((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvand")) {
            return z3ctx.mkBVAND((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvnot")) {
            return z3ctx.mkBVNot((BitVecExpr)args[0]);
        }
        if (name.equals("bvnand")) {
            return z3ctx.mkBVNAND((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvxor")) {
            return z3ctx.mkBVXOR((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvnor")) {
            return z3ctx.mkBVNOR((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvxnor")) {
            return z3ctx.mkBVXNOR((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvuge")) {
            return z3ctx.mkBVUGE((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvugt")) {
            return z3ctx.mkBVUGT((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvule")) {
            return z3ctx.mkBVULE((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvult")) {
            return z3ctx.mkBVULT((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvsge")) {
            return z3ctx.mkBVSGE((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvsgt")) {
            return z3ctx.mkBVSGT((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvsle")) {
            return z3ctx.mkBVSLE((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("bvslt")) {
            return z3ctx.mkBVSLT((BitVecExpr)args[0],(BitVecExpr)args[1]);
        }
        if (name.equals("xor")) {
            return z3ctx.mkXor((BoolExpr)args[0],(BoolExpr)args[1]);
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

    public Long pbeDispatch(String name, long[] args, boolean doNotInterp, int size) {
        if (name.equals("bvadd")) {
            return BitVector.bvadd(args[0],args[1]);
        }
        if (name.equals("bvsub")) {
            return BitVector.bvsub(args[0],args[1]);
        }
        if (name.equals("bvshl")) {
            return BitVector.bvshl(args[0],args[1]);
        }
        if (name.equals("bvlshr")) {
            return BitVector.bvlshr(args[0],args[1]);
        }
        if (name.equals("bvor")) {
            return BitVector.bvor(args[0],args[1]);
        }
        if (name.equals("bvand")) {
            return BitVector.bvand(args[0],args[1]);
        }
        if (name.equals("xor")) {
            return BitVector.bvxor(args[0],args[1]);
        }
        if (name.equals("bvnot")) {
            return BitVector.bvnot(args[0]);
        }
        if (name.equals("bvnand")) {
            return BitVector.bvnand(args[0],args[1]);
        }
        if (name.equals("bvxor")) {
            return BitVector.bvxor(args[0],args[1]);
        }
        if (name.equals("bvnor")) {
            return BitVector.bvnor(args[0],args[1]);
        }
        if (name.equals("bvxnor")) {
            return BitVector.bvxnor(args[0],args[1]);
        }
        if (name.equals("=")) {
            return BitVector.bveq(args[0], args[1]);
        }

        DefinedFunc df = funcs.get(name);
        if (df != null) {
            if (!doNotInterp) {
                return applyThenCompute(df.getDef(), df.getArgs(), args, size);
            }
        }
        return null;
    }

    public Expr bitVectorToExpr(long bv, int size){
        return z3ctx.mkBV(bv, size);
    }

    public static long exprToBitVector(Expr input){
        String exprStr = input.toString();
        if (exprStr.equals("true")) {
            return 1;
        } else if (exprStr.equals("false")) {
            return 0;
        }
        long value = Long.parseUnsignedLong(exprStr, 10);
        return value;
    }

    public Long applyThenCompute(Expr expr, Expr[] args, long[] params, int size) {
        if (args.length != params.length) {
            return null;
        }
        if (expr.isNumeral()) {
            return exprToBitVector(expr);
        }
        if (expr.isConst()) {
            for (int i = 0; i < args.length; i++) {
                if (expr.toString().equals(args[i].toString())) {
                    return params[i];
                }
            }
            // Anything reaches here is not valid, generating an error using null
            return null;
        }
        if (expr.isApp()) {
            Expr[] innerArgs = expr.getArgs();
            long[] computedArgs = new long[innerArgs.length];
            for (int i = 0; i < innerArgs.length; i++) {
                computedArgs[i] = applyThenCompute(innerArgs[i], args, params, size);
            }
            if (expr.isBVShiftLeft()) {
                return pbeDispatch("bvshl", computedArgs, false, size);
            }
            if (expr.isBVShiftRightLogical()) {
                return pbeDispatch("bvlshr", computedArgs, false, size);
            }
            // TODO: consider other bv operators
            if (expr.isEq()) {
                return pbeDispatch("=", computedArgs, false, size);
            }
            // Anything reaches here should be a defined-function
            return pbeDispatch(expr.getFuncDecl().getName().toString(), computedArgs, false, size);
        }
        // Anything reaches here is not valid, generating an error using null
        return null;
    }

}
