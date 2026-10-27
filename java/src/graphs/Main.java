package graphs;

import common.Stats;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Correctness tests for every graph algorithm: the notes' worked examples, edge cases,
// and random graphs checked against simple independent references.
public class Main {
    static final int INF = BFS.INF;
    static int failures = 0;

    public static void main(String[] args) {
        testBFS();
        System.out.println();
        testDFS();
        System.out.println();
        testShortestPaths();
        System.out.println();
        testUnionFind();
        System.out.println();
        testMST();
        System.out.println();
        System.out.println(failures == 0 ? "ALL TESTS PASSED" : failures + " TEST(S) FAILED");
    }

    // ---------- BFS ----------

    static void testBFS() {
        Graph notes = Graph.notesExample();
        bfsCase("notes from 0", notes, 0, List.of(0, 1, 3, 4, 2, 5), new int[]{0, 1, 2, 1, 1, 2});
        bfsCase("notes from 5", notes, 5, List.of(5, 1, 4, 0, 2, 3), new int[]{2, 1, 2, 2, 1, 0});
        bfsCase("path", path(4), 0, List.of(0, 1, 2, 3), new int[]{0, 1, 2, 3});
        bfsCase("disconnected", split(), 0, List.of(0, 1), new int[]{0, 1, INF, INF});
        bfsCase("single", new Graph(1), 0, List.of(0), new int[]{0});
    }

    static void bfsCase(String name, Graph g, int s, List<Integer> order, int[] dist) {
        BFS.Result r = BFS.bfs(g, s, false);
        check("bfs  " + name, r.order().equals(order) && Arrays.equals(r.dist(), dist));
    }

    // ---------- DFS ----------

    static void testDFS() {
        Graph notes = Graph.notesExample();
        dfsCase("notes from 0", notes, 0, List.of(0, 1, 2, 3, 4, 5), List.of("3-0", "4-0", "5-1"));
        dfsCase("notes from 5", notes, 5, List.of(5, 1, 0, 3, 2, 4), List.of("2-1", "4-0", "4-5"));
        dfsCase("path", path(4), 0, List.of(0, 1, 2, 3), List.of());
        dfsCase("disconnected", split(), 0, List.of(0, 1), List.of());
        dfsCase("triangle", triangle(), 0, List.of(0, 1, 2), List.of("2-0"));

        // A tree has no cycles, so DFS must find no back edges; anything with a cycle must.
        Random rng = new Random(1);
        boolean ok = true;
        for (int t = 0; t < 300; t++) {
            int n = 2 + rng.nextInt(9);
            Graph tree = Graph.random(n, 0, rng);
            Graph cyclic = Graph.random(Math.max(n, 3), 1, rng);
            ok &= DFS.dfs(tree, 0, false).backEdges().isEmpty();
            ok &= !DFS.dfs(cyclic, 0, false).backEdges().isEmpty();
            ok &= DFS.dfs(tree, 0, false).order().size() == n;
        }
        check("dfs  300 random trees/cyclic", ok);
    }

    static void dfsCase(String name, Graph g, int s, List<Integer> order, List<String> back) {
        DFS.Result r = DFS.dfs(g, s, false);
        check("dfs  " + name, r.order().equals(order) && r.backEdges().equals(back));
    }

    // ---------- Dijkstra and Bellman-Ford ----------

    static void testShortestPaths() {
        WeightedGraph notes = WeightedGraph.notesExample();
        int[] expected = {0, 6, 1, 9, 5, 8, 8, 8, 10, 13};     // a..j, from the notes' table
        check("dijkstra notes from a",
                Arrays.equals(Dijkstra.run(notes, 0, new Stats(), false).dist(), expected));
        check("bellman  notes from a",
                Arrays.equals(BellmanFord.run(notes, 0, new Stats(), false).dist(), expected));

        WeightedGraph neg = WeightedGraph.negativeExample();
        check("dijkstra negative example gives the WRONG 1 (as the notes say)",
                Dijkstra.run(neg, 0, new Stats(), false).dist()[2] == 1);
        check("bellman  negative example gives the right -1",
                BellmanFord.run(neg, 0, new Stats(), false).dist()[2] == -1);

        WeightedGraph cycle = new WeightedGraph(3, true, false);
        cycle.addEdge(0, 1, 1);
        cycle.addEdge(1, 2, -2);
        cycle.addEdge(2, 1, 1);
        check("bellman  detects a negative cycle", BellmanFord.run(cycle, 0, new Stats(), false).negativeCycle());
        check("bellman  no false alarm", !BellmanFord.run(notes, 0, new Stats(), false).negativeCycle());

        WeightedGraph lonely = new WeightedGraph(3, false, false);
        lonely.addEdge(0, 1, 5);
        int[] d = Dijkstra.run(lonely, 0, new Stats(), false).dist();
        check("dijkstra unreachable stays inf", d[2] == INF && d[1] == 5);

        // Random graphs vs Floyd-Warshall (a simple, independent all-pairs reference).
        Random rng = new Random(2);
        boolean ok = true;
        for (int t = 0; t < 500; t++) {
            int n = 1 + rng.nextInt(9);
            boolean directed = rng.nextBoolean();
            WeightedGraph g = WeightedGraph.random(n, rng.nextInt(n + 1), 9, directed, rng);
            int[][] ref = floydWarshall(g);
            int s = rng.nextInt(n);
            ok &= Arrays.equals(Dijkstra.run(g, s, new Stats(), false).dist(), ref[s]);
            ok &= Arrays.equals(BellmanFord.run(g, s, new Stats(), false).dist(), ref[s]);
        }
        check("dijkstra + bellman vs Floyd-Warshall, 500 random graphs", ok);

        // Negative weights but no cycles (a DAG): only Bellman-Ford is guaranteed correct.
        ok = true;
        for (int t = 0; t < 300; t++) {
            int n = 2 + rng.nextInt(8);
            WeightedGraph dag = new WeightedGraph(n, true, false);
            for (int u = 0; u < n; u++) {
                for (int v = u + 1; v < n; v++) {
                    if (rng.nextInt(3) == 0) {
                        dag.addEdge(u, v, rng.nextInt(15) - 5);
                    }
                }
            }
            BellmanFord.Result r = BellmanFord.run(dag, 0, new Stats(), false);
            ok &= !r.negativeCycle() && Arrays.equals(r.dist(), floydWarshall(dag)[0]);
        }
        check("bellman  negative-weight DAGs vs Floyd-Warshall, 300 graphs", ok);
    }

    static int[][] floydWarshall(WeightedGraph g) {
        int n = g.size();
        long[][] d = new long[n][n];
        for (long[] row : d) {
            Arrays.fill(row, Long.MAX_VALUE / 4);
        }
        for (int v = 0; v < n; v++) {
            d[v][v] = 0;
        }
        for (WeightedGraph.Edge e : g.edges()) {
            d[e.from()][e.to()] = Math.min(d[e.from()][e.to()], e.weight());
            if (!g.isDirected()) {
                d[e.to()][e.from()] = Math.min(d[e.to()][e.from()], e.weight());
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                }
            }
        }
        int[][] out = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out[i][j] = d[i][j] >= Long.MAX_VALUE / 8 ? INF : (int) d[i][j];
            }
        }
        return out;
    }

    // ---------- Union-Find ----------

    static void testUnionFind() {
        UnionFind uf = new UnionFind(6);
        check("uf   fresh sets are separate", uf.find(0) == 0 && uf.find(5) == 5);
        check("uf   union merges", uf.union(0, 1) && uf.find(0) == uf.find(1));
        check("uf   repeated union returns false", !uf.union(1, 0));
        uf.union(2, 3);
        uf.union(1, 3);
        check("uf   transitive", uf.find(0) == uf.find(2) && uf.find(4) != uf.find(0));
        check("uf   equal ranks: y's root wins, rank grows", rankCase());

        // Random unions vs a simple "label array" reference.
        Random rng = new Random(3);
        boolean ok = true;
        for (int t = 0; t < 300; t++) {
            int n = 1 + rng.nextInt(12);
            UnionFind f = new UnionFind(n);
            int[] label = new int[n];
            for (int x = 0; x < n; x++) {
                label[x] = x;
            }
            for (int step = 0; step < 20; step++) {
                int a = rng.nextInt(n);
                int b = rng.nextInt(n);
                boolean merged = f.union(a, b);
                ok &= merged == (label[a] != label[b]);
                int old = label[a];
                for (int x = 0; x < n; x++) {
                    if (label[x] == old) {
                        label[x] = label[b];
                    }
                }
                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < n; y++) {
                        ok &= (f.find(x) == f.find(y)) == (label[x] == label[y]);
                    }
                }
            }
        }
        check("uf   300 random union sequences vs reference", ok);
    }

    static boolean rankCase() {
        UnionFind uf = new UnionFind(2);
        uf.union(0, 1);
        return uf.find(0) == 1 && uf.rankOf(1) == 1 && uf.rankOf(0) == 0;
    }

    // ---------- Prim and Kruskal ----------

    static void testMST() {
        WeightedGraph notes = WeightedGraph.notesExample();
        check("prim     notes MST weight 22", Prim.run(notes, 0, new Stats(), false).totalWeight() == 22);
        check("kruskal  notes MST weight 22", Kruskal.run(notes, new Stats(), false).totalWeight() == 22);
        check("kruskal  notes skips only h-i",
                Kruskal.run(notes, new Stats(), false).tree().size() == 9);

        // Random small graphs vs brute force (try every subset of n-1 edges).
        Random rng = new Random(4);
        boolean ok = true;
        for (int t = 0; t < 300; t++) {
            int n = 1 + rng.nextInt(6);
            WeightedGraph g = WeightedGraph.random(n, rng.nextInt(5), 9, false, rng);
            int best = bruteForceMST(g);
            Kruskal.Result k = Kruskal.run(g, new Stats(), false);
            ok &= Prim.run(g, 0, new Stats(), false).totalWeight() == best;
            ok &= k.totalWeight() == best && k.tree().size() == n - 1;
        }
        check("prim + kruskal vs brute force, 300 random graphs", ok);
    }

    static int bruteForceMST(WeightedGraph g) {
        List<WeightedGraph.Edge> edges = g.edges();
        int n = g.size();
        int m = edges.size();
        int best = Integer.MAX_VALUE;
        for (int mask = 0; mask < (1 << m); mask++) {
            if (Integer.bitCount(mask) != n - 1) {
                continue;
            }
            UnionFind uf = new UnionFind(n);
            int weight = 0;
            boolean tree = true;
            for (int i = 0; i < m && tree; i++) {
                if ((mask & (1 << i)) != 0) {
                    tree = uf.union(edges.get(i).from(), edges.get(i).to());
                    weight += edges.get(i).weight();
                }
            }
            if (tree) {
                best = Math.min(best, weight);
            }
        }
        return n == 1 ? 0 : best;
    }

    // ---------- helpers ----------

    static Graph path(int n) {
        Graph g = new Graph(n);
        for (int v = 0; v + 1 < n; v++) {
            g.addEdge(v, v + 1);
        }
        return g;
    }

    static Graph split() {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(2, 3);
        return g;
    }

    static Graph triangle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(0, 2);
        return g;
    }

    static void check(String name, boolean ok) {
        if (!ok) {
            failures++;
        }
        System.out.printf("%-62s %s%n", name, ok ? "OK" : "MISMATCH");
    }
}
