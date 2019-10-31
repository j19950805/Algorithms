
import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final MyTrieSET dictionaryTrie;
    private final int[][] ADJACENT = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictionaryTrie = new MyTrieSET();
        for (String word: dictionary) {
            if (word.length() > 2 && word.length() <= 16) {
                dictionaryTrie.add(word);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> result = new HashSet<>();
        int col = board.cols();
        int row = board.rows();
        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                boolean [][] onStack = new boolean[col][row];
                dfs(x, y, "", board, result, onStack);
            }
        }
        return result;
    }

    private void dfs(int col, int row, String prefix, BoggleBoard board,
                     Set<String> result, boolean[][] onStack) {
        if (col < 0 || row < 0 || col >= board.cols() || row >= board.rows()
            || onStack[col][row]) {
            return;
        }
        prefix += board.getLetter(col, row);
        boolean[] validPrefixOrWord = dictionaryTrie.validPrefixOrWord(prefix);
        if (!validPrefixOrWord[0]) {
            return;
        }
        if (validPrefixOrWord[1]) {
            result.add(prefix);
        }
        onStack[col][row] = true;
        for (int[] adj: ADJACENT) {
            dfs(col + adj[0], col + adj[1], prefix, board, result, onStack);
        }
        onStack[col][row] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dictionaryTrie.contains(word)) {
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

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
