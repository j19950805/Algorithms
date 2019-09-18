import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
       if (args.length == 1) {
           try {
               int k = Integer.parseInt(args[0]);
               RandomizedQueue<String> rq = new RandomizedQueue<>();
               while (!StdIn.isEmpty()) {
                   rq.enqueue(StdIn.readString());
               }
               if (rq.size() < k) {
                   System.out.println("Number k is out of bound.");
               } else {
                   for (int i = 0; i < k; i++) {
                       System.out.println(rq.dequeue());
                   }
               }
           } catch (NumberFormatException e) {
               System.out.println("Invalid number input.");
           }
        } else {
           System.out.println("You must provide command-line arguments.");
           System.out.println("Ex. number < filename.");
       }
    }
}
