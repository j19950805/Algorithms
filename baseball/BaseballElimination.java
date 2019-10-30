import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;


public class BaseballElimination {
    private final HashMap<String, Integer> teamId;
    private final int n;     // numberOfTeams
    private final int[] w;   // wins of i-team
    private final int[] l;   // loses of i-team
    private final int[] r;   // remaining games of i-team
    private final int[][] a; // number of r games between i-team and j-team
    private final HashMap<String, Stack<String>> certificateOfElimination; //cache

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        In in = new In(filename);
        n = in.readInt();
        teamId = new HashMap<>();
        w = new int[n];
        l = new int[n];
        r = new int[n];
        a = new int[n][n];
        certificateOfElimination = new HashMap<>();

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
        validTeam(team);
        if (certificateOfElimination.containsKey(team)) {
            return true;
        }
        int id = teamId.get(team);
        int maxWinOfTeam = w[id] + r[id];
        // number of teams + remaining game between teams exclude target team + virtual source & sink
        FlowNetwork G = new FlowNetwork(n * (n - 1) / 2 + 3);
        int s = G.V() - 2;  // Artificial source
        int t = G.V() - 1;  // Artificial sink
        int gameVertex = n; // Index of games after team ids
        int gameTotal = 0;  // total game between teams exclude target team
        for (int i = 0; i < n; i++) {
            if (i == id) {
                continue;
            }
            int threshold = maxWinOfTeam - w[i];
            if (threshold < 0) { // trivial elimination by i-team
                G.addEdge(new FlowEdge(s, i, Double.POSITIVE_INFINITY));
                gameTotal -= threshold;
            } else {
                G.addEdge(new FlowEdge(i, t, threshold));
            }
            for (int j = 0; j < n; j++) {
                if (j > i && j != id && a[i][j] > 0) {
                    G.addEdge(new FlowEdge(s, gameVertex, a[i][j]));
                    G.addEdge(new FlowEdge(gameVertex, i, Double.POSITIVE_INFINITY));
                    G.addEdge(new FlowEdge(gameVertex, j, Double.POSITIVE_INFINITY));
                    gameVertex++;
                    gameTotal += a[i][j];
                }
            }
        }
        FordFulkerson ff = new FordFulkerson(G, s, t);
        if (ff.value() < gameTotal) {
            Stack<String> certificate = new Stack<>();
            for (String otherTeam: teams()) {
                if (ff.inCut(teamId.get(otherTeam))) {
                    certificate.push(otherTeam);
                }
            }
            certificateOfElimination.put(team, certificate);
            return true;
        }
        return false;
    }


    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (isEliminated(team)) {
            return certificateOfElimination.get(team);
        } else {
            return null;
        }
    }


    private void validTeam(String team) {
        if (!teamId.containsKey(team)) {
            throw new IllegalArgumentException("Invalid team");
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams4b.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                System.out.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    System.out.print(t + " ");
                }
                System.out.println("}");
            }
            else {
                System.out.println(team + " is not eliminated");
            }
        }

    }
}
