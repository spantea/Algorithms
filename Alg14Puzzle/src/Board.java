import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;

public class Board { 
  private int N;
  private int[][] tiles;
  
  //construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
  public Board(int[][] blocks) {
    if (blocks == null)
      throw new NullPointerException();
    
    N = blocks.length;
    tiles = copyArray(blocks);
  }
  
  //board dimension N
  public int dimension() {
    return N;
  }
  
  //number of blocks out of place
  public int hamming() {
    int dist = 0;
    int correctTile = 1;
    
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {                    
        if (tiles[i][j] != correctTile && tiles[i][j] != 0) {
          dist++;
        }
        correctTile++;
      }
    }
    return dist;
  }
  
  //sum of Manhattan distances between blocks and goal
  public int manhattan() {
    int dist = 0;
    int tile = -1;
    int correctRow = -1;
    int correctCol = -1;
    
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        tile = tiles[i][j];
        
        if (tile != 0) {
          int t = tile - 1;
          correctRow = t / N;
          correctCol = t % N;      
          int td = Math.abs(i - correctRow) + Math.abs(j - correctCol);
          dist += td;
          //System.out.println(tile + " cRow: " + correctRow + " cCol: " + correctCol + " dist: " + td); 
        }                      
      }
    }
    
    return dist;
  }
  
  //is this board the goal board?
  public boolean isGoal() {
    if (this.hamming() == 0) return true;
    return false;
  }
  
  //a board that is obtained by exchanging two adjacent blocks in the same row
  public Board twin() {
    boolean twin = false;
    int[][] twinTiles = copyArray(tiles);
    
    while(!twin) {
      int row = StdRandom.uniform(N);
      int col = StdRandom.uniform(N-1);
      
      if(twinTiles[row][col] != 0 && twinTiles[row][col+1] != 0) {
        int i = twinTiles[row][col+1];
        twinTiles[row][col+1] = twinTiles[row][col];
        twinTiles[row][col] = i;
        twin = true;
      }
    }    
    
    Board twinBoard = new Board(twinTiles);
    return twinBoard;
  }
  
  //does this board equal y?
  public boolean equals(Object y) {
    
    if (y == this) return true;   
    if (y == null) return false;  
    if (y.getClass() != this.getClass()) return false;
    
    Board that = (Board) y;
    
    if (that.dimension() != this.dimension()) return false;
    
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (this.tiles[i][j] != that.tiles[i][j]) return false;
      }
    }
    
    return true;
  }
  
  //all neighboring boards
  public Iterable<Board> neighbors() {
    List<Board> neighbors = new ArrayList<Board>();
    
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (tiles[i][j] == 0) {
          
          //get left neighbor
          if (j > 0) {
            int[][] left = copyArray(tiles);
            int ln = left[i][j-1];
            left[i][j-1] = left[i][j];
            left[i][j] = ln;            
            Board lBoard = new Board(left);       
            neighbors.add(lBoard);
          }
          
          //get top neighbor
          if (i > 0) {
            int[][] top = copyArray(tiles);
            int tn = top[i-1][j];
            top[i-1][j] = top[i][j];
            top[i][j] = tn;
            Board tBoard = new Board(top);
            neighbors.add(tBoard);
          }
          
          //get right neighbor
          if (j < N-1) {
            int[][] right = copyArray(tiles);
            int rn = right[i][j+1];
            right[i][j+1] = right[i][j];
            right[i][j] = rn;
            Board rBoard = new Board(right);
            neighbors.add(rBoard);
          }
          
          //get bottom neighbor
          if (i < N-1) {
            int[][] bot = copyArray(tiles);
            int bn = bot[i+1][j];
            bot[i+1][j] = bot[i][j];
            bot[i][j] = bn;
            Board bBoard = new Board(bot);
            neighbors.add(bBoard);
          }
          
          break;
        }
      }
    }
    
    return neighbors;
  }
  
  //string representation of this board (in the output format specified below)
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(N + "\n");
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            s.append(String.format("%2d ", tiles[i][j]));
        }
        s.append("\n");
    }
    return s.toString();
  }
  
  private int[][] copyArray(int[][] t) {
    int[][] n = new int[t.length][t.length];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        n[i][j] = t[i][j];
      }
    }
    return n;
  }
  
  public static void main(String[] args) {
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);
       
    //System.out.println("Ham " + initial.hamming());
    System.out.println("Man " + initial.manhattan());
    
    System.out.println(initial.twin().toString());   
    //System.out.println();   
    //System.out.println(initial.toString());
    
//    for (Board b : initial.neighbors()) {
//      System.out.println(b);
//    }

  }
}