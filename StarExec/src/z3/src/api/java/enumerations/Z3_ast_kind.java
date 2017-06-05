/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_ast_kind
 **/
public enum Z3_ast_kind {
    Z3_NUMERAL_AST (0),
    Z3_APP_AST (1),
    Z3_VAR_AST (2),
    Z3_QUANTIFIER_AST (3),
    Z3_SORT_AST (4),
    Z3_FUNC_DECL_AST (5),
    Z3_UNKNOWN_AST (1000);

    private final int intValue;

    Z3_ast_kind(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_ast_kind_MappingHolder {
        private static final Map<Integer, Z3_ast_kind> intMapping = new HashMap<>();
        static {
            for (Z3_ast_kind k : Z3_ast_kind.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_ast_kind fromInt(int v) {
        Z3_ast_kind k = Z3_ast_kind_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_ast_kind");
    }

    public final int toInt() { return this.intValue; }
}

