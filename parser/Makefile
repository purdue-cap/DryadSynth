LIB_ANTLR := lib/antlr.jar
ANTLR_SCRIPT := Sygus.g4

all: group compiler

group:
	@echo "Yanjun Wang"
compiler:
	rm -rf build
	mkdir build
	java -cp $(LIB_ANTLR) org.antlr.v4.Tool -o build $(ANTLR_SCRIPT)
	rm -rf classes
	mkdir classes
	javac -source 1.7 -target 1.7 -cp $(LIB_ANTLR) -d classes src/*.java build/*.java
clean:
	rm -rf classes build

.PHONY: all group compiler clean
