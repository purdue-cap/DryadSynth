import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.microsoft.z3.*;

public class EUSolverTest {
    static Context ctx = new Context();
    static SygusProblem createProblem (String content) {
		ANTLRInputStream input = new ANTLRInputStream(content);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);
		ParseTree tree;
        tree = parser.start();
		ParseTreeWalker walker = new ParseTreeWalker();
		SygusExtractor extractor = new SygusExtractor(ctx);
        walker.walk(extractor, tree);
        return extractor.createProblem();
    }

    static String CLIA_max2 =
    "(set-logic LIA)" +
    "\n(synth-fun mux_2 ((x Int) (y Int)) Int" +
    "\n)" +
    "\n(declare-var x Int)" +
    "\n(declare-var y Int)" +
    "\n(constraint (>= (mux_2 x y) x))" +
    "\n(constraint (>= (mux_2 x y) y))" +
    "\n(constraint (or (= x (mux_2 x y))" +
    "\n                (= y (mux_2 x y))))" +
    "\n(check-synth)";
    static SygusProblem CLIA_max2_problem = createProblem(CLIA_max2);
    static DefinedFunc CLIA_max2_solution = new DefinedFunc(ctx, "mux_2",
        CLIA_max2_problem.requestArgs.get("mux_2"),
        ctx.mkITE(ctx.mkGe(ctx.mkIntConst("x"), ctx.mkIntConst("y")), ctx.mkIntConst("x"), ctx.mkIntConst("y"))
    );
        
    static String General_qm_max2 =
    "(set-logic LIA)" +
    "\n(define-fun qm ((a Int) (b Int)) Int" +
    "\n    (ite (< a 0) b a))" +
    "\n(synth-fun qm-foo ((x Int)  (y Int)      ) Int" +
    "\n    ((Start Int (x" +
    "\n                y" +
    "\n                0" +
    "\n                1" +
    "\n                (- Start Start)" +
    "\n                (+ Start Start)" +
    "\n                (qm Start Start)))))" +
    "\n(declare-var x Int)" +
    "\n(declare-var y Int)" +
    "\n(constraint (= (qm-foo x y) " +
    "\n                (ite (<= x y) y x))) " +
    "\n(check-synth)";
    static SygusProblem General_qm_max2_problem = createProblem(General_qm_max2);
    static DefinedFunc General_qm_max2_solution = new DefinedFunc(ctx, "qm-foo",
        General_qm_max2_problem.requestArgs.get("qm-foo"),
        ctx.mkAdd(ctx.mkIntConst("x"),
                  (ArithExpr)General_qm_max2_problem.funcs.get("qm").applyUninterp(
                      ctx.mkSub(
                        (ArithExpr)General_qm_max2_problem.funcs.get("qm").applyUninterp(
                            ctx.mkIntConst("y"), ctx.mkAdd(ctx.mkInt(0), ctx.mkIntConst("y"))
                        )
                          , ctx.mkIntConst("x")), ctx.mkInt(0)
                  ))
    );


    static String INV_dec = 
    "(set-logic LIA)" +
    "\n(synth-inv InvF ((x Int)))" +
    "\n(declare-primed-var x Int)" +
    "\n(define-fun PreF ((x Int)) Bool" +
    "\n(= x 100))" +
    "\n(define-fun TransF ((x Int) (x! Int)) Bool" +
    "\n(and (> x 0) (= x! (- x 1))))" +
    "\n(define-fun PostF ((x Int)) Bool" +
    "\n(not (and (<= x 0) (not (= x 0)))))" +
    "\n(inv-constraint InvF PreF TransF PostF)" +
    "\n(check-synth)";
    static SygusProblem INV_dec_problem = createProblem(INV_dec);
    static DefinedFunc INV_dec_solution = new DefinedFunc(ctx, "InvF",
        INV_dec_problem.requestArgs.get("InvF"),
        ctx.mkOr(ctx.mkAnd(
            ctx.mkGe(ctx.mkIntConst("x"), ctx.mkInt(0)),
            ctx.mkLe(ctx.mkIntConst("x"), ctx.mkInt(99))
        ), ctx.mkAnd(
            ctx.mkGe(ctx.mkIntConst("x"), ctx.mkInt(1)),
            ctx.mkLe(ctx.mkIntConst("x"), ctx.mkInt(100))
        ))
    );

    public static void main(String[] args) throws Exception {
        EUSolver solver = new EUSolver("external/eusolver.sh");
        if (!solver.solve(CLIA_max2_problem)) {
            throw new Exception();
        }
        System.out.println(solver.results[0].toString());
        if (!solver.solve(General_qm_max2_problem)) {
            throw new Exception();
        }
        System.out.println(solver.results[0].toString());
        if (!solver.solve(INV_dec_problem)) {
            throw new Exception();
        }
        System.out.println(solver.results[0].toString());
    }
}