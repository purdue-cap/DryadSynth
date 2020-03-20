import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.microsoft.z3.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public abstract class ExternalSolver {
    public abstract String solveEncoded(String problem, long timeout) throws Exception;

    public volatile DefinedFunc[] results = null;
    public volatile boolean running = false;
    public boolean solve(SygusProblem problem, long timeout) {
        return false;
    }

    public static String encode(SygusProblem problem) {
        String encoded = "";
        encoded += "(set-logic LIA)\n"; // Header
        switch (problem.problemType) {
            case CLIA:
            encoded += encodeCLIA(problem);
            break;
            case INV:
            encoded += encodeINV(problem);
            break;
            case GENERAL:
            encoded += encodeGeneral(problem);
            break;
        }
        encoded += "(check-synth)\n"; // Footer
        return encoded;
    }

    public static String encodeSynthFun(SygusProblem problem) {
        String output = "";
        for (String funcName: problem.names) {
            String fmt = "(synth-fun %s %s %s)\n";
            String argList = "(";
            for (Expr arg: problem.requestArgs.get(funcName)) {
                argList += String.format("(%s %s) ", arg.toString(), arg.getSort().toString());
            }
            argList += ")";
            output += String.format(fmt, funcName, argList, problem.requests.get(funcName).getRange().toString());
        }
        return output;
    }

    public static String encodeSynthFunGeneral(SygusProblem problem) {
        String output = "";
        for (String funcName: problem.names) {
            String fmt = "(synth-fun %s %s %s %s\n)\n";
            String argList = "(";
            for (Expr arg: problem.requestArgs.get(funcName)) {
                argList += String.format("(%s %s) ", arg.toString(), arg.getSort().toString());
            }
            argList += ")";

            SygusProblem.CFG cfg = problem.cfgs.get(funcName);
            String CFGList = "\n  (";
            for (String ruleName: cfg.grammarRules.keySet()) {
                List<String[]> rules = cfg.grammarRules.get(ruleName);
                Sort ruleSort = cfg.grammarSybSort.get(ruleName);
                String ruleFmt = "(%s %s %s  )\n  ";

                String ruleList = "(\n";
                for (String[] rule: rules) {
                    if (rule.length == 1) {
                        ruleList += "    "+ rule[0] + "\n";
                    } else {
                        ruleList += "    (";
                        for (String ruleArg : rule) {
                            ruleList += ruleArg + " ";
                        }
                        ruleList += ")\n";
                    }
                }
                ruleList += "    )\n";
                CFGList += String.format(ruleFmt, ruleName, ruleSort.toString(), ruleList);
            }
            CFGList += ")";


            output += String.format(fmt,
                funcName, argList, problem.requests.get(funcName).getRange().toString(), CFGList);
        }
        return output;
    }

    public static String encodeSynthInv(SygusProblem problem) {
        String output = "";
        for (String funcName: problem.names) {
            if (!problem.requests.get(funcName).getRange().equals(problem.ctx.getBoolSort())) {
                return null;
            }
            String fmt = "(synth-inv %s %s)\n";
            String argList = "(";
            for (Expr arg: problem.requestArgs.get(funcName)) {
                argList += String.format("(%s %s) ", arg.toString(), arg.getSort().toString());
            }
            argList += ")";
            output += String.format(fmt, funcName, argList);
        }
        return output;

    }

    public static String encodeVarDecl(SygusProblem problem) {
        String output = "";
        for (String varName: problem.regularVars.keySet()) {
            if (problem.vars.containsKey(varName + "!")) {
                String fmt = "(declare-primed-var %s %s)\n";
                output += String.format(fmt, varName, problem.regularVars.get(varName).getSort().toString());
            } else {
                String fmt = "(declare-var %s %s)\n";
                output += String.format(fmt, varName, problem.regularVars.get(varName).getSort().toString());
            }
        }
        return output;
    }

    public static String encodeFunDef(SygusProblem problem) {
        String output = "";
        for (DefinedFunc func: problem.funcs.values()) {
            output += func.toString() + "\n";
        }
        return output;
    }

    public static String encodeConstraints(SygusProblem problem) {
        String output = "";
        Context ctxPrint = new Context();
        ctxPrint.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
        for (BoolExpr constraint: problem.constraints) {
            String fmt = "(constraint %s)\n";
            Expr printExpr = constraint.translate(ctxPrint);
            output += String.format(fmt, printExpr.toString());
        }
        return output;
    }

    public static String encodeInvConstraints(SygusProblem problem) {
        String output = "";
        for (String invName: problem.invConstraints.keySet()) {
            String fmt = "(inv-constraint %s %s %s %s)\n";
            DefinedFunc[] invFunc = problem.invConstraints.get(invName);
            output += String.format(fmt, invName, invFunc[0].getName(), invFunc[1].getName(), invFunc[2].getName());
        }
        return output;
    }

    static String encodeCLIA(SygusProblem problem) {
        return encodeSynthFun(problem) + 
            encodeVarDecl(problem) +
            encodeFunDef(problem) +
            encodeConstraints(problem);
    }

    static String encodeINV(SygusProblem problem) {
        return encodeSynthInv(problem) +
            encodeVarDecl(problem) +
            encodeFunDef(problem) +
            encodeInvConstraints(problem);
    }

    static String encodeGeneral(SygusProblem problem) {
        return encodeFunDef(problem) + 
            encodeSynthFunGeneral(problem) +
            encodeVarDecl(problem) +
            encodeConstraints(problem);
    }

    public static DefinedFunc[] decode(SygusProblem problem, String results) {

        ANTLRInputStream resultStream = new ANTLRInputStream(results);
        SygusLexer lexer = new SygusLexer(resultStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SygusParser parser = new SygusParser(tokens);

        ANTLRErrorStrategy es = new CustomErrorStrategy();
        parser.setErrorHandler(es);

        ParseTree tree;
		try{
			tree = parser.start();
		} catch(Exception ex) {
			return null;
        }

		ParseTreeWalker walker = new ParseTreeWalker();
		ResultParser resultParser = new ResultParser(problem.ctx, problem.opDis);
        walker.walk(resultParser, tree);

        return resultParser.results;
    }

}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}