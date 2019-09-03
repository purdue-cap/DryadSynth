import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.microsoft.z3.*;

public class SygusExtractor extends SygusBaseListener {
	Context z3ctx;
    public SygusExtractor(Context initctx) {
        z3ctx = initctx;
        combinedConstraint = z3ctx.mkTrue();
        invCombinedConstraint = z3ctx.mkTrue();
    }
    enum CmdType {
        SYNTHFUNC, SYNTHINV, FUNCDEF, CONSTRAINT, INVCONSTRAINT, DECLVAR, NTDEF, NONE
    }//NTDEF->grammardef
    CmdType currentCmd = CmdType.NONE;
    boolean currentOnArgList = false;

    List<String> names = new LinkedList<String>();
    Map<String, FuncDecl> requests = new LinkedHashMap<String, FuncDecl>(); // Original requests
    Map<String, Expr[]> requestArgs = new LinkedHashMap<String, Expr[]>(); // Request arguments with readable names
    Map<String, Expr[]> requestUsedArgs = new LinkedHashMap<String, Expr[]>(); // Used arguments
    Map<String, Expr[]> requestSyntaxUsedArgs = new LinkedHashMap<String, Expr[]>(); // Used arguments in Syntax, used in AT fragment
    Map<String, FuncDecl> rdcdRequests = new LinkedHashMap<String, FuncDecl>(); // Reduced request using used arguments
    Map<String, DefinedFunc> candidate = new LinkedHashMap<String, DefinedFunc>(); // possible solution candidates from the benchmark
    List<Expr> currentArgList;
    List<String> currentArgNameList;
    List<Sort> currentSortList;
    Map<String, Expr> lettmp = new LinkedHashMap<String,Expr>();
    SygusProblem.ProbType problemType = null;

    Map<String, Expr> vars = new LinkedHashMap<String, Expr>();
    Map<String, Expr> regularVars = new LinkedHashMap<String, Expr>();
    List<BoolExpr> constraints = new ArrayList<BoolExpr>(); // General constraints
    Map<String, DefinedFunc[]> invConstraints = new LinkedHashMap<String, DefinedFunc[]>(); // Invariant constraints
    BoolExpr combinedConstraint; // CLIA combined constraints
    BoolExpr invCombinedConstraint; // INV combined constraints
    BoolExpr finalConstraint = null; // Final constraint expressed using reduced request declearations
    Stack<Object> termStack = new Stack<Object>();

    Map<String, DefinedFunc> funcs = new LinkedHashMap<String, DefinedFunc>();
    Map<String, Expr> defFuncVars;

    String currentSymbol;
    String currentTerm;
    boolean inGrammarArgs = false;
    boolean inLetTerms = false;
    List<String> grammarArgs = new ArrayList<String>();

    Map<String, SygusProblem.SybType> glbSybTypeTbl = new LinkedHashMap<String, SygusProblem.SybType>();
    Map<String, SygusProblem.CFG> cfgs = new LinkedHashMap<String, SygusProblem.CFG>();
    SygusProblem.CFG currentCFG = null;
    boolean isGeneral;

    // CLIA grammar extension to enforce CLIA algorithm on general track benchmarks
    boolean cliaGrammar = false;

    public SygusProblem createProblem() {
        SygusProblem pblm = new SygusProblem(z3ctx);
        pblm.names = new LinkedList<String>(this.names);
        pblm.requests = new LinkedHashMap<String, FuncDecl>(this.requests);
        pblm.requestArgs = new LinkedHashMap<String, Expr[]>(this.requestArgs);
        pblm.requestUsedArgs = new LinkedHashMap<String, Expr[]>(this.requestUsedArgs);
        pblm.requestSyntaxUsedArgs = new LinkedHashMap<String, Expr[]>(this.requestSyntaxUsedArgs);
        pblm.rdcdRequests = new LinkedHashMap<String, FuncDecl>(this.rdcdRequests);
        pblm.candidate = new LinkedHashMap<String, DefinedFunc>(this.candidate);

        pblm.problemType = this.problemType;

        pblm.vars = new LinkedHashMap<String, Expr>(this.vars);
        pblm.regularVars = new LinkedHashMap<String, Expr>(this.regularVars);
        pblm.constraints = new ArrayList<BoolExpr>(this.constraints);
        pblm.invConstraints = new LinkedHashMap<String, DefinedFunc[]>(this.invConstraints);
        pblm.combinedConstraint = this.combinedConstraint;
        pblm.invCombinedConstraint = this.invCombinedConstraint;
        pblm.finalConstraint = this.finalConstraint;
        pblm.funcs = new LinkedHashMap<String, DefinedFunc>(this.funcs);
        pblm.opDis = new OpDispatcher(this.z3ctx, this.requests, this.funcs);

        pblm.glbSybTypeTbl = new LinkedHashMap<String, SygusProblem.SybType>(this.glbSybTypeTbl);
        for (String key : this.cfgs.keySet()) {
            pblm.cfgs.put(key, new SygusProblem.CFG(this.cfgs.get(key)));
        }
        pblm.isGeneral = this.isGeneral;
        return pblm;
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
    
    public void exitStart(SygusParser.StartContext ctx){
        // This listener is for used variable scanning after the parsing of the
        // input benchmark, for the sake of simplifying function synthesis

        // Unset isGeneral when CLIA grammar is detected
        if (cliaGrammar) {
            isGeneral = false;
        }

        // Currently we're not trying these procedures on General tracks
        if (isGeneral) {
            // Generate finalConstraint
            finalConstraint = z3ctx.mkAnd(constraints.toArray(new BoolExpr[constraints.size()]));
            finalConstraint = (BoolExpr)finalConstraint.simplify();
            // Use unprocessed as dummy for processed
            rdcdRequests = requests;
            requestUsedArgs = requestArgs;
            requestSyntaxUsedArgs = requestArgs;
            return;
        }

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

            Set<Expr> syntaxUnused = new HashSet<Expr>(unusedFromPre);
            syntaxUnused.addAll(unusedFromTrans);
            syntaxUnused.removeAll(usedInTrans);
            syntaxUnused.removeAll(usedInPost);
            
            List<Expr> usedList = new ArrayList<Expr>();
            List<Expr> syntaxUsedList = new ArrayList<Expr>();
            for (Expr expr : requestArgs.get(name)) {
                if (!unused.contains(expr)) {
                    usedList.add(expr);
                }
                if (!syntaxUnused.contains(expr)) {
                    syntaxUsedList.add(expr);
                }
            }
            requestUsedArgs.put(name, usedList.toArray(new Expr[usedList.size()]));
            requestSyntaxUsedArgs.put(name, syntaxUsedList.toArray(new Expr[syntaxUsedList.size()]));
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
        todo.add(combinedConstraint);
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

        // Avoid functions with completely empty arglist, which may cause CEGIS
        // algorithm to behave badly
        for (String name: requestUsedArgs.keySet()) {
            if (requestUsedArgs.get(name).length == 0) {
                requestUsedArgs.put(name, requestArgs.get(name));
            }
        }
        for (String name: requestSyntaxUsedArgs.keySet()) {
            if (requestSyntaxUsedArgs.get(name).length == 0) {
                requestSyntaxUsedArgs.put(name, requestArgs.get(name));
            }
        }

        // Generate reduced function declarations and final constraints
        finalConstraint = z3ctx.mkAnd(combinedConstraint, invCombinedConstraint);
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

    public void enterSynthfun(SygusParser.SynthfunContext ctx) {
        currentCmd = CmdType.SYNTHFUNC;
        currentArgList = new ArrayList<Expr>();
        currentArgNameList = new ArrayList<String>();
        currentSortList = new ArrayList<Sort>();
    }
    public void exitSynthfun(SygusParser.SynthfunContext ctx) {
        String name = ctx.symbol().getText();
        Expr[] argList = currentArgList.toArray(new Expr[currentArgList.size()]);
        Sort[] typeList = currentSortList.toArray(new Sort[currentSortList.size()]);
        Sort returnType = identifierToSort(ctx.sort().identifier());
        FuncDecl func = z3ctx.mkFuncDecl(name, typeList, returnType);
        names.add(name);
        requests.put(name, func);
        requestArgs.put(name, argList);
        if (currentCFG != null) {
            int i = 0;
            for (String argName : currentArgNameList) {
                currentCFG.localArgs.put(argName, argList[i]);
                i++;
            }
            for (String arg : currentCFG.localArgs.keySet()) {
                currentCFG.sybTypeTbl.put(arg, SygusProblem.SybType.LCLARG);
            }
            cfgs.put(name, currentCFG);
            currentCFG = null;
        }
        currentCmd = CmdType.NONE;
    }


    public void enterGrammardef(SygusParser.GrammardefContext ctx){
        problemType = SygusProblem.ProbType.GENERAL;
        isGeneral = true;
        currentCmd = CmdType.NTDEF;
    }
    public void enterGroupedrulelist(SygusParser.GroupedrulelistContext ctx){
        if (currentCFG == null) {
            currentCFG = new SygusProblem.CFG(z3ctx);
        }
        currentSymbol = ctx.symbol().getText();
        assert (ctx.sort().identifier() != null) :ctx.sort().sortextra().getText();   
        Sort currentSort = identifierToSort(ctx.sort().identifier());
        currentCFG.grammarSybSort.put(currentSymbol, currentSort);
        currentCFG.sybTypeTbl.put(currentSymbol, SygusProblem.SybType.SYMBOL);
        currentCFG.grammarRules.put(currentSymbol, new ArrayList<String[]>());

        currentCmd = CmdType.NTDEF;
    }
    public void enterGterm(SygusParser.GtermContext ctx){
        if (ctx.getChild(1) != null && ctx.getChild(1).getText().equals("Constant")) {
            if (ctx.sort().getText().equals("Int")) {
                currentTerm = "ConstantInt";
                glbSybTypeTbl.put(currentTerm, SygusProblem.SybType.CSTINT);
            } else if (ctx.sort().getText().equals("Bool")) {
                currentTerm = "ConstantBool";
                glbSybTypeTbl.put(currentTerm, SygusProblem.SybType.CSTBOL);
            } else {
                currentTerm = null;
            }
        }//todo:add support for (Variable sort) for btr*
    }
    public void enterBfterm(SygusParser.BftermContext ctx){
        if(ctx.idenbftermplus()!=null){
            inGrammarArgs = true;
        }
    }
    public void exitBfterm(SygusParser.BftermContext ctx){//todo:convert symbol into bfterm
        if(ctx.idenbftermplus()!=null){
            if(ctx.idenbftermplus().bfiteexpr()!=null){
                currentTerm = "ite";
            }else if(ctx.idenbftermplus().bfboolexpr()!=null){
                SygusParser.BfboolexprContext tmpctx = ctx.idenbftermplus().bfboolexpr();
                if(tmpctx.bfandexpr()!=null){
                    currentTerm = "and";
                }else if (tmpctx.bforexpr()!=null) {
                    currentTerm = "or";
                }else if (tmpctx.bfnotexpr()!=null) {
                    currentTerm = "not";
                }else if (tmpctx.bfeqexpr()!=null) {
                    currentTerm = "=";
                }else if (tmpctx.bfgtexpr()!=null) {
                    currentTerm = ">";
                }else if (tmpctx.bfgeexpr()!=null) {
                    currentTerm = ">=";
                }else if (tmpctx.bfltexpr()!=null) {
                    currentTerm = "<";
                }else if (tmpctx.bfleexpr()!=null) {
                    currentTerm = "<=";
                }else if (tmpctx.bftoexpr()!=null) {
                    currentTerm = "=>";

                }
            }else if(ctx.idenbftermplus().bfintexpr()!=null){
                SygusParser.BfintexprContext tmpctx = ctx.idenbftermplus().bfintexpr();
                if(tmpctx.bfaddexpr()!=null){
                    currentTerm = "+";
                }else if (tmpctx.bfminusexpr()!=null) {
                    currentTerm = "-";

                }else if (tmpctx.bfnegexpr()!=null) {
                    currentTerm = "-";

                }else if (tmpctx.bfmulexpr()!=null) {
                    currentTerm = "=*";

                }
            }else if(ctx.idenbftermplus().bfbitexpr()!=null){
                if(ctx.idenbftermplus().bfbitexpr().bfbitarith()!=null){
                    SygusParser.BfbitarithContext tmpctx = ctx.idenbftermplus().bfbitexpr().bfbitarith();
                    if(tmpctx.bfbvadd()!=null){
                        currentTerm = "bvadd";
                    }else if (tmpctx.bfbvsub()!=null) {
                        currentTerm = "bvsub";
                    }else if (tmpctx.bfbvneg()!=null) {
                        currentTerm = "bvneg";
                    }else if (tmpctx.bfbvmul()!=null) {
                        currentTerm = "bvmul";
                    }else if (tmpctx.bfbvurem()!=null) {
                        currentTerm = "bvurem";
                    }else if (tmpctx.bfbvsrem()!=null) {
                        currentTerm = "bvsrem";
                    }else if (tmpctx.bfbvsmod()!=null) {
                        currentTerm = "bvsmod";
                    }else if (tmpctx.bfbvshl()!=null) {
                        currentTerm = "bvshl";
                    }else if (tmpctx.bfbvlshr()!=null) {
                        currentTerm = "bvlshr";
                    }else if (tmpctx.bfbvashr()!=null) {
                        currentTerm = "bvashr";
                    }
                }else{
                    SygusParser.BfbitwiseContext tmpctx = ctx.idenbftermplus().bfbitexpr().bfbitwise();
                    if(tmpctx.bfbvor()!=null){
                        currentTerm = "bvor";
                    }else if (tmpctx.bfbvand()!=null) {
                        currentTerm = "bvand";
                    }else if (tmpctx.bfbvnot()!=null) {
                        currentTerm = "bvnot";
                    }else if (tmpctx.bfbvnand()!=null) {
                        currentTerm = "bvnand";
                    }else if (tmpctx.bfbvnor()!=null) {
                        currentTerm = "bvnor";
                    }else if(tmpctx.bfbvxnor()!=null) {
                        currentTerm = "bvxnor";
                    }
                }
            }else{
                currentTerm = ctx.idenbftermplus().idenbftermplusextra().identifier().getText();
            }

        }else if(ctx.identifier()!=null){
            assert ctx.identifier().symbol()!=null : "unexpect bfterm";
            currentTerm = ctx.identifier().symbol().getText();
        }else if (ctx.literal()!=null) {
            currentTerm = ctx.literal().getText();
            glbSybTypeTbl.put(currentTerm, SygusProblem.SybType.LITERAL);
        }else{
            currentTerm = null;
        }
        if(inGrammarArgs){
            if(ctx.idenbftermplus()==null){
                grammarArgs.add(currentTerm);
            }else{
                if(OpDispatcher.internalOps.contains(currentTerm)){
                    glbSybTypeTbl.put(currentTerm, SygusProblem.SybType.FUNC);
                }
                String[] args = grammarArgs.toArray(new String[grammarArgs.size()]);
                String[] repr = Arrays.copyOf(new String[]{currentTerm}, 1 + args.length);
                System.arraycopy(args, 0, repr, 1, args.length);
                currentCFG.grammarRules.get(currentSymbol).add(repr);
                grammarArgs.clear();
                inGrammarArgs = false;
            }
            
            
        } else{
            currentCFG.grammarRules.get(currentSymbol).add(new String[]{currentTerm});
        }
    }
    public void exitGroupedrulelist(SygusParser.GroupedrulelistContext ctx){
        currentCmd = CmdType.NONE;
    }

    
    public void enterSynthinv(SygusParser.SynthinvContext ctx) {
        problemType = SygusProblem.ProbType.INV;
        currentCmd = CmdType.SYNTHINV;
        currentArgList = new ArrayList<Expr>();
        currentArgNameList = new ArrayList<String>();
        currentSortList = new ArrayList<Sort>();
    }
    public void exitSynthinv(SygusParser.SynthinvContext ctx) {
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
    Sort identifierToSort(SygusParser.IdentifierContext idctx) {
        Sort sort;
        if(idctx.identifierextra()==null){
            String name = idctx.getText();
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
                    assert false :name;
            }
        } else{
            assert idctx.identifierextra().symbol().getText().equals("BitVec") : "New type detected in identifierextra";
            sort = z3ctx.mkBitVecSort(Integer.parseInt(idctx.identifierextra().index(0).getText()));
        }
        return sort;
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
            glbSybTypeTbl.put(name, SygusProblem.SybType.GLBVAR);
            if(!prime) {
                regularVars.put(name, newVar);
            }
        }
        return newVar;
    }
    public void enterDeclarevar(SygusParser.DeclarevarContext ctx) { 
        currentCmd = CmdType.DECLVAR;
    }
    public void exitDeclarevar(SygusParser.DeclarevarContext ctx) { 
        String name = ctx.symbol().getText();
        assert (ctx.sort().identifier() != null) :ctx.sort().sortextra().getText();   
        Sort type = identifierToSort(ctx.sort().identifier());
        addOrGetVarPool(name, type, false);
        currentCmd = CmdType.NONE;
    }


    public void enterTerm(SygusParser.TermContext ctx){
        if(ctx.literal()!=null){
            Expr tmp = literalToExpr(ctx.literal());
            termStack.push(tmp);
        } else if(ctx.identifier()!=null){
            String name = ctx.identifier().getText();
            symbolDispatcher(name, currentCmd == CmdType.FUNCDEF);
        } else if(ctx.identermplus()==null&&ctx.let()==null){
            assert false : ctx.getText();
        } else{
        	if(ctx.let()==null)
            	termStack.push(ctx);
        } 
    }
    void symbolDispatcher(String name, boolean checkLocal) {
        Expr var;
        if (checkLocal) {
            var = defFuncVars.get(name);
            if (var != null) {
                termStack.push(var);
                return;
            }else{
                var = lettmp.get(name);
                if(var != null){
                    termStack.push(var);
                    return;
                }
            }
        }
        var = vars.get(name);
        if (var != null) {
            termStack.push(var);
            return;
        }
        else{
            var = lettmp.get(name);
            if(var != null){
                termStack.push(var);
                return;
            }
        }
        if (checkLocal) {
            DefinedFunc df = funcs.get(name);
            if (df != null && df.getNumArgs() == 0) {
                termStack.push(df.apply(new Expr[0]));
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
    Expr literalToExpr(SygusParser.LiteralContext ctx) {
        if (ctx.numeral()!= null) {
            return z3ctx.mkInt(ctx.numeral().getText());
        }
        if (ctx.decimal()!= null) {
            return z3ctx.mkReal(ctx.decimal().getText());
        }
        if (ctx.boolconst()!= null) {
            return ctx.boolconst().getText().equals("true") ? z3ctx.mkTrue() : z3ctx.mkFalse();
        }
        if (ctx.hexconst()!=null) {
            return hex(ctx.hexconst().getText());
        }
        if (ctx.binconst()!= null) {
            return bin(ctx.binconst().getText());
        }
        if (ctx.stringconst()!= null) {
            return z3ctx.mkString(ctx.stringconst().getText());
        }
        return null;
    }
    BitVecNum hex(String hexnum){
        int len = hexnum.length();
        int tmp = 0;
        for(char c:hexnum.toCharArray()){
            if(c>='0'&&c<='9'){
                tmp*=2;
                tmp+=c-'0';
            }
            if(c>='a'&&c<='f'){
                tmp*=2;
                tmp+=c-'a'+10;
            }
            if(c>='A'&&c<='F'){
                tmp*=2;
                tmp+=c-'a'+10;
            }
        }
        return z3ctx.mkBV(tmp,(len-1)*4);
    }
    BitVecNum bin(String binnum){
        int len = binnum.length();
        int tmp = 0;
        for(char c:binnum.toCharArray()){
            if(c=='0')
                tmp*=2;
            if(c=='1'){
                tmp*=2;
                tmp+=1;
            }
        }
        return z3ctx.mkBV(tmp,(len-1));
    }
    public void identermplustostack(SygusParser.IdentermplusContext ctx,SygusParser.TermContext topctx){
        List<Expr> tmpargs = new ArrayList<Expr>();
        Object top = termStack.pop();
        while (top != topctx) {
        	if (top instanceof SygusParser.TermContext){
        		SygusParser.TermContext tmp2=(SygusParser.TermContext)top;
        	}
            Expr tmpexpr = (Expr)top;
            tmpargs.add(0, tmpexpr);
            top = termStack.pop();
        }
        Expr[] args = tmpargs.toArray(new Expr[tmpargs.size()]);
        Expr expr=null;
        if(ctx.iteexpr()!=null){
            assert args.length==3 : "Wrong args number";
            expr = z3ctx.mkITE((BoolExpr)args[0],args[1],args[2]);
        }else if(ctx.boolexpr()!=null){
            SygusParser.BoolexprContext tmpctx = ctx.boolexpr();
            if(tmpctx.andexpr()!=null){
                expr = z3ctx.mkAnd(Arrays.copyOf(args, args.length, BoolExpr[].class));
            }else if (tmpctx.orexpr()!=null) {
                expr = z3ctx.mkOr(Arrays.copyOf(args, args.length, BoolExpr[].class));
            }else if (tmpctx.notexpr()!=null) {
                assert args.length==1 : "Wrong args number";
                expr = z3ctx.mkNot((BoolExpr)args[0]);
            }else if (tmpctx.eqexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkEq(args[0],args[1]);
            }else if (tmpctx.gtexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkGt((ArithExpr)args[0], (ArithExpr)args[1]);
            }else if (tmpctx.geexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkGe((ArithExpr)args[0], (ArithExpr)args[1]);
            }else if (tmpctx.ltexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkLt((ArithExpr)args[0], (ArithExpr)args[1]);
            }else if (tmpctx.leexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkLe((ArithExpr)args[0], (ArithExpr)args[1]);
            }else if (tmpctx.toexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkImplies((BoolExpr)args[0], (BoolExpr)args[1]);
            }
        }else if(ctx.intexpr()!=null){
            SygusParser.IntexprContext tmpctx = ctx.intexpr();
            if(tmpctx.addexpr()!=null){
                expr = z3ctx.mkAdd(Arrays.copyOf(args, args.length, ArithExpr[].class));
            }else if (tmpctx.minusexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkSub(Arrays.copyOf(args, args.length, ArithExpr[].class));
            }else if (tmpctx.negexpr()!=null) {
                assert args.length==1 : "Wrong args number";
                expr = z3ctx.mkSub(Arrays.copyOf(args, args.length, ArithExpr[].class));//todo: not sure
            }else if (tmpctx.mulexpr()!=null) {
                assert args.length==2 : "Wrong args number";
                expr = z3ctx.mkMul(Arrays.copyOf(args, args.length, ArithExpr[].class));
            }
        }else if(ctx.bitexpr()!=null){
            if(ctx.bitexpr().bitarith()!=null){
                SygusParser.BitarithContext tmpctx = ctx.bitexpr().bitarith();
                if(tmpctx.bvadd()!=null){
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVAdd((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvsub()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVSub((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvneg()!=null) {
                    assert args.length==1 : "Wrong args number";
                    expr = z3ctx.mkBVNeg((BitVecExpr)args[0]);
                }else if (tmpctx.bvmul()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVMul((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvurem()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVURem((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvsrem()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVSRem((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvsmod()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVSMod((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvshl()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVSHL((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvlshr()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVLSHR((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvashr()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVASHR((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }
            }else{
                SygusParser.BitwiseContext tmpctx = ctx.bitexpr().bitwise();
                if(tmpctx.bvor()!=null){
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVOR((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvand()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVAND((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvnot()!=null) {
                    assert args.length==1 : "Wrong args number";
                    expr = z3ctx.mkBVNot((BitVecExpr)args[0]);
                }else if (tmpctx.bvnand()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVNAND((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if (tmpctx.bvnor()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVNOR((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }else if(tmpctx.bvxnor()!=null) {
                    assert args.length==2 : "Wrong args number";
                    expr = z3ctx.mkBVXNOR((BitVecExpr)args[0],(BitVecExpr)args[1]);
                }
            }
        }else{
            assert ctx.identermplusextra().identifier().symbol()!=null : ctx.identermplusextra().identifier().getText();
            String name = ctx.identermplusextra().identifier().symbol().getText();
            DefinedFunc df = funcs.get(name);
            if(df!=null){
                expr = df.apply(args);
            }else{
                FuncDecl f =requests.get(name);
                assert f!=null;
                expr = z3ctx.mkApp(f,args);
            }
        }
        termStack.push(expr);
        return;
    }
    public void exitVarbinding(SygusParser.VarbindingContext ctx){
        String name = ctx.symbol().getText();
        Expr expr = (Expr)termStack.pop();
        lettmp.put(name,expr);
    }
    public void exitLet(SygusParser.LetContext ctx){
        List<SygusParser.VarbindingContext> vartodel = ctx.varbinding();
        for(SygusParser.VarbindingContext tmpctx:vartodel){
            String tmp = tmpctx.symbol().getText();
            lettmp.remove(tmp);
        }
    }
    public void exitTerm(SygusParser.TermContext ctx){
        if(ctx.identermplus()!=null){
            identermplustostack(ctx.identermplus(),ctx);
        }
    }


    public void enterConstraint(SygusParser.ConstraintContext ctx) {
        currentCmd = CmdType.CONSTRAINT;
    }
    public void exitConstraint(SygusParser.ConstraintContext ctx) {
        BoolExpr cstrt = (BoolExpr)termStack.pop();
        constraints.add(cstrt);
        combinedConstraint = z3ctx.mkAnd(combinedConstraint, cstrt);
        currentCmd = CmdType.NONE;
    }
    public void enterInvconstraint(SygusParser.InvconstraintContext ctx) {
        currentCmd = CmdType.INVCONSTRAINT;
    }
    public void exitInvconstraint(SygusParser.InvconstraintContext ctx) {
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


    public void enterDefinefun(SygusParser.DefinefunContext ctx) {
        currentCmd = CmdType.FUNCDEF;
        defFuncVars = new LinkedHashMap<String, Expr>();
    }
    public void enterSortedvar(SygusParser.SortedvarContext ctx){
        Sort type;
        String name = ctx.symbol().getText();
        assert ctx.sort().identifier() != null : ctx.sort().sortextra().getText();
        type = identifierToSort(ctx.sort().identifier());
        if (currentCmd == CmdType.SYNTHFUNC || currentCmd == CmdType.SYNTHINV) {
            currentArgList.add(addOrGetVarPool(name, type, false));
            currentArgNameList.add(name);
            currentSortList.add(type);
        }
        if (currentCmd == CmdType.FUNCDEF) {
            defFuncVars.put(name, z3ctx.mkConst(name, type));
            if(name.toCharArray()[name.length()-1]=='!'){
                addOrGetVarPool(name.substring(0,name.length()-1), type, false);
                addOrGetVarPool(name, type, true);
            }//for prime-var in v1
        }
    }
    public void exitDefinefun(SygusParser.DefinefunContext ctx) {
        String name = ctx.symbol().getText();
        Expr[] argList = defFuncVars.values().toArray(new Expr[defFuncVars.size()]);
        Expr def = (Expr)termStack.pop();
        DefinedFunc func = new DefinedFunc(z3ctx, name, argList, def);
        funcs.put(name, func);
        glbSybTypeTbl.put(name, SygusProblem.SybType.FUNC);
        currentCmd = CmdType.NONE;
    }


    public void enterSetlogic(SygusParser.SetlogicContext ctx) {
        String logic = ctx.logicsymbol().getText();
        if(logic.equals("CLIA")){
            problemType = SygusProblem.ProbType.CLIA;
        } else if(logic.equals("SLIA")){
            problemType = SygusProblem.ProbType.SLIA;
        } else if(logic.equals("BV")){
            problemType = SygusProblem.ProbType.BV;
        }
    }
}