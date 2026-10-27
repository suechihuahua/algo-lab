package graphs;

import java.util.Scanner;

// Interactive Union-Find: type commands and watch the parent pointers and ranks change.
//   u a b   union the sets containing a and b
//   f a     find a's root (with path compression)
//   r       reset          q   quit
public class UnionFindTrace {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Number of elements: ");
        int n = Integer.parseInt(in.nextLine().trim());
        System.out.print("Name them with letters a, b, c...? (y/N): ");
        boolean letters = in.nextLine().trim().equalsIgnoreCase("y");
        WeightedGraph names = new WeightedGraph(n, false, letters);   // only used for label/parse

        UnionFind uf = new UnionFind(n);
        System.out.println("Commands: u x y (union) | f x (find) | r (reset) | q (quit)");
        while (true) {
            System.out.println(uf.show(names::label));
            System.out.print("> ");
            String[] parts = in.nextLine().trim().split("\\s+");
            try {
                switch (parts[0].toLowerCase()) {
                    case "q" -> {
                        System.out.println("Bye!");
                        return;
                    }
                    case "r" -> uf = new UnionFind(n);
                    case "u" -> {
                        int x = names.parseVertex(parts[1]);
                        int y = names.parseVertex(parts[2]);
                        int rx = uf.find(x);
                        int ry = uf.find(y);
                        boolean merged = uf.union(x, y);
                        System.out.println(merged
                                ? "union: roots " + names.label(rx) + " and " + names.label(ry)
                                  + " -> new root " + names.label(uf.find(x))
                                : "already in the same set (root " + names.label(rx) + ")");
                    }
                    case "f" -> {
                        int x = names.parseVertex(parts[1]);
                        System.out.println("find(" + names.label(x) + ") = " + names.label(uf.find(x)));
                    }
                    default -> System.out.println("unknown command");
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("usage: u x y | f x | r | q   (elements " + names.label(0)
                        + ".." + names.label(n - 1) + ")");
            }
        }
    }
}
