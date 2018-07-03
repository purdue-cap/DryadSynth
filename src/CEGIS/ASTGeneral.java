import java.util.*;
import com.microsoft.z3.*;

public class ASTGeneral {
    public String node;
    public List<ASTGeneral> children = new ArrayList<ASTGeneral>();
    public String toString() {
        if (children.size() == 0) {
            return node;
        } else {
            List<String> strs = new ArrayList<String>();
            strs.add(node);
            for (ASTGeneral ast : children) {
                strs.add(ast.toString());
            }
            return "(" + String.join(" ", strs) + ")";
        }
    }
}
