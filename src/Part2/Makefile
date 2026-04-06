# Makefile for Part 2

# Directories
SRC_DIR = src
LIB_DIR = lib
BUILD_DIR = ../../

# JARs
JFLEX_JAR = $(LIB_DIR)/jflex-full-1.9.1.jar
CUP_JAR   = $(LIB_DIR)/java-cup-11b.jar
CUP_RT    = $(LIB_DIR)/java-cup-11b-runtime.jar

# Classpath separator: semicolon on Windows, colon on Unix
ifeq ($(OS),Windows_NT)
  SEP = ;
else
  SEP = :
endif

# Classpath: runtime jar + build output dir (so sym.class is found)
CP = $(CUP_RT)$(SEP)$(BUILD_DIR)

# Targets
all: parser lexer compile

# --- Parser generation ---
# Use -destdir to put parser.java and sym.java directly into src/
parser: $(SRC_DIR)/parser.java $(SRC_DIR)/sym.java

$(SRC_DIR)/parser.java $(SRC_DIR)/sym.java: $(SRC_DIR)/parser.cup
	java -jar $(CUP_JAR) -expect 2 -destdir $(SRC_DIR) -parser parser -symbols sym $<

# --- Lexer generation ---
lexer: $(SRC_DIR)/Lexer.java

$(SRC_DIR)/Lexer.java: $(SRC_DIR)/lexer.flex $(SRC_DIR)/sym.java
	java -jar $(JFLEX_JAR) --outdir $(SRC_DIR) $<

# --- Compilation ---
# Step 1: compile sym.java alone first (no dependencies on other generated files)
# Step 2: compile everything else with sym.class already on the classpath
compile: $(SRC_DIR)/Lexer.java $(SRC_DIR)/parser.java $(SRC_DIR)/sym.java
	javac -cp "$(CP)" -d "$(BUILD_DIR)" "$(SRC_DIR)/sym.java"
	javac -cp "$(CP)" -d "$(BUILD_DIR)" "$(SRC_DIR)/Lexer.java" "$(SRC_DIR)/parser.java" "$(SRC_DIR)/MyMain.java"

clean:
	rm -f $(SRC_DIR)/Lexer.java $(SRC_DIR)/parser.java $(SRC_DIR)/sym.java
	rm -rf $(BUILD_DIR)/Part2

.PHONY: all parser lexer compile clean
