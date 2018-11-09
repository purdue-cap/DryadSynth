import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class DnCegis extends Cegis {
	public DnCegis(DnCEnv env, Logger logger) {
		super(env, logger);
	}

	public DnCegis(Context ctx, DnCEnv env, Logger logger){
		super(ctx, env, logger);
	}

	Expr currentSubExpr = null;

	@Override
	public Set<Expr[]> getCE() {
		if (currentSubExpr == null) {
			return this.env.counterExamples;
		} else {
			return ((DnCEnv)this.env).dncCEs.get(currentSubExpr);
		}
	}

	@Override
	public void run() {
		env.runningThreads.incrementAndGet();
		if (!problem.isGeneral || env.feedType != CEGISEnv.FeedType.HEIGHTONLY) {
			return;
		}
		logger.info(Thread.currentThread().getName() + " Started");
		logger.info("Starting DnC general track CEGIS");
		while (running) {
			fixedVectorLength = pdc1D.get();
			logger.info("Started loop with fixedVectorLength = " + fixedVectorLength);
			expand = new Expand(ctx, problem);
			cegisGeneral();
            if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
				synchronized(env) {
					env.notify();
				}
				env.runningThreads.decrementAndGet();
                return;
            }
			if(this.results != null) {
				break;
			}
			for (Expr expr : ((DnCEnv)env).dncPblms.keySet()) {
				this.currentSubExpr = expr;
				SygusProblem pblm = ((DnCEnv)env).dncPblms.get(expr);
				synchronized(pblm) {
					this.problem = pblm.translate(this.ctx);
				}
				expand = new Expand(ctx, problem);
				cegisGeneral();
	            if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
					synchronized(env) {
						env.notify();
					}
					env.runningThreads.decrementAndGet();
	                return;
	            }
				if (this.results != null){
					DefinedFunc subSol = this.results[0].translate(pblm.ctx);
					((DnCEnv)env).addSubSolutionToAll(subSol);
				}
			}
			this.currentSubExpr = null;
			synchronized(env.problem) {
				this.problem = env.problem.translate(this.ctx);
			}
			expand = new Expand(ctx, problem);
			cegisGeneral();
            if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
				synchronized(env) {
					env.notify();
				}
				env.runningThreads.decrementAndGet();
                return;
            }
			if(this.results != null) {
				break;
			}
		}
		return;
	}

}
