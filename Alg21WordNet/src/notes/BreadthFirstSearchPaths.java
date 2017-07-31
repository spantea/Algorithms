package notes;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class BreadthFirstSearchPaths {
	private boolean[] marked;
	private int[] edgeTo;
	private int[] distTo;
	
	public BreadthFirstSearchPaths(Graph G, int s) {
		this.marked = new boolean[G.V()];
		this.edgeTo = new int[G.V()];
		this.distTo = new int[G.V()];
		
		bfs(G,s);
	}
	
	private void bfs(Graph G, int s) {
		Queue<Integer> q = new Queue<>();		
		for (int x = 0; x < G.V(); x++)
			distTo[x] = Integer.MAX_VALUE;
		
		distTo[s] = 0;
		marked[s] = true;
		q.enqueue(s);
		
		while (!q.isEmpty()) {
			int v = q.dequeue();			
			for (int w: G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					marked[w] = true;
					distTo[w] = distTo[v] + 1;
					q.enqueue(w);
				}
			}
		}
	}
	
	private void bfs(Graph G, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		for (int s: sources) {
			distTo[s] = 0;
			marked[s] = true;			
			q.enqueue(s);
		}
		
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					marked[w] = true;
					distTo[w] = distTo[v] + 1;
					q.enqueue(w);
				}
			}
		}
	}
	
	public boolean hasPathTo(int v) {
		return marked[v];
	}
	
	public int distTo(int v) {
		return distTo[v];
	}
	
	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v)) return null;
		
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		
		return path;
	}
}
