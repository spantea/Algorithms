package notes;

import java.util.Scanner;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class CC {
	private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components
	
	public CC(Graph G) {
		this.marked = new boolean[G.V()];
		this.id = new int[G.V()];
		this.size = new int[G.V()];
		
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v]) {
				dfs(G,v);
				count++;
			}
		}
		
	}
	
	private void dfs(Graph G, int v) {
		marked[v] = true;
		id[v] = count;
		size[count]++;
		for (int w : G.adj(v)) {
			if (!marked[w])
				dfs(G, w);
		}
	}
	
	/**
	 * Are v and w in the same component ?
	 * @param v
	 * @param w
	 * @return
	 */
	public boolean connected(int v, int w) {
		return id[v] == id[w];
	}
	
	/**
	 * How many connected components are there ?
	 * @return
	 */
	public int count() {
		return count;
	}
	
	/**
	 * In which connected component is v
	 * @param v
	 * @return
	 */
	public int id(int v) {
		return id[v];
	}
	
	/**
	 * How many vertices are in the component of v
	 * @param v
	 * @return
	 */
	public int size(int v) {
		return size[id[v]];
	}
	
	public static void main(String... args) {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		CC cc = new CC(G);
		
		int nbOfCC = cc.count();
		Queue<Integer>[] components = (Queue<Integer>[])new Queue[nbOfCC];
		for (int i = 0; i < nbOfCC; i++) {
			components[i] = new Queue<Integer>();
		}
		
		for (int i = 0; i < G.V(); i++) {
			components[cc.id(i)].enqueue(i);
		}
		
		for (int i = 0; i < nbOfCC; i++) {
			for (int v : components[i]) {
				StdOut.print(v + " ");
			}
			StdOut.println();
		}
	}
}
