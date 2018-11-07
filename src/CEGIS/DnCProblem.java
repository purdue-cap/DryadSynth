import java.util.*;
import com.microsoft.z3.*;

public class DnCProblem extends SygusProblem {
    DnCProblem(SygusProblem pblm) {
        super(pblm);
    }
    DnCProblem(DnCProblem pblm) {
        super(pblm);
    }
    public DnCProblem createSubProblem(Expr subExpr, Expr[] subArgs) {
        DnCProblem subPblm = new DnCProblem(this);
        subPblm.names.clear();
        subPblm.requests.clear();
        subPblm.requestArgs.clear();
        subPblm.constraints.clear(); // Assume only constraint now

        String oldName = this.names.get(0);
        FuncDecl oldDecl = this.requests.get(oldName);
        FuncDecl newDecl = ctx.mkFreshFuncDecl("DnC", oldDecl.getDomain(), oldDecl.getRange());
        BoolExpr newSpec = ctx.mkEq(newDecl.apply(subArgs), subExpr);
        String name = newDecl.getName().toString();
        subPblm.names.add(name);
        subPblm.requests.put(name, oldDecl);
        subPblm.requestArgs.put(name, this.requestArgs.get(oldName));
        subPblm.constraints.add(newSpec);

        subPblm.rdcdRequests = subPblm.requests;
        subPblm.requestUsedArgs = subPblm.requestArgs;
        subPblm.requestSyntaxUsedArgs = subPblm.requestArgs;
        subPblm.combinedConstraint = newSpec;
        subPblm.finalConstraint = newSpec;
        return subPblm;
    }

    public void addSubSolution(DefinedFunc subSolution) {
        String name = subSolution.getName();
        String targetName = this.names.get(0);

        this.funcs.put(name, subSolution);
        this.opDis = new OpDispatcher(this.ctx, this.requests, this.funcs);

        this.glbSybTypeTbl.put(name, SygusProblem.SybType.FUNC);
        // Assume all grammars here are added as (func Start Start Start ...)
        // TODO: Use some more general analysis
        String[] rule = new String[subSolution.getArgs().length + 1];
        rule[0] = name;
        for (int i = 1; i < rule.length; i++){
            rule[i] = "Start";
        }
        this.cfgs.get(targetName).grammarRules.get("Start").add(rule);
    }
}
