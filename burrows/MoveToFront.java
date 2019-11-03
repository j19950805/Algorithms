import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class MoveToFront {
    private static final int EXTENDED_ASCII = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] ASCII = new char[EXTENDED_ASCII];
        for (char i = 0; i < EXTENDED_ASCII; i++) {
            ASCII[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (char j = 0; j < EXTENDED_ASCII; j++) {
                if (c == ASCII[j]) {
                    BinaryStdOut.write(j);
                    System.arraycopy(ASCII, 0, ASCII, 1, j);
                    ASCII[0] = c;
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] ASCII = new char[EXTENDED_ASCII];
        for (char i = 0; i < EXTENDED_ASCII; i++) {
            ASCII[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char d = ASCII[c];  // decoded character
            BinaryStdOut.write(d);
            System.arraycopy(ASCII, 0, ASCII, 1, c);
            ASCII[0] = d;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }
    }

}
