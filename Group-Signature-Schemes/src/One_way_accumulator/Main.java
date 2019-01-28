package One_way_accumulator;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Group group = Group.getInstance();
        BigInteger a = new BigInteger(group.lambda, new Random());

        BigInteger b = new BigInteger(group.lambda, new Random());
        Member amem = new Member(a);
        Member.MessageSignature signature = amem.signMessage("HELLO");
//        amem.SkLogLogTest("HELLO");
//        System.out.println(Util.SKLOGLOGtest(signature.LoglogResults, signature.message, signature.lly, signature.llg));
        Util.SKROOTLOGtest(signature.RootlogResults, signature.message, signature.rly, signature.rlg);

    }
}
