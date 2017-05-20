import java.util.*;
import com.microsoft.z3.*;

public class Cegis {
	
	private Context ctx;
	private int numVar;
	private int numFunc;

	public IntExpr[] var;
	public ArithExpr[] functions;
	public HashSet<IntExpr[]> counterExamples;

	public Cegis(Context ctx, int numVar, int numFunc) {
		this.ctx = ctx;
		this.numVar = numVar;
		this.numFunc = numFunc;

		var = new IntExpr[numVar];
		functions = new ArithExpr[numFunc];
		counterExamples = new HashSet<IntExpr[]>();

		init();
	}

	public void init() {
		for (int i = 0; i < numVar; i++) {
			var[i] = ctx.mkIntConst("var" + i);
		}

		for (int i = 0; i < numFunc; i++) {
			functions[i] = ctx.mkInt(0);
		}
	}

	public void cegis() {
		
		boolean flag = true;
		int k = 0;

		while(flag) {

			k = k + 1;

			Verifier testVerifier = new Verifier(ctx, numVar, numFunc, var);
			//testVerifier.s.push();
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
					VerifierDecoder decoder = new VerifierDecoder(ctx, testVerifier.s.getModel(), numVar, var);
					//testVerifier.s.pop();
					IntExpr[] cntrExmp = decoder.decode();
					counterExamples.add(cntrExmp);
					//print out for debug
					System.out.println("Verifier satisfiable! Here is all the counter example: ");
					for (IntExpr[] params : counterExamples) {
						for (int i = 0; i < numVar; i++) {
							System.out.println("var" + i + " : " + params[i]);
						}
						System.out.println();
					}

					/*if (k == 6) {
						counterExamples.clear();
						IntExpr[] var1 = {ctx.mkInt(-2), ctx.mkInt(-2)};
						IntExpr[] var2 = {ctx.mkInt(0), ctx.mkInt(-3)};
						IntExpr[] var3 = {ctx.mkInt(0), ctx.mkInt(-1)};
						IntExpr[] var4 = {ctx.mkInt(-1), ctx.mkInt(-1)};
						IntExpr[] var5 = {ctx.mkInt(-3), ctx.mkInt(0)};
						IntExpr[] var6 = {ctx.mkInt(-5), ctx.mkInt(2)};
						counterExamples.add(var1);
						counterExamples.add(var2);
						counterExamples.add(var3);
						counterExamples.add(var4);
						counterExamples.add(var5);
						counterExamples.add(var6);
					}*/

					Synthesizer testSynthesizer = new Synthesizer(ctx, numVar, numFunc, counterExamples);
					//print out for debug
					System.out.println("Start synthesizing............");
					//testSynthesizer.s.push();
					Status synth = testSynthesizer.synthesis();
					//print out for debug
					System.out.println("Synthesizing Done!");

					if (synth == Status.UNSATISFIABLE) {
						System.out.println("Synthesizer Error : Unsatisfiable!");
						flag = false;
					} else if (synth == Status.UNKNOWN) {
						System.out.println("Synthesizer Error : Unknown!");
						flag = false;
					} else if (synth == Status.SATISFIABLE) {
						SynthDecoder synthDecoder = new SynthDecoder(ctx, testSynthesizer.s.getModel(), testSynthesizer.e.getValid(), testSynthesizer.e.getCoefficients(), testSynthesizer.bound, numVar, numFunc);
						//testSynthesizer.s.pop();
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

				/*if (k == 6) {
					flag = false;
				}*/

		}
		

	}

}