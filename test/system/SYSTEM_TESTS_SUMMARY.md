# GD-BASIC System Tests Summary

## New File Operations Tests (May 30, 2026)

Comprehensive system integration test suite created to validate the four new file operation statements and existing file I/O functionality.

### Test Files Created

| # | Test File | Purpose | Lines | Focus |
|----|-----------|---------|-------|-------|
| 1 | test_file_operations_comprehensive.bas | Basic write/read operations | 45 | FOPEN, FCLOSE, FPRINT, FINPUT |
| 2 | test_character_level_io.bas | Character-by-character I/O | 45 | FPUT, FPRINT, FINPUT |
| 3 | test_file_rewind.bas | File position reset | 45 | FREWIND, multi-pass processing |
| 4 | test_file_rename.bas | File renaming/moving | 40 | FRENAME, FEXISTS, content preservation |
| 5 | test_file_peek.bas | Character lookahead | 45 | FPEEK, FGET, position tracking |
| 6 | test_file_search_lookahead.bas | Search with FPEEK | 45 | FPEEK, FGET, conditional processing |
| 7 | test_file_character_transform.bas | Character transformation | 50 | FGET, FPUT, conditional mapping |
| 8 | test_file_integration.bas | Complete workflow | 50 | FOPEN, FPRINT, FINPUT, FPEEK, FREWIND |
| 9 | test_file_copy.bas | File copying | 50 | Line-by-line copy, FINPUT, FPRINT |
| 10 | test_file_eof.bas | EOF handling | 45 | EOF detection, FGET, FPEEK at EOF |
| 11 | test_file_exists.bas | File existence | 40 | FEXISTS, lifecycle verification |

**Total**: 11 new system test files, ~500 lines of BASIC test code

### Test Coverage

#### File Operations Tested

**Core Operations** (100% coverage):
- ✅ FOPEN - File opening (all tests)
- ✅ FCLOSE - File closing (all tests)
- ✅ EOF - End-of-file detection (7 tests)
- ✅ FEXISTS - File existence checking (3 tests)

**Line-Based I/O** (64% coverage):
- ✅ FINPUT - Line reading (7 tests)
- ✅ FPRINT - Line writing (7 tests)

**Character-Level I/O** (36% coverage):
- ✅ FGET - Character reading with advance (4 tests)
- ✅ FPUT - Character writing without newline (4 tests)
- ✅ FPEEK - Character lookahead (4 tests)

**File Management** (>10% coverage):
- ✅ FREWIND - Position reset (2 tests)
- ✅ FRENAME - File renaming (1 test)

### Documentation Integration

All tests are based on examples from the updated documentation:

**BASIC_CODING_STANDARD.md**
- File I/O Operations section (350+ lines)
- 4 comprehensive usage examples
- Character-level operations documentation
- Multi-pass file processing examples

**BASIC.md**
- Enhanced File I/O section
- FGET, FPEEK, FPUT, FRENAME, FREWIND documentation
- Syntax specifications for each operation
- Practical usage examples

**GD-BASIC_Detailed_Design.md**
- Technical implementation details for each statement
- FileManager integration patterns
- Parser case implementations
- Unit test coverage information

**FILE_OPERATIONS_REQUIREMENTS.md**
- Implementation status (Section 11)
- Parser integration code examples
- Use case documentation
- Unit test specifications

### Test Patterns

All tests follow consistent BASIC patterns:

**Pattern 1: Create and Verify**
```basic
FOPEN 1 "file.txt" "w"
FPRINT 1 "content"
FCLOSE 1 ""
FOPEN 1 "file.txt" "r"
FINPUT 1 RESULT$
IF RESULT$ != "content" THEN GOTO 9000
FCLOSE 1 "DELETE"
PRINT "PASSED"
END
```

**Pattern 2: Character Processing**
```basic
WHILE NOT EOF(1)
  FGET 1, C$
  IF C$ = "X" THEN GOSUB 1000
WEND
```

**Pattern 3: Lookahead Processing**
```basic
FPEEK 1, NEXT$
IF NEXT$ = "X" THEN
  FGET 1, ACTUAL$
  ! Process matching character
END IF
```

**Pattern 4: Multi-pass Processing**
```basic
FREWIND 1
GOSUB 1000  ! First pass
FREWIND 1
GOSUB 2000  ! Second pass
```

### Test Execution

**Run individual test**:
```bash
java -jar target/BASIC-0.1.1-jar-with-dependencies.jar test/system/test_file_peek.bas
```

**Run all file operation tests**:
```bash
cd test/system
for test in test_file*.bas; do
  echo "Running $test..."
  java -jar ../target/BASIC-0.1.1-jar-with-dependencies.jar "$test"
done
```

### Test Verification

Each test verifies:
1. **File creation** - Files are created with correct content
2. **File reading** - Content is read exactly as written
3. **Position tracking** - Read positions advance correctly
4. **Position reset** - FREWIND correctly resets to start
5. **Lookahead** - FPEEK shows next character without advancing
6. **Transformation** - Characters are modified correctly
7. **EOF detection** - End-of-file is properly detected
8. **File existence** - Files are created and deleted correctly

### Known Findings

The test suite revealed the following implementation issues:

1. **FileManager Limitation**: 
   - Issue: Files must exist before opening in write mode
   - Impact: Tests create files with `touch` before running
   - Recommendation: Modify FileManager to create non-existent files

2. **Type System Issue**:
   - Issue: EOF returns IntegerValue instead of BooleanValue
   - Impact: IF statements that use EOF directly cause ClassCastException
   - Recommendation: Fix type conversion in EOF function or IfThenStatement

3. **EOF String Convention**:
   - Issue: FPEEK/FGET return "EOF" as string at end-of-file
   - Impact: Tests check for "EOF" string instead of EOF value
   - Recommendation: Standardize EOF return value across functions

### Test Quality Metrics

| Metric | Value |
|--------|-------|
| Total test files | 11 |
| Total test lines | ~500 |
| Test coverage | 11 operations tested |
| Documentation examples | 4 main patterns + 20+ small examples |
| Pattern adherence | 100% (consistent structure) |
| Error handling | All tests include error paths (GOTO 9000) |

### Next Steps

1. **Fix identified issues** in FileManager and type system
2. **Run tests against fixed implementation** to validate
3. **Add append mode tests** for "a" file mode
4. **Add FSEEK tests** for random file access
5. **Add binary mode tests** for "rb"/"wb" modes

---

**Document Version**: 1.0  
**Created**: May 30, 2026  
**Test Files**: 11  
**Total LOC**: ~500 lines of BASIC test code  
**Coverage**: File I/O operations (100% FOPEN/FCLOSE, 64% FINPUT/FPRINT, 36% character ops)
