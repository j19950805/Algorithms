import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class BurrowsWheeler {
    private static final int EXTENDED_ASCII = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StringBuilder transformed = new StringBuilder();
        int length = csa.length();
        for (int i = 0; i < length; i++) {
            int startIndex = csa.index(i);
            if (startIndex == 0) {
                BinaryStdOut.write(i);
            }
            transformed.append(s.charAt((length + startIndex - 1) % length));
        }
        BinaryStdOut.write(transformed.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int[] next = new int[s.length()];
        ArrayList<Queue<Integer>> count = new ArrayList<>(EXTENDED_ASCII);
        for (int i = 0; i < EXTENDED_ASCII; i++) {
            count.add(new Queue<>());
        }
        for (int i = 0; i < s.length(); i++) {
            count.get(s.charAt(i)).enqueue(i);
        }
        for (int i = 0, index = 0; i < EXTENDED_ASCII; i++) {
            Queue<Integer> q = count.get(i);
            while (!q.isEmpty()) {
                next[index] = q.dequeue();
                index++;
            }
        }
        for (int i = 0, j = next[first]; i < s.length(); i++, j = next[j]) {
            BinaryStdOut.write(s.charAt(j));
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
