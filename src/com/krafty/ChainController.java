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

    /*
    * Class constructor
    *
    * Creates a new ChainModel object.  Until the app is closed, the buffer will keep storing/retrieving new data.
    */
    public ChainController() {
        model = new ChainModel();
    }

    // Start method (run by MarkovChainApp) - creates a new View object and ensures the window is visible.
    public void startApplication() {
        ChainView view = new ChainView();
        view.setVisible(true);
    }

    // Retrieve the filePath from View, open file and process the input.
    public boolean processInputFile(String filePath) throws IOException {
        return model.processInput(filePath);
    }

    // This is where the magic happens - get input params, build state table and generate the text
    public String generateText(Map<String, Integer> inputParams) throws IOException {
        model.allocateInputParams(inputParams);  // Retrieve order-k & max count params from View
        model.build();                          // Build the prefix/suffixes table
        return model.generateRandomText();                // Return the newly generated text to the View
    }
}
