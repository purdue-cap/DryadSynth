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

				long estimatedTime = System.currentTimeMillis() - startTime;
				System.out.println("Runtime: " + estimatedTime);

        	}
    	});

		try {
			future.get(60, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			System.out.println("Terminated!");
        	future.cancel(true);
        	System.exit(0);
    	}

        executor.shutdownNow();
	}
}
