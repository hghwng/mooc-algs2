import java.io.*;
import java.util.*;
import edu.princeton.cs.algs4.*;

public class WordNet {
    private Map<String, ArrayList<Integer>> mapWordToId;
    private ArrayList<String> mapIdToSynset;
    //private ArrayList<String> glossTable;
    private Digraph G;
    private SAP sap;

    private ArrayList<Integer> getWordId(String word) {
        if (word == null) throw new NullPointerException();
        return mapWordToId.getOrDefault(word, new ArrayList<Integer>());
    }

    private void dumpTreeDoIt(int root, Deque<Integer> stack) {
        stack.addLast(root);
        if (G.outdegree(root) == 0) {
            for (int i : stack) {
                System.out.print(i + " -> ");
            }
            System.out.println();
        }
        for (int i : G.adj(root)) {
            dumpTreeDoIt(i, stack);
        }
        stack.removeLast();
    }

    private void dumpTree(String noun) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i : getWordId(noun)) {
            dumpTreeDoIt(i, stack);
        }
    }

    public WordNet(String synsetsPath, String hypernymsPath) throws Exception {
        if (synsetsPath == null || hypernymsPath == null) throw new NullPointerException();
        //glossTable = new ArrayList<String>();
        mapWordToId = new HashMap<>();
        mapIdToSynset = new ArrayList<>();

        // Read synset
        FileInputStream synsetStream = new FileInputStream(synsetsPath);
        BufferedReader synsetReader = new BufferedReader(new InputStreamReader(synsetStream, "UTF-8"));
        String lineString;

        while ((lineString = synsetReader.readLine()) != null) {
            Scanner scanner = new Scanner(lineString);
            scanner.useDelimiter(",");
            int id = scanner.nextInt();
            String synsetString = scanner.next();
            mapIdToSynset.add(synsetString);
            //String gloss = scanner.next();
            //glossTable.add(id, gloss);

            Scanner synScanner = new Scanner(synsetString);
            while (synScanner.hasNext()) {
                String name = synScanner.next();
                if (!mapWordToId.containsKey(name)) {
                    mapWordToId.put(name, new ArrayList<Integer>());
                }
                mapWordToId.get(name).add(id);
            }
        }
        synsetReader.close();
        synsetStream.close();

        // Read hypernsym
        G = new Digraph(mapIdToSynset.size());
        FileInputStream hypernsymStream = new FileInputStream(hypernymsPath);
        BufferedReader hypernsymReader = new BufferedReader(new InputStreamReader(hypernsymStream, "UTF-8"));
        while ((lineString = hypernsymReader.readLine()) != null) {
            Scanner scanner = new Scanner(lineString);
            scanner.useDelimiter(",");

            int child = scanner.nextInt();
            while (scanner.hasNext()) {
                int parent = scanner.nextInt();
                G.addEdge(child, parent);
            }
        }
        hypernsymReader.close();

        int rootNumber = 0;
        for (int i = 0; i < G.V(); ++i) {
            if (G.outdegree(i) == 0) ++rootNumber;
        }

        if (rootNumber != 1) {
            throw new IllegalArgumentException("Not rooted DAG");
        }

        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        ArrayList<String> nouns = new ArrayList<>();
        for (String i : mapWordToId.keySet()) {
            nouns.add(i);
        }
        return nouns;
    }

    public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException();
        return getWordId(word).size() != 0;
    }

    public int distance(String nounA, String nounB) {
        ArrayList<Integer> idA = getWordId(nounA);
        ArrayList<Integer> idB = getWordId(nounB);
        if (idA.size() == 0 || idB.size() == 0) throw new IllegalArgumentException("Not a noun");
        return sap.length(idA, idB);
    }

    public String sap(String nounA, String nounB) {
        ArrayList<Integer> idA = getWordId(nounA);
        ArrayList<Integer> idB = getWordId(nounB);
        if (idA.size() == 0 || idB.size() == 0) throw new IllegalArgumentException("Not a noun");
        return mapIdToSynset.get(sap.ancestor(idA, idB));
    }

    public static void main(String[] args) throws Exception {
        WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(w.isNoun("Turing"));
    }
}