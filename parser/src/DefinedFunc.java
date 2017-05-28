import java.util.*;
import com.microsoft.z3.*;

public class DefinedFunc {

    DefinedFunc(Context ctx, String name, Expr[] args, Expr definition) {
        this.ctx = ctx;
        this.name = name;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
    }

    DefinedFunc(Context ctx, Expr[] args, Expr definition) {
        this.ctx = ctx;
        this.name = null;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
    }

    Context ctx;
    String name;
    Expr [] args;
    Expr definition;
    int numArgs;

    public Expr apply(Expr... argList){
        return definition.substitute(args, argList);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        List<String> argstr = new ArrayList<String>();
        for (Expr expr : args) {
            argstr.add(expr.toString());
        }
        return argstr.toString() + " -> " + definition.toString();
    }

    public int getNumArgs() {
        return numArgs;
    }
}
