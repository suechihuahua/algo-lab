package graphs;

import common.Stats;
import java.util.Arrays;

// Bellman-Ford: relax every edge, |V| - 1 rounds; then one extra round to detect a
// negative cycle. Edges are relaxed in the order they were added (undirected edges
// are tried in both directions). Stats counts relaxation attempts.
public class BellmanFord {
    public static final int INF = Integer.MAX_VALUE;

    public record Result(int[] dist, int[] par, boolean negativeCycle) {}

    public static Result run(WeightedGraph g, int s, Stats stats, boolean trace) {
        int n = g.size();
        int[] dist = new int[n];
        int[] par = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(par, -1);
        dist[s] = 0;

        for (int round = 1; round <= n - 1; round++) {
            StringBuilder changes = new StringBuilder();
            for (WeightedGraph.Edge e : g.edges()) {
                relax(g, e.from(), e.to(), e.weight(), dist, par, stats, changes);
                if (!g.isDirected()) {
                    relax(g, e.to(), e.from(), e.weight(), dist, par, stats, changes);
                }
            }
            if (trace) {
                System.out.println("round " + round + ": "
                        + (changes.length() == 0 ? "no change" : changes.toString().trim()));
            }
        }

        boolean negativeCycle = false;
        for (WeightedGraph.Edge e : g.edges()) {
            if (canImprove(dist, e.from(), e.to(), e.weight())
                    || (!g.isDirected() && canImprove(dist, e.to(), e.from(), e.weight()))) {
                negativeCycle = true;
                break;
            }
        }
        if (trace) {
            System.out.println("round " + n + " (check): "
                    + (negativeCycle ? "still improving -> NEGATIVE CYCLE" : "no change -> no negative cycle"));
        }
        return new Result(dist, par, negativeCycle);
    }

    // Relax(u, v): if going through u is shorter, update v.
    private static void relax(WeightedGraph g, int u, int v, int w, int[] dist, int[] par,
                              Stats stats, StringBuilder changes) {
        stats.incrementComparisons();
        if (canImprove(dist, u, v, w)) {
            changes.append(g.label(v)).append(dist[v] == INF ? " inf" : " " + dist[v])
                   .append(" -> ").append(dist[u] + w).append(" (via ").append(g.label(u)).append(")  ");
            dist[v] = dist[u] + w;
            par[v] = u;
        }
    }

    // dist[u] == INF means u is not reached yet, so nothing can be relaxed from it
    // (also avoids INF + w overflowing).
    private static boolean canImprove(int[] dist, int u, int v, int w) {
        return dist[u] != INF && dist[u] + w < dist[v];
    }
}
