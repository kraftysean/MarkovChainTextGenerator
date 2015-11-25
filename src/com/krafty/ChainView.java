package com.krafty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Seán Marnane 22/11/15
 *
 * This is the main GUI implementation for the Markov Chain algorithm Text Generator.
 * It allows input parameters to be set, new text to be generated and output to the screen.
 * The outputted text may also be saved.
 */
public class ChainView extends JFrame {
    private static JFileChooser openFile = new JFileChooser(System.getProperty("user.dir"));
    private static JFileChooser saveFile = new JFileChooser(System.getProperty("user.dir"));
    private JSlider orderK, maxCount;
    private JTextField notifications;
    private JTextArea displayOutput;
    private JButton generateBtn;

    private ChainController controller = new ChainController();

    public ChainView(String title) {
        initComponents(title);
    }

    private void initComponents(String title) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel parent = (JPanel) getContentPane();
        parent.setLayout(new BorderLayout());
        parent.add(makeInputPanel(), BorderLayout.NORTH);
        parent.add(makeDisplayPanel(), BorderLayout.CENTER);
        parent.add(makeNotifications(), BorderLayout.SOUTH);

        setTitle(title);

        makeMenus();
        generateTextEvent();

        pack();
        setSize(750, 450);
        setVisible(true);
    }

    private JPanel makeInputPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel flow = new JPanel(new FlowLayout());
        orderK = makeCustomSlider(1, 5, 2, 1, 3, makeOrderKSliderLabel());
        maxCount = makeCustomSlider(1, 10000, 1000, 500, 0, makeMaxCSliderLabel());
        generateBtn = new JButton("Generate Text");
        generateBtn.setEnabled(false);
        p.setBorder(BorderFactory.createTitledBorder("Input:"));
        flow.add(new JLabel("Order-k"));
        flow.add(orderK);
        flow.add(new JLabel("MAX_COUNT"));
        flow.add(maxCount);
        flow.add(generateBtn);
        p.add(flow);
        return p;
    }


    private JPanel makeDisplayPanel() {
        JPanel p = new JPanel(new BorderLayout());
        displayOutput = new JTextArea(10, 40);
        p.setBorder(BorderFactory.createTitledBorder("Output:"));
        p.add(new JScrollPane(displayOutput), BorderLayout.CENTER);
        return p;
    }

    private JPanel makeNotifications() {
        JPanel p = new JPanel(new BorderLayout());
        notifications = new JTextField(30);
        p.setBorder(BorderFactory.createTitledBorder("Notifications:"));
        p.add(notifications, BorderLayout.CENTER);
        return p;
    }

    private JSlider makeCustomSlider(int min, int max, int def, int minT, int majT, Hashtable<Integer, JLabel> lbl) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, def);
        slider.setMinorTickSpacing(minT);
        slider.setMajorTickSpacing(majT);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setLabelTable(lbl);
        return slider;
    }

    private Hashtable<Integer, JLabel> makeOrderKSliderLabel() {
        Hashtable<Integer, JLabel> table = new Hashtable<>();
        table.put(1, new JLabel("1"));
        table.put(3, new JLabel("3"));
        table.put(5, new JLabel("5"));
        return table;
    }

    private Hashtable<Integer, JLabel> makeMaxCSliderLabel() {
        Hashtable<Integer, JLabel> table = new Hashtable<>();
        table.put(1, new JLabel("1"));
        table.put(5000, new JLabel("5000"));
        table.put(10000, new JLabel("10000"));
        return table;
    }

    private void makeMenus() {
        JMenuBar bar = new JMenuBar();
        bar.add(makeFileMenu());
        setJMenuBar(bar);
    }

    @SuppressWarnings("serial")
    private JMenu makeFileMenu() {
        JMenu fileMenu = new JMenu("File");
        makeOpenFileMenuItem(fileMenu);
        makeSaveFileMenuItem(fileMenu);
        makeClearMenuItem(fileMenu);
        makeQuitMenuItem(fileMenu);
        return fileMenu;
    }

    /**
     * This is the event listener for opening a file.  It will send the 'filePath' to the Controller class,
     * which will process the file and return a boolean.  If true, enable the 'Generate Text' button and update
     * the Notification message.  If false, show Error Message dialog.
     * @param fileMenu  the filePath for the selected item in the File Explorer window.
     */
    private void makeOpenFileMenuItem(JMenu fileMenu) {
        fileMenu.add(new AbstractAction("Open File") {
            public void actionPerformed(ActionEvent ev) {
                int retval = openFile.showOpenDialog(null);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    showNotification("Processing...");
                    File file = openFile.getSelectedFile();
                    try{
                        long start = System.nanoTime();
                        int wordCount = controller.processInputFile(file.getPath());
                        long time = System.nanoTime() - start;
                        System.out.printf("Time: %f seconds%n", time / 1e9);
                        if(wordCount > 0) {
                            generateBtn.setEnabled(true);
                            showNotification(wordCount + " words successfully parsed in " + String.format("%.2g%n", (time / 1e9)) + "seconds");
                        }
                        else {
                            showNotification("");
                            showError("Error reading " + file.getName());
                        }
                    } catch (Exception ex) {
                        showNotification("");
                        showError("Error reading " + file.getName());
                    }
                }
            }
        });
    }

    /**
     * This is the event-listener for saving the output to a file.  It will take the output currently in the display
     * window, open the File Explorer and write the text to a file selected by the user.  If there is no text in the
     * display window then an error dialog will be displayed.
     * @param fileMenu
     */
    private void makeSaveFileMenuItem(JMenu fileMenu) {
        // Event Listener for 'Save' menu item
        fileMenu.add(new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
                if(displayOutput.getText().isEmpty()) {
                    showError("No output to save");
                }
                else {
                    int retval = saveFile.showSaveDialog(null);
                    if (retval == JFileChooser.APPROVE_OPTION) {
                        File file = saveFile.getSelectedFile();
                        try {
                            PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8.toString());
                            String[] lines = displayOutput.getText().split("\\n");
                            for (String s : lines) {
                                pw.println(s);
                            }
                            pw.close();
                            showNotification("Successfully saved output to " + file.getName());
                        } catch (FileNotFoundException e1) {
                            showError("Could not open " + file);
                            e1.printStackTrace();
                        } catch (UnsupportedEncodingException e2) {
                            showError("Encoding not supported");
                            e2.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * Resets the sliders to their default values & clears the output from the display window
     * @param fileMenu
     */
    private void makeClearMenuItem(JMenu fileMenu) {
        fileMenu.add(new AbstractAction("Clear") {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
    }

    /**
     * Exits the application
     * @param fileMenu
     */
    private void makeQuitMenuItem(JMenu fileMenu) {
        fileMenu.add(new AbstractAction("Quit") {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });
    }

    /**
     * Gets the current position of the input parameters 'order-k' and 'max count'.  These values are passed to
     * the controller.  Random text data will be generated and returned at which point it can be displayed in the
     * output display window.  If no data is generated, display an error dialog.
     */
    private void generateTextEvent() {
        generateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                Map<String,Integer> inputParams = new HashMap<>();
                inputParams.put("orderK", orderK.getValue());
                inputParams.put("maxC", maxCount.getValue());

                showNotification("Processing...");
                try {
                    long start = System.nanoTime();
                    String generatedText = controller.generateText(inputParams);
                    long time = System.nanoTime() - start;
                    System.out.printf("Time: %f seconds%n", time / 1e9);
                    if (generatedText.isEmpty()) {
                        showNotification("");
                        showError("No text to display - check file contents");
                    } else {
                        update(generatedText);
                        showNotification(maxCount.getValue() + " random words generated in "
                                + String.format("%.2g%n", (time / 1e9)) + "seconds");
                    }
                    generateBtn.setEnabled(false);  // Prevents adding more text to the buffer.
                } catch (Exception e) {
                    showNotification("");
                    showError("Encountered problem generating the text");
                }
            }
        });
    }

    private void showNotification(String s) {
        notifications.setText(s);
    }

    private void showError(String s) {
        JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void update(String s) {
        displayOutput.setLineWrap(true);
        displayOutput.setWrapStyleWord(false);
        displayOutput.append(s);
    }

    private void clear() {
        orderK.setValue(2);
        maxCount.setValue(1000);
        displayOutput.setText("");
    }
}