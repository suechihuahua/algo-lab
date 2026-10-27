package graphs;

import java.util.Random;
import java.util.Scanner;

public class Trace {
    // Edges are typed as pairs:  0-1 0-3 1-2   (or press Enter for the notes' example)
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            Graph g = askGraph(in);
            if (g == null) {
                System.out.println("Bye!");
                return;
            }
            System.out.println("adjacency list:");
            GraphPrint.adjacencyList(g);

            int s = askStart(in, g.size());
            System.out.println();
            System.out.println("=== BFS from " + s + " ===");
            BFS.Result r = BFS.bfs(g, s, true);
            System.out.println("order " + r.order());
            GraphPrint.layers(r.dist());
            System.out.println("BFS tree:");
            GraphPrint.tree(r.par(), s);
            System.out.println();
        }
    }

    static Graph askGraph(Scanner in) {
        while (true) {
            System.out.print("Number of vertices (Enter = notes' example, r = random, q = quit): ");
            String line = in.nextLine().trim();
            if (line.equalsIgnoreCase("q")) {
                return null;
            }
            if (line.isEmpty()) {
                return Graph.notesExample();
            }
            if (line.equalsIgnoreCase("r")) {
                return randomGraph(in);
            }
            try {
                int n = Integer.parseInt(line);
                Graph g = new Graph(n);
                System.out.print("Edges, e.g. 0-1 0-3 1-2: ");
                for (String pair : in.nextLine().trim().split("\\s+")) {
                    if (pair.isEmpty()) {
                        continue;
                    }
                    String[] ends = pair.split("-");
                    g.addEdge(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]));
                }
                return g;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Couldn't read that. Vertices are 0..n-1, edges like 2-5.");
            }
        }
    }

    static Graph randomGraph(Scanner in) {
        System.out.print("Seed (press Enter for a new random one): ");
        String text = in.nextLine().trim();
        long seed;
        try {
            seed = text.isEmpty() ? System.currentTimeMillis() : Long.parseLong(text);
        } catch (NumberFormatException e) {
            System.out.println("Not a number, using a new random seed.");
            seed = System.currentTimeMillis();
        }
        Random rng = new Random(seed);
        int n = 5 + rng.nextInt(4);
        int extra = 1 + rng.nextInt(3);
        System.out.println("seed " + seed + ": " + n + " vertices, " + (n - 1 + extra) + " edges");
        return Graph.random(n, extra, rng);
    }

    static int askStart(Scanner in, int n) {
        while (true) {
            System.out.print("Start vertex (0.." + (n - 1) + "): ");
            try {
                int s = Integer.parseInt(in.nextLine().trim());
                if (s >= 0 && s < n) {
                    return s;
                }
            } catch (NumberFormatException e) {
                // fall through and ask again
            }
            System.out.println("Please enter a number from 0 to " + (n - 1) + ".");
        }
    }
}