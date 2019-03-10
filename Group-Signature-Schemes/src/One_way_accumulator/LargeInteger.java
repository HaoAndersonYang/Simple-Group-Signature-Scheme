package One_way_accumulator;

public class LargeInteger {


    public int[] mag;
    public int size;
    public final int default_arr_size = 200;
    public final int KA_threshold = 5;

    public LargeInteger() {
        mag = new int[default_arr_size];
        mag[0] = 0;
        size = 1;
    }

    public LargeInteger(int[] mag, int size) {
        this.mag = mag;
        this.size = size;
        removeLeadingZeros();
    }

    //Accepts hex string
    public LargeInteger(String val) {
        mag = new int[default_arr_size];
        int size = val.length() / 4 + 1;
        for (int i = size; i >= 1; i--) {
            int stringpos = val.length() - i * 4;
            String substring = val.substring(Math.max(0, stringpos), stringpos + 4);
            if (substring.equals("")) {
                mag[size - i] = 0;
            } else {
                mag[size - i] = Integer.parseUnsignedInt(substring, 16);
            }
        }
        this.mag = removeLeadingZeros(mag);
        for (size = 0; size < this.mag.length && this.mag[size] != 0; size++)
            ;
        this.size = size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            StringBuilder val = new StringBuilder(Integer.toHexString(mag[i]));
            while (val.length() < 4) {
                val.insert(0, "0");
            }
            sb.append(val);
            sb.append(" ");
        }
        return sb.toString().toUpperCase();
    }

    public LargeInteger(int val) {
        int lo = val & 0xffff;
        int hi = val >> 16;
        mag = new int[default_arr_size];
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
        int[] resultMag = new int[default_arr_size];
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

    //The input is always going to be smaller than this value in the scheme
    public LargeInteger subtract(LargeInteger val) {
        //If the size of val is larger than this large integer, swap them
        int[] resultMag = new int[default_arr_size];
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
            temp -= b;
            temp += cout;
            cout = 0;
            while (temp < 0) {
                temp += 65536;
                cout -= 1;
            }

            resultMag[res_index] = (int) (temp & 0xffff);
        }
        return new LargeInteger(resultMag, result_size);
    }

    //Naive Multiplication
    public LargeInteger naive_multiply(LargeInteger val) {
        //If the size of val is smaller than this large integer, swap them
//        if (val.size < this.size) {
//            return val.naive_multiply(this);
//        }
        int[] resultMag = new int[default_arr_size];
        int result_size = this.size + val.size + 1;
        for (int i = 0; i < this.size; i++) {
            int this_index = this.size - i - 1;
            long a = this.mag[this_index];
            long cout = 0;
            for (int j = 0; j < val.size; j++) {
                int val_index = val.size - j - 1;
                long b = val.mag[val_index];
                int res_index = result_size - i - j - 1;
                long temp = 0;
                temp += a * b;
                temp += cout;
                temp += resultMag[res_index];
                cout = temp >> 16;
                resultMag[res_index] = (int) (temp & 0xffff);
            }
            //This should not cause out of bound since we are computing from lowest significant bit.
            resultMag[result_size - i - val.size - 1] += cout;
        }
        return new LargeInteger(resultMag, result_size);
    }


    public LargeInteger KA_multiply(LargeInteger val) {
        if (val.size < KA_threshold || this.size < KA_threshold) {
            return naive_multiply(val);
        }
        int min_size = Math.min(val.size, this.size);
        int middle = min_size / 2;
        LargeInteger hi_val = new LargeInteger(arrayCopy(val.mag, 0, middle), middle);
        LargeInteger lo_val = new LargeInteger(arrayCopy(val.mag, middle, val.size), val.size - middle);
        LargeInteger hi_this = new LargeInteger(arrayCopy(mag, 0, middle), middle);
        LargeInteger lo_this = new LargeInteger(arrayCopy(mag, middle, size), size - middle);

//        System.out.println("hival: " + hi_val);
//        System.out.println("loval: " + lo_val);
//        System.out.println("hithi: " + hi_this);
//        System.out.println("lothi: " + lo_this);


        LargeInteger z0 = lo_val.KA_multiply(lo_this);
        LargeInteger z1 = (lo_val.add(hi_val)).KA_multiply((lo_this.add(hi_this)));
        LargeInteger z2 = hi_val.KA_multiply(hi_this);
        LargeInteger temp = (z1.subtract(z2)).subtract(z0);
        temp.size += (middle + 1);
        z2.size += 2 * (middle + 1);

//        System.out.println("z0 :" + z0);
//        System.out.println("z1 :" + z1);
//        System.out.println("z2 :" + z2);
//        System.out.println("temp :" + temp);
//        System.out.println("z2+temp: " + z2.add(temp));
        return z2.add(temp).add(z0);
    }

    //Remove the leading zeros from input array
    public int[] removeLeadingZeros(int[] mag) {
        int keep, pos = 0;
        int[] res = new int[default_arr_size];
        for (keep = 0; keep < mag.length && mag[keep] == 0; keep++)
            ;
        for (; keep < mag.length && mag[keep] != 0; keep++) {
            res[pos] = mag[keep];
            pos++;
        }
        return res;
    }

    //Remove the leading zeros from current LargeInteger
    public void removeLeadingZeros() {
        int keep, pos = 0;
        for (keep = 0; keep < size && mag[keep] == 0; keep++)
            ;
        if (keep == 0)
            return;
        int oldsize = size;
        size -= keep;
        for (; keep < oldsize; keep++) {
            mag[pos] = mag[keep];
            mag[keep] = 0;
            pos++;
        }
    }

    public int[] arrayCopy(int[] mag, int start, int end) {
        int[] res = new int[default_arr_size];
        int pos = 0;
        for (int i = start; i < end; i++) {
            res[pos] = mag[i];
            pos++;
        }
        return res;
    }

}
