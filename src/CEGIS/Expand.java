import java.util.*;
import com.microsoft.z3.*;

public class Expand {

	public IntExpr[][][] c;

	private Context ctx;	
	private int bound;
	private int numVar;
	private int numFunc;

	public Expand(int bound, Context ctx, int numVar, int numFunc) {
		this.bound = bound;
		this.ctx = ctx;
		this.numVar = numVar;
		this.numFunc = numFunc;

		c = new IntExpr[numFunc][bound][numVar + 1];
		declareConstants();

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

	public IntExpr[][][] getCoefficients() {
		return c;
	}

	public BoolExpr expandCoefficient() {

		BoolExpr coefficientProp = ctx.mkTrue();

		for (int i = 0; i < numFunc; i++) {
			for (int j = 0; j < bound; j++) {
				int condBound = 2;
				int leafBound = 20;

				BoolExpr cProp = ctx.mkTrue();
				BoolExpr coefficientBound = ctx.mkAnd(ctx.mkLe(c[i][j][0], ctx.mkInt(condBound)), ctx.mkGe(c[i][j][0], ctx.mkInt(-condBound)));
				BoolExpr coefficientBoundLeaf = ctx.mkAnd(ctx.mkLe(c[i][j][0], ctx.mkInt(leafBound)), ctx.mkGe(c[i][j][0], ctx.mkInt(-leafBound)));
				BoolExpr coeffEqualOneOrMinusOne = ctx.mkFalse();

				for (int k = 1; k < numVar + 1; k++) {
					cProp = ctx.mkAnd(cProp, ctx.mkEq(c[i][j][k], ctx.mkInt(0)));
					coeffEqualOneOrMinusOne = ctx.mkOr(coeffEqualOneOrMinusOne, ctx.mkEq(c[i][j][k], ctx.mkInt(1)), ctx.mkEq(c[i][j][k], ctx.mkInt(-1)));
					coefficientBound = ctx.mkAnd(coefficientBound, ctx.mkLe(c[i][j][k], ctx.mkInt(condBound)), ctx.mkGe(c[i][j][k], ctx.mkInt(-condBound)));
					coefficientBoundLeaf = ctx.mkAnd(coefficientBoundLeaf, ctx.mkLe(c[i][j][k], ctx.mkInt(leafBound)), ctx.mkGe(c[i][j][k], ctx.mkInt(-leafBound)));
					
				}

				if (j < ((bound - 1)/2)) {
					coefficientProp = ctx.mkAnd(coefficientProp, coeffEqualOneOrMinusOne, ctx.mkNot(cProp), coefficientBound);

				} else {
					coefficientProp = ctx.mkAnd(coefficientProp, coefficientBoundLeaf);
				}

				//coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkNot(cProp));
				//coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkNot(cProp), coefficientConstraint);
			}			
		}

		return coefficientProp;
	}

	public Expr generateEval(int k, IntExpr[] var, int i) {

		ArithExpr poly = c[k][i][0];

		for (int j = 1; j < numVar + 1; j++) {
			poly = ctx.mkAdd(poly, ctx.mkMul(c[k][i][j], var[j - 1]));
		}

		if (i < ((bound - 1)/2)) {
			return ctx.mkITE(ctx.mkGe(poly, ctx.mkInt(0)), generateEval(k, var, 2*i + 1), generateEval(k, var, 2*i + 2));
		} else {
			return poly;
		}

	}

}