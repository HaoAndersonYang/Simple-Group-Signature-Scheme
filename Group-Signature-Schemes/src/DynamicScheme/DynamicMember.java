package DynamicScheme;

import java.math.BigInteger;
import java.util.Random;

public class DynamicMember {
    private BigInteger x;
    private BigInteger y;
    private BigInteger z;
    public BigInteger v;

    public DynamicMember(BigInteger x) {
        this.x = x;
//        System.out.println("Secret key (x): \n" + x);
        DynamicGroup dynamicGroup = DynamicGroup.getInstance();
        y = dynamicGroup.a.modPow(x, dynamicGroup.n);
//        System.out.println("Constant value based on secret key (y): \n" + y);
        z = dynamicGroup.cyclicPow(dynamicGroup.g, y);
//        System.out.println("Membership key (z): \n" + z);
        v = dynamicGroup.getCertificate(y);
//        System.out.println("Certificate (v): \n" + v);
//        System.out.println("Y+1 " + y.add(BigInteger.ONE));
//        System.out.println("x^e " + x.modPow(dynamicGroup.e, dynamicGroup.n));
//        System.out.println("v^e " + v.modPow(dynamicGroup.e, dynamicGroup.n));
//        System.out.println("Y   " + y);
//        System.out.println("a^x " + dynamicGroup.a.modPow(x, dynamicGroup.n));
    }

    public MessageSignature signMessage(String message) {
        DynamicGroup dynamicGroup = DynamicGroup.getInstance();
        BigInteger r = new BigInteger(dynamicGroup.lambda, new Random());
        BigInteger ghat = dynamicGroup.cyclicPow(dynamicGroup.g, r);
        BigInteger zhat = dynamicGroup.cyclicPow(ghat, y);
        Util.SKLOGLOGTuple LoglogResults = computeSkLogLog(message, zhat, ghat);
        Util.SKROOTLOGTuple RootlogResults = computeSKROOTLog(message, (zhat.multiply(ghat)).mod(dynamicGroup.cyclicbase), ghat);
        return new MessageSignature(message, LoglogResults, RootlogResults, ghat, zhat);
    }

    public class MessageSignature {
        public String message;
        public Util.SKLOGLOGTuple LoglogResults;
        public Util.SKROOTLOGTuple RootlogResults;
        public BigInteger lly;
        public BigInteger llg;
        public BigInteger rly;
        public BigInteger rlg;


        public MessageSignature(String message, Util.SKLOGLOGTuple loglogResults, Util.SKROOTLOGTuple rootlogResults, BigInteger ghat, BigInteger zhat) {
            this.message = message;
            LoglogResults = loglogResults;
            RootlogResults = rootlogResults;
            this.lly = zhat;
            this.llg = ghat;
            DynamicGroup dynamicGroup = DynamicGroup.getInstance();
            this.rly = (zhat.multiply(ghat)).mod(dynamicGroup.cyclicbase);
            rlg = llg;
        }

    }

    public Util.SKLOGLOGTuple computeSkLogLog(String message, BigInteger y, BigInteger g) {
        return Util.SKLOGLOG(message, y, x, g);
    }

    public Util.SKROOTLOGTuple computeSKROOTLog(String message, BigInteger y, BigInteger g) {
        return Util.SKROOTLOG(message, y, v, g);
    }

}
