package com.krafty.util;

import java.util.Random;

/**
 * @author Seán Marnane 23/11/15
 *
 * Utility class that takes an integer (bound) or integer and SEED as inputs, and returns a value within the
 * range 0 (inclusive) and bound (exclusive).  Setting the SEED will make testing expected output easier.
 */
public class Randomizer {

    private Random rand;

    /**
     * Class constructor that takes an array of integer values and a SEED
     * @param seed  the initial seed
     */
    public Randomizer(long seed) {
        rand = new Random(seed);
    }

    /**
     * Generate a random number within the bounds of the input
     * @param bound the upper bound (exclusive). Must be positive
     * @return  uniformly distributed int value between 0 (inclusive) and the specified bound value (exclusive)
     */
    public int randomGenerator(int bound) {
        int result = rand.nextInt(bound);
        return result;
    }
}
