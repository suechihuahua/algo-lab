package sorting;

import common.Stats;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        test("sorted",     new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 9);
        test("reverse",    new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, 45);
        test("min last",   new int[]{2, 3, 4, 5, 1}, 7);
        test("empty",      new int[]{}, 0);
        test("single",     new int[]{5}, 0);
        test("duplicates", new int[]{3, 1, 3, 2, 1}, 9);
    }

    static void test(String name, int[] input, long predicted) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        Stats stats = new Stats();
        InsertionSort.sort(input, stats);

        boolean correct = Arrays.equals(input, expected);
        long actual = stats.getComparisons();
        System.out.printf("%-10s correct=%b comparisons=%d predicted=%d %s%n",
                name, correct, actual, predicted,
                actual == predicted ? "OK" : "MISMATCH");
    }
}
