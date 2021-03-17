import java.math.BigInteger;
import java.util.*;
public class bitVector {
    BitSet bitset;
    public bitVector(){
        bitset = new BitSet();
    }

    public bitVector(int nbit){
        bitset = new BitSet(nbit);
    }
    
    public int length(){
        return this.bitset.length();
    }

    public int size(){
        return this.bitset.size();
    }

    public BigInteger unsignValue(int size){
        BigInteger result = BigInteger.valueOf(0);
        BigInteger two = BigInteger.valueOf(2);
        for(int i = 0;i<size;i++){
            if(this.bitset.get(i)==true){
                result=result.add(two.pow(i));
            }
        }
        return result;
    }

    public BigInteger signValue(int size){
        BigInteger result;
        BigInteger negOne = BigInteger.valueOf(-1);
        if(this.bitset.get(size-1) == true){
            bitVector negThis = neg(this);
            result = negThis.unsignValue(size);
            result = result.multiply(negOne);
        }
        else{
            result = this.unsignValue(size);
        }
        return result;
    }

    public String toString(){
        String result = new String();
        if(this.length() <= 0){
            result = "0";
            return result;
        }
        for(int i = this.bitset.length() - 1; i >=0;i--){
            if(this.bitset.get(i) == true){
                result += "1";
            }
            else{
                result += "0";
            }
        }
        return result;
        //return this.unsignValue(64).toString();
    }

    public void setData(long number){
        if(number >= 0){
            int length = (int)(Math.log10(number) / Math.log10(2));
            for(int i = 0; i<=length;i++){
                if((number%2) == 1){   
                    this.bitset.set(i);
                }
                number /= 2;
            }
        }
        else{
            bitVector intermediate = new bitVector();
            intermediate.setData(-1*number);
            this.bitset = neg(intermediate).bitset;
        }
        
        
    }

    public static bitVector copy(bitVector target){
        bitVector result = new bitVector();
        for(int i = 0; i < target.length();i++){
            if(target.bitset.get(i) == true){
                result.bitset.set(i);
            }
        }
        return result;
    }

    public static bitVector and(bitVector operand1,bitVector operand2){
        bitVector result = copy(operand1);
        result.bitset.and(operand2.bitset);
        return result;
    }

    public static bitVector or(bitVector operand1,bitVector operand2){
        bitVector result = copy(operand1);
        result.bitset.or(operand2.bitset);
        return result;
    }

    public static bitVector xor(bitVector operand1,bitVector operand2){
        bitVector result = copy(operand1);
        result.bitset.xor(operand2.bitset);
        return result;
    }

    public static bitVector not(bitVector operand1){
        bitVector result = copy(operand1);
        result.bitset.flip(0,result.bitset.size());
        return result;
    }

    public static bitVector nand(bitVector operand1,bitVector operand2){
        bitVector result = and(operand1,operand2);
        result = not(result);
        return result;
    }

    public static bitVector xnor(bitVector operand1,bitVector operand2){
        bitVector result = xor(operand1,operand2);
        result = not(result);
        return result;
    }

    public static bitVector nor(bitVector operand1,bitVector operand2){
        bitVector result = or(operand1,operand2);
        result = not(result);
        return result;
    }

    public static boolean eq(bitVector operand1,bitVector operand2,int size){
        boolean result = true;
        if((operand1.length() != operand2.length()) && (operand1.length() <= size || operand2.length() <= size)){
            return false;
        }
        else{
            for(int i = 0; i < size;i++){
                if(operand1.bitset.get(i) != operand2.bitset.get(i)){
                    result = false;
                }
            }
        }
        return result;
    }

    public static boolean ugt(bitVector operand1,bitVector operand2,int size){
        if((operand1.length() > operand2.length()) && operand1.length() < size){
            return true;
        }
        else if((operand1.length() < operand2.length())&& operand2.length() < size){
            return false;
        }
        else{
            for(int i = size; i>=0;i--){
                if(operand1.bitset.get(i) == true && operand2.bitset.get(i) == false){
                    return true;
                }
                else if(operand1.bitset.get(i) == false && operand2.bitset.get(i) == true){
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean uge(bitVector operand1,bitVector operand2,int size){
        if(ugt(operand1,operand2,size) == true || eq(operand1,operand2,size) == true){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean ult(bitVector operand1,bitVector operand2,int size){
        if(uge(operand1,operand2,size) == true){
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean ule(bitVector operand1,bitVector operand2,int size){
        if(ugt(operand1,operand2,size) == true){
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean sgt(bitVector operand1,bitVector operand2,int size){
        if(operand1.bitset.get(size) == operand2.bitset.get(size)){
            return ugt(operand1,operand2,size);
        }
        else{
            if(operand1.bitset.get(size) == true){
                return false;
            }
            else{
                return true;
            }
        }
    }

    public static boolean sge(bitVector operand1,bitVector operand2,int size){
        if(operand1.bitset.get(size) == operand2.bitset.get(size)){
            return uge(operand1,operand2,size);
        }
        else{
            if(operand1.bitset.get(size) == true){
                return false;
            }
            else{
                return true;
            }
        }
    }

    public static boolean slt(bitVector operand1,bitVector operand2,int size){
        if(operand1.bitset.get(size) == operand2.bitset.get(size)){
            return ult(operand1,operand2,size);
        }
        else{
            if(operand1.bitset.get(size) == true){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public static boolean sle(bitVector operand1,bitVector operand2,int size){
        if(operand1.bitset.get(size) == operand2.bitset.get(size)){
            return ule(operand1,operand2,size);
        }
        else{
            if(operand1.bitset.get(size) == true){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public static bitVector add(bitVector operand1,bitVector operand2){
        bitVector result = new bitVector(operand1.bitset.size());
        int carry = 0;
        int a,b,sum;
        int length = Math.max(operand1.length(),operand2.length());
        for(int i = 0; i < length;i++){
            a = operand1.bitset.get(i)?1:0;
            b = operand2.bitset.get(i)?1:0;
            sum = a+b+carry;
            switch(sum){
                case 0:
                    carry = 0;
                    break;
                case 1:
                    carry = 0;
                    result.bitset.set(i);
                    break;
                case 2:
                    carry = 1;
                    break;
                case 3:
                    carry = 1;
                    result.bitset.set(i);
                    break; 
            }
        }
        if(length < operand1.bitset.size()){
            if(carry == 1){ 
                result.bitset.set(length);
            }
        }
        return result;
    }

    public static bitVector sub(bitVector operand1,bitVector operand2){
        bitVector intermediate = add(operand1,not(operand2));
        bitVector carry = new bitVector(operand1.bitset.size());
        carry.setData(1);
        return add(intermediate,carry);
    }

    public static bitVector neg(bitVector operand1){
        bitVector intermediate = not(operand1);
        bitVector carry = new bitVector(operand1.bitset.size());
        carry.setData(1);
        return add(intermediate,carry);
    }

    public static bitVector mul(bitVector operand1,bitVector operand2,int size){
        bitVector result = new bitVector(operand1.bitset.size());
        for(int i = 0; i < operand2.length();i++){
            if(operand2.bitset.get(i)==true){
                result = add(result,shl(operand1,i,size));
            }
        }
        return result;
    }

    public static bitVector urem(bitVector operand1,bitVector operand2,int size){
        BigInteger o1 = operand1.unsignValue(size);
        BigInteger o2 = operand2.unsignValue(size);
        BigInteger result_int = o1.remainder(o2);
        bitVector result = new bitVector(operand1.bitset.size());
        result.setData(result_int.longValue());
        return result;
    }

    public static bitVector udiv(bitVector operand1,bitVector operand2,int size){
        BigInteger o1 = operand1.unsignValue(size);
        BigInteger o2 = operand2.unsignValue(size);
        BigInteger result_int = o1.divide(o2);
        bitVector result = new bitVector(operand1.bitset.size());
        result.setData(result_int.longValue());
        return result;
    }

    public static bitVector srem(bitVector operand1,bitVector operand2,int size){
        BigInteger o1 = operand1.signValue(size);
        BigInteger o2 = operand2.signValue(size);
        BigInteger result_int = o1.remainder(o2);
        bitVector result = new bitVector(operand1.bitset.size());
        result.setData(result_int.longValue());
        return result;
    }

    public static bitVector sdiv(bitVector operand1,bitVector operand2,int size){
        BigInteger o1 = operand1.signValue(size);
        BigInteger o2 = operand2.signValue(size);
        BigInteger result_int = o1.divide(o2);
        bitVector result = new bitVector(operand1.bitset.size());
        result.setData(result_int.longValue());
        return result;
    }

    public static bitVector smod(bitVector operand1,bitVector operand2,int size){
        BigInteger o1 = operand1.signValue(size);
        BigInteger o2 = operand2.signValue(size);
        BigInteger result_int = o1.mod(o2);
        bitVector result = new bitVector(operand1.bitset.size());
        result.setData(result_int.longValue());
        return result;
    }

    public static bitVector shl(bitVector operand1,int num,int size){
        bitVector result = new bitVector(operand1.bitset.size());
        for(int i = 0; i < operand1.bitset.length();i++){
            if(operand1.bitset.get(i) == true && (i+num) < size){
                result.bitset.set(i+num);
            }
        }
        return result;
    }

    public static bitVector lshr(bitVector operand1,int num,int size){
        bitVector result = new bitVector(operand1.bitset.size());
        for(int i = 0; i < operand1.bitset.length() - num;i++){
            if(operand1.bitset.get(i+num) == true){
                result.bitset.set(i);
            }
        }
        result.bitset.clear(size-num,size);
        return result;
    }
    public static bitVector ashr(bitVector operand1,int num,int size){
        bitVector result = new bitVector(operand1.bitset.size());
        for(int i = 0; i < operand1.bitset.length() - num;i++){
            if(operand1.bitset.get(i+num) == true){
                result.bitset.set(i);
            }
        }
        if(operand1.bitset.get(size-1) == true){
            result.bitset.set(size-num,size);
        }
        else{
            result.bitset.clear(size-num,size);
        }
        return result;
    }

    public static bitVector ite(bitVector operand1,bitVector operand2,bitVector operand3,int size){
        bitVector result = new bitVector(operand2.bitset.size());
        if(operand1.signValue(size).longValue() == 1){
            result = copy(operand2);
        }
        else{
            result = copy(operand3);
        }
        return result;
    }
}
