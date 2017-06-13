import java.util.*;

public class Producer1D {
    private int height = 1;
    public int get() {
        int i;
        synchronized(this) {
            i = height;
            height++;
        }
        return i;
    }
}
