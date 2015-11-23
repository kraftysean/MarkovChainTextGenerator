package com.krafty;

import java.io.IOException;

/**
 * @author  Seán Marnane 20/11/15
 *
 * @apiNote  "Use any existing text to construct a statistical model of the language as used in that text, and from
 * that generate random text that has similar statistics to the original."  This is the main class which starts the
 * GUI application.
 */

public class MarkovChainApp {

    public static void main(String[] args) throws IOException {

        ChainController controller = new ChainController();
        controller.startApplication();
    }
}
