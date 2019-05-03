package OWASimpleScheme;

import java.math.BigInteger;

public class OWAMember {
    private BigInteger id;
    private BigInteger privateAcc;

    public void showIdentity() {
        OWAGroup OWAGroup = OWASimpleScheme.OWAGroup.getInstance();
//        System.out.println(privateAcc.modPow(id, OWAGroup.getN()));
//        System.out.println(OWAGroup.getAccumulator());
    }

    public OWAMember(BigInteger id, BigInteger privateAcc) {
        this.id = id;
        this.privateAcc = privateAcc;
    }

    public BigInteger getId() {
        return id;
    }

    public void update(BigInteger newID) {
        OWAGroup owaGroup = OWASimpleScheme.OWAGroup.getInstance();
        privateAcc = privateAcc.modPow(newID, owaGroup.getN());
    }

    public void setPrivateAcc(BigInteger privateAcc) {
        this.privateAcc = privateAcc;
    }
}