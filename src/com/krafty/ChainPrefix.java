package com.krafty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author  Seán Marnane 21/11/15
 *
 * Note: Must implement the hashCode and equals method in order to store & retrieve it's entries.
 */
public class ChainPrefix {

    private List<String> cPref;

    /*
    * Class constructor
    *
    * Create a new ArrayList of n words. If 'startPos' is less than 0, set prefix to MARKER, else add prefix value.
    * @param src  source list containing prefix values to be assigned
    * @param pos  current index in the source list
    * @param n  number of prefix values
    * @param MARKER  escape character to identify a non-prefix/suffix value.
    *
    */
    public ChainPrefix(List<String> src, int pos, int n, String marker) {
        cPref = new ArrayList<>();
        int startPos = pos - n;
        int nonValue = startPos;
        for (int i = 0; i < n; i++) {
            if (nonValue < 0) {
                cPref.add(marker);
                nonValue++;
            } else {
                cPref.add(src.get(startPos + i));
            }
        }
    }

    /*
    * Simple getter method
    */
    public List<String> getcPref() {
        return cPref;
    }

    /*
    * To successfully store and retrieve objects from Hashtable, must over-ride the hashCode method
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChainPrefix prefix = (ChainPrefix) o;
        return Objects.equals(this.cPref, prefix.cPref);
    }

    /*
    * To successfully store and retrieve objects from Hashtable, must over-ride the hashCode method
    */
    @Override
    public int hashCode() {
        return Objects.hash(cPref);
    }
}