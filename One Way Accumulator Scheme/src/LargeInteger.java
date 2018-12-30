import java.sql.SQLSyntaxErrorException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

// Assume no leading zero
public class LargeInteger implements Comparable<LargeInteger> {

    public int[] value = new int[1000];//Big endian
    public int head = 0;//value[head] is the head
    public int tail = 0;//value[tail] is place to insert
    public int size = 0;//Number of bits

    public LargeInteger(LargeInteger li) {
        this.value = li.value.clone();
        this.head = li.head;
        this.tail = li.tail;
        this.size = li.size;
    }

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

    public LargeInteger(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '1') {
                this.insertAtLast((byte) 1);
            }
            if (input.charAt(i) == '0') {
                this.insertAtLast((byte) 0);
            }
        }
    }

    //Single bit
    public LargeInteger(int i) {
        this.insertAtLast((byte) i);
    }

    public void insertAtLast(int insert) {
        value[tail] = insert;
        tail += 1;
        size += 1;
        if (tail >= value.length) {
            tail = 0;
        }
        if (size > value.length) {
            System.out.println("ERROR: OVERFLOW");
            System.exit(-1);
        }
    }

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

    public LargeInteger() {
        this.insertAtLast((byte) 0);
    }

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
//            System.out.println();
            int indexi = this.size - i - 1;
//            System.out.println(indexi + " " + this.value[i]);
            int carry = 0;
            for (int j = multiplier.size - 1; j >= 0; j--) {
                int indexj = multiplier.size - j - 1;
//                System.out.println(indexj + " " + multiplier.value[j]);
                int a = result.value[result.tail - indexi - indexj - 1];
                int b = (multiplier.value[j] & this.value[i]);
//                byte b = (byte) (multiplier.value[j] * this.value[i]);
                int su = (a ^ b ^ carry);
//                byte su = (byte) ((a + b + carry) % 2);
                carry = ((a & b) ^ (carry & (a ^ b)));
//                carry = (byte) ((a + b + carry) / 2);
                result.value[result.tail - indexi - indexj - 1] = su;
            }
            result.value[result.tail - multiplier.size - indexi - 1] = carry;
//            System.out.println(result);
        }
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


    public LargeInteger add(LargeInteger toAdd) {
        return this.add(toAdd, head, tail, toAdd.head, toAdd.tail);
    }

    public LargeInteger add(LargeInteger toAdd, int a_head, int a_tail, int b_head, int b_tail) {
        int a_size = a_tail - a_head;
        int b_size = b_tail - b_head;
        if (a_size < b_size) {
            return toAdd.add(this, b_head, b_tail, a_head, a_tail);
        }
        LargeInteger sum = new LargeInteger();
        while (sum.size < a_size + 1) {
            sum.insertAtLast(0);
        }
        int cout = 0;
        int a, b;
        for (int i = 0; i < a_size; i++) {
            a = this.value[a_tail - i - 1];
            if (i < b_size) {
                b = toAdd.value[b_tail - i - 1];
            } else {
                b = 0;
            }
            int s = a ^ b ^ cout;
            cout = (byte) ((a & b) ^ (cout & (a ^ b)));
            sum.value[sum.size - i - 1] = s;
        }
        sum.value[0] = cout;
        sum.stripLeadingZeros();
        return sum;
    }

    public void stripLeadingZeros() {
        while (peekFirst() == 0 && this.size > 1) {
            removeFirst();
        }
    }

    public LargeInteger square() {
        return this.KAmultiply(this);
    }

    public LargeInteger mod(LargeInteger module) {
        LargeInteger res = new LargeInteger(this);
        while (res.compareTo(module) > 0) {
            res = res.subtract(module);
        }
        return res;
    }

    @Override
    public int compareTo(LargeInteger o) {
        if (o.size > this.size) {
            return -1;
        } else if (o.size < this.size) {
            return 1;
        }
        int result = 0;
        for (int i = 0; i < this.size; i++) {
            int aindex = head + i;
            if (aindex >= value.length) {
                aindex = 0;
            }
            int bindex = o.head + i;
            if (bindex >= value.length) {
                bindex = 0;
            }
            int a = this.value[aindex];
            int b = o.value[bindex];
            if (a != b) {
                result = a - b;
                break;
            }
        }
        return result;
    }
}
