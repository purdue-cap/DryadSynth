import java.util.*;
import com.microsoft.z3.*;

public class Expand {

	// Coefficients for CLIA/INV
	public IntExpr[][][] c;
	// Boolean flags for CLIA/INV to indicate whether the condition is an "equation" or "greater than" 
	public BoolExpr[][] eq;
	// Terms for General
	public IntExpr[][] t;
	// Bound is coefficient array length for CLIA/INV
	// Bound is term array length for General
	// Public for outer reference
	public int bound;
	// EquationBound is eqflag length for CLIA/INV
	public int equationBound;

	private Context ctx;
	private SygusProblem problem;
	private int numFunc;

	// General track essentials
	// Internal term expression, for use in dynamic programming
	private List<IntExpr> it = new ArrayList<IntExpr>();
	// Key should be funcIndex_ruleName_termLength for generic Valid
	// Key should be funcIndex+ruleIndex_termLength for a specific rule
	private Map<String, BoolExpr> validCache = new HashMap<String, BoolExpr>();
	// Key should be funcIndex_ruleName_termLength
	// Key should be funcIndex+ruleIndex_termLength for a specific rule
	private Map<String, Expr> interpretCache = new HashMap<String, Expr>();


	public Expand(Context ctx, SygusProblem problem) {
		this.ctx = ctx;
		this.problem = problem;
		this.numFunc = problem.names.size();
	}

	public void setEquationBound(int eqBound) {
		this.equationBound = (int)Math.pow(2, eqBound) - 1;
	}

	public void setVectorBound(int vectorBound) {
		assert problem.isGeneral;
		this.bound = vectorBound;
		t = new IntExpr[numFunc][bound];
		declareTerms();
		prepareGrammar();
	}

	public void setHeightBound(int heightBound) {
		assert !problem.isGeneral;
		this.bound = (int)Math.pow(2, heightBound) - 1;
		c = new IntExpr[numFunc][bound][0];
		eq = new BoolExpr[numFunc][bound];
		declareConstants();
	}

	public void declareConstants() {
		for (int k = 0; k < numFunc; k++) {
			for (int j = 0; j < bound; j++) {
				String name = problem.names.get(k);
				int argCount = problem.requestUsedArgs.get(name).length;
				c[k][j] = new IntExpr[argCount + 1];
				for (int i = 0; i < argCount + 1; i++) {
					c[k][j][i] = ctx.mkIntConst("f" + k + "_c" + j + "_" + i);
				}
			}
		}

		for (int k = 0; k < numFunc; k++) {
			for (int j = 0; j < bound; j++) {
				eq[k][j] = ctx.mkBoolConst("f" + k + "_eq" + j);
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
	class Grammar {
		public SygusProblem.CFG[] cfgs;
		public List<List<String[]>> ruleTbl;
		public List<Map<String, Integer>> ruleTblRev;
		public List<Map<String, Integer>> subrulePos;
		public List<Map<String, Integer>> subruleLen;
		public List<Map<Integer, Sort>> ruleTypeLookup;
		public List<Integer> ruleOrders;
	}
	Grammar grammar = null;

	public void prepareGrammar() {
		if (grammar == null) {
			grammar = new Grammar();
			grammar.cfgs = new SygusProblem.CFG[numFunc];
			grammar.ruleTbl = new ArrayList<List<String[]>>();
			grammar.ruleTblRev = new ArrayList<Map<String, Integer>>();
			grammar.subrulePos = new ArrayList<Map<String, Integer>>();
			grammar.subruleLen = new ArrayList<Map<String, Integer>>();
			grammar.ruleTypeLookup = new ArrayList<Map<Integer, Sort>>();
			grammar.ruleOrders = new ArrayList<Integer>();
			int f = 0;
			for (String funcName: problem.cfgs.keySet()) {
				SygusProblem.CFG cfg = problem.cfgs.get(funcName);
				grammar.cfgs[f] = cfg;
				grammar.ruleTbl.add(new ArrayList<String[]>());
				grammar.ruleTblRev.add(new LinkedHashMap<String, Integer>()) ;
				grammar.subrulePos.add(new LinkedHashMap<String, Integer>()) ;
				grammar.subruleLen.add(new LinkedHashMap<String, Integer>()) ;
				grammar.ruleTypeLookup.add(new LinkedHashMap<Integer, Sort>());
				int i = 0;
				int order = 0;
				for(String ruleName: cfg.grammarRules.keySet()) {
					List<String[]> subRules = cfg.grammarRules.get(ruleName);
					Sort sort = cfg.grammarSybSort.get(ruleName);
					grammar.subrulePos.get(f).put(ruleName, i);
					grammar.subruleLen.get(f).put(ruleName, subRules.size());
					order = order + subRules.size();
					for(String[] ruleArray: subRules) {
						grammar.ruleTbl.get(f).add(ruleArray);
						grammar.ruleTblRev.get(f).put(ruleArray[0], i);
						grammar.ruleTypeLookup.get(f).put(i, sort);
						i++;
					}
				}
				grammar.ruleOrders.add(order);
				f++;
			}
		}
	}

	public IntExpr[][][] getCoefficients() {
		return c;
	}

	public BoolExpr[][] getEquationFlags() {
		return eq;
	}

	public IntExpr[][] getTerms() {
		return t;
	}

	public BoolExpr expandCoefficient(int condBound, Map<String, SygusDispatcher.CoeffRange> coeffRange) {

		BoolExpr coefficientProp = ctx.mkTrue();

		for (int i = 0; i < numFunc; i++) {

			String name = problem.names.get(i);
			SygusDispatcher.CoeffRange rangeType = null;
			if (coeffRange != null) {
				rangeType = coeffRange.get(name);
			}
			
			if (rangeType == SygusDispatcher.CoeffRange.ADDSUB) {
				for (int j = 0; j < bound; j++) {
					coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkEq(c[i][j][0], ctx.mkInt(0)));
				}
			} else if (rangeType == SygusDispatcher.CoeffRange.ADDONLY) {
				for (int j = 0; j < bound; j++) {
					coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkEq(c[i][j][0], ctx.mkInt(0)));
					BoolExpr allzero = ctx.mkTrue();
					for (int k = 1; k < c[i][j].length; k++) {
						allzero = ctx.mkAnd(allzero, ctx.mkEq(c[i][j][k], ctx.mkInt(0)));
						coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkGe(c[i][j][k], ctx.mkInt(0)));
					}
					coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkNot(allzero));
				}
			} else if (rangeType == SygusDispatcher.CoeffRange.TENS || rangeType == SygusDispatcher.CoeffRange.TENSEQUAL) {
				for (int j = 0; j < bound; j++) {
					BoolExpr from0to100 = ctx.mkOr(ctx.mkEq(c[i][j][0], ctx.mkInt(0)), ctx.mkEq(c[i][j][0], ctx.mkInt(-10))
												, ctx.mkEq(c[i][j][0], ctx.mkInt(-20)), ctx.mkEq(c[i][j][0], ctx.mkInt(-30))
												, ctx.mkEq(c[i][j][0], ctx.mkInt(-40)), ctx.mkEq(c[i][j][0], ctx.mkInt(-50))
												// , ctx.mkEq(c[i][j][0], ctx.mkInt(-60)), ctx.mkEq(c[i][j][0], ctx.mkInt(-70))
												// , ctx.mkEq(c[i][j][0], ctx.mkInt(-80)), ctx.mkEq(c[i][j][0], ctx.mkInt(-90))
												// , ctx.mkEq(c[i][j][0], ctx.mkInt(-100))
												);
					if (j >= ((bound - 1)/2)) {
						from0to100 = ctx.mkOr(ctx.mkEq(c[i][j][0], ctx.mkInt(0)), ctx.mkEq(c[i][j][0], ctx.mkInt(10))
											, ctx.mkEq(c[i][j][0], ctx.mkInt(20)), ctx.mkEq(c[i][j][0], ctx.mkInt(30))
											, ctx.mkEq(c[i][j][0], ctx.mkInt(40)), ctx.mkEq(c[i][j][0], ctx.mkInt(50))
											// , ctx.mkEq(c[i][j][0], ctx.mkInt(60)), ctx.mkEq(c[i][j][0], ctx.mkInt(70))
											// , ctx.mkEq(c[i][j][0], ctx.mkInt(80)), ctx.mkEq(c[i][j][0], ctx.mkInt(90))
											// , ctx.mkEq(c[i][j][0], ctx.mkInt(100))
											);
					}
					coefficientProp = ctx.mkAnd(coefficientProp, from0to100);
					coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkEq(eq[i][j], ctx.mkFalse()));
				}
			} else {
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
					BoolExpr oneInThree = ctx.mkTrue();

					for (int k = 1; k < c[i][j].length ; k++) {
						cProp = ctx.mkAnd(cProp, ctx.mkEq(c[i][j][k], ctx.mkInt(0)));
						coeffEqualOneOrMinusOne = ctx.mkOr(coeffEqualOneOrMinusOne, ctx.mkEq(c[i][j][k], ctx.mkInt(1)), ctx.mkEq(c[i][j][k], ctx.mkInt(-1)));
						if (condBound > 0) {
							coefficientBound = ctx.mkAnd(coefficientBound, ctx.mkLe(c[i][j][k], ctx.mkInt(condBound)), ctx.mkGe(c[i][j][k], ctx.mkInt(-condBound)));
							coefficientBoundLeaf = ctx.mkAnd(coefficientBoundLeaf, ctx.mkLe(c[i][j][k], ctx.mkInt(leafBound)), ctx.mkGe(c[i][j][k], ctx.mkInt(-leafBound)));
						}
						for (int t = 1; t < k; t++) {
							oneInThree = ctx.mkAnd(oneInThree, ctx.mkOr(ctx.mkEq(c[i][j][k], ctx.mkInt(0)), ctx.mkEq(c[i][j][t], ctx.mkInt(0))));
						}
						oneInThree = ctx.mkOr(oneInThree, ctx.mkEq(c[i][j][0], ctx.mkInt(0)));

					}

					if (j < ((bound - 1)/2)) {
						//coefficientProp = ctx.mkAnd(coefficientProp, coeffEqualOneOrMinusOne, coefficientBound);
						coefficientProp = ctx.mkAnd(coefficientProp, coeffEqualOneOrMinusOne, coefficientBound, oneInThree);
					} else {
						boolean isBool = problem.requests.get(problem.names.get(i)).getRange().toString().equals("Bool");
						if (isBool) {
							coefficientProp = ctx.mkAnd(coefficientProp, ctx.mkOr(coeffEqualOneOrMinusOne, cProp), coefficientBoundLeaf);
							//coefficientProp = ctx.mkAnd(coefficientProp, coeffEqualOneOrMinusOne, coefficientBoundLeaf, oneInThree);
						}
						coefficientProp = ctx.mkAnd(coefficientProp, coefficientBoundLeaf);
					}
				}
			}
		}
		return coefficientProp;
	}

	public BoolExpr expandCoefficientGeneral() {
		// Currently only has term range restrictions and general valid
		BoolExpr cond = ctx.mkTrue();
		for (int i = 0; i < numFunc; i++) {
			int order = grammar.ruleOrders.get(i);
			cond = ctx.mkAnd(cond, validPredicate(i));
			for (int j = 0; j < bound; j++) {
				ArithExpr term = t[i][j];
				cond = ctx.mkAnd(cond, ctx.mkGe(term, ctx.mkInt(0)), ctx.mkLt(term, ctx.mkInt(order)));
			}
		}
		return cond;
	}

	public Expr generateEval(int k, int i) {

		String name = problem.names.get(k);
		Expr[] var = problem.requestUsedArgs.get(name);


		ArithExpr poly = c[k][i][0];

		for (int j = 1; j < c[k][i].length; j++) {
			poly = ctx.mkAdd(poly, ctx.mkMul(c[k][i][j], (ArithExpr)var[j - 1]));
		}

		BoolExpr condition;
		if (c[k].length <= equationBound) {
			condition = ctx.mkOr(ctx.mkAnd(ctx.mkGt(poly, ctx.mkInt(0)), eq[k][i])
							, ctx.mkEq(poly, ctx.mkInt(0)));
		} else {
			condition = ctx.mkGe(poly, ctx.mkInt(0));
		}

		if (i < ((bound - 1)/2)) {
			return ctx.mkITE(condition, generateEval(k, 2*i + 1), generateEval(k, 2*i + 2));
		} else {
			boolean isBool = problem.requests.get(name).getRange().toString().equals("Bool");
			if (isBool) {
				return ctx.mkITE(condition, ctx.mkTrue(), ctx.mkFalse());
			} else {
				return poly;
			}

		}

	}

	SygusProblem.SybType resolveSyb(int funcIndex, String syb) {
		if (grammar.cfgs[funcIndex].sybTypeTbl.containsKey(syb)){
			return grammar.cfgs[funcIndex].sybTypeTbl.get(syb);
		} else if (problem.glbSybTypeTbl.containsKey(syb)){
			return problem.glbSybTypeTbl.get(syb);
		} else {
			return null;
		}
	}

	public Expr interpretGeneral(int funcIndex, int[] terms) {
		assert isInterpretable(funcIndex, terms.length);
		ConcreteInterpreter conInte = new ConcreteInterpreter();
		return conInte.interpretConcrete(funcIndex, terms, 0);
	}

	public ASTGeneral expandGeneral(int funcIndex, int[] terms) {
		assert isInterpretable(funcIndex, terms.length);
		ASTBuilder astBuilder = new ASTBuilder();
		return astBuilder.buildAST(funcIndex, terms, 0);
	}

	class ASTBuilder {
		int lastInterpreted = -1;
		public ASTGeneral buildAST(int funcIndex, int[] terms, int start) {
			int ruleIndex = terms[start];
			String[] fullRule = grammar.ruleTbl.get(funcIndex).get(ruleIndex);
			String termSyb = fullRule[0];
			int argCount = fullRule.length - 1;
			ASTGeneral result = new ASTGeneral();
			SygusProblem.SybType termType = resolveSyb(funcIndex, termSyb);
			lastInterpreted = start;
			if (termType == SygusProblem.SybType.NUMERAL) {
				result.node = termSyb;
			} else if (termType == SygusProblem.SybType.GLBVAR) {
				result.node = termSyb;
			} else if (termType == SygusProblem.SybType.LCLARG) {
				result.node = termSyb;
			} else if (termType == SygusProblem.SybType.CSTINT) {
				result.node = Integer.toString(terms[start + 1]);
				lastInterpreted = start + 1;
			} else if (termType == SygusProblem.SybType.CSTBOL) {
				if (terms[start + 1] == 0) {
					result.node = "false";
				} else {
					result.node = "true";
				}
				lastInterpreted = start + 1;
			} else if (termType == SygusProblem.SybType.SYMBOL) {
				result.node = termSyb;
			} else {
				assert termType == SygusProblem.SybType.FUNC;
				result.node = termSyb;
				for (int i = 0; i < argCount; i++) {
					result.children.add(buildAST(funcIndex, terms, lastInterpreted + 1));
				}
			}
			return result;
		}
	}

	class ConcreteInterpreter {
		int lastInterpreted = -1;
		boolean doNotInterpFuncs = false;
		public Expr interpretConcrete(int funcIndex, int[] terms, int start) {
			int ruleIndex = terms[start];

			String[] fullRule = grammar.ruleTbl.get(funcIndex).get(ruleIndex);
			String termSyb = fullRule[0];
			int argCount = fullRule.length - 1;
			SygusProblem.SybType termType = resolveSyb(funcIndex, termSyb);
			Expr result;
			lastInterpreted = start;
			if (termType == SygusProblem.SybType.NUMERAL) {
				result = ctx.mkInt(Integer.parseInt(termSyb));
			} else if (termType == SygusProblem.SybType.GLBVAR) {
				result = problem.vars.get(termSyb);
			} else if (termType == SygusProblem.SybType.LCLARG) {
				result = grammar.cfgs[funcIndex].localArgs.get(termSyb);
			} else if (termType == SygusProblem.SybType.CSTINT) {
				result = ctx.mkInt(terms[start + 1]);
				lastInterpreted = start + 1;
			} else if (termType == SygusProblem.SybType.CSTBOL) {
				if (terms[start + 1] == 0) {
					result = ctx.mkFalse();
				} else {
					result = ctx.mkTrue();
				}
				lastInterpreted = start + 1;
			} else if (termType == SygusProblem.SybType.SYMBOL) {
				result = interpretConcrete(funcIndex, terms, lastInterpreted + 1);
			} else {
				assert termType == SygusProblem.SybType.FUNC;
				Expr[] args = new Expr[argCount];
				for (int i = 0; i < argCount; i++) {
					args[i] = interpretConcrete(funcIndex, terms, lastInterpreted + 1);
				}
				result = problem.opDis.dispatch(termSyb, args, true, doNotInterpFuncs);
			}
			return result;
		}
	}

	public Expr interpretGeneral(int funcIndex){
		IntExpr[] terms = t[funcIndex];
		return generateInterpret(funcIndex, terms, "Start");
	}

	public BoolExpr validPredicate(int funcIndex){
		IntExpr[] terms = t[funcIndex];
		return generateValid(funcIndex, terms, "Start");
	}

	public boolean isInterpretableNow() {
		for (int i = 0; i < numFunc; i++) {
			if (!isInterpretable(i, bound)) {
				return false;
			}
		}
		return true;
	}

	Expr getFallback(int funcIndex, int ruleIndex) {
		Sort sort = grammar.ruleTypeLookup.get(funcIndex).get(ruleIndex);
		return getFallback(sort);
	}

	Expr getFallback(int funcIndex, String ruleName) {
		Sort sort = grammar.cfgs[funcIndex].grammarSybSort.get(ruleName);
		return getFallback(sort);
	}

	Expr getFallback(Sort sort) {
		if (sort.equals(ctx.getIntSort())) {
			return ctx.mkInt(0);
		} else if (sort.equals(ctx.getBoolSort())) {
			return ctx.mkFalse();
		} else {
			assert false;
			return null;
		}
	}

	// Interpret generation for expanding vector vars with grammar rule
	// corresponding to ruleIndex
	public Expr generateInterpret(int funcIndex, IntExpr[] vars, int ruleIndex) {
		assert isInterpretable(funcIndex, vars.length, ruleIndex);
		String cacheKey = Integer.toString(funcIndex) + "+" + Integer.toString(ruleIndex) + "_" + Integer.toString(vars.length);
		IntExpr[] ivars = it.subList(0, vars.length).toArray(new IntExpr[vars.length]);
		if (interpretCache.containsKey(cacheKey)) {
			return interpretCache.get(cacheKey).substitute(ivars, vars);
		}
		String[] fullRule = grammar.ruleTbl.get(funcIndex).get(ruleIndex);
		String termSyb = fullRule[0];
		Expr result;
		SygusProblem.SybType termType = resolveSyb(funcIndex, termSyb);
		if (termType == SygusProblem.SybType.NUMERAL) {
			result = ctx.mkInt(Integer.parseInt(termSyb));
		} else if (termType == SygusProblem.SybType.GLBVAR) {
			result = problem.vars.get(termSyb);
		} else if (termType == SygusProblem.SybType.LCLARG) {
			result = grammar.cfgs[funcIndex].localArgs.get(termSyb);
		} else if (termType == SygusProblem.SybType.CSTINT) {
			result = ivars[1];
		} else if (termType == SygusProblem.SybType.CSTBOL) {
			result = ctx.mkITE(
			ctx.mkEq(ivars[1], ctx.mkInt(0)),
			ctx.mkFalse(),
			ctx.mkTrue()
			);
		} else if (termType == SygusProblem.SybType.SYMBOL) {//todo:add support for SYMBOL
			result = generateInterpret(funcIndex, ivars, termSyb);
		} else {
			assert termType == SygusProblem.SybType.FUNC;
			int termLength = ivars.length - 1;
			int argCount = fullRule.length - 1;
			if (argCount == 1) {
				IntExpr[] subterms = Arrays.copyOfRange(ivars, 1, ivars.length);
				String subtermSyb = fullRule[1];
				SygusProblem.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
				assert subtermType == SygusProblem.SybType.SYMBOL;
				Expr subtermInterpreted = generateInterpret(funcIndex, subterms, subtermSyb);
				result = problem.opDis.dispatch(termSyb, new Expr[]{subtermInterpreted}, true, false);
			} else {
				int[][] combinations = combination(termLength, argCount - 1);
				List<BoolExpr> branchGuards = new ArrayList<BoolExpr>();
				List<Expr> branches = new ArrayList<Expr>();
				Expr iteExpr = getFallback(funcIndex, ruleIndex);
				int brCount = 0;
				for (int[] division: combinations) {
					int start = 1;
					boolean allInterpretable = true;
					BoolExpr[] structValids = new BoolExpr[division.length];
					Expr[] argsInterpreted = new Expr[division.length + 1];
					for (int i = 0; i <= division.length; i++) {
						int end;
						if (i == division.length) {
							end = ivars.length;
						} else {
							end = division[i] + 1;
						}
						IntExpr[] subterms = Arrays.copyOfRange(ivars, start, end);
						String subtermSyb = fullRule[i + 1];
						SygusProblem.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
						assert subtermType == SygusProblem.SybType.SYMBOL;
						if (!isInterpretable(funcIndex, subterms.length, subtermSyb)) {
							allInterpretable = false;
							break;
						}
						argsInterpreted[i] = generateInterpret(funcIndex, subterms, subtermSyb);
						if (i != division.length) {
							structValids[i] = generateValid(funcIndex, subterms, subtermSyb);
						}
						start = end;
					}
					if (!allInterpretable) {
						continue;
					}
					BoolExpr branchGuard = ctx.mkAnd(structValids);
					Expr branch = problem.opDis.dispatch(termSyb, argsInterpreted, true, false);
					branchGuards.add(branchGuard);
					branches.add(branch);
					iteExpr = ctx.mkITE(branchGuard, branch, iteExpr);
					brCount++;
				}
				result = iteExpr;
			}
		}
		result = result.simplify();
		interpretCache.put(cacheKey, result);
		return result.substitute(ivars, vars);
	}

	// Interpret generation for expanding vector vars to non-terminal ruleName
	public Expr generateInterpret(int funcIndex, IntExpr[] vars, String ruleName) {
		assert isInterpretable(funcIndex, vars.length, ruleName);
		String cacheKey = Integer.toString(funcIndex) + "_" + ruleName + "_" + Integer.toString(vars.length);
		IntExpr[] ivars = it.subList(0, vars.length).toArray(new IntExpr[vars.length]);
		if (interpretCache.containsKey(cacheKey)) {
			return interpretCache.get(cacheKey).substitute(ivars, vars);
		}
		Expr result;
		int ruleS = grammar.subrulePos.get(funcIndex).get(ruleName);
		int ruleE = ruleS + grammar.subruleLen.get(funcIndex).get(ruleName);
		List<BoolExpr> branchGuards = new ArrayList<BoolExpr>();
		List<Expr> branches = new ArrayList<Expr>();
		Expr iteExpr = getFallback(funcIndex, ruleName);
		for (int type = ruleS; type < ruleE; type++) {
			if (!isInterpretable(funcIndex, vars.length, type)) {
				continue;
			}
			BoolExpr branchGuard = generateValid(funcIndex, ivars, type);
			Expr branch = generateInterpret(funcIndex, ivars, type);
			branchGuards.add(branchGuard);
			branches.add(branch);
			iteExpr = ctx.mkITE(branchGuard, branch, iteExpr);
		}
		result = iteExpr.simplify();
		interpretCache.put(cacheKey, result);
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
		int termLength = ivars.length - 1;
		IntExpr typeVar;
		String[] fullRule;
		BoolExpr typeCond;
		int argCount;
		SygusProblem.SybType sybType;
		BoolExpr result;
		if (termLength >= 0) {
			typeVar = ivars[0];
			fullRule = grammar.ruleTbl.get(funcIndex).get(ruleIndex);
			typeCond = ctx.mkEq(typeVar, ctx.mkInt(ruleIndex));
			argCount = fullRule.length - 1;
			sybType = resolveSyb(funcIndex, fullRule[0]);
		} else {
			typeVar = null;
			fullRule = null;
			typeCond = null;
			argCount = 0;
			sybType = null;
		}
		if (sybType == null) {
			result = ctx.mkFalse();
		}
		else if (fullRule != null && fullRule[0].equals("ConstantInt")) {
			if (termLength == 1) {
				result = typeCond;
			} else {
				result = ctx.mkFalse();
			}
		} else if (fullRule != null && fullRule[0].equals("ConstantBool")) {
			if (termLength == 1) {
				IntExpr valueVar = ivars[1];
				result = ctx.mkAnd(typeCond, ctx.mkOr(ctx.mkEq(valueVar, ctx.mkInt(0)), ctx.mkEq(valueVar, ctx.mkInt(1))));
			} else {
				result = ctx.mkFalse();
			}

		} else if (argCount > termLength) {
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
			SygusProblem.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
			if(subtermType == SygusProblem.SybType.SYMBOL){
				result = ctx.mkAnd(typeCond, generateValid(funcIndex, subterms, subtermSyb));
			}else{
				return ctx.mkTrue();
			}
			
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
					SygusProblem.SybType subtermType = grammar.cfgs[funcIndex].sybTypeTbl.get(subtermSyb);
					assert subtermType == SygusProblem.SybType.SYMBOL;
					subValids[i] = generateValid(funcIndex, subterms, subtermSyb);
					start = end;
				}
				candidates[canCount] = ctx.mkAnd(subValids);
				canCount++;
			}
			result = ctx.mkAnd(typeCond, ctx.mkOr(candidates));
		}
		result = (BoolExpr)result.simplify();
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
		result = (BoolExpr)result.simplify();
		validCache.put(cacheKey, result);
		return (BoolExpr)result.substitute(ivars, vars);
	}

	// Overall interpretability check
	public boolean isInterpretable(int funcIndex, int varLength, int ruleIndex) {
		IntExpr[] vars = Arrays.copyOfRange(t[funcIndex], 0, varLength);
		BoolExpr valid = generateValid(funcIndex, vars, ruleIndex);
		return !valid.isFalse();
	}

	public boolean isInterpretable(int funcIndex, int varLength, String ruleName) {
		IntExpr[] vars = Arrays.copyOfRange(t[funcIndex], 0, varLength);
		BoolExpr valid = generateValid(funcIndex, vars, ruleName);
		return !valid.isFalse();
	}

	public boolean isInterpretable(int funcIndex, int varLength) {
		return isInterpretable(funcIndex, varLength, "Start");
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
