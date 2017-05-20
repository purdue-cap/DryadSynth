import java.util.*;
import com.microsoft.z3.*;

public class SynthDecoder {
	
	private Context ctx;
	private Model model;
	private BoolExpr[][] valid;
	private IntExpr[][][] c;
	private int bound;
	private int numVar;
	private int numCoeff;
	private int numFunc;

	public SynthDecoder(Context ctx, Model model, BoolExpr[][] valid, IntExpr[][][] c, int bound, int numVar, int numFunc) {
		this.ctx = ctx;
		this.model = model;
		this.valid = valid;
		this.c = c;
		this.bound = bound;
		this.numVar = numVar;
		this.numCoeff = numVar + 1;
		this.numFunc = numFunc;
	}

	public IntExpr[][][] evaluteCoefficient() {
		IntExpr[][][] coeff = new IntExpr[numFunc][bound][numCoeff];

		for (int i = 0; i < numFunc; i++) {
			for (int j = 0; j < bound; j++) {
				for (int k = 0; k < numCoeff; k++) {
					coeff[i][j][k] = (IntExpr) model.evaluate(c[i][j][k], false);
				}
			}	
		}

		return coeff;
	}

	public String[][] evaluteValid() {
		String[][] v = new String[numFunc][bound];

		for (int i = 0; i < numFunc; i++) {
			for (int j = 0; j < bound; j++) {
				v[i][j] = model.evaluate(valid[i][j], false).toString();
			}
		}

		return v;
	}

	public ArithExpr[] generateFunction(IntExpr[] var) {
		ArithExpr[][] p = new ArithExpr[numFunc][bound];
		IntExpr[][][] coeff = evaluteCoefficient();
		String[][] v = evaluteValid();
		ArithExpr[][] f = new ArithExpr[numFunc][bound];
		ArithExpr[] functions = new ArithExpr[numFunc];

		for (int k = 0; k < numFunc; k++) {
			for (int i = 0; i < bound; i++) {
				p[k][i] = coeff[k][i][0];

				for (int j = 1; j < numCoeff; j++) {
					p[k][i] = ctx.mkAdd(p[k][i], ctx.mkMul(coeff[k][i][j], var[j - 1]));
				}
			}
		}

		for (int j = 0; j < numFunc; j++) {
			for (int i = bound - 1; i >= 0; i--) {
				if (i < ((bound - 1)/2)) {
					BoolExpr cond = ctx.mkGe(p[j][i], ctx.mkInt(0));

					if (v[j][i].equals("true")) {
						f[j][i] = (ArithExpr) ctx.mkITE(cond, f[j][2*i + 1], f[j][2*i + 2]);
					} else {
						f[j][i] = p[j][i];
					}

				} else {
					f[j][i] = p[j][i];
				}
			}
			functions[j] = f[j][0];
		}

		return functions;

	}

	/*public void printOutput() {
		IntExpr[][] coeff0 = evaluteCoefficient();
		for (int i = 0; i < bound; i++) {
			for (int j = 0; j < numCoeff; j++) {
				System.out.println("node" + i + ": c" + j + ": " + coeff0[i][j]);
			}
		}

		String[] v = evaluteValid();
		for (int i = 0; i < bound; i++) {
			System.out.println("valid" + i + ": " + v[i]);
		}

		ArithExpr[] p = getPoly();
		for (int i = 0; i < bound; i++) {
			System.out.println("poly" + i + ": " + p[ i]);
		}

		ArithExpr[] function = generateFunction();
		for (int i = 0; i < numFunc; i++) {
			System.out.println(function[i]);
		}
		
	}*/

}