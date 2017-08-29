/**
 * Created by tian on 2017/8/20.
 */
public class Function {

    public String name;

    public String basicFuncName;

    public int funcPara;

    public int xPara;

    public int yPara;

    public int cPara;

    public Function(String name, String basicFuncName, int funcPara, int xPara, int yPara, int cPara) {
        this.name = name;
        this.basicFuncName = basicFuncName;
        this.funcPara = funcPara;
        this.xPara = xPara;
        this.yPara=yPara;
        this.cPara=cPara;
    }

    public Function plus(Function addFunction){
        Function res=null;
        if (addFunction.basicFuncName.equals(this.basicFuncName)){
            res=new Function("variable", this.basicFuncName,addFunction.funcPara+this.funcPara,addFunction.xPara+this.xPara,addFunction.yPara+this.yPara,addFunction.cPara+this.cPara);
        }else {
            res=new Function("variable", this.basicFuncName==""?addFunction.basicFuncName:this.basicFuncName,addFunction.funcPara+this.funcPara,addFunction.xPara+this.xPara,addFunction.yPara+this.yPara,addFunction.cPara+this.cPara);
        }
        return res;
    }

    public Function minus(Function addFunction){
        Function res=null;
        if (addFunction.basicFuncName.equals(this.basicFuncName)){
            res=new Function("variable", this.basicFuncName,this.funcPara-addFunction.funcPara,this.xPara-addFunction.xPara,this.yPara-addFunction.yPara,this.cPara-addFunction.cPara);
        }else {
            res=new Function("variable", this.basicFuncName==""?addFunction.basicFuncName:this.basicFuncName,this.funcPara-addFunction.funcPara,this.xPara-addFunction.xPara,this.yPara-addFunction.yPara,this.cPara-addFunction.cPara);
        }
        return res;
    }

    public Function times(Function timesFunction){
        Function res=null;
        if (timesFunction.name.equals("number")){
            res=new Function("variable", this.basicFuncName,timesFunction.cPara*this.funcPara,timesFunction.cPara*this.xPara,timesFunction.cPara*this.yPara,timesFunction.cPara*this.cPara);
        }else {
            res=new Function("variable", timesFunction.basicFuncName,this.cPara*timesFunction.funcPara,this.cPara*timesFunction.xPara,this.cPara*timesFunction.yPara,this.cPara*timesFunction.cPara);
        }
        return res;
    }

    public String toString(){
        String string=funcPara+" "+basicFuncName+" "+(xPara<0?xPara:"+ "+xPara)+" x "+(yPara<0?yPara:"+ "+yPara)+" y + "+cPara;
        return string;
    }

    public int getcPara() {
        return cPara;
    }

    public void setcPara(int cPara) {
        this.cPara = cPara;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasicFuncName() {
        return basicFuncName;
    }

    public void setBasicFuncName(String basicFuncName) {
        this.basicFuncName = basicFuncName;
    }

    public int getFuncPara() {
        return funcPara;
    }

    public void setFuncPara(int funcPara) {
        this.funcPara = funcPara;
    }

    public int getxPara() {
        return xPara;
    }

    public void setxPara(int xPara) {
        this.xPara = xPara;
    }

    public int getyPara() {
        return yPara;
    }

    public void setyPara(int yPara) {
        this.yPara = yPara;
    }
}
