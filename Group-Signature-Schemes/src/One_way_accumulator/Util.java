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
            BigInteger ri = new BigInteger((int) (group.eps * group.lambda), new Random());
            BigInteger ti = g.modPow(group.a.modPow(ri, group.n), group.n);
            c.append(converttoBinaryString(ti.toByteArray()));
            si[i] = ri;
            if (c.charAt(i) != '0') {
                si[i] = si[i].subtract(x);
            }
        }
        SKLOGLOGTuple result = new SKLOGLOGTuple();
        System.out.println(c);
        result.c = hash(c.toString());
        result.si = si;
        return result;
    }


    public static boolean SKLOGLOGtest(SKROOTLOGTuple tuple, String m, BigInteger y, BigInteger g) {
        StringBuilder c = new StringBuilder(hash(m));
        Group group = Group.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(group.a.toString(2));
        for (int i = 1; i <= group.l; i++) {
            BigInteger ti;
            if (c.charAt(i) == '0') {
                ti = g.modPow(group.a.modPow(tuple.si[i], group.n), group.n);
            } else {
                ti = y.modPow(group.a.modPow(tuple.si[i], group.n), group.n);
            }
            c.append(converttoBinaryString(ti.toByteArray()));
        }
        System.out.println(c);
//        System.out.println(hash(c.toString()));
//        System.out.println(tuple.c);
        return false;
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

    public static SKROOTLOGTuple SKROOTLOG(String m, BigInteger y, BigInteger x, BigInteger g) {
        StringBuilder c = new StringBuilder(hash(m));
        Group group = Group.getInstance();
        c.append(y.toString(2));
        c.append(g.toString(2));
        c.append(group.e.toString(2));
        BigInteger[] si = new BigInteger[group.l + 1];
        for (int i = 1; i <= group.l; i++) {
            BigInteger ri = new BigInteger((int) (group.eps * group.lambda), new Random());
            BigInteger ti = g.modPow(group.e.modPow(ri, group.n), group.n);
            c.append(converttoBinaryString(ti.toByteArray()));
            si[i] = ri;
            if (c.charAt(i) != '0') {
                si[i] = si[i].divide(x).mod(group.n);
            }
        }
        SKROOTLOGTuple result = new SKROOTLOGTuple();
        result.c = hash(c.toString());
        result.si = si;
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
            if (c.charAt(i) == '0') {
                ti = g.modPow(tuple.si[i].modPow(group.e, group.n), group.n);
            } else {
                ti = y.modPow(tuple.si[i].modPow(group.e, group.n), group.n);
            }
            c.append(converttoBinaryString(ti.toByteArray()));
        }
        System.out.println(hash(c.toString()));
        System.out.println(tuple.c);
        return false;
    }

//    public static byte[] concatenate(byte[] a, byte[] b) {
//        byte[] c = new byte[a.length + b.length];
//        System.arraycopy(a, 0, c, 0, a.length);
//        System.arraycopy(b, 0, c, a.length, b.length);
//        return c;
//    }
}
