#!/usr/bin/env python3
import sys
num = int(sys.argv[1])
var = ["x{}".format(i) for i in range(num)]
header = "(set-logic LIA)\n"
synth_fun = "(synth-fun max{} ({}) Int )\n".format(num, " ".join("({} Int)".format(i) for i in var))

var_decl = "\n".join("(declare-var {} Int)".format(i) for i in var)

func_call = "(max{} {})".format(num, " ".join(var))

constr_larger = "\n".join("(constraint (>= {} {}))".format(func_call,i) for i in var)
constr_dis = "(constraint (or {} ) )".format(
        "        \n".join(
            "(= {} {})".format(i, func_call) for i in var
            ))

constr = "{}\n{}".format(constr_larger, constr_dis)
footer = "(check-synth)"

print("{}\n{}\n{}\n{}\n{}".format(header, synth_fun, var_decl, constr, footer))
