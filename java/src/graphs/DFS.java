package graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Recursive depth-first search. Neighbours are visited in increasing order.
public class DFS {
    public record Result(int[] par, List<Integer> order, List<Integer> finish, List<String> backEdges) {}

    private static final int UNVISITED = 0;
    private static final int ACTIVE = 1;     // visited, still on the recursion stack
    private static final int FINISHED = 2;   // visited, all its neighbours done

    private final Graph g;
    private final boolean trace;
    private final int[] state;
    private final int[] par;
    private final List<Integer> order = new ArrayList<>();
    private final List<Integer> finish = new ArrayList<>();
    private final List<String> backEdges = new ArrayList<>();

    private DFS(Graph g, boolean trace) {
        this.g = g;
        this.trace = trace;
        this.state = new int[g.size()];
        this.par = new int[g.size()];
        Arrays.fill(par, -1);
    }

    public static Result dfs(Graph g, int s, boolean trace) {
        DFS search = new DFS(g, trace);
        search.visit(s, 0);
        return new Result(search.par, search.order, search.finish, search.backEdges);
    }

    private void visit(int v, int depth) {
        String indent = "  ".repeat(depth);
        state[v] = ACTIVE;
        order.add(v);
        if (trace) {
            System.out.println(indent + "visit " + v + (par[v] == -1 ? "" : " (par " + par[v] + ")"));
        }
        for (int u : g.neighbours(v)) {
            if (state[u] == UNVISITED) {
                par[u] = v;
                visit(u, depth + 1);
            } else if (state[u] == ACTIVE && u != par[v]) {
                backEdges.add(v + "-" + u);
                if (trace) {
                    System.out.println(indent + "  back edge " + v + "-" + u);
                }
            }
        }
        state[v] = FINISHED;
        finish.add(v);
        if (trace) {
            System.out.println(indent + "finish " + v);
        }
    }
}
