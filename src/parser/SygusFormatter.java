import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.microsoft.z3.*;

public class SygusFormatter extends SygusBaseVisitor<String> {
    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        //System.out.println(aggregate + "," + nextResult);
        if (nextResult == "" || nextResult == " ") {
            return aggregate;
        }
        if (aggregate == "" || aggregate == " ") {
            return nextResult;
        }
        if (aggregate.endsWith(" ") ||
            nextResult.startsWith(" ") ||
            aggregate.endsWith("(") ||
            nextResult.startsWith(")")) {
            return aggregate + nextResult;
        }
        return aggregate + " " + nextResult;
    }

    @Override
    protected String defaultResult() { return ""; }

    @Override
    public String visitTerminal(TerminalNode node) {
        return node.getText();
    }

    @Override
    public String visitTerm(SygusParser.TermContext ctx) {
        if (ctx.symbol() != null && ctx.symbol().getText().equals("-") &&
            ctx.termStar().termStar().getChildCount() == 0 &&
            ctx.termStar().term().literal() != null) {
            return "-" + ctx.termStar().term().literal().getText();
        }
        return this.visitChildren(ctx);
    }

    public static Expr elimITE(Context ctx, Expr orig) {
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(orig);
        Map<Expr, Expr> cache = new HashMap<Expr, Expr>();

        boolean visited;
        Expr expr, newExpr, body;
        Expr [] args, newArgsArray;
        List<Expr> newArgs = new ArrayList<Expr>();
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
                    newArgsArray = newArgs.toArray(new Expr[newArgs.size()]);
                    if (expr.isITE()) {
                        BoolExpr condTerm = (BoolExpr)newArgsArray[0];
                        BoolExpr thenTerm = (BoolExpr)newArgsArray[1];
                        BoolExpr elseTerm = (BoolExpr)newArgsArray[2];
                        newExpr = ctx.mkOr(ctx.mkAnd(condTerm, thenTerm),
                                            ctx.mkAnd(ctx.mkNot(condTerm), elseTerm));
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
}
