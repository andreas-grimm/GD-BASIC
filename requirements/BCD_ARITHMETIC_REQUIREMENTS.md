# BCD (Binary Coded Decimal) Arithmetic Implementation Requirements

**Version**: 1.0  
**Date**: 2026-05-14  
**Status**: Requirements Specification

---

## Executive Summary

This document specifies the implementation of Binary Coded Decimal (BCD) arithmetic support for the GD-BASIC interpreter. BCD arithmetic provides precise decimal calculations without floating-point rounding errors, making it suitable for financial and accounting applications. The implementation will be triggered via command-line parameters or pragma directives.

---

## 1. Background & Motivation

### Current State
- GD-BASIC supports multiple numeric types: `RealValue` (double), `IntegerValue` (32-bit), `LongValue` (64-bit)
- All arithmetic operations use standard binary representations with inherent rounding errors
- No support for arbitrary-precision decimal arithmetic
- Financial and accounting use cases suffer from floating-point precision issues

### BCD Advantages
- Exact decimal representation (no binary rounding)
- Preserves significant digits in financial calculations
- Industry standard for banking and accounting
- Eliminates $0.01 rounding errors in monetary calculations

### Scope
- Add BCD as optional arithmetic mode, not replacement for existing types
- Implement via configuration (CLI flag or pragma)
- Apply to all mathematical operations when enabled
- Maintain backward compatibility

---

## 2. Activation Mechanisms

### 2.1 Command-Line Parameter

Add flag to `Basic.java`:
```bash
java -jar BASIC-*.jar --bcd program.bas
java -jar BASIC-*.jar -B program.bas              # short form
```

**Implementation Location**: `eu.gricom.basic.Basic`

```java
private static boolean _bBCDMode = false;

public static void main(String[] args) {
    // In argument parsing:
    case "-B", "--bcd" -> {
        _bBCDMode = true;
        Program.setBCDMode(true);
    }
}
```

### 2.2 Pragma Directive

Support in BASIC source code:
```basic
#PRAGMA BCD_ENABLE
10 A = 10.5 + 0.3
20 PRINT A                ! Result: 10.8 (exact, no rounding)
#PRAGMA BCD_DISABLE
30 B = 10.5 + 0.3         ! Back to floating-point
40 PRINT B
```

**Implementation Location**: `eu.gricom.basic.parser.BasicParser` and `eu.gricom.basic.macroManager.MacroProcessor`

---

## 3. Architecture

### 3.1 New BCDValue Class

**File**: `src/main/java/eu/gricom/basic/variableTypes/BCDValue.java`

```java
public class BCDValue implements Value {
    private BigDecimal _bdValue;
    private static final int SCALE = 10;  // 10 decimal places precision
    
    // Constructors
    public BCDValue(String strValue)
    public BCDValue(double dValue)
    public BCDValue(BigDecimal bdValue)
    
    // Value interface implementation
    public Value plus(Value other)
    public Value minus(Value other)
    public Value times(Value other)
    public Value dividedBy(Value other)
    public Value power(Value other)
    public Value modulo(Value other)
    
    // Comparison
    public BooleanValue lessThan(Value other)
    public BooleanValue greaterThan(Value other)
    public BooleanValue equal(Value other)
    
    // Conversion
    public StringValue toStringValue()
    public double toDouble()
    public BigDecimal getValue()
    
    // Bit operations (unsupported in BCD)
    public Value shiftLeft(Value other) throws ArithmeticException
    public Value shiftRight(Value other) throws ArithmeticException
}
```

**Key Properties**:
- Uses `java.math.BigDecimal` for arbitrary-precision arithmetic
- Default scale: 10 decimal places (configurable)
- Throws `ArithmeticException` for unsupported operations (bit shifts)
- Automatic type coercion from/to other Value types

### 3.2 Type Coercion Rules

When BCD mode is enabled:
- `RealValue` → `BCDValue` (loss of precision, but acceptable)
- `IntegerValue` → `BCDValue` (exact)
- `LongValue` → `BCDValue` (exact)
- `BCDValue` + `RealValue` → `BCDValue` (preserves BCD result)
- `BCDValue` + `IntegerValue` → `BCDValue` (exact)

**Implementation Location**: `eu.gricom.basic.variableTypes.Value` interface

```java
// New method in Value interface
default Value toValue(boolean bBCDMode) {
    if (bBCDMode && !(this instanceof BCDValue)) {
        return new BCDValue(this.toDouble());
    }
    return this;
}
```

### 3.3 Operator Expression Modifications

**File**: `src/main/java/eu/gricom/basic/statements/OperatorExpression.java`

When BCD mode is active:
1. At parse time, detect BCD mode from `Program.isBCDMode()`
2. In `evaluate()`, convert operands to `BCDValue` if needed
3. Perform operation using `BCDValue` methods
4. Return `BCDValue` result

```java
public Value evaluate(VariableManagement oVariableManagement) {
    Value oLeft = _oLeft.evaluate(oVariableManagement);
    Value oRight = _oRight.evaluate(oVariableManagement);
    
    if (Program.isBCDMode()) {
        if (!(oLeft instanceof BCDValue)) {
            oLeft = new BCDValue(oLeft.toDouble());
        }
        if (!(oRight instanceof BCDValue)) {
            oRight = new BCDValue(oRight.toDouble());
        }
    }
    
    return performOperation(oLeft, oRight);
}
```

---

## 4. Detailed Code Changes

### 4.1 New Files to Create

| File | Purpose | Lines |
|------|---------|-------|
| `src/main/java/eu/gricom/basic/variableTypes/BCDValue.java` | BCD value type | 250-300 |
| `src/test/java/eu/gricom/basic/variableTypes/BCDValueTest.java` | Unit tests for BCD | 150-200 |
| `src/test/basic/test_bcd_arithmetic.bas` | System test: BCD operations | 30-50 |
| `src/test/basic/test_bcd_precision.bas` | System test: Precision validation | 20-30 |

### 4.2 Files to Modify

| File | Changes | Impact |
|------|---------|--------|
| `src/main/java/eu/gricom/basic/Basic.java` | Add `-B/--bcd` parameter parsing | Low |
| `src/main/java/eu/gricom/basic/memoryManager/Program.java` | Add `_bBCDMode` flag, setter/getter | Low |
| `src/main/java/eu/gricom/basic/statements/OperatorExpression.java` | Add BCD mode conversion logic in `evaluate()` | Medium |
| `src/main/java/eu/gricom/basic/functions/Function.java` | Add BCD support to mathematical functions | Medium |
| `src/main/java/eu/gricom/basic/variableTypes/Value.java` | Add `isBCD()` interface method | Low |
| `src/main/java/eu/gricom/basic/parser/BasicParser.java` | Parse `#PRAGMA BCD_ENABLE/BCD_DISABLE` | Medium |
| `src/main/java/eu/gricom/basic/macroManager/MacroProcessor.java` | Handle BCD pragma directives | Low |
| `src/test/java/eu/gricom/basic/statements/OperatorExpressionTest.java` | Add BCD test cases | Low |

### 4.3 Mathematical Functions with BCD Support

All functions in `eu.gricom.basic.functions` must support BCD:

| Function | Implementation | Status |
|----------|-----------------|--------|
| `Abs.java` | Use `BigDecimal.abs()` | Straightforward |
| `Sin.java` | Convert to double, compute, convert back | Acceptable loss |
| `Cos.java` | Convert to double, compute, convert back | Acceptable loss |
| `Tan.java` | Convert to double, compute, convert back | Acceptable loss |
| `Log.java` | Convert to double, compute, convert back | Acceptable loss |
| `Log10.java` | Convert to double, compute, convert back | Acceptable loss |
| `Exp.java` | Convert to double, compute, convert back | Acceptable loss |
| `Sqr.java` | Use `BigDecimal.sqrt()` (Java 9+) or custom | Requires care |
| `Atn.java` | Convert to double, compute, convert back | Acceptable loss |

**Pattern**:
```java
public Value evaluate(List<Value> aoParams, VariableManagement oVariableManagement) {
    if (Program.isBCDMode() && aoParams.get(0) instanceof BCDValue) {
        BCDValue oBCD = (BCDValue) aoParams.get(0);
        // Operate on BCD, return BCD
        return new BCDValue(computeResult(oBCD.getValue()));
    }
    // Standard floating-point path
    return standardEvaluate(aoParams);
}
```

---

## 5. Variable Type Suffix Convention

Extend variable suffix convention:

| Suffix | Type | Range/Precision |
|--------|------|-----------------|
| `$` | String | Text |
| `%` | Integer | -2^31 to 2^31-1 |
| `&` | Long | -2^63 to 2^63-1 |
| `#` | Real (Double) | IEEE 754 (±1.7e308) |
| `!` | Boolean | true/false |
| `~` | **BCD (NEW)** | Arbitrary decimal precision (10 places) |

**Example**:
```basic
#PRAGMA BCD_ENABLE
10 AMOUNT~ = 1234.56
20 RATE~ = 0.035
30 INTEREST~ = AMOUNT~ * RATE~
40 PRINT INTEREST~                ! 43.21 (exact)
```

**Note**: Suffix is optional when BCD mode is enabled (all numeric operations default to BCD).

---

## 6. Configuration Options

### 6.1 Global BCD Scale

Add configuration parameter for decimal precision:

```java
// In Program class
private static int _iBCDScale = 10;  // Default 10 decimal places

public static void setBCDScale(int iScale) {
    if (iScale < 2 || iScale > 30) {
        throw new IllegalArgumentException("BCD scale must be 2-30");
    }
    _iBCDScale = iScale;
}
```

### 6.2 Command-Line Override

```bash
java -jar BASIC-*.jar --bcd --bcd-scale 15 program.bas
```

### 6.3 Pragma Configuration

```basic
#PRAGMA BCD_SCALE 15
#PRAGMA BCD_ROUNDING HALF_UP
```

---

## 7. Rounding Behavior

### 7.1 Rounding Modes

Support standard `BigDecimal` rounding modes:

| Mode | Behavior | Use Case |
|------|----------|----------|
| `HALF_UP` | Round 0.5 up (banker's rounding) | **Default for BCD** |
| `DOWN` | Always round toward zero | Truncation |
| `UP` | Always round away from zero | Conservative |
| `CEILING` | Round toward positive infinity | Financial minimum |
| `FLOOR` | Round toward negative infinity | Financial maximum |

### 7.2 Configuration

```basic
#PRAGMA BCD_ROUNDING HALF_UP      ! Default
#PRAGMA BCD_ROUNDING DOWN
#PRAGMA BCD_ROUNDING UP
```

---

## 8. Error Handling

### 8.1 Division by Zero

```java
// In BCDValue.dividedBy()
if (other.toBigDecimal().equals(BigDecimal.ZERO)) {
    throw new ArithmeticException("Division by zero (BCD)");
}
```

### 8.2 Unsupported Operations

```java
// Bit shift operations not supported in BCD
public Value shiftLeft(Value other) {
    throw new ArithmeticException("Bit shift not supported for BCD values");
}
```

### 8.3 Overflow/Underflow

BCD uses arbitrary precision, so overflow is not possible (until memory exhaustion).

---

## 9. Testing Strategy

### 9.1 Unit Tests (BCDValueTest.java)

```java
@Test
void testPrecision() {
    BCDValue a = new BCDValue("0.1");
    BCDValue b = new BCDValue("0.2");
    BCDValue c = a.plus(b);
    assertEquals("0.3000000000", c.toString());  // Exact
}

@Test
void testFinancialCalculation() {
    BCDValue principal = new BCDValue("1000.00");
    BCDValue rate = new BCDValue("0.05");
    BCDValue interest = principal.times(rate);
    assertEquals("50.0000000000", interest.toString());
}

@Test
void testTypeCoercion() {
    BCDValue bcd = new BCDValue("10.5");
    RealValue real = new RealValue(5.3);
    Value result = bcd.plus(real);
    assertTrue(result instanceof BCDValue);
}
```

### 9.2 System Tests

**test_bcd_arithmetic.bas**:
```basic
#PRAGMA BCD_ENABLE
10 A~ = 0.1
20 B~ = 0.2
30 C~ = A~ + B~
40 IF C~ = 0.3 THEN PRINT "PASSED" ELSE PRINT "FAILED"
50 END
```

**test_bcd_precision.bas**:
```basic
#PRAGMA BCD_ENABLE
10 AMOUNT~ = 19.99
20 TAX~ = AMOUNT~ * 0.0725
30 TOTAL~ = AMOUNT~ + TAX~
40 PRINT TOTAL~                    ! Should be 21.4492... (exact)
50 END
```

### 9.3 Performance Benchmarks

- Compare BCDValue arithmetic performance vs RealValue
- Document overhead of BigDecimal operations
- Establish performance targets (e.g., < 5x slower than double)

---

## 10. Documentation Updates

### 10.1 Files to Update

| File | Updates |
|------|----------|
| `BASIC_CODING_STANDARD.md` | Document BCD variable suffix, pragma directives |
| `README.md` | Mention BCD arithmetic support |
| `doc/GD-BASIC_Detailed_Design.md` | Add BCDValue to architecture, update Value type section |

### 10.2 User Documentation

Create `doc/BCD_USER_GUIDE.md`:
- When to use BCD (financial apps, precise calculations)
- Performance implications
- Examples: currency calculations, tax computations
- Limitations: trigonometric functions converted to double

---

## 11. Backward Compatibility

### 11.1 Guarantees

- BCD mode is **opt-in** (default is standard floating-point)
- Existing programs run unchanged
- No changes to `.bas` file format
- Type suffix `~` does not conflict with existing code

### 11.2 Migration Path

Applications using `RealValue` for financial calculations can:
1. Enable BCD mode: `--bcd` flag
2. Update variable types: `amount#` → `amount~` (optional)
3. Add pragmas: `#PRAGMA BCD_ENABLE`

---

## 12. Future Enhancements

### 12.1 Phase 2 Features

- [ ] Fixed-point arithmetic alternative (for embedded systems)
- [ ] Currency formatting with BCD (e.g., format to 2 decimal places)
- [ ] BCD-native trigonometric functions (currently delegated to double)
- [ ] Decimal type literal syntax (e.g., `123.45d` for explicit BCD)

### 12.2 Performance Optimization

- [ ] Cache frequently-used BCD constants (0, 1, 0.1, etc.)
- [ ] Implement BCD arithmetic in native code (JNI) for critical paths
- [ ] Profile and optimize BigDecimal usage

---

## 13. Implementation Timeline

### Phase 1: Core Implementation (2-3 weeks)
1. Create BCDValue class and unit tests
2. Modify OperatorExpression for BCD conversion
3. Add CLI parameter parsing
4. Basic integration tests

### Phase 2: Function Support (1-2 weeks)
1. Add BCD support to all mathematical functions
2. Add pragma directive handling
3. Comprehensive function testing

### Phase 3: Validation & Optimization (1 week)
1. Performance testing and optimization
2. Financial calculation validation
3. Documentation and user guide

### Phase 4: Release (ongoing)
1. Code review and integration
2. Regression testing with existing programs
3. User documentation and examples

---

## 14. Success Criteria

- ✅ All arithmetic operations support BCD mode
- ✅ No precision loss in financial calculations (0.1 + 0.2 = 0.3 exactly)
- ✅ CLI parameter `--bcd` correctly enables BCD mode
- ✅ Pragma directives work correctly
- ✅ All mathematical functions handle BCD inputs
- ✅ Type coercion rules correctly implemented
- ✅ Backward compatibility maintained (existing programs unaffected)
- ✅ Unit tests cover all BCD operations (95%+ coverage)
- ✅ System tests validate financial use cases
- ✅ Performance overhead < 5x compared to double arithmetic
- ✅ Documentation complete and examples provided

---

## Appendix A: BCD Example Programs

### A.1 Financial Calculation
```basic
#PRAGMA BCD_ENABLE
! Calculate total with tax
10 PRICE~ = 99.99
20 TAX_RATE~ = 0.0825
30 TAX~ = PRICE~ * TAX_RATE~
40 TOTAL~ = PRICE~ + TAX~
50 PRINT "Price: "; PRICE~
60 PRINT "Tax: "; TAX~
70 PRINT "Total: "; TOTAL~
80 END
```

### A.2 Currency Conversion
```basic
#PRAGMA BCD_ENABLE
10 USD~ = 100.00
20 EXCHANGE_RATE~ = 0.92
30 EUR~ = USD~ * EXCHANGE_RATE~
40 PRINT USD~; " USD = "; EUR~; " EUR"
50 END
```

### A.3 Compound Interest
```basic
#PRAGMA BCD_ENABLE
10 PRINCIPAL~ = 10000.00
20 RATE~ = 0.05
30 TIME% = 10
40 RESULT~ = PRINCIPAL~ * (1.05 ^ TIME%)
50 PRINT "Investment: "; PRINCIPAL~
60 PRINT "After "; TIME%; " years: "; RESULT~
70 END
```

---

## Appendix B: BigDecimal Reference

Reference Java documentation:
- `java.math.BigDecimal` - Arbitrary-precision decimal arithmetic
- `java.math.RoundingMode` - Rounding modes (HALF_UP, DOWN, etc.)
- `java.math.MathContext` - Precision and rounding context

Key BigDecimal methods:
```java
BigDecimal add(BigDecimal augend)
BigDecimal subtract(BigDecimal subtrahend)
BigDecimal multiply(BigDecimal multiplicand)
BigDecimal divide(BigDecimal divisor)
BigDecimal abs()
int compareTo(BigDecimal val)
String toString()
double doubleValue()
```

