public class Member {
    private LargeInteger id;
    private Group group;
    private LargeInteger privateAcc;

    public void showIdentity() {
        System.out.println(id.modular_pow(id, group.base));
        System.out.println(group.accumulator);
    }
}
