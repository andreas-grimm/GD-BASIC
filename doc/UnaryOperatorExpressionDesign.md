# UnaryOperatorExpression Class Design

## Overview

The `UnaryOperatorExpression` class is designed to handle unary operators in the GD-BASIC interpreter. Unary operators operate on a single operand, unlike binary operators which operate on two operands.

## Class Structure

### **Package Location**
```
src/main/java/eu/gricom/basic/statements/UnaryOperatorExpression.java
```

### **Class Signature**
```java
public class UnaryOperatorExpression implements Expression
```

## Supported Unary Operators

### **1. Unary Plus (+)**
- **Purpose**: Validates the operand without changing its value
- **Behavior**: Returns the operand unchanged
- **Example**: `+5` evaluates to `5`
- **Use Cases**: Explicit positive number declaration, validation

### **2. Unary Minus (-)**
- **Purpose**: Negates the operand value
- **Behavior**: Changes the sign of numeric values
- **Example**: `-5` evaluates to `-5`
- **Use Cases**: Negative numbers, sign inversion

### **3. Unary NOT (!)**
- **Purpose**: Logical or bitwise NOT operation
- **Behavior**: 
  - For boolean values: inverts the boolean (true → false, false → true)
  - For numeric values: performs bitwise NOT operation
- **Example**: `!true` evaluates to `false`, `!5` performs bitwise NOT on 5
- **Use Cases**: Boolean logic, bitwise operations

## Constructor

```java
public UnaryOperatorExpression(final BasicTokenType oOperator, final Expression oOperand)
```

**Parameters:**
- `oOperator`: The unary operator (PLUS, MINUS, or NOT)
- `oOperand`: The expression to apply the operator to

**Validation:**
- Throws `IllegalArgumentException` if the operator is not a valid unary operator
- Only accepts `BasicTokenType.PLUS`, `BasicTokenType.MINUS`, or `BasicTokenType.NOT`

## Core Methods

### **1. evaluate()**
```java
@Override
public final Value evaluate() throws Exception
```

**Purpose**: Evaluates the unary expression and returns the result
**Process**:
1. Evaluates the operand expression
2. Applies the unary operator based on the operator type
3. Returns the result as a `Value` object

### **2. evaluateUnaryPlus()**
```java
private Value evaluateUnaryPlus(Value operand) throws SyntaxErrorException
```

**Purpose**: Handles unary plus operator
**Behavior**:
- Validates that operand is not null
- Returns operand unchanged
- Throws `SyntaxErrorException` for null operands

### **3. evaluateUnaryMinus()**
```java
private Value evaluateUnaryMinus(Value operand) throws SyntaxErrorException
```

**Purpose**: Handles unary minus operator
**Behavior**:
- Converts operand to real number using `toReal()`
- Negates the value
- Returns new `RealValue` with negated value
- Throws `SyntaxErrorException` for non-numeric operands

### **4. evaluateUnaryNot()**
```java
private Value evaluateUnaryNot(Value operand) throws SyntaxErrorException
```

**Purpose**: Handles unary NOT operator
**Behavior**:
- For `BooleanValue`: inverts the boolean value
- For numeric values: performs bitwise NOT operation
- Returns appropriate value type (IntegerValue or RealValue)
- Throws `SyntaxErrorException` for non-numeric operands

## Integration with Parser

### **Updated unary() Method**
The `unary()` method in `BasicParser` (precedence mode) uses this class:

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

### **Precedence Integration**
The `UnaryOperatorExpression` fits into the operator precedence hierarchy:

```
1. Parentheses and function calls (highest)
2. Unary operators (+, -, !) ← UnaryOperatorExpression handles this
3. Exponentiation (^)
4. Multiplication/Division (*, /, %)
5. Addition/Subtraction (+, -)
6. Comparison operators (<, <=, >, >=, ==, !=)
7. Logical operators (&&, ||) (lowest)
```

## Error Handling

### **Exception Types**
1. **IllegalArgumentException**: Invalid unary operator in constructor
2. **SyntaxErrorException**: 
   - Null operand values
   - Non-numeric operands for unary minus
   - Non-numeric operands for unary NOT
   - Unknown unary operators

### **Error Messages**
- `"Invalid unary operator: {operator}"`
- `"Cannot apply unary plus to null value"`
- `"Cannot apply unary minus to null value"`
- `"Cannot apply unary minus to non-numeric value: {operand}"`
- `"Cannot apply unary NOT to null value"`
- `"Cannot apply unary NOT to non-numeric value: {operand}"`

## Testing Considerations

### **Test Cases to Implement**

#### **Unary Plus Tests**
```java
@Test
void testUnaryPlus() throws Exception {
    // Test with positive numbers
    // Test with negative numbers
    // Test with zero
    // Test with null (should throw exception)
}
```

#### **Unary Minus Tests**
```java
@Test
void testUnaryMinus() throws Exception {
    // Test positive to negative: +5 → -5
    // Test negative to positive: -5 → +5
    // Test zero: 0 → 0
    // Test with non-numeric values (should throw exception)
}
```

#### **Unary NOT Tests**
```java
@Test
void testUnaryNot() throws Exception {
    // Test boolean values: true → false, false → true
    // Test numeric values: bitwise NOT operations
    // Test with non-numeric values (should throw exception)
}
```

#### **Integration Tests**
```java
@Test
void testNestedUnaryOperators() throws Exception {
    // Test: --5 (double negative)
    // Test: !!true (double NOT)
    // Test: -!5 (negative of NOT 5)
}
```

## Performance Considerations

### **Optimizations**
1. **Caching**: Consider caching common unary operations
2. **Type Checking**: Efficient type checking for different operand types
3. **Memory Usage**: Minimal object creation for simple operations

### **Memory Management**
- Uses existing `Value` objects where possible
- Creates new `Value` objects only when necessary
- Follows the same pattern as `OperatorExpression`

## Compatibility

### **BASIC Language Standards**
- **Dartmouth BASIC**: Supports unary minus, limited unary plus
- **ECMA-55**: Defines unary operators
- **Modern BASIC**: Extended support for unary NOT

### **Backward Compatibility**
- Maintains compatibility with existing `Expression` interface
- Follows same patterns as `OperatorExpression`
- No breaking changes to existing code

## Usage Examples

### **BASIC Code Examples**
```basic
10 A# = -5        ' Unary minus
20 B# = +10       ' Unary plus
30 C! = !TRUE     ' Unary NOT
40 D% = !5        ' Bitwise NOT
50 E# = --5       ' Double negative
```

### **Java Usage**
```java
// Create unary minus expression
Expression operand = new VariableExpression("x");
UnaryOperatorExpression unaryMinus = new UnaryOperatorExpression(
    BasicTokenType.MINUS, operand);

// Evaluate the expression
Value result = unaryMinus.evaluate();
```

## Future Enhancements

### **Potential Extensions**
1. **Additional Unary Operators**: 
   - Bitwise complement (`~`)
   - Increment/decrement (`++`, `--`)
2. **Type-Specific Operations**: 
   - String operations
   - Array operations
3. **Optimization**: 
   - Constant folding for compile-time evaluation
   - Expression simplification

### **Integration with Code Generator**
- Support for generating optimized bytecode
- Integration with JIT compilation
- Support for different target platforms

## Conclusion

The `UnaryOperatorExpression` class provides a clean, extensible way to handle unary operators in the GD-BASIC interpreter. It follows established patterns, provides comprehensive error handling, and integrates seamlessly with the existing expression evaluation system.

**Key Benefits:**
- **Type Safety**: Validates operators at construction time
- **Error Handling**: Comprehensive exception handling
- **Extensibility**: Easy to add new unary operators
- **Performance**: Efficient evaluation with minimal overhead
- **Maintainability**: Clear separation of concerns and well-documented code 