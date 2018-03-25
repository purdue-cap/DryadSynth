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
	private Producer1D pdc1D = null;
	private Producer2D pdc2D = null;
	private Object condition = null;

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

		this.functions = new LinkedHashMap<String, Expr>();

		for(String name : extractor.names) {
			FuncDecl func = extractor.rdcdRequests.get(name);

			if (func.getRange().toString().equals("Bool")) {
				functions.put(name, ctx.mkTrue());
			} else {
				functions.put(name, ctx.mkInt(0));
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

		while(flag && running) {

			k = k + 1;

			logger.info("Start verifying");
			Verifier testVerifier = new Verifier(ctx, extractor, logger);

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
					VerifierDecoder decoder = new VerifierDecoder(ctx, testVerifier.s.getModel(), extractor.vars.values().toArray(new Expr[extractor.vars.size()]));

					Expr[] cntrExmp = decoder.decode();
					counterExamples.add(cntrExmp);
					//print out for debug
					logger.info("Verifier satisfiable, Counter example(s):" + Arrays.deepToString(counterExamples.toArray()));

					// if (k >= 10) {
					// 	break;
					// }

					boolean unsat = true;

					while(unsat && flag && running) {

						Synthesizer testSynthesizer = new Synthesizer(ctx, counterExamples, heightBound, extractor, logger);
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
							SynthDecoder synthDecoder = new SynthDecoder(ctx, testSynthesizer.getLastModel(), testSynthesizer.e.getCoefficients(), extractor);
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
