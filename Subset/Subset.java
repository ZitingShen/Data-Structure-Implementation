/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/12/2015
 *
 * Compilation:  javac-algs4 Subset.java
 * Execution: echo (Strings) | java-algs4 Subset (integer k)
 * Dependencies: StdIn.java  StdRandom.java  RandomizedQueue.java
 *
 * Description: Given a set of strings, print out a subset of size k which
 * is uniformly randomly chosen from the given set.
 *
 *************************************************************************/

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> strings = new RandomizedQueue<String>();
        if (k != 0) {
            for (int i = 0; i < k; i++) 
                strings.enqueue(StdIn.readString());
            int extra = k + 1;
            while (!StdIn.isEmpty()) {
                int j = StdRandom.uniform(extra);
                if (j < k) {
                    strings.dequeue();
                    strings.enqueue(StdIn.readString());
                } else StdIn.readString();
                extra++;
            }
            while (!strings.isEmpty()) System.out.println(strings.dequeue());
        }
    }
}