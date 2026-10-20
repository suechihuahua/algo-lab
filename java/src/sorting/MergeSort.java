package sorting;

import common.Stats;
import java.util.Arrays;

public class MergeSort {
    public static void sort(int[] arr, Stats stats) {
        sort(arr, stats, false);
    }

    public static void sort(int[] arr, Stats stats, boolean trace) {
        if (trace) {
            System.out.println("original: " + Arrays.toString(arr));
        }
        mergeSort(arr, 0, arr.length - 1, stats, trace);
    }

    private static void mergeSort(int[] arr, int lo, int hi, Stats stats, boolean trace) {
        if (lo >= hi) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        mergeSort(arr, lo , mid, stats, trace);
        mergeSort(arr, mid + 1, hi, stats, trace);
        merge(arr, lo, mid, hi, stats, trace);
    }

    private static void merge(int[] arr, int lo, int mid, int hi, Stats stats, boolean trace) {
        int[] left = Arrays.copyOfRange(arr, lo, mid + 1);
        int[] right = Arrays.copyOfRange(arr, mid + 1, hi + 1);

        int i = 0;
        int j = 0;
        int k = lo;

        while (i < left.length && j < right.length) {
            stats.incrementComparisons();
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
                
            } else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }
        while (i < left.length) {
            arr[k] = left[i];
            k++; 
            i++;
        }
        while (j < right.length) {
            arr[k] = right[j];
            k++;
            j++;
        }

        if (trace) {
            System.out.println("merge [" + lo + ".." + hi + "]: " + Arrays.toString(left)
                    + " + " + Arrays.toString(right)
                    + " -> " + Arrays.toString(Arrays.copyOfRange(arr, lo, hi + 1)));
        }
    }
}