# GD-BASIC vs Commodore PET BASIC Analysis

**Version:** 1.0  
**Date:** 2026-05-30  
**Component:** GD-BASIC Interpreter (v0.1.1)  
**Reference:** Commodore PET BASIC (CBM 3.0 / BASIC 2.0 era)

---

## Executive Summary

GD-BASIC is a modern BASIC interpreter inspired by Dartmouth BASIC and BASIC 2.0 (Commodore PET era), with significant enhancements beyond the original specification. While maintaining compatibility with classic BASIC syntax and semantics, GD-BASIC introduces contemporary features such as:

- **Rich type system** with explicit type suffixes (vs implicit single-precision floats)
- **Advanced control flow** (block IF/ELSE/END-IF structures)
- **Expression-based array indexing** with full operator precedence
- **Comprehensive file I/O** (19 file operations vs. 2-3 basic operations)
- **Modern operator precedence** (BODMAS/PEMDAS vs left-to-right evaluation)
- **40+ built-in functions** (vs ~20-30 in original BASIC)

This analysis documents compatibility, differences, and enhancements.

---

## 1. Program Structure

### Line Numbers

| Aspect | GD-BASIC | Commodore PET BASIC | Status |
|--------|----------|-------------------|--------|
| Required | Yes, all statements | Yes, all statements | ✅ Compatible |
| Format | `LineNumber Statement` | `LineNumber Statement` | ✅ Compatible |
| Range | 0-65535 | 0-63999 | ⚠️ GD-BASIC extends range |
| Ordering | Must be ascending | Must be ascending | ✅ Compatible |
| Convention | Increment by 10 | Increment by 10 | ✅ Compatible |

**Compatibility**: GD-BASIC uses 32-bit line numbers vs 16-bit in PET BASIC, but maintains the same semantic rules.

### Multiple Statements Per Line

| Feature | GD-BASIC | Commodore PET BASIC | Status |
|---------|----------|-------------------|--------|
| Colon separator | `A:B:C` | `A:B:C` | ✅ Compatible |
| Usage | `10 X=5 : Y=10 : PRINT X+Y` | `10 X=5 : Y=10 : PRINT X+Y` | ✅ Compatible |

**Compatibility**: Identical syntax and semantics.

### Program Termination

| Feature | GD-BASIC | Commodore PET BASIC | Status |
|---------|----------|-------------------|--------|
| END statement | Required | Required | ✅ Compatible |
| Effect | Stops execution | Stops execution | ✅ Compatible |

**Compatibility**: Identical behavior.

---

## 2. Variable Declaration and Types

### Type System

| Suffix | Type | GD-BASIC | Commodore PET | Status |
|--------|------|----------|---------------|--------|
| `%` | Integer | 32-bit signed | 16-bit signed | ⚠️ Extended range |
| `&` | Long | 64-bit signed | Not standard | ❌ GD-BASIC only |
| `#` | Real (Double) | IEEE 754 (64-bit) | Single-precision (32-bit) | ⚠️ Higher precision |
| `!` | Boolean | true/false | Not standard | ❌ GD-BASIC only |
| `$` | String | Unlimited | Unlimited | ✅ Compatible |
| *(none)* | Untyped | Real (single-precision) | Single-precision | ✅ Compatible |

**Key Differences**:
- **Extended integer range**: GD-BASIC uses 32-bit integers vs 16-bit (PET range: -32768 to 32767)
- **Additional types**: GD-BASIC adds Long (`&`), Boolean (`!`), and Double (`#`) type suffixes
- **Higher precision**: Real numbers use 64-bit doubles vs 32-bit singles in PET BASIC
- **Type coercion**: GD-BASIC supports explicit conversions (CINT, CDBL, etc.); PET has implicit coercion

### Variable Declaration

| Aspect | GD-BASIC | Commodore PET BASIC | Status |
|--------|----------|-------------------|--------|
| Declaration | Implicit on first use | Implicit on first use | ✅ Compatible |
| Type inference | From suffix character | From suffix character | ✅ Compatible |
| DIM arrays | Optional (not required) | Required for arrays | ⚠️ GD-BASIC auto-allocates |
| Default values | Type-dependent (0 for numbers, "" for strings) | 0 for numbers, "" for strings | ✅ Compatible |

**Compatibility**: Implicit declaration and type inference work the same way, but GD-BASIC eliminates the need for DIM statements.

### Array Support

| Feature | GD-BASIC | Commodore PET | Status |
|---------|----------|---------------|--------|
| DIM required | No (auto-allocate) | Yes, mandatory | ⚠️ GD-BASIC more flexible |
| Dimensions | Multi-dimensional | Multi-dimensional | ✅ Compatible |
| Expression indices | Supported with operator spacing | Literal indices or simple variables | ⚠️ GD-BASIC enhanced |
| Index syntax | `ARR%(expr)` | `ARR(expr)` | ⚠️ Different syntax due to types |
| Negative indices | Supported | Not documented | ⚠️ GD-BASIC permits |
| Dynamic expansion | Automatic | Pre-declared with DIM | ⚠️ GD-BASIC dynamic |

**Key Differences**:
```basic
' Commodore PET BASIC (requires DIM)
10 DIM A(10)
20 A(5) = 42

' GD-BASIC (no DIM needed)
10 A%(5) = 42     ' Auto-allocated
```

GD-BASIC supports expression-based indices with automatic normalization:
```basic
10 X% = 3
20 ARR%(X% + 1) = 100     ' Works (normalized automatically)
30 ARR%(X%+1) = 100       ' Also works (spacing added by Normalizer)
```

---

## 3. Operators and Expressions

### Arithmetic Operators

| Operator | GD-BASIC | PET BASIC | Precedence* | Status |
|----------|----------|-----------|-------------|--------|
| `+` | Addition | Addition | 6 | ✅ Compatible |
| `-` | Subtraction | Subtraction | 6 | ✅ Compatible |
| `*` | Multiplication | Multiplication | 7 | ✅ Compatible |
| `/` | Division | Division | 7 | ✅ Compatible |
| `^` | Exponentiation | Exponentiation | 8 | ✅ Compatible |
| `%` | Modulo | Not standard | 7 | ❌ GD-BASIC only |
| `//` | Integer division | Not standard | 7 | ❌ GD-BASIC only |

*Precedence: Higher number = higher precedence

### Comparison Operators

| Operator | GD-BASIC | PET BASIC | Status |
|----------|----------|-----------|--------|
| `=` | Assignment/Equality (context-sensitive) | Assignment/Equality (context-sensitive) | ✅ Compatible |
| `==` | Equality (explicit) | Not standard | ⚠️ GD-BASIC for clarity |
| `!=` | Not equal | Not standard | ❌ GD-BASIC only |
| `<` | Less than | Less than | ✅ Compatible |
| `>` | Greater than | Greater than | ✅ Compatible |
| `<=` | Less than or equal | Less than or equal | ✅ Compatible |
| `>=` | Greater than or equal | Greater than or equal | ✅ Compatible |

### Logical Operators

| Operator | GD-BASIC | PET BASIC | Status |
|----------|----------|-----------|--------|
| `AND` | Logical AND | Logical AND | ✅ Compatible |
| `OR` | Logical OR | Logical OR | ✅ Compatible |
| `NOT` | Logical NOT | Logical NOT | ✅ Compatible |

### Bitwise Operators

| Operator | GD-BASIC | PET BASIC | Status |
|----------|----------|-----------|--------|
| `&` | Bitwise AND / Long type suffix | Not standard | ❌ GD-BASIC only |
| `\|` | Bitwise OR | Not standard | ❌ GD-BASIC only |
| `<<` | Bit shift left | Not standard | ❌ GD-BASIC only |
| `>>` | Bit shift right | Not standard | ❌ GD-BASIC only |

### Operator Precedence

**GD-BASIC** (BODMAS/PEMDAS - correct mathematical precedence):
```
Level 1: OR (||, OR)
Level 2: AND (&&, AND)
Level 3: Equality (==, !=, =)
Level 4: Comparison (<, <=, >, >=)
Level 5: Bitwise Shift (<<, >>)
Level 6: Addition/Subtraction (+, -)
Level 7: Multiplication/Division/Modulo (*, /, %)
Level 8: Exponentiation (^) — RIGHT-ASSOCIATIVE
Level 9: Unary (+, -, !)
Level 10: Parentheses, Function Calls, Atomic Values
```

**Commodore PET BASIC** (Left-to-right evaluation with some exceptions):
```
Exponentiation (^) — Highest
Unary minus (-)
Multiplication (*) and Division (/)
Addition (+) and Subtraction (-)
Comparison operators (<, >, =, <=, >=, <>)
Logical NOT
Logical AND
Logical OR — Lowest
```

**Impact**:
```basic
' Expression: 2 + 3 * 4
' GD-BASIC (BODMAS): 2 + (3 * 4) = 14 ✅ Correct math
' PET BASIC (left-to-right): (2 + 3) * 4 = 20 ⚠️ Different result

' Mode flag can enable Dartmouth (left-to-right) for compatibility
' Command: java -jar BASIC-*.jar -d program.bas
```

GD-BASIC has a **Dartmouth mode** (`-d` flag) to emulate PET BASIC's left-to-right evaluation for legacy compatibility.

---

## 4. Control Flow Statements

### IF Statement

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Single-line | `IF cond THEN 100` | `IF cond THEN 100` | ✅ Compatible |
| Inline | `IF cond THEN PRINT "msg"` | Not standard | ⚠️ GD-BASIC only |
| Block IF | `IF cond THEN ... ELSE ... END-IF` | Not standard | ❌ GD-BASIC only |
| ELSE clause | Supported (in blocks) | Not standard | ❌ GD-BASIC only |
| Nesting | Full support | Limited (via GOTO) | ⚠️ GD-BASIC cleaner |

**GD-BASIC IF Examples**:
```basic
' Single-line (PET compatible)
10 IF X > 5 THEN 100

' Inline (GD-BASIC extension)
10 IF X > 5 THEN PRINT "X is large"

' Block IF (GD-BASIC extension)
10 IF X > 5 THEN
20   PRINT "X is large"
30   Y = X * 2
40 ELSE
50   PRINT "X is small"
60   Y = X / 2
70 END-IF
```

### GOTO Statement

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Syntax | `GOTO 100` | `GOTO 100` | ✅ Compatible |
| Behavior | Jump to line 100 | Jump to line 100 | ✅ Compatible |
| Usage | Line-number-based | Line-number-based | ✅ Compatible |

**Compatibility**: Identical behavior.

### GOSUB/RETURN

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Syntax | `GOSUB 1000 ... RETURN` | `GOSUB 1000 ... RETURN` | ✅ Compatible |
| Stack | Maintained internally | Maintained by interpreter | ✅ Compatible |
| Nesting | Full support | Full support | ✅ Compatible |

**Compatibility**: Identical behavior.

---

## 5. Loops

### FOR-NEXT Loop

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Syntax | `FOR var = start TO end STEP inc` | `FOR var = start TO end STEP inc` | ✅ Compatible |
| STEP default | 1 | 1 | ✅ Compatible |
| Expression bounds | Supported | Not standard (literals usually) | ⚠️ GD-BASIC enhanced |
| Loop variable type | Integer or Real | Integer or Real | ✅ Compatible |

**GD-BASIC Enhancement**:
```basic
' PET BASIC (usually literals)
10 FOR I = 1 TO 10

' GD-BASIC (expressions supported)
10 N% = 5
20 FOR I% = 1 TO N% * 2 STEP N% / 5
```

### DO-UNTIL Loop

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Syntax | `DO ... UNTIL condition` | Not standard | ❌ GD-BASIC only |
| Semantics | Post-test loop | N/A | N/A |

**GD-BASIC Extension**:
```basic
10 DO
20   INPUT X%
30 UNTIL X% < 0
```

### WHILE-WEND Loop

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Syntax | `WHILE condition ... WEND` | Not standard | ❌ GD-BASIC only |
| Semantics | Pre-test loop | N/A | N/A |

**GD-BASIC Extension**:
```basic
10 WHILE X% > 0
20   PRINT X%
30   X% = X% - 1
40 WEND
```

---

## 6. Built-in Functions

### Mathematical Functions

| Function | GD-BASIC | PET BASIC | Notes |
|----------|----------|-----------|-------|
| `ABS(x)` | ✅ | ✅ | Identical |
| `SIN(x)` | ✅ | ✅ | Identical |
| `COS(x)` | ✅ | ✅ | Identical |
| `TAN(x)` | ✅ | ✅ | Identical |
| `ATN(x)` | ✅ | ✅ | Identical (arctangent) |
| `SQR(x)` | ✅ | ✅ | Identical (square root) |
| `EXP(x)` | ✅ | ✅ | Identical |
| `LOG(x)` | ✅ | ✅ | Identical (natural log) |
| `LOG10(x)` | ✅ | ❌ | GD-BASIC only |
| `INT(x)` | ✅ | ✅ | Identical |
| `RND` | ✅ | ✅ | Similar (minor differences) |
| `CINT(x)` | ✅ | ❌ | GD-BASIC only (int conversion) |
| `CDBL(x)` | ✅ | ❌ | GD-BASIC only (double conversion) |

### String Functions

| Function | GD-BASIC | PET BASIC | Notes |
|----------|----------|-----------|-------|
| `LEN(s$)` | ✅ | ✅ | Identical |
| `LEFT$(s$, n)` | ✅ | ✅ | Identical |
| `RIGHT$(s$, n)` | ✅ | ✅ | Identical |
| `MID$(s$, pos, len)` | ✅ | ✅ | Identical |
| `STR$(x)` | ✅ | ✅ | Identical |
| `VAL(s$)` | ✅ | ✅ | Identical |
| `CHR$(n)` | ✅ | ✅ | Identical |
| `ASC(s$)` | ✅ | ✅ | Identical |
| `INSTR(haystack$, needle$)` | ✅ | ✅ | Identical |
| `UPPER$(s$)` | ❌ | ✅ | PET BASIC only |
| `LOWER$(s$)` | ❌ | ✅ | PET BASIC only |

### File I/O Functions

| Function | GD-BASIC | PET BASIC | Notes |
|----------|----------|-----------|-------|
| `FOPEN(f$, mode)` | ✅ | ✅ | Basic file open |
| `FCLOSE(id)` | ✅ | ✅ | Basic file close |
| `FINPUT(id)` | ✅ | ✅ | Read line from file |
| `FPRINT(id, data$)` | ✅ | ✅ | Write line to file |
| `EOF(id)` | ✅ | ✅ | Check end-of-file |
| `FILEEXISTS(f$)` | ✅ | ❌ | GD-BASIC only |
| `FGETNAME(id)` | ✅ | ❌ | GD-BASIC only |
| `FGETSIZE(f$)` | ✅ | ❌ | GD-BASIC only |
| `FMODTIME(f$)` | ✅ | ❌ | GD-BASIC only |
| `FISOPEN(id)` | ✅ | ❌ | GD-BASIC only |
| `FLINECOUNT(id)` | ✅ | ❌ | GD-BASIC only |
| `FPEEK(id, var$)` | ✅ | ❌ | GD-BASIC only (lookahead) |
| `FGET(id, var$)` | ✅ | ❌ | GD-BASIC only (char read) |
| `FPUT(id, char$)` | ✅ | ❌ | GD-BASIC only (char write) |
| `FREWIND(id)` | ✅ | ❌ | GD-BASIC only (reset position) |
| `FCOPY(src$, dst$)` | ✅ | ❌ | GD-BASIC only |
| `FCOMPARE(f1$, f2$)` | ✅ | ❌ | GD-BASIC only |

### Directory Functions

| Function | GD-BASIC | PET BASIC | Notes |
|----------|----------|-----------|-------|
| `DIREXISTS(path$)` | ✅ | ❌ | GD-BASIC only |
| `MKDIR(path$)` | ✅ | ❌ | GD-BASIC only |
| `RMDIR(path$)` | ✅ | ❌ | GD-BASIC only |
| `GETCWD()` | ✅ | ❌ | GD-BASIC only |
| `CHDIR(path$)` | ✅ | ❌ | GD-BASIC only |
| `LISTDIR(path$, pattern$)` | ✅ | ❌ | GD-BASIC only |

### System Functions

| Function | GD-BASIC | PET BASIC | Notes |
|----------|----------|-----------|-------|
| `TIME()` | ✅ | ✅ | System time |
| `SYSTEM(cmd$)` | ✅ | ❌ | GD-BASIC only (execute OS command) |
| `CALL(url$)` | ✅ | ❌ | GD-BASIC only (HTTP call) |
| `MEM()` | ✅ | ✅ | Available memory |

**Summary**: GD-BASIC has 40+ built-in functions vs ~20-30 in PET BASIC, with significant enhancements in file I/O and system operations.

---

## 7. Input/Output

### PRINT Statement

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Basic output | `PRINT "text"` | `PRINT "text"` | ✅ Compatible |
| Multiple values | `PRINT A, B` | `PRINT A, B` | ✅ Compatible |
| Comma spacing | 16 chars (tab) | 16 chars (tab) | ✅ Compatible |
| Semicolon | `PRINT A;B;` (no newline) | `PRINT A;B;` (no newline) | ✅ Compatible |
| File output | `PRINT #id, data` | `PRINT #id, data` | ✅ Compatible |

**Compatibility**: Identical behavior.

### INPUT Statement

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| User input | `INPUT X%` | `INPUT X%` | ✅ Compatible |
| Prompt | `INPUT "text"; X%` | `INPUT "text"; X%` | ✅ Compatible |
| File input | `INPUT #id, X%` | `INPUT #id, X%` | ✅ Compatible |
| Array input | `INPUT A%(I%)` | Not standard | ⚠️ GD-BASIC enhanced |

**Compatibility**: Mostly compatible with enhancements for array elements.

### READ/DATA

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| DATA statement | `DATA 1, 2, 3` | `DATA 1, 2, 3` | ✅ Compatible |
| READ statement | `READ X%` | `READ X%` | ✅ Compatible |
| Restoration | `RESTORE` | `RESTORE` | ✅ Compatible |
| Array READ | `READ A%(I%)` | Not standard | ⚠️ GD-BASIC enhanced |

**Compatibility**: Identical core behavior with array enhancements.

---

## 8. String Operations

### String Concatenation

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Operator | `+` | `+` | ✅ Compatible |
| Example | `A$ = "Hello" + " World"` | `A$ = "Hello" + " World"` | ✅ Compatible |

**Compatibility**: Identical.

### String Indexing

| Feature | GD-BASIC | PET BASIC | Status |
|---------|----------|-----------|--------|
| Syntax | `A$[index]` | Not standard | ❌ GD-BASIC only |
| Returns | Single character | N/A | N/A |
| Example | `C$ = "Hello"[1]` (returns "H") | N/A | N/A |

**GD-BASIC Extension**:
```basic
10 S$ = "HELLO"
20 PRINT S$[0]     ' Output: H
30 PRINT S$[4]     ' Output: O
```

---

## 9. Unsupported Features

### In GD-BASIC (Not Implemented)

| Feature | PET BASIC | Status | Notes |
|---------|-----------|--------|-------|
| `UPPER$` function | ✅ | ❌ | Not implemented |
| `LOWER$` function | ✅ | ❌ | Not implemented |
| Tape I/O (SAVE/LOAD) | ✅ | ❌ | Not applicable (modern file systems) |
| Disk SAVE/LOAD | ✅ | ❌ | Replaced by FOPEN/FPRINT |
| `POKE`/`PEEK` | ✅ | ❌ | Not applicable (protected memory) |
| Graphics/Sound | ✅ | ❌ | Not implemented |
| `SPC()` function | ✅ | ❌ | Not implemented |
| `TAB()` function | ✅ | ❌ Not implemented |
| `WAIT` statement | ✅ | ❌ | Not implemented |
| `GET`/`PUT` statements | ✅ | ❌ | Not implemented |
| Relative file access | ✅ | ❌ | Sequential only |

### In PET BASIC (Not in GD-BASIC)

| Feature | GD-BASIC | Status | Notes |
|---------|----------|--------|-------|
| Commodore-specific I/O | ❌ | N/A | Tape, disk device access |
| 16-bit integers | ❌ | N/A | GD-BASIC uses 32-bit |
| Single-precision floats | ❌ | N/A | GD-BASIC uses 64-bit |
| Graphics commands | ❌ | N/A | Not implemented |

---

## 10. Statement Comparison

### Implemented Statements

| Statement | GD-BASIC | PET BASIC | Status |
|-----------|----------|-----------|--------|
| `REM` | ✅ | ✅ | Compatible |
| `PRINT` | ✅ | ✅ | Compatible |
| `INPUT` | ✅ | ✅ | Compatible |
| `LET` | ✅ (implicit) | ✅ (implicit) | Compatible |
| `IF...THEN` | ✅ (enhanced) | ✅ | GD-BASIC has block form |
| `GOTO` | ✅ | ✅ | Compatible |
| `GOSUB` | ✅ | ✅ | Compatible |
| `RETURN` | ✅ | ✅ | Compatible |
| `FOR...NEXT` | ✅ | ✅ | Compatible |
| `DO...UNTIL` | ✅ | ❌ | GD-BASIC only |
| `WHILE...WEND` | ✅ | ❌ | GD-BASIC only |
| `READ` | ✅ | ✅ | Compatible |
| `DATA` | ✅ | ✅ | Compatible |
| `END` | ✅ | ✅ | Compatible |
| `FOPEN` | ✅ | ✅ | Compatible |
| `FCLOSE` | ✅ | ✅ | Compatible |
| `FPRINT` | ✅ | ✅ | Compatible |
| `FINPUT` | ✅ | ✅ | Compatible |

---

## 11. Compatibility Assessment

### Migration from PET BASIC to GD-BASIC

**Easy** (100% compatible):
```basic
10 REM Classic PET BASIC program
20 FOR I% = 1 TO 10
30   PRINT I%, I% * 2
40 NEXT
50 END
```

**Moderate** (Minor adjustments):
```basic
' PET BASIC with DIM
10 DIM A(5)
20 FOR I = 1 TO 5
30   A(I) = I * 2
40 NEXT

' GD-BASIC (remove DIM, add type suffix)
10 FOR I% = 1 TO 5
20   A%(I%) = I% * 2
30 NEXT
```

**Complex** (Needs refactoring):
```basic
' PET BASIC with complex GOTO logic
10 IF X > 5 THEN 100
20 PRINT "small"
30 GOTO 200
100 PRINT "large"
200 END

' GD-BASIC equivalent (block IF)
10 IF X > 5 THEN
20   PRINT "large"
30 ELSE
40   PRINT "small"
50 END-IF
60 END
```

### Compatibility Mode

GD-BASIC offers a **Dartmouth mode** (`-d` flag) for legacy compatibility:

```bash
# Standard mode (BODMAS/PEMDAS)
java -jar BASIC-*.jar program.bas

# Dartmouth mode (left-to-right evaluation)
java -jar BASIC-*.jar -d program.bas
```

---

## 12. Type System Differences

### PET BASIC Type System

```
Variables:        Number or String
Number format:    32-bit single-precision float (4 bytes)
Integer suffix:   % (stored as float, integer semantics)
String suffix:    $ (unlimited length)
Implicit type:    Single-precision float
Type conversion:  Implicit, automatic
```

### GD-BASIC Type System

```
Variables:        Integer, Long, Real, Boolean, String
Integer:          32-bit signed (same magnitude as PET)
Long:             64-bit signed
Real:             64-bit double (higher precision than PET)
Boolean:          true/false
String:           Unlimited length
Type coercion:    Explicit conversion functions available
                  (CINT, CDBL, STR$, VAL, CHR$, ASC)
```

**Impact on Programs**:
```basic
' PET BASIC (all numeric internally)
10 X = 3.7
20 I = X      ' Assigns 3.7, may truncate to 3 in integer context
30 PRINT I    ' Output depends on context

' GD-BASIC (explicit types)
10 X# = 3.7
20 I% = CINT(X#)    ' Explicit: rounds 3.7 to 4
30 PRINT I%         ' Output: 4
```

---

## 13. Key Enhancement Areas

### 1. Array Handling

**PET BASIC**:
- Requires explicit `DIM` declaration
- Literal or simple variable indices
- Limited expression support

**GD-BASIC**:
- Automatic allocation (no DIM needed)
- Expression-based indices with full operator support
- Normalized operator spacing
- Multi-dimensional arrays with dynamic expansion

### 2. Control Flow

**PET BASIC**:
- Line-number-based flow control
- GOTO/GOSUB heavy usage
- Limited structured control

**GD-BASIC**:
- Block IF/ELSE/END-IF structures
- DO-UNTIL and WHILE-WEND loops
- Cleaner nesting support
- Legacy GOTO still supported

### 3. File I/O

**PET BASIC**:
- Basic FOPEN/FCLOSE/FINPUT/FPRINT
- Limited file operations
- No directory operations

**GD-BASIC**:
- 19 file operations total
- Character-level I/O (FGET, FPUT, FPEEK)
- File metadata queries
- Directory operations (MKDIR, RMDIR, LISTDIR)
- File utilities (FCOPY, FCOMPARE, FREWIND)

### 4. Operator Precedence

**PET BASIC**:
- Left-to-right evaluation (non-standard)
- Can lead to unexpected results

**GD-BASIC**:
- BODMAS/PEMDAS precedence (standard)
- Dartmouth mode for legacy compatibility
- Correct mathematical evaluation

### 5. Built-in Functions

**PET BASIC**: ~20-30 functions  
**GD-BASIC**: 40+ functions

Additional functions:
- File I/O: FILEEXISTS, FGETSIZE, FMODTIME, FPEEK, FGET, FPUT, FREWIND, FCOPY, FCOMPARE
- Directory: DIREXISTS, MKDIR, RMDIR, GETCWD, CHDIR, LISTDIR
- Math: LOG10, CINT, CDBL, NOT
- System: SYSTEM, CALL

---

## 14. Conclusion

### GD-BASIC Positioning

**Backward Compatible**: Most PET BASIC programs run unchanged or with minimal modifications.

**Forward Compatible**: Adds modern features without breaking classic BASIC:
- Block structures replace complex GOTO logic
- Enhanced type system with explicit conversions
- Rich file I/O replacing tape-based systems
- Better operator precedence (with legacy mode available)

**Suitable For**:
- Educational purposes (classic BASIC learning)
- Legacy program preservation and modernization
- Quick scripting with modern features
- Embedding as a scripting engine in Java applications

**Not Suitable For**:
- Exact PET BASIC emulation (use actual PET emulator for that)
- Graphics and sound (not implemented)
- Real-time embedded systems
- Tape-based I/O operations

### Migration Path

1. **Start with PET BASIC program**: Works as-is in most cases
2. **Add GD-BASIC features**: Block IF, WHILE loops, better file I/O
3. **Leverage new functions**: Directory operations, character-level I/O
4. **Use modern types**: Explicit type declarations for clarity
5. **Maintain compatibility**: Use `-d` flag if needed for legacy semantics

---

## 15. References

- **GD-BASIC**: v0.1.1 (2026-05-30)
- **Commodore PET BASIC**: BASIC 2.0 / 3.0 era
- **BASIC Standards**: Dartmouth BASIC (1964), ANSI BASIC X3.60-1978
- **Documentation**: BASIC_CODING_STANDARD.md, GD-BASIC_Detailed_Design.md

---

## Appendix A: Side-by-Side Examples

### Example 1: Fibonacci Sequence

**Commodore PET BASIC**:
```basic
10 DIM F(10)
20 F(0) = 0 : F(1) = 1
30 FOR I = 2 TO 10
40   F(I) = F(I-1) + F(I-2)
50 NEXT I
60 FOR I = 0 TO 10
70   PRINT F(I)
80 NEXT I
90 END
```

**GD-BASIC** (same, no DIM needed):
```basic
10 F#(0) = 0 : F#(1) = 1
20 FOR I% = 2 TO 10
30   F#(I%) = F#(I%-1) + F#(I%-2)
40 NEXT I%
50 FOR I% = 0 TO 10
60   PRINT F#(I%)
70 NEXT I%
80 END
```

**GD-BASIC** (modern block style):
```basic
10 F#(0) = 0 : F#(1) = 1
20 FOR I% = 2 TO 10
30   F#(I%) = F#(I%-1) + F#(I%-2)
40 NEXT I%
50 PRINT "Fibonacci sequence:"
60 FOR I% = 0 TO 10
70   PRINT I%; ": "; F#(I%)
80 NEXT I%
90 END
```

### Example 2: File Processing

**Commodore PET BASIC** (limited):
```basic
10 FOPEN "data.txt", "r", 1
20 WHILE NOT EOF(1)
30   FINPUT 1, LINE$
40   PRINT LINE$
50 WEND
60 FCLOSE 1
70 END
```

**GD-BASIC** (enhanced):
```basic
10 IF NOT FILEEXISTS("data.txt") THEN PRINT "Not found" : END
20 ID = FOPEN("data.txt", "r")
30 WHILE NOT EOF(ID)
40   FINPUT ID, LINE$
50   PRINT LINE$
60 WEND
70 FCLOSE ID
80 END
```

**GD-BASIC** (with file metadata):
```basic
10 FILENAME$ = "data.txt"
20 IF NOT FILEEXISTS(FILENAME$) THEN
30   PRINT "File not found"; FILENAME$
40 ELSE
50   PRINT "File: "; FGETNAME(FILENAME$)
60   PRINT "Size: "; FGETSIZE(FILENAME$); " bytes"
70   ID = FOPEN(FILENAME$, "r")
80   WHILE NOT EOF(ID)
90     FINPUT ID, LINE$
100    PRINT LINE$
110  WEND
120  FCLOSE ID
130 END-IF
140 END
```

### Example 3: Conditional Logic

**Commodore PET BASIC** (GOTO-heavy):
```basic
10 INPUT "Enter a number: ", X
20 IF X < 0 THEN PRINT "Negative" : GOTO 50
30 IF X = 0 THEN PRINT "Zero" : GOTO 50
40 PRINT "Positive"
50 END
```

**GD-BASIC** (block IF):
```basic
10 INPUT "Enter a number: ", X%
20 IF X% < 0 THEN
30   PRINT "Negative"
40 ELSEIF X% = 0 THEN
50   PRINT "Zero"
60 ELSE
70   PRINT "Positive"
80 END-IF
90 END
```

Note: GD-BASIC doesn't have ELSEIF yet; use nested IF blocks instead:
```basic
10 INPUT "Enter a number: ", X%
20 IF X% < 0 THEN
30   PRINT "Negative"
40 ELSE
50   IF X% = 0 THEN
60     PRINT "Zero"
70   ELSE
80     PRINT "Positive"
90   END-IF
100 END-IF
110 END
```

---

**Document End**  
Analysis completed: 2026-05-30  
Analyzer: Claude Haiku 4.5
