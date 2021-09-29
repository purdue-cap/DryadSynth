import java.util.*;
import java.math.BigInteger;
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
    private Map<String, Map<List<Long>, Expr>> exprStorage = new LinkedHashMap<String, Map<List<Long>, Expr>>();  // nonTerminal -> <output[] -> expr>
    private Map<String, Map<Integer, Set<List<Long>>>> outputStorage = new LinkedHashMap<String, Map<Integer, Set<List<Long>>>>();  // nonTerminal -> <size -> Set<output[]>>

    private Expr[][] input; // [numExample][numInputArgs]
    private String[] output;    // [numExample]
    private int bvSize;     // size of bitvec expressions in benchmark
    private Set<String> varNames = new HashSet<String>(); // variable names appear in grammar
    private Expr definition;  // the definition of resulting function

    public DefinedFunc[] results;

    private String[] symmetricOp = {"bvand", "bvor", "bvadd", "bvxor"};
    private Map<Integer, List<Integer>> covered = new HashMap<Integer, List<Integer>>();
    private List<Expr> coveredExpr = new LinkedList<Expr>();
    private Map<Integer, Integer> classToExpr = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> conditionToclass = new HashMap<Integer, Integer>();
    private Map<Integer, Boolean> classTF = new HashMap<Integer, Boolean>();
    private List<Integer> coveredExample = new LinkedList<Integer>();
    private List<Expr> conditionExpr = new LinkedList<Expr>();
    private List<Integer> outsider = new LinkedList<Integer>();
    private List<List<Integer>> equivClass = new LinkedList<List<Integer>>();
    private Integer elseExpr;

    private boolean coverFound = false;

    public PBEEnumSize (Context ctx, SygusProblem problem, Logger logger, int numCore) {
        this.ctx = ctx;
        this.problem = problem;
        this.logger = logger;
        this.numCore = numCore;
        this.results = new DefinedFunc[problem.names.size()];
        // for (String funcname: this.problem.funcs.keySet()) {
        //     System.out.println("Func: " + funcname);
        //     System.out.println("Def: " + this.problem.funcs.get(funcname).getDef().toString());
        // }
    }

    public void run() {
        if (problem.names.size() > 1) {
            logger.info("BV backend does not support multiple functions yet");
            System.exit(1);
        } else {
            generate();
            if (this.definition == null) {
                generateResult();
            }
            String name = problem.names.get(0);
            Expr result = this.definition;
            this.results[0] = new DefinedFunc(ctx, name, problem.requestArgs.get(name), result);
            // System.out.println("this.results[0]: " + this.results[0].toString());
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
            outsider.add(i);
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
            Map<List<Long>, Expr> exprStrg = new HashMap<List<Long>, Expr>();
            Map<Integer, Set<List<Long>>> outputStrg = new HashMap<Integer, Set<List<Long>>>();
            Set<List<Long>> outputSet = new HashSet<List<Long>>();
            outputStrg.put(1, outputSet);

            List<String[]> rules = cfg.grammarRules.get(symbol);
            for (String[] rule : rules) {
                if (rule.length == 1) {
                    // convert rule[0] to expr
                    String term = rule[0];
                    SygusProblem.SybType termType = getSybType(cfg, term);
                    Expr terminal = getSybExpr(cfg, term, termType);
                    logger.info("terminal: " + terminal.toString());
                    // collect variables in grammar
                    if (terminal.isConst()) {
                        this.varNames.add(terminal.toString());
                    }
                    // compute output of the terminal
                    Expr[] funcArgs = this.problem.requestArgs.get(this.problem.names.get(0));
                    DefinedFunc df = new DefinedFunc(ctx, funcArgs, terminal);
                    List<Long> outputs = new ArrayList<Long>();
                    for (int i = 0; i < this.input.length; i++) {
                        // System.out.println(Arrays.toString(this.input[i]));
                        outputs.add(OpDispatcher.exprToBitVector(df.apply(this.input[i])));
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
		if (cfg.sybTypeTbl.containsKey(syb)) {
			return cfg.sybTypeTbl.get(syb);
		} else if (this.problem.glbSybTypeTbl.containsKey(syb)) {
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

    List<Integer[]> genPrmt(int budget, int length, String[] rule) {
        List<Integer[]> prmt = new ArrayList<Integer[]>();
        Integer[] working = new Integer[length];
        genPrmtHelper(budget, length, working, 0, prmt, rule);
        return prmt;
    }

    void genPrmtHelper(int budget, int length, Integer[] working, int index, List<Integer[]> prmt, String[] rule) {
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

        if (Arrays.asList(this.symmetricOp).contains(rule[0]) && index >= 1 && budget < working[index - 1]) {
            return;
        }

        for (int i = 1; i <= budget; i++) {
            working[index] = i;
            genPrmtHelper(budget - i, length, working, index + 1, prmt, rule);
        }
    }

    void genOutputCombs(Integer[] prmt, String[] rule, Set<List<Long>> currNew, String nonTerminal) {
        if (prmt.length + 1 != rule.length) {
            logger.severe("prmt and rule: Length mismatch!");
        }

        List<List<Long>> comb = new ArrayList<List<Long>>();
        List<Expr> subexprs = new ArrayList<Expr>();
        this.genOutputCombsHelper(prmt, rule, 0, comb, subexprs, currNew, nonTerminal);
    }

    void genOutputCombsHelper(Integer[] prmt, String[] rule, int index, List<List<Long>> comb, List<Expr> subexprs, Set<List<Long>> currNew, String nonTerminal) {
        if (index == prmt.length) {
            // generate new expression
            Expr[] operands = subexprs.toArray(new Expr[subexprs.size()]);
            Expr newExpr = this.problem.opDis.dispatch(rule[0], operands, true, true);

            boolean containsVar = false;
            if (containsVar(newExpr, this.varNames)) {
                containsVar = true;
            }

            // for each combination, compute the output[]
            List<Long> outputs = this.compute(rule, comb, containsVar);
            // check if newly-generated outputs have already existed in storage
            if (this.exprStorage.get(nonTerminal).containsKey(outputs)) {
                // if exist, return
                return;
            }

            if (this.coveredExample.size() != this.output.length) {
                // check if the output[] are expected
                // if so, assign those possible expressions to this.definition
                if (this.verifyOutput(outputs, newExpr)) {
                    this.definition = newExpr;
                    return;
                }
            }

            // add output[]&expressions to storages
            currNew.add(outputs);
            this.exprStorage.get(nonTerminal).put(outputs, newExpr);

            // evaluate the examples on ite conditions
            if (containsVar) {
                String[] iteRule = {"iteEval", "Start"};
                List<List<Long>> outputs2 = new LinkedList<List<Long>>();
                outputs2.add(outputs);
                List<Long> iteoutputs = this.compute(iteRule, outputs2, true);
                this.secondVerifyOutput(iteoutputs, newExpr);
            }

            return;
        }

        Set<List<Long>> inputs = this.outputStorage.get(rule[index + 1]).get(prmt[index]);
        for (List<Long> input : inputs) {
            comb.add(input);
            subexprs.add(this.exprStorage.get(rule[index + 1]).get(input));
            this.genOutputCombsHelper(prmt, rule, index + 1, comb, subexprs, currNew, nonTerminal);
            comb.remove(comb.size() - 1);
            subexprs.remove(subexprs.size() - 1);
        }
    }

    boolean containsVar(Expr expr, Set<String> vars) {
        if (expr.isConst()) {
            return vars.contains(expr.toString());
        }
        if (expr.isNumeral()) {
            return false;
        }
        if (expr.isApp()) {
            Expr[] args = expr.getArgs();
            for (int i = 0; i < args.length; i++) {
                if (containsVar(args[i], vars)) {
                    return true;
                }
            }
        }
        return false;
    }

    long[][] transpose(List<List<Long>> comb) {
        int n = comb.size();
        int m = comb.get(0).size();
        long[][] args = new long[n][m];
        for (int i = 0; i < comb.size(); i++) {
            for (int j = 0; j < comb.get(i).size(); j++) {
                args[i][j] = comb.get(i).get(j);
            }
        }
        long[][] trans = new long[m][n];
        for (int i = 0; i < trans.length; i++) {
            for (int j = 0; j < trans[0].length; j++) {
                trans[i][j] = args[j][i];
            }
        }
        return trans;
    }

    List<Long> compute(String[] rule, List<List<Long>> comb, boolean containsVar) {
        if (comb.size() != rule.length - 1) {
            logger.severe("Outputs Length mismatch!");
        }

        long[][] args = this.transpose(comb);

        if (!containsVar) {
            return Collections.nCopies(
                args.length, 
                this.problem.opDis.pbeDispatch(rule[0], args[0], false, this.bvSize)
            );
        }

        List<Long> outputs = new ArrayList<Long>();
        for (int i = 0; i < args.length; i++) {
            outputs.add(this.problem.opDis.pbeDispatch(rule[0], args[i], false, this.bvSize));
        }

        return outputs;
    }

    void secondVerifyOutput(List<Long> outputs, Expr expr){
        if (outputs == null) {
            return;
        }
        
        List<Integer> trueOutputs = new LinkedList<Integer>();
        List<Integer> falseOutputs = new LinkedList<Integer>();
        for (int i: outsider) {
            if (outputs.get(i) == 1) {
                trueOutputs.add(i);
            }
            else {
                falseOutputs.add(i);
            }
        }
        if (trueOutputs.size() == 0 || falseOutputs.size() ==0) {
            // System.out.println("ALL TRUE or ALL FALSE");
            return;
        }
        for (int key: covered.keySet()) {
            if (covered.get(key).containsAll(trueOutputs)) {
                equivClass.add(trueOutputs);
                outsider.removeAll(trueOutputs);
                if (outsider.size() == 0) {
                    elseExpr = key;
                }
                if (!conditionExpr.contains(expr)) {
                    conditionExpr.add(expr);
                    conditionToclass.put(conditionExpr.indexOf(expr), equivClass.indexOf(trueOutputs));
                }
                classToExpr.put(equivClass.indexOf(trueOutputs), key);
                classTF.put(equivClass.indexOf(trueOutputs), true);
            }
            if (covered.get(key).containsAll(falseOutputs)) {
                equivClass.add(falseOutputs);
                outsider.removeAll(falseOutputs);
                if (outsider.size() == 0) {
                    elseExpr = key;
                }
                if (!conditionExpr.contains(expr)) {
                    conditionExpr.add(expr);
                    conditionToclass.put(conditionExpr.indexOf(expr), equivClass.indexOf(falseOutputs));
                }
                classToExpr.put(equivClass.indexOf(falseOutputs), key);
                classTF.put(equivClass.indexOf(falseOutputs), false);
            }
        }
    }

    boolean verifyOutput(List<Long> outputs, Expr expr) {
        // long start = System.nanoTime();
        if (outputs.size() != this.output.length) {
            logger.severe("Outputs Length mismatch!");
        }

        boolean result = true;
        List<Integer> coveredOutputs = new LinkedList<Integer>();
        for (int i = 0; i < outputs.size(); i++) {
            if (!this.output[i].equals(Long.toUnsignedString(outputs.get(i)))) {
                result = false;
            } else {
                coveredOutputs.add(i);
                if (!coveredExample.contains(i)) {
                    coveredExample.add(i);
                }
            }
        }

        if (coveredOutputs.size() != 0){
            List<List<Integer>> removedTargets = new LinkedList<List<Integer>>();
            for (List<Integer> target: covered.values()) {
                if (target.containsAll(coveredOutputs)) {
                    return result;
                }
                if (coveredOutputs.containsAll(target)) {
                    System.out.println(target);
                    System.out.println(coveredOutputs);
                    removedTargets.add(target);
                }
            }
            for (List<Integer> target: removedTargets) {
                covered.values().remove(target);
            }
            coveredExpr.add(expr);
            covered.put(coveredExpr.indexOf(expr),coveredOutputs);
        }
        // long usedTime = System.nanoTime() - start;
        // System.out.println("Time verifyOutput: " + usedTime);

        return result;
    }

    void generate() {
        // init storage, compute initial output and put to storage
        init();

        // check those initial outputs
        Map<List<Long>, Expr> initStorage = this.exprStorage.get(this.start);
        for (List<Long> out : initStorage.keySet()) {
            if (this.verifyOutput(out, initStorage.get(out))) {
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
                Set<List<Long>> newOutputs = new HashSet<List<Long>>();

                // for every operator
                for (String[] rule : this.recRules.get(nonTerminal)) {
                    if (rule[0].equals("im")) {
                        continue;
                    }
                    System.out.println("rule: " + Arrays.toString(rule));
                    // generate all possible sums that add up to the given size
                    List<Integer[]> prmts = genPrmt(iter - 1, rule.length - 1, rule);
                    for (Integer[] prmt : prmts) {
                        System.out.println("prmt: " + Arrays.toString(prmt));
                        genOutputCombs(prmt, rule, newOutputs, nonTerminal);
                        if (this.definition != null) {
                            return;
                        }
                        if (this.coveredExample.size() == this.output.length) {
                            if (!coverFound) {
                                printCovered(this.covered);
                                coverFound = true;
                            }
                            if (this.outsider.size() == 0) {
                                printCovered(this.covered);
                                printEquivClass(this.equivClass);
                                return;
                            }
                        }
                    }
                }
                logger.info("Store: " + nonTerminal + " size: " + iter + " #exprs: " + newOutputs.size());
                this.outputStorage.get(nonTerminal).put(iter, newOutputs);
            }
        }

    }

    void generateResult(){
        Expr result = coveredExpr.get(elseExpr);
        for (int i = conditionExpr.size() - 1; i >= 0; i--) {
            Expr[] operands = new Expr[3];
            operands[0] = conditionExpr.get(i);
            int equivClassIndex = conditionToclass.get(i);
            if (classTF.get(equivClassIndex)) {
                operands[1] = coveredExpr.get(classToExpr.get(equivClassIndex));
                operands[2] = result;
                result = this.problem.opDis.dispatch(this.problem.iteName, operands, true, true);
            }
            else {
                operands[1] = result;
                operands[2] = coveredExpr.get(classToExpr.get(equivClassIndex));
                result = this.problem.opDis.dispatch(this.problem.iteName, operands, true, true);
            }
        }
        this.definition = result;
        // System.out.println("this.definition: " + this.definition.toString());
        // todo: problem with some examples,PRE_18_10, PRE_58_10, PRE_70_10,PRE_74_10
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

    void printExprStorage(Map<String, Map<List<Long>, Expr>> exprStorage) {
        System.out.println("Print storage:");
        for (String nonTerminal : exprStorage.keySet()) {
            System.out.println(nonTerminal);
            Map<List<Long>, Expr> map = exprStorage.get(nonTerminal);
            for (List<Long> subexpr : map.keySet()) {
                System.out.println("outputs: " + Arrays.toString(subexpr.toArray()));
                System.out.println("expr: " + map.get(subexpr).toString());
            }
            System.out.println();
        }
    }

    void printOutputStorage(Map<String, Map<Integer, Set<List<Long>>>> outputStorage) {
        System.out.println("Print storage:");
        for (String nonTerminal : outputStorage.keySet()) {
            System.out.println(nonTerminal);
            Map<Integer, Set<List<Long>>> map = outputStorage.get(nonTerminal);
            for (Integer size : map.keySet()) {
                System.out.println("Size: " + size);
                System.out.println("#expr: " + map.get(size).size());
            }
            System.out.println();
        }
    }

    void printStorageSize(Map<String, Map<List<Long>, Expr>> storage) {
        for (String nonTerminal : storage.keySet()) {
            if (storage.containsKey(nonTerminal)) {
                logger.info(nonTerminal + " in storage: " + storage.get(nonTerminal).size());
            }
        }
    }

    void printCovered(Map<Integer, List<Integer>> covered) {
        for (Integer i : covered.keySet()) {
            System.out.println("Expr " + coveredExpr.get(i) + ": " + covered.get(i).toString());
        }
    }

    void printEquivClass(List<List<Integer>> equivClass) {
        System.out.println("");
        System.out.println(conditionExpr);
        for (int i = 0; i < conditionExpr.size();i++) {
            System.out.println("condition Expr:" + conditionExpr.get(i));
            int equivClassIndex = conditionToclass.get(i);
            System.out.println("Branch: " + classTF.get(equivClassIndex));
            System.out.println("Equivalence Class: " + equivClass.get(equivClassIndex));
            System.out.println("Corresponding covered expr: " + coveredExpr.get(classToExpr.get(equivClassIndex)));
            System.out.println("");
        }

        System.out.println("else expr: " + coveredExpr.get(elseExpr));
        int elseIndex = 0;
        for (int i: classToExpr.keySet()) {
            if (classToExpr.get(i).equals(elseExpr)) {
                elseIndex = i;
                continue;
            }
        }
        System.out.println("Branch: "+classTF.get(elseIndex));
        System.out.println("Equivalence Class: " + equivClass.get(elseIndex));
    }
}