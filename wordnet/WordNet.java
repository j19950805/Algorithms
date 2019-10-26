import edu.princeton.cs.algs4.Digraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class WordNet {
    private HashMap<String, List<Integer>> wordToId;
    private Digraph wordNet;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        wordToId = new HashMap<>();
        int size = 0;

        In synsetsFile = new In(synsets);
        while (synsetsFile.hasNextLine()) {
            String[] synsetINFO = synsetsFile.readLine().split(",");
            int id = Integer.parseInt(synsetINFO[0]);
            String synset = synsetINFO[1];
            if (wordToId.containsKey(synset)) {
                wordToId.get(synset).add(id);
            } else {
                LinkedList<Integer> synsetIds = new LinkedList<>();
                synsetIds.add(id);
                wordToId.put(synset, synsetIds);
            }
            size ++;
        }

        wordNet = new Digraph(size);

        In hypernymsFile = new In(hypernyms);
        while (hypernymsFile.hasNextLine()) {
            String[] hypernymsINFO = synsetsFile.readLine().split(",");
            int synsetId = Integer.parseInt(hypernymsINFO[0]);
            for (int i = 1; i < hypernymsINFO.length; i++) {
                int hyponymId = Integer.parseInt(hypernymsINFO[i]);
                wordNet.addEdge(synsetId, hyponymId);
            }
        }

        if (!isDAG(wordNet)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isDAG(Digraph g) {

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordToId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
