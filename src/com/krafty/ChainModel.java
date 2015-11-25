package com.krafty;

import com.krafty.util.Randomizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Seán Marnane 21/11/15
 *
 * An implementation of the Markov Chain algorithm to generate random text, given a text file as input.  Input file
 * must be ASCII or Unicode (supports UTF-8) encoded or an <tt>IOException</tt> will be thrown.
 *
 * ChainModel consists of a collection of prefixes, each containing a list of suffixes.  Each prefix must be unique
 * and will contain a pre-defined number of words (order-K).  Each prefix must have one or more suffixes.  To handle
 * end-of-file, final prefix will contain a MARKER value signifying the end of input.
 */
public class ChainModel {

    public static final String MARKER = "\n";               // Used for non-words at start and end of input processing
    private static final long SEED = System.currentTimeMillis();

    private final Map<ChainPrefix, List<String>> chain;     // Map to store prefix and it's suffixes
    private final List<String> words;                       // List of words (parsed text file input)

    private Randomizer randomizer;                          // Utility class to get a random number
    private static int orderK = 2;                          // Default: order-k count to evaluate the ChainPrefix
    private static int maxCount = 1000;                     // Default: maxCount
    private static int counter;                             // TBD - Considering using static counter to restrict GUI calls

    /**
    * Class Constructor
    *
    */
    public ChainModel() {
        chain = new Hashtable<>();
        words = new ArrayList<>();
        counter = 0;
    }

    /**
     * Get filepath from GUI, open and copy text in to List
     * @param pathToFile  the relative location of the file on the users machine
     * @return  if input is processed will return true, otherwise false.
     * @throws IOException  File Not Found, File locked, Character Encoding not valid etc.
     */
    public int processInput(String pathToFile) {
        String line = null;
        Pattern splitSpace = Pattern.compile("\\s+");

        try (BufferedReader in = Files.newBufferedReader(Paths.get(pathToFile), StandardCharsets.UTF_8)) {
            while ((line = in.readLine()) != null) {                   // Read lines until file is empty or reached the end
                if (line.isEmpty())                                    // Skip lines with no text
                    continue;
                buildWordList(line, splitSpace);
            }
            words.add(MARKER);                                          // End of file marker appended to ArrayList
            return words.size() - 1;
        } catch (IOException e) {
            System.err.println(e);
            return -1;
        }
    }

    private void buildWordList(String line, Pattern splitSpace) {
        Collections.addAll(words, splitSpace.split(line, 0));       // Remove spaces before adding words to ArrayList
        // ALTERNATIVELY: words.addAll(Arrays.asList(splitSpace.split(line, 0)));
    }

    /**
     * Assigns the order-k num (number of prefix words) and max count (total number of words to return)
     * @param params
     */
    public void assignInputParams(Map<String, Integer> params) {
        orderK = params.get("orderK");
        maxCount = params.get("maxC");
    }

    /**
     * Build prefix/suffixes state table, given a collection of words.
     * @throws IOException
     */
    public void buildStateTable() throws IOException {
        for (int i = 0; i < words.size(); i++) {
            String suffix = words.get(i);

            ChainPrefix key = new ChainPrefix(words, i, orderK, MARKER);
            if (!chain.containsKey(key)) {
                chain.put(key, new ArrayList<>());
            }
            chain.get(key).add(suffix);
        }
    }

    /**
     * Generate the random text.  This uses the Markov Chain algorithm.
     *
     * Set first key (ChainPrefix) in map as the starting point.  Begin looping through the chain state table.
     * Randomly choose one of the suffixes from the initial prefix. Append this word to the String. Remove first
     * word in ChainPrefix and append the suffix as the last word in the ChainPrefix.  This generates the next
     * key when the loop iterates.  The loop will exit when one of two conditions is met:
     *      1. The maxCount is reached
     *      2. The suffix is the MARKER ('\n').
     * Finally, output the random text.
     * @return  returns the output i.e. the randomly generated text.
     */
    public String generateRandomText() {
        StringBuilder sb = new StringBuilder();
        randomizer = new Randomizer(SEED);

        ChainPrefix key = getChainPrefix();
        for(int i = 0; i < maxCount; i++) {
            List<String> suffixes = chain.get(key);
            String suffix = suffixes.get(randomizer.randomGenerator(suffixes.size()));

            if (suffix.equals(MARKER))
                break;
            sb.append(suffix).append(" ");

            key.getPrefixList().remove(0);
            key.getPrefixList().add(suffix);
        }
        return sb.toString().trim();
    }

    /**
     * Choose an initial key/prefix which will be the starting point for the random text generator.  Subsequent keys
     * will be iterated from this
     * @return prefix key
     */
    private ChainPrefix getChainPrefix() {
        List<ChainPrefix> keys = new ArrayList<>(chain.keySet());
        return keys.get(randomizer.randomGenerator(keys.size()));
    }

    /**
     * Getter for 'words' list
     * @return  list of individual words processed from text file
     */
    public List<String> getWords() {
        return words;
    }

    /**
     * Getter for chain state table
     * @return  map of prefix's (each containing a list of suffixes)
     */
    public Map<ChainPrefix, List<String>> getChain() {
        return chain;
    }

    /**
     * Getter for number of prefixes of order-k
     * @return number of prefix words
     */
    public static int getOrderK() {
        return orderK;
    }

    /**
     * Getter for max count of size N.
     * @return max number of words to return
     */
    public static int getMaxCount() {
        return maxCount;
    }
}