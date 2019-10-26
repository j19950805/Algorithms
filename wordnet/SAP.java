import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class SAP {
    private final Digraph G;
    private final HashMap<IdPair, int[]> sapCache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.G = G;
        sapCache = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validVertex(v);
        validVertex(w);
        if (v == w) {
            return 0;
        }
        IdPair idp = new IdPair(v, w);
        if (sapCache.containsKey(idp)) {
            return sapCache.get(idp)[0];
        }
        TwoSourceSynchronizeBFS bfs = new TwoSourceSynchronizeBFS(G, v, w);
        sapCache.put(idp, new int[]{bfs.getSapLength(), bfs.sca()});
        return bfs.getSapLength();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validVertex(v);
        validVertex(w);
        if (v == w) {
            return v;
        }
        IdPair idp = new IdPair(v, w);
        if (sapCache.containsKey(idp)) {
            return sapCache.get(idp)[1];
        }
        TwoSourceSynchronizeBFS bfs = new TwoSourceSynchronizeBFS(G, v, w);
        sapCache.put(idp, new int[]{bfs.getSapLength(), bfs.sca()});
        return bfs.sca();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validIterable(v);
        validIterable(w);
        TwoSourceSynchronizeBFS bfs = new TwoSourceSynchronizeBFS(G, v, w, sapCache);
        return bfs.getSapLength();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validIterable(v);
        validIterable(w);
        TwoSourceSynchronizeBFS bfs = new TwoSourceSynchronizeBFS(G, v, w, sapCache);
        return bfs.sca();
    }

    private void validVertex(int v) {
        if (v < 0 || v >= G.V()) {
            throw new IllegalArgumentException();
        }
    }

    private void validIterable(Iterable<Integer> iterable) {
        if (iterable == null) {
            throw new IllegalArgumentException();
        }
        for (Integer i: iterable) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
    }




    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int length   = sap.length(1, 5);
        int ancestor = sap.ancestor(1, 5);
        System.out.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
