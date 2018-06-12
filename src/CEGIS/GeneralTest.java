import java.util.*;

public class GeneralTest{
    public static void main(String [] args) throws Exception {
        int [][] testComb = Expand.combination(9, 3);
        for (int[] comb: testComb) {
            for (int i: comb) {
                System.out.print(i + " ");
            }
            System.out.println("");
        }
    }
}
