import java.lang.reflect.Array;
import java.util.*;
import com.microsoft.z3.*;
import java.math.BigInteger;

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

    public bitVector pbeDispatch(String name, bitVector[] args, boolean definedOnly, boolean doNotInterp,int size) {
        // if (name.equals("+")) {
        //     return bitVector.add(args[0],args[1]);
        // }
        // if (name.equals("-")) {
        //     return bitVector.sub(args[0],args[1]);
        // }
        // if (name.equals("*")) {
        //     return bitVector.mul(args[0],args[1],size);
        // }
        // if (name.equals("/")) {
        //     return bitVector.div(args[0],args[1],size);
        // }
        // if (name.equals("and")) {
        //     return bitVector.and(args[0],args[1]);
        // }
        // if (name.equals("or")) {
        //     return bitVector.or(args[0],args[1]);
        // }
        // if (name.equals("not")) {
        //     return bitVector.not(args[0]);
        // }
        // if (name.equals(">")) {
        //     return bitVector.sgt(args[0],args[1],size);
        // }
        // if (name.equals(">=")) {
        //     return bitVector.sge(args[0],args[1],size);
        // }
        // if (name.equals("<")) {
        //     return bitVector.slt(args[0],args[1],size);
        // }
        // if (name.equals("<=")) {
        //     return bitVector.sle(args[0],args[1],size);
        // }
        // if (name.equals("=")) {
        //     return bitVector.eq(args[0],args[1],size);
        // }
        // if (name.equals("=>")) {
        //     return z3ctx.mkImplies((BoolExpr)args[0], (BoolExpr)args[1]);
        // }
        // if (name.equals("ite")) {
        //     return z3ctx.mkITE((BoolExpr)args[0], args[1], args[2]);
        // }
        // if (name.equals("if")) {
        //     return z3ctx.mkITE((BoolExpr)args[0], args[1], args[2]);
        // }
        // if (name.equals("div")) {
        //     return z3ctx.mkDiv((ArithExpr)args[0], (ArithExpr)args[1]);
        // }
        // if (name.equals("mod")) {
        //     return z3ctx.mkMod((IntExpr)args[0], (IntExpr)args[1]);
        // }
        // bitvec operators
        if (name.equals("ite")) {
            return bitVector.ite(args[0],args[1],args[2],size);
        }
        if (name.equals("bvadd")) {
            return bitVector.add(args[0],args[1]);
        }
        if (name.equals("bvsub")) {
            return bitVector.sub(args[0],args[1]);
        }
        if (name.equals("bvneg")) {
            return bitVector.neg(args[0]);
        }
        if (name.equals("bvmul")) {
            return bitVector.mul(args[0],args[1],size);
        }
        if (name.equals("bvurem")) {
            return bitVector.urem(args[0],args[1],size);
        }
        if (name.equals("bvudiv")) {
            return bitVector.udiv(args[0],args[1],size);
        }
        if (name.equals("bvsdiv")) {
            return bitVector.sdiv(args[0],args[1],size);
        }
        if (name.equals("bvsrem")) {
            return bitVector.srem(args[0],args[1],size);
        }
        if (name.equals("bvsmod")) {
            return bitVector.smod(args[0],args[1],size);
        }
        if (name.equals("bvshl")) {
            return bitVector.shl(args[0],args[1].unsignValue(size).intValue(),size);
        }
        if (name.equals("bvlshr")) {
            return bitVector.lshr(args[0],args[1].unsignValue(size).intValue(),size);
        }
        if (name.equals("bvashr")) {
            return bitVector.ashr(args[0],args[1].unsignValue(size).intValue(),size);
        }
        if (name.equals("bvor")) {
            return bitVector.or(args[0],args[1]);
        }
        if (name.equals("bvand")) {
            return bitVector.and(args[0],args[1]);
        }
        if (name.equals("bvnot")) {
            return bitVector.not(args[0]);
        }
        if (name.equals("bvnand")) {
            return bitVector.nand(args[0],args[1]);
        }
        if (name.equals("bvxor")) {
            return bitVector.xor(args[0],args[1]);
        }
        if (name.equals("bvnor")) {
            return bitVector.nor(args[0],args[1]);
        }
        if (name.equals("bvxnor")) {
            return bitVector.xnor(args[0],args[1]);
        }
        if (name.equals("bvuge")) {
            bitVector result = new bitVector();
            if(bitVector.uge(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("bvugt")) {
            bitVector result = new bitVector();
            if(bitVector.ugt(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("bvule")) {
            bitVector result = new bitVector();
            if(bitVector.ule(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("bvult")) {
            bitVector result = new bitVector();
            if(bitVector.ult(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("bvsge")) {
            bitVector result = new bitVector();
            if(bitVector.sge(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("bvsgt")) {
            bitVector result = new bitVector();
            if(bitVector.sgt(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("bvsle")) {
            bitVector result = new bitVector();
            if(bitVector.sle(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("bvslt")) {
            bitVector result = new bitVector();
            if(bitVector.slt(args[0],args[1],size) == true){
                result.bitset.set(0);
            }
            return result;
        }
        if (name.equals("xor")) {
            return bitVector.xor(args[0],args[1]);
        }

        DefinedFunc df = funcs.get(name);
        if (df != null) {
            if (doNotInterp) {
                // return df.applyUninterp(args);
            } else {
                Expr[] ExprArgs = new Expr[args.length];
                for(int i = 0;i<args.length;i++){
                    ExprArgs[i] = BitVectorToExpr(args[i],size);
                }
                return ExprToBitVector(df.apply(ExprArgs).simplify());
                // Expr tmpOutput = df.apply(ExprArgs);
                // Expr[] tmpArgs = tmpOutput.getArgs();
                // bitVector[] inputArgs = new bitVector[tmpArgs.length];
                // for(int i = 0 ; i < inputArgs.length;i++){
                //     inputArgs[i] = ExprToBitVector(tmpArgs[i]);
                // }
                // int pos = tmpOutput.toString().indexOf(" ");
                // return pbeDispatch(tmpOutput.toString().substring(1,pos), inputArgs, true, false, size);
            }
        }
        if (definedOnly) {
            return null;
        }
        // FuncDecl f = requests.get(name);
        // if (f != null) {
        //     return z3ctx.mkApp(f, args);
        // }
        return null;
    }

    public bitVector ExprToBitVector(Expr input){
        bitVector result = new bitVector();
        String inputStr = input.simplify().toString();
        if(inputStr.equals("false")){
            BigInteger value = BigInteger.valueOf(0);
            result.setData(value.longValue());
            return result;
        } 
        else if(inputStr.equals("true")){
            BigInteger value = BigInteger.valueOf(1);
            result.setData(value.longValue());
            return result;
        }
        else{   
            BigInteger value = new BigInteger(inputStr);
            result.setData(value.longValue());
            return result;
        }
        
    }

    public Expr BitVectorToExpr(bitVector input,int size){
        Expr result = z3ctx.mkBV(input.signValue(size).longValue(), size);
        return result;
    }
}
