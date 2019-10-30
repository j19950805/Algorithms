import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Stack;


class BaseballElimination {
    private HashMap<String, Integer> teamId;
    private int n;     // numberOfTeams
    private int[] w;   // wins of team-i
    private int[] l;   // loses of team-i
    private int[] r;   // remaining games of team-i
    private int[][] a; // number of r games between team-i and team-j

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        In in = new In(filename);
        n = in.readInt();
        teamId = new HashMap<>();
        w = new int[n];
        l = new int[n];
        r = new int[n];
        a = new int[n][n];

        for (int i = 0; i < n; i++) {
            teamId.put(in.readString(), i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                a[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return teamId.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        validTeam(team);
        return w[teamId.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        validTeam(team);
        return l[teamId.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validTeam(team);
        return r[teamId.get(team)];
    }

    // number of r games between team1 and team2
    public int against(String team1, String team2) {
        validTeam(team1);
        validTeam(team2);
        return a[teamId.get(team1)][teamId.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int id = teamId.get(team);
        int maxWinOfTeam = w[id] + r[id];
        int rTotal = 0;
        for (int i = 0; i < n; i++) {
            if (maxWinOfTeam < w[i]) {
                return true;
            }
            // calculate total remaining games exclude target team
            if (i != id) {
                rTotal += r[i];
            }
        }
        FordFulkerson ff = elimination(team);
        return ff.value() == rTotal;
    }


    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        FordFulkerson ff = elimination(team);
        Stack<String> R = new Stack<>();
        for (String otherTeam: teams()) {
            if (ff.inCut(teamId.get(otherTeam))) {
                R.push(otherTeam);
            }
        }
        return R;
    }

    private FordFulkerson elimination(String team) {
        validTeam(team);
        int id = teamId.get(team);
        int maxWinOfTeam = w[id] + r[id];
        FlowNetwork G = new FlowNetwork(n * (n - 1) / 2 + 3);
        int s = G.V() - 2;
        int t = G.V() - 1;
        int gameVertex = n;
        for (int i = 0; i < n; i++) {
            if (i == id) {
                break;
            }
            int threshold = maxWinOfTeam - w[i];
            G.addEdge(new FlowEdge(i, t, Math.max(threshold, 0)));
            for (int j = 0; j < n; j++) {
                if (j > i && j != id && a[i][j] > 0) {
                    G.addEdge(new FlowEdge(s, gameVertex, a[i][j]));
                    G.addEdge(new FlowEdge(gameVertex, i, Double.POSITIVE_INFINITY));
                    G.addEdge(new FlowEdge(gameVertex, j, Double.POSITIVE_INFINITY));
                    gameVertex++;
                }
            }
        }
        return new FordFulkerson(G, s, t);
    }

    private void validTeam(String team) {
        if (!teamId.containsKey(team)) {
            throw new IllegalArgumentException("Invalid team");
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams12.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}
