NAME := Sygus
GRAMMAR_SCRIPT := $(NAME).g4
PARSER_CLASS_NAMES := BaseListener Lexer Listener Parser
PARSER_SOURCE := $(foreach class,$(PARSER_CLASS_NAMES),build/$(NAME)$(class).java)
PARSER_CLASSES := $(PARSER_SOURCE:build/%.java=classes/%.class)
SUBDIRS := $(wildcard src/*/.)

EMPTY :=
SPACE := $(EMPTY) $(EMPTY)
LIB_ANTLR := lib/antlr.jar
LIB_Z3 := lib/com.microsoft.z3.jar
export LIB := $(LIB_Z3):$(LIB_ANTLR):classes:build:$(subst $(SPACE),:,$(SUBDIRS)):$(CLASSPATH)

all: classes subdir

classes: $(PARSER_CLASSES)

$(PARSER_CLASSES): classes/%.class : build/%.java
	mkdir -p classes
	javac -source 1.7 -target 1.7 -cp $(LIB) -d classes $<

$(PARSER_SOURCE): $(GRAMMAR_SCRIPT)
	mkdir -p build
	java -cp $(LIB_ANTLR) org.antlr.v4.Tool -o build $(GRAMMAR_SCRIPT)

subdir: $(SUBDIRS)

$(SUBDIRS):
	$(MAKE) -C $@

clean_classes:
	rm -rf classes

clean_parser:
	rm -rf build

clean: clean_classes clean_parser

.PHONY: all classes subdir $(SUBDIRS) clean_classes clean_parser clean
