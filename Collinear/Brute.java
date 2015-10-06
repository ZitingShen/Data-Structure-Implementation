/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/13/2015
 *
 * Compilation:  javac-algs4 Brute.java
 * Execution: java-algs4 Brute (file name)
 * Dependencies: In.java  StdOut.java  StdDraw.java  StdRandom.java 
 *               Point.java
 *
 * Description: Use brute force to detect groups of four collinear points
 * among a set of points.
 *
 *************************************************************************/

public class Brute {
    public static void main(String[] args) {
        In input = new In(args[0]);
        int N = input.readInt(); // the # of points
        Point[] points = new Point[N];

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (int i = 0; i < N; i++) {
            points[i] = new Point(input.readInt(), input.readInt());
            points[i].draw();
        }
        if (N > 3 && N < 11) insertionSort(points);
        else quickSort(points);
        for (int p = 0; p < N; p++)
            for (int q = p + 1; q < N; q++) {
                double pq = points[p].slopeTo(points[q]);
                for (int r = q + 1; r < N; r++)
                    if (pq == points[p].slopeTo(points[r]))
                        for (int s = r + 1; s < N; s++)
                            if (pq == points[p].slopeTo(points[s])) {
                                StdOut.println(points[p] + " -> " + points[q]
                                               + " -> " + points[r] + " -> "
                                               + points[s]);
                                points[p].drawTo(points[s]);
                            }
            }
    }
    
    private static void insertionSort(Point[] points) {
        for (int i = 0; i < points.length; i++)
            for (int j = i; j > 0; j--)
                if (points[j].compareTo(points[j-1]) < 0) swap(points, j, j-1);
    }
    
    private static void quickSort(Point[] points) {
        StdRandom.shuffle(points);
        quickSort(points, 0, points.length - 1);
    }
    
    private static void quickSort(Point[] points, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(points, lo, hi);
        quickSort(points, lo, j - 1);
        quickSort(points, j + 1, hi);
    }
    
    private static int partition(Point[] points, int lo, int hi) {
        int i = lo, j = hi + 1;
        while (true) {
            while (points[++i].compareTo(points[lo]) < 0)
                if (i == hi) break;
            while (points[lo].compareTo(points[--j]) < 0)
                if (j == lo) break;
            if (i >= j) break;
            swap(points, i, j);
        }
        swap(points, lo, j);
        return j;
    }
    
    private static void swap(Point[] points, int i, int j) {
        Point temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
}