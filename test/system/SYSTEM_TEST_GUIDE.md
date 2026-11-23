# GD-BASIC System Test Guide

This guide describes every program that the system test runner executes. For each program it explains
what the program tests, how the runner decides whether it passed, and what the expected outcome is.

It replaces the five older markdown files in this directory (see the last section).

## 1. Purpose and scope

The system tests are small BASIC programs. Each one checks one feature of the GD-BASIC interpreter.
They are run by the shell script `run_all_tests.sh` in this directory, which runs the GD-BASIC jar on each program.

The runner executes **63 programs** in total:

| Group | Count | How each program is run | Section |
|---|---|---|---|
| Runner list (`TEST_FILES`) | 55 | Run with `-r`, judged by exit code and output text | 4 |
| BCD (decimal, `#`) examples | 7 | Run with `-r -q`, output compared with a saved `.expected` file | 5 |
| Binary search example | 1 | Run with `-r -q`, keyboard input from a file, output compared with a saved file | 5 |

Source for these counts: the `TEST_FILES` array and the `BCD_TESTS` array in `run_all_tests.sh`, and the
final `run_example_test` call for `BSearch.bas`.

## 2. How to run the tests

Build the project first. This creates the jar file that the runner needs:

```bash
cd /Users/andreas/Projects/Sources/Java/GD-BASIC
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home   # Java 21
mvn clean test package
```

Run the whole suite:

```bash
cd /Users/andreas/Projects/Sources/Java/GD-BASIC
test/system/run_all_tests.sh
```

Run one program on its own (replace the file name):

```bash
java -jar target/BASIC-0.3.0-jar-with-dependencies.jar -r test/system/test_arithmetic_operators.bas
```

The runner stops at the first failing program and shows that program's output.

## 3. How a program is judged

There are two kinds of check. Both are defined in `run_all_tests.sh`.

### 3.1 Runner tests (`run_test`)

A program **passes** when both of these are true:

1. The interpreter exits with code `0`.
2. The output does not contain the text `ERROR: Test failed`.

A program that stops with a parse or runtime error exits with code `1`, so it fails.

Most programs are **self-checking**. Each check compares a value with the expected value. If a check fails,
the program jumps to line `9000`, prints `ERROR: Test failed!`, and ends. If every check passes, the program
prints a `PASSED` line.

**Important:** nine programs print values for a person to read, but they do not compare them.
For these, the runner checks only the exit code and the absence of the error text. Their rows in section 4 say **No PASSED line**.

### 3.2 Example tests (`run_example_test`)

These programs have a saved **expected output file** in `test/system/examples/`. The runner:

1. Runs the program with `-r -q`, optionally with keyboard input from a `.input` file.
2. Removes colour codes, timestamps and stack-trace lines from the output.
3. Compares what is left with the `.expected` file using `diff`.

Each example also has an **expected exit code**:

| Expected exit code | Meaning | Programs |
|---|---|---|
| `0` | Normal run; the output must match | `test_bcd_arithmetic`, `test_bcd_functions`, `test_bcd_output`, `BSearch` |
| `1` | Negative test: the program must stop with an error, and the error message must match | `test_bcd_bits`, `test_bcd_overflow`, `test_bcd_divzero`, `test_bcd_array_rejected` |

Source: the fourth argument to `run_example_test` in the `BCD_TESTS` loop in `run_all_tests.sh`.

## 4. Runner programs, by group

Each table row gives the program name, what it tests (taken from the program's own header comments), how it is judged,
and the expected outcome. "PASSED" means the program prints a line that says PASSED.

### 4.1 Operator tests

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_arithmetic_operators` | `+`, `-`, `*`, `/`, `^`, `%`, precedence, brackets | Self-check; PASSED | Exit 0, PASSED |
| `test_comparison_operators` | `==`, `!=`, `<`, `>`, `<=`, `>=` | Self-check; PASSED | Exit 0, PASSED |
| `test_logical_operators` | `AND`, `OR`, `NOT` truth tables | Self-check; PASSED | Exit 0, PASSED |
| `test_bitwise_operators` | `<<` and `>>` on INTEGER variables | Self-check; PASSED | Exit 0, PASSED |
| `test_assignment_operators` | `=`, `:=`, `LET` | Self-check; PASSED | Exit 0, PASSED |

### 4.2 Variable type tests

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_variable_types` | Integer `%`, real (no suffix), string `$`, boolean `!`, long `&` | Self-check; PASSED | Exit 0, PASSED |
| `test_string_assignment` | String assignment, `+` concatenation, empty strings | Self-check; PASSED | Exit 0, PASSED |

### 4.3 Control flow tests

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_if_then_else` | `IF-THEN`, `IF-THEN-ELSE`, nested IF, `AND`/`OR` in conditions | Self-check; PASSED | Exit 0, PASSED |
| `test_block_if_else` | Block `IF` / `ELSE` / `END-IF`, single-line IF, nested block IF | Self-check; PASSED | Exit 0, PASSED |
| `test_for_next_loop` | `FOR-NEXT` with positive and negative `STEP`, nested loops, variable bounds | Self-check; PASSED | Exit 0, PASSED |
| `test_for_nested_restart` | A finished inner `FOR` loop starts again on each pass of the outer loop | Self-check; PASSED | Exit 0, PASSED |
| `test_for_first_statement` | A `FOR` loop that starts at the first statement of the program | Self-check; PASSED | Exit 0, PASSED |
| `test_do_until_loop` | `DO-UNTIL`, condition checked after each pass, nested loops | Self-check; PASSED | Exit 0, PASSED |
| `test_while_loop` | `WHILE-END-WHILE`, condition checked before each pass, false start | Self-check; PASSED | Exit 0, PASSED |
| `test_while_nested` | Nested `WHILE` loops; the outer loop ends at its own `END-WHILE` | Self-check; PASSED | Exit 0, PASSED |
| `test_goto_gosub_return` | `GOTO`, `GOSUB`, `RETURN`, nested subroutine calls | Self-check; PASSED | Exit 0, PASSED |

### 4.4 Function and data tests

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_math_functions` | `ABS`, `ATN`, `CDBL`, `CINT`, `COS`, `EXP`, `LOG`, `LOG10`, `RND`, `SIN`, `SQR`, `TAN` | Self-check; PASSED | Exit 0, PASSED |
| `test_string_functions` | `ASC`, `CHR$`, `INSTR`, `LEFT$`, `LEN`, `MID$`, `RIGHT$`, `STR$`, `VAL` | Self-check; PASSED | Exit 0, PASSED |
| `test_string_indexing` | Single characters read with `[index]` | Self-check; PASSED | Exit 0, PASSED |
| `test_system_functions` | `MEM`, `TIME$`, `SYSTEM` | Self-check; PASSED | Exit 0, PASSED |
| `test_arrays_dim` | `DIM`, 1-D and multi-dimensional arrays, variable indices | Self-check; PASSED | Exit 0, PASSED |
| `test_data_read` | `DATA` and `READ`, mixed types, reading in loops | Self-check; PASSED | Exit 0, PASSED |

### 4.5 Statement, expression and edge-case tests

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_print_statement` | `PRINT` with `;` and `,` separators, variables, expressions, empty `PRINT` | Self-check; PASSED | Exit 0, PASSED |
| `test_rem_comments` | `REM` and `'` comments, inline comments | Self-check; PASSED | Exit 0, PASSED |
| `test_colon_separator` | Several statements on one line with `:` | Self-check; PASSED | Exit 0, PASSED |
| `test_not_operator` | `NOT` with booleans, in `IF` | Self-check; PASSED | Exit 0, PASSED |
| `test_pragma_statement` | `@PRAGMA` settings, changed at run time | Self-check; PASSED | Exit 0, PASSED |
| `test_end_statement` | `END` stops the program; later lines do not run | **No PASSED line** (see note) | Exit 0. Prints `Before END statement` and `This is the last line before END`, then stops |
| `test_complex_expressions` | Nested and mixed arithmetic, logic, functions, negative numbers | Self-check; PASSED | Exit 0, PASSED |
| `test_edge_cases` | Zero, division by one, power of zero and one, empty and one-character strings | Self-check; PASSED | Exit 0, PASSED |
| `test_defs_functions` | `DEF FN` macros with 1, 2 and 3 parameters | **No PASSED line** (see note) | Exit 0. Prints lines such as `Should be 4: 4.0` and ends with `Ending the Program` |

### 4.6 ON statement tests

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_on_gosub_basic` | `ON n GOSUB`, computed subroutine call | Self-check; PASSED | Exit 0, PASSED |
| `test_on_gosub_simple` | `ON 2 GOSUB` calls the subroutine and the program returns | **No PASSED line** (see note) | Exit 0. Prints `Success: ON 2 GOSUB called subroutine at line 200` and `Program returned from ON GOSUB call` |
| `test_on_goto_basic` | `ON n GOTO`, computed jump; an out-of-range index does not jump | Self-check; PASSED | Exit 0, PASSED |
| `test_on_goto_simple` | `ON 1 GOTO` jumps to line 100 | **No PASSED line** (see note) | Exit 0. Prints `Success: ON 1 GOTO jumped to line 100` |
| `test_on_outofrange` | `ON` with an out-of-range index continues normally | **No PASSED line** (see note) | Exit 0. Prints `Success: Out-of-range index (5) did not jump, continued normally` |

### 4.7 File I/O tests

The file tests write to fixed paths in `/tmp` (see section 6). They are all self-checking and print a PASSED line
unless noted.

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_file_io` | `FOPEN`, `FPRINT`, `FINPUT`, `FCLOSE`, `EOF`; writes a file, reads it back, deletes it | Self-check; PASSED | Exit 0, PASSED |
| `test_character_level_io` | `FPUT` (write without newline), `FGET` (read and advance), `FPEEK` (look ahead) | Self-check; PASSED | Exit 0, PASSED |
| `test_file_character_transform` | Character-by-character transform with `FGET` and `FPUT` | Self-check; PASSED | Exit 0, PASSED |
| `test_file_copy` | Copy a file line by line | Self-check; PASSED | Exit 0. Prints ` PASSED: File copy successful` |
| `test_file_eof` | `EOF` with file operations | Self-check; PASSED | Exit 0, PASSED |
| `test_file_exists` | `FEXISTS` for existing and missing files | Self-check; PASSED | Exit 0, PASSED |
| `test_file_integration` | `FOPEN`, `FCLOSE`, `FINPUT`, `FPRINT`, `FGET`, `FPUT`, `FPEEK`, `FREWIND`, `FRENAME` together | Self-check; PASSED | Exit 0, PASSED |
| `test_file_operations_comprehensive` | Main file operations, in one program | Self-check; PASSED | Exit 0, PASSED |
| `test_file_peek` | `FPEEK` looks ahead without moving the position | Self-check; PASSED | Exit 0, PASSED |
| `test_file_rename` | `FRENAME` renames or moves a file by file ID | Self-check; PASSED | Exit 0, PASSED |
| `test_file_rewind` | `FREWIND` resets the position to the start | Self-check; PASSED | Exit 0, PASSED |
| `test_file_search_lookahead` | `FPEEK` used to choose which characters to process | Self-check; PASSED | Exit 0, PASSED |

### 4.8 Regression and mixed-feature tests

| Program | What it tests | Judged by | Expected outcome |
|---|---|---|---|
| `test_mixed_tests_1` | String indexing, multi-dimensional arrays (up to 4 dimensions), `FOR` with arrays | **No PASSED line** (see note) | Exit 0. Prints sums such as `Sum of above (should be 15): 15.0` |
| `test_mixed_tests_2` | Brackets, `MEM`, `RND`, `ABS`, `LEFT`, `RIGHT`, `SYSTEM` | **No PASSED line** (see note) | Exit 0. Prints string results. The `SYSTEM` command prints a directory listing, so its output depends on the machine |
| `test_mixed_tests_3` | `DATA` and `READ` into arrays, `IF-THEN-ELSE`, `:`, `<<`, `>>`, `%`, `@PRAGMA` | **No PASSED line** (see note) | Exit 0. Ends with `Test done...` |
| `test_mixed_tests_4` | `REM`, `'` comments, empty lines, precedence, `GOTO`, block `IF` | **No PASSED line** (see note) | Exit 0. Prints `This line should be printed` and similar lines |
| `test_mixed_tests_5` | `FOR` with positive and negative `STEP`, `GOSUB`, `WHILE`, `DO-UNTIL` | **No PASSED line** (see note) | Exit 0. Prints `1.0`, `2.0`, `3.0` from the `DO` loop, then `Ending the Program` |
| `test_large_program` | A program of more than 10,000 lines, with large line numbers | Self-check; PASSED | Exit 0, PASSED |
| `test_bcd_pi` | PI to 32 digits with BCD (`#`) variables, using Gauss-Legendre and Machin's formula | Self-check; PASSED | Exit 0. Prints the progress of each iteration and term, then `All PI (BCD) tests PASSED` |

**Note on the nine programs without a PASSED line** (they appear in sections 4.5, 4.6 and 4.8): `test_end_statement`, `test_defs_functions`,
`test_mixed_tests_1` to `_5`, `test_on_gosub_simple`, `test_on_goto_simple` and `test_on_outofrange`.
They do not compare their own results. A failure in them can only be found by reading the output, unless the
program stops with an error (exit code 1). Converting them to self-checking programs is a possible improvement.

## 5. Example tests (saved expected output)

These files are in `test/system/examples/`. Each `.expected` file holds the exact output that the runner compares with.

### 5.1 BCD (decimal, `#`) programs

| Program | What it tests | Expected exit code | Expected output (from the `.expected` file) |
|---|---|---|---|
| `test_bcd_arithmetic` | Exact decimal results: `0.1 + 0.2`, `1 / 3` and `2 / 3` to 40 digits, `*`, `-`, mixing with REAL and INTEGER | 0 | Saved output of the program (compared with `diff`) |
| `test_bcd_functions` | `SQR`, `EXP`, `LOG`, `SIN`, `ABS`, `CINT` on BCD values | 0 | Saved output of the program |
| `test_bcd_output` | Output format: plain decimal, no trailing zeros, no exponent | 0 | Saved output of the program |
| `test_bcd_bits` | Negative: a shift with a fractional BCD value (`3.9`) must fail | 1 | `eu.gricom.basic.error.SyntaxErrorException: Operator '<<' needs whole numbers within the INTEGER range, but got 3.9` |
| `test_bcd_overflow` | Negative: more than 40 digits before the decimal point must fail | 1 | `eu.gricom.basic.error.OverflowException: Overflow: a BCD value may have at most 40 digits before the decimal point, this value needs 41` |
| `test_bcd_divzero` | Negative: division by zero must fail | 1 | `eu.gricom.basic.error.DivideByZeroException: 1/0 is a division by zero` |
| `test_bcd_array_rejected` | Negative: arrays with the `#` suffix are not supported yet and must fail | 1 | `eu.gricom.basic.error.SyntaxErrorException: Syntax Error: arrays with the '#' (BCD) suffix are not supported yet: A#-1` |

### 5.2 Binary search example

| Program | What it tests | Input | Expected output |
|---|---|---|---|
| `doc/GD_Basic_Examples/BSearch.bas` (not in this directory) | Binary search of a sorted array of 9 elements, read from the keyboard | `examples/BSearch.input` (6 lines) | `examples/BSearch.expected` (16 lines), for example `A HAS9ELEMENTS` and `A(0)=1.0` |

## 6. Side effects and known gaps

- **Temporary files in `/tmp`.** The file tests write to fixed paths such as `/tmp/test_exists.txt`,
  `/tmp/copy_source.txt` and `/tmp/integration_test.txt`. The runner removes only `test_io.txt` from the
  current directory at the end of a run (`rm -f test_io.txt` in `run_all_tests.sh`). The `/tmp` files are not removed by the runner.
- **Machine-dependent output.** `test_mixed_tests_2` runs the `SYSTEM` command, which lists a directory, so its output
  changes from one machine to another. `MEM`, `RND` and `TIME$` values also change.
- **Nine programs are not self-checking** (see the note in section 4).
- **Log output.** Running with `-r` also prints log lines with timestamps. The example comparison removes these
  lines before comparing.

## 7. Adding a new program

1. Create `test/system/test_<feature_name>.bas`.
2. Start the file with a header comment block: test name, description, expected behaviour.
3. Write one step per check. Print a description before each step.
4. On a failed check, `GOTO 9000`. Line `9000` prints `ERROR: Test failed!` and then `END`.
5. Print a `PASSED` line at the end when every check has passed.
6. Add the file name to the `TEST_FILES` array in `run_all_tests.sh`.
7. Document the program in this guide, in the matching section of section 4.
8. If the program has saved output, add a `.expected` file in `examples/` and a call to `run_example_test` in `run_all_tests.sh`.
9. Write a negative test as well: a program or copy that must fail with an error, so the failure path is proven.

## 8. Documents this guide replaces

After this guide is approved, these files are to be removed. They are tracked in git, so they can be recovered from git history.

- `README.md`
- `QUICK_START.md`
- `SYSTEM_TESTS_SUMMARY.md`
- `TEST_SUITE_SUMMARY.md`
- `FILE_OPERATIONS_TESTS_README.md`

Known differences in the old files that this guide corrects: `README.md` gives the program count as 62 and lists three negative
tests; the runner has 63 programs and four negative tests (`test_bcd_bits` is the fourth).
