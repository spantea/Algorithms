import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {
	private Set<String> words;
	private Set<String> dict;
	private BoggleBoard board;
	
	/**
	 *  Initializes the data structure using the given array of strings as the dictionary.
	 *  (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	 * @param dictionary
	 */
    public BoggleSolver(String[] dictionary) {
    	dict = new TreeSet<String>();
        for (String word : dictionary) dict.add(word);
    }

    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     * @param board
     * @return
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	this.board = board;
	    words = new TreeSet<String>();
	    findWords();
	    return words;
    }

    /**
     *  Returns the score of the given word if it is in the dictionary, zero otherwise.
     *  (You can assume the word contains only the uppercase letters A through Z.)
     * @param word
     * @return
     */
    public int scoreOf(String word) {
    	int len = word.length();
        if (len <= 2) return 0;
        else if (len > 2 && len <= 4) return 1;
        else if (len == 5) return 2;
        else if (len == 6) return 3;
        else if (len == 7) return 5;
        else if (len > 7) return 11;
        return 0;
    }
    
    private void findWords() {
    	int M = board.rows();
    	int N = board.cols();
    	for (int i = 0; i < M; i++) {
    		for (int j = 0; j < N; j++) {
    			//String str = String.valueOf(board.getLetter(i, j));
    			findWords(i, j, "", new boolean[board.rows()][board.cols()]);  	    	
    		}
    	}
    }
    
    private void findWords(int i, int j, String str, boolean[][] marked) {
    	if (i < 0 || j < 0 || i >= board.rows() || j >= board.cols() || marked[i][j]) {
    		return;
    	}
    	if (str.equals("TRIES") /*dict.contains(str)*/) {
    		System.out.println(str);
    		words.add(str);
    	}
    	
    	str += board.getLetter(i, j);
    	marked[i][j] = true;
    	
    	findWords(i - 1, j, str, marked);
    	findWords(i, j - 1, str, marked);
    	findWords(i + 1, j, str, marked);
    	findWords(i, j + 1, str, marked);
    	findWords(i - 1, j - 1, str, marked);
    	findWords(i - 1, j + 1, str, marked);
    	findWords(i + 1, j - 1, str, marked);
    	findWords(i + 1, j + 1, str, marked);
    	marked[i][j] = false;
    }
    
    public static void main(String[] args) {
//    	BoggleBoard board = new BoggleBoard();
//    	System.out.println(board.toString());
//    	new BoggleSolver(new String[0]).getAllValidWords(board);
    	In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard bd = new BoggleBoard(args[1]);
        System.out.println(bd.toString());
        int score = 0;
        for (String word : solver.getAllValidWords(bd))
        {
            //StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
	}
}
