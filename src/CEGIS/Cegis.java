import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

public class Cegis extends Thread{

	protected Context ctx;
	protected SygusExtractor extractor;
	protected BoolExpr finalConstraint;
	protected Logger logger;
	protected int minFinite;
	protected int minInfinite;
	protected boolean maxsmtFlag;
	protected int fixedHeight = -1;
	protected int fixedCond = -1;
	protected int fixedVectorLength = -1;
    protected Producer1D pdc1D = null;
    protected Producer2D pdc2D = null;
	protected Expand expand = null;
	protected CEGISEnv env = null;

	protected int heightBound = 1;
	protected int condBound = 1;
	protected int condBoundInc = 1;
	protected int searchRegions = 2;
	protected int vectorBound = 1;

	protected boolean isGeneral = false;

    public int iterLimit = 0;
	public Map<String, int[]> generalFuncs;
	public Map<String, ASTGeneral> ASTs;

	public Map<String, Expr> functions;
	public volatile DefinedFunc[] results = null;
	public volatile boolean running = true;

	public Cegis(CEGISEnv env, Logger logger) {
		this(new Context(), env, logger);
	}

	public Cegis(Context ctx, CEGISEnv env, Logger logger){
		this.ctx = ctx;
		this.env = env;
		this.extractor = env.extractor.translate(ctx);
		this.logger = logger;
		this.minFinite = env.minFinite;
		this.minInfinite = env.minInfinite;
		this.maxsmtFlag = env.maxsmtFlag;

		switch (env.feedType) {
			case FIXED:
				this.fixedHeight = env.fixedHeight;
				this.fixedCond = env.fixedCond;
				this.fixedVectorLength = env.fixedVectorLength;
				break;
			case HEIGHTONLY:
				this.pdc1D = env.pdc1D;
				break;
			case HEIGHTANDCOND:
				this.pdc2D = env.pdc2D;
				break;
		}

		this.ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);

		this.finalConstraint = extractor.finalConstraint;

		this.generalFuncs = new LinkedHashMap<String, int[]>();
		this.ASTs = new LinkedHashMap<String, ASTGeneral>();
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
			synchronized(env.counterExamples) {
				env.counterExamples.add(randomExample);
			}
		}

	}

	// Subclasses for subprocedures
	// Previously Verifier.java
	public class Verifier {
		public Solver s;

		public Verifier() {
			this.s = ctx.mkSolver();
		}

		public Status verify(Map<String, Expr> functions) {

			if(extractor.problemType == SygusExtractor.ProbType.INV){
				// System.out.println("inv dnc");
			// 	s.push();
			// 	DefinedFunc[] funcs = extractor.invConstraints.get("InvF");
			// 	System.out.println(funcs[1].getArgs()[2].getSort());
			// 	// FuncDecl f = extractor.rdcdRequests.get(extractor.names.get(0));
			// 	// System.out.println(f);
			// 	String name = extractor.names.get(0);
			// 	FuncDecl f = extractor.rdcdRequests.get(name);
			// 	System.out.println(f);
			// 	Expr[] args = extractor.requestUsedArgs.get(name);
			// 	Expr def = functions.get(name);
			// 	DefinedFunc df = new DefinedFunc(ctx, args, def);
			// 	Expr spec = ctx.mkOr((BoolExpr)ctx.mkApp(f,args), ctx.mkNot((BoolExpr)funcs[0].getDef()));
			// 	spec = df.rewrite(spec, f);
			// 	s.add(ctx.mkNot((BoolExpr)spec));
			// 	System.out.println("1 "+spec);
			// 	spec = ctx.mkOr((BoolExpr)ctx.mkApp(f,args), ctx.mkNot((BoolExpr)ctx.mkAnd((BoolExpr)funcs[1].getDef(), (BoolExpr)ctx.mkApp(f,args))));
			// 	spec = df.rewrite(spec, f);
			// 	s.add(ctx.mkNot((BoolExpr)spec));
			// 	spec = ctx.mkOr((ctx.mkNot((BoolExpr)ctx.mkApp(f,args))), (BoolExpr)funcs[2].getDef());
			// 	spec = df.rewrite(spec, f);
			// 	s.add(ctx.mkNot((BoolExpr)spec));


			// }else{

				Expr spec = extractor.finalConstraint;
				// System.out.println("2 "+spec);
				for (String name : extractor.names) {
					FuncDecl f = extractor.rdcdRequests.get(name);
					Expr[] args = extractor.requestUsedArgs.get(name);
					Expr def = functions.get(name);
					DefinedFunc df = new DefinedFunc(ctx, args, def);
					spec = df.rewrite(spec, f);
				}
				// System.out.println("3 "+spec);
				// int j = 0;
				// for(Expr expr: extractor.vars.values()) {
				// 	spec = 	spec.substitute(expr, var[j]);
				// 	j = j + 1;
				// }

				//BoolExpr spec = max2Prop(functions);
				//BoolExpr spec = max3Prop(functions);

				s.push();
				s.add(ctx.mkNot((BoolExpr)spec));
				//System.out.println("Verifying... Formula: ");
				//System.out.println(s);
			}
			Status status = s.check();
			s.pop();
			
			return status;
			//return Status.UNSATISFIABLE;

		}

		public Model getLastModel() {
			return this.s.getModel();
		}

	}

	protected Verifier createVerifier() {
		return new Verifier();
	}

	// Previously VerifierDecoder.java
	public class VerifierDecoder {
		private Verifier vrfr;
		private Model model;
		private Expr[] vars;
		private int numVar;

		public VerifierDecoder(Verifier vrfr) {
			this.vrfr = vrfr;
			this.model = vrfr.getLastModel();
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

	protected VerifierDecoder createVerifierDecoder(Verifier vrfr) {
		return new VerifierDecoder(vrfr);
	}

	// Previously Synthesizer.java
	public class Synthesizer {

		public Solver s;
		public Optimize optimize;

		public Model m = null;
		public String lastFailReason;

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
				Expr eval = this.getEval(k);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			synchronized(env.counterExamples) {
				for (Expr[] params : env.counterExamples) {
					Expr[] newParas = new Expr[params.length];
					for (int i = 0; i < params.length; i++) {
						newParas[i] = params[i].translate(ctx);
					}
					BoolExpr expandSpec = (BoolExpr) spec.substitute(extractor.vars.values().toArray(new Expr[extractor.vars.size()]), newParas);
					q = ctx.mkAnd(q, expandSpec);
				}
			}

			s.add(q);

			Status sts = s.check();

			if (sts == Status.SATISFIABLE) {
				m = s.getModel();
			}

			s.pop();
			return sts;

		}

		protected Expr getEval(int funcIndex) {
			return expand.generateEval(funcIndex, 0);
		}

		public Status synthesisGeneral() {

			s.push();

			Expr spec = extractor.finalConstraint;

			// CondBound not implemented for general yet
			// Currently expandCoefficientGeneral only applies term range restrictions and overall valid
			BoolExpr q = expand.expandCoefficientGeneral();
			//BoolExpr q = expand.expandCoefficient(condBound);

			int k = 0;
			for (String name : extractor.names) {
				// Currently preprocessing is disabled, using dummy
				FuncDecl f = extractor.rdcdRequests.get(name);
				Expr[] var = extractor.requestUsedArgs.get(name);
				Expr eval = expand.interpretGeneral(k);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			synchronized (env.counterExamples) {
				for (Expr[] params : env.counterExamples) {
					Expr[] newParas = new Expr[params.length];
					for (int i = 0; i < params.length; i++) {
						newParas[i] = params[i].translate(ctx);
					}
					BoolExpr expandSpec = (BoolExpr) spec.substitute(extractor.vars.values().toArray(new Expr[extractor.vars.size()]), newParas);
					q = ctx.mkAnd(q, expandSpec);
				}
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

			synchronized(env.counterExamples) {
				for (Expr[] params : env.counterExamples) {
					Expr[] newParas = new Expr[params.length];
					for (int i = 0; i < params.length; i++) {
						newParas[i] = params[i].translate(ctx);
					}
					BoolExpr expandSpec = (BoolExpr) spec.substitute(extractor.vars.values().toArray(new Expr[extractor.vars.size()]), newParas);
					q = ctx.mkAnd(q, expandSpec);
				}
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

	protected Synthesizer createSynthesizer() {
		return new Synthesizer();
	}

	// Previously SynthDecoder.java
	public class SynthDecoder {

		protected Synthesizer synth;
		protected Model model;
		protected IntExpr[][][] c;
		protected IntExpr[][] t;

		public SynthDecoder(Synthesizer synth) {
			this.synth = synth;
			this.model = synth.getLastModel();
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

		public void expandFunctions(Map<String, int[]> generalFunc, Map<String, ASTGeneral> ASTs) {
			int i = 0;
			for (String name: extractor.names) {
				int[] terms = generalFunc.get(name);
				ASTs.put(name, expand.expandGeneral(i, terms));
				i++;
			}
		}

		public void interpretFunctions(Map<String, int[]> generalFunc, Map<String, Expr> functions) {
			int i = 0;
			for (String name: extractor.names) {
				int[] terms = generalFunc.get(name);
				functions.put(name, expand.interpretGeneral(i, terms));
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

	protected SynthDecoder createSynthDecoder(Synthesizer s) {
		return new SynthDecoder(s);
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
		synchronized(env.counterExamples) {
			for (int j = 0; j < examples.length; j++) {
				env.counterExamples.add(examples[j]);
			}
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
		if (fixedVectorLength > 0) {
			vectorBound = fixedVectorLength;
		}
		logger.warning("Condition bound not implemented for general");
		logger.warning("Using minFinite + minInfinite as timeout");
		//int condBound = 1;
		//if (fixedCond > 0) {
		//	condBound = fixedCond;
		//}
		//int condBoundInc = 1;
		//int searchRegions = 2;
		long startTime = System.currentTimeMillis();

		int k = 0;	//number of iterations

		//print out initial examples
		synchronized(env.counterExamples) {
			logger.info("Initial examples:" + Arrays.deepToString(env.counterExamples.toArray()));
		}

		// Subprocedure classes
		Verifier testVerifier = this.createVerifier();
		Synthesizer testSynthesizer = this.createSynthesizer();
		expand.setVectorBound(vectorBound);
		if (fixedVectorLength > 0) {
			if (!expand.isInterpretableNow()) {
				logger.info(String.format("vectorLength %d uninterpretable, exited", vectorBound));
				return;
			}
		} else {
			while(!expand.isInterpretableNow()) {
				logger.info(String.format("vectorLength %d uninterpretable, exited", vectorBound));
				vectorBound++;
				expand.setVectorBound(vectorBound);
			}
		}

		while(running) {

			k = k + 1;

            if (this.iterLimit > 0 && k > this.iterLimit) {
                logger.info("Iteration Limit Hit, returning without a result.");
                this.results = null;
                return;
            }

			logger.info("Start synthesizing");

			Status synth = Status.UNKNOWN;

			if (maxsmtFlag) {
				logger.warning("MaxSMT not implemented for General Synthesis");
			}

			synth = testSynthesizer.synthesisGeneral();

			//print out for debug
			logger.info("Synthesis Done");

			if (synth == null) {
				logger.info("Synthesizer actively exited synthesis due to " + testSynthesizer.lastFailReason);
				if (fixedVectorLength > 0){
					logger.info(String.format("Exited vectorLength %d due to ", vectorBound) + testSynthesizer.lastFailReason);
					return;
				} else {
                    logger.info("Iteration : " + k);
					continue;
				}
			}

			if (synth == Status.UNSATISFIABLE) {
				logger.info("Synthesizer : Unsatisfiable");
				logger.info(String.format("Exited vectorLength %d due to UNSAT", vectorBound));
				if (fixedVectorLength > 0) {
					return;
				} else {
					vectorBound++;
					expand.setVectorBound(vectorBound);
					while(!expand.isInterpretableNow()) {
						logger.info(String.format("vectorLength %d uninterpretable, exited", vectorBound));
						vectorBound++;
						expand.setVectorBound(vectorBound);
					}
					startTime = System.currentTimeMillis();
                    logger.info("Iteration : " + k);
					continue;
				}
			} else if (synth == Status.UNKNOWN) {
				logger.severe("Synthesizer Error : Unknown");
				return;
			} else if (synth == Status.SATISFIABLE) {
				//logger.info(testSynthesizer.s.getModel());	//for test only
				SynthDecoder synthDecoder = this.createSynthDecoder(testSynthesizer);
				//print out for debug
				logger.info("Start decoding synthesizer output");
				synthDecoder.generateFuncGeneral(generalFuncs);
				synthDecoder.interpretFunctions(generalFuncs, functions);
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
				SynthDecoder synthDecoder = this.createSynthDecoder(testSynthesizer);
				synthDecoder.expandFunctions(generalFuncs, ASTs);
				results = new DefinedFunc[functions.size()];
				int i = 0;
				for (String name : extractor.rdcdRequests.keySet()) {
					Expr def = functions.get(name);
					ASTGeneral ast = ASTs.get(name);
					results[i] = new DefinedFunc(ctx, name, extractor.requestArgs.get(name), def, ast);
					logger.info("Done, Synthesized function(s):" + Arrays.toString(results));
                    logger.info(String.format("Total iteration count: %d", k));
					i = i + 1;
				}
				if (fixedVectorLength > 0) {
					synchronized(env) {
						env.notify();
					}
				}
				return;

			} else if (v == Status.UNKNOWN) {
				logger.severe("Verifier Error : Unknown");
				return;

			} else if (v == Status.SATISFIABLE) {

				logger.info("Verifier results:" + testVerifier.s.getModel());	//for test only
				VerifierDecoder decoder = this.createVerifierDecoder(testVerifier);

				Expr[] cntrExmp = decoder.decode();
				synchronized(env.counterExamples) {
					env.counterExamples.add(cntrExmp);
					logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(env.counterExamples.toArray()));
				}
			}

			if (System.currentTimeMillis() - startTime > 60000 * (this.minInfinite + this.minFinite)) {
				if (fixedVectorLength > 0) {
					return;
				} else {
					vectorBound++;
					expand.setVectorBound(vectorBound);
					while(!expand.isInterpretableNow()) {
						logger.info(String.format("vectorLength %d uninterpretable, exited", vectorBound));
						vectorBound++;
						expand.setVectorBound(vectorBound);
					}
					startTime = System.currentTimeMillis();
                    logger.info("Iteration : " + k);
					continue;
				}
			}
			logger.info("Iteration : " + k);
		}
	}

	public void resetBounds() {
		switch(env.feedType) {
			case ALLINONE:
				logger.info("Resetting all bounds to 1");
				heightBound = 1;
				expand.setHeightBound(heightBound);
				condBound = 1;
				condBoundInc = 1;
				break;
			case HEIGHTONLY:
				logger.info("Resetting 1D Producer");
				pdc1D.reset();
				break;
		}
	}

	public void resetBoundsGeneral() {
		switch(env.feedType) {
			case ALLINONE:
				logger.info("Resetting all bounds to 1");
				vectorBound = 1;
				expand.setVectorBound(vectorBound);
				break;
			case HEIGHTONLY:
				logger.info("Resetting 1D Producer");
				pdc1D.reset();
				break;
		}

	}

	public void cegis() {

		boolean flag = true;
		if (fixedHeight > 0) {
			heightBound = fixedHeight;
		}
		if (fixedCond > 0) {
			condBound = fixedCond;
		}
		long startTime = System.currentTimeMillis();

		int k = 0;	//number of iterations

		//print out initial examples
		synchronized(env.counterExamples) {
			logger.info("Initial examples:" + Arrays.deepToString(env.counterExamples.toArray()));
		}

		// Subprocedure classes
		Verifier testVerifier = this.createVerifier();
		Synthesizer testSynthesizer = this.createSynthesizer();
		expand = new Expand(ctx, extractor);
		expand.setHeightBound(heightBound);

		while(flag && running) {

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
                        logger.info(String.format("Total iteration count: %d", k));
						i = i + 1;
					}
					flag = false;
					if (fixedCond > 0 || fixedHeight > 0) {
						synchronized(env) {
							env.notify();
						}
					}

				} else if (v == Status.UNKNOWN) {
					logger.severe("Verifier Error : Unknown");
					flag = false;

				} else if (v == Status.SATISFIABLE) {

					logger.info("Verifier results:" + testVerifier.s.getModel());	//for test only
					VerifierDecoder decoder = this.createVerifierDecoder(testVerifier);

					Expr[] cntrExmp = decoder.decode();
					synchronized(env.counterExamples) {
						env.counterExamples.add(cntrExmp);
						//print out for debug
						logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(env.counterExamples.toArray()));
					}

					// if (k >= 10) {
					// 	break;
					// }

					boolean unsat = true;

					while(unsat && flag && running) {

						k = k + 1;

			            if (this.iterLimit > 0 && k > this.iterLimit) {
			                logger.info("Iteration Limit Hit, returning without a result.");
			                this.results = null;
			                return;
			            }
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

						if (synth == null) {
							logger.info("Synthesizer actively exited synthesis due to " + testSynthesizer.lastFailReason);
							if (fixedCond > 0 || fixedHeight > 0) {
								logger.info(String.format("Exited height %d, cond %d due to ", fixedHeight, fixedCond) + testSynthesizer.lastFailReason);
								return;
							} else {
								continue;
							}
						}

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
							SynthDecoder synthDecoder = this.createSynthDecoder(testSynthesizer);
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

					logger.info("Iteration : " + k);
					}
				}
		}
	}
}
