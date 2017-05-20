import java.util.*;
import com.microsoft.z3.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class Sygus {

	public static void main(String[] args) throws Exception {

		ExecutorService executor = Executors.newSingleThreadExecutor();
    	Future<?> future = executor.submit(new Runnable() {

        	@Override
        	public void run() {

				com.microsoft.z3.Global.ToggleWarningMessages(true);
			
				long startTime = System.currentTimeMillis();

				HashMap<String, String> cfg = new HashMap<String, String>();
				cfg.put("model", "true");
				Context ctx = new Context(cfg);

				int numVar = 2;
				int numFunc = 1;
				
				Cegis test = new Cegis(ctx, numVar, numFunc);
				test.cegis();

				/*IntExpr[] var = new IntExpr[numVar];
				for (int i = 0; i < numVar; i++) {
					var[i] = ctx.mkIntConst("var" + i);
				}
				ArithExpr[] functions = {ctx.mkInt(0)};
				//IntExpr[] counterExample = new IntExpr[numVar];

				Verifier testVerifier = new Verifier(ctx, numVar, numFunc, var);
				Status v = testVerifier.verify(functions);
				if (v == Status.UNSATISFIABLE) {
					System.out.println("Done!");
				} else if (v == Status.UNKNOWN) {
					System.out.println("Unknown!");
				} else if (v == Status.SATISFIABLE) {
					VerifierDecoder decoder = new VerifierDecoder(ctx, testVerifier.s.getModel(), numVar, var);
					decoder.printOutput();
				}*/



				/*int flag = testVerifier.verify(functions);
				if (flag == 0) {
					counterExample = testVerifier.getCounterExample();
					for (int i = 0; i < numVar; i++) {
						System.out.println("counterExample: " + counterExample[i]);
					}
				} else {
					System.out.println("Error");
				}*/



				/*Synthesizer test = new Synthesizer(ctx, 2, 1);

				HashSet<IntExpr[]> counterExamples = new HashSet<IntExpr[]>();
				IntExpr[] var1 = {ctx.mkInt(1), ctx.mkInt(2)};
				IntExpr[] var2 = {ctx.mkInt(2), ctx.mkInt(1)};
				counterExamples.add(var1);
				counterExamples.add(var2);

				test.synthesis(counterExamples);*/

				long estimatedTime = System.currentTimeMillis() - startTime;
				System.out.println("Runtime: " + estimatedTime);

        	}
    	});

		try {
			future.get(120, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			System.out.println("Terminated!");
        	future.cancel(true);
        	System.exit(0);
    	}

        executor.shutdownNow();
	}
}