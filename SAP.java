import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
  private final Digraph dG;
  private int commonAncestor;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    if (G == null) { 
      throw new IllegalArgumentException("Digraph must not be null");
    }
    if (G.V() == 0) { 
      throw new IllegalArgumentException("zero vertices");
    }
    this.dG = new Digraph(G);
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    return this.length(List.of(v), List.of(w));
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    return this.ancestor(List.of(v), List.of(w));
  }

  private void validateIterables(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException("The iterables are null");
    }
    v.forEach(val -> {
      if (val == null) {
        throw new IllegalArgumentException("The iterable v has a null value");
      }
    });
    w.forEach(val -> {
      if (val == null) {
        throw new IllegalArgumentException("The iterable w has a null value");
      }
    });
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    this.validateIterables(v, w);
    int shortestPath = -1;
    this.commonAncestor = -1;

    BreadthFirstDirectedPaths bfdsV = new BreadthFirstDirectedPaths(this.dG, v);
    BreadthFirstDirectedPaths bfdsW = new BreadthFirstDirectedPaths(this.dG, w);
    for (int i = 0; i < this.dG.V(); i++) {
      if (bfdsV.hasPathTo(i) && bfdsW.hasPathTo(i)) {
        int tempVPath = bfdsV.distTo(i);
        int tempWPath = bfdsW.distTo(i);
        int tempPath = tempVPath + tempWPath;
        if (shortestPath == -1 || (shortestPath >= 0 && tempPath < shortestPath)) {
          shortestPath = tempPath;
          this.commonAncestor = i;
        }
      }
    }
    return shortestPath;
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    this.length(v, w);
    return this.commonAncestor;
  }

  // do unit testing of this class
  //  public static void main(String[] args) {}
  // General testing:
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length   = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}


// Corner cases.  Throw an IllegalArgumentException in the following situations:
// Any argument is null
// Any vertex argument is outside its prescribed range
// Any iterable argument contains a null item

// Performance requirements.  
// All methods (and the constructor) should take time at most proportional to E + V in the worst case, 
// where E and V are the number of edges and vertices in the digraph, respectively. 
// Your data type should use space proportional to E + V.