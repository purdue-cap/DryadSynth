import java.util.*;
import java.util.logging.Logger;
import com.microsoft.z3.*;

/**
 * Created by tian on 2017/8/8.
 * */
public class NewMethod{

    private String z3String;

    private String oldZ3String;

    private String DNFString;

    private FuncDecl funcDecl;

    private String FuncName;

    private List<String> orString;

    private Logger logger;

    private List<Expr> arrangeRes;

    private List<Expr> DNFExpr;

    private List<Function> calFunc;

    private Map<String, String> funcNameAndFormat;

    private Context ctx;

    public Solver s;

    private SygusExtractor extractor;

    private Expr z3Expr;

    private boolean ifException;

    public DefinedFunc[] results = null;

    public NewMethod(Context ctx, SygusExtractor extractor, Logger logger){
        this.ctx=ctx;
        this.extractor=extractor.translate(ctx);
        this.s = ctx.mkSolver();
        this.z3Expr=this.extractor.finalConstraint;
        this.oldZ3String=this.extractor.finalConstraint.toString();
        this.orString=new LinkedList<>();
        this.logger=logger;
        this.DNFExpr=new LinkedList<Expr>();
        this.calFunc=new LinkedList<>();
        this.funcNameAndFormat=new LinkedHashMap<>();
        this.ifException=false;
        this.FuncName=extractor.names.get(0);
        this.funcDecl=extractor.rdcdRequests.get(this.FuncName);

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
                    functions.put(name , ctx.mkInt("0"));
                }else {
                    for (Function function:calFunc){
                        if (function.getName().equals(name)){
                            if (ifNeedSwap(name)){
                                int x=function.getxPara();
                                function.setxPara(function.getyPara());
                                function.setyPara(x);
                            }
                            Expr expr=this.makeFunction(function);
                            System.out.println(expr.toString());
                            functions.put(name , expr);
                        }
                    }
                }
            }
        }else {
            this.DNFExpr=this.changeToDNF(this.z3Expr);

            logger.info("*******1*****");
            Expr res = this.getFinalRes();

            for (String name : extractor.names) {
                functions.put(name , res);
            }
        }

//        Verifier testVerifier = new Verifier(ctx, extractor, logger);
//        Status v = testVerifier.verify(functions);
//        logger.info("************"+v);

        results = new DefinedFunc[functions.size()];
        int i = 0;
        for (String name : extractor.rdcdRequests.keySet()) {
            Expr def = functions.get(name);
            if (def.isBool()) {
                def = SygusFormatter.elimITE(this.ctx, def);
            }
            results[i] = new DefinedFunc(ctx, name, extractor.requestArgs.get(name), def);
            //logger.info("Done, Synthesized function(s):" + Arrays.toString(results));
            i = i + 1;
        }
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
            System.out.println(function.toString()+"!!!!");
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
                    //System.out.println(function.toString()+"*********");
                    result=result.plus(function);
                    //System.out.println(result.toString()+"&&&&&&&&");
                }
                return result;
            }
        }else {
            result=new Function("number","",0,0,0,Integer.parseInt(orig.toString()));
            return result;
        }
        return null;

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
            if (expr.isConst()){
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

    /*
    * 根据DNF表达式构造最后的ite表达式
    * */
    private Expr getFinalRes(){
        Expr lastExpr=ctx.mkInt(-1);
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

    public String getDNFString() {
        return DNFString;
    }

}
