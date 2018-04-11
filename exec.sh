#!/bin/bash
java -cp classes/:lib/antlr.jar:lib/com.microsoft.z3.jar:lib/jopt-simple.jar Run $@
