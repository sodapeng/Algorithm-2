import java.util.*;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {
	private WordNet wordnet;
	private static final int INFINITY = Integer.MIN_VALUE;
	//constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}

	//given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int dismax = INFINITY;
		int[] dissum = new int[nouns.length];
		String nounmax = "";
		for (int i = 0; i < nouns.length; i++) {
			dissum[i] = 0;
			for (int j = 0; j < nouns.length; j++) {
				dissum[i] += wordnet.distance(nouns[i], nouns[j]);
			}
		}			
		for(int i = 0; i < dissum.length; i++) {
			if (dissum[i] > dismax) {
				dismax = dissum[i];
				nounmax = nouns[i];
			}
		}
		return nounmax;
	}
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
}
}

