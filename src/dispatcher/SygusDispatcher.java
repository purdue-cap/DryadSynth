import java.util.*;
import java.util.logging.Logger;
import com.microsoft.z3.*;

public class SygusDispatcher {
    enum SolveMethod {
        PRESCREENED, CEGIS
    }
    SolveMethod method = SolveMethod.CEGIS;
    Context z3ctx;
    SygusExtractor extractor;
    Logger logger;
    int numCore;
    Thread mainThread;
    Thread [] threads = null;

    SygusDispatcher(Context z3ctx, SygusExtractor extractor, Logger logger) {
        this.z3ctx = z3ctx;
        this.extractor = extractor;
        this.logger = logger;
        this.numCore = Runtime.getRuntime().availableProcessors();
        this.mainThread = Thread.currentThread();
    }

    public void setNumCore(int cores) {
        this.numCore = cores;
    }

    public void prescreen() {
        logger.info("Checking candidates generated from parsing.");
        boolean checkResult = this.validateCandidates();
        if (checkResult) {
            logger.info("Parsing candidates are valid.");
            this.method = SolveMethod.PRESCREENED;
            return;
        }

        return;
    }

    public void initAlgorithm() {
        if (this.method == SolveMethod.PRESCREENED) {
            logger.info("Taking prescreened candidates, skipping algorithm initialization.");
            return;
        }

    }

    public DefinedFunc[] runAlgorithm() {
        if (this.method == SolveMethod.PRESCREENED) {
            logger.info("Outputing prescreened candidates as results.");
            List<DefinedFunc> resList = new ArrayList<DefinedFunc>();
            for (String name : extractor.candidate.keySet()) {
                resList.add(extractor.candidate.get(name).replaceName(name));
            }
            return resList.toArray(new DefinedFunc[resList.size()]);
        }

    }

    boolean validateCandidates() {
        Solver solver = z3ctx.mkSolver();
        Expr spec = extractor.finalConstraint;
        for (String name : extractor.candidate.keySet()) {
			FuncDecl f = extractor.rdcdRequests.get(name);
			Expr[] args = extractor.requestUsedArgs.get(name);
			DefinedFunc df = extractor.candidate.get(name).replaceArgs(args);
			spec = df.rewrite(spec, f);
        }
        solver.push();
        solver.add(ctx.mkNot((BoolExpr)spec));
        Status status = solver.check();
        solver.pop();
        return status == Status.UNSATISFIABLE;

    }


}
