import java.util.*;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class Cegis extends Thread{

	private Context ctx;
	private SygusExtractor extractor;
	private int numVar;
	private int numV;
	private int numFunc;
	private BoolExpr finalConstraint;
	private String returnType = null;
	private Logger logger;
	private int fixedHeight = -1;
	private int fixedCond = -1;
	private Producer1D pdc1D = null;
	private Producer2D pdc2D = null;
	private Object condition = null;

	public IntExpr[] var;
	public Expr[] functions;
	public HashSet<IntExpr[]> counterExamples;
	public volatile DefinedFunc[] results = null;
	public volatile boolean running = true;

	public Cegis(SygusExtractor extractor, int fixedHeight, int fixedCond, Logger logger) {
		this(new Context(), extractor, logger);
		this.fixedHeight = fixedHeight;
		this.fixedCond = fixedCond;
	}

	public Cegis(SygusExtractor extractor, Producer1D pdc1D, Object condition, Logger logger) {
		this(new Context(), extractor, logger);
		this.pdc1D = pdc1D;
		this.condition = condition;
	}

	public Cegis(SygusExtractor extractor, Producer2D pdc2D, Object condition, Logger logger) {
		this(new Context(), extractor, logger);
		this.pdc2D = pdc2D;
		this.condition = condition;
	}

	public Cegis(Context ctx, SygusExtractor extractor, Logger logger) {
		this.ctx = ctx;
		this.extractor = extractor.translate(ctx);
		this.logger = logger;

		ArrayList<Integer> argsNumList = new ArrayList<Integer>();
		for(FuncDecl func : extractor.requests.values()) {
			Integer argsNum = func.getDomain().length;
			argsNumList.add(argsNum);

			if (func.getRange().equals(ctx.mkBoolSort())) {
				this.returnType = "INV";
			} else {
				this.returnType = "CLIA";
			}
		}

		this.numV = Collections.max(argsNumList);	//number of all args in synthesized function
		this.numVar = extractor.vars.size();	//number of all declared variables
		this.numFunc = extractor.requests.size();
		this.finalConstraint = extractor.finalConstraint;

		var = new IntExpr[numVar];
		functions = new Expr[numFunc];
		counterExamples = new HashSet<IntExpr[]>();

		init();
		//addRandomInitialExamples();
		//addSimpleExamples();
	}

	public void init() {
		for (int i = 0; i < numVar; i++) {
			var[i] = ctx.mkIntConst("var" + i);
		}

		for (int i = 0; i < numFunc; i++) {
			if (returnType.equals("INV")) {
				functions[i] = ctx.mkTrue();
			} else {
				functions[i] = ctx.mkInt(0);
			}

		}
	}

	public void addRandomInitialExamples() {

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

	public IntExpr[][] addSimpleExamplesRecursive(int nv) {
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
		IntExpr[][] examples = addSimpleExamplesRecursive(numVar);
		for (int j = 0; j < examples.length; j++) {
			counterExamples.add(examples[j]);
		}
	}

	public void run() {
		if (pdc1D != null) {
			while (results == null) {
				fixedHeight = pdc1D.get();
				cegis();
			}
		} else if (pdc2D != null) {
			while (results == null) {
				int[] args = pdc2D.get();
				fixedHeight = args[0];
				fixedCond = args[1];
				cegis();
			}
		} else {
			cegis();
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
		int iterationCntr = 0;
		long startTime = System.currentTimeMillis();

		int k = 0;	//number of iterations

		//print out initial examples
		logger.info("Initial examples:" + Arrays.deepToString(counterExamples.toArray()));

		while(flag && running) {

			k = k + 1;

			logger.info("Start verifying");
			Verifier testVerifier = new Verifier(ctx, returnType, numVar, numV, numFunc, var, extractor);

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
					String[] names = extractor.requests.keySet().toArray(new String[numFunc]);
					Expr[] readableVars = extractor.vars.values().toArray(new Expr[numVar]);
					results = new DefinedFunc[numFunc];
					for (int i = 0; i < numFunc; i++) {
						results[i] = new DefinedFunc(ctx, names[i], readableVars,
						 								functions[i].substitute(var, readableVars));
						if (fixedCond <= 0 && fixedHeight <= 0) {
							System.out.println(results[i]);
						}
						logger.info("Done, Synthesized function(s):" + Arrays.toString(results));
					}
					flag = false;
					condition.notify();

				} else if (v == Status.UNKNOWN) {
					logger.severe("Verifier Error : Unknown");
					flag = false;

				} else if (v == Status.SATISFIABLE) {

					logger.info("Verifier results:" + testVerifier.s.getModel());	//for test only
					VerifierDecoder decoder = new VerifierDecoder(ctx, testVerifier.s.getModel(), numVar, var);

					IntExpr[] cntrExmp = decoder.decode();
					counterExamples.add(cntrExmp);
					//print out for debug
					logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(counterExamples.toArray()));

					//for test only
					//if (k >= 100) {
					//	break;
					//}

					boolean unsat = true;

					while(unsat && flag && running) {

						Synthesizer testSynthesizer = new Synthesizer(ctx, returnType, numVar, numV, numFunc, counterExamples, heightBound, extractor);
						//print out for debug
						logger.info("Start synthesizing");

						Status synth = testSynthesizer.synthesis(condBound);
						//print out for debug
						logger.info("Synthesis Done");

						if (synth == Status.UNSATISFIABLE) {
							startTime = System.currentTimeMillis();
							logger.info("Synthesizer : Unsatisfiable");
							if (fixedCond > 0) {
								return;
							}
							condBound = (int)Math.pow(64, condBoundInc);	//64
							logger.info("Synthesizer : Increase coefficient bound to " + condBound);

							condBoundInc = condBoundInc + 1;
							iterationCntr = 0;
							if (condBoundInc > 3) {		//for 2, >6		//for 4, >4	64 	//infinite 5
								if (fixedHeight > 0) {
									return;
								}
								heightBound = heightBound + 1;
								logger.info("Synthesizer : Increase height bound to " + heightBound);
								condBound = 1;
								if (fixedCond > 0) {
									condBound = fixedCond;
								}
								condBoundInc = 1;
							}
							//flag = false;
						} else if (synth == Status.UNKNOWN) {
							logger.info("Synthesizer Error : Unknown");
							flag = false;
							unsat = false;
						} else if (synth == Status.SATISFIABLE) {

							unsat = false;
							//flag = false;	//for test only

							//logger.info(testSynthesizer.s.getModel());	//for test only
							SynthDecoder synthDecoder = new SynthDecoder(ctx, returnType, testSynthesizer.s.getModel(), testSynthesizer.e.getCoefficients(), testSynthesizer.bound, numV, numFunc);
							//print out for debug
							logger.info("Start decoding synthesizer output");
							functions = synthDecoder.generateFunction(var);
							//print out for debug
							logger.info("Synthesizer output decode done");
							//print out for debug
							for (int i = 0; i < numFunc; i++) {
								logger.info("f" + i + " : " + functions[i]);
							}

							if (condBound == 64) {
								if (System.currentTimeMillis() - startTime > 240000) {
									if (fixedCond > 0) {
										return;
									}
									condBound = (int)Math.pow(64, condBoundInc);
									condBoundInc = condBoundInc + 1;
									logger.info("Synthesizer : Increase coefficient bound to " + condBound);
									startTime = System.currentTimeMillis();
								}
							}

							if (condBound == 4096) {
								if (System.currentTimeMillis() - startTime > 60000) {
									if (fixedHeight > 0) {
										return;
									}
									heightBound = heightBound + 1;
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
			logger.info("Iteration : " + k);
		}
	}
}
