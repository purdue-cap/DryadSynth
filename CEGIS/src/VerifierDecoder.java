import java.util.*;
import com.microsoft.z3.*;

public class VerifierDecoder {
	
	private Context ctx;
	private Model model;
	private IntExpr[] var;
	private int numVar;

	public VerifierDecoder(Context ctx, Model model, int numVar, IntExpr[] var) {
		this.ctx = ctx;
		this.model = model;
		this.numVar = numVar;
		this.var = var;
	}

	public IntExpr[] decode() {
		IntExpr[] counterExample = new IntExpr[numVar];

		for (int i = 0; i < numVar; i++) {
			counterExample[i] = (IntExpr) model.evaluate(var[i], false);
		}

		return counterExample;
	}

	public void printOutput() {
		IntExpr[] ex = decode();
		for (int i = 0; i < numVar; i++) {
			System.out.println("var" + i + " : " + ex[i]);
		}
		
	}

}