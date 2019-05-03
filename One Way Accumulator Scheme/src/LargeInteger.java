public class LargeInteger implements Comparable<LargeInteger> {

    private final int KA_threshold = 5000;

    /**
     * The value of the LargeInteger, in <i>big endian</i> order:
     * the element of the array at index head is the most significant int
     * of the value.
     * The value is stored in binary representation: all elements in the
     * array must be zeros and ones.
     */
    public int[] value = new int[100000];//Big endian

    /**
     * The head of the value array. Integers in the array between {@code head}
     * (inclusive) and {@code tail} (exclusive) represents the binary value
     * of the LargeInteger.
     * value[head] is the most significant int of the value.
     */
    public int head = 0;

    /**
     * The tail of the value array. Integers in the array between {@code head}
     * (inclusive) and {@code tail} (exclusive) represents the binary value
     * of the LargeInteger.
     * value[tail] is the position for insertion of new bits.
     */
    public int tail = 0;

    /**
     * The number of valid bits of the value array.
     */
    public int size = 0;//Number of bits

    /**
     * Basic Constructor.
     * Creates a LargeInteger with value 0.
     */
    public LargeInteger() {
        this.insertAtLast((byte) 0);
    }

    /**
     * Creates a new LargeInteger which is the copy of the input
     * LargeInteger
     *
     * @param input the input LargeInteger
     */
    public LargeInteger(LargeInteger input) {
        this.head = input.head;
        this.tail = input.tail;
        this.size = input.size;
        System.arraycopy(input.value, head, value, 0, size);
    }

    /**
     * Creates a LargeInteger based on the input string. The string can
     * only contain '0's and '1's.
     *
     * @param input the input string
     */
    public LargeInteger(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '1') {
                this.insertAtLast((byte) 1);
            } else if (input.charAt(i) == '0') {
                this.insertAtLast((byte) 0);
            } else {
                System.out.println("ERROR: INVALID INSTANTIATION");
                System.exit(-1);
            }
        }
        stripLeadingZeros();
    }

    /**
     * Creates the LargeInteger based on the integer inputed.
     * The integer can only be zero or one.
     *
     * @param val the input integer
     */
    public LargeInteger(int val) {
        if (val != 1 && val != 0) {
            System.out.println("ERROR: INVALID INSTANTIATION");
            System.exit(-1);
        }
        this.insertAtLast(val);
    }

    /**
     * Inserts the specified int at the tail of the value array
     *
     * @param val the specified int
     */
    public void insertAtLast(int val) {
        size += 1;
        if (size > value.length) {
            System.out.println("ERROR: OVERFLOW");
            System.exit(-1);
        }
        value[tail] = val;
        tail += 1;
        if (tail >= value.length) {
            tail = 0;
        }
    }

    /**
     * Retrieves and removes the last element of the value array.
     *
     * @return the removed element
     */
    public int removeLast() {
        tail -= 1;
        size -= 1;
        if (tail < 0) {
            tail = value.length - 1;
        }
        int res = value[tail];
        if (size < 0) {
            System.out.println("ERROR: INVALID REMOVE");
            System.exit(-1);
        }
        return res;
    }

    /**
     * Returns a LargeInteger which value equals this>>1
     *
     * @return result LargeInteger
     */
    public LargeInteger shiftRight() {
        if (this.size == 1) {
            System.out.println("ERROR: Invalid shift");
            System.exit(-1);
        }
        LargeInteger result = new LargeInteger(this);
        result.tail -= 1;
        result.size -= 1;
        return result;
    }

    public LargeInteger shiftLeftbyn(int n) {
        LargeInteger result = new LargeInteger(this);
        for (int i = tail; i < tail + n; i++) {
            result.value[i] = 0;
        }
        result.tail += n;
        result.size += n;
        return result;
    }

    public int removeFirst() {
        int res = value[head];
        head += 1;
        size -= 1;
        if (head >= value.length) {
            head = 0;
        }
        if (size <= 0) {
            System.out.println("ERROR: INVALID REMOVE");
            System.exit(-1);
        }
        return res;
    }

    private int peekFirst() {
        return value[head];
    }

    /**
     * Strips the leading zeros in the {@code value} array of this LargeIntegers.
     */
    public void stripLeadingZeros() {
        while (peekFirst() == 0 && this.size > 1) {
            removeFirst();
        }
    }

    public LargeInteger[] split() {
        LargeInteger[] result = new LargeInteger[2];
        result[0] = new LargeInteger(this);
        result[0].tail = tail / 2;
        result[0].size -= tail - tail / 2;
        result[1] = new LargeInteger(this);
        result[1].head = tail / 2;
        result[1].size = this.size - result[0].size;
//        System.out.println(result[1].size);
        return result;
    }

//    //Not needed.
//    private void insertAtHead(byte insert) {
//        if (head == 0) {
//            head = value.length;
//        }
//        head -= 1;
//        value[head] = insert;
//        size += 1;
//        if (size > value.length) {
//            System.out.println("ERROR: OVERFLOW");
//            System.exit(-1);
//        }
//    }


    /**
     * Returns a LargeInteger whose value is {@code (this+val)}.
     *
     * @param val the value to be added to this LargeInteger.
     * @return {@code this+val}.
     */
    public LargeInteger add(LargeInteger val) {
        return this.add(val, head, tail, val.head, val.tail);
    }

    /**
     * Returns a LargeInteger whose value is the sum of binary number represented
     * by {@code this[a_head..a_tail]} and {@code val[b_head..b_tail]}.
     *
     * @param val    the value to be added to this LargeInteger.
     * @param a_head the starting index of this LargeInteger.
     * @param a_tail the ending index of this LargeInteger.
     * @param b_head the starting index of val.
     * @param b_tail the ending index of val.
     * @return sum of {@code this[a_head..a_tail]} and {@code val[b_head..b_tail]}.
     */
    public LargeInteger add(LargeInteger val, int a_head, int a_tail, int b_head, int b_tail) {
        int a_size = a_tail - a_head;
        int b_size = b_tail - b_head;
        //Swap two LargeInteger if val is longer than this
        if (a_size < b_size) {
            return val.add(this, b_head, b_tail, a_head, a_tail);
        }
        LargeInteger result = new LargeInteger();
        //Initiate the result
        while (result.size < a_size + 1) {
            result.insertAtLast(0);
        }
        int cout = 0;
        int a, b;
        for (int i = 0; i < a_size; i++) {
            a = this.value[a_tail - i - 1];
            //If we are done with the shorter LargeInteger, we set b to 0.
            if (i < b_size) {
                b = val.value[b_tail - i - 1];
            } else {
                b = 0;
            }
            int s = a ^ b ^ cout;
            cout = (a & b) ^ (cout & (a ^ b));
            result.value[result.size - i - 1] = s;
        }
        result.value[0] = cout;
        result.stripLeadingZeros();
        return result;
    }


    /**
     * Returns a LargeInteger whose value is {@code (this-val)}.
     *
     * @param val the value to be subtracted from this LargeInteger.
     *            Must be smaller than this LargeInteger
     * @return {@code this-val}.
     */
    public LargeInteger subtract(LargeInteger val) {
        return this.subtract(val, head, tail, val.head, val.tail);
    }

    /**
     * Returns a LargeInteger whose value is the difference between binary number
     * represented by {@code this[a_head..a_tail]} and {@code val[b_head..b_tail]}.
     *
     * @param val    the value to be subtracted from this LargeInteger.
     *               Must be smaller than this LargeInteger
     * @param a_head the starting index of this LargeInteger.
     * @param a_tail the ending index of this LargeInteger.
     * @param b_head the starting index of val.
     * @param b_tail the ending index of val.
     * @return sum of {@code this[a_head..a_tail]} and {@code val[b_head..b_tail]}.
     */
    public LargeInteger subtract(LargeInteger val, int a_head, int a_tail, int b_head, int b_tail) {
        LargeInteger result = new LargeInteger();
        int a_size = a_tail - a_head;
        int b_size = b_tail - b_head;
        while (result.size < a_size + 1) {
            result.insertAtLast(0);
        }
        int bout = 0;
        int a, b;
        for (int i = 0; i < a_size; i++) {
            a = this.value[a_tail - i - 1];
            //If we are done with the shorter LargeInteger, we set b to 0.
            if (i < b_size) {
                b = val.value[b_tail - i - 1];
            } else {
                b = 0;
            }
            int d = a ^ b ^ bout;
            //a^1 is equivalent to !a
            bout = ((a ^ 1) & b) ^ (((a ^ b) ^ 1) & bout);
            result.value[result.size - i - 1] = d;
        }
        result.stripLeadingZeros();
        return result;
    }


    //Multiple-precision classical multiplication
    public LargeInteger CMmultiply(LargeInteger val) {
        return this.CMmultiply(val, head, tail, val.head, val.tail);
    }

    public LargeInteger CMmultiply(LargeInteger val, int a_head, int a_tail, int b_head, int b_tail) {
        LargeInteger result = new LargeInteger();
        int a_size = a_tail - a_head;
        int b_size = b_tail - b_head;
        for (int i = 0; i < a_size + b_size; i++) {
            result.insertAtLast((byte) 0);
        }
        for (int i = a_tail - 1; i >= a_head; i--) {
            int indexi = a_tail - i - 1;
            int carry = 0;
            for (int j = b_tail - 1; j >= b_head; j--) {
                int indexj = b_tail - j - 1;
                int a = result.value[result.tail - indexi - indexj - 1];
                int b = (val.value[j] & this.value[i]);
                int su = (a ^ b ^ carry);
                carry = ((a & b) ^ (carry & (a ^ b)));
                result.value[result.tail - indexi - indexj - 1] = su;
            }
            result.value[result.tail - b_size - indexi - 1] = carry;
        }
        result.stripLeadingZeros();
        return result;
    }

    public LargeInteger naive_multiply(LargeInteger val) {
        LargeInteger result = new LargeInteger();
        for (int i = tail - 1; i >= head; i--) {
            int a = this.value[i];
            if (a == 0) {
                continue;
            }
            result.add(val.shiftLeftbyn(tail - 1 - i));
        }
        result.stripLeadingZeros();
        return result;
    }

    //Karatsuba Algorithm
    public LargeInteger KAmultiply(LargeInteger val) {
        return this.KAmultiply(val, this.head, this.tail, val.head, val.tail);
    }

    public LargeInteger KAmultiply(LargeInteger val, int a_head, int a_tail, int b_head, int b_tail) {
//        System.out.println(a_head + " " + a_tail + " " + b_head + " " + b_tail);
        int a_size = a_tail - a_head;
        int b_size = b_tail - b_head;
        if (a_size <= KA_threshold || b_size <= KA_threshold) {
            return this.CMmultiply(val, a_head, a_tail, b_head, b_tail);
        }
//        if (a_size == 0 || b_size == 0) {
//            return new LargeInteger();
//        }
//        if (a_size == 1) {
//            return this.add(val, a_head, a_tail, b_head, b_tail);
//        }
//        if (b_size == 1) {
//            return this.add(val, a_head, a_tail, b_head, b_tail);
//        }
        int half;
        if (a_size > b_size) {
            half = b_size / 2;
        } else {
            half = a_size / 2;
        }
        int a_mid = a_tail - half;
        if (a_mid < a_head) {
            a_mid = a_head;
        }
        int b_mid = b_tail - half;
        if (b_mid < b_head) {
            b_mid = b_head;
        }
//        System.out.println(a_head + " " + a_mid + " " + a_tail + " " + b_head + " " + b_mid + " " + b_tail);
        //Multiply upper half
        LargeInteger d1 = this.KAmultiply(val, a_head, a_mid, b_head, b_mid);
        //Multiply lower half
        LargeInteger d0 = this.KAmultiply(val, a_mid, a_tail, b_mid, b_tail);
        LargeInteger d01 = this.add(this, a_head, a_mid, a_mid, a_tail).
                KAmultiply(val.add(val, b_head, b_mid, b_mid, b_tail));
//        System.out.println("d1: " + d1);
//        System.out.println("d0: " + d0);
//        System.out.println("d01: " + d01);
//        LargeInteger d1s2 = d1.shiftLeftbyn(half * 2);
//        System.out.println("d1s2:" + d1s2);
//        LargeInteger d01subd0 = d01.subtract(d0);
//        LargeInteger d01subd0d1 = d01subd0.subtract(d1);
//        LargeInteger d01subd0d1s2 = d01subd0d1.shiftLeftbyn(half);
//        System.out.println(d01subd0);
//        System.out.println(d01subd0d1);
//        System.out.println(d01subd0d1s2);
//        return d1s2.add(d01subd0d1s2).add(d0);
        return (d1.shiftLeftbyn(half * 2)).add(((d01.subtract(d1)).subtract(d0)).shiftLeftbyn(half)).add(d0);
    }


    //https://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
    public LargeInteger modular_pow(LargeInteger exp, LargeInteger mod) {
        if (mod.size == 1) { //Either mod 1 or mod 0.
            return new LargeInteger();
        }
        LargeInteger base = this.mod(mod);
        LargeInteger result = new LargeInteger("1");
        for (int i = exp.tail; i >= exp.head; i--) {
            if (exp.value[i - 1] != 0) {
                result = (result.KAmultiply(base)).mod(mod);
            }
            base = base.square().mod(mod);
        }
        return result;
    }
//    public LargeInteger shiftLeft() {
//        LargeInteger result = new LargeInteger(this);
//        result.insertAtLast((byte) 0);
//        return result;
//    }


    public LargeInteger square() {
        return this.KAmultiply(this);
    }

    public LargeInteger mod(LargeInteger module) {
        //TODO: REIMPLEMENT
        LargeInteger res = new LargeInteger(this);
        while (res.compareTo(module) > 0) {
            res = res.subtract(module);
        }
        return res;
    }

    /**
     * Compare this LargeInteger with the specified LargeInteger.
     *
     * @param val LargeInteger to compared with.
     * @return -1 if this is smaller than val, 1 if this is larger than val and 0 if they are equal.
     */
    @Override
    public int compareTo(LargeInteger val) {
        if (val.size > this.size) {
            return -1;
        } else if (val.size < this.size) {
            return 1;
        }
        int result = 0;
        for (int i = 0; i < this.size; i++) {
            int aindex = head + i;
            if (aindex >= value.length) {
                aindex = 0;
            }
            int bindex = val.head + i;
            if (bindex >= value.length) {
                bindex = 0;
            }
            int a = this.value[aindex];
            int b = val.value[bindex];
            if (a != b) {
                result = a - b;
                break;
            }
        }
        return result;
    }

    /**
     * Returns the string which is the binary representation of the value
     *
     * @return the string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (tail < head) {
            for (int i = head; i < value.length; i++) {
                sb.append(value[i]);
            }
            for (int i = 0; i < tail; i++) {
                sb.append(value[i]);
            }
        } else {
            for (int i = head; i < tail; i++) {
                sb.append(value[i]);
            }
        }
        return sb.toString();
    }

}