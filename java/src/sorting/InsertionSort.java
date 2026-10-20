package sorting;

import common.Stats;
import java.util.Arrays;

public class InsertionSort {
    public static void sort(int[] arr, Stats stats) {
        sort(arr, stats, false);
    }

    public static void sort(int[] arr, Stats stats, boolean trace) {
        if (trace) {
            System.out.println("original: " + Arrays.toString(arr));
        }
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            int passComparisons = 0;
            while (j >= 0) {
                stats.incrementComparisons();
                passComparisons++;
                if (arr[j] <= key) {
                    break;
                }
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
            if (trace) {
                System.out.println("pass " + i + ":   " + Arrays.toString(arr)
                        + "  (key " + key + ", " + passComparisons + " comparisons)");
            }
        }
    }
}
