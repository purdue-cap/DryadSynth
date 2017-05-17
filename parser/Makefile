LIB_ANTLR := lib/antlr.jar
LIB := $(LIB_ANTLR):classes:build:src
NAME := Sygus
GRAMMAR_SCRIPT := $(NAME).g4
PARSER_CLASS_NAMES := BaseListener Lexer Listener Parser
PARSER_SOURCE := $(foreach class,$(PARSER_CLASS_NAMES),build/$(NAME)$(class).java)
SRC_SOURCE := $(wildcard src/*.java)
PARSER_CLASSES := $(PARSER_SOURCE:build/%.java=classes/%.class)
SRC_CLASSES := $(SRC_SOURCE:src/%.java=classes/%.class)

all: group classes

group:
	@echo "Yanjun Wang"

classes: $(PARSER_CLASSES) $(SRC_CLASSES)

$(PARSER_CLASSES): classes/%.class : build/%.java
	mkdir -p classes
	javac -source 1.7 -target 1.7 -cp $(LIB) -d classes $<

$(PARSER_SOURCE): $(GRAMMAR_SCRIPT)
	mkdir -p build
	java -cp $(LIB_ANTLR) org.antlr.v4.Tool -o build $(GRAMMAR_SCRIPT)

$(SRC_CLASSES): classes/%.class : src/%.java
	mkdir -p classes
	javac -source 1.7 -target 1.7 -cp $(LIB) -d classes $<

clean_classes:
	rm -rf classes

clean_parser:
	rm -rf build

clean: clean_classes clean_parser

.PHONY: all group classes clean_classes clean_parser clean
