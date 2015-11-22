package com.krafty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author  Seán Marnane 21/11/15
 *
 * Note: Must implement the hashCode and equals method in order to store & retrieve it's entries.
 */
public class Prefix {

    private List<String> prefix;

    /*
    * Create a new ArrayList of n words beginning at index 'pos' of src.
    * @param
    * */
    public Prefix(List<String> src, int pos, int n) {
        prefix = new ArrayList<>();
        for (int i = pos; i < (pos+n); i++)
            prefix.add(src.get(i));
//        prefix = src.subList(pos, pos + n);
    }

    public List<String> getPrefix() {
        return prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prefix prefix = (Prefix) o;
        return Objects.equals(this.prefix, prefix.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix);
    }
}