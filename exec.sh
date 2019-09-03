#!/bin/bash
BASE=`dirname $0`
java -ea -cp $BASE/classes/:$BASE/lib/antlr.jar:$BASE/lib/com.microsoft.z3.jar:$BASE/lib/jopt-simple.jar Run $@
