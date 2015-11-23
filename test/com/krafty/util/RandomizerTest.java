package com.krafty.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Seán Marnane 23/11/15
 */
public class RandomizerTest {

    private static final int SEED = 1234;
    private Randomizer rand;

    @Before
    public void setUp() throws Exception {
        rand = new Randomizer(SEED);
    }

    @Test
    public void testRandomGenerator() throws Exception {
        assertEquals(3, rand.randomGenerator(5));
    }
}