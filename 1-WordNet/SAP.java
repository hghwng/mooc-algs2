import edu.princeton.cs.algs4.*;
import java.util.*;

public class SAP {
    private Digraph G;

    private void verifyRange(Iterable<Integer> v) {
        for (int i : v) {
            if (i < 0 || i >= G.V()) throw new IndexOutOfBoundsException();
        }
    }

    private void bfs(Iterable<Integer> starts, Map<Integer, Integer> map) {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i : starts) {
            queue.add(i);
            map.put(i, 0);
        }

        while (!queue.isEmpty()) {
            int root = queue.removeLast();
            int currentDist = map.get(root) + 1;
            for (int child : G.adj(root)) {
                if (map.getOrDefault(child, Integer.MAX_VALUE) > currentDist) {
                    map.put(child, currentDist);
                    if (!queue.contains(child)) queue.add(child);
                }
            }
        }
    }

    private class SapResult {
        public int id;
        public int length;
        public SapResult() {
            id = -1;
            length = Integer.MAX_VALUE;
        }
    }

    private SapResult sap(Iterable<Integer> v, Iterable<Integer> w) {
        HashMap<Integer, Integer> vmap = new HashMap<>(), wmap = new HashMap<>();
        bfs(v, vmap);
        bfs(w, wmap);

        SapResult ret = new SapResult();
        for (Map.Entry<Integer, Integer> entry : vmap.entrySet()) {
            if (!wmap.containsKey(entry.getKey())) continue;
            int dist = wmap.get(entry.getKey()) + entry.getValue();
            if (dist < ret.length) {
                ret.id = entry.getKey();
                ret.length = dist;
            }
        }

        if (ret.id == -1) ret.length = -1;
        return ret;
    }

    public SAP(Digraph G) {
        if (G == null) throw new NullPointerException();
        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        ArrayList<Integer> vv = new ArrayList<>();
        vv.add(v);
        ArrayList<Integer> ww = new ArrayList<>();
        ww.add(w);
        return length(vv, ww);
    }

    public int ancestor(int v, int w) {
        ArrayList<Integer> vv = new ArrayList<>();
        vv.add(v);
        ArrayList<Integer> ww = new ArrayList<>();
        ww.add(w);
        return ancestor(vv, ww);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException();
        verifyRange(v);
        verifyRange(w);
        SapResult r = sap(v, w);
        return r.length;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException();
        verifyRange(v);
        verifyRange(w);
        SapResult r = sap(v, w);
        return r.id;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

