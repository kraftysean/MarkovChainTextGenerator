package com.krafty;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Seán Marnane 21/11/15
 */
public class ChainTest {

    private Chain chainDefCon = new Chain();
    private Chain chainParamsCon = new Chain(2);
    private List<String> listData;
    private String testData;

    @Before
    public void setUp() {
        testData = "A Selection of Words To Test";
        listData = Arrays.asList(testData.split("\\s+"));
    }

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Test
    public void testReadTrainingData() throws Exception {
        File tempFile = testFolder.newFile("test.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(testData);
        bw.close();

        List<String> strings = chainDefCon.readTrainingData(testFolder.getRoot() + "/" + tempFile.getName());
        assertEquals(listData, strings);
    }

    @Test
    public void testReadTrainingDataNoSuchFileException() throws IOException {
        chainDefCon.readTrainingData("invalid_name");
    }

    @Test (expected = IOException.class)
    public void testReadTrainingDataIOException() throws IOException {
        File tempFile = testFolder.newFile("test.txt");
        tempFile.setReadable(false);

        chainDefCon.readTrainingData(testFolder.getRoot() + "/" + tempFile.getName());
    }

    @Test
    public void testBuild() throws Exception {
        chainDefCon.build(listData);
        fail("Not implemented yet!");
    }

    @Test
    public void testGenerate() throws Exception {
        chainDefCon.generate(100);
        fail("Not implemented yet!");
    }
}