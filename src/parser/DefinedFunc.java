import java.util.*;
import com.microsoft.z3.*;

public class DefinedFunc {

    DefinedFunc (Context ctx, String name, Expr[] args, Expr definition, ASTGeneral ASTDef) {
        this.ctx = ctx;
        this.name = name;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
        declareFunc();
        this.ASTDef = ASTDef;
    }

    DefinedFunc(Context ctx, String name, Expr[] args, Expr definition) {
        this.ctx = ctx;
        this.name = name;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
        declareFunc();
    }

    DefinedFunc(Context ctx, Expr[] args, Expr definition) {
        this.ctx = ctx;
        this.name = null;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
    }

    void declareFunc() {
        Sort[] sorts = new Sort[args.length];
        for (int i = 0; i < sorts.length; i++) {
            sorts[i] = args[i].getSort();
        }
        Sort rtn = definition.getSort();
        this.decl = ctx.mkFuncDecl(name, sorts, rtn);
    }

    Context ctx;
    String name;
    Expr [] args;
    Expr definition;
    int numArgs;
    FuncDecl decl;
    ASTGeneral ASTDef = null;

    public Expr apply(Expr... argList){
        return definition.substitute(args, argList);
    }

    public Expr applyUninterp(Expr... argList) {
        return decl.apply(argList);
    }

    public DefinedFunc translate(Context ctx) {
        if (this.ctx == ctx) {
            return this;
        }
        Expr[] newArgs = new Expr[this.args.length];
        for(int i = 0; i < this.args.length; i++) {
            newArgs[i] = this.args[i].translate(ctx);
        }
        DefinedFunc df = new DefinedFunc(ctx, this.name, newArgs, this.definition.translate(ctx));
        return df;
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
            if (expr.isConst()) {
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
            } else if(expr.isQuantifier()) {
                body = ((Quantifier)expr).getBody();
                if (cache.containsKey(body)) {
                    todo.pop();
                    newExpr = expr.update(new Expr[]{ cache.get(body) });
                    cache.put(expr, newExpr);
                } else {
                    todo.push(body);
                }
            } else {
                todo.pop();
                cache.put(expr, expr);
            }
        }
        return cache.get(orig);
    }

    public final String getName() {
        return name;
    }

    public final Expr[] getArgs () {
        return args;
    }

    public final Expr getDef () {
        return definition;
    }

    public final FuncDecl getDecl() {
        return decl;
    }

    // Return a new DefinedFunc with replaced argument list
    public DefinedFunc replaceArgs(Expr[] newArgs) {
        return new DefinedFunc(this.ctx, this.name, newArgs, this.definition);
    }

    // Return a new DefinedFunc with replaced name
    public DefinedFunc replaceName(String newName) {
        return new DefinedFunc(this.ctx, newName, this.args, this.definition);
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean simplify) {
        String str = "(define-fun %s (%s) %s %s)";
        String argStr = "";
        for (Expr expr : args) {
            argStr = argStr + String.format("(%s %s) ", expr.toString(), expr.getSort().toString());
        }
        String typeStr = definition.getSort().toString();
        String def;
        if (ASTDef == null) {
            if (simplify) {
                def = definition.simplify().toString();
            } else {
                def = definition.toString();
            }
        } else {
            def = ASTDef.toString();
        }
        return String.format(str, name, argStr, typeStr, def);
    }

    public int getNumArgs() {
        return numArgs;
    }
}
