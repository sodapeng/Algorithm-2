import java.util.*;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {
	private final Digraph G;
	private static final int INFINITY = Integer.MAX_VALUE;

	//constrcutor takes a digraph (not necessarily a DAG)
	public SAP (Digraph G) {
		this.G = new Digraph(G);
	}

	//length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		int min = INFINITY;
		BreadthFirstDirectedPaths bfdv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfdw = new BreadthFirstDirectedPaths(G, w);
		for (int i = 0; i < G.V(); i++) {
			//check whether there is a path or not first
			if(bfdv.hasPathTo(i) && bfdw.hasPathTo(i)) {
				if ( bfdv.distTo(i) + bfdw.distTo(i) < min) {
					min = bfdw.distTo(i) + bfdv.distTo(i);
				}
			}
		}
		if (min == INFINITY) return -1;
		return min;
	}

	//a common ancestor of v and w that participates in a shorthest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		int ancestor = -1; 
		int min = INFINITY;
		BreadthFirstDirectedPaths bfdv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfdw = new BreadthFirstDirectedPaths(G, w);
		for (int i = 0; i < G.V(); i++) {
			if(bfdv.hasPathTo(i) && bfdw.hasPathTo(i)) {
				if ( bfdv.distTo(i) + bfdw.distTo(i) < min) {
					min = bfdv.distTo(i) + bfdw.distTo(i);
					ancestor = i;
				}
			}
		}
		return ancestor;
	}

	//length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		int min = INFINITY;
		BreadthFirstDirectedPaths bfdv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfdw = new BreadthFirstDirectedPaths(G, w);
		for (int i = 0; i < G.V(); i++) {
			//check whether there is a path or not first
			if(bfdv.hasPathTo(i) && bfdw.hasPathTo(i)) {
				if ( bfdv.distTo(i) + bfdw.distTo(i) < min) {
					min = bfdw.distTo(i) + bfdv.distTo(i);
				}
			}
		}
		if (min == INFINITY) return -1;
		return min;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		int ancestor = -1; 
		int min = INFINITY;
		BreadthFirstDirectedPaths bfdv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfdw = new BreadthFirstDirectedPaths(G, w);
		for (int i = 0; i < G.V(); i++) {
			if(bfdv.hasPathTo(i) && bfdw.hasPathTo(i)) {
				if ( bfdv.distTo(i) + bfdw.distTo(i) < min) {
					min = bfdv.distTo(i) + bfdw.distTo(i);
					ancestor = i;
				}
			}
		}
		return ancestor;
	}

	//test unit
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);

		while (!StdIn.isEmpty()) {
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }

	}

}