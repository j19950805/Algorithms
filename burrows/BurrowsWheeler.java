import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

public class BurrowsWheeler {
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
        Queue<Integer>[] count =  new Queue[256];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (count[c] == null) {
                count[c] = new Queue<>();
            }
            count[c].enqueue(i);
        }
        int nextIndex = 0;
        for (int i = 0; i < 255; i++) {
            while (count[i] != null && !count[i].isEmpty()) {
                next[nextIndex] = count[i].dequeue();
                nextIndex++;
            }
        }
        for (int i = next[first]; i != first ; i = next[i]){
            BinaryStdOut.write(s.charAt(i));
        }
        BinaryStdOut.write(s.charAt(first));
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
