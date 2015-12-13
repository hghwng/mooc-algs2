import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private int length;
    Integer[] index;

    public CircularSuffixArray(final String s) {
        if (s == null) throw new NullPointerException();
        final char[] arr = s.toCharArray();
        length = s.length();

        index = new Integer[length];
        for (int i = 0; i < length; ++i) index[i] = i;
        Arrays.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                for (int i = 0; i < length; ++i) {
                    char ac = arr[(a + i) % length];
                    char bc = arr[(b + i) % length];
                    if (ac == bc) continue;
                    return ac - bc;
                }
                return 0;
            }
        });
    }

    public int length() {
        return length;
    }

    public int index(int i) {
        return index[i];
    }
}
