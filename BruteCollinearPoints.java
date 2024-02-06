import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
  private Point[] pointsArray;
  private LineSegment[] segmentsArray;
  private LineSegment[] segmentPoints;
  private int numberOfSegments;

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException("There are no arguments");
    }
    for (int i = 0; i < points.length; i++) {
      if (points[i] == null) {
        throw new IllegalArgumentException("One or more points are null");
      }
      if (i + 1 < points.length && points[i] == points[i + 1]) {
        throw new IllegalArgumentException("There is one or more points duplicated");
      }
    }
    pointsArray = points.clone();
    Arrays.sort(pointsArray);
    numberOfSegments = 0;
    segmentPoints = new LineSegment[pointsArray.length];
    for (int i = 0; i < pointsArray.length; i++) {
      for (int j = i + 1; j < pointsArray.length; j++) {
        for (int k = j + 1; k < pointsArray.length; k++) {
          for (int m = k + 1; m < pointsArray.length; m++) {
            if (pointsArray[i].slopeTo(pointsArray[j]) == pointsArray[i].slopeTo(pointsArray[k]) && pointsArray[i].slopeTo(pointsArray[j]) == pointsArray[i].slopeTo(pointsArray[m])) {
              segmentPoints[numberOfSegments] = new LineSegment(pointsArray[i], pointsArray[m]);
              numberOfSegments++;
            }
          }
        }
      }
    }

    segmentsArray = new LineSegment[numberOfSegments];
  }
  
  // the number of line segments
  public int numberOfSegments() {
    return numberOfSegments;
  }

  // the line segments
  public LineSegment[] segments() {
    for (int i = 0; i < numberOfSegments; i++) {
      segmentsArray[i] = segmentPoints[i];
    }
    return segmentsArray;
  }

  // testing
  public static void main(String[] args) {
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}