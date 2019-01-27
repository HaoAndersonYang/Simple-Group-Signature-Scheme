package One_way_accumulator;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Group {
    private static Group group;
    public BigInteger n;
    public BigInteger e;
    private BigInteger d;
    public BigInteger lcm;
    public BigInteger a;
    public BigInteger g;
    public BigInteger cyclicbase;
    public BigInteger cyclicmod;
    public BigInteger cyclicK;
    public final int l = 64;
    public final int k = 160;
    public final double eps = 1.5;
    public final int lambda = 170;

    private final int bitlen = 100;
    private BigInteger accumulator = new BigInteger("2");//Initial value is base 2

    private ArrayList<Member> members = new ArrayList<>();

    private Group() {
        BigInteger p = BigInteger.probablePrime(bitlen, new Random());
        BigInteger q = BigInteger.probablePrime(bitlen, new Random());
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        n = p.multiply(q);
        System.out.println("n: " + n);
        BigInteger mul = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger gcd = (p.subtract(BigInteger.ONE)).gcd(q.subtract(BigInteger.ONE));
        lcm = mul.divide(gcd);
        System.out.println("lcm: " + lcm);
        e = BigInteger.probablePrime(bitlen, new Random());
        while (e.compareTo(lcm) >= 0 || lcm.remainder(e).equals(BigInteger.ZERO)) {
            e = BigInteger.probablePrime(bitlen, new Random());
        }
        d = e.modInverse(lcm);
//        g = BigInteger.probablePrime(bitlen, new Random());
//        while (g.compareTo(n) >= 0 || n.remainder(g).equals(BigInteger.ZERO)) {
//            g = BigInteger.probablePrime(bitlen, new Random());
//        }
        //TODO: CHECK THIS
        cyclicK = BigInteger.ONE;
        while (!(cyclicK.multiply(n).add(BigInteger.ONE)).isProbablePrime(20000)) {
            cyclicK = cyclicK.add(BigInteger.ONE);
        }
        cyclicbase = cyclicK.multiply(n).add(BigInteger.ONE);
        cyclicmod = cyclicbase.subtract(BigInteger.ONE);
        g = cyclicPow(p, cyclicK);
        System.out.println("cyclicmod " + cyclicmod);
        System.out.println("cyclicbase " + cyclicbase);
        System.out.println("g: " + g);
//        System.out.println(cyclicPow(g,n));
//        while (index.compareTo(n) <= 0) {
//            System.out.println(cyclicPow(g, n));
//            index = index.add(BigInteger.ONE);
//        }
        a = BigInteger.probablePrime(bitlen, new Random());
        System.out.println("a: " + a);
    }

    public BigInteger cyclicPow(BigInteger base, BigInteger pow) {
        return base.modPow(pow, cyclicbase);
    }

    public static Group getInstance() {
        if (group == null) {
            group = new Group();
        }
        return group;
    }

    public BigInteger getCertificate(BigInteger y) {
        return (y.add(BigInteger.ONE)).modPow(d, n);
    }
}
