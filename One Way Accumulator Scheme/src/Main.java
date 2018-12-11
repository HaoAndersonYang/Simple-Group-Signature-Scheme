import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        //For testing
        LargeInteger i = new LargeInteger("1010");
        System.out.println(i.value);
        LargeInteger j = new LargeInteger("11");
        System.out.println(j.value);
        System.out.println();
        System.out.println(i.subtract(j).value);
        System.out.println();
        System.out.println(i.multiply(j).value);
        System.out.println(i.compareTo(j));

    }
}
