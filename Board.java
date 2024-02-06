import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;

public class Board {
  private final int matrixN;
  private int[][] matrixWork;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null) {
      throw new IllegalArgumentException("There are no arguments");
    }
    this.matrixN = tiles.length;
    this.matrixWork = new int[this.matrixN][];
    for (int row = 0; row < this.matrixN; row++) {
      this.matrixWork[row] = tiles[row].clone();
    }
  }
                                         
  // string representation of this board
  @Override
  public String toString() {
    StringBuilder matrixString = new StringBuilder();
    matrixString.append(matrixN + "\n");
    for (int row = 0; row < this.matrixN; row++) {
      StringBuilder matrixRow = new StringBuilder();
      for (int col = 0; col < this.matrixN; col++) {
        matrixRow.append(" " + matrixWork[row][col]);
      }
      matrixString.append(matrixRow.toString() + '\n');
    }
    return matrixString.toString();
  }

  // board dimension n
  public int dimension() {
    return matrixN;
  }

  // number of tiles out of place
  public int hamming() {
    int tilesOut = 0;
    for (int row = 0; row < this.matrixN; row++) {
      for (int col = 0; col < this.matrixN; col++) {
        if (matrixWork[row][col] != row * matrixN + (col + 1)) {
          tilesOut++;
        }
        if (row == this.matrixN - 1 && col == this.matrixN - 1 && matrixWork[row][col] == 0) {
          tilesOut--; 
        }
      }
    }
    if (tilesOut > 0 && matrixWork[this.matrixN - 1][this.matrixN - 1] != 0) {
      tilesOut--;
    }
    return tilesOut;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    int tilesDistance = 0;
    for (int row = 0; row < this.matrixN; row++) {
      for (int col = 0; col < this.matrixN; col++) {
        int value = this.matrixWork[row][col] - 1;
        if (value != -1) {
          int targetRow = value / this.matrixN;
          int targetColumn = value % this.matrixN; 
          int x = Math.abs(targetRow - row);
          int y = Math.abs(targetColumn - col);
          tilesDistance = tilesDistance + x + y;
        }
      }
    }
    return tilesDistance;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return (this.hamming() == 0);
  }

  // does this board equal y?
  @Override
  public boolean equals(Object y) {
    if (this == y) {
      return true;
    }
    if (y == null || y.getClass() != this.getClass()) {
      return false;
    }
    Board boardToCompare = (Board) y;
    if (boardToCompare.matrixN != this.matrixN) {
      return false;
    }
    for (int row = 0; row < this.matrixN; row++) {
      for (int col = 0; col < this.matrixN; col++) {
        if (this.matrixWork[row][col] != boardToCompare.matrixWork[row][col]) {
          return false;
        }
      }
    }
    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    int[] positionZero = zeroSearch(matrixWork);
    int[] right = { positionZero[0], positionZero[1], positionZero[0], positionZero[1] + 1 };
    int[] left = { positionZero[0], positionZero[1], positionZero[0], positionZero[1] - 1 };
    int[] top = { positionZero[0], positionZero[1], positionZero[0] - 1, positionZero[1] };
    int[] bottom = { positionZero[0], positionZero[1], positionZero[0] + 1, positionZero[1] };
    int[][] positions = { right, left, bottom, top };
    return swapSaveReinit(positions);
  }

  private List<Board> swapSaveReinit(int[][] positions) {
    int repeat = positions.length;
    List<Board> boards = new ArrayList<>();
    for (int i = 0; i < repeat; i++) {
      if (positions[i][2] < 0 || positions[i][3] < 0 || positions[i][2] == matrixN || positions[i][3] == matrixN) {
        continue;
      }
      int[][] tempMatrix = new int[this.matrixN][];;
      for (int row = 0; row < this.matrixN; row++) {
        tempMatrix[row] = this.matrixWork[row].clone();
      }
      this.swap(tempMatrix, positions[i][0], positions[i][1], positions[i][2], positions[i][3]);
      boards.add(new Board(tempMatrix));
    }
    return boards;
  }
  
  private int[] zeroSearch(int[][] matrix) {
    int[] result = new int[2];
    for (int row = matrix.length - 1; row >= 0; row--) {
      for (int col = matrix.length - 1; col >= 0; col--) {
        if (matrix[row][col] == 0) {
          result[0] = row;
          result[1] = col;
        }
      }
    }
    return result;
  }
  
  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    int[][] tempMatrix = new int[this.matrixN][];;
    for (int row = 0; row < this.matrixN; row++) {
      tempMatrix[row] = this.matrixWork[row].clone();
    }
    if (tempMatrix[0][0] == 0) {
      this.swap(tempMatrix, 0, 1, matrixN - 1, matrixN - 1);
    } else if (tempMatrix[this.matrixN - 1][this.matrixN - 1] == 0) {
      this.swap(tempMatrix, 0, 0, matrixN - 1, matrixN - 2);
    } else {
      this.swap(tempMatrix, 0, 0, matrixN - 1, matrixN - 1);
    }
    Board twinBoard = new Board(tempMatrix);
    return twinBoard;
  }

  private void swap(int[][] matrix, int posA, int posB, int posC, int posD) {
    int value = matrix[posA][posB];
    matrix[posA][posB] = matrix[posC][posD];
    matrix[posC][posD] = value;
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    int[][] testing = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
    Board testMatrix = new Board(testing);
    // StdOut.println(testMatrix);
    // StdOut.println(testMatrix.dimension());
    // StdOut.println(testMatrix.hamming());
    // StdOut.println(testMatrix.manhattan());
    // StdOut.println(testMatrix.isGoal());
    // StdOut.println(testMatrix.neighbors());
    StdOut.println(testMatrix.twin());
  }

}