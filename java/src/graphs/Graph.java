package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Undirected, unweighted graph on vertices 0..n-1, stored as an adjacency list.
// Each neighbour list is kept in increasing order (the lecture's visiting order).
public class Graph {
    private final List<List<Integer>> adj = new ArrayList<>();

    public Graph(int n) {
        for (int v = 0; v < n; v++) {
            adj.add(new ArrayList<>());
        }
    }

    public int size() {
        return adj.size();
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
        Collections.sort(adj.get(u));
        Collections.sort(adj.get(v));
    }

    public List<Integer> neighbours(int v) {
        return Collections.unmodifiableList(adj.get(v));
    }

    // The 6-vertex example graph from the notes (G1/G2).
    public static Graph notesExample() {
        Graph g = new Graph(6);
        int[][] edges = {{0, 1}, {0, 3}, {0, 4}, {1, 2}, {1, 5}, {2, 3}, {3, 4}, {4, 5}};
        for (int[] e : edges) {
            g.addEdge(e[0], e[1]);
        }
        return g;
    }

    // A random connected graph for practice: a random tree (so every vertex is
    // reachable) plus `extra` additional edges (which create cycles).
    public static Graph random(int n, int extra, Random rng) {
        Graph g = new Graph(n);
        for (int v = 1; v < n; v++) {
            g.addEdge(v, rng.nextInt(v));
        }
        int maxEdges = n * (n - 1) / 2;
        int edges = n - 1;
        int added = 0;
        while (added < extra && edges < maxEdges) {
            int u = rng.nextInt(n);
            int v = rng.nextInt(n);
            if (u != v && !g.neighbours(u).contains(v)) {
                g.addEdge(u, v);
                edges++;
                added++;
            }
        }
        return g;
    }
}