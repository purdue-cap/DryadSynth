import java.util.*;
import com.microsoft.z3.*;

public class Expand {

	// Coefficients for CLIA/INV
	public IntExpr[][][] c;
	// Terms for General
	public IntExpr[][] t;

	private Context ctx;
	// Bound is height for CLIA/INV
	// Bound is term array length for General
	private int bound;
	private SygusExtractor extractor;
	private int numFunc;

	// General track essentials
	// Internal term expression, for use in dynamic programming
	// Should be static, so shared amongst different calls to Expand
	private static List<IntExpr> it = new ArrayList<IntExpr>();
	// Key should be ruleName_termLength
	private static Map<String, BoolExpr> validCache = new HashMap<String, BoolExpr>();


	public Expand(int bound, Context ctx, SygusExtractor extractor) {
		this.bound = bound;
		this.ctx = ctx;
		this.extractor = extractor;
		this.numFunc = extractor.names.size();

		if (extractor.isGeneral) {
			t = new IntExpr[numFunc][bound];
			declareTerms();
			prepareGrammar();
		} else {
			c = new IntExpr[numFunc][bound][0];
			declareConstants();
		}

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

	public void declareTerms() {
		if (it.size() < bound) {
			for (int i = it.size(); i < bound; i++) {
				IntExpr newit = (IntExpr)ctx.mkFreshConst("general_internal", ctx.getIntSort());
				it.add(newit);
			}
		}
		for (int k = 0; k < numFunc; k++) {
			for (int j = 0; j < bound; j++) {
				t[k][j] = ctx.mkIntConst("f" + k + "_t" + j);
			}
		}
	}

	// Prepared grammar rules
	// Only need to prepare once
	static Map<String, List<String[]>> grammarRules = null;
    static Map<String, SygusExtractor.SybType> sybTypeTbl = null;
    static Map<String, Sort> grammarSybSort = null;
	static List<String[]> ruleTbl = new ArrayList<String[]>();
	static Map<String, Integer> ruleTblRev = new HashMap<String, Integer>();
	static Map<String, Integer> subrulePos = new HashMap<String, Integer>();
	static Map<String, Integer> subruleLen = new HashMap<String, Integer>();

	public void prepareGrammar() {
		if (grammarRules == null) {
			grammarRules = extractor.grammarRules;
			sybTypeTbl = extractor.sybTypeTbl;
			grammarSybSort = extractor.grammarSybSort;
			int i = 0;
			for(String ruleName: grammarRules.keySet()) {
				List<String[]> subRules = grammarRules.get(ruleName);
				subrulePos.put(ruleName, i);
				subrulePos.put(ruleName, subRules.size());
				for(String[] ruleArray: subRules) {
					ruleTbl.add(ruleArray);
					ruleTblRev.put(ruleArray[0], i);
					i++;
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

	public BoolExpr generateValid(IntExpr[] vars, String ruleName) {
		String cacheKey = ruleName + "_" + Integer.toString(vars.length);
		IntExpr[] ivars = it.subList(0, vars.length).toArray(new IntExpr[vars.length]);
		if (validCache.containsKey(cacheKey)) {
			return (BoolExpr)validCache.get(cacheKey).substitute(ivars, vars);
		}
		int ruleS = subrulePos.get(ruleName);
		int ruleE = ruleS + subruleLen.get(ruleName);
		IntExpr typeVar = ivars[0];
		BoolExpr[] conds = new BoolExpr[ruleE - ruleS];
		for (int type = ruleS; type < ruleE; type++) {
			String[] fullRule = ruleTbl.get(type);
			BoolExpr typeCond = ctx.mkEq(typeVar, ctx.mkInt(type));
			int argCount = fullRule.length - 1;
			int termLength = ivars.length - 1;
			if (argCount > termLength) {
				conds[type - ruleS] = ctx.mkFalse();
				continue;
			}
			if (argCount == 0) {
				if (termLength == 0) {
					conds[type - ruleS] = typeCond;
				} else {
					conds[type - ruleS] = ctx.mkFalse();
				}
				continue;
			}
			if (argCount == 1) {
				IntExpr[] subterms = Arrays.copyOfRange(ivars, 1, ivars.length);
				String subtermSyb = fullRule[1];
				SygusExtractor.SybType subtermType = sybTypeTbl.get(subtermSyb);
				assert subtermType != SygusExtractor.SybType.FUNC;
				if (subtermType == SygusExtractor.SybType.VAR ||
				    subtermType == SygusExtractor.SybType.LITERAL) {
					if (subterms.length == 1) {
						conds[type - ruleS] = ctx.mkAnd(typeCond, ctx.mkEq(subterms[0], ctx.mkInt(ruleTbl)));
					} else {
						conds[type - ruleS] = ctx.mkFalse();
					}
				} else if (subtermType == SygusExtractor.SybType.SYMBOL) {
					conds[type - ruleS] = generateValid(subterms, subtermSyb);
				} else {
					assert false;
				}
				continue;
			}
			int[][] combinations = combination(termLength, argCount - 1);
			BoolExpr[] candidates = new BoolExpr[combinations.length];
			for (int[] division: combinations) {
				int start = 1;
				BoolExpr[] subValids = new BoolExpr[division.length + 1]
				for (int i = 0; i <= division.length; i++) {
					int end;
					if (i == division.length) {
						end = ivars.length;
					} else {
						end = division[i] + 1;
					}
					IntExpr[] subterms = Arrays.copyOfRange(ivars, start, end);
					String subtermSyb = fullRule[i + 1];
					SygusExtractor.SybType subtermType = sybTypeTbl.get(subtermSyb);
					assert subtermType != SygusExtractor.SybType.FUNC;
					if (subtermType == SygusExtractor.SybType.VAR ||
					    subtermType == SygusExtractor.SybType.LITERAL) {
						if (subterms.length == 1) {
							// TODO
						} else {
							// TODO
						}
					} else if (subtermType == SygusExtractor.SybType.SYMBOL) {
						// TODO
					} else {
						assert false;
					}
					continue;
				}
			}
		}
		BoolExpr result = ctx.mkOr(conds);
		validCache.put(cacheKey, result);
		return (BoolExpr)result.substitute(ivars, vars);
	}

	// Combination of choosing number of count numbers
	// from a pool of 1 to pool - 1
	static int[][] combination(int pool, int count) {
		return combinationCalc.combination(pool, count);
	}
	static class combinationCalc {
		static Map<int[], int[][]> combinationCache = new HashMap<int[], int[][]>();
		static List<int[]> results;
		static int[] data;
		static int eleCount;
		public static int[][] combination(int pool, int count) {
			assert pool - 1 >= count;
			int[] sig = new int[]{pool, count};
			if (combinationCache.containsKey(sig)) {
				return combinationCache.get(sig);
			}
			data = new int[count];
			eleCount = count;
			results = new ArrayList<int[]>();
			combine(1, pool - 1, 0);
			int[][] resultsArray = results.toArray(new int[results.size()][]);
			combinationCache.put(sig, resultsArray);
			return resultsArray;
		}
		static void combine(int start, int end, int index) {
			if (index == data.length) {
				int[] newData = new int[eleCount];
				System.arraycopy(data, 0, newData, 0, data.length);
				results.add(newData);
				return;
			}

			for (int i = start; i <= end && end - i + 1 >= eleCount - index; i++) {
				data[index] = i;
				combine(i+1, end, index+1);
			}
		}
	}

}
