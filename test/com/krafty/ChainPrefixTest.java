package com.krafty;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Seán Marnane 22/11/15
 */
public class ChainPrefixTest {

    private ChainPrefix cPref, cPrefDup, cPrefAlt, cPrefNonValueDouble, cPrefNonValueSingle;
    private List<String> srcInput;

    @Before
    public void setUp() throws Exception {
        srcInput = new ArrayList<>();
        srcInput.add("A");
        srcInput.add("B");
        srcInput.add("C");
        srcInput.add("B");
        srcInput.add("A");
        srcInput.add("B");
        srcInput.add("C");

        cPref = new ChainPrefix(srcInput, 2, 2, ChainModel.MARKER);
        cPrefDup = new ChainPrefix(srcInput, 2, 2, ChainModel.MARKER);
        cPrefAlt = new ChainPrefix(srcInput, 3, 2, ChainModel.MARKER);

        cPrefNonValueDouble = new ChainPrefix(srcInput, 0, 2, ChainModel.MARKER);
        cPrefNonValueSingle = new ChainPrefix(srcInput, 1, 2, ChainModel.MARKER);
    }

    @Test
    public void testGetPrefix() throws Exception {
        List<String> expectedDouble = new ArrayList<>();
        expectedDouble.add("\n");
        expectedDouble.add("\n");
        assertEquals(expectedDouble, cPrefNonValueDouble.getcPref());

        List<String> expectedSingle = new ArrayList<>();
        expectedSingle.add("\n");
        expectedSingle.add("A");
        assertEquals(expectedSingle, cPrefNonValueSingle.getcPref());

        List<String> expectedPrefix = new ArrayList<>();
        expectedPrefix.add("A");
        expectedPrefix.add("B");
        assertEquals(expectedPrefix, cPref.getcPref());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue("Equals self", cPref.equals(cPref));
        assertFalse("Equals null", cPref.equals(null));
        assertTrue("Equals each-other", cPref.equals(cPrefDup));
        // More tests: reflexive, symmetric, transitive, consistent, etc.
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(cPref, cPrefDup);

        int initialHashCode = cPref.hashCode();
        assertEquals("Consistent hashCode", initialHashCode, cPref.hashCode());
        assertEquals("Consistent hashCode", initialHashCode, cPref.hashCode());

        int xHashCode = cPref.hashCode();
        int yHashCode = cPrefDup.hashCode();
        assertEquals(xHashCode, yHashCode);
        // More tests:
        // Invoking same object more than once, hashCode must consistently return the same integer
        // If 2 objects are equal i.e. A.equals(B), hashCode must consistently return the same integer
    }
}