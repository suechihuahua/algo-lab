package graphs;

import common.Stats;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Kruskal's MST: take edges from lightest to heaviest, keep an edge if its ends are in
// different trees (checked with Union-Find). The sort is stable, so equal weights keep
// the order the edges were added in. Stats counts edges examined.
public class Kruskal {
    public record Result(List<WeightedGraph.Edge> tree, int totalWeight) {}

    public static Result run(WeightedGraph g, Stats stats, boolean trace) {
        List<WeightedGraph.Edge> sorted = new ArrayList<>(g.edges());
        sorted.sort(Comparator.comparingInt(WeightedGraph.Edge::weight));
        if (trace) {
            StringBuilder sb = new StringBuilder("sorted:");
            for (WeightedGraph.Edge e : sorted) {
                sb.append(' ').append(g.label(e.from())).append('-').append(g.label(e.to()))
                  .append('(').append(e.weight()).append(')');
            }
            System.out.println(sb);
        }

        UnionFind uf = new UnionFind(g.size());
        List<WeightedGraph.Edge> tree = new ArrayList<>();
        int total = 0;
        for (WeightedGraph.Edge e : sorted) {
            if (tree.size() == g.size() - 1) {
                break;                               // n - 1 edges: the tree is complete
            }
            stats.incrementComparisons();
            boolean added = uf.union(e.from(), e.to());
            if (added) {
                tree.add(e);
                total += e.weight();
            }
            if (trace) {
                System.out.println(g.show(e) + ": " + (added ? "add" : "skip, same tree (cycle)"));
            }
        }
        if (trace) {
            System.out.println(uf.show(g::label));
            System.out.println("MST weight " + total + " with " + tree.size() + " edges");
        }
        return new Result(tree, total);
    }
}
