/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_parameter_kind
 **/
public enum Z3_parameter_kind {
    Z3_PARAMETER_INT (0),
    Z3_PARAMETER_DOUBLE (1),
    Z3_PARAMETER_RATIONAL (2),
    Z3_PARAMETER_SYMBOL (3),
    Z3_PARAMETER_SORT (4),
    Z3_PARAMETER_AST (5),
    Z3_PARAMETER_FUNC_DECL (6);

    private final int intValue;

    Z3_parameter_kind(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_parameter_kind_MappingHolder {
        private static final Map<Integer, Z3_parameter_kind> intMapping = new HashMap<>();
        static {
            for (Z3_parameter_kind k : Z3_parameter_kind.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_parameter_kind fromInt(int v) {
        Z3_parameter_kind k = Z3_parameter_kind_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_parameter_kind");
    }

    public final int toInt() { return this.intValue; }
}

