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
    public boolean changed = false;     // flag indicates that if a problem is the original problem
    public Map<String, SygusDispatcher.CoeffRange> coeffRange;  // coefficient range for converted General track functions

    public enum ProbType {
        CLIA, INV, GENERAL, SLIA, BV;
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
    public Map<String, Set<Set<Expr>>> varsRelation = new LinkedHashMap<String, Set<Set<Expr>>>(); // vars relationship map, used for INV DnC

    // For grammar parsing
    public enum SybType {
        LITERAL, NUMERAL, HEX, BIN, GLBVAR, FUNC, SYMBOL, LCLARG, CSTINT, CSTBOL, CSTBIT, BOOL;
    }
    // Inner grammar class
    public static class CFG {
        private Context ctx;
        public Map<String, Sort> grammarSybSort = new LinkedHashMap<String, Sort>();
        public Map<String, List<String[]>>  grammarRules = new LinkedHashMap<String, List<String[]>>();
        // For symbol resolving
        public Map<String, SybType> sybTypeTbl = new LinkedHashMap<String, SybType>();
        public Map<String, Expr> localArgs = new LinkedHashMap<String, Expr>();
        public void printcfg(){
            System.out.println("grammarSybSort");
            for(String key : this.grammarSybSort.keySet()){
                System.out.println(key);
                System.out.println(this.grammarSybSort.get(key).toString());
            }
            System.out.println("grammarRules");
            for(String key : this.grammarRules.keySet()){
                System.out.println(key);
                for (String[] grammarRule : this.grammarRules.get(key)) {
                    System.out.println(Arrays.deepToString(grammarRule));
                }
            }
            System.out.println("sybTypeTbl");
            for(String key : this.sybTypeTbl.keySet()){
                System.out.println(key);
                System.out.println(this.sybTypeTbl.get(key));
            }
            System.out.println("localArgs");
            for(String key : this.localArgs.keySet()){
                System.out.println(key);
                System.out.println(this.localArgs.get(key).toString());
            }
        }
        public CFG(Context ctx) {this.ctx = ctx;}
        public CFG(CFG src){
            this.ctx = src.ctx;
            this.grammarSybSort.putAll(src.grammarSybSort);
            for (String key : src.grammarRules.keySet()) {
                List<String[]> newList = new ArrayList<String[]>(src.grammarRules.get(key));
                this.grammarRules.put(key, newList);
            }
            this.sybTypeTbl.putAll(src.sybTypeTbl);
            this.localArgs.putAll(src.localArgs);
        }

        public CFG translate(Context newctx) {
            if (this.ctx == newctx) {
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
        this.varsRelation = new LinkedHashMap<String, Set<Set<Expr>>>(src.varsRelation);

        this.glbSybTypeTbl = new LinkedHashMap<String, SygusProblem.SybType>(src.glbSybTypeTbl);
        for (String key : src.cfgs.keySet()) {
            this.cfgs.put(key, new SygusProblem.CFG(src.cfgs.get(key)));
        }
        this.isGeneral = src.isGeneral;

        this.searchHeight = src.searchHeight;
        this.changed = src.changed;
        this.coeffRange = src.coeffRange;
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

        // this.varsRelation = new LinkedHashMap<String, Set<Set<Expr>>>(src.varsRelation);
        newProblem.varsRelation = new LinkedHashMap<String, Set<Set<Expr>>>();
        for (String key : this.varsRelation.keySet()) {
            Set<Set<Expr>> relation = new HashSet<Set<Expr>>();
            for (Set<Expr> varset : this.varsRelation.get(key)) {
                Set<Expr> newset = new HashSet<Expr>();
                for (Expr variable : varset) {
                    newset.add(variable.translate(ctx));
                }
                relation.add(newset);
            }
            newProblem.varsRelation.put(key, relation);
        }

        return newProblem;
    }

    public SygusProblem createINVSubProblem(Expr[] usedArgs, Expr[] usedArgswprime, Expr pre, Expr trans, Expr post) {
        String name = this.names.get(0);        // names.size() should be 1
        
        // // prescreen to eliminate unused variables
        // Set<Expr> usedInPre = SygusExtractor.scanForVars(pre);
        // Set<Expr> usedInTrans = SygusExtractor.scanForVars(trans);
        // Set<Expr> usedInPost = SygusExtractor.scanForVars(post);
        // // Check for possible candidates
        // Set<Expr> unusedRegularFromTrans = new HashSet<Expr>(Arrays.asList(usedArgs));
        // unusedRegularFromTrans.retainAll(usedInTrans);
        // // if (unusedRegularFromTrans.isEmpty()) {
        // //     pblm.candidate.put(name, invConstraints.get(name)[2]);
        // // }
        // // Unused variable in pref definition is unused
        // Set<Expr> unusedFromPre = new HashSet<Expr>(Arrays.asList(usedArgs));
        // unusedFromPre.removeAll(usedInPre);
        // // Unused prime variable in transf definition is unused
        // Set<Expr> unusedPrimeFromTrans = new HashSet<Expr>(Arrays.asList(usedArgswprime));
        // unusedPrimeFromTrans.removeAll(Arrays.asList(usedArgs));
        // unusedPrimeFromTrans.removeAll(usedInTrans);
        // Set<Expr> unusedFromTrans = new HashSet<Expr>();
        // for (Expr expr : unusedPrimeFromTrans) {
        //     String str = expr.toString();
        //     str = str.substring(0, str.length() - 1);
        //     unusedFromTrans.add(vars.get(str));
        // }
        // Set<Expr> unused = new HashSet<Expr>(unusedFromPre);
        // unused.addAll(unusedFromTrans);
        // // Any variable used in postf is used
        // unused.removeAll(usedInPost);
        // List<Expr> usedList = new ArrayList<Expr>();
        // for (Expr expr : requestArgs.get(name)) {
        //     if (!unused.contains(expr)) {
        //         usedList.add(expr);
        //     }
        // }
        // // requestUsedArgs.put(name, usedList.toArray(new Expr[usedList.size()]));
        // Expr[] used = usedList.toArray(new Expr[usedList.size()]);
        // Expr[] usedprime = new Expr[used.length];
        // for (int i = 0; i < used.length; i++) {
        //     String argname = used[i].toString() + "!";
        //     usedprime[i] = ctx.mkConst(argname, used[i].getSort());
        // }
        // Expr[] argswithprime = new Expr[used.length * 2];
        // System.arraycopy(used, 0, argswithprime, 0, used.length);
        // System.arraycopy(usedprime, 0, argswithprime, used.length, used.length);

        Sort[] domain = new Sort[usedArgs.length];
        for (int i = 0; i < domain.length; i++) {
            domain[i] = usedArgs[i].getSort();
        }
        FuncDecl func = this.requests.get(name);
        Sort range = func.getRange();
        FuncDecl rdcdFunc = ctx.mkFuncDecl(name, domain, range);

        SygusProblem pblm = new SygusProblem(ctx);
        pblm.names.add(name);
        pblm.requests.put(name, this.requests.get(name));
        pblm.requestArgs.put(name, this.requestArgs.get(name));
        pblm.requestUsedArgs.put(name, usedArgs);
        pblm.requestSyntaxUsedArgs.put(name, usedArgs);
        pblm.rdcdRequests.put(name, rdcdFunc);

        pblm.problemType = this.problemType;

        pblm.vars = new LinkedHashMap<String, Expr>(this.vars);
        pblm.regularVars = new LinkedHashMap<String, Expr>(this.regularVars);

        DefinedFunc[] origfuncs = this.invConstraints.get(name);
        DefinedFunc[] newfuncs = new DefinedFunc[3];
        newfuncs[0] = new DefinedFunc(ctx, origfuncs[0].getName(), usedArgs, pre);
        newfuncs[1] = new DefinedFunc(ctx, origfuncs[1].getName(), usedArgswprime, trans);
        newfuncs[2] = new DefinedFunc(ctx, origfuncs[2].getName(), usedArgs, post);

        pblm.combinedConstraint = this.combinedConstraint;
        pblm.invConstraints.put(name, newfuncs);

        Expr[] transArgs = newfuncs[1].getArgs();
        Expr[] transArgsOrig = Arrays.copyOfRange(transArgs, 0, transArgs.length/2);
        Expr[] transArgsPrime = Arrays.copyOfRange(transArgs, transArgs.length/2, transArgs.length);
        BoolExpr startCstrt = ctx.mkImplies((BoolExpr)newfuncs[0].getDef(),
                                (BoolExpr)rdcdFunc.apply(newfuncs[0].getArgs()));
        BoolExpr loopCstrt = ctx.mkImplies(ctx.mkAnd((BoolExpr)newfuncs[1].getDef(),
                                                (BoolExpr)rdcdFunc.apply(transArgsOrig)),
                                (BoolExpr)rdcdFunc.apply(transArgsPrime));
        BoolExpr endCstrt = ctx.mkImplies((BoolExpr)rdcdFunc.apply(newfuncs[2].getArgs()),
                                (BoolExpr)newfuncs[2].getDef());

        pblm.constraints.add(startCstrt);
        pblm.constraints.add(loopCstrt);
        pblm.constraints.add(endCstrt);

        pblm.invCombinedConstraint = ctx.mkAnd(startCstrt, loopCstrt, endCstrt);
        pblm.finalConstraint = (BoolExpr)ctx.mkAnd(pblm.combinedConstraint, pblm.invCombinedConstraint).simplify();

        pblm.funcs = new LinkedHashMap<String, DefinedFunc>(this.funcs);
        pblm.funcs.put(origfuncs[0].getName(), newfuncs[0]);
        pblm.funcs.put(origfuncs[1].getName(), newfuncs[1]);
        pblm.funcs.put(origfuncs[2].getName(), newfuncs[2]);
        pblm.opDis = new OpDispatcher(this.ctx, this.requests, this.funcs);

        pblm.glbSybTypeTbl = new LinkedHashMap<String, SygusProblem.SybType>(this.glbSybTypeTbl);
        for (String key : this.cfgs.keySet()) {
            pblm.cfgs.put(key, new SygusProblem.CFG(this.cfgs.get(key)));
        }
        pblm.isGeneral = this.isGeneral;

        // pblm.candidate = new LinkedHashMap<String, DefinedFunc>();
        // if (unusedRegularFromTrans.isEmpty()) {
        //     pblm.candidate.put(name, pblm.invConstraints.get(name)[2]);
        // }

        return pblm;
    }
}
