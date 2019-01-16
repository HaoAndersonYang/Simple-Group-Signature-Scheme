package One_way_accumulator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Util {
    public static BigInteger hash(BigInteger input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input.toByteArray());
        BigInteger hashed = new BigInteger(result).abs();
        return hashed;
    }

    public static BigInteger hash(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input.getBytes());
        BigInteger hashed = new BigInteger(result).abs();
        return hashed;
    }

    public static BigInteger hash(byte[] input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input);
        BigInteger hashed = new BigInteger(result).abs();
        return hashed;
    }

    public static SKLOGLOGTuple SKLOGLOG(String m, BigInteger y, BigInteger x) {
        BigInteger message = hash(m);
        Group group = Group.getInstance();
        byte[] temp = concatenate(message.toByteArray(), y.toByteArray());
        temp = concatenate(temp, group.g.toByteArray());
        temp = concatenate(temp, group.a.toByteArray());
        StringBuilder c = new StringBuilder(converttoBinaryString(temp));
        BigInteger[] si = new BigInteger[group.l];
        for (int i = 0; i < group.l; i++) {
            BigInteger ri = new BigInteger((int) (group.eps * group.lambda), new Random());
            BigInteger ti = group.g.modPow(group.a.modPow(ri, group.n), group.n);
            c.append(converttoBinaryString(ti.toByteArray()));
            si[i] = ri;
            if (c.charAt(i) != '0') {
                si[i] = si[i].subtract(x);
            }
        }
        SKLOGLOGTuple result = new SKLOGLOGTuple();
        result.c = converttoBinaryString(hash(c.toString()).toByteArray());
        result.si = si;
        return result;
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
        return sb.toString();
    }

    public static class SKLOGLOGTuple {
        String c;
        BigInteger[] si;
    }

    public static SKROOTLOGTuple SKROOTLOG(String m, BigInteger y, BigInteger x) {
        BigInteger message = hash(m);
        Group group = Group.getInstance();
        byte[] temp = concatenate(message.toByteArray(), y.toByteArray());
        temp = concatenate(temp, group.g.toByteArray());
        temp = concatenate(temp, group.e.toByteArray());
        StringBuilder c = new StringBuilder(converttoBinaryString(temp));
        BigInteger[] si = new BigInteger[group.l];
        for (int i = 0; i < group.l; i++) {
            BigInteger ri = new BigInteger((int) (group.eps * group.lambda), new Random());
            BigInteger ti = group.g.modPow(group.e.modPow(ri, group.n), group.n);
            c.append(converttoBinaryString(ti.toByteArray()));
            si[i] = ri;
            if (c.charAt(i) != '0') {
                si[i] = si[i].divide(x).mod(group.n);
            }
        }
        SKROOTLOGTuple result = new SKROOTLOGTuple();
        result.c = converttoBinaryString(hash(c.toString()).toByteArray());
        result.si = si;
        return result;
    }

    public static class SKROOTLOGTuple {
        String c;
        BigInteger[] si;
    }


    public static byte[] concatenate(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
