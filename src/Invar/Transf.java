import java.util.*;
import com.microsoft.z3.*;

public class Transf {
    private Context z3ctx;
    private Map<String, Expr> vars;
    // Complement Region
    private Expr rc;

    private Tactic qe;
    private Tactic simp;
    public int lastRunIterCount;

    public class uniTrans {
        public Map<Expr, Integer> deltaTbl = new LinkedHashMap<Expr, Integer>();

        public Expr toExpr(Transf t) {
            List<Expr> exprs = new ArrayList<Expr>();
            for (Expr e: deltaTbl.keySet()) {
                exprs.add(z3ctx.mkEq(
                            t.prime(e),
                            z3ctx.mkAdd((ArithExpr)e,
                                z3ctx.mkInt(deltaTbl.get(e)))
                            ));
            }
            return z3ctx.mkAnd(exprs.toArray(new BoolExpr[exprs.size()]));

        }
    }

    private Map<Region, uniTrans> transMap = new LinkedHashMap<Region, uniTrans>();

    public Expr prime(Expr orig) {
        return z3ctx.mkConst(orig.toString() + "!", z3ctx.mkIntSort());
    }

    public Set<Region> getRegions() {
        return this.transMap.keySet();
    }

    public Expr toExpr() {
        List<Expr> clauses = new ArrayList<Expr>();
        for (Region r : transMap.keySet()) {
            clauses.add(z3ctx.mkAnd((BoolExpr)r.toExpr(), (BoolExpr)transMap.get(r).toExpr(this)));
        }
        return z3ctx.mkOr(clauses.toArray(new BoolExpr[clauses.size()]));
    }

    public String toString() {
        String str = "";
        for (Region r : transMap.keySet()) {
            str = str + r.toString() + "\n  =>";
            str = str + transMap.get(r).deltaTbl.toString() + "\n";
        }
        return str;
    }

    public String toKString() {
        String str = "";
        Expr k = z3ctx.mkFreshConst("k", z3ctx.mkIntSort());
        for (Region r : transMap.keySet()) {
            str = str + r.toKExpr(k).toString() + "\n  =>";
            str = str + transMap.get(r).deltaTbl.toString() + "\n";
        }
        return str;
    }

    public void kExtend() {
        List<Expr> clauses = new ArrayList<Expr>();
        for (Region r : transMap.keySet()) {
            r.kExtend(transMap.get(r));
            clauses.add(r.toExpr());
        }
        this.rc = z3ctx.mkNot(z3ctx.mkOr(clauses.toArray(new BoolExpr[clauses.size()])));
        Solver s = z3ctx.mkSolver();
        s.add((BoolExpr)this.rc);
        Status result = s.check();
        if (result == Status.SATISFIABLE) {
            Goal g = z3ctx.mkGoal(false, false, false);
            g.add((BoolExpr)this.rc);
            this.rc = simp.apply(g).getSubgoals()[0].AsBoolExpr();
        } else {
            this.rc = null;
        }
    }

    public Transf(Map<String, Expr> vars, Context ctx) {
        this(vars, ctx, true);
    }

    public Transf(Map<String, Expr> vars, Context ctx, boolean simplify) {
        this.vars = vars;
        this.z3ctx = ctx;

        // Build the necessary tactics here
        this.qe = z3ctx.mkTactic("qe");
        if (simplify) {
            this.simp = z3ctx.repeat(z3ctx.then(
                    z3ctx.mkTactic("simplify"),
                    z3ctx.mkTactic("ctx-simplify"),
                    z3ctx.mkTactic("ctx-solver-simplify")
                    ), 8);
        } else {
            this.simp = z3ctx.skip();
        }
    }

    public Expr localExpand(Expr input) {
        Expr k = z3ctx.mkFreshConst("k", z3ctx.mkIntSort());
        List<Expr> clauses = new ArrayList<Expr>();
        for (Region r : transMap.keySet()) {
            List<Expr> conj = new ArrayList<Expr>();
            conj.add(r.toKExpr(k));
            Expr substInput = input;
            for (Expr var: transMap.get(r).deltaTbl.keySet()) {
                substInput = substInput.substitute(var,
                        z3ctx.mkSub((ArithExpr)var,
                            z3ctx.mkMul((ArithExpr)k,
                                z3ctx.mkInt(transMap.get(r).deltaTbl.get(var))))
                        );
            }
            conj.add(substInput);
            conj.add(z3ctx.mkGe((ArithExpr)k, z3ctx.mkInt(0)));
            clauses.add(z3ctx.mkAnd(conj.toArray(new BoolExpr[conj.size()])));
        }
        Quantifier q = z3ctx.mkExists(
                new Expr[] {k},
                z3ctx.mkOr(clauses.toArray(new BoolExpr[clauses.size()])),
                0,
                new Pattern[] {},
                new Expr[] {},
                z3ctx.mkSymbol(""),
                z3ctx.mkSymbol("")
                );
        Goal g = z3ctx.mkGoal(false, false, false);
        g.add(z3ctx.mkOr(q, (BoolExpr)input));
        g = z3ctx.then(qe, simp).apply(g).getSubgoals()[0];
        return g.AsBoolExpr();
    }

    public Expr switchExpand(Expr input) {
        List<Expr> clauses = new ArrayList<Expr>();
        for (Region r1 : transMap.keySet()) {
            for (Region r2: transMap.keySet()) {
                if (r1 == r2) {
                    continue;
                }
                Expr orig = r1.toExpr();
                Expr dest = r2.toExpr();
                Expr substInput = input;
                Map<Expr, Integer> tbl = transMap.get(r1).deltaTbl;
                for (Expr var: tbl.keySet()) {
                    orig = orig.substitute(var,
                            z3ctx.mkSub((ArithExpr)var, z3ctx.mkInt(tbl.get(var)))
                            );
                    substInput = substInput.substitute(var,
                            z3ctx.mkSub((ArithExpr)var, z3ctx.mkInt(tbl.get(var)))
                            );
                }
                clauses.add(z3ctx.mkAnd(
                            (BoolExpr)orig,
                            (BoolExpr)dest,
                            (BoolExpr)substInput
                            ));
            }
            if (this.rc != null) {
                Expr orig = r1.toExpr();
                Expr dest = this.rc;
                Expr substInput = input;
                Map<Expr, Integer> tbl = transMap.get(r1).deltaTbl;
                for (Expr var: tbl.keySet()) {
                    orig = orig.substitute(var,
                            z3ctx.mkSub((ArithExpr)var, z3ctx.mkInt(tbl.get(var)))
                            );
                    substInput = substInput.substitute(var,
                            z3ctx.mkSub((ArithExpr)var, z3ctx.mkInt(tbl.get(var)))
                            );
                }
                clauses.add(z3ctx.mkAnd(
                            (BoolExpr)orig,
                            (BoolExpr)dest,
                            (BoolExpr)substInput
                            ));
            }
        }
        clauses.add(input);
        BoolExpr expr = z3ctx.mkOr(clauses.toArray(new BoolExpr[clauses.size()]));
        Goal g = z3ctx.mkGoal(false, false, false);
        g.add(expr);
        g = simp.apply(g).getSubgoals()[0];
        return g.AsBoolExpr();
    }

    public boolean isExpanded(Expr before, Expr after) {
        Solver s = z3ctx.mkSolver();
        s.add(z3ctx.mkNot(z3ctx.mkEq(before, after)));
        Status r = s.check();
        return r == Status.SATISFIABLE;
    }

    public Expr run(Expr pre) {
        Expr init = pre;
        this.lastRunIterCount = 0;
        // Timeout count
        int timeout = 8;
        while(true) {
            this.lastRunIterCount++;
            //System.out.println("Iteration: " + this.lastRunIterCount);
            Expr expanded = this.localExpand(init);
            //System.out.println("localExpand: " + expanded.toString());
            expanded = this.switchExpand(expanded);
            //System.out.println("switchExpand: " + expanded.toString());
            if (!this.isExpanded(init, expanded)){
                return expanded;
            }
            init = expanded;
            if (this.lastRunIterCount > timeout) {
                return expanded;
            }
        }
    }

    public void addMap(Region r, int[] deltas) {
        // Input format must match
        if ( deltas.length != vars.size() ) {
            return;
        }

        uniTrans t = new uniTrans();
        int i = 0;
        for (Expr e : vars.values()) {
            t.deltaTbl.put(e, deltas[i]);
            i++;
        }
        this.transMap.put(r, t);
    }

    public static void splitDisj(Expr expr, List<Expr> conjs) {
        Expr [] args;

        if (expr.isOr()) {
            args = expr.getArgs();
            for (Expr arg: args) {
                splitDisj(arg, conjs);
            }
        } else {
            conjs.add(expr);
        }
    }

    public static boolean getDeltaBySyntax(Expr nonCond, Map<String, Expr> vars, Map<Expr, Integer> deltas) {

        // assume nonCond only have the form x! = x + c
        // and x! = x
        if (!nonCond.isEq()) {
            return false;
        }
        Expr[] args  = nonCond.getArgs();
        if (!args[0].isConst()) {
            return false;
        }
        boolean add = true;

        if (args[1].isAdd()) {
            add = true;
        } else if (args[1].isSub()) {
            add = false;
        } else if (args[1].isConst()) {
            if (vars.containsValue(args[1]) &&
                    args[0].toString().equals(args[1].toString() + "!")) {
                deltas.put(args[1], 0);
                return true;
            }
            return false;
        } else {
            return false;
        }

        Expr[] subargs = args[1].getArgs();

        if (!vars.containsValue(subargs[0])) {
            return false;
        }

        if (!subargs[1].isIntNum())  {
            return false;
        }

        if (!args[0].toString().equals(subargs[0].toString() + "!")) {
            return false;
        }

        if (add) {
        deltas.put(subargs[0], Integer.valueOf(subargs[1].toString()));
        } else {
            deltas.put(subargs[0], -Integer.valueOf(subargs[1].toString()));
        }

        return true;
    }

    
    public static Expr mkAlt(Expr v, Context ctx) {
        return ctx.mkIntConst(v.toString() + "!alt");
    }
    public static Expr mkAlt(Expr e, List<Expr> v, Context ctx) {
        Expr result = e;
        for (Expr c : v) {
            Expr alt = mkAlt(c, ctx);
            result = result.substitute(c, alt);
        }
        return result;
    }

    public static boolean getDeltaBySMT(List<Expr>nonConds, Map<String, Expr> vars, Map<Expr, Integer> deltas, Context ctx) {

        List<Expr> allVars = new ArrayList<Expr>();
        Map<String, Expr> pVars = new HashMap<String, Expr>();
        Map<String, Expr> cVars = new HashMap<String, Expr>();
        for (String name : vars.keySet()) {
            pVars.put(name, ctx.mkIntConst(name + "!"));
            cVars.put(name, ctx.mkIntConst("delta_" + name));
            allVars.add(vars.get(name));
            allVars.add(pVars.get(name));
            allVars.add(cVars.get(name));
        }
        Map<String, Expr> rExprs = new HashMap<String, Expr>();

        Solver s = ctx.mkSolver();
        for (Expr e: nonConds) {
            s.add((BoolExpr)e);
        }
        // Save state
        s.push();
        
        // Add the delta expressions
        BoolExpr base = ctx.mkTrue();
        for (String name : vars.keySet()) {
            base = ctx.mkAnd(base, ctx.mkEq(pVars.get(name), ctx.mkAdd((ArithExpr)vars.get(name), (ArithExpr)cVars.get(name))));
        }
        s.add(base);

        // Save state
        s.push();
        
        // Check if unique solution exists
        for (Expr e: nonConds) {
            s.add((BoolExpr)mkAlt(e, allVars, ctx));
        }
        s.add((BoolExpr)mkAlt(base, allVars, ctx));
        for (String name : cVars.keySet()) {
            s.add(ctx.mkNot(ctx.mkEq(cVars.get(name), mkAlt(cVars.get(name), ctx))));
        }

        Status sts = s.check();
        if (sts != Status.UNSATISFIABLE){
            return false;
        }

        s.pop();
        // Solve the deltas
        sts = s.check();
        if (sts != Status.SATISFIABLE){
            return false;
        }

        Model m = s.getModel();
        for (String name : vars.keySet()) {
            Expr dExpr = cVars.get(name);
            Expr result = m.eval(dExpr, false);
            if (!result.isIntNum()) {
                return false;
            }
            rExprs.put(name, result);
        }
        s.pop();

        s.push();
        // Now check if they are constants 
        base = ctx.mkTrue();
        for (String name : vars.keySet()) {
            base = ctx.mkAnd(base, ctx.mkEq(pVars.get(name), ctx.mkAdd((ArithExpr)vars.get(name), (ArithExpr)rExprs.get(name))));
        }
        s.add(ctx.mkNot(base));
        sts = s.check();
        if (sts != Status.UNSATISFIABLE) {
            return false;
        }

        // Put results in delta_map
        for (String name : vars.keySet()) {
            deltas.put(vars.get(name), Integer.valueOf(rExprs.get(name).toString()));
        }
        return true;
    }

    public static Transf fromTransfFormula(Expr formula, Map<String, Expr> vars, Context ctx) {
        Transf t = new Transf(vars, ctx);
        // Convert input formula to DNF format
        Expr dnf = t.convertToDNF(formula);
        if (dnf == null) {
            return null;
        }

        // We enforced may assumptions here, later they need be changed to
        // validation/conversion codes so that these conditions would be ensured
        // for any possible input. These assumptions include but are not limited
        // to these below:
        // 1. Input formula is in DNF format
        // 2. Atomic formulas in DNF are in their simplest forms
        // 3. Transfer functions are in x! = x + delta_x forms, strictly even in
        // the order of the terms
        // 4. Conflit cases would not occur, in any means

        // Split formula to Conjs and iterate over each Conj
        // For each Conj: call Region.fromConj to extract conditions out as
        // regions, and then parse remaining transfer fomulas into uniTrans,
        // and add them using addMap

        List<Expr> conjs = new ArrayList<Expr>();
        splitDisj(formula, conjs);

        for (Expr conj : conjs) {
            Region r = new Region(vars, ctx);
            List<Expr> nonConds = new ArrayList<Expr>();
            Region[] regions = r.fromConj(conj, nonConds, ctx);

            if (regions == null) {
                return null;
            }

            int size = vars.size();
            int[] deltas = new int[size];
            Map<Expr, Integer> delta_map = new LinkedHashMap<Expr, Integer>();


            boolean done = true;
            for(Expr nonCond : nonConds) {
                done = done && getDeltaBySyntax(nonCond, vars, delta_map);
            }

            if (!done) {
                // Syntax extraction failed, falling back to SMT extraction
                done = getDeltaBySMT(nonConds, vars, delta_map, ctx);
            }

            if (!done) {
                // Giving up
                return null;
            }

            int i = 0;
            for (Expr var : vars.values()) {
                if (delta_map.containsKey(var)) {
                    deltas[i] = delta_map.get(var);
                }
                i = i + 1;
            }

            if (regions.length == 0) {
                regions = new Region[1];
                regions[0] = new Region(vars, ctx);
                int[] allzero = new int[size];
                for(int j = 0; j < size; j++) {
                    allzero[j] = 0;
                }
                regions[0].addCond(allzero, 1);
            }

            for (Region region : regions) {
                t.addMap(region, deltas);
            }
        }
        return t;
    }

    // Implement convertion from any formula to DNF
    // First convert the formula to NNF
    // To convert fomula to NNF, first eliminate -> & <-> and ite, then push negations in
    // Then convert NNF to DNF
    public Expr eliminateImpEq(Expr expr) {
        // First eliminate ->
        if (expr.isImplies()) {
            Expr[] args = expr.getArgs();
            return z3ctx.mkOr(z3ctx.mkNot((BoolExpr)eliminateImpEq(args[0])), (BoolExpr)eliminateImpEq(args[1]));
        }
        // Then eliminate <->
        if (expr.isEq()) {
            Expr[] args = expr.getArgs();
            // Equality between arithmetic exprs should be preserved
            // Only eliminate equality between booleans
            if (args[0].isBool() && args[1].isBool()) {
                BoolExpr e1 = (BoolExpr)eliminateImpEq(args[0]);
                BoolExpr e2 = (BoolExpr)eliminateImpEq(args[1]);
                return z3ctx.mkOr(z3ctx.mkAnd(e1, e2), z3ctx.mkAnd(z3ctx.mkNot(e1), z3ctx.mkNot(e2)));
            }
        }
        // Also need to eliminate ITE
        if (expr.isITE()) {
            Expr[] args = expr.getArgs();
            BoolExpr e1 = (BoolExpr)eliminateImpEq(args[0]);
            BoolExpr e2 = (BoolExpr)eliminateImpEq(args[1]);
            BoolExpr e3 = (BoolExpr)eliminateImpEq(args[2]);
            return z3ctx.mkOr(z3ctx.mkAnd(z3ctx.mkNot(e1), e3), z3ctx.mkAnd(e1, e2));
        }
        // Other kind of expression should do the elimination recursively
        if (expr.isNot()) {
            Expr[] args = expr.getArgs();
            return z3ctx.mkNot((BoolExpr)eliminateImpEq(args[0]));
        }
        if (expr.isAnd()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(eliminateImpEq((BoolExpr)e));
            }
            return z3ctx.mkAnd(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (expr.isOr()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(eliminateImpEq((BoolExpr)e));
            }
            return z3ctx.mkOr(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (Region.isAtom(expr)) {
            return expr;
        }
        // Anything reaches here is not valid, generating an error using null
        return null;
    }

    // Already eliminate -> <-> and ite
    // "Push negations in" using DeMorgan's Law
    // \neg (f1 /\ f2) -> (\neg f1) \/ (\neg f2)
    // \neg (f1 \/ f2) -> (\neg f1) /\ (\neg f2)
    public Expr pushNegIn(Expr expr) {
        // Check for (not (not ...)), (not (and ...)), (not (or ...)) forms
        if (expr.isNot()) {
            Expr arg = expr.getArgs()[0];
            if (arg.isNot()) {
                return pushNegIn(arg.getArgs()[0]);
            }
            if (arg.isAnd()) {
                List<Expr> argList = new ArrayList<Expr>();
                for (Expr e: arg.getArgs()) {
                    argList.add(pushNegIn(z3ctx.mkNot((BoolExpr)e)));
                }
                return z3ctx.mkOr(argList.toArray(new BoolExpr[argList.size()]));
            }
            if (arg.isOr()) {
                List<Expr> argList = new ArrayList<Expr>();
                for (Expr e: arg.getArgs()) {
                    argList.add(pushNegIn(z3ctx.mkNot((BoolExpr)e)));
                }
                return z3ctx.mkAnd(argList.toArray(new BoolExpr[argList.size()]));
            }
            // Should be (not ...atomic...) here
            return z3ctx.mkNot((BoolExpr)pushNegIn(arg));
        }
        if (expr.isAnd()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(pushNegIn((BoolExpr)e));
            }
            return z3ctx.mkAnd(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (expr.isOr()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(pushNegIn((BoolExpr)e));
            }
            return z3ctx.mkOr(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (Region.isAtom(expr)) {
            return expr;
        }
        // Anything reaches here is not valid, generating an error using null
        return null;
    }

    // Convert NNF to DNF
    // Distribute /\ over \/
    // (f1 \/ f2) /\ f3 -> (f1 /\ f3) \/ (f2 /\ f3)
    public Expr convertNNFToDNF(Expr expr) {
        if (Region.isAtom(expr)) {
            return expr;
        }
        if (expr.isOr()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(convertNNFToDNF((BoolExpr)e));
            }
            return z3ctx.mkOr(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (expr.isAnd()) {
            List<Expr> orList = new ArrayList<Expr>();
            List<Expr> otherList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                // First convert all the subformulae to DNF format
                e = convertNNFToDNF(e);
                // Store "or" subformula and other (including "and" and "not") subformula into different list
                if (e.isOr()) {
                    orList.add(e);
                } else {
                    otherList.add(e);
                }
            }
            // All the other formula (other than "or") can be combined together
            Expr andExpr;
            if (otherList.size() == 0) {
                // Eliminate redundant (and ...) if there is no "other" expr
                andExpr = null;
            } else if (otherList.size() == 1) {
                // Eliminate redundant (and ...) if there is only one "other" expr
                andExpr = otherList.get(0);
            } else {
                andExpr = z3ctx.mkAnd(otherList.toArray(new BoolExpr[otherList.size()]));
            }
            if (orList.size() == 0) {
                // If there is no "or" subformula inside, then simply return all the other subformula
                return andExpr;
            }
            // distribute /\ over \/
            Expr result = z3ctx.mkFalse();
            for (Expr orExpr: orList) {
                result = distributeAnd(result, orExpr, andExpr);
            }
            return result;
        }
        // Anything reaches here is not valid, generating an error using null
        return null;
    }

    public List<Expr> getArgsListForOr(Expr expr) {
        // to find args that are connected by "or"
        List<Expr> argList = new ArrayList<Expr>();
        if (!expr.isOr()) {
            argList.add(expr);
            return argList;
        }
        for (Expr e : expr.getArgs()) {
            if (!e.isOr()) {
                argList.add(e);
            } else {
                List<Expr> innerArgList = new ArrayList<Expr>();
                innerArgList = getArgsListForOr(e);
                argList.addAll(innerArgList);
            }
        }
        return argList;
    }

    public Expr[] getArgsForOr(Expr expr) {
        List<Expr> argList = getArgsListForOr(expr);
        return argList.toArray(new Expr[argList.size()]);
    }

    // distribute and over or
    // assume or1.isOr() /\ or2.isOr()
    // assume or1 and or2 are connected by /\ in the original formula
    public Expr distributeAnd(Expr or1, Expr or2, Expr and) {
        Expr[] or1Args = getArgsForOr(or1);
        Expr[] or2Args = getArgsForOr(or2);
        Expr ret = z3ctx.mkFalse();
        // Anything "or" with False should node change the validity of that formula
        if (or1.isFalse()) {
            // Consider the case where or1 is False in the first iteration
            List<Expr> ors = new ArrayList<Expr>();
            for (Expr e2: or2Args) {
                if (and == null) {
                    // If "and" is null, then no need to conjuct "and" with subformula of "or"
                    // Simply return the original subformula to eliminate redundant (and ...)
                    ors.add(e2);
                } else {
                    ors.add(z3ctx.mkAnd((BoolExpr)e2, (BoolExpr)and));
                }
            }
            return z3ctx.mkOr(ors.toArray(new BoolExpr[ors.size()]));
        }
        if (and == null) {
            // If "and" is null, do not conjunct "and"
            for (Expr e1: or1Args) {
                for (Expr e2: or2Args) {
                    if (ret.isFalse()) {
                        // Consider the case where ret is False in the first iteration
                        ret = z3ctx.mkAnd((BoolExpr)e1, (BoolExpr)e2);
                    } else {
                        ret = z3ctx.mkOr((BoolExpr)ret, z3ctx.mkAnd((BoolExpr)e1, (BoolExpr)e2));
                    }
                }
            }
        } else {
            for (Expr e1: or1Args) {
                for (Expr e2: or2Args) {
                    if (ret.isFalse()) {
                        ret = z3ctx.mkAnd((BoolExpr)e1, (BoolExpr)e2, (BoolExpr)and);
                    } else {
                        ret = z3ctx.mkOr((BoolExpr)ret, z3ctx.mkAnd((BoolExpr)e1, (BoolExpr)e2, (BoolExpr)and));
                    }
                }
            }
        }
        return ret;
    }

    // Convert NNF to CNF
    // Distribute \/ over /\
    // (f1 /\ f2) \/ f3 -> (f1 \/ f3) /\ (f2 \/ f3)
    public Expr convertNNFToCNF(Expr expr) {
        if (Region.isAtom(expr)) {
            return expr;
        }
        if (expr.isAnd()) {
            List<Expr> argList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                argList.add(convertNNFToCNF((BoolExpr)e));
            }
            return z3ctx.mkAnd(argList.toArray(new BoolExpr[argList.size()]));
        }
        if (expr.isOr()) {
            List<Expr> andList = new ArrayList<Expr>();
            List<Expr> otherList = new ArrayList<Expr>();
            for (Expr e: expr.getArgs()) {
                // First convert all the subformulae to CNF format
                e = convertNNFToCNF(e);
                // Store "and" subformula and other (including "or" and "not") subformula into different list
                if (e.isAnd()) {
                    andList.add(e);
                } else {
                    otherList.add(e);
                }
            }
            // All the other formula (other than "and") can be combined together
            Expr orExpr;
            if (otherList.size() == 0) {
                // Eliminate redundant (or ...) if there is no "other" expr
                orExpr = null;
            } else if (otherList.size() == 1) {
                // Eliminate redundant (or ...) if there is only one "other" expr
                orExpr = otherList.get(0);
            } else {
                orExpr = z3ctx.mkOr(otherList.toArray(new BoolExpr[otherList.size()]));
            }
            if (andList.size() == 0) {
                // If there is no "and" subformula inside, then simply return all the other subformula
                return orExpr;
            }
            // distribute \/ over /\
            Expr result = z3ctx.mkTrue();
            for (Expr andExpr: andList) {
                result = distributeOr(result, andExpr, orExpr);
            }
            return result;
        }
        // Anything reaches here is not valid, generating an error using null
        return null;
    }

    public List<Expr> getArgsListForAnd(Expr expr) {
        // to find args that are connected by "and"
        List<Expr> argList = new ArrayList<Expr>();
        if (!expr.isAnd()) {
            argList.add(expr);
            return argList;
        }
        for (Expr e : expr.getArgs()) {
            if (!e.isAnd()) {
                argList.add(e);
            } else {
                List<Expr> innerArgList = new ArrayList<Expr>();
                innerArgList = getArgsListForAnd(e);
                argList.addAll(innerArgList);
            }
        }
        return argList;
    }

    public Expr[] getArgsForAnd(Expr expr) {
        List<Expr> argList = getArgsListForAnd(expr);
        return argList.toArray(new Expr[argList.size()]);
    }

    // distribute or over and
    // assume and1.isAnd() /\ and2.isAnd()
    // assume and1 and and2 are connected by \/ in the original formula
    public Expr distributeOr(Expr and1, Expr and2, Expr or) {
        Expr[] and1Args = getArgsForAnd(and1);
        Expr[] and2Args = getArgsForAnd(and2);
        Expr ret = z3ctx.mkTrue();
        // Anything "and" with True should node change the validity of that formula
        if (and1.isTrue()) {
            // Consider the case where and1 is True in the first iteration
            List<Expr> ands = new ArrayList<Expr>();
            for (Expr e2: and2Args) {
                if (or == null) {
                    // If "or" is null, then no need to disjuct "or" with subformula of "or"
                    // Simply return the original subformula to eliminate redundant (or ...)
                    ands.add(e2);
                } else {
                    ands.add(z3ctx.mkOr((BoolExpr)e2, (BoolExpr)or));
                }
            }
            return z3ctx.mkAnd(ands.toArray(new BoolExpr[ands.size()]));
        }
        if (or == null) {
            // If "or" is null, do not disjunct "or"
            for (Expr e1: and1Args) {
                for (Expr e2: and2Args) {
                    if (ret.isTrue()) {
                        // Consider the case where ret is True in the first iteration
                        ret = z3ctx.mkOr((BoolExpr)e1, (BoolExpr)e2);
                    } else {
                        ret = z3ctx.mkAnd((BoolExpr)ret, z3ctx.mkOr((BoolExpr)e1, (BoolExpr)e2));
                    }
                }
            }
        } else {
            for (Expr e1: and1Args) {
                for (Expr e2: and2Args) {
                    if (ret.isTrue()) {
                        ret = z3ctx.mkOr((BoolExpr)e1, (BoolExpr)e2, (BoolExpr)or);
                    } else {
                        ret = z3ctx.mkAnd((BoolExpr)ret, z3ctx.mkOr((BoolExpr)e1, (BoolExpr)e2, (BoolExpr)or));
                    }
                }
            }
        }
        return ret;
    }

    public Expr convertToNNF(Expr expr) {
        Expr ret = eliminateImpEq(expr);
        ret = pushNegIn(ret);
        return ret;
    }

    public Expr convertToDNF(Expr expr) {
        Expr ret = convertToNNF(expr);
        ret = convertNNFToDNF(ret);
        return ret;
    }

    public Expr convertToCNF(Expr expr) {
        Expr ret = convertToNNF(expr);
        ret = convertNNFToCNF(ret);
        return ret;
    }

}
