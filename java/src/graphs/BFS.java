package graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class BFS {
    public static final int INF = Integer.MAX_VALUE;   // stands for "dist = ∞"

    public record Result(int[] dist, int[] par, List<Integer> order) {}

    public static Result bfs(Graph g, int s, boolean trace) {
        int n = g.size();
        int[] dist = new int[n];
        int[] par = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(par, -1);                          // -1 stands for "par = null"
        List<Integer> order = new ArrayList<>();       // vertices in the order they're popped

        Queue<Integer> q = new ArrayDeque<>();
        dist[s] = 0;
        q.add(s);
        if (trace) {
            System.out.println("start: discover " + s + " (dist 0)  Q = " + q);
        }

        while (!q.isEmpty()) {
            int v = q.remove();
            order.add(v);
            StringBuilder found = new StringBuilder();
            for (int u : g.neighbours(v)) {
                if (dist[u] == INF) {
                    dist[u] = dist[v] + 1;
                    par[u] = v;
                    q.add(u);
                    found.append(u).append(" (dist ").append(dist[u])
                         .append(", par ").append(v).append(") ");
                }
            }
            if (trace) {
                System.out.println("pop " + v + ": "
                        + (found.length() == 0 ? "nothing new " : found) + " Q = " + q);
            }
        }
        return new Result(dist, par, order);
    }
}