import java.util.*;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;

import com.microsoft.z3.enumerations.Z3_ast_print_mode;

import com.microsoft.z3.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


final class PBEProblem extends SygusProblem {
    public String synth_fun_name;
    public String start_nonterminal;
    public Map<String, NonTerminal> nonterminals;
    public Expr[][] input;
    public String[] output;

    public record RecRuleOp(String name, String[] nonterminals) {}
    public record RecRuleConst(String name){}
    public record RecRuleVariable(String name) {}
    public record NonTerminal(String name, RecRuleOp[] ops, RecRuleConst[] constants, RecRuleVariable[] variables, RecRuleOp[] branch_combinators) {}

    NonTerminal start_nonterminal() {
        return this.nonterminals.get(this.start_nonterminal);
    }

    public RecRuleOp branchCombinator() {
        return this.start_nonterminal().branch_combinators[0];
    }

    public PBEProblem(SygusProblem problem, Logger logger) throws InvalidAlgorithmParameterException {
        super(problem);
        if( problem.names.size() > 1) { throw new InvalidAlgorithmParameterException("`PBEEnum` does not currently support multiple `synth-fun` yet."); }

        this.synth_fun_name = problem.names.get(0);

        final var cfg = problem.cfgs.get(this.synth_fun_name);
        this.start_nonterminal = (new ArrayList<String>(cfg.grammarSybSort.keySet())).get(0);
        
        this.output = ioexamples.stream().map((List<Expr> x) -> x.get(x.size() - 1).toString()).toArray(String[]::new);
        this.input = ioexamples.stream().map((List<Expr> x) -> x.subList(0, x.size() - 1).toArray(Expr[]::new)).toArray(Expr[][]::new);
        assert(output.length == input.length);
        // TODO: Better Type check.
        if( ((BitVecExpr)this.input[0][0]).getSortSize() != 64) { throw new InvalidAlgorithmParameterException("`PBEEnum` dose not currently support BitVec size other than 64"); }

        this.nonterminals = cfg.grammarRules.entrySet().stream().map(x -> {
            final var recrules = x.getValue();
            var ops = recrules.stream()
                .filter(y -> y.length > 1)
                .map(y -> new RecRuleOp(y[0], Arrays.copyOfRange(y, 1, y.length)))
                .toArray(RecRuleOp[]::new);

            final var constants = recrules.stream()
                .filter(y -> y.length == 1 && y[0].charAt(0) == '#')
                .map(y -> new RecRuleConst(y[0]))
                .toArray(RecRuleConst[]::new);

            final var vars = recrules.stream()
                .filter(y -> y.length == 1 && y[0].charAt(0) != '#')
                .map(y -> new RecRuleVariable(y[0]))
                .toArray(RecRuleVariable[]::new);

            final var bcs = Arrays.stream(ops)
                .map(y -> {
                    if(problem.funcs.containsKey(y.name)) {
                        DefinedFunc func = problem.funcs.get(y.name);
                        if(func.definition.isITE() && func.args.length == 3) {
                            final var leftBranch = func.definition.getArgs()[1];
                            final var rightBranch = func.definition.getArgs()[2];
                            if(leftBranch.toString().equals(func.args[1].toString()) && rightBranch.toString().equals(func.args[2].toString())) {
                                return Optional.of(y);
                            } else {
                                return Optional.empty();
                            }
                        }
                    } else if(y.name.equals("ite") && y.nonterminals.length == 3) {
                        return Optional.of(y);
                    }
                    return Optional.empty();
                })
                .flatMap(Optional::stream)
                .toArray(RecRuleOp[]::new);
            ops = Arrays.stream(ops).filter(y -> !Arrays.asList(bcs).contains(y)).toArray(RecRuleOp[]::new);

            return new NonTerminal(x.getKey(), ops, constants, vars, bcs);
        }).collect(Collectors.toMap(x -> x.name, x -> x));


        if( this.nonterminals.get(this.start_nonterminal).branch_combinators.length != 1 ) { logger.warning("No ite-like rules found, which may slow down the enumerate search.");}
        final var l = this.branchCombinator().nonterminals.length;
        if( this.branchCombinator().nonterminals[l - 2] != this.start_nonterminal ) { logger.warning("No ite-like rules found, which may slow down the enumerate search.");}
        if( this.branchCombinator().nonterminals[l - 1] != this.start_nonterminal ) { logger.warning("No ite-like rules found, which may slow down the enumerate search.");}
    }

}


public class PBEEnumSize extends Thread {

    private Context ctx;
    private PBEProblem problem;
    private Logger logger;
    private int seed;

    // for now, we assume there is only one function to synthesize
    // TODO: support multiple functions
    // TODO: support BV constant
    private Map<String, Map<List<Long>, Expr>> exprStorage = new LinkedHashMap<String, Map<List<Long>, Expr>>();  // nonTerminal -> <output[] -> expr>
    private Map<String, Map<Integer, Set<List<Long>>>> outputStorage = new LinkedHashMap<String, Map<Integer, Set<List<Long>>>>();  // nonTerminal -> <size -> Set<output[]>>

    private int bvSize;     // size of bitvec expressions in benchmark
    private Set<String> varNames = new HashSet<String>(); // variable names appear in grammar
    private Expr definition;  // the definition of resulting function

    public DefinedFunc[] results;

    private String[] symmetricOp = {"bvand", "bvor", "bvadd", "bvxor"};
    private String[] noopOn0Op = {"bvand", "bvor", "bvadd", "bvxor"};
    private List<Long> zeros;

    private Map<Integer, List<Integer>> covered = new HashMap<Integer, List<Integer>>();
    private List<Expr> coveredExpr = new LinkedList<Expr>();
    private List<Integer> coveredExample = new LinkedList<Integer>();
    private List<Integer> outsider = new LinkedList<Integer>(); // ioexamples in uncovered equivalence classes
    private Map<Integer, List<Integer>> uncoveredEquivClasses = new HashMap<Integer, List<Integer>>(); // equivalence classes that can not be fully covered by a single expression
    private int ecCounter = 0; // index of an EC, should increment every time creating a new EC
    private List<List<Integer>> coveredEquivClasses = new LinkedList<List<Integer>>(); // equivalence classes that can be covered by a expression
    private List<Expr> conditions = new LinkedList<Expr>(); // conditional expressions, each expression is stored twice, even index indicates true branch, odd index indicates false branch
    private Map<Integer, List<Integer>> uncoveredECConds = new LinkedHashMap<Integer, List<Integer>>();
    private Map<Integer, List<Integer>> coveredECConds = new LinkedHashMap<Integer, List<Integer>>();
    private Map<List<Integer>, Expr> ecConds2Expr = new LinkedHashMap<List<Integer>, Expr>(); // Map ec conditions to the expression that covers the ec

    private boolean coverFound = false;
    // private int numComp = 0;
    // private int numDump = 0;
    // private int numKeep = 0;
    // private int numConstant = 0;
    // private long startTime;

    private int numExamples;
    private int numSample;
    private Expr[][] sampleInput; // [numSample][numInputArgs]
    private String[] sampleOutput;    // [numSample]
    private List<Integer> selected = new ArrayList<Integer>();
    private List<Integer> failed = new ArrayList<Integer>();


    public PBEEnumSize (Context ctx, SygusProblem problem, Logger logger, int numCore, int seed) throws InvalidAlgorithmParameterException {
        this.ctx = ctx;
        this.problem = new PBEProblem(problem, logger);
        this.logger = logger;
        this.results = new DefinedFunc[problem.names.size()];
        this.seed = seed;
        logger.info("Running PBEEnumSize with random seed: " + seed);
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
            init();

            int numTries = 1;
            while (this.definition == null || !this.failed.isEmpty()) {
                clearStorage();

                logger.info("Start numTries: " + numTries);

                generate();
                if (this.definition == null) {
                    preprocessResult();
                    // simplifyResult();
                    generateResult();
                }
                // System.out.println("#Tries: " + numTries);
                // System.out.println("Tried: " + this.definition.toString()); 
                logger.info("Solution for this try: " + this.definition.toString());

                getFailed(this.definition);
                logger.info("Finished numTries: " + numTries + " with selected: " + this.selected.toString());
                // System.out.println("this.failed: " + Arrays.toString(this.failed.toArray()));
                updateSelected(this.selected, this.failed, this.conditions);
                // System.out.println("this.selected: " + Arrays.toString(this.selected.toArray()));
                numTries++;
            }
            String name = problem.names.get(0);
            Expr result = this.definition;
            this.results[0] = new DefinedFunc(ctx, name, problem.requestArgs.get(name), result);
            // System.out.println("this.results[0]: " + this.results[0].toString());
            // Expr inlined = result;
            // for (String funcname : this.problem.funcs.keySet()) {
            //     FuncDecl f = this.problem.funcs.get(funcname).getDecl();
            //     DefinedFunc df = this.problem.funcs.get(funcname);
            //     inlined = df.rewrite(inlined, f);
            // }
            // Map<String, Expr> resultfunc = new LinkedHashMap<String, Expr>();
            // resultfunc.put(this.results[0].getName(), inlined);
            // Verifier verifier = new Verifier(ctx);
            // Status v = verifier.verify(resultfunc, this.problem);
            // System.out.println("Final check status: " + v);
            // System.out.println("this.numSample: " + this.numSample);
            // System.out.println("numTries: " + (numTries - 1));
        }
    }

    void getFailed(Expr expr) {
        Expr interpreted = expr;
        for (String funcname: this.problem.funcs.keySet()) {
            DefinedFunc df = this.problem.funcs.get(funcname);
            interpreted = df.rewrite(interpreted, df.getDecl());
        }

        this.failed.clear();
        Expr[] funcArgs = this.problem.requestArgs.get(this.problem.names.get(0));
        DefinedFunc df = new DefinedFunc(ctx, funcArgs, interpreted);
        for (int i = 0; i < this.problem.input.length; i++) {
            long o = OpDispatcher.exprToBitVector(df.apply(this.problem.input[i]).simplify());
            if (!this.problem.output[i].equals(Long.toUnsignedString(o))) {
                this.failed.add(i);
            }
        }
    }

    void updateSelected(List<Integer> selected, List<Integer> failed, List<Expr> conditions) {
        if (failed.isEmpty()) {
            // Problem solved
            return;
        }

        if (conditions.isEmpty() || failed.size() == 1) {
            // add a random failed example to selected
            selected.add(failed.get(0));
            return;
        }

        // Get all the conditions in previous solution
        List<Expr> prevConds = new ArrayList<Expr>();
        for (int i = 0; i < conditions.size(); i = i + 2) {
            prevConds.add(conditions.get(i));
        }

        // System.out.println("conditions: " + Arrays.toString(conditions.toArray()));
        // System.out.println("Cond: " + Arrays.toString(prevConds.toArray()));

        // Make failed examples in the right format
        Expr[][] failedInput = new Expr[this.failed.size()][this.problem.ioexamples.get(0).size() - 1];
        for (int i = 0; i < this.failed.size(); i++) {
            failedInput[i] = this.problem.input[this.failed.get(i)];
        }

        Expr[] funcArgs = this.problem.requestArgs.get(this.problem.names.get(0));
        // Evaluate failed examples on ite conditions
        List<List<Long>> iteOutput = new ArrayList<List<Long>>();
        for (int i = 0; i < prevConds.size(); i++) {
            // TODO: make another interpreted copy during enumeration
            Expr interpreted = prevConds.get(i);
            for (String funcname: this.problem.funcs.keySet()) {
                DefinedFunc df = this.problem.funcs.get(funcname);
                interpreted = df.rewrite(interpreted, df.getDecl());
            }
            DefinedFunc df = new DefinedFunc(ctx, funcArgs, interpreted);
            List<Long> outputs = new ArrayList<Long>();
            for (int j = 0; j < failedInput.length; j++) {
                // System.out.println(Arrays.toString(this.sampleInput[i]));
                outputs.add(OpDispatcher.exprToBitVector(df.apply(failedInput[j]).simplify()));
            }
            // System.out.println("outputs: " + Arrays.toString(outputs.toArray()));
            final var bc = this.problem.branchCombinator();
            List<List<Long>> outputs2 = new LinkedList<List<Long>>();
            outputs2.add(outputs);
            outputs2.add(outputs.stream().map(x -> 1L).collect(Collectors.toList()));
            outputs2.add(outputs.stream().map(x -> 0L).collect(Collectors.toList()));
            List<Long> iteoutputs = this.compute(bc, outputs2, true, false);
            // System.out.println("iteoutputs: " + Arrays.toString(iteoutputs.toArray()));
            iteOutput.add(iteoutputs);
        }

        // for (int i = 0; i < prevConds.size(); i++) {
        //     System.out.println("Cond " + i + " output: " + Arrays.toString(iteOutput.get(i).toArray()));
        // }

        List<List<Long>> exsOnITE = new ArrayList<List<Long>>();
        for (int i = 0; i < failedInput.length; i++) {
            List<Long> exOnITEs = new ArrayList<Long>();
            for (int j = 0; j < prevConds.size(); j++) {
                exOnITEs.add(iteOutput.get(j).get(i));
            }
            exsOnITE.add(exOnITEs);
        }

        Map<List<Long>, Integer> exOnBranch = new HashMap<List<Long>, Integer>();
        for (int i = 0; i < failedInput.length; i++) {
            exOnBranch.putIfAbsent(exsOnITE.get(i), this.failed.get(i));
        }

        this.selected.addAll(exOnBranch.values());
    }

    void init() {
        // find out starting non-terminal
        String funcName = problem.names.get(0);
        SygusProblem.CFG cfg = problem.cfgs.get(funcName);

        // extract inputs and outputs
        List<List<Expr>> ioexamples = this.problem.ioexamples;
        this.numExamples = ioexamples.size();
        this.bvSize = ((BitVecExpr)this.problem.input[0][0]).getSortSize();
        this.numSample = 1;
        Random rand = new Random();
        rand.setSeed(seed);
        this.selected.add(rand.nextInt(this.numExamples));
    }

    void clearStorage() {
        this.exprStorage.clear();
        this.outputStorage.clear();
        this.covered.clear();
        this.coveredExpr.clear();
        this.coveredExample.clear();
        this.outsider.clear();
        this.uncoveredEquivClasses.clear();
        this.coveredEquivClasses.clear();
        this.conditions.clear();
        this.uncoveredECConds.clear();
        this.coveredECConds.clear();
        this.ecConds2Expr.clear();
        this.definition = null;
        this.coverFound = false;
    }

    void initStorage() {
        // // find out starting non-terminal
        String funcName = problem.names.get(0);
        SygusProblem.CFG cfg = problem.cfgs.get(funcName);
        Long[] zeroArray = new Long[this.numSample];
        long zero = 0;
        Arrays.fill(zeroArray, zero);
        this.zeros = Arrays.asList(zeroArray);
        Long[] oneArray = new Long[this.numSample];
        Arrays.fill(oneArray, 1L);

        this.numSample = this.selected.size();
        this.sampleInput = new Expr[this.numSample][this.problem.ioexamples.get(0).size() - 1];
        this.sampleOutput = new String[this.numSample];
        for (int i = 0; i < this.selected.size(); i++) {
            this.sampleInput[i] = this.problem.input[this.selected.get(i)];
            this.sampleOutput[i] = this.problem.output[this.selected.get(i)];
            this.outsider.add(i);
        }
        this.uncoveredEquivClasses.put(this.ecCounter, new LinkedList<Integer>(this.outsider));
        this.uncoveredECConds.put(this.ecCounter, new LinkedList<Integer>());
        this.ecCounter++;

        // initialize storage and recRules
        for (String symbol : cfg.grammarRules.keySet()) {
            List<String[]> recs = new ArrayList<String[]>();
            Map<List<Long>, Expr> exprStrg = new HashMap<List<Long>, Expr>();
            Map<Integer, Set<List<Long>>> outputStrg = new HashMap<Integer, Set<List<Long>>>();
            Set<List<Long>> outputSet = new HashSet<List<Long>>();
            outputStrg.put(1, outputSet);

            List<String[]> rules = cfg.grammarRules.get(symbol);

            // boolean removeRule = false;
            // String[] toRm = null;
            // for (String[] rule : rules) {
            //     if (rule.length == 2 && rule[0].equals("bvnot")) {            //         removeRule = true;
            //         toRm = rule;
            //     }
            // }
            // if (removeRule) {
            //     rules.remove(toRm);
            //     Expr terminal = ctx.mkBVNot(ctx.mkBV(0, this.bvSize)).simplify();
            //     Expr[] funcArgs = this.problem.requestArgs.get(this.problem.names.get(0));
            //     DefinedFunc df = new DefinedFunc(ctx, funcArgs, terminal);
            //     List<Long> outputs = new ArrayList<Long>();
            //     for (int i = 0; i < this.sampleInput.length; i++) {
            //         outputs.add(OpDispatcher.exprToBitVector(df.apply(this.sampleInput[i])));
            //     }
            //     logger.info("Initial outputs: " + Arrays.toString(outputs.toArray()));
            //     if (!exprStrg.containsKey(outputs)) {
            //         exprStrg.put(outputs, terminal);
            //         outputStrg.get(1).add(outputs);
            //     }
            // }

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
                    for (int i = 0; i < this.sampleInput.length; i++) {
                        // System.out.println(Arrays.toString(this.sampleInput[i]));
                        outputs.add(OpDispatcher.exprToBitVector(df.apply(this.sampleInput[i])));
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

    List<Integer[]> genPrmt(int budget, int length, PBEProblem.RecRuleOp rule) {
        List<Integer[]> prmt = new ArrayList<Integer[]>();
        Integer[] working = new Integer[length];
        genPrmtHelper(budget, length, working, 0, prmt, rule);
        return prmt;
    }

    void genPrmtHelper(int budget, int length, Integer[] working, int index, List<Integer[]> prmt, PBEProblem.RecRuleOp rule) {
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

        if (Arrays.asList(this.symmetricOp).contains(rule.name()) && index >= 1 && budget < working[index - 1]) {
            return;
        }

        for (int i = 1; i <= budget; i++) {
            working[index] = i;
            genPrmtHelper(budget - i, length, working, index + 1, prmt, rule);
        }
    }

    void genOutputCombs(Integer[] prmt, PBEProblem.RecRuleOp rule, Set<List<Long>> currNew, String nonTerminal) {
        if (prmt.length != rule.nonterminals().length) {
            logger.severe("prmt and rule: Length mismatch!");
        }

        List<List<Long>> comb = new ArrayList<List<Long>>();
        List<Expr> subexprs = new ArrayList<Expr>();
        Set<List<Long>> avoidSymm = null;

        if (Arrays.asList(this.symmetricOp).contains(rule.name())) {
            avoidSymm = new HashSet<List<Long>>();
        }

        this.genOutputCombsHelper(prmt, rule, 0, comb, subexprs, currNew, nonTerminal, avoidSymm);
    }

    void genOutputCombsHelper(Integer[] prmt, PBEProblem.RecRuleOp rule, int index, List<List<Long>> comb, List<Expr> subexprs, Set<List<Long>> currNew, String nonTerminal, Set<List<Long>> avoidSymm) {
        if (this.outsider.isEmpty()) {
            return;
        }

        if (index == prmt.length) {
            // generate new expression
            Expr[] operands = subexprs.toArray(new Expr[subexprs.size()]);
            Expr newExpr = this.problem.opDis.dispatch(rule.name(), operands, true, true);

            boolean containsVar = false;
            if (containsVar(newExpr, this.varNames)) {
                containsVar = true;
            }

            // if (!containsVar) {
            //     this.numConstant += 1;
            // }
            
            // for each combination, compute the output[]
            List<Long> outputs = null;
            try {
                outputs = this.compute(rule, comb, containsVar, true);
            } catch(java.lang.ArithmeticException e) {
                return;
            }

            // this.numComp += 1;
            // if (this.numComp % 100 == 0) {
            //     long usedTime = System.nanoTime() - this.startTime;
            //     System.out.println("100 Computations take: " + usedTime);
            //     this.startTime = System.nanoTime();
            // }

            // check if newly-generated outputs have already existed in storage
            if (this.exprStorage.get(nonTerminal).containsKey(outputs)) {
                // // if exist, return
                // this.numDump += 1;
                // if (!containsVar) {
                //     this.numConstant += 1;
                // }
                // System.out.println("Expr: " + newExpr.toString());
                // // System.out.println("outputs: " + Arrays.toString(outputs.toArray()));
                // System.out.println("Corresponding expr: " + this.exprStorage.get(nonTerminal).get(outputs).toString());
                return;
            }
            // this.numKeep += 1;

            if (this.coveredExample.size() != this.sampleOutput.length && nonTerminal.equals(this.problem.start_nonterminal)) {
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
            final var bc = this.problem.branchCombinator();
            if (containsVar && bc.nonterminals()[0].equals(nonTerminal)) {
                List<List<Long>> outputs2 = new LinkedList<List<Long>>();
                outputs2.add(outputs);
                outputs2.add(outputs.stream().map(x -> 1L).collect(Collectors.toList()));
                outputs2.add(outputs.stream().map(x -> 0L).collect(Collectors.toList()));
                List<Long> iteoutputs = this.compute(bc, outputs2, true, true);
                assert(outputs.size() == iteoutputs.size());
                this.updateECs(iteoutputs, newExpr);
            }

            return;
        }

        Set<List<Long>> inputs = this.outputStorage.get(rule.nonterminals()[index]).get(prmt[index]);
        boolean symm = Arrays.asList(this.symmetricOp).contains(rule.name());

        for (List<Long> input : inputs) {
            boolean skip = false;

            if (Arrays.asList(this.noopOn0Op).contains(rule.name()) && input.equals(this.zeros)) {
                // System.out.println("all zeros");
                // continue;
                skip = true;
            }

            if (avoidSymm != null && symm && avoidSymm.contains(input)) {
                // continue;
                skip = true;
            }

            if (rule.name().equals("bvnot") && this.exprStorage.get(rule.nonterminals()[index]).get(input).isBVNOT()) {
                // skip (bvnot (bvnot x))
                skip = true;
            }

            if (!skip) {
                comb.add(input);
                subexprs.add(this.exprStorage.get(rule.nonterminals()[index]).get(input));
                this.genOutputCombsHelper(prmt, rule, index + 1, comb, subexprs, currNew, nonTerminal, avoidSymm);
                comb.remove(comb.size() - 1);
                subexprs.remove(subexprs.size() - 1);
            }

            if (symm && index == 0) {
                avoidSymm.add(input);
            }
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

    Long[][] transpose(List<List<Long>> comb) {
        int n = comb.size();
        int m = comb.get(0).size();
        Long[][] args = new Long[n][m];
        for (int i = 0; i < comb.size(); i++) {
            for (int j = 0; j < comb.get(i).size(); j++) {
                args[i][j] = comb.get(i).get(j);
            }
        }
        Long[][] trans = new Long[m][n];
        for (int i = 0; i < trans.length; i++) {
            for (int j = 0; j < trans[0].length; j++) {
                trans[i][j] = args[j][i];
            }
        }
        return trans;
    }

    List<Long> compute(PBEProblem.RecRuleOp rule, List<List<Long>> comb, boolean containsVar, boolean enablePartialCompute) {
        if (comb.size() != rule.nonterminals().length) {
            logger.severe("Outputs Length mismatch!");
        }

        Long[][] args = this.transpose(comb);

        if (!containsVar) {
            for (int i = 0; i < args.length; i++) {
                Long ret = this.problem.opDis.pbeDispatch(rule.name(), args[i], false, this.bvSize);
                if (ret != null) {
                    return Collections.nCopies(args.length, ret);
                }
            }
            return null;
        }

        List<Long> outputs = new ArrayList<Long>();
        for (int i = 0; i < args.length; i++) {
            if (this.outsider.contains(i) || !this.coveredExample.contains(i) || !enablePartialCompute) {
                outputs.add(this.problem.opDis.pbeDispatch(rule.name(), args[i], false, this.bvSize));
            } else {
                outputs.add(null);
            }
        }

        return outputs;
    }

    void updateECs(List<Long> outputs, Expr cond){
        if (outputs == null) {
            return;
        }
        
        List<Integer> trueOutputs = new LinkedList<Integer>();
        for (int i: this.outsider) {
            if (outputs.get(i) == 1) {
                trueOutputs.add(i);
            }
        }

        if (trueOutputs.size() == 0 || trueOutputs.size() == this.outsider.size()) {
            // System.out.println("ALL TRUE or ALL FALSE");
            return;
        }

        // for every uncovered equivalence class,
        // check if trueOutputs are included in an equivalnce class
        for (List<Integer> uncoveredEC : this.uncoveredEquivClasses.values()) {
            if (uncoveredEC.equals(trueOutputs)) {
                // if the trueOutputs is exactly the same as an equivalence class,
                // which means it is not helping us split examples,
                // it will be no-opt on current equivalence classes
                return;
            }
        }

        // at this point, cond can be used to split ECs,
        // hence store it twice in conditions
        int condTrueIndex = this.conditions.size();
        this.conditions.add(cond);
        this.conditions.add(cond);

        Map<Integer, List<Integer>> uncoveredECs = new HashMap<Integer, List<Integer>>(this.uncoveredEquivClasses);
        for (int ecIndex : uncoveredECs.keySet()) {
            List<Integer> uncoveredEC = uncoveredECs.get(ecIndex);
            List<Integer> newEC = new LinkedList<Integer>(uncoveredEC);
            newEC.retainAll(trueOutputs);
            if (!newEC.isEmpty()) {
                // add/update new ECs to current EC list
                this.uncoveredEquivClasses.get(ecIndex).removeAll(trueOutputs);
                List<Integer> updatedEC = new LinkedList<Integer>(this.uncoveredEquivClasses.get(ecIndex));
                this.uncoveredEquivClasses.put(this.ecCounter, newEC);
                // update the conditions correspond to the EC
                List<Integer> newECConds = new LinkedList<Integer>(this.uncoveredECConds.get(ecIndex));
                newECConds.add(condTrueIndex);
                this.uncoveredECConds.put(this.ecCounter, newECConds);
                this.uncoveredECConds.get(ecIndex).add(condTrueIndex + 1);
                // remove empty EC
                if (updatedEC.isEmpty()) {
                    this.uncoveredEquivClasses.remove(ecIndex);
                    this.uncoveredECConds.remove(ecIndex);
                }
                this.ecCounter++;
            } else {
                this.uncoveredECConds.get(ecIndex).add(condTrueIndex + 1);
            }
        }
        // check if an uncovered EC can be covered by a single expression
        this.updateCoveredECs();
    }

    void updateCoveredECs() {
        // check if an uncovered EC can be covered by a single expression
        // and then update accordingly
        List<Integer> toRm = new ArrayList<Integer>();

        for (int ecIndex : this.uncoveredEquivClasses.keySet()) {
            List<Integer> ec = this.uncoveredEquivClasses.get(ecIndex);
            boolean isCovered = false;
            for (int exprIndex: covered.keySet()) {
                if (!isCovered && covered.get(exprIndex).containsAll(ec)) {
                    isCovered = true;
                    // remove newly-covered EC from outsider
                    this.outsider.removeAll(ec);
                    // store covered EC
                    this.coveredEquivClasses.add(ec);
                    logger.info("Added newly-covered EC: " + Arrays.toString(ec.toArray()));
                    logger.info(exprIndex + " Expr: " + coveredExpr.get(exprIndex).toString());
                    List<Integer> newECConds = new LinkedList<Integer>(this.uncoveredECConds.get(ecIndex));
                    this.coveredECConds.put(this.coveredEquivClasses.size() - 1, newECConds);
                    // remove covered EC from uncovered map
                    toRm.add(ecIndex);
                }
            }
        }

        // Remove covered EC from uncovered map
        for (int index: toRm) {
            this.uncoveredEquivClasses.remove(index);
            this.uncoveredECConds.remove(index);
        }
    }

    boolean verifyOutput(List<Long> outputs, Expr expr) {
        // long start = System.nanoTime();
        if (outputs.size() != this.sampleOutput.length) {
            logger.severe("Outputs Length mismatch!");
        }

        boolean result = true;
        List<Integer> coveredOutputs = new LinkedList<Integer>();
        for (int i = 0; i < outputs.size(); i++) {
            if (outputs.get(i) == null || !this.sampleOutput[i].equals(Long.toUnsignedString(outputs.get(i)))) {
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
                    // System.out.println(target);
                    // System.out.println(coveredOutputs);
                    removedTargets.add(target);
                }
            }
            for (List<Integer> target: removedTargets) {
                covered.values().remove(target);
            }
            coveredExpr.add(expr);
            covered.put(coveredExpr.indexOf(expr),coveredOutputs);
            // printCovered(this.covered);
        }
        // long usedTime = System.nanoTime() - start;
        // System.out.println("Time verifyOutput: " + usedTime);

        return result;
    }

    void generate() {
        // init storage, compute initial output and put to storage
        initStorage();

        // check those initial outputs
        Map<List<Long>, Expr> initStorage = this.exprStorage.get(this.problem.start_nonterminal);
        for (List<Long> out : initStorage.keySet()) {
            if (this.verifyOutput(out, initStorage.get(out))) {
                this.definition = initStorage.get(out);
                return;
            }
        }
        // this.startTime = System.nanoTime();

        int iter = 1;
        while (true) {
            iter++;
            logger.info("Size: " + iter);

            // for each non-terminal
            for (PBEProblem.NonTerminal nonTerminal : this.problem.nonterminals.values()) {
                Set<List<Long>> newOutputs = new HashSet<List<Long>>();

                // for every operator
                for (PBEProblem.RecRuleOp rule : nonTerminal.ops()) {
                    if (rule.name().equals(this.problem.branchCombinator().name())) {
                        continue;
                    }
                    logger.info("rule: " + rule.toString());
                    // generate all possible sums that add up to the given size
                    List<Integer[]> prmts = genPrmt(iter - 1, rule.nonterminals().length, rule);
                    for (Integer[] prmt : prmts) {
                        logger.info("prmt: " + Arrays.toString(prmt));
                        genOutputCombs(prmt, rule, newOutputs, nonTerminal.name());
                        if (this.definition != null) {
                            return;
                        }
                        if (this.coveredExample.size() == this.sampleOutput.length) {
                            if (!this.coverFound) {
                                printCovered(this.covered);
                                this.coverFound = true;
                                // check if an uncovered EC can be covered by a single expression
                                this.updateCoveredECs();
                            }
                            if (this.outsider.size() == 0) {
                                printCovered(this.covered);
                                printCoveredEC();
                                return;
                            }
                        }
                    }
                }
                logger.info("Store: " + nonTerminal.name() + " size: " + iter + " #exprs: " + newOutputs.size());
                this.outputStorage.get(nonTerminal.name()).put(iter, newOutputs);
            }
            // System.out.println("Size: " + iter);
            // System.out.println("numComp: " + this.numComp);
            // System.out.println("numDump: " + this.numDump);
            // System.out.println("numKeep: " + this.numKeep);
            // System.out.println("numConstant: " + this.numConstant);
        }

    }

    void preprocessResult() {
        for (int i = 0; i < this.coveredEquivClasses.size(); i++) {
            List<Integer> coveredEC = this.coveredEquivClasses.get(i);
            List<Integer> conds = this.coveredECConds.get(i);
            for (int exprIndex: covered.keySet()) {
                if (covered.get(exprIndex).containsAll(coveredEC)) {
                    this.ecConds2Expr.put(conds, this.coveredExpr.get(exprIndex));
                }
            }
        }
    }

    void simplifyResult() {
        List<List<Integer>> ecConds = new LinkedList<List<Integer>>();
        Map<Integer, Expr> ecCondsIndex2Expr = new HashMap<Integer, Expr>();
        for (List<Integer> conds: ecConds2Expr.keySet()) {
            ecConds.add(conds);
            ecCondsIndex2Expr.put(ecConds.size() - 1, ecConds2Expr.get(conds));
        }

        Map<List<Integer>, List<Integer>> prefix2Indices = new HashMap<List<Integer>, List<Integer>>();
        List<Integer> startBy0 = new LinkedList<Integer>();
        List<Integer> startBy1 = new LinkedList<Integer>();
        for (int i = 0; i < ecConds.size(); i++) {
            if (ecConds.get(i).contains(0)) {
                startBy0.add(i);
            } else if (ecConds.get(i).contains(1)) {
                startBy1.add(i);
            } else {
                // conds should start by either 0 or 1
                logger.severe("Conds should start by either 0 or 1!");
            }
        }
        prefix2Indices.put(Arrays.asList(0), startBy0);
        prefix2Indices.put(Arrays.asList(1), startBy1);

        // printECCondsList(ecConds);
        // printPrefix2Indices(prefix2Indices);

        while (!prefix2Indices.isEmpty()) {
            List<List<Integer>> prefixes = new LinkedList<List<Integer>>(prefix2Indices.keySet());
            for (List<Integer> prefix: prefixes) {
                List<Integer> indices = prefix2Indices.get(prefix);
                int lookingAt = prefix.size();
                int lookingAtVal = -1;
                boolean unique = true;
                for (int condIdx: indices) {
                    List<Integer> ecCond = ecConds.get(condIdx);
                    if (lookingAt >= ecCond.size()) {
                        continue;
                    }
                    int val = ecCond.get(lookingAt);
                    if (lookingAtVal == -1) {
                        lookingAtVal = val;
                    } else if (lookingAtVal != val) {
                        unique = false;
                    }
                    List<Integer> newPrefix = new LinkedList<Integer>(prefix);
                    newPrefix.add(val);
                    if (prefix2Indices.containsKey(newPrefix)) {
                        prefix2Indices.get(newPrefix).add(condIdx);
                    } else {
                        prefix2Indices.put(newPrefix, new LinkedList<Integer>(Arrays.asList(condIdx)));
                    }
                }
                if (unique && lookingAtVal != -1) {
                    for (int condIdx: indices) {
                        if (lookingAt >= ecConds.get(condIdx).size()) {
                            continue;
                        }
                        ecConds.get(condIdx).remove(lookingAt);
                    }
                    List<Integer> newPrefix = new LinkedList<Integer>(prefix);
                    newPrefix.add(lookingAtVal);
                    prefix2Indices.remove(newPrefix);
                } else {
                    prefix2Indices.remove(prefix);
                }
            }
        }

        this.ecConds2Expr.clear();
        for (int i = 0; i < ecConds.size(); i++) {
            this.ecConds2Expr.put(ecConds.get(i), ecCondsIndex2Expr.get(i));
        }
    }

    void generateResult() {
        // printECCond2Expr();
        // printECConds();

        // conds should not be empty at this point
        Map<List<Integer>, Expr> cond02expr = this.selectConds(0, this.ecConds2Expr);
        Map<List<Integer>, Expr> cond12expr = this.selectConds(1, this.ecConds2Expr);
        Expr[] operands = new Expr[3];
        operands[0] = this.conditions.get(0); // condition
        operands[1] = this.construct(0, cond02expr); // true branch
        operands[2] = this.construct(1, cond12expr); // false branch
        this.definition = this.problem.opDis.dispatch(this.problem.branchCombinator().name(), operands, true, true);

        // System.out.println("");
        // System.out.println("this.definition: " + this.definition.toString());
        // System.out.println("");
        // todo: problem with some examples,PRE_18_10, PRE_58_10, PRE_70_10,PRE_74_10
    }

    Expr construct(int condIndex, Map<List<Integer>, Expr> cond2expr) {
        if (cond2expr.size() == 0) {
            return null;
        } else if (cond2expr.size() == 1) {
            return (Expr)cond2expr.values().toArray()[0];
        } else {
            int listIndex = (condIndex / 2) + 1;
            int trueCondIndex = listIndex * 2;
            int falseCondIndex = listIndex * 2 + 1;
            Map<List<Integer>, Expr> trueCond2expr = this.selectConds(trueCondIndex, cond2expr);
            Map<List<Integer>, Expr> falseCond2expr = this.selectConds(falseCondIndex, cond2expr);

            while (trueCond2expr.isEmpty() && falseCond2expr.isEmpty()) {
                listIndex = listIndex + 1;
                trueCondIndex = listIndex * 2;
                falseCondIndex = listIndex * 2 + 1;
                trueCond2expr = this.selectConds(trueCondIndex, cond2expr);
                falseCond2expr = this.selectConds(falseCondIndex, cond2expr);
            }

            Expr[] operands = new Expr[3];
            operands[0] = this.conditions.get(trueCondIndex); // condition
            operands[1] = this.construct(trueCondIndex, trueCond2expr); // true branch
            operands[2] = this.construct(falseCondIndex, falseCond2expr); // false branch
            Expr result = null;
            if (operands[1] == null && operands[2] == null) {
                logger.severe("Both operands are null!");
            } else if (operands[1] == null) {
                result = operands[2];
            } else if (operands[2] == null) {
                result = operands[1];
            } else {
                if (operands[1].toString().equals(operands[2].toString())) {
                    result = operands[1];
                } else {
                    result = this.problem.opDis.dispatch(this.problem.branchCombinator().name(), operands, true, true);
                }
            }
            return result;
        }
    }

    Map<List<Integer>, Expr> selectConds(int index, Map<List<Integer>, Expr> cond2expr) {
        Map<List<Integer>, Expr> result = new LinkedHashMap<List<Integer>, Expr>();
        for (List<Integer> cond: cond2expr.keySet()) {
            if (cond.contains(index)) {
                result.put(cond, cond2expr.get(cond));
            }
        }
        return result;
    }

    void printRecRules() {
        System.out.println("recRules");
        for (PBEProblem.NonTerminal symbol : problem.nonterminals.values()) {
            System.out.println(symbol);
            for (PBEProblem.RecRuleOp rule : symbol.ops()) {
                System.out.println(rule.name() + " " + Arrays.deepToString(rule.nonterminals()));
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
        logger.info("Covered: ");
        for (Integer i : covered.keySet()) {
            logger.info("Expr " + coveredExpr.get(i) + ": " + covered.get(i).toString());
        }
    }

    void printCoveredEC() {
        logger.info("CoveredEC:");
        for (int i = 0; i < this.coveredEquivClasses.size(); i++) {
            logger.info("Covered EC: " + Arrays.toString(this.coveredEquivClasses.get(i).toArray()));
            List<Integer> conds = this.coveredECConds.get(i);
            logger.info("Conds: " + Arrays.toString(conds.toArray()));
        }
    }

    void printUncoveredEC() {
        System.out.println("");
        System.out.println("UncoveredEC:");
        for (int i: this.uncoveredEquivClasses.keySet()) {
            System.out.println("Uncovered EC: " + Arrays.toString(this.uncoveredEquivClasses.get(i).toArray()));
        }
    }

    void printECCond2Expr() {
        System.out.println("");
        for (List<Integer> ecConds: this.ecConds2Expr.keySet()) {
            System.out.println("EC Conds: " + Arrays.toString(ecConds.toArray()));
            System.out.println("Expr: " + this.ecConds2Expr.get(ecConds).toString());
        }
    }

    void printECConds() {
        System.out.println("");
        for (int i = 0; i < this.conditions.size(); i++) {
            System.out.println("Cond " + i + ": " + this.conditions.get(i).toString());
        }
    }

    void printECCondsList(List<List<Integer>> ecConds) {
        System.out.println("");
        for (int i = 0; i < ecConds.size(); i++) {
            System.out.println("ecConds " + i + ": " + Arrays.toString(ecConds.get(i).toArray()));
        }
    }

    void printPrefix2Indices(Map<List<Integer>, List<Integer>> prefix2Indices) {
        System.out.println("");
        for (List<Integer> prefix: prefix2Indices.keySet()) {
            System.out.println("Prefix: " + Arrays.toString(prefix.toArray()));
            System.out.println("Conds indices: " + Arrays.toString(prefix2Indices.get(prefix).toArray()));
        }
    }
}
