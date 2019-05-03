package DynamicScheme;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class DynamicGroup {
    private static DynamicGroup dynamicGroup;
    private BigInteger q;
    private BigInteger p;
    public BigInteger n;
    public BigInteger e;
    public BigInteger d;
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

    private ArrayList<DynamicMember> dynamicMembers = new ArrayList<>();

    private DynamicGroup() {
        while (lcm == null || lcm.mod(new BigInteger("3")).equals(BigInteger.ZERO)) {
            p = BigInteger.probablePrime(bitlen, new Random());
            q = BigInteger.probablePrime(bitlen, new Random());
//            System.out.println("p: " + p);
//            System.out.println("q: " + q);
            n = p.multiply(q);
//            System.out.println("n: " + n);
            BigInteger mul = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
            BigInteger gcd = (p.subtract(BigInteger.ONE)).gcd(q.subtract(BigInteger.ONE));
            lcm = mul.divide(gcd);
//            System.out.println("lcm: " + lcm);
        }
        e = new BigInteger("3");
        d = e.modInverse(lcm);
//        llg = BigInteger.probablePrime(bitlen, new Random());
//        while (llg.compareTo(n) >= 0 || n.remainder(llg).equals(BigInteger.ZERO)) {
//            llg = BigInteger.probablePrime(bitlen, new Random());
//        }
        //TODO: CHECK THIS
        cyclicK = BigInteger.ONE;
        while (!(cyclicK.multiply(n).add(BigInteger.ONE)).isProbablePrime(20000)) {
            cyclicK = cyclicK.add(BigInteger.ONE);
        }
        cyclicbase = cyclicK.multiply(n).add(BigInteger.ONE);
        cyclicmod = cyclicbase.subtract(BigInteger.ONE);
        g = cyclicPow(p, cyclicK);
//        System.out.println("cyclicmod " + cyclicmod);
//        System.out.println("cyclicbase " + cyclicbase);
//        System.out.println("llg: " + g);
//        System.out.println(cyclicPow(llg,n));
//        while (index.compareTo(n) <= 0) {
//            System.out.println(cyclicPow(llg, n));
//            index = index.add(BigInteger.ONE);
//        }
        a = BigInteger.probablePrime(bitlen, new Random());
//        System.out.println("a: " + a);
//
//        System.out.println("e: " + e);
//        System.out.println("d: " + d);
    }

    public BigInteger cyclicPow(BigInteger base, BigInteger pow) {
        return base.modPow(pow, cyclicbase);
    }

    public static DynamicGroup getInstance() {
        if (dynamicGroup == null) {
            dynamicGroup = new DynamicGroup();
        }
        return dynamicGroup;
    }

    public BigInteger getCertificate(BigInteger y) {
        return (y.add(BigInteger.ONE)).modPow(d, n);
    }
}
