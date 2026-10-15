import java.util.Random;

public class ArrayGen {
    public static int[] random(int n, Random rng) {
        int[] a = sorted(n);
        for (int i = n - 1; i > 0; i--) {
            //walks backwards from the last index down to 1.
            int r = rng.nextInt(i + 1);
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
        return a;
        //Fisher–Yates shuffle
    }

    public static int[] sorted(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        return a;
    }

    public static int[] reversed(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = n - 1 - i;
        }
        return a;
    }
}