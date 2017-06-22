import java.util.*;
import com.microsoft.z3.*;

public class VerifierDecoder {

	private Context ctx;
	private Model model;
	private Expr[] vars;
	private int numVar;

	public VerifierDecoder(Context ctx, Model model, Expr[] vars) {
		this.ctx = ctx;
		this.model = model;
		this.numVar = vars.length;
		this.vars = vars;
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
