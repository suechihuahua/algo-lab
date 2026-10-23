package sorting;

import common.Stats;
import java.util.Arrays;

// Lomuto partition (lecture Strategy 1), last element as pivot.
public class QuickSort {
    public static void sort(int[] arr, Stats stats) {
        sort(arr, stats, false);
    }

    public static void sort(int[] arr, Stats stats, boolean trace) {
        if (trace) {
            System.out.println("original: " + Arrays.toString(arr));
        }
        quickSort(arr, 0, arr.length - 1, stats, trace);
    }

    private static void quickSort(int[] arr, int lo, int hi, Stats stats, boolean trace) {
        if (lo < hi) {
            int mid = partition(arr, lo, hi, stats, trace);
            quickSort(arr, lo, mid - 1, stats, trace);
            quickSort(arr, mid + 1, hi, stats, trace);
        }
    }

    private static int partition(int[] arr, int lo, int hi, Stats stats, boolean trace) {
        int p = arr[hi];
        int j = lo;
        for (int i = lo; i < hi; i++) {
            stats.incrementComparisons();
            if (arr[i] < p) {
                swap(arr, i, j);
                j++;
            }
        }
        swap(arr, j, hi);

        if (trace) {
            System.out.println("partition [" + lo + ".." + hi + "] pivot " + p + " -> "
                    + Arrays.toString(arr) + "  (pivot now at index " + j + ")");
        }
        return j;
    }

    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}