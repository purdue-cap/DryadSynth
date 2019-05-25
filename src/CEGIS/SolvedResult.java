import com.microsoft.z3.*;
import java.util.*;

public class SolvedResult {
    public int currentHeight;
    public DefinedFunc[] solution;

    SolvedResult() {
        this.currentHeight = 1;
        this.solution = null;
    }
}