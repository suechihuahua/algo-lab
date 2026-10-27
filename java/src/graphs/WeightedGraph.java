package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

// Weighted graph on vertices 0..n-1, directed or undirected.
// Vertices can be shown as letters (0 = a, 1 = b, ...) to match the notes' examples.
// edges() keeps the order edges were added in: Bellman-Ford relaxes in that order, and
// Kruskal breaks ties between equal weights by it.
public class WeightedGraph {
    public record Edge(int from, int to, int weight) {}

    private final boolean directed;
    private final boolean letters;
    private final List<List<Edge>> adj = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public WeightedGraph(int n, boolean directed, boolean letters) {
        this.directed = directed;
        this.letters = letters;
        for (int v = 0; v < n; v++) {
            adj.add(new ArrayList<>());
        }
    }

    public int size() {
        return adj.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public boolean usesLetters() {
        return letters;
    }

    public void addEdge(int u, int v, int w) {
        edges.add(new Edge(u, v, w));
        adj.get(u).add(new Edge(u, v, w));
        adj.get(u).sort(Comparator.comparingInt(Edge::to));
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w));
            adj.get(v).sort(Comparator.comparingInt(Edge::to));
        }
    }

    // Outgoing edges of v, sorted by the other endpoint (increasing order).
    public List<Edge> neighbours(int v) {
        return Collections.unmodifiableList(adj.get(v));
    }

    // Every edge once, in the order it was added.
    public List<Edge> edges() {
        return Collections.unmodifiableList(edges);
    }

    public boolean hasEdge(int u, int v) {
        for (Edge e : adj.get(u)) {
            if (e.to() == v) {
                return true;
            }
        }
        return false;
    }

    // The same vertices and edges without weights or directions, for BFS and DFS.
    public Graph toUnweighted() {
        Graph u = new Graph(size());
        for (Edge e : edges) {
            if (!u.neighbours(e.from()).contains(e.to())) {
                u.addEdge(e.from(), e.to());
            }
        }
        return u;
    }

    public String label(int v) {
        return letters ? String.valueOf((char) ('a' + v)) : String.valueOf(v);
    }

    public int parseVertex(String text) {
        text = text.trim();
        return letters ? text.charAt(0) - 'a' : Integer.parseInt(text);
    }

    public String show(Edge e) {
        return label(e.from()) + (directed ? "->" : "-") + label(e.to()) + " (" + e.weight() + ")";
    }

    // The 10-vertex example from the notes (G3/G4): Dijkstra, Prim and Kruskal all use it.
    // Edges are listed in the notes' sorted order, so Kruskal breaks ties the same way.
    public static WeightedGraph notesExample() {
        WeightedGraph g = new WeightedGraph(10, false, true);
        String[] edges = {"g-h:0", "f-h:0", "a-c:1", "g-i:2", "d-f:2", "b-d:3", "c-e:4", "h-i:4",
                "e-f:5", "f-j:5", "a-b:6", "i-j:6", "c-g:7", "e-h:7", "b-e:8", "e-d:9", "h-j:9"};
        for (String e : edges) {
            g.addEdge(e.charAt(0) - 'a', e.charAt(2) - 'a', Integer.parseInt(e.substring(4)));
        }
        return g;
    }

    // The notes' counterexample where Dijkstra fails: 0->1 (2), 0->2 (1), 1->2 (-3).
    public static WeightedGraph negativeExample() {
        WeightedGraph g = new WeightedGraph(3, true, false);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 2, -3);
        return g;
    }

    // A random connected graph: a random tree plus `extra` more edges, weights 0..maxWeight.
    public static WeightedGraph random(int n, int extra, int maxWeight, boolean directed, Random rng) {
        WeightedGraph g = new WeightedGraph(n, directed, false);
        for (int v = 1; v < n; v++) {
            g.addEdge(rng.nextInt(v), v, rng.nextInt(maxWeight + 1));
        }
        long maxEdges = directed ? (long) n * (n - 1) : (long) n * (n - 1) / 2;
        int added = 0;
        while (added < extra && n - 1 + added < maxEdges) {
            int u = rng.nextInt(n);
            int v = rng.nextInt(n);
            if (u != v && !g.hasEdge(u, v) && (directed || !g.hasEdge(v, u))) {
                g.addEdge(u, v, rng.nextInt(maxWeight + 1));
                added++;
            }
        }
        return g;
    }
}
