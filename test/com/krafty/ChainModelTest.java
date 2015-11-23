package com.krafty;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author  Seán Marnane 21/11/15
 *
 */
public class ChainModelTest {

    private ChainModel model;
    private List<String> listData, expectedData;
    private String testData;

    @Before
    public void setUp() {
        model = new ChainModel();
        listData = new ArrayList<>();
        testData = "To Test A Selection of Multiline\n\n Words To Test";
        Collections.addAll(listData, testData.split("\\s+"));
        listData.add(ChainModel.MARKER);

        expectedData = new ArrayList<>();
        expectedData.add("To");
    }

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Test
    public void testProcessInput() throws Exception {
        File tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();

        model.processInput(testFolder.getRoot() + "/" + tempFile.getName());
        assertEquals(listData, model.getWords());
    }

    @Test (expected = NoSuchFileException.class)
    public void testProcessInputNoSuchFileException() throws IOException {
        model.processInput("invalid_name");
    }

    @Test (expected = IOException.class)
    public void testProcessInputIOException() throws IOException {
        File tempFile = testFolder.newFile("test.txt");
        tempFile.setReadable(false);

        model.processInput(testFolder.getRoot() + "/" + tempFile.getName());
    }

    @Test
    public void testBuild() throws Exception {
        File tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();

        model.processInput(testFolder.getRoot() + "/" + tempFile.getName());
        model.build();

        assertEquals(9, model.getStateTable().size());
        assertEquals(expectedData, model.getStateTable().get(new ChainPrefix(listData, 0, 2, ChainModel.MARKER)));
    }

    @Test
    public void testGenerate() throws Exception {
        File tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();

        model.processInput(testFolder.getRoot() + "/" + tempFile.getName());
        model.build();

        assertNotNull(model.generateRandomText());
        // Update class to allow a seed value to be specified for the Randomizer
    }
}