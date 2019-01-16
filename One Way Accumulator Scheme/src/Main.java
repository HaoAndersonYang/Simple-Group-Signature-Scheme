import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BigInteger a = new BigInteger(200, new Random());

        BigInteger b = new BigInteger(200, new Random());
        Group group = Group.getInstance();
        Member amem = new Member(a, BigInteger.ONE);
        Member bmem = new Member(b, BigInteger.ONE);

        group.addMember(amem);
        group.addMember(bmem);
        amem.showIdentity();
        System.out.println();
        bmem.showIdentity();

    }
}
