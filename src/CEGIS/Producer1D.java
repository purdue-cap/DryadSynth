import java.util.*;

public class Producer1D {
    int height = 1;
    public boolean heightsOnly = false;
    public int get() {
        int i;
        synchronized(this) {
            i = height;
            height++;
            if (heightsOnly) {
                System.out.println("heightEntered:" + new Integer(i).toString());
            }
        }
        return i;
    }
    public void reset() {
        synchronized(this){
            height = 1;
        }
    }
}
