package One_way_accumulator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Util {
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
        Group group = Group.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(group.a.toString(2));
        BigInteger[] si = new BigInteger[group.l + 1];
        for (int i = 1; i <= group.l; i++) {
            si[i] = new BigInteger((int) (group.eps * group.lambda), new Random());
            BigInteger ti = group.cyclicPow(g, group.a.modPow(si[i], group.n));
//            BigInteger test = group.cyclicPow(lly, group.a.modPow(si[i].subtract(x), group.cyclicmod));
//            System.out.println(test.compareTo(ti));
            c.append(ti.toString(2));
        }
        SKLOGLOGTuple result = new SKLOGLOGTuple();
        result.c = hash(c.toString());
        for (int i = 1; i <= group.l; i++) {
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
        Group group = Group.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(group.a.toString(2));
        for (int i = 1; i <= group.l; i++) {
            BigInteger ti;
            if (tuple.c.charAt(i) == '0') {
                ti = group.cyclicPow(g, group.a.modPow(tuple.si[i], group.cyclicmod));
            } else {
                ti = group.cyclicPow(y, group.a.modPow(tuple.si[i], group.cyclicmod));
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
        Group group = Group.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(group.e.toString(2));
        BigInteger vinv = v.modInverse(group.n);
        BigInteger[] si = new BigInteger[group.l + 1];
        for (int i = 1; i <= group.l; i++) {
            si[i] = new BigInteger(group.n.bitLength(), new Random());
            BigInteger ti = group.cyclicPow(g, si[i].modPow(group.e, group.cyclicmod));
//            System.out.println(si[i]);
//            BigInteger test = group.cyclicPow(lly, group.a.modPow(si[i].subtract(x), group.cyclicmod));
//            System.out.println(test.compareTo(ti));
            c.append(ti.toString(2));
        }
        SKROOTLOGTuple result = new SKROOTLOGTuple();
        result.c = hash(c.toString());
        for (int i = 1; i <= group.l; i++) {
            if (result.c.charAt(i) != '0') {
//                System.out.println();
//                System.out.println(group.cyclicPow(g, si[i].modPow(group.e, group.cyclicmod)));
                si[i] = (si[i].multiply(vinv)).mod(group.n);
//                System.out.println(group.cyclicPow(y, si[i].modPow(group.e, group.cyclicmod)));
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
        Group group = Group.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(group.e.toString(2));
        for (int i = 1; i <= group.l; i++) {
            BigInteger ti;
            if (tuple.c.charAt(i) == '0') {
                ti = group.cyclicPow(g, tuple.si[i].modPow(group.e, group.n));
            } else {
                ti = group.cyclicPow(y, tuple.si[i].modPow(group.e, group.n));
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
