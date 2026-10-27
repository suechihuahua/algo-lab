package graphs;

import java.util.ArrayList;
import java.util.List;

// Text pictures of a graph and of BFS/DFS results.
public class GraphPrint {

    // 0: 1 3 4
    public static void adjacencyList(Graph g) {
        for (int v = 0; v < g.size(); v++) {
            System.out.print(v + ":");
            for (int u : g.neighbours(v)) {
                System.out.print(" " + u);
            }
            System.out.println();
        }
    }

    // Grid with 1 where an edge exists, . where it doesn't.
    public static void matrix(Graph g) {
        int n = g.size();
        System.out.print("   ");
        for (int c = 0; c < n; c++) {
            System.out.printf("%2d", c);
        }
        System.out.println();
        for (int r = 0; r < n; r++) {
            System.out.printf("%2d|", r);
            for (int c = 0; c < n; c++) {
                System.out.printf("%2s", g.neighbours(r).contains(c) ? "1" : ".");
            }
            System.out.println();
        }
    }

    // Vertices grouped by distance from the start (BFS layers).
    public static void layers(int[] dist) {
        int max = -1;
        for (int d : dist) {
            if (d != BFS.INF && d > max) {
                max = d;
            }
        }
        for (int layer = 0; layer <= max; layer++) {
            System.out.print("layer " + layer + ":");
            for (int v = 0; v < dist.length; v++) {
                if (dist[v] == layer) {
                    System.out.print(" " + v);
                }
            }
            System.out.println();
        }
    }

    // The search tree built from the par[] array, drawn sideways:
    // 0
    // +-- 1
    // |   `-- 2
    public static void tree(int[] par, int root) {
        System.out.println(root);
        printChildren(par, root, "");
    }

    private static void printChildren(int[] par, int v, String indent) {
        List<Integer> children = new ArrayList<>();
        for (int u = 0; u < par.length; u++) {
            if (par[u] == v) {
                children.add(u);
            }
        }
        for (int k = 0; k < children.size(); k++) {
            boolean last = (k == children.size() - 1);
            int child = children.get(k);
            System.out.println(indent + (last ? "`-- " : "+-- ") + child);
            printChildren(par, child, indent + (last ? "    " : "|   "));
        }
    }
}