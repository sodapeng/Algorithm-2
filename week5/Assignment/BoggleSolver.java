import java.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
	private TST<Integer> dict;
	private SET<String> boardWords;

	// Initializes the data structure using the given array of strings as the dictionary.
	// (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		boardWords = new SET<String>();
		dict = new TST<Integer>();
		for (String word : dictionary) {
			dict.put(word, 0);
		}
	}

	// Returns the SET of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		int row = board.rows();
		int col = board.cols();
		boolean[][] visited = new boolean[row][col];
		for(int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				visited[i][j] = false;
			}
		}
		
		//search for all valid string in the board, and store in SET,words/
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				dfs(board, i, j, charToString(board.getLetter(i,j)), visited, boardWords);
			}
		}
		// for (String word : boardWords)
		// {
		// 	StdOut.println(word);
		// 	//score += solver.scoreOf(word);
		// }

		//traverse each string in the SET, check whether in the dig
		// SET<String> validWords = new SET<String>();
		// for (String words : boardWords) {
		// 	if(dict.contains(words)) validWords.add(words);
		// }
		return boardWords;
	}

	//search every possible string
	private void dfs(BoggleBoard board, int i, int j, String wordsofar, boolean[][] visited, SET<String> boardWords) {
		int row = board.rows();
		int col = board.cols();
		if ( wordsofar.length() >= 2) {
			if (dict.keysWithPrefix(wordsofar) == null) return;
			if (dict.contains(wordsofar)) boardWords.add(wordsofar);
		}
		visited[i][j] = true;
		for(int m = Math.max(0, i-1); m <= i+1 && m < row; m++) {
			for (int n = Math.max(0, j-1); n <= j+1 && n < col; n++) {
				if (! visited[m][n]) {
					String localwordsofar = wordsofar + charToString(board.getLetter(m,n));
					dfs(board, m, n, localwordsofar, visited, boardWords);
				}
			}
		}
		visited[i][j] = false; 
	}

	//Returns the score of the given word if it is in the dictionary, zero otherwise.
	// (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String word) {
		int l = word.length();
		if(! dict.contains(word)) return 0;
		if (l >= 0 && l <=2) return 0;
		if (l >= 3 && l <=4) return 1;
		if (l == 5) return 2;
		if (l == 6) return 3;
		if (l == 7) return 5;
		return 11;
	}

	//convert char to string
	private String charToString(char c) {
		if (c == 'Q') return "QU";
		return String.valueOf(c);
	}

	//unit test
	public static void main(String[] args)
	{
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args[1]);
		int score = 0;
		System.out.println();
		for (String word : solver.getAllValidWords(board))
		{
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
	}
}




