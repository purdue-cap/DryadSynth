import java.util.*;
import com.microsoft.z3.*;

public class SynthDecoder {
	
	private Context ctx;
	private String returnType;
	private Model model;
	private IntExpr[][][] c;
	private int bound;
	private int numV;
	private int numCoeff;
	private int numFunc;

	public SynthDecoder(Context ctx, String returnType, Model model, IntExpr[][][] c, int bound, int numV, int numFunc) {
		this.ctx = ctx;
		this.returnType = returnType;
		this.model = model;
		this.c = c;
		this.bound = bound;
		this.numV = numV;
		this.numCoeff = numV + 1;
		this.numFunc = numFunc;
	}

	public IntExpr[][][] evaluteCoefficient() {
		IntExpr[][][] coeff = new IntExpr[numFunc][bound][numCoeff];

		for (int i = 0; i < numFunc; i++) {
			for (int j = 0; j < bound; j++) {
				for (int k = 0; k < numCoeff; k++) {
					coeff[i][j][k] = (IntExpr) model.evaluate(c[i][j][k], true);
				}
			}	
		}

		return coeff;
	}

	public Expr[] generateFunction(IntExpr[] var) {
		ArithExpr[][] p = new ArithExpr[numFunc][bound];
		IntExpr[][][] coeff = evaluteCoefficient();
		Expr[][] f = new Expr[numFunc][bound];
		Expr[] functions = new Expr[numFunc];
		IntExpr[] args = new IntExpr[numV];

		if (returnType.equals("INV")) {
			for (int i = 0; i < numV; i++) {
				args[i] = var[2*i];
			}
		} else {
			System.arraycopy(var, 0, args, 0, numV);
		}

		for (int k = 0; k < numFunc; k++) {
			for (int i = 0; i < bound; i++) {
				p[k][i] = coeff[k][i][0];

				for (int j = 1; j < numCoeff; j++) {
					p[k][i] = ctx.mkAdd(p[k][i], ctx.mkMul(coeff[k][i][j], args[j - 1]));
				}
			}
		}

		for (int j = 0; j < numFunc; j++) {
			for (int i = bound - 1; i >= 0; i--) {
				BoolExpr cond = ctx.mkGe(p[j][i], ctx.mkInt(0));
				
				if (i < ((bound - 1)/2)) {

					f[j][i] = ctx.mkITE(cond, f[j][2*i + 1], f[j][2*i + 2]);

				} else {
					if (returnType.equals("INV")) {
						f[j][i] = ctx.mkITE(cond, ctx.mkTrue(), ctx.mkFalse());
					} else {
						f[j][i] = p[j][i];
					}
					
				}
			}
			functions[j] = f[j][0].simplify();
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