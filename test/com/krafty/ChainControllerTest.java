package com.krafty;

import com.krafty.util.Randomizer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * @author Seán Marnane 23/11/15
 */
public class ChainControllerTest {

    private ChainController cCon;
    private ChainModel model;
    private File tempFile;
    private Randomizer rand;
    private List listData;
    private String testData;

    @Before
    public void setUp() throws Exception {
        cCon = new ChainController();
        rand = new Randomizer(1234);
        model = new ChainModel();
        listData = new ArrayList<>();
        testData = "To Test A Selection of Multiline\n\n Words To Test";
        Collections.addAll(listData, testData.split("\\s+"));
        listData.add(ChainModel.MARKER);
        tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();
    }

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Test
    public void testStartApplication() throws Exception {
        cCon.startApplication("Text Generator - using Markov Chain algorithm");
    }

    @Test
    public void testProcessInputFile() throws Exception {
        cCon.processInputFile(testFolder.getRoot() + "/" + tempFile.getName());
    }

    @Ignore
    @Test
    public void testGenerateText() throws Exception {
        model.processInput(testFolder.getRoot() + "/" + tempFile.getName());
        model.buildStateTable();

        Map<String, Integer> testParams = new HashMap<>();
        testParams.put("orderK", 1);
        testParams.put("maxC", 2);
        cCon.generateText(testParams);
    }
}