/**
 *  Automatically generated file
 **/

package com.microsoft.z3.enumerations;

import java.util.HashMap;
import java.util.Map;

/**
 * Z3_goal_prec
 **/
public enum Z3_goal_prec {
    Z3_GOAL_PRECISE (0),
    Z3_GOAL_UNDER (1),
    Z3_GOAL_OVER (2),
    Z3_GOAL_UNDER_OVER (3);

    private final int intValue;

    Z3_goal_prec(int v) {
        this.intValue = v;
    }

    // Cannot initialize map in constructor, so need to do it lazily.
    // Easiest thread-safe way is the initialization-on-demand holder pattern.
    private static class Z3_goal_prec_MappingHolder {
        private static final Map<Integer, Z3_goal_prec> intMapping = new HashMap<>();
        static {
            for (Z3_goal_prec k : Z3_goal_prec.values())
                intMapping.put(k.toInt(), k);
        }
    }

    public static final Z3_goal_prec fromInt(int v) {
        Z3_goal_prec k = Z3_goal_prec_MappingHolder.intMapping.get(v);
        if (k != null) return k;
        throw new IllegalArgumentException("Illegal value " + v + " for Z3_goal_prec");
    }

    public final int toInt() { return this.intValue; }
}

