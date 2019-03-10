package One_way_accumulator;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BigInteger x = new BigInteger(100, new Random());
        BigInteger y = new BigInteger(100, new Random());

//        BigInteger x = new BigInteger("5b3678248", 16);
//        BigInteger y = new BigInteger("2e42faed9", 16);

        String xhex = x.toString(16);
        String yhex = y.toString(16);

        System.out.println("BI x: " + xhex);
        System.out.println("BI y: " + yhex);

        LargeInteger a = new LargeInteger(xhex);
        LargeInteger b = new LargeInteger(yhex);
        System.out.println("LI x: " + a);
        System.out.println("LI y: " + b);
        long t, newt;

        t = System.nanoTime();
        x.multiply(y);
        newt = System.nanoTime();
        System.out.println("BI~100digit: " + (newt - t));

        t = System.nanoTime();
        a.naive_multiply(b);
        newt = System.nanoTime();
        System.out.println("LI~100digit: " + (newt - t));

//        t = System.nanoTime();
//        a.KA_multiply(b);
//        newt = System.nanoTime();
//        System.out.println("LI~100digit KA: " + (newt - t));


        System.out.println(x.multiply(y).toString(16));
        System.out.println(a.naive_multiply(b).toString());
//
//        System.out.println(x.add(y).toString(16));
//        System.out.println(a.add(b));
//
//
//        System.out.println(x.subtract(y).toString(16));
//        System.out.println(a.subtract(b));


//        System.out.println(a.KA_multiply(b));

//        System.out.println(a.KA_multiply(b));
//        System.out.println(a.naive_multiply(b));

//        System.out.println("a[0]: " + a.mag[0]);
//        System.out.println("a[1]: " + a.mag[1]);
//        System.out.println("a[2]: " + a.mag[2]);
//        for (int i = 0; i < c.size; i++) {
//            System.out.println("c[" + i + "]: " + c.mag[i]);
//        }
    }
}
