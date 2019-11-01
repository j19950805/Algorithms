import java.util.Arrays;

public class CircularSuffixArray {
    private int length;
    private int[] index;
    private String s;

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
        Arrays.sort(suffixes);
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

    private class CircularSuffix implements Comparable<CircularSuffix>{
        private int first;

        private CircularSuffix(int first) {
            this.first = first;
        }

        @Override
        public int compareTo(CircularSuffix o) {
            for (int i = 0; i < length; i++) {
                char c = s.charAt((first + i) % length);
                char other = s.charAt((o.first + i) % length);
                if (c != other) {
                    return c - other;
                }
            }
            return 0;
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("banana");
        for (int i = 0; i < c.length(); i++) {
            System.out.println(c.index(i));
        }
    }
}
