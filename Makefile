NAME1 := Sygus
GRAMMAR1_SCRIPT := $(NAME1).g4
NAME2 := SygusV1
GRAMMAR2_SCRIPT := $(NAME2).g4
PARSER_CLASS_NAMES := BaseListener Lexer Listener Parser
PARSER_SOURCE := $(foreach class,$(PARSER_CLASS_NAMES),build/$(NAME1)$(class).java)
PARSER_CLASSES := $(PARSER_SOURCE:build/%.java=classes/%.class)
SUBDIRS := $(wildcard src/*/.)

EMPTY :=
SPACE := $(EMPTY) $(EMPTY)
LIB_ANTLR := lib/antlr.jar
LIB_Z3 := lib/com.microsoft.z3.jar
LIB_JOPTS := lib/jopt-simple.jar
export CP := $(LIB_Z3):$(LIB_ANTLR):$(LIB_JOPTS):classes:$(CLASSPATH)
export SP := build:$(subst $(SPACE),:,$(SUBDIRS))

all: classes subdir

StarExec.zip: all
	cp -rf classes StarExec/bin
	cd StarExec && zip ../$@ -r *

classes: $(PARSER_CLASSES)

$(PARSER_CLASSES): classes/%.class : build/%.java
	mkdir -p classes
	javac -classpath $(CP) -sourcepath $(SP) -d classes $<

$(PARSER_SOURCE): $(GRAMMAR_SCRIPT)
	mkdir -p build
	java -cp $(LIB_ANTLR) org.antlr.v4.Tool -o build -visitor $(GRAMMAR1_SCRIPT)
	java -cp $(LIB_ANTLR) org.antlr.v4.Tool -o build -visitor $(GRAMMAR2_SCRIPT)

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
	rm -f log.*.txt.*
	rm -f log.*.txt.lck

clean: clean_classes clean_parser clean_package clean_logs

.PHONY: all classes subdir $(SUBDIRS)\
	clean_classes clean_parser clean_package clean_logs clean