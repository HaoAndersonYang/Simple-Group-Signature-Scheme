import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        //For testing
//        LargeInteger i = new LargeInteger("10");
//        System.out.println(i.value);
//        LargeInteger j = new LargeInteger("11");

//        LargeInteger res = i.modular_pow(j, Group.N);
//        System.out.println(res.value);
//        System.out.println(j.value);
//        System.out.println();
//        System.out.println(i.subtract(j).value);
//        System.out.println();
//        System.out.println(i.multiply(j).value);
//        System.out.println(i.compareTo(j));
        LargeInteger id1 = new LargeInteger("100");//4
        LargeInteger id2 = new LargeInteger("11");//3
        LargeInteger id3 = new LargeInteger("10");//2
        Group.addMember(id1);
        System.out.println(); //4^2=16
        Group.addMember(id2);
        System.out.println(); //16^3=4096
        Group.addMember(id3);
        System.out.println(); //4096^2=16777216
        for (Member member : Group.members) {
            member.showIdentity();
            System.out.println();
        }

    }
}
