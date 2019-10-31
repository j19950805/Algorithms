/**
 * My trie set for 26 uppercase Alphabets.
 */
public class MyTrieSET {
    private Node root;

    private static class Node {
        private boolean isKey = false;
        private Node[] next = new Node[91];
    }

    public MyTrieSET() {

    }

    public boolean contains(String key) {
        Node x = get(root, key, 0);
        return x != null && x.isKey;
    }

    public boolean[] validPrefixOrWord(String prefix) {
        Node x = get(root, prefix, 0);
        if (x != null) {
            if (x.isKey) {
                return new boolean[]{true, true};
            } else {
                return new boolean[]{true, false};
            }
        }
        return new boolean[] {false, false};
    }


    public void add(String key) {
        root = add(key, root, 0);
    }

    private Node add(String key, Node x, int d) {
        if (x == null) {x = new Node();}
        if (d == key.length()) {
            x.isKey = true;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = add(key, x.next[c], d + 1);
        return x;
    }


    private Node get(Node x, String prefix, int d) {
        if (x == null) {return null;}
        if (d == prefix.length()) {
            return x;
        }
        return get(x.next[prefix.charAt(d)], prefix, d + 1);
    }

}
