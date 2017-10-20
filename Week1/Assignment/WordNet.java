import java.util.*;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;


public class WordNet {
	private Digraph wordNet;
	private HashMap<Integer, String> synsetArray;
	private HashMap<String, ArrayList<Integer>> synsetMap;
	private SAP sapWordNet;

	//constructor takes the name of the two inputs files
	public WordNet (String synsets, String hypernyms) {
		synsetArray = new HashMap<Integer, String>();
		synsetMap = new HashMap<String, ArrayList<Integer>>();
		In syn = new In(synsets);
		int numVertices = 0;
		String line;
		while ((line = syn.readLine()) != null) {
			numVertices++;
			String[] lineSplit = line.split(",");
			int id = Integer.parseInt(lineSplit[0]);
			synsetArray.put(id, lineSplit[1]);
			String nouns[] = lineSplit[1].split(" ");
			for (int i = 0; i < nouns.length; i++) {
				if(!synsetMap.containsKey(nouns[i])) {
					synsetMap.put(nouns[i], new ArrayList<Integer>());
					synsetMap.get(nouns[i]).add(id);
				}
				else synsetMap.get(nouns[i]).add(id);
			}
		}
		Digraph wordNet = new Digraph(numVertices);
		In hyper = new In(hypernyms);
		while ((line = hyper.readLine()) != null) {
			String[] idSplit = line.split(",");
			int v = Integer.parseInt(idSplit[0]);
			for (int i = 1; i < idSplit.length; i++) {
				int w = Integer.parseInt(idSplit[i]);
				wordNet.addEdge(v, w);
			}
		}

		Topological wordNetTop = new Topological(wordNet);
		if (! wordNetTop.hasOrder()) throw new IllegalArgumentException();

		sapWordNet = new SAP(wordNet);

		// boolean word1 = synsetMap.containsKey("a");
		// System.out.println(word1);
	}

	//returns all WordNet nouns
	public Iterable<String> nouns() {
		return synsetMap.keySet();
	}

	//is the word a WordNet noun?
	public boolean isNoun(String word) {
		return synsetMap.containsKey(word);
	}

	//distance between nounA and nounB 
	public int distance(String nounA, String nounB) {
		if(!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		return sapWordNet.length(synsetMap.get(nounA), synsetMap.get(nounB));
	}

	//a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if(!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		int ancestor = sapWordNet.ancestor(synsetMap.get(nounA), synsetMap.get(nounB));
		return synsetArray.get(ancestor);
	}

	//do unit testing
	public static void main(String[] args) {
		//In in1 = new In(args[0]);
		//In in2 = new In(args[1]);
		WordNet test10 = new WordNet(args[0], args[1]);
		System.out.println(test10.distance("a" ,"f"));
	}
}