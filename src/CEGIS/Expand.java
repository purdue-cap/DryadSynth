import java.util.*;
import com.microsoft.z3.*;

public class Expand {

	// Coefficients for CLIA/INV
	public IntExpr[][][] c;
	// Terms for General
	public IntExpr[][] t;
	// Bound is coefficient array length for CLIA/INV
	// Bound is term array length for General
	// Public for outer reference
	public int bound;

	private Context ctx;
	private SygusExtractor extractor;
	private int numFunc;

	// General track essentials
	// Internal term expression, for use in dynamic programming
	// Should be static, so shared amongst different calls to Expand
	private static List<IntExpr> it = new ArrayList<IntExpr>();
	// Key should be funcIndex_ruleName_termLength for generic Valid
	// Key should be funcIndex+ruleIndex_termLength for a specific rule
	private static Map<String, BoolExpr> validCache = new HashMap<String, BoolExpr>();
	// Key should be funcIndex_ruleName_termLength
	// Key should be funcIndex+ruleIndex_termLength for a specific rule
	private static Map<String, Expr> intepretCache = new HashMap<String, Expr>();


	public Expand(Context ctx, SygusExtractor extractor) {
		this.ctx = ctx;
		this.extractor = extractor;
		this.numFunc = extractor.names.size();
	}

	public void setVectorBound(int vectorBound) {
		assert extractor.isGeneral;
		this.bound = vectorBound;
		t = new IntExpr[numFunc][bound];
		declareTerms();
		prepareGrammar();
	}

	public void setHeightBound(int heightBound) {
		assert !extractor.isGeneral;
		this.bound = (int)Math.pow(2, heightBound) - 1;
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
	static class Grammar {
		SygusExtractor.CFG[] cfgs;
		List<List<String[]>> ruleTbl;
		List<Map<String, Integer>> ruleTblRev;
		List<Map<String, Integer>> subrulePos;
		List<Map<String, Integer>> subruleLen;
	}
	static Grammar grammar = null;

	public void prepareGrammar() {
		if (grammar == null) {
			grammar = new Grammar();
			grammar.cfgs = new SygusExtractor.CFG[numFunc];
			grammar.ruleTbl = new ArrayList<List<String[]>>();
			grammar.ruleTblRev = new ArrayList<Map<String, Integer>>();
			grammar.subrulePos = new ArrayList<Map<String, Integer>>();
			grammar.subruleLen = new ArrayList<Map<String, Integer>>();
			int f = 0;
			for (String funcName: extractor.cfgs.keySet()) {
				SygusExtractor.CFG cfg = extractor.cfgs.get(funcName);
				grammar.cfgs[f] = cfg;
				grammar.ruleTbl.add(new ArrayList<String[]>());
				grammar.ruleTblRev.add(new LinkedHashMap<String, Integer>()) ;
				grammar.subrulePos.add(new LinkedHashMap<String, Integer>()) ;
				grammar.subruleLen.add(new LinkedHashMap<String, Integer>()) ;
				int i = 0;
				for(String ruleName: cfg.grammarRules.keySet()) {
					List<String[]> subRules = cfg.grammarRules.get(ruleName);
					grammar.subrulePos.get(f).put(ruleName, i);
					grammar.subrulePos.get(f).put(ruleName, subRules.size());
					for(String[] ruleArray: subRules) {
						grammar.ruleTbl.get(f).add(ruleArray);
						grammar.ruleTblRev.get(f).put(ruleArray[0], i);
						i++;
					}
				}
				f++;
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

	SygusExtractor.SybType resolveSyb(int funcIndex, String syb) {
		if (grammar.cfgs[funcIndex].sybTypeTbl.containsKey(syb)){
			return grammar.cfgs[funcIndex].sybTypeTbl.get(syb);
		} else {
			assert extractor.glbSybTypeTbl.containsKey(syb);
			return extractor.glbSybTypeTbl.get(syb);
		}
	}

	public Expr intepretGeneral(int funcIndex, int[] terms) {
		ConcreteIntepreter conInte = new ConcreteIntepreter();
		return conInte.intepretConcrete(funcIndex, terms, 0);
	}

	class ConcreteIntepreter {
		int lastIntepreted = -1;
		public Expr intepretConcrete(int funcIndex, int[] terms, int start) {
			int ruleIndex = terms[start];
			String[] fullRule = grammar.ruleTbl.get(funcIndex).get(ruleIndex);
			String termSyb = fullRule[0];
			int argCount = fullRule.length - 1;
			SygusExtractor.SybType termType = resolveSyb(funcIndex, termSyb);
			Expr result;
			lastIntepreted = start;
			if (termType == SygusExtractor.SybType.LITERAL) {
				result = ctx.mkInt(Integer.parseInt(termSyb));
			} else if (termType == SygusExtractor.SybType.GLBVAR) {
				result = extractor.vars.get(termSyb);
			} else if (termType == SygusExtractor.SybType.LCLARG) {
				result = grammar.cfgs[funcIndex].localArgs.get(termSyb);
			} else {
				assert termType == SygusExtractor.SybType.FUNC;
				Expr[] args = new Expr[argCount];
				for (int i = 0; i < argCount; i++) {
					args[i] = intepretConcrete(funcIndex, terms, lastIntepreted + 1);
				}
				result = extractor.operationDispatcher(termSyb, args, true);
			}
			return result;
		}
	}

	public Expr intepretGeneral(int funcIndex, IntExpr[] terms){
		return generateIntepret(funcIndex, terms, "Start");
	}

	// Intepret generation for expanding vector vars with grammar rule
	// corresponding to ruleIndex
	// Will NOT check overall validity, should be caller to do it
	public Expr generateIntepret(int funcIndex, IntExpr[] vars, int ruleIndex) {
		String cacheKey = Integer.toString(funcIndex) + "+" + Integer.toString(ruleIndex) + "_" + Integer.toString(vars.length);
		IntExpr[] ivars = it.subList(0, vars.length).toArray(new IntExpr[vars.length]);
		if (intepretCache.containsKey(cacheKey)) {
			return intepretCache.get(cacheKey).substitute(ivars, vars);
		}
		String[] fullRule = grammar.ruleTbl.get(funcIndex).get(ruleIndex);
		String termSyb = fullRule[0];
		Expr result;
		SygusExtractor.SybType termType = resolveSyb(funcIndex, termSyb);
		if (termType == SygusExtractor.SybType.LITERAL) {
			result = ctx.mkInt(Integer.parseInt(termSyb));
		} else if (termType == SygusExtractor.SybType.GLBVAR) {
			result = extractor.vars.get(termSyb);
		} else if (termType == SygusExtractor.SybType.LCLARG) {
			result = grammar.cfgs[funcIndex].localArgs.get(termSyb);
		} else {
			assert termType == SygusExtractor.SybType.FUNC;
			int termLength = ivars.length - 1;
			int argCount = fullRule.length - 1;
			if (argCount == 1) {
				IntExpr[] subterms = Arrays.copyOfRange(ivars, 1, ivars.length);
				String subtermSyb = fullRule[1];
				SygusExtractor.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
				assert subtermType == SygusExtractor.SybType.SYMBOL;
				Expr subtermIntepreted = generateIntepret(funcIndex, subterms, subtermSyb);
				result = extractor.operationDispatcher(termSyb, new Expr[]{subtermIntepreted}, true);
			} else {
				int[][] combinations = combination(termLength, argCount - 1);
				BoolExpr[] branchGuard = new BoolExpr[combinations.length];
				Expr[] branches = new Expr[combinations.length];
				Expr iteExpr = ctx.mkInt(0);
				int brCount = 0;
				for (int[] division: combinations) {
					int start = 1;
					BoolExpr[] structValids = new BoolExpr[division.length];
					Expr[] argsIntepreted = new Expr[division.length + 1];
					for (int i = 0; i <= division.length; i++) {
						int end;
						if (i == division.length) {
							end = ivars.length;
						} else {
							end = division[i] + 1;
						}
						IntExpr[] subterms = Arrays.copyOfRange(ivars, start, end);
						String subtermSyb = fullRule[i + 1];
						SygusExtractor.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
						assert subtermType == SygusExtractor.SybType.SYMBOL;
						argsIntepreted[i] = generateIntepret(funcIndex, subterms, subtermSyb);
						if (i != division.length) {
							structValids[i] = generateValid(funcIndex, subterms, subtermSyb);
						}
						start = end;
					}
					branchGuard[brCount] = ctx.mkAnd(structValids);
					branches[brCount] = extractor.operationDispatcher(termSyb, argsIntepreted, true);
					iteExpr = ctx.mkITE(branchGuard[brCount], branches[brCount], iteExpr);
					brCount++;
				}
				result = iteExpr;
			}
		}
		intepretCache.put(cacheKey, result);
		return result.substitute(ivars, vars);
	}

	// Intepret generation for expanding vector vars to non-terminal ruleName
	public Expr generateIntepret(int funcIndex, IntExpr[] vars, String ruleName) {
		String cacheKey = Integer.toString(funcIndex) + "_" + ruleName + "_" + Integer.toString(vars.length);
		IntExpr[] ivars = it.subList(0, vars.length).toArray(new IntExpr[vars.length]);
		if (intepretCache.containsKey(cacheKey)) {
			return intepretCache.get(cacheKey).substitute(ivars, vars);
		}
		Expr result;
		int ruleS = grammar.subrulePos.get(funcIndex).get(ruleName);
		int ruleE = ruleS + grammar.subruleLen.get(funcIndex).get(ruleName);
		BoolExpr[] branchGuard = new BoolExpr[ruleE - ruleS];
		Expr[] branches = new Expr[ruleE - ruleS];
		Expr iteExpr = ctx.mkInt(0);
		for (int type = ruleS; type < ruleE; type++) {
			branchGuard[type - ruleS] = generateValid(funcIndex, ivars, type);
			branches[type - ruleS] = generateIntepret(funcIndex, ivars, type);
			iteExpr = ctx.mkITE(branchGuard[type - ruleS], branches[type - ruleS], iteExpr);
		}
		result = iteExpr;
		intepretCache.put(cacheKey, result);
		return result.substitute(ivars, vars);
	}

	// Valid predicate generation for a specific grammar rule
	// Predicate is true if vars vector is valid for representing the grammar node
	// corresponding to the funcIndex and ruleIndex
	public BoolExpr generateValid(int funcIndex, IntExpr[] vars, int ruleIndex) {
		String cacheKey = Integer.toString(funcIndex) + "+" + Integer.toString(ruleIndex) + "_" + Integer.toString(vars.length);
		IntExpr[] ivars = it.subList(0, vars.length).toArray(new IntExpr[vars.length]);
		if (validCache.containsKey(cacheKey)) {
			return (BoolExpr)validCache.get(cacheKey).substitute(ivars, vars);
		}
		IntExpr typeVar = ivars[0];
		String[] fullRule = grammar.ruleTbl.get(funcIndex).get(ruleIndex);
		BoolExpr typeCond = ctx.mkEq(typeVar, ctx.mkInt(ruleIndex));
		int argCount = fullRule.length - 1;
		int termLength = ivars.length - 1;
		BoolExpr result;
		if (argCount > termLength) {
			result = ctx.mkFalse();
		} else if (argCount == 0) {
			if (termLength == 0) {
				result = typeCond;
			} else {
				result = ctx.mkFalse();
			}
		} else if (argCount == 1) {
			IntExpr[] subterms = Arrays.copyOfRange(ivars, 1, ivars.length);
			String subtermSyb = fullRule[1];
			SygusExtractor.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
			assert subtermType == SygusExtractor.SybType.SYMBOL;
			result = ctx.mkAnd(typeCond, generateValid(funcIndex, subterms, subtermSyb));
		} else {
			int[][] combinations = combination(termLength, argCount - 1);
			BoolExpr[] candidates = new BoolExpr[combinations.length];
			int canCount = 0;
			for (int[] division: combinations) {
				int start = 1;
				BoolExpr[] subValids = new BoolExpr[division.length + 1];
				for (int i = 0; i <= division.length; i++) {
					int end;
					if (i == division.length) {
						end = ivars.length;
					} else {
						end = division[i] + 1;
					}
					IntExpr[] subterms = Arrays.copyOfRange(ivars, start, end);
					String subtermSyb = fullRule[i + 1];
					SygusExtractor.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
					assert subtermType == SygusExtractor.SybType.SYMBOL;
					subValids[i] = generateValid(funcIndex, subterms, subtermSyb);
					start = end;
				}
				candidates[canCount] = ctx.mkAnd(subValids);
				canCount++;
			}
			result = ctx.mkAnd(typeCond, ctx.mkOr(candidates));
		}
		validCache.put(cacheKey, result);
		return (BoolExpr)result.substitute(ivars, vars);
	}

	// Valid predicate generation for a non-terminal
	// Predicate is true if vars vector is valid for representing a non-terminal
	// corresponding to funcIndex and non-terminal ruleName
	public BoolExpr generateValid(int funcIndex, IntExpr[] vars, String ruleName) {
		String cacheKey = Integer.toString(funcIndex) + "_" + ruleName + "_" + Integer.toString(vars.length);
		IntExpr[] ivars = it.subList(0, vars.length).toArray(new IntExpr[vars.length]);
		if (validCache.containsKey(cacheKey)) {
			return (BoolExpr)validCache.get(cacheKey).substitute(ivars, vars);
		}
		int ruleS = grammar.subrulePos.get(funcIndex).get(ruleName);
		int ruleE = ruleS + grammar.subruleLen.get(funcIndex).get(ruleName);
		IntExpr typeVar = ivars[0];
		BoolExpr[] conds = new BoolExpr[ruleE - ruleS];
		for (int type = ruleS; type < ruleE; type++) {
			conds[type - ruleS] = generateValid(funcIndex, ivars, type);
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
