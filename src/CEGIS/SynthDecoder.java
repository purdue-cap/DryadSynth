import java.util.*;
import com.microsoft.z3.*;

public class SynthDecoder {

	private Context ctx;
	private String returnType;
	private Model model;
	private IntExpr[][][] c;
	private SygusExtractor extractor;

	public SynthDecoder(Context ctx, Model model, IntExpr[][][] c, SygusExtractor extractor) {
		this.ctx = ctx;
		this.returnType = returnType;
		this.model = model;
		this.c = c;
		this.extractor = extractor;
	}

	public IntExpr[][][] evaluteCoefficient() {

		IntExpr[][][] coeff = new IntExpr[c.length][0][0];

		for (int i = 0; i < c.length; i++) {
			coeff[i] = new IntExpr[c[i].length][0];
			for (int j = 0; j < c[i].length; j++) {
				coeff[i][j] = new IntExpr[c[i][j].length];
				for (int k = 0; k < c[i][j].length; k++) {
					coeff[i][j][k] = (IntExpr) model.evaluate(c[i][j][k], true);
				}
			}
		}

		return coeff;
	}

	public void generateFunction(Map<String, Expr> functions) {
		IntExpr[][][] coeff = evaluteCoefficient();

		ArithExpr[][] p = new ArithExpr[coeff.length][0];
		Expr[][] f = new Expr[coeff.length][0];

		for (int k = 0; k < coeff.length; k++) {
			p[k] = new ArithExpr[coeff[k].length];
			for (int i = 0; i < coeff[k].length; i++) {
				p[k][i] = coeff[k][i][0];

				Expr[] args = extractor.requestUsedArgs.get(extractor.names.get(k));
				for (int j = 1; j < coeff[k][i].length; j++) {
					p[k][i] = ctx.mkAdd(p[k][i], ctx.mkMul(coeff[k][i][j], (ArithExpr)args[j - 1]));
				}
			}
		}

		for (int j = 0; j < coeff.length; j++) {
			f[j] = new Expr[coeff[j].length];
			for (int i = coeff[j].length - 1; i >= 0; i--) {
				BoolExpr cond = ctx.mkGe(p[j][i], ctx.mkInt(0));

				if (i < ((coeff[j].length - 1)/2)) {

					f[j][i] = ctx.mkITE(cond, f[j][2*i + 1], f[j][2*i + 2]);

				} else {
					boolean isINV = extractor.requests.get(extractor.names.get(j)).getRange().toString().equals("Bool");
					if (isINV) {
						f[j][i] = ctx.mkITE(cond, ctx.mkTrue(), ctx.mkFalse());
					} else {
						f[j][i] = p[j][i];
					}

				}
			}
			functions.put(extractor.names.get(j), f[j][0].simplify());
		}

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
