package heaps;

import common.Stats;
import java.util.Arrays;
import java.util.Scanner;

public class Trace {
    static final int CAPACITY = 32;

    // Commands:  i 55   insert 55        x   extract max
    //            b 3 9 2 7   build a heap from these numbers
    //            c   clear (empty heap)  q   quit
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] a = new int[CAPACITY];
        int heapSize = 0;

        System.out.println("Max-heap tracer. Commands: i <n> | x | b <numbers> | c | q");
        while (true) {
            System.out.print("heap " + show(a, heapSize) + " > ");
            String[] parts = in.nextLine().trim().split("\\s+");
            String cmd = parts[0].toLowerCase();
            Stats stats = new Stats();

            try {
                switch (cmd) {
                    case "q" -> {
                        System.out.println("Bye!");
                        return;
                    }
                    case "c" -> heapSize = 0;
                    case "i" -> {
                        if (heapSize == CAPACITY) {
                            System.out.println("heap is full");
                            continue;
                        }
                        int key = Integer.parseInt(parts[1]);
                        heapSize = Heap.insert(a, heapSize, key, stats);
                        System.out.println("insert " + key + " -> " + show(a, heapSize)
                                + "  (" + stats.getComparisons() + " comparisons)");
                    }
                    case "x" -> {
                        if (heapSize == 0) {
                            System.out.println("heap is empty");
                            continue;
                        }
                        int max = Heap.extractMax(a, heapSize, stats);
                        heapSize--;
                        System.out.println("extract " + max + " -> " + show(a, heapSize)
                                + "  (" + stats.getComparisons() + " comparisons)");
                    }
                    case "b" -> {
                        int[] values = new int[parts.length - 1];
                        for (int k = 1; k < parts.length; k++) {
                            values[k - 1] = Integer.parseInt(parts[k]);
                        }
                        Heap.buildMaxHeap(values, stats, true);
                        System.arraycopy(values, 0, a, 0, values.length);
                        heapSize = values.length;
                    }
                    default -> System.out.println("unknown command: " + cmd);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("usage: i <number> | x | b <numbers...> | c | q");
            }
        }
    }

    static String show(int[] a, int heapSize) {
        return Arrays.toString(Arrays.copyOfRange(a, 0, heapSize));
    }
}