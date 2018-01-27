import java.util.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import com.microsoft.z3.*;

/**
 * Created by tian on 2017/8/8.
 * */
public class NewMutiWay {

    private String oldZ3String;

    private Logger logger;

    private List<Expr> DNFExpr;

    private List<Function> calFunc;

    private Context ctx;

    public Solver s;

    private SygusExtractor extractor;

    private Expr z3Expr;

    private boolean ifException;

    // Default expression to fall back to
    // This shall be 0 in the final result, but we need to use a placeholder
    // to avoid other conflicts
    private Expr defaultExpr;

    public Map<String,Expr> results = null;

    private Map<String,Expr[]> callCache = null;

    private int numCore;


    public NewMutiWay(Context ctx, SygusExtractor extractor, Logger logger, int numCore){
        this.ctx=ctx;
        this.extractor=extractor;
        this.s = ctx.mkSolver();
        this.z3Expr=this.extractor.finalConstraint;
        this.oldZ3String=this.extractor.finalConstraint.toString();
        this.logger=logger;
        this.DNFExpr=new LinkedList<Expr>();
        this.calFunc=new LinkedList<>();
        this.ifException=false;
        this.numCore=numCore;
        this.defaultExpr = ctx.mkFreshConst("default", ctx.mkIntSort());
        this.callCache = new LinkedHashMap<String,Expr[]>();

        Map<String, Expr> functions = new LinkedHashMap<String, Expr>();

        if (extractor.names.size()>1){
            if (this.z3Expr.isAnd()){
                Expr [] exprs=this.z3Expr.getArgs();
                for (Expr expr:exprs){
                    this.calculFunc(expr);
                }
            }else {
                this.calculFunc(this.z3Expr);
            }

            String basicFunc=calFunc.get(0).getBasicFuncName();

            for (String name : extractor.names) {
                if (name.equals(basicFunc)){
                    functions.put(name , this.defaultExpr);
                }else {
                    for (Function function:calFunc){
                        if (function.getName().equals(name)){
                            if (ifNeedSwap(name)){
                                int x=function.getxPara();
                                function.setxPara(function.getyPara());
                                function.setyPara(x);
                            }
                            Expr expr=this.makeFunction(function);
                            logger.info(expr.toString());
                            functions.put(name , expr);
                        }
                    }
                }
            }
        }else {
            this.DNFExpr=this.changeToDNF(this.z3Expr);
            //for (Expr expr: this.DNFExpr) {
            //    logger.info(expr.toString());
            //}

            logger.info("*******1*****"+DNFExpr.size());
            int gap=0;
            if (this.DNFExpr.size()>=numCore){
                gap=this.DNFExpr.size()/numCore;
            }else {
                numCore=this.DNFExpr.size();
                gap=1;
            }
            //logger.info("numcore="+numCore);

            NewMutiThread[] threads = new NewMutiThread[numCore];
            int begin=0;
            int end=0;
            for (int i = 0; i < numCore; i++) {
                begin=end;
                end=begin+gap;
                if (i==numCore-1){
                    end=this.DNFExpr.size();
                }
                //logger.info(String.format("%d %d %d %d", this.DNFExpr.size(), gap, begin, end));
                threads[i] = new NewMutiThread(extractor, logger, begin, end, this.DNFExpr.subList(begin,end), this.defaultExpr);
                threads[i].start();
            }

            for (int i = 0;i < numCore;i++)
            {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info("Finish one!!!!!!!!!!!");

            Expr finalExpr=threads[0].resultExpr.translate(ctx);
            for (int i = 1;i < numCore;i++){
                //logger.info(finalExpr.toString());
                finalExpr=finalExpr.substitute(this.defaultExpr,threads[i].resultExpr.translate(ctx));
                //logger.info(threads[i].resultExpr.toString());
                //logger.info(finalExpr.toString());
            }

            finalExpr = finalExpr.substitute(this.defaultExpr, ctx.mkInt(0));
            for (String name : extractor.names) {
                finalExpr = finalExpr.substitute(this.callCache.get(name), extractor.requestUsedArgs.get(name));
            }
            logger.info("Finish!!!!!!!!!!!");
            for (String name : extractor.names) {
                functions.put(name , finalExpr);
            }
        }

        results = functions;
    }

    /*
    *
    * 计算Func
    * */
    private void calculFunc(Expr expr){
        Expr [] exprs=expr.getArgs();
        String rightFuncName=null;
        if ((rightFuncName=ifExprIsFunc(exprs[1]))!=null){
            Function function=this.getAFunction(exprs[0]);
            function.setName(rightFuncName);
            calFunc.add(function);
            logger.info(function.toString()+"!!!!");
        }else if (ifContainsFuncs(exprs[1])){
            Expr [] rightArgs=exprs[1].getArgs();
            Expr newExpr=null;
            if ((rightFuncName=ifExprIsFunc(rightArgs[0]))!=null){
                newExpr=ctx.mkEq(ctx.mkAdd((ArithExpr)exprs[0], ctx.mkMul(ctx.mkInt(-1),(ArithExpr)rightArgs[1])),rightArgs[0]);
            }else {
                newExpr=ctx.mkEq(ctx.mkAdd((ArithExpr)exprs[0], ctx.mkMul(ctx.mkInt(-1),(ArithExpr)rightArgs[0])),rightArgs[1]);
            }
            this.calculFunc(newExpr);
        }else {
            Expr newExpr=null;
            newExpr=ctx.mkEq(exprs[1],exprs[0]);
            this.calculFunc(newExpr);
        }
    }

    /*
    * 判断orig是否包含函数
    *
    * */
    private boolean ifContainsFuncs(Expr orig){
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(orig);
        while (!todo.empty()) {
            Expr expr = todo.pop();
            if (expr.isApp()){
                FuncDecl exprFunc = expr.getFuncDecl();
                for (String name : extractor.names) {
                    FuncDecl f = extractor.rdcdRequests.get(name);
                    if (exprFunc.equals(f)){
                        if (!this.callCache.keySet().contains(name)) {
                            this.callCache.put(name, expr.getArgs());
                        }
                        return true;
                    }
                }

                Expr [] args = expr.getArgs();
                for (Expr arg: args) {
                    todo.push(arg);
                }

            }
        }
        return false;
    }

    /*
    * 判断expr是否是一个函数
    *
    * */
    private String ifExprIsFunc(Expr expr){
        FuncDecl exprFunc=expr.getFuncDecl();
        for (String name : extractor.names) {
            FuncDecl f = extractor.rdcdRequests.get(name);
            if (exprFunc.equals(f)){
                return name;
            }
        }
        return null;
    }

    /*
    * 将expr转变成DNF形式
    * */
    private List<Expr> changeToDNF(Expr expr){
        if (expr.isAnd()){
            Expr [] args = expr.getArgs();
            List<List<Expr>> everyAtomicExpr=new LinkedList<>();
            for (int i=0;i<args.length;i++){
                if (!ifContainsFuncs(args[i])){
                    List<Expr> res=new LinkedList<>();
                    res.add(expr);
                    everyAtomicExpr.add(res);
                }else {
                    everyAtomicExpr.add(this.changeToDNF(args[i]));
                }
            }

            this.getArrange(everyAtomicExpr, 0, null, new LinkedList<Expr>());

            List<Expr> res=new LinkedList<>();
            res.addAll(this.DNFExpr);
            this.DNFExpr.clear();
            return res;
        }else if (expr.isOr()){
            Expr [] args = expr.getArgs();
            List<Expr> res=new LinkedList<>();
            for (int i=0;i<args.length;i++) {
                if (!ifContainsFuncs(args[i])) {
                    res.add(args[i]);
                }else {
                    res.addAll(this.changeToDNF(args[i]));
                }
            }
            return res;
        }else if (expr.isNot()){
            Expr [] args = expr.getArgs();
            if (args[0].isAnd()){
                Expr [] subargs = args[0].getArgs();
                Expr [] newargs = new Expr[subargs.length];
                for (int i=0;i<subargs.length;i++){
                    newargs[i]=ctx.mkNot((BoolExpr)subargs[i]);
                }
                return this.changeToDNF(ctx.mkOr(Arrays.copyOf(newargs, newargs.length, BoolExpr[].class)));
            }else if (args[0].isOr()){
                Expr [] subargs = args[0].getArgs();
                Expr [] newargs = new Expr[subargs.length];
                for (int i=0;i<subargs.length;i++){
                    newargs[i]=ctx.mkNot((BoolExpr)subargs[i]);
                }
                return this.changeToDNF(ctx.mkAnd(Arrays.copyOf(newargs, newargs.length, BoolExpr[].class)));
            }else if (args[0].isNot()){
                Expr [] subargs = args[0].getArgs();
                List<Expr> res=new LinkedList<>();
                res.add(subargs[0]);
                return res;
            } else {
                List<Expr> res=new LinkedList<>();
                res.add(expr);
                return res;
            }
        }else {
            List<Expr> res=new LinkedList<>();
            res.add(expr);
            return res;
        }
    }


    private boolean ifNeedSwap(String name){
        Stack<Expr> todo = new Stack<Expr>();
        todo.push(this.z3Expr);
        while (!todo.empty()) {
            Expr expr = todo.pop();
            if (expr.isApp()){
                FuncDecl exprFunc = expr.getFuncDecl();
                FuncDecl f = extractor.rdcdRequests.get(name);
                if (exprFunc.equals(f)){
                    String format=expr.toString();
                    String [] temp=format.split(" ");
                    if (temp.length==1){
                        return false;
                    }
                    if (!temp[1].equals("x")){
                        return true;
                    }else {
                        return false;
                    }
                }

                Expr [] args = expr.getArgs();
                for (Expr arg: args) {
                    todo.push(arg);
                }

            }
        }

        return false;
    }

    private Expr makeFunction(Function function){
        Expr expr=ctx.mkAdd(ctx.mkMul(ctx.mkInt(function.getxPara()),ctx.mkIntConst("x")),ctx.mkMul(ctx.mkInt(function.getyPara()),ctx.mkIntConst("y")),ctx.mkInt(function.getcPara()));
        return expr;
    }

    private Function getAFunction(Expr orig){

        Function result=new Function("Variable","",0,0,0,0);
        if (orig.isConst()){
            if (orig.toString().equals("x")){
                result=new Function("variable","",0,1,0,0);
            }else if ((orig.toString().equals("y"))){
                result=new Function("variable","",0,0,1,0);
            }else {
                result=new Function("variable","",0,0,0,0);
            }
            return result;
        }else if (orig.isApp()){
            String name=null;
            if ((name=ifExprIsFunc(orig))!=null){
                for (Function function:calFunc){
                    if (function.name.equals(name)){
                        return function;
                    }
                }
                result=new Function(name,name,1,0,0,0);
                return result;
            } else if (orig.isMul()){
                result=new Function("number","",0,0,0,1);
                Expr [] exprs=orig.getArgs();
                for (Expr expr:exprs){
                    Function function=this.getAFunction(expr);
                    result=result.times(function);
                }
                return result;
            }else if (orig.isAdd()){
                Expr [] exprs=orig.getArgs();
                for (Expr expr:exprs){
                    Function function=this.getAFunction(expr);
                    logger.info(function.toString()+"*********");
                    result=result.plus(function);
                    logger.info(result.toString()+"&&&&&&&&");
                }
                return result;
            }
        }else {
            result=new Function("number","",0,0,0,Integer.parseInt(orig.toString()));
            return result;
        }
        return null;

    }

    private Expr tempExpr=null;

    /*
    * 用递归的方式来构造DNF
    *
    * */
    public void getArrange(List <List<Expr>> atomic, int pointer, Expr res, List <Expr> history){

        List<Expr> temp=atomic.get(pointer);
        for (int i=0;i< temp.size();i++){
            List<Expr> Next=new LinkedList<>(history);
            if (history.contains(temp.get(i))){
                tempExpr=res;
            }else {
                if (res==null){
                    tempExpr=temp.get(i);
                }else {
                    tempExpr=ctx.mkAnd((BoolExpr)res,(BoolExpr)temp.get(i));
                }
                Next.add(temp.get(i));
            }

            if (pointer+1<atomic.size()){
                getArrange(atomic, pointer+1, tempExpr, Next);
            }else {
                DNFExpr.add(tempExpr);

            }
        }

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

}
