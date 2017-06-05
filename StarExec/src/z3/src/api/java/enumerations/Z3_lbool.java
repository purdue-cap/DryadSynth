/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_lbool
 **/
public enum Z3_lbool {
    Z3_L_FALSE (-1),
    Z3_L_UNDEF (0),
    Z3_L_TRUE (1);

    private final int intValue;

    Z3_lbool(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_lbool_MappingHolder {
        private static final Map<Integer, Z3_lbool> intMapping = new HashMap<>();
        static {
            for (Z3_lbool k : Z3_lbool.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_lbool fromInt(int v) {
        Z3_lbool k = Z3_lbool_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_lbool");
    }

    public final int toInt() { return this.intValue; }
}

