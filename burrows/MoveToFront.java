import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        ArrayList<Character> ASCII = new ArrayList<>();
        for (char i = 0; i < 256; i++) {
            ASCII.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (char j = 0; j < 256; j++) {
                if(c == ASCII.get(j)) {
                    BinaryStdOut.write((char) j);
                    ASCII.add(0, ASCII.remove(j));
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        ArrayList<Character> ASCII = new ArrayList<>();
        for (char i = 0; i < 256; i++) {
            ASCII.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(ASCII.get(c));
            ASCII.add(0, ASCII.remove(c));
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
