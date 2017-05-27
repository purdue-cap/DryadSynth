import java.util.*;
import com.microsoft.z3.*;

public class Expand {

	public BoolExpr[][] valid;
	public IntExpr[][][] c;
	public FuncDecl eval;

	private Context ctx;	
	private int bound;
	private int numVar;
	private int numFunc;

	public Expand(int bound, Context ctx, int numVar, int numFunc) {
		this.bound = bound;
		this.ctx = ctx;
		this.numVar = numVar;
		this.numFunc = numFunc;

		valid = new BoolExpr[numFunc][bound];
		c = new IntExpr[numFunc][bound][numVar + 1];

		declareValid();
		declareEval();
		declareConstants();

	}

	public void declareValid() {
		for (int j = 0; j < numFunc; j++) {
			for (int i = 0; i < bound; i++) {
				valid[j][i] = ctx.mkBoolConst("f" + j + "_valid_" + i);
			}
		}
	}

	public void declareConstants() {
		for (int k = 0; k < numFunc; k++) {
			for (int j = 0; j < bound; j++) {
				for (int i = 0; i < numVar + 1; i++) {
					c[k][j][i] = ctx.mkIntConst("f" + k + "_c" + j + "_" + i);
				}
			}
		}
	}

	public void declareEval() {
		Sort intSort = ctx.getIntSort();

		Sort[] domain = new Sort[numVar + 2];
		for (int i = 0; i < numVar + 2; i++) {
			domain[i] = intSort;
		}

		Sort range = intSort;

		eval = ctx.mkFuncDecl("eval", domain, range);
	}

	public BoolExpr[][] getValid() {
		return valid;
	}

	public IntExpr[][][] getCoefficients() {
		return c;
	}

	public BoolExpr expandCoefficient() {

		BoolExpr coefficientProp = ctx.mkTrue();

		for (int i = 0; i < numFunc; i++) {
			for (int j = 0; j < bound; j++) {
				BoolExpr cProp = ctx.mkTrue();
				//BoolExpr coefficientConstraint = ctx.mkAnd(ctx.mkLe(c[i][j][0], ctx.mkInt(9)), ctx.mkGe(c[i][j][0], ctx.mkInt(-9)));
				//BoolExpr coefficientConstraint = ctx.mkEq(c[i][j][0], ctx.mkInt(0));

				for (int k = 1; k < numVar + 1; k++) {
					cProp = ctx.mkAnd(cProp, ctx.mkEq(c[i][j][k], ctx.mkInt(0)));
					//coefficientConstraint = ctx.mkAnd(coefficientConstraint, ctx.mkLe(c[i][j][k], ctx.mkInt(9)), ctx.mkGe(c[i][j][k], ctx.mkInt(-9)));
				}

				coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkNot(cProp));
				//coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkNot(cProp), coefficientConstraint);
			}			
		}

		return coefficientProp;
	}

	public BoolExpr expandValid() {

		BoolExpr v = ctx.mkTrue();

		for (int j = 0; j < numFunc; j++) {
			for (int i = 0; i < bound; i++) {
				if (i % 2 == 0 && i != 0) {
					v = ctx.mkAnd(v, ctx.mkImplies(valid[j][i], ctx.mkAnd(valid[j][(i-2)/2], valid[j][i-1])));
				} else if (i % 2 == 1) {
					v = ctx.mkAnd(v, ctx.mkImplies(valid[j][i], ctx.mkAnd(valid[j][(i-1)/2], valid[j][i+1])));
				}
			}
		}

		return v;
	}

	public BoolExpr expandEval(int k, IntExpr[] var) {

		BoolExpr evalProperty = ctx.mkTrue();

		//for (int k = 0; k < numFunc; k++) {
			for (int i = 0; i < bound; i++) {
				ArithExpr poly = c[k][i][0];

				for (int j = 1; j < numVar + 1; j++) {
					poly = ctx.mkAdd(poly, ctx.mkMul(c[k][i][j], var[j - 1]));
				}

				IntExpr[] args = new IntExpr[numVar + 2];
				IntExpr[] argsLeft = new IntExpr[numVar + 2];
				IntExpr[] argsRight = new IntExpr[numVar + 2];
				System.arraycopy(var, 0, args, 0, numVar);
				System.arraycopy(var, 0, argsLeft, 0, numVar);
				System.arraycopy(var, 0, argsRight, 0, numVar);
				args[numVar] = ctx.mkInt(i);
				args[numVar + 1] = ctx.mkInt(k);
				argsLeft[numVar] = ctx.mkInt(2*i + 1);
				argsLeft[numVar + 1] = ctx.mkInt(k);
				argsRight[numVar] = ctx.mkInt(2*i + 2);
				argsRight[numVar + 1] = ctx.mkInt(k);

				BoolExpr notValidEval = ctx.mkEq(poly, ctx.mkApp(eval, args));
				BoolExpr validEval;

				if (i < ((bound - 1)/2)) {
					validEval = ctx.mkOr(ctx.mkAnd(ctx.mkGe(poly, ctx.mkInt(0)), ctx.mkEq(ctx.mkApp(eval, args), ctx.mkApp(eval, argsLeft)))
						, ctx.mkAnd(ctx.mkLt(poly, ctx.mkInt(0)), ctx.mkEq(ctx.mkApp(eval, args), ctx.mkApp(eval, argsRight))));
				} else {
					validEval = ctx.mkEq(poly, ctx.mkApp(eval, args));
				}

				evalProperty = ctx.mkAnd(evalProperty, ctx.mkImplies(ctx.mkNot(valid[k][i]), notValidEval));
				evalProperty = ctx.mkAnd(evalProperty, ctx.mkImplies(valid[k][i], validEval));

			}
		//}

		return evalProperty;
	}

}