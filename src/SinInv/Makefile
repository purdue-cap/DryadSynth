PWD := $(shell pwd)
SOURCE := $(wildcard *.java)
TARGET := $(SOURCE:%.java=../../classes/%.class)

placeholder:
	@echo Placeholder for Single Invocation classes

all: $(TARGET)

$(TARGET): ../../classes/%.class : %.java
	cd ../.. && javac -source 1.7 -target 1.7 -classpath $(CP) -sourcepath $(SP) -d classes $(PWD)/$<

.PHONY: all
