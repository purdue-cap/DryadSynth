#!/usr/bin/env python3
import sys
num = int(sys.argv[1])
var = ["x{}".format(i) for i in range(num)]
idx = "k"
header = "(set-logic LIA)\n"
synth_fun = "(synth-fun findIdx ({}) Int )\n".format(" ".join("({} Int)".format(i) for i in var + [idx]))

var_decl = "\n".join("(declare-var {} Int)".format(i) for i in var + [idx])

func_call = "(findIdx {})".format(" ".join(var + [idx]))

comm_cond = " ".join("(< x{} x{})".format(i, i+1) for i in range(num-1))

k_cond = ["(> k x{}) (< k x{})".format(i, i+1) for i in range(num-1)]
k_cond.append("(> k x{})".format(num-1))
k_cond.insert(0, "(< k x{})".format(0))

constrs = ["(constraint (=> (and {} {}) (= {} {})))".format(comm_cond, i, func_call, j) for j, i in enumerate(k_cond) ]

constr = "\n".join(constrs)
footer = "(check-synth)"

print("{}\n{}\n{}\n{}\n{}".format(header, synth_fun, var_decl, constr, footer))
