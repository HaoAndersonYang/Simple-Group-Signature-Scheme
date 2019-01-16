import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Group {
    private static Group group;
    private BigInteger N;
    private BigInteger accumulator = new BigInteger("2");//Initial value is base 2

    private ArrayList<Member> members = new ArrayList<>();

    private Group() {
        BigInteger randomp = BigInteger.probablePrime(100, new Random());
        BigInteger randomq = BigInteger.probablePrime(100, new Random());
        N = randomp.multiply(randomq);

    }

    public static Group getInstance() {
        if (group == null) {
            group = new Group();
        }
        return group;
    }

    public void setN(BigInteger n) {
        N = n;
    }

    public BigInteger getAccumulator() {
        return accumulator;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public BigInteger getN() {
        return N;
    }

    public Member addMember(BigInteger id) {
        Member newmem = new Member(id, accumulator);
        addMember(newmem);
        return newmem;
    }

    public void addMember(Member newmem) {
        newmem.setPrivateAcc(accumulator);
        accumulator = accumulator.modPow(newmem.getId(), N);
        for (Member member : members) {
            member.update(newmem.getId());
        }
        members.add(newmem);
    }
}
