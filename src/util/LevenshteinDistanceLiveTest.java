package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LevenshteinDistanceLiveTest {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        ArrayList<String> strings = new ArrayList<>();
        System.out
                .println("Type /q at any time to quit, or /n to move on the next step or /b to move backwards.");
        System.out.println("==== Step 1 ====");
        System.out.println("Insert strings into the list by entering one now.");

        String in;

        while (true) {
            System.out.println("List\t: " + strings);
            in = br.readLine();
            if (in.equals("/q") || in.equals("/b")) {
                System.exit(0);
            } else if (in.equals("/n")) {
                System.out.println("==== Step 2 ====");
                System.out.println("Now enter a search.");
                while (true) {
                    System.out.println("List\t: " + strings);
                    in = br.readLine();
                    if (in.equals("/q")) {
                        System.exit(0);
                    } else if (in.equals("/b")) {
                        break;
                    } else {
                        System.out.println("Result\t: "
                                + LevenshteinDistance.compare(in, strings));
                        System.out.println("================");
                    }
                }
            } else {
                strings.add(in);
            }
        }
    }

}
