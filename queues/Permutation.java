public class Permutation {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("You must provide command-line arguments.");
            System.out.println("Ex. number < filename.");
            System.exit(0);
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
            System.exit(0);
        }
        int k = Integer.parseInt(args[0]);
        In in = new In(args[2]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!in.isEmpty()) {
            rq.enqueue(in.readString());
        }
        if (rq.size() < k) {
            System.out.println("Number k is out of bound.");
            System.exit(0);
        }
        for (int i = 0; i < k; i++) {
            System.out.println(rq.dequeue());
        }
    }
}
