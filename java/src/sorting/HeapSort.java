package sorting;

import common.Stats;
import heaps.Heap;
import java.util.Arrays;

public class HeapSort {
    public static void sort(int[] arr, Stats stats) {
        sort(arr, stats, false);
    }

    public static void sort(int[] arr, Stats stats, boolean trace) {
        Heap.buildMaxHeap(arr, stats, trace);
        int heapSize = arr.length;
        for (int end = arr.length - 1; end > 0; end--) {
            Heap.swap(arr, 0, end);
            heapSize--;
            Heap.maxHeapify(arr, 0, heapSize, stats);
            if (trace) {
                System.out.println("extract " + arr[end] + ": heap "
                        + Arrays.toString(Arrays.copyOfRange(arr, 0, heapSize))
                        + " | sorted " + Arrays.toString(Arrays.copyOfRange(arr, heapSize, arr.length)));
            }
        }
    }
}