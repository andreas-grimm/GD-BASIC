# Standard Mathematical Operator Precedence Implementation

## Overview

This document explains how to modify the GD-BASIC interpreter to follow standard mathematical operator precedence rules instead of the current left-to-right evaluation.

## Current Implementation Problem

The current GD-BASIC parser uses a single `operator()` method that treats all operators equally and evaluates expressions from left to right. This leads to incorrect mathematical results.

**Example:**
```basic
A# = 1 + 2 * 3 - 4 / 5
```

**Current Result:** `1` (incorrect)
- `1 + 2 = 3`
- `3 * 3 = 9` 
- `9 - 4 = 5`
- `5 / 5 = 1`

**Expected Result:** `6.2` (correct)
- `2 * 3 = 6` (multiplication first)
- `4 / 5 = 0.8` (division first)
- `1 + 6 = 7` (addition)
- `7 - 0.8 = 6.2` (subtraction)

## Solution: Precedence-Aware Parser

### Standard Operator Precedence (highest to lowest)

1. **Parentheses and function calls** - `()`, `ABS()`, `SIN()`, etc.
2. **Unary operators** - `+`, `-`, `NOT`
3. **Exponentiation** - `^` (right-associative)
4. **Multiplication, Division, Modulo** - `*`, `/`, `%`
5. **Addition, Subtraction** - `+`, `-`
6. **Bitwise shifts** - `<<`, `>>`
7. **Comparison operators** - `<`, `<=`, `>`, `>=`, `==`, `!=`
8. **Logical operators** - `AND`, `OR`

### Implementation Strategy

Replace the single `operator()` method with multiple methods, each handling a specific precedence level:

```java
private Expression expression() throws SyntaxErrorException {
    return logicalOr();  // Start with lowest precedence
}

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

private Expression logicalAnd() throws SyntaxErrorException {
    Expression left = equality();
    
    while (getToken(0).getType() == BasicTokenType.AND) {
        Token operator = getToken(0);
        _iPosition++;
        Expression right = equality();
        left = new OperatorExpression(left, operator.getType(), right);
    }
    
    return left;
}

// Continue with equality(), comparison(), shift(), addition(), 
// multiplication(), exponentiation(), unary(), atomic()
```

### Key Implementation Details

#### 1. Method Hierarchy
Each precedence level method calls the next higher precedence method:

```
expression() → logicalOr() → logicalAnd() → equality() → comparison() 
→ shift() → addition() → multiplication() → exponentiation() → unary() → atomic()
```

#### 2. Right-Associative Operators
Exponentiation (`^`) is right-associative, so it calls itself recursively:

```java
private Expression exponentiation() throws SyntaxErrorException {
    Expression left = unary();
    
    if (getToken(0).getType() == BasicTokenType.POWER) {
        Token operator = getToken(0);
        _iPosition++;
        // Right-associative: call exponentiation() recursively
        Expression right = exponentiation();
        left = new OperatorExpression(left, operator.getType(), right);
    }
    
    return left;
}
```

#### 3. Unary Operators
Unary operators (`+`, `-`, `NOT`) are handled in the `unary()` method using the `UnaryOperatorExpression` class:

```java
private Expression unary() throws SyntaxErrorException {
    if (getToken(0).getType() == BasicTokenType.PLUS ||
        getToken(0).getType() == BasicTokenType.MINUS ||
        getToken(0).getType() == BasicTokenType.NOT) {
        Token operator = getToken(0);
        _iPosition++;
        Expression operand = unary();  // Recursive call for nested unary operators
        return new UnaryOperatorExpression(operator.getType(), operand);
    }
    
    return atomic();
}
```

The `UnaryOperatorExpression` class provides:
- **Unary Plus (+)**: Validates operand without changing value
- **Unary Minus (-)**: Negates numeric values
- **Unary NOT (!)**: Logical NOT for booleans, bitwise NOT for numbers
- **Nested Support**: Handles expressions like `--5` (double negative) and `!!true` (double NOT)
- **Error Handling**: Comprehensive exception handling for invalid operands

## Required Changes

### 1. Enhanced Parser Implementation
The precedence functionality has been integrated into `BasicParser.java` with a `_bDartmouthFlag` parameter to control evaluation mode.

### 2. Unary Operator Support
The `UnaryOperatorExpression` class has been implemented to handle unary operators:

```java
public class UnaryOperatorExpression implements Expression {
    private final BasicTokenType _oOperator;
    private final Expression _oOperand;
    
    public UnaryOperatorExpression(final BasicTokenType oOperator, final Expression oOperand) {
        _oOperator = oOperator;
        _oOperand = oOperand;
        
        // Validate that the operator is a valid unary operator
        if (oOperator != BasicTokenType.PLUS && 
            oOperator != BasicTokenType.MINUS && 
            oOperator != BasicTokenType.NOT) {
            throw new IllegalArgumentException("Invalid unary operator: " + oOperator);
        }
    }
    
    @Override
    public final Value evaluate() throws Exception {
        Value operandValue = _oOperand.evaluate();
        
        switch (_oOperator) {
            case PLUS:
                return evaluateUnaryPlus(operandValue);
            case MINUS:
                return evaluateUnaryMinus(operandValue);
            case NOT:
                return evaluateUnaryNot(operandValue);
            default:
                throw new SyntaxErrorException("Unknown unary operator: " + _oOperator);
        }
    }
    
    private Value evaluateUnaryPlus(Value operand) throws SyntaxErrorException {
        if (operand == null) {
            throw new SyntaxErrorException("Cannot apply unary plus to null value");
        }
        return operand;
    }
    
    private Value evaluateUnaryMinus(Value operand) throws SyntaxErrorException {
        if (operand == null) {
            throw new SyntaxErrorException("Cannot apply unary minus to null value");
        }
        double numericValue = operand.toReal();
        return new RealValue(-numericValue);
    }
    
    private Value evaluateUnaryNot(Value operand) throws SyntaxErrorException {
        if (operand == null) {
            throw new SyntaxErrorException("Cannot apply unary NOT to null value");
        }
        
        if (operand instanceof BooleanValue) {
            boolean boolValue = operand.toReal() != 0.0;
            return new BooleanValue(!boolValue);
        }
        
        double numericValue = operand.toReal();
        long longValue = (long) numericValue;
        long notValue = ~longValue;
        
        if (notValue >= Integer.MIN_VALUE && notValue <= Integer.MAX_VALUE) {
            return new IntegerValue((int) notValue);
        } else {
            return new RealValue((double) notValue);
        }
    }
}
```

**Key Features:**
- **Type Safety**: Validates operators at construction time
- **Comprehensive Error Handling**: Specific exceptions for different error conditions
- **Type-Aware Operations**: Different behavior for boolean vs numeric operands
- **Memory Efficiency**: Returns appropriate value types (IntegerValue or RealValue)
- **Nested Support**: Handles complex expressions like `--5` and `!!true`

### 3. Value Interface Integration
The `UnaryOperatorExpression` class integrates with the existing `Value` interface without requiring changes to the interface itself. It uses the existing `toReal()` method and type checking to perform appropriate operations:

- **Boolean Values**: Uses `instanceof BooleanValue` to detect boolean operands
- **Numeric Values**: Uses `toReal()` method to convert operands to numeric values
- **Type Conversion**: Automatically converts between `IntegerValue` and `RealValue` as needed

### 4. Testing
Create comprehensive tests to verify precedence behavior and unary operator functionality:

```java
@Test
public void testOperatorPrecedence() {
    // Test: 1 + 2 * 3 should equal 7, not 9
    Expression expr = parseExpression("1 + 2 * 3");
    Value result = expr.evaluate();
    assertEquals(7.0, result.toReal(), 0.001);
}

@Test
public void testExponentiation() {
    // Test: 2 ^ 3 + 1 should equal 9, not 16
    Expression expr = parseExpression("2 ^ 3 + 1");
    Value result = expr.evaluate();
    assertEquals(9.0, result.toReal(), 0.001);
}

@Test
public void testUnaryOperators() {
    // Test unary minus
    Expression expr1 = parseExpression("-5");
    Value result1 = expr1.evaluate();
    assertEquals(-5.0, result1.toReal(), 0.001);
    
    // Test unary plus
    Expression expr2 = parseExpression("+10");
    Value result2 = expr2.evaluate();
    assertEquals(10.0, result2.toReal(), 0.001);
    
    // Test unary NOT
    Expression expr3 = parseExpression("!true");
    Value result3 = expr3.evaluate();
    assertEquals(false, result3.toReal() != 0.0);
}

@Test
public void testNestedUnaryOperators() {
    // Test double negative
    Expression expr1 = parseExpression("--5");
    Value result1 = expr1.evaluate();
    assertEquals(5.0, result1.toReal(), 0.001);
    
    // Test double NOT
    Expression expr2 = parseExpression("!!true");
    Value result2 = expr2.evaluate();
    assertEquals(true, result2.toReal() != 0.0);
}
```

## Migration Strategy

### Phase 1: Implementation Complete ✅
1. ✅ Precedence functionality integrated into `BasicParser`
2. ✅ Comprehensive tests implemented
3. ✅ All existing functionality preserved

### Phase 2: Configuration Available ✅
1. ✅ `_bDartmouthFlag` parameter controls evaluation mode
2. ✅ Backward compatibility maintained
3. ✅ Documentation updated

### Phase 3: Usage Guidelines
1. Use `_bDartmouthFlag = true` for Dartmouth BASIC compatibility
2. Use `_bDartmouthFlag = false` for standard mathematical precedence
3. Test existing programs with both modes to identify differences

## Benefits

1. **Mathematical Correctness**: Expressions evaluate according to standard mathematical rules
2. **Intuitive Behavior**: Programmers get expected results without explicit parentheses
3. **Compatibility**: Matches behavior of most modern programming languages
4. **Maintainability**: Clear precedence hierarchy makes code easier to understand

## Drawbacks

1. **Breaking Changes**: Existing programs may produce different results
2. **Complexity**: More complex parser implementation
3. **Performance**: Slightly more overhead due to multiple method calls

## Recommendations

1. **Use Parentheses**: Always use parentheses for complex expressions to make intent clear
2. **Documentation**: Clearly document the precedence rules in the BASIC language guide
3. **Testing**: Comprehensive testing of edge cases and complex expressions
4. **Migration**: Provide tools to help users identify expressions that may behave differently

## Example Usage

```basic
10 REM Example with proper precedence
20 A# = 1 + 2 * 3 - 4 / 5  ' Result: 6.2
30 B# = 2 ^ 3 + 1          ' Result: 9.0
40 C# = 10 - 2 * 3 + 4 / 2 ' Result: 6.0
50 PRINT "A = "; A#
60 PRINT "B = "; B#
70 PRINT "C = "; C#
80 END
```

This implementation ensures that GD-BASIC follows standard mathematical conventions while maintaining compatibility with the existing language features. 