import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private HashMap<String, List<Integer>> syns = new HashMap<String, List<Integer>>();
    private HashMap<Integer, String> synsById = new HashMap<Integer, String>();
    private Digraph hypernymsDG;
    private int V;
    private SAP sap;
    
   /**
    * constructor takes the name of the two input files
    * @param synsets
    * @param hypernyms
    */
   public WordNet(String synsets, String hypernyms) {
     if (synsets == null || hypernyms == null) {
       throw new java.lang.NullPointerException();
     }
     
     //read synsets
     V = readSynsets(synsets);
     
     //read hypernyms     
     readHyperNyms(hypernyms);
   }
   
   private int readSynsets(String synsets) {
     In in = new In(synsets);
     int vertexId = -1;
     
     while (in.hasNextLine()) {
       String line = in.readLine();
       String[] parts = line.split(",");
       vertexId = Integer.parseInt(parts[0]);
       
       String synonym = parts[1];
       String[] synSet = synonym.split(" ");
       
       //construct the data structure for keeping syns by ids
       synsById.put(vertexId, synonym);
       
       //construct the data structure for keeping syns by word-value, and add the vertex ids
       for (String syn : synSet) {
         if (syns.containsKey(syn)) {
           List<Integer> ids = syns.get(syn);
           ids.add(vertexId);
         } else {
           List<Integer> ids = new ArrayList<Integer>();
           ids.add(vertexId);
           syns.put(syn, ids);  
         }
       }
       
     }
     return vertexId+1;
   }
   
   private void readHyperNyms(String hypernyms) {
     In in = new In(hypernyms);
     hypernymsDG = new Digraph(V);
          
     while (in.hasNextLine()) {
       String line = in.readLine();
       String[] parts = line.split(",");
       int v = Integer.parseInt(parts[0]);       
       
       for (int w = 1; w < parts.length; w++) {
         hypernymsDG.addEdge(v, Integer.parseInt(parts[w]));
       }
     }
     
     if (!isRootedGraph(hypernymsDG) || new DirectedCycle(hypernymsDG).hasCycle()) {
       throw new IllegalArgumentException();
     }
     
     sap = new SAP(hypernymsDG);
   }
   
   //checks if digraph has multiple roots
   private boolean isRootedGraph(Digraph G) {
     int roots = 0;
     
     for (int i = 0; i < G.V(); i++) {
       if (G.outdegree(i) == 0) {
         roots++;
         if (roots > 1) return false;
       }
     }
     
     return true;
   }

   /**
    * @return returns all WordNet nouns
    */
   public Iterable<String> nouns() {
     return syns.keySet();
   }

   /**
    * @param word
    * @return is the word a WordNet noun?
    */
   public boolean isNoun(String word) {
     if (word == null) {
       throw new java.lang.NullPointerException();
     }
     
     return syns.containsKey(word);
   }

   /**
    * @param nounA
    * @param nounB
    * @return distance between nounA and nounB (defined below)
    */
   public int distance(String nounA, String nounB) {
     if (nounA == null || nounB == null) {
       throw new java.lang.NullPointerException();
     }
     
     List<Integer> v = syns.get(nounA);
     List<Integer> w = syns.get(nounB);
     
     if (v == null || w == null) {
       throw new java.lang.IllegalArgumentException();
     }

     return sap.length(v, w);
   }

   /**
    * @param nounA
    * @param nounB
    * @return a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    *  in a shortest ancestral path (defined below)
    */
   public String sap(String nounA, String nounB) {
     if (nounA == null || nounB == null) {
       throw new java.lang.NullPointerException();
     }
     
     List<Integer> v = syns.get(nounA);
     List<Integer> w = syns.get(nounB);
     
     int ancestorId = sap.ancestor(v, w);
     return synsById.get(ancestorId);
   }
   
   // do unit testing of this class
   public static void main(String[] args) {
     WordNet wn = new WordNet("test/synsets.txt","test/hypernyms.txt");
     System.out.println(wn.distance("worm", "bird"));
     System.out.println(wn.sap("worm", "bird"));
     
     System.out.println();
     
     System.out.println(wn.distance("ilang-ilang", "acyl_group"));
     System.out.println(wn.sap("ilang-ilang", "acyl_group"));
     
     //WordNet wn2 = new WordNet("test/synsets.txt","test/hypernyms3InvalidCycle.txt");
   }
}