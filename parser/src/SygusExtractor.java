import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class SygusExtractor extends SygusBaseListener {
    enum CmdType {
        SYNTHFUNC, FUNCDEF, CONSTRAIN, NONE
    }
    CmdType currentCmd;
    boolean currentOnArgList = false;

    public class SynthRequest {
        public String name;
        public SortedMap<String,String> arguments = new TreeMap<String,String>();
        public String returnType;
    }
    public List<SynthRequest> requests = new ArrayList<SynthRequest>();
    SynthRequest currentRequest;

    public void enterSynthFunCmd(SygusParser.SynthFunCmdContext ctx) {
        currentCmd = CmdType.SYNTHFUNC;
        currentRequest = new SynthRequest();
        currentRequest.name = ctx.symbol().getText();
        currentRequest.returnType = ctx.sortExpr().getText();
    }

    public void exitSynthFunCmd(SygusParser.SynthFunCmdContext ctx) {
        requests.add(currentRequest);
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
            String name = ctx.symbol().getText();
            String type = ctx.sortExpr().getText();
            currentRequest.arguments.put(name, type);
        }
    }
}
