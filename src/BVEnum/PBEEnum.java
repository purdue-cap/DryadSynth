import java.math.BigInteger;
import java.util.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;
import com.microsoft.z3.*;
import java.util.logging.Logger;

public class PBEEnum extends Thread {

    private Context ctx;
    private SygusProblem problem;
    private Logger logger;
    private int numCore;
    private int size;

    // for now, we assume there is only one function to synthesize
    // TODO: support multiple functions
    // TODO: support BV constant
    private String start = null;  // start symbol of the grammar
    private Map<String, List<String[]>> recRules = new LinkedHashMap<String, List<String[]>>();  // grammar rules with recursive production
    private Map<String, Map<bitVector[], Set<Expr>>> storage = new LinkedHashMap<String, Map<bitVector[], Set<Expr>>>();  // nonTerminal -> <output[] -> Set<bitVector>>
    private Map<String, Map<bitVector[], Set<Expr>>> prevNewStorage = new LinkedHashMap<String, Map<bitVector[], Set<Expr>>>();  // nonTerminal -> <output[] -> Set<bitVector>>
    private Map<String, Map<bitVector[], Set<Expr>>> currNewStorage = new LinkedHashMap<String, Map<bitVector[], Set<Expr>>>();  // nonTerminal -> <output[] -> Set<bitVector>>
    private bitVector[][] input; // [numExample][numInputArgs]
    private String[] output;    // [numExample]
    private Set<Expr> definitions;  // the definitions of resulting function

    public DefinedFunc[] results;

    public PBEEnum (Context ctx, SygusProblem problem, Logger logger, int numCore) {
        this.ctx = ctx;
        this.problem = problem;
        this.logger = logger;
        this.numCore = numCore;
        this.results = new DefinedFunc[problem.names.size()];
        this.size = 64; // for test
    }

    public void run() {
        if (problem.names.size() > 1) {
            logger.info("BV backend does not support multiple functions yet");
            System.exit(1);
        } else {
            // bitVector result = generate();
            generate();
            String name = problem.names.get(0);
            Expr result = null;
            for (Expr e : this.definitions) {
                result = e;
                break;
            }
            this.results[0] = new DefinedFunc(ctx, name, problem.requestArgs.get(name),result);
        }
    }

    public bitVector ExprToBitVector(Expr input){
        bitVector result = new bitVector();
        BigInteger value = new BigInteger(input.toString());
        result.setData(value.longValue());
        return result;
    }

    public Expr BitVectorToExpr(bitVector input){
        Expr result = ctx.mkBV(input.signValue(size).longValue(),64);
        return result;
    }

    class OutputComparator implements Comparator<bitVector[]>{
    	@Override
    	public int compare(bitVector[] output1,bitVector[] output2){
            if (output1.length > output2.length) {
                return 1;
            } else if (output1.length < output2.length) {
                return -1;
            } else {
                int diff = 0;
                for (int i = 0; i < output1.length; i++) {
                    String output1Str = output1[i].toString();
                    String output2Str = output2[i].toString();
                    diff += output1Str.compareTo(output2Str);
                }
                return diff;
            }
    	}
    }

    class ExprComparator implements Comparator<Expr>{
    	@Override
    	public int compare(Expr e1,Expr e2){
            return e1.toString().compareTo(e2.toString());
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
        this.input = new bitVector[ioexamples.size()][ioexamples.get(0).size() - 1];
        for (int i = 0; i < ioexamples.size(); i++) {
            List<Expr> example = ioexamples.get(i);
            for (int j = 0; j < example.size() - 1; j++) {
                this.input[i][j] = ExprToBitVector(example.get(j));
            }
            this.output[i] = example.get(example.size() - 1).toString();
            logger.info("Example " + i + " input: " + Arrays.toString(this.input[i]) + ", output: " + this.output[i]);
        }

        // initialize storage and recRules
        for (String symbol : cfg.grammarRules.keySet()) {
            List<String[]> recs = new ArrayList<String[]>();
            // Map<Integer, List<bitVector>> bitVectorByHeight = new LinkedHashMap<Integer, List<bitVector>>();
            Map<bitVector[], Set<Expr>> outputStorage = new TreeMap<bitVector[], Set<Expr>>(new OutputComparator());

            List<String[]> rules = cfg.grammarRules.get(symbol);
            for (String[] rule : rules) {
                if (rule.length == 1) {
                    // convert rule[0] to bitVector
                    String term = rule[0];
                    SygusProblem.SybType termType = getSybType(cfg, term);
                    // todo: change back to Expr
                    Expr terminal = getSybExpr(cfg, term, termType);
                    logger.info("terminal: " + terminal.toString());
                    // compute output of the terminal
                    Expr[] funcArgs = this.problem.requestArgs.get(this.problem.names.get(0));
                    DefinedFunc df = new DefinedFunc(ctx, funcArgs, terminal);
                    bitVector[] outputs = new bitVector[this.output.length];
                    for (int i = 0; i < this.input.length; i++) {
                        // System.out.println(Arrays.toString(this.input[i]));
                        Expr[] ExprArgs = new Expr[this.input[i].length];
                        for(int j = 0;j<this.input[i].length;j++){
                            ExprArgs[j] = BitVectorToExpr(this.input[i][j]);
                        }
                        outputs[i] = ExprToBitVector(df.apply(ExprArgs));
                    }
                    System.out.println("Outputs[0]:");
                    System.out.println(outputs[0].toString());
                    logger.info("Initial outputs: " + Arrays.toString(outputs));
                    // add generated outputs to outputStorage
                    if (outputStorage.containsKey(outputs)) {
                        outputStorage.get(outputs).add(terminal);
                    } else {
                        Set<Expr> initset = new TreeSet<Expr>(new ExprComparator());
                        initset.add(terminal);
                        outputStorage.put(outputs, initset);
                    }
                } else {
                    recs.add(rule);
                }
            }
            this.recRules.put(symbol, recs);
            this.prevNewStorage.put(symbol, outputStorage);
            // initialize this.storage
            this.storage.put(symbol, new TreeMap<bitVector[], Set<Expr>>(new OutputComparator()));
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

    Set<String[]> genPrmt(int length) {
        // assume "1" represents "new", "0" represents "old"
        Set<String[]> prmt = new HashSet<String[]>();
        String[] working = new String[length];
        genOldNewPrmt(new String[]{"1", "0"}, working, 0, prmt);
        // Arrays.fill(working, "0");
        // prmt.remove(working);
        return prmt;
    }

    void genOldNewPrmt(String[] inputSet, String[] working, int index, Set<String[]> prmt) {
        if (index == working.length) {
            for (int i = 0; i < working.length; i++) {
                if (working[i] == "1") {
                    String[] copy = new String[working.length];
                    System.arraycopy(working, 0, copy, 0, working.length);
                    prmt.add(copy);
                    return;
                }
            }
            // String[] copy = new String[working.length];
            // System.arraycopy(working, 0, copy, 0, working.length);
            // prmt.add(copy);
            return;
        }    

        for (int i = 0; i < inputSet.length; i++) {
            working[index] = inputSet[i];
            genOldNewPrmt(inputSet, working, index + 1, prmt);
        }
    }

    Set<Expr> genExprs(String[] rule, List<Set<Expr>> subexprs) {
        if (subexprs.size() + 1 != rule.length) {
            logger.severe("subexprs and rule: Length mismatch!");
        }

        Set<Expr> exprs = new TreeSet<Expr>(new ExprComparator());
        Expr[] args = new Expr[subexprs.size()];
        this.genExprsHelper(subexprs, rule, 0, args, exprs);
        return exprs;
    }

    void genExprsHelper(List<Set<Expr>> subexprs, String[] rule, int index, Expr[] args, Set<Expr> exprs) {
        // if (!exprs.isEmpty()) {
        //     return;
        // }

        if (index == subexprs.size()) {
            Expr expr = this.problem.opDis.dispatch(rule[0], args, true, false);
            exprs.add(expr);
            return;
        }

        Set<Expr> subexpressions = subexprs.get(index);
        if (subexpressions == null || subexpressions.isEmpty()) {
            return;
        }

        for (Expr e : subexpressions) {
            // if (!exprs.isEmpty()) {
            //     return;
            // }
            args[index] = e;
            this.genExprsHelper(subexprs, rule, index + 1, args, exprs);
            args[index] = null;
        }
    }

    void genOutputCombs(String[] oldNewPrmt, String[] rule, Map<bitVector[], Set<Expr>> currNew) {
        if (oldNewPrmt.length + 1 != rule.length) {
            logger.severe("oldNewPrmt and rule: Length mismatch!");
        }

        List<bitVector[]> comb = new ArrayList<bitVector[]>();
        List<Set<Expr>> subexprs = new ArrayList<Set<Expr>>();
        this.genOutputCombsHelper(oldNewPrmt, rule, 0, comb, subexprs, currNew);
    }

    void genOutputCombsHelper(String[] oldNewPrmt, String[] rule, int index, List<bitVector[]> comb, List<Set<Expr>> subexprs, Map<bitVector[], Set<Expr>> currNew) {
        if (index == oldNewPrmt.length) {
            // for each combination, compute the output[]
            bitVector[] outputs = this.compute(rule, comb);
            // generate every possible expressions
            // long timeend = System.nanoTime();
            // System.out.println("Timestamp end: " + timeend);
            long start = System.nanoTime();
            Set<Expr> newExprs = genExprs(rule, subexprs);
            long usedTime = System.nanoTime() - start;
            // System.out.println("Time genExprs: " + usedTime);
            // long timestart = System.nanoTime();
            // System.out.println("Timestamp start: " + timestart);
            // check if the output[] are expected
            if (this.verifyOutput(outputs)) {
                // if so, assign those possible expressions to this.results
                this.definitions = newExprs;
            } else {
                // add output[]&expressions to currNew
                // check if output[] exists in currNew
                if (currNew.containsKey(outputs)) {
                    // add generated possible expressions to coressponding value in currNew
                    currNew.get(outputs).addAll(newExprs);
                } else {
                    // create new pair in currNew
                    currNew.put(outputs, newExprs);
                }
            }
            return;
        }

        Map<bitVector[], Set<Expr>> storagePointer = null;
        if (oldNewPrmt[index] == "1") {
            storagePointer = this.prevNewStorage.get(rule[index + 1]);
        } else {
            storagePointer = this.storage.get(rule[index + 1]);
        }
        List<bitVector[]> inputs = new ArrayList<bitVector[]>(storagePointer.keySet());
        for (int i = 0; i < inputs.size(); i++) {
            comb.add(inputs.get(i));
            subexprs.add(storagePointer.get(inputs.get(i)));
            this.genOutputCombsHelper(oldNewPrmt, rule, index + 1, comb, subexprs, currNew);
            comb.remove(comb.size() - 1);
            subexprs.remove(subexprs.size() - 1);
        }
    }

    bitVector[][] transpose(List<bitVector[]> comb) {
        int n = comb.size();
        int m = comb.get(0).length;
        bitVector[][] args = new bitVector[n][m];
        for (int i = 0; i < comb.size(); i++) {
            args[i] = comb.get(i);
        }
        bitVector[][] trans = new bitVector[m][n];
        for (int i = 0; i < trans.length; i++) {
            for (int j = 0; j < trans[0].length; j++) {
                trans[i][j] = args[j][i];
            }
        }       
        return trans;
    }

    bitVector[] compute(String[] rule, List<bitVector[]> comb) {
        if (comb.size() != rule.length - 1) {
            logger.severe("Outputs Length mismatch!");
        }

        bitVector[][] args = this.transpose(comb);
        bitVector[] outputs = new bitVector[args.length];
        for (int i = 0; i < args.length; i++) {
            outputs[i] = this.problem.opDis.pbeDispatch(rule[0], args[i], true, false, size);
        }

        return outputs;
    }

    boolean verifyOutput(bitVector[] outputs) {
        long start = System.nanoTime();
        if (outputs.length != this.output.length) {
            logger.severe("Outputs Length mismatch!");
        }

        for (int i = 0; i < outputs.length; i++) {
            if (!this.output[i].equals(outputs[i].toString())) {
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
        Map<bitVector[], Set<Expr>> initStorage = this.prevNewStorage.get(this.start);
        for (bitVector[] out : initStorage.keySet()) {
            if (this.verifyOutput(out)) {
                this.definitions = initStorage.get(out);
                return;
            }
        }

        int iter = 1;

        while (true) {
            logger.info("Interation: " + iter);
            iter++;
            
            System.out.println("storage");
            printStorage(this.storage);
            System.out.println("prevNewStorage");
            printStorage(this.prevNewStorage);
            // System.out.println("currNewStorage");
            // printStorage(this.currNewStorage);
            
            if (iter > 5) {
                System.exit(0);
            }

            // for each non-terminal
            for (String nonTerminal : this.storage.keySet()) {
                Map<bitVector[], Set<Expr>> currNew = new TreeMap<bitVector[], Set<Expr>>(new OutputComparator());
                // for every operator
                for (String[] rule : this.recRules.get(nonTerminal)) {
                    System.out.println("rule: " + Arrays.toString(rule));
                    // generate all possible permutations of "old" and "new"
                    // for example, if there are two arguments for this operator
                    // possible permutations are [new, new] [old, new] [new, old]
                    Set<String[]> prmts = genPrmt(rule.length - 1);
                    for (String[] prmt : prmts) {
                        System.out.println("prmt: " + Arrays.toString(prmt));
                        printCurrNew(currNew);
                        genOutputCombs(prmt, rule, currNew);
                        if (this.definitions != null) {
                            return;
                        }
                    }
                }
                
                currNewStorage.put(nonTerminal, currNew);
            }

            System.out.println("currNewStorage: before adding to prevNewStorage");
            printStorage(this.currNewStorage);

            // update storage & prevNewStorage
            for (String nonTerminal : this.storage.keySet()) {
                Map<bitVector[], Set<Expr>> currStrg = this.storage.get(nonTerminal);
                Map<bitVector[], Set<Expr>> prevNewStrg = this.prevNewStorage.get(nonTerminal);
                Map<bitVector[], Set<Expr>> currNewStrg = this.currNewStorage.get(nonTerminal);

                // put pairs in prevNewStorage to storage one by one
                for (bitVector[] outputs : prevNewStrg.keySet()) {
                    if (currStrg.containsKey(outputs)) {
                        currStrg.get(outputs).addAll(prevNewStrg.get(outputs));
                    } else {
                        currStrg.put(outputs, prevNewStrg.get(outputs));
                    }
                }

                // remove pairs (in currNew) of which key exists in storage
                List<bitVector[]> toRm = new ArrayList<bitVector[]>();
                for (bitVector[] outputs : currNewStrg.keySet()) {
                    if (currStrg.containsKey(outputs)) {
                        System.out.println(Arrays.toString(outputs));
                        currStrg.get(outputs).addAll(currNewStrg.get(outputs));
                        toRm.add(outputs);
                    }
                }
                for (bitVector[] outputs : toRm) {
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

    void printStorage(Map<String, Map<bitVector[], Set<Expr>>> storage) {
        System.out.println("Print storage:");
        for (String nonTerminal : storage.keySet()) {
            System.out.println(nonTerminal);
            Map<bitVector[], Set<Expr>> map = storage.get(nonTerminal);
            for (bitVector[] subbitVector : map.keySet()) {
                System.out.println("subBitVector: " + Arrays.toString(subbitVector));
            }
            System.out.println();
        }
    }

    void printCurrNew(Map<bitVector[], Set<Expr>> storage) {
        System.out.println("Print storage:");
            Map<bitVector[], Set<Expr>> map = storage;
            for (bitVector[] subbitVector : map.keySet()) {
                System.out.println("subBitVector: " + Arrays.toString(subbitVector));
            }
            System.out.println();
    }
}
