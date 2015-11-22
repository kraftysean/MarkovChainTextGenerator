package com.krafty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Seán Marnane 21/11/15.
 *
 * Contains a collection of prefixes to a list of suffixes, where prefix is a unique String consisting of a pre-defined
 * number of words separated by spaces and suffix is the list of words that follows each prefix.
 *
 */
public class Chain {

    public static final String EOF_MARKER = "\n";  // Marker to signal that the end of the file has been reached.

    private static int ORDER_K = 2;  // The default order to evaluate the Prefix
    private Map<String, List<String>> stateTable;

    public Chain() {
        stateTable = new Hashtable<>();
    }

    public Chain(int orderK) {
        ORDER_K = orderK;
        stateTable = new Hashtable<>();
    }

    public List<String> readTrainingData(String pathToFile) throws IOException {
        List<String> words = new ArrayList<>();
        Path path = Paths.get(pathToFile);
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

            while(true) {
                final String line = reader.readLine();
                if(line == null)
                    break;
                Collections.addAll(words, line.split("\\s+"));
            }
            return words;
        }
        catch (NoSuchFileException e) {
            System.out.println("File not found.");
        }
        return null;
    }


    public void build(List<String> trainingData) throws IOException {
            System.out.println(trainingData.toString());

    }


    public String generate(int maxCount) {
        return null;
    }
}
