import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Board {
    private int n;
    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
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
                if (tiles[i][j] != 0 && tiles[i][j] != posNum(i, j)) {
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
                manhattan += manhattan(tiles[i][j], i, j);
            }
        }
        return manhattan;
    }

    private int manhattan(int tileNum, int row, int col) {
       if (tileNum == posNum(row, col) || tileNum == 0) {
           return 0;
       }
       return Math.abs(tileNum / n - row) + Math.abs(tileNum % n - 1 - col);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (this.n == other.n || Arrays.deepEquals(this.tiles, other.tiles)) {
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<>();
        int[] zeroPos = zeroPos(tiles);
        int i = zeroPos[0];
        int j = zeroPos[1];
        int[][] directions = new int[][] {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] dir: directions) {
            int slideRow = i + dir[0];
            int slideCol = j + dir[1];
            if (slideRow < 0 || slideRow >= n ||slideCol < 0 || slideCol >= n) {
                continue;
            }
            int[][] copy = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
            copy[i][j] = copy[slideRow][slideCol];
            copy[slideRow][slideCol] = 0;
            neighbors.add(new Board(copy));
        }

        return neighbors;
    }

    private int[] zeroPos(int[][] tiles) {
        for (int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    return new int[] {i, j};
                }
            }
        }
        throw new IllegalArgumentException("Tiles missing blank");
    }



    private void slideTile(int row, int col) {
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    private int posNum (int row, int col) {
        return (row * n + col + 1) % (n * n);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles1 = new int[][] {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board b1 = new Board(tiles1);
        System.out.print(b1);
        b1.neighbors();
        Iterator iter = b1.neighbors().iterator();
        while (iter.hasNext()) {
            System.out.println();
            System.out.print(iter.next());
        }
    }
}