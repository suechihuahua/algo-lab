package graphs;

import common.Stats;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Interactive tracer for every graph algorithm.
// Pick one or more algorithms, pick a graph, pick a start vertex, read the traces.
public class Trace {
    // To add an algorithm: add its name here AND a case in run() below.
    static final String[] ALGORITHMS = {"BFS", "DFS", "Dijkstra", "Bellman-Ford", "Prim", "Kruskal"};

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            List<String> chosen = askAlgorithms(in);
            if (chosen == null) {
                System.out.println("Bye!");
                return;
            }
            WeightedGraph g = askGraph(in);
            System.out.println("adjacency list (weights in brackets):");
            GraphPrint.weighted(g);

            boolean needsStart = false;
            for (String name : chosen) {
                if (!name.equals("Kruskal")) {
                    needsStart = true;
                }
            }
            int s = needsStart ? askStart(in, g) : 0;

            for (String name : chosen) {
                System.out.println();
                System.out.println("=== " + name + " ===");
                run(name, g, s);
            }
            System.out.println();
        }
    }

    static void run(String name, WeightedGraph g, int s) {
        boolean undirectedOnly = name.equals("BFS") || name.equals("DFS")
                || name.equals("Prim") || name.equals("Kruskal");
        if (undirectedOnly && g.isDirected()) {
            System.out.println("skipped: " + name + " here needs an undirected graph");
            return;
        }
        boolean hasNegative = false;
        for (WeightedGraph.Edge e : g.edges()) {
            if (e.weight() < 0) {
                hasNegative = true;
            }
        }
        if (g.usesLetters() && (name.equals("BFS") || name.equals("DFS"))) {
            System.out.println("(BFS/DFS print vertex numbers: a=0, b=1, c=2, ...)");
        }

        switch (name) {
            case "BFS" -> {
                BFS.Result r = BFS.bfs(g.toUnweighted(), s, true);
                System.out.println("order " + r.order());
                GraphPrint.layers(r.dist());
                System.out.println("BFS tree:");
                GraphPrint.tree(r.par(), s);
            }
            case "DFS" -> {
                DFS.Result r = DFS.dfs(g.toUnweighted(), s, true);
                System.out.println("order " + r.order() + "   finish " + r.finish()
                        + "   back edges " + r.backEdges());
                System.out.println("DFS tree:");
                GraphPrint.tree(r.par(), s);
            }
            case "Dijkstra" -> {
                if (hasNegative) {
                    System.out.println("warning: negative edge weights -- Dijkstra may be wrong here");
                }
                Dijkstra.Result r = Dijkstra.run(g, s, new Stats(), true);
                printDistances(g, r.dist(), r.par());
                System.out.println("shortest-path tree:");
                GraphPrint.tree(r.par(), s, g::label);
            }
            case "Bellman-Ford" -> {
                BellmanFord.Result r = BellmanFord.run(g, s, new Stats(), true);
                if (r.negativeCycle()) {
                    System.out.println("negative cycle reachable: distances are not meaningful");
                } else {
                    printDistances(g, r.dist(), r.par());
                    System.out.println("shortest-path tree:");
                    GraphPrint.tree(r.par(), s, g::label);
                }
            }
            case "Prim" -> {
                Prim.Result r = Prim.run(g, s, new Stats(), true);
                System.out.println("MST:");
                GraphPrint.tree(r.par(), s, g::label);
            }
            case "Kruskal" -> Kruskal.run(g, new Stats(), true);
            default -> throw new IllegalArgumentException("unknown algorithm: " + name);
        }
    }

    static void printDistances(WeightedGraph g, int[] dist, int[] par) {
        StringBuilder sb = new StringBuilder("dist:");
        for (int v = 0; v < g.size(); v++) {
            sb.append(' ').append(g.label(v)).append('=')
              .append(dist[v] == Integer.MAX_VALUE ? "inf" : String.valueOf(dist[v]));
            if (par[v] != -1) {
                sb.append('(').append(g.label(par[v])).append(')');
            }
        }
        System.out.println(sb + "   (parent in brackets)");
    }

    static List<String> askAlgorithms(Scanner in) {
        System.out.println("Algorithms:");
        for (int i = 0; i < ALGORITHMS.length; i++) {
            System.out.println("  " + (i + 1) + ") " + ALGORITHMS[i]);
        }
        while (true) {
            System.out.print("Choose one or more (e.g. 3 5), 'all', or 'q' to quit: ");
            String line = in.nextLine().trim();
            if (line.equalsIgnoreCase("q")) {
                return null;
            }
            List<String> chosen = new ArrayList<>();
            if (line.equalsIgnoreCase("all")) {
                chosen.addAll(List.of(ALGORITHMS));
                return chosen;
            }
            try {
                for (String part : line.split("[\\s,]+")) {
                    chosen.add(ALGORITHMS[Integer.parseInt(part) - 1]);
                }
                return chosen;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Please enter numbers from 1 to " + ALGORITHMS.length + ".");
            }
        }
    }

    static WeightedGraph askGraph(Scanner in) {
        while (true) {
            System.out.println("Graph:");
            System.out.println("  1) notes' BFS/DFS example (0-5, all weights 1)");
            System.out.println("  2) notes' weighted example (a-j) -- Dijkstra / Prim / Kruskal tables");
            System.out.println("  3) notes' negative-edge example (directed: Dijkstra fails, Bellman-Ford works)");
            System.out.println("  4) random");
            System.out.println("  5) type your own");
            System.out.print("Choose 1-5: ");
            String line = in.nextLine().trim();
            try {
                switch (line) {
                    case "1" -> {
                        WeightedGraph g = new WeightedGraph(6, false, false);
                        int[][] edges = {{0, 1}, {0, 3}, {0, 4}, {1, 2}, {1, 5}, {2, 3}, {3, 4}, {4, 5}};
                        for (int[] e : edges) {
                            g.addEdge(e[0], e[1], 1);
                        }
                        return g;
                    }
                    case "2" -> {
                        return WeightedGraph.notesExample();
                    }
                    case "3" -> {
                        return WeightedGraph.negativeExample();
                    }
                    case "4" -> {
                        return randomGraph(in);
                    }
                    case "5" -> {
                        return typedGraph(in);
                    }
                    default -> System.out.println("Please choose 1 to 5.");
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Couldn't read that graph, let's try again.");
            }
        }
    }

    static WeightedGraph randomGraph(Scanner in) {
        System.out.print("Seed (press Enter for a new random one): ");
        String text = in.nextLine().trim();
        long seed;
        try {
            seed = text.isEmpty() ? System.currentTimeMillis() : Long.parseLong(text);
        } catch (NumberFormatException e) {
            System.out.println("Not a number, using a new random seed.");
            seed = System.currentTimeMillis();
        }
        System.out.print("Directed? (y/N): ");
        boolean directed = in.nextLine().trim().equalsIgnoreCase("y");
        Random rng = new Random(seed);
        int n = 5 + rng.nextInt(4);
        int extra = 1 + rng.nextInt(3);
        System.out.println("seed " + seed + ": " + n + " vertices, " + (n - 1 + extra) + " edges, weights 0-9");
        return WeightedGraph.random(n, extra, 9, directed, rng);
    }

    // Edges like  a-c:1 a-b:6  (letters)  or  0-1:4 1-2  (numbers; no ":w" means weight 1)
    static WeightedGraph typedGraph(Scanner in) {
        System.out.print("Number of vertices: ");
        int n = Integer.parseInt(in.nextLine().trim());
        System.out.print("Name vertices with letters a, b, c...? (y/N): ");
        boolean letters = in.nextLine().trim().equalsIgnoreCase("y");
        System.out.print("Directed? (y/N): ");
        boolean directed = in.nextLine().trim().equalsIgnoreCase("y");
        WeightedGraph g = new WeightedGraph(n, directed, letters);
        String example = letters ? "a-c:1 a-b:6 c-e:4" : "0-1:4 1-2:2 0-2";
        System.out.print("Edges as pairs with optional weight, e.g. " + example + ": ");
        for (String token : in.nextLine().trim().split("\\s+")) {
            if (token.isEmpty()) {
                continue;
            }
            String[] parts = token.split(":");
            String[] ends = parts[0].split("-");
            int w = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            int u = g.parseVertex(ends[0]);
            int v = g.parseVertex(ends[1]);
            if (u < 0 || u >= n || v < 0 || v >= n) {
                throw new IndexOutOfBoundsException("vertex out of range in " + token);
            }
            g.addEdge(u, v, w);
        }
        return g;
    }

    static int askStart(Scanner in, WeightedGraph g) {
        String range = g.label(0) + ".." + g.label(g.size() - 1);
        while (true) {
            System.out.print("Start vertex (" + range + "): ");
            try {
                int s = g.parseVertex(in.nextLine());
                if (s >= 0 && s < g.size()) {
                    return s;
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                // fall through and ask again
            }
            System.out.println("Please enter a vertex from " + range + ".");
        }
    }
}
