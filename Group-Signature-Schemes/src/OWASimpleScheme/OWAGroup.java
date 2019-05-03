package OWASimpleScheme;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class OWAGroup {
    private static OWAGroup OWAGroup;
    private BigInteger N;
    private BigInteger accumulator = new BigInteger("2");//Initial value is base 2

    private ArrayList<OWAMember> OWAMembers = new ArrayList<>();

    private OWAGroup() {
        BigInteger randomp = BigInteger.probablePrime(100, new Random());
        BigInteger randomq = BigInteger.probablePrime(100, new Random());
        N = randomp.multiply(randomq);
    }

    public static OWAGroup getInstance() {
        if (OWAGroup == null) {
            OWAGroup = new OWAGroup();
        }
        return OWAGroup;
    }

    public void setN(BigInteger n) {
        N = n;
    }

    public BigInteger getAccumulator() {
        return accumulator;
    }

    public ArrayList<OWAMember> getOWAMembers() {
        return OWAMembers;
    }

    public BigInteger getN() {
        return N;
    }

    public OWAMember addMember(BigInteger id) {
        OWAMember newmem = new OWAMember(id, accumulator);
        addMember(newmem);
        return newmem;
    }

    public void addMember(OWAMember newmem) {
        newmem.setPrivateAcc(accumulator);
        accumulator = accumulator.modPow(newmem.getId(), N);
        for (OWAMember owaMember : OWAMembers) {
            owaMember.update(newmem.getId());
        }
        OWAMembers.add(newmem);
    }
}