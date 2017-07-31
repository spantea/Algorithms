public class Outcast {

  private final WordNet wordnet;
   /**
    * @param wordnet constructor takes a WordNet object
    */
   public Outcast(WordNet wordnet) {
     this.wordnet = wordnet;
   }
   
   /**
    * @param nouns, an array of WordNet nouns
    * @return an outcast
    */
   public String outcast(String[] nouns) {
     int maxDistance = Integer.MIN_VALUE;
     String outcast = null;
     for (String noun : nouns) {
       int nounDistance = 0;
       if (wordnet.isNoun(noun)) {
         for(String otherNoun: nouns) { 
           nounDistance += wordnet.distance(noun, otherNoun);
         }
         if (nounDistance > maxDistance) {
           maxDistance = nounDistance;
           outcast = noun;
         }
       }
     }
     return outcast;
   }

   // see test client below
//   public static void main(String[] args) {
//     WordNet wordnet = new WordNet(args[0], args[1]);
//     Outcast outcast = new Outcast(wordnet);
//     for (int t = 2; t < args.length; t++) {
//         In in = new In(args[t]);
//         String[] nouns = in.readAllStrings();
//         StdOut.println(args[t] + ": " + outcast.outcast(nouns));
//     }
//
//   }
}