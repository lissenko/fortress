# Author: Tanguy Lissenko

JC := javac

OUT_DIR := bin
SRC_DIR := src
DIST_DIR := dist
JAR := $(DIST_DIR)/compiler.jar

JFLAGS := -d $(OUT_DIR) -cp $(SRC_DIR)
LEXER := $(SRC_DIR)/Lexer.java
SRCS := $(wildcard src/*.java) $(LEXER)
TEXFILE := test.tex

all: $(LEXER) $(JAR)

$(LEXER): $(SRC_DIR)/flex/LexicalAnalyzer.flex
	jflex $^ -d $(SRC_DIR) --nobak

$(JAR): $(SRCS)
	$(JC) $(JFLAGS) $(SRCS)
	jar cfe $@ Main -C bin/ .

run: all
ifndef input-file
	$(error Please set input-file: make input-file=test/your-file run)
else
	java -jar $(JAR) $(input-file)
endif

llvm: all
ifndef input-file
	$(error Please set input-file: make input-file=test/your-file llvm)
else
	java -jar $(JAR) $(input-file) > llvm.ll
	llvm-as llvm.ll
	lli llvm.bc
endif

run-tree: all
ifndef input-file
	$(error Please set input-file: make input-file=test/your-file run-tree)
else
	mkdir -p tex
	touch tex/$(TEXFILE)
	rm tex/$(TEXFILE)
	java -jar $(JAR) -wt $(TEXFILE) $(input-file)
	pdflatex -output-directory tex/ tex/$(TEXFILE)
	xdg-open tex/$(TEXFILE:.tex=.pdf)
endif

test: all
ifndef input-file
	$(error Please set input-file: make input-file=test/your-file test)
else
	java -jar $(JAR) $(input-file) > more/instance.out
	diff more/instance.out $(patsubst test/%.fs,more/%.out,$(input-file))
endif

doc: report
	javadoc -d doc/javadoc -sourcepath $(SRC_DIR) $(SRCS)


clean:
	rm -rf $(OUT_DIR) $(JAR) $(LEXER)

.PHONY: all run test doc clean
