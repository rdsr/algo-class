package algo_class;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * The file contains the adjacency list representation of a simple
 * undirected graph. There are 40 vertices labeled 1 to 40. The first
 * column in the file represents the vertex label, and the particular
 * row (other entries except the first column) tells all the vertices
 * that the vertex is adjacent to. So for example, the 6th row looks
 * liks : "6 29 32 37 27 16". This just means that the vertex with
 * label 6 is adjacent to (i.e., shares an edge with) the vertices
 * with labels 29, 32, 37, 27 and 16.
 * Your task is to code up and run the randomized contraction algorithm
 * for the min cut problem and use it on the above graph to compute the
 * min cut. (HINT: Note that you'll have to figure out an implementation
 * of edge contractions. Initially, you might want to do this naively,
 * creating a new graph from the old every time there's an edge
 * contraction. But you also think about more efficient implementations.)
 * (WARNING: As per the video lectures, please make sure to run the
 * algorithm many times with different random seeds, and remember the
 * smallest cut that you ever find). Write your numeric answer in the
 * space provided. So e.g., if your answer is 5, just type 5 in the space
 * provided
 * 
 * @author rdsr
 */

public class ProgrammingAssignment3 {
    public static int computeMinCut(Graph g) {
        while (g.verticesCount() > 2) {
            final Edge edge = g.getRandomEdge();
            g.merge(edge.u, edge.v);
        }
        return g.noOfEdges();
    }

    public static int computeMinCut(String src) throws FileNotFoundException {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 1600; i++) {
            final Graph g = Graph.buildGraph(new File(src));
            min = Math.min(min, computeMinCut(g));
        }
        return min;
    }

    public static void main(String[] args) throws FileNotFoundException {
        sanityTest();
        System.out.println(computeMinCut("resources/programming_assignment3/kargerAdj.txt"));
    }

    private static void sanityTest() {
        final Integer u = 1;
        final Integer v = 2;
        System.out.println(new Edge(u, v).equals(new Edge(v, u)));
    }

    private static final class Edge {
        Integer u, v;

        public Edge(Integer _u, Integer _v) {
            u = Math.min(_u, _v);
            v = Math.max(_u, _v);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((u == null) ? 0 : u.hashCode());
            result = prime * result + ((v == null) ? 0 : v.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof Edge))
                return false;
            final Edge other = (Edge) obj;
            if (u == null) {
                if (other.u != null)
                    return false;
            } else if (!u.equals(other.u))
                return false;
            if (v == null) {
                if (other.v != null)
                    return false;
            } else if (!v.equals(other.v))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "(" + u + " " + v + ")";
        }
    }

    private static final class Graph {
        private final Random random;
        private List<Edge> edges;
        private final Map<Integer, Set<Integer>> adjList;

        private Graph() {
            random = new Random();
            adjList = new HashMap<Integer, Set<Integer>>();
        }

        static Graph buildGraph(File input) throws FileNotFoundException {
            final Graph g = new Graph();
            final Set<Edge> edges = new HashSet<Edge>();
            final Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                row = row.trim();
                final String[] nos = row.split("\\s+");

                final Integer u = Integer.parseInt(nos[0]);
                final Set<Integer> uInc = new HashSet<Integer>();
                for (int i = 1; i < nos.length; i++) {
                    final Integer v = Integer.parseInt(nos[i]);
                    edges.add(new Edge(u, v));
                    uInc.add(v);
                }

                g.adjList.put(u, uInc);
            }

            g.edges = new LinkedList<Edge>(edges);
            return g;
        }

        public int verticesCount() {
            return adjList.keySet().size();
        }

        public Edge getRandomEdge() {
            final int r = random.nextInt(edges.size());
            return edges.get(r);
        }

        public void merge(Integer a, Integer b) {
            final Set<Integer> aNieghbors = adjList.remove(a);
            final Set<Integer> bNeighbors = adjList.remove(b);
            aNieghbors.remove(b);
            bNeighbors.remove(a);
            bNeighbors.addAll(aNieghbors); // union
            adjList.put(b, bNeighbors);

            fixGraph(a, b);
        }

        private void fixGraph(Integer a, Integer b) {
            final List<Edge> toRemove = new ArrayList<Edge>();
            for (final Edge edge : edges) {
                if (edge.u == a)
                    edge.u = b;
                else if (edge.v == a)
                    edge.v = b;

                if (edge.u == b && edge.v == b)
                    toRemove.add(edge);
            }
            edges.removeAll(toRemove);

            for (final Integer u : adjList.keySet()) {
                final Set<Integer> neighbors = adjList.get(u);
                if (neighbors.contains(a)) {
                    neighbors.remove(a);
                    neighbors.add(b);
                }
            }
        }

        public int noOfEdges() {
            return edges.size();
        }
    }

}
