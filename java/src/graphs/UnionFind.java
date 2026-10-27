package graphs;

import java.util.function.IntFunction;

// Disjoint sets as a forest: union by rank + path compression (the notes' version).
// On equal ranks, the root of y's set becomes the new root and its rank grows by 1.
public class UnionFind {
    private final int[] par;
    private final int[] rank;

    public UnionFind(int n) {
        par = new int[n];
        rank = new int[n];
        for (int x = 0; x < n; x++) {
            par[x] = x;          // Make-Set(x): x is its own root
        }
    }

    // Find-Set with path compression: every node on the path is re-pointed to the root.
    public int find(int x) {
        if (par[x] != x) {
            par[x] = find(par[x]);
        }
        return par[x];
    }

    // Returns true if x and y were in different sets (and are now merged).
    public boolean union(int x, int y) {
        x = find(x);
        y = find(y);
        if (x == y) {
            return false;
        }
        if (rank[x] > rank[y]) {
            par[y] = x;
        } else {
            par[x] = y;
            if (rank[x] == rank[y]) {
                rank[y]++;
            }
        }
        return true;
    }

    public int parentOf(int x) {
        return par[x];
    }

    public int rankOf(int x) {
        return rank[x];
    }

    // e.g.  par: a->a b->d c->c ...   rank: a0 b0 ...
    public String show(IntFunction<String> label) {
        StringBuilder sb = new StringBuilder("par:");
        for (int x = 0; x < par.length; x++) {
            sb.append(' ').append(label.apply(x)).append("->").append(label.apply(par[x]));
        }
        sb.append("   rank:");
        for (int x = 0; x < rank.length; x++) {
            sb.append(' ').append(label.apply(x)).append(rank[x]);
        }
        return sb.toString();
    }
}
