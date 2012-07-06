package algo_class;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

interface PivotSubroutine {
    int getPivotIndex(int[] input, int i, int j);
}

class FirstElement implements PivotSubroutine {
    public int getPivotIndex(int[] input, int i, int j) {
        return i;
    }
}

class FinalElement implements PivotSubroutine {
    public int getPivotIndex(int[] input, int i, int j) {
        return j;
    }
}

class MedianOfThree implements PivotSubroutine {
    static int[] toSort = new int[3];

    public int getPivotIndex(int[] input, int i, int j) {
        final int m = (i + j) / 2;
        return median(input, i, m, j);
    }

    private static int median(int[] input, int i, int m, int j) {
        toSort[0] = input[i];
        toSort[1] = input[m];
        toSort[2] = input[j];
        Arrays.sort(toSort);
        if (toSort[1] == input[i])
            return i;
        if (toSort[1] == input[m])
            return m;
        return j;
    }
}

public class ProgrammingAssignment2 {
    int count;
    PivotSubroutine pivotSubroutine;

    int countComparisons(int[] input, PivotSubroutine pivotSubroutine) {
        this.count = 0;
        this.pivotSubroutine = pivotSubroutine;

        countComparisons(input, 0, input.length - 1);
        return count;
    }

    private void countComparisons(int[] input, int i, int j) {
        if (i >= j)
            return;

        count = count + (j - i);
        final int pivotIndex = pivotSubroutine.getPivotIndex(input, i, j);
        swap(input, i, pivotIndex);
        final int pi = partition(input, i, j);
        countComparisons(input, i, pi - 1);
        countComparisons(input, pi + 1, j);
    }

    private static int partition(int[] input, int l, int r) {
        final int p = input[l];
        int i = l + 1;
        for (int j = l + 1; j <= r; j++) {
            if (input[j] < p) {
                swap(input, i, j);
                i++;
            }
        }
        swap(input, l, i - 1);
        return i - 1;
    }

    private static void swap(int[] input, int i, int j) {
        final int t = input[i];
        input[i] = input[j];
        input[j] = t;
    }

    public static void main(String[] args) throws Exception {
        sanityTest();

        final int[] nos = new int[10000];
        final Scanner scanner = new Scanner(new File("resources/programming_assignment2/QuickSort.txt"));
        for (int i = 0; i < 10000; i++)
            nos[i] = scanner.nextInt();
        System.out.println(new ProgrammingAssignment2().countComparisons(nos, new MedianOfThree()));
    }

    private static void sanityTest() {
        int[] input = new int[] { 4, 8, 9, 4, 10, 1, 3, };
        testPartition(input);
        testCountComparisons(input);

        input = new int[] { 3, 2, 4, 20, 4 };
        testPartition(input);
        testCountComparisons(input);

        input = new int[] { 10, 8, 7, 6, 4 };
        testPartition(input);

        input = new int[] { 10, 8 };
        testPartition(input);
        testCountComparisons(input);
    }

    private static void testCountComparisons(int[] input) {
        System.out.println(new ProgrammingAssignment2().countComparisons(input, new FirstElement()));
        System.out.println(Arrays.toString(input));
    }

    private static void testPartition(int[] input) {
        ProgrammingAssignment2.partition(input, 0, input.length - 1);
        System.out.println(Arrays.toString(input));
    }
}
