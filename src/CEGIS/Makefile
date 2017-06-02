PWD := $(shell pwd)
SOURCE := $(wildcard *.java)
TARGET := $(SOURCE:%.java=../../classes/%.class)

all: $(TARGET)

$(TARGET): ../../classes/%.class : %.java
	cd ../.. && javac -source 1.7 -target 1.7 -classpath $(LIB) -d classes $(PWD)/$<

.PHONY: all
