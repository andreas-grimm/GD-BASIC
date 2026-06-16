# GD-BASIC vs Apple II BASIC: Comprehensive Analysis

## Executive Summary

This document provides a detailed comparison between GD-BASIC (GriCom Diminutive BASIC Interpreter) and Apple II BASIC, specifically focusing on Applesoft BASIC, which was the standard BASIC implementation on Apple II computers from 1978 onwards. The analysis covers language features, data types, operators, control structures, built-in functions, and I/O capabilities.

**Key Findings:**
- GD-BASIC is a modern, full-featured BASIC dialect with 40+ built-in functions vs Applesoft's ~25 functions
- GD-BASIC supports multi-dimensional arrays with dynamic allocation; Apple II requires DIM declaration
- GD-BASIC offers 64-bit precision (double) for real numbers; Apple II uses 40-bit floating-point format
- Both support similar control structures (IF-THEN-ELSE, FOR-NEXT, GOSUB-RETURN) with GD-BASIC adding DO-UNTIL and WHILE-WEND
- GD-BASIC includes modern file I/O, bitwise operations, and command-line parameter support not available in Apple II

---

## 1. Program Structure

### GD-BASIC
- Programs are organized as free-form text with optional line numbers
- Line numbers (typically incremented by 10) are not strictly required but recommended for GOTO/GOSUB references
- Multiple statements per line supported using `:` separator
- Program termination requires `END` statement
- Supports `.bas` file extension
- Can be compiled to Java source code or executed directly by the interpreter

### Apple II BASIC (Applesoft)
- Line-numbered programs (1-63999) are mandatory for execution
- Line numbers serve as execution sequence markers and GOTO/GOSUB targets
- One statement per line (with rare exceptions using `:`)
- Program termination is implicit; END statement optional but recommended
- Stored in tokenized format in memory (abbreviated syntax)
- No separate compilation step; always interpreted

### Difference Assessment
**Impact**: Moderate. GD-BASIC's line number flexibility provides more modern syntax options, but both systems support the traditional numbered-line approach for backward compatibility.

---

## 2. Variable Declaration and Naming

### GD-BASIC
- **Dynamic typing with explicit type suffixes:**
  - `#` Real (64-bit IEEE double)
  - `%` Integer (32-bit signed)
  - `&` Long (64-bit signed)
  - `$` String
  - `!` Boolean
  - No suffix: defaults to Real (64-bit double)

- **Examples:**
  ```basic
  score% = 100
  name$ = "Player One"
  value# = 3.14159265359
  count& = 9223372036854775807
  active! = TRUE
  result = 42.5  ' Defaults to Real
  ```

- **Variable scope:** Global by default; subroutines share same scope
- **Arrays:** Automatically allocated (no DIM required); can have any number of dimensions
- **Array syntax:** `array%(index)`, `matrix%(row, col)`, etc.

### Apple II BASIC (Applesoft)
- **Dynamic typing with minimal type hints:**
  - `%` Integer (16-bit signed, range -32768 to 32767)
  - `$` String
  - No suffix: Real (40-bit floating-point, ~9 significant digits)

- **Examples:**
  ```basic
  SCORE% = 100
  NAME$ = "PLAYER ONE"
  VALUE = 3.14159265
  ACTIVE = -1  ' TRUE (non-zero)
  ```

- **Variable scope:** Global; subroutines share scope
- **Arrays:** Must be declared with DIM statement; limited to 2 dimensions typically
- **Array syntax:** `A(X)`, `MATRIX(ROW,COL)` with DIM A(100), DIM MATRIX(10,10) required

### Key Differences

| Feature | GD-BASIC | Apple II |
|---------|----------|----------|
| Integer range | -2,147,483,648 to 2,147,483,647 (32-bit) | -32,768 to 32,767 (16-bit) |
| Long range | 64-bit signed | Not available |
| Real precision | 64-bit (IEEE 754 double, ~15 digits) | 40-bit (~9 significant digits) |
| Type declaration | Suffix on variable name | Suffix on variable name (fewer options) |
| Array declaration | Automatic allocation | Mandatory DIM statement |
| Multi-dimensional arrays | Unlimited dimensions | Typically 2D |
| Boolean type | Explicit boolean type | Represented as -1 (TRUE) or 0 (FALSE) |

---

## 3. Operators and Expressions

### Arithmetic Operators

**Both support:**
- `+` Addition
- `-` Subtraction (binary)
- `*` Multiplication
- `/` Division (real)
- `^` or `**` Exponentiation (GD-BASIC uses `^`)

**GD-BASIC only:**
- `//` Integer division (floor division)
- `%` Modulo (remainder)
- `>>` Bitwise right shift
- `<<` Bitwise left shift

**Apple II:**
- No integer division operator (must use INT(A/B))
- No modulo operator
- No bitwise operators
- Integer division requires conversion: `INT(A/B)`

### Comparison Operators

**Both support:**
- `=` Equality (in Apple II, also assignment)
- `<>` or `!=` Not equal
- `<` Less than
- `>` Greater than
- `<=` Less than or equal
- `>=` Greater than or equal

**Difference:**
- Apple II uses `=` for both assignment and comparison in expressions
- GD-BASIC uses `=` for assignment and `==` for comparison (though `=` also works in expressions)

### Logical Operators

**Both support:**
- `AND` Logical AND
- `OR` Logical OR
- `NOT` Logical NOT

**GD-BASIC additional:**
- Explicit boolean type with `TRUE` and `FALSE` constants

### Operator Precedence

**GD-BASIC:**
- Standard BODMAS/PEMDAS order (configurable with `-d` flag for Dartmouth left-to-right evaluation)
- Parentheses, Exponentiation, Unary minus
- Multiplication, Division, Modulo
- Addition, Subtraction
- Comparison operators
- Logical NOT, AND, OR

**Apple II:**
- Simpler precedence rules
- Similar to GD-BASIC but less sophisticated operator interactions
- Parentheses override all precedence

### Difference Assessment
**Impact**: High. GD-BASIC's support for integer division, modulo, and bitwise operators provides significantly more computational capability. Lack of these operators in Apple II limits certain algorithms.

---

## 4. Control Flow Statements

### IF-THEN-ELSE Structure

**GD-BASIC:**
```basic
' Single-line IF
IF condition THEN statement1 ELSE statement2

' Block IF (multi-line)
IF condition THEN
  statements...
ELSE IF other_condition THEN
  statements...
ELSE
  statements...
END IF
```

**Apple II:**
```basic
IF condition THEN statement (no ELSE in single line)
' Must use GOTO for conditional branches
IF condition THEN GOTO 100
GOTO 200
100 REM conditional path
' ...
200 REM continue
```

**Key Differences:**
- Apple II IF-THEN requires GOTO for conditional branching; no ELSE clause
- GD-BASIC supports both single-line and block IF-THEN-ELSE structures
- GD-BASIC's block structure eliminates need for line-number jumping

### GOTO and Labels

**Both support:**
```basic
GOTO line_number  ' Jump to specified line number
100 REM Label  ' Target label
```

**Difference:**
- Both function identically for unconditional jumps
- Apple II relies more heavily on GOTO due to lack of ELSE
- GD-BASIC provides block structures to reduce GOTO usage

### GOSUB-RETURN (Subroutines)

**Both support:**
```basic
GOSUB subroutine_line
RETURN
```

**Characteristics:**
- Both push return address on stack
- Both share global variable scope
- Both support nested GOSUB calls
- GD-BASIC supports deeper nesting due to larger stack

### ON...GOTO Structure

**GD-BASIC:** Not implemented (uses computed GOTO patterns instead)

**Apple II:**
```basic
ON expression GOTO line1, line2, line3
' Jumps based on expression value (1=line1, 2=line2, etc.)
```

**Impact**: Moderate. Apple II's ON...GOTO is rarely used; GD-BASIC achieves same effect through other means.

### Difference Assessment
**Impact**: High. GD-BASIC's block IF-THEN-ELSE structure represents a significant modernization, reducing spaghetti code problems inherent in Apple II's line-number-dependent approach.

---

## 5. Loop Structures

### FOR-NEXT Loop

**GD-BASIC:**
```basic
FOR counter% = start TO end STEP increment
  statements...
NEXT counter%
' Loop parameters can be variables or expressions
FOR I% = 1 TO N% STEP S%
```

**Apple II:**
```basic
FOR I = 1 TO 100 STEP 2
  statements...
NEXT I
' Step parameter is optional (default 1)
```

**Comparison:**
- Both support identical syntax
- GD-BASIC allows loop parameters as variables/expressions
- Apple II typically requires literal values (though newer implementations allow variables)
- GD-BASIC supports nested loops with independent counters
- Both support NEXT without counter name in some versions

### DO-UNTIL Loop

**GD-BASIC:**
```basic
DO
  statements...
LOOP UNTIL condition
```

**Apple II:** Not supported

### WHILE-WEND Loop

**GD-BASIC:**
```basic
WHILE condition
  statements...
WEND
```

**Apple II:** Not supported

### Loop Control

**GD-BASIC:**
- EXIT or BREAK statements to exit loop prematurely
- CONTINUE to skip to next iteration

**Apple II:**
- No built-in exit mechanism; must use GOTO

**Difference Assessment**
**Impact**: High. GD-BASIC's DO-UNTIL and WHILE-WEND loops provide modern iteration patterns. Apple II requires FOR loops with GOTO for complex loop control.

---

## 6. Built-in Functions

### Mathematical Functions

| Function | GD-BASIC | Apple II | Notes |
|----------|----------|----------|-------|
| ABS(x) | ✅ | ✅ | Absolute value |
| SQR(x) | ✅ | ✅ | Square root |
| INT(x) | ✅ | ✅ | Integer part |
| RND | ✅ | ✅ | Random 0-1 |
| SIN(x) | ✅ | ✅ | Sine (radians) |
| COS(x) | ✅ | ✅ | Cosine (radians) |
| TAN(x) | ✅ | ✅ | Tangent (radians) |
| ATN(x) | ✅ | ✅ | Arctangent |
| EXP(x) | ✅ | ✅ | e^x |
| LOG(x) | ✅ | ✅ | Natural logarithm |
| LOG10(x) | ✅ | ❌ | Base-10 logarithm (GD-BASIC only) |
| CDBL(x) | ✅ | ❌ | Convert to double (GD-BASIC) |
| CINT(x) | ✅ | ✅ | Convert to integer |

### String Functions

| Function | GD-BASIC | Apple II | Notes |
|----------|----------|----------|-------|
| LEN(s$) | ✅ | ✅ | String length |
| LEFT$(s$,n) | ✅ | ✅ | Leftmost n characters |
| RIGHT$(s$,n) | ✅ | ✅ | Rightmost n characters |
| MID$(s$,pos,len) | ✅ | ✅ | Substring |
| STR$(n) | ✅ | ✅ | Convert number to string |
| VAL(s$) | ✅ | ✅ | Convert string to number |
| CHR$(n) | ✅ | ✅ | Character from ASCII code |
| ASC(s$) | ✅ | ✅ | ASCII code of first character |
| INSTR(s$,search$) | ✅ | ❌ | Find substring position |
| UPPER$(s$) | ❌ | ✅ | Convert to uppercase |

### File I/O Functions

| Function | GD-BASIC | Apple II | Notes |
|----------|----------|----------|-------|
| FOPEN | ✅ | ❌ | Open file for I/O |
| FCLOSE | ✅ | ❌ | Close file |
| FINPUT | ✅ | ❌ | Read line from file |
| FPRINT | ✅ | ❌ | Write line to file |
| FGET | ✅ | ❌ | Read character from file |
| FPUT | ✅ | ❌ | Write character to file |
| FPEEK | ✅ | ❌ | Preview next character |
| EOF | ✅ | ❌ | Check end-of-file (GD-BASIC) |
| FEXISTS | ✅ | ❌ | Check if file exists |
| FGETSIZE | ✅ | ❌ | File size in bytes |
| FMODTIME | ✅ | ❌ | File modification time |
| DIREXISTS | ✅ | ❌ | Check if directory exists |

**Apple II File I/O (if available):**
- OPEN/CLOSE: Sequential file access
- INPUT#/PRINT#: Line-based I/O
- GET#/PUT#: Character-based I/O
- Very limited compared to GD-BASIC

### System Functions

| Function | GD-BASIC | Apple II | Notes |
|----------|----------|----------|-------|
| TIME | ✅ | ✅ | Seconds since midnight |
| MEM | ✅ | ✅ | Available memory |
| SYSTEM(cmd) | ✅ | ❌ | Execute system command |
| CALL(url) | ✅ | ❌ | Call REST API (GD-BASIC) |
| GETCWD | ✅ | ❌ | Current working directory |
| LISTDIR | ✅ | ❌ | List directory contents |

### Difference Assessment
**Impact**: Very High. GD-BASIC includes 40+ built-in functions vs Apple II's ~25. GD-BASIC has complete modern file I/O, directory operations, and API integration capabilities absent in Apple II.

---

## 7. I/O Operations

### PRINT Statement

**GD-BASIC:**
```basic
PRINT expr1; expr2, expr3  ' ; suppresses newline, , adds spacing
PRINT "text" @ 10, 20      ' Tab stops and positioning
PRINT CHR$(27) "HOME"      ' ANSI escape sequences
```

**Apple II:**
```basic
PRINT "text"; NUM; "more"  ' ; suppresses newline
PRINT TAB(10); "text"      ' TAB function for spacing
PRINT AT(col, row); "text" ' Graphics positioning (some versions)
```

**Differences:**
- Both support semicolon for suppression of newline
- Apple II uses comma for spacing, GD-BASIC similar behavior
- GD-BASIC supports more flexible output formatting
- Neither has advanced formatting like printf-style

### INPUT Statement

**GD-BASIC:**
```basic
INPUT prompt$; variable1, variable2
INPUT variable  ' Uses default prompt
```

**Apple II:**
```basic
INPUT "Prompt:"; VARIABLE
INPUT X, Y, Z
```

**Differences:**
- Both function similarly
- GD-BASIC allows reading array elements: `INPUT A$(I%)`
- Apple II limited to simple variables

### File I/O

**GD-BASIC:**
```basic
' File operations with file handles
OPEN "filename.txt" FOR OUTPUT AS #1
PRINT #1, "data"
FPRINT 1, "data"
CLOSE #1
```

**Apple II:**
```basic
' Sequential file access (limited)
OPEN "FILENAME" AS #1 FOR INPUT
INPUT #1, X, Y
CLOSE #1
```

**Difference Assessment**
**Impact**: Very High. GD-BASIC has comprehensive file I/O with multiple access modes; Apple II has minimal file support, making data persistence difficult.

---

## 8. String Operations

### String Concatenation

**GD-BASIC:**
```basic
result$ = str1$ + str2$ + str3$
result$ = CONCAT(str1$, str2$, str3$)  ' Explicit function
```

**Apple II:**
```basic
RESULT$ = STR1$ + STR2$ + STR3$  ' Concatenation via +
```

**Identical capability** in both systems.

### String Indexing/Character Access

**GD-BASIC:**
```basic
char$ = string$[index]  ' Square bracket notation
substring$ = string$[5:10]  ' Not typically supported
```

**Apple II:**
```basic
CHAR$ = MID$(STRING$, INDEX, 1)  ' Must use MID$
```

**Difference**: GD-BASIC offers more direct character access; Apple II requires function call.

### String Comparison

**Both support:**
- `=` Equality
- `<>` Inequality
- `<` Less than (lexicographic)
- `>` Greater than
- `<=` Less than or equal
- `>=` Greater than or equal

**Apple II advantage**: Case-insensitive comparison by default (converts to uppercase internally).

### String Functions Available

**GD-BASIC additional functions:**
- INSTR(s$, search$): Find substring
- No UPPER$/LOWER$ (but can achieve via CHR$/ASC and string functions)

**Apple II advantage:**
- UPPER$(s$): Convert to uppercase (not in GD-BASIC)

---

## 9. Unsupported and Missing Features

### In GD-BASIC (Not in Apple II)
1. **Modern loop structures**: DO-UNTIL, WHILE-WEND
2. **Bitwise operators**: >>, <<, AND, OR, NOT (bitwise variants)
3. **Integer division**: //
4. **Modulo operator**: %
5. **64-bit integers**: & suffix for long type
6. **File I/O**: FOPEN, FCLOSE, FINPUT, FPRINT, FGET, FPUT, EOF, etc.
7. **Directory operations**: CHDIR, DIREXISTS, LISTDIR, GETCWD
8. **REST API**: CALL function
9. **Block control structures**: IF-THEN-ELSE-END IF, DO-UNTIL-LOOP
10. **Boolean type**: Explicit ! suffix for boolean values
11. **System interaction**: SYSTEM() function
12. **Multi-parameter functions**: Full range not available in Apple II

### In Apple II (Not in GD-BASIC)
1. **High-resolution graphics**: HGR, HPLOT (6502 assembly required)
2. **Low-resolution graphics**: GR, PLOT, HLIN, VLIN
3. **Sound**: BELL, PEEK/POKE for sound control
4. **Hardware access**: PEEK/POKE for memory inspection and modification
5. **Keyboard control**: GET key input with wait
6. **Screen control**: HOME (clear screen), VTAB, HTAB
7. **String functions**: UPPER$, LOWER$ (Apple II only in some versions)
8. **Disk operations**: CATALOG, SAVE, LOAD (handled by OS, not language)
9. **Assembly integration**: CALL to assembly routines (via line number)
10. **Machine-specific operators**: Integer division workarounds

### Difference Assessment
**Impact**: High. Apple II's graphics and hardware access capabilities are not available in GD-BASIC; GD-BASIC's modern I/O and data structure support exceed Apple II's capabilities.

---

## 10. Statement Comparison

### Supported Statements

| Statement | GD-BASIC | Apple II | Purpose |
|-----------|----------|----------|---------|
| PRINT | ✅ | ✅ | Output |
| INPUT | ✅ | ✅ | Read input |
| READ/DATA | ✅ | ✅ | Data initialization |
| IF-THEN-ELSE | ✅ | ✅* | Conditional (GD-BASIC: block; Apple II: line) |
| FOR-NEXT | ✅ | ✅ | Loop |
| DO-UNTIL | ✅ | ❌ | Loop variant |
| WHILE-WEND | ✅ | ❌ | Loop variant |
| GOTO | ✅ | ✅ | Jump |
| GOSUB-RETURN | ✅ | ✅ | Subroutine |
| END | ✅ | ✅ | Termination |
| REM | ✅ | ✅ | Comment |
| DIM | ❌** | ✅ | Array declaration |
| FOPEN-FCLOSE | ✅ | ❌ | File operations |
| CHDIR | ✅ | ❌ | Directory change |

*Apple II: Single-line only, no ELSE clause  
**GD-BASIC: Arrays auto-allocated; DIM not required

### Data Statements

**Both support:**
```basic
DATA value1, value2, value3
READ variable1, variable2
RESTORE  ' Reset READ pointer
```

---

## 11. Compatibility Assessment

### Apple II → GD-BASIC Migration Path

**Highly Compatible:**
1. Basic arithmetic and variable assignments
2. FOR-NEXT loops
3. GOSUB subroutine calls
4. Simple IF-THEN structures (converted to block form)
5. String operations (with minor function name changes)
6. Mathematical functions (except CDBL functions)
7. READ-DATA statements

**Requires Refactoring:**
1. IF-THEN with GOTO branches → IF-THEN-ELSE blocks
2. Graphics commands → Remove or comment out (not supported)
3. PEEK/POKE operations → Platform-specific handling
4. Hardware access → Remove or implement via system calls
5. Apple II-specific keywords → Map to GD-BASIC equivalents

**Example Conversion:**
```basic
' Apple II
10 REM Read and sum numbers
20 INPUT "Enter count:"; N
30 FOR I = 1 TO N
40 INPUT "Number:"; X
50 SUM = SUM + X
60 NEXT I
70 PRINT "Sum:", SUM
80 END

' GD-BASIC (Functionally equivalent)
10 REM Read and sum numbers
INPUT "Enter count:"; N%
FOR I% = 1 TO N%
  INPUT "Number:"; X
  SUM = SUM + X
NEXT I%
PRINT "Sum:"; SUM
END
```

### Estimated Compatibility
- **Syntax level**: ~70% (Apple II programs typically run with minor modifications)
- **Functional level**: ~60% (loss of graphics/hardware access reduces functionality)
- **Data processing**: ~95% (numeric and string operations nearly identical)

---

## 12. Type System Comparison

### GD-BASIC Type System

| Type | Suffix | Range | Precision | Example |
|------|--------|-------|-----------|---------|
| Real | None or # | ±1.7×10⁻³⁰⁸ to ±1.7×10³⁰⁸ | 64-bit IEEE 754 (~15 digits) | `PI = 3.14159265358979` |
| Integer | % | -2,147,483,648 to 2,147,483,647 | 32-bit signed | `COUNT% = 1000000` |
| Long | & | -9.22×10¹⁸ to 9.22×10¹⁸ | 64-bit signed | `BIG& = 9223372036854775807` |
| String | $ | 0 to 32KB+ | Variable length | `NAME$ = "Player"` |
| Boolean | ! | TRUE (-1) or FALSE (0) | 1 bit | `ACTIVE! = TRUE` |

### Apple II Type System

| Type | Suffix | Range | Precision | Example |
|------|--------|-------|-----------|---------|
| Real | None | ±1.7×10⁻⁴⁰ to ±1.7×10⁴⁰ | 40-bit (~9 digits) | `PI = 3.14159265` |
| Integer | % | -32,768 to 32,767 | 16-bit signed | `COUNT% = 32000` |
| String | $ | 0 to 255 characters | Fixed length | `NAME$ = "PLAYER"` |
| Boolean | None | -1 (TRUE) or 0 (FALSE) | Integer representation | `IF X THEN ...` |

### Type Conversion

**GD-BASIC:**
```basic
result% = CINT(3.7)      ' 3
result# = CDBL(value%)   ' Convert to real
result$ = STR$(42)       ' "42"
result = VAL("3.14")     ' 3.14
```

**Apple II:**
```basic
RESULT% = INT(3.7)       ' 3 (rounds toward zero)
RESULT$ = STR$(42)       ' "42"
RESULT = VAL("3.14")     ' 3.14
```

### Difference Assessment
**Impact**: High. GD-BASIC's 64-bit precision and 32-bit integers vs Apple II's 40-bit and 16-bit provide significantly better numeric range and accuracy for scientific/financial calculations.

---

## 13. Enhancement and Extension Areas

### Areas Where GD-BASIC Exceeds Apple II
1. **Numeric precision**: 64-bit IEEE 754 vs 40-bit (Apple II)
2. **Integer range**: 32-bit (-2B to +2B) vs 16-bit (-32K to +32K)
3. **Long integer support**: 64-bit longs not in Apple II
4. **File I/O**: Comprehensive file operations vs minimal support
5. **Directory operations**: CHDIR, DIREXISTS, LISTDIR
6. **String operations**: INSTR() for substring search
7. **Modern control structures**: Block IF-THEN-ELSE, DO-UNTIL, WHILE-WEND
8. **Bitwise operations**: >>, <<, AND, OR, NOT
9. **API integration**: CALL function for REST APIs
10. **System integration**: SYSTEM() for external commands
11. **Explicit boolean type**: ! suffix vs integer representation
12. **Array automation**: No DIM needed; automatic allocation

### Areas Where Apple II Exceeds GD-BASIC
1. **Graphics capability**: HGR, GR, HPLOT, PLOT (6502-dependent)
2. **Hardware access**: PEEK/POKE for memory manipulation
3. **Sound generation**: Built-in sound control via PEEK/POKE
4. **Keyboard input**: GET statement for raw key codes
5. **Screen control**: HOME, VTAB, HTAB built-in
6. **String case conversion**: UPPER$ function in some versions

### Potential Enhancements to GD-BASIC
1. **Graphical output support**: Graphics primitives for drawing
2. **Case conversion functions**: UPPER$(), LOWER$() functions
3. **Enhanced string manipulation**: Additional pattern matching
4. **Regular expressions**: Pattern-based string operations
5. **JSON support**: Built-in JSON parsing and generation
6. **Network operations**: TCP/IP socket support
7. **Database connectivity**: SQL query execution
8. **Floating-point options**: 32-bit float type for memory efficiency

---

## 14. Migration Strategy: Apple II → GD-BASIC

### Phase 1: Assessment
```
1. Identify all PEEK/POKE, graphics, and hardware-dependent code
2. Categorize code sections:
   - Pure BASIC (easily portable)
   - Platform-dependent (requires rewrite)
   - Graphics/Sound (needs redesign)
```

### Phase 2: Code Transformation
```
1. Convert line-numbered programs to optional numbering
2. Transform IF-THEN-GOTO sequences to IF-THEN-ELSE blocks
3. Add type suffixes to variables for clarity
4. Replace string functions:
   - MID$() stays the same
   - LEFT$/RIGHT$ identical
   - Add INSTR() for substring search
5. Convert UPPER$ to string processing logic
```

### Phase 3: Data and File Handling
```
1. Replace OPEN/INPUT# with FOPEN/FINPUT
2. Update file handles (Apple II: #1 → GD-BASIC: 1)
3. Convert DATA/READ statements (unchanged)
4. Add error handling for file operations
```

### Phase 4: Testing and Validation
```
1. Verify numeric calculations with test cases
2. Test file I/O operations
3. Validate loop constructs and control flow
4. Performance profiling on target system
```

### Example: Complete Program Migration

**Apple II Original:**
```basic
10 REM Grade Calculator
20 INPUT "Number of students:"; N
30 DIM GRADES(N), NAMES$(N)
40 FOR I = 1 TO N
50 INPUT "Name:"; NAMES$(I)
60 INPUT "Grade (0-100):"; GRADES(I)
70 NEXT I
80 SUM = 0
90 FOR I = 1 TO N
100 SUM = SUM + GRADES(I)
110 NEXT I
120 AVG = SUM / N
130 PRINT "Average grade:", AVG
140 END
```

**GD-BASIC Equivalent:**
```basic
REM Grade Calculator
INPUT "Number of students:"; N%
' Arrays auto-allocate in GD-BASIC
FOR I% = 1 TO N%
  INPUT "Name:"; NAMES$(I%)
  INPUT "Grade (0-100):"; GRADES(I%)
NEXT I%

SUM = 0
FOR I% = 1 TO N%
  SUM = SUM + GRADES(I%)
NEXT I%

AVG = SUM / N%
PRINT "Average grade:"; AVG
END
```

---

## 15. Performance Characteristics

### Execution Model

**GD-BASIC:**
- JVM-based interpreter running on modern hardware
- Multi-core capable (via Java threading)
- Memory: Modern systems (2GB+)
- CPU: Modern multi-GHz processors
- Compilation option for improved performance

**Apple II:**
- 6502/6502C processor at 1-2 MHz (original)
- 65C02 processor at 3.58 MHz (enhanced)
- Memory: 64 KB (original) to 128 KB (typical)
- Single-core, no threading
- Direct memory access via PEEK/POKE

### Performance Implications

| Operation | GD-BASIC | Apple II | Advantage |
|-----------|----------|----------|-----------|
| Loop iteration | <1 µs | ~10 µs | GD-BASIC 10-100× faster |
| String operation | <10 µs | ~100 µs | GD-BASIC 5-20× faster |
| File I/O | Variable (OS) | 1-10 ms | Depends on storage |
| Math calculation | <1 µs | ~5-100 µs | GD-BASIC 50-100× faster |
| Graphics rendering | Not supported | 10-100 ms | Apple II has capability |

### Scalability
- **GD-BASIC**: Scales to millions of iterations, large arrays (limited by JVM heap)
- **Apple II**: Limited to thousands of iterations, arrays constrained by 64-128 KB memory

---

## 16. Reference Documentation

### Apple II BASIC References
1. **Applesoft BASIC Reference Manual** - Apple Computer Inc. (1982)
2. **Apple II Reference Manual** - Apple Computer Inc. (1978)
3. **Applesoft BASIC Programmer's Reference** - Various (community documentation)
4. **Integer BASIC vs Applesoft** - Historical comparison documents

### GD-BASIC References
1. **GD-BASIC Documentation** - `/Users/Andreas/Projects/Sources/Java/GD-BASIC/BASIC_CODING_STANDARD.md`
2. **GD-BASIC Design Document** - `/Users/Andreas/Projects/Sources/Java/GD-BASIC/doc/GD-BASIC_Detailed_Design.md`
3. **GD-BASIC README** - Version history and features
4. **GD-BASIC Architecture Guide** - Technical implementation details

---

## 17. Side-by-Side Code Examples

### Example 1: Factorial Calculation

**Apple II BASIC:**
```basic
10 INPUT "Enter number:"; N
20 FACT = 1
30 FOR I = 1 TO N
40 FACT = FACT * I
50 NEXT I
60 PRINT N; "! ="; FACT
70 END
```

**GD-BASIC Equivalent:**
```basic
INPUT "Enter number:"; N%
FACT = 1
FOR I% = 1 TO N%
  FACT = FACT * I%
NEXT I%
PRINT N%; "! ="; FACT
END
```

**Differences:**
- GD-BASIC uses `%` suffix for integers (optional but recommended)
- GD-BASIC doesn't require line numbers
- Identical logic and flow

### Example 2: String Processing

**Apple II BASIC:**
```basic
10 INPUT "Enter text:"; TEXT$
20 LEN = LEN(TEXT$)
30 PRINT "Length:"; LEN
40 FOR I = 1 TO LEN
50 CHAR$ = MID$(TEXT$, I, 1)
60 PRINT "Pos "; I; ": "; CHAR$
70 NEXT I
80 END
```

**GD-BASIC Equivalent:**
```basic
INPUT "Enter text:"; TEXT$
LEN = LEN(TEXT$)
PRINT "Length:"; LEN
FOR I = 1 TO LEN
  CHAR$ = TEXT$[I]  ' Direct character access
  PRINT "Pos "; I; ": "; CHAR$
NEXT I
END
```

**Differences:**
- GD-BASIC supports direct character indexing with `[I]`
- Apple II requires MID$() function call
- GD-BASIC approach more concise

### Example 3: File Processing

**Apple II BASIC (Limited):**
```basic
10 OPEN "DATA.TXT" AS #1 FOR INPUT
20 IF EOF(#1) THEN GOTO 50
30 INPUT #1, LINE$
40 PRINT LINE$: GOTO 20
50 CLOSE #1
60 END
```

**GD-BASIC Equivalent:**
```basic
FOPEN "DATA.TXT" FOR INPUT AS 1
DO
  IF EOF(1) THEN EXIT
  FINPUT 1, LINE$
  PRINT LINE$
LOOP
FCLOSE 1
END
```

**Differences:**
- Apple II OPEN syntax vs GD-BASIC FOPEN
- GD-BASIC's DO-UNTIL/LOOP vs Apple II's GOTO-based approach
- GD-BASIC's FINPUT vs Apple II's INPUT#
- GD-BASIC's EOF returns boolean, clearer structure

### Example 4: Matrix Operations

**Apple II BASIC:**
```basic
10 DIM M(3,3)
20 FOR I = 1 TO 3
30 FOR J = 1 TO 3
40 M(I,J) = I * J
50 NEXT J
60 NEXT I
70 FOR I = 1 TO 3
80 FOR J = 1 TO 3
90 PRINT M(I,J);
100 NEXT J
110 PRINT
120 NEXT I
130 END
```

**GD-BASIC Equivalent:**
```basic
' No DIM needed; arrays auto-allocate
FOR I% = 1 TO 3
  FOR J% = 1 TO 3
    M(I%, J%) = I% * J%
  NEXT J%
NEXT I%

FOR I% = 1 TO 3
  FOR J% = 1 TO 3
    PRINT M(I%, J%);
  NEXT J%
  PRINT  ' Newline
NEXT I%
END
```

**Differences:**
- GD-BASIC eliminates DIM requirement
- GD-BASIC allows proper variable typing with %
- Identical algorithm and structure

---

## 18. Conclusion

### Key Takeaways

1. **Language Evolution**: GD-BASIC represents a modern evolution of BASIC, maintaining syntactic compatibility with Apple II while adding contemporary features like block control structures, comprehensive file I/O, and bitwise operations.

2. **Numeric Precision**: GD-BASIC's 64-bit IEEE 754 floating-point and 32-bit integer types provide significantly better precision and range than Apple II's 40-bit floats and 16-bit integers.

3. **Control Structure Modernization**: Block IF-THEN-ELSE, DO-UNTIL, and WHILE-WEND loops in GD-BASIC represent a major improvement over Apple II's line-number-dependent control flow.

4. **File I/O Capability**: GD-BASIC's comprehensive file operations (FOPEN, FCLOSE, FINPUT, FPRINT, FGET, FPUT, directory operations) far exceed Apple II's limited support.

5. **Migration Feasibility**: Most Apple II BASIC programs can be migrated to GD-BASIC with minimal modification, except those relying on graphics, sound, and hardware-level features.

6. **Backward Compatibility**: GD-BASIC maintains syntactic compatibility with Apple II programs while allowing modern practices.

### Compatibility Summary

| Aspect | Compatibility | Notes |
|--------|----------------|-------|
| Numeric operations | 95% | Minor type conversion differences |
| String operations | 90% | UPPER$ missing in GD-BASIC |
| Control flow | 85% | Block structures modernized |
| I/O operations | 50% | GD-BASIC has more capabilities |
| Graphics | 0% | Apple II only |
| Hardware access | 0% | Apple II only (PEEK/POKE) |
| **Overall** | **70-75%** | Most data-processing programs highly portable |

### Recommendation

**GD-BASIC is suitable for:**
- Educational BASIC learning (better type system, modern control structures)
- Data processing applications (numeric precision, file I/O)
- System scripting and automation
- Prototyping and algorithm development

**Apple II BASIC remains better for:**
- Retro computing and historical preservation
- Graphics and sound applications
- Hardware-level system programming
- Original Apple II system compatibility

### Future Considerations

GD-BASIC could benefit from:
- Graphics primitives for drawing (vector/raster)
- Case conversion functions (UPPER$, LOWER$)
- Enhanced file operations (buffering, formatting)
- Network and database connectivity
- Regular expression support
- Better memory profiling and optimization

This analysis demonstrates that while GD-BASIC and Apple II BASIC share common syntactic roots, GD-BASIC has evolved significantly to meet modern programming requirements while maintaining reasonable backward compatibility for data-processing applications.

---

*Analysis prepared: 2026-05-30*  
*GD-BASIC Version: 0.1.1*  
*Apple II BASIC: Applesoft BASIC (1978-1986)*
