package com.krafty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Seán Marnane 21/11/15.
 */
public class Prefix {

    private List<String> pref;  //

    public Prefix(Prefix p) {
        pref = new ArrayList<>();
        for (String word : p.getPref())
            pref.add(word);
    }

    public Prefix(int n, String str) {
        for (int i = 0; i < n; i++) {
            pref.add(str);
        }
    }

    public List<String> getPref() {
        return pref;
    }

    public void setPref(List<String> pref) {
        this.pref = pref;
    }
}
