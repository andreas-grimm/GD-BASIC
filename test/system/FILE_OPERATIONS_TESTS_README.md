# File Operations System Tests (May 30, 2026)

## Overview

This document describes the comprehensive system integration test suite for file operations in GD-BASIC, focusing on the four new file operation statements added on May 30, 2026:
- **FPEEK** - Character lookahead without advancing position
- **FPUT** - Character output without newline
- **FRENAME** - File renaming/moving
- **FREWIND** - File position reset

## Test Files Created

### 1. test_file_operations_comprehensive.bas
**Purpose**: Basic file write and read operations
**Tests**:
- `FOPEN` file creation in write mode
- `FPRINT` line writing
- `FCLOSE` with file closure
- `FINPUT` line reading with verification

**Status**: Demonstrates basic file I/O workflow

---

### 2. test_character_level_io.bas
**Purpose**: Character-by-character file operations
**Tests**:
- `FPUT` building lines character by character
- Combining multiple FPUT calls to form words
- `FPRINT` adding final newline
- Verification of composed content

**Key Feature**: Shows how FPUT enables building strings without automatic newlines

**Usage Pattern**:
```basic
FPUT 1, "H"
FPUT 1, "e"
FPUT 1, "l"
FPUT 1, "l"
FPUT 1, "o"
FPRINT 1, ""  ! Add newline
```

---

### 3. test_file_rewind.bas
**Purpose**: File position reset using FREWIND
**Tests**:
- `FREWIND` resetting position to start
- Multiple passes over same file
- Verifying position reset between passes
- Efficient file re-reading without close/open

**Key Feature**: FREWIND allows reading same file multiple times efficiently

**Usage Pattern**:
```basic
FINPUT 1, LINE1$     ! Read first line
FREWIND 1            ! Go back to start
FINPUT 1, LINE_AGAIN ! Read again
```

---

### 4. test_file_rename.bas
**Purpose**: File renaming and moving operations
**Tests**:
- `FRENAME` changing file name
- `FEXISTS` verifying file presence before/after
- Content preservation during rename
- Re-registering file with FileManager

**Key Feature**: FRENAME allows moving files to different paths using same file ID

**Usage Pattern**:
```basic
FOPEN 1 "/tmp/original.txt" "w"
FPRINT 1, "Data"
FCLOSE 1
FRENAME 1, "/tmp/renamed.txt"
```

---

### 5. test_file_peek.bas
**Purpose**: Lookahead character reading without advancing position
**Tests**:
- `FPEEK` reading next character without consuming
- Multiple consecutive peeks return same character
- `FGET` advancing position after peeking
- Position verification after operations

**Key Feature**: FPEEK enables conditional character processing based on lookahead

**Usage Pattern**:
```basic
FPEEK 1, C$           ! Look ahead
IF C$ = "X" THEN ...
FGET 1, ACTUAL$       ! Now read it
```

---

### 6. test_file_search_lookahead.bas
**Purpose**: Search and count using lookahead (FPEEK)
**Tests**:
- `FPEEK` for conditional character detection
- Selective `FGET` based on lookahead result
- Character counting in file
- Loop control with EOF

**Key Feature**: Demonstrates conditional processing of file content

**Usage Pattern**:
```basic
WHILE NOT EOF(1)
  FPEEK 1, C$
  IF C$ = "X" THEN
    FGET 1, CHAR$      ! Consume matching character
    FOUND% = FOUND% + 1
  ELSE
    FGET 1, DUMMY$     ! Skip non-matching
  END IF
WEND
```

---

### 7. test_file_character_transform.bas
**Purpose**: Character-by-character file transformation
**Tests**:
- `FGET` reading from source file
- `FPUT` writing to destination file
- Conditional transformation (space → underscore)
- File-to-file copying with character mapping

**Key Feature**: Shows sophisticated file processing with transformation

**Usage Pattern**:
```basic
WHILE NOT EOF(1)
  FGET 1, C$
  IF C$ = " " THEN
    FPUT 2, "_"
  ELSE
    FPUT 2, C$
  END IF
WEND
```

---

### 8. test_file_integration.bas
**Purpose**: Combined file operations demonstrating complete workflow
**Tests**:
- `FOPEN`, `FPRINT`, `FINPUT` basic operations
- `FPEEK` lookahead verification
- `FINPUT` continued reading
- `FREWIND` re-reading
- `FCLOSE` cleanup

**Key Feature**: Shows real-world usage combining multiple statement types

---

### 9. test_file_copy.bas
**Purpose**: File copying using line-by-line operations
**Tests**:
- Source file creation with multiple lines
- `FINPUT` reading lines
- `FPRINT` writing lines to destination
- Verification of copied content

**Key Feature**: Demonstrates file copying without special copy functions

**Usage Pattern**:
```basic
WHILE NOT EOF(1)
  FINPUT 1, LINE$
  FPRINT 2, LINE$
WEND
```

---

### 10. test_file_eof.bas
**Purpose**: EOF (End-of-File) detection and handling
**Tests**:
- `EOF` function verification at different positions
- Correct EOF status before/after reads
- `FPEEK` behavior at EOF (returns "EOF")
- EOF loop control

**Key Feature**: Ensures proper EOF handling in various scenarios

---

### 11. test_file_exists.bas
**Purpose**: File existence verification
**Tests**:
- `FEXISTS` for non-existent files
- `FEXISTS` after file creation
- `FEXISTS` after file deletion
- File presence lifecycle

**Key Feature**: Demonstrates robust file existence checking

---

## Test Execution

### Prerequisites
1. Build the project: `mvn clean package`
2. Create temporary test files: `touch /tmp/test*.txt`
3. JAR available: `target/BASIC-0.1.1-jar-with-dependencies.jar`

### Running Individual Tests
```bash
java -jar target/BASIC-0.1.1-jar-with-dependencies.jar test/system/test_file_eof.bas
```

### Running All Tests
```bash
cd test/system
./run_all_tests.sh
```

### Test Output
Each test prints:
- Test name
- Subtest descriptions
- "PASSED" message on success
- "ERROR" message on failure

## Test Coverage Matrix

| Test File | FOPEN | FCLOSE | FINPUT | FPRINT | FGET | FPUT | FPEEK | FREWIND | FRENAME | EOF | FEXISTS |
|-----------|-------|--------|--------|--------|------|------|-------|---------|---------|-----|---------|
| test_file_operations_comprehensive.bas | ✓ | ✓ | ✓ | ✓ | - | - | - | - | - | - | - |
| test_character_level_io.bas | ✓ | ✓ | ✓ | ✓ | - | ✓ | - | - | - | - | - |
| test_file_rewind.bas | ✓ | ✓ | ✓ | ✓ | - | - | - | ✓ | - | ✓ | ✓ |
| test_file_rename.bas | ✓ | ✓ | ✓ | ✓ | - | - | - | - | ✓ | - | ✓ |
| test_file_peek.bas | ✓ | ✓ | - | - | ✓ | - | ✓ | - | - | ✓ | - |
| test_file_search_lookahead.bas | ✓ | ✓ | - | - | ✓ | - | ✓ | - | - | ✓ | - |
| test_file_character_transform.bas | ✓ | ✓ | - | - | ✓ | ✓ | - | - | - | ✓ | - |
| test_file_integration.bas | ✓ | ✓ | ✓ | ✓ | - | - | ✓ | ✓ | - | - | - |
| test_file_copy.bas | ✓ | ✓ | ✓ | ✓ | - | - | - | - | - | ✓ | - |
| test_file_eof.bas | ✓ | ✓ | - | - | ✓ | ✓ | ✓ | - | - | ✓ | - |
| test_file_exists.bas | ✓ | ✓ | - | - | - | - | - | - | - | - | ✓ |

**Coverage Summary**:
- FOPEN: 11/11 tests (100%)
- FCLOSE: 11/11 tests (100%)
- FINPUT: 7/11 tests (64%)
- FPRINT: 7/11 tests (64%)
- FGET: 4/11 tests (36%)
- FPUT: 4/11 tests (36%)
- FPEEK: 4/11 tests (36%)
- FREWIND: 2/11 tests (18%)
- FRENAME: 1/11 tests (9%)
- EOF: 7/11 tests (64%)
- FEXISTS: 3/11 tests (27%)

## Documentation References

Each test is based on examples from:
- **BASIC_CODING_STANDARD.md**: File I/O Operations section
- **BASIC.md**: File I/O subsection
- **GD-BASIC_Detailed_Design.md**: Advanced File Operations section
- **FILE_OPERATIONS_REQUIREMENTS.md**: New File Operation Statements (Section 11)

## Notes on Implementation

### Known Issues
- FileManager requires files to exist even for write mode (creates files before tests)
- EOF function returns IntegerValue instead of BooleanValue (type casting issue in IfThenStatement)
- FPEEK/FGET EOF behavior returns "EOF" string rather than EOF value

### Recommendations for Future Work
1. Fix FileManager to support file creation in write mode
2. Fix type system EOF handling (IntegerValue vs BooleanValue)
3. Add APPEND mode support ("a" parameter)
4. Implement FSEEK for random file access
5. Add binary file mode support

## Test Validation

All tests follow standard BASIC testing patterns:
- Create test files with known content
- Perform operations
- Verify results match expectations
- Clean up with FCLOSE "DELETE"
- Use GOTO 9000 for error handling

Example test structure:
```basic
FOPEN 1 "/tmp/testfile.txt" "w"
FPRINT 1 "Expected content"
FCLOSE 1 ""

FOPEN 1 "/tmp/testfile.txt" "r"
FINPUT 1 CONTENT$
IF CONTENT$ != "Expected content" THEN GOTO 9000
FCLOSE 1 "DELETE"

PRINT "PASSED"
END

9000 PRINT "ERROR"
END
```

---

**Document Version**: 1.0  
**Created**: May 30, 2026  
**Author**: GD-BASIC Development Team
