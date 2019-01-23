package One_way_accumulator;

import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Group group = Group.getInstance();
        BigInteger a = new BigInteger(group.lambda, new Random());

        BigInteger b = new BigInteger(group.lambda, new Random());
        Member amem = new Member(a);
        amem.signMessage("HELLO");
//        amem.SkLogLogTest("HELLO");
    }
}
