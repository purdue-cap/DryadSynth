import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class ITCegis extends Cegis {
	enum Tmplt {
		DISJ,
		CONJ,
		MAX,
		MIN;
	}

	public Expr[] lastCands = null;
	public DefinedFunc[] lastTmplts = null;
	public final Tmplt intTmplts[] = {Tmplt.MAX, Tmplt.MIN};
	public final Tmplt boolTmplts[] = {Tmplt.DISJ, Tmplt.CONJ};

	boolean isExtensible(Tmplt type, int funcIndex) {
		logger.info("Checking extensibility for funcIndex " + String.valueOf(funcIndex) + " with Tmplt " + type.toString());
		if(lastCands[funcIndex] == null){
			logger.info("No candidate present, returning false");
			return false;
		}
		String name = problem.names.get(funcIndex);
		FuncDecl f = problem.rdcdRequests.get(name);
		DefinedFunc newTmplt = extend(type, funcIndex);
		BoolExpr predToCheck = ctx.mkImplies(problem.finalConstraint,
								(BoolExpr)newTmplt.rewrite(problem.finalConstraint, f));
		logger.info("Running SAT check");
        Solver svr = ctx.mkSolver();
		svr.add(ctx.mkNot(predToCheck));
		Status sts = svr.check();
		logger.info("Result:" + sts.toString());
		return sts == Status.UNSATISFIABLE;
	}

	DefinedFunc extend(Tmplt type, int funcIndex) {
		if(lastCands[funcIndex] == null){
			return null;
		}
		String name = problem.names.get(funcIndex);
		FuncDecl f = problem.rdcdRequests.get(name);
		Expr[] vars = problem.requestUsedArgs.get(name);
		Expr fapp = f.apply(vars);
		Expr cand = lastCands[funcIndex];
		Expr newDef;
		switch(type) {
			case MAX:
				newDef = ctx.mkITE(ctx.mkGe((ArithExpr)fapp, (ArithExpr)cand),
 								fapp, cand);
				break;
			case MIN:
				newDef = ctx.mkITE(ctx.mkLe((ArithExpr)fapp, (ArithExpr)cand),
 								fapp, cand);
				break;
			case DISJ:
				newDef = ctx.mkOr((BoolExpr)fapp, (BoolExpr)cand);
				break;
			case CONJ:
				newDef = ctx.mkAnd((BoolExpr)fapp, (BoolExpr)cand);
				break;
			default:
				return null;
		}
		return new DefinedFunc(ctx, name, vars, newDef);
	}

	// Request sort is checked in this procedure
	DefinedFunc findExtension(int funcIndex) {
		logger.info("Finding extension for funcIndex " + String.valueOf(funcIndex));
		String name = problem.names.get(funcIndex);
		boolean isBool = problem.requests.get(name).getRange().toString().equals("Bool");
		Tmplt[] tmplts;
		if (isBool) {
			tmplts = boolTmplts;
		} else {
			tmplts = intTmplts;
		}
		for (Tmplt type : tmplts) {
			if (isExtensible(type, funcIndex)) {
				logger.info("funcIndex " + String.valueOf(funcIndex) + " with Tmplt " + type.toString() + " is extensible");
				return extend(type, funcIndex);
			}
		}
		logger.info("No extension found for funcIndex " + String.valueOf(funcIndex));
		return null;

	}

	public ITCegis(Context ctx, CEGISEnv env, Logger logger){
		super(ctx, env, logger);
	}

	// Overriding Synthesizer class and its factory function
	public class Synthesizer extends Cegis.Synthesizer {

		@Override
		public Status synthesis(int condBound) {
			synchronized(env.lastTmplts) {
				if (env.lastTmplts != null) {
					lastTmplts = env.lastTmplts.array;
				}
			}
			return super.synthesis(condBound);
		}

		@Override
		protected Expr getEval(int funcIndex){
			Expr pureEval = super.getEval(funcIndex);
			if (lastTmplts == null) {
				return pureEval;
			}
			if (lastTmplts[funcIndex] == null) {
				return pureEval;
			}
			String name = problem.names.get(funcIndex);
			FuncDecl f = problem.rdcdRequests.get(name);
			Expr[] vars = problem.requestUsedArgs.get(name);
			Expr fapp = f.apply(vars);
			DefinedFunc tmplt = lastTmplts[funcIndex].translate(ctx);
			return tmplt.getDef().substitute(fapp, pureEval);
		}
	}

	@Override
	protected Synthesizer createSynthesizer() {
		return new Synthesizer();
	}

	// Overriding SynthDecoder class and its factory function
	public class SynthDecoder extends Cegis.SynthDecoder {
		public SynthDecoder(Cegis.Synthesizer synth) {
			super(synth);
		}

		@Override
		public void generateFunction(Map<String, Expr> functions) {
			super.generateFunction(functions);
			if (lastCands == null) {
				lastCands = new Expr[problem.names.size()];
				for (int i = 0; i < lastCands.length; i++) {
					lastCands[i] = null;
				}
			}
			int k = 0;
			for (String name : problem.names) {
				Expr tree = functions.get(name);
                tree = tree.simplify();
				if (lastTmplts == null || lastTmplts[k] == null){
					lastCands[k] = tree;
					k++;
					continue;
				}
				FuncDecl f = problem.rdcdRequests.get(name);
				Expr[] vars = problem.requestUsedArgs.get(name);
				Expr fapp = f.apply(vars);
				Expr tmplt = lastTmplts[k].translate(ctx).getDef();
				Expr newCand = tmplt.substitute(fapp, tree);
                newCand = newCand.simplify();
				functions.put(name, newCand);
				lastCands[k] = newCand;
				k++;
			}
		}
	}

	@Override
	protected SynthDecoder createSynthDecoder(Cegis.Synthesizer synth) {
		return new SynthDecoder(synth);
	}

	@Override
	public void run() {
		env.runningThreads.incrementAndGet();
		if (problem.isGeneral || env.feedType != CEGISEnv.FeedType.HEIGHTONLY) {
			return;
		}
		logger.info("Check for possible candidates from parser.");
		for (String name : problem.candidate.keySet()) {
			DefinedFunc df = problem.candidate.get(name);
			logger.info(String.format("Candidate for %s : %s", name, df.getDef()));
			functions.put(name, df.getDef());
		}
		logger.info(Thread.currentThread().getName() + " Started");
		logger.info("Starting CEGIS algorithm with templates.");
		fixedHeight = pdc1D.get();
		while (running) {
			logger.info("Started loop with fixedHeight = " + fixedHeight);
			cegis();
            if ((this.iterLimit > 0 && iterCount > this.iterLimit) || this.results != null) {
				synchronized(env) {
					env.notify();
				}
				env.runningThreads.decrementAndGet();
                return;
            }
			DefinedFunc[] newTmplts = null;
			for (int i = 0; i < problem.names.size(); i++) {
				DefinedFunc df = this.findExtension(i);
				if (df == null) {
					continue;
				}
				if (newTmplts == null) {
					newTmplts = new DefinedFunc[problem.names.size()];
					for (int j = 0; j < problem.names.size(); j++) {
						newTmplts[j] = null;
					}
				}
				logger.info("Template found: " + df.toString());
                env.tmpltApplied = true;
                if (env.checkITOnly) {
                    synchronized(env) {
                        env.notify();
                    }
                    env.runningThreads.decrementAndGet();
                    return;
                }
				newTmplts[i] = df;
			}
			if (newTmplts == null) {
				fixedHeight = pdc1D.get();
			} else {
				synchronized(env.lastTmplts) {
					env.lastTmplts.array = newTmplts;
				}
			}
		}
		env.runningThreads.decrementAndGet();
	}
}
