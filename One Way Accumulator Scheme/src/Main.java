import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BigInteger randoma = new BigInteger(20000, new Random());
//        System.out.println(randoma);

        LargeInteger a = new LargeInteger(randoma.toString(2));
//        System.out.println("a: " + a);

        BigInteger randomb = new BigInteger(20000, new Random());
//        System.out.println(randomb);

        LargeInteger b = new LargeInteger(randomb.toString(2));
//        System.out.println("b: " + b);

        long currentTime = System.nanoTime();
        LargeInteger mul = a.KAmultiply(b);
        System.out.println("KA MULT     : " + (System.nanoTime() - currentTime));

        currentTime = System.nanoTime();
        LargeInteger mulh = a.KAmultiply(b, a.head, a.size / 2, b.head, b.size / 2);
        System.out.println("KA MULT half: " + (System.nanoTime() - currentTime));

        currentTime = System.nanoTime();
        LargeInteger m = a.CMmultiply(b);
        System.out.println("CM MULT:      " + (System.nanoTime() - currentTime));

        currentTime = System.nanoTime();
        m = a.CMmultiply(b, a.head, a.size / 2, b.head, b.size / 2);
        System.out.println("CM MULT half: " + (System.nanoTime() - currentTime));

        currentTime = System.nanoTime();
        BigInteger ma = randoma.multiply(randomb);
        System.out.println("BIG INT MULT: " + (System.nanoTime() - currentTime));

        currentTime = System.nanoTime();
        LargeInteger temp = a.add(b);
        System.out.println("add: " + (System.nanoTime() - currentTime));

        currentTime = System.nanoTime();
        temp = a.shiftLeftbyn(1000);
        System.out.println("shift: " + (System.nanoTime() - currentTime));

        int[] adwwqd = new int[10000];


//        BigInteger decmul = new BigInteger(mul.toString(), 2);
//        System.out.println("a*b bin: " + mul);
//        System.out.println("a*b dec: " + decmul.toString());
//        System.out.println("a-b: " + a.subtract(b));
//        System.out.println("a+b: " + a.add(b));
//        System.out.println();


//        System.out.println(bc.binaryToDecimal(mul));
//        Bons aa = new Bons(a);
//        Bons bb = new Bons(b);
//        Bons mm = new Bons(mul);
////        Bons b = new Bons(a);
//        System.out.println(aa);
//        System.out.println(bb);
//        System.out.println(mm);
//        currentTime = System.nanoTime();
//        LargeInteger mmmmm = aa.BOCM(aa, bb);
//        System.out.println("BONS TIME: " + (System.nanoTime() - currentTime));
//        System.out.println();
////        System.out.println("BONS result: " + mmmmm);

        currentTime = System.nanoTime();
        LargeInteger aaaaa = a.naive_multiply(b);
        System.out.println("NAIVE MULTIPLY TIME: " + (System.nanoTime() - currentTime));
//        System.out.println(aaaaa);
    }
}
