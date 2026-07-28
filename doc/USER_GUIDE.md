# GD-BASIC User's Guide

**Version:** 0.2.0+  
**Last Updated:** 2026-07-26 10:40 UTC  
**Document Type:** User Manual  

---

## Table of Contents

1. [Introduction](#introduction)
2. [Installation & Setup](#installation--setup)
3. [Running the Interpreter](#running-the-interpreter)
4. [Command-Line Parameters](#command-line-parameters)
5. [Interactive Line Editor](#interactive-line-editor)
6. [Program Development Workflow](#program-development-workflow)
7. [Example Programs](#example-programs)
8. [Troubleshooting](#troubleshooting)

---

## Introduction

GD-BASIC (GriCom Diminutive BASIC Interpreter) is a Java-based interpreter for Dartmouth-style BASIC programs. It allows you to:

- **Execute BASIC programs** from `.bas` files
- **Develop programs interactively** using the built-in line editor
- **Compile to Java source** for code generation (optional)
- **Embed as a scripting engine** in Java applications

This guide covers using GD-BASIC from the command line and the interactive line editor.

---

## Installation & Setup

### Prerequisites

- **Java 21+** (OpenJDK 21 or later)
- **Terminal or Command Prompt** for command-line usage

### Setup

1. Download the executable JAR: `BASIC-0.2.0-jar-with-dependencies.jar`
2. Place it in a directory of your choice
3. No additional installation needed

### Verify Installation

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -h
```

You should see the help message displaying available command-line options.

---

## Running the Interpreter

### Basic Usage

#### Run an Existing Program

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar program.bas
```

This loads and enters the interactive line editor with your program. You can then:
- View the program with `LIST`
- Modify lines
- Add new lines
- Run with `RUN`

#### Run in Direct Execution Mode

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -r program.bas
```

The `-r` flag (run directly) skips the interactive editor and executes immediately.

#### Start Interactive Editor with Empty Program

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar
```

No program file needed! Start with an empty program and build it interactively.

---

## Command-Line Parameters

All command-line options use single-letter flags:

### Option: `-h` (Help)

Display the help message with all available options.

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -h
```

### Option: `-q` (Quiet Mode)

Suppress the splash screen banner. Program still runs normally.

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -q program.bas
```

### Option: `-v LEVEL` (Verbose Logging)

Set logging verbosity. Useful for debugging.

**Valid levels:**
- `trace` — Maximum detail (all operations logged)
- `debug` — Detailed diagnostic information
- `info` — General informational messages (default)
- `warning` — Warnings only (least verbose)

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -v debug program.bas
```

### Option: `-d` (Dartmouth Mode)

Enable Dartmouth-style left-to-right expression evaluation instead of standard BODMAS/PEMDAS.

**Standard mode (default)**: `2 + 3 * 4 = 14`  
**Dartmouth mode**: `2 + 3 * 4 = 20`

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -d program.bas
```

### Option: `-r` (Run Directly)

Execute the program immediately without entering the interactive editor.

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -r program.bas
```

> **Note**: The `-r` flag is ignored when no program file is provided (interactive mode).

### Combining Options

Options can be combined:

```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -q -v debug -d program.bas
```

---

## Interactive Line Editor

When you run GD-BASIC with a program file (or without one), the interactive line editor starts.

### Line Editor Commands

#### `LIST`

Display the current program source code.

```
>LIST
10 PRINT "HELLO WORLD"
20 PRINT "This is line 20"
30 END
```

#### `RUN`

Parse and execute the program.

```
>RUN
HELLO WORLD
This is line 20
```

> **Requirement**: The program must have at least one line. `RUN` on an empty program shows an error.

#### `LOAD <filename>`

Load a BASIC program from a file, replacing the current program.

```
>LOAD myprogram.bas
Program loaded from myprogram.bas
```

**Behavior**:
- File must exist; throws error if not found
- File must not be empty; throws error if empty
- Current program is completely replaced

#### `SAVE <filename>`

Save the current program to a file.

```
>SAVE myoutput.bas
Program saved to myoutput.bas
```

**Behavior**:
- File must NOT already exist (prevents accidental overwriting)
- Throws error if file exists
- Creates a new file with the program source

#### `DELETE <line-number>`

Delete a single line by line number.

```
>DELETE 20
Deleted line 20
```

#### `DELETE <start> <end>`

Delete a range of lines (inclusive).

```
>DELETE 10 20
Deleted lines 10 to 20
```

Supports two syntax formats:
- Space-separated: `DELETE 100 200`
- Comma-separated: `DELETE 100,200`

#### `HELP`

Display help information for all editor commands.

```
>HELP
================================================================================
  GD-BASIC (GriCom Diminutive BASIC Interpreter) - Line Editor Help
================================================================================
...
```

#### `EXIT`, `BYE`, or `QUIT`

Exit the interpreter.

```
>EXIT
Good bye.
```

### Entering Program Lines

To enter a program line, start with a line number followed by a space and the BASIC statement:

```
>10 PRINT "Hello"
>20 INPUT "Enter a number: "; N
>30 PRINT "You entered: "; N
>40 END
```

Lines are automatically sorted by line number, so you don't need to enter them in order:

```
>30 END
>10 PRINT "Start"
>20 PRINT "Next"
>LIST
10 PRINT "Start"
20 PRINT "Next"
30 END
```

### Modifying Existing Lines

Re-enter a line with the same line number to replace it:

```
>10 PRINT "Original"
>LIST
10 PRINT "Original"
>10 PRINT "Modified"
>LIST
10 PRINT "Modified"
```

### Multiple Statements Per Line

Use colons (`:`) to separate multiple statements on one line:

```
>10 X = 5 : PRINT "X is "; X : X = X + 1
```

---

## Program Development Workflow

### Workflow 1: Interactive Development

**Best for:** Learning, prototyping, quick testing

1. Start the interpreter without a file:
   ```bash
   java -jar BASIC-0.2.0-jar-with-dependencies.jar
   ```

2. Build your program interactively:
   ```
   >10 PRINT "Enter a number: ";
   >20 INPUT N
   >30 PRINT "Double: "; N * 2
   >40 END
   ```

3. Test with `RUN`:
   ```
   >RUN
   Enter a number: ?
   ```

4. Fix errors and re-test:
   ```
   >DELETE 20
   >20 INPUT "Enter value: "; N
   >RUN
   ```

5. Save your work:
   ```
   >SAVE myfirst.bas
   ```

### Workflow 2: File-Based Development

**Best for:** Larger programs, version control, batch processing

1. Create a `.bas` file in your text editor:
   ```basic
   10 REM Fibonacci Sequence
   20 PRINT "Enter count: ";
   30 INPUT N
   40 A = 0 : B = 1
   50 FOR I = 1 TO N
   60 PRINT A;
   70 C = A + B
   80 A = B : B = C
   90 NEXT I
   100 PRINT
   110 END
   ```

2. Test with the editor:
   ```bash
   java -jar BASIC-0.2.0-jar-with-dependencies.jar fibonacci.bas
   ```

3. Inside the editor:
   ```
   >RUN
   >LIST
   >DELETE 65
   >70 PRINT A; " ";
   >RUN
   ```

4. Execute directly (when ready):
   ```bash
   java -jar BASIC-0.2.0-jar-with-dependencies.jar -r fibonacci.bas
   ```

### Workflow 3: Batch Processing

**Best for:** Unattended execution, scripts, CI/CD

```bash
# Create program
cat > batch_job.bas << 'EOF'
10 PRINT "Processing..."
20 FOR I = 1 TO 100
30 PRINT I; " ";
40 NEXT I
50 PRINT
60 PRINT "Done!"
70 END
EOF

# Run directly without editor
java -jar BASIC-0.2.0-jar-with-dependencies.jar -r -q batch_job.bas
```

---

## Example Programs

### Hello World

```basic
10 PRINT "Hello, World!"
20 END
```

Run it:
```bash
java -jar BASIC-0.2.0-jar-with-dependencies.jar -r -q hello.bas
```

### Simple Calculator

```basic
10 PRINT "Simple Calculator"
20 PRINT "Enter first number: ";
30 INPUT A
40 PRINT "Enter operator (+,-,*,/): ";
50 INPUT OP$
60 PRINT "Enter second number: ";
70 INPUT B
80 IF OP$ == "+" THEN PRINT "Result: "; A + B
90 IF OP$ == "-" THEN PRINT "Result: "; A - B
100 IF OP$ == "*" THEN PRINT "Result: "; A * B
110 IF OP$ == "/" THEN PRINT "Result: "; A / B
120 END
```

### Factorial Calculator

```basic
10 PRINT "Factorial Calculator"
20 PRINT "Enter a number (0-20): ";
30 INPUT N
40 IF N < 0 THEN PRINT "Error: must be >= 0" : GOTO 20
50 IF N > 20 THEN PRINT "Error: must be <= 20" : GOTO 20
60 F = 1
70 FOR I = 1 TO N
80 F = F * I
90 NEXT I
100 PRINT N; "! = "; F
110 END
```

### String Operations

```basic
10 PRINT "String Operations Demo"
20 S$ = "GriCom BASIC"
30 PRINT "Original: "; S$
40 PRINT "Uppercase: "; UPPER(S$)
50 PRINT "Lowercase: "; LOWER(S$)
60 PRINT "Length: "; LEN(S$)
70 PRINT "First 5 chars: "; LEFT(S$, 5)
80 PRINT "Last 5 chars: "; RIGHT(S$, 5)
90 PRINT "Position of 'BASIC': "; INSTR(S$, "BASIC")
100 END
```

---

## Troubleshooting

### "Program file name missing..."

**Cause:** No program file provided and unclear if interactive mode intended.

**Solution:** Either provide a program file or explicitly start interactive mode (no args).

```bash
# Interactive mode (empty program)
java -jar BASIC-0.2.0-jar-with-dependencies.jar

# Or load an existing file
java -jar BASIC-0.2.0-jar-with-dependencies.jar existing.bas
```

### "File not found: myfile.bas"

**Cause:** The file path doesn't exist or is misspelled.

**Solution:** 
- Check file exists: `ls myfile.bas`
- Use absolute path: `/full/path/to/myfile.bas`
- Check spelling and case (case-sensitive on Linux/Mac)

### "File already exists: output.bas"

**Cause:** SAVE target file already exists (protection against overwriting).

**Solution:**
- Use a different filename: `SAVE newname.bas`
- Delete the old file first: `rm output.bas`
- Use LOAD to load and modify the existing file

### "RUN: No program loaded..."

**Cause:** Attempted to RUN an empty program.

**Solution:**
- Enter program lines first: `10 PRINT "Hello"`
- Or use LOAD to load an existing program

### Syntax error in program

**Cause:** Invalid BASIC syntax in your code.

**Solution:**
- Review the error message for line number
- Delete the offending line: `DELETE <line-number>`
- Re-enter the line correctly
- Refer to BASIC_CODING_STANDARD.md for valid syntax

### Program runs but produces wrong output

**Cause:** Logic error in your BASIC code.

**Solution:**
- Add debug output: `PRINT "DEBUG: variable = "; var`
- Use `-v debug` flag to see parser/execution details
- Test with simpler versions to isolate the issue
- Check variable names (case-sensitive)

### Out of memory error

**Cause:** Program uses too much memory or has infinite loop.

**Solution:**
- Increase Java heap: `java -Xmx512m -jar BASIC-0.2.0-jar-with-dependencies.jar`
- Check for infinite loops: `WHILE 1 THEN PRINT X` (missing exit condition)
- Reduce array sizes or data volume

---

## Configuration & Environment

### Environment Variables

Override configuration via environment variables:

```bash
# Set log level
export log_level=debug
java -jar BASIC-0.2.0-jar-with-dependencies.jar program.bas

# Enable Dartmouth mode
export dartmouth=true
java -jar BASIC-0.2.0-jar-with-dependencies.jar program.bas
```

### Configuration File

The interpreter uses `application.yaml` for default configuration. If customizing, see CLAUDE.md for configuration details.

---

## Performance Tips

1. **Use direct execution mode for production**: `java -jar BASIC-*.jar -r -q program.bas`
2. **Avoid large arrays**: Pre-allocate with DIM if possible
3. **Use appropriate variable types**: `%` for integers, `$` for strings
4. **Minimize string concatenation**: Use PRINT with commas instead of `+`

---

## Getting Help

- **In-editor help**: Type `HELP` in the line editor
- **Command-line help**: `java -jar BASIC-*.jar -h`
- **Language reference**: See `doc/BASIC.md`
- **Coding standards**: See `doc/BASIC_CODING_STANDARD.md`
- **Issue reporting**: GitHub issues for bugs and feature requests

---

**End of User's Guide**