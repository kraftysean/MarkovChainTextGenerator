package com.krafty;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Seán Marnane 22/11/15
 */
public class PrefixTest {

    private Prefix prefix, prefixDup, prefixAlt;
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

        prefix = new Prefix(srcInput, 0, 2);
        prefixDup = new Prefix(srcInput, 0, 2);
        prefixAlt = new Prefix(srcInput, 1, 2);
    }

    @Test
    public void testGetPrefix() throws Exception {
        List<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("B");
        assertEquals(expected, prefix.getPrefix());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue("Equals self", prefix.equals(prefix));
        assertFalse("Equals null", prefix.equals(null));
        assertTrue("Equals each-other", prefix.equals(prefixDup));


        // Add more assertions (reflexive, symemtric, transitive, consistent, etc.)
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(prefix, prefixDup);

        int initialHashCode = prefix.hashCode();
        assertEquals("Consistent hashCode", initialHashCode, prefix.hashCode());
        assertEquals("Consistent hashCode", initialHashCode, prefix.hashCode());

        int xHashCode = prefix.hashCode();
        int yHashCode = prefixDup.hashCode();
        assertEquals(xHashCode, yHashCode);

        // Invoking same object more than once, hashCode must consistently return the same integer
        // If 2 objects are equal i.e. A.equals(B), hashCode must consistently return the same integer
    }
}