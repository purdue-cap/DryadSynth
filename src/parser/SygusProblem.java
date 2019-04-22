import java.util.*;
import com.microsoft.z3.*;

public class SygusProblem {
    public Context ctx;

    public List<String> names = new LinkedList<String>();
    public Map<String, FuncDecl> requests = new LinkedHashMap<String, FuncDecl>(); // Original requests
    public Map<String, Expr[]> requestArgs = new LinkedHashMap<String, Expr[]>(); // Request arguments with readable names
    public Map<String, Expr[]> requestUsedArgs = new LinkedHashMap<String, Expr[]>(); // Used arguments
    public Map<String, Expr[]> requestSyntaxUsedArgs = new LinkedHashMap<String, Expr[]>(); // Used arguments in Syntax, used in AT fragment
    public Map<String, FuncDecl> rdcdRequests = new LinkedHashMap<String, FuncDecl>(); // Reduced request using used arguments
    public Map<String, DefinedFunc> candidate = new LinkedHashMap<String, DefinedFunc>(); // possible solution candidates from the benchmark

    public int searchHeight;    // the search height tried in fixed-height-cegis

    public enum ProbType {
        CLIA, INV, GENERAL;
    }

    public ProbType problemType;

    public SygusProblem(Context ctx) {
        this.ctx = ctx;
        this.combinedConstraint = ctx.mkTrue();
        this.invCombinedConstraint = ctx.mkTrue();
        this.opDis = new OpDispatcher(ctx, this.requests, this.funcs);
    }

    public Map<String, Expr> vars = new LinkedHashMap<String, Expr>();
    public Map<String, Expr> regularVars = new LinkedHashMap<String, Expr>();
    public List<BoolExpr> constraints = new ArrayList<BoolExpr>(); // General constraints
    public Map<String, DefinedFunc[]> invConstraints = new LinkedHashMap<String, DefinedFunc[]>(); // Invariant constraints
    public BoolExpr combinedConstraint; // CLIA combined constraints
    public BoolExpr invCombinedConstraint; // INV combined constraints
    public BoolExpr finalConstraint = null; // Final constraint expressed using reduced request declearations
    public Map<String, DefinedFunc> funcs = new LinkedHashMap<String, DefinedFunc>();
    public OpDispatcher opDis;

    // For grammar parsing
    public enum SybType {
        LITERAL, GLBVAR, FUNC, SYMBOL, LCLARG, CSTINT, CSTBOL;
    }
    // Inner grammar class
    public static class CFG {
        private Context z3ctx;
        public Map<String, Sort> grammarSybSort = new LinkedHashMap<String, Sort>();
        public Map<String, List<String[]>>  grammarRules = new LinkedHashMap<String, List<String[]>>();
        // For symbol resolving
        public Map<String, SybType> sybTypeTbl = new LinkedHashMap<String, SybType>();
        public Map<String, Expr> localArgs = new LinkedHashMap<String, Expr>();

        public CFG(Context ctx) {this.z3ctx = ctx;}
        public CFG(CFG src){
            this.z3ctx = src.z3ctx;
            this.grammarSybSort.putAll(src.grammarSybSort);
            for (String key : src.grammarRules.keySet()) {
                List<String[]> newList = new ArrayList<String[]>(src.grammarRules.get(key));
                this.grammarRules.put(key, newList);
            }
            this.sybTypeTbl.putAll(src.sybTypeTbl);
            this.localArgs.putAll(src.localArgs);
        }

        public CFG translate(Context newctx) {
            if (this.z3ctx == newctx) {
                return this;
            }
            CFG newcfg = new CFG(newctx);
            for (String key : this.grammarSybSort.keySet()) {
                newcfg.grammarSybSort.put(key, (Sort)this.grammarSybSort.get(key).translate(newctx));
            }
            for (String key : this.grammarRules.keySet()) {
                List<String[]> newList = new ArrayList<String[]>(this.grammarRules.get(key));
                newcfg.grammarRules.put(key, newList);
            }
            newcfg.sybTypeTbl.putAll(this.sybTypeTbl);
            for (String key : this.localArgs.keySet()) {
                newcfg.localArgs.put(key, this.localArgs.get(key).translate(newctx));
            }
            return newcfg;
        }
    }

    public Map<String, SybType> glbSybTypeTbl = new LinkedHashMap<String, SybType>();
    public Map<String, CFG> cfgs = new LinkedHashMap<String, CFG>();
    public boolean isGeneral;

    public SygusProblem(SygusProblem src) {
        this.ctx = src.ctx;
        this.names = new LinkedList<String>(src.names);
        this.requests = new LinkedHashMap<String, FuncDecl>(src.requests);
        this.requestArgs = new LinkedHashMap<String, Expr[]>(src.requestArgs);
        this.requestUsedArgs = new LinkedHashMap<String, Expr[]>(src.requestUsedArgs);
        this.requestSyntaxUsedArgs = new LinkedHashMap<String, Expr[]>(src.requestSyntaxUsedArgs);
        this.rdcdRequests = new LinkedHashMap<String, FuncDecl>(src.rdcdRequests);
        this.candidate = new LinkedHashMap<String, DefinedFunc>(src.candidate);

        this.problemType = src.problemType;

        this.vars = new LinkedHashMap<String, Expr>(src.vars);
        this.regularVars = new LinkedHashMap<String, Expr>(src.regularVars);
        this.constraints = new ArrayList<BoolExpr>(src.constraints);
        this.invConstraints = new LinkedHashMap<String, DefinedFunc[]>(src.invConstraints);
        this.combinedConstraint = src.combinedConstraint;
        this.invCombinedConstraint = src.invCombinedConstraint;
        this.finalConstraint = src.finalConstraint;
        this.funcs = new LinkedHashMap<String, DefinedFunc>(src.funcs);
        this.opDis = new OpDispatcher(this.ctx, this.requests, this.funcs);

        this.glbSybTypeTbl = new LinkedHashMap<String, SygusProblem.SybType>(src.glbSybTypeTbl);
        for (String key : src.cfgs.keySet()) {
            this.cfgs.put(key, new SygusProblem.CFG(src.cfgs.get(key)));
        }
        this.isGeneral = src.isGeneral;

        this.searchHeight = src.searchHeight;
    }

    public SygusProblem translate(Context ctx) {
        if (this.ctx == ctx) {
            return this;
        }
        SygusProblem newProblem = new SygusProblem(ctx);
        newProblem.names.addAll(this.names);
        for(String key : this.requests.keySet()) {
            newProblem.requests.put(key, this.requests.get(key).translate(ctx));
        }
        for(String key: this.requestArgs.keySet()){
            Expr[] argList = this.requestArgs.get(key);
            Expr[] newArgList = new Expr[argList.length];
            for(int i = 0; i < argList.length; i++){
                newArgList[i] = argList[i].translate(ctx);
            }
            newProblem.requestArgs.put(key, newArgList);
        }
        for(String key: this.requestUsedArgs.keySet()){
            Expr[] argList = this.requestUsedArgs.get(key);
            Expr[] newArgList = new Expr[argList.length];
            for(int i = 0; i < argList.length; i++){
                newArgList[i] = argList[i].translate(ctx);
            }
            newProblem.requestUsedArgs.put(key, newArgList);
        }
        for(String key: this.requestSyntaxUsedArgs.keySet()){
            Expr[] argList = this.requestSyntaxUsedArgs.get(key);
            Expr[] newArgList = new Expr[argList.length];
            for(int i = 0; i < argList.length; i++){
                newArgList[i] = argList[i].translate(ctx);
            }
            newProblem.requestSyntaxUsedArgs.put(key, newArgList);
        }
        for(String key : this.rdcdRequests.keySet()) {
            newProblem.rdcdRequests.put(key, this.rdcdRequests.get(key).translate(ctx));
        }
        for(String key : this.candidate.keySet()) {
            newProblem.candidate.put(key, this.candidate.get(key).translate(ctx));
        }
        newProblem.problemType = this.problemType;
        for(String key : this.vars.keySet()) {
            newProblem.vars.put(key, this.vars.get(key).translate(ctx));
        }
        for(String key : this.regularVars.keySet()) {
            newProblem.regularVars.put(key, this.regularVars.get(key).translate(ctx));
        }
        for(BoolExpr expr : this.constraints) {
            newProblem.constraints.add((BoolExpr)expr.translate(ctx));
        }
        for(String key : this.invConstraints.keySet()) {
            DefinedFunc[] funcs = new DefinedFunc[3];
            DefinedFunc[] origFuncs = this.invConstraints.get(key);
            for (int i = 0; i < 3; i++) {
                funcs[i] = origFuncs[i].translate(ctx);
            }
            newProblem.invConstraints.put(key, funcs);
        }
        newProblem.combinedConstraint = (BoolExpr)this.combinedConstraint.translate(ctx);
        newProblem.invCombinedConstraint = (BoolExpr)this.invCombinedConstraint.translate(ctx);
        if (this.finalConstraint != null) {
            newProblem.finalConstraint = (BoolExpr)this.finalConstraint.translate(ctx);
        }
        for(String key : this.funcs.keySet()) {
            newProblem.funcs.put(key, this.funcs.get(key).translate(ctx));
        }
        newProblem.opDis = new OpDispatcher(newProblem.ctx, newProblem.requests, newProblem.funcs);

        newProblem.glbSybTypeTbl.putAll(this.glbSybTypeTbl);
        for(String key : this.cfgs.keySet()) {
            newProblem.cfgs.put(key, this.cfgs.get(key).translate(ctx));
        }
        newProblem.isGeneral = this.isGeneral;

        return newProblem;
    }
}
