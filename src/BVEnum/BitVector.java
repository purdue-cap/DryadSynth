import java.math.BigInteger;
import java.util.*;
public class BitVector {

    long value;

    public BitVector(long value) {
        this.value = value;
    }

    public static long bvand(long operand1, long operand2) {
        return operand1 & operand2;
    }

    public static long bvor(long operand1, long operand2) {
        return operand1 | operand2;
    }

    public static long bvxor(long operand1, long operand2) {
        return operand1 ^ operand2;
    }

    public static long bvnot(long operand1) {
        return ~operand1;
    }

    public static long bvnand(long operand1, long operand2) {
        return ~(operand1 & operand2);
    }

    public static long bvxnor(long operand1, long operand2) {
        return ~(operand1 ^ operand2);
    }

    public static long bvnor(long operand1, long operand2) {
        return ~(operand1 | operand2);
    }

    public static long bvadd(long operand1, long operand2) {
        return operand1 + operand2;
    }

    public static long bvsub(long operand1, long operand2) {
        return operand1 - operand2;
    }

    public static long bvshl(long operand1, long numbits) {
        return operand1 << numbits;
    }

    public static long bvlshr(long operand1, long numbits) {
        return operand1 >>> numbits;
    }

    public static long bveq(long operand1, long operand2) {
        return (operand1 == operand2) ? 1 : 0;
    }

}
