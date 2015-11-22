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
public class ChainTest {

    private Chain chain;
    private List<String> listData;
    private String testData;

    @Before
    public void setUp() {
        chain = new Chain(2);
        listData = new ArrayList<>();
        testData = "To Test A Selection of \n\nMultiline Words To Test";
        Collections.addAll(listData, testData.split("\\s+"));
        listData.add(Chain.MARKER);
    }

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Test
    public void testReadTxtInput() throws Exception {
        File tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();

        chain.readTxtInput(testFolder.getRoot() + "/" + tempFile.getName());
        assertEquals(listData, chain.getWords());
    }

    @Test (expected = NoSuchFileException.class)
    public void testReadTxtInputNoSuchFileException() throws IOException {
        chain.readTxtInput("invalid_name");
    }

    @Test (expected = IOException.class)
    public void testReadTxtInputIOException() throws IOException {
        File tempFile = testFolder.newFile("test.txt");
        tempFile.setReadable(false);

        chain.readTxtInput(testFolder.getRoot() + "/" + tempFile.getName());
    }

    @Test
    public void testBuild() throws Exception {
        File tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();

        chain.readTxtInput(testFolder.getRoot() + "/" + tempFile.getName());
        chain.build();

        List<String> expectedValue = new ArrayList<>();
        expectedValue.add("A");
        expectedValue.add("\n");

        assertEquals(7, chain.getStateTable().size());
        assertEquals(expectedValue, chain.getStateTable().get(new Prefix(listData, 0, 2)));
    }

    @Test
    public void testGenerate() throws Exception {
        File tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();

        chain.readTxtInput(testFolder.getRoot() + "/" + tempFile.getName());
        chain.build();

        assertNotNull(chain.generate(100));
        // Update class to allow a seed value to be specified for the Randomizer
    }
}