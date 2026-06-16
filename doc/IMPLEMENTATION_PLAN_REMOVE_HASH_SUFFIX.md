# Implementation Plan: Remove # Suffix from Variable Type System

**Project:** GD-BASIC 0.1.1  
**Version Change:** 0.1.1 → 0.2.0 (breaking change)  
**Status:** Planning Phase  
**Date Created:** 2026-06-16

---

## Executive Summary

This plan describes the complete removal of the `#` (hash/pound) suffix from GD-BASIC's variable type system. Currently, `#` explicitly marks a variable as REAL type (double). After this change:

- Variables with `#` suffix will be rejected as syntax errors
- Untyped variables (no suffix) will default to REAL storage
- The `!` suffix remains for explicit DOUBLE type (equivalent behavior)
- All other suffixes (`%`, `&`, `$`, `@`) remain unchanged
- Breaking change: code using `#` requires rewriting to use untyped or `!`

**Estimated effort:** 3-4 development days  
**Risk level:** Medium (affects core type system, many tests)

---

## Design Decisions & Rationale

### 1. Type Detection Strategy: **Option B**

**Decision:** Remove `#` from type detection, immediately throw `SyntaxErrorException` if `#` is found in variable name.

**Rationale:**
- **Clarity:** Explicit rejection is clearer than silent removal
- **User feedback:** Users get immediate, actionable error message
- **Future flexibility:** Frees `#` for use as comment marker or operator
- **Minimal scope:** Changes are localized to one method

**Location:** `VariableManagement.putMap(String, Value)` line 57-58

---

### 2. Untyped Variable Handling: **Option A**

**Decision:** Route UNDEFINED untyped variables directly to `_moReals` map (merge behavior).

**Rationale:**
- **Semantic clarity:** "Untyped" = "Real by default" in BASIC tradition
- **No breaking API change:** Only the destination map changes, not public interface
- **Simpler memory layout:** Reduces from 5 maps to 4 (future optimization potential)
- **Performance:** Eliminates one lookup in `getMap()` call sequence
- **Backwards compatible for retrieval:** Untyped variables still resolve correctly

**Impact:**
- Change default case in `putMap(String, Value)` line 81-82
- Simplify `getMap()` retrieval sequence (remove `_moUntyped` check)
- Update `mapContainsKey()` (remove `_moUntyped` check)

---

### 3. putMap(String, double) Modification: **Accept untyped variables**

**Decision:** Modify `putMap(String, double)` to accept **BOTH** explicit `!` suffix AND untyped variables.

**Current behavior (lines 93-102):**
```java
if (strKey.contains("!") || strKey.contains("#")) {
    // Store in _moReals
} else {
    throw SyntaxErrorException  // Requires ! or #
}
```

**New behavior:**
```java
if (strKey.contains("!")) {
    // Store in _moReals (explicit double)
} else if (!hasAnyTypeSuffix(strKey)) {
    // Store in _moReals (untyped defaults to real)
} else {
    throw SyntaxErrorException  // Other types not allowed for double
}
```

**Rationale:**
- Allows: `X = 3.14` (untyped real assignment)
- Allows: `Y! = 2.71` (explicit double assignment)
- Rejects: `Z$ = 3.14` (type mismatch)
- Rejects: `W% = 3.14` (type mismatch)

---

### 4. Validation Strategy: **Option A - Late Validation Only**

**Decision:** Validate only in `VariableManagement.putMap()`, not in Lexer or Parser.

**Rationale:**
- **Efficiency:** Early validation would require pre-scanning entire program
- **Separation of concerns:** Lexer tokenizes, Parser parses, Runtime validates
- **Practical:** Variables are only assigned/created at runtime via `putMap()`
- **Error clarity:** Runtime error shows exact assignment line/context
- **Scope:** Minimal code changes, lower risk of regression

**Consideration:** Variables used in expressions (reads) will fail at retrieval time anyway, giving clear error "Unknown variable".

---

### 5. Base Key Normalization (getVariableBaseKey): **Not Necessary**

**Decision:** Do **NOT** implement a separate `getVariableBaseKey()` method.

**Rationale:**
- Current `Normalizer.normalizeIndex()` already handles suffix preservation:
  - Input: `matrix#(x*3)` → Output: `matrix#-x*3` (suffix before dash)
  - Works correctly with arrays
- Existing code pattern works reliably
- Adding new method increases maintenance burden
- Test coverage of existing patterns is adequate

---

## Implementation Phases

### Phase 1: Error Handling Foundation (Day 1, ~4 hours)
**Goal:** Set up infrastructure to detect and reject `#` suffix

**Files Modified:**
1. **VariableManagement.java** (3 locations)
   - Line 57-58: Change `#` check to throw error
   - Line 94-101: Modify `putMap(String, double)`
   - Line 28: Remove `_moUntyped` map declaration

**Specific Changes:**

**1a. putMap(String, Value) - Add # rejection**
```java
// Line 48-84, change lines 57-58
OLD:
} else if (strKey.contains("#")) {
    eVariableType = VariableType.REAL;

NEW:
} else if (strKey.contains("#")) {
    throw new SyntaxErrorException(
        "Syntax Error: Variable name [" + strKey + 
        "] uses unsupported '#' suffix. Use untyped (no suffix) " +
        "or '!' for real numbers.");
```

**1b. putMap(String, Value) - Route untyped to REAL**
```java
// Line 81-82, change default case
OLD:
default:
    _moUntyped.put(Normalizer.normalizeIndex(strKey), oValue);

NEW:
case UNDEFINED:
    // Untyped variables default to REAL type
    if (oValue instanceof RealValue) {
        _moReals.put(Normalizer.normalizeIndex(strKey), (RealValue) oValue);
    } else {
        // Convert untyped to real
        _moReals.put(Normalizer.normalizeIndex(strKey), 
            new RealValue(oValue.toReal()));
    }
    break;
default:
    // Should not reach here with current suffix logic
    _moReals.put(Normalizer.normalizeIndex(strKey), 
        new RealValue(oValue.toReal()));
    break;
```

**1c. putMap(String, double) - Accept untyped**
```java
// Line 93-102, complete replacement
OLD:
public final void putMap(final String strKey, final double dValue) 
        throws SyntaxErrorException {
    if (strKey.contains("!") || strKey.contains("#")) {
        RealValue oValue = new RealValue(dValue);
        _moReals.put(Normalizer.normalizeIndex(strKey), oValue);
        return;
    }
    throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey
            + "] does not end as a Real: '!' or " + "'#'");
}

NEW:
public final void putMap(final String strKey, final double dValue) 
        throws SyntaxErrorException {
    // Explicit ! suffix or untyped variables both map to REAL
    if (strKey.contains("!") || !_hasTypeSuffix(strKey)) {
        RealValue oValue = new RealValue(dValue);
        _moReals.put(Normalizer.normalizeIndex(strKey), oValue);
        return;
    }
    throw new SyntaxErrorException("Syntax Error: Variable name [" + strKey
            + "] cannot store double value. Use untyped (no suffix) " +
            "or '!' for real numbers.");
}

// NEW HELPER METHOD (add after line 137)
/**
 * Helper method to determine if a variable name has any type suffix.
 * 
 * @param strKey variable name to check
 * @return true if variable has type suffix ($, %, &, !, @), false otherwise
 */
private boolean _hasTypeSuffix(final String strKey) {
    return strKey.contains("$") || strKey.contains("%") || 
           strKey.contains("&") || strKey.contains("!") || 
           strKey.contains("@");
}
```

**1d. Remove _moUntyped declaration**
```java
// Line 28, DELETE:
private final static Map<String, Value> _moUntyped = new HashMap<>();
```

**Tests for Phase 1:**
- Unit test: `VariableManagementTest.testHashSuffixRejected()` (new)
- Unit test: `VariableManagementTest.testUntypedVariableStoredAsReal()` (new)
- Unit test: `VariableManagementTest.testPutMapDoubleAcceptsUntyped()` (new)

---

### Phase 2: Retrieval Path Updates (Day 1, ~2 hours)
**Goal:** Remove `_moUntyped` from retrieval logic

**Files Modified:**
1. **VariableManagement.java** (2 methods)

**Specific Changes:**

**2a. getMap() - Remove untyped check**
```java
// Line 147-193, modify line 161-164
OLD (lines 161-164):
if (_moUntyped.containsKey(strWork)) {
    oLogger.debug("-getMap-> retrieving key: <" + strWork + "> [untyped] ");
    return _moUntyped.get(strWork);
}

NEW:
// DELETED - untyped variables now stored in _moReals
```

**2b. mapContainsKey() - Remove untyped check**
```java
// Line 202-221, modify line 212-216
OLD:
if (_moUntyped.containsKey(strWork)
        || _moBooleans.containsKey(strWork)
        || _moIntegers.containsKey(strWork)
        || _moReals.containsKey(strWork)
        || _moStrings.containsKey(strWork)) {
    return true;
}

NEW:
if (_moBooleans.containsKey(strWork)
        || _moIntegers.containsKey(strWork)
        || _moReals.containsKey(strWork)
        || _moStrings.containsKey(strWork)) {
    return true;
}
```

**Tests for Phase 2:**
- Unit test: `VariableManagementTest.testGetMapUntypedVariable()` (modified)
- Integration test: Verify array retrieval with untyped names
- Integration test: Verify `mapContainsKey()` still finds untyped variables

---

### Phase 3: Test Updates (Day 2, ~6 hours)
**Goal:** Replace all `#` suffix test cases with untyped equivalents

**Scope:** Comprehensive replacement in all test files:

**Files to Update:**
1. `src/test/java/eu/gricom/basic/statements/AssignStatementTest.java` (1 test)
2. `src/test/java/eu/gricom/basic/statements/ReadStatementTest.java` (? occurrences)
3. `src/test/java/eu/gricom/basic/statements/ForStatementTest.java` (? occurrences)
4. `src/test/java/eu/gricom/basic/tokenizer/TokenizerTest.java` (2 occurrences)
5. `src/test/java/eu/gricom/basic/tokenizer/NormalizerTest.java` (2 occurrences)
6. `src/test/java/eu/gricom/basic/parser/BasicParserTest.java` (? occurrences)
7. All other test files with `#` (systematic scan needed)

**Replacement Pattern:**
```
OLD: AssignStatement("Test#", ...)  → NEW: AssignStatement("Test", ...)
OLD: "a# = "  → NEW: "a = "
OLD: assertEquals("A#", ...)  → NEW: assertEquals("A", ...)
```

**New Tests to Add:**
```java
@Test
public void testHashSuffixThrowsSyntaxError() {
    SyntaxErrorException exception = assertThrows(
        SyntaxErrorException.class,
        () -> {
            AssignStatement stmt = new AssignStatement("badVar#", 
                new RealValue(1.0));
            stmt.execute();
        }
    );
    assertTrue(exception.getMessage().contains("#"));
    assertTrue(exception.getMessage().contains("unsupported"));
}

@Test
public void testUntypedVariableIsReal() {
    AssignStatement stmt = new AssignStatement("x", new RealValue(3.14));
    stmt.execute();
    Value result = vm.getMap("x");
    assertTrue(result instanceof RealValue);
    assertEquals(3.14, result.toReal(), 0.001);
}
```

---

### Phase 4: System Tests (test/system/*.bas) (Day 2, ~4 hours)
**Goal:** Rewrite all BASIC test programs using `#` suffix

**Test Files to Update:**
Based on earlier scan, update:
- `test/system/test_arithmetic_operators.bas`
- `test/system/test_arrays_dim.bas`
- `test/system/test_complex_expressions.bas`
- (Full list from grep scan needed)

**Replacement Pattern:**
```basic
OLD: 50 a# = 5 + 3
NEW: 50 a = 5 + 3

OLD: 60 PRINT a#
NEW: 60 PRINT a
```

**Validation:**
- Run each test before/after to verify output matches
- Ensure no floating-point precision issues from type changes

---

### Phase 5: Documentation Updates (Day 2, ~2 hours)
**Goal:** Update all documentation reflecting removal of `#` suffix

**Files to Update:**

**5a. doc/BASIC_CODING_STANDARD.md**
- Lines 55-62: Remove `#` row from variable type table
- Update example code (lines 38, 71, 82)
- Add migration section: "Upgrading from 0.1.x to 0.2.0"

**5b. CLAUDE.md (project instructions)**
- Update "Variable Types" section
- Update code examples

**5c. README.md**
- Add breaking change note to changelog
- Add migration guide

**5d. Any API documentation**
- Update JavaDoc on `VariableManagement` class
- Update any architectural docs

**New Migration Guide Section (add to BASIC_CODING_STANDARD.md):**
```markdown
## Migration Guide: 0.1.x to 0.2.0

### Breaking Change: Removal of # Suffix

**Version 0.2.0 removes the # suffix.** Code using `#` for real variables must be updated.

**What changed:**
- `#` suffix is no longer supported (syntax error if used)
- Untyped variables (no suffix) now default to REAL type
- `!` suffix still available for explicit DOUBLE type

**How to migrate:**

| Old Code | New Code | Reason |
|----------|----------|--------|
| `X# = 3.14` | `X = 3.14` | Untyped defaults to real |
| `a# = a# + 1` | `a = a + 1` | Simple case |
| `PRINT x#` | `PRINT x` | Works with untyped |
| `SUM# = A# + B#` | `SUM = A + B` | Recommended approach |
| `RESULT! = 2.71` | `RESULT! = 2.71` | No change for ! suffix |

**Valid patterns in 0.2.0:**
- Untyped (default real): `X = 3.14`, `a = a + 1`
- Explicit real: `Y! = 2.71` (explicit double)
- Integer: `N% = 42`, `M& = 1000000`
- String: `NAME$ = "Alice"`
- Boolean: `FLAG@ = TRUE`
```

---

### Phase 6: Version & Integration Tests (Day 3, ~3 hours)
**Goal:** Verify full integration and prepare release

**6a. Version Bump**
- `pom.xml`: Change `<revision>0.1.1</revision>` to `<revision>0.2.0</revision>`
- `CLAUDE.md`: Update version number
- Create commit with version bump

**6b. Full Build & Test Suite**
```bash
mvn clean test package
# Expected: ALL tests pass with 0 failures
# Expected: No warnings about deprecated `#` usage
```

**6c. Manual Integration Tests**
1. Compile sample program with untyped variables
2. Verify output matches previous version's output (except syntax)
3. Test array operations with untyped names
4. Test mixed type expressions

**6d. Regression Test Suite**
Run provided system tests:
```bash
test/system/run_all_tests.sh
# Expected: All tests pass
```

---

## Type Detection Logic: New Algorithm

The new type detection algorithm in `putMap(String, Value)`:

```
IF variable_name.contains("$"):
    → Store in STRING map
ELSE IF variable_name.contains("%"):
    → Store in INTEGER map
ELSE IF variable_name.contains("&"):
    → Store in LONG map
ELSE IF variable_name.contains("!"):
    → Store in REAL map
ELSE IF variable_name.contains("@"):
    → Store in BOOLEAN map
ELSE IF variable_name.contains("#"):
    → THROW SyntaxErrorException (UNSUPPORTED)
ELSE:
    → UNDEFINED (treat as REAL, store in REAL map)
```

---

## Error Messages

### 1. Hash Suffix in Assignment
```
Syntax Error: Variable name [myVar#] uses unsupported '#' suffix. 
Use untyped (no suffix) or '!' for real numbers.
```

### 2. Hash in putMap(String, double)
```
Syntax Error: Variable name [myVar#] cannot store double value. 
Use untyped (no suffix) or '!' for real numbers.
```

### 3. Unknown Variable (improved)
```
Runtime Error: Unknown variable <myVar#>
(Note: '#' suffix is not supported in version 0.2.0. Use untyped variables instead.)
```

---

## Test Strategy

### Unit Test Coverage

**VariableManagementTest.java (new tests):**
```java
class VariableManagementTestHashRemoval {
    @Test void testHashSuffixThrowsExceptionPutMapValue() { }
    @Test void testHashSuffixThrowsExceptionPutMapDouble() { }
    @Test void testUntypedVariableStoredAsReal() { }
    @Test void testPutMapDoubleAcceptsUntyped() { }
    @Test void testPutMapDoubleRejectsTypedVariables() { }
    @Test void testGetMapReturnsUntypedAsReal() { }
    @Test void testMapContainsKeyFindsUntyped() { }
    @Test void testArraysWithUntypedNames() { }
}
```

**AssignStatementTest.java (updates):**
- Change all `"Test#"` → `"Test"`
- Add new test for hash suffix rejection

**ReadStatementTest.java (updates):**
- Replace all hash references

**ForStatementTest.java (updates):**
- Replace all hash references

### Integration Test Coverage

**BasicParserTest.java (updates):**
- Update all test programs using `#`
- Add test for syntax error when `#` is used

### System Test Coverage

**test/system/*.bas files:**
- Update ~10-20 BASIC test programs
- Run full test suite: `test/system/run_all_tests.sh`

---

## Success Criteria

Each phase must meet these criteria before proceeding:

### Phase 1 Success
- [ ] `mvn test` passes with no failures
- [ ] New unit tests pass
- [ ] `#` suffix in any variable name throws `SyntaxErrorException`
- [ ] Error messages are clear and actionable
- [ ] `_moUntyped` map removed (no compilation errors)

### Phase 2 Success
- [ ] `mvn test` passes with no failures
- [ ] Untyped variable retrieval returns values correctly
- [ ] `mapContainsKey()` finds untyped variables
- [ ] Array operations with untyped names work
- [ ] No references to `_moUntyped` remain in codebase

### Phase 3 Success
- [ ] All unit tests updated to use untyped variables
- [ ] All unit tests pass
- [ ] Code coverage remains ≥95% for modified files
- [ ] No test uses `#` suffix

### Phase 4 Success
- [ ] All `.bas` test files updated
- [ ] `test/system/run_all_tests.sh` passes
- [ ] Output of updated tests matches semantically (values match)
- [ ] No floating-point precision issues

### Phase 5 Success
- [ ] Documentation builds without errors
- [ ] Migration guide is clear and complete
- [ ] All code examples use untyped or `!` syntax
- [ ] README contains breaking change notice

### Phase 6 Success
- [ ] `mvn clean test package` produces error-free build
- [ ] Version is 0.2.0 in all files
- [ ] All integration tests pass
- [ ] `test/system/run_all_tests.sh` passes
- [ ] Manual spot-checks confirm expected behavior

---

## Risk Assessment & Mitigations

### Risk 1: Unintended Variable Type Coercion
**Severity:** Medium  
**Description:** Untyped variables stored as REAL might cause unexpected behavior if code assumed they were in `_moUntyped` for some reason.  
**Mitigation:**
- Comprehensive test coverage of all retrieval paths
- Verify array operations continue to work correctly
- Run full system test suite

### Risk 2: Hash in Array Indices
**Severity:** Low  
**Description:** Code like `matrix#(5)` has `#` before array index; normalizer must handle correctly.  
**Mitigation:**
- Test array operations during Phase 2
- Verify `Normalizer.normalizeIndex()` correctly preserves suffix
- Scan test code for edge cases

### Risk 3: String Literals Containing #
**Severity:** Low  
**Description:** String constants like `"This costs #5"` should not trigger error.  
**Mitigation:**
- Lexer already handles quoted strings; `#` in strings is tokenized separately
- Unit test with string containing `#`
- Verify lexer tests pass

### Risk 4: Performance Impact
**Severity:** Very Low  
**Description:** Removing `_moUntyped` map and its lookups might subtly change timing.  
**Mitigation:**
- Lookup sequence is now shorter (4 maps vs 5)
- Overall should be slightly faster
- Not a concern for typical programs

### Risk 5: Incomplete Test Coverage
**Severity:** Medium  
**Description:** Some `#` usage might be missed during test migration.  
**Mitigation:**
- Systematic grep scan before Phase 3
- Use CI/CD to verify all tests pass
- Manual verification of sample programs

### Risk 6: Breaking External Code
**Severity:** High  
**Description:** Users with existing programs using `#` will need to rewrite.  
**Mitigation:**
- Clear major version bump (0.2.0)
- Comprehensive migration guide
- Informative error messages guiding fix
- Document in README prominently

---

## Files Changed Summary

| File | Phase | Type | Change |
|------|-------|------|--------|
| `VariableManagement.java` | 1-2 | Core | Remove `#`, add helper method, update type routing |
| `VariableManagementTest.java` | 1 | Test | Add 7 new unit tests |
| `AssignStatementTest.java` | 3 | Test | Replace `#` with untyped |
| `ReadStatementTest.java` | 3 | Test | Replace `#` with untyped |
| `ForStatementTest.java` | 3 | Test | Replace `#` with untyped |
| `TokenizerTest.java` | 3 | Test | Replace 2 `#` references |
| `NormalizerTest.java` | 3 | Test | Replace 2 `#` references |
| `BasicParserTest.java` | 3 | Test | Replace `#` references |
| `test/system/*.bas` | 4 | System | Replace all `#` with untyped (~10-20 files) |
| `BASIC_CODING_STANDARD.md` | 5 | Doc | Remove `#` table row, add migration guide |
| `CLAUDE.md` | 5 | Doc | Update version, variable types |
| `README.md` | 5 | Doc | Add breaking change notice |
| `pom.xml` | 6 | Build | Version 0.1.1 → 0.2.0 |

**Total files:** ~30 files  
**Total changes:** ~150 individual edits (mostly replacements)

---

## Detailed Method Signatures Changed

### Before
```java
public final void putMap(final String strKey, final double dValue) 
    throws SyntaxErrorException
// Accepts: variables with ! or # suffix
// Rejects: untyped, %,  &, $, @ suffixed variables
```

### After
```java
public final void putMap(final String strKey, final double dValue) 
    throws SyntaxErrorException
// Accepts: variables with ! suffix OR untyped (no suffix)
// Rejects: #, %, &, $, @ suffixed variables
// Reason: Untyped variables now default to REAL type
```

### Before
```java
public final Value getMap(final String strKey) 
    throws SyntaxErrorException, RuntimeException
// Checks: _moUntyped first, then strings, integers, reals, booleans
```

### After
```java
public final Value getMap(final String strKey) 
    throws SyntaxErrorException, RuntimeException
// Checks: strings, integers, reals, booleans (removed untyped)
// Note: untyped variables now stored in _moReals, found in that lookup
```

### New Helper Method
```java
private boolean _hasTypeSuffix(final String strKey)
// Returns: true if variable has any type suffix ($, %, &, !, @)
// Returns: false if variable is untyped
```

---

## Backward Compatibility Statement

**This is a BREAKING CHANGE.**

### What Breaks
- Any BASIC program using `#` suffix will fail with `SyntaxErrorException`
- Compiled Java code using the old `VariableManagement` API expecting `_moUntyped` to exist will not compile

### What Doesn't Break
- Programs using `!`, `%`, `&`, `$`, `@` suffixes work unchanged
- Programs using untyped variables work unchanged (and now work better)
- Variable assignment via untyped variables continues to work
- Array operations continue to work
- All expression evaluation continues to work

### Migration Path
- Change `X# = value` to `X = value`
- Change `PRINT x#` to `PRINT x`
- No logic changes needed; straightforward find-and-replace

---

## Timeline & Effort Estimate

| Phase | Tasks | Duration | Risk |
|-------|-------|----------|------|
| 1 | Implement error handling | 4 hours | Medium |
| 2 | Update retrieval paths | 2 hours | Low |
| 3 | Update unit tests | 6 hours | Medium |
| 4 | Update system tests | 4 hours | Low |
| 5 | Update documentation | 2 hours | Low |
| 6 | Integration & release | 3 hours | Medium |
| **Total** | | **21 hours** | |

**Parallel work:** Phases 3, 4, 5 can overlap with 1-2  
**Critical path:** 1 → 2 → 6 (others can run in parallel)  
**Realistic timeline:** 3-4 business days with testing

---

## Implementation Checklist

### Pre-Implementation
- [ ] Code review of this plan
- [ ] Backup current state/create branch
- [ ] Verify current test suite passes (`mvn clean test package`)

### Phase 1
- [ ] Add `_hasTypeSuffix()` helper method
- [ ] Modify `putMap(String, Value)` to reject `#`
- [ ] Modify `putMap(String, double)` to accept untyped
- [ ] Remove `_moUntyped` declaration
- [ ] Verify code compiles
- [ ] Create unit tests for Phase 1
- [ ] Run Phase 1 tests: ✓ pass

### Phase 2
- [ ] Remove `_moUntyped` from `getMap()`
- [ ] Remove `_moUntyped` from `mapContainsKey()`
- [ ] Run Phase 1+2 tests: ✓ pass
- [ ] Manual test: retrieve untyped variable
- [ ] Manual test: array operations

### Phase 3
- [ ] Scan all test files for `#` usage
- [ ] Update `AssignStatementTest.java`
- [ ] Update `ReadStatementTest.java`
- [ ] Update `ForStatementTest.java`
- [ ] Update `TokenizerTest.java`
- [ ] Update `NormalizerTest.java`
- [ ] Update `BasicParserTest.java`
- [ ] Run full test suite: ✓ pass (0 failures)

### Phase 4
- [ ] Identify all `.bas` files using `#`
- [ ] Update `test_arithmetic_operators.bas`
- [ ] Update other identified `.bas` files
- [ ] Run system tests: ✓ all pass
- [ ] Verify output semantics unchanged

### Phase 5
- [ ] Update `BASIC_CODING_STANDARD.md` variable type table
- [ ] Add migration guide to documentation
- [ ] Update `CLAUDE.md`
- [ ] Update `README.md` with breaking change notice
- [ ] Verify doc builds cleanly

### Phase 6
- [ ] Update `pom.xml` version to 0.2.0
- [ ] Update version in `CLAUDE.md`
- [ ] Run `mvn clean test package`: ✓ success
- [ ] Run system tests: ✓ all pass
- [ ] Manual integration tests
- [ ] Code review
- [ ] Create commit(s)
- [ ] Create pull request (if applicable)
- [ ] Mark complete

---

## References & Related Code

**Key files:**
- `/src/main/java/eu/gricom/basic/memoryManager/VariableManagement.java` (lines 28, 48-84, 93-102, 147-193, 202-221)
- `/src/main/java/eu/gricom/basic/tokenizer/Normalizer.java` (lines 136-159)
- `/src/main/java/eu/gricom/basic/statements/AssignStatement.java` (lines 28-108)
- `/src/main/java/eu/gricom/basic/statements/VariableExpression.java` (lines 38-86)
- `/doc/BASIC_CODING_STANDARD.md` (lines 55-62)

**Related test files:**
- Identified ~15 test files needing updates
- ~20 system test `.bas` files needing updates

---

## Conclusion

This plan provides a clear, low-risk path to removing the `#` suffix while maintaining system integrity and comprehensive test coverage. The change is well-scoped, well-documented, and broken into manageable phases with clear success criteria at each stage.

The approach prioritizes:
1. **Clarity:** Explicit error messages guide users
2. **Completeness:** All code paths are addressed
3. **Testing:** Comprehensive test coverage at each phase
4. **Documentation:** Clear migration path for users
5. **Safety:** Rollback possible at any phase

---

**Plan Version:** 1.0  
**Last Updated:** 2026-06-16  
**Status:** Ready for Review & Implementation
