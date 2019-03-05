import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BigInteger a = new BigInteger(170, new Random());

        BigInteger b = new BigInteger(170, new Random());

////        GROUP FUNCTION TEST
//        Member amem = new Member(BigInteger.ONE);
//        Member bmem = new Member(BigInteger.ZERO);
//        Member.MessageSignature signature = amem.signMessage("HELLO");
////        amem.SkLogLogTest("HELLO");
//        System.out.println();
//        System.out.println(Util.SKLOGLOGtest(signature.LoglogResults, signature.message, signature.lly, signature.llg));
//        System.out.println();
//        System.out.println(Util.SKROOTLOGtest(signature.RootlogResults, signature.message, signature.rly, signature.rlg));
//        System.out.println();


        //Time testing:
        long time = System.nanoTime();
        a.shiftLeft(1);
        time = System.nanoTime() - time;
        System.out.println(time);

        BigInteger two = BigInteger.valueOf(2);
        time = System.nanoTime();
        a.multiply(two);
        time = System.nanoTime() - time;
        System.out.println(time);

    }
}
