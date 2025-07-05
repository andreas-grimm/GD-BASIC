# Parser Class Design Documentation

## Overview

The Parser is a critical component of the GD-BASIC interpreter that converts a sequence of tokens into an Abstract Syntax Tree (AST). It implements a **recursive descent parser** that processes BASIC language constructs and generates executable statement objects. The parser now supports **two evaluation modes** controlled by the `_bDartmouthFlag`.

## Architecture

### **Interface Definition**

**Location**: `src/main/java/eu/gricom/basic/parser/Parser.java`

```java
public interface Parser {
    /**
     * Parses the tokenized program into a list of executable statements
     * @return List of Statement objects representing the program
     * @throws SyntaxErrorException for syntax errors
     */
    List<Statement> parse() throws SyntaxErrorException;
}
```

### **Implementation Class**

**`BasicParser`** - Unified implementation supporting both evaluation modes

## BasicParser Implementation

### **Class Structure**

**Location**: `src/main/java/eu/gricom/basic/parser/BasicParser.java`

```java
public class BasicParser implements Parser {
    private final Logger _oLogger;
    private final List<Token> _aoTokens;
    private int _iPosition;
    private final LineNumberXRef _oLineNumber;
    private final boolean _bDartmouthFlag;  // Controls evaluation mode
}
```

### **Core Components**

#### **1. Token Management**
- **`_aoTokens`**: List of tokens from the lexer
- **`_iPosition`**: Current position in the token stream
- **`getToken(int offset)`**: Retrieves token at current position + offset

#### **2. Line Number Tracking**
- **`_oLineNumber`**: Maps token positions to BASIC source line numbers
- **Used for**: GOTO statements, error reporting, debugging

#### **3. Evaluation Mode Control**
- **`_bDartmouthFlag`**: Controls mathematical expression evaluation behavior
  - **`true`**: Dartmouth BASIC mode (left-to-right evaluation)
  - **`false`**: Standard precedence mode (mathematical operator precedence)

### **Parsing Process**

#### **Phase 1: Pre-Run Parsing**
```java
public final List<Statement> parsePreRun() throws SyntaxErrorException
```

**Purpose**: Processes statements that must be executed before the main program starts

**Handled Statements**:
- **DATA**: Defines data for READ statements
- **PRAGMA**: Compiler directives and settings

**Example**:
```basic
10 DATA "Hello", 42, 3.14
20 @PRAGMA OPTIMIZE=ON
```

#### **Phase 2: Main Parsing**
```java
@Override
public final List<Statement> parse() throws SyntaxErrorException
```

**Purpose**: Parses the main program statements

**Process**:
1. **Token Recognition**: Identifies statement types by first token
2. **Statement Construction**: Creates appropriate Statement objects
3. **Expression Parsing**: Parses expressions within statements using selected mode
4. **Error Handling**: Reports syntax errors with line numbers

### **Statement Types Supported**

#### **Control Flow Statements**
- **`GOTO`**: Unconditional jump to line number
- **`IF-THEN-ELSE`**: Conditional execution
- **`FOR-NEXT`**: Loop with counter
- **`DO-UNTIL`**: Loop with condition
- **`WHILE-WEND`**: Loop with condition
- **`GOSUB-RETURN`**: Subroutine calls

#### **Data Manipulation**
- **`LET`/Assignment**: Variable assignment
- **`INPUT`**: User input
- **`PRINT`**: Output
- **`READ`**: Read from DATA statements
- **`DIM`**: Array declaration

#### **Program Structure**
- **`REM`**: Comments
- **`END`**: Program termination
- **`COLON`**: Statement separator

## Expression Parsing Modes

### **Mode Selection**

The parser uses the `_bDartmouthFlag` to determine expression evaluation behavior:

```java
private Expression expression() throws SyntaxErrorException {
    _oLogger.debug("-expression-> <" + _iPosition + "> [" + getToken(0).getType().toString() + "]");
    if (_bDartmouthFlag == true) {
        return operator();  // Dartmouth mode: left-to-right evaluation
    }
    return logicalOr();     // Precedence mode: standard mathematical precedence
}
```

### **Mode 1: Dartmouth BASIC Mode (`_bDartmouthFlag = true`)**

#### **Left-to-Right Evaluation**
```java
public final Expression operator() throws SyntaxErrorException {
    Expression oExpression = atomic();
    
    while (isOperator(getToken(0).getType())) {
        Token oToken = getToken(0);
        _iPosition++;
        Expression oRight = atomic();
        oExpression = new OperatorExpression(oExpression, oToken.getType(), oRight);
    }
    
    return oExpression;
}
```

**Characteristics**: All operators have equal precedence, evaluated left-to-right

**Example**: `1 + 2 * 3` evaluates as `(1 + 2) * 3 = 9` instead of `1 + (2 * 3) = 7`

### **Mode 2: Standard Precedence Mode (`_bDartmouthFlag = false`)**

#### **Mathematical Operator Precedence**

**Precedence Hierarchy**:
```
1. Parentheses and function calls (highest)
2. Unary operators (+, -, NOT)
3. Exponentiation (^) - right-associative
4. Multiplication, Division, Modulo (*, /, %)
5. Addition, Subtraction (+, -)
6. Bitwise shifts (<<, >>)
7. Comparison operators (<, <=, >, >=, ==, !=)
8. Logical operators (AND, OR) (lowest)
```

#### **Method Hierarchy**
```java
expression() → logicalOr() → logicalAnd() → equality() → comparison() 
→ shift() → addition() → multiplication() → exponentiation() → unary() → atomic()
```

#### **Key Precedence Methods**

##### **1. logicalOr() - Logical OR (lowest precedence)**
```java
private Expression logicalOr() throws SyntaxErrorException {
    Expression left = logicalAnd();
    
    while (getToken(0).getType() == BasicTokenType.OR) {
        Token operator = getToken(0);
        _iPosition++;
        Expression right = logicalAnd();
        left = new OperatorExpression(left, operator.getType(), right);
    }
    
    return left;
}
```

##### **2. addition() - Addition and Subtraction**
```java
private Expression addition() throws SyntaxErrorException {
    Expression left = multiplication();
    
    while (getToken(0).getType() == BasicTokenType.PLUS ||
            getToken(0).getType() == BasicTokenType.MINUS) {
        Token operator = getToken(0);
        _iPosition++;
        Expression right = multiplication();
        left = new OperatorExpression(left, operator.getType(), right);
    }
    
    return left;
}
```

##### **3. multiplication() - Multiplication, Division, Modulo**
```java
private Expression multiplication() throws SyntaxErrorException {
    Expression left = exponentiation();
    
    while (getToken(0).getType() == BasicTokenType.MULTIPLY ||
            getToken(0).getType() == BasicTokenType.DIVIDE ||
            getToken(0).getType() == BasicTokenType.MODULO) {
        Token operator = getToken(0);
        _iPosition++;
        Expression right = exponentiation();
        left = new OperatorExpression(left, operator.getType(), right);
    }
    
    return left;
}
```

##### **4. exponentiation() - Exponentiation (right-associative)**
```java
private Expression exponentiation() throws SyntaxErrorException {
    Expression left = unary();
    
    if (getToken(0).getType() == BasicTokenType.POWER) {
        Token operator = getToken(0);
        _iPosition++;
        // Exponentiation is right-associative, so we call exponentiation() recursively
        Expression right = exponentiation();
        left = new OperatorExpression(left, operator.getType(), right);
    }
    
    return left;
}
```

##### **5. unary() - Unary Operators**
```java
private Expression unary() throws SyntaxErrorException {
    if (getToken(0).getType() == BasicTokenType.PLUS ||
            getToken(0).getType() == BasicTokenType.MINUS ||
            getToken(0).getType() == BasicTokenType.NOT) {
        Token operator = getToken(0);
        _iPosition++;
        Expression operand = unary();
        return new UnaryOperatorExpression(operator.getType(), operand);
    }
    
    return atomic();
}
```

##### **6. atomic() - Highest Precedence**
```java
public final Expression atomic() throws SyntaxErrorException {
    // Handles: numbers, variables, parentheses, function calls
    Token oToken = getToken(0);
    
    switch (oToken.getType()) {
        case WORD:
            return new VariableExpression(oToken.getText());
        case NUMBER:
            return new RealValue(Double.parseDouble(oToken.getText()));
        case STRING:
            return new StringValue(oToken.getText());
        case LEFT_PAREN:
            _iPosition++;
            Expression oExpression = expression();
            consumeToken(BasicTokenType.RIGHT_PAREN);
            return oExpression;
        // ... function calls and other cases
    }
}
```

## Error Handling

### **Exception Types**

#### **1. SyntaxErrorException**
- **Thrown by**: Parser when encountering invalid syntax
- **Contains**: Error message, line number, token information
- **Example**: `"Expected NUMBER but found STRING at line 10"`

### **Error Recovery**

#### **Parser Error Recovery**
```java
public final Token consumeToken(final BasicTokenType oType) throws SyntaxErrorException {
    if (getToken(0).getType() != oType) {
        throw new SyntaxErrorException(
            "Expected " + oType + " but found " + getToken(0).getType() + 
            " at line " + getToken(0).getLine()
        );
    }
    return getToken(_iPosition++);
}
```

#### **Line Number Tracking**
```java
_oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
```

## Statement Parsing Examples

### **Assignment Statement**
```basic
10 A# = 5 + 3 * 2
```

**Dartmouth Mode (`_bDartmouthFlag = true`)**:
1. Recognize `A#` as variable
2. Consume `=` token
3. Parse expression `5 + 3 * 2` (left-to-right: `(5 + 3) * 2 = 16`)
4. Create `AssignStatement` object

**Precedence Mode (`_bDartmouthFlag = false`)**:
1. Recognize `A#` as variable
2. Consume `=` token
3. Parse expression `5 + 3 * 2` (standard precedence: `5 + (3 * 2) = 11`)
4. Create `AssignStatement` object

### **IF-THEN Statement**
```basic
20 IF A# > 5 THEN PRINT "Greater"
```

**Parsing Process** (same in both modes):
1. Recognize `IF` token
2. Parse condition `A# > 5`
3. Consume `THEN` token
4. Parse statement `PRINT "Greater"`
5. Create `IfThenStatement` object

### **FOR Loop**
```basic
30 FOR I% = 1 TO 10 STEP 2
```

**Parsing Process** (same in both modes):
1. Recognize `FOR` token
2. Parse variable `I%`
3. Consume `=` token
4. Parse start value `1`
5. Consume `TO` token
6. Parse end value `10`
7. Parse step value `2`
8. Create `ForStatement` object

## Expression Tree Construction Examples

### **Example: 1 + 2 * 3**

#### **Dartmouth Mode (`_bDartmouthFlag = true`)**
```
     *
    / \
   +   3
  / \
 1   2
```
**Result**: `(1 + 2) * 3 = 9`

**Evaluation Order**:
1. `1 + 2 = 3`
2. `3 * 3 = 9`

#### **Precedence Mode (`_bDartmouthFlag = false`)**
```
     +
    / \
   1   *
      / \
     2   3
```
**Result**: `1 + (2 * 3) = 7`

**Evaluation Order**:
1. `2 * 3 = 6`
2. `1 + 6 = 7`

### **Example: 10 - 2 * 3 + 4 / 2**

#### **Dartmouth Mode (`_bDartmouthFlag = true`)**
```
         /
        / \
       +   2
      / \
     -   4
    / \
   10   *
       / \
      2   3
```
**Result**: `((10 - 2) * 3) + 4) / 2 = 14`

**Evaluation Order**:
1. `10 - 2 = 8`
2. `8 * 3 = 24`
3. `24 + 4 = 28`
4. `28 / 2 = 14`

#### **Precedence Mode (`_bDartmouthFlag = false`)**
```
       +
      / \
     -   /
    / \ / \
   10  *   2
      / \
     2   3
```
**Result**: `(10 - (2 * 3)) + (4 / 2) = 6`

**Evaluation Order**:
1. `2 * 3 = 6`
2. `4 / 2 = 2`
3. `10 - 6 = 4`
4. `4 + 2 = 6`

### **Example: -5 + 3**

#### **Dartmouth Mode (`_bDartmouthFlag = true`)**
```
     +
    / \
   -   3
    \
     5
```
**Result**: `(-5) + 3 = -2`

#### **Precedence Mode (`_bDartmouthFlag = false`)**
```
     +
    / \
   -   3
    \
     5
```
**Result**: `(-5) + 3 = -2` (same result, different parsing)

## Performance Considerations

### **Optimizations**

#### **1. Token Lookup**
```java
private Token getToken(final int iOffset) {
    if (_iPosition + iOffset >= _aoTokens.size()) {
        return new Token("", BasicTokenType.EOP, 0);
    }
    return _aoTokens.get(_iPosition + iOffset);
}
```

#### **2. Early Termination**
- Check for `EOP` token to end parsing
- Skip comments and whitespace efficiently

#### **3. Memory Management**
- Reuse token objects where possible
- Minimize object creation during parsing

### **Memory Usage**

#### **Token Storage**
- **Memory**: O(n) where n is number of tokens
- **Access**: O(1) for token lookup

#### **AST Construction**
- **Memory**: O(n) for expression trees
- **Complexity**: Linear with program size

## Testing Strategy

### **Unit Tests**

#### **1. Statement Parsing Tests**
```java
@Test
public void testAssignmentStatement() {
    // Test: A# = 5
    // Verify: AssignStatement with variable A# and value 5
}

@Test
public void testIfThenStatement() {
    // Test: IF A# > 5 THEN PRINT "Hello"
    // Verify: IfThenStatement with condition and statement
}
```

#### **2. Expression Parsing Tests - Dartmouth Mode**
```java
@Test
public void testDartmouthLeftToRightEvaluation() {
    // Test: 1 + 2 * 3 with _bDartmouthFlag = true
    // Verify: Left-to-right evaluation (result = 9, not 7)
}

@Test
public void testDartmouthComplexExpression() {
    // Test: 10 - 2 * 3 + 4 / 2 with _bDartmouthFlag = true
    // Verify: Left-to-right evaluation (result = 14)
}
```

#### **3. Expression Parsing Tests - Precedence Mode**
```java
@Test
public void testPrecedenceStandardEvaluation() {
    // Test: 1 + 2 * 3 with _bDartmouthFlag = false
    // Verify: Standard precedence (result = 7, not 9)
}

@Test
public void testPrecedenceComplexExpression() {
    // Test: 10 - 2 * 3 + 4 / 2 with _bDartmouthFlag = false
    // Verify: Standard precedence (result = 6)
}

@Test
public void testUnaryOperators() {
    // Test: -5, +10, NOT true with _bDartmouthFlag = false
    // Verify: Correct unary operator handling
}
```

#### **4. Error Handling Tests**
```java
@Test
public void testSyntaxError() {
    // Test: Invalid syntax
    // Verify: Appropriate SyntaxErrorException with line number
}
```

### **Integration Tests**

#### **1. Complete Program Parsing**
```java
@Test
public void testCompleteProgram() {
    // Test: Parse entire BASIC program
    // Verify: All statements parsed correctly
}
```

#### **2. Mode Switching Tests**
```java
@Test
public void testModeSwitching() {
    // Test: Same expression with different _bDartmouthFlag values
    // Verify: Different results for Dartmouth vs precedence mode
}
```

## Extension Points

### **Adding New Statement Types**

#### **1. Create Statement Class**
```java
public class NewStatement implements Statement {
    public void execute() throws Exception {
        // Implementation
    }
}
```

#### **2. Add Token Type**
```java
public enum BasicTokenType {
    // ... existing types
    NEW_STATEMENT,
}
```

#### **3. Update Parser**
```java
case NEW_STATEMENT:
    // Parse statement parameters
    return new NewStatement(parameters);
```

### **Adding New Operators**

#### **1. Add Token Type**
```java
public enum BasicTokenType {
    // ... existing types
    NEW_OPERATOR,
}
```

#### **2. Update Expression Parsing**
```java
// In operator() method for Dartmouth mode
while (isOperator(getToken(0).getType())) {
    // Add new operator to condition
}

// In appropriate precedence method for precedence mode
while (getToken(0).getType() == BasicTokenType.NEW_OPERATOR) {
    // Handle new operator
}
```

#### **3. Update Value Interface**
```java
// Add method to Value interface if needed
Value newOperation(Value other) throws Exception;
```

## Compatibility

### **BASIC Language Standards**

#### **Dartmouth BASIC**
- **Supported**: Core Dartmouth BASIC statements
- **Mode**: `_bDartmouthFlag = true`
- **Features**: Line numbers, GOTO, left-to-right expression evaluation

#### **ECMA-55**
- **Supported**: Standard BASIC features
- **Mode**: `_bDartmouthFlag = false`
- **Features**: Arrays, functions, standard mathematical precedence

#### **Modern BASIC**
- **Supported**: Extended features
- **Mode**: `_bDartmouthFlag = false`
- **Features**: Complex expressions, modern control structures

### **Implementation Characteristics**

#### **Dartmouth Mode (`_bDartmouthFlag = true`)**
- **Expression Evaluation**: Left-to-right evaluation
- **Precedence**: All operators have equal precedence
- **Parentheses**: Properly handled for grouping
- **Function Calls**: Supported with proper precedence

#### **Precedence Mode (`_bDartmouthFlag = false`)**
- **Expression Evaluation**: Standard mathematical precedence
- **Precedence**: Full operator precedence hierarchy
- **Unary Operators**: Full support for `+`, `-`, `NOT`
- **Function Calls**: Supported with proper precedence

#### **Statement Support**
- **Complete**: All standard BASIC statements
- **Extensible**: Easy to add new statement types
- **Robust**: Comprehensive error handling

## Usage Examples

### **BASIC Program Example**
```basic
10 REM Simple calculator
20 INPUT "Enter first number: ", A#
30 INPUT "Enter second number: ", B#
40 IF A# > B# THEN PRINT "First is larger"
50 FOR I% = 1 TO 10 STEP 2
60   PRINT I%
70 NEXT I%
80 END
```

### **Java Usage - Dartmouth Mode**
```java
// Create parser with Dartmouth mode
List<Token> tokens = lexer.tokenize(sourceCode);
Parser parser = new BasicParser(tokens, true);  // _bDartmouthFlag = true

// Parse program with left-to-right evaluation
List<Statement> statements = parser.parse();

// Execute statements
for (Statement stmt : statements) {
    stmt.execute();
}
```

### **Java Usage - Precedence Mode**
```java
// Create parser with standard precedence mode
List<Token> tokens = lexer.tokenize(sourceCode);
Parser parser = new BasicParser(tokens, false);  // _bDartmouthFlag = false

// Parse program with standard mathematical precedence
List<Statement> statements = parser.parse();

// Execute statements
for (Statement stmt : statements) {
    stmt.execute();
}
```

### **Expression Examples - Dartmouth Mode**
```basic
10 A# = 1 + 2 * 3        ' Evaluates as (1 + 2) * 3 = 9
20 B# = 10 - 2 * 3 + 4   ' Evaluates as ((10 - 2) * 3) + 4 = 28
30 C# = (1 + 2) * 3      ' Evaluates as 3 * 3 = 9 (parentheses respected)
```

### **Expression Examples - Precedence Mode**
```basic
10 A# = 1 + 2 * 3        ' Evaluates as 1 + (2 * 3) = 7
20 B# = 10 - 2 * 3 + 4   ' Evaluates as (10 - (2 * 3)) + 4 = 8
30 C# = (1 + 2) * 3      ' Evaluates as 3 * 3 = 9 (parentheses respected)
40 D# = -5 + 3           ' Evaluates as (-5) + 3 = -2
```

## Current Features

### **Expression Evaluation**
- **Dual Mode**: Supports both Dartmouth and standard precedence modes
- **Configurable**: Switch between modes via `_bDartmouthFlag`
- **Backward Compatible**: Maintains existing Dartmouth behavior
- **Enhanced**: Adds standard mathematical precedence option

### **Operator Support**
- **Binary Operators**: `+`, `-`, `*`, `/`, `^`, `%`, `<<`, `>>`, `==`, `!=`, `<`, `<=`, `>`, `>=`, `AND`, `OR`
- **Unary Operators**: `+`, `-`, `NOT` (in precedence mode)
- **Function Calls**: Full support for built-in functions
- **Parentheses**: Properly handled in both modes

## Future Enhancements

### **Potential Improvements**

#### **1. Runtime Mode Switching**
- **Dynamic**: Allow mode switching during program execution
- **Per-Expression**: Different modes for different expressions
- **Configuration**: Program-level mode configuration

#### **2. Enhanced Expression Parsing**
- **Type Checking**: Enhanced type safety for expressions
- **Optimization**: Expression optimization and constant folding
- **Debugging**: Better expression tree visualization

#### **3. Performance Optimizations**
- **JIT Compilation**: Compile frequently executed expressions
- **Caching**: Cache parsed expressions
- **Lazy Evaluation**: Defer expression evaluation until needed

### **Language Extensions**

#### **1. Modern Features**
- **Object-Oriented**: Classes, methods, inheritance
- **Modern Control Structures**: Try-catch, switch statements
- **Advanced Data Types**: Structures, pointers

#### **2. Tooling Integration**
- **IDE Support**: Syntax highlighting, code completion
- **Debugging**: Breakpoints, variable inspection
- **Profiling**: Performance analysis tools

## Conclusion

The `BasicParser` class is a robust and well-tested component of the GD-BASIC interpreter that provides comprehensive BASIC language support with **dual evaluation modes**. The integration of precedence functionality offers the best of both worlds: backward compatibility with Dartmouth BASIC and modern mathematical expression evaluation.

**Key Benefits**:
- **Flexibility**: Supports both Dartmouth and standard precedence modes
- **Compatibility**: Maintains backward compatibility with existing code
- **Extensibility**: Easy to add new statements and operators
- **Error Handling**: Comprehensive syntax error detection and reporting
- **Performance**: Efficient parsing with minimal memory overhead

**Current Characteristics**:
- **Dual Mode**: Configurable expression evaluation via `_bDartmouthFlag`
- **Statement Support**: Complete BASIC statement coverage
- **Error Reporting**: Detailed error messages with line numbers
- **Memory Efficiency**: Linear memory usage with program size

**Development Guidelines**:
- **Choose Mode**: Select appropriate evaluation mode for your use case
- **Test Both Modes**: Verify behavior in both Dartmouth and precedence modes
- **Document Mode**: Make evaluation mode clear to users
- **Consider Migration**: Evaluate need for standard precedence in new projects 