import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class BVEnum extends Thread {

    private Context ctx;
    private SygusProblem problem;
    private Logger logger;
    private int numCore;

    // for now, we assume there is only one function to synthesize
    // TODO: support multiple functions
    // TODO: support BV constant
    private String start = null;  // start symbol of the grammar
    private Map<String, List<String[]>> recRules = new LinkedHashMap<String, List<String[]>>();  // grammar rules with recursive production
    private List<String[]> startRecRules;// grammar rules with recursive production for start symbol
    private Map<String, Map<Integer, List<Expr>>> storage = new LinkedHashMap<String, Map<Integer, List<Expr>>>();  // type -> <tree_height -> possible_formulae>
    private Map<Integer, Map<Integer, Set<Integer[]>>> permutations = new LinkedHashMap<Integer, Map<Integer, Set<Integer[]>>>(); // max_height -> <num_args -> possible_permutations>

    private Verifier verifier;
    public DefinedFunc[] results;

    public BVEnum (Context ctx, SygusProblem problem, Logger logger, int numCore) {
        this.ctx = ctx;
        this.problem = problem;
        this.logger = logger;
        this.numCore = numCore;
        this.results = new DefinedFunc[problem.names.size()];
        this.verifier = new Verifier(this.ctx, this.problem);
    }

    public void run() {
        if (problem.names.size() > 1) {
            logger.info("BV backend does not support multiple functions yet");
            System.exit(1);
        } else {
            logger.info("BV backend not implemented yet");
            Expr result = generate();
            String name = problem.names.get(0);
            this.results[0] = new DefinedFunc(ctx, name, problem.requestArgs.get(name), result);
        }
    }

    void init() {
        // find out starting non-terminal
        String funcName = problem.names.get(0);
        SygusProblem.CFG cfg = problem.cfgs.get(funcName);
        this.start = (new ArrayList<String>(cfg.grammarSybSort.keySet())).get(0);
        logger.info("Start symbol: " + start);
        // initialize storage and recRules
        for (String symbol : cfg.grammarRules.keySet()) {
            List<String[]> recs = new ArrayList<String[]>();
            Map<Integer, List<Expr>> exprByHeight = new LinkedHashMap<Integer, List<Expr>>();
            List<String[]> rules = cfg.grammarRules.get(symbol);
            for (String[] rule : rules) {
                if (rule.length == 1) {
                    // convert rule[0] to expr
                    String term = rule[0];
                    SygusProblem.SybType termType = getSybType(cfg, term);
                    if(termType.toString() == "CSTBIT"){
                        /*for(int i = 0;i < 100;i++){ // not sure if this is the correct way to implement (Constant _Bitvec 32)
                           Expr terminal =  SygusExtractor.hex(i);
                           if (!exprByHeight.containsKey(1)) {
                                List<Expr> exprs = new ArrayList<Expr>();
                                exprs.add(terminal);
                                exprByHeight.put(1, exprs);
                            } else {
                                exprByHeight.get(1).add(terminal);
                            }
                        }*/
                    }else{
                        Expr terminal = getSybExpr(cfg, term, termType);
                        // populate storage with expressions with h = 1
                        if (!exprByHeight.containsKey(1)) {
                            List<Expr> exprs = new ArrayList<Expr>();
                            exprs.add(terminal);
                            exprByHeight.put(1, exprs);
                        } else {
                            exprByHeight.get(1).add(terminal);
                        }
                    }
                } else {
                    recs.add(rule);
                }
            }
            this.recRules.put(symbol, recs);
            this.storage.put(symbol, exprByHeight);
        }
        this.startRecRules = recRules.get(start);
    }

    SygusProblem.SybType getSybType(SygusProblem.CFG cfg, String syb) {
		if (cfg.sybTypeTbl.containsKey(syb)){
			return cfg.sybTypeTbl.get(syb);
		} else if (this.problem.glbSybTypeTbl.containsKey(syb)){
			return this.problem.glbSybTypeTbl.get(syb);
		} else {
			return null;
		}
	}

    Expr getSybExpr(SygusProblem.CFG cfg, String term, SygusProblem.SybType type) {
        if (type == SygusProblem.SybType.NUMERAL) {
            return ctx.mkInt(Integer.parseInt(term));
        } else if (type == SygusProblem.SybType.HEX) {
            return SygusExtractor.hex(term);
        } else if (type == SygusProblem.SybType.BIN) {
            return SygusExtractor.bin(term);
        } else if (type == SygusProblem.SybType.GLBVAR) {
            return this.problem.vars.get(term);
        } else if (type == SygusProblem.SybType.LCLARG) {
            return cfg.localArgs.get(term);
        } else {
            logger.severe("getSybExpr: Symbol type unseen before");
            return null;
        }
    }

    Set<Integer[]> genPermutations(int maxH, int numArgs) {
        // Generate all possible combinations of maxH with size (numArgs - 1)
        List<Integer[]> res = new ArrayList<Integer[]>();
        Integer[] working = new Integer[numArgs - 1];
        genPrmtHelper(working, 0, 1, maxH, numArgs - 1, res);

        // then insert maxH into every possible position
        Set<Integer[]> prmts = new TreeSet<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
              return Arrays.equals(o1, o2)? 0 : Arrays.hashCode(o1) - Arrays.hashCode(o2);
            }
        });
        for (int i = 0; i < numArgs; i++) {
            for (Integer[] arr : res) {
                List<Integer> arrlist = new ArrayList<Integer>(Arrays.asList(arr));
                arrlist.add(i, maxH);
                Integer[] resarr = new Integer[numArgs];
                resarr = arrlist.toArray(resarr);
                prmts.add(resarr);
            }
        }

        return prmts;
    }

    void genPrmtHelper(Integer[] working, int index, int start, int end, int size, List<Integer[]> res) {
        if (index == size) {
            Integer[] copy = new Integer[working.length];
            System.arraycopy(working, 0, copy, 0, working.length);
            res.add(copy);
            return;
        }

        for (int i = start; i <= end; i++) { 
            working[index] = i; 
            genPrmtHelper(working, index + 1, i, end, size, res); 
        }
    }

    Set<Integer[]> getPermutation(int maxH, int numArgs) {
        // check if the permutation already computed
        if (this.permutations.containsKey(maxH) && this.permutations.get(maxH).containsKey(numArgs)) {
            return this.permutations.get(maxH).get(numArgs);
        }

        // if not, then compute
        Set<Integer[]> computed = genPermutations(maxH, numArgs);
        // collect generated permutations
        if (!this.permutations.containsKey(maxH)) {
            Map<Integer, Set<Integer[]>> map = new LinkedHashMap<Integer, Set<Integer[]>>();
            map.put(numArgs, computed);
            this.permutations.put(maxH, map);
        } else {
            this.permutations.get(maxH).put(numArgs, computed);
        }

        return computed;
    }

    List<List<Expr>> genCombinations(Integer[] heightPrmt, String[] rule) {
        if (heightPrmt.length + 1 != rule.length) {
            logger.severe("Permutation and rule: Length mismatch!");
        }

        List<List<Expr>> res = new ArrayList<List<Expr>>();
        List<Expr> comb = new LinkedList<Expr>();
        this.backtrack(heightPrmt, rule, 0, comb, res);
        return res;
    }

    void backtrack(Integer[] heightPrmt, String[] rule, int index, List<Expr> comb, List<List<Expr>> res) {
        if (index == heightPrmt.length) {
            res.add(new ArrayList<Expr>(comb));
            return;
        }

        List<Expr> exprs = storage.get(rule[index + 1]).get(heightPrmt[index]);
        for (int i = 0; i < exprs.size(); i++) {
            comb.add(exprs.get(i));
            this.backtrack(heightPrmt, rule, index + 1, comb, res);
            comb.remove(comb.size() - 1);
        }
    }

    Expr generate() {
        init();

        // check the expressions with height 1
        List<Expr> exprsHeightOne = storage.get(this.start).get(1);
        for (Expr expr : exprsHeightOne) {
            Map<String, Expr> functions = new HashMap<String, Expr>();
            functions.put(this.problem.names.get(0), expr);
            Status status = verifier.verify(functions);
            if (status == Status.UNSATISFIABLE) {
                return expr;
            }
        }

        // initialize height of expression to 2
        int height = 1;

        while (true) {
            // gradually increase the tree height
            height++;

            for (String nonTerminal : this.storage.keySet()) {
                // generate all the possible expressions of given height and given type
                List<Expr> candidateList = new ArrayList<Expr>();
                // for each possible rule of the start symbol
                for (String[] rule : this.startRecRules) {
                    int numArgs = rule.length - 1;
                    // get or generate all the possible permutations of tree heights
                    Set<Integer[]> permutations = getPermutation(height - 1, numArgs);
                    for (Integer[] permutation : permutations) {
                        logger.info("Subtree heights: " + Arrays.deepToString(permutation));
                        // get all the possible instances of expressions of given tree height
                        List<List<Expr>> argsCombs = genCombinations(permutation, rule);
                        for (List<Expr> argsComb : argsCombs) {
                            Expr[] args = new Expr[argsComb.size()];
                            args = argsComb.toArray(args);
                            Expr candidate = this.problem.opDis.dispatch(rule[0], args, true, false);
                            // logger.info("Operator: " + rule[0]);
                            // logger.info("Args: " + Arrays.deepToString(args));
                            candidateList.add(candidate);
                            // verify each candidate expression
                            if (nonTerminal.equals(this.start)) {
                                logger.info("Verifying candidate: " + candidate.toString());
                                Map<String, Expr> functions = new HashMap<String, Expr>();
                                functions.put(this.problem.names.get(0), candidate);
                                Status status = verifier.verify(functions);
                                if (status == Status.UNSATISFIABLE) {
                                    return candidate;
                                }
                            }
                        }
                    }
                }
                // add candidateList to storage
                logger.info("Store: " + nonTerminal + " height: " + height + " #exprs: " + candidateList.size());
                storage.get(nonTerminal).put(height, candidateList);
            }

        }

        // generating an error using null
        // return null;
    }

    void printRecRules() {
        System.out.println("recRules");
        for (String symbol : recRules.keySet()) {
            System.out.println(symbol);
            for (String[] rule : recRules.get(symbol)) {
                System.out.println(Arrays.deepToString(rule));
            }
            System.out.println();
        }
    }

    void printStorage() {
        System.out.println("storage");
        for (String symbol : storage.keySet()) {
            System.out.println(symbol);
            Map<Integer, List<Expr>> map = storage.get(symbol);
            for (Integer height : map.keySet()) {
                System.out.println("height: " + height);
                for (Expr expr : map.get(height)) {
                    System.out.print(expr + ": " + expr.getSort().toString() + " ");
                }
                System.out.print("\n");
            }
            System.out.println();
        }
    }

    // // iterative version
    // List<List<Expr>> genCombinations(Integer[] heightPrmt, String[] rule) {
    //     if (heightPrmt.length + 1 != rule.length) {
    //         logger.severe("Permutation and rule: Length mismatch!");
    //     }

    //     List<List<Expr>> res = new ArrayList<List<Expr>>();
    //     for (int i = 1; i < rule.length; i++) {
    //         List<List<Expr>> curr = new ArrayList<List<Expr>>();
    //         for (Expr expr : storage.get(rule[i]).get(heightPrmt[i - 1])) {
    //             for (List<Expr> current : curr) {
    //                 List<Expr> copy = new ArrayList<Expr>(current);
    //                 copy.add(expr);
    //                 curr.add(copy);
    //             }
    //         }
    //         res = curr;
    //     }
        
    //     return res;
    // }

}
