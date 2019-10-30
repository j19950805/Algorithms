import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WordNet {
    private final HashMap<String, List<Integer>> wordToId;
    private final HashMap<Integer, String[]> idToWord;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Input fileName is Null.");
        }

        In synsetsFile = new In(synsets);
        In hypernymsFile = new In(hypernyms);
        wordToId = new HashMap<>();
        idToWord = new HashMap<>();
        int root = -1;

        // Read in synsets information
        while (synsetsFile.hasNextLine()) {
            String[] synsetINFO = synsetsFile.readLine().split(",");
            int id = Integer.parseInt(synsetINFO[0]);
            String[] words = synsetINFO[1].split(" ");
            idToWord.put(id, words);
            for (String word: words) {
                if (wordToId.containsKey(word)) {
                    wordToId.get(word).add(id);
                } else {
                    LinkedList<Integer> synsetIds = new LinkedList<>();
                    synsetIds.add(id);
                    wordToId.put(word, synsetIds);
                }
            }
        }

        // Build hypernyms relationships in WordNet graph
        Digraph wordNet = new Digraph(idToWord.size());
        while (hypernymsFile.hasNextLine()) {
            String[] hypernymsINFO = hypernymsFile.readLine().split(",");
            int synsetId = Integer.parseInt(hypernymsINFO[0]);

            // A synset without any hypernyms is root, only one root allowed.
            if (hypernymsINFO.length == 1) {
                if (root < 0) {
                    root = synsetId;
                } else {
                    throw new IllegalArgumentException("More than one root exist.");
                }
            }

            for (int i = 1; i < hypernymsINFO.length; i++) {
                int hyponymId = Integer.parseInt(hypernymsINFO[i]);
                wordNet.addEdge(synsetId, hyponymId);
            }
        }


        // No root -> Invalid WordNet
        if (root < 0) {
            throw new IllegalArgumentException("No root exist.");
        }

        sap = new SAP(wordNet);

        // Cycle detection, Valid WordNet is acyclic.
        boolean[] marked = new boolean[wordNet.V()];
        boolean[] onStack = new boolean[wordNet.V()];
        for (int v = 0; v < wordNet.V(); v++) {
            if (!marked[v]) {
                dfs(v, wordNet, marked, onStack);
            }
        }
    }


    private void dfs(int v, Digraph G, boolean[] marked, boolean[] onStack) {
        marked[v] = true;
        onStack[v] = true;
        for (int w: G.adj(v)) {
            if (marked[w] && onStack[w]) {
                throw new IllegalArgumentException("WordNet has cycle.");
            }
            dfs(w, G, marked, onStack);
        }
        onStack[v] = false;
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Input word is null.");
        }
        return wordToId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Argument is not a wordNet noun." + nounA + nounB);
        }
        List<Integer> a = wordToId.get(nounA);
        List<Integer> b = wordToId.get(nounB);
        if (a.size() == 1 && b.size() == 1) {
            return sap.length(a.get(0), b.get(0));
        }
        return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) && !isNoun(nounB)) {
            throw new IllegalArgumentException("Argument is not a WordNet noun." + nounA + nounB);
        }
        List<Integer> a = wordToId.get(nounA);
        List<Integer> b = wordToId.get(nounB);
        int caId = -1;
        if (a.size() == 1 && b.size() == 1) {
            caId = sap.ancestor(a.get(0), b.get(0));
        } else {
            caId = sap.ancestor(a, b);
        }
        StringBuilder synset = new StringBuilder();
        for (String word: idToWord.get(caId)) {
            synset.append(" ").append(word);
        }
        synset.delete(0, 1);
        return synset.toString();
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        List<Integer> a = new LinkedList<>();
        List<Integer> b = new LinkedList<>();
        a.add(29104);
        a.add(51703);
        b.add(40858);
        b.add(72642);
        int length   = wordnet.sap.length(a, b);
        int ancestor = wordnet.sap.ancestor(a, b);
        System.out.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
