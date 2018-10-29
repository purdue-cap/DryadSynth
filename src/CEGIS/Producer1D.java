import java.util.*;

public class Producer1D {
    int height = 1;
    public int get() {
        int i;
        synchronized(this) {
            i = height;
            height++;
        }
        return i;
    }
    public void reset() {
        synchronized(this){
            height = 1;
        }
    }
}
