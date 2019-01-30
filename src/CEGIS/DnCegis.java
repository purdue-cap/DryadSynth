import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class DnCegis extends Cegis {

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
			results = null;
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
			boolean newInter = false;
			for (Expr expr : ((DnCEnv)env).dncPblms.keySet()) {
				this.currentSubExpr = expr;
				synchronized(env) {
					SygusProblem pblm = ((DnCEnv)env).dncPblms.get(expr);
					this.problem = pblm.translate(this.ctx);
					String name = problem.names.get(0);
					if (((DnCEnv)env).interResults.containsKey(name)) {
						continue;
					}
				}
				expand = new Expand(ctx, problem);
				results = null;
				cegisGeneral();
	            if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
					synchronized(env) {
						env.notify();
					}
					env.runningThreads.decrementAndGet();
	                return;
	            }
				synchronized(env) {
					if (this.results != null) {
						SygusProblem pblm = ((DnCEnv)env).dncPblms.get(expr);
						String name = problem.names.get(0);
						DefinedFunc subSol = this.results[0].translate(pblm.ctx);
						if (!((DnCEnv)env).interResults.containsKey(name)) {
							newInter = true;
							((DnCEnv)env).addSubSolutionToAll(subSol);
							((DnCEnv)env).interResults.put(name, subSol);
						}
					}
				}
			}
			this.currentSubExpr = null;
			synchronized(env) {
				this.problem = env.problem.translate(this.ctx);
			}
			if (!newInter) {
				continue;
			}
			expand = new Expand(ctx, problem);
			results = null;
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
		// We should have results at this point, we need to fill in all intermediate results
		String funcName;
		DefinedFunc def = this.results[0];
		logger.info("Expr with internal results: " + def.toString());
		ASTGeneral defAST = def.getAST();
		logger.info("Internal results:");
		for (String func : ((DnCEnv)env).interResults.keySet()) {
			DefinedFunc df = ((DnCEnv)env).interResults.get(func);
			logger.info(df.toString());
		}
		while((funcName = ((DnCEnv)env).scanInterResults(defAST)) != null) {
			DefinedFunc interFunc = ((DnCEnv)env).interResults.get(funcName);
			defAST = interFunc.rewrite(defAST);
		}
		this.results[0].setAST(defAST);
		synchronized(env) {
			env.notify();
		}
		env.runningThreads.decrementAndGet();
		return;
	}

}
