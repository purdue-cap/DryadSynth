import java.util.*;
//import java.util.Random;
import com.microsoft.z3.*;

public class Cegis {
	
	private Context ctx;
	private int numVar;
	private int numFunc;

	public IntExpr[][] var;
	public ArithExpr[] functions;
	public HashSet<IntExpr[][]> counterExamples;

	public Cegis(Context ctx, int numVar, int numFunc) {
		this.ctx = ctx;
		this.numVar = numVar;
		this.numFunc = numFunc;

		var = new IntExpr[numFunc][numVar];
		functions = new ArithExpr[numFunc];
		counterExamples = new HashSet<IntExpr[][]>();

		init();
		//addRandomInitialExamples(numVar);
	}

	public void init() {
		for (int j = 0; j < numFunc; j++) {
			for (int i = 0; i < numVar; i++) {
				var[j][i] = ctx.mkIntConst("f" + j + "_" + "var" + i);
			}
		}

		for (int i = 0; i < numFunc; i++) {
			functions[i] = ctx.mkInt(0);
		}
	}

	public void addRandomInitialExamples(int numVar) {

		//int numExamples = (int)Math.pow(4, numVar) + 1;
		//int numExamples = (int)Math.pow(3, numVar) + 1;
		//int numExamples = (int)Math.pow(2, numVar) + 1;
		int numExamples = 10;

		for (int i = 0; i < numExamples; i++) {
			IntExpr[][] randomExample = new IntExpr[1][numVar];
			for (int j = 0; j < numVar; j++) {
				Random rand = new Random();
				int n;
				int randomFlag = rand.nextInt(10);
				if (randomFlag%2 == 0) {
					n = rand.nextInt(10);
				} else {
					n = -rand.nextInt(10);

				}
				randomExample[0][j] = ctx.mkInt(n);
			}
			counterExamples.add(randomExample);
		}
		
	}

	public void cegis() {
		
		boolean flag = true;
		int heightBound = 1;

		int k = 0;	//number of iterations

		//print out initial examples
		for (IntExpr[][] example : counterExamples) {
			for (int i = 0; i < numVar; i++) {
				System.out.println("initial example: var" + i + " : " + example[0][i]);
			}
			System.out.println();
		}

		while(flag) {

			k = k + 1;

			System.out.println("Start verifying............");
			Verifier testVerifier = new Verifier(ctx, numVar, numFunc, var);

			Status v = testVerifier.verify(functions);

			if (v == Status.UNSATISFIABLE) {
					System.out.println("Done! Here is the function: ");
					for (int i = 0; i < numFunc; i++) {
						System.out.println("f" + i + " : " + functions[i]);
					}
					flag = false;
					
				} else if (v == Status.UNKNOWN) {
					System.out.println("Verifier Error : Unknown!");
					flag = false;

				} else if (v == Status.SATISFIABLE) {
					
					System.out.println(testVerifier.s.getModel());
					VerifierDecoder decoder = new VerifierDecoder(ctx, testVerifier.s.getModel(), numVar, numFunc, var);

					IntExpr[][] cntrExmp = decoder.decode();
					counterExamples.add(cntrExmp);
					//print out for debug
					System.out.println("Verifier satisfiable! Here is all the counter example: ");
					for (IntExpr[][] params : counterExamples) {
						for (int i = 0; i < numVar; i++) {
							System.out.println("var" + i + " : " + params[0][i]);
						}
						System.out.println();
					}

					//for test only
					//if (k >= 2) {
					//	break;
					//}

					boolean unsat = true;

					while(unsat) {

						Synthesizer testSynthesizer = new Synthesizer(ctx, numVar, numFunc, counterExamples, heightBound);
						//print out for debug
						System.out.println("Start synthesizing............");

						Status synth = testSynthesizer.synthesis();
						//print out for debug
						System.out.println("Synthesis Done!");

						if (synth == Status.UNSATISFIABLE) {
							System.out.println("Synthesizer Error : Unsatisfiable!");
							heightBound = heightBound + 1;
							//flag = false;
						} else if (synth == Status.UNKNOWN) {
							System.out.println("Synthesizer Error : Unknown!");
							flag = false;
							unsat = false;
						} else if (synth == Status.SATISFIABLE) {

							unsat = false;
							//flag = false;	//for test only

							//System.out.println(testSynthesizer.s.getModel());
							SynthDecoder synthDecoder = new SynthDecoder(ctx, testSynthesizer.s.getModel(), testSynthesizer.e.getValid(), testSynthesizer.e.getCoefficients(), testSynthesizer.bound, numVar, numFunc);
							//print out for debug
							System.out.println("Start decoding synthesizer output............");
							functions = synthDecoder.generateFunction(var);
							//print out for debug
							System.out.println("Synthesizer output decode done!");
							//print out for debug
							for (int i = 0; i < numFunc; i++) {
								System.out.println("f" + i + " : " + functions[i]);
							}
							System.out.println();
						}
					}

				}

			System.out.println("Iteration : " + k);
			System.out.println();

		}
		

	}

}