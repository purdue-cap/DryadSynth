import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

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
}
