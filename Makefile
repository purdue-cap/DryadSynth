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
export CP := $(LIB_Z3):$(LIB_ANTLR):classes:$(CLASSPATH)
export SP := build:$(subst $(SPACE),:,$(SUBDIRS))

all: classes subdir

StarExec.zip: all
	cp -rf classes StarExec/bin
	cd StarExec && zip ../$@ -r *

classes: $(PARSER_CLASSES)

$(PARSER_CLASSES): classes/%.class : build/%.java
	mkdir -p classes
	javac -source 1.7 -target 1.7 -classpath $(CP) -sourcepath $(SP) -d classes $<

$(PARSER_SOURCE): $(GRAMMAR_SCRIPT)
	mkdir -p build
	java -cp $(LIB_ANTLR) org.antlr.v4.Tool -o build -visitor $(GRAMMAR_SCRIPT)

subdir: $(SUBDIRS)

$(SUBDIRS):
	$(MAKE) -C $@

clean_classes:
	rm -rf classes

clean_parser:
	rm -rf build

clean_package:
	rm -f StarExec.zip
	rm -rf StarExec/bin/classes

clean_logs:
	rm -f log.*.txt
	rm -f log.*.txt.lck

clean: clean_classes clean_parser clean_package clean_logs

.PHONY: all classes subdir $(SUBDIRS)\
	clean_classes clean_parser clean_package clean_logs clean
