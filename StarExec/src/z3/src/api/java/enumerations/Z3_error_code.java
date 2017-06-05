/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_error_code
 **/
public enum Z3_error_code {
    Z3_OK (0),
    Z3_SORT_ERROR (1),
    Z3_IOB (2),
    Z3_INVALID_ARG (3),
    Z3_PARSER_ERROR (4),
    Z3_NO_PARSER (5),
    Z3_INVALID_PATTERN (6),
    Z3_MEMOUT_FAIL (7),
    Z3_FILE_ACCESS_ERROR (8),
    Z3_INTERNAL_FATAL (9),
    Z3_INVALID_USAGE (10),
    Z3_DEC_REF_ERROR (11),
    Z3_EXCEPTION (12);

    private final int intValue;

    Z3_error_code(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_error_code_MappingHolder {
        private static final Map<Integer, Z3_error_code> intMapping = new HashMap<>();
        static {
            for (Z3_error_code k : Z3_error_code.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_error_code fromInt(int v) {
        Z3_error_code k = Z3_error_code_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_error_code");
    }

    public final int toInt() { return this.intValue; }
}

