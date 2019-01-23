package One_way_accumulator;

import java.math.BigInteger;
import java.util.Random;

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
        System.out.println("Secret key (x): \n" + x);
        Group group = Group.getInstance();
        y = group.a.modPow(x, group.n);
        System.out.println("Constant value based on secret key (y): \n" + y);
        z = group.g.modPow(y, group.n);
        System.out.println("Membership key (z): \n" + z);
        v = group.getCertificate(y);
        System.out.println("Certificate (v): \n" + v);
    }

    public void signMessage(String message) {
        Group group = Group.getInstance();
        BigInteger r = new BigInteger(group.lambda, new Random());
        BigInteger ghat = group.g.modPow(r, group.n);
        BigInteger zhat = ghat.modPow(y, group.n);
        Util.SKLOGLOGTuple LoglogResults = computeSkLogLog(message, zhat, ghat);
        Util.SKROOTLOGTuple RootlogResults = computeSKROOTLog(message, zhat.multiply(ghat).mod(group.n), ghat);
    }

    public Util.SKLOGLOGTuple computeSkLogLog(String message, BigInteger y, BigInteger g) {
        return Util.SKLOGLOG(message, y, x, g);
    }

    public Util.SKROOTLOGTuple computeSKROOTLog(String message, BigInteger y, BigInteger g) {
        return Util.SKROOTLOG(message, y, x, g);
    }

}
