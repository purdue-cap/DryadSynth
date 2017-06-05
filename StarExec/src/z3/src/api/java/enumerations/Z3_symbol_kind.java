/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_symbol_kind
 **/
public enum Z3_symbol_kind {
    Z3_INT_SYMBOL (0),
    Z3_STRING_SYMBOL (1);

    private final int intValue;

    Z3_symbol_kind(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_symbol_kind_MappingHolder {
        private static final Map<Integer, Z3_symbol_kind> intMapping = new HashMap<>();
        static {
            for (Z3_symbol_kind k : Z3_symbol_kind.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_symbol_kind fromInt(int v) {
        Z3_symbol_kind k = Z3_symbol_kind_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_symbol_kind");
    }

    public final int toInt() { return this.intValue; }
}

