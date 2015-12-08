import edu.princeton.cs.algs4.*;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class BoggleSolver {
    private static class DictNode {
        DictNode[] next;
        boolean isWord;

        DictNode() {
            next = new DictNode[26];
        }

        void insert(String s, int start) {
            if (start == s.length()) {
                isWord = true;
                return;
            }

            int v = s.charAt(start) - 'A';
            if (next[v] == null) next[v] = new DictNode();
            next[v].insert(s, start + 1);
        }

        boolean find(String s, int start) {
            if (start == s.length()) return isWord;
            int v = s.charAt(start) - 'A';
            if (next[v] == null) return false;
            return next[v].find(s, start + 1);
        }

        void dump(String prefix) {
            if (isWord) StdOut.println(prefix);
            for (int i = 0; i < next.length; ++i) {
                if (next[i] == null) continue;
                next[i].dump(prefix + (char)(i + 'A'));
            }
        }
    }

    private class Solver {
        final int INDEX_OF_Q = 'Q' - 'A';
        final int INDEX_OF_U = 'U' - 'A';

        class Cell {
            int v;
            boolean isAvailable;
        }

        int cols;
        Cell [] cells;

        int []path;
        int pathSize;
        HashSet<String> results;

        Solver(BoggleBoard boggleBoard) {
            cols = boggleBoard.cols() + 2;
            results = new HashSet<>();

            pathSize = 0;
            path = new int[boggleBoard.rows() * boggleBoard.cols() + 1];

            cells = new Cell[(boggleBoard.rows() + 2) * cols];
            for (int i = 0; i < cells.length; ++i) {
                cells[i] = new Cell();
                int r = i / cols;
                int c = i % cols;

                if (r == 0 || r == boggleBoard.rows() + 1 ||
                        c == 0 || c == cols - 1) {
                    cells[i].isAvailable = false;
                } else {
                    cells[i].v = boggleBoard.getLetter(r - 1, c - 1) - 'A';
                    cells[i].isAvailable = true;
                }
            }
        }

        void printCells() {
            for (int i = 0; i < cols; ++i) {
                for (int j = 0; j < cells.length / cols; ++j) {
                    Cell c = cells[i * cols + j];
                    StdOut.printf("%s%c\t",
                                  c.isAvailable ? " " : "*",
                                  (char)('A' + c.v));
                }
                StdOut.println();
            }
        }

        String getStringFromPath() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pathSize; ++i) {
                sb.append((char)(path[i] + 'A'));
                if (path[i] == INDEX_OF_Q) sb.append('U');
            }
            return sb.toString();
        }

        void dfs(DictNode node, int idx) {
            DictNode next = node.next[cells[idx].v];
            if (next == null) return;

            path[pathSize++] = cells[idx].v;
            cells[idx].isAvailable = false;

            if (cells[idx].v == INDEX_OF_Q) {
                next = next.next[INDEX_OF_U];
                if (next == null) {
                    --pathSize;
                    cells[idx].isAvailable = true;
                    return;
                }
            }

            if (next.isWord) {
                String s = getStringFromPath();
                if (s.length() > 2) {
                    results.add(s);
                }
            }

            if (cells[idx - 1].isAvailable)         dfs(next, idx - 1);
            if (cells[idx + 1].isAvailable)         dfs(next, idx + 1);
            if (cells[idx - cols].isAvailable)      dfs(next, idx - cols);
            if (cells[idx + cols].isAvailable)      dfs(next, idx + cols);
            if (cells[idx - cols - 1].isAvailable)  dfs(next, idx - cols - 1);
            if (cells[idx - cols + 1].isAvailable)  dfs(next, idx - cols + 1);
            if (cells[idx + cols - 1].isAvailable)  dfs(next, idx + cols - 1);
            if (cells[idx + cols + 1].isAvailable)  dfs(next, idx + cols + 1);

            --pathSize;
            cells[idx].isAvailable = true;
        }

        HashSet<String> run(DictNode n) {
            for (int idx = cols; idx < cells.length - cols; ++idx) {
                if (!cells[idx].isAvailable) continue;
                dfs(n, idx);
            }
            return results;
        }
    }

    private DictNode root;

    public BoggleSolver(String[] dictionary) {
        root = new DictNode();
        for (String s : dictionary) {
            root.insert(s, 0);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard boggleBoard) {
        Solver solver = new Solver(boggleBoard);
        return solver.run(root);
    }

    public int scoreOf(String word) {
        if (!root.find(word, 0)) return 0;
        int l = word.length();
        if (l <= 2) return 0;
        if (l <= 4) return 1;
        if (l <= 5) return 2;
        if (l <= 6) return 3;
        if (l <= 7) return 5;
        return 11;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In("../testcase/dictionary-yawl.txt");
        BoggleSolver solver = new BoggleSolver(in.readAllStrings());

        final String[] TESTCASES = {"board-16q.txt", "board4x4.txt", "board-antidisestablishmentarianisms.txt", "board-aqua.txt", "board-couscous.txt", "board-diagonal.txt", "board-dichlorodiphenyltrichloroethanes.txt", "board-dodo.txt", "board-estrangers.txt", "board-horizontal.txt", "board-inconsequentially.txt", "board-noon.txt", "board-pneumonoultramicroscopicsilicovolcanoconiosis.txt", "board-points0.txt", "board-points1000.txt", "board-points100.txt", "board-points1111.txt", "board-points1250.txt", "board-points13464.txt", "board-points1500.txt", "board-points1.txt", "board-points2000.txt", "board-points200.txt", "board-points26539.txt", "board-points2.txt", "board-points300.txt", "board-points3.txt", "board-points400.txt", "board-points4410.txt", "board-points4527.txt", "board-points4540.txt", "board-points4.txt", "board-points500.txt", "board-points5.txt", "board-points750.txt", "board-points777.txt", "board-q.txt", "board-quinquevalencies.txt", "board-qwerty.txt", "board-rotavator.txt", "board-vertical.txt"};
        for (String testcase : TESTCASES) {
            BoggleBoard board = new BoggleBoard("../testcase/" + testcase);

            int score = 0;
            for (String word : solver.getAllValidWords(board)) {
                // StdOut.println(word);
                score += solver.scoreOf(word);
            }
            StdOut.printf("%35s\t\t%d\n", testcase, score);
        }
    }
}
