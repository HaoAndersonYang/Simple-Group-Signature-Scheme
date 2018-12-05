import java.math.BigInteger;

public class LargeInteger implements Comparable<LargeInteger> {

    public byte[] value;

    public LargeInteger(byte[] value) {
        this.value = value;
    }

    public LargeInteger() {
        value = new byte[]{0};
    }

    public LargeInteger power(LargeInteger exponent) {


        //Is the exponent even or odd
        if (exponent.value[exponent.value.length - 1] == 0) {

        } else {

        }

        //TODO: IMPLEMENT
        return new LargeInteger();
    }

    //https://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
    public LargeInteger modular_pow(LargeInteger exponent, LargeInteger module) {
        if (module.value.length == 1) { //Either mod 1 or mod 0.
            return new LargeInteger();
        }
        LargeInteger base = this.mod(module);
        LargeInteger result = new LargeInteger(new byte[]{1});
        while (exponent.compareTo(new LargeInteger()) > 0) {
            if (exponent.value[exponent.value.length - 1] == 1) {
                result = (result.multiply(base)).mod(module);
            }
            exponent = exponent.shiftRight();
            base = base.square().mod(module);
        }
        return result;
    }

    private LargeInteger shiftRight() {
        return null;
    }

    //https://en.wikipedia.org/wiki/Multiplication_algorithm#Peasant_or_binary_multiplication
    private LargeInteger multiply(LargeInteger multiplier) {
        LargeInteger result = new LargeInteger();
        LargeInteger current = this;
        while (multiplier.value.length >= 1) {
            if (multiplier.value[multiplier.value.length - 1] != 0) {
                result = result.add(current);
            }
            current = current.shiftLeft();
        }
        return result;
    }

    private LargeInteger shiftLeft() {
        //TODO: IMPLEMENT
        return null;
    }

    private LargeInteger add(LargeInteger toAdd) {
        //TODO: IMPLEMENT
        return null;
    }

    public LargeInteger square() {
        //TODO: IMPLEMENT
        return null;
    }

    public LargeInteger mod(LargeInteger module) {
        //TODO: IMPLEMENT
        return new LargeInteger();
    }

    @Override
    public int compareTo(LargeInteger o) {
        return 0;
    }
}
