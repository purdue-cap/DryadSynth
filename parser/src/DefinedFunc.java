import java.util.*;
import com.microsoft.z3.*;

public class DefinedFunc {

    DefinedFunc(Context ctx, String name, Expr[] args, Expr definition) {
        this.ctx = ctx;
        this.name = name;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
    }

    DefinedFunc(Context ctx, Expr[] args, Expr definition) {
        this.ctx = ctx;
        this.name = null;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
    }

    Context ctx;
    String name;
    Expr [] args;
    Expr definition;
    int numArgs;

    public Expr apply(Expr... argList){
        return definition.substitute(args, argList);
    }

    public Expr rewrite(Expr orig, FuncDecl func) {
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(orig);
        Map<Expr, Expr> cache = new HashMap<Expr, Expr>();

        boolean visited;
        Expr expr, newExpr, body;
        Expr [] args, newArgsArray;
        List<Expr> newArgs = new ArrayList<Expr>();
        FuncDecl exprFunc;
        while (!todo.empty()) {
            expr = todo.peek();
            if (expr.isVar()) {
                todo.pop();
                cache.put(expr, expr);
            } else if (expr.isApp()) {
                visited = true;
                newArgs.clear();
                args = expr.getArgs();
                for (Expr arg: args) {
                    if (!cache.containsKey(arg)) {
                        todo.push(arg);
                        visited = false;
                    } else {
                        newArgs.add(cache.get(arg));
                    }
                }
                if (visited) {
                    todo.pop();
                    exprFunc = expr.getFuncDecl();
                    newArgsArray = newArgs.toArray(new Expr[newArgs.size()]);
                    if (exprFunc.equals(func)) {
                        newExpr = this.apply(newArgsArray);
                    } else {
                        newExpr = expr.update(newArgsArray);
                    }
                    cache.put(expr, newExpr);
                }
            } else {
                assert expr.isQuantifier();
                body = ((Quantifier)expr).getBody();
                if (cache.containsKey(body)) {
                    todo.pop();
                    newExpr = expr.update(new Expr[]{ cache.get(body) });
                    cache.put(expr, newExpr);
                } else {
                    todo.push(body);
                }
            }
        }
        return cache.get(orig);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        List<String> argstr = new ArrayList<String>();
        for (Expr expr : args) {
            argstr.add(expr.toString());
        }
        return argstr.toString() + " -> " + definition.toString();
    }

    public int getNumArgs() {
        return numArgs;
    }
}
