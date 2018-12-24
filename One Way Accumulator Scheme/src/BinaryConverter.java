public class BinaryConverter {
    public LargeInteger binaryToDecimal(int input) {
        LargeInteger result = new LargeInteger(Integer.toBinaryString(input));
        return result;
    }


    public int binaryToDecimal(LargeInteger input) {
        return Integer.parseInt(input.toString(), 2);
    }
}
