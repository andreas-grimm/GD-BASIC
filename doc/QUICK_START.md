# GD-BASIC: Quick Start Guide

**Version:** 0.4.0 (branch `feature/compiler`, not yet merged) · **Checked:** 2026-11-23

This guide gets you from nothing to a running BASIC program in about 15 minutes. Every example here was run on
2026-11-23 and the output shown is the real output. If something in this guide does not match your screen, check
`doc/KNOWN_ISSUES.md` first.

**New to programming?** Read `doc/GLOSSARY.md` alongside this page. It explains words such as *interpreter*,
*variable* and *suffix*.

---

## 1. What you need

| Tool | Version | Check with |
|---|---|---|
| Java **JDK** (not only a JRE) | 21 | `java -version` and `javac -version` |
| Maven | 3.6.3 or newer | `mvn -version` |

A JDK includes the Java compiler `javac`. You need it for the `COMPILE` command (section 9). Running a program does not
need it.

---

## 2. Build the interpreter

From the project folder:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home   # macOS path; adjust for your system
mvn clean test package
```

What happens:

1. `clean` deletes old build output in `target/`.
2. `test` runs all unit tests. The last run had **1560 tests, 0 failures**.
3. `package` builds the jar.

The file you need is:

```text
target/BASIC-0.4.0-jar-with-dependencies.jar
```

To check that the jar works:

```bash
java -jar target/BASIC-0.4.0-jar-with-dependencies.jar -h
```

---

## 3. Your first program

### 3.1 In the line editor

Start the interpreter with no file. You get a prompt (`>`). Type line numbers and statements:

```text
$ java -jar target/BASIC-0.4.0-jar-with-dependencies.jar
Starting line editor
Welcome
>10 PRINT "Hello, World!"
>20 END
>LIST
10 PRINT "Hello, World!"
20 END
>RUN
Hello, World!
>EXIT
Good bye.
```

(The lines you type are shown after each `>` prompt. The interpreter prints its own text; your screen may differ a little.)

- A line that starts with a number is stored as part of the program.
- `LIST` shows the program, `RUN` runs it, `EXIT` leaves the editor.

### 3.2 As a file

Save this as `hello.bas`:

```basic
10 PRINT "Hello, World!"
20 END
```

Run it directly (`-r` means "run now, without the editor"):

```bash
java -jar target/BASIC-0.4.0-jar-with-dependencies.jar -r hello.bas
```

Output:

```text
Hello, World!
```

Every program must end with `END`.

---

## 4. The line editor commands

These are the commands the editor accepts (checked in `lineeditor/LineEditor.java` on 2026-11-23):

| Command | What it does |
|---|---|
| `LIST` | Shows the program. |
| `RUN` | Runs the program. |
| `LOAD <file>` | Loads a `.bas` file. |
| `SAVE <file>` | Saves the program to a new file. It does not overwrite an existing file. |
| `DELETE <line>` or `DELETE <start> <end>` | Deletes one line, or a range of lines. |
| `HELP` | Shows the built-in help. |
| `COMPILE <name>` | Writes `<name>.java` and builds `<name>.jar` from the loaded program. Without a name it uses the `-o` option (section 9). |
| `SAVEJAVA [file.java]` | Writes the Java source for the program. |
| `EXIT`, `QUIT`, `BYE` | Leave the editor. |

There is **no** `NEW` command. To start again, use `DELETE` with the full range, or start the interpreter again.

---

## 5. Variables and types

A variable's type is shown by its last character (its *suffix*). Every example below was run on 2026-11-23.

```basic
10 X = 3.14
20 I% = 42
30 S$ = "Hello"
40 B! = TRUE
50 A# = 0.1
60 PRINT X
70 PRINT I%
80 PRINT S$
90 PRINT B!
100 PRINT A# + 0.2
110 END
```

Output:

```text
3.14
42
Hello
True
0.3
```

| Ending | Type | Example | Notes |
|---|---|---|---|
| none | Real (binary floating point) | `X = 3.14` | `0.1 + 0.2` shows `0.30000000000000004`. |
| `%` | Integer | `I% = 42` | Whole numbers. |
| `&` | Long | `N& = 1000000` | Whole numbers. **Known problem:** values above 2147483647 are not stored correctly (KI-16). |
| `$` | String | `S$ = "Hello"` | Text in double quotes. |
| `!` | Boolean | `B! = TRUE` | Only `TRUE` or `FALSE`. |
| `#` | Decimal (exact) | `A# = 0.1` | Up to 40 significant digits. Use this for money. |

**Careful:** the suffix `@` is no longer valid (`F@ = TRUE` gives an error that tells you to use `!`). Older
programs need `@` changed to `!`.

For exact decimal arithmetic, use `#`. Put the number in a decimal variable first (a number written with `#`
directly after it is refused):

```basic
10 A# = 1
20 PRINT A# / 3
30 END
```

Output: `0.3333333333333333333333333333333333333333` (40 digits).

---

## 6. Loops and decisions

### 6.1 A counting loop

```basic
10 FOR I = 1 TO 3
20   PRINT I
30 NEXT
40 END
```

Output: `1.0`, `2.0`, `3.0`. Write `NEXT` on its own; `NEXT I` is refused.

### 6.2 A decision

```basic
10 INPUT AGE
20 IF AGE >= 18 THEN
30   PRINT "Adult"
40 ELSE
50   PRINT "Minor"
60 END-IF
70 END
```

Run it with `20` typed in: the output is `Adult`. With `12`: `Minor`.

Notes:

- The block ends with `END-IF` (with a hyphen).
- `INPUT` reads one value and stores it in a variable. It does not take a prompt text: `INPUT "Age: ", AGE` is
  refused.
- Compare values with `==`, `<`, `<=`, `>`, `>=` or `!=`. A single `=` means "store a value", so
  `IF A = 1 THEN` is refused. Use `IF A == 1 THEN`.

### 6.3 A loop with a condition

```basic
10 I = 0
20 WHILE I < 2
30   I = I + 1
40   PRINT I
50 WEND
60 END
```

Output: `1.0`, `2.0`.

---

## 7. Built-in functions

These were run on 2026-11-23:

```basic
10 PRINT SIN(3.14159)
20 PRINT LEN("Hello")
30 PRINT CINT(3.7)
40 PRINT RND
50 END
```

Output (the `RND` value changes every run):

```text
2.65358979335273E-6
5
3
0.39090636469182416
```

- `CINT` removes the fraction (it truncates): `CINT(3.7)` gives `3`.
- `RND` takes **no** brackets.
- The full list is in `doc/BASIC.md`, section "Reserved Words", with a status for each function.

---

## 8. Run modes (command-line options)

```bash
java -jar BASIC-0.4.0-jar-with-dependencies.jar                 # line editor
java -jar BASIC-0.4.0-jar-with-dependencies.jar prog.bas        # load prog.bas into the editor
java -jar BASIC-0.4.0-jar-with-dependencies.jar -r prog.bas     # run now, no editor
java -jar BASIC-0.4.0-jar-with-dependencies.jar -d prog.bas     # Dartmouth evaluation order
java -jar BASIC-0.4.0-jar-with-dependencies.jar -q -r prog.bas  # quiet: no splash screen (log lines still show)
java -jar BASIC-0.4.0-jar-with-dependencies.jar -v debug -r prog.bas   # more log output
java -jar BASIC-0.4.0-jar-with-dependencies.jar -h              # help
```

The option `-o` sets the default build target for compiling (section 9). The complete list is in `doc/USER_GUIDE.md`.

---

## 9. Compile a program to a jar

The compiler turns your `.bas` program into Java source and then into a jar that runs on its own. You need a **JDK**.

```bash
java -jar target/BASIC-0.4.0-jar-with-dependencies.jar
```

Then, in the editor:

```text
>LOAD hello.bas
Program loaded from hello.bas
>COMPILE hello
Java source written to hello.java
Build target created: hello.jar (start it with: java -jar hello.jar)
>EXIT
Good bye.
```

- `COMPILE hello` writes `hello.java` (the Java source) and `hello.jar` (the runnable jar).
- `COMPILE hello.jar` gives the same two files.

Run the compiled program:

```bash
java -jar hello.jar
```

`SAVEJAVA` writes only the Java source (no compile). `COMPILE` with no name and no `-o` prints a message and writes no file.

Known limits of the compiler (arrays, `DATA`/`READ`, labels) are in `doc/COMPILER.md`, section 5.3.

---

## 10. When something goes wrong

| What you see | What it means | Where to look |
|---|---|---|
| `Incorrect Command: 10: [LET]` | `LET` is not accepted in this version. Write `A = 4`. | `doc/KNOWN_ISSUES.md`, KI-08 |
| `Found: = expected THEN.` | You used `=` to compare. Use `==`. | `doc/KNOWN_ISSUES.md`, D1 |
| `Incorrect Command: 40: [EXIT]` | `EXIT` does not work yet. | `doc/KNOWN_ISSUES.md`, KI-06 |
| `Missing statement END-IF` | A block `IF` has no `END-IF`. | `doc/BASIC.md`, section IF |
| `EmptyStackException: Stack Empty` after `WEND` | A `WEND` has no `WHILE` before it. | `doc/KNOWN_ISSUES.md`, KI-11 |

For more messages and fixes, see `doc/USER_GUIDE.md` (troubleshooting). To see every step of the interpreter, run with
`-v debug`.

---

## 11. Developer quick commands

```bash
mvn clean test package                      # build, all unit tests, jar
mvn test -Dtest=BasicParserTest             # one test class
test/system/run_all_tests.sh                # all system programs
mvn -o checkstyle:check -Dcheckstyle.config.location=$PWD/etc/checkstyle-config.xml   # style check
```

Before you change code, read `prompts/STYLEGUIDE.md` (the coding rules) and `doc/ARCHITECTURE.md` (how the parts fit
together).

---

## 12. Where to read next

| I want to... | Read |
|---|---|
| Learn the words of the project | `doc/GLOSSARY.md` |
| Use the interpreter day to day | `doc/USER_GUIDE.md` |
| Look up a statement or function | `doc/BASIC.md` |
| Understand the compiler | `doc/COMPILER.md` |
| Understand the code structure | `doc/ARCHITECTURE.md` and `doc/GD-BASIC_Detailed_Design.md` |
| See what is broken | `doc/KNOWN_ISSUES.md` |
| See what changed | `CHANGELOG.md` |
| Find any document | `doc/INDEX.md` |

---

## 13. Corrections to earlier versions of this guide

The earlier version of this guide contained errors, which were removed on 2026-11-23:

- It said GD-BASIC "supports multiple BASIC dialects (Dartmouth, TRS-80, PET, Apple)". Only Dartmouth evaluation (`-d`)
  exists. The dialect packages are empty (see `doc/KNOWN_ISSUES.md`, KI-12).
- Its examples used `LET`, `B@`, `END IF`, `INT(3.7)` and `RND()`. None of these works in 0.4.0 as written.
- It listed a `NEW` command and example folders (`examples/`, `scripts/`) that do not exist in the repository.
- It gave the test count as 1435 and the status as "Production Ready". The current run is 1560 unit tests and 109 system
  program runs. The status is "in development", because the known issues in `doc/KNOWN_ISSUES.md` are still open.
