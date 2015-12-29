import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.SET;

public class BaseballElimination {
    private int teamNum;
    private LinearProbingHashST<Integer, Team> teams;
    private LinearProbingHashST<String, Integer> nametoid;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        String s = in.readLine();
        if (s == null) 
            throw new IllegalArgumentException("Cannot find team amount!");
        teamNum = Integer.parseInt(s);
        teams = new LinearProbingHashST<Integer, Team>();
        nametoid = new LinearProbingHashST<String, Integer>();
        for (int i = 0; i < teamNum; i++) {
            s = in.readLine();
            if (s == null) throw new IllegalArgumentException("No enough data!");
            String[] line = s.trim().split(" +");
            int[] schedule = new int[teamNum];
            for (int j = 0; j < teamNum; j++) {
                schedule[j] = Integer.parseInt(line[j + 4]);
            }
            nametoid.put(line[0], i);
            teams.put(i, new Team(line[0], Integer.parseInt(line[1]), 
                                  Integer.parseInt(line[2]), 
                                  Integer.parseInt(line[3]), schedule));
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamNum;
    }

    // all teams
    public Iterable<String> teams() {
        return nametoid.keys();
    } 

    // number of wins for given team
    public int wins(String team) {
        if (nametoid.get(team) == null) 
            throw new IllegalArgumentException("Illegal team name!");
        return teams.get(nametoid.get(team)).wins();
    } 
    
    // number of losses for given team
    public int losses(String team) {
        if (nametoid.get(team) == null) 
            throw new IllegalArgumentException("Illegal team name!");
        return teams.get(nametoid.get(team)).losses();
    } 

    // number of remaining games for given team
    public int remaining(String team) {
        if (nametoid.get(team) == null) 
            throw new IllegalArgumentException("Illegal team name!");
        return teams.get(nametoid.get(team)).remaining();
    } 

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if ((nametoid.get(team1) == null) || (nametoid.get(team2) == null)) 
            throw new IllegalArgumentException("Illegal team name!");
        return against(nametoid.get(team1), nametoid.get(team2));
    } 

    private int against(int id1, int id2) {
        int[] schedule = teams.get(id1).schedule();
        return schedule[id2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (nametoid.get(team) == null) 
            throw new IllegalArgumentException("Illegal team name!");
        int targetid = nametoid.get(team);
        Team target = teams.get(targetid);

        FlowNetwork f = new FlowNetwork(2 + (teamNum - 1)*(teamNum)/2);
        int potential = target.wins() + target.remaining();
        int count = 0;
        int teamVertex = (teamNum - 1)*(teamNum - 2)/2 + 1;
        for (int i = 0; i < teamNum - 1; i++) {
            Team other;
            if (i < targetid) other = teams.get(i); 
            else other = teams.get(i + 1);

            if (other.wins() > potential) return true;
            if (other.wins() == potential) continue;
            f.addEdge(new FlowEdge(teamVertex + i, f.V() - 1, 
                                   potential - other.wins()));
        }
        for (int i  = 0; i < teamNum - 1; i++) {
            for (int j = i + 1; j < teamNum - 1; j++) {
                int capacity;
                if (i < targetid) {
                    if (j < targetid) capacity = against(i, j);
                    else capacity = against(i, j + 1);
                } else {
                    capacity = against(i + 1, j + 1);
                }
                if (capacity == 0) {
                    count++;
                    continue;
                }
                f.addEdge(new FlowEdge(0, ++count, capacity));
                f.addEdge(new FlowEdge(count, teamVertex + i, 
                                       Double.POSITIVE_INFINITY));
                f.addEdge(new FlowEdge(count, teamVertex + j, 
                                       Double.POSITIVE_INFINITY));
            }
        }
        
        FordFulkerson ff = new FordFulkerson(f, 0, f.V() - 1);
        for (FlowEdge edge: f.adj(0)) {
            if (ff.inCut(edge.to()))
                return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (nametoid.get(team) == null) 
            throw new IllegalArgumentException("Illegal team name!");
        int targetid = nametoid.get(team);
        Team target = teams.get(targetid);

        FlowNetwork f = new FlowNetwork(2 + (teamNum - 1)*(teamNum)/2);
        int potential = target.wins() + target.remaining();
        int count = 0;
        int teamVertex = (teamNum - 1)*(teamNum - 2)/2 + 1;
        for (int i = 0; i < teamNum - 1; i++) {
            Team other;
            if (i < targetid) other = teams.get(i); 
            else other = teams.get(i + 1);

            if (other.wins() > potential) {
                SET<String> result = new SET<String>();
                result.add(other.name());
                return result;
            }
            if (other.wins() == potential) continue;

            f.addEdge(new FlowEdge(teamVertex + i, f.V() - 1, 
                                   potential - other.wins()));
        }
        for (int i  = 0; i < teamNum - 1; i++) {
            for (int j = i + 1; j < teamNum - 1; j++) {
                int capacity;
                if (i < targetid) {
                    if (j < targetid) capacity = against(i, j);
                    else capacity = against(i, j + 1);
                } else {
                    capacity = against(i + 1, j + 1);
                }
                if (capacity == 0) {
                    count++;
                    continue;
                }
                f.addEdge(new FlowEdge(0, ++count, capacity));
                f.addEdge(new FlowEdge(count, teamVertex + i, 
                                       Double.POSITIVE_INFINITY));
                f.addEdge(new FlowEdge(count, teamVertex + j, 
                                       Double.POSITIVE_INFINITY));
            }
        }
        
        FordFulkerson ff = new FordFulkerson(f, 0, f.V() - 1);
        SET<String> result = new SET<String>();
        for (FlowEdge edge: f.adj(0)) {
            int vertex = edge.to();
            if (ff.inCut(vertex)) {
                for (FlowEdge e: f.adj(vertex)) {
                    int i = e.to();
                    if (i == vertex) continue;
                    if (i - teamVertex < targetid)
                        result.add(teams.get(i - teamVertex).name());
                    else
                        result.add(teams.get(i - teamVertex + 1).name());
                }
            }
        }
        if (!result.isEmpty()) return result;
        return null;
    }

    // print output
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
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