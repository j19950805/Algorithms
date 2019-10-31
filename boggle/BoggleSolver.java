import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final int[][] adjacent = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    private Node root;

    private static class Node {
        private boolean isKey = false;
        private Node[] next = new Node[26];
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word: dictionary) {
            if (word.length() > 2) {
                addWord(word);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> result = new HashSet<>();
        int row = board.rows();
        int col = board.cols();
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                boolean [][] onStack = new boolean[row][col];
                dfs(x, y, root, "", board, result, onStack);
            }
        }
        return result;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (containsWord(word)) {
            int length = word.length();
            if (length < 5) {
                return 1;
            } else if (length == 5) {
                return 2;
            } else if (length == 6) {
                return 3;
            } else if (length == 7) {
                return 5;
            } else {
                return 11;
            }
        }
        return  0;
    }

    private void dfs(int row, int col, Node trieNode, String prefix,
                     BoggleBoard board, Set<String> result, boolean[][] onStack) {
        if (col < 0 || row < 0 || col >= board.cols() || row >= board.rows()
            || onStack[row][col] || trieNode == null) {
            return;
        }
        char c = board.getLetter(row, col);
        prefix += c;
        trieNode = trieNode.next[c - 'A'];
        if (trieNode == null) {
            return;
        }
        if (c == 'Q') {
            prefix += 'U';
            trieNode = trieNode.next['U' - 'A'];
        }
        if (trieNode == null) {
            return;
        }
        if (trieNode.isKey) {
            result.add(prefix);
        }
        onStack[row][col] = true;
        for (int[] adj: adjacent) {
            dfs(row + adj[0], col + adj[1], trieNode, prefix, board, result, onStack);
        }
        onStack[row][col] = false;
    }


    private boolean containsWord(String word) {
        Node x = get(root, word, 0);
        return x != null && x.isKey;
    }

    private void addWord(String key) {
        root = addWord(key, root, 0);
    }

    private Node addWord(String key, Node x, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.isKey = true;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = addWord(key, x.next[c], d + 1);
        return x;
    }

    private Node get(Node x, String prefix, int d) {
        if (x == null) {
            return null;
        }
        if (d == prefix.length()) {
            return x;
        }
        return get(x.next[prefix.charAt(d) - 'A'], prefix, d + 1);
    }
}
