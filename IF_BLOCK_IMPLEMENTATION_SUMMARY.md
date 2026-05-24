# IF-Block Support Implementation - Progress Report

## Current Branch
`if-block-support` - Created for implementing multi-line IF-THEN-ELSE-END-IF blocks

## Completed Changes

### 1. IfThenStatement Class (statements/IfThenStatement.java)

#### Added Fields
- `_aoIfBlockStatements`: `List<Statement>` - stores statements in IF block
- `_aoElseBlockStatements`: `List<Statement>` - stores statements in ELSE block  
- `_bHasBlockStatements`: `boolean` - flag indicating this IF has block statements

#### New Constructor
```java
public IfThenStatement(Expression oCondition, int iTokenNumber, 
                       List<Statement> aoIfBlockStatements,
                       List<Statement> aoElseBlockStatements, int iEndIfLine)
```
- Designed to handle multi-line block IF statements
- Stores statement lists instead of relying on line number jumping

#### Modified execute() Method
- Added block statement execution logic at the beginning
- If `_bHasBlockStatements` is true:
  - Evaluates condition
  - Executes IF block statements if true
  - Executes ELSE block statements if false
- Falls back to line-number-based execution for legacy IF statements

#### New Getter Methods
- `getIfBlockStatements()`: Returns IF block statements
- `getElseBlockStatements()`: Returns ELSE block statements
- `hasBlockStatements()`: Returns whether this IF has block statements

### 2. BasicParser Class (parser/BasicParser.java)

#### New Helper Methods

**isStatementKeyword(BasicTokenType)**
- Identifies if a token is a statement keyword (PRINT, INPUT, LET, WORD, etc.)
- Used to distinguish between:
  - Line number after THEN: `IF x THEN 100`
  - Statement after THEN: `IF x THEN PRINT "msg"`
  - Block IF: `IF x THEN` (on separate line)

**findTokenOnNextLine(BasicTokenType)**
- Locates a token type on subsequent lines without changing parser position
- Better than existing `findToken()` for block IF detection
- Throws SyntaxErrorException if token not found

**findTokenPosition(BasicTokenType)**
- Returns the index position of a token without changing parser state
- Used to skip to ENDIF or ELSE positions while maintaining parsing

**parseBlockStatements(int iBlockEndLine)** *(PARTIAL)*
- Currently incomplete stub
- Should parse all statements between THEN and ELSE/ENDIF
- Needs integration with main switch statement parsing logic

**parseInlineStatement()** *(PARTIAL)*
- Currently handles only PRINT statements
- Should support all statement types: INPUT, assignment, GOTO, etc.
- Needs expansion to complete inline IF support

#### Modified IF Case (lines ~431-485)

Replaced existing IF handling with three-branch logic:

```
IF token sequence:
├─ IF <condition> THEN <NUMBER>
│  └─ Single-line IF with line number (WORKING - legacy behavior)
│
├─ IF <condition> THEN <STATEMENT>
│  └─ Inline IF with statement (PARTIAL - PRINT only)
│
└─ IF <condition> THEN
   <statements>
   [ELSE]
   <statements>
   END-IF
   └─ Block IF (DETECTED - execution incomplete)
```

## Current Status

### ✅ Working
1. Block IF detection no longer throws "Missing statement END-IF" error
2. Parser identifies block IF vs single-line IF vs inline IF correctly
3. IfThenStatement stores and executes block statements when properly parsed
4. Compilation successful, all 865 unit tests still pass

### ⚠️ Partial/Incomplete
1. **Block statement parsing**: parseBlockStatements() returns empty list
   - Statements between THEN/ELSE/ENDIF are not being collected
   - Need to extract PRINT, assignment, and other statement parsing from main switch
   
2. **Inline statement support**: Only PRINT partially supported
   - Other statement types cause exceptions
   - Need to extend parseInlineStatement() for all statement types

### ❌ Not Working Yet
1. Block IF statements execute but don't run the intended statements
2. Inline IF statements with most statement types fail
3. Nested IF blocks within IF blocks

## Architecture Challenges Identified

1. **Parser Structure Issue**: The main parser uses a large switch statement with embedded statement parsing
   - Each statement type is parsed inline in the switch case
   - Extracting this logic into helper methods requires careful refactoring
   - Risk of breaking existing parsing logic if not done carefully

2. **Statement vs. Expression Context**: 
   - Some constructs can appear in multiple contexts (e.g., PRINT can be standalone or inline)
   - Parser needs to handle both contexts correctly

3. **Block Boundary Detection**:
   - Must correctly identify where blocks end (ELSE vs ENDIF)
   - Must handle nested structures
   - Must maintain proper position tracking during multi-line parsing

## Work Remaining

### High Priority (Required for basic functionality)
1. Complete `parseBlockStatements()` implementation
   - Integrate with existing statement parsing logic
   - Properly collect statements from different case branches
   - Handle all statement types (PRINT, INPUT, assignment, GOTO, loops, etc.)

2. Complete `parseInlineStatement()` implementation
   - Support all statement types, not just PRINT
   - Properly parse arguments and expressions

3. Test with system tests
   - Verify block IF statements work correctly
   - Test with ELSE blocks
   - Test nested IF statements

### Medium Priority (Improvements)
1. Refactor main parser switch statement
   - Extract common statement parsing logic
   - Create unified methods for each statement type
   - Reduce code duplication

2. Improve error messages for block IF parsing
   - Better diagnostics when blocks are malformed
   - Clear indication of nesting issues

### Low Priority (Future enhancements)
1. Support other block constructs using same pattern
   - FOR-NEXT blocks (partially works with line numbers)
   - WHILE-WEND blocks
   - DO-UNTIL blocks

2. Optimize block statement storage
   - Consider memory usage for large blocks
   - Possible statement compression/optimization

## Files Modified
- `src/main/java/eu/gricom/basic/statements/IfThenStatement.java` (+60 lines)
- `src/main/java/eu/gricom/basic/parser/BasicParser.java` (+155 lines)

## Testing Status
- Unit tests: ✅ All 865 pass
- System tests: ⏳ Not yet working with block IF
- Manual test (block IF detection): ✅ No more "Missing END-IF" error
- Manual test (block IF execution): ❌ Statements not executing

## Next Steps

1. Analyze main parser switch statement structure
2. Extract statement parsing methods carefully
3. Integrate them into parseBlockStatements() and parseInlineStatement()
4. Add comprehensive testing for:
   - Block IF with IF block only
   - Block IF with ELSE block
   - Nested IF blocks
   - Mixed single-line and block IF statements
5. Run system tests and fix any remaining issues
