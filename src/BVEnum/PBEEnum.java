import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class PBEEnum extends Thread {

    private Context ctx;
    private SygusProblem problem;
    private Logger logger;
    private int numCore;

    // for now, we assume there is only one function to synthesize
    // TODO: support multiple functions
    // TODO: support BV constant
    private String start = null;  // start symbol of the grammar
    private Map<String, List<String[]>> recRules = new LinkedHashMap<String, List<String[]>>();  // grammar rules with recursive production
    private Map<String, Map<List<String>, Expr>> storage = new LinkedHashMap<String, Map<List<String>, Expr>>();  // nonTerminal -> <output[] -> expr>
    private Map<String, Map<List<String>, Expr>> prevNewStorage = new LinkedHashMap<String, Map<List<String>, Expr>>();  // nonTerminal -> <output[] -> expr>
    private Map<String, Map<List<String>, Expr>> currNewStorage = new LinkedHashMap<String, Map<List<String>, Expr>>();  // nonTerminal -> <output[] -> expr>
    private Expr[][] input; // [numExample][numInputArgs]
    private String[] output;    // [numExample]
    private int bvSize;     // size of bitvec expressions in benchmark
    private Expr definition;  // the definition of resulting function

    public DefinedFunc[] results;

    public PBEEnum (Context ctx, SygusProblem problem, Logger logger, int numCore) {
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
            Map<List<String>, Expr> outputStorage = new HashMap<List<String>, Expr>();

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
                    // add generated outputs to outputStorage
                    outputStorage.putIfAbsent(outputs, terminal);
                } else {
                    recs.add(rule);
                }
            }
            this.recRules.put(symbol, recs);
            this.prevNewStorage.put(symbol, outputStorage);

            // initialize this.storage
            this.storage.put(symbol, new HashMap<List<String>, Expr>());

            // printStorage(this.prevNewStorage);
            // printStorage(this.storage);
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

    Set<List<String>> genPrmt(int length) {
        // assume "1" represents "new", "0" represents "old"
        Set<List<String>> prmt = new HashSet<List<String>>();
        List<String> working = new ArrayList<String>();
        genOldNewPrmt(new String[]{"1", "0"}, length, working, 0, prmt);
        // if (length == 2) {
        //     List<String> rm = new ArrayList<String>();
        //     rm.add("1");
        //     rm.add("0");
        //     prmt.remove(rm);
        // }
        return prmt;
    }

    void genOldNewPrmt(String[] inputSet, int length, List<String> working, int index, Set<List<String>> prmt) {
        if (index == length) {
            if (working.contains("1")) {
                List<String> copy = new ArrayList<String>(working);
                prmt.add(copy);
            }
            return;
        }    

        for (int i = 0; i < inputSet.length; i++) {
            working.add(inputSet[i]);
            genOldNewPrmt(inputSet, length, working, index + 1, prmt);
            working.remove(working.size() - 1);
        }
    }

    void genOutputCombs(List<String> oldNewPrmt, String[] rule, Map<List<String>, Expr> currNew) {
        if (oldNewPrmt.size() + 1 != rule.length) {
            logger.severe("oldNewPrmt and rule: Length mismatch!");
        }

        List<List<String>> comb = new ArrayList<List<String>>();
        List<Expr> subexprs = new ArrayList<Expr>();
        this.genOutputCombsHelper(oldNewPrmt, rule, 0, comb, subexprs, currNew);
    }

    void genOutputCombsHelper(List<String> oldNewPrmt, String[] rule, int index, List<List<String>> comb, List<Expr> subexprs, Map<List<String>, Expr> currNew) {
        if (index == oldNewPrmt.size()) {
            // for each combination, compute the output[]
            List<String> outputs = this.compute(rule, comb);
            // generate new expression
            Expr[] operands = subexprs.toArray(new Expr[subexprs.size()]);
            Expr newExpr = this.problem.opDis.dispatch(rule[0], operands, true, false);
            // check if the output[] are expected
            if (this.verifyOutput(outputs)) {
                // if so, assign those possible expressions to this.definition
                this.definition = newExpr;
            } else {
                // add output[]&expressions to currNew
                currNew.putIfAbsent(outputs, newExpr);
            }
            return;
        }

        Map<List<String>, Expr> storagePointer = null;
        if (oldNewPrmt.get(index) == "1") {
            storagePointer = this.prevNewStorage.get(rule[index + 1]);
        } else {
            storagePointer = this.storage.get(rule[index + 1]);
        }

        List<List<String>> inputs = new ArrayList<List<String>>(storagePointer.keySet());
        for (int i = 0; i < inputs.size(); i++) {
            comb.add(inputs.get(i));
            subexprs.add(storagePointer.get(inputs.get(i)));
            this.genOutputCombsHelper(oldNewPrmt, rule, index + 1, comb, subexprs, currNew);
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
        Map<List<String>, Expr> initStorage = this.prevNewStorage.get(this.start);
        for (List<String> out : initStorage.keySet()) {
            if (this.verifyOutput(out)) {
                this.definition = initStorage.get(out);
                return;
            }
        }

        int iter = 1;

        while (true) {
            logger.info("Interation: " + iter);
            iter++;
            
            // System.out.println("storage");
            // printStorage(this.storage);
            // System.out.println("prevNewStorage");
            // printStorage(this.prevNewStorage);
            // System.out.println("currNewStorage");
            // printStorage(this.currNewStorage);
            
            // if (iter > 4) {
            //     System.exit(0);
            // }

            // for each non-terminal
            for (String nonTerminal : this.storage.keySet()) {
                Map<List<String>, Expr> currNew = new HashMap<List<String>, Expr>();

                // for every operator
                for (String[] rule : this.recRules.get(nonTerminal)) {
                    if (rule[0].equals("im")) {
                        continue;
                    }
                    System.out.println("rule: " + Arrays.toString(rule));
                    // generate all possible permutations of "old" and "new"
                    // for example, if there are two arguments for this operator
                    // possible permutations are [new, new] [old, new] [new, old]
                    Set<List<String>> prmts = genPrmt(rule.length - 1);
                    for (List<String> prmt : prmts) {
                        System.out.println("prmt: " + Arrays.toString(prmt.toArray()));
                        genOutputCombs(prmt, rule, currNew);
                        if (this.definition != null) {
                            return;
                        }
                    }
                }
                
                currNewStorage.put(nonTerminal, currNew);
            }

            // System.out.println("currNewStorage: before adding to prevNewStorage");
            // printStorage(this.currNewStorage);

            // update storage & prevNewStorage
            for (String nonTerminal : this.storage.keySet()) {
                Map<List<String>, Expr> currStrg = this.storage.get(nonTerminal);
                Map<List<String>, Expr> prevNewStrg = this.prevNewStorage.get(nonTerminal);
                Map<List<String>, Expr> currNewStrg = this.currNewStorage.get(nonTerminal);

                // put pairs in prevNewStorage to storage one by one
                for (List<String> outputs : prevNewStrg.keySet()) {
                    currStrg.putIfAbsent(outputs, prevNewStrg.get(outputs));
                }

                // remove pairs (in currNew) of which key exists in storage
                List<List<String>> toRm = new ArrayList<List<String>>();
                for (List<String> outputs : currNewStrg.keySet()) {
                    if (currStrg.containsKey(outputs)) {
                        toRm.add(outputs);
                    }
                }
                for (List<String> outputs : toRm) {
                    currNewStrg.remove(outputs);
                }

                // wipe out all the prevNew, put currNew to prevNew
                this.prevNewStorage.get(nonTerminal).clear();
                this.prevNewStorage.put(nonTerminal, this.currNewStorage.get(nonTerminal));
                this.currNewStorage.put(nonTerminal, null);
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

    void printStorage(Map<String, Map<List<String>, Expr>> storage) {
        System.out.println("Print storage:");
        for (String nonTerminal : storage.keySet()) {
            System.out.println(nonTerminal);
            Map<List<String>, Expr> map = storage.get(nonTerminal);
            for (List<String> subexpr : map.keySet()) {
                System.out.println("outputs: " + Arrays.toString(subexpr.toArray()));
                System.out.println("expr: " + map.get(subexpr).toString());
            }
            System.out.println();
        }
    }

}