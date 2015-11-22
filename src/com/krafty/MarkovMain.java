package com.krafty;

import java.io.IOException;

/**
 * @author  Seán Marnane 20/11/15
 *
 * @apiNote  "Use any existing text to construct a statistical model of the language as used in that text, and from
 * that generate random text that has similar statistics to the original."  This is the main class which reads the
 * input from console. builds the prefix/suffix data structure and generates the random output.
 */

public class MarkovMain {

    private static int MAX_COUNT = 100;  // Default limit for number of words to output

    public static void main(String[] args) throws IOException {
        System.out.println("MARKOV CHAIN TEXT GENERATOR\n===========================\n");

        // Initialise Chain - assuming default order = 2, max = 100
        Chain chain = new Chain(2);

        // Prepare training data
        chain.readTxtInput("alice.txt");

        // Build the prefix/suffixes table
        chain.build();

        // Generate the new text (limit of MAX_COUNT words).
        String output = chain.generate(MAX_COUNT);

        System.out.println(output);

//        ConsoleReader.readInputParams();  // Helper: Enables the setting of prefix and max counts
//        ConsoleReader.listTextFilesOnPath(); // Helper: To list txt files currently on the path
//        List<String> fileData = ConsoleReader.readFileFromConsole(); // returns contents of FILE_NAME.txt
//        String output = chain.generate(MAXCOUNT);
//        System.out.println(output);
    }
}
