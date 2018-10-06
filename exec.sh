#!/bin/bash
BASEDIR=$(dirname "$0")
export CLASSPATH=$BASEDIR/classes/:$BASEDIR/lib/com.microsoft.z3.jar:$BASEDIR/lib/jopt-simple.jar:$BASEDIR/lib/antlr.jar
java Run -t 1 -v $@
