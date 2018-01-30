import java.util.*;
import com.microsoft.z3.*;

public class Region {
    private List<Cond> conds = new ArrayList<Cond>();
    // List to store if the corresponding condition shall be k-extended
    private List<Boolean> kExtended = new ArrayList<Boolean>();
    private Transf.uniTrans trans = null;
    static private Map<String, Expr> vars;
    static private Context z3ctx;


    public class Cond {
        public Map<Expr, Integer> coeffTbl = new LinkedHashMap<Expr, Integer>();
        public int constTerm;

        public Expr toExpr() {
            List<Expr> terms = new ArrayList<Expr>();
            for (Expr e: coeffTbl.keySet()) {
                terms.add(z3ctx.mkMul(z3ctx.mkInt(coeffTbl.get(e)), (ArithExpr)e));
            }
            terms.add(z3ctx.mkInt(constTerm));
            return z3ctx.mkGe(z3ctx.mkAdd(
                            terms.toArray(new ArithExpr[terms.size()])
                            ), z3ctx.mkInt(0));
        }

        public Expr toKExpr(Expr k) {
            Expr expr = this.toExpr();
            for (Expr var: coeffTbl.keySet()) {
                expr = expr.substitute(
                        var,
                        z3ctx.mkSub((ArithExpr)var, z3ctx.mkMul((ArithExpr)k, z3ctx.mkInt(trans.deltaTbl.get(var))))
                        );
            }
            return expr;
        }
    }

    public Expr toExpr() {
        List<Expr> clauses = new ArrayList<Expr>();
        for (Cond c : conds) {
            clauses.add(c.toExpr());
        }
        return z3ctx.mkAnd(clauses.toArray(new BoolExpr[clauses.size()]));
    }

    public Expr toKExpr(Expr k) {
        List<Expr> clauses = new ArrayList<Expr>();
        int i = 0;
        for (Cond c : conds) {
            if (this.kExtended.get(i)) {
                clauses.add(c.toKExpr(k));
            } else {
                clauses.add(c.toExpr());
            }
            ++i;
        }
        return z3ctx.mkAnd(clauses.toArray(new BoolExpr[clauses.size()]));
    }

    public void kExtend(Transf.uniTrans t) {
        this.trans = t;
        this.kExtended.clear();
        for (Cond c : conds) {
            int deltaSum = 0;
            for (Expr var: c.coeffTbl.keySet()) {
                deltaSum = deltaSum + c.coeffTbl.get(var) * t.deltaTbl.get(var);
            }
            if (deltaSum > 0) {
                this.kExtended.add(true);
            } else {
                this.kExtended.add(false);
            }
        }
    }

    public String toString() {
        return this.toExpr().simplify().toString();
    }

    public Region(Map<String, Expr> vars, Context ctx) {
        this.vars = vars;
        this.z3ctx = ctx;
    }

    public void addCond(int[] coeff, int constTerm) {
        // Input format must match
        if ( coeff.length != vars.size() ) {
            return;
        }

        Cond newC = new Cond();

        int i = 0;
        for (Expr e: vars.values()) {
            newC.coeffTbl.put(e, coeff[i]);
            i++;
        }
        newC.constTerm = constTerm;

        this.conds.add(newC);
    }

    public static boolean isAtom(Expr expr) {
        if (expr.isNot()) {
            return isAtom(expr.getArgs()[0]);
        }
        if (expr.isAnd() || expr.isOr() || expr.isImplies() || expr.isITE() ) {
            return false;
        }
        return true;
    }

    public static boolean hasPrime(Expr input) {
        Queue<Expr> todo = new LinkedList<Expr>();
        todo.add(input);
        while (!todo.isEmpty()) {
            Expr expr = todo.remove();
            if (expr.isConst()) {
                if (expr.toString().endsWith("!")){
                    return true;
                }
            }
            if (expr.isApp()) {
                Expr[] args = expr.getArgs();
                for (Expr arg: args) {
                    todo.add(arg);
                }
            }
        }
        return false;
    }

    public static void eliminate_negation(Expr cond, List<Expr> nnCondList, Context ctx) {

        if (cond.isConst()) {
            return;
        }
        if (cond.isApp()) {
            Expr[] args = cond.getArgs();
            if (cond.isNot()) {
                Expr inner = args[0];
                Expr[] innerArgs = inner.getArgs();
                if (inner.isNot()) {
                    eliminate_negation(inner, nnCondList, ctx);
                }
                if (inner.isGE()) {
                    nnCondList.add(ctx.mkLt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                }
                if (inner.isGT()) {
                    nnCondList.add(ctx.mkLe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                }
                if (inner.isLT()) {
                    nnCondList.add(ctx.mkGe((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                }
                if (inner.isLE()) {
                    nnCondList.add(ctx.mkGt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                }
                if (inner.isEq()) {
                    nnCondList.add(ctx.mkLt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                    nnCondList.add(ctx.mkGt((ArithExpr)innerArgs[0], (ArithExpr)innerArgs[1]));
                }
            } else {
                nnCondList.add(cond);
            }
        }

    }

    public static Expr getCoeff(Expr expr, Expr[] from, int index, int size) {
        Expr[] to = new Expr[size];
        for (int i = 0; i < size; i++) {
            if (i == index) {
                to[i] = z3ctx.mkInt(1);
            } else {
                to[i] = z3ctx.mkInt(0);
            }
        }
        Expr ret = expr.substitute(from, to).simplify();
        return ret;
    }

    public static Expr getConstant(Expr expr, Expr[] from, int size) {
        return getCoeff(expr, from, size, size);
    }

    public static Expr[] getCoeffArray(Expr expr, Expr[] from, Expr constant) {
        int size = from.length;
        Expr[] coeff = new Expr[size];
        //Expr constant = getConstant(expr, from, size);
        for (int i = 0; i < size; i++) {
            coeff[i] = getCoeff(expr, from, i, size);
            coeff[i] = z3ctx.mkSub((ArithExpr)coeff[i], (ArithExpr)constant).simplify();
        }
        return coeff;
    }

    public static Expr[] getVarsArray() {
        int size = vars.size();
        Expr[] varArray = new Expr[size];
        int i = 0;
        for (Expr var : vars.values()) {
            varArray[i] = var;
            i = i + 1;
        }
        return varArray;
    }

    public static void normalize(Expr nnCond, Context ctx, Map<Expr[], Expr> region_map) {
        if (nnCond.isConst()) {
            return;
        }
        if (nnCond.isApp()) {
            Expr[] args = nnCond.getArgs();
            Expr[] from = getVarsArray();
            int size = vars.size();
            Expr constant_l = getConstant(args[0], from, size);
            Expr constant_r = getConstant(args[1], from, size);
            Expr[] argsCoeff_l = getCoeffArray(args[0], from, constant_l);
            Expr[] argsCoeff_r = getCoeffArray(args[1], from, constant_r);
            Expr[] coeff_normal = new Expr[size];
            Expr constant_normal = null;

            if (nnCond.isGE()) {
                for (int i = 0; i < size; i++) {
                    coeff_normal[i] = ctx.mkSub((ArithExpr)argsCoeff_l[i], (ArithExpr)argsCoeff_r[i]).simplify();
                }
                constant_normal = ctx.mkSub((ArithExpr)constant_l, (ArithExpr)constant_r).simplify();
            }
            if (nnCond.isGT()) {
                for (int i = 0; i < size; i++) {
                    coeff_normal[i] = ctx.mkSub((ArithExpr)argsCoeff_l[i], (ArithExpr)argsCoeff_r[i]).simplify();
                }
                constant_normal = ctx.mkSub(ctx.mkSub((ArithExpr)constant_l, (ArithExpr)constant_r), ctx.mkInt(1)).simplify();
            }
            if (nnCond.isLE()) {
                for (int i = 0; i < size; i++) {
                    coeff_normal[i] = ctx.mkSub((ArithExpr)argsCoeff_r[i], (ArithExpr)argsCoeff_l[i]).simplify();
                }
                constant_normal = ctx.mkSub((ArithExpr)constant_r, (ArithExpr)constant_l).simplify();
            }
            if (nnCond.isLT()) {
                for (int i = 0; i < size; i++) {
                    coeff_normal[i] = ctx.mkSub((ArithExpr)argsCoeff_r[i], (ArithExpr)argsCoeff_l[i]).simplify();
                }
                constant_normal = ctx.mkSub(ctx.mkSub((ArithExpr)constant_r, (ArithExpr)constant_l), ctx.mkInt(1)).simplify();
            }
            if (nnCond.isEq()) {

                for (int i = 0; i < size; i++) {
                    coeff_normal[i] = ctx.mkSub((ArithExpr)argsCoeff_l[i], (ArithExpr)argsCoeff_r[i]).simplify();
                }
                constant_normal = ctx.mkSub((ArithExpr)constant_l, (ArithExpr)constant_r).simplify();
                    
            }
            region_map.put(coeff_normal, constant_normal);
        }

    }

    public static int convertExprToInt(Expr expr) {
        String str = expr.toString();
        return Integer.parseInt(str);
    }

    public static Region[] fromConj(Expr conj, List<Expr> nonConds, Context ctx) {
        // NOT IMPLEMENTED
        // This function should implement a mechanism to convert conjuctions
        // of conditions to regions, by do essential splitting and parsing 
        // of expressions and then call addCond
        // non-conditions (transfer formulas) in the conjunctions would be
        // added to the nonConds list.
        List<Expr> condList = new ArrayList<Expr>();
        Queue<Expr> todo = new LinkedList<Expr>();
        todo.add(conj);
        while (!todo.isEmpty()) {
            Expr expr = todo.remove();
            if (expr.isAnd()) {
                Expr[] args = expr.getArgs();
                for (Expr arg: args) {
                    todo.add(arg);
                }
                continue;
            }
            if (isAtom(expr)) {
                if (hasPrime(expr)) {
                    nonConds.add(expr);
                } else {
                    condList.add(expr);
                }
            } else {
                return null;
            }
        }

        // Do splitting of Eqs and NEqs here
        // Consider implementing this using a Z3 tactics
        List<Expr> nnCondList = new ArrayList<Expr>();
        for (Expr c : condList) {
            eliminate_negation(c, nnCondList, ctx);
        }

        // Do parsing of Exprs here
        Map<Expr[], Expr> region_map = new LinkedHashMap<Expr[], Expr>();
        for (Expr nnCond : nnCondList) {
            normalize(nnCond, ctx, region_map);
        }

        // Then add the conditions to the returning Region array 
        int num_region = nnCondList.size();
        Region[] region_array = new Region[num_region];

        int j = 0;
        for (Map.Entry<Expr[], Expr> entry : region_map.entrySet()) {
            Expr[] coeff = entry.getKey();
            Expr constnt = entry.getValue();
            int len = coeff.length;
            int[] coefficient= new int[len];
            int constant;
            for (int i = 0; i < coeff.length; i++) {
                coefficient[i] = convertExprToInt(coeff[i]);
            }
            constant = convertExprToInt(constnt);
            region_array[j] = new Region(vars, ctx);
            region_array[j].addCond(coefficient, constant);
            j = j + 1;
        }


        return region_array;
    }
}
