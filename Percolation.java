import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private boolean[][] percolationMatrix;
  private final int gridSize;
  private int openingCounter;
  private WeightedQuickUnionUF wQuickUnionUF;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("Value out of range: " + n);
    }
    gridSize = n;
    percolationMatrix = new boolean[n + 1][n + 1];
    openingCounter = 0;
    wQuickUnionUF = new WeightedQuickUnionUF(gridSize * gridSize + 2);
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    if (checkRange(row) || checkRange(col)) {
      throw new ArrayIndexOutOfBoundsException("Values out of range: " + row + "-" + col);
    }
    if (!percolationMatrix[row][col]) {
      percolationMatrix[row][col] = true;
      openingCounter++;
      if (row == 1) {
        wQuickUnionUF.union(calculateIndex(row, col), calculateIndex(gridSize + 1, 1));
      }
      if (row == gridSize) {
        wQuickUnionUF.union(calculateIndex(row, col), calculateIndex(gridSize + 1, 2));
      }
      if (col - 1 > 0 && isOpen(row, col - 1)) {
        wQuickUnionUF.union(calculateIndex(row, col), calculateIndex(row, col - 1));
      }
      if (col + 1 <= gridSize && isOpen(row, col + 1)) {
        wQuickUnionUF.union(calculateIndex(row, col), calculateIndex(row, col + 1));
      }
      if (row - 1 > 0 && isOpen(row - 1, col)) {
        wQuickUnionUF.union(calculateIndex(row, col), calculateIndex(row - 1, col));
      }
      if (row + 1 <= gridSize && isOpen(row + 1, col)) {
        wQuickUnionUF.union(calculateIndex(row, col), calculateIndex(row + 1, col));
      }
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    if (checkRange(row) && checkRange(col)) {
      throw new ArrayIndexOutOfBoundsException("Values out of range: " + row + "-" + col);
    }
    return percolationMatrix[row][col];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    return wQuickUnionUF.find(calculateIndex(gridSize + 1, 1)) == wQuickUnionUF.find(calculateIndex(row, col));
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openingCounter;
  }

  // does the system percolate?
  public boolean percolates() {
    return isFull(gridSize + 1, 2);
  }

  // check if the row and column exists in the percolation matrix
  private boolean checkRange(int num) {
    return (num <= 0 || num > gridSize);
  }

  // calculate grid index
  private int calculateIndex(int row, int col) {
    return (row - 1) * gridSize + (col - 1);
  }
}