import java.util.*;
import com.microsoft.z3.*;

public class Expand {

	public IntExpr[][][] c;

	private Context ctx;
	private int bound;
	private SygusExtractor extractor;
	private int numFunc;

	public Expand(int bound, Context ctx, SygusExtractor extractor) {
		this.bound = bound;
		this.ctx = ctx;
		this.extractor = extractor;
		this.numFunc = extractor.names.size();

		c = new IntExpr[numFunc][bound][0];
		declareConstants();

	}

	public void declareConstants() {
		for (int k = 0; k < numFunc; k++) {
			for (int j = 0; j < bound; j++) {
				String name = extractor.names.get(k);
				int argCount = extractor.requestUsedArgs.get(name).length;
				c[k][j] = new IntExpr[argCount + 1];
				for (int i = 0; i < argCount + 1; i++) {
					c[k][j][i] = ctx.mkIntConst("f" + k + "_c" + j + "_" + i);
				}
			}
		}
	}

	public IntExpr[][][] getCoefficients() {
		return c;
	}

	public BoolExpr expandCoefficient(int condBound) {

		BoolExpr coefficientProp = ctx.mkTrue();

		for (int i = 0; i < numFunc; i++) {
			for (int j = 0; j < bound; j++) {
				//int condBound = 1;
				int leafBound = condBound;

				BoolExpr cProp = ctx.mkTrue();
				BoolExpr coefficientBound = ctx.mkTrue();
				BoolExpr coefficientBoundLeaf = ctx.mkTrue();
				//BoolExpr cProp = ctx.mkFalse();
				if (condBound > 0) {
					coefficientBound = ctx.mkAnd(ctx.mkLe(c[i][j][0], ctx.mkInt(condBound)), ctx.mkGe(c[i][j][0], ctx.mkInt(-condBound)));
					coefficientBoundLeaf = ctx.mkAnd(ctx.mkLe(c[i][j][0], ctx.mkInt(leafBound)), ctx.mkGe(c[i][j][0], ctx.mkInt(-leafBound)));
				}

				BoolExpr coeffEqualOneOrMinusOne = ctx.mkFalse();
				//BoolExpr coeffEqualOneOrMinusOne = ctx.mkTrue();

				for (int k = 1; k < c[i][j].length ; k++) {
					cProp = ctx.mkAnd(cProp, ctx.mkEq(c[i][j][k], ctx.mkInt(0)));
					coeffEqualOneOrMinusOne = ctx.mkOr(coeffEqualOneOrMinusOne, ctx.mkEq(c[i][j][k], ctx.mkInt(1)), ctx.mkEq(c[i][j][k], ctx.mkInt(-1)));
					if (condBound > 0) {
						coefficientBound = ctx.mkAnd(coefficientBound, ctx.mkLe(c[i][j][k], ctx.mkInt(condBound)), ctx.mkGe(c[i][j][k], ctx.mkInt(-condBound)));
						coefficientBoundLeaf = ctx.mkAnd(coefficientBoundLeaf, ctx.mkLe(c[i][j][k], ctx.mkInt(leafBound)), ctx.mkGe(c[i][j][k], ctx.mkInt(-leafBound)));
					}

				}

				if (j < ((bound - 1)/2)) {
					coefficientProp = ctx.mkAnd(coefficientProp, coeffEqualOneOrMinusOne, ctx.mkNot(cProp), coefficientBound);
				} else {
					boolean isINV = extractor.requests.get(extractor.names.get(i)).getRange().toString().equals("Bool");
					if (isINV) {
						coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkOr(coeffEqualOneOrMinusOne, cProp));
					}
					coefficientProp = ctx.mkAnd(coefficientProp, coefficientBoundLeaf);
				}

			}
		}

		return coefficientProp;
	}

	public Expr generateEval(int k, int i) {

		String name = extractor.names.get(k);
		Expr[] var = extractor.requestUsedArgs.get(name);


		ArithExpr poly = c[k][i][0];

		for (int j = 1; j < c[k][i].length; j++) {
			poly = ctx.mkAdd(poly, ctx.mkMul(c[k][i][j], (ArithExpr)var[j - 1]));
		}

		if (i < ((bound - 1)/2)) {
			return ctx.mkITE(ctx.mkGe(poly, ctx.mkInt(0)), generateEval(k, 2*i + 1), generateEval(k, 2*i + 2));
		} else {
			boolean isINV = extractor.requests.get(name).getRange().toString().equals("Bool");
			if (isINV) {
				return ctx.mkITE(ctx.mkGe(poly, ctx.mkInt(0)), ctx.mkTrue(), ctx.mkFalse());
			} else {
				return poly;
			}

		}

	}

}
