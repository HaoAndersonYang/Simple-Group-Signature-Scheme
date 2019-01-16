package One_way_accumulator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Group {
    private static Group group;
    public BigInteger n;
    public BigInteger e;
    private BigInteger d;
    private BigInteger lcm;
    public BigInteger a;
    public BigInteger g;
    public final int l = 64;
    public final int k = 160;
    public final double eps = 1.5;
    public final int lambda = 170;

    private BigInteger accumulator = new BigInteger("2");//Initial value is base 2

    private ArrayList<Member> members = new ArrayList<>();

    private Group() {
        BigInteger p = BigInteger.probablePrime(100, new Random());
        BigInteger q = BigInteger.probablePrime(100, new Random());
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        n = p.multiply(q);
        System.out.println("n: " + n);
        BigInteger mul = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger gcd = (p.subtract(BigInteger.ONE)).gcd(q.subtract(BigInteger.ONE));
        lcm = mul.divide(gcd);
        System.out.println("lcm: " + lcm);
        e = BigInteger.probablePrime(100, new Random());
        while (e.compareTo(lcm) >= 0 || lcm.remainder(e).equals(BigInteger.ZERO)) {
            e = BigInteger.probablePrime(100, new Random());
        }
        d = e.modInverse(lcm);
        g = BigInteger.probablePrime(100, new Random());
        while (g.compareTo(n) >= 0 || n.remainder(g).equals(BigInteger.ZERO)) {
            g = BigInteger.probablePrime(100, new Random());
        }
        a = BigInteger.probablePrime(100, new Random());

    }

    public static Group getInstance() {
        if (group == null) {
            group = new Group();
        }
        return group;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    public BigInteger getAccumulator() {
        return accumulator;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getCertificate(BigInteger y) {
        return (y.add(BigInteger.ONE)).modPow(BigInteger.ONE.divide(e), n);
    }
}
