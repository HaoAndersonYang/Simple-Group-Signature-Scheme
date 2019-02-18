package One_way_accumulator;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Group group = Group.getInstance();
        BigInteger a = new BigInteger(group.lambda, new Random());

        BigInteger b = new BigInteger(group.lambda, new Random());

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
        System.out.println(a.shiftLeft(1));
        System.out.println(System.nanoTime() - time);
        time = System.nanoTime();
        System.out.println(a.multiply(BigInteger.ONE.add(BigInteger.ONE)));
        System.out.println(System.nanoTime() - time);

    }
}
