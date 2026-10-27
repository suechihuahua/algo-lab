package graphs;

import common.Stats;
import java.io.PrintWriter;
import java.util.Random;

// Measures every graph algorithm on random connected undirected graphs with E = 5V.
// Writes data/graphs.csv (run from the repo root). One row per run.
// ops = what the algorithm counts through Stats:
//   Dijkstra / Bellman-Ford: relaxation attempts   Prim: key checks   Kruskal: edges examined
//   BFS / DFS don't count anything (ops = -1): compare their time against V + E instead.
public class Experiment {
    static final int TRIALS = 3;
    static final int[] SIZES = {1000, 2000, 4000, 8000, 16000};
    static final int BELLMAN_FORD_MAX_V = 4000;     // O(V * E): bigger sizes take too long
    static final int EDGES_PER_VERTEX = 5;
    static final int MAX_WEIGHT = 99;

    public static void main(String[] args) throws Exception {
        // Recursive DFS can go thousands of levels deep, so run on a thread with a big stack.
        Thread worker = new Thread(null, Experiment::runAll, "experiment", 1L << 29);
        worker.start();
        worker.join();
    }

    static void runAll() {
        Random rng = new Random(42);
        warmUp(rng);
        try (PrintWriter out = new PrintWriter("data/graphs.csv")) {
            out.println("algorithm,V,E,trial,ops,time_ns");
            for (int v : SIZES) {
                for (int trial = 1; trial <= TRIALS; trial++) {
                    WeightedGraph g = randomGraph(v, rng);
                    Graph plain = g.toUnweighted();
                    int e = g.edges().size();

                    long start = System.nanoTime();
                    BFS.bfs(plain, 0, false);
                    row(out, "BFS", v, e, trial, -1, System.nanoTime() - start);

                    start = System.nanoTime();
                    DFS.dfs(plain, 0, false);
                    row(out, "DFS", v, e, trial, -1, System.nanoTime() - start);

                    Stats stats = new Stats();
                    start = System.nanoTime();
                    Dijkstra.run(g, 0, stats, false);
                    row(out, "Dijkstra", v, e, trial, stats.getComparisons(), System.nanoTime() - start);

                    if (v <= BELLMAN_FORD_MAX_V) {
                        stats = new Stats();
                        start = System.nanoTime();
                        BellmanFord.run(g, 0, stats, false);
                        row(out, "Bellman-Ford", v, e, trial, stats.getComparisons(), System.nanoTime() - start);
                    }

                    stats = new Stats();
                    start = System.nanoTime();
                    Prim.run(g, 0, stats, false);
                    row(out, "Prim", v, e, trial, stats.getComparisons(), System.nanoTime() - start);

                    stats = new Stats();
                    start = System.nanoTime();
                    Kruskal.run(g, stats, false);
                    row(out, "Kruskal", v, e, trial, stats.getComparisons(), System.nanoTime() - start);
                }
                System.out.println("done V = " + v);
            }
        } catch (java.io.IOException ex) {
            throw new RuntimeException("could not write data/graphs.csv -- run from the repo root", ex);
        }
        System.out.println("wrote data/graphs.csv");
    }

    static WeightedGraph randomGraph(int v, Random rng) {
        int extra = (EDGES_PER_VERTEX - 1) * v + 1;   // tree has V-1 edges, so total E = 5V
        return WeightedGraph.random(v, extra, MAX_WEIGHT, false, rng);
    }

    static void row(PrintWriter out, String algorithm, int v, int e, int trial, long ops, long time) {
        out.println(algorithm + "," + v + "," + e + "," + trial + "," + ops + "," + time);
    }

    static void warmUp(Random rng) {
        for (int round = 0; round < 10; round++) {
            WeightedGraph g = randomGraph(500, rng);
            Graph plain = g.toUnweighted();
            BFS.bfs(plain, 0, false);
            DFS.dfs(plain, 0, false);
            Dijkstra.run(g, 0, new Stats(), false);
            BellmanFord.run(g, 0, new Stats(), false);
            Prim.run(g, 0, new Stats(), false);
            Kruskal.run(g, new Stats(), false);
        }
    }
}
