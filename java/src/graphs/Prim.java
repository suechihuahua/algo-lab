package graphs;

import common.Stats;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

// Prim's MST: like Dijkstra, but key[v] is the weight of the single cheapest edge from the
// tree to v (not a path length). Ties: smallest vertex first. Stats counts key checks.
public class Prim {
    public static final int INF = Integer.MAX_VALUE;

    public record Result(int[] par, int[] key, List<Integer> order, int totalWeight) {}

    public static Result run(WeightedGraph g, int s, Stats stats, boolean trace) {
        int n = g.size();
        int[] key = new int[n];
        int[] par = new int[n];
        boolean[] inTree = new boolean[n];
        Arrays.fill(key, INF);
        Arrays.fill(par, -1);
        List<Integer> order = new ArrayList<>();
        int total = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (x, y) -> x[0] != y[0] ? Integer.compare(x[0], y[0]) : Integer.compare(x[1], y[1]));
        key[s] = 0;
        pq.add(new int[]{0, s});

        while (!pq.isEmpty()) {
            int[] top = pq.remove();
            int v = top[1];
            if (inTree[v] || top[0] != key[v]) {
                continue;
            }
            inTree[v] = true;
            order.add(v);
            if (par[v] != -1) {
                total += key[v];
            }

            StringBuilder changes = new StringBuilder();
            for (WeightedGraph.Edge e : g.neighbours(v)) {
                int w = e.to();
                if (inTree[w]) {
                    continue;
                }
                stats.incrementComparisons();
                if (e.weight() < key[w]) {
                    if (key[w] == INF) {
                        changes.append(g.label(w)).append(": ").append(e.weight()).append("  ");
                    } else {
                        changes.append(g.label(w)).append(" improves ").append(key[w])
                               .append(" -> ").append(e.weight()).append("  ");
                    }
                    key[w] = e.weight();
                    par[w] = v;
                    pq.add(new int[]{key[w], w});
                }
            }
            if (trace) {
                String via = par[v] == -1 ? "start"
                        : "via " + g.label(par[v]) + "-" + g.label(v) + " (" + key[v] + ")";
                System.out.println("add " + g.label(v) + " " + via + ": "
                        + (changes.length() == 0 ? "-" : changes.toString().trim()));
            }
        }
        if (trace) {
            System.out.println("MST weight " + total);
        }
        return new Result(par, key, order, total);
    }
}
