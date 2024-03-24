import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
  private SET<Point2D> points;
  // construct an empty set of points
  public PointSET() {
    this.points = new SET<Point2D>();
  }

  // is the set empty?
  public boolean isEmpty() {
    return this.points.isEmpty();
  }

  // number of points in the set
  public int size() {
    return this.points.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("called insert() with a null key");
    }
    if (this.points.contains(p)) {
      return;
    }
    this.points.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) { 
      throw new IllegalArgumentException("called contains() with a null key");
    }
    return this.points.contains(p);
  }

  // draw all points to standard draw 
  public void draw() {
    this.points.iterator().forEachRemaining(point -> {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      point.draw();
    });
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException("called range() with a null key");
    }
    List<Point2D> rectPoints = new ArrayList<Point2D>();
    while (this.points.iterator().hasNext()) {
      Point2D newPoint = this.points.iterator().next();
      if (rect.contains(newPoint)) {
        rectPoints.add(newPoint);
      }
    }
    return rectPoints;
  }
  
  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("called nearest() with a null key");
    }
    if (this.points.isEmpty()) {
      return null;
    }
    double distance = 0.0;
    Point2D nearestPoint = null;
    while (this.points.iterator().hasNext()) {
      Point2D newPoint = this.points.iterator().next();
      double dist = p.distanceTo(newPoint);
      if (nearestPoint == null || distance >= dist) {
        distance = dist;
        nearestPoint = newPoint;
      }
    }
    return nearestPoint;
  }

  // unit testing of the methods (optional) 
  public static void main(String[] args) {
    // initialize the data structures from file
    String filename = args[0];
    In in = new In(filename);
    PointSET brute = new PointSET();
    while (!in.isEmpty()) {
        double x = in.readDouble();
        double y = in.readDouble();
        Point2D p = new Point2D(x, y);
        brute.insert(p);
    }
    
    // test draw
    brute.draw();
    StdDraw.show();
  }
}