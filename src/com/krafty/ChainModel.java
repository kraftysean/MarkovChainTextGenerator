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
public class ChainModel {

    public static final String MARKER = "\n";

    private final Map<ChainPrefix, List<String>> stateTable;
    private final List<String> words;

    private static Random randomizer = new Random();
    private static int orderK = 2;                           // Default: order-k count to evaluate the ChainPrefix
    private static int maxCount = 1000;                      // Default: maxCount
    private static int counter;                              // TBD - Considering using static counter to restrict GUI calls

    /*
    * Class Constructor
    *
    */
    public ChainModel() {
        stateTable = new Hashtable<>();
        words = new ArrayList<>();
        counter = 0;
    }

    // Get filepath from GUI, open and copy text in to List
    public boolean processInput(String pathToFile) throws IOException {
        String line;

        BufferedReader in = Files.newBufferedReader(Paths.get(pathToFile), StandardCharsets.UTF_8);
        while ((line = in.readLine()) != null) {                   // Read lines until File empty or reached the end
            if (line.isEmpty())                                    // Skip lines with no text
                continue;
            Collections.addAll(words, line.trim().split("\\s+"));  // Remove spaces before adding words to ArrayList
        }
        words.add(MARKER);  // End of file marker appended to ArrayList
        return true;
    }

    // Set the order-k num and max count
    public void allocateInputParams(Map<String, Integer> params) {
        orderK = params.get("orderK");
        maxCount = params.get("maxC");
    }

    // Build the State table
    public void build() throws IOException {
        for (int i = 0; i < words.size(); i++) {
            String suffix = words.get(i);

            ChainPrefix key = new ChainPrefix(words, i, orderK, MARKER);
            if (!stateTable.containsKey(key)) {
                stateTable.put(key, new ArrayList<>());
            }
            stateTable.get(key).add(suffix);
        }
    }

    // Generate the text (using a counter to prevent the GUI from re-running)
    public String generateRandomText() {
        StringBuilder sb = new StringBuilder();

        // Get initial prefix key as starting point
        ChainPrefix key = stateTable.keySet().iterator().next();
        for(int i = 0; i < maxCount; i++) {
            List<String> suffixes = stateTable.get(key);
            String suffix = suffixes.get(randomizer.nextInt(suffixes.size()));

            if (suffix.equals(MARKER))
                break;
            sb.append(suffix).append(" ");

            key.getcPref().remove(0);
            key.getcPref().add(suffix);
        }
        return sb.toString().trim();
    }

    // Getter for 'words' list
    public List<String> getWords() {
        return words;
    }

    // Getter for 'state table'
    public Map<ChainPrefix, List<String>> getStateTable() {
        return stateTable;
    }
}
