
public class CircularSuffixArray {
    private final int length;
    private final int[] index;
    private final String s;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        this.s = s;
        length = s.length();
        index = new int[length];
        CircularSuffix[] suffixes = new CircularSuffix[length];
        for (int i = 0; i < length; i++) {
            suffixes[i] = new CircularSuffix(i);
        }
        quick3Sort(suffixes, 0, length, 0);
        for (int i = 0; i < length; i++) {
            index[i] = suffixes[i].first;
        }
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        validIndex(i);
        return index[i];
    }

    private void validIndex(int i) {
        if (i < 0 || i >= length) {
            throw new IllegalArgumentException();
        }
    }

    private void quick3Sort(CircularSuffix[] a, int lo, int hi, int d) {

        if (d >= length) {
            return;
        }

        // cutoff to insertion sort for small subarrays (size <= 15)
        int cutoff = 16;
        if (hi <= lo + cutoff) {
            insertionSort(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = s.charAt((a[lo].first + d) % length);
        int i = lo + 1;
        while (i < gt) {
            int t = s.charAt((a[i].first + d) % length);
            if (t < v) {
                exch(a, lt, i);
                lt++;
                i++;
            }
            else if (t > v) {
                exch(a, i, gt - 1);
                gt--;
            } else {
                i++;
            }
        }

        quick3Sort(a, lo, lt, d);
        quick3Sort(a, lt, gt, d + 1);
        quick3Sort(a, gt, hi, d);
    }

    private void exch(CircularSuffix[] a, int i, int j) {
        int temp = a[i].first;
        a[i].first = a[j].first;
        a[j].first = temp;
    }


    private void insertionSort(CircularSuffix[] a, int lo, int hi, int d) {
        for (int i = lo; i < hi; i++) {
            for (int j = i; j > lo && less(a[j].first, a[j - 1].first, d); j--) {
                exch(a, j, j - 1);
            }
        }
    }

    private boolean less(int first1, int first2, int d) {
        for (int i = d; i < length; i++) {
            char a = s.charAt((first1 + i) % length);
            char b = s.charAt((first2 + i) % length);
            if (a < b) return true;
            if (a > b) return false;
        }
        return false;
    }

    private class CircularSuffix {
        private int first;

        private CircularSuffix(int first) {
            this.first = first;
        }

    }


    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("bananaajighqhvihsjoihasjsh;");
        for (int i = 0; i < c.length(); i++) {
            System.out.println(c.index(i));
        }
    }
}
