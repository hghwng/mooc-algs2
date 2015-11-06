import edu.princeton.cs.algs4.*;

public class Outcast {
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new NullPointerException();
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxDist = -1;
        int maxIndex = -1;

        for (int i = 0; i < nouns.length; ++i) {
            int dst = 0;
            for (int j = 0; j < nouns.length; ++j) {
                if (i == j) continue;
                dst += wordnet.distance(nouns[i], nouns[j]);
            }
//            System.out.println(nouns[i] + "\t" + dst);

            if (dst > maxDist) {
                maxDist = dst;
                maxIndex = i;
            }
        }

        return nouns[maxIndex];
    }

    public static void main(String[] args) throws Exception {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
//            for (String noun : nouns) {
//                wordnet.dumpTree(noun);
//            }
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
