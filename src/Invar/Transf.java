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

    public static void getDelta(Expr nonCond, Map<String, Expr> vars, Map<Expr, Integer> deltas) {
        Integer delta = null;
        Expr variable = null;

        // assume nonCond only have the form x' = x + c
        if(nonCond.isEq()) {
            Expr[] args;
            args = nonCond.getArgs();
            for (Expr arg : args) {
                if (!arg.isConst()) {
                    boolean add = true;

                    System.out.println("Add: " + arg);
                    System.out.println();

                    if (arg.isAdd()) {
                        add = true;
                    } else if (arg.isSub()) {
                        add = false;
                    } else {
                        System.out.println("Deltas Extraction Failed!");
                    }
                    Expr[] subargs = arg.getArgs();
                    for (Expr subarg : subargs) {
                        if (vars.containsValue(subarg)) {
                            variable = subarg;
                            System.out.println("var: " + variable);
                            System.out.println();
                        } else {
                            delta = Integer.valueOf(subarg.toString());
                            System.out.println("delta: " + delta);
                            System.out.println();
                        }
                    }
                    if (add) {
                    deltas.put(variable, delta);
                    } else {
                        deltas.put(variable, (-delta));
                    }
                }
            }
        } else {
            System.out.println("Deltas Extraction Failed!");
        }
    }

    public static Transf fromTransfFormula(Expr formula, Map<String, Expr> vars, Context ctx) {
        // NOT IMPLEMENTED
        // We assume that the input format is DNF at this point
        // Later we should use convertToDNF to do the conversion
        Transf t = new Transf(vars, ctx);

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
            int size = vars.size();
            int[] deltas = new int[size];
            Map<Expr, Integer> delta_map = new LinkedHashMap<Expr, Integer>();

            int i = 0;
            for (Expr var : vars.values()) {
                if (delta_map.containsKey(var)) {
                    deltas[i] = delta_map.get(var);
                }
                i = i + 1;
            }

            for (Region region : regions) {
                t.addMap(region, deltas);
            }
        }
        return t;
    }

    // Reimplementation of converting any formula to DNF format
    // The version in SinInv uses single invocation assumption
    // Should be modified
    // Currently not tested and not used yet
    public List<Expr> convertToDNF(Expr expr) {
        // Check for (not (not ...)), (not (and ...)), (not (or ...)) forms 
        // before anything else
        if (expr.isNot()) {
            Expr arg = expr.getArgs()[0];
            if (arg.isNot()) {
                return convertToDNF(arg.getArgs()[0]);
            }
            if (arg.isAnd()) {
                List<Expr> argList = new ArrayList<Expr>();
                for (Expr e: arg.getArgs()) {
                    argList.add(z3ctx.mkNot((BoolExpr)e));
                }
                return convertToDNF(z3ctx.mkOr(argList.toArray(new BoolExpr[argList.size()])));
            }
            if (arg.isOr()) {
                List<Expr> argList = new ArrayList<Expr>();
                for (Expr e: arg.getArgs()) {
                    argList.add(z3ctx.mkNot((BoolExpr)e));
                }
                return convertToDNF(z3ctx.mkAnd(argList.toArray(new BoolExpr[argList.size()])));
            }
        }
        // Check for atomic formulas
        if (Region.isAtom(expr)) {
            List<Expr> list = new ArrayList<Expr>();
            list.add(expr);
            return list;
        }
        // Check for any (not ...) expressions that are neither atomic nor
        // (not or) (not and), should be passed through and passed back
        if (expr.isNot()) {
            List<Expr> innerDNF = convertToDNF(expr.getArgs()[0]);
            return convertToDNF(z3ctx.mkNot(
                        z3ctx.mkOr(innerDNF.toArray(new BoolExpr[innerDNF.size()]))
                        ));
        }
        // (or ..) could be combined directly
        if (expr.isOr()) {
            List<Expr> list = new ArrayList<Expr>();
            for (Expr atom: expr.getArgs()) {
                list.addAll(convertToDNF(atom));
            }
            return list;
        }
        // (and ..) should be combined distributively
        if (expr.isAnd()) {
            List<List<Expr>> list = new ArrayList<List<Expr>>();
            for (Expr atom: expr.getArgs()) {
                list.add(convertToDNF(atom));
            }
            return combineDNF(list);
        }
        // Implies should be converted to basic logical format
        if (expr.isImplies()) {
            Expr[] args = expr.getArgs();
            return convertToDNF(z3ctx.mkOr(z3ctx.mkNot((BoolExpr)args[0]), (BoolExpr)args[1]));
        }
        // ITEs should be converted to basic logical format
        if (expr.isITE()) {
            Expr[] args = expr.getArgs();
            return convertToDNF(z3ctx.mkAnd(z3ctx.mkOr(z3ctx.mkNot((BoolExpr)args[0]), (BoolExpr)args[1]), z3ctx.mkOr((BoolExpr)args[0], (BoolExpr)args[2])));
        }
        // Anything reaches here is not valid, generating an error using null
        return null;
    }
    // Combining DNFs connected using (and ...) distributively
    public List<Expr> combineDNF(List<List<Expr>> DNFs) {
        List<Expr> result = new ArrayList<Expr>();
        for (List<Expr> dnf: DNFs) {
            result = combine2DNF(result, dnf);
        }
        return result;
    }
    // Combining (and DNF DNF)
    public List<Expr> combine2DNF(List<Expr>DNF1, List<Expr>DNF2) {
        List<Expr> result = new ArrayList<Expr>();
        for (Expr term1: DNF1) {
            for (Expr term2: DNF2) {
                result.add(joinConj(term1, term2));
            }
        }
        return result;
    }
    // Join 2 conjuctions into 1 conjunction
    public Expr joinConj(Expr conj1, Expr conj2) {
        List<Expr> terms = new ArrayList<Expr>();
        terms.addAll(Arrays.asList(conj1));
        terms.addAll(Arrays.asList(conj2));
        return z3ctx.mkAnd(terms.toArray(new BoolExpr[terms.size()]));
    }
}
