package One_way_accumulator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        System.out.println(input);
        System.out.println(hashed);
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
        System.out.println(input);
        System.out.println(hashed);
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
        System.out.println(input);
        System.out.println(hashed);
        return hashed;
    }

    public SKLOGLOGTuple SKLOGLOG(String m, BigInteger y, BigInteger g, BigInteger a, BigInteger x) {
        BigInteger message = hash(m);
        Group group = Group.getInstance();
        byte[] c = concatenate(message.toByteArray(), y.toByteArray());
        c = concatenate(c, g.toByteArray());
        c = concatenate(c, a.toByteArray());
        BigInteger[] si = new BigInteger[group.l];
        for (int i = 0; i < group.l; i++) {
            BigInteger ri = new BigInteger((int) (group.eps * group.lambda), new Random());
            BigInteger ti = g.modPow(a.modPow(ri, group.n), group.n);
            c = concatenate(c, ti.toByteArray());
            si[i] = ri;
            if (c[i] != 0) {
                si[i] = si[i].subtract(x);
            }
        }
        SKLOGLOGTuple result = new SKLOGLOGTuple();
        result.c = hash(c).toByteArray();
        result.si = si;
        return result;
    }

    public class SKLOGLOGTuple {
        byte[] c;
        BigInteger[] si;
    }

    public SKROOTLOGTuple SKROOTLOG(String m, BigInteger y, BigInteger g, BigInteger e, BigInteger x) {
        BigInteger message = hash(m);
        Group group = Group.getInstance();
        byte[] c = concatenate(message.toByteArray(), y.toByteArray());
        c = concatenate(c, g.toByteArray());
        c = concatenate(c, e.toByteArray());
        BigInteger[] si = new BigInteger[group.l];
        for (int i = 0; i < group.l; i++) {
            BigInteger ri = new BigInteger((int) (group.eps * group.lambda), new Random());
            BigInteger ti = g.modPow(e.modPow(ri, group.n), group.n);
            c = concatenate(c, ti.toByteArray());
            si[i] = ri;
            if (c[i] != 0) {
                si[i] = si[i].divide(x).mod(group.n);
            }
        }
        SKROOTLOGTuple result = new SKROOTLOGTuple();
        result.c = hash(c).toByteArray();
        result.si = si;
        return result;
    }

    public class SKROOTLOGTuple {
        byte[] c;
        BigInteger[] si;
    }


    public byte[] concatenate(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
