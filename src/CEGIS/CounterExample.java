import com.microsoft.z3.*;
import java.util.*;

public class CounterExample {

    public Expr[] consts;
    public Map<FuncDecl, Expr> funcs;

    public CounterExample(Expr[] consts, Map<FuncDecl, Expr> funcs) {
        this.consts = consts;
        this.funcs = funcs;
    }

    public Expr[] getConsts() {
        return this.consts;
    }

    public Map<FuncDecl, Expr> getFuncs() {
        return this.funcs;
    }

    public void setConsts(Expr[] c) {
        this.consts = c;
    }

    public void setFuncs(Map<FuncDecl, Expr> f) {
        this.funcs = f;
    }
    
}
