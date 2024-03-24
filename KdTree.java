import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

class Node {
  private static double lowestDistance;
  private static Point2D nearestPoint;
  private static List<Point2D> rectPoints;
  Point2D point;
  Node right;
  Node left;
  boolean isX;

  public Node(Point2D p, Node right, Node left, boolean isX) {
    this.point = p;
    this.right = right;
    this.left = left;
    this.isX = isX;
  }

  public Node(Point2D p, boolean isX) {
    this(p, null, null, isX);
  }

  public void setRight(Node node) {
    this.right = node;
  }

  public void setLeft(Node node) {
    this.left = node;
  }

  public void setIsX(boolean isX) {
    this.isX = isX;
  }

  public static void setNearest(double dist, Point2D p) {
    lowestDistance = dist;
    nearestPoint = p;
  }

  public static void setRectanglePoints(Point2D point) {
    if (point == null) {
      rectPoints = new ArrayList<Point2D>();
    } else {
      rectPoints.add(point);
    }
  }

  public Point2D getPoint() {
    return this.point;
  }

  public Node getRight() {
    return this.right;
  }

  public Node getLeft() {
    return this.left;
  }

  public static Point2D getNearestPoint() {
    return nearestPoint;
  }

  public static List<Point2D> getRectanglePoints() {
    return rectPoints;
  }

  public boolean isX() {
    return this.isX;
  }

  public void insert(Point2D p) {
    boolean isSmaller = isSmaller(p);

    if (isSmaller) {
      if (this.left != null) {
        this.left.insert(p);
      } else {
        this.left = new Node(p, !this.isX());
      }
    } else {
      if (this.right != null) {
        this.right.insert(p);
      } else {
        this.right = new Node(p, !this.isX());
      }
    }
  }

  private boolean isSmaller(Point2D p) {
    if (this.isX) {
      return (p.x() < this.point.x());
    }
    return (p.y() < this.point.y());
  }

  public boolean contains(Point2D p) {
    if (this.point.equals(p)) {
      return true;
    }

    boolean isSmaller = isSmaller(p);

    if (isSmaller) {
      if (this.left != null) {
        return this.left.contains(p);
      }
      return false;
    } else {
      if (this.right != null) {
        return this.right.contains(p);
      }
      return false;
    }
  }

  private double[] calculateDividerCoordinates(RectHV rectangle) {
    double[] coordinates = {0.0, 0.0, 1.1, 1.1};
    if (isX()) {
      coordinates[0] = point.x();
      coordinates[1] = rectangle.ymin();
      coordinates[2] = point.x();
      coordinates[3] = rectangle.ymax();

    } else {
      coordinates[0] = rectangle.xmin();
      coordinates[1] = point.y();
      coordinates[2] = rectangle.xmax();
      coordinates[3] = point.y();
    }
    return coordinates;
  }

  public void draw(RectHV rectangle) {
    double[] coordinates = this.calculateDividerCoordinates(rectangle);
    
    if (this.isX()) {
      StdDraw.setPenColor(StdDraw.RED);
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
    }
    StdDraw.setPenRadius(0.01);
    StdDraw.line(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);

    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.03);
    this.point.draw();
    
    if (this.left != null) {
      this.left.draw(new RectHV(rectangle.xmin(), rectangle.ymin(), coordinates[2], coordinates[3]));
    }    
    if (this.right != null) {
      this.right.draw(new RectHV(coordinates[0], coordinates[1], rectangle.xmax(), rectangle.ymax()));
    }
  }

  public void range(RectHV rect, RectHV rectangle) {
    if (rect.contains(this.point)) {
      setRectanglePoints(this.point);
    }

    RectHV newRectangleLeft = null;
    RectHV newRectangleRight = null;
    boolean leftIntersects = false;
    boolean rightIntersects = false;
    double[] coordinates = calculateDividerCoordinates(rectangle);

    if (this.left != null) {
      newRectangleLeft = new RectHV(rectangle.xmin(), rectangle.ymin(), coordinates[2], coordinates[3]);    
      leftIntersects = rect.intersects(newRectangleLeft);
    }
    if (this.right != null) {
      newRectangleRight = new RectHV(coordinates[0], coordinates[1], rectangle.xmax(), rectangle.ymax());
      rightIntersects = rect.intersects(newRectangleRight);
    }

    if (this.left != null && leftIntersects) {
      this.left.range(rect, newRectangleLeft);
    }
    if (this.right != null && rightIntersects) {
      this.right.range(rect, newRectangleRight);
    }
  }

  public void nearest(Point2D p, RectHV rectangle) {
    double pointDist = this.point.distanceTo(p);
    if (pointDist < lowestDistance) {
      setNearest(pointDist, this.point);
    }

    double lowestDist = rectangle.distanceTo(p);

    if (lowestDist < lowestDistance) {
      double[] coordinates = this.calculateDividerCoordinates(rectangle);
    
      Runnable leftRunnable = () -> {
        if (this.left != null) {
          RectHV newRectangleLeft = new RectHV(rectangle.xmin(), rectangle.ymin(), coordinates[2], coordinates[3]); 
          this.left.nearest(p, newRectangleLeft); 
        } 
      };
      Runnable rightRunnable = () -> {
        if (this.right != null) {
          RectHV newRectangleRight = new RectHV(coordinates[0], coordinates[1], rectangle.xmax(), rectangle.ymax()); 
          this.right.nearest(p, newRectangleRight); 
        } 
      };
      if (this.isSmaller(p)) {
        leftRunnable.run();
        rightRunnable.run();
      } else {
        rightRunnable.run();
        leftRunnable.run();
      }
    }
  }
}

public class KdTree {
    private Node root;
    private int size;

  // construct an empty kdTree 
  public KdTree() {
    this.size = 0;
    this.root = null;
  }

  // is the kdTree empty?
  public boolean isEmpty() {
    if (size == 0) {
      return true;
    }
    return false;
  }

  // number of points in the kdTree
  public int size() {
    return size;
  }

  // add the point to the kdTree (if it is not already in it)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("called insert() with a null key");
    }
    if (this.contains(p)) {
      return;
    }
    if (this.isEmpty()) {
      this.root = new Node(p, true);
    } else {
      this.root.insert(p);
    }
    this.size++;
  }

  // does the kdTree contain point p?
  public boolean contains(Point2D p) {
    if (p == null) { 
      throw new IllegalArgumentException("called contains() with a null key");
    }
    if (this.isEmpty()) {
      return false;
    }
    return this.root.contains(p);
  }

  // draw all points to standard draw 
  public void draw() {
    if (!this.isEmpty()) {
      RectHV rectangle = new RectHV(0.0, 0.0, 1.0, 1.0);
      this.root.draw(rectangle);
    }
  }

  // all points that are inside the rectangle (or on the boundary) 
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException("called range() with a null key");
    }
    if (this.isEmpty()) {
      return null;
    }
    Node.setRectanglePoints(null);
    RectHV rectangle = new RectHV(0.0, 0.0, 1.0, 1.0);
    this.root.range(rect, rectangle);
    return Node.getRectanglePoints();
  }
  
  // a nearest neighbor in the kdTree to point p; null if the kdTree is empty
  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("called nearest() with a null key");
    }
    if (this.isEmpty()) {
      return null;
    }
    Node.setNearest(this.root.getPoint().distanceTo(p), this.root.getPoint());
    RectHV rectangle = new RectHV(0.0, 0.0, 1.0, 1.0);
    this.root.nearest(p, rectangle);
    return Node.getNearestPoint();
  }

  // unit testing of the methods (optional) 
  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();
    while (!in.isEmpty()) {
        double x = in.readDouble();
        double y = in.readDouble();
        Point2D p = new Point2D(x, y);
        kdtree.insert(p);
    }
    
    // test draw
    kdtree.draw();
    StdDraw.show();
  }
}