# GD-BASIC Coding Standard

**Version:** 0.1.0  
**Interpreter:** GriCom Basic Interpreter  
**Last Updated:** 2026-04-30

## Table of Contents

1. [Program Structure](#program-structure)
2. [Variable Declaration and Types](#variable-declaration-and-types)
3. [Operators and Expressions](#operators-and-expressions)
4. [Control Flow Statements](#control-flow-statements)
5. [Loops](#loops)
6. [Functions and Subroutines](#functions-and-subroutines)
7. [Input/Output](#inputoutput)
8. [String Operations](#string-operations)
9. [Unsupported Features](#unsupported-features)
10. [Best Practices](#best-practices)

---

## Program Structure

### Line Numbers
- **Required:** All executable statements must have a line number
- **Format:** `LineNumber Statement`
- **Convention:** Use increments of 10 for flexibility in adding code
  ```basic
  10 REM This is a comment
  20 PRINT "Hello, World!"
  30 END
  ```

### Line Organization
- **Sequential:** Line numbers must be in ascending order
- **Line Separation:** Use colons (`:`) to separate multiple statements on one line
  ```basic
  10 X# = 5 : Y# = 10 : PRINT X# + Y#
  ```

### Program Termination
- **Required:** Every program must end with an `END` statement
- **Effect:** Terminates program execution
  ```basic
  999 END
  ```

---

## Variable Declaration and Types

### Variable Naming
Variables are identified by their suffix character:

| Suffix | Type | Example | Default Value |
|--------|------|---------|----------------|
| `#` | Real (floating-point) | `X#`, `VALUE#` | 0.0 |
| `!` | Double | `RESULT!` | 0.0 |
| `%` | Integer | `COUNT%`, `INDEX%` | 0 |
| `&` | Long | `BIG&` | 0 |
| `$` | String | `NAME$`, `MESSAGE$` | "" |
| `@` | Boolean | `FLAG@` | False |
| *(none)* | Untyped | `X`, `Y` | 0 |

### Variable Declaration

**Implicit Declaration:**
- Variables are created on first use
- Type is inferred from suffix
  ```basic
  10 X# = 3.14159
  20 NAME$ = "John"
  30 COUNT% = 42
  ```

### Valid Variable Names
- Alphanumeric characters (A-Z, 0-9)
- Underscores (_)
- Must start with a letter
- Case-insensitive
  ```basic
  10 TOTAL_SUM# = 100
  20 firstName$ = "Alice"
  30 my_flag@ = true
  ```

### Array Variables

**Arrays ARE Supported** ✅

This BASIC interpreter supports arrays with automatic allocation:
- **No DIM required** - Arrays are created on first use (DIM statement not supported)
- **Array syntax** - Use parentheses for indexing: `VARIABLE(index)`
- **Auto-allocation** - Array elements are created automatically when referenced
- **Type suffixes apply** - Array elements inherit the type from the variable suffix

**Array Examples:**
```basic
10 REM Arrays work without DIM declaration
20 F#(0) = 0
30 F#(1) = 1
40 F#(2) = F#(0) + F#(1)
50 PRINT F#(2)

100 REM Array with string elements
110 NAME$(1) = "Alice"
120 NAME$(2) = "Bob"
130 PRINT NAME$(1), NAME$(2)

200 REM Array with loop
210 FOR I% = 1 TO 10
220   VALUES#(I%) = I% * 2
230 NEXT
240 PRINT VALUES#(5)           REM Output: 10
```

**Multi-dimensional access:**
```basic
10 REM Simulate 2D arrays using string notation
20 DATA$(1,1) = "A1"
30 DATA$(1,2) = "A2"
40 DATA$(2,1) = "B1"
```

**Notes:**
- Arrays expand dynamically as needed
- No size limits need to be pre-declared
- Negative indices are allowed
- Each array element follows the type rules of its parent variable

---

## Operators and Expressions

### Arithmetic Operators

| Operator | Operation | Example | Result |
|----------|-----------|---------|--------|
| `+` | Addition | `5 + 3` | 8 |
| `-` | Subtraction | `10 - 4` | 6 |
| `*` | Multiplication | `6 * 7` | 42 |
| `/` | Division | `20 / 4` | 5 |
| `^` | Exponentiation | `2 ^ 3` | 8 |
| `%` (MOD) | Modulo | `17 % 5` | 2 |

### Comparison Operators

| Operator | Meaning | Example |
|----------|---------|---------|
| `=` or `==` | Equal to | `X# = 5` |
| `<>` or `!=` | Not equal to | `X# <> 10` |
| `<` | Less than | `X# < 20` |
| `>` | Greater than | `X# > 0` |
| `<=` | Less than or equal | `X# <= 100` |
| `>=` | Greater than or equal | `X# >= 50` |

### Logical Operators

| Operator | Operation | Example |
|----------|-----------|---------|
| `AND` | Logical AND | `IF X# > 0 AND X# < 10` |
| `OR` | Logical OR | `IF X# < 0 OR X# > 100` |
| `NOT` | Logical NOT | `IF NOT FLAG@` |

### Bitwise Operators

| Operator | Operation | Example |
|----------|-----------|---------|
| `<<` | Bit shift left | `4 << 1` (result: 8) |
| `>>` | Bit shift right | `8 >> 1` (result: 4) |

### Operator Precedence (highest to lowest)
1. `^` (Exponentiation) - right associative
2. `NOT` (Logical NOT)
3. `*`, `/`, `%` (Multiplication, Division, Modulo)
4. `+`, `-` (Addition, Subtraction)
5. `<<`, `>>` (Bitwise shifts)
6. `<`, `>`, `<=`, `>=` (Comparisons)
7. `=`, `==`, `<>`, `!=` (Equality)
8. `AND` (Logical AND)
9. `OR` (Logical OR)

### Expression Examples
```basic
10 RESULT# = 1 + 2 * 3           REM Result: 7 (not 9)
20 POWER# = 2 ^ 3 ^ 2            REM Result: 512 (right associative: 2^(3^2))
30 CHECK@ = X# > 0 AND X# < 10   REM Compound condition
```

---

## Control Flow Statements

### IF-THEN-ELSE Statement

**Single-line syntax:**
```basic
10 IF X# > 0 THEN PRINT "Positive"
```

**Block syntax:**
```basic
10 IF X# > 5 THEN
20   PRINT "X is greater than 5"
30 ELSE
40   PRINT "X is 5 or less"
50 END-IF
```

**Notes:**
- `THEN` is required after the condition
- `ELSE` clause is optional
- `END-IF` marks the end of the block
- For single-line, if the next statement is a line number, use `THEN` with a goto

### GOTO Statement
```basic
10 PRINT "Start"
20 GOTO 50
30 PRINT "This is skipped"
50 PRINT "Continue here"
```

### IF-THEN with GOTO
```basic
10 IF N# < 20 THEN 100
20 PRINT "Done"
30 END
100 REM Process more
```

---

## Loops

### FOR-NEXT Loop

**Syntax:**
```basic
FOR Variable = StartValue TO EndValue [STEP StepValue]
  Statements...
NEXT
```

**Example:**
```basic
10 FOR I# = 1 TO 10
20   PRINT I#
30 NEXT
```

**With STEP:**
```basic
10 FOR X# = 10 TO 0 STEP -1
20   PRINT X#
30 NEXT
```

**Notes:**
- Default STEP is 1
- STEP can be negative for counting down
- Loop variable should match the variable in NEXT statement
- NEXT is required to complete the loop

### WHILE-END-WHILE Loop

**Syntax:**
```basic
WHILE Condition
  Statements...
END-WHILE
```

**Example:**
```basic
10 X# = 0
20 WHILE X# < 10
30   PRINT X#
40   X# = X# + 1
50 END-WHILE
```

### DO-UNTIL Loop

**Syntax:**
```basic
DO
  Statements...
UNTIL Condition
```

**Example:**
```basic
10 X# = 0
20 DO
30   PRINT X#
40   X# = X# + 1
50 UNTIL X# > 5
```

**Notes:**
- DO-UNTIL executes at least once before checking the condition
- Condition is checked after each iteration
- Loop continues while condition is false, exits when true

---

## Functions and Subroutines

### GOSUB-RETURN (Subroutines)

**Syntax:**
```basic
GOSUB LineNumber
...
LineNumber
  Subroutine statements...
RETURN
```

**Example:**
```basic
10 GOSUB 100
20 PRINT "Back from subroutine"
30 END

100 PRINT "In subroutine"
110 RETURN
```

**Notes:**
- GOSUB jumps to a line number and stores the return location
- RETURN jumps back to the line after GOSUB
- Useful for code reuse

### Built-in Functions

#### Mathematical Functions
| Function | Purpose | Example |
|----------|---------|---------|
| `ABS(X)` | Absolute value | `ABS(-5)` → 5 |
| `SQR(X)` | Square root | `SQR(16)` → 4 |
| `SIN(X)` | Sine (radians) | `SIN(3.14159/2)` → 1 |
| `COS(X)` | Cosine (radians) | `COS(0)` → 1 |
| `TAN(X)` | Tangent (radians) | `TAN(0)` → 0 |
| `ATN(X)` | Arctangent (radians) | `ATN(1)` → 0.785398 |
| `EXP(X)` | e^X | `EXP(1)` → 2.71828 |
| `LOG(X)` | Natural logarithm | `LOG(2.71828)` → 1 |
| `LOG10(X)` | Base-10 logarithm | `LOG10(100)` → 2 |

#### String Functions
| Function | Purpose | Example |
|----------|---------|---------|
| `LEN(S$)` | String length | `LEN("Hello")` → 5 |
| `LEFT$(S$, N)` | Leftmost N characters | `LEFT$("Hello", 2)` → "He" |
| `RIGHT$(S$, N)` | Rightmost N characters | `RIGHT$("Hello", 2)` → "lo" |
| `MID$(S$, Start, Length)` | Substring | `MID$("Hello", 2, 3)` → "ell" |
| `CHR$(N)` | Character from ASCII code | `CHR$(65)` → "A" |
| `ASC(S$)` | ASCII code of character | `ASC("A")` → 65 |
| `VAL(S$)` | String to number | `VAL("123")` → 123 |
| `STR$(X)` | Number to string | `STR$(123)` → "123" |

#### Utility Functions
| Function | Purpose | Example |
|----------|---------|---------|
| `RND()` | Random number 0-1 | `RND()` → 0.42376 |
| `MEM()` | Available memory bytes | `MEM()` → 605006424 |
| `TIME()` | Current time in seconds | `TIME()` → 1234567890 |

#### Type Conversion Functions
| Function | Purpose | Example |
|----------|---------|---------|
| `CINT(X)` | Convert to integer | `CINT(3.7)` → 3 |
| `CDBL(X)` | Convert to double | `CDBL(5)` → 5.0 |

---

## Input/Output

### PRINT Statement

**Basic syntax:**
```basic
PRINT Expression [, Expression] [, Expression]
```

**Examples:**
```basic
10 PRINT "Hello"
20 PRINT X#, Y#, Z#
30 PRINT "Sum is"; SUM#
```

**Formatting:**
- **Comma (`,`)** - aligns output in columns
- **Semicolon (`;`)** - prints without line break (no newline)
- **No separator** - prints with newline after

**Examples:**
```basic
10 PRINT "A", "B", "C"     REM Columns
20 PRINT "X="; X#;         REM No newline after X#
30 PRINT ", Y="; Y#        REM Continues on same line
```

### INPUT Statement

**Syntax:**
```basic
INPUT Variable
```

**Example:**
```basic
10 PRINT "Enter your name:"
20 INPUT NAME$
30 PRINT "Hello, "; NAME$
```

**Notes:**
- Waits for user input
- Stores input in the specified variable
- Type is determined by variable suffix

---

## String Operations

### String Literals
- Enclosed in double quotes
- Example: `"Hello, World!"`
- To include quotes, use two quotes: `"He said ""Hi"""`

### String Concatenation
- Use string functions like `LEFT$`, `RIGHT$`, `MID$`
- Concatenate in PRINT with multiple expressions

**Example:**
```basic
10 FIRST$ = "John"
20 LAST$ = "Doe"
30 PRINT FIRST$; " "; LAST$    REM Output: John Doe
```

### String Arrays (Simulated)
- Use `LEFT$()`, `RIGHT$()`, `MID$()` to extract characters
  ```basic
  10 TEXT$ = "ABCDE"
  20 PRINT MID$(TEXT$, 2, 2)    REM Output: BC
  ```

---

## Unsupported Features

### NOT IMPLEMENTED:

#### DIM Statement (Arrays Don't Need Pre-declaration)
- ❌ **DIM statement** - Not supported (not needed because arrays auto-allocate)
- ✅ **Array indexing** - Fully supported without DIM
- ✅ **Dynamic arrays** - Arrays grow automatically as needed

**What You CAN Do:**
```basic
10 X# = 5              REM Scalar variable
20 Y$ = "Hello"        REM Scalar string
30 Z% = 10             REM Scalar integer
40 A#(5) = 42          REM SUPPORTED - array element assignment
50 B$(1) = "Test"      REM SUPPORTED - array string element
60 FOR I% = 1 TO 10
70   C#(I%) = I% * 2   REM SUPPORTED - array in loop
80 NEXT
```

**What You CANNOT Do:**
```basic
10 DIM A#(100)         REM NOT SUPPORTED - DIM not available
REM But arrays work without it:
20 A#(100) = 999       REM This works fine without DIM
```

#### Other Unsupported Features
- **READ/DATA** - Data blocks (limited support)
- **LET** - Statement keyword (use direct assignment instead)
  ```basic
  10 X# = 5          REM Use this
  REM Not: LET X# = 5
  ```
- **User-defined functions** - Use GOSUB for subroutines instead
- **File I/O** - Advanced operations like APPEND, SEEK, random access (see FILE_IO_STATUS.md)

---

## Best Practices

### 1. Line Numbering
```basic
✓ GOOD:
10 REM Main program
20 PRINT "Start"
30 FOR I# = 1 TO 10
40   PRINT I#
50 NEXT
999 END

✗ BAD:
1 PRINT "Start"
2 FOR I# = 1 TO 10
3 PRINT I#
4 NEXT
```

### 2. Comments
```basic
✓ GOOD:
10 REM Initialize variables
20 X# = 0
30 Y# = 100
40 REM Calculate sum
50 SUM# = X# + Y#

✗ BAD:
10 X# = 0           REM No space before comment
```

### 3. Variable Naming
```basic
✓ GOOD:
10 TOTAL_PRICE# = 99.99
20 CUSTOMER_NAME$ = "Alice"
30 ITEM_COUNT% = 5

✗ BAD:
10 X# = 99.99                REM Non-descriptive
20 N$ = "Alice"              REM Single letter
```

### 4. Indentation (for readability)
```basic
✓ GOOD:
10 FOR I# = 1 TO 10
20   IF I# MOD 2 = 0 THEN
30     PRINT "Even: "; I#
40   ELSE
50     PRINT "Odd: "; I#
60   END-IF
70 NEXT

✗ BAD:
10 FOR I# = 1 TO 10
20 IF I# MOD 2 = 0 THEN
30 PRINT "Even: "; I#
40 ELSE
50 PRINT "Odd: "; I#
60 END-IF
70 NEXT
```

### 5. Use Meaningful Conditionals
```basic
✓ GOOD:
10 IF SCORE# >= 90 THEN
20   PRINT "Grade: A"
30 END-IF

✗ BAD:
10 IF S# >= 90 THEN
20   PRINT "Grade: A"
30 END-IF
```

### 6. Avoid Deep Nesting
```basic
✓ GOOD (use GOSUB for reusable logic):
10 GOSUB 100          REM Validate input
20 IF VALID@ THEN GOSUB 200    REM Process
30 END

100 REM Validate subroutine
110 VALID@ = true
120 RETURN

200 REM Process subroutine
210 PRINT "Processing..."
220 RETURN

✗ BAD (deeply nested):
10 IF X# > 0 THEN
20   IF X# < 100 THEN
30     IF Y# > 0 THEN
40       IF Y# < 100 THEN
50         PRINT "Valid"
60       END-IF
70     END-IF
80   END-IF
90 END-IF
```

### 7. Loop Variables
```basic
✓ GOOD:
10 FOR INDEX% = 1 TO 100
20   PRINT INDEX%
30 NEXT

✗ BAD:
10 FOR I# = 1 TO 100    REM Using floating point for index
20   PRINT I#
30 NEXT
```

### 8. Error Handling with Conditionals
```basic
✓ GOOD:
10 INPUT VALUE#
20 IF VALUE# = 0 THEN
30   PRINT "Error: Cannot divide by zero"
40 ELSE
50   RESULT# = 100 / VALUE#
60   PRINT "Result: "; RESULT#
70 END-IF

✗ BAD:
10 INPUT VALUE#
20 RESULT# = 100 / VALUE#      REM No error check
30 PRINT "Result: "; RESULT#
```

---

## Summary

This BASIC interpreter supports a substantial subset of BASIC functionality with **arrays and scalars**:

### What IS Supported ✅
- **Scalar variables** with type suffixes (# for real, $ for string, % for integer, etc.)
- **Arrays with auto-allocation** - Array elements are created on first use (no DIM needed)
- Arithmetic and logical operations
- Control flow statements (IF-THEN-ELSE, GOTO)
- Loop constructs (FOR-NEXT, WHILE-END-WHILE, DO-UNTIL)
- Subroutines (GOSUB-RETURN)
- 20+ built-in mathematical and string functions
- Input/output operations (PRINT, INPUT, file I/O)
- String manipulation (LEN, LEFT$, RIGHT$, MID$, etc.)

### What IS NOT Supported ❌
- **DIM statement** - Not available (not needed because arrays auto-allocate)
- **Pre-declaration of array sizes** - Arrays grow dynamically as needed
- User-defined functions (use GOSUB instead)
- Advanced file I/O (append mode, random access, seek operations)

### Design Philosophy
This interpreter supports **both scalar and array variables** with automatic memory allocation. Unlike traditional BASIC, there is no need to declare array sizes with DIM—arrays grow dynamically. This simplifies array usage and eliminates memory pre-planning requirements.

### Key Differences from Traditional BASIC
- ✅ **No DIM required** - Arrays work immediately without declaration
- ✅ **Dynamic sizing** - Arrays grow as needed
- ✅ **Simpler syntax** - No memory management needed
- ❌ **No DIM support** - The DIM keyword is not recognized (but not needed)

For examples and test programs, see the `src/test/basic/` directory in the project repository.
