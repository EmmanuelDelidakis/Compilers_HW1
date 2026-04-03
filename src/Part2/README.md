# String-Language → Java Translator

A **JFlex + JavaCUP** based parser/translator that compiles a small
string-manipulation language into valid Java source code.

---

## Language Reference

### Supported constructs

| Construct | Syntax |
|-----------|--------|
| String literal | `"hello"` |
| Concatenation | `expr + expr` |
| Prefix test | `expr1 prefix expr2` |
| Suffix test | `expr1 suffix expr2` |
| Conditional | `if (cond) expr else expr` |
| Function definition | `def name(p1, p2, ...) : stmts def` |
| Function call | `name(arg1, arg2, ...)` |
| Statement | `expr;` |

**Precedence** (low → high): `if/else` < `prefix/suffix` < `+`

### Rules
- All values are strings.
- Every `if` must have an `else` branch.
- Function declarations **precede** all statements.
- The last expression of a function body is its return value.

### Example input

```
def greet(name) :
    "Hello, " + name;
def

greet("World");
if ("he" prefix "hello") "yes" else "no";
```

### Example output

```java
public class Main {
    public static boolean isPrefix(String a, String b) {
        return b.startsWith(a);
    }
    public static boolean isSuffix(String a, String b) {
        return b.endsWith(a);
    }
    public static String greet(String name) {
        return ("Hello, " + name);
    }
    public static void main(String[] args) {
        System.out.println(greet("World"));
        System.out.println((isPrefix("he", "hello") ? "yes" : "no"));
    }
}
```

---

## Project Structure

```
strparser/
├── Makefile
├── README.md
├── lib/                         ← place JAR files here (see below)
│   ├── java-cup-11b.jar
│   └── jflex-full-1.9.1.jar
└── src/
    ├── Lexer.jflex              ← JFlex lexer spec
    ├── parser.cup               ← JavaCUP grammar + translation actions
    └── Main.java                ← driver (reads stdin, prints Java to stdout)
```

---

## Prerequisites

### 1 — Java

Any JDK ≥ 8.

```bash
java -version   # verify
```

### 2 — JFlex

Download **jflex-full-1.9.1.jar** (or newer) from
<https://jflex.de/downloading.html> and place it in `lib/`.

```bash
mkdir -p lib
curl -L https://github.com/jflex-de/jflex/releases/download/v1.9.1/jflex-full-1.9.1.jar \
     -o lib/jflex-full-1.9.1.jar
```

### 3 — JavaCUP

Download **java-cup-11b.jar** from
<http://www2.cs.tum.edu/projects/cup/> and place it in `lib/`.

```bash
curl -L https://search.maven.org/remotecontent?filepath=com/github/vbmacher/java-cup/11b/java-cup-11b.jar \
     -o lib/java-cup-11b.jar
```

> **VS Code users**: install the *Extension Pack for Java* so IntelliSense
> resolves the generated sources. Point `java.project.sourcePaths` to `src/`
> and `build/` in your workspace settings if needed.

---

## Build

```bash
make          # generates Lexer.java, parser.java, sym.java, then compiles
```

Individual steps (if you prefer not to use make):

```bash
# 1. Generate lexer
java -jar lib/jflex-full-1.9.1.jar -d src src/Lexer.jflex

# 2. Generate parser
java -jar lib/java-cup-11b.jar -interface -destdir src src/parser.cup

# 3. Compile
mkdir -p build
javac -cp lib/java-cup-11b.jar -d build \
      src/sym.java src/Lexer.java src/parser.java src/Main.java
```

---

## Run

```bash
# Translate a program (read from stdin, Java source to stdout)
echo 'greet("world");' | java -cp lib/java-cup-11b.jar:build Main

# Or from a file
java -cp lib/java-cup-11b.jar:build Main < examples/example1.str

# Pipe directly into javac to compile & run in one go
java -cp lib/java-cup-11b.jar:build Main < examples/example1.str > Main.java
javac Main.java
java Main
```

> **Windows**: replace `:` with `;` in `-cp` paths.

---

## Example Programs

### example1.str — concatenation and function

```
def repeat(s) :
    s + s;
def

repeat("ha");
```

Expected output after `java Main`:
```
haha
```

### example2.str — prefix / suffix conditionals

```
if ("foo" prefix "foobar") "yes" else "no";
if ("baz" suffix "foobar") "nope" else "yep";
```

Expected output:
```
yes
yep
```

### example3.str — nested if-else

```
def check(a, b) :
    if (a prefix b) "prefix-match" else if (a suffix b) "suffix-match" else "no-match";
def

check("he", "hello");
check("lo", "hello");
check("xy", "hello");
```

Expected output:
```
prefix-match
suffix-match
no-match
```

---

## Clean

```bash
make clean
```

---

## Design Notes

* **Grammar ambiguity**: the dangling-else problem is resolved by giving
  `IF`/`ELSE` the lowest precedence (`precedence right IF, ELSE`), so each
  `else` binds to the nearest `if`.
* **Function body return**: the last statement's expression is automatically
  wrapped in `return …;` instead of `System.out.println(…);`.
* **Helper methods**: `isPrefix` / `isSuffix` are emitted once at the top of
  the class and delegate to `String.startsWith` / `String.endsWith`.
* **No type checking** is performed, matching the assignment specification.
