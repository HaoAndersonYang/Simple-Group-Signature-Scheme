public class LargeInteger implements Comparable<LargeInteger> {

    /**
     * The value of the LargeInteger, in <i>big endian</i> order:
     * the element of the array at index head is the most significant int
     * of the value.
     * The value is stored in binary representation: all elements in the
     * array must be zeros and ones.
     */
    public int[] value = new int[1000];//Big endian

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
        this.value = input.value.clone();
        this.head = input.head;
        this.tail = input.tail;
        this.size = input.size;
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

    //https://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
    public LargeInteger modular_pow(LargeInteger exp, LargeInteger mod) {
        LargeInteger exponent = new LargeInteger(exp);
        LargeInteger module = new LargeInteger(mod);
        if (module.size == 1) { //Either mod 1 or mod 0.
            return new LargeInteger();
        }
        LargeInteger base = this.mod(module);
        LargeInteger result = new LargeInteger("1");
        while (exponent.compareTo(new LargeInteger()) > 0) {
            if (exponent.value[exponent.tail - 1] != 0) {
                result = (result.KAmultiply(base)).mod(module);
            }
            exponent = exponent.shiftRight();
            base = base.square().mod(module);
        }
        return result;
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

    //Multiple-precision classical multiplication
    public LargeInteger CMmultiply(LargeInteger multiplier) {
        LargeInteger result = new LargeInteger();
        for (int i = 0; i < this.size + multiplier.size; i++) {
            result.insertAtLast((byte) 0);
        }
        for (int i = this.size - 1; i >= 0; i--) {
            int indexi = this.size - i - 1;
            int carry = 0;
            for (int j = multiplier.size - 1; j >= 0; j--) {
                int indexj = multiplier.size - j - 1;
                int a = result.value[result.tail - indexi - indexj - 1];
                int b = (multiplier.value[j] & this.value[i]);
                int su = (a ^ b ^ carry);
                carry = ((a & b) ^ (carry & (a ^ b)));
                result.value[result.tail - indexi - indexj - 1] = su;
            }
            result.value[result.tail - multiplier.size - indexi - 1] = carry;
        }
        result.stripLeadingZeros();
        return result;
    }

    //Karatsuba Algorithm
    public LargeInteger KAmultiply(LargeInteger multiplier) {
        if (this.size <= 1) {
            return new LargeInteger(this.value[head] & multiplier.value[head]);
        }
        LargeInteger[] ALR = this.split();
        LargeInteger[] BLR = multiplier.split();
        System.out.println(this);
        System.out.println(ALR[0] + " " + ALR[1]);
        System.out.println(multiplier);
        System.out.println(BLR[0] + " " + BLR[1]);
        System.out.println();
        LargeInteger d1 = ALR[0].KAmultiply(BLR[0]);
        LargeInteger d0 = ALR[1].KAmultiply(BLR[1]);
        LargeInteger d01 = (ALR[0].add(ALR[1])).KAmultiply((BLR[0].add(BLR[1])));
        return d1.shiftLeftbyn(this.size - 1).add((d01.subtract(d0).subtract(d1))).shiftLeftbyn((this.size - 1) / 2).add(d0);
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

//    public LargeInteger shiftLeft() {
//        LargeInteger result = new LargeInteger(this);
//        result.insertAtLast((byte) 0);
//        return result;
//    }


    public LargeInteger shiftLeftbyn(int n) {
        LargeInteger result = new LargeInteger(this);
        while (n > 0) {
            n--;
            result.insertAtLast((byte) 0);
        }
        return result;
    }

    public LargeInteger subtract(LargeInteger toSubtract) {
        LargeInteger diff = new LargeInteger();
        LargeInteger l = new LargeInteger(this);
        LargeInteger s = new LargeInteger(toSubtract);
//        //ASSUME L>S
//        while (s.size < l.size) {
//            s.insertAtHead((byte) 0);
//        }
//        byte bout = 0;
//        while (s.size > 0) {
//            byte a = l.removeLast();
//            byte b = s.removeLast();
//            byte d = (byte) (a ^ b ^ bout);
//            bout = (byte) ((a & bout) ^ (a & b) ^ (b & bout));
//            diff.insertAtHead(d);
//        }
//        while (diff.peekFirst() == 0) {
//            diff.removeFirst();
//        }
        return new LargeInteger(diff);
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
        LargeInteger sum = new LargeInteger();
        //Initiate the sum
        while (sum.size < a_size + 1) {
            sum.insertAtLast(0);
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
            sum.value[sum.size - i - 1] = s;
        }
        sum.value[0] = cout;
        sum.stripLeadingZeros();
        return sum;
    }

    /**
     * Strips the leading zeros in the {@code value} array of this LargeIntegers.
     */
    public void stripLeadingZeros() {
        while (peekFirst() == 0 && this.size > 1) {
            removeFirst();
        }
    }

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
