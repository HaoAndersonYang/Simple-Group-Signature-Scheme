public class Bons {
    private final int LIMIT_LENGTH = 5;
    public int[][] BO = new int[2][1000]; // Length->Pos
    private int BOsize = 0;

    public Bons(LargeInteger input) {
        LargeInteger binaryNumber = new LargeInteger(input);
        int last = 0;
        int cur = 0;
        int length = 0;
        boolean newbo = false;
        int pos = -1;
        int index = -1;
        boolean update = true;
        while (binaryNumber.size > 0) {
            if (update) {
                length = 0;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOsize; i++) {
            sb.append(BO[0][i]).append(" at ").append(BO[1][i]).append("\n");
        }
        return sb.toString();
    }
}
