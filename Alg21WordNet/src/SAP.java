//import java.util.Random;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DigraphGenerator;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;

public class SAP {
  private final Digraph G;
  private boolean[] vMarked;  // marked[v] = is there an s->v path?
  private boolean[] wMarked;  // marked[v] = is there an s->v path?
  private int[] vDistTo;      // distTo[v] = length of shortest s->v path
  private int[] wDistTo;      // distTo[v] = length of shortest s->v path

  /**
   *  constructor takes a digraph (not necessarily a DAG)
   *  @param G
   */
  public SAP(Digraph G) {
    if (G == null) {
      throw new NullPointerException();
    }
    
    if (G.getClass() == Digraph.class) {
      this.G = new Digraph(G);
    } else {
      throw new java.lang.IllegalArgumentException();
    }   
  }
  
  

  /**
   * @param v
   * @param w
   * @return length of shortest ancestral path between v and w; -1 if no such path
   */
  public int length(int v, int w) {
    if (!isValidArgument(v) || !isValidArgument(w)) {
      throw new IndexOutOfBoundsException();
    }
    verifyInput(v);
    verifyInput(w);
    
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    int minDist = -1;  
    for (int i = 0; i < G.V(); i++) {
      if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
        int dist = bfsV.distTo(i) + bfsW.distTo(i);
        
        if (minDist == -1 || dist < minDist) {
          minDist = dist;
        }
      }
    }
    
    return minDist;
  }
  
  /**
   * @param v
   * @param w
   * @return a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   */
  private int ancestor_slow(int v, int w) {
    if (!isValidArgument(v) || !isValidArgument(w)) {
      throw new IndexOutOfBoundsException();
    }
    verifyInput(v);
    verifyInput(w);
    
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    for (int i = 0; i < G.V(); i++) {
      if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
        int dist = bfsV.distTo(i) + bfsW.distTo(i);
        if (length(v, w) == dist) {
          return i;
        }
      }
    }
    
    return -1;
  }

  /**
   * @param v
   * @param w
   * @return length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   */
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new NullPointerException();
    }
    verifyInput(v);
    verifyInput(w);
    
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    int minDist = -1;
    for (int i = 0; i < G.V(); i++) {
      if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
        int dist = bfsV.distTo(i) + bfsW.distTo(i);
        
        if (minDist == -1 || dist < minDist) {
          minDist = dist;
        }
      }
    }
    
    return minDist;
  }

  /**
   * @param v
   * @param w
   * @return  a common ancestor that participates in shortest ancestral path; -1 if no such path
   */
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new NullPointerException();
    }
    verifyInput(v);
    verifyInput(w);
    
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    for (int i = 0; i < G.V(); i++) {
      if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
        int dist = bfsV.distTo(i) + bfsW.distTo(i);
        if (length(v,w) == dist) {
          return i;
        }
      }
    }
    
    return -1;
  }
  
  private boolean isValidArgument(int arg) {
    if (arg >= 0 && arg < G.V()) {
      return true;
    }
    return false;
  }
  
  private void verifyInput(int v) {
    if (v < 0 || v >= G.V())
        throw new java.lang.IndexOutOfBoundsException();
  }
  
  private void verifyInput(Iterable<Integer> v) {
    for (int w : v) {
        verifyInput(w);
    }
  }
  
  public int ancestor(int v, int w) {
	  this.vMarked = new boolean[G.V()];
	  this.wMarked = new boolean[G.V()];
	  this.vDistTo = new int[G.V()];
	  this.wDistTo = new int[G.V()];
	    
	  return ancestor_bfs(v, w);
  }
  
  private int ancestor_bfs(int v, int w) {
    Queue<Integer> qv = new Queue<Integer>();
    Queue<Integer> qw = new Queue<Integer>();
    vMarked[v] = true;
    wMarked[w] = true;
    vDistTo[v] = 0;
    wDistTo[w] = 0;
    qv.enqueue(v);
    qw.enqueue(w);
    
    int minDist = -1;
    int ancestor = -1;
    int vi = -1;
    int wi = -1;
    while (!qv.isEmpty() || !qw.isEmpty()) {
      if (!qv.isEmpty()) vi = qv.dequeue();
      if (!qw.isEmpty()) wi = qw.dequeue();
                
        if (wMarked[vi]) {
          int dist = vDistTo[vi] + wDistTo[vi];
          if (dist < minDist || minDist == -1) {
            ancestor = vi;
            minDist = dist;            
          }
        }
        
        if (vMarked[wi]) {
          int dist = vDistTo[wi] + wDistTo[wi];
          if (dist < minDist || minDist == -1) {
            ancestor = wi;
            minDist = dist;            
          }
        }
        
        for (int vn : G.adj(vi)) {
            if (!vMarked[vn]) {
              vDistTo[vn] = vDistTo[vi] + 1;
              vMarked[vn] = true;
              if (vDistTo[vn] > minDist && minDist != -1) break;
              qv.enqueue(vn);
            }
        }
        
        for (int wn : G.adj(wi)) {
          if (!wMarked[wn]) {
              wDistTo[wn] = wDistTo[wi] + 1;
              wMarked[wn] = true;
              if (wDistTo[wn] > minDist && minDist != -1) break;
              qw.enqueue(wn);
          }
        }
    }
    
    return ancestor;
}

    
  // do unit testing of this class
  public static void main(String[] args) {
    Digraph d1 = new Digraph(new In("digraph3.txt"));
    System.out.println(d1.toString());
    
    SAP s = new SAP(d1);
    //System.out.println(s.length(8, 5));
    System.out.println(s.ancestor(8, 1));
    System.out.println(s.ancestor_slow(8, 1));
    
//    Digraph d2 = new Digraph(new In("test/digraph-ambiguous-ancestor.txt"));
//    System.out.println(d2.toString());
//    s = new SAP(d2);
//    //System.out.println(s.length(0, 0));
//    System.out.println(s.ancestor(0, 6));
//    System.out.println(s.ancestor_bfs(0, 6));
//    
//    Digraph d3 = new Digraph(new In("test/digraph2.txt"));
//    System.out.println(d3.toString());
//    s = new SAP(d3);
//    ArrayList<Integer> myList = new ArrayList<Integer>();
//    myList.add(0);
//    myList.add(2);
//    ArrayList<Integer> myList2 = new ArrayList<Integer>();
//    myList2.add(1);
//    myList2.add(2);
//    
//    System.out.println(s.length(myList,myList2));
//    System.out.println(s.ancestor(2, 5));
    
//    Digraph d = DigraphGenerator.rootedInDAG(100, 100);   
//    SAP s = new SAP(d);
//    System.out.println(d.toString());
//    Random rand = new Random();
//    for (int i = 0; i < 100; i++) {
//     int a = rand.nextInt(10);
//     int b = rand.nextInt(10);
//     
//     //System.out.println(s.length(a,b));    
//     System.out.println(s.ancestor(a,b)); 
//     System.out.println(s.ancestor_slow(a,b)); 
//     System.out.println();
//    }
    
  }
}