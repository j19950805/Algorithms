import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {
    private LinkedList<Board> solution;
    private boolean isSolvable = false;

    private class SearchNode {
        private int move;
        private int manhattan;
        private Board b;
        private SearchNode previousBoard;

        private SearchNode(Board b, int move, SearchNode previousBoard) {
            this.b = b;
            this.move = move;
            manhattan = b.manhattan();
            this.previousBoard = previousBoard;
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Comparator<SearchNode> manhattan = (s1, s2) -> {
            return s1.move + s1.manhattan - s2.move - s2.manhattan;
        };
        MinPQ<SearchNode> pq = new MinPQ<>(manhattan);
        MinPQ<SearchNode> pqTwin = new MinPQ<>(manhattan);
        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        while (!pq.isEmpty() && !pq.min().b.isGoal() && !pqTwin.isEmpty() && !pqTwin.min().b.isGoal()) {
            SearchNode s = pq.delMin();
            SearchNode sTwin = pqTwin.delMin();
            for (Board neighbor: s.b.neighbors()) {
                if (s.previousBoard == null || !neighbor.equals(s.previousBoard.b)) {
                    pq.insert(new SearchNode(neighbor, s.move + 1, s));
                }
            }
            for (Board neighbor: sTwin.b.neighbors()) {
                if (sTwin.previousBoard == null || !neighbor.equals(sTwin.previousBoard.b)) {
                    pqTwin.insert(new SearchNode(neighbor, sTwin.move + 1, sTwin));
                }
            }
        }

        if (!pq.isEmpty() && pq.min().b.isGoal()) {
            isSolvable = true;
            solution = new LinkedList<>();
            SearchNode s = pq.delMin();
            while (s != null) {
                solution.addFirst(s.b);
                s = s.previousBoard;
            }
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable) {
            return solution.size() - 1;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        // solve the puzzle
        Board b = new Board(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}});
        Solver solver = new Solver(b.twin());

        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                System.out.println(board);
        }
    }

}
