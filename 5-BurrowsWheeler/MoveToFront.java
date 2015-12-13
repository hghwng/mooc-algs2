import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int BASE_ADJUSTMENT = 128;
    private static final int RADIX = 256;

    public static void encode() {
        char[] arr = BinaryStdIn.readString().toCharArray();
        LinkedList<Integer> dict = new LinkedList<>();
        for (int i = 0; i < RADIX; ++i) dict.add(i);
        for (char c : arr) {
            int i = dict.indexOf((int)c);
            BinaryStdOut.write(i, 8);
            dict.add(0, dict.remove(i));
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] arr = BinaryStdIn.readString().toCharArray();
        LinkedList<Integer> dict = new LinkedList<>();
        for (int i = 0; i < RADIX; ++i) dict.add(i);

        for (char i : arr) {
            int c = dict.get((int)i);
            dict.remove((int)i);
            dict.add(0, c);
            BinaryStdOut.write(c, 8);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        switch (args[0]) {
            case "-":
                encode();
                break;

            case "+":
                decode();
                break;

            default:
                throw new IllegalArgumentException("Usage: MoveToFront + / -");
        }
    }
}
