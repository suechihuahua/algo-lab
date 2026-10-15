import java.io.PrintWriter;
import java.util.Random;

public class Experiment {
    static final int[] SIZES = {1000, 2000, 4000, 8000, 16000};
    static final int TRIALS = 5;

    public static void main(String[] args) throws Exception {
        Random rng = new Random(42);

        try (PrintWriter out = new PrintWriter("data/insertion.csv")) {
            out.println("n,input,trial,comparisons,time_ns");

            for (int n : SIZES) {
                for (int trial = 1; trial <= TRIALS; trial++) {
                    run(out, n, "random", trial, ArrayGen.random(n, rng));
                }
                run(out, n, "sorted", 1, ArrayGen.sorted(n));
                run(out, n, "reversed", 1, ArrayGen.reversed(n));
                System.out.println("done n = " + n);
            }
        }
    }

    static void run(PrintWriter out, int n, String input, int trial, int[] arr) {
        Stats stats = new Stats();
        long start = System.nanoTime();
        InsertionSort.sort(arr, stats);
        long time = System.nanoTime() - start;
        out.println(n + "," + input + "," + trial + "," + stats.getComparisons() + "," + time);
    }
}