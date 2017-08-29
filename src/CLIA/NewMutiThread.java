import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;
import com.microsoft.z3.*;
import com.microsoft.z3.enumerations.Z3_ast_print_mode;

/**
 * Created by tian on 2017/8/28.
 */
public class NewMutiThread extends Thread{

    private Context ctx;

    public Solver s;

    private SygusExtractor extractor;

    private int begin;

    private int end;

    private List<Expr> DNFExpr;

    private Logger logger;

    private FuncDecl funcDecl;

    public Expr resultExpr;

    public NewMutiThread(SygusExtractor extractor, Logger logger, int begin, int end, List<Expr> DNFExpr){
        this.ctx=new Context();
        this.extractor=extractor.translate(ctx);

        this.DNFExpr=new LinkedList<>();
        for (Expr expr : DNFExpr){
            this.DNFExpr.add(expr.translate(ctx));
        }

        this.ctx.setPrintMode(Z3_ast_print_mode.Z3_PRINT_SMTLIB_FULL);
        this.s = ctx.mkSolver();
        this.logger=logger;
        this.begin=begin;
        this.end=end;
        this.funcDecl=this.extractor.rdcdRequests.get(this.extractor.names.get(0));
    }

    public void run(){
        System.out.println("Thread "+ this.begin + " Started");
        this.resultExpr = this.getFinalRes();
        System.out.println(this.begin+" Finish");
    }

    /*
    * 根据DNF表达式构造最后的ite表达式
    * */
    private Expr getFinalRes(){
        Expr lastExpr=ctx.mkInt(-11);
        boolean ifFirst=true;
        for (Expr expr: DNFExpr){

            Status status=ifSatisfy(expr);

            if (status == Status.SATISFIABLE) {

            Expr []twoExpr=null;
            Expr resExpr=ctx.mkInt(0);

            if ((twoExpr=findFuncEq(expr))!=null){

                resExpr=twoExpr[1];
                expr = expr.substitute(twoExpr[0], resExpr);

            }
            lastExpr = ctx.mkITE((BoolExpr)expr, resExpr, lastExpr);
            }
        }

        return lastExpr;
    }

    /*
    * 判断expr表达式是否有解
    * */
    private Status ifSatisfy(Expr expr){
        s.push();
        s.add((BoolExpr)expr);

        Status status = s.check();
        s.pop();
        return status;
    }

    /*
    * 判断orig里面是否包含函数等式，若包含，则返回该函数以及该函数的值
    * 如：= max2(x y) x
    * 则返回 max2(x y) 和 x
    * */
    private Expr[] findFuncEq(Expr orig){
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(orig);
        while (!todo.empty()) {
            Expr expr = todo.pop();
            if (expr.isConst()||expr.isInt()){
                continue;
            }
            if (expr.isApp()){
                Expr [] args = expr.getArgs();
                if (expr.isEq()){
                    if (args[0].getFuncDecl().equals(this.funcDecl)){
                        return args;
                    }
                    if (args[1].getFuncDecl().equals(this.funcDecl)){
                        Expr temp=args[1];
                        args[1]=args[0];
                        args[0]=temp;
                        return args;
                    }
                }
                for (Expr arg:args){
                    todo.push(arg);
                }
            }
        }
        return null;
    }

}
