import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class Cegis extends Thread{

	private Context ctx;
	private SygusExtractor extractor;
	private BoolExpr finalConstraint;
	private Logger logger;
	private int minFinite;
	private int minInfinite;
	private boolean maxsmtFlag;
	private int fixedHeight = -1;
	private int fixedCond = -1;
	private int fixedVectorLength = -1;
	private Producer1D pdc1D = null;
	private Producer2D pdc2D = null;
	private Object condition = null;
	private Expand expand = null;

	private boolean isGeneral = false;

	public Map<String, int[]> generalFuncs;

	public Map<String, Expr> functions;
	public Set<Expr[]> counterExamples;
	public volatile DefinedFunc[] results = null;
	public volatile boolean running = true;

	public Cegis(SygusExtractor extractor, int fixedHeight, int fixedCond, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
		this(new Context(), extractor, logger, minFinite, minInfinite, maxsmtFlag);
		this.fixedHeight = fixedHeight;
		this.fixedCond = fixedCond;
	}

	public Cegis(SygusExtractor extractor, Producer1D pdc1D, Object condition, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
		this(new Context(), extractor, logger, minFinite, minInfinite, maxsmtFlag);
		this.pdc1D = pdc1D;
		this.condition = condition;
	}

	public Cegis(SygusExtractor extractor, Producer2D pdc2D, Object condition, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
		this(new Context(), extractor, logger, minFinite, minInfinite, maxsmtFlag);
		this.pdc2D = pdc2D;
		this.condition = condition;
	}

	public Cegis(Context ctx, SygusExtractor extractor, Logger logger, int minFinite, int minInfinite, boolean maxsmtFlag) {
		this.ctx = ctx;
		this.extractor = extractor.translate(ctx);
		this.logger = logger;
		this.minFinite = minFinite;
		this.minInfinite = minInfinite;
		this.maxsmtFlag = maxsmtFlag;

		this.ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);

		this.finalConstraint = extractor.finalConstraint;

		this.generalFuncs = new LinkedHashMap<String, int[]>();
		this.functions = new LinkedHashMap<String, Expr>();

		if (!extractor.isGeneral) {
			for(String name : extractor.names) {
				FuncDecl func = extractor.rdcdRequests.get(name);

				if (func.getRange().toString().equals("Bool")) {
					functions.put(name, ctx.mkTrue());
				} else {
					functions.put(name, ctx.mkInt(0));
				}
			}
		}

		counterExamples = new HashSet<Expr[]>();

		//addRandomInitialExamples();
		//addSimpleExamples();
	}

	public void addRandomInitialExamples() {
		int numVar = extractor.vars.size();
		//int numExamples = (int)Math.pow(4, numVar) + 1;
		int numExamples = (int)Math.pow(numVar, 3) + 1;
		//int numExamples = (int)Math.pow(2, numVar) + 1;
		//int numExamples = 90;

		for (int i = 0; i < numExamples; i++) {
			//IntExpr[] randomExample = new IntExpr[numVar];
			IntExpr[] randomExample = new IntExpr[numVar];
			for (int j = 0; j < numVar; j++) {
				Random rand = new Random();
				int n;
				int randomFlag = rand.nextInt(10);
				if (randomFlag%2 == 0) {
					n = rand.nextInt(10);
				} else {
					n = -rand.nextInt(10);

				}
				randomExample[j] = ctx.mkInt(n);
			}
			counterExamples.add(randomExample);
		}

	}

	// Subclasses for subprocedures
	// Previously Verifier.java
	class Verifier {
		public Solver s;

		public Verifier() {
			this.s = ctx.mkSolver();
		}

		public Status verify(Map<String, Expr> functions) {

			Expr spec = extractor.finalConstraint;

			for (String name : extractor.names) {
				FuncDecl f = extractor.rdcdRequests.get(name);
				Expr[] args = extractor.requestUsedArgs.get(name);
				Expr def = functions.get(name);
				DefinedFunc df = new DefinedFunc(ctx, args, def);
				spec = df.rewrite(spec, f);
			}

			// int j = 0;
			// for(Expr expr: extractor.vars.values()) {
			// 	spec = 	spec.substitute(expr, var[j]);
			// 	j = j + 1;
			// }

			//System.out.println("Specification : ");
			//System.out.println(ctx.mkNot((BoolExpr)spec));

			//BoolExpr spec = max2Prop(functions);
			//BoolExpr spec = max3Prop(functions);

			s.push();
			s.add(ctx.mkNot((BoolExpr)spec));
			//System.out.println("Verifying... Formula: ");
			//System.out.println(s);

			Status status = s.check();
			s.pop();
			return status;
			//return Status.UNSATISFIABLE;

		}

	}

	// Previously VerifierDecoder.java
	class VerifierDecoder {
		private Model model;
		private Expr[] vars;
		private int numVar;

		public VerifierDecoder(Model model) {
			this.model = model;
			this.numVar = extractor.vars.size();
			this.vars = extractor.vars.values().toArray(new Expr[numVar]);
		}

		public Expr[] decode() {

			Expr[] counterExample = new Expr[numVar];

			for (int i = 0; i < numVar; i++) {
				counterExample[i] = model.evaluate(vars[i], true);
			}

			return counterExample;

		}

		public String toString() {
			Expr[] ex = decode();
			return Arrays.toString(ex);
		}
	}

	// Previously Synthesizer.java
	class Synthesizer {

		public Solver s;
		public Optimize optimize;

		public Model m = null;

		public Synthesizer() {
			this.s = ctx.mkSolver();
			this.optimize = ctx.mkOptimize();
		}

		public Model getLastModel() {
			return m;
		}

		public Status synthesis(int condBound) {

			s.push();

			Expr spec = extractor.finalConstraint;

			BoolExpr q = expand.expandCoefficient(condBound);

			int k = 0;
			for (String name : extractor.names) {
				FuncDecl f = extractor.rdcdRequests.get(name);
				Expr[] var = extractor.requestUsedArgs.get(name);
				Expr eval = expand.generateEval(k, 0);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			for (Expr[] params : counterExamples) {
				BoolExpr expandSpec = (BoolExpr) spec.substitute(extractor.vars.values().toArray(new Expr[extractor.vars.size()]), params);
				q = ctx.mkAnd(q, expandSpec);
			}

			s.add(q);

			/*try {
			PrintWriter writer2 = new PrintWriter("s.smt2", "UTF-8");
			writer2.println("(set-logic QF_UFLIA)");
			writer2.println("(set-option :produce-models true)");
			writer2.println(s);
			writer2.println("(check-sat)");
			writer2.println("(get-model)");
			writer2.close();
			} catch (IOException e) {
	   			System.out.println("Print out error");
			}*/

			//System.out.println("Synthesizing... Formula: ");
			//System.out.println(s);

			Status sts = s.check();

			if (sts == Status.SATISFIABLE) {
				m = s.getModel();
			}

			s.pop();
			return sts;

		}

		public Status synthesisGeneral() {

			s.push();

			Expr spec = extractor.finalConstraint;

			// CondBound not implemented for general yet
			// Currently expandCoefficientGeneral only applies term range restrictions
			BoolExpr q = expand.expandCoefficientGeneral();
			//BoolExpr q = expand.expandCoefficient(condBound);

			int k = 0;
			for (String name : extractor.names) {
				// Currently preprocessing is disabled, using dummy
				FuncDecl f = extractor.rdcdRequests.get(name);
				Expr[] var = extractor.requestUsedArgs.get(name);
				Expr eval = expand.intepretGeneral(k);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			for (Expr[] params : counterExamples) {
				BoolExpr expandSpec = (BoolExpr) spec.substitute(extractor.vars.values().toArray(new Expr[extractor.vars.size()]), params);
				q = ctx.mkAnd(q, expandSpec);
			}

			s.add(q);

			Status sts = s.check();

			if (sts == Status.SATISFIABLE) {
				m = s.getModel();
			}

			s.pop();
			return sts;
		}

		public BoolExpr setMax(IntExpr c, IntExpr absolute) {
			BoolExpr setMax;
			IntExpr minusc = (IntExpr)ctx.mkSub(ctx.mkInt(0), c);

			setMax = ctx.mkOr(ctx.mkAnd(ctx.mkGe(c, minusc), ctx.mkEq(c, absolute))
				, ctx.mkAnd(ctx.mkGt(minusc, c), ctx.mkEq(minusc, absolute)));

			return setMax;
		}

		public Status synthesisWithSMT() {
			optimize.Push();

			Expr spec = extractor.finalConstraint;

			BoolExpr q = ctx.mkTrue();

			int k = 0;
			for (String name : extractor.names) {
				FuncDecl f = extractor.rdcdRequests.get(name);
				Expr[] var = extractor.requestUsedArgs.get(name);
				Expr eval = expand.generateEval(k, 0);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			for (Expr[] params : counterExamples) {
				BoolExpr expandSpec = (BoolExpr) spec.substitute(extractor.vars.values().toArray(new Expr[extractor.vars.size()]), params);
				q = ctx.mkAnd(q, expandSpec);
			}

			optimize.Add(q);

			int numFunc = extractor.names.size();
			int bound = expand.bound;
			IntExpr[][][] c = expand.getCoefficients();
			IntExpr[][][] absolute = new IntExpr[numFunc][bound][0];

			for (int l = 0; l < numFunc; l++) {
				for (int j = 0; j < bound; j++) {
					String name = extractor.names.get(l);
					int argCount = extractor.requestUsedArgs.get(name).length;
					absolute[l][j] = new IntExpr[argCount + 1];
					for (int i = 0; i < argCount + 1; i++) {
						absolute[l][j][i] = ctx.mkIntConst("f" + l + "_absolute_c" + j + "_" + i);
						optimize.Add(setMax(c[l][j][i], absolute[l][j][i]));
						Optimize.Handle minimize = optimize.MkMinimize(absolute[l][j][i]);
					}
				}
			}

			Status sts = optimize.Check();

			if (sts == Status.SATISFIABLE) {
				m = optimize.getModel();
			}

			optimize.Pop();
			return sts;
		}
	}

	// Previously SynthDecoder.java
	class SynthDecoder {

		private Model model;
		private IntExpr[][][] c;
		private IntExpr[][] t;

		public SynthDecoder(Model model) {
			this.model = model;
			this.c = expand.getCoefficients();
			this.t = expand.getTerms();
		}

		public IntExpr[][][] evaluteCoefficient() {

			IntExpr[][][] coeff = new IntExpr[c.length][0][0];

			for (int i = 0; i < c.length; i++) {
				coeff[i] = new IntExpr[c[i].length][0];
				for (int j = 0; j < c[i].length; j++) {
					coeff[i][j] = new IntExpr[c[i][j].length];
					for (int k = 0; k < c[i][j].length; k++) {
						coeff[i][j][k] = (IntExpr) model.evaluate(c[i][j][k], true);
					}
				}
			}

			return coeff;
		}

		public int[][] evaluateTerm() {
			int[][] terms = new int[t.length][];

			for (int i = 0; i < t.length; i++) {
				terms[i] = new int[t[i].length];
				for (int j = 0; j < t[i].length; j++) {
					Expr termExpr = model.evaluate(t[i][j], true);
					assert termExpr.isIntNum();
					terms[i][j] = ((IntNum)termExpr).getInt();
				}
			}
			return terms;
		}

		public void generateFunction(Map<String, Expr> functions) {
			IntExpr[][][] coeff = evaluteCoefficient();

			ArithExpr[][] p = new ArithExpr[coeff.length][0];
			Expr[][] f = new Expr[coeff.length][0];

			for (int k = 0; k < coeff.length; k++) {
				p[k] = new ArithExpr[coeff[k].length];
				for (int i = 0; i < coeff[k].length; i++) {
					p[k][i] = coeff[k][i][0];

					Expr[] args = extractor.requestUsedArgs.get(extractor.names.get(k));
					for (int j = 1; j < coeff[k][i].length; j++) {
						p[k][i] = ctx.mkAdd(p[k][i], ctx.mkMul(coeff[k][i][j], (ArithExpr)args[j - 1]));
					}
				}
			}

			for (int j = 0; j < coeff.length; j++) {
				f[j] = new Expr[coeff[j].length];
				for (int i = coeff[j].length - 1; i >= 0; i--) {
					BoolExpr cond = ctx.mkGe(p[j][i], ctx.mkInt(0));

					if (i < ((coeff[j].length - 1)/2)) {

						f[j][i] = ctx.mkITE(cond, f[j][2*i + 1], f[j][2*i + 2]);

					} else {
						boolean isINV = extractor.requests.get(extractor.names.get(j)).getRange().toString().equals("Bool");
						if (isINV) {
							f[j][i] = ctx.mkITE(cond, ctx.mkTrue(), ctx.mkFalse());
						} else {
							f[j][i] = p[j][i];
						}

					}
				}
				functions.put(extractor.names.get(j), f[j][0].simplify());
			}

		}

		public void generateFuncGeneral(Map<String, int[]> generalFunc) {
			int[][] terms = evaluateTerm();
			for (int i = 0; i < terms.length; i++) {
				generalFunc.put(extractor.names.get(i), terms[i]);
			}
		}

		public void intepretFunctions(Map<String, int[]> generalFunc, Map<String, Expr> functions) {
			int i = 0;
			for (String name: extractor.names) {
				int[] terms = generalFunc.get(name);
				functions.put(name, expand.intepretGeneral(i, terms));
				i++;
			}
		}

		/*public void printOutput() {
			IntExpr[][] coeff0 = evaluteCoefficient();
			for (int i = 0; i < bound; i++) {
				for (int j = 0; j < numCoeff; j++) {
					System.out.println("node" + i + ": c" + j + ": " + coeff0[i][j]);
				}
			}

			String[] v = evaluteValid();
			for (int i = 0; i < bound; i++) {
				System.out.println("valid" + i + ": " + v[i]);
			}

			ArithExpr[] p = getPoly();
			for (int i = 0; i < bound; i++) {
				System.out.println("poly" + i + ": " + p[ i]);
			}

			ArithExpr[] function = generateFunction();
			for (int i = 0; i < numFunc; i++) {
				System.out.println(function[i]);
			}

		}*/

	}

	public IntExpr[][] addSimpleExamplesRecursive(int nv) {
		int numVar = extractor.vars.size();
		IntExpr[][] examples = new IntExpr[(int)Math.pow(3, nv)][numVar];

		if (nv == 0) return examples;

		else {
			IntExpr[][] smaller_examples = addSimpleExamplesRecursive(nv-1);
			int temp = (int)Math.pow(3, nv-1);

			for (int j = 0; j < temp; j++) {
				System.arraycopy(smaller_examples[j], 0, examples[j], 0, nv-1);
				examples[j][nv-1] = ctx.mkInt(-1);
				System.arraycopy(smaller_examples[j], 0, examples[temp + j], 0, nv-1);
				examples[temp + j][nv-1] = ctx.mkInt(0);
				System.arraycopy(smaller_examples[j], 0, examples[temp * 2 + j], 0, nv-1);
				examples[temp * 2 + j][nv-1] = ctx.mkInt(1);
			}

			return examples;

		}

	}

	public void addSimpleExamples() {
		int numVar = extractor.vars.size();
		IntExpr[][] examples = addSimpleExamplesRecursive(numVar);
		for (int j = 0; j < examples.length; j++) {
			counterExamples.add(examples[j]);
		}
	}

	public void run() {
		if (extractor.isGeneral) {
			logger.info(Thread.currentThread().getName() + " Started");
			logger.info("Starting general track CEGIS");
			// Initialize expand here for max data sharing
			expand = new Expand(ctx, extractor);
			if (pdc1D != null) {
				while (results == null && running) {
					fixedVectorLength = pdc1D.get();
					logger.info("Started loop with fixedVectorLength = " + fixedVectorLength);
					cegisGeneral();
				}
			} else {
				cegisGeneral();
			}
			return;
		}
		logger.info("Check for possible candidates from parser.");
		for (String name : extractor.candidate.keySet()) {
			DefinedFunc df = extractor.candidate.get(name);
			logger.info(String.format("Candidate for %s : %s", name, df.getDef()));
			functions.put(name, df.getDef());
		}
		logger.info(Thread.currentThread().getName() + " Started");
		if (pdc1D != null) {
			while (results == null && running) {
				fixedHeight = pdc1D.get();
				logger.info("Started loop with fixedHeight = " + fixedHeight);
				cegis();
			}
		} else if (pdc2D != null) {
			while (results == null && running) {
				int[] args = pdc2D.get();
				fixedHeight = args[0];
				fixedCond = args[1];
				logger.info("Started loop with fixedHeight = " + fixedHeight + ", fixedCond = " + fixedCond);
				cegis();
			}
		} else {
			cegis();
		}
	}

	public void cegisGeneral() {
		int vectorBound = 1;
		if (fixedVectorLength > 0) {
			vectorBound = fixedVectorLength;
		}
		logger.warning("Condition bound not implemented for general");
		logger.warning("Using minInfinite as timeout");
		//int condBound = 1;
		//if (fixedCond > 0) {
		//	condBound = fixedCond;
		//}
		//int condBoundInc = 1;
		//int searchRegions = 2;
		long startTime = System.currentTimeMillis();

		int k = 0;	//number of iterations

		//print out initial examples
		logger.info("Initial examples:" + Arrays.deepToString(counterExamples.toArray()));

		// Subprocedure classes
		Verifier testVerifier = new Verifier();
		Synthesizer testSynthesizer = new Synthesizer();
		expand.setVectorBound(vectorBound);
		if (fixedVectorLength > 0) {
			if (!expand.isIntepretableNow()) {
				logger.info(String.format("vectorLength %d unintepretable, exited", vectorBound));
				return;
			}
		} else {
			while(!expand.isIntepretableNow()) {
				logger.info(String.format("vectorLength %d unintepretable, exited", vectorBound));
				vectorBound++;
				expand.setVectorBound(vectorBound);
			}
		}

		while(running) {

			k = k + 1;

			logger.info("Start synthesizing");

			Status synth = Status.UNKNOWN;

			if (maxsmtFlag) {
				logger.warning("MaxSMT not implemented for General Synthesis");
			}

			synth = testSynthesizer.synthesisGeneral();

			//print out for debug
			logger.info("Synthesis Done");

			if (synth == Status.UNSATISFIABLE) {
				logger.info("Synthesizer : Unsatisfiable");
				logger.info(String.format("Exited vectorLength %d due to UNSAT", vectorBound));
				if (fixedVectorLength > 0) {
					return;
				} else {
					vectorBound++;
					expand.setVectorBound(vectorBound);
					while(!expand.isIntepretableNow()) {
						logger.info(String.format("vectorLength %d unintepretable, exited", vectorBound));
						vectorBound++;
						expand.setVectorBound(vectorBound);
					}
					startTime = System.currentTimeMillis();
					continue;
				}
			} else if (synth == Status.UNKNOWN) {
				logger.severe("Synthesizer Error : Unknown");
				return;
			} else if (synth == Status.SATISFIABLE) {
				//logger.info(testSynthesizer.s.getModel());	//for test only
				SynthDecoder synthDecoder = new SynthDecoder(testSynthesizer.getLastModel());
				//print out for debug
				logger.info("Start decoding synthesizer output");
				synthDecoder.generateFuncGeneral(generalFuncs);
				synthDecoder.intepretFunctions(generalFuncs, functions);
				//print out for debug
				logger.info("Synthesizer output decode done");
				//print out for debug
				for (String name : extractor.names) {
					logger.info(name + " = " + Arrays.toString(generalFuncs.get(name)));
					logger.info(name + " : " + functions.get(name).toString());
				}

			}
			logger.info("Start verifying");

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
				results = new DefinedFunc[functions.size()];
				int i = 0;
				for (String name : extractor.rdcdRequests.keySet()) {
					Expr def = functions.get(name);
					results[i] = new DefinedFunc(ctx, name, extractor.requestArgs.get(name), def);
					logger.info("Done, Synthesized function(s):" + Arrays.toString(results));
					i = i + 1;
				}
				if (fixedVectorLength > 0) {
					synchronized(condition) {
						condition.notify();
					}
				}
				return;

			} else if (v == Status.UNKNOWN) {
				logger.severe("Verifier Error : Unknown");
				return;

			} else if (v == Status.SATISFIABLE) {

				logger.info("Verifier results:" + testVerifier.s.getModel());	//for test only
				VerifierDecoder decoder = new VerifierDecoder(testVerifier.s.getModel());

				Expr[] cntrExmp = decoder.decode();
				counterExamples.add(cntrExmp);
				logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(counterExamples.toArray()));
			}

			if (System.currentTimeMillis() - startTime > 60000 * this.minInfinite) {
				if (fixedVectorLength > 0) {
					return;
				} else {
					vectorBound++;
					expand.setVectorBound(vectorBound);
					while(!expand.isIntepretableNow()) {
						logger.info(String.format("vectorLength %d unintepretable, exited", vectorBound));
						vectorBound++;
						expand.setVectorBound(vectorBound);
					}
					startTime = System.currentTimeMillis();
					continue;
				}
			}
			logger.info("Iteration : " + k);
		}
	}

	public void cegis() {

		boolean flag = true;
		int heightBound = 1;
		if (fixedHeight > 0) {
			heightBound = fixedHeight;
		}
		int condBound = 1;
		if (fixedCond > 0) {
			condBound = fixedCond;
		}
		int condBoundInc = 1;
		int searchRegions = 2;
		long startTime = System.currentTimeMillis();

		int k = 0;	//number of iterations

		//print out initial examples
		logger.info("Initial examples:" + Arrays.deepToString(counterExamples.toArray()));

		// Subprocedure classes
		Verifier testVerifier = new Verifier();
		Synthesizer testSynthesizer = new Synthesizer();
		expand = new Expand(ctx, extractor);
		expand.setHeightBound(heightBound);

		while(flag && running) {

			k = k + 1;

			logger.info("Start verifying");

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
					results = new DefinedFunc[functions.size()];
					int i = 0;
					for (String name : extractor.rdcdRequests.keySet()) {
						Expr def = functions.get(name);
						if (def.isBool()) {
							def = SygusFormatter.elimITE(this.ctx, def);
						}
						results[i] = new DefinedFunc(ctx, name, extractor.requestArgs.get(name), def);
						logger.info("Done, Synthesized function(s):" + Arrays.toString(results));
						i = i + 1;
					}
					flag = false;
					if (fixedCond > 0 || fixedHeight > 0) {
						synchronized(condition) {
							condition.notify();
						}
					}

				} else if (v == Status.UNKNOWN) {
					logger.severe("Verifier Error : Unknown");
					flag = false;

				} else if (v == Status.SATISFIABLE) {

					logger.info("Verifier results:" + testVerifier.s.getModel());	//for test only
					VerifierDecoder decoder = new VerifierDecoder(testVerifier.s.getModel());

					Expr[] cntrExmp = decoder.decode();
					counterExamples.add(cntrExmp);
					//print out for debug
					logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(counterExamples.toArray()));

					// if (k >= 10) {
					// 	break;
					// }

					boolean unsat = true;

					while(unsat && flag && running) {

						//print out for debug
						logger.info("Start synthesizing");

						Status synth = Status.UNKNOWN;

						if (!maxsmtFlag) {
							synth = testSynthesizer.synthesis(condBound);
						} else {
							synth = testSynthesizer.synthesisWithSMT();
						}

						//print out for debug
						logger.info("Synthesis Done");

						if (synth == Status.UNSATISFIABLE) {
							logger.info("Synthesizer : Unsatisfiable");
							if (fixedCond > 0) {
								logger.info(String.format("Exited height %d, cond %d due to UNSAT", fixedHeight, fixedCond));
								return;
							}
							if (condBoundInc > searchRegions) {		//for 2, >5		//for 4, >3	64 	//infinite 5
								if (fixedHeight > 0) {
									logger.info(String.format("Exited height %d due to UNSAT", fixedHeight));
									return;
								}
								heightBound = heightBound + 1;
								expand.setHeightBound(heightBound);
								logger.info("Synthesizer : Increase height bound to " + heightBound);
								condBound = 1;
								if (fixedCond > 0) {
									condBound = fixedCond;
								}
								condBoundInc = 1;
							} else {
								if (condBoundInc == searchRegions) {
									condBound = -1;
									logger.info("Synthesizer : Increase coefficient bound to infinity");
								} else {
									condBound = (int)Math.pow(64, condBoundInc);	//64
									logger.info("Synthesizer : Increase coefficient bound to " + condBound);
								}

								condBoundInc = condBoundInc + 1;
							}
							startTime = System.currentTimeMillis();
							//flag = false;
						} else if (synth == Status.UNKNOWN) {
							logger.severe("Synthesizer Error : Unknown");
							flag = false;
							unsat = false;
						} else if (synth == Status.SATISFIABLE) {

							unsat = false;
							//flag = false;	//for test only

							//logger.info(testSynthesizer.s.getModel());	//for test only
							SynthDecoder synthDecoder = new SynthDecoder(testSynthesizer.getLastModel());
							//print out for debug
							logger.info("Start decoding synthesizer output");
							synthDecoder.generateFunction(functions);
							//print out for debug
							logger.info("Synthesizer output decode done");
							//print out for debug
							for (String name : extractor.names) {
								logger.info(name + " : " + functions.get(name).toString());
							}

							if (condBoundInc > 1) {
								if (condBoundInc <= searchRegions) {
									if (System.currentTimeMillis() - startTime > 60000 * this.minFinite) {
										if (fixedCond > 0) {
											logger.info(String.format("Exited height %d, cond %d due to TIMEOUT", fixedHeight, fixedCond));
											return;
										}
										if (condBoundInc == searchRegions) {
											condBound = -1;
											logger.info("Synthesizer : Increase coefficient bound to infinity");
										} else {
											condBound = (int)Math.pow(64, condBoundInc);	//64
											logger.info("Synthesizer : Increase coefficient bound to " + condBound);
										}
										condBoundInc = condBoundInc + 1;
										startTime = System.currentTimeMillis();
									}
								} else {
									if (System.currentTimeMillis() - startTime > 60000 * this.minInfinite) {
										if (fixedHeight > 0) {
											logger.info(String.format("Exited height %d due to TIMEOUT", fixedHeight));
											return;
										}
										heightBound = heightBound + 1;
										expand.setHeightBound(heightBound);
										logger.info("Synthesizer : Increase height bound to " + heightBound);
										condBound = 1;
										if (fixedCond > 0) {
											condBound = fixedCond;
										}
										condBoundInc = 1;
										startTime = System.currentTimeMillis();

									}
								}
							}

						}

					}
				}
			logger.info("Iteration : " + k);
		}
	}
}
