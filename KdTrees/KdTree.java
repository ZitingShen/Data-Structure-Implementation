/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/22/2015
 *
 * Compilation:  javac-algs4 KdTree.java
 * Execution: java-algs4 KdTree
 * Dependencies: Point2D.java  RectHV.java  SET.java  StdDraw.java
 *
 * Description: Implement Kd-tree to do 2d search among a group of points.
 *
 *************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private SET<Point2D> set;
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean v;
        
        private Node(Point2D point, double xmin, double ymin, 
                     double xmax, double ymax, boolean isVertical) {
            p = point;
            rect = new RectHV(xmin, ymin, xmax, ymax);
            lb = null;
            rt = null;
            v = isVertical;
        }
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return root == null;
    }
    
    // number of points in the set
    public int size() {
        return size;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        root = insert(p, root, 0, 1, 0, 1, true);
    }
    
    private Node insert(Point2D p, Node n, double lb, double rb, double bb,
                        double tb, boolean isVertical) {
        if (n == null) {
            size++;
            return new Node(p, lb, bb, rb, tb, isVertical);
        } else if (isVertical) {
            if (p.equals(n.p)) return n;
            if (p.x() < n.p.x()) n.lb = insert(p, n.lb, lb, n.p.x(), 
                                               bb, tb, !isVertical);
            else n.rt = insert(p, n.rt, n.p.x(), rb, bb, tb, !isVertical);
            return n;
        } else {
            if (p.equals(n.p)) return n;
            if (p.y() < n.p.y()) n.lb = insert(p, n.lb, lb, rb, bb, 
                                               n.p.y(), !isVertical);
            else n.rt = insert(p, n.rt, lb, rb, n.p.y(), tb, !isVertical);
            return n;
        }
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return contains(p, root);
    }
    
    private boolean contains(Point2D p, Node n) {
        if (n == null) return false;
        if (n.v) {
            if (p.equals(n.p)) return true;
            if (p.x() < n.p.x()) return contains(p, n.lb);
            return contains(p, n.rt);    
        }
        if (p.equals(n.p)) return true;
        if (p.y() < n.p.y()) return contains(p, n.lb);
        return contains(p, n.rt);
    }
    
    // draw all points to standard draw 
    public void draw() {
        draw(root);
    }
    
    private void draw(Node n) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        n.p.draw();
        if (n.v) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            draw(n.lb);
            draw(n.rt);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            draw(n.lb);
            draw(n.rt);
        }
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        set = new SET<Point2D>();
        if (root != null) range(rect, root);
        return set;
    }
    
    private void range(RectHV rect, Node n) {
        if (rect.contains(n.p)) set.add(n.p);
        if (n.lb != null && rect.intersects(n.lb.rect)) range(rect, n.lb);
        if (n.rt != null && rect.intersects(n.rt.rect)) range(rect, n.rt);
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        if (root == null) return null;
        return nearest(root.p, root, p);
    }
    
    private Point2D nearest(Point2D nearest, Node n, Point2D p) {
        if (n == null) return nearest;
        Point2D nearer = nearest;
        if (sqDistance(n.p, p) < sqDistance(nearer, p)) nearer = n.p;
        if (n.v) {
            if (p.x() < n.p.x()) {
                nearer = nearest(nearer, n.lb, p);
                if (n.rt != null && n.rt.rect.xmin() - p.x() < p.distanceTo(nearer))
                    nearer = nearest(nearer, n.rt, p);
            } else {
                nearer = nearest(nearer, n.rt, p);
                if (n.lb != null && p.x() - n.lb.rect.xmax() < p.distanceTo(nearer))
                    nearer = nearest(nearer, n.lb, p);
            }
        } else {
            if (p.y() < n.p.y()) {
                nearer = nearest(nearer, n.lb, p);
                if (n.rt != null && n.rt.rect.ymin() - p.y() < p.distanceTo(nearer))
                    nearer = nearest(nearer, n.rt, p);
            } else {
                nearer = nearest(nearer, n.rt, p);
                if (n.lb != null && p.y() - n.lb.rect.ymax() < p.distanceTo(nearer))
                    nearer = nearest(nearer, n.lb, p);
            }
        }
        return nearer;
    }
    
    private double sqDistance(Point2D p1, Point2D p2) {
        return (p1.x() - p2.x())*(p1.x() - p2.x()) 
            + (p1.y() - p2.y())*(p1.y() - p2.y());
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        KdTree set = new KdTree();
        set.insert(new Point2D(.2, .3));
        set.insert(new Point2D(.3, .3));
        set.insert(new Point2D(.6, .7));
        set.insert(new Point2D(.8, .5));
//        System.out.println(set.contains(new Point2D(.4, .3)));
//        set.draw();
//        for (Point2D p: set.range(new RectHV(.2, .2, .8, .8)))
//            System.out.println(p);
        System.out.println(set.nearest(new Point2D(.4, .3)));
    }
}
