
public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDistanceSum = 0;
        String outcast = null;
        for (String noun : nouns) {
            int distanceSum = 0;
            for (String s : nouns) {
                distanceSum += wordNet.distance(noun, s);
            }
            if (distanceSum > maxDistanceSum) {
                maxDistanceSum = distanceSum;
                outcast = noun;
            }
        }
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String[] a = {"horse", "zebra", "cat", "bear", "table"};
        System.out.println("a: " + outcast.outcast(a));
        String[] b = {"water", "soda", "bed", "orange_juice", "milk", "apple_juice", "tea", "coffee"};
        System.out.println("b: " + outcast.outcast(b));
        String[] c = {"apple", "pear", "peach", "banana", "lime", "lemon", "blueberry",
                "strawberry", "mango", "watermelon", "potato"};
        System.out.println("c: " + outcast.outcast(c));
    }
}
