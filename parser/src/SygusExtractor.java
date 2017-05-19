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
        SYNTHFUNC, FUNCDEF, CONSTRAIN, NONE
    }
    CmdType currentCmd;
    boolean currentOnArgList = false;

    public SortedMap<String, FuncDecl> requests = new TreeMap<String, FuncDecl>();
    List<Sort> currentArgList;

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
}
