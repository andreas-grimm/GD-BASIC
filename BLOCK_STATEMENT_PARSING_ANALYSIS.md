# Block Statement Parsing - Detailed Analysis and Implementation Plan

## Problem Statement

Currently, `parseBlockStatements()` in BasicParser is a stub that returns an empty list. To complete block IF support, we need to extract statement parsing logic from the main parser switch statement and reuse it in the block parsing context.

## Current Architecture

### Main Parser Loop (parse() method, lines 137-689)
```
while (bContinue):
    switch (getToken(0).getType()):
        case PRINT:     → 541-571 (parse and create PrintStatement)
        case READ:      → 574-603 (parse and create ReadStatement)
        case WORD:      → 636-682 (parse assignment or array assignment)
        case INPUT:     → 489-496 (parse and create InputStatement)
        case GOTO:      → 411-420 (parse and create GotoStatement)
        case GOSUB:     → 421-428 (parse and create GosubStatement)
        case FOR:       → 355-410 (parse and create ForNextLoopStatement)
        case WHILE:     → 624-633 (parse and create WhileStatement)
        case IF:        → 431-485 (parse and create IfThenStatement)
        case RETURN:    → 510-515 (parse and create ReturnStatement)
        ...
```

Each case:
1. Gets the current position: `int iOrgPosition = _iPosition;`
2. Logs the statement: `_oLogger.debug(...)`
3. Records line number: `_oLineNumber.putLineNumber(getToken(0).getLine(), iOrgPosition);`
4. Advances position: `_iPosition++;` or `_iPosition += 2;`
5. Parses statement details (expressions, tokens, etc.)
6. Creates a Statement object
7. Adds to list: `aoStatements.add(new XxxStatement(...))`

## Key Statement Types Needed for Blocks

### 1. PRINT Statement (lines 541-571)
```java
// Current code structure:
int iPrintPosition = _iPosition;
List<Expression> aoExpression = new ArrayList<>();
boolean bCRLF = true;

_oLogger.debug("-parse-> found Token: <" + iPrintPosition + "> [PRINT] ");
_oLineNumber.putLineNumber(getToken(0).getLine(), iPrintPosition);
_iPosition++;

// Parse print arguments
if (getToken(0).getType() != BasicTokenType.NUMBER
    && getToken(0).getType() != BasicTokenType.STRING
    && getToken(0).getType() != BasicTokenType.WORD) {
    aoExpression.add(new StringValue(" "));
} else {
    aoExpression.add(expression());
}

while (getToken(0).getType() == BasicTokenType.COMMA) {
    _iPosition++;
    aoExpression.add(expression());
}

if (getToken(0).getType() == BasicTokenType.SEMICOLON) {
    _iPosition++;
    bCRLF = false;
}

aoStatements.add(new PrintStatement(iPrintPosition, aoExpression, bCRLF));
```

**Extraction Strategy**: Extract into `private Statement parsePrintStatement()` method
- Takes no parameters (uses current parser state)
- Returns PrintStatement
- Handles all expression parsing internally

### 2. WORD Statement (Assignment & Array Assignment, lines 636-682)

Two sub-types:
- **Simple Assignment**: `LET x = expression` or `x = expression`
- **Array Assignment**: `array(idx1, idx2, ...) = expression`

```java
// Simple assignment:
String strName = getToken(0).getText();
_iPosition = _iPosition + 2;  // skip NAME and =
Expression oExpression = expression();
aoStatements.add(new AssignStatement(iCurrPosition, strName, oExpression));

// Array assignment:
String strName = getToken(0).getText();
_iPosition = _iPosition + 2;  // skip NAME and (

List<Expression> aoIndices = new ArrayList<>();
aoIndices.add(expression());

while (getToken(0).getType() == BasicTokenType.COMMA) {
    _iPosition++;
    aoIndices.add(expression());
}

if (getToken(0).getType() != BasicTokenType.RIGHT_PAREN)
    throw exception...

_iPosition++;  // skip )

if (getToken(0).getType() != BasicTokenType.ASSIGN_EQUAL)
    throw exception...

_iPosition++;  // skip =

Expression oValue = expression();
aoStatements.add(new ArrayAssignStatement(iCurrPosition, strName, aoIndices, oValue));
```

**Extraction Strategy**: Extract into `private Statement parseWordStatement()` method
- Checks next token to determine assignment type
- Delegates to either simple or array assignment parsing
- Handles error checking

### 3. READ Statement (lines 574-603)

```java
int iReadPosition = _iPosition;
List<String> astrVariables = new ArrayList<>();
_iPosition++;

Token oReadToken = getToken(0);
if (oReadToken.getType() != BasicTokenType.WORD)
    throw exception...

astrVariables.add(oReadToken.getText());
_iPosition++;

while (getToken(0).getType() == BasicTokenType.COMMA) {
    _iPosition++;
    oReadToken = getToken(0);
    if (oReadToken.getType() != BasicTokenType.WORD)
        throw exception...
    astrVariables.add(oReadToken.getText());
    _iPosition++;
}

aoStatements.add(new ReadStatement(iReadPosition, astrVariables));
```

**Extraction Strategy**: Extract into `private Statement parseReadStatement()` method

### 4. INPUT Statement (lines 489-496)

```java
_oLogger.debug("-parse-> found Token: <" + _iPosition + "> [INPUT] ");
_oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
_iPosition++;
aoStatements.add(new InputStatement(_iPosition - 1, 
                                   consumeToken(BasicTokenType.WORD).getText()));
```

**Extraction Strategy**: Extract into `private Statement parseInputStatement()` method

### 5. GOTO Statement (lines 411-420)

```java
_oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOTO] ");
_oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
_iPosition++;
strTargetLineNumber = consumeToken(BasicTokenType.NUMBER).getText();
aoStatements.add(new GotoStatement(_iPosition - 1, strTargetLineNumber));
```

**Extraction Strategy**: Extract into `private Statement parseGotoStatement()` method

### 6. GOSUB Statement (lines 421-428)

```java
_oLogger.debug("-parse-> found Token: <" + _iPosition + "> [GOSUB] ");
_oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
_iPosition++;
strTargetLineNumber = consumeToken(BasicTokenType.NUMBER).getText();
aoStatements.add(new GosubStatement(_iPosition - 1, strTargetLineNumber));
```

**Extraction Strategy**: Extract into `private Statement parseGosubStatement()` method

### 7. FOR Loop (lines 355-410)

```java
iOrgPosition = _iPosition;
_oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
_iPosition++;
String strForVariable = consumeToken(BasicTokenType.WORD).getText();
consumeToken(BasicTokenType.ASSIGN_EQUAL);
Expression oStartValue = expression();
consumeToken(BasicTokenType.TO);
Expression oEndValue = expression();

Expression oStepValue = null;
if (getToken(0).getType() == BasicTokenType.STEP) {
    _iPosition++;
    oStepValue = expression();
}

Token oNextToken = findToken(BasicTokenType.NEXT);
aoStatements.add(new ForNextLoopStatement(oStartValue, oEndValue, oStepValue, 
                                         iOrgPosition, strForVariable, 
                                         oNextToken.getLine()));
```

**Extraction Strategy**: Extract into `private Statement parseForLoop()` method
- **Issue**: Uses `findToken(NEXT)` which scans forward and assumes NEXT exists
- **Block context issue**: When called from within a block, NEXT might not belong to this FOR
- **Solution needed**: Add nesting level tracking

### 8. WHILE Loop (lines 624-633)

```java
iOrgPosition = _iPosition;
_oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
_iPosition++;
Expression oWhileCondition = expression();
_oLogger.debug("-parse-> found Token: <" + (_iPosition - 1) + "> [WHILE]");
Token oEndWhileToken = findToken(BasicTokenType.ENDWHILE);
_oLogger.debug("-parse-> followed Token: <" + oEndWhileToken.getLine());
aoStatements.add(new WhileStatement(iOrgPosition, oWhileCondition, 
                                   oEndWhileToken.getLine()));
```

**Extraction Strategy**: Extract into `private Statement parseWhileLoop()` method
- **Same issue as FOR**: `findToken(ENDWHILE)` assumes nesting

### 9. RETURN Statement (lines 510-515)

```java
_oLogger.debug("-parse-> found Token: <" + _iPosition + "> [RETURN]");
_oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
aoStatements.add(new ReturnStatement(_iPosition));
_iPosition++;
```

**Extraction Strategy**: Extract into `private Statement parseReturnStatement()` method

## Implementation Strategy

### Phase 1: Extract Simple Statements (No Nesting Issues)

These statements don't require forward scanning beyond their own boundaries:
1. `private Statement parsePrintStatement()`
2. `private Statement parseWordStatement()` (assignment)
3. `private Statement parseReadStatement()`
4. `private Statement parseInputStatement()`
5. `private Statement parseGotoStatement()`
6. `private Statement parseGosubStatement()`
7. `private Statement parseReturnStatement()`

### Phase 2: Handle Nested Loop Statements

**Challenge**: FOR, WHILE, and IF statements use `findToken()` which searches globally and doesn't respect nesting.

**Solution**: Implement block-aware finding with nesting tracking:

```java
/**
 * Find a token of a specific type that belongs to the current context.
 * Respects nesting levels for nested blocks.
 * 
 * @param oType the token type to find
 * @param bAllowNesting whether to allow nesting (true for NEXT with FOR, etc.)
 * @return the found token
 * @throws SyntaxErrorException if not found at correct nesting level
 */
private Token findTokenInContext(BasicTokenType oType, 
                                 BasicTokenType oStartType,
                                 boolean bAllowNesting) 
    throws SyntaxErrorException {
    
    int iNestingLevel = 0;
    int iCurrentPosition = _iPosition;
    
    while (iCurrentPosition < _aoTokens.size()) {
        Token oToken = _aoTokens.get(iCurrentPosition);
        
        // Track nesting depth
        if (oToken.getType() == oStartType) {
            iNestingLevel++;
        }
        
        // Found matching closing token at correct level
        if (oToken.getType() == oType && iNestingLevel <= 0) {
            return oToken;
        }
        
        // Track nesting exit
        if (oToken.getType() == oType) {
            iNestingLevel--;
        }
        
        iCurrentPosition++;
    }
    
    throw new SyntaxErrorException("Missing " + oType + " for " + oStartType);
}
```

### Phase 3: Complete parseBlockStatements()

```java
private List<Statement> parseBlockStatements(final int iBlockEndLine) 
    throws SyntaxErrorException {
    
    List<Statement> aoBlockStatements = new ArrayList<>();
    
    while (_iPosition < _aoTokens.size()) {
        BasicTokenType oTokenType = getToken(0).getType();
        int iCurrentLine = getToken(0).getLine();
        
        // Stop at block boundaries
        if ((oTokenType == BasicTokenType.ELSE || 
             oTokenType == BasicTokenType.ENDIF) && 
            iCurrentLine == iBlockEndLine) {
            break;
        }
        
        // Stop at end of program
        if (oTokenType == BasicTokenType.EOP) {
            break;
        }
        
        // Skip colons (statement separators)
        if (oTokenType == BasicTokenType.COLON) {
            _iPosition++;
            continue;
        }
        
        // Parse the statement based on its type
        Statement oStatement = null;
        
        try {
            switch (oTokenType) {
                case PRINT:
                    oStatement = parsePrintStatement();
                    break;
                case INPUT:
                    oStatement = parseInputStatement();
                    break;
                case WORD:
                case LET:
                    oStatement = parseWordStatement();
                    break;
                case READ:
                    oStatement = parseReadStatement();
                    break;
                case GOTO:
                    oStatement = parseGotoStatement();
                    break;
                case GOSUB:
                    oStatement = parseGosubStatement();
                    break;
                case FOR:
                    oStatement = parseForLoop();
                    break;
                case WHILE:
                    oStatement = parseWhileLoop();
                    break;
                case IF:
                    oStatement = parseIfStatement();
                    break;
                case RETURN:
                    oStatement = parseReturnStatement();
                    break;
                case REM:
                    _iPosition++;
                    continue;  // Skip comments
                default:
                    // Unknown statement in block
                    _iPosition++;
                    continue;
            }
            
            if (oStatement != null) {
                aoBlockStatements.add(oStatement);
            }
        } catch (SyntaxErrorException e) {
            // Re-throw with block context
            throw new SyntaxErrorException("Error in block at line " + 
                                          getToken(0).getLine() + ": " + 
                                          e.getMessage());
        }
    }
    
    return aoBlockStatements;
}
```

## Implementation Complexity Analysis

### Easy (Low Risk - Can be extracted directly)
- PRINT (40 lines of logic)
- READ (30 lines of logic)
- INPUT (10 lines)
- GOTO (10 lines)
- GOSUB (10 lines)
- RETURN (5 lines)
- **Subtotal**: ~105 lines, straightforward extraction

### Medium (Medium Risk - Need careful handling of expressions)
- WORD/Assignment (50 lines including both simple and array)
- **Risk**: Expression parsing is shared; need to ensure it works correctly in block context

### Hard (Higher Risk - Requires nesting awareness)
- FOR Loop (~60 lines, uses findToken for NEXT)
- WHILE Loop (~10 lines, uses findToken for ENDWHILE)
- IF Statement (complex, recursive)
- **Risk**: Current `findToken()` doesn't respect nesting; need block-aware implementation
- **Complexity**: Need to track nesting levels for proper block boundaries

## Recommended Implementation Order

1. **Step 1**: Extract and test PRINT, READ, INPUT parsing (easy wins, validates approach)
2. **Step 2**: Extract and test WORD/assignment parsing (medium complexity)
3. **Step 3**: Extract and test GOTO/GOSUB/RETURN (easy, builds on step 1)
4. **Step 4**: Implement block-aware `findTokenInContext()` method
5. **Step 5**: Extract and test FOR/WHILE parsing with nesting
6. **Step 6**: Update IF parsing to use block-aware finding
7. **Step 7**: Comprehensive integration testing with all statement types
8. **Step 8**: Add support for complex nested scenarios

## Risk Mitigation

1. **Unit Test Each Extract**: Create tests for each extracted method
2. **Compare Outputs**: Ensure extracted methods produce identical statements to original
3. **Incremental Integration**: Test each addition to parseBlockStatements()
4. **Regression Tests**: Run full system test suite after each phase
5. **Nesting Tests**: Create specific tests for nested blocks:
   - IF within IF
   - FOR within IF
   - WHILE within FOR
   - Multiple nesting levels

## Expected Effort

- Phase 1 (Simple statements): 2-3 hours
- Phase 2 (Nested statements): 3-4 hours
- Phase 3 (Integration & testing): 2-3 hours
- **Total**: 7-10 hours of focused development

## Validation Criteria

After implementation, these should work:

```basic
10 IF x > 5 THEN
20   PRINT "x is greater than 5"
30   y = x * 2
40 END-IF

10 IF x > 5 THEN
20   FOR i = 1 TO 10
30     PRINT i
40   NEXT
50 ELSE
60   PRINT "x is not greater than 5"
70 END-IF
```

All 34 system tests should pass, with particular focus on:
- test_if_then_else.bas
- test_comparison_operators.bas
- test_logical_operators.bas
- Complex nested block structures
