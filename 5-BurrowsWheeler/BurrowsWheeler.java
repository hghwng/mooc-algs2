import edu.princeton.cs.algs4.*;

import java.util.Arrays;

public class BurrowsWheeler {
    private static final int RADIX = 256;

    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        for (int i = 0; i < s.length(); ++i) {
            if (csa.index(i) != 0) continue;
            BinaryStdOut.write(i);
            break;
        }

        for (int j = 0; j < s.length(); ++j) {
            int idx = (s.length() - 1 + csa.index(j)) % s.length();
            BinaryStdOut.write(s.charAt(idx), 8);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        int first = BinaryStdIn.readInt();
        char[] tail = BinaryStdIn.readString().toCharArray();
        int l = tail.length;
        char[] head = new char[l];
        System.arraycopy(tail, 0, head, 0, l);
        Arrays.sort(head);

        int[] nextChar = new int[RADIX];
        for (int i = l - 1; i >= 0; --i) {
            nextChar[tail[i]] = i;
        }

        int[] next = new int[l];
        for (int i = 0; i < l; ++i) {
            next[i] = nextChar[head[i]];
            for (int t = nextChar[head[i]] + 1; t < l; ++t) {
                if (tail[t] == head[i]) {
                    nextChar[head[i]] = t;
                    break;
                }
            }
        }

        int p = first;
        for (int i = 0; i < l; ++i) {
            BinaryStdOut.write(head[p], 8);
            p = next[p];
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
                throw new IllegalArgumentException("Usage: BurrowsWheeler + / -");
        }
    }
}
