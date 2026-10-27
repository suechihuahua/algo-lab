package graphs;

import common.Stats;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

// Dijkstra's algorithm (non-negative weights only).
// Repeatedly removes the unfinished vertex with the smallest dist (ties: smallest vertex),
// then relaxes its outgoing edges. Stats counts relaxation attempts.
public class Dijkstra {
    public static final int INF = Integer.MAX_VALUE;

    public record Result(int[] dist, int[] par, List<Integer> order) {}

    public static Result run(WeightedGraph g, int s, Stats stats, boolean trace) {
        int n = g.size();
        int[] dist = new int[n];
        int[] par = new int[n];
        boolean[] done = new boolean[n];
        Arrays.fill(dist, INF);
        Arrays.fill(par, -1);
        List<Integer> order = new ArrayList<>();

        // Entries are {dist, vertex}. Smaller dist first; equal dist -> smaller vertex first.
        // A vertex can be in the queue several times (once per improvement); stale copies are skipped.
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (x, y) -> x[0] != y[0] ? Integer.compare(x[0], y[0]) : Integer.compare(x[1], y[1]));
        dist[s] = 0;
        pq.add(new int[]{0, s});

        while (!pq.isEmpty()) {
            int[] top = pq.remove();
            int v = top[1];
            if (done[v] || top[0] != dist[v]) {
                continue;
            }
            done[v] = true;
            order.add(v);

            StringBuilder changes = new StringBuilder();
            for (WeightedGraph.Edge e : g.neighbours(v)) {
                int w = e.to();
                if (done[w]) {
                    continue;
                }
                stats.incrementComparisons();
                int candidate = dist[v] + e.weight();
                if (candidate < dist[w]) {
                    if (dist[w] == INF) {
                        changes.append(g.label(w)).append(" -> ").append(candidate).append("  ");
                    } else {
                        changes.append(g.label(w)).append(" improves ").append(dist[w])
                               .append(" -> ").append(candidate).append("  ");
                    }
                    dist[w] = candidate;
                    par[w] = v;
                    pq.add(new int[]{candidate, w});
                }
            }
            if (trace) {
                System.out.println("remove " + g.label(v) + " (dist " + dist[v] + "): "
                        + (changes.length() == 0 ? "-" : changes.toString().trim()));
            }
        }
        return new Result(dist, par, order);
    }
}
