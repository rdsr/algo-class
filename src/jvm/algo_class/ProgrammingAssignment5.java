package algo_class;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * * The file contains 100,000 integers all randomly chosen between 1
 * and 1,000,000 (there might be some repetitions).This is your array of
 * integers: the ith row of the file gives you the ith entry of the
 * array.
 * Here are 9 "target sums", in increasing order:
 * 231552,234756,596873,648219,726312,981237,988331,1277361,1283379. Your
 * task is to implement the hash table-based algorithm explained in the
 * video lectures and determine, for each of the 9 target sums x, whether
 * or not x can be formed as the sum of two entries in the given array.
 * Your answer should be in the form of a 9-bit string, with a 1
 * indicating "yes" for the corresponding target sum and 0 indicating
 * "no". For example, if you discover that all of the target sums except
 * for the 5th and the 7th one (i.e., except for 726312 and 988331) can
 * be formed from pairs from the input file, then your answer should be
 * "111101011" (without the quotes). We reiterate that the answer should
 * be in the same order as the target sums listed above (i.e., in
 * increasing order of the target).
 * 
 * @author rdsr
 */

public class ProgrammingAssignment5 {

    private static boolean solve(int x, Set<Integer> nos) {
        for (int i = 0; i <= x / 2 + 1; i++) {
            if (nos.contains(i) && nos.contains(x - i))
                return true;
        }
        return false;
    }

    public static String solve(String src) throws FileNotFoundException {
        final Scanner scanner = new Scanner(new File(src));
        final Set<Integer> nos = new HashSet<Integer>();

        while (scanner.hasNextLine())
            nos.add(Integer.parseInt(scanner.nextLine().trim()));

        final int[] targetSums = new int[] { 231552, 234756, 596873, 648219, 726312, 981237, 988331, 1277361, 1283379 };
        final StringBuilder result = new StringBuilder();
        for (final int x : targetSums) {
            if (solve(x, nos))
                result.append("1");
            else
                result.append("0");
        }
        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(solve("resources/programming_assignment5/HashInt.txt"));
    }
}
