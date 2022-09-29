PWD := $(shell pwd)
SOURCE := $(wildcard *.java)
TARGET := $(SOURCE:%.java=../../classes/%.class)

all: $(TARGET)

$(TARGET): ../../classes/%.class : %.java
	cd ../.. && javac  -classpath $(CP) -sourcepath $(SP) -d classes $(PWD)/$<

.PHONY: all
