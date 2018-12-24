import java.util.ArrayList;

public class Group {
    public static LargeInteger N = new LargeInteger("1111111111111111111111111111111111111111111111111111111111111");
    public static LargeInteger accumulator = new LargeInteger("10");//Initial value is base
    //2

    public static ArrayList<Member> members = new ArrayList<>();

    public static void addMember(LargeInteger id) {
        Member newmem = new Member(id, accumulator);
        accumulator = accumulator.modular_pow(id, Group.N);
        System.out.println(accumulator.value);
        for (Member member : members) {
            member.update(id);
        }
        members.add(newmem);
    }
}
