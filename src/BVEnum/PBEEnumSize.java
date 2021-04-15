import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class PBEEnumSize extends Thread {

    private Context ctx;
    private SygusProblem problem;
    private Logger logger;
    private int numCore;

    // for now, we assume there is only one function to synthesize
    // TODO: support multiple functions
    // TODO: support BV constant
    private String start = null;  // start symbol of the grammar
    private Map<String, List<String[]>> recRules = new LinkedHashMap<String, List<String[]>>();  // grammar rules with recursive production
    private Map<String, Map<List<String>, Expr>> exprStorage = new LinkedHashMap<String, Map<List<String>, Expr>>();  // nonTerminal -> <output[] -> expr>
    private Map<String, Map<Integer, Set<List<String>>>> outputStorage = new LinkedHashMap<String, Map<Integer, Set<List<String>>>>();  // nonTerminal -> <size -> Set<output[]>>

    private Expr[][] input; // [numExample][numInputArgs]
    private String[] output;    // [numExample]
    private int bvSize;     // size of bitvec expressions in benchmark
    private Expr definition;  // the definition of resulting function

    public DefinedFunc[] results;

    public PBEEnumSize (Context ctx, SygusProblem problem, Logger logger, int numCore) {
        this.ctx = ctx;
        this.problem = problem;
        this.logger = logger;
        this.numCore = numCore;
        this.results = new DefinedFunc[problem.names.size()];
    }

    public void run() {
        if (problem.names.size() > 1) {
            logger.info("BV backend does not support multiple functions yet");
            System.exit(1);
        } else {
            generate();
            String name = problem.names.get(0);
            Expr result = this.definition;
            this.results[0] = new DefinedFunc(ctx, name, problem.requestArgs.get(name), result);
        }
    }

    void init() {
        // find out starting non-terminal
        String funcName = problem.names.get(0);
        SygusProblem.CFG cfg = problem.cfgs.get(funcName);
        this.start = (new ArrayList<String>(cfg.grammarSybSort.keySet())).get(0);
        logger.info("Start symbol: " + start);
        // extract inputs and outputs
        List<List<Expr>> ioexamples = this.problem.ioexamples;
        this.output = new String[ioexamples.size()];
        this.input = new Expr[ioexamples.size()][ioexamples.get(0).size() - 1];
        for (int i = 0; i < ioexamples.size(); i++) {
            List<Expr> example = ioexamples.get(i);
            for (int j = 0; j < example.size() - 1; j++) {
                this.input[i][j] = example.get(j);
            }
            this.output[i] = example.get(example.size() - 1).toString();
            logger.info("Example " + i + " input: " + Arrays.toString(this.input[i]) + ", output: " + this.output[i]);
        }
        this.bvSize = ((BitVecExpr)this.input[0][0]).getSortSize();

        // initialize storage and recRules
        for (String symbol : cfg.grammarRules.keySet()) {
            List<String[]> recs = new ArrayList<String[]>();
            Map<List<String>, Expr> exprStrg = new HashMap<List<String>, Expr>();
            Map<Integer, Set<List<String>>> outputStrg = new HashMap<Integer, Set<List<String>>>();
            Set<List<String>> outputSet = new HashSet<List<String>>();
            outputStrg.put(1, outputSet);

            List<String[]> rules = cfg.grammarRules.get(symbol);
            for (String[] rule : rules) {
                if (rule.length == 1) {
                    // convert rule[0] to expr
                    String term = rule[0];
                    SygusProblem.SybType termType = getSybType(cfg, term);
                    Expr terminal = getSybExpr(cfg, term, termType);
                    logger.info("terminal: " + terminal.toString());
                    // compute output of the terminal
                    Expr[] funcArgs = this.problem.requestArgs.get(this.problem.names.get(0));
                    DefinedFunc df = new DefinedFunc(ctx, funcArgs, terminal);
                    List<String> outputs = new ArrayList<String>();
                    for (int i = 0; i < this.input.length; i++) {
                        // System.out.println(Arrays.toString(this.input[i]));
                        outputs.add(df.apply(this.input[i]).toString());
                    }
                    logger.info("Initial outputs: " + Arrays.toString(outputs.toArray()));
                    // add generated outputs to storages
                    if (!exprStrg.containsKey(outputs)) {
                        exprStrg.put(outputs, terminal);
                        outputStrg.get(1).add(outputs);
                    }
                } else {
                    recs.add(rule);
                }
            }
            this.recRules.put(symbol, recs);

            // initialize global storages
            this.exprStorage.put(symbol, exprStrg);
            this.outputStorage.put(symbol, outputStrg);

        }
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

    List<Integer[]> genPrmt(int budget, int length) {
        List<Integer[]> prmt = new ArrayList<Integer[]>();
        Integer[] working = new Integer[length];
        genPrmtHelper(budget, length, working, 0, prmt);
        return prmt;
    }

    void genPrmtHelper(int budget, int length, Integer[] working, int index, List<Integer[]> prmt) {
        if (index == length) {
            if (budget == 0) {
                Integer[] copy = new Integer[working.length];
                System.arraycopy(working, 0, copy, 0, working.length);
                prmt.add(copy);
            }
            return;
        }    

        if (budget <= 0) {
            return;
        }

        for (int i = 1; i <= budget; i++) {
            working[index] = i;
            genPrmtHelper(budget - i, length, working, index + 1, prmt);
        }
    }

    void genOutputCombs(Integer[] prmt, String[] rule, Set<List<String>> currNew, String nonTerminal) {
        if (prmt.length + 1 != rule.length) {
            logger.severe("prmt and rule: Length mismatch!");
        }

        List<List<String>> comb = new ArrayList<List<String>>();
        List<Expr> subexprs = new ArrayList<Expr>();
        this.genOutputCombsHelper(prmt, rule, 0, comb, subexprs, currNew, nonTerminal);
    }

    void genOutputCombsHelper(Integer[] prmt, String[] rule, int index, List<List<String>> comb, List<Expr> subexprs, Set<List<String>> currNew, String nonTerminal) {
        if (index == prmt.length) {
            // for each combination, compute the output[]
            List<String> outputs = this.compute(rule, comb);
            // check if newly-generated outputs have already existed in storage
            if (this.exprStorage.get(nonTerminal).containsKey(outputs)) {
                // if exist, return
                return;
            }
            // generate new expression
            Expr[] operands = subexprs.toArray(new Expr[subexprs.size()]);
            Expr newExpr = this.problem.opDis.dispatch(rule[0], operands, true, true);
            // check if the output[] are expected
            if (this.verifyOutput(outputs)) {
                // if so, assign those possible expressions to this.definition
                this.definition = newExpr;
            } else {
                // add output[]&expressions to storages
                currNew.add(outputs);
                this.exprStorage.get(nonTerminal).put(outputs, newExpr);
            }
            return;
        }

        Set<List<String>> inputs = this.outputStorage.get(rule[index + 1]).get(prmt[index]);
        for (List<String> input : inputs) {
            comb.add(input);
            subexprs.add(this.exprStorage.get(rule[index + 1]).get(input));
            this.genOutputCombsHelper(prmt, rule, index + 1, comb, subexprs, currNew, nonTerminal);
            comb.remove(comb.size() - 1);
            subexprs.remove(subexprs.size() - 1);
        }
    }

    Expr[][] transpose(List<List<String>> comb) {
        int n = comb.size();
        int m = comb.get(0).size();
        Expr[][] args = new Expr[n][m];
        for (int i = 0; i < comb.size(); i++) {
            for (int j = 0; j < comb.get(i).size(); j++) {
                String str = comb.get(i).get(j);
                args[i][j] = ctx.mkBV(str, this.bvSize);
            }
        }
        Expr[][] trans = new Expr[m][n];
        for (int i = 0; i < trans.length; i++) {
            for (int j = 0; j < trans[0].length; j++) {
                trans[i][j] = args[j][i];
            }
        }       
        return trans;
    }

    List<String> compute(String[] rule, List<List<String>> comb) {
        if (comb.size() != rule.length - 1) {
            logger.severe("Outputs Length mismatch!");
        }

        Expr[][] args = this.transpose(comb);
        List<String> outputs = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            outputs.add(this.problem.opDis.dispatch(rule[0], args[i], true, false).simplify().toString());
        }

        return outputs;
    }

    boolean verifyOutput(List<String> outputs) {
        long start = System.nanoTime();
        if (outputs.size() != this.output.length) {
            logger.severe("Outputs Length mismatch!");
        }

        for (int i = 0; i < outputs.size(); i++) {
            if (!this.output[i].equals(outputs.get(i))) {
                return false;
            }
        }
        long usedTime = System.nanoTime() - start;
        System.out.println("Time verifyOutput: " + usedTime);

        return true;
    }

    void generate() {
        // init storage, compute initial output and put to storage
        init();

        // check those initial outputs
        Map<List<String>, Expr> initStorage = this.exprStorage.get(this.start);
        for (List<String> out : initStorage.keySet()) {
            if (this.verifyOutput(out)) {
                this.definition = initStorage.get(out);
                return;
            }
        }

        int iter = 1;

        while (true) {
            iter++;
            logger.info("Size: " + iter);

            // for each non-terminal
            for (String nonTerminal : this.outputStorage.keySet()) {
                Set<List<String>> newOutputs = new HashSet<List<String>>();

                // for every operator
                for (String[] rule : this.recRules.get(nonTerminal)) {
                    // if (rule[0].equals("im")) {
                    //     continue;
                    // }
                    System.out.println("rule: " + Arrays.toString(rule));
                    // generate all possible sums that add up to the given size
                    List<Integer[]> prmts = genPrmt(iter - 1, rule.length - 1);
                    for (Integer[] prmt : prmts) {
                        System.out.println("prmt: " + Arrays.toString(prmt));
                        genOutputCombs(prmt, rule, newOutputs, nonTerminal);
                        if (this.definition != null) {
                            return;
                        }
                    }
                }
                
                logger.info("Store: " + nonTerminal + " size: " + iter + " #exprs: " + newOutputs.size());
                this.outputStorage.get(nonTerminal).put(iter, newOutputs);
            }

        }

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

    void printExprStorage(Map<String, Map<List<String>, Expr>> exprStorage) {
        System.out.println("Print storage:");
        for (String nonTerminal : exprStorage.keySet()) {
            System.out.println(nonTerminal);
            Map<List<String>, Expr> map = exprStorage.get(nonTerminal);
            for (List<String> subexpr : map.keySet()) {
                System.out.println("outputs: " + Arrays.toString(subexpr.toArray()));
                System.out.println("expr: " + map.get(subexpr).toString());
            }
            System.out.println();
        }
    }

    void printOutputStorage(Map<String, Map<Integer, Set<List<String>>>> outputStorage) {
        System.out.println("Print storage:");
        for (String nonTerminal : outputStorage.keySet()) {
            System.out.println(nonTerminal);
            Map<Integer, Set<List<String>>> map = outputStorage.get(nonTerminal);
            for (Integer size : map.keySet()) {
                System.out.println("Size: " + size);
                System.out.println("#expr: " + map.get(size).size());
            }
            System.out.println();
        }
    }

    void printStorageSize(Map<String, Map<List<String>, Expr>> storage) {
        for (String nonTerminal : storage.keySet()) {
            if (storage.containsKey(nonTerminal)) {
                logger.info(nonTerminal + " in storage: " + storage.get(nonTerminal).size());
            }
        }
    }

}