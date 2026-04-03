# -----------------------------------------------------------------------
# Makefile for the String-Language -> Java translator (Part 2)
#
# Prerequisites (place JARs in the repository root lib/ directory):
#   ../../lib/java-cup-11b.jar
#   ../../lib/jflex-full-1.9.1.jar
#
# Build:  make
# Run:    echo '<program>' | java -cp ../../lib/java-cup-11b.jar:build Main
# Clean:  make clean
# -----------------------------------------------------------------------

JFLEX_JAR   ?= ../../lib/jflex-full-1.9.1.jar
CUP_JAR     ?= ../../lib/java-cup-11b.jar

BUILD       = build

all: $(BUILD)/Main.class

# ── 1. Generate Lexer.java from Lexer.jflex ────────────────────────────
Lexer.java: Lexer.jflex
	java -cp "$(JFLEX_JAR);$(CUP_JAR)" jflex.Main -d . -pack Lexer.jflex

# ── 2. Generate parser.java + sym.java from parser.cup ─────────────────
parser.java sym.java: parser.cup
	java -cp "$(CUP_JAR)" java_cup.Main -expect 4 -interface -destdir . parser.cup

# ── 3. Compile everything ───────────────────────────────────────────────
$(BUILD)/Main.class: Lexer.java parser.java sym.java Main.java | $(BUILD)
	javac -cp $(CUP_JAR) -d $(BUILD) \
	    sym.java \
	    Lexer.java \
	    parser.java \
	    Main.java

$(BUILD):
	mkdir -p $(BUILD)

clean:
	rm -f Lexer.java parser.java sym.java
	rm -rf $(BUILD)

.PHONY: all clean
