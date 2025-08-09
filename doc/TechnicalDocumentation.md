# GD-BASIC Interpreter Technical Documentation

## Table of Contents

1. [Overview](#overview)
2. [System Architecture](#system-architecture)
3. [Core Components](#core-components)
4. [Processing Pipeline](#processing-pipeline)
5. [Memory Management](#memory-management)
6. [Type System](#type-system)
7. [Parser Implementation](#parser-implementation)
8. [Runtime Execution](#runtime-execution)
9. [Error Handling](#error-handling)
10. [Extension Points](#extension-points)
11. [Development Guidelines](#development-guidelines)
12. [Testing Strategy](#testing-strategy)
13. [Performance Considerations](#performance-considerations)
14. [Troubleshooting](#troubleshooting)

## Overview

GD-BASIC (GriCom Diminutive BASIC Interpreter) is a Java-based implementation of a Dartmouth-style BASIC programming language interpreter. The system is designed to be both a standalone interpreter and an embeddable scripting engine for Java applications.

### Key Design Principles

- **Modularity**: Clear separation of concerns across components
- **Extensibility**: Easy to add new language features and functions
- **Type Safety**: Strongly typed variable system with runtime type checking
- **Cross-Platform**: Java-based implementation for platform independence
- **Minimal Dependencies**: Limited external dependencies for portability

### System Requirements

- **Java**: JDK 1.8.0_131 or higher
- **Build Tool**: Maven 3.6.3 or higher
- **Memory**: Minimum 128MB RAM
- **Platforms**: Windows, Linux, macOS

## System Architecture

### High-Level Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Source Code   │───▶│   Tokenizer     │───▶│     Parser      │
│   (.bas file)   │    │   (Lexer)       │    │   (AST Builder) │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Output        │◀───│   Runtime       │◀───│   Statement     │
│   (Console)     │    │   (Executor)    │    │   Objects       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Component Relationships

The interpreter follows a classic compiler/interpreter architecture with these main phases:

1. **Lexical Analysis** (Tokenizer)
2. **Syntax Analysis** (Parser)
3. **Semantic Analysis** (Type checking)
4. **Execution** (Runtime)

## Core Components

### 1. Main Entry Point (`Basic.java`)

**Location**: `src/main/java/eu/gricom/basic/Basic.java`

**Responsibilities**:
- Program initialization and configuration
- Command-line argument processing
- Orchestration of the processing pipeline
- Error handling and exit management

**Key Methods**:
```java
public final void interpret(final Program oProgram)  // Main interpretation
public final void process(final Program oProgram)    // Parsing only
public final void compile(final Program oProgram)    // Code generation
```

### 2. Tokenizer (`BasicLexer.java`)

**Location**: `src/main/java/eu/gricom/basic/tokenizer/BasicLexer.java`

**Responsibilities**:
- Convert source code into tokens
- Handle line-based tokenization (Dartmouth BASIC style)
- Maintain line number information
- Support for comments and whitespace

**Token Types**:
```java
public enum BasicTokenType {
    // Keywords
    IF, THEN, ELSE, FOR, NEXT, WHILE, DO, UNTIL, GOTO, GOSUB, RETURN,
    
    // Operators
    PLUS, MINUS, MULTIPLY, DIVIDE, POWER, MODULO,
    COMPARE_EQUAL, COMPARE_NOT_EQUAL, SMALLER, GREATER,
    
    // Functions
    ABS, SIN, COS, TAN, LOG, EXP, SQR, RND,
    
    // Literals
    NUMBER, STRING, WORD,
    
    // Punctuation
    LEFT_PAREN, RIGHT_PAREN, COMMA, SEMICOLON
}
```

### 3. Parser (`BasicParser.java`)

**Location**: `src/main/java/eu/gricom/basic/parser/BasicParser.java`

**Responsibilities**:
- Build Abstract Syntax Tree (AST) from tokens
- Handle operator precedence (currently left-to-right)
- Create statement objects for execution
- Validate syntax and structure

**Parser Strategy**:
- Recursive descent parsing
- Single-pass token processing
- Error recovery with detailed error messages

### 4. Memory Manager

**Location**: `src/main/java/eu/gricom/basic/memoryManager/`

**Components**:
- `Program.java` - Program state container
- `VariableManagement.java` - Variable storage and retrieval
- `Stack.java` - Call stack for GOSUB/RETURN
- `LineNumberXRef.java` - Line number to statement mapping

**Key Features**:
- Global variable scope
- Dynamic array allocation
- Type-safe variable storage
- Memory cleanup on program end

### 5. Runtime Manager (`Execute.java`)

**Location**: `src/main/java/eu/gricom/basic/runtimeManager/Execute.java`

**Responsibilities**:
- Execute parsed statements
- Manage program flow control
- Handle variable assignments
- Coordinate I/O operations

## Processing Pipeline

### Phase 1: Program Loading

```java
Program oProgram = new Program();
oProgram.load(sourceCode);
```

**Steps**:
1. Read source file or string
2. Store in `Program` object
3. Validate basic structure
4. Prepare for tokenization

### Phase 2: Macro Processing

```java
MacroProcessor oMacroProcessor = new MacroProcessor();
oProgram.setProgram(oMacroProcessor.process(oProgram.getProgram()));
```

**Purpose**:
- Process `DEF FN` macro definitions
- Expand macro calls
- Prepare code for tokenization

### Phase 3: Tokenization

```java
Lexer oTokenizer = new BasicLexer();
oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));
```

**Process**:
1. Split source into lines
2. Process each line character by character
3. Identify tokens and their types
4. Maintain line number information
5. Handle comments and whitespace

### Phase 4: Parsing

```java
BasicParser oParser = new BasicParser(oProgram.getTokens());
oProgram.setStatements(oParser.parse());
```

**Process**:
1. Process tokens sequentially
2. Build expression trees
3. Create statement objects
4. Validate syntax
5. Build execution order

### Phase 5: Execution

```java
Execute oRun = new Execute(oProgram);
oRun.loadEnvironment();
oRun.runProgram();
```

**Process**:
1. Initialize runtime environment
2. Execute statements in order
3. Handle flow control (GOTO, IF, loops)
4. Manage variable assignments
5. Handle I/O operations

## Memory Management

### Variable Storage

**Type System**:
```java
public interface Value {
    String toString();
    double toReal();
    Value plus(Value oValue);
    Value minus(Value oValue);
    Value multiply(Value oValue);
    Value divide(Value oValue);
    // ... other operations
}
```

**Supported Types**:
- `RealValue` - Double precision floating point
- `IntegerValue` - 32-bit integers
- `LongValue` - 64-bit integers
- `StringValue` - Character strings
- `BooleanValue` - Boolean values

### Variable Naming Convention

```basic
A#    ' Real number
A%    ' Integer
A&    ' Long integer
A$    ' String
A!    ' Boolean
A     ' Default (Real)
```

### Array Support

```basic
DIM A$(10)     ' String array
A$(5) = "Hello" ' Array assignment
B#(3,4) = 42   ' Multi-dimensional array
```

**Implementation**:
- Dynamic sizing (no fixed bounds)
- HashMap-based storage for flexibility
- Automatic memory allocation

### Memory Layout

```
Program State:
├── Variables (HashMap<String, Value>)
├── Arrays (HashMap<String, HashMap<String, Value>>)
├── Stack (Call stack for GOSUB/RETURN)
├── Line Number Cross-Reference
└── Current Execution State
```

## Type System

### Type Hierarchy

```
Value (Interface)
├── RealValue
├── IntegerValue
├── LongValue
├── StringValue
└── BooleanValue
```

### Type Operations

Each type implements the `Value` interface with type-specific behavior:

**RealValue**:
```java
public Value plus(Value oValue) {
    if (oValue instanceof RealValue) {
        return new RealValue(_fValue + oValue.toReal());
    }
    throw new SyntaxErrorException("Type mismatch");
}
```

**StringValue**:
```java
public Value plus(Value oValue) {
    return new StringValue(_strValue + oValue.toString());
}
```

### Type Conversion

**Automatic Conversions**:
- String concatenation with `+`
- Numeric operations promote to Real
- Boolean operations on non-zero values

**Explicit Conversions**:
```basic
A% = CINT(3.7)    ' Convert to integer
B# = CDBL(42)     ' Convert to real
C$ = STR(123)     ' Convert to string
```

## Parser Implementation

### Current Parser (Left-to-Right)

**Location**: `src/main/java/eu/gricom/basic/parser/BasicParser.java`

**Method**: `operator()`
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

**Limitation**: All operators have equal precedence, evaluated left-to-right.

### Enhanced Parser (Standard Precedence)

**Location**: `src/main/java/eu/gricom/basic/parser/BasicParser.java` (precedence mode with `_bDartmouthFlag = false`)

**Precedence Levels**:
1. `expression()` → `logicalOr()`
2. `logicalOr()` → `logicalAnd()`
3. `logicalAnd()` → `equality()`
4. `equality()` → `comparison()`
5. `comparison()` → `shift()`
6. `shift()` → `addition()`
7. `addition()` → `multiplication()`
8. `multiplication()` → `exponentiation()`
9. `exponentiation()` → `unary()`
10. `unary()` → `atomic()`

**Unary Operator Support**:
The `unary()` method uses the `UnaryOperatorExpression` class to handle unary operators (`+`, `-`, `!`):

```java
private Expression unary() throws SyntaxErrorException {
    if (getToken(0).getType() == BasicTokenType.PLUS ||
        getToken(0).getType() == BasicTokenType.MINUS ||
        getToken(0).getType() == BasicTokenType.NOT) {
        Token operator = getToken(0);
        _iPosition++;
        Expression operand = unary(); // Recursive call for nested unary operators
        return new UnaryOperatorExpression(operator.getType(), operand);
    }
    return atomic();
}
```

**UnaryOperatorExpression Features**:
- **Type Safety**: Validates operators at construction time
- **Nested Support**: Handles expressions like `--5` and `!!true`
- **Type-Aware Operations**: Different behavior for boolean vs numeric operands
- **Error Handling**: Comprehensive exception handling for invalid operands

### Expression Tree Construction

```java
// Input: 1 + 2 * 3
// Current: ((1 + 2) * 3) = 9
// Standard: (1 + (2 * 3)) = 7

Expression tree = new OperatorExpression(
    new RealValue(1),
    BasicTokenType.PLUS,
    new OperatorExpression(
        new RealValue(2),
        BasicTokenType.MULTIPLY,
        new RealValue(3)
    )
);
```

### Unary Operator Expression

**Location**: `src/main/java/eu/gricom/basic/statements/UnaryOperatorExpression.java`

**Purpose**: Handles unary operators that operate on a single operand

**Supported Operators**:
- **Unary Plus (+)**: Validates operand without changing value
- **Unary Minus (-)**: Negates numeric values
- **Unary NOT (!)**: Logical NOT for booleans, bitwise NOT for numbers

**Example Usage**:
```java
// Create unary minus expression
Expression operand = new VariableExpression("x");
UnaryOperatorExpression unaryMinus = new UnaryOperatorExpression(
    BasicTokenType.MINUS, operand);

// Evaluate the expression
Value result = unaryMinus.evaluate();
```

**Nested Unary Operators**:
```java
// Handles expressions like --5 (double negative)
Expression doubleNegative = new UnaryOperatorExpression(
    BasicTokenType.MINUS,
    new UnaryOperatorExpression(BasicTokenType.MINUS, new RealValue(5))
);
```

## Runtime Execution

### Statement Execution

**Statement Interface**:
```java
public interface Statement {
    int getTokenNumber();
    void execute() throws Exception;
    String content() throws Exception;
    String structure() throws Exception;
}
```

**Statement Types**:
- `AssignStatement` - Variable assignment
- `PrintStatement` - Output operations
- `IfThenStatement` - Conditional execution
- `ForStatement` - Loop control
- `GotoStatement` - Flow control
- `Function` - Built-in function calls

### Flow Control

**GOTO Implementation**:
```java
public void execute() throws Exception {
    int targetLine = getTargetLineNumber();
    int targetStatement = getStatementFromLine(targetLine);
    getProgramPointer().setCurrentStatement(targetStatement);
}
```

**Loop Implementation**:
```java
// FOR loop
for (int i = start; i <= end; i += step) {
    // Execute loop body
    if (shouldBreak) break;
}
```

### Function Calls

**Built-in Functions**:
```java
public class Function implements Expression {
    public Value evaluate() throws Exception {
        switch (_oToken.getType()) {
            case ABS:
                return Abs.execute(_oFirstParam.evaluate());
            case SIN:
                return Sin.execute(_oFirstParam.evaluate());
            // ... other functions
        }
    }
}
```

## Error Handling

### Exception Hierarchy

```
RuntimeException
├── SyntaxErrorException
├── DivideByZeroException
├── OutOfDataException
├── UndefinedUserFunctionException
└── EmptyStackException
```

### Error Recovery

**Parser Errors**:
- Detailed error messages with line numbers
- Token position information
- Expected vs. found token reporting

**Runtime Errors**:
- Graceful error handling
- Error message display
- Program termination on fatal errors

### Error Reporting

```java
throw new SyntaxErrorException(
    "Expected " + expectedType + 
    " but found " + actualType + 
    " at line " + lineNumber
);
```

## Extension Points

### Adding New Functions

1. **Create Function Class**:
```java
public class NewFunction {
    public static Value execute(Value parameter) throws Exception {
        // Implementation
        return result;
    }
}
```

2. **Add to Token Types**:
```java
public enum BasicTokenType {
    // ... existing types
    NEW_FUNCTION,
}
```

3. **Update Parser**:
```java
case NEW_FUNCTION:
    return new Function(token, parameter);
```

4. **Add Tests**:
```java
@Test
public void testNewFunction() {
    // Test implementation
}
```

### Adding New Statement Types

1. **Create Statement Class**:
```java
public class NewStatement implements Statement {
    public void execute() throws Exception {
        // Implementation
    }
}
```

2. **Update Parser**:
```java
case NEW_KEYWORD:
    return new NewStatement(parameters);
```

3. **Update Tokenizer**:
```java
// Add keyword recognition
```

### Adding New Variable Types

1. **Implement Value Interface**:
```java
public class NewTypeValue implements Value {
    // Implement all interface methods
}
```

2. **Update Variable Management**:
```java
// Add type detection logic
```

## Development Guidelines

### Code Style

**Naming Conventions**:
- Classes: `PascalCase` (e.g., `BasicParser`)
- Methods: `camelCase` (e.g., `parseExpression`)
- Variables: `camelCase` with Hungarian notation (e.g., `oProgram`, `iCount`)
- Constants: `UPPER_SNAKE_CASE` (e.g., `MAX_ARRAY_SIZE`)

**File Organization**:
```
src/main/java/eu/gricom/basic/
├── Basic.java                    # Main entry point
├── tokenizer/                    # Lexical analysis
├── parser/                       # Syntax analysis
├── statements/                   # Statement implementations
├── functions/                    # Built-in functions
├── variableTypes/               # Type system
├── memoryManager/               # Memory management
├── runtimeManager/              # Execution engine
├── error/                       # Exception classes
└── helper/                      # Utility classes
```

### Testing Strategy

**Unit Tests**:
- Each class should have corresponding test class
- Test all public methods
- Include edge cases and error conditions

**Integration Tests**:
- Test complete BASIC programs
- Verify end-to-end functionality
- Test error handling scenarios

**Test Structure**:
```java
@Test
public void testMethodName_Scenario_ExpectedResult() {
    // Arrange
    // Act
    // Assert
}
```

### Performance Considerations

**Memory Management**:
- Reuse objects where possible
- Avoid unnecessary object creation
- Use appropriate data structures

**Parsing Performance**:
- Single-pass tokenization
- Efficient expression tree construction
- Minimal object allocation during parsing

**Runtime Performance**:
- Cached variable lookups
- Efficient type checking
- Optimized mathematical operations

## Testing Strategy

### Test Categories

1. **Unit Tests** (`src/test/java/`)
   - Individual component testing
   - Method-level validation
   - Edge case coverage

2. **Integration Tests** (`src/test/basic/`)
   - Complete program execution
   - End-to-end functionality
   - Error scenario testing

3. **Regression Tests** (`test/regression/`)
   - Known working programs
   - Performance benchmarks
   - Compatibility verification

### Test Execution

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BasicParserTest

# Run with coverage
mvn test jacoco:report
```

### Test Data

**Sample Programs** (`src/test/resources/`):
- `ECMA_Basic_Examples/` - Standard BASIC programs
- `test_basic_*.bas` - Unit test programs
- `regression/` - Regression test programs

## Performance Considerations

### Memory Usage

**Variable Storage**:
- HashMap-based for O(1) access
- Dynamic allocation for arrays
- Automatic cleanup on program end

**Expression Trees**:
- Immutable expression objects
- Shared sub-expressions where possible
- Minimal object creation during parsing

### Execution Speed

**Optimizations**:
- Cached line number lookups
- Efficient type checking
- Optimized mathematical operations
- Minimal reflection usage

**Bottlenecks**:
- String operations in loops
- Large array operations
- Complex nested expressions

### Scalability

**Large Programs**:
- Line-by-line processing
- Streaming tokenization
- Memory-efficient data structures

**Memory Limits**:
- Configurable maximum array size
- Stack depth limits
- Variable count limits

## Troubleshooting

### Common Issues

**Parser Errors**:
```
SyntaxErrorException: Expected PLUS but found MULTIPLY at line 10
```
**Solution**: Check operator precedence and parentheses usage.

**Runtime Errors**:
```
DivideByZeroException: Division by zero at line 15
```
**Solution**: Add input validation and error checking.

**Memory Issues**:
```
OutOfMemoryError: Java heap space
```
**Solution**: Increase JVM heap size or optimize memory usage.

### Debugging Tools

**Logging**:
```java
Logger _oLogger = new Logger(this.getClass().getName());
_oLogger.debug("Debug message");
_oLogger.info("Info message");
_oLogger.error("Error message");
```

**Tracing**:
```java
// Enable statement tracing
@PRAGMA TRACE = ON
```

**Variable Inspection**:
```basic
PRINT "Variable A = "; A#
PRINT "Array B(5) = "; B(5)
```

### Performance Profiling

**JVM Options**:
```bash
java -Xmx512m -XX:+UseG1GC -jar gd-basic.jar program.bas
```

**Profiling Tools**:
- JProfiler for memory analysis
- VisualVM for performance monitoring
- JConsole for runtime monitoring

### Getting Help

**Documentation**:
- This technical documentation
- API documentation (`mvn javadoc:javadoc`)
- Code comments and examples

**Testing**:
- Run test suite to verify functionality
- Use sample programs as reference
- Check regression tests for known issues

**Community**:
- GitHub issues and discussions
- Code review and pull requests
- Developer documentation updates

---

This documentation provides a comprehensive overview of the GD-BASIC interpreter architecture and implementation. For specific implementation details, refer to the source code and individual component documentation. 