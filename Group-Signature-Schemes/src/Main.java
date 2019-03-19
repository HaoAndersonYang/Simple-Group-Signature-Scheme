import DynamicScheme.DynamicGroup;
import DynamicScheme.DynamicMember;
import DynamicScheme.Util;
import OWASimpleScheme.OWAGroup;
import OWASimpleScheme.OWAMember;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        DynamicGroup dynamicGroup = DynamicGroup.getInstance();
        OWAGroup owaGroup = OWAGroup.getInstance();

        BigInteger a = new BigInteger(dynamicGroup.lambda, new Random());

        BigInteger b = new BigInteger(dynamicGroup.lambda, new Random());

        int num_of_members = 600;
        long time_dynamic = 0;
        for (int i = 0; i < num_of_members; i++) {
            BigInteger tmp = new BigInteger(dynamicGroup.lambda, new Random());
            long t = System.nanoTime();
            DynamicMember amem = new DynamicMember(tmp);
            amem.signMessage("message");
            time_dynamic += System.nanoTime() - t;
        }
        System.out.println("Dynamic Scheme: " + num_of_members + " users: " + time_dynamic);

        long time_owa = 0;
        for (int i = 0; i < num_of_members; i++) {
            BigInteger tmp = new BigInteger(dynamicGroup.lambda, new Random());
            OWAMember member = new OWAMember(tmp, BigInteger.ONE);
            long t = System.nanoTime();
            owaGroup.addMember(member);
            member.showIdentity();
            time_owa += System.nanoTime() - t;
        }
        System.out.println("Simple Scheme: " + num_of_members + " users:  " + time_owa);
//        DynamicMember.MessageSignature signature = amem.signMessage("HELLO");
//        amem.SkLogLogTest("HELLO");
//        System.out.println();
//        System.out.println(Util.SKLOGLOGtest(signature.LoglogResults, signature.message, signature.lly, signature.llg));
//        System.out.println();
//        System.out.println(Util.SKROOTLOGtest(signature.RootlogResults, signature.message, signature.rly, signature.rlg));
//        System.out.println();


    }
}
