import java.sql.SQLSyntaxErrorException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

// Assume no leading zero
public class LargeInteger implements Comparable<LargeInteger>, Iterator<Byte> {

    public Byte[] value = new Byte[1000];
    public int head = 0;//value[head] is the head
    public int tail = 0;//value[tail] is place to insert
    public int size = 0;


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
        this.insertAtHead((byte) i);
    }

    public void insertAtLast(byte insert) {
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

    public byte removeLast() {
        byte res = value[tail - 1];
        tail -= 1;
        size -= 1;
        if (tail < 0) {
            tail = value.length - 1;
        }
        if (size <= 0) {
            System.out.println("ERROR: INVALID REMOVE");
            System.exit(-1);
        }
        return res;
    }

    private void insertAtHead(byte insert) {
        if (head == 0) {
            head = value.length;
        }
        head -= 1;
        value[head] = insert;
        size += 1;
        if (size > value.length) {
            System.out.println("ERROR: OVERFLOW");
            System.exit(-1);
        }
    }

    public LargeInteger() {
        this.insertAtHead((byte) 0);
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
            System.out.println();
            int indexi = this.size - i - 1;
            System.out.println(indexi + " " + this.value[i]);
            byte carry = 0;
            for (int j = multiplier.size - 1; j >= 0; j--) {
                int indexj = multiplier.size - j - 1;
                System.out.println(indexj + " " + multiplier.value[j]);
                byte a = result.value[result.tail - indexi - indexj - 1];
                byte b = (byte) (multiplier.value[j] & this.value[i]);
//                byte b = (byte) (multiplier.value[j] * this.value[i]);
                byte su = (byte) (a ^ b ^ carry);
//                byte su = (byte) ((a + b + carry) % 2);
                carry = (byte) ((a & b) ^ (carry & (a ^ b)));
//                carry = (byte) ((a + b + carry) / 2);
                result.value[result.tail - indexi - indexj - 1] = su;
            }
            result.value[result.tail - multiplier.size - indexi - 1] = carry;
            System.out.println(result);
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
        //ASSUME L>S
        while (s.size < l.size) {
            s.insertAtHead((byte) 0);
        }
        byte bout = 0;
        while (s.size > 0) {
            byte a = l.removeLast();
            byte b = s.removeLast();
            byte d = (byte) (a ^ b ^ bout);
            bout = (byte) ((a & bout) ^ (a & b) ^ (b & bout));
            diff.insertAtHead(d);
        }
        while (diff.peekFirst() == 0) {
            diff.removeFirst();
        }
        return new LargeInteger(diff);
    }


    public byte removeFirst() {
        byte res = value[head];
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

    private byte peekFirst() {
        return value[head];
    }


    public LargeInteger add(LargeInteger toAdd) {
        if (toAdd.size > this.size) {
            return toAdd.add(this);
        }
        LargeInteger sum = new LargeInteger();
        LargeInteger l = new LargeInteger(this);
        LargeInteger s = new LargeInteger(toAdd);
        while (s.size < l.size) {
            s.insertAtHead((byte) 0);
        }
        byte cout = 0;
        while (s.size > 0) {
            byte a = l.removeLast();
            byte b = s.removeLast();
            byte su = (byte) (a ^ b ^ cout);
            cout = (byte) ((a & b) ^ (cout & (a ^ b)));
            sum.insertAtHead(su);
        }
        LargeInteger res = new LargeInteger(sum);
        while (res.compareTo(Group.N) > 0) {
            res = res.subtract(Group.N);
        }
        return res;
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
            byte a = this.value[aindex];
            byte b = o.value[bindex];
            if (a != b) {
                result = a - b;
                break;
            }
        }
        return result;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Byte next() {
        return null;
    }
}
