/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_sort_kind
 **/
public enum Z3_sort_kind {
    Z3_UNINTERPRETED_SORT (0),
    Z3_BOOL_SORT (1),
    Z3_INT_SORT (2),
    Z3_REAL_SORT (3),
    Z3_BV_SORT (4),
    Z3_ARRAY_SORT (5),
    Z3_DATATYPE_SORT (6),
    Z3_RELATION_SORT (7),
    Z3_FINITE_DOMAIN_SORT (8),
    Z3_FLOATING_POINT_SORT (9),
    Z3_ROUNDING_MODE_SORT (10),
    Z3_SEQ_SORT (11),
    Z3_RE_SORT (12),
    Z3_UNKNOWN_SORT (1000);

    private final int intValue;

    Z3_sort_kind(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_sort_kind_MappingHolder {
        private static final Map<Integer, Z3_sort_kind> intMapping = new HashMap<>();
        static {
            for (Z3_sort_kind k : Z3_sort_kind.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_sort_kind fromInt(int v) {
        Z3_sort_kind k = Z3_sort_kind_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_sort_kind");
    }

    public final int toInt() { return this.intValue; }
}

