/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_param_kind
 **/
public enum Z3_param_kind {
    Z3_PK_UINT (0),
    Z3_PK_BOOL (1),
    Z3_PK_DOUBLE (2),
    Z3_PK_SYMBOL (3),
    Z3_PK_STRING (4),
    Z3_PK_OTHER (5),
    Z3_PK_INVALID (6);

    private final int intValue;

    Z3_param_kind(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_param_kind_MappingHolder {
        private static final Map<Integer, Z3_param_kind> intMapping = new HashMap<>();
        static {
            for (Z3_param_kind k : Z3_param_kind.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_param_kind fromInt(int v) {
        Z3_param_kind k = Z3_param_kind_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_param_kind");
    }

    public final int toInt() { return this.intValue; }
}

