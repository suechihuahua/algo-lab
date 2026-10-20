package sorting;

import common.ArrayGen;
import common.Stats;
import java.util.Random;

public class Trace {
    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : System.currentTimeMillis();
        Random rng = new Random(seed);

        int n = 2 + rng.nextInt(7);
        int[] arr = ArrayGen.randomValues(n, 99, rng);

        System.out.println("seed " + seed + ", n = " + n);
        Stats stats = new Stats();
        InsertionSort.sort(arr, stats, true);
        System.out.println("total comparisons: " + stats.getComparisons());
    }
}