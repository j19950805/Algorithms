
import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private int n;
    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        n = tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(n);
        for (int i = 0; i < n; i++) {
            boardString.append(System.lineSeparator());
            for (int j = 0; j < n; j++) {
                boardString.append(" ").append(tiles[i][j]);
            }
        }
        return boardString.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }


    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tileNum = tiles[i][j];
                if (tileNum != 0 && tileNum != posNum(i, j)) {
                    hamming += 1;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tileNum = tiles[i][j];
                if (tileNum != 0 && tileNum != posNum(i, j)) {
                    manhattan += Math.abs((tileNum - 1) / n - i) + Math.abs((tileNum - 1) % n - j);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (this.n == other.n && Arrays.deepEquals(this.tiles, other.tiles)) {
            return true;
        }
        return false;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<>();
        int[] zeroPos = zeroPos();
        int i = zeroPos[0];
        int j = zeroPos[1];
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] dir: directions) {
            int slideRow = i + dir[0];
            int slideCol = j + dir[1];
            if (slideRow < 0 || slideRow >= n || slideCol < 0 || slideCol >= n) {
                continue;
            }
            int[][] copy = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
            copy[i][j] = copy[slideRow][slideCol];
            copy[slideRow][slideCol] = 0;
            neighbors.add(new Board(copy));
        }

        return neighbors;
    }

    // number of tiles out of place
    private int[] zeroPos() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n; j++) {
                if (copy[i][j] != 0 && copy[i][j - 1] != 0) {
                    int temp = copy[i][j];
                    copy[i][j] = copy[i][j - 1];
                    copy[i][j - 1] = temp;
                    return new Board(copy);
                }
            }
        }
        return null;
    }

    private int posNum(int row, int col) {
        return (row * n + col + 1) % (n * n);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles1 = {{5, 8, 7}, {1, 4, 6}, {3, 0, 2}};
        Board b1 = new Board(tiles1);
        System.out.print(b1.manhattan());
    }
}