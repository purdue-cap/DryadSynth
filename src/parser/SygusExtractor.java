import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.microsoft.z3.*;

public class SygusExtractor extends SygusBaseListener {
    Context z3ctx;
    SygusExtractor(Context initctx) {
        z3ctx = initctx;
        nomCombinedConstraint = z3ctx.mkTrue();
        invCombinedConstraint = z3ctx.mkTrue();
    }

    enum CmdType {
        SYNTHFUNC, SYNTHINV, FUNCDEF, CONSTRAINT, INVCONSTRAINT, DECLVAR, DECLPVAR, NONE
    }
    CmdType currentCmd = CmdType.NONE;
    boolean currentOnArgList = false;

    public List<String> names = new LinkedList<String>();
    public Map<String, FuncDecl> requests = new LinkedHashMap<String, FuncDecl>(); // Original requests
    public Map<String, Expr[]> requestArgs = new LinkedHashMap<String, Expr[]>(); // Request arguments with readable names
    public Map<String, Expr[]> requestUsedArgs = new LinkedHashMap<String, Expr[]>(); // Used arguments
    public Map<String, FuncDecl> rdcdRequests = new LinkedHashMap<String, FuncDecl>(); // Reduced request using used arguments
    public Map<String, DefinedFunc> candidate = new LinkedHashMap<String, DefinedFunc>(); // possible solution candidates from the benchmark
    List<Expr> currentArgList;
    List<Sort> currentSortList;
    
    public enum ProbType {
        CLIA, INV
    }
    public ProbType problemType = null;

    public Map<String, Expr> vars = new LinkedHashMap<String, Expr>();
    public Map<String, Expr> regularVars = new LinkedHashMap<String, Expr>();
    public List<BoolExpr> constraints = new ArrayList<BoolExpr>(); // General constraints
    public Map<String, DefinedFunc[]> invConstraints = new LinkedHashMap<String, DefinedFunc[]>(); // Invariant constraints
    public BoolExpr nomCombinedConstraint; // CLIA combined constraints
    public BoolExpr invCombinedConstraint; // INV combined constraints
    public BoolExpr finalConstraint = null; // Final constraint expressed using reduced request declearations
    Stack<Object> termStack = new Stack<Object>();

    public Map<String, DefinedFunc> funcs = new LinkedHashMap<String, DefinedFunc>();
    Map<String, Expr> defFuncVars;

    Sort strToSort(String name) {
        Sort sort;
        switch(name) {
            case "Int":
                sort = z3ctx.getIntSort();
                break;
            case "Bool":
                sort = z3ctx.getBoolSort();
                break;
            case "Real":
                sort = z3ctx.getRealSort();
                break;
            default:
                sort = null;
            }
        return sort;
    }

    public SygusExtractor translate(Context ctx) {
        if (this.z3ctx == ctx) {
            return this;
        }
        SygusExtractor newExtractor = new SygusExtractor(ctx);
        for(String name : this.names) {
            newExtractor.names.add(name);
        }
        for(String key : this.requests.keySet()) {
            newExtractor.requests.put(key, this.requests.get(key).translate(ctx));
        }
        for(String key: this.requestArgs.keySet()){
            Expr[] argList = this.requestArgs.get(key);
            Expr[] newArgList = new Expr[argList.length];
            for(int i = 0; i < argList.length; i++){
                newArgList[i] = argList[i].translate(ctx);
            }
            newExtractor.requestArgs.put(key, newArgList);
        }
        for(String key: this.requestUsedArgs.keySet()){
            Expr[] argList = this.requestUsedArgs.get(key);
            Expr[] newArgList = new Expr[argList.length];
            for(int i = 0; i < argList.length; i++){
                newArgList[i] = argList[i].translate(ctx);
            }
            newExtractor.requestUsedArgs.put(key, newArgList);
        }
        for(String key : this.rdcdRequests.keySet()) {
            newExtractor.rdcdRequests.put(key, this.rdcdRequests.get(key).translate(ctx));
        }
        for(String key : this.candidate.keySet()) {
            newExtractor.candidate.put(key, this.candidate.get(key).translate(ctx));
        }
        for(String key : this.vars.keySet()) {
            newExtractor.vars.put(key, this.vars.get(key).translate(ctx));
        }
        for(String key : this.regularVars.keySet()) {
            newExtractor.regularVars.put(key, this.regularVars.get(key).translate(ctx));
        }
        for(BoolExpr expr : this.constraints) {
            newExtractor.constraints.add((BoolExpr)expr.translate(ctx));
        }
        for(String key : this.invConstraints.keySet()) {
            DefinedFunc[] funcs = new DefinedFunc[3];
            DefinedFunc[] origFuncs = this.invConstraints.get(key);
            for (int i = 0; i < 3; i++) {
                funcs[i] = origFuncs[i].translate(ctx);
            }
            newExtractor.invConstraints.put(key, funcs);
        }
        newExtractor.nomCombinedConstraint = (BoolExpr)this.nomCombinedConstraint.translate(ctx);
        newExtractor.invCombinedConstraint = (BoolExpr)this.invCombinedConstraint.translate(ctx);
        if (this.finalConstraint != null) {
            newExtractor.finalConstraint = (BoolExpr)this.finalConstraint.translate(ctx);
        }
        for(String key : this.funcs.keySet()) {
            newExtractor.funcs.put(key, this.funcs.get(key).translate(ctx));
        }
        return newExtractor;
    }

    Set<Expr> scanForVars(Expr orig) {
        Set<Expr> scanned = new HashSet<Expr>();
        Queue<Expr> todo = new LinkedList<Expr>();
        todo.add(orig);
        while (!todo.isEmpty()) {
            Expr expr = todo.remove();
            if (expr.isConst()) {
                scanned.add(expr);
            } else if (expr.isApp()) {
                for(Expr arg: expr.getArgs()) {
                    todo.add(arg);
                }
            } else if(expr.isQuantifier()) {
                todo.add(((Quantifier)expr).getBody());
            }
        }
        return scanned;
    }

    public void exitStart(SygusParser.StartContext ctx) {
        // This listener is for used variable scanning after the parsing of the
        // input benchmark, for the sake of simplifying function synthesis

        // CLIA problems and INV problems shall be handled separately
        Set<String> invFuncs = new HashSet<String>();
        for (String name : names) {
            if (invConstraints.keySet().contains(name)) {
                invFuncs.add(name);
            }
        }
        Set<String> nomFuncs = new HashSet<String>(requests.keySet());
        nomFuncs.removeAll(invFuncs);

        // Store variables in Sets as preparation
        Set<Expr> varSet = new HashSet<Expr>(vars.values());
        Set<Expr> rVarSet = new HashSet<Expr>(regularVars.values());
        Set<Expr> pVarSet = new HashSet<Expr>(varSet);
        pVarSet.removeAll(rVarSet);
        // INV problem variable scan
        for (String name : invFuncs) {
            Set<Expr> usedInPre = scanForVars(invConstraints.get(name)[0].getDef());
            Set<Expr> usedInTrans = scanForVars(invConstraints.get(name)[1].getDef());
            Set<Expr> usedInPost = scanForVars(invConstraints.get(name)[2].getDef());
            // Check for possible candidates
            Set<Expr> unusedRegularFromTrans = new HashSet<Expr>(rVarSet);
            unusedRegularFromTrans.retainAll(usedInTrans);
            if (unusedRegularFromTrans.isEmpty()) {
                candidate.put(name, invConstraints.get(name)[2]);
            }
            // Unused variable in pref definition is unused
            Set<Expr> unusedFromPre = new HashSet<Expr>(rVarSet);
            unusedFromPre.removeAll(usedInPre);
            // Unused prime variable in transf definition is unused
            Set<Expr> unusedPrimeFromTrans = new HashSet<Expr>(pVarSet);
            unusedPrimeFromTrans.removeAll(usedInTrans);
            Set<Expr> unusedFromTrans = new HashSet<Expr>();
            for (Expr expr : unusedPrimeFromTrans) {
                String str = expr.toString();
                str = str.substring(0, str.length() - 1);
                unusedFromTrans.add(vars.get(str));
            }
            Set<Expr> unused = new HashSet<Expr>(unusedFromPre);
            unused.addAll(unusedFromTrans);
            // Any variable used in postf is used
            unused.removeAll(usedInPost);
            List<Expr> usedList = new ArrayList<Expr>();
            for (Expr expr : requestArgs.get(name)) {
                if (!unused.contains(expr)) {
                    usedList.add(expr);
                }
            }
            requestUsedArgs.put(name, usedList.toArray(new Expr[usedList.size()]));
        }

        // Prepare for CLIA Scan
        // Sets for variable usage in each function calls
        Map<String, List<Set<Expr>>> usedInArgs = new HashMap<String, List<Set<Expr>>>();
        for (String name : nomFuncs) {
            List<Set<Expr>> setList = new ArrayList<Set<Expr>>();
            for (int i = 0; i < requestArgs.get(name).length; i++) {
                setList.add(new HashSet<Expr>());
            }
            usedInArgs.put(name, setList);
        }

        // CLIA problem variable scan, in constraints and in function call arguments
        Set<Expr> usedInConstraints = new HashSet<Expr>();
        Stack<Expr> todo = new Stack<Expr>();
        Stack<Expr> funcCall = new Stack<Expr>();
        int requestCallDepth = 0;
        todo.add(nomCombinedConstraint);
        while (!todo.empty()) {
            Expr expr = todo.peek();
            if (expr.isConst()) {
                todo.pop();
                if (requestCallDepth == 0) {
                    usedInConstraints.add(expr);
                }
            } else if (expr.isApp()) {
                FuncDecl func = expr.getFuncDecl();
                String name = func.getName().toString();
                Expr[] args = expr.getArgs();
                if (funcCall.empty() || funcCall.peek() != expr) {
                    for(Expr arg: args) {
                        todo.push(arg);
                    }
                    if (nomFuncs.contains(name)) {
                        for (int i = 0; i < args.length; i++) {
                            usedInArgs.get(name).get(i).addAll(scanForVars(args[i]));
                        }
                        requestCallDepth = requestCallDepth + 1;
                    }
                    funcCall.push(expr);
                } else {
                    todo.pop();
                    funcCall.pop();
                    if (nomFuncs.contains(name)) {
                        requestCallDepth = requestCallDepth - 1;
                    }
                }
            } else if(expr.isQuantifier()) {
                todo.pop();
                todo.push(((Quantifier)expr).getBody());
            } else {
                todo.pop();
            }
        }

        // Generate used arg list for CLIA problems
        for (String name : nomFuncs) {
            Set<Expr> unused = new HashSet<Expr>();
            List<Set<Expr>> argSets = usedInArgs.get(name);
            for (Set<Expr> argSet : argSets) {
                Set<Expr> used;
                boolean hasInterpart = false;
                used = new HashSet<Expr>(argSet);
                used.retainAll(usedInConstraints);
                if (!used.isEmpty()) {
                    hasInterpart = true;
                }
                for (Set<Expr> set : argSets) {
                    if (set != argSet) {
                        used = new HashSet<Expr>(argSet);
                        used.retainAll(set);
                        if (!used.isEmpty()) {
                            hasInterpart = true;
                        }
                    }
                }
                if (!hasInterpart) {
                    unused.addAll(argSet);
                }
            }

            List<Expr> usedList = new ArrayList<Expr>();
            for (Expr expr : requestArgs.get(name)) {
                if (!unused.contains(expr)) {
                    usedList.add(expr);
                }
            }
            requestUsedArgs.put(name, usedList.toArray(new Expr[usedList.size()]));
        }

        // Generate reduced function declarations and final constraints
        finalConstraint = z3ctx.mkAnd(nomCombinedConstraint, invCombinedConstraint);
        for (String name : names) {
            Expr[] args = requestUsedArgs.get(name);
            Expr[] allArgs = requestArgs.get(name);
            Sort[] domain = new Sort[args.length];
            for (int i = 0; i < domain.length; i++) {
                domain[i] = args[i].getSort();
            }
            FuncDecl func = requests.get(name);
            Sort range = func.getRange();
            FuncDecl rdcdFunc = z3ctx.mkFuncDecl(name, domain, range);
            rdcdRequests.put(name, rdcdFunc);
            DefinedFunc df = new DefinedFunc(z3ctx, name, allArgs, rdcdFunc.apply(args));
            finalConstraint = (BoolExpr)df.rewrite(finalConstraint, func);
        }

        finalConstraint = (BoolExpr)finalConstraint.simplify();
    }

    public void enterSynthFunCmd(SygusParser.SynthFunCmdContext ctx) {
        problemType = ProbType.CLIA;
        currentCmd = CmdType.SYNTHFUNC;
        currentArgList = new ArrayList<Expr>();
        currentSortList = new ArrayList<Sort>();
    }

    public void exitSynthFunCmd(SygusParser.SynthFunCmdContext ctx) {
        String name = ctx.symbol().getText();
        Expr[] argList = currentArgList.toArray(new Expr[currentArgList.size()]);
        Sort[] typeList = currentSortList.toArray(new Sort[currentSortList.size()]);
        Sort returnType = strToSort(ctx.sortExpr().getText());
        FuncDecl func = z3ctx.mkFuncDecl(name, typeList, returnType);
        names.add(name);
        requests.put(name, func);
        requestArgs.put(name, argList);
        currentCmd = CmdType.NONE;
    }

    public void enterSynthInvCmd(SygusParser.SynthInvCmdContext ctx) {
        problemType = ProbType.INV;
        currentCmd = CmdType.SYNTHINV;
        currentArgList = new ArrayList<Expr>();
        currentSortList = new ArrayList<Sort>();
    }

    public void exitSynthInvCmd(SygusParser.SynthInvCmdContext ctx) {
        String name = ctx.symbol().getText();
        Expr[] argList = currentArgList.toArray(new Expr[currentArgList.size()]);
        Sort[] typeList = currentSortList.toArray(new Sort[currentSortList.size()]);
        Sort returnType = z3ctx.mkBoolSort();
        FuncDecl func = z3ctx.mkFuncDecl(name, typeList, returnType);
        names.add(name);
        requests.put(name, func);
        requestArgs.put(name, argList);
        currentCmd = CmdType.NONE;
    }

    public void enterArgList(SygusParser.ArgListContext ctx) {
        currentOnArgList = true;
    }

    public void exitArgList(SygusParser.ArgListContext ctx) {
        currentOnArgList = false;
    }

    Expr addOrGetVarPool(String name, Sort type, boolean prime) {
        Expr newVar;
        if (vars.get(name) != null) {
            newVar = vars.get(name);
            Sort poolType = newVar.getSort();
            assert poolType.toString().equals(type.toString()) : "Type mismatch for same name var";
        } else {
            newVar = z3ctx.mkConst(name, type);
            vars.put(name, newVar);
            if (!prime) {
                regularVars.put(name, newVar);
            }
        }
        return newVar;
    }

    public void enterSymbolSortPair(SygusParser.SymbolSortPairContext ctx) {
        if (currentOnArgList) {
            Sort type = strToSort(ctx.sortExpr().getText());
            String name = ctx.symbol().getText();
            if (currentCmd == CmdType.SYNTHFUNC || currentCmd == CmdType.SYNTHINV) {
                currentArgList.add(addOrGetVarPool(name, type, false));
                currentSortList.add(type);
            }
            if (currentCmd == CmdType.FUNCDEF) {
                defFuncVars.put(name, z3ctx.mkConst(name, type));
            }
        }
    }

    public void enterVarDeclCmd(SygusParser.VarDeclCmdContext ctx) {
        currentCmd = CmdType.DECLVAR;
    }

    public void exitVarDeclCmd(SygusParser.VarDeclCmdContext ctx) {
        String name = ctx.symbol().getText();
        Sort type = strToSort(ctx.sortExpr().getText());
        addOrGetVarPool(name, type, false);
        currentCmd = CmdType.NONE;
    }

    public void enterDeclarePrimedVar(SygusParser.DeclarePrimedVarContext ctx) {
        currentCmd = CmdType.DECLPVAR;
    }

    public void exitDeclarePrimedVar(SygusParser.DeclarePrimedVarContext ctx) {
        String name = ctx.symbol().getText();
        String namep = name + "!";
        Sort type = strToSort(ctx.sortExpr().getText());
        Expr var = z3ctx.mkConst(name, type);
        Expr varp = z3ctx.mkConst(namep, type);
        addOrGetVarPool(name, type, false);
        addOrGetVarPool(namep, type, true);
        currentCmd = CmdType.NONE;
    }

    public void enterConstraintCmd(SygusParser.ConstraintCmdContext ctx) {
        currentCmd = CmdType.CONSTRAINT;
    }

    public void exitConstraintCmd(SygusParser.ConstraintCmdContext ctx) {
        BoolExpr cstrt = (BoolExpr)termStack.pop();
        constraints.add(cstrt);
        nomCombinedConstraint = z3ctx.mkAnd(nomCombinedConstraint, cstrt);
        currentCmd = CmdType.NONE;
    }

    public void enterInvConstraintCmd(SygusParser.InvConstraintCmdContext ctx) {
        currentCmd = CmdType.INVCONSTRAINT;
    }

    public void exitInvConstraintCmd(SygusParser.InvConstraintCmdContext ctx) {
        String name = ctx.symbol(0).getText();
        FuncDecl inv = requests.get(name);
        DefinedFunc pre = funcs.get(ctx.symbol(1).getText());
        DefinedFunc trans = funcs.get(ctx.symbol(2).getText());
        DefinedFunc post = funcs.get(ctx.symbol(3).getText());
        Expr[] transArgs = trans.getArgs();
        Expr[] transArgsOrig = Arrays.copyOfRange(transArgs, 0, transArgs.length/2);
        Expr[] transArgsPrime = Arrays.copyOfRange(transArgs, transArgs.length/2, transArgs.length);
        BoolExpr startCstrt = z3ctx.mkImplies((BoolExpr)pre.getDef(),
                                (BoolExpr)inv.apply(pre.getArgs()));
        BoolExpr loopCstrt = z3ctx.mkImplies(z3ctx.mkAnd((BoolExpr)trans.getDef(),
                                                (BoolExpr)inv.apply(transArgsOrig)),
                                (BoolExpr)inv.apply(transArgsPrime));
        BoolExpr endCstrt = z3ctx.mkImplies((BoolExpr)inv.apply(post.getArgs()),
                                (BoolExpr)post.getDef());
        // Add to general constraints, invariant constraints and combined constraints
        constraints.add(startCstrt);
        constraints.add(loopCstrt);
        constraints.add(endCstrt);
        invConstraints.put(name, new DefinedFunc[]{pre, trans, post});
        BoolExpr cstrt = z3ctx.mkAnd(startCstrt, loopCstrt, endCstrt);
        invCombinedConstraint = z3ctx.mkAnd(invCombinedConstraint, cstrt);
        currentCmd = CmdType.NONE;
    }

    public void enterFunDefCmd(SygusParser.FunDefCmdContext ctx){
        currentCmd = CmdType.FUNCDEF;
        defFuncVars = new LinkedHashMap<String, Expr>();
    }

    public void exitFunDefCmd(SygusParser.FunDefCmdContext ctx){
        String name = ctx.symbol().getText();
        Expr[] argList = defFuncVars.values().toArray(new Expr[defFuncVars.size()]);
        Expr def = (Expr)termStack.pop();
        DefinedFunc func = new DefinedFunc(z3ctx, name, argList, def);
        funcs.put(name, func);
        currentCmd = CmdType.NONE;
    }

    // Since vars in `defFuncVars` should now be always in `vars`, this
    // dispatcher may not be neccessary and could be deprecated
    void symbolDispatcher(String name, boolean checkLocal) {
        Expr var;
        if (checkLocal) {
            var = defFuncVars.get(name);
            if (var != null) {
                termStack.push(var);
                return;
            }
        }
        var = vars.get(name);
        if (var != null) {
            termStack.push(var);
            return;
        }
        if (checkLocal) {
            DefinedFunc df = funcs.get(name);
            if (df != null && df.getNumArgs() == 0) {
                termStack.push(df.apply());
                return;
            }
        }
        FuncDecl f = requests.get(name);
        if (f != null && f.getDomainSize() == 0) {
            termStack.push(f.apply());
            return;
        }
        termStack.push(null);
        return;
    }

    public void enterTerm(SygusParser.TermContext ctx) {
        if (currentCmd == CmdType.CONSTRAINT ||
            currentCmd == CmdType.FUNCDEF) {
            int numChildren = ctx.getChildCount();
            if (numChildren == 1) {
                if (ctx.symbol() != null) {
                    String name = ctx.symbol().getText();
                    symbolDispatcher(name, currentCmd == CmdType.FUNCDEF);
                } else if (ctx.literal() != null) {
                    termStack.push(literalToExpr(ctx.literal()));
                }
            } else {
                termStack.push(ctx);
            }
        }
    }

    Expr literalToExpr(SygusParser.LiteralContext ctx) {
        if (ctx.intConst()!= null) {
            return z3ctx.mkInt(ctx.intConst().getText());
        }
        if (ctx.realConst()!= null) {
            return z3ctx.mkReal(ctx.realConst().getText());
        }
        if (ctx.boolConst()!= null) {
            return ctx.boolConst().getText() == "true" ? z3ctx.mkTrue() : z3ctx.mkFalse();
        }
        return null;
    }

    public void exitTerm(SygusParser.TermContext ctx){
        if (currentCmd == CmdType.CONSTRAINT ||
            currentCmd == CmdType.FUNCDEF) {
            if (ctx.getChildCount()!= 1) {
                List<Expr> args = new ArrayList<Expr>();
                Object top = termStack.pop();
                while (top != ctx) {
                    args.add(0, (Expr)top);
                    top = termStack.pop();
                }
                String name = ctx.symbol().getText();
                Expr res = operationDispatcher(name, args.toArray(new Expr[args.size()]));
                termStack.push(res);
            }
        }
    }

    Expr operationDispatcher(String name, Expr[] args) {
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
        DefinedFunc df = funcs.get(name);
        if (df != null) {
            return df.apply(args);
        }
        FuncDecl f = requests.get(name);
        if (f != null) {
            return z3ctx.mkApp(f, args);
        }
        return null;
    }
}
