# GD-BASIC: Known Issues

**Status date:** 2026-11-23 · **Version:** 0.4.0 (branch `feature/compiler`, not yet merged)

This page lists the problems we have found in GD-BASIC, and how we know about them. It is written for a developer
who is new to the project, so each entry explains what you should have seen, what you actually saw, and how to
check it yourself.

## How to read this page

Each issue has these parts:

| Part | Meaning |
|---|---|
| **Severity** | How much the problem hurts. **High**: a documented feature does not work, or the program crashes. **Medium**: wrong or unclear behaviour, or an ugly crash message. **Low**: small or rarely used. |
| **Repro** | The smallest BASIC program that shows the problem. Save it as `test.bas` and run `java -jar BASIC-0.4.0-jar-with-dependencies.jar -r test.bas`. |
| **Expected** | What the documentation or the code comment says should happen. The source is named. |
| **Actual** | What we saw, copied from the interpreter output. |
| **Where** | The source file and line where the problem starts (from the Java stack trace). |

All results were checked by running the 0.4.0 jar on 2026-11-23. Line numbers are from the code on that day and
will move when the code changes.

## Summary

| ID | Severity | Area | One-line summary |
|---|---|---|---|
| KI-01 | High | Decimal (`#`) | Long number literals lose digits before they become decimal values. |
| KI-02 | Medium | Decimal (`#`) | Assigning a string to a decimal variable prints a Java stack trace. |
| KI-03 | Medium | Macros (`DEF`) | A bare `DEF` line crashes the interpreter. |
| KI-04 | High | Functions | `LISTDIRECTORY(...)` in a BASIC program crashes. |
| KI-05 | Medium | Functions | `SYSTEM("START", ...)` is documented but not implemented. |
| KI-06 | High | Loops | `EXIT` does not end a `WHILE` or `DO` loop. |
| KI-07 | Low | Statements | `STOP` is not implemented (documented as planned). |
| KI-08 | Medium | Statements | `LET` is rejected; the docs mark it as deprecated and show an invalid example. |
| KI-09 | High | `IF` | A single-line `IF ... THEN statement ELSE statement` crashes. |
| KI-10 | Medium | Editor | The line editor crashes when its input ends (for example `< /dev/null`). |
| KI-11 | Low | Statements | `WEND` without a `WHILE` shows a raw exception. |
| KI-12 | Low | Dialects | The `dialects` packages are empty and not used. |
| KI-13 | Info | Reserved words | Many reserved words are not implemented. |
| KI-14 | Info | Style | 32 methods are longer than 50 lines (checkstyle warnings). |
| KI-15 | Medium | Operators | `||`, `=>` and `=<` are listed as implemented, but are refused. |
| KI-16 | High | Variables (`&`) | A LONG (`&`) value above 2147483647 is clamped to that number. The docs say 64-bit. |
| KI-17 | Info | `PRINT` | Not a defect: a comma between `PRINT` items prints no space (decision confirmed 2026-11-22). Documented in `BASIC.md`. |
| KI-18 | Medium | Lexer | A line that starts with `!` and has no line number stops the interpreter with a Java exception. |

Documentation corrections that were made together with this page are listed in section D at the end.

---

## KI-01: Long number literals lose digits before they become decimal values

**Severity:** High · **Area:** decimal variables (`#`, BCD)

**Repro**

```basic
10 X# = 1234567890123456789012345678901234567890
20 PRINT X#
30 END
```

**Expected.** `doc/BASIC.md`, section "Decimal Numbers (BCD)", says that a decimal variable "holds up to **40
significant digits**". The printed value should be `1234567890123456789012345678901234567890`.

**Actual**

```text
1234567890123456800000000000000000000000
```

Only the first 17 digits are right. The rest is noise from the binary number format.

**Cause (from the code).** Every number literal is first turned into a Java `double` (a binary number that keeps
about 17 significant digits). The line is in `src/main/java/eu/gricom/basic/parser/BasicParser.java`:

```java
return new RealValue(Double.parseDouble(oToken.getText()));   // line 1024
```

Lines 123 and 140 use the same pattern. The decimal conversion happens only after this step, so the digits are
already lost.

**Why short numbers look fine.** `A# = 0.1` + `0.2` prints `0.3` because a short literal survives the
`double` step, and `BigDecimal.valueOf(double)` turns it back into the text the user wrote. Only long literals are
affected.

**Workaround.** Use values with 17 or fewer significant digits, or compute the big value from smaller parts.
Assigning a string does not work either (KI-02).

**Fix idea (not done).** Keep the literal text in the token and convert it with `new BigDecimal(text)` when the
target variable is decimal.

---

## KI-02: Assigning a string to a decimal variable prints a Java stack trace

**Severity:** Medium · **Area:** decimal variables (`#`)

**Repro**

```basic
10 A# = "1.5"
20 END
```

**Expected.** One clear BASIC error line, in the same style as the other errors (for example
`Syntax Error: ...`).

**Actual.** The program stops with the exception text and a Java stack trace:

```text
eu.gricom.basic.error.SyntaxErrorException: 1.5 is not a number
	at eu.gricom.basic.variabletypes.BcdValue.toDecimal(BcdValue.java:116)
	at eu.gricom.basic.variabletypes.BcdValue.of(BcdValue.java:89)
	at eu.gricom.basic.memorymanager.VariableManagement.putMap(VariableManagement.java:97)
```

**Where.** `BcdValue.toDecimal`, line 116, where the exception is thrown. It reaches the top level without a
one-line message.

---

## KI-03: A bare `DEF` line crashes the interpreter

**Severity:** Medium · **Area:** macros (`DEF`)

**Repro**

```basic
10 DEF
20 END
```

**Expected.** A syntax error message. `doc/BASIC.md`, section "`DEF` Command", says `DEF` defines a macro and gives
the syntax `DEF FNA(X) = "X * 2"`.

**Actual**

```text
Exception in thread "main" java.lang.IndexOutOfBoundsException: Index 3 out of bounds for length 2
	at eu.gricom.basic.macromanager.MacroProcessor.process(MacroProcessor.java:55)
```

**Related.** A `DEF` line with the body not in quotes, for example `DEF FNA(X) = X * 2`, does not crash. It gives
`Macro body not found`. The documented form puts the body in double quotes, and that form works.

---

## KI-04: `LISTDIRECTORY(...)` crashes in a BASIC program

**Severity:** High · **Area:** functions (`LISTDIRECTORY`)

**Repro**

```basic
10 PRINT LISTDIRECTORY(".", FALSE, FALSE)
20 END
```

**Expected.** A list of the names in the directory, separated by semicolons. The Java class
`functions/ListDirectory.java` implements this with three parameters (directory, include hidden, include
subdirectories).

**Actual**

```text
java.lang.NumberFormatException: For input string: "."
	at eu.gricom.basic.variabletypes.StringValue.toReal(StringValue.java:40)
	at eu.gricom.basic.statements.ArrayAccessExpression.buildKey(ArrayAccessExpression.java:110)
	at eu.gricom.basic.statements.ArrayAccessExpression.evaluate(ArrayAccessExpression.java:47)
```

**Cause (observed).** The call is evaluated as an **array element** (`ArrayAccessExpression`), so the function is
never reached. The parser has a `case LISTDIRECTORY:` in `BasicParser.java` near line 1046, so the reason it is not
used is not yet known. Next step: trace the parser with `-v debug`.

**Test gap.** `src/test/java/eu/gricom/basic/functions/ListDirectoryTest.java` tests the Java method directly. No test
runs `LISTDIRECTORY(...)` from a BASIC program, which is why the crash was not found before.

---

## KI-05: `SYSTEM("START", ...)` is documented but not implemented

**Severity:** Medium · **Area:** functions (`SYSTEM`)

**Repro**

```basic
10 PRINT SYSTEM("START", "x")
20 END
```

**Expected.** `doc/BASIC.md`, section "`SYSTEM`", says that `START` "executes any program from the system" and
returns no result.

**Actual**

```text
eu.gricom.basic.error.RuntimeException: Parameter error
	at eu.gricom.basic.functions.System.execute(System.java:48)
```

**Cause.** `functions/System.java` lines 45 to 50 only accept the command `RUN`. Every other command gives
`Parameter error`. `RUN` works (`SYSTEM("RUN", "echo hi")` prints `hi`).

---

## KI-06: `EXIT` does not end a `WHILE` or `DO` loop

**Severity:** High · **Area:** loops (`EXIT`)

**Repro** (a `WHILE` loop that should stop when `I` reaches 2)

```basic
10 I = 0
20 WHILE I < 5
30 I = I + 1
40 IF I == 2 THEN
45 EXIT
47 END-IF
50 WEND
60 PRINT "after";I
70 END
```

**Expected.** `doc/BASIC.md`, section "`EXIT` Command", says: "The `EXIT` command terminates the `WHILE` and the `DO`
loop immediately and continues with the first command after the `END-WHILE` or the `UNTIL` statement." So the
program should print `after2.0`.

**Actual.** `after5.0`. The loop runs to the end.

**Repro for DO** (the same problem as an error):

```basic
10 I = 0
20 DO
30 I = I + 1
40 EXIT
50 UNTIL I >= 5
60 END
```

Output: `Incorrect Command: 40: [EXIT]`.

**Cause.** `EXIT` has no `case` in `parser/BasicParser.java`, so a standalone `EXIT` is refused. Inside an `IF`
block the word is accepted but does nothing. The status table in `BASIC.md` says `planned`, while the `EXIT`
section describes it as working. The status table is the accurate one.

**Test gap.** No test in `src/test` or `test/system` uses `EXIT` as a statement.

---

## KI-07: `STOP` is not implemented

**Severity:** Low · **Area:** statements (`STOP`)

**Repro**

```basic
10 PRINT "a"
20 STOP
30 PRINT "b"
40 END
```

**Actual.** `Incorrect Command: 20: [STOP]`. The status table in `BASIC.md` says `planned`, so this is known, but
the command should be listed as not available.

---

## KI-08: `LET` is rejected

**Severity:** Medium · **Area:** statements (`LET`)

**Repro**

```basic
10 LET A = 4
20 END
```

**Actual.** `Incorrect Command: 10: [LET]`. The same happens with `LET A=4`.

**Expected (as the docs said before 2026-11-23).** The status table in `BASIC.md` said `LET` is `depreciated` (a typo for
*deprecated*), and the depreciated section said it "will be ignored". The example in the section "Multiple Assignments"
(`10 LET B=C=0`) is also not valid BASIC for this interpreter. Both are now corrected in `BASIC.md`.

**Workaround.** Write the assignment without `LET`: `A = 4`.

**Note.** The system test `test/system/test_assignment_operators.bas` has `LET` only inside a string (it prints the
text `LET c% = `). It does not test the statement.

---

## KI-09: A single-line `IF ... THEN statement ELSE statement` crashes

**Severity:** High · **Area:** `IF`

**Repro**

```basic
10 A = 1
20 IF A == 1 THEN PRINT "one" ELSE PRINT "other"
30 END
```

**Actual.** The program prints `one` and then stops with an exception:

```text
eu.gricom.basic.error.EmptyStackException: Stack Empty
	at eu.gricom.basic.memorymanager.Stack.pop(Stack.java:51)
	at eu.gricom.basic.statements.ElseStatement.execute(ElseStatement.java:41)
```

**Expected.** The `IF` section of `BASIC.md` documents two forms only: the block form
(`IF ... THEN` ... `ELSE` ... `END-IF`) and the line-number form (`IF cond THEN <line_number>`). The
single-line form is not documented. Either it should work, or it should give a syntax error. It should not
crash after the `THEN` part has run.

**Related.** `IF A == 1 THEN EXIT` (a statement after `THEN` with no `END-IF`) gives
`Missing statement END-IF`. The interpreter treats the statement as the start of a block.

**Workaround.** Use the block form with `END-IF`, or the line-number form.

---

## KI-10: The line editor crashes when its input ends

**Severity:** Medium · **Area:** line editor

**Repro.** Start the interpreter with no program file and with its input closed:

```bash
java -jar BASIC-0.4.0-jar-with-dependencies.jar < /dev/null
```

**Actual**

```text
>Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "strLine" is null
	at eu.gricom.basic.lineeditor.LineEditor.processLine(LineEditor.java:76)
	at eu.gricom.basic.lineeditor.LineEditor.execute(LineEditor.java:63)
```

**Expected.** The editor should end cleanly when no more input is available, the same way it ends when the user
types `EXIT`.

**Why it matters.** This happens when commands are piped in and the pipe ends before an `EXIT` line.

---

## KI-11: `WEND` without a `WHILE` shows a raw exception

**Severity:** Low · **Area:** loops (`WEND`)

**Repro**

```basic
10 WEND
20 END
```

**Actual.** `eu.gricom.basic.error.EmptyStackException: Stack Empty`. A message like
`WEND without WHILE` would be clearer.

---

## KI-12: The `dialects` packages are empty and not used

**Severity:** Low · **Area:** packages

The folders `src/main/java/eu/gricom/basic/dialects/apple`, `pet` and `trs80` contain only `package-info.java`
files and a few empty classes (for example `dialects/trs80/functions/String.java` is an empty class). Nothing in the
interpreter refers to them.

The only dialect switch in the interpreter is the `-d` option (Dartmouth mode, which changes the order of
evaluation). An earlier version of `doc/QUICK_START.md` said that GD-BASIC "supports multiple BASIC dialects
(Dartmouth, TRS-80, PET, Apple)". That claim was wrong and has been removed (see section D).

**Risk.** An empty class named `String` inside a package hides `java.lang.String` for code in that package. Do not
add code to these packages until the design is decided.

---

## KI-13: Many reserved words are not implemented

**Severity:** Info · **Area:** reserved words

The lexer (`tokenizer/ReservedWords.java`) reserves these words. Each of them was tried as a statement in its own
small program, and each one gave `Incorrect Command` (or a similar error):

`CLOSE`, `CLS`, `CMD`, `CONT`, `ERL`, `ERR`, `EOL`, `FREE`, `LENGTH`, `LISTDIR`, `RANDOM`, `SQL`, `TAB`, `WRITE`,
`OPEN`, `CLEAN`.

The reserved words cannot be used as variable names, so a program that uses `TAB` or `LENGTH` as a variable will
fail. The status table in `doc/BASIC.md` now shows the real state of each word.

---

## KI-14: Long methods (checkstyle warnings)

**Severity:** Info · **Area:** style

`checkstyle` reports 32 methods that are longer than 50 lines (rule `MethodLength`). The team agreed to keep these
as warnings for now. The build does not fail because of them.

---

## KI-15: `||`, `=>` and `=<` are refused

**Severity:** Medium · **Area:** operators

**Repro**

```basic
10 PRINT TRUE || FALSE
20 END
```

**Actual.** `Incorrect Operator: || followed by BOOLEAN in Line [10]`.

The same happens for the alias operators `=>` (for `>=`) and `=<` (for `<=`). `PRINT 2 => 1` gives
`Incorrect Command: 10: [ASSIGN_EQUAL]`.

**Expected.** `doc/BASIC.md`, section "Reserved Words" (operator rows), lists `||` and `=>` and `=<` as implemented.
The boolean OR is `OR` (it works), and `&&` works as boolean AND.

**Workaround.** Use `OR`, `>=` and `<=`.

---

## KI-16: LONG (`&`) values above 2147483647 are clamped

**Severity:** High · **Area:** variables (`&`, LONG)

**Repro**

```basic
10 N& = 3000000000
20 PRINT N&
30 END
```

**Expected.** `doc/BASIC.md`, the variable table (line 224), says LONG is `AB&` "(64-bit)" and "Implemented".
The output should be `3000000000`.

**Actual.** `2147483647` (the largest INTEGER). `PRINT 5000000000` (no suffix) prints `5.0E9` and is correct, so only
the LONG variable is affected.

**Cause.** `memorymanager/VariableManagement.java`, line 93. The `INTEGER` and `LONG` cases are handled together and
stored as an `IntegerValue` after a cast to `int`:

```java
case INTEGER:
case LONG:
    _moIntegers.put(Normalizer.normalizeIndex(strKey), new IntegerValue((int) oValue.toReal()));
```

A cast from a large double to `int` in Java gives `Integer.MAX_VALUE`, which is 2147483647.

**Workaround.** Keep LONG values within the INTEGER range. Use a REAL (no suffix) or a decimal (`#`) variable for
bigger numbers.

**Fix idea (not done).** Give LONG its own storage (a `LongValue` map), or store it as a `long`, and test a value
above 2147483647 in a unit test and in `test/system/`.

---

## KI-17: `PRINT` with a comma gives no space between items (decision, not a defect)

**Severity:** Info · **Area:** `PRINT`

**Status:** This is the behaviour the owner confirmed on 2026-11-22. `CHANGELOG.md`, section "Decisions confirmed on
2026-11-22 (owner)", says: "A comma between `PRINT` items adds no space: `PRINT 1,2` prints `1.02.0`." It is listed
here so that a reader who finds it is not surprised.

**Repro**

```basic
10 PRINT "A", "B"
20 PRINT "C"; "D"
30 END
```

**Actual.** The output is `AB` and then `CD` (the bytes are `A B \n C D \n`). The interpreter and the compiled jar
behave the same way.

**Note for readers.** In many older BASIC dialects a comma moves to the next print zone (a tab position). GD-BASIC
does not do that. If you want a space, write `PRINT "A"; " "; "B"`.

---


## KI-18: A line that starts with `!` and has no line number crashes the interpreter

**Severity:** Medium · **Area:** lexer (`tokenizer/BasicLexer.java`)

**Repro:** a file `bang2.bas` with these two lines:

```basic
! whole line comment
10 PRINT 5
```

Run it with `java -jar ... -r bang2.bas`.

**Expected:** Either the line is a comment, as `10 ! comment` is (checked 2026-11-23: that form prints `5.0`), or a
clear syntax error. The user guide said that "A line that starts with `!` is accepted as a comment"; that sentence was
not true for this form and has been corrected.

**Actual:** The program never runs. The output is:

```text
Exception in thread "main" java.lang.NumberFormatException: For input string: "!"
	at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:67)
```

**Where:** `tokenizer/BasicLexer.java`, line 49, `Integer.parseInt(strProgramLine.substring(0, strProgramLine.indexOf(" ")))`.
The exception is raised while `MacroProcessor.tokenize` (`macromanager/MacroProcessor.java`, line 143) runs.

**Workaround:** Give the comment a line number: `10 ! whole line comment`, or use `REM` with a line number.

## Documentation corrections

These are not interpreter bugs. The documentation was wrong and has been corrected with this page.

### D1. Comparisons use `==`, and `=` is assignment only

`BASIC.md`, section "Operators", lists `=` and `==` both as implemented, which is unclear. The rule is stated in
`BASIC.md` around line 901: "the assignment equal (`=`) is different from the comparison equal (`==`)".

| Program | Result |
|---|---|
| `IF A = 1 THEN` | Error: `Found: = expected THEN.` |
| `IF A == 1 THEN` | Works. |
| `PRINT 1 == 2` | `False` |
| `PRINT 1 = 1` | Error: `Incorrect Command ... [ASSIGN_EQUAL]` |

### D2. `FOR ... NEXT` takes no variable name

`NEXT I` gives `Incorrect Operator: I followed by END`. Only `NEXT` on its own works. The documentation shows only
the form without a name, so this is now stated in the `FOR` section.

### D3. `RND` takes no parentheses

`RND` on its own gives a number between 0 and 1. `RND(1)` gives a syntax error. The section "`RND`" in `BASIC.md`
already says the function takes no parameter.

### D4. `ON` is implemented

The reserved-words table listed `ON` as `reserved`. `ON GOTO` works (the program
`10 X = 2` / `20 ON X GOTO 100, 200` prints `two`). `ON GOSUB` is not re-run on 2026-11-23.

### D5. The QUICK_START guide had wrong examples and claims

The earlier `doc/QUICK_START.md` said GD-BASIC "supports multiple BASIC dialects (Dartmouth, TRS-80, PET, Apple)". It
also used `LET`, `B@`, `END IF`, `INT(3.7)`, `RND()` and a `NEW` command, none of which works in 0.4.0. The guide was
rewritten on 2026-11-23, and every example in it was run.

### D6. The README claims were out of date

`README.md` gave 1435 unit tests and 62 system programs, and said "Zero violations (Checkstyle, PMD)". The current run
is 1560 unit tests and 109 system program runs. Checkstyle reports 32 warnings (KI-14). PMD was not run on
2026-11-23.

### D7. Broken links

Several links pointed to files that do not exist (`TEST_SUMMARY.md` in `README.md`), or were written relative to the
project root instead of the `doc/` folder (the Dartmouth PDF in `doc/BASIC.md`). They are fixed.

### D8. `COMPILER.md` used a wrong function name

`COMPILER.md` used `TIME$`. The interpreter's function is `TIME` (it returns milliseconds).

---

## What is not a known issue

- **Compiler limitations** (arrays, `DATA`/`READ`, labels in `GOTO`) are listed in `doc/COMPILER.md`, section 5.3.
  They are documented limits, not bugs.
- **Random and machine values** (`RND`, `MEM`, `TIME`) differ in every run. That is expected.

## Checklist for fixing an issue

1. Write a unit test that fails with the current behaviour (a positive test for the fixed result and a negative test
   for bad input).
2. Fix the code and keep the change small.
3. Add the program to `test/system/` if it is a language feature, and check its output.
4. Update this page (move the entry to the "fixed" list in `CHANGELOG.md`) and the affected section of `BASIC.md`.
