import java.util.*;
import com.microsoft.z3.*;

public class DefinedFunc {

    DefinedFunc(String name, Expr[] args, Expr definition) {
        this.name = name;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
    }

    DefinedFunc(Expr[] args, Expr definition) {
        this.name = null;
        this.args = args;
        this.definition = definition;
        this.numArgs = args.length;
    }

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
