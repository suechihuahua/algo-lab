package sorting;

import common.Stats;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // insertion sort
        test("sorted",     new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 9);
        test("reverse",    new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, 45);
        test("min last",   new int[]{2, 3, 4, 5, 1}, 7);
        test("empty",      new int[]{}, 0);
        test("single",     new int[]{5}, 0);
        test("duplicates", new int[]{3, 1, 3, 2, 1}, 9);

        System.out.println();

        // merge sort
        testMerge("sorted",     new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 12);
        testMerge("reverse",    new int[]{8, 7, 6, 5, 4, 3, 2, 1}, 12);
        testMerge("interleave", new int[]{1, 5, 3, 7, 2, 6, 4, 8}, 17);
        testMerge("empty",      new int[]{}, 0);
        testMerge("single",     new int[]{5}, 0);
        testMerge("duplicates", new int[]{3, 1, 3, 2, 1}, 7);
        
        System.out.println();

        // quicksort
        testQuick("sorted",     new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 28);
        testQuick("reverse",    new int[]{8, 7, 6, 5, 4, 3, 2, 1}, 28);
        testQuick("all equal",  new int[]{4, 4, 4, 4, 4}, 10);
        testQuick("empty",      new int[]{}, 0);
        testQuick("single",     new int[]{5}, 0);
        testQuick("duplicates", new int[]{3, 1, 3, 2, 1}, 8);
        System.out.println();

        // heapsort
        testHeap("sorted",     new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 27);
        testHeap("reverse",    new int[]{8, 7, 6, 5, 4, 3, 2, 1}, 24);
        testHeap("all equal",  new int[]{4, 4, 4, 4, 4}, 9);
        testHeap("empty",      new int[]{}, 0);
        testHeap("single",     new int[]{5}, 0);
        testHeap("duplicates", new int[]{3, 1, 3, 2, 1}, 9);
    }

    static void test(String name, int[] input, long predicted) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        Stats stats = new Stats();
        InsertionSort.sort(input, stats);

        boolean correct = Arrays.equals(input, expected);
        long actual = stats.getComparisons();
        System.out.printf("insert %-10s correct=%b comparisons=%d predicted=%d %s%n",
                name, correct, actual, predicted,
                actual == predicted ? "OK" : "MISMATCH");
    }

    static void testMerge(String name, int[] input, long predicted) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        Stats stats = new Stats();
        MergeSort.sort(input, stats);

        boolean correct = Arrays.equals(input, expected);
        long actual = stats.getComparisons();
        System.out.printf("merge  %-10s correct=%b comparisons=%d predicted=%d %s%n",
                name, correct, actual, predicted,
                actual == predicted ? "OK" : "MISMATCH");
    }
        static void testQuick(String name, int[] input, long predicted) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        Stats stats = new Stats();
        QuickSort.sort(input, stats);

        boolean correct = Arrays.equals(input, expected);
        long actual = stats.getComparisons();
        System.out.printf("quick  %-10s correct=%b comparisons=%d predicted=%d %s%n",
                name, correct, actual, predicted,
                actual == predicted ? "OK" : "MISMATCH");
    }
        static void testHeap(String name, int[] input, long predicted) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        Stats stats = new Stats();
        HeapSort.sort(input, stats);

        boolean correct = Arrays.equals(input, expected);
        long actual = stats.getComparisons();
        System.out.printf("heap   %-10s correct=%b comparisons=%d predicted=%d %s%n",
                name, correct, actual, predicted,
                actual == predicted ? "OK" : "MISMATCH");
    }
    
}