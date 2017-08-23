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
    
    private List<String> orString;
    
    private Logger logger;
    
    private List<Expr> arrangeRes;
    
    private List<Expr> DNFExpr;
    
    private List<Function> calFunc;
    
    private Map<String, String> funcNameAndFormat;
    
    private Context ctx;
    
    public Solver s;
    
    private SygusExtractor extractor;
    
    private boolean ifException;
    
    public NewMethod(Context ctx, SygusExtractor extractor, Logger logger){
        this.ctx=ctx;
        this.extractor=extractor.translate(ctx);;
        this.s = ctx.mkSolver();
        this.oldZ3String=this.extractor.finalConstraint.toString();
        this.orString=new LinkedList<>();
        this.logger=logger;
        this.DNFExpr=new LinkedList<Expr>();
        this.calFunc=new LinkedList<>();
        this.funcNameAndFormat=new LinkedHashMap<>();
        this.ifException=false;
        
        this.z3String=this.changeFunc(this.oldZ3String);
        if (extractor.names.size()>1){
            int position=this.oldZ3String.indexOf(extractor.names.get(0));
            if (!this.oldZ3String.substring(position-1,position).equals("(")){
                this.z3String=this.oldZ3String;
                this.ifException=true;
            }
        }
        
        Map<String, Expr> functions = new LinkedHashMap<String, Expr>();
        
        if (extractor.names.size()>1){
            List<String> list=this.getEveryExpr(this.z3String.substring(1,this.z3String.length()-1));
            if (!z3String.substring(1,4).equals("and")){
                List<String> everyRightList=this.getEveryExpr(list.get(1));
                boolean ifExist=false;
                for (String s1:everyRightList){
                    if (extractor.names.contains(s1)){
                        ifExist=true;
                        break;
                    }
                }
                if (!ifExist){
                    Function function=this.calculatorFunc(list.get(1));
                    function.setBasicFuncName(extractor.names.get(0));
                    function.setFuncPara(-1);
                    function.setName(extractor.names.get(1));
                    calFunc.add(function);
                    System.out.println(function.toString());
                }else {
                    this.simplifyFunc(list);
                }
            }else {
                for (String s:list) {
                    List<String> everyList=this.getEveryExpr(s);
                    this.simplifyFunc(everyList);
                }
            }
            String basicFunc=calFunc.get(0).getBasicFuncName();
            
            for (String name : extractor.names) {
                if (name.equals(basicFunc)){
                    functions.put(name , ctx.mkInt("0"));
                }else {
                    for (Function function:calFunc){
                        if (function.getName().equals(name)){
                            if ((!ifException)&&ifNeedSwap(name)){
                                int x=function.getxPara();
                                function.setxPara(function.getyPara());
                                function.setyPara(x);
                            }
                            Expr expr=this.makeFunction(function);
                            functions.put(name , expr);
                        }
                    }
                }
            }
        }else {
            this.DNFExpr=this.convertToDNF(this.z3String);
            
            Expr res = this.getFinalRes();
            
            for (String name : extractor.names) {
                functions.put(name , res);
            }
        }
        
        Verifier testVerifier = new Verifier(ctx, extractor, logger);
        Status v = testVerifier.verify(functions);
        logger.info("************"+v);
    }
    
    private boolean ifNeedSwap(String name){
        String format=funcNameAndFormat.get(name);
        String [] temp=format.split(" ");
        if (!temp[1].equals("x")){
            return true;
        }
        return false;
    }
    
    private Expr makeFunction(Function function){
        Expr expr=ctx.mkAdd(ctx.mkMul(ctx.mkInt(function.getxPara()),ctx.mkIntConst("x")),ctx.mkMul(ctx.mkInt(function.getyPara()),ctx.mkIntConst("y")),ctx.mkInt(function.getcPara()));
        return expr;
    }
    
    private void simplifyFunc(List<String> everyList){
        
        Function function=this.calculatorFunc(everyList.get(0));
        if (extractor.names.contains(everyList.get(1))){
            boolean ifFind=false;
            for (Function tempFunc:calFunc){
                if (tempFunc.name.equals(everyList.get(1))){
                    ifFind=true;
                    break;
                }
            }
            if (!ifFind){
                function.setName(everyList.get(1));
                calFunc.add(function);
                System.out.println(function.toString());
            }
        }else {
            
            List<String> everyRightList=this.getEveryExpr(everyList.get(1));
            Function result=null;
            int i=0;
            String name="";
            for (String everyString:everyRightList){
                Function tempFunction=this.calculatorFunc(everyString);
                if (extractor.names.contains(tempFunction.name)){
                    boolean ifFind=false;
                    for (Function f:calFunc){
                        if (f.name.equals(tempFunction.name)){
                            ifFind=true;
                            break;
                        }
                    }
                    if (!ifFind){
                        name=tempFunction.name;
                        continue;
                    }
                }
                if (i==0){
                    result=function.minus(tempFunction);
                    i++;
                }else {
                    result=result.minus(tempFunction);
                }
            }
            result.setName(name);
            calFunc.add(result);
            System.out.println(result.toString());
        }
        
    }
    
    /*
     * 将一个String表达式转成函数形式
     *
     * */
    private Function calculatorFunc(String funcString){
        List<String> everyList=this.getEveryExpr(funcString);
        
        Function result=new Function("tempVariable","",0,0,0,0);
        if (funcString.substring(0,1).equals("+")){
            
            for (String everyString:everyList){
                Function function=this.calculatorFunc(everyString);
                result=result.plus(function);
            }
            return result;
        }else if (funcString.substring(0,1).equals("*")){
            result=new Function("tempVariable","",0,0,0,1);
            for (String everyString:everyList){
                Function function=this.calculatorFunc(everyString);
                result=result.times(function);
            }
            
            return result;
        }else if (extractor.names.contains(funcString)){
            for (Function function:calFunc){
                if (function.name.equals(funcString)){
                    return function;
                }
            }
            result=new Function(funcString,funcString,1,0,0,0);
            return result;
        } else if (funcString.substring(0,1).equals("-")||Character.isDigit(funcString.toCharArray()[0])){
            if (funcString.substring(0,1).equals("-")){
                result=new Function("number","",0,0,0,Integer.parseInt(funcString.substring(2))*-1);
            }else {
                result=new Function("number","",0,0,0,Integer.parseInt(funcString));
            }
            return result;
        }else{
            if (funcString.equals("x")){
                result=new Function("variable","",0,1,0,0);
            }else {
                result=new Function("variable","",0,0,1,0);
            }
            return result;
        }
        
    }
    
    private Expr getFinalRes(){
        Expr lastExpr=null;
        boolean ifFirst=true;
        for (int i=0;i<DNFExpr.size();i++){
            Status status=ifSatisfy(DNFExpr.get(i));
            
            if (status == Status.UNSATISFIABLE) {
                
                continue;
                
            }else if (status == Status.UNKNOWN) {
                
                logger.severe("Verifier Error : Unknown");
                
            }else if (status == Status.SATISFIABLE) {
                String DNFString=DNFExpr.get(i).toString();
                
                boolean ifContain=false;
                for (String name : extractor.names) {
                    if (DNFString.contains(name)){
                        ifContain=true;
                        char [] temp=DNFString.toCharArray();
                        Stack<Character> stack=new Stack<>();
                        
                        int last=0;
                        
                        int position=DNFString.indexOf("(=");
                        for (int j=position;j<temp.length;j++){
                            if (temp[j]=='('){
                                stack.push('(');
                            }
                            if (temp[j]==')'){
                                stack.pop();
                                if (stack.isEmpty()){
                                    last=j;
                                    break;
                                }
                            }
                        }
                        String eqString=DNFString.substring(position+1,last);
                        String replace="";
                        if (eqString.substring(0,name.length()+2).equals("= "+name))
                        {
                            replace=eqString.substring(name.length()+3);
                        }else {
                            replace=eqString.substring(2);
                            replace=replace.substring(0, replace.length()-name.length()-1);
                        }
                        if (replace.substring(0,1).equals("(")){
                            replace=replace.substring(1,replace.length()-1);
                        }
                        
                        Expr boolExpr = null;
                        Expr resExpr = this.convertStringToExpr(replace);
                        
                        boolExpr = DNFExpr.get(i).substitute(ctx.mkIntConst(name), resExpr);
                        
                        if (ifFirst){
                            lastExpr = ctx.mkITE((BoolExpr)boolExpr, resExpr, ctx.mkInt(-1));
                            ifFirst=false;
                        }else {
                            lastExpr = ctx.mkITE((BoolExpr)boolExpr, resExpr, lastExpr);
                        }
                    }
                }
                
                if (!ifContain){
                    if (ifFirst){
                        lastExpr = ctx.mkITE((BoolExpr)DNFExpr.get(i), ctx.mkInt(0), ctx.mkInt(-1));
                        ifFirst=false;
                    }else {
                        lastExpr = ctx.mkITE((BoolExpr)DNFExpr.get(i), ctx.mkInt(0), lastExpr);
                    }
                }
            }
        }
        
        return lastExpr;
    }
    
    private Status ifSatisfy(Expr expr){
        s.push();
        s.add((BoolExpr)expr);
        
        Status status = s.check();
        s.pop();
        return status;
    }
    
    private List<Expr> convertToDNF(String tempString){
        char [] temp=tempString.toCharArray();
        
        if (tempString.substring(1,4).equals("and")){
            
            List <List<Expr>> atomic= new LinkedList<>();
            int pointer=0;
            boolean ifFirst=true;
            Stack<Character> stack=new Stack<>();
            
            for (int i=1;i<temp.length-1;i++){
                if (temp[i]=='('){
                    stack.push('(');
                    if (ifFirst){
                        pointer=i;
                        ifFirst=false;
                    }
                }
                if (temp[i]==')'){
                    stack.pop();
                    if (stack.isEmpty()){
                        atomic.add(this.convertToDNF(tempString.substring(pointer,i+1)));
                        ifFirst=true;
                    }
                }
            }
            
            this.getArrange(atomic, 0, null);
            
            List<Expr> res=new LinkedList<>();
            res.addAll(this.DNFExpr);
            this.DNFExpr.clear();
            return res;
            
        }else if (tempString.substring(1,3).equals("or")){
            
            int pointer=0;
            boolean ifFirst=true;
            Stack<Character> stack=new Stack<>();
            List<Expr> res=new LinkedList<>();
            
            for (int i=1;i<temp.length-1;i++){
                if (temp[i]=='('){
                    stack.push('(');
                    if (ifFirst){
                        pointer=i;
                        ifFirst=false;
                    }
                }
                if (temp[i]==')'){
                    stack.pop();
                    if (stack.isEmpty()){
                        res.addAll(this.convertToDNF(tempString.substring(pointer,i+1)));
                        ifFirst=true;
                    }
                }
            }
            
            return res;
        }else {
            //Expr expr=this.stackWay(tempString);
            Expr expr=this.convertStringToExpr(tempString.substring(1,tempString.length()-1));
            List<Expr> res=new LinkedList<>();
            res.add(expr);
            return res;
        }
    }
    
    private String changeFunc(String z3String){
        for (String name : extractor.names) {
            int length=name.length();
            
            char [] temp=z3String.toCharArray();
            
            int pointer=0;
            int last=0;
            boolean ifFind=false;
            for (int i=0;i<temp.length;i++){
                if (temp[i]=='('){
                    pointer=i;
                }
                if ((i+length)<=z3String.length()&&z3String.substring(i,i+length).equals(name)){
                    ifFind=true;
                }
                if (temp[i]==')'){
                    if (ifFind){
                        last=i;
                        break;
                    }
                }
            }
            String regs=z3String.substring(pointer,last+1);
            funcNameAndFormat.put(name,regs);
            //logger.info("####33####"+regs);
            z3String=z3String.replace(regs,name);
        }
        
        return z3String;
    }
    
    /*
     * 将String用栈的方法转成Expr
     *
     * */
    private Expr stackWay(String string){
        Stack<String> operatorStack = new Stack<String>();
        Stack<Object> variableStack = new Stack<Object>();
        char [] charString=string.toCharArray();
        for (int i=0;i<charString.length;){
            if (charString[i]=='('){
                variableStack.push(null);
                
                int end=string.substring(i+1).indexOf(' ');
                String operator=string.substring(i+1,i+1+end);
                operatorStack.push(operator);
                i=i+end+2;
                //System.out.println("i : "+i);
            }else if (Character.isDigit(charString[i])){
                int j=i;
                for (;j<charString.length;j++){
                    if (charString[j]==' '||charString[j]==')'||charString[j]=='\r'||charString[j]=='\n'||charString[j]=='\t'){
                        break;
                    }
                }
                String number=string.substring(i,j);
                variableStack.push(ctx.mkInt(number));
                i=j;
            }else if (charString[i]==')'){
                Object top=variableStack.pop();
                List<Expr> args = new ArrayList<Expr>();
                while (top!=null){
                    args.add(0,(Expr)top);
                    top=variableStack.pop();
                    //System.out.println(top+"!!");
                }
                String op=operatorStack.pop();
                Expr expr=this.getExpr(op, args.toArray(new Expr[args.size()]));
                variableStack.push(expr);
                i++;
            }else if (charString[i]==' '||charString[i]=='\r'||charString[i]=='\n'||charString[i]=='\t'){
                i++;
                continue;
            } else {
                int j=i;
                for (;j<charString.length;j++){
                    if (charString[j]==' '||charString[j]==')'||charString[j]=='\r'||charString[j]=='\n'||charString[j]=='\t'){
                        break;
                    }
                }
                String number=string.substring(i,j);
                //System.out.println("push : "+number);
                variableStack.push(ctx.mkIntConst(number));
                i=j;
            }
        }
        Expr res=(Expr)variableStack.pop();
        //System.out.println(res.toString()+"@@@@@@");
        return res;
    }
    
    private Expr getExpr(String name, Expr[] args) {
        if (name.equals("+")) {
            return ctx.mkAdd(Arrays.copyOf(args, args.length, ArithExpr[].class));
        }
        if (name.equals("-")) {
            return ctx.mkSub(Arrays.copyOf(args, args.length, ArithExpr[].class));
        }
        if (name.equals("*")) {
            return ctx.mkMul(Arrays.copyOf(args, args.length, ArithExpr[].class));
        }
        if (name.equals("/")) {
            return ctx.mkDiv((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("and")) {
            return ctx.mkAnd(Arrays.copyOf(args, args.length, BoolExpr[].class));
        }
        if (name.equals("or")) {
            return ctx.mkOr(Arrays.copyOf(args, args.length, BoolExpr[].class));
        }
        if (name.equals("not")) {
            return ctx.mkNot((BoolExpr)args[0]);
        }
        if (name.equals(">")) {
            return ctx.mkGt((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals(">=")) {
            return ctx.mkGe((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("<")) {
            return ctx.mkLt((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("<=")) {
            return ctx.mkLe((ArithExpr)args[0], (ArithExpr)args[1]);
        }
        if (name.equals("=")) {
            return ctx.mkEq(args[0], args[1]);
        }
        return null;
    }
    
    /*
     * 将String用递归的方法转成Expr
     *
     * */
    private Expr convertStringToExpr(String z3String){
        char [] temp=z3String.toCharArray();
        //logger.info("####33####"+z3String);
        if (temp.length>2&&z3String.substring(0,3).equals("not")){
            Stack<Character> stack=new Stack<>();
            
            int pointer=0;
            boolean ifFirst=true;
            for (int i=0;i<temp.length;i++){
                if (temp[i]=='('){
                    stack.push('(');
                    if (ifFirst){
                        pointer=i+1;
                        ifFirst=false;
                    }
                }
                if (temp[i]==')'){
                    stack.pop();
                    if (stack.isEmpty()){
                        Expr expr=this.convertStringToExpr(z3String.substring(pointer,i));
                        //logger.info("!!!!!!!!!!!!!!"+expr.toString());
                        return ctx.mkNot((BoolExpr)expr);
                    }
                }
            }
        }else if (temp.length>2&&z3String.substring(0,3).equals("and")){
            Expr expr=null;
            Stack<Character> stack=new Stack<>();
            
            int pointer=0;
            boolean ifFirst=true;
            boolean ifNumberOne=true;
            for (int i=0;i<temp.length;i++){
                if (temp[i]=='('){
                    stack.push('(');
                    if (ifFirst){
                        pointer=i+1;
                        ifFirst=false;
                    }
                }
                if (temp[i]==')'){
                    stack.pop();
                    if (stack.isEmpty()){
                        if (ifNumberOne){
                            expr = this.convertStringToExpr(z3String.substring(pointer,i));
                            ifNumberOne=false;
                        }else {
                            expr = ctx.mkAnd((BoolExpr)expr, (BoolExpr)this.convertStringToExpr(z3String.substring(pointer,i)));
                        }
                        ifFirst=true;
                    }
                }
            }
            return expr;
        }else if (temp.length>1&&z3String.substring(0,2).equals("or")){
            Expr expr=null;
            Stack<Character> stack=new Stack<>();
            
            int pointer=0;
            boolean ifFirst=true;
            boolean ifNumberOne=true;
            for (int i=0;i<temp.length;i++){
                if (temp[i]=='('){
                    stack.push('(');
                    if (ifFirst){
                        pointer=i+1;
                        ifFirst=false;
                    }
                }
                if (temp[i]==')'){
                    stack.pop();
                    if (stack.isEmpty()){
                        if (ifNumberOne){
                            expr = this.convertStringToExpr(z3String.substring(pointer,i));
                            ifNumberOne=false;
                        }else {
                            expr = ctx.mkOr((BoolExpr)expr, (BoolExpr)this.convertStringToExpr(z3String.substring(pointer,i)));
                        }
                        ifFirst=true;
                    }
                }
            }
            return expr;
        }else if (temp.length>1&&z3String.substring(0,2).equals(">=")){
            
            List<String> res=this.getEveryExpr(z3String);
            return ctx.mkGe((ArithExpr)this.convertStringToExpr(res.get(0)),(ArithExpr)this.convertStringToExpr(res.get(1)));
            
        }else if (temp.length>1&&z3String.substring(0,2).equals("<=")){
            
            List<String> res=this.getEveryExpr(z3String);
            return ctx.mkLe((ArithExpr)this.convertStringToExpr(res.get(0)),(ArithExpr)this.convertStringToExpr(res.get(1)));
            
        }else if (temp.length>1&&z3String.substring(0,1).equals(">")){
            
            List<String> res=this.getEveryExpr(z3String);
            return ctx.mkGt((ArithExpr)this.convertStringToExpr(res.get(0)),(ArithExpr)this.convertStringToExpr(res.get(1)));
            
        }else if (temp.length>1&&z3String.substring(0,1).equals("<")){
            
            List<String> res=this.getEveryExpr(z3String);
            return ctx.mkLt((ArithExpr)this.convertStringToExpr(res.get(0)),(ArithExpr)this.convertStringToExpr(res.get(1)));
            
        }else if (z3String.substring(0,1).equals("=")){
            
            List<String> res=this.getEveryExpr(z3String);
            return ctx.mkEq((ArithExpr)this.convertStringToExpr(res.get(0)),(ArithExpr)this.convertStringToExpr(res.get(1)));
            
        }else if (z3String.substring(0,1).equals("+")){
            
            List<String> res=this.getEveryExpr(z3String);
            Expr expr=null;
            for (int i=0;i<res.size();i++){
                if (i==0){
                    expr=this.convertStringToExpr(res.get(i));
                }else {
                    expr=ctx.mkAdd((ArithExpr)expr,(ArithExpr)this.convertStringToExpr(res.get(i)));
                }
            }
            return expr;
            
        }else if (z3String.substring(0,1).equals("-")){
            
            return ctx.mkInt("-"+z3String.substring(2));
            
        }else if (z3String.substring(0,1).equals("*")){
            
            List<String> res=this.getEveryExpr(z3String);
            Expr expr=null;
            for (int i=0;i<res.size();i++){
                if (i==0){
                    expr=this.convertStringToExpr(res.get(i));
                }else {
                    expr=ctx.mkMul((ArithExpr)expr,(ArithExpr)this.convertStringToExpr(res.get(i)));
                }
            }
            return expr;
            
        }else if (z3String.substring(0,1).equals("/")){
            
            List<String> res=this.getEveryExpr(z3String);
            Expr expr=null;
            for (int i=0;i<res.size();i++){
                if (i==0){
                    expr=this.convertStringToExpr(res.get(i));
                }else {
                    expr=ctx.mkDiv((ArithExpr)expr,(ArithExpr)this.convertStringToExpr(res.get(i)));
                }
            }
            return expr;
        }else {
            if (Character.isDigit(z3String.toCharArray()[0])){
                return ctx.mkInt(z3String);
            }else {
                return ctx.mkIntConst(z3String);
            }
        }
        return null;
    }
    
    public List<String> getEveryExpr(String experString){
        char [] temp=experString.toCharArray();
        List<String> res=new LinkedList<>();
        Stack<Character> stack=new Stack<>();
        
        int pointer=0;
        boolean ifFirst=true;
        boolean ifAlone=true;
        
        for (int i=experString.indexOf(" ")+1;i<temp.length;i++){
            if (temp[i]=='('){
                stack.push('(');
                ifAlone=false;
                if (ifFirst){
                    pointer=i+1;
                    ifFirst=false;
                }
            }
            if (temp[i]==')'){
                stack.pop();
                if (stack.isEmpty()){
                    res.add(experString.substring(pointer,i));
                    ifFirst=true;
                    ifAlone=true;
                }
            }
            if (ifAlone){
                if (temp[i]!=' '&&temp[i]!='('&&temp[i]!=')'&&temp[i]!='\n'&&temp[i]!='\r'&&temp[i]!='\t'){
                    int j=i;
                    for (;j<temp.length;j++){
                        if (temp[j]==' '||temp[j]==')'||temp[j]=='\r'||temp[j]=='\n'||temp[j]=='\t'){
                            break;
                        }
                    }
                    
                    if (j==temp.length){
                        res.add(experString.substring(i));
                        i=i+experString.substring(i).length();
                    }else {
                        res.add(experString.substring(i,j));
                        i=j;
                    }
                }
            }
        }
        return res;
    }
    
    private Expr tempString=null;
    
    public void getArrange(List <List<Expr>> atomic, int pointer, Expr res){
        
        List<Expr> temp=atomic.get(pointer);
        int tempLength=0;
        for (int i=0;i< temp.size();i++){
            if (res==null){
                tempString=temp.get(i);
            }else {
                tempString=ctx.mkAnd((BoolExpr)res,(BoolExpr)temp.get(i));
            }
            if (pointer+1<atomic.size()){
                getArrange(atomic, pointer+1, tempString);
            }else {
                DNFExpr.add(tempString);
            }
        }
        
    }
    
    public String getDNFString() {
        return DNFString;
    }
    
}
