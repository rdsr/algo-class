package algo_class;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * The file contains the edges of a directed graph. Vertices are
 * labeled as positive integers from 1 to 875714. Every row indicates
 * an edge, the vertex label in first column is the tail and the
 * vertex label in second column is the head (recall the graph is
 * directed, and the edges are directed from the first column vertex
 * to the second column vertex). So for example, the 11th row looks
 * liks : "2 47646". This just means that the vertex with label 2 has
 * an outgoing edge to the vertex with label 47646
 * Your task is to code up the algorithm from the video lectures for
 * computing strongly connected components (SCCs), and to run this
 * algorithm on the given graph.
 * Output Format: You should output the sizes of the 5 largest SCCs in
 * the given graph, in decreasing order of sizes, separated by commas
 * (avoid any spaces). So if your algorithm computes the sizes of the
 * five largest SCCs to be 500, 400, 300, 200 and 100, then your answer
 * should be "500,400,300,200,100". If your algorithm finds less than 5
 * SCCs, then write 0 for the remaining terms. Thus, if your algorithm
 * computes only 3 SCCs whose sizes are 400, 300, and 100, then your
 * answer should be "400,300,100,0,0"
 * 
 * @author rdsr
 */

public class ProgrammingAssignment4 {

    public static void main(String[] args) throws FileNotFoundException {
        final Graph g = Graph.buildGraph("resources/programming_assignment4/SCC_test.txt");
        final Map<Integer, Integer> res = g.computeSCCS();
        final List<Integer> values = new ArrayList<Integer>(res.values());

        /** quick and dirty, I just need top 5 */
        Collections.sort(values, Collections.reverseOrder());
        for (int i = 0; i < 5; i++)
            System.out.println(values.get(i));
    }

    private static final class Graph {
        private static final int N_VERTICES = 9; // 875714;
        private final Map<Integer, Set<Integer>> adjList;

        private Graph() {
            adjList = new HashMap<Integer, Set<Integer>>();
        }

        static Graph buildGraph(String src) throws FileNotFoundException {
            final Graph g = new Graph();
            final File input = new File(src);
            final Scanner scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                final String row = scanner.nextLine().trim();
                final String[] nos = row.split("\\s+");

                final int tail = Integer.parseInt(nos[0]);
                final int head = Integer.parseInt(nos[1]);

                Set<Integer> neighbors = g.adjList.get(tail);
                if (neighbors == null)
                    g.adjList.put(tail, neighbors = new HashSet<Integer>());
                neighbors.add(head);
            }
            return g;
        }

        Graph reverseGraph() {
            final Graph gr = new Graph();

            for (int u = 1; u <= N_VERTICES; u++) {
                final Set<Integer> gNeighbors = adjList.get(u);
                if (gNeighbors == null)
                    continue;

                for (final Integer v : gNeighbors) {
                    Set<Integer> grNeighbors = gr.adjList.get(v);
                    if (grNeighbors == null)
                        gr.adjList.put(v, grNeighbors = new HashSet<Integer>());
                    grNeighbors.add(u);
                }
            }

            return gr;
        }

        Map<Integer, Integer> computeSCCS() {
            final Graph reversed = reverseGraph();
            final int[] finishingTime = reversed.computeFinishingTimes();

            final Map<Integer, Integer> leaderSize = new HashMap<Integer, Integer>();
            final boolean[] explored = new boolean[N_VERTICES + 1];

            for (int i = N_VERTICES; i > 0; i--) {
                final int u = finishingTime[i];
                if (!explored[u]) {
                    leaderSize.put(u, 1);
                    computeSCC(u, u, explored, leaderSize);
                }
            }

            return leaderSize;
        }

        private void computeSCC(int u, int leader, boolean[] explored, Map<Integer, Integer> leaderSize) {
            explored[u] = true;

            final Set<Integer> neighbors = adjList.get(u);
            if (neighbors != null) {
                for (final int v : neighbors) {
                    if (!explored[v]) {
                        leaderSize.put(leader, leaderSize.get(leader) + 1);
                        computeSCC(v, leader, explored, leaderSize);
                    }
                }
            }
        }

        private int[] computeFinishingTimes() {
            final int[] time = new int[1];
            final int[] finishingTime = new int[N_VERTICES + 1];
            final boolean[] explored = new boolean[N_VERTICES + 1];

            for (int u = 1; u <= N_VERTICES; u++) {
                if (!explored[u]) {
                    computeFinishingTimes(u, time, finishingTime, explored);
                }
            }
            return finishingTime;
        }

        private void computeFinishingTimes(int u, int[] time, int[] finishingTime, boolean[] explored) {
            explored[u] = true;

            final Set<Integer> neighbors = adjList.get(u);
            if (neighbors != null) {
                for (final int v : neighbors) {
                    if (!explored[v])
                        computeFinishingTimes(v, time, finishingTime, explored);
                }
            }
            time[0] += 1;
            finishingTime[time[0]] = u;
        }
    }
}
