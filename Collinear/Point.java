/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/13/2015
 *
 * Compilation:  javac-algs4 Point.java
 * Execution: java-algs4 Point
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;
import java.util.Arrays;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y == that.y && this.x == that.x) return -1 / 0.0;
        if (this.y == that.y) return 0.0;
        if (this.x == that.x) return 1 / 0.0;
        return (that.y - this.y) / (double) (that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y < that.y) return -1;
        if (this.y > that.y) return 1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return 1;
        return 0;
    }
    
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point v, Point w) {
            return Double.compare(Point.this.slopeTo(v), Point.this.slopeTo(w));
        }
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point a = new Point(2, 3);
        Point[] collections = {new Point(2, 3), new Point(1, 2), new Point(4, 5), new Point(2, 4), 
            new Point(2, 2), new Point(3, 3)};
        Arrays.sort(collections, a.SLOPE_ORDER);
        for (Point p: collections) {
            System.out.println(a.slopeTo(p));
//            System.out.println(a.compareTo(p));
            System.out.println(p);
        }
        Point i = new Point(7000, 3000);
        Point j = new Point(10000, 0);
        System.out.println(i.compareTo(j));
    }
}