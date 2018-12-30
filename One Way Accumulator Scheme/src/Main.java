import java.math.BigInteger;
import java.util.Random;

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
//        System.out.println(i.KAmultiply(j).value);
//        System.out.println(i.compareTo(j));
//        LargeInteger id1 = new LargeInteger("100");//4
//        LargeInteger id2 = new LargeInteger("11");//3
//        LargeInteger id3 = new LargeInteger("10");//2
//        Group.addMember(id1);
//        System.out.println(); //4^2=16
//        Group.addMember(id2);
//        System.out.println(); //16^3=4096
//        Group.addMember(id3);
//        System.out.println(); //4096^2=16777216
//        for (Member member : Group.members) {
//            member.showIdentity();
//            System.out.println();
//        }

        BinaryConverter bc = new BinaryConverter();
//        StringBuilder sb = new StringBuilder();
//        int randoma = (int) (Math.random() * Math.pow(10, 100));
        BigInteger randoma = new BigInteger(1000, new Random());
        System.out.println(randoma);
//        System.out.println(randoma.toString(2));
//        System.out.println(randoma.shiftLeft(2).toString(2));
//        LargeInteger a = bc.binaryToDecimal(randoma);
        LargeInteger a = new LargeInteger(randoma.toString(2));
        System.out.println("a: " + a);
//        LargeInteger[] splita = a.split();
//        System.out.println(a.size);
//        System.out.println(a.head);
//        System.out.println(a.tail);
//        System.out.println(splita[0]);
//        System.out.println(splita[1]);
//        int randomb = (int) (Math.random() * Math.pow(10, 100));
        BigInteger randomb = new BigInteger(1000, new Random());
        System.out.println(randomb);
//        LargeInteger b = bc.binaryToDecimal(randomb);
        LargeInteger b = new LargeInteger(randomb.toString(2));
        System.out.println("b: " + b);

        long currentTime = System.nanoTime();
        LargeInteger mul = a.KAmultiply(b);
        System.out.println(System.nanoTime() - currentTime);

        currentTime = System.nanoTime();
        LargeInteger m = a.CMmultiply(b);
        System.out.println(System.nanoTime() - currentTime);

        currentTime = System.nanoTime();
        BigInteger ma = randoma.multiply(randomb);
        System.out.println(System.nanoTime() - currentTime);

        BigInteger decmul = new BigInteger(mul.toString(), 2);
        System.out.println("a*b bin: " + mul);
        System.out.println("a*b dec: " + decmul.toString());
        System.out.println("a-b: " + a.subtract(b));
        System.out.println("a+b: " + a.add(b));
        System.out.println();
//        System.out.println(bc.binaryToDecimal(mul));
        Bons aa = new Bons(a);
        Bons bb = new Bons(b);
//        Bons mm = new Bons(mul);
////        Bons b = new Bons(a);
//        System.out.println(aa);
//        System.out.println(bb);
//        System.out.println(mm);
        currentTime = System.nanoTime();
        LargeInteger mmmmm = aa.BOCM(aa, bb);
        System.out.println(System.nanoTime() - currentTime);
        System.out.println();
        System.out.println("BONS result: " + mmmmm);

        currentTime = System.nanoTime();
        LargeInteger aaaaa = a.naive_multiply(b);
        System.out.println(System.nanoTime() - currentTime);
        System.out.println(aaaaa);
    }
}
