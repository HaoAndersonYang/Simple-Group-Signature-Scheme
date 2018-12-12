import java.util.LinkedList;

// Assume no leading zero
public class LargeInteger implements Comparable<LargeInteger> {

    public LinkedList<Byte> value;

    public LargeInteger(LinkedList<Byte> value) {
        this.value = value;
    }

    public LargeInteger(LargeInteger li) {
        this.value = new LinkedList<>(li.value);
    }

    public LargeInteger(String input) {
        value = new LinkedList<>();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '1') {
                value.offerLast((byte) 1);
            }
            if (input.charAt(i) == '0') {
                value.offerLast((byte) 0);
            }
        }
    }

    public LargeInteger() {
        value = new LinkedList<>();
        value.offer((byte) 0);
    }

    //https://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
    public LargeInteger modular_pow(LargeInteger exp, LargeInteger mod) {
        LargeInteger exponent = new LargeInteger(exp);
        LargeInteger module = new LargeInteger(mod);
        if (module.value.size() == 1) { //Either mod 1 or mod 0.
            return new LargeInteger();
        }
        LargeInteger base = this.mod(module);
        LargeInteger result = new LargeInteger("1");
        while (exponent.compareTo(new LargeInteger()) > 0) {
            if (exponent.value.getLast() == 1) {
                result = (result.multiply(base)).mod(module);
            }
            exponent = exponent.shiftRight();
            base = base.square().mod(module);
        }
        return result;
    }

    public LargeInteger shiftRight() {
        LinkedList<Byte> newList = new LinkedList<>(value);
        newList.removeLast();
        return new LargeInteger(newList);
    }

    //https://en.wikipedia.org/wiki/Multiplication_algorithm#Peasant_or_binary_multiplication
    public LargeInteger multiply(LargeInteger multiplier) {
        LargeInteger result = new LargeInteger();
        LargeInteger current = new LargeInteger(this);
        LargeInteger multi = new LargeInteger(multiplier);
        while (current.value.size() >= 1) {
            if (current.value.getLast() != 0) {
                result = result.add(multi);
            }
            multi = multi.shiftLeft();
            current = current.shiftRight();
        }
        return result;
    }

    public LargeInteger shiftLeft() {
        LinkedList<Byte> newList = new LinkedList<>(value);
        newList.offerLast((byte) 0);
        return new LargeInteger(newList);
    }

    public LargeInteger subtract(LargeInteger toSubtract) {
        LinkedList<Byte> diff = new LinkedList<>();
        LinkedList<Byte> l = new LinkedList<>(this.value);
        LinkedList<Byte> s = new LinkedList<>(toSubtract.value);
        byte cout = 0;
        while (s.size() > 0) {
            int a = l.removeLast();
            int b = s.removeLast();
            int res = 0;
            if (a == 0) {
                if (b + cout >= 1) {
                    res = 2 - b - cout;
                    cout = 1;
                }
            } else {
                if (b + cout >= 2) {
                    cout = 1;
                    res = 1;
                } else {
                    res = a - b - cout;
                    cout = 0;
                }
            }
            diff.offerFirst((byte) res);
        }
        while (l.size() > 0) {
            int a = l.removeLast();
            if (cout == 1) {
                if (a == 1) {
                    cout = 0;
                    a = 0;
                } else {
                    a = 1;
                }
            }
            diff.offerFirst((byte) a);
        }
        while (diff.peekFirst() == 0) {
            diff.removeFirst();
        }
        return new LargeInteger(diff);
    }


    public LargeInteger add(LargeInteger toAdd) {
        if (toAdd.value.size() > this.value.size()) {
            return toAdd.add(this);
        }
        LinkedList<Byte> sum = new LinkedList<>();
        LinkedList<Byte> l = new LinkedList<>(this.value);
        LinkedList<Byte> s = new LinkedList<>(toAdd.value);
        byte cout = 0;
        while (s.size() > 0) {
            int a = l.removeLast() + s.removeLast() + cout;
            if (a >= 2) {
                cout = 1;
                a -= 2;
            } else {
                cout = 0;
            }
            sum.offerFirst((byte) a);
        }
        while (l.size() > 0) {
            int a = l.removeLast() + cout;
            if (a >= 2) {
                cout = 1;
                a -= 2;
            } else {
                cout = 0;
            }
            sum.offerFirst((byte) a);
        }
        if (cout != 0) {
            sum.offerFirst((byte) 1);
        }
        LargeInteger res = new LargeInteger(sum);
        while (res.compareTo(Group.N) > 0) {
            res = res.subtract(Group.N);
        }
        return res;
    }

    public LargeInteger square() {
        return this.multiply(this);
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
        if (o.value.size() > this.value.size()) {
            return -1;
        } else if (o.value.size() < this.value.size()) {
            return 1;
        }
        int result = 0;
        boolean found = false;
        for (int i = 0; i < this.value.size(); i++) {
            byte a = this.value.removeFirst();
            byte b = o.value.removeFirst();
            if (!found && a != b) {
                found = true;
                result = a - b;
            }
            this.value.offerLast(a);
            o.value.offerLast(b);
        }
        return result;
    }
}
