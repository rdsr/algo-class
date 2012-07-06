package algo_class;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The file contains all the 100,000 integers between 1 and 100,000 (including both) in some random order( no integer is
 * repeated).
 * Your task is to find the number of inversions in the file given (every row has a single integer between 1 and
 * 100,000). Assume your array is from 1 to 100,000 and ith row of the file gives you the ith entry of the array.
 * Write a program and run over the file given. The numeric answer should be written in the space.
 * So if your answer is 1198233847, then just type 1198233847 in the space provided without any space / commas / any
 * other punctuation marks. You can make upto 5 attempts, and we'll count the best one for grading.
 * 
 * @author rdsr
 */
public class ProgrammingAssignment1 {
    public static long countInversions(int[] nos) {
        return countInversions(nos, 0, nos.length - 1);
    }

    private static long countInversions(int[] nos, int i, int j) {
        if (i >= j)
            return 0;

        if (j - i == 1) {
            if (nos[i] > nos[j]) {
                final int no = nos[i];
                nos[i] = nos[j];
                nos[j] = no;
                return 1;
            }
            return 0;
        }

        final int m = (i + j) / 2;
        final long inversionsOnleft = countInversions(nos, i, m);
        final long inversionsOnRight = countInversions(nos, m + 1, j);
        final int spannedInversions = countInversions(nos, i, m, j);
        return inversionsOnleft + inversionsOnRight + spannedInversions;
    }

    private static int countInversions(int[] nos, int leftIndex, int middleIndex, int rightIndex) {
        int count = 0;
        int i = leftIndex, j = middleIndex + 1;
        while (i <= middleIndex && j <= rightIndex) {
            if (nos[i] > nos[j]) {
                count += middleIndex - i + 1;
                j += 1;
            } else {
                i += 1;
            }
        }
        merge(nos, leftIndex, middleIndex, rightIndex);
        return count;
    }

    private static void merge(int[] nos, int leftIndex, int middleIndex, int rightIndex) {
        final int[] copy = new int[middleIndex - leftIndex + 1];
        for (int l = 0, i = leftIndex; i <= middleIndex; i++, l++)
            copy[l] = nos[i];

        final int i = leftIndex;
        int j = middleIndex + 1, k = leftIndex, l = 0;
        while (i + l <= middleIndex && j <= rightIndex) {
            if (copy[l] > nos[j]) {
                nos[k] = nos[j];
                j += 1;
            } else {
                nos[k] = copy[l];
                l += 1;
            }
            k += 1;
        }

        for (; i + l <= middleIndex; l++, k++)
            nos[k] = copy[l];
        for (; j <= rightIndex; j++, k++)
            nos[k] = nos[j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        sanityTest();

        final int[] nos = new int[100000];
        final Scanner scanner = new Scanner(new File("resources/programming_assignment1/IntegerArray.txt"));
        for (int i = 0; scanner.hasNextInt(); i++)
            nos[i] = scanner.nextInt();
        System.out.println(ProgrammingAssignment1.countInversions(nos));
    }

    private static void sanityTest() {
        System.out.println(ProgrammingAssignment1.countInversions(new int[] { 0, 1, 1, 1, 5, 2 }));
        System.out.println(ProgrammingAssignment1.countInversions(new int[] { 3, 1, 1, 1, 2 }));
        System.out.println(ProgrammingAssignment1.countInversions(new int[] { 2, 1, 4, 3, 0 }));
        System.out.println(ProgrammingAssignment1.countInversions(new int[] { 4, 3, 2, 1, 0 }));
        System.out.println(ProgrammingAssignment1.countInversions(new int[] { 2, 1, 4, 3 }));
        System.out.println(ProgrammingAssignment1.countInversions(new int[] { 4, 3, 2, 1 }));
        System.out.println(ProgrammingAssignment1.countInversions(new int[] { 1, 2, 3, 4 }));
        System.out.println(ProgrammingAssignment1.countInversions(new int[] {}));
    }
}
