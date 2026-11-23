# GD-BASIC Compiler

This document explains the BASIC-to-Java compiler that is part of GD-BASIC 0.4.0 (branch `feature/compiler`).
It is written for a developer who is new to the project.

## 1. What the compiler does

The compiler takes a BASIC program (a `.bas` file) and writes an equivalent **Java source file** (`.java`).
It then compiles that Java file with the Java compiler that comes with the JDK, and packs the result into a
**runnable jar file**. The jar can be started on any Java 21 system with:

```bash
java -jar hello.jar
```

The compiled program prints exactly what the interpreter prints for the same BASIC program.
This is checked automatically by the system tests (section 7).

## 2. Re-use of the interpreter

The compiler re-uses the interpreter as much as possible, so that the two cannot drift apart:

| Part | Re-used from the interpreter | Why |
|---|---|---|
| Tokenizer and parser | `BasicLexer`, `BasicParser` | The compiler starts from the same parsed statements the interpreter runs. |
| Variables | `VariableManagement` | Type rules (for example `A%` is an integer, `A#` is decimal) are exactly the same. |
| Values and arithmetic | `RealValue`, `IntegerValue`, `StringValue`, `BcdValue`, ... and their `plus`, `multiply`, ... methods | The same maths and the same error messages. |
| Built-in functions | The static `execute` methods in `functions/` (for example `Sqr.execute`) | The same results. |
| Unary minus and NOT | `UnaryOperatorExpression.evaluateUnaryMinus` and `evaluateUnaryNot` | Shared static methods. |
| Shift operators | `OperatorExpression.integerShiftLeft` and `integerShiftRight` | Shared static methods. |
| PRAGMA | `PragmaStatement` | The compiled program changes the log level in the same way. |
| Stack for GOSUB, FOR, WHILE, DO | `Stack` | The same last-in, first-out behaviour. |

The generated code is therefore a **translation** of the interpreter's statement logic into Java source,
using the interpreter's own building blocks.

## 3. How to use it

### 3.1 Command-line option

| Option | Meaning | Example |
|---|---|---|
| `-o <target-name>` | The default build target, used by `COMPILE` and `SAVEJAVA` when no name is typed. | `-o hello` |

The `-j` option was removed in version 0.4.0. The Java file name now comes from the name typed after `COMPILE`.

### 3.2 Editor commands

| Command | What it does |
|---|---|
| `COMPILE <name>` | Writes `<name>.java`, compiles it, and builds `<name>.jar`. `COMPILE <name>.jar` gives the same two files. Without a name, the `-o` target is used. |
| `SAVEJAVA [file.java]` | Writes the generated Java source only. Nothing is compiled. Without a file name it writes `<target>.java` when `-o` is given, and `BasicProgram.java` when it is not. |

Example session:

```text
>LOAD hello.bas
>COMPILE hello
Java source written to hello.java
Build target created: hello.jar (start it with: java -jar hello.jar)
>EXIT
```

The commands can also be piped in, which is how the system tests use them:

```bash
printf 'LOAD hello.bas\nCOMPILE hello\nEXIT\n' | java -jar BASIC-0.4.0-jar-with-dependencies.jar
```



| Message | Cause |
|---|---|
| `COMPILE: give a target name, for example COMPILE hello (or start with -o hello).` | `COMPILE` was used with no name and without `-o`. No file is written. |
| `No program loaded. Use LOAD command or enter program lines.` | There is no program in memory. |
| `The Java file name is not a valid class name: my-prog.java` | The file name contains a character that is not allowed in a Java class name (only letters, digits and `_`, and it must not start with a digit). |
| `Statement not supported by the compiler yet (BASIC line 10): Read` | The program uses a statement that the compiler does not support yet (section 5.3). |
| `No Java compiler found. The compiler needs a JDK (not only a JRE).` | The program is running on a Java runtime (JRE) without the compiler. Use a JDK. |

## 4. How it works, step by step

```text
.bas file
   |  LOAD (existing)
   v
BasicLexer  ->  BasicParser  ->  list of Statement objects      (existing interpreter code)
   |
   v
JavaCodeGenerator  ->  Java source text  ->  file written with -j
   |
   v
JavaSourceCompiler (javac, built into the JDK)  ->  .class files
   |
   v
JarPackager  ->  <target>.jar  (the compiled class + the GD-BASIC runtime classes)
```

Classes in the new package `eu.gricom.basic.compiler`:

| Class | Job |
|---|---|
| `BasicCompiler` | The entry point. Runs the steps above in order. |
| `JavaCodeGenerator` | Writes the Java source. This is the main part of the compiler. |
| `JavaSourceCompiler` | Runs `javac` on the generated file. |
| `JarPackager` | Builds the jar. It copies the GD-BASIC runtime into the jar, so the jar runs on its own. |
| `CompileException` | The error that is reported to the user when something is not supported. |

### 4.1 Units: the flat list of steps

A BASIC program is a list of statements. A block IF (`IF ... THEN ... ELSE ... END-IF`) keeps its statements inside
its own object. The generator first makes one flat list of **units**:

- each ordinary statement is one unit;
- a block IF becomes: the test unit, then the THEN statements, then (only if there is an ELSE) a jump unit,
  then the ELSE statements.

### 4.2 The switch

The generated class contains a `run` loop and a `switch` with one `case` per unit. The variable `iPc` holds the
number of the unit to run next. Each case sets `iPc` to the next unit, which is the same idea as the interpreter's
program pointer. A jump (GOTO, GOSUB, a failed test) just sets `iPc` to another unit.

Long programs are split into several methods (`part0`, `part1`, ...) of 100 units each, because Java does not allow
one method to be larger than 64 KB.

### 4.3 Example: what a PRINT becomes

BASIC:

```basic
20 PRINT "A = "; A%
```

Java (simplified):

```java
case 1: {
    // BASIC line 20
    System.out.print(new StringValue("A = "));
    System.out.print(var("A%"));
    System.out.println();
    iPc = 2;
    break;
}
```

### 4.4 Example: what a FOR loop becomes

The FOR logic is copied from the interpreter's `ForStatement`, including the rounding of the counter to two decimal
places. The loop keeps a flag per FOR statement (`_forStarted[n]`). The end of the loop is the unit after the
matching NEXT line.

## 5. What is supported

### 5.1 Supported statements

| Statement | Notes |
|---|---|
| `REM`, `'` comments | No code is generated. |
| `:` separator | No code is generated. |
| Assignment (`A = 1`, `LET`) | Scalar variables only. |
| `PRINT` | All expressions, `;` and `,` separators, and the new line. A comma adds no space: `PRINT 1,2` prints `1.02.0` (KI-17, confirmed 2026-11-22). |
| `INPUT <variable>` | Reads a line from standard input. Uses the interpreter's own statement class. |
| `END` | Stops the program. |
| `GOTO <line>` | Line numbers only. |
| `GOSUB <line>` / `RETURN` | |
| `IF cond THEN <line>` | Single-line form with a line number. |
| Block `IF ... THEN ... [ELSE ...] END-IF` | Including nested block IFs. |
| `FOR ... NEXT` | Including `STEP`, and nested loops. |
| `WHILE ... END-WHILE` | |
| `DO ... UNTIL` | |
| `ON n GOTO` / `ON n GOSUB` | |
| `@PRAGMA( "name" = "value" )` | Same syntax as the interpreter (see `doc/BASIC.md`). |
| File statements | `FOPEN`, `FCLOSE`, `FPRINT`, `FINPUT`, `FGET`, `FPEEK`, `FPUT`, `FREWIND`, `FRENAME`, `FCOPY`, `FDELETE`. |
| Directory statements | `CHDIR`, `MKDIR`, `RMDIR`. |

The file and directory statements create the interpreter's own statement object with the same constant values, and call its `execute()` method. So their behaviour, including their error messages, is the same as in the interpreter.

### 5.2 Supported expressions

- Numbers (real, integer `%`, long `&`, decimal `#`), strings `$`, booleans `!`.
- Operators: `+`, `-`, `*`, `/`, `^`, `%` (modulo), `=`, `==`, `!=`, `<`, `<=`, `>`, `>=`, `AND`, `OR`, `NOT`,
  unary `-` and `+`, `<<` and `>>` (integer shifts). Operators written without spaces (`A-B`, `2*A`, `X=-A`) are read
  the same way as in the interpreter (see `doc/BASIC.md`, "Unary Minus and Operators Without Spaces").
- Unary minus calls the interpreter's `UnaryOperatorExpression.evaluateUnaryMinus`. A string gives the same error.
- Functions: `ABS`, `ASC`, `ATN`, `CDBL`, `CHR$`, `CINT`, `COS`, `EXP`, `INSTR`, `LEFT$`, `LEN`, `LOG`, `LOG10`,
  `LOWER$`, `MEM`, `MID$`, `RIGHT$`, `RND`, `SIN`, `SQR`, `STR$`, `TAN`, `TIME`, `UPPER$`, `VAL`.
- File functions: `EOF`, `FCOMPARE`, `FEXISTS`, `FGETNAME`, `FGETSIZE`, `FISOPEN`, `FLINECOUNT`, `FMODTIME`.
- Directory functions and system: `DIREXISTS`, `GETCWD`, `SYSTEM`.

### 5.3 Not supported yet

The compiler refuses these with a message that names the line and the statement.

| Feature | Programs that use it (examples) | Why it is not done yet |
|---|---|---|
| Arrays (`DIM`, `A(1) = ...`) | `test_arrays_dim`, `test_mixed_tests_1` | The interpreter builds array keys at run time with its own rules. |
| `DATA` / `READ` | `test_data_read`, `test_mixed_tests_3` | Data is collected before the program starts. |
| Labels in `GOTO` / `IF` | none | Only line numbers are supported. |

## 6. Known differences and findings

- **`TIME` is the name, not `TIME$`.** `PRINT TIME` gives a number of milliseconds. `TIME$` is not a valid name:
  `PRINT TIME$` stops with `Unknown variable <TIME$>` (checked 2026-11-23 in the interpreter).
- **LONG values.** The interpreter clamps `&` values above 2147483647 (KI-16). The compiled program was not checked for
  this limit on 2026-11-23.

- **Random and machine values.** `RND`, `MEM` and `TIME` give different values in every run, so their output
  can never match exactly. The test script leaves these lines out of the comparison.
- **FOR with a start value above the end value** runs its body once. This is how the interpreter works, and the
  compiler copies it. It is recorded in the unit test `testForLoopWithStartAboveEndRunsOnce`.
- **Unary minus** now works in the interpreter and in the compiled program: `PRINT -A` prints the value of A times -1,
  with the type of A kept. A string is refused. (An earlier version of the interpreter read `-A` as one name.)
- **File and directory errors stop the program.** Some file and directory statements (for example `MKDIR` of an
  existing directory, or `FGET` at the wrong time) log the error and end the program with exit code -1. This is the
  interpreter's design, and the compiled program does the same. Because of this, such errors are tested with a
  process-level check in `run_all_tests.sh`, not with unit tests.
- **`GETCWD()`** returns the working directory as an absolute path. It starts as the directory in which the program was started, and `CHDIR` changes it (the same rules as the interpreter).
- **Huge programs** are supported. `test_large_program` (more than 10,000 lines) compiles and matches the
  interpreter, because of the split into parts (section 4.2).
- **Speed is not measured yet.** Variables still use the interpreter's `VariableManagement` class, so the
  compiled program does not gain much speed from the variable handling. A later version could use typed Java fields.

## 7. Tests

### 7.1 Unit tests

- `src/test/java/eu/gricom/basic/compiler/JavaCodeGeneratorTest.java`: positive tests compile a small program, run it,
  and compare the printed text with the expected output. Negative tests check that unsupported statements (for example
  `READ`, arrays, `GOTO` to a missing line) are refused with a clear message. Further tests cover unary minus, INPUT
  (with standard input), SYSTEM, FEXISTS, file writing and reading, directories, and file IDs written as literals.
  Each test resets the file manager's current directory first, because that setting is kept in a static field.
- `src/test/java/eu/gricom/basic/tokenizer/OperatorSpacerTest.java`: spacing around operators, the tokens the lexer
  produces, and text that must not change (strings, comments, `END-IF`).
- `src/test/java/eu/gricom/basic/statements/UnaryMinusTest.java`: the type rules of unary minus, and the string refusal.
- `src/test/java/eu/gricom/basic/parser/FileDirectoryAndPragmaParserTest.java`: `MKDIR`, `RMDIR`, `@PRAGMA` (accepted
  and refused forms), and the file ID check.

### 7.2 System tests (compared with the interpreter)

`test/system/run_all_tests.sh` has a section that, for each program in `COMPILER_TESTS`:

1. runs the program with the interpreter and saves its output;
2. compiles the program to a jar with the editor commands `LOAD`, `COMPILE`, `EXIT`;
3. runs the jar and saves its output;
4. fails the test if the two outputs are different.

If a file with the same name and the extension `.in` exists (for example `compiler_input.in`), its content is sent to
standard input of both runs. The programs are in `test/system/compiler/` and in `test/system/`.

## 8. Adding support for a new statement

1. Find the statement class in `src/main/java/eu/gricom/basic/statements/`.
2. If the generator needs a field that is private, add a read-only getter (keep the interpreter's behaviour the same).
3. In `JavaCodeGenerator.statementBody()`, add a branch that returns the Java code for the statement. Copy the logic
   from the interpreter's `execute()` method, and call the same helper classes.
4. Add a positive and a negative unit test in `JavaCodeGeneratorTest`.
5. Add the program to `COMPILER_TESTS` in `run_all_tests.sh` and check that the output matches the interpreter.
