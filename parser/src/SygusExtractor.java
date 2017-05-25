import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.microsoft.z3.*;

public class SygusExtractor extends SygusBaseListener {
    Context z3ctx;
    SygusExtractor(Context initctx) {
        z3ctx = initctx;
    }

    enum CmdType {
        SYNTHFUNC, FUNCDEF, CONSTRAINT, DECLVAR, NONE
    }
    CmdType currentCmd = CmdType.NONE;
    boolean currentOnArgList = false;

    public SortedMap<String, FuncDecl> requests = new TreeMap<String, FuncDecl>();
    List<Sort> currentArgList;

    public SortedMap<String, Expr> vars = new TreeMap<String, Expr>();
    public List<Expr> constraints = new ArrayList<Expr>();
    Stack<Object> termStack = new Stack<Object>();

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

    public void enterSynthFunCmd(SygusParser.SynthFunCmdContext ctx) {
        currentCmd = CmdType.SYNTHFUNC;
        currentArgList = new ArrayList<Sort>();
    }

    public void exitSynthFunCmd(SygusParser.SynthFunCmdContext ctx) {
        String name = ctx.symbol().getText();
        Sort[] argList = currentArgList.toArray(new Sort[0]);
        Sort returnType = strToSort(ctx.sortExpr().getText());
        FuncDecl func = z3ctx.mkFuncDecl(name, argList, returnType);
        requests.put(name, func);
        currentCmd = CmdType.NONE;
    }

    public void enterArgList(SygusParser.ArgListContext ctx) {
        currentOnArgList = true;
    }

    public void exitArgList(SygusParser.ArgListContext ctx) {
        currentOnArgList = false;
    }

    public void enterSymbolSortPair(SygusParser.SymbolSortPairContext ctx) {
        if (currentCmd == CmdType.SYNTHFUNC && currentOnArgList) {
            Sort type = strToSort(ctx.sortExpr().getText());
            currentArgList.add(type);
        }
    }

    public void enterVarDeclCmd(SygusParser.VarDeclCmdContext ctx) {
        currentCmd = CmdType.DECLVAR;
    }

    public void exitVarDeclCmd(SygusParser.VarDeclCmdContext ctx) {
        String name = ctx.symbol().getText();
        Sort type = strToSort(ctx.sortExpr().getText());
        Expr var = z3ctx.mkConst(name, type);
        vars.put(name, var);
        currentCmd = CmdType.NONE;
    }

    public void enterConstraintCmd(SygusParser.ConstraintCmdContext ctx) {
        currentCmd = CmdType.CONSTRAINT;
    }

    public void exitConstraintCmd(SygusParser.ConstraintCmdContext ctx) {
        constraints.add((Expr)termStack.pop());
        currentCmd = CmdType.NONE;
    }

    public void enterFunDefCmd(SygusParser.FunDefCmdContext ctx){
        currentCmd = CmdType.FUNCDEF;
    }

    public void exitFunDefCmd(SygusParser.FunDefCmdContext ctx){
        currentCmd = CmdType.NONE;
    }

    public void enterTerm(SygusParser.TermContext ctx) {
        if (currentCmd == CmdType.CONSTRAINT) {
            int numChildren = ctx.getChildCount();
            if (numChildren == 1) {
                if (ctx.symbol() != null) {
                    termStack.push(vars.get(ctx.symbol().getText()));
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
        if (currentCmd == CmdType.CONSTRAINT) {
            if (ctx.getChildCount()!= 1) {
                List<Expr> args = new ArrayList<Expr>();
                Object top = termStack.pop();
                while (top != ctx) {
                    args.add(0, (Expr)top);
                    top = termStack.pop();
                }
                String name = ctx.symbol().getText();
                Expr res = operationDispatcher(name, args.toArray(new Expr[args.size()]));
                // Debugging point
                // System.out.println(name);
                // System.out.println(args);
                // System.out.println(res);
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
        FuncDecl f = requests.get(name);
        if (f != null) {
            return z3ctx.mkApp(f, args);
        }
        return null;
    }
}
