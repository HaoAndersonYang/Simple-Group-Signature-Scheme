package One_way_accumulator;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        LargeInteger a = new LargeInteger(237246);
        LargeInteger b = new LargeInteger(2);
        long t = System.nanoTime();
        LargeInteger c = a.naive_multiply(b);
        long newt = System.nanoTime();
        System.out.println(newt - t);
        BigInteger x = new BigInteger("200000");
        BigInteger y = new BigInteger("20");
        t = System.nanoTime();
        x.add(y);
        newt = System.nanoTime();
        System.out.println(newt - t);
        System.out.println("a[0]: " + a.mag[0]);
        System.out.println("a[1]: " + a.mag[1]);
        System.out.println("c[0]: " + c.mag[0]);
        System.out.println("c[1]: " + c.mag[1]);
        System.out.println("c[2]: " + c.mag[2]);
    }
}
