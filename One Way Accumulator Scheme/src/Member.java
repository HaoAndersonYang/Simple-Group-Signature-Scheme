public class Member {
    private LargeInteger id;
    private LargeInteger privateAcc;

    public void showIdentity() {
        System.out.println(privateAcc.modular_pow(id, Group.N).value);
        System.out.println(Group.accumulator.value);
    }

    public Member(LargeInteger id, LargeInteger privateAcc) {
        this.id = id;
        this.privateAcc = privateAcc;
    }

    public void update(LargeInteger newID) {
        privateAcc = privateAcc.modular_pow(newID, Group.N);
    }


}
