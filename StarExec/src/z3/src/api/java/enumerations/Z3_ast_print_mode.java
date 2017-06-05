/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_ast_print_mode
 **/
public enum Z3_ast_print_mode {
    Z3_PRINT_SMTLIB_FULL (0),
    Z3_PRINT_LOW_LEVEL (1),
    Z3_PRINT_SMTLIB_COMPLIANT (2),
    Z3_PRINT_SMTLIB2_COMPLIANT (3);

    private final int intValue;

    Z3_ast_print_mode(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_ast_print_mode_MappingHolder {
        private static final Map<Integer, Z3_ast_print_mode> intMapping = new HashMap<>();
        static {
            for (Z3_ast_print_mode k : Z3_ast_print_mode.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_ast_print_mode fromInt(int v) {
        Z3_ast_print_mode k = Z3_ast_print_mode_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_ast_print_mode");
    }

    public final int toInt() { return this.intValue; }
}

