package heaps;

import common.Stats;
import java.util.Arrays;

// Max-heap stored in an array, 0-based: children of i are 2i+1 and 2i+2.
// Only a[0 .. heapSize-1] is the heap; anything after that is ignored.
public class Heap {
    public static int parent(int i) { return (i - 1) / 2; }
    public static int left(int i)   { return 2 * i + 1; }
    public static int right(int i)  { return 2 * i + 2; }

    public static void maxHeapify(int[] a, int i, int heapSize, Stats stats) {
        int l = left(i);
        int r = right(i);
        int largest = i;
        if (l < heapSize) {
            stats.incrementComparisons();
            if (a[l] > a[largest]) {
                largest = l;
            }
        }
        if (r < heapSize) {
            stats.incrementComparisons();
            if (a[r] > a[largest]) {
                largest = r;
            }
        }
        if (largest != i) {
            swap(a, i, largest);
            maxHeapify(a, largest, heapSize, stats);
        }
    }

    public static void buildMaxHeap(int[] a, Stats stats, boolean trace) {
        if (trace) {
            System.out.println("original: " + Arrays.toString(a));
        }
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            maxHeapify(a, i, a.length, stats);
            if (trace) {
                System.out.println("heapify(" + i + "): " + Arrays.toString(a));
            }
        }
    }
    // Adds key at position heapSize and sifts it up. Returns the new heap size.
    // The array must have room: heapSize < a.length.
    public static int insert(int[] a, int heapSize, int key, Stats stats) {
        int i = heapSize;
        a[i] = key;
        while (i > 0) {
            stats.incrementComparisons();
            if (a[parent(i)] >= a[i]) {
                break;
            }
            swap(a, i, parent(i));
            i = parent(i);
        }
        return heapSize + 1;
    }

    // Removes and returns the maximum. Afterwards the heap has heapSize - 1 elements.
    public static int extractMax(int[] a, int heapSize, Stats stats) {
        int max = a[0];
        a[0] = a[heapSize - 1];
        maxHeapify(a, 0, heapSize - 1, stats);
        return max;
    }

    public static void swap(int[] a, int x, int y) {
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }
}