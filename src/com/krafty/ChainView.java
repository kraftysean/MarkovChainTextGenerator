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
 */
public class ChainView extends JFrame {
    protected static JFileChooser openFile = new JFileChooser(System.getProperty("user.dir"));
    protected static JFileChooser saveFile = new JFileChooser(System.getProperty("user.dir"));
    protected JSlider orderK, maxCount;
    protected JTextField notifications;
    protected JTextArea displayOutput;
    protected JButton generateBtn;

    private ChainController controller = new ChainController();

    public ChainView() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel parent = (JPanel) getContentPane();
        parent.setLayout(new BorderLayout());
        parent.add(makeInputPanel(), BorderLayout.NORTH);
        parent.add(makeDisplayPanel(), BorderLayout.CENTER);
        parent.add(makeNotifications(), BorderLayout.SOUTH);

        setTitle("Text Generator - using Markov Chain algorithm");

        makeMenus();
        generateTextEvent();

        pack();
        setSize(750, 450);
        setVisible(true);
    }

    protected JPanel makeInputPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel flow = new JPanel(new FlowLayout());
        orderK = makeCustomSlider(1, 5, 2, 1, 3, setOrderKSlider());
        maxCount = makeCustomSlider(100, 10000, 1000, 1000, 2500, setMaxCSlider());
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


    protected JPanel makeDisplayPanel() {
        JPanel p = new JPanel(new BorderLayout());
        displayOutput = new JTextArea(10, 40);
        p.setBorder(BorderFactory.createTitledBorder("Output:"));
        p.add(new JScrollPane(displayOutput), BorderLayout.CENTER);
        return p;
    }

    protected JPanel makeNotifications() {
        JPanel p = new JPanel(new BorderLayout());
        notifications = new JTextField(30);
        p.setBorder(BorderFactory.createTitledBorder("Notifications:"));
        p.add(notifications, BorderLayout.CENTER);
        return p;
    }

    protected JSlider makeCustomSlider(int min, int max, int def, int minT, int majT, Hashtable<Integer, JLabel> lbl) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, def);
        slider.setMinorTickSpacing(minT);
        slider.setMajorTickSpacing(majT);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setLabelTable(lbl);
        return slider;
    }

    private Hashtable<Integer, JLabel> setOrderKSlider() {
        Hashtable<Integer, JLabel> table = new Hashtable<>();
        table.put(1, new JLabel("1"));
        table.put(3, new JLabel("3"));
        table.put(5, new JLabel("5"));
        return table;
    }

    private Hashtable<Integer, JLabel> setMaxCSlider() {
        Hashtable<Integer, JLabel> table = new Hashtable<>();
        table.put(100, new JLabel("100"));
        table.put(5000, new JLabel("5000"));
        table.put(10000, new JLabel("10000"));
        return table;
    }

    protected void makeMenus() {
        JMenuBar bar = new JMenuBar();
        bar.add(makeFileMenu());
        setJMenuBar(bar);
    }


    @SuppressWarnings("serial")
    protected JMenu makeFileMenu() {
        JMenu fileMenu = new JMenu("File");

        // Event listener for Open menu item
        fileMenu.add(new AbstractAction("Open File") {
            public void actionPerformed(ActionEvent ev) {
                int retval = openFile.showOpenDialog(null);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    File file = openFile.getSelectedFile();
                    try{
                        boolean isFileValid = controller.processInputFile(file.getPath());
                        if(isFileValid) {
                            generateBtn.setEnabled(true);
                            showNotification("File successfully read in to buffer");
                        }
                    } catch (Exception ex) {
                        showError("Error reading " + file.getName());
                    }
                }
            }
        });

        // Event Listener for 'Save' menu item
        fileMenu.add(new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
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
                    } catch (FileNotFoundException e1) {
                        showError("Could not open " + file);
                        e1.printStackTrace();
                    } catch (UnsupportedEncodingException e2) {
                        showError("Encoding not supported");
                        e2.printStackTrace();
                    }
                }
            }
        });

        fileMenu.add(new AbstractAction("Clear") {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        fileMenu.add(new AbstractAction("Quit") {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });
        return fileMenu;
    }

    // Event Listener for 'Generate Text' button
    protected void generateTextEvent() {
        generateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                Map<String,Integer> inputParams = new HashMap<>();
                inputParams.put("orderK", orderK.getValue());
                inputParams.put("maxC", maxCount.getValue());

                try {
                    String generatedText = controller.generateText(inputParams);
                    update(generatedText);

                } catch (Exception e) {
                    showError("Encountered problem generating the text");
                }
            }
        });
    }

    // Prints message in notifications panel
    public void showNotification(String s) {
        notifications.setText(s);
    }

    // Displays a dialog for any error's returned
    public void showError(String s) {
        JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Appends data to the output textbox
    public void update(String s) {
        displayOutput.setLineWrap(true);
        displayOutput.setWrapStyleWord(true);
        displayOutput.append(s);
    }

    // Resets order-K, maxCount and output - however data is still in buffer
    public void clear() {
        orderK.setValue(2);
        maxCount.setValue(1000);
        displayOutput.setText("");
    }
}