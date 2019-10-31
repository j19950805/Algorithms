/**
 * My trie set for 26 uppercase Alphabets.
 */
public class MyTrieSET {
    public Node root;

    public static class Node {
        public boolean isKey = false;
        public Node[] next = new Node[26];
    }

    public MyTrieSET() {

    }

    public boolean contains(String key) {
        Node x = get(root, key, 0);
        return x != null && x.isKey;
    }



    public void add(String key) {
        root = add(key, root, 0);
    }

    private Node add(String key, Node x, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.isKey = true;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = add(key, x.next[c], d + 1);
        return x;
    }

    private Node get(Node x, String prefix, int d) {
        if (x == null) {return null;}
        if (d == prefix.length()) {
            return x;
        }
        return get(x.next[prefix.charAt(d) - 'A'], prefix, d + 1);
    }

}
