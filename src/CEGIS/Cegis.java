import java.util.*;
import com.microsoft.z3.*;

public class Cegis {

	private Context ctx;
	private SygusExtractor extractor;
	private int numVar;
	private int numV;
	private int numFunc;
	private BoolExpr finalConstraint;
	private String returnType = null;
	private boolean debug;

	public IntExpr[] var;
	public Expr[] functions;
	public HashSet<IntExpr[]> counterExamples;

	public Cegis(Context ctx, SygusExtractor extractor) {
		this(ctx, extractor, false);
	}

	public Cegis(Context ctx, SygusExtractor extractor, boolean debug) {
		this.ctx = ctx;
		this.extractor = extractor;
		this.debug = debug;

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

	public void cegis() {

		boolean flag = true;
		int heightBound = 1;
		int condBound = 1;
		int condBoundInc = 1;
		int iterationCntr = 0;
		long startTime = System.currentTimeMillis();

		int k = 0;	//number of iterations

		//print out initial examples
		if (debug) {
			System.out.println("Initial examples:");
			for (IntExpr[] example : counterExamples) {
				System.out.print(Arrays.toString(example) + " ");
			}
			System.out.println();
		}

		while(flag) {

			k = k + 1;

			if (debug) {
				System.out.println("Start verifying............");
			}
			Verifier testVerifier = new Verifier(ctx, returnType, numVar, numV, numFunc, var, extractor);

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
					if (debug) {
						System.out.println("Done! Synthesized function(s): ");
					}
					String[] names = extractor.requests.keySet().toArray(new String[extractor.requests.size()]);
					Expr[] readableVars = extractor.vars.values().toArray(new Expr[extractor.vars.size()]);
					for (int i = 0; i < numFunc; i++) {
						System.out.println(new DefinedFunc(ctx, names[i], readableVars,
						 								functions[i].substitute(var, readableVars)));
					}
					flag = false;

				} else if (v == Status.UNKNOWN) {
					if (debug) {
						System.out.println("Verifier Error : Unknown!");
					}
					flag = false;

				} else if (v == Status.SATISFIABLE) {

					if (debug) {
						System.out.println(testVerifier.s.getModel());	//for test only
					}
					VerifierDecoder decoder = new VerifierDecoder(ctx, testVerifier.s.getModel(), numVar, var);

					IntExpr[] cntrExmp = decoder.decode();
					counterExamples.add(cntrExmp);
					if (debug) {
						//print out for debug
						System.out.println("Verifier satisfiable! Counter example(s): ");
						for (IntExpr[] params : counterExamples) {
							System.out.print(Arrays.toString(params) + " ");
						}
						System.out.println();
					}

					//for test only
					//if (k >= 100) {
					//	break;
					//}

					boolean unsat = true;

					while(unsat) {

						Synthesizer testSynthesizer = new Synthesizer(ctx, returnType, numVar, numV, numFunc, counterExamples, heightBound, extractor);
						if (debug) {
							//print out for debug
							System.out.println("Start synthesizing............");
						}

						Status synth = testSynthesizer.synthesis(condBound);
						if (debug) {
							//print out for debug
							System.out.println("Synthesis Done!");
						}

						if (synth == Status.UNSATISFIABLE) {
							startTime = System.currentTimeMillis();
							if (debug) {
								System.out.println("Synthesizer : Unsatisfiable!");
							}
							condBound = (int)Math.pow(64, condBoundInc);	//64
							if (debug) {
								System.out.println("Synthesizer : Increase coefficient bound to " + condBound);
							}

							condBoundInc = condBoundInc + 1;
							iterationCntr = 0;
							if (condBoundInc > 3) {		//for 2, >6		//for 4, >4	64 	//infinite 5
								heightBound = heightBound + 1;
								if (debug) {
									System.out.println("Synthesizer : Increase height bound to " + heightBound);
								}
								condBound = 1;
								condBoundInc = 1;
							}
							//flag = false;
						} else if (synth == Status.UNKNOWN) {
							if (debug) {
								System.out.println("Synthesizer Error : Unknown!");
							}
							flag = false;
							unsat = false;
						} else if (synth == Status.SATISFIABLE) {

							unsat = false;
							//flag = false;	//for test only

							//System.out.println(testSynthesizer.s.getModel());	//for test only
							SynthDecoder synthDecoder = new SynthDecoder(ctx, returnType, testSynthesizer.s.getModel(), testSynthesizer.e.getCoefficients(), testSynthesizer.bound, numV, numFunc);
							if (debug) {
								//print out for debug
								System.out.println("Start decoding synthesizer output............");
							}
							functions = synthDecoder.generateFunction(var);
							if (debug) {
								//print out for debug
								System.out.println("Synthesizer output decode done!");
								//print out for debug
								for (int i = 0; i < numFunc; i++) {
									System.out.println("f" + i + " : " + functions[i]);
								}
								System.out.println();
							}

							if (condBound == 64) {
								if (System.currentTimeMillis() - startTime > 240000) {
									condBound = (int)Math.pow(64, condBoundInc);
									condBoundInc = condBoundInc + 1;
									if (debug) {
										System.out.println("Synthesizer : Increase coefficient bound to " + condBound);
									}
									startTime = System.currentTimeMillis();
								}
							}

							if (condBound == 4096) {
								if (System.currentTimeMillis() - startTime > 60000) {
									condBound = (int)Math.pow(64, condBoundInc);
									heightBound = heightBound + 1;
									if (debug) {
										System.out.println("Synthesizer : Increase height bound to " + heightBound);
									}
									condBound = 1;
									condBoundInc = 1;
									startTime = System.currentTimeMillis();

								}
							}

						}

					}
				}
			if (debug) {
				System.out.println("Iteration : " + k);
				System.out.println();
			}
		}
	}
}
