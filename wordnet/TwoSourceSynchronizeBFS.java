import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;

public class TwoSourceSynchronizeBFS {
    private int sca; // shortest common ancestor
    private int sapLength;
    private HashMap<Integer, Integer> distToV;
    private HashMap<Integer, Integer> distToW;
    private HashMap<Integer, Character> marked; // value: 'v', 'w', 'b' (marked by both -> b)

    public TwoSourceSynchronizeBFS(Digraph G, Iterable<Integer> v, Iterable<Integer> w,
                                   HashMap<Integer, HashMap<Integer, int[]>> cache) {
        sca = -1;
        sapLength = Integer.MAX_VALUE;
        distToV = new HashMap<>();
        distToW = new HashMap<>();
        marked = new HashMap<>();
        Queue<Integer> q = new Queue<>();

        for (int i: v) {
            for (int j: w) {
                if (i == j) {
                    sca = i;
                    sapLength = 0;
                    return;
                }
                if (cache.containsKey(i) && cache.get(i).containsKey(j)) {
                    int[] cacheInfo = cache.get(i).get(j);
                    if (cacheInfo[0] < sapLength) {
                        sapLength = cacheInfo[0];
                        sca = cacheInfo[1];
                    }
                } else if (cache.containsKey(j) && cache.get(j).containsKey(i)) {
                    int[] cacheInfo = cache.get(j).get(i);
                    if (cacheInfo[0] < sapLength) {
                        sapLength = cacheInfo[0];
                        sca = cacheInfo[1];
                    }
                } else {
                    marked.put(i, 'v');
                    marked.put(j, 'w');
                    distToV.put(i, 0);
                    distToW.put(j, 0);
                    q.enqueue(i);
                    q.enqueue(j);
                }
            }
        }

        while (!q.isEmpty()) {
            int p = q.dequeue();
            char mark = marked.get(p);
            if (mark == 'v') {
                bfs(G, p, q, distToV, distToW);
            } else if (mark == 'w') {
                bfs(G, p, q, distToW, distToV);
            } else {
                bfsBoth(G, p, q);
            }
        }
    }

    public TwoSourceSynchronizeBFS(Digraph G, int v, int w) {
        if (v == w) {
            sca = v;
            sapLength = 0;
            return;
        }
        sca = -1;
        sapLength = Integer.MAX_VALUE;
        distToV = new HashMap<>();
        distToW = new HashMap<>();
        marked = new HashMap<>();
        Queue<Integer> q = new Queue<>();

        marked.put(v, 'v');
        marked.put(w, 'w');
        distToV.put(v, 0);
        distToW.put(w, 0);
        q.enqueue(v);
        q.enqueue(w);

        while (!q.isEmpty()) {
            int p = q.dequeue();
            char mark = marked.get(p);
            if (mark == 'v') {
                bfs(G, p, q, distToV, distToW);
            } else if (mark == 'w') {
                bfs(G, p, q, distToW, distToV);
            } else {
                bfsBoth(G, p, q);
            }
        }
    }

    private void bfs(Digraph G, int p, Queue<Integer> q,
                         HashMap<Integer, Integer> distTo1, HashMap<Integer, Integer> distTo2) {
        for (int adj: G.adj(p)) {
            if (marked.containsKey(adj)) {
                char mark = marked.get(adj);
                if (mark == marked.get(p) || mark == 'b') {
                    continue;
                }
                marked.replace(adj, 'b');
                distTo1.put(adj, distTo1.get(p) + 1);
                int length = distTo1.get(adj) + distTo2.get(adj);
                if (length < sapLength) {
                    sca = adj;
                    sapLength = length;
                }
            } else {
                marked.put(adj, marked.get(p));
                distTo1.put(adj, distTo1.get(p) + 1);
            }

            if (distTo1.get(adj) < sapLength) {
                q.enqueue(adj);
            }

        }
    }

    private void bfsBoth(Digraph G, int p, Queue<Integer> q) {
        for (int adj: G.adj(p)) {
            if (marked.containsKey(adj)) {
                char mark = marked.get(adj);
                if (mark == 'v') {
                    marked.replace(adj, 'b');
                    distToW.put(adj, distToW.get(p) + 1);
                } else if (mark == 'w') {
                    marked.replace(adj, 'b');
                    distToV.put(adj, distToV.get(p) + 1);
                } else {
                    continue;
                }
                int length = distToV.get(adj) + distToW.get(adj);
                if (length < sapLength) {
                    sca = adj;
                    sapLength = length;
                }
            } else {
                marked.put(adj, 'b');
                distToV.put(adj, distToV.get(p) + 1);
                distToW.put(adj, distToW.get(p) + 1);
            }

            if (distToV.get(adj) < sapLength || distToW.get(adj) < sapLength) {
                q.enqueue(adj);
            }
        }
    }


    public int sca() {
        return sca;
    }

    public int getSapLength() {
        return sca == -1 ? sca : sapLength;
    }

}
