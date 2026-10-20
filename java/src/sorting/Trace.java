package sorting;

import common.ArrayGen;
import common.Stats;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Trace {
    // To add an algorithm: add its name here AND a case in run() below.
    static final String[] ALGORITHMS = {"insertion", "merge"};

        public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            List<String> chosen = askAlgorithms(in);
            if (chosen == null) {
                System.out.println("Sayonara!");
                return;
            }
            int[] arr = askArray(in);

            for (String name : chosen) {
                System.out.println();
                System.out.println("=== " + name + " ===");
                int[] copy = arr.clone();
                Stats stats = new Stats();
                run(name, copy, stats);
                System.out.println("total comparisons: " + stats.getComparisons());
            }
            System.out.println();
        }
    }

    static List<String> askAlgorithms(Scanner in) {
        System.out.println("Algorithms:");
        for (int i = 0; i < ALGORITHMS.length; i++) {
            System.out.println("  " + (i + 1) + ") " + ALGORITHMS[i]);
        }
        while (true) {
            System.out.print("Choose one or more (e.g. 1 2), or 'all', or 'q' to quit: ");
            String line = in.nextLine().trim();
            if (line.equalsIgnoreCase("q")) {
                return null;
            }
            List<String> chosen = new ArrayList<>();
            if (line.equalsIgnoreCase("all")) {
                chosen.addAll(List.of(ALGORITHMS));
                return chosen;
            }
            try {
                for (String part : line.split("[\\s,]+")) {
                    int number = Integer.parseInt(part);
                    chosen.add(ALGORITHMS[number - 1]);
                }
                return chosen;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Please enter numbers from 1 to " + ALGORITHMS.length + ".");
            }
        }
    }

    static int[] askArray(Scanner in) {
        while (true) {
            System.out.print("Input: (t)ype your own array or (r)andom? ");
            String mode = in.nextLine().trim().toLowerCase();

            if (mode.startsWith("t")) {
                System.out.print("Numbers separated by spaces: ");
                String[] parts = in.nextLine().trim().split("[\\s,]+");
                try {
                    int[] arr = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        arr[i] = Integer.parseInt(parts[i]);
                    }
                    return arr;
                } catch (NumberFormatException e) {
                    System.out.println("Only whole numbers, please.");
                }
            } else if (mode.startsWith("r")) {
                System.out.print("Seed (press Enter for a new random one): ");
                String text = in.nextLine().trim();
                long seed = text.isEmpty() ? System.currentTimeMillis() : Long.parseLong(text);

                Random rng = new Random(seed);
                int n = 2 + rng.nextInt(7);
                int[] arr = ArrayGen.randomValues(n, 99, rng);
                System.out.println("seed " + seed + ", n = " + n);
                return arr;
            }
        }
    }

    static void run(String name, int[] arr, Stats stats) {
        switch (name) {
            case "insertion" -> InsertionSort.sort(arr, stats, true);
            case "merge" -> MergeSort.sort(arr, stats, true);
            default -> throw new IllegalArgumentException("unknown algorithm: " + name);
        }
    }
}