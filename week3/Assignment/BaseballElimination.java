import java.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class BaseballElimination {
	private int teamnum;
	private int[] win, lose, remain;
	private int[][] against;
	private HashMap<Integer, String> intteam;
	private HashMap<String, Integer> strteam;
	private FlowNetwork teamflow;

	//create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		intteam = new HashMap<Integer, String>();
		strteam = new HashMap<String, Integer>();
		In teamfile = new In(filename);
		teamnum = teamfile.readInt();
		win = new int[teamnum];
		lose = new int[teamnum];
		remain = new int[teamnum];
		against = new int[teamnum][teamnum];
		int n = 0;
		while (!teamfile.isEmpty()) {
			String name = teamfile.readString();
			intteam.put(n, name);
			strteam.put(name, n);
			//StdOut.println()
			win[n] = teamfile.readInt();
			lose[n] = teamfile.readInt();
			remain[n] = teamfile.readInt();
			int j = 0;
			while( j < teamnum) {
				against[n][j] = teamfile.readInt();
				j++;
			}
			n++;
		}
		// for (String key : strteam.keySet()) {
		// 	StdOut.print(key);
		// 	StdOut.println(strteam.get(key));
		// }
	}

	//number of team
	public int numberOfTeams() {
		return teamnum;
	}

	//all teams
	public Iterable<String> teams() {
		return strteam.keySet();
	}

	//number of wins for given team
	public int wins(String team) {
		int i = strteam.get(team);
		return win[i];
	}

	//number of losses for given team
	public int losses(String team) {
		int i = strteam.get(team);
		return lose[i];
	}

	//number of remaining games for given team
	public int remaining(String team) {
		int i = strteam.get(team);
		return remain[i];
	}

	//number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		int i = strteam.get(team1);
		int j = strteam.get(team2);
		return against[i][j];
	}

	// is given team eliminated?
	public boolean isEliminated (String team) {
		if (checktrivial(team)) return true;
		int numgames = (teamnum - 1) * teamnum / 2;
		int numvertices = numgames + teamnum + 2;
		int s = numvertices - 2;
		int t = numvertices - 1;
		int g = 0;
		teamflow = new FlowNetwork(numvertices);
		int eli = strteam.get(team);
		for (int i = 0; i < teamnum; i++) {
			for (int j = i+1; j < teamnum; j++) {
				if (i != eli && j != eli) {
					FlowEdge e1 = new FlowEdge(s, teamnum+g, against[i][j]);
					FlowEdge e2 = new FlowEdge(teamnum+g, i, Double.POSITIVE_INFINITY);
					FlowEdge e3 = new FlowEdge(teamnum+g, j, Double.POSITIVE_INFINITY);
					teamflow.addEdge(e1);
					teamflow.addEdge(e2);
					teamflow.addEdge(e3);
					g++;
				}
			}
		}
		for(int i = 0; i < teamnum; i++) {
			if (i != eli) {
				FlowEdge e1 = new FlowEdge(i, t, win[eli]+remain[eli] - win[i]);
				teamflow.addEdge(e1);
			}
		}
		//StdOut.println(teamflow);
		FordFulkerson flowff = new FordFulkerson(teamflow, s, t);
		int sumgames = 0;
		for (int i = 0; i < teamnum; i++) {
			for (int j = i +1; j < teamnum; j++) {
				if (i != eli && j != eli) sumgames += against[i][j];
			}
		}
		// StdOut.print("sum ");
		// StdOut.println(sumgames);
		// StdOut.println(flowff.value());
		if (sumgames == flowff.value()) return false;
		return true;

	}


	//check trivial condition
	private boolean checktrivial(String team) {
		int x = strteam.get(team);
		for (int i = 0; i < teamnum; i++) {
			if ( (win[x] + remain[x]) < win[i] ) return true; 
		}
		return false;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		int eli = strteam.get(team);
		ArrayList<String> elimset = new ArrayList<String>();

		for (int i = 0; i < teamnum; i++) {
			if ( (win[eli] + remain[eli]) < win[i] ) elimset.add(intteam.get(i)); 
		}
		if (! elimset.isEmpty()) return elimset;
		
		int numgames = (teamnum - 1) * teamnum / 2;
		int numvertices = numgames + teamnum + 2;
		int s = numvertices - 2;
		int t = numvertices - 1;
		int g = 0;
		teamflow = new FlowNetwork(numvertices);

		for (int i = 0; i < teamnum; i++) {
			for (int j = i+1; j < teamnum; j++) {
				if (i != eli && j != eli) {
					FlowEdge e1 = new FlowEdge(s, teamnum+g, against[i][j]);
					FlowEdge e2 = new FlowEdge(teamnum+g, i, Double.POSITIVE_INFINITY);
					FlowEdge e3 = new FlowEdge(teamnum+g, j, Double.POSITIVE_INFINITY);
					teamflow.addEdge(e1);
					teamflow.addEdge(e2);
					teamflow.addEdge(e3);
					g++;
				}
			}
		}
		for(int i = 0; i < teamnum; i++) {
			if (i != eli) {
				FlowEdge e1 = new FlowEdge(i, t, win[eli]+remain[eli] - win[i]);
				teamflow.addEdge(e1);
			}
		}
		//StdOut.println(teamflow);
		FordFulkerson flowff = new FordFulkerson(teamflow, s, t);
		int sumgames = 0;
		for (int i = 0; i < teamnum; i++) {
			for (int j = i +1; j < teamnum; j++) {
				if (i != eli && j != eli) sumgames += against[i][j];
			}
		}

		if (sumgames == flowff.value()) return null;


		for (int i = 0; i < teamnum; i++) {
			if (flowff.inCut(i)) elimset.add(intteam.get(i));
		}

		return elimset;
	}

	//unit test

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);

		// for (String team : division.teams()) {
		// 	if (division.isEliminated(team)) StdOut.println(team + " is eliminated");
		// 	else StdOut.println(team + " is not eliminated");
		// }

		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			}
			else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}