package util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA. User: Jonas Date: 22.05.12 Time: 08:17 To change
 * this template use File | Settings | File Templates.
 */
public class LevenshteinDistanceTest {

    @Test
    public void LevenshteinDistanceTest() {
        ArrayList<String> strings = new ArrayList<>();

        strings.add("Jonas");
        strings.add("Anders");
        strings.add("Chuppa");
        strings.add("Gjemblen");
        strings.add("Svarvaa");

        Assert.assertFalse(strings.isEmpty());
        Assert.assertTrue(strings.contains("Jonas"));
        Assert.assertTrue(strings.contains("Anders"));
        Assert.assertTrue(strings.contains("Chuppa"));
        Assert.assertTrue(strings.contains("Gjemblen"));
        Assert.assertTrue(strings.contains("Svarvaa"));

        for (String s : LevenshteinDistance.compare("Svarvaa", strings)) {
            Assert.assertTrue(strings.get(0).equals("Svarvaa"));
            Assert.assertTrue(strings.get(1).equals("Jonas"));
            Assert.assertTrue(strings.get(2).equals("Chuppa"));
            Assert.assertTrue(strings.get(3).equals("Anders"));
            Assert.assertTrue(strings.get(4).equals("Gjemblen"));
        }

        for (String s : LevenshteinDistance.compare("Gjemblen", strings)) {
            Assert.assertTrue(strings.get(0).equals("Gjemblen"));
            Assert.assertTrue(strings.get(1).equals("Svarvaa"));
            Assert.assertTrue(strings.get(2).equals("Jonas"));
            Assert.assertTrue(strings.get(3).equals("Chuppa"));
            Assert.assertTrue(strings.get(4).equals("Anders"));
        }

        for (String s : LevenshteinDistance.compare("ppa", strings)) {
            Assert.assertTrue(strings.get(0).equals("Chuppa"));
            Assert.assertTrue(strings.get(1).equals("Jonas"));
            Assert.assertTrue(strings.get(2).equals("Svarvaa"));
            Assert.assertTrue(strings.get(3).equals("Anders"));
            Assert.assertTrue(strings.get(4).equals("Gjemblen"));
        }

    }

}
