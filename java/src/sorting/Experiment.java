package sorting;

import common.ArrayGen;
import common.Stats;
import java.io.PrintWriter;
import java.util.Random;

// Measures comparisons and time for every sort on random, sorted and reversed input.
// Writes data/sorting.csv (run from the repo root). One row per run.
public class Experiment {
    static final int TRIALS = 5;
    static final int[] SMALL = {1000, 2000, 4000, 8000, 16000};                     // O(n^2) cases
    static final int[] LARGE = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 256000};
    static final String[] INPUTS = {"random", "sorted", "reversed"};
    static final String[] ALGORITHMS = {"insertion", "merge", "quick", "heap"};

    public static void main(String[] args) throws Exception {
        // Quicksort on sorted input recurses n levels deep, so run on a thread with a big stack.
        Thread worker = new Thread(null, Experiment::runAll, "experiment", 1L << 28);
        worker.start();
        worker.join();
    }

    static void runAll() {
        Random rng = new Random(42);
        warmUp(rng);
        try (PrintWriter out = new PrintWriter("data/sorting.csv")) {
            out.println("algorithm,n,input,trial,comparisons,time_ns");
            for (String algorithm : ALGORITHMS) {
                for (String input : INPUTS) {
                    for (int n : sizesFor(algorithm, input)) {
                        for (int trial = 1; trial <= TRIALS; trial++) {
                            int[] arr = makeInput(input, n, rng);
                            Stats stats = new Stats();
                            long start = System.nanoTime();
                            sort(algorithm, arr, stats);
                            long time = System.nanoTime() - start;
                            out.println(algorithm + "," + n + "," + input + "," + trial + ","
                                    + stats.getComparisons() + "," + time);
                        }
                    }
                    System.out.println("done " + algorithm + " / " + input);
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("could not write data/sorting.csv -- run from the repo root", e);
        }
        System.out.println("wrote data/sorting.csv");
    }

    // Insertion sort is O(n^2) on everything but sorted input, and so is quicksort (last-element
    // pivot) on sorted/reversed input, so those only use the small sizes.
    static int[] sizesFor(String algorithm, String input) {
        boolean quadratic = algorithm.equals("insertion")
                || (algorithm.equals("quick") && !input.equals("random"));
        return quadratic ? SMALL : LARGE;
    }

    static int[] makeInput(String input, int n, Random rng) {
        return switch (input) {
            case "random" -> ArrayGen.random(n, rng);
            case "sorted" -> ArrayGen.sorted(n);
            case "reversed" -> ArrayGen.reversed(n);
            default -> throw new IllegalArgumentException("unknown input: " + input);
        };
    }

    static void sort(String algorithm, int[] arr, Stats stats) {
        switch (algorithm) {
            case "insertion" -> InsertionSort.sort(arr, stats);
            case "merge" -> MergeSort.sort(arr, stats);
            case "quick" -> QuickSort.sort(arr, stats);
            case "heap" -> HeapSort.sort(arr, stats);
            default -> throw new IllegalArgumentException("unknown algorithm: " + algorithm);
        }
    }

    // Run everything a few times first so the JIT compiler has optimised the code
    // before we start timing (otherwise the first runs look much slower).
    static void warmUp(Random rng) {
        for (int round = 0; round < 20; round++) {
            for (String algorithm : ALGORITHMS) {
                sort(algorithm, ArrayGen.random(2000, rng), new Stats());
            }
        }
    }
}
