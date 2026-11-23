# GD-BASIC: Glossary

**Version:** 0.4.0 (branch `feature/compiler`)

This glossary explains the words used in the GD-BASIC documents. Each entry is written for a reader who is new to
programming, to BASIC, or to this project. Terms are listed in alphabetical order inside each group.

Where a term is used by the Java code, the class name is given so you can find it.

---

## 1. The BASIC language

**Array**
: A group of values with one name and a number (the index) for each value. In GD-BASIC you write `A(3)` to read
  the third value. No `DIM` statement is needed. Arrays with the decimal suffix `#` are not supported yet.

**BASIC**
: *Beginner's All-purpose Symbolic Instruction Code*. A simple programming language from 1964 that uses line numbers
  (`10`, `20`, `30` ...). GD-BASIC is a BASIC that runs on Java. See `doc/BASIC.md`.

**Boolean**
: A value that is either `TRUE` or `FALSE`. In GD-BASIC a boolean variable has the suffix `!` (for example `FLAG!`).

**Comment**
: Text in a program that the computer ignores. Write it after `REM`, for example `10 REM this is a comment`.

**Decimal (BCD)**
: A number with an exact decimal point, such as `0.1`, that does not have the rounding errors of a binary number.
  BCD means *Binary Coded Decimal*. In GD-BASIC a decimal variable has the suffix `#` and holds up to 40
  significant digits. See `doc/BASIC.md`, "Decimal Numbers (BCD)".

**Expression**
: A part of a statement that has a value, for example `A + 2 * B`. An expression can be a number, a variable, a
  function call, or a calculation made from these.

**Function**
: A built-in operation that gives back a value, for example `SQR(16)` gives `4`. Functions are written in capital
  letters in the documents. See `doc/BASIC.md`, section "Built-in Functions".

**Integer**
: A whole number, with no fraction. In GD-BASIC an integer variable has the suffix `%` (for example `COUNT%`).

**Line number**
: The number at the start of each program line. The interpreter runs the lines in number order. Use steps of 10
  (`10`, `20`, `30`) so you can add lines later.

**Long**
: A large whole number. In GD-BASIC a long variable has the suffix `&` (for example `TOTAL&`).

**Macro (`DEF`)**
: A short name for a formula. `DEF FNA(X) = "X * 2"` defines a macro, and `FNA(3)` then gives `6`. The macro
  processor replaces each use of the name before the program runs. See `doc/BASIC.md`, "`DEF` Command".

**Pragma (`@PRAGMA`)**
: An instruction for the interpreter itself, not for the program. For example `@PRAGMA( "log" = "info" )` changes
  how much logging is shown. The brackets and quotes are required.

**Real**
: A number that can have a fraction, such as `3.14`. A variable with no suffix is REAL, for example `X = 2.5`.
  REAL numbers use the binary `double` type, so `0.1 + 0.2` shows `0.30000000000000004`. Use `#` when exact
  decimals matter.

**Statement**
: One instruction, such as `PRINT "Hello"` or `GOTO 100`. A line can hold several statements separated by a colon
  (`10 A = 1 : PRINT A`).

**String**
: Text, written inside double quotes, for example `"Hello"`. A string variable has the suffix `$` (for example
  `NAME$`).

**Suffix**
: A character at the end of a variable name that tells the interpreter the type of the value. `%` is integer, `&`
  is long, `$` is string, `#` is decimal, `!` is boolean, and no suffix is real.

**Variable**
: A named box that holds a value. `A = 5` puts `5` into the variable `A`.

---

## 2. Control flow (how a program moves from line to line)

**`DO ... UNTIL`**
: A loop that runs its body at least once and stops when the condition after `UNTIL` is `TRUE`.

**`FOR ... NEXT`**
: A loop that counts a variable from a start value to an end value, for example `FOR I = 1 TO 10` ... `NEXT`.
  `STEP` sets the size of each count. In GD-BASIC the `NEXT` line takes no variable name.

**`GOSUB` / `RETURN`**
: Jump to a part of the program and come back afterwards. Useful for a piece of code used in several places.

**`GOTO`**
: Jump to a line number. Using many `GOTO` statements makes a program hard to read.

**`IF ... THEN ... ELSE ... END-IF`**
: Run one block of statements if a condition is true, and another block if it is false.

**`ON ... GOTO` / `ON ... GOSUB`**
: Choose a line to jump to from a list, by a number: `ON X GOTO 100, 200` jumps to 100 when `X` is 1 and to 200
  when `X` is 2.

**`WHILE ... END-WHILE` (or `WEND`)**
: A loop that runs while a condition is `TRUE`. The condition is checked before each run of the body.

---

## 3. The interpreter and the compiler

**Compiler**
: A program that turns a BASIC program into a Java program, and then builds a runnable jar file from it. See
  `doc/COMPILER.md`.

**Interpreter**
: A program that reads your BASIC program and runs it at once, line by line. GD-BASIC is an interpreter. Start it
  with `java -jar BASIC-0.4.0-jar-with-dependencies.jar`.

**Jar file**
: A single file that holds many Java classes. You start it with `java -jar name.jar`. The file
  `BASIC-0.4.0-jar-with-dependencies.jar` contains the whole interpreter.

**JDK / JRE**
: The *Java Development Kit* (JDK) includes the compiler `javac`. The *Java Runtime Environment* (JRE) only runs
  Java programs. The `COMPILE` command needs a JDK.

**Line editor**
: The interactive mode of GD-BASIC. You type line numbers and statements, and commands such as `LIST`, `RUN`,
  `LOAD` and `SAVE`. Code: `src/main/java/eu/gricom/basic/lineeditor/`.

**Direct execution (`-r`)**
: Run a program file at once, without the line editor.

**Dartmouth mode (`-d`)**
: Evaluate expressions strictly from left to right, as the original Dartmouth BASIC did. Normal mode follows the usual
  rules of precedence (multiplication before addition).

**Working directory**
: The folder that a program is "in" for file names. Relative paths such as `data.txt` start from here. `GETCWD()`
  gives this folder as an absolute path. `CHDIR` changes it.

---

## 4. Program building (for developers)

**Absolute path**
: A complete file path that starts at the top of the disk, for example `/Users/name/project/data.txt`. A relative
  path (`data.txt`) depends on where the program is started.

**Checkstyle**
: A tool that checks Java source code against style rules (for example line length and naming). Its rules are in
  `etc/checkstyle-config.xml`, and they follow `prompts/STYLEGUIDE.md`.

**Hungarian notation**
: A naming system where the first letters of a name show its type. For example `strName` is a `String`, `iCount`
  is an `int`, and `astrNames` is an array of strings. The rules are in `prompts/STYLEGUIDE.md`.

**JUnit**
: The test library used for unit tests. A unit test checks one small piece of Java code, for example one method.

**Lexer (`BasicLexer`)**
: The part that splits the text of a program into *tokens* (words, numbers, operators). See
  `doc/ARCHITECTURE.md`.

**Maven**
: The build tool. `mvn clean test package` deletes old output, runs all tests, and builds the jar.

**Mermaid**
: A text format for drawing diagrams (flowcharts, sequence diagrams). The diagrams in the documents are written in
  Mermaid, so they can be changed as plain text.

**Parser (`BasicParser`)**
: The part that reads the tokens and builds *statement* and *expression* objects, following the rules of the
  language.

**Regression test**
: A test that checks a bug, once fixed, does not come back. Every fixed bug should get one.

**System test**
: A BASIC program in `test/system/` that prints `PASSED` when its own checks succeed. The script
  `test/system/run_all_tests.sh` runs them all. See `test/system/SYSTEM_TEST_GUIDE.md`.

**Unit test**
: A Java test in `src/test/java/`. Positive tests check that correct input gives the right result. Negative tests
  check that wrong input is refused.

**Known issue**
: A problem that is known but not yet fixed. See `doc/KNOWN_ISSUES.md`.

---

## 5. Words used in these documents

**Depreciated / deprecated**
: *Deprecated* means "still there, but do not use it". The documents use the spelling *depreciated* in some tables,
  which is a typo for *deprecated*.

**Implemented / planned / reserved**
: The status of a keyword in the table in `doc/BASIC.md`. *Implemented*: works now. *Planned*: intended, not
  working yet. *Reserved*: the word cannot be used as a variable name, but may have no meaning yet.

**Status date**
: The date on which a document was checked against the code. Dates are written as `YYYY-MM-DD`.
