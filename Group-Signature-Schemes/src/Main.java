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

        long time_dynamic = 0;
        long time_owa = 0;
        int count = 0;
        int transaction = 10;
        while (time_owa <= time_dynamic) {
            BigInteger tmp = new BigInteger(dynamicGroup.lambda, new Random());
            count++;
            long t = System.nanoTime();
            DynamicMember amem = new DynamicMember(tmp);
            for (int i = 0; i < transaction; i++) {
                amem.signMessage("message");
            }
            time_dynamic += System.nanoTime() - t;
            OWAMember member = new OWAMember(tmp, BigInteger.ONE);
            t = System.nanoTime();
            owaGroup.addMember(member);
            for (int i = 0; i < transaction; i++) {
                member.showIdentity();
            }
            time_owa += System.nanoTime() - t;
            System.out.println(count);
        }

        System.out.println(count);


    }
}
