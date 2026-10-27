package graphs;

import java.util.Arrays;
import java.util.List;

public class Main {
    static final int INF = BFS.INF;

    public static void main(String[] args) {
        Graph notes = Graph.notesExample();
        testBFS("notes from 0", notes, 0, List.of(0, 1, 3, 4, 2, 5), new int[]{0, 1, 2, 1, 1, 2});
        testBFS("notes from 5", notes, 5, List.of(5, 1, 4, 0, 2, 3), new int[]{2, 1, 2, 2, 1, 0});

        Graph path = new Graph(4);
        path.addEdge(0, 1);
        path.addEdge(1, 2);
        path.addEdge(2, 3);
        testBFS("path", path, 0, List.of(0, 1, 2, 3), new int[]{0, 1, 2, 3});

        Graph split = new Graph(4);
        split.addEdge(0, 1);
        split.addEdge(2, 3);
        testBFS("disconnected", split, 0, List.of(0, 1), new int[]{0, 1, INF, INF});

        testBFS("single", new Graph(1), 0, List.of(0), new int[]{0});
    }

    static void testBFS(String name, Graph g, int s, List<Integer> expectedOrder, int[] expectedDist) {
        BFS.Result r = BFS.bfs(g, s, false);
        boolean ok = r.order().equals(expectedOrder) && Arrays.equals(r.dist(), expectedDist);
        System.out.printf("bfs  %-14s order=%s %s%n", name, r.order(), ok ? "OK" : "MISMATCH");
    }
}