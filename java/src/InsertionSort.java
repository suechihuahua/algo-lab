public class InsertionSort {
    public static void sort(int[] arr, Stats stats) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0) {
                stats.incrementComparisons();
                if (arr[j] <= key) {
                    break;
                }
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
}
