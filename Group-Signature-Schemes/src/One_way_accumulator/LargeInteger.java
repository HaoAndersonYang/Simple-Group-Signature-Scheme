package One_way_accumulator;

public class LargeInteger {


    public int[] mag = new int[200];
    public int size;

    public LargeInteger() {
        mag[0] = 0;
        size = 0;
    }

    public LargeInteger(int[] mag, int size) {
        this.mag = mag;
        this.size = size;
    }

    public LargeInteger(String val) {

    }

    public LargeInteger(int val) {
        int lo = val & 0xffff;
        int hi = val >> 16;
        if (hi == 0) {
            mag[0] = lo;
            size = 1;
        } else {
            mag[0] = hi;
            mag[1] = lo;
            size = 2;
        }
    }

    public LargeInteger add(LargeInteger val) {
        //If the size of val is larger than this large integer, swap them
        if (val.size > this.size) {
            return val.add(this);
        }
        int[] resultMag = new int[200];
        int result_size = this.size + 1;
        int cout = 0;
        for (int i = 0; i < result_size; i++) {
            int a = 0;
            int b = 0;
            if (i < val.size) {
                int val_index = val.size - i - 1;
                b = val.mag[val_index];
            }
            if (i < this.size) {
                int this_index = this.size - i - 1;
                a = this.mag[this_index];
            }
            int res_index = result_size - i - 1;
            long temp = 0;
            temp += a;
            temp += b;
            temp += cout;
            cout = (int) (temp >> 16);
            resultMag[res_index] = (int) (temp & 0xffff);
        }
        return new LargeInteger(resultMag, result_size);
    }

    //Naive Multiplication
    public LargeInteger naive_multiply(LargeInteger val) {
        //If the size of val is larger than this large integer, swap them
        if (val.size > this.size) {
            return val.add(this);
        }
        int[] resultMag = new int[200];
        int result_size = this.size + val.size + 1;
        int cout = 0;
        for (int i = 0; i < result_size; i++) {
            int a = 0;
            int b = 0;
            if (i < val.size) {
                int val_index = val.size - i - 1;
                b = val.mag[val_index];
            }
            if (i < this.size) {
                int this_index = this.size - i - 1;
                a = this.mag[this_index];
            }
            int res_index = result_size - i - 1;
            long temp = 0;
            temp += a;
            temp += b;
            temp += cout;
            cout = (int) (temp >> 16);
            resultMag[res_index] = (int) (temp & 0xffff);
        }
        return new LargeInteger(resultMag, result_size);
    }

}
