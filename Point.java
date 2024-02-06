import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

class PointComparator implements Comparator<Point> {
  final Point point;

  public PointComparator(Point p) {
    if (p == null) {
      throw new IllegalArgumentException("There are no arguments");
    }
    point = p;
  }
 
  public int compare(Point firstPoint, Point secondPoint) {
    if (firstPoint == null || secondPoint == null) {
      throw new NullPointerException("One or both points are null");
    }
    double firstSlope = point.slopeTo(firstPoint);
    double secondSlope = point.slopeTo(secondPoint);
    double diff = Math.abs(firstSlope - secondSlope);
    if (diff < Math.pow(10, -9)) {
      return 0;
    } else if (firstSlope > secondSlope) {
      return 1;
    } else {
      return -1;
    }
  }
}

public class Point implements Comparable<Point> {
  private final int x;
  private final int y;

  // constructs the point (x, y) 
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  // Draws this point to standard draw.
  public void draw() {
      /* DO NOT MODIFY */
      StdDraw.point(x, y);
  }

  // Draws the line segment between this point and the specified point to standard draw.
  public void drawTo(Point that) {
      /* DO NOT MODIFY */
      StdDraw.line(this.x, this.y, that.x, that.y);
  }

  // Returns a string representation of this point. This method is provide for debugging; your program should not rely on the format of the string representation.
  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }

  // compare two points by y-coordinates, breaking ties by x-coordinates
  public int compareTo(Point that) {
    if (that == null) {
      throw new NullPointerException("The argument is null");
    }
    if (that.y < y) {
      return 1;
    } else if (that.y > y) {
      return -1;
    } else {
      if (that.x < x) {
        return 1;
      } else if (that.x > x) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  // the slope between this point and that point
  public double slopeTo(Point that) {
    if (that == null) {
      throw new NullPointerException("The argument is null");
    }
    double yMinus = that.y - y;
    double xMinus = that.x - x;
    if (yMinus == 0 && xMinus == 0) {
      return Double.NEGATIVE_INFINITY;
    } else if (xMinus == 0) {
      return Double.POSITIVE_INFINITY;
    } else if (yMinus == 0) {
      return +0.0;
    } else {
      return yMinus/xMinus;
    }
  }

  // compare two points by slopes they make with this point
  public Comparator<Point> slopeOrder() {
    return new PointComparator(this);
  }

  // Unit tests the Point data type.
  public static void main(String[] args) {
    Point testPoint = new Point(2, 3);
    Point testPoint2 = new Point(4, 5);
    Point testPoint3 = new Point(1, 3);
    Point testPoint4 = new Point(2, 6);
    // StdOut.println("Testing compare To: " + testPoint.compareTo(testPoint));
    // StdOut.println("Testing compare To: " + testPoint.compareTo(testPoint2));
    // StdOut.println("Testing compare To: " + testPoint.compareTo(testPoint3));
    // StdOut.println("Testing compare To: " + testPoint.slopeTo(testPoint));
    // StdOut.println("Testing compare To: " + testPoint.slopeTo(testPoint2));
    // StdOut.println("Testing compare To: " + testPoint.slopeTo(testPoint3));
    // StdOut.println("Testing compare To: " + testPoint.slopeTo(testPoint4));
    Comparator<Point> testingCompare = testPoint.slopeOrder();
    StdOut.println(testingCompare.compare(testPoint2, testPoint4));
    StdOut.println(testingCompare.compare(testPoint3, testPoint4));
  }
}