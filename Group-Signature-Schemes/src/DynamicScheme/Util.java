package DynamicScheme;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Util {

    private static long modPowTime = 0;
    private static long currentTime;

    public static void modpowTimeBeginCollection() {
        currentTime = System.nanoTime();
    }

    public static void modpowTimeEndCollection() {
        modPowTime += System.nanoTime() - currentTime;
    }

    public static long getModPowTime(){
        return modPowTime;
    }

    public static String hash(BigInteger input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input.toByteArray());
        return converttoBinaryString(result);
    }

    public static String hash(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input.getBytes());
        return converttoBinaryString(result);
    }

    public static String hash(byte[] input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input);
        return converttoBinaryString(result);
    }

    public static SKLOGLOGTuple SKLOGLOG(String m, BigInteger y, BigInteger x, BigInteger g) {
        StringBuilder c = new StringBuilder(hash(m));
        DynamicGroup dynamicGroup = DynamicGroup.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(dynamicGroup.a.toString(2));
        BigInteger[] si = new BigInteger[dynamicGroup.l + 1];
        for (int i = 1; i <= dynamicGroup.l; i++) {
            si[i] = new BigInteger((int) (dynamicGroup.eps * dynamicGroup.lambda), new Random());
            modpowTimeBeginCollection();
            BigInteger ti = dynamicGroup.cyclicPow(g, dynamicGroup.a.modPow(si[i], dynamicGroup.n));
            modpowTimeEndCollection();
//            BigInteger test = dynamicGroup.cyclicPow(lly, dynamicGroup.a.modPow(si[i].subtract(x), dynamicGroup.cyclicmod));
//            System.out.println(test.compareTo(ti));
            c.append(ti.toString(2));
        }
        SKLOGLOGTuple result = new SKLOGLOGTuple();
        result.c = hash(c.toString());
        for (int i = 1; i <= dynamicGroup.l; i++) {
            if (result.c.charAt(i) != '0') {
                si[i] = si[i].subtract(x);
            }
        }
//        System.out.println(c);
        result.si = si;
        return result;
    }


    public static boolean SKLOGLOGtest(SKLOGLOGTuple tuple, String m, BigInteger y, BigInteger g) {
        StringBuilder c = new StringBuilder(hash(m));
        DynamicGroup dynamicGroup = DynamicGroup.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(dynamicGroup.a.toString(2));
        for (int i = 1; i <= dynamicGroup.l; i++) {
            BigInteger ti;
            if (tuple.c.charAt(i) == '0') {
                modpowTimeBeginCollection();
                ti = dynamicGroup.cyclicPow(g, dynamicGroup.a.modPow(tuple.si[i], dynamicGroup.cyclicmod));
                modpowTimeEndCollection();
            } else {
                modpowTimeBeginCollection();
                ti = dynamicGroup.cyclicPow(y, dynamicGroup.a.modPow(tuple.si[i], dynamicGroup.cyclicmod));
                modpowTimeEndCollection();
            }
            c.append(ti.toString(2));
        }
//        System.out.println(c);
        String hashed = hash(c.toString());
        System.out.println("SKLOGLOGTEST:     " + hashed);
        System.out.println("SKLOGLOGOriginal: " + tuple.c);
        return hashed.compareTo(tuple.c) == 0;
    }


    public static String converttoBinaryString(byte[] input) {
        StringBuilder sb = new StringBuilder();
        for (byte b : input) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                sb.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        int i = 0;
        while (sb.charAt(i) == '0') {
            i++;
        }
        sb.replace(0, i, "");
        return sb.toString();
    }

    public static class SKLOGLOGTuple {
        String c;
        BigInteger[] si;
    }

    public static SKROOTLOGTuple SKROOTLOG(String m, BigInteger y, BigInteger v, BigInteger g) {
        StringBuilder c = new StringBuilder(hash(m));
        DynamicGroup dynamicGroup = DynamicGroup.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(dynamicGroup.e.toString(2));
        BigInteger vinv = v.modInverse(dynamicGroup.n);
        BigInteger[] si = new BigInteger[dynamicGroup.l + 1];
        for (int i = 1; i <= dynamicGroup.l; i++) {
            si[i] = new BigInteger(dynamicGroup.n.bitLength(), new Random());
            modpowTimeBeginCollection();
            BigInteger ti = dynamicGroup.cyclicPow(g, si[i].modPow(dynamicGroup.e, dynamicGroup.cyclicmod));
            modpowTimeEndCollection();

//            System.out.println(si[i]);
//            BigInteger test = dynamicGroup.cyclicPow(lly, dynamicGroup.a.modPow(si[i].subtract(x), dynamicGroup.cyclicmod));
//            System.out.println(test.compareTo(ti));
            c.append(ti.toString(2));
        }
        SKROOTLOGTuple result = new SKROOTLOGTuple();
        result.c = hash(c.toString());
        for (int i = 1; i <= dynamicGroup.l; i++) {
            if (result.c.charAt(i) != '0') {
//                System.out.println();
//                System.out.println(dynamicGroup.cyclicPow(g, si[i].modPow(dynamicGroup.e, dynamicGroup.cyclicmod)));
                si[i] = (si[i].multiply(vinv)).mod(dynamicGroup.n);
//                System.out.println(dynamicGroup.cyclicPow(y, si[i].modPow(dynamicGroup.e, dynamicGroup.cyclicmod)));
//                System.out.println();
            }
        }
        result.si = si;
//        System.out.println(c);
//        result.c = hash(c.toString());
//        result.si = si;
        return result;
    }

    public static class SKROOTLOGTuple {
        String c;
        BigInteger[] si;
    }


    public static boolean SKROOTLOGtest(SKROOTLOGTuple tuple, String m, BigInteger y, BigInteger g) {
        StringBuilder c = new StringBuilder(hash(m));
        DynamicGroup dynamicGroup = DynamicGroup.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(dynamicGroup.e.toString(2));
        for (int i = 1; i <= dynamicGroup.l; i++) {
            BigInteger ti;
            if (tuple.c.charAt(i) == '0') {
                modpowTimeBeginCollection();
                ti = dynamicGroup.cyclicPow(g, tuple.si[i].modPow(dynamicGroup.e, dynamicGroup.n));
                modpowTimeEndCollection();
            } else {
                modpowTimeBeginCollection();
                ti = dynamicGroup.cyclicPow(y, tuple.si[i].modPow(dynamicGroup.e, dynamicGroup.n));
                modpowTimeEndCollection();
            }
            c.append(ti.toString(2));
        }
//        System.out.println(c);
        String hashed = hash(c.toString());
        System.out.println("SKROOTLOGTEST:     " + hashed);
        System.out.println("SKROOTLOGOriginal: " + tuple.c);
        return hashed.compareTo(tuple.c) == 0;
    }

//    public static byte[] concatenate(byte[] a, byte[] b) {
//        byte[] c = new byte[a.length + b.length];
//        System.arraycopy(a, 0, c, 0, a.length);
//        System.arraycopy(b, 0, c, a.length, b.length);
//        return c;
//    }
}
