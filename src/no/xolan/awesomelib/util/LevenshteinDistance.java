package no.xolan.awesomelib.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author: Jonas Svarvaa Date: 05.05.12 Time: 01:40
 */
public class LevenshteinDistance {

    public static int compare(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                dp[i][j] = i == 0 ? j : j == 0 ? i : 0;
                if (i > 0 && j > 0) {
                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = Math.min(dp[i][j - 1] + 1, Math.min(
                                dp[i - 1][j - 1] + 1, dp[i - 1][j] + 1));
                    }
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    public static List<String> compare(String match, List<String> list) {
        LevenshteinDistanceComparator comp = new LevenshteinDistanceComparator(
                match);
        Collections.sort(list, comp);
        return list;
    }

    private static class LevenshteinDistanceComparator implements Comparator {
        final String match;

        public LevenshteinDistanceComparator(String match) {
            this.match = match;
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (LevenshteinDistance.compare(match, (String) o1) < LevenshteinDistance
                    .compare(match, (String) o2)) {
                return -1;
            } else if (LevenshteinDistance.compare(match, (String) o1) < LevenshteinDistance
                    .compare(match, (String) o2)) {
                return 0;
            } else {
                return 1;
            }
        }
    }

}
