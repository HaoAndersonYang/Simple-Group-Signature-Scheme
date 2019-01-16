import java.math.BigInteger;

public class Member {
    private BigInteger id;
    private BigInteger privateAcc;

    public void showIdentity() {
        Group group = Group.getInstance();
        System.out.println(privateAcc.modPow(id, group.getN()));
        System.out.println(group.getAccumulator());
    }

    public Member(BigInteger id, BigInteger privateAcc) {
        this.id = id;
        this.privateAcc = privateAcc;
    }

    public BigInteger getId() {
        return id;
    }

    public void update(BigInteger newID) {
        Group group = Group.getInstance();
        privateAcc = privateAcc.modPow(newID, group.getN());
    }

    public void setPrivateAcc(BigInteger privateAcc) {
        this.privateAcc = privateAcc;
    }
}
