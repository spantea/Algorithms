import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
  private Node solutionNode;
  private Stack<Board> solution;
  private boolean solvable;
  
  private class Node implements Comparable<Node> {
    private int moves;
    private Board board;
    private Node previous;
    
    public Node(Board initial) {
      this.board = initial;
      this.previous = null;
      this.moves = 0;
    }
    
    public Board getBoard() {
      return board;
    }
    
    public Node getPrevious() {
      return this.previous;
    }
    
    public void setPrevious(Node previous) {
      this.previous = previous;
    }
    
    public int getMoves() {
      return this.moves;
    }
    
    public void setMoves(int moves) {
      this.moves = moves;
    }
    
    public int compareTo(Node that) {
      if (this.board.manhattan() + this.moves < that.getBoard().manhattan() + that.moves) return -1;
      if (this.board.manhattan() + this.moves > that.getBoard().manhattan() + that.moves) return 1;
      return 0;
    }
  }
  
  //find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if(initial == null) throw new NullPointerException();
    
    Node minPrNode = new Node(initial);
    Node minPrNodeTwin = new Node(initial.twin());
    
    MinPQ<Node> searchGamePQ = new MinPQ<Node>();
    MinPQ<Node> searchGamePQTwin = new MinPQ<Node>();
    
    searchGamePQ.insert(minPrNode);
    searchGamePQTwin.insert(minPrNodeTwin);
        
    while (!minPrNode.getBoard().isGoal() && !minPrNodeTwin.getBoard().isGoal()) {
      minPrNode = searchGamePQ.delMin(); 
      minPrNodeTwin = searchGamePQTwin.delMin();  
            
      for (Board b : minPrNode.getBoard().neighbors()) {
        if (minPrNode.previous == null || !b.equals(minPrNode.previous.board)) {
          Node neighbor = new Node(b);
          neighbor.setPrevious(minPrNode);
          neighbor.setMoves(minPrNode.getMoves()+1);
          searchGamePQ.insert(neighbor);
        }
      }  
               
      for (Board b : minPrNodeTwin.getBoard().neighbors()) {
        if (minPrNodeTwin.previous == null || !b.equals(minPrNodeTwin.previous.board)) {
          Node neighborTwin = new Node(b);
          neighborTwin.setPrevious(minPrNodeTwin);
          neighborTwin.setMoves(minPrNodeTwin.getMoves()+1);
          searchGamePQTwin.insert(neighborTwin);
        }
      }  
      
    }

    //we found solution for initial board, so it is solvable
    if (minPrNode.getBoard().isGoal()) {
      solutionNode = minPrNode;
      solvable = true;
    }

    //we found a solution using the twin board, so the initial board is unsolvable
    if (minPrNodeTwin.getBoard().isGoal()) {
      solvable = false;
    }
  }
  
  //is the initial board solvable?
  public boolean isSolvable() { 
    return solvable;
  }
  
  //min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (solutionNode == null)
      return -1;
    return solutionNode.getMoves();
  }
  
  //sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (solutionNode == null) {
      return null;
    } else {
      if (solution == null) {
        solution = new Stack<Board>();
        Node stepNode = solutionNode;
        while (stepNode != null) {
          Board board = stepNode.getBoard();
          solution.push(board);
          stepNode = stepNode.getPrevious();
        }
      }
    }
    
    return solution;
  }
  
  //solve a slider puzzle (given below)
  public static void main(String[] args) {
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
  }
}