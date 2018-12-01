import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Created by Maciek Niechaj on 10.10.17.
 */
public class AppClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Program should be executed with 2 parameters!");
        }
        else {
            String filePath = args[0];
            String testString = args[1];
            AppClient appClient = new AppClient();
            try {
                Stream<String> entries = Files.lines(Paths.get(filePath));
                int lineNumber = appClient.findMostSimilarLine(entries, testString, new Levenshtein());
                if (lineNumber != -1)
                    System.out.println("Linia : " + lineNumber);
                else
                    System.out.println("No lines to compare provided!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param s string to be organised
     * @return  organised string; without leading and heading whitespaces,
     *          with gaps between words in string set to one whitespace
     *          with words in string sorted alphabetically
     */
    private String organizeSentence(String s) {
        String trimmed = s.replaceAll("\\s+", " ").trim();
        String[] parts = trimmed.split("\\s");
        Arrays.sort(parts);
        return Arrays.toString(parts).replaceAll("\\[|\\]|,", "");
    }

    /**
     *
     * @param original  original word that is compared with other entries by application
     * @param entry     word that is being compared with original one
     * @param levenshtein   implementation of Levenshtein's algorithm
     * @return          integer value of levenshtein's algorithm distance
     */
    private int compareByWordCount(String original, String entry, Levenshtein levenshtein) {
        String[] split1 = original.split("\\s");
        String[] split2 = entry.split("\\s");
        int distance;

        if (split1.length == split2.length) {
            distance = levenshtein.countDistance(original, entry);
        } else {
            String longer;
            String shorter;
            if (split1.length > split2.length) {
                longer = original;
                shorter = entry;
            } else {
                longer = entry;
                shorter = entry;
            }
            String[] split = longer.split("\\s");
            String expectedSearchInput = split[0] + " " + split[2];
            distance = levenshtein.countDistance(expectedSearchInput, shorter) + split[1].length() + 1;
        }

        return distance;
    }

    /**
     *
     * @param entries   Stream of lines to be compared with testSentence to find most similar one
     * @param testSentence  string that method is looking most similar to in entries stream
     * @param levenshtein   implementation of Levenshtein's algorithm
     * @return  integer index of entries element, that was most similar to testSentence
     */
    private int findMostSimilarLine(Stream<String> entries, String testSentence, Levenshtein levenshtein) {
        int distance = Integer.MAX_VALUE;
        int closestLineNumber = -1;
        String organizedTestString = organizeSentence(testSentence);
        String closestSentence = null;

        Iterator<String> iterator = entries.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            ++index;
            String entry = iterator.next();
            String organizedEntry = organizeSentence(entry);
            int dist = compareByWordCount(organizedTestString, organizedEntry, levenshtein);

            if (dist < distance) {
                distance = dist;
                closestLineNumber = index;
                closestSentence = entry;
            } else if (dist == distance) {
                // Stops algorithm if sentences are equals from algorithm point of view
                // Working together with Streams can possibly significantly shorten computations and memory usage
                if (testSentence.length() - entry.length() == 0) return index;
                if (Math.abs(testSentence.length() - entry.length()) - Math.abs(testSentence.length() - closestSentence.length()) < 0) {
                    closestSentence = entry;
                    closestLineNumber = index;
                }
            }
        }
        return  closestLineNumber;
    }

}
