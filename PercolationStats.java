// import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private double[] thresholds;
  private int trials;
  private double confidence = 1.96;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("Value out of range: " + n + "-" + trials);
    }
    this.trials = trials;
    thresholds = new double[trials];
    for (int i = 0; i < trials; i++) {
      Percolation percolation = new Percolation(n);
      
      // randomize positions
      int[] x = new int[n * n];   
      for (int j = 0; j < n * n; j++) {
        x[j] = j;
      }
      StdRandom.shuffle(x);

      // open holds until percolation
      int threshold = 0;
      while (!percolation.percolates()) {
        percolation.open(calculateIndexReverseX(x[threshold], n), calculateIndexReverseY(x[threshold], n));
        threshold++;
      }
      thresholds[i] = ((double) threshold) / (n * n);
    }
  }

  // sample mean of percolation threshold
  public double mean() {
      return StdStats.mean(thresholds);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(thresholds);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - ((confidence * stddev()) / Math.sqrt(trials));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + ((confidence * stddev()) / Math.sqrt(trials));
  }

  // calculate grid index
  private int calculateIndexReverseX(int index, int gridSize) {
    return (index / gridSize) + 1;
  }

  private int calculateIndexReverseY(int index, int gridSize) {
    return (index % gridSize) + 1;
  }

   // test client (see below)
   public static void main(String[] args) {
    PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    StdOut.println("mean = " + percStats.mean());
    StdOut.println("stddev = " + percStats.stddev());
    StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]");

   }
}
