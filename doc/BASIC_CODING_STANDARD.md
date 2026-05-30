# GD-BASIC Coding Standard

**Version:** 0.1.1  
**Interpreter:** GriCom Basic Interpreter  
**Last Updated:** 2026-05-25

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

This BASIC interpreter supports arrays with automatic allocation and expression-based indices:
- **No DIM required** - Arrays are created on first use (DIM statement not supported)
- **Array syntax** - Use parentheses for indexing: `VARIABLE(index)`
- **Auto-allocation** - Array elements are created automatically when referenced
- **Type suffixes apply** - Array elements inherit the type from the variable suffix
- **Expression indices** - Array indices can include mathematical expressions (requires proper spacing)

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

**Array Indices with Expressions** (NEW)

The interpreter supports calculated expressions as array indices, enabling dynamic array access:

```basic
10 REM Expression-based array indices
20 F%(0) = 0
30 F%(1) = 1
40 N% = 2
50 F%(N%) = F%(N% - 1) + F%(N% - 2)    REM Calculate Fibonacci
60 PRINT F%(N%)
```

**Operator Spacing in Array Indices (Automatic Normalization)**

The Normalizer automatically normalizes operator spacing inside parentheses during preprocessing. This means the interpreter accepts flexible spacing in array index expressions:

| Format | Status | Example | Notes |
|--------|--------|---------|-------|
| Spaces around operators | ✅ Works | `A$(X% + 1)` | Normalized to standard form |
| No spaces | ✅ Works | `A$(X%+1)` | Automatically spaced by Normalizer |
| Mixed spacing | ✅ Works | `A$(X% +1)` | Normalized to consistent spacing |

**How Normalization Works:**

The Normalizer preprocesses each BASIC line before parsing:
1. Detects expressions inside parentheses `(` and `)`
2. Automatically adds spaces around arithmetic operators: `+`, `-`, `*`, `/`, `^`, `&`, `|`
3. Preserves multi-character operators: `>=`, `<=`, `!=`, `<<`, `>>`
4. Handles unary operators correctly (e.g., `-1` stays intact)

**Examples (All equivalent after normalization):**
```basic
10 X% = 5
20 ARR%(X%+1) = 100          REM No spaces: normalized automatically
30 ARR%(X% + 1) = 100        REM Already spaced: kept as-is
40 ARR%(X% +1) = 100         REM Mixed spacing: normalized
```

All three statements above are automatically converted to the same normalized form: `ARR%( X + 1 ) = 100`

**Unary Operator Handling:**

Negative literals are preserved correctly:
```basic
10 RESULT# = ABS(-5)          REM ✅ Negative sign preserved
20 ARR%(0) = -10              REM ✅ Unary minus handled correctly
30 ARR%(I% - 1) = 0           REM ✅ Binary minus gets spaces: I -  1
```

**Notes:**
- Arrays expand dynamically as needed
- No size limits need to be pre-declared
- Negative indices are allowed
- Each array element follows the type rules of its parent variable
- While flexible spacing is now supported, **clear spacing in source code is still recommended for readability** (e.g., `X% + 1` is clearer than `X%+1`)

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

### File I/O Operations

#### Basic File Operations

**FOPEN - Open File**
```basic
FOPEN fileId, "filename", "mode"
```
- **fileId** (int): Unique file identifier (1-256)
- **filename** (string): Path to file
- **mode** (string): "r" (read), "w" (write), or "a" (append)
- **Example**: `FOPEN 1 "data.txt" "r"`

**FCLOSE - Close File**
```basic
FCLOSE fileId [, "DELETE"]
```
- **fileId** (int): File identifier
- **"DELETE"** (optional): Delete file when closing
- **Example**: `FCLOSE 1 ""`  or  `FCLOSE 1 "DELETE"`

**FINPUT - Read Line from File**
```basic
FINPUT fileId, variableName
```
- Reads entire line from file
- Stores in specified variable
- **Example**: `FINPUT 1, LINE$`

**FPRINT - Write Line to File**
```basic
FPRINT fileId, expression
```
- Writes expression followed by newline
- **Example**: `FPRINT 1, "Hello"`

**EOF - Check End of File**
```basic
IF EOF(fileId) THEN ...
```
- Returns TRUE if at end of file
- **Example**: `WHILE NOT EOF(1)`

#### Character-Level File Operations

**FGET - Read Single Character (Advances Position)**
```basic
FGET fileId, variableName
```
- **Purpose**: Read one character and advance file position
- **Returns**: Single character as string or "EOF"
- **Advances**: Read position by 1 character
- **Example**:
```basic
10 FOPEN 1 "input.txt" "r"
20 FGET 1, C$
30 PRINT "Read: "; C$
40 FCLOSE 1
```

**FPUT - Write Character Without Newline**
```basic
FPUT fileId, expression
```
- **Purpose**: Write character/string without line terminator
- **Parameters**: Expression evaluates to string to write
- **Newline**: NOT added (use FPRINT for newline)
- **Example**:
```basic
10 FOPEN 1 "output.txt" "w"
20 FPUT 1, "H"
30 FPUT 1, "i"
40 FPRINT 1, ""          ! Add newline
50 FCLOSE 1
```

**FPEEK - Peek at Next Character (No Advance)**
```basic
FPEEK fileId, variableName
```
- **Purpose**: Read next character WITHOUT advancing position
- **Returns**: Next character or "EOF"
- **Position**: NOT advanced (lookahead operation)
- **Example**:
```basic
10 FOPEN 1 "data.txt" "r"
20 FPEEK 1, C$           ! Look ahead
30 IF C$ = "X" THEN GOSUB 1000
40 FGET 1, C$            ! Actually read it
50 FCLOSE 1
```

**Key Differences - Character I/O**:
| Operation | Advances Position | Returns |
|-----------|-------------------|---------|
| FGET | ✅ Yes (+1) | Next character |
| FPUT | N/A (write) | N/A |
| FPEEK | ❌ No | Next character |

#### File Management Operations

**FRENAME - Rename/Move File**
```basic
FRENAME fileId, "newFileName"
```
- **Purpose**: Rename or move file tracked by file ID
- **fileId**: ID of file to rename
- **newFileName**: New filename/path
- **FileID**: Remains valid after rename
- **Content**: Preserved during rename
- **Example**:
```basic
10 FOPEN 1 "temp.txt" "w"
20 FPRINT 1, "Data"
30 FCLOSE 1
40 FRENAME 1, "final.txt"    ! Rename file
50 IF FEXISTS("final.txt") THEN PRINT "Success"
```

**FREWIND - Reset File Position**
```basic
FREWIND fileId
```
- **Purpose**: Reset read position to beginning
- **File State**: Remains open (no close/reopen)
- **Efficiency**: Better than FCLOSE/FOPEN
- **Example**:
```basic
10 FOPEN 1 "data.txt" "r"
20 PRINT "First read:"
30 GOSUB 100
40 FREWIND 1                 ! Go back to start
50 PRINT "Second read:"
60 GOSUB 100
70 FCLOSE 1
80 END
100 REM Read file content
110 WHILE NOT EOF(1)
120    FINPUT 1, LINE$
130    PRINT LINE$
140 WEND
150 RETURN
```

#### Advanced File Functions

**FEXISTS - Check File Existence**
```basic
IF FEXISTS("filename") THEN ...
```
- Returns TRUE if file exists
- **Example**: `IF FEXISTS("backup.txt") THEN DELETE`

**FGETSIZE - Get File Size**
```basic
SIZE = FGETSIZE("filename")
```
- Returns file size in bytes
- **Example**: `SIZE = FGETSIZE("data.bin")`

**FGETNAME - Get File Name from ID**
```basic
FILENAME$ = FGETNAME(fileId)
```
- Retrieves filename associated with file ID
- **Example**: `CURRENT$ = FGETNAME(1)`

**DIREXISTS - Check Directory Existence**
```basic
IF DIREXISTS("dirname") THEN ...
```
- Returns TRUE if directory exists
- **Example**: `IF DIREXISTS("backup") THEN ...`

#### File I/O Examples

**Example 1: Copy File**
```basic
10 SRC$ = "original.txt"
20 DST$ = "backup.txt"
30 IF NOT FEXISTS(SRC$) THEN PRINT "Source not found": END
40 
50 SRC_ID = FOPEN 1 SRC$ "r"
60 DST_ID = FOPEN 2 DST$ "w"
70 
80 WHILE NOT EOF(1)
90     FINPUT 1, LINE$
100    FPRINT 2, LINE$
110 WEND
120
130 FCLOSE 1 ""
140 FCLOSE 2 ""
150 PRINT "Copy complete"
160 END
```

**Example 2: Character-by-Character Processing**
```basic
10 FOPEN 1 "input.txt" "r"
20 FOPEN 2 "output.txt" "w"
30
40 WHILE NOT EOF(1)
50     FGET 1, C$          ! Read character
60     IF C$ != "EOF" THEN
70         IF C$ = " " THEN
80             FPUT 2, "_" ! Replace space with underscore
90         ELSE
100            FPUT 2, C$ ! Keep original
110        END IF
120    END IF
130 WEND
140
150 FCLOSE 1 ""
160 FCLOSE 2 ""
170 END
```

**Example 3: Search with Lookahead**
```basic
10 FOPEN 1 "search.txt" "r"
20 FOUND% = 0
30
40 WHILE NOT EOF(1)
50     FPEEK 1, C$         ! Look ahead
60     IF C$ = "X" THEN
70         FGET 1, CHAR$   ! Consume it
80         FOUND% = FOUND% + 1
90         PRINT "Found X at position"; FOUND%
100    ELSE
110        FGET 1, DUMMY$  ! Skip character
120    END IF
130 WEND
140
150 FCLOSE 1 ""
160 PRINT "Total X's found: "; FOUND%
170 END
```

**Example 4: Multi-pass File Processing**
```basic
10 FOPEN 1 "report.txt" "r"
20
30 PRINT "Analysis:"
40 PRINT "Pass 1 - Count lines:"
50 GOSUB 1000
60
70 FREWIND 1              ! Go back to start
80 PRINT "Pass 2 - Process content:"
90 GOSUB 2000
100
110 FCLOSE 1 ""
120 END
130
1000 REM Count lines
1010 COUNT% = 0
1020 WHILE NOT EOF(1)
1030    FINPUT 1, LINE$
1040    COUNT% = COUNT% + 1
1050 WEND
1060 PRINT "Total lines: "; COUNT%
1070 RETURN
1080
2000 REM Process content
2010 WHILE NOT EOF(1)
2020    FINPUT 1, LINE$
2030    PRINT ">> "; LINE$
2040 WEND
2050 RETURN
```

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
