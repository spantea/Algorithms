import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
  private Map<String, Integer> teamIndex;
  private Map<Integer, Integer> teamVertexIndex;
  private List<String> teams;
  private int[] w; //won games by team i
  private int[] l; //lost games by team i
  private int[] r; //remaining games to play
  private int[][] g; //games against teams
  private int N;
  private FlowNetwork FN;
  private FordFulkerson ff;
  
  /**
   * Create a baseball division from given filename in format specified below
   * % more teams4.txt
   * Atlanta       83 71  8  0 1 6 1
   * Philadelphia  80 79  3  1 0 0 2
   * New_York      78 78  6  6 0 0 0
   * Montreal      77 82  3  1 2 0 0
   * @param filename
   */
  public BaseballElimination(String filename) {
    readData(filename);  
  }
  
  private void readData(String filename) {
    In in = new In(filename);
    N = in.readInt();
    w = new int[N];
    l = new int[N];
    r = new int[N];
    g = new int[N][N];
    teamIndex = new HashMap<String, Integer>();
    teams = new ArrayList<String>();
    
    try {
      int i = 0;
      while (!in.isEmpty()) {
        String s[] = in.readLine().trim().split("\\s+");
        if (s.length > 1) {
          teams.add(s[0]);
          teamIndex.put(s[0], i);
          w[i] = Integer.parseInt(s[1]);
          l[i] = Integer.parseInt(s[2]);
          r[i] = Integer.parseInt(s[3]);
          for (int j = 0; j < N; j++) {
            g[i][j] = Integer.parseInt(s[j+4]);
          }
          i++;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Number of teams
   * @return number of teams
   */
  public int numberOfTeams() {
	  return N;
  }
  
  /**
   * All teams
   * @return all teams
   */
  public Iterable<String> teams() {
	  return teams;
  }
  
  /**
   * Number of wins for given team
   * @param team
   * @return number of wins for given team
   */
  public int wins(String team) {
	  if (!teams.contains(team)) throw new IllegalArgumentException("Invalid team");
	  return w[teamIndex.get(team)];
  }
  
  /**
   * Number of losses for given team
   * @param team
   * @return number of losses for given team
   */
  public int losses(String team) {
	  if (!teams.contains(team)) throw new IllegalArgumentException("Invalid team");
	  return l[teamIndex.get(team)];
  }
  
  /**
   * Number of remaining games for given team
   * @param team
   * @return number of remaining games for given team
   */
  public int remaining(String team) {
	  if (!teams.contains(team)) throw new IllegalArgumentException("Invalid team");
	  return r[teamIndex.get(team)];
  }
  
  /**
   * Number of remaining games between team1 and team2
   * @param team1
   * @param team2
   * @return number of remaining games between team1 and team2
   */
  public int against(String team1, String team2) {
	  if (!teams.contains(team1) || !teams.contains(team2)) throw new IllegalArgumentException("Invalid team");
	  return g[teamIndex.get(team1)][teamIndex.get(team2)];
  }
  
  /**
   * Is given team eliminated?
   * @param team
   * @return is given team eliminated?
   */
  public boolean isEliminated(String team) {
	  if (!teams.contains(team)) throw new IllegalArgumentException("Invalid team");
	  if (N < 2) return false;
      int x = teamIndex.get(team);
      for (int i = 0; i < N; i++) {
        if (w[x] + r[x] < w[i]) {
        	return true;
        }
      }
      
      FlowNetwork G = buildNetwork(x);
      //System.out.println(G.toString());
      
      int t = G.V() - 1;
      ff = new FordFulkerson(G, 0, t);
      //System.out.println(G.toString());
      
      for (FlowEdge e : G.adj(0)) {
        if (e.flow() < e.capacity()) {
        	return true;
        }
      }
      
	  return false;
  }
  
  /**
   * Subset R of teams that eliminates given team; null if not eliminated
   * @param team
   * @return subset R of teams that eliminates given team; null if not eliminated
   */
  public Iterable<String> certificateOfElimination(String team) {
	  if (!teams.contains(team)) throw new IllegalArgumentException("Invalid team");
	  if (!isEliminated(team)) return null;
	  
	  List<String> eliminatingTeams = new ArrayList<String>();
	  //check for trivial elimination
	  for (String other : teams) {
		  if (!other.equals(team) && wins(team) + remaining(team) < wins(other)) {
			  eliminatingTeams.add(other);
			  return eliminatingTeams;
		  }
	  }  
	  //build FlowNet to compute the certificate
	  FlowNetwork G = buildNetwork(teamIndex.get(team));
	  int t = G.V() - 1;
      ff = new FordFulkerson(G, 0, t);
	  for (String tm : teams) {
		  if (!tm.equals(team)) {
			  int v = teamVertexIndex.get(teamIndex.get(tm));
			  if (ff.inCut(v)) eliminatingTeams.add(tm);
		  }  
	  }
	  
	  return eliminatingTeams;
  }
  
  private FlowNetwork buildNetwork(int team) {
	  int nbOfVSNodes = ( N - 1 ) * ( N - 2 ) / 2;
	  int nbOfTeamNods = N - 1;
	  //nb of nodes in network flow: s + vsNodes + otherTeams + t
	  int nbOfNodes = 1 + nbOfVSNodes + nbOfTeamNods + 1;
	  FN = new FlowNetwork(nbOfNodes);
	  
	  int v = 1;
	  for (int i = 0; i < N; i++) {
		  for (int j = i+1; j < N; j++) {
			  if (i == team || j == team) continue;
			  //System.out.println(i + " - " + j + ": " + g[i][j]);
			  FlowEdge e = new FlowEdge(0, v, g[i][j]);
			  FN.addEdge(e);
			  v++;
		  }
	  }
	  
	  //second set of edges that determine the winners (inf capacity)
	  int x = 1;
	  for (int i = 0; i < N-1; i++) {
		  for (int j = i + 1; j < N-1; j++) {	  
			  FlowEdge e1 = new FlowEdge(x, v + i, Double.POSITIVE_INFINITY);
			  FN.addEdge(e1);
			  FlowEdge e2 = new FlowEdge(x, v + j, Double.POSITIVE_INFINITY);
			  FN.addEdge(e2);
			  x++;
		  }
	  }
	  
	  //third set of edges towards t (sinq) with wi + ri - wk capacity
	  teamVertexIndex = new HashMap<Integer, Integer>();
	  int t = nbOfNodes - 1;
	  for (int i = 0; i < N; i++) {
		  if (i == team) continue;
		  FlowEdge e = new FlowEdge(nbOfVSNodes++ + 1, t, w[team] + r[team] - w[i]);
		  teamVertexIndex.put(i, e.from());
		  FN.addEdge(e);
	  }
	  return FN;
  }
  
  public static void main(String... args) {
	  BaseballElimination division = new BaseballElimination("test/teams4.txt");
	  for (String team : division.teams()) {
		  if (division.isEliminated(team)) {
			  StdOut.print(team + " is eliminated by the subset R = { ");
			  for (String t : division.certificateOfElimination(team)) {
				  StdOut.print(t + " ");
			  }
			  StdOut.println("}");
		  } else {
			  StdOut.println(team + " is not eliminated");
		  }
	  } 
  }
}
