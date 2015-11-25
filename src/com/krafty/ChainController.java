package com.krafty;

import java.io.IOException;
import java.util.Map;

/**
 * @author Seán Marnane 22/11/15
 *
 * Simple controller class which handles the communication between Model & View.
 */
public class ChainController {

    private ChainModel model;

    /**
    * Class constructor
    *
    * Creates a new ChainModel object.  Until the app is closed, the buffer will keep storing/retrieving new data.
    */
    public ChainController() {
        model = new ChainModel();
    }

    /**
     * Creates a new View object and makes the window visible on the screen.
     * @param title  Enables a unique title to be set for the application window
     */
    public void startApplication(String title) {
        ChainView view = new ChainView(title);
        view.setVisible(true);
    }

    /**
     * Retrieve the filePath user selected in View, open file, process file contents, return true/false to View.
     * @param filePath  relative path of the file to be processed
     * @return          true/false depending on whether the file could be processed or not
     * @throws IOException
     */
    public int processInputFile(String filePath) throws IOException {
        return model.processInput(filePath);
    }

    /**
     * This is where the magic happens - get input params, build state table and generate the text
     * @param inputParams   a map containing keys ['orderK', 'maxC'] and integer values for these keys
     * @return              String containing the generated text (or empty if none)
     * @throws IOException
     */
    public String generateText(Map<String, Integer> inputParams) throws IOException {
        model.assignInputParams(inputParams);         // Retrieve order-k & max count params from View
        model.buildStateTable();                      // Build the prefix/suffixes table
        return model.generateRandomText();            // Return the newly generated text to the View
    }
}
