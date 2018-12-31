public class Bons {
    private final int LIMIT_LENGTH = 5;
    public int[][] BO = new int[2][2000]; // Length->Pos
    private int BOsize = 0;

    public LargeInteger[][] lookup_table = new LargeInteger[5][5];

    public Bons(LargeInteger input) {
        lookup_table[0][0] = new LargeInteger("1");//1*1
        lookup_table[0][1] = new LargeInteger("11");//1*11
        lookup_table[0][2] = new LargeInteger("111");//1*111
        lookup_table[0][3] = new LargeInteger("1111");//1*1111
        lookup_table[0][4] = new LargeInteger("11111");//1*11111
        lookup_table[1][0] = new LargeInteger("11");//11*1
        lookup_table[1][1] = new LargeInteger("1001");//11*11
        lookup_table[1][2] = new LargeInteger("10101");//11*111
        lookup_table[1][3] = new LargeInteger("101101");//11*1111
        lookup_table[1][4] = new LargeInteger("1011101");//11*11111
        lookup_table[2][0] = new LargeInteger("111");//111*1
        lookup_table[2][1] = new LargeInteger("10101");//111*11
        lookup_table[2][2] = new LargeInteger("110001");//111*111
        lookup_table[2][3] = new LargeInteger("1101001");//111*1111
        lookup_table[2][4] = new LargeInteger("11011001");//111*11111
        lookup_table[3][0] = new LargeInteger("1111");//1111*1
        lookup_table[3][1] = new LargeInteger("101101");//1111*11
        lookup_table[3][2] = new LargeInteger("1101001");//1111*111
        lookup_table[3][3] = new LargeInteger("11100001");//1111*1111
        lookup_table[3][4] = new LargeInteger("111010001");//1111*11111
        lookup_table[4][0] = new LargeInteger("11111");//11111*1
        lookup_table[4][1] = new LargeInteger("1011101");//11111*11
        lookup_table[4][2] = new LargeInteger("11011001");//11111*111
        lookup_table[4][3] = new LargeInteger("111010001");//11111*1111
        lookup_table[4][4] = new LargeInteger("1111000001");//11111*11111


        LargeInteger binaryNumber = new LargeInteger(input);
        int last = 0;
        int cur = 0;
        int length = 0;
        boolean newbo = false;
        int pos = -1;
        int index = -1;
        boolean update = true;
        while (binaryNumber.size > 0) {
            length = 0;
            if (update) {
                last = cur;
                index++;
                cur = binaryNumber.removeLast();
            } else {
                update = true;
            }
            if (cur == 1 && last == 0) {
                newbo = true;
                pos = index;
            }
            while (newbo) {
                length++;
                if (binaryNumber.size > 0) {
                    last = cur;
                    cur = binaryNumber.removeLast();
                    index++;
                    if (cur == 0 && last == 1) {
                        newbo = false;
                    }
                } else {
                    newbo = false;
                }
                if (length == LIMIT_LENGTH) {
                    newbo = false;
                    last = 0;
                    update = false;
                }
            }
            if (length > 0) {
                BO[0][BOsize] = length;
                BO[1][BOsize] = pos;
                BOsize++;
            }
        }
    }

    public Bons() {
        //huh?
    }

    public LargeInteger LUT(int len1, int len2) {
        return lookup_table[len1 - 1][len2 - 1];
    }

    /**
     * @param A
     * @param B
     * @return
     */
    public LargeInteger BOCM(Bons A, Bons B) {
        LargeInteger result = new LargeInteger();
//        for (int i = 0; i < A.BOsize + B.BOsize + 1; i++) {
//            result.value[i] = 0;
//        }
//        for (int i = 0; i<A.)
        for (int i = 0; i < A.BOsize; i++) {
            int carry = 0;
            for (int j = 0; j < B.BOsize; j++) {
                LargeInteger temp = LUT(A.BO[0][i], B.BO[0][j]).shiftLeftbyn(A.BO[1][i] + B.BO[1][j]);
//                System.out.println(temp);
                result = result.add(temp);
//                System.out.println(result);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOsize; i++) {
            sb.append(BO[0][i]).append(" at ").append(BO[1][i]).append("\n");
        }
        return sb.toString();
    }
}
