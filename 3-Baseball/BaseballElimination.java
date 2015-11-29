import edu.princeton.cs.algs4.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class BaseballElimination {
    private class Team {
        String name;
        int wins;
        int looses;
        int remains;

        boolean isCalculated;
        ArrayList<Integer> dep;
    }

    private Team[] teams;
    private int[][] matches;

    public BaseballElimination(String filename) throws Exception {
        Scanner file = new Scanner(new FileReader(filename));
        int n = file.nextInt();
        teams = new Team[n];
        matches = new int[n][n];

        file.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = file.nextLine();
            Scanner s = new Scanner(line);
            teams[i] = new Team();
            teams[i].name = s.next();
            teams[i].wins = s.nextInt();
            teams[i].looses = s.nextInt();
            teams[i].remains = s.nextInt();

            for (int j = 0; j < n; ++j) {
                matches[i][j] = s.nextInt();
            }
        }
    }

    private int getTeamIdByName(String name) {
        for (int i = 0; i < teams.length; ++i) {
            if (teams[i].name.equals(name)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid team name");
    }

    public int numberOfTeams() {
        return teams.length;
    }

    public Iterable<String> teams() {
        ArrayList<String> arr = new ArrayList<>(teams.length);
        for (int i = 0; i < teams.length; ++i) {
            arr.add(i, teams[i].name);
        }
        return arr;
    }

    public int wins(String name) {
        return teams[getTeamIdByName(name)].wins;
    }

    public int losses(String name) {
        return teams[getTeamIdByName(name)].looses;
    }

    public int remaining(String name) {
        return teams[getTeamIdByName(name)].remains;
    }

    public int against(String team1, String team2) {
        return matches[getTeamIdByName(team1)][getTeamIdByName(team2)];
    }

    private void determine(int self) {
        if (teams[self].isCalculated) return;
        teams[self].isCalculated = true;
        final int bestScore = teams[self].wins + teams[self].remains;
        for (int i = 0; i < teams.length; ++i) {
            if (i == self) continue;
            if (bestScore < teams[i].wins) {
                teams[self].dep = new ArrayList<>();
                teams[self].dep.add(i);
                return;
            }
        }

        // Number of vertex
        //      2               : Source, Dest                  1
        //      (v - 1)(v - 2)  : match of teams excluding v    N * N
        //      v - 1           : teams excluding v             N
        final int N = teams.length;
        final int matchIdBase = 1;
        final int teamIdBase = matchIdBase + N * N;
        final int destId = teamIdBase + N;
        FlowNetwork network = new FlowNetwork(destId + 1);

        for (int i = 0; i < teams.length; ++i) {
            if (i == self) continue;
            for (int j = i + 1; j < teams.length; ++j) {
                if (j == self) continue;
                if (matches[i][j] == 0) continue;
                int matchId = matchIdBase + i * N + j;
                // StdOut.printf("match %d-%d -> %d\n", i, j, matchId);

                // Add source -> match
                network.addEdge(new FlowEdge(0, matchId, matches[i][j]));

                // Add match -> team
                network.addEdge(new FlowEdge(matchId, teamIdBase + i, Double.MAX_VALUE));
                network.addEdge(new FlowEdge(matchId, teamIdBase + j, Double.MAX_VALUE));
            }
        }

        // Add team -> dest
        for (int i = 0; i < teams.length; ++i) {
            if (i == self) continue;
            // StdOut.printf("team %d -> %d\n", i, teamIdBase + i);
            network.addEdge(new FlowEdge(teamIdBase + i, destId, bestScore - teams[i].wins));
        }

        // Run max flow!
        FordFulkerson ff = new FordFulkerson(network, 0, destId);
        // StdOut.println(network);

        // Check result
        boolean isEliminated = false;
        for (FlowEdge e : network.adj(0)) {
            int i = (e.to() - matchIdBase) / N;
            int j = (e.to() - matchIdBase) % N;
            if (e.capacity() - e.flow() > 1e-10) {
                isEliminated = true;
                break;
            }
        }
        if (!isEliminated) return;

        // Get dependencies
        teams[self].dep = new ArrayList<>();
        for (int i = 0; i < teams.length; ++i) {
            if (i == self) continue;
            if (ff.inCut(teamIdBase + i)) teams[self].dep.add(i);
        }
    }

    public boolean isEliminated(String name) {
        int self = getTeamIdByName(name);
        determine(self);
        return teams[self].dep != null;
    }

    public Iterable<String> certificateOfElimination(String name) {
        int self = getTeamIdByName(name);
        determine(self);
        if (teams[self].dep == null) return null;

        ArrayList<String> ret = new ArrayList<>();
        for (int id : teams[self].dep) ret.add(teams[id].name);
        return ret;
    }

    public static void main(String[] args) throws Exception {
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
