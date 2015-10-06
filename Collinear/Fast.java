/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/13/2015
 *
 * Compilation:  javac-algs4 Fast.java
 * Execution: java-algs4 Fast (file name)
 * Dependencies: In.java  StdOut.java  StdDraw.java  StdRandom.java 
 *               Point.java
 *
 * Description: Detect groups of four collinear points among a set of 
 * points by sorting the points by the slopes they make with a certain
 * point.
 *
 *************************************************************************/

import java.util.Comparator;

public class Fast {
    public static void main(String[] args) {
        In input = new In(args[0]);
        int N = input.readInt(); // the # of points
        if (N > 3) {
            final Point[] points = new Point[N];
            Point[] sort = new Point[N];
            
            StdDraw.setXscale(0, 32768);
            StdDraw.setYscale(0, 32768);
            for (int i = 0; i < N; i++) {
                int x = input.readInt();
                int y = input.readInt();
                points[i] = new Point(x, y);
                sort[i] = new Point(x, y);
                points[i].draw();
            }
            
            for (int i = 0; i < N; i++) {
                quickSortBySlope(sort, points[i].SLOPE_ORDER); 
                
                int index = 0;
                while (index < N - 2) {
                    int counter = 1;
                    
                    while (index + counter < N && points[i].slopeTo(sort[index]) 
                               == points[i].slopeTo(sort[index+counter])) counter++;
                    
                    if (counter >= 3) {
                        boolean repeated = false;
                        for (int j = 0; j < counter; j++) 
                            if (sort[index+j].compareTo(points[i]) < 0) {
                                repeated = true;
                                break;
                            }
                        if (!repeated) {
                            insertionSort(sort, index, index + counter - 1);
                            StdOut.print(points[i]);
                            for (int j = 0; j < counter; j++) 
                                StdOut.print(" -> " + sort[index+j]);
                            StdOut.println();
                            points[i].drawTo(sort[index+counter-1]);
                        }
                    }
                    index += counter;
                }
            }
        }
    }
    
    private static void quickSortBySlope(Point[] points, 
                                         Comparator<Point> comparator) {
        StdRandom.shuffle(points);
        quickSort(points, comparator, 0, points.length - 1);
    }
    
    private static void quickSort(Point[] points, Comparator<Point> comparator, 
                                  int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(points, comparator, lo, hi);
        quickSort(points, comparator, lo, j - 1);
        quickSort(points, comparator, j + 1, hi);
    }
    
    private static int partition(Point[] points, Comparator<Point> comparator, 
                                 int lo, int hi) {
        int i = lo, j = hi + 1;
        while (true) {
            while (comparator.compare(points[++i], points[lo]) < 0)
                if (i == hi) break;
            while (comparator.compare(points[lo], points[--j]) < 0)
                if (j == lo) break;
            if (i >= j) break;
            swap(points, i, j);
        }
        swap(points, lo, j);
        return j;
    }
    
    private static void insertionSort(Point[] points, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo; j--)
                if (points[j].compareTo(points[j-1]) < 0) swap(points, j, j-1);
    }
    
    private static void swap(Point[] points, int i, int j) {
        Point temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
}