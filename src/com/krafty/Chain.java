package com.krafty;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Seán Marnane 21/11/15
 *         <p>
 *         Contains a collection of prefixes to a list of suffixes, where prefix is a unique String consisting of a
 *         pre-defined number of words separated by spaces and suffix is the list of words that follows each prefix.
 */
public class Chain {

    /* Marker signals end of list has been reached. This is a good marker to use as
     * the file will be buffered line by line.  Therefore, it can never have "\n";
     */
    public static final String MARKER = "\n";

    private final Map<Prefix, List<String>> stateTable;
    private final List<String> words;

    private static Random randomizer = new Random();  // TEST: Seed set to make results reproducable
    private static int ORDER_K;                           // Default: order to evaluate the Prefix

    public Chain(int orderK) {
        ORDER_K = orderK;
        stateTable = new Hashtable<>();
        words = new ArrayList<>();
    }

    public void readTxtInput(String pathToFile) throws IOException {
        String line = "";

        BufferedReader in = Files.newBufferedReader(Paths.get(pathToFile), StandardCharsets.UTF_8);
        while ((line = in.readLine()) != null) {                   // Read lines until File empty or reached the end
            if (line.isEmpty())                                    // Skip lines with no text
                continue;
            Collections.addAll(words, line.trim().split("\\s+"));  // Remove spaces before adding words to ArrayList
        }
        words.add(MARKER);  // End of file marker appended to ArrayList
    }


    public void build() throws IOException {
        int adjInputSize = words.size() - ORDER_K;  // Adjust input size to account for list of size order-k

        for (int i = 0; i < adjInputSize; i++) {
            Prefix currentPrefix = new Prefix(words, i, ORDER_K);
            if (!stateTable.containsKey(currentPrefix)) {
                stateTable.put(currentPrefix, new ArrayList<>());
            }
            stateTable.get(currentPrefix).add(words.get(i + ORDER_K));
        }
    }


    public String generate(int maxCount) {
        StringBuilder sb = new StringBuilder();

        // Get initial prefix key as starting point
        Prefix key = stateTable.keySet().iterator().next();
        for (int i = 0; i < maxCount; i++) {
            List<String> suffixes = stateTable.get(key);
            String suffix = suffixes.get(randomizer.nextInt(suffixes.size()));

            if (suffix.equals(MARKER))
                break;
            sb.append(suffix).append(" ");

            key.getPrefix().remove(0);
            key.getPrefix().add(suffix);
        }
        return sb.toString().trim();
    }

    public List<String> getWords() {
        return words;
    }

    public Map<Prefix, List<String>> getStateTable() {
        return stateTable;
    }
}
