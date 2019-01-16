package One_way_accumulator;

import java.math.BigInteger;

public class Member {
    private BigInteger x;
    private BigInteger y;
    private BigInteger z;
    private BigInteger v;

    public void showIdentity() {
        Group group = Group.getInstance();
    }

    public Member(BigInteger x) {
        this.x = x;
        Group group = Group.getInstance();
        y = group.a.modPow(x, group.n);
        System.out.println("y: " + y);
        z = group.g.modPow(y, group.n);
        System.out.println("z: " + z);
        v = group.getCertificate(y);
        System.out.println(v);
    }

}