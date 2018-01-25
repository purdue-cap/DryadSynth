import java.util.*;
import com.microsoft.z3.*;

public class Region {
    private List<Cond> conds = new ArrayList<Cond>();
    // List to store if the corresponding condition shall be k-extended
    private List<Boolean> kExtended = new ArrayList<Boolean>();
    private Transf.uniTrans trans = null;
    private Map<String, Expr> vars;
    private Context z3ctx;


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
        
        // Do parsing of Exprs here
        
        // Then add the conditions to the returning Region array 
        return null;
    }
}
