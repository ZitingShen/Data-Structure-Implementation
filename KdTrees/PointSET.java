/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/20/2015
 *
 * Compilation:  javac-algs4 PointSET.java
 * Execution: java-algs4 PointSET
 * Dependencies: Point2D.java  RectHV.java  SET.java  StdDraw.java
 *
 * Description: Use brute force to do 2d search among a group of points.
 *
 *************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> points;
    
    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return points.isEmpty();
    }
    
    // number of points in the set
    public int size() {
        return points.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        points.add(p);
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return points.contains(p);
    }
    
    // draw all points to standard draw 
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p: points) 
            p.draw();
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        SET<Point2D> insidePoints = new SET<Point2D>();
        for (Point2D p: points)
            if (rect.contains(p)) insidePoints.add(p);
        return insidePoints;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        Point2D nearest = null;
        for (Point2D i: points)
            if (nearest == null || sqDistance(i, p) < sqDistance(nearest, p))
                nearest = i;
        return nearest;
    }
    
    private double sqDistance(Point2D p1, Point2D p2) {
        return (p1.x() - p2.x())*(p1.x() - p2.x()) 
            + (p1.y() - p2.y())*(p1.y() - p2.y());
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        PointSET set = new PointSET();
        set.insert(new Point2D(0.2, 0.3));
        set.insert(new Point2D(0.3, 0.3));
        set.draw();
        System.out.println(set.nearest(new Point2D(0.4, 0.3)));
        for (Point2D p: set.range(new RectHV(0.3, 0.3, 0.3, 0.3)))
            System.out.println(p);
    }
}
