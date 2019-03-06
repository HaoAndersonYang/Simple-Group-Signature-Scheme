package One_way_accumulator;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        LargeInteger a = new LargeInteger(831028043);
        LargeInteger b = new LargeInteger(489159823);
        long t = System.nanoTime();
        LargeInteger c = a.naive_multiply(b);
        long newt = System.nanoTime();
        System.out.println(newt - t);
        BigInteger x = new BigInteger("831028043");
        BigInteger y = new BigInteger("489159823");
        t = System.nanoTime();
        x.multiply(y);
        newt = System.nanoTime();
        System.out.println(newt - t);
        System.out.println("a[0]: " + a.mag[0]);
        System.out.println("a[1]: " + a.mag[1]);
//        System.out.println("a[2]: " + a.mag[2]);
        for (int i = 0; i < c.size; i++) {
            System.out.println("c[" + i + "]: " + c.mag[i]);
        }
    }
}
