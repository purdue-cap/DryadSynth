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
    public ASTGeneral() {
    }
    public ASTGeneral(String node, ASTGeneral ... childNodes) {
        this.node = node;
        for (ASTGeneral ast : childNodes) {
            this.children.add(ast);
        }
    }
    // All recursive algorithms here,
    // cound move to cache based dynamic programming if
    // this becomes the bottleneck in the future
    public ASTGeneral(ASTGeneral copy) {
        this.node = new String(copy.node);
        List<ASTGeneral> newChildren = new ArrayList<ASTGeneral>(copy.children.size());
        for (ASTGeneral child : copy.children) {
            newChildren.add(new ASTGeneral(child));
        }
        this.children = newChildren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        ASTGeneral ast = (ASTGeneral)o;
        if (ast.node != this.node) {
            return false;
        }
        if (ast.children.size() != this.children.size()) {
            return false;
        }
        boolean result = true;
        for (int i = 0; i < this.children.size(); i++) {
            result = this.children.get(i).equals(ast.children.get(i)) && result;
        }
         return result;
    }

    @Override
    public int hashCode() {
        Object[] objs = new Object[this.children.size() + 1];
        objs[0] = this.node;
        for (int i = 1; i <= this.children.size(); i++) {
            objs[i] = this.children.get(i);
        }
        return Arrays.hashCode(objs);
    }

    public ASTGeneral substitute(ASTGeneral from, ASTGeneral to) {
        if (this.equals(from)) {
            return new ASTGeneral(to);
        }
        ASTGeneral newAST = new ASTGeneral();
        newAST.node = new String(this.node);
        for (ASTGeneral child : this.children) {
            newAST.children.add(child.substitute(from, to));
        }
        return newAST;
    }
}
