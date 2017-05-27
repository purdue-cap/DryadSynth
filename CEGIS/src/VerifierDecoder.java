import java.util.*;
import com.microsoft.z3.*;

public class VerifierDecoder {
	
	private Context ctx;
	private Model model;
	private IntExpr[][] var;
	private int numVar;
	private int numFunc;

	public VerifierDecoder(Context ctx, Model model, int numVar, int numFunc, IntExpr[][] var) {
		this.ctx = ctx;
		this.model = model;
		this.numVar = numVar;
		this.numFunc = numFunc;
		this.var = var;
	}

	public IntExpr[][] decode() {
		IntExpr[][] counterExample = new IntExpr[numFunc][numVar];

		for (int j = 0; j < numFunc; j++) {
			for (int i = 0; i < numVar; i++) {
				counterExample[j][i] = (IntExpr) model.evaluate(var[j][i], true);
			}
		}

		return counterExample;
	}

}