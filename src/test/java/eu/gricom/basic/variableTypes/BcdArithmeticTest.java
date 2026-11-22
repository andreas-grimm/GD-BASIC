package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.OverflowException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.statements.OperatorExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BcdArithmeticTest.java
 * <p>
 * Description: Unit tests for the BCD arithmetic and mixed-type rules (Phase 3). Positive tests prove correct results,
 * rounding, and conversion of INTEGER, LONG and REAL operands (decision D11). Negative tests prove that text, TRUE/FALSE,
 * division by zero and overflow are reported as errors.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BcdArithmeticTest {

    private static final String FORTY_NINES = "9999999999999999999999999999999999999999";

    // ----- positive: BCD with BCD -----

    @Test
    public void testPlus() throws Exception {
        assertEquals("3.75", new BcdValue("1.5").plus(new BcdValue("2.25")).toString());
    }

    @Test
    public void testMinus() throws Exception {
        assertEquals("-0.75", new BcdValue("1.5").minus(new BcdValue("2.25")).toString());
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals("5.0625", new BcdValue("2.25").multiply(new BcdValue("2.25")).toString());
    }

    @Test
    public void testDivideExact() throws Exception {
        assertEquals("0.125", new BcdValue("1").divide(new BcdValue("8")).toString());
    }

    @Test
    public void testDivideRoundsToFortyDigitsWithHalfUp() throws Exception {
        // 1/3 has 40 significant digits of 3
        assertEquals("0." + "3".repeat(40), new BcdValue("1").divide(new BcdValue("3")).toString());
        // 2/3 = 0.666... The 41st digit is 6, so HALF_UP rounds the 40th digit up to 7
        assertEquals("0." + "6".repeat(39) + "7", new BcdValue("2").divide(new BcdValue("3")).toString());
    }

    @Test
    public void testModulo() throws Exception {
        assertEquals("1.5", new BcdValue("7.5").modulo(new BcdValue("2")).toString());
    }

    @Test
    public void testPowerWholeExponent() throws Exception {
        assertEquals("1024", new BcdValue("2").power(new BcdValue("10")).toString());
    }

    @Test
    public void testPowerNegativeWholeExponent() throws Exception {
        assertEquals("0.25", new BcdValue("2").power(new BcdValue("-2")).toString());
    }

    @Test
    public void testComparisonsByNumber() throws Exception {
        assertTrue(((BooleanValue) new BcdValue("2.0").equals(new BcdValue("2.00"))).toBoolean());
        assertTrue(((BooleanValue) new BcdValue("2").smallerThan(new BcdValue("3"))).toBoolean());
        assertFalse(((BooleanValue) new BcdValue("3").smallerThan(new BcdValue("3"))).toBoolean());
        assertTrue(((BooleanValue) new BcdValue("3").largerEqualThan(new BcdValue("3"))).toBoolean());
        assertTrue(((BooleanValue) new BcdValue("3").notEqual(new BcdValue("2"))).toBoolean());
    }

    // ----- positive: mixed types (decision D11) -----

    @Test
    public void testBcdPlusRealGivesBcd() throws Exception {
        Value oResult = new BcdValue("1.5").plus(new RealValue(0.5));
        assertTrue(oResult instanceof BcdValue);
        assertEquals("2", oResult.toString());
    }

    @Test
    public void testRealPlusBcdGivesBcd() throws Exception {
        Value oResult = new RealValue(0.5).plus(new BcdValue("1.5"));
        assertTrue(oResult instanceof BcdValue);
        assertEquals("2", oResult.toString());
    }

    @Test
    public void testIntegerTimesBcdGivesBcd() throws Exception {
        Value oResult = new IntegerValue(3).multiply(new BcdValue("4.5"));
        assertTrue(oResult instanceof BcdValue);
        assertEquals("13.5", oResult.toString());
    }

    @Test
    public void testBcdMinusLongGivesBcd() throws Exception {
        assertEquals("1.5", new BcdValue("4.5").minus(new LongValue(3L)).toString());
    }

    @Test
    public void testLongMinusBcd() throws Exception {
        assertEquals("1.5", new LongValue(4L).minus(new BcdValue("2.5")).toString());
    }

    @Test
    public void testRealDividedByBcdIsBcd() throws Exception {
        Value oResult = new RealValue(1.0).divide(new BcdValue("8"));
        assertTrue(oResult instanceof BcdValue);
        assertEquals("0.125", oResult.toString());
    }

    @Test
    public void testComparisonRealWithBcd() throws Exception {
        assertTrue(((BooleanValue) new RealValue(2.5).smallerThan(new BcdValue("3"))).toBoolean());
        assertTrue(((BooleanValue) new BcdValue("3").largerThan(new RealValue(2.5))).toBoolean());
        assertTrue(((BooleanValue) new IntegerValue(3).equals(new BcdValue("3.0"))).toBoolean());
        assertTrue(((BooleanValue) new LongValue(3L).equals(new BcdValue("3.0"))).toBoolean());
    }

    @Test
    public void testShiftOfFractionalBcdValueIsRejected() {
        // Owner decision (2026-11-22): shift operands are integers. 3.9 is not a whole number.
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new BcdValue("3.9"), "<<", new IntegerValue(2)).evaluate());
    }

    @Test
    public void testBitOperatorWithBcdOperandIsInteger() throws Exception {
        // The shift count may be a BCD value: it is converted to its integer part (2.5 -> 2). 3 * 2 * 2 = 12
        assertEquals("12", new IntegerValue(3).shiftLeft(new BcdValue("2.5")).toString());
    }

    // ----- negative -----

    @Test
    public void testDivisionByZeroIsReported() {
        assertThrows(DivideByZeroException.class, () -> new BcdValue("1").divide(new BcdValue("0")));
    }

    @Test
    public void testModuloByZeroIsReported() {
        assertThrows(DivideByZeroException.class, () -> new BcdValue("1").modulo(new BcdValue("0")));
    }

    @Test
    public void testNegativePowerOfZeroIsReported() {
        assertThrows(DivideByZeroException.class, () -> new BcdValue("0").power(new BcdValue("-1")));
    }

    @Test
    public void testTextOperandIsReported() {
        assertThrows(SyntaxErrorException.class, () -> new BcdValue("1").plus(new StringValue("abc")));
    }

    @Test
    public void testBooleanOperandIsReported() {
        assertThrows(SyntaxErrorException.class, () -> new BcdValue("1").multiply(new BooleanValue(true)));
    }

    @Test
    public void testBooleanShiftCountIsReported() {
        assertThrows(SyntaxErrorException.class, () -> new IntegerValue(1).shiftLeft(new BooleanValue(true)));
    }

    @Test
    public void testRealNaNCannotBeMixed() {
        assertThrows(SyntaxErrorException.class, () -> new BcdValue("1").plus(new RealValue(Double.NaN)));
    }

    @Test
    public void testAdditionOverflows() {
        assertThrows(OverflowException.class, () -> new BcdValue(FORTY_NINES).plus(new BcdValue("1")));
    }

    @Test
    public void testMultiplicationOverflows() {
        assertThrows(OverflowException.class, () -> new BcdValue(FORTY_NINES).multiply(new BcdValue("10")));
    }

    @Test
    public void testPowerOverflows() {
        assertThrows(OverflowException.class, () -> new BcdValue("10").power(new BcdValue("40")));
    }

    @Test
    public void testMixedWithRealOverflows() {
        assertThrows(OverflowException.class, () -> new RealValue(1e50).plus(new BcdValue("1")));
    }
}
