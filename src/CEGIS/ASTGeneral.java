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
        if (!ast.node.equals(this.node)) {
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
            objs[i] = this.children.get(i - 1);
        }
        return Arrays.hashCode(objs);
    }


    public ASTGeneral substitute(ASTGeneral[] from, ASTGeneral[] to) {
        if (from.length != to.length) {
            return null;
        }
        Stack<ASTGeneral> todo = new Stack<ASTGeneral>();
        todo.push(this);
        Map<ASTGeneral, ASTGeneral> cache = new HashMap<ASTGeneral, ASTGeneral>();
        for (int i = 0; i < from.length; i++) {
            cache.put(from[i], new ASTGeneral(to[i]));
        }

        boolean visited;
        List<ASTGeneral> newChildren = new ArrayList<ASTGeneral>();
        while(!todo.empty()) {
            ASTGeneral tree = todo.peek();
            if (tree.isLeaf()) {
                todo.pop();
                cache.put(tree, new ASTGeneral(tree));
            } else {
                visited = true;
                newChildren.clear();
                for (ASTGeneral child : tree.children) {
                    if(!cache.containsKey(child)) {
                        todo.push(child);
                        visited = false;
                    } else {
                        newChildren.add(cache.get(child));
                    }
                }
                if (visited) {
                    todo.pop();
                    ASTGeneral newTree;
                    ASTGeneral[] newChildrenArray = newChildren.toArray(new ASTGeneral[newChildren.size()]);
                    newTree = tree.update(newChildrenArray);
                    cache.put(tree, newTree);
                }
            }
        }

        return cache.get(this);
    }

    public ASTGeneral substitute(ASTGeneral from, ASTGeneral to) {
        if (this.equals(from)) {
            return new ASTGeneral(to);
        }
        return this.substitute(new ASTGeneral[]{from}, new ASTGeneral[]{to});
    }

    public ASTGeneral update(ASTGeneral[] newArgs) {
        ASTGeneral newAST = new ASTGeneral(this.node);
        for (ASTGeneral arg : newArgs) {
            newAST.children.add(new ASTGeneral(arg));
        }
        return newAST;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

}
