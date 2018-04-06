#!/usr/bin/env python3
import sys

var = sys.argv[1:]
varl = " ".join("({} Int)".format(i) for i in var)
pvarl = " ".join("({}! Int)".format(i) for i in var)
decl = "\n".join("(declare-primed-var {} Int)".format(i) for i in var)

print("(set-logic LIA)")
print("")
print("(synth-inv inv-f ({}))".format(varl))
print("")
print(decl)
print("")
print("(define-fun pre-f ({}) Bool".format(varl))
print(")")
print("")
print("(define-fun trans-f ({} {}) Bool".format(varl, pvarl))
print(")")
print("")
print("(define-fun post-f ({}) Bool".format(varl))
print(")")
print("")
print("(inv-constraint inv-f pre-f trans-f post-f)")
print("")
print("(check-synth)")


