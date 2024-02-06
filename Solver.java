import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
  private class SearchNode implements Comparable<SearchNode> {
    Board board;
    SearchNode previous;
    int moves;
    int priority;

    public SearchNode(Board board, SearchNode previous, int moves) {
      this.board = board;
      this.previous = previous;
      this.moves = moves;
      this.priority = this.calculatePriority();
    }

    public Board getBoard() {
      return this.board;
    }

    public int getMoves() {
      return this.moves;
    }

    public int getPriority() {
      return this.priority;
    }

    public SearchNode getPrevious() {
      return this.previous;
    }

    public int calculatePriority() {
      return this.board.manhattan() + this.moves;
    }

    public int compareTo(SearchNode that) {
      if (that == null) {
        throw new NullPointerException("The argument is null");
      }
      int delta = this.priority - that.getPriority();
      if (delta == 0) {
        return 0;
      } else if (delta > 0) {
        return 1;
      } else {
        return -1;
      }
    }
  }

  private class InternalSolver {
    MinPQ<SearchNode> pq;
    private SearchNode result;

    
    public InternalSolver(Board board) {
      this.result = new SearchNode(board, null, 0);
      this.pq = new MinPQ<>(5);
      this.pq.insert(this.result);
    }

    // return if the board is solved
    public boolean nextStep() {
      this.result = this.pq.delMin();
      if (this.result.getBoard().isGoal()) {
        return true;
      } else {
        this.result.getBoard().neighbors().forEach(board -> {
          SearchNode previous = this.result.getPrevious();
          int moves = this.result.getMoves() + 1;
          if ((previous != null && !previous.getBoard().equals(board)) || previous == null) {
            SearchNode newNode = new SearchNode(board, this.result, moves);
            this.pq.insert(newNode);
          }
        });
        return false;
      }
    }

    // return result node
    public SearchNode getResult() {
      return result;
    }
  }

  private SearchNode result;
  private boolean solved;
  private boolean solvable;
  private int totalMoves;
  
  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException("There are no arguments");
    }
    Board initialBoard = initial;
    Board twinBoard = initialBoard.twin();
    this.solved = false;
    this.solvable = false;
    this.totalMoves = 0;

    InternalSolver mainBoardSolver = new InternalSolver(initialBoard);
    InternalSolver twinBoardSolver = new InternalSolver(twinBoard);

    while (!solved) {
      this.solved = mainBoardSolver.nextStep();
      if (this.solved) {
        this.solvable = this.solved;
        this.result = mainBoardSolver.getResult();
        this.totalMoves = this.result.getMoves();
      } else {
        this.solved = twinBoardSolver.nextStep();
      }
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
     return this.solvable;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (!isSolvable()) {
      return -1;
    }
    return this.totalMoves;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (!isSolvable()) {
      return null;
    }
    List<Board> boards = new ArrayList<Board>();
    SearchNode iterNode = this.result;
    boards.add(iterNode.getBoard());
    while (iterNode.getPrevious() != null) {
      iterNode = iterNode.getPrevious();
      boards.add(iterNode.getBoard());
    }
    return boards;
  }

  // test client
  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        tiles[i][j] = in.readInt();
        StdOut.println(tiles[i][j]);
      }
    }
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable()) {
      StdOut.println("No solution possible");
    } else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution()) {
        StdOut.println(board);
      }
    }
  }
}