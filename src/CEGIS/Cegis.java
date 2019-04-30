import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.enumerations.Z3_decl_kind;

public class Cegis extends Thread{

	protected Context ctx;
	protected SygusProblem problem;
	protected BoolExpr finalConstraint;
	protected Logger logger;
	protected int minFinite;
	protected int minInfinite;
	protected boolean maxsmtFlag;
	protected boolean enforceFHCEGIS;
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

	protected int iterCount = 0;

	protected boolean isGeneral = false;

    public int iterLimit = 0;
	public Map<String, int[]> generalFuncs;
	public Map<String, ASTGeneral> ASTs;

	public Map<String, Expr> functions;
	public volatile DefinedFunc[] results = null;
	public volatile boolean running = true;
	public volatile int resultHeight = 0;

	public Map<Expr, Map<Integer, DefinedFunc[]>> triedProblem = new LinkedHashMap<Expr, Map<Integer, DefinedFunc[]>>();	// should be shared in env in multithread version

	public enum ProbType {
        POSMIX,
        NEGMIX,
        NORMAL
        ;
    }

	public Cegis(CEGISEnv env, Logger logger) {
		this(new Context(), env, logger);
	}

	public Cegis(Context ctx, CEGISEnv env, Logger logger){
		this.ctx = ctx;
		this.env = env;
		this.problem = env.problem.translate(ctx);
		this.logger = logger;
		this.minFinite = env.minFinite;
		this.minInfinite = env.minInfinite;
		this.maxsmtFlag = env.maxsmtFlag;
		this.enforceFHCEGIS = env.enforceFHCEGIS;

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

		this.finalConstraint = problem.finalConstraint;

		this.generalFuncs = new LinkedHashMap<String, int[]>();
		this.ASTs = new LinkedHashMap<String, ASTGeneral>();
		this.functions = new LinkedHashMap<String, Expr>();

		if (!problem.isGeneral) {
			for(String name : problem.names) {
				FuncDecl func = problem.rdcdRequests.get(name);

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

	// Function to get counterExample set,
	// for subclass overriding
	public Set<Expr[]> getCE() {
		return this.env.counterExamples;
	}

	public Map<Expr, Set<Expr[]>> getCntrExp() {
		return this.env.cntrExpMap;
	}

	public void resetFunctions() {
		this.functions = new LinkedHashMap<String, Expr>();
		if (!problem.isGeneral) {
			for(String name : problem.names) {
				FuncDecl func = problem.rdcdRequests.get(name);

				if (func.getRange().toString().equals("Bool")) {
					functions.put(name, ctx.mkTrue());
				} else {
					functions.put(name, ctx.mkInt(0));
				}
			}
		}
	}

	public void addRandomInitialExamples() {
		int numVar = problem.vars.size();
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
			synchronized(getCE()) {
				getCE().add(randomExample);
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

			Expr spec = problem.finalConstraint;

			for (String name : problem.names) {
				FuncDecl f = problem.rdcdRequests.get(name);
				Expr[] args = problem.requestUsedArgs.get(name);
				Expr def = functions.get(name);
				DefinedFunc df = new DefinedFunc(ctx, args, def);
				spec = df.rewrite(spec, f);
			}

			// int j = 0;
			// for(Expr expr: problem.vars.values()) {
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
			this.numVar = problem.vars.size();
			this.vars = problem.vars.values().toArray(new Expr[numVar]);
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

			Expr spec = problem.finalConstraint;

			BoolExpr q = expand.expandCoefficient(condBound);

			int k = 0;
			for (String name : problem.names) {
				FuncDecl f = problem.rdcdRequests.get(name);
				Expr[] var = problem.requestUsedArgs.get(name);
				Expr eval = this.getEval(k);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			synchronized(getCE()) {
				for (Expr[] params : getCE()) {
					Expr[] newParas = new Expr[params.length];
					for (int i = 0; i < params.length; i++) {
						newParas[i] = params[i].translate(ctx);
					}
					BoolExpr expandSpec = (BoolExpr) spec.substitute(problem.vars.values().toArray(new Expr[problem.vars.size()]), newParas);
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

		public Status synthesisFixedHeight(int condBound, ProbType type) {

			s.push();

			Expr spec = problem.finalConstraint;

			BoolExpr q = expand.expandCoefficient(condBound);

			int k = 0;
			for (String name : problem.names) {
				FuncDecl f = problem.rdcdRequests.get(name);
				Expr[] var = problem.requestUsedArgs.get(name);
				Expr eval = this.getEval(k);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;

				// assume all f are boolean functions
				if (type == ProbType.POSMIX) {
					Expr ffalse = ctx.mkEq(f.apply(var), ctx.mkFalse());
					ffalse = definedfunc.rewrite(ffalse, f);
					q = ctx.mkAnd(q, (BoolExpr)ffalse);
					logger.info("For function: " + name + ". Added ffalse constraint: " + ffalse.toString());
				} else if (type == ProbType.NEGMIX) {
					Expr ftrue = ctx.mkEq(f.apply(var), ctx.mkTrue());
					ftrue = definedfunc.rewrite(ftrue, f);
					q = ctx.mkAnd(q, (BoolExpr)ftrue);
					logger.info("For function: " + name + ". Added ftrue constraint: " + ftrue.toString());
				}
			}

			synchronized(getCntrExp()) {
				for (Expr[] params : getCntrExp().get(problem.finalConstraint)) {
					Expr[] newParas = new Expr[params.length];
					for (int i = 0; i < params.length; i++) {
						newParas[i] = params[i].translate(ctx);
					}
					BoolExpr expandSpec = (BoolExpr) spec.substitute(problem.vars.values().toArray(new Expr[problem.vars.size()]), newParas);
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

			Expr spec = problem.finalConstraint;

			// CondBound not implemented for general yet
			// Currently expandCoefficientGeneral only applies term range restrictions and overall valid
			BoolExpr q = expand.expandCoefficientGeneral();
			//BoolExpr q = expand.expandCoefficient(condBound);

			int k = 0;
			for (String name : problem.names) {
				// Currently preprocessing is disabled, using dummy
				FuncDecl f = problem.rdcdRequests.get(name);
				Expr[] var = problem.requestUsedArgs.get(name);
				Expr eval = expand.interpretGeneral(k);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			synchronized (getCE()) {
				for (Expr[] params : getCE()) {
					Expr[] newParas = new Expr[params.length];
					for (int i = 0; i < params.length; i++) {
						newParas[i] = params[i].translate(ctx);
					}
					BoolExpr expandSpec = (BoolExpr) spec.substitute(problem.vars.values().toArray(new Expr[problem.vars.size()]), newParas);
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

			Expr spec = problem.finalConstraint;

			BoolExpr q = ctx.mkTrue();

			int k = 0;
			for (String name : problem.names) {
				FuncDecl f = problem.rdcdRequests.get(name);
				Expr[] var = problem.requestUsedArgs.get(name);
				Expr eval = expand.generateEval(k, 0);
				DefinedFunc definedfunc = new DefinedFunc(ctx, var, eval);
				spec = definedfunc.rewrite(spec, f);
				k = k + 1;
			}

			synchronized(getCE()) {
				for (Expr[] params : getCE()) {
					Expr[] newParas = new Expr[params.length];
					for (int i = 0; i < params.length; i++) {
						newParas[i] = params[i].translate(ctx);
					}
					BoolExpr expandSpec = (BoolExpr) spec.substitute(problem.vars.values().toArray(new Expr[problem.vars.size()]), newParas);
					q = ctx.mkAnd(q, expandSpec);
				}
			}

			optimize.Add(q);

			int numFunc = problem.names.size();
			int bound = expand.bound;
			IntExpr[][][] c = expand.getCoefficients();
			IntExpr[][][] absolute = new IntExpr[numFunc][bound][0];

			for (int l = 0; l < numFunc; l++) {
				for (int j = 0; j < bound; j++) {
					String name = problem.names.get(l);
					int argCount = problem.requestUsedArgs.get(name).length;
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

					Expr[] args = problem.requestUsedArgs.get(problem.names.get(k));
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
						boolean isINV = problem.rdcdRequests.get(problem.names.get(j)).getRange().toString().equals("Bool");
						if (isINV) {
							f[j][i] = ctx.mkITE(cond, ctx.mkTrue(), ctx.mkFalse());
						} else {
							f[j][i] = p[j][i];
						}

					}
				}
				functions.put(problem.names.get(j), f[j][0].simplify());
			}

		}

		public void generateFuncGeneral(Map<String, int[]> generalFunc) {
			int[][] terms = evaluateTerm();
			for (int i = 0; i < terms.length; i++) {
				generalFunc.put(problem.names.get(i), terms[i]);
			}
		}

		public void expandFunctions(Map<String, int[]> generalFunc, Map<String, ASTGeneral> ASTs) {
			int i = 0;
			for (String name: problem.names) {
				int[] terms = generalFunc.get(name);
				ASTs.put(name, expand.expandGeneral(i, terms));
				i++;
			}
		}

		public void interpretFunctions(Map<String, int[]> generalFunc, Map<String, Expr> functions) {
			int i = 0;
			for (String name: problem.names) {
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
		int numVar = problem.vars.size();
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
		int numVar = problem.vars.size();
		IntExpr[][] examples = addSimpleExamplesRecursive(numVar);
		synchronized(getCE()) {
			for (int j = 0; j < examples.length; j++) {
				getCE().add(examples[j]);
			}
		}
	}

	public void run() {
		env.runningThreads.incrementAndGet();
		if (problem.isGeneral) {
			logger.info(Thread.currentThread().getName() + " Started");
			logger.info("Starting general track CEGIS");
			// Initialize expand here for max data sharing
			expand = new Expand(ctx, problem);
			if (pdc1D != null) {
				while (results == null && running) {
					fixedVectorLength = pdc1D.get();
					logger.info("Started loop with fixedVectorLength = " + fixedVectorLength);
					cegisGeneral();
		            if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
						synchronized(env) {
							env.notify();
						}
						env.runningThreads.decrementAndGet();
		                return;
		            }
				}
				synchronized(env) {
					env.notify();
				}
			} else {
				cegisGeneral();
			}
			env.runningThreads.decrementAndGet();
			return;
		}

		//
		if (enforceFHCEGIS) {
			logger.info(Thread.currentThread().getName() + " Started");
			logger.info("Starting fixed height search CEGIS");
			SygusProblem origProblem = new SygusProblem(problem);
			if (pdc1D != null) {
				while (results == null && running) {
					fixedHeight = pdc1D.get();
					origProblem.searchHeight = fixedHeight;
					logger.info("Started loop with fixedHeight = " + fixedHeight);
					search(fixedHeight, origProblem, ProbType.NORMAL);
					if (getSolution(triedProblem.get(origProblem.finalConstraint)) != null) {
						logger.info("Found the results in height " + fixedHeight + ". Exit searching process.");
						results = getSolution(triedProblem.get(origProblem.finalConstraint));
						synchronized(env) {
							env.notify();
						}
						env.runningThreads.decrementAndGet();
		                return;
					}

		    //         if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
						// synchronized(env) {
						// 	env.notify();
						// }
						// env.runningThreads.decrementAndGet();
		    //             return;
		    //         }
				}
				synchronized(env) {
					env.notify();
				}
			} else {
				results = gradualSynth();
			}
			env.runningThreads.decrementAndGet();
			return;
		}
		//

		logger.info("Check for possible candidates from parser.");
		for (String name : problem.candidate.keySet()) {
			DefinedFunc df = problem.candidate.get(name);
			logger.info(String.format("Candidate for %s : %s", name, df.getDef()));
			functions.put(name, df.getDef());
		}
		logger.info(Thread.currentThread().getName() + " Started");

		if (pdc1D != null) {
			while (results == null && running) {
				fixedHeight = pdc1D.get();
				logger.info("Started loop with fixedHeight = " + fixedHeight);
				cegis();
	            if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
					synchronized(env) {
						env.notify();
					}
					env.runningThreads.decrementAndGet();
	                return;
	            }
			}
			synchronized(env) {
				env.notify();
			}
		} else if (pdc2D != null) {
			while (results == null && running) {
				int[] args = pdc2D.get();
				fixedHeight = args[0];
				fixedCond = args[1];
				logger.info("Started loop with fixedHeight = " + fixedHeight + ", fixedCond = " + fixedCond);
				cegis();
	            if (this.iterLimit > 0 && iterCount > this.iterLimit && this.results == null) {
					synchronized(env) {
						env.notify();
					}
					env.runningThreads.decrementAndGet();
	                return;
	            }
			}
			synchronized(env) {
				env.notify();
			}
		} else {
			cegis();
		}
		env.runningThreads.decrementAndGet();
	}

	private Verifier testVerifier = null;
	private Synthesizer testSynthesizer = null;

	public void cegisGeneral() {
		if (fixedVectorLength > 0) {
			vectorBound = fixedVectorLength;
		} else {
			vectorBound = 1;
		}
		logger.warning("Condition bound not implemented for general");
		logger.warning("Using minFinite + minInfinite as timeout");
		//int condBound = 1;
		//if (fixedCond > 0) {
		//	condBound = fixedCond;
		//}
		//int condBoundInc = 1;
		long startTime = System.currentTimeMillis();

		//print out initial examples
		synchronized(getCE()) {
			logger.info("Initial examples:" + Arrays.deepToString(getCE().toArray()));
		}

		// Subprocedure classes
		if (testVerifier == null) {
			testVerifier = this.createVerifier();
		}
		if (testSynthesizer == null) {
			testSynthesizer = this.createSynthesizer();
		}
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

			iterCount = iterCount + 1;

            if (this.iterLimit > 0 && iterCount > this.iterLimit) {
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
                    logger.info("Iteration : " + iterCount);
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
                    logger.info("Iteration : " + iterCount);
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
				for (String name : problem.names) {
					logger.info(name + " = " + Arrays.toString(generalFuncs.get(name)));
					logger.info(name + " : " + functions.get(name).toString());
				}

			}
			logger.info("Start verifying");

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
				SynthDecoder synthDecoder = this.createSynthDecoder(testSynthesizer);
				synthDecoder.expandFunctions(generalFuncs, ASTs);
				results = new DefinedFunc[problem.rdcdRequests.size()];
				int i = 0;
				for (String name : problem.rdcdRequests.keySet()) {
					Expr def = functions.get(name);
					ASTGeneral ast = ASTs.get(name);
					Expr[] args = problem.requestArgs.get(name);
					String[] strArgs = new String[args.length];
					for (int j = 0; j < args.length; j++) {
						strArgs[j] = args[j].toString();
					}
					results[i] = new DefinedFunc(ctx, name, args, def, strArgs, ast);
					resultHeight = vectorBound;
					logger.info("Done, Synthesized function(s):" + Arrays.toString(results));
                    logger.info(String.format("Total iteration count: %d", iterCount));
					i = i + 1;
				}
				return;

			} else if (v == Status.UNKNOWN) {
				logger.severe("Verifier Error : Unknown");
				return;

			} else if (v == Status.SATISFIABLE) {

				logger.info("Verifier results:" + testVerifier.s.getModel());	//for test only
				VerifierDecoder decoder = this.createVerifierDecoder(testVerifier);

				Expr[] cntrExmp = decoder.decode();
				synchronized(getCE()) {
					getCE().add(cntrExmp);
					logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(getCE().toArray()));
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
                    logger.info("Iteration : " + iterCount);
					continue;
				}
			}
			logger.info("Iteration : " + iterCount);
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

		if (fixedHeight > 0) {
			heightBound = fixedHeight;
		} else {
			heightBound = 1;
		}
		if (fixedCond > 0) {
			condBound = fixedCond;
		} else {
			condBound = 1;
			condBoundInc = 1;
		}
		long startTime = System.currentTimeMillis();

		//print out initial examples
		synchronized(getCE()) {
			logger.info("Initial examples:" + Arrays.deepToString(getCE().toArray()));
		}

		// Subprocedure classes
		if (testVerifier == null) {
			testVerifier = this.createVerifier();
		}
		if (testSynthesizer == null) {
			testSynthesizer = this.createSynthesizer();
		}
		if (expand == null) {
			expand = new Expand(ctx, problem);
		}
		expand.setHeightBound(heightBound);

		while(running) {

			iterCount = iterCount + 1;

            if (this.iterLimit > 0 && iterCount > this.iterLimit) {
                logger.info("Iteration Limit Hit, returning without a result.");
                this.results = null;
                return;
            }
			logger.info("Iteration : " + iterCount);

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
				continue;
			} else if (synth != Status.SATISFIABLE) {
				logger.severe("Synthesizer Error : Unknown");
				return;
			}

			SynthDecoder synthDecoder = this.createSynthDecoder(testSynthesizer);
			logger.info("Start decoding synthesizer output");
			synthDecoder.generateFunction(functions);
			logger.info("Synthesizer output decode done");
			for (String name : problem.names) {
				logger.info(name + " : " + functions.get(name).toString());
			}

			logger.info("Start verifying");

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
				results = new DefinedFunc[problem.rdcdRequests.size()];
				int i = 0;
				for (String name : problem.rdcdRequests.keySet()) {
					Expr def = functions.get(name);
					if (def.isBool()) {
						def = SygusFormatter.elimITE(this.ctx, def);
					}
					results[i] = new DefinedFunc(ctx, name, problem.requestArgs.get(name), def);
					resultHeight = heightBound;
					logger.info("Done, Synthesized function(s):" + Arrays.toString(results));
                    logger.info(String.format("Total iteration count: %d", iterCount));
					i = i + 1;
				}
				return;
			} else if (v != Status.SATISFIABLE) {
				logger.severe("Verifier Error : Unknown");
				return;
			}

			logger.info("Verifier results:" + testVerifier.s.getModel());
			VerifierDecoder decoder = this.createVerifierDecoder(testVerifier);

			Expr[] cntrExmp = decoder.decode();
			synchronized(getCE()) {
				getCE().add(cntrExmp);
				logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(getCE().toArray()));
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

	public DefinedFunc[] cegisFixedHeight(int fixedHeight, ProbType type) {		// counterexample sets to be modified

		iterCount = 0;

		if (fixedHeight > 0) {
			heightBound = fixedHeight;
		} else {
			heightBound = 1;
		}
		if (fixedCond > 0) {
			condBound = fixedCond;
		} else {
			condBound = 1;
			condBoundInc = 1;
		}
		long startTime = System.currentTimeMillis();

		// print out initial examples
		synchronized(getCntrExp()) {
			if (!getCntrExp().containsKey(problem.finalConstraint)) {
				Set<Expr[]> ce = new LinkedHashSet<Expr[]>();
				getCntrExp().put(problem.finalConstraint, ce);
			}
			logger.info("Initial examples:" + Arrays.deepToString(getCntrExp().get(problem.finalConstraint).toArray()));
		}

		// Subprocedure classes
		if (testVerifier == null) {
			testVerifier = this.createVerifier();
		}
		if (testSynthesizer == null) {
			testSynthesizer = this.createSynthesizer();
		}
		if (expand == null) {
			expand = new Expand(ctx, problem);
		}
		expand.setHeightBound(heightBound);

		resetFunctions();

		while(running) {

			logger.info("heightBound: " + heightBound + ". fixedHeight: " + fixedHeight);

			iterCount = iterCount + 1;

            if (this.iterLimit > 0 && iterCount > this.iterLimit) {
                logger.info("Iteration Limit Hit, returning without a result.");
                // this.results = null;
                return null;
            }
			logger.info("Iteration : " + iterCount);

			logger.info("Start synthesizing");

			Status synth = testSynthesizer.synthesisFixedHeight(condBound, type);

			// if (!maxsmtFlag) {
			// 	synth = testSynthesizer.synthesis(condBound);
			// } else {
			// 	synth = testSynthesizer.synthesisWithSMT();
			// }

			//print out for debug
			logger.info("Synthesis Done");

			if (synth == null) {
				logger.info("Synthesizer actively exited synthesis due to " + testSynthesizer.lastFailReason);
				if (fixedCond > 0 || fixedHeight > 0) {
					logger.info(String.format("Exited height %d, cond %d due to ", fixedHeight, fixedCond) + testSynthesizer.lastFailReason);
					return null;
				} else {
					continue;
				}
			}

			if (synth == Status.UNSATISFIABLE) {
				logger.info("Synthesizer : Unsatisfiable");
				if (fixedCond > 0) {
					logger.info(String.format("Exited height %d, cond %d due to UNSAT", fixedHeight, fixedCond));
					return null;
				}
				if (condBoundInc > searchRegions) {		//for 2, >5		//for 4, >3	64 	//infinite 5
					if (fixedHeight > 0) {
						logger.info(String.format("Exited height %d due to UNSAT", fixedHeight));
						return null;
					}
					// heightBound = heightBound + 1;
					// expand.setHeightBound(heightBound);
					// logger.info("Synthesizer : Increase height bound to " + heightBound);
					// condBound = 1;
					// if (fixedCond > 0) {
					// 	condBound = fixedCond;
					// }
					// condBoundInc = 1;
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
				continue;
			} else if (synth != Status.SATISFIABLE) {
				logger.severe("Synthesizer Error : Unknown");
				return null;
			}

			SynthDecoder synthDecoder = this.createSynthDecoder(testSynthesizer);
			logger.info("Start decoding synthesizer output");
			synthDecoder.generateFunction(functions);
			logger.info("Synthesizer output decode done");
			for (String name : problem.names) {
				logger.info(name + " : " + functions.get(name).toString());
			}

			logger.info("Start verifying");

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
				DefinedFunc[] func = new DefinedFunc[problem.rdcdRequests.size()];
				int i = 0;
				for (String name : problem.rdcdRequests.keySet()) {
					Expr def = functions.get(name);
					if (def.isBool()) {
						def = SygusFormatter.elimITE(this.ctx, def);
					}
					func[i] = new DefinedFunc(ctx, name, problem.requestArgs.get(name), def);
					// resultHeight = heightBound;
					logger.info("Done, Synthesized function(s):" + Arrays.toString(func));
                    logger.info(String.format("Total iteration count: %d", iterCount));
					i = i + 1;
				}
				return func;
			} else if (v != Status.SATISFIABLE) {
				logger.severe("Verifier Error : Unknown");
				return null;
			}

			logger.info("Verifier results:" + testVerifier.s.getModel());
			VerifierDecoder decoder = this.createVerifierDecoder(testVerifier);

			Expr[] cntrExmp = decoder.decode();
			synchronized(getCntrExp()) {
				Set<Expr[]> ce = getCntrExp().get(problem.finalConstraint);
				ce.add(cntrExmp);
				getCntrExp().put(problem.finalConstraint, ce);
				logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(getCntrExp().get(problem.finalConstraint).toArray()));
			}

			if (condBoundInc > 1) {
				if (condBoundInc <= searchRegions) {
					if (System.currentTimeMillis() - startTime > 60000 * this.minFinite) {
						if (fixedCond > 0) {
							logger.info(String.format("Exited height %d, cond %d due to TIMEOUT", fixedHeight, fixedCond));
							return null;
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
							return null;
						}
						// heightBound = heightBound + 1;
						// expand.setHeightBound(heightBound);
						// logger.info("Synthesizer : Increase height bound to " + heightBound);
						// condBound = 1;
						// if (fixedCond > 0) {
						// 	condBound = fixedCond;
						// }
						// condBoundInc = 1;
						// startTime = System.currentTimeMillis();

					}
				}
			}
		}

		return null;
	}

	public DefinedFunc[] gradualSynth() {
		int height = 1;
		boolean runningFlag = true;
		SygusProblem origProblem = new SygusProblem(problem);
		while (runningFlag) {
			origProblem.searchHeight = height;
			search(height, origProblem, ProbType.NORMAL);
			if (getSolution(triedProblem.get(origProblem.finalConstraint)) != null) {
				logger.info("Found the results in height " + height + ". Exit searching process.");
				runningFlag = false;
			}
			height = height + 1;
		}
		return getSolution(triedProblem.get(origProblem.finalConstraint));
	}

	public boolean isPos(Expr orexpr) {
		// assume functions are all boolean functions
		if (orexpr.isConst()) {
			return false;
		}
		if (orexpr.isApp()) {
			Expr[] args = orexpr.getArgs();
			if (orexpr.isNot()) {
				Expr inner = args[0];
                Expr[] innerArgs = inner.getArgs();
                if (inner.isNot()) {
                	return isPos(innerArgs[0]);
                }
			}
			if (orexpr.getFuncDecl().getDeclKind() == Z3_decl_kind.Z3_OP_UNINTERPRETED) {
				return true;
            } else {
            	return false;
            }
		}
		return false;
	}

	public boolean isNeg(Expr orexpr) {
		// assume functions are all boolean functions
		if (orexpr.isConst()) {
			return false;
		}
		if (orexpr.isApp()) {
			Expr[] args = orexpr.getArgs();
			if (orexpr.isNot()) {
				Expr inner = args[0];
                Expr[] innerArgs = inner.getArgs();
                if (inner.isNot()) {
                	return isNeg(innerArgs[0]);
                }
                if (inner.getFuncDecl().getDeclKind() == Z3_decl_kind.Z3_OP_UNINTERPRETED && !inner.isConst()) {
					return true;
	            } else {
	            	return false;
	            }
			}
		}
		return false;
	}

	public enum ExprType {
        POS, MIX, NEG;
    }

	public ExprType getExprType(Expr andexpr) {
		// assume andexpr is one of a conjunction from the cnf-converted formula
		Transf trans = new Transf(null, ctx);
		List<Expr> orList = trans.getArgsListForOr(andexpr);
		boolean pos = false;
		boolean neg = false;

		for (Expr orexpr : orList) {
			if (isPos(orexpr)) {
				pos = true;
			}
			if (isNeg(orexpr)) {
				neg = true;
			}
		}

		if (pos && neg) {
			return ExprType.MIX;
		} else if (pos) {
			return ExprType.POS;
		} else if (neg) {
			return ExprType.NEG;
		} else {
			// do not exist f.apply in andexpr, return the type to be mixed
			return ExprType.MIX;
		}
	}

	public SygusProblem getProbWithHeight(SygusProblem p, int ht) {
		SygusProblem newP = new SygusProblem(p);
		newP.searchHeight = ht;
		return newP;
	}

	public DefinedFunc[] getSolution(Map<Integer, DefinedFunc[]> map) {
		if (map == null) {
			return null;
		}
		for (DefinedFunc[] funcs : map.values()) {
			if (funcs != null) {
				return funcs;
			}
		}
		return null;
	}

	public void search(int height, SygusProblem prblm, ProbType type) {

		if (height <= 0) {
			logger.severe("Search height <= 0.");
		}

		if (height > 1) {

			SygusProblem smallerProb = getProbWithHeight(prblm, prblm.searchHeight - 1);
			search(height - 1, smallerProb, type);

			if (getSolution(triedProblem.get(prblm.finalConstraint)) != null) {
				logger.info(String.format("Find a smaller solution at searchHeight %d", height - 1));
				return;
			}

			// convert the finalConstraint to CNF
			BoolExpr constraint = prblm.finalConstraint;
			Transf trans = new Transf(null, ctx);
			BoolExpr cnf = (BoolExpr)trans.convertToCNF(constraint);
			cnf = (BoolExpr) cnf.simplify();		// may have unexpected behavior with simplify()

			List<Expr> andList = trans.getArgsListForAnd(cnf);

			// figure out the pos mix neg subformula
			List<Expr> posList = new ArrayList<Expr>();
			List<Expr> mixList = new ArrayList<Expr>();
			List<Expr> negList = new ArrayList<Expr>();

			for (Expr andexpr : andList) {
				ExprType exprtype = getExprType(andexpr);
				if (exprtype == ExprType.POS) {
					posList.add(andexpr);
				}
				if (exprtype == ExprType.NEG) {
					negList.add(andexpr);
				}
				if (exprtype == ExprType.MIX) {
					mixList.add(andexpr);
				}
			}

			// store pos mix neg subformula to three lists
			BoolExpr posExpr = ctx.mkAnd(posList.toArray(new BoolExpr[posList.size()]));
			BoolExpr mixExpr = ctx.mkAnd(mixList.toArray(new BoolExpr[mixList.size()]));
			BoolExpr negExpr = ctx.mkAnd(negList.toArray(new BoolExpr[negList.size()]));

			// // test region below
			// logger.info("Constraint: " + constraint.toString());
			// logger.info("Converted to CNF" + cnf.toString());
			// logger.info("Pos constraint: " + posExpr.toString());
			// logger.info("Mix constraint: " + mixExpr.toString());
			// logger.info("Neg constraint: " + negExpr.toString());
			// Solver testsolver = ctx.mkSolver();
			// testsolver.add(ctx.mkNot(ctx.mkEq(cnf, ctx.mkAnd(posExpr, mixExpr, negExpr))));
			// Status resultstatus = testsolver.check();
			// logger.info("checking result: " + resultstatus);
			// System.exit(0);
			// // test region above

			BoolExpr posMix = ctx.mkAnd(posExpr, mixExpr);
			SygusProblem posMixProb = new SygusProblem(smallerProb);
			posMixProb.finalConstraint = posMix;
			logger.info(String.format("Searching for partial solution for pos & mix at searchHeight %d", posMixProb.searchHeight));
			logger.info("Pos & Mix constraint: " + posMix);

			search(height - 1, posMixProb, ProbType.POSMIX);

			if (getSolution(triedProblem.get(posMixProb.finalConstraint)) != null) {
				// then construct a new problem
				// synthesize "f" that phi(f) => phi(E/\f)
				logger.info(String.format("Exists a partial solution for pos & mix at searchHeight %d", posMixProb.searchHeight));
				DefinedFunc[] subexpr = getSolution(triedProblem.get(posMixProb.finalConstraint));
				SygusProblem simpProb = getSimpProb(smallerProb, subexpr, true);

				logger.info(String.format("Searching with the help of partial solution for pos & mix at searchHeight %d", simpProb.searchHeight));
				search(height - 1, simpProb, type);

				if (getSolution(triedProblem.get(simpProb.finalConstraint)) != null) {	// if an f can be found
					// combime f and E together (get E \/ f), and put the results in the map
					DefinedFunc[] subf = getSolution(triedProblem.get(simpProb.finalConstraint));
					DefinedFunc[] combined = getCombinedResults(subexpr, subf, true);
					logger.info(String.format("Found solution with the help of pos & mix solution at searchHeight %d", simpProb.searchHeight));
					logger.info("The solution is :" + Arrays.toString(subf));
					logger.info("Combined solution is :" + Arrays.toString(combined));
					if (triedProblem.containsKey(prblm.finalConstraint)) {
						Map<Integer, DefinedFunc[]> solumap = triedProblem.get(prblm.finalConstraint);
						solumap.put(height - 1, combined);	// height does not matter if we have a solution
						triedProblem.put(prblm.finalConstraint, solumap);
					} else {
						Map<Integer, DefinedFunc[]> solumap = new LinkedHashMap<Integer, DefinedFunc[]>();
						solumap.put(height - 1, combined);
						triedProblem.put(prblm.finalConstraint, solumap);
					}
					return;
				}
			} else {
				logger.info(String.format("Did not find partial solution for pos & mix at searchHeight %d", posMixProb.searchHeight));
			}

			BoolExpr negMix = ctx.mkAnd(negExpr, mixExpr);
			SygusProblem negMixProb = new SygusProblem(smallerProb);
			negMixProb.finalConstraint = negMix;
			logger.info(String.format("Searching for partial solution for neg & mix at searchHeight %d", negMixProb.searchHeight));
			logger.info("Neg & Mix constraint: " + negMix);

			search(height - 1, negMixProb, ProbType.NEGMIX);

			if (getSolution(triedProblem.get(negMixProb.finalConstraint)) != null) {
				// then construct a new problem
				// synthesize "f" that phi(f) => phi(E\/f)
				logger.info(String.format("Exists a partial solution for neg & mix at searchHeight %d", negMixProb.searchHeight));
				DefinedFunc[] subexpr = getSolution(triedProblem.get(negMixProb.finalConstraint));
				SygusProblem simpProb = getSimpProb(smallerProb, subexpr, false);

				logger.info(String.format("Searching with the help of partial solution for neg & mix at searchHeight %d", simpProb.searchHeight));
				search(height - 1, simpProb, type);

				if (getSolution(triedProblem.get(simpProb.finalConstraint)) != null) {	// if an f can be found
					// combime f and E together (get E /\ f), and put the results in the map
					DefinedFunc[] subf = getSolution(triedProblem.get(simpProb.finalConstraint));
					DefinedFunc[] combined = getCombinedResults(subexpr, subf, false);
					logger.info(String.format("Found solution with the help of neg & mix solution at searchHeight %d", simpProb.searchHeight));
					logger.info("The solution is :" + Arrays.toString(subf));
					logger.info("Combined solution is :" + Arrays.toString(combined));
					if (triedProblem.containsKey(prblm.finalConstraint)) {
						Map<Integer, DefinedFunc[]> solumap = triedProblem.get(prblm.finalConstraint);
						solumap.put(height - 1, combined);	// height does not matter if we have a solution
						triedProblem.put(prblm.finalConstraint, solumap);
					} else {
						Map<Integer, DefinedFunc[]> solumap = new LinkedHashMap<Integer, DefinedFunc[]>();
						solumap.put(height - 1, combined);
						triedProblem.put(prblm.finalConstraint, solumap);
					}
					return;
				}
			} else {
				logger.info(String.format("Did not find partial solution for neg & mix at searchHeight %d", negMixProb.searchHeight));
			}
		}

		problem = getProbWithHeight(prblm, height);
		logger.info(String.format("Searching for full tree at searchHeight %d", problem.searchHeight));
		if (!triedProblem.containsKey(problem.finalConstraint) || !triedProblem.get(problem.finalConstraint).containsKey(height) ) {
			logger.info("At height " + height + ". Problem is not solved before: " + problem.finalConstraint.toString());
			DefinedFunc[] func = cegisFixedHeight(height, type);		// cegisFixedHeight should return DefinedFunc[]
			logger.info("Found synthesized function(s):" + Arrays.toString(func));
			if (triedProblem.containsKey(prblm.finalConstraint)) {
				Map<Integer, DefinedFunc[]> solumap = triedProblem.get(prblm.finalConstraint);
				solumap.put(height, func);	// height does not matter if we have a solution
				triedProblem.put(prblm.finalConstraint, solumap);
			} else {
				Map<Integer, DefinedFunc[]> solumap = new LinkedHashMap<Integer, DefinedFunc[]>();
				solumap.put(height, func);
				triedProblem.put(prblm.finalConstraint, solumap);
			}
			logger.info("Store solutions for later use.");
		} else {
			logger.info("At height " + height + ". Problem is solved before: " + problem.finalConstraint.toString());
			DefinedFunc[] func = getSolution(triedProblem.get(problem.finalConstraint));
			logger.info("The solution is :" + Arrays.toString(func));
			return;
		}
		
	}

	public SygusProblem getSimpProb(SygusProblem prblm, DefinedFunc[] subexpr, boolean pos) {
		// construct simplified problem for posmix partial solution if pos is true
		// construct simplified problem for negmix partial solution if pos is false
		BoolExpr rewritten = prblm.finalConstraint;

		for (String name : prblm.names) {
			FuncDecl f = prblm.rdcdRequests.get(name);
			Expr[] vars = prblm.requestUsedArgs.get(name);
			Expr fapp = f.apply(vars);
			boolean found = false;
			for (DefinedFunc e : subexpr) {
				if (e.getName().equals(name)) {
					// assume all to-be-synthesized functions are boolean functions
					Expr newDef = null;
					if (pos) {
						newDef = ctx.mkAnd((BoolExpr)e.getDef(), (BoolExpr)fapp);
					} else {
						newDef = ctx.mkOr((BoolExpr)e.getDef(), (BoolExpr)fapp);
					}
					DefinedFunc newf = new DefinedFunc(ctx, name, vars, newDef);
					rewritten = (BoolExpr)newf.rewrite(rewritten, f);
					found = true;
				}
			}
			if (!found) {
				logger.severe("Partial solution does not match original problem.");
			}
		}

		SygusProblem newProb = new SygusProblem(prblm);
		newProb.finalConstraint = rewritten;
		return newProb;
	}

	public DefinedFunc[] getCombinedResults(DefinedFunc[] subexpr, DefinedFunc[] subf, boolean pos) {
		// construct combined result for posmix partial solution if pos is true
		// construct combined result for negmix partial solution if pos is false
		if (subexpr.length != subf.length) {
			logger.severe("Partial solutions length do not match.");
		}

		DefinedFunc[] combined = new DefinedFunc[subf.length];

		for (int i = 0; i < subexpr.length; i++) {
			DefinedFunc e = subexpr[i];
			String name = e.getName();
			Expr[] args = e.getArgs();
			Expr edef = e.getDef();
			boolean found = false;

			for (DefinedFunc f : subf) {
				if (f.getName().equals(name)) {
					Expr newDef = null;
					Expr fdef = f.getDef();
					if (pos) {
						newDef = ctx.mkAnd((BoolExpr)edef, (BoolExpr)fdef);
					} else {
						newDef = ctx.mkOr((BoolExpr)edef, (BoolExpr)fdef);
					}
					combined[i] = new DefinedFunc(ctx, name, args, newDef);
				}
				found = true;
			}
			if (!found) {
				logger.severe("Partial solution does not match original problem.");
			}
		}

		return combined;
	}

}
