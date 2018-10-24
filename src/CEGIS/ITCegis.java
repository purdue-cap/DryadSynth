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
    // Constructors
	public ITCegis(SygusExtractor extractor, int fixedHeight, int fixedCond, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
        super(extractor, fixedHeight, fixedCond, logger, minFinite, minInfinite, maxsmtFlag);
	}

	public ITCegis(SygusExtractor extractor, Producer1D pdc1D, Object condition, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
        super(extractor, pdc1D, condition, logger, minFinite, minInfinite, maxsmtFlag);
	}

	public ITCegis(SygusExtractor extractor, Producer2D pdc2D, Object condition, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
        super(extractor, pdc2D, condition, logger, minFinite, minInfinite, maxsmtFlag);
	}

	public ITCegis(Context ctx, SygusExtractor extractor, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
        super(ctx, extractor, logger, minFinite, minInfinite, maxsmtFlag);
	}

	// Overriding Synthesizer class and its factory function
	public class Synthesizer extends Cegis.Synthesizer {
		// This array to be filled by SynthDecoder, read by this class itself
		public Expr[] lastCands = null;
		// This array to be filled by this class, read by this class itself and SynthDecoder
		public DefinedFunc[] lastTmplts = null;
		public final Tmplt intTmplts[] = {Tmplt.MAX, Tmplt.MIN};
		public final Tmplt boolTmplts[] = {Tmplt.DISJ, Tmplt.CONJ};

		boolean isExtensible(Tmplt type, int funcIndex) {
			logger.info("Checking extensibility for funcIndex " + String.valueOf(funcIndex) + " with Tmplt " + type.toString());
			if(lastCands[funcIndex] == null){
				logger.info("No candidate present, returning false");
				return false;
			}
			String name = extractor.names.get(funcIndex);
			FuncDecl f = extractor.rdcdRequests.get(name);
			DefinedFunc newTmplt = extend(type, funcIndex);
			BoolExpr predToCheck = ctx.mkImplies(extractor.finalConstraint,
									(BoolExpr)newTmplt.rewrite(extractor.finalConstraint, f));
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
			String name = extractor.names.get(funcIndex);
			FuncDecl f = extractor.rdcdRequests.get(name);
			Expr[] vars = extractor.requestUsedArgs.get(name);
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
			String name = extractor.names.get(funcIndex);
			boolean isBool = extractor.requests.get(name).getRange().toString().equals("Bool");
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

		// lastTmplts filled here
		@Override
		public Status synthesis(int condBound) {
			if (lastCands == null) {
				logger.info("No candidate present, calling normal synthesis");
				lastTmplts = null;
				return super.synthesis(condBound);
			}
			if (lastTmplts == null) {
				lastTmplts = new DefinedFunc[extractor.names.size()];
				for (int i = 0; i < lastTmplts.length; i++) {
					lastTmplts[i] = null;
				}
			}
			int k = 0;
			for (String name : extractor.names) {
				DefinedFunc tmplt = findExtension(k);
				if (tmplt != null) {
					lastTmplts[k] = tmplt;
				}
				k++;
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
			String name = extractor.names.get(funcIndex);
			FuncDecl f = extractor.rdcdRequests.get(name);
			Expr[] vars = extractor.requestUsedArgs.get(name);
			Expr fapp = f.apply(vars);
			DefinedFunc tmplt = lastTmplts[funcIndex];
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

		// lastCands of Synthesizer filled here
		@Override
		public void generateFunction(Map<String, Expr> functions) {
			super.generateFunction(functions);
			ITCegis.Synthesizer synthIT = (ITCegis.Synthesizer)this.synth;
			if (synthIT.lastCands == null) {
				synthIT.lastCands = new Expr[extractor.names.size()];
				for (int i = 0; i < synthIT.lastCands.length; i++) {
					synthIT.lastCands[i] = null;
				}
			}
			int k = 0;
			for (String name : extractor.names) {
				Expr tree = functions.get(name);
				if (synthIT.lastTmplts == null || synthIT.lastTmplts[k] == null){
					synthIT.lastCands[k] = tree;
					k++;
					continue;
				}
				FuncDecl f = extractor.rdcdRequests.get(name);
				Expr[] vars = extractor.requestUsedArgs.get(name);
				Expr fapp = f.apply(vars);
				Expr tmplt = synthIT.lastTmplts[k].getDef();
				Expr newCand = tmplt.substitute(fapp, tree);
				functions.put(name, newCand);
				synthIT.lastCands[k] = newCand;
				k++;
			}
		}
	}

	@Override
	protected SynthDecoder createSynthDecoder(Cegis.Synthesizer synth) {
		return new SynthDecoder(synth);
	}
}
