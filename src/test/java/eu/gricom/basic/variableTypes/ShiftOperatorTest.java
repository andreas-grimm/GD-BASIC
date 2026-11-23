package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.statements.OperatorExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ShiftOperatorTest.java
 * <p>
 * Description: Tests the << and >> operators on INTEGER values. A shift by n bits multiplies (<<) or divides (>>) by
 * 2 to the power n. The earlier formulas (* 2 * n and / (2 * n)) were only right for some shift counts (bug fixed in
 * Phase 7).
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ShiftOperatorTest {

    @Test
    public void testLeftShiftByOne() throws Exception {
        assertEquals("8", new IntegerValue(4).shiftLeft(new IntegerValue(1)).toString());
    }

    @Test
    public void testLeftShiftByThree() throws Exception {
        assertEquals("8", new IntegerValue(1).shiftLeft(new IntegerValue(3)).toString());
    }

    @Test
    public void testRightShiftByThree() throws Exception {
        assertEquals("2", new IntegerValue(16).shiftRight(new IntegerValue(3)).toString());
    }

    @Test
    public void testRightShiftByZeroKeepsValue() throws Exception {
        assertEquals("5", new IntegerValue(5).shiftRight(new IntegerValue(0)).toString());
    }

    @Test
    public void testShiftWithStringOperandIsRejected() {
        assertThrows(SyntaxErrorException.class, () -> new IntegerValue(1).shiftLeft(new StringValue("2")));
        assertThrows(SyntaxErrorException.class, () -> new IntegerValue(1).shiftRight(new StringValue("2")));
    }

    // ----- Owner decision (2026-11-22): all operands of a shift are integers -----

    @Test
    public void testIntegerShiftThroughOperatorWorks() throws Exception {
        assertEquals("16", new OperatorExpression(new IntegerValue(256), ">>", new IntegerValue(4)).evaluate().toString());
        assertEquals("8", new OperatorExpression(new IntegerValue(1), "<<", new IntegerValue(3)).evaluate().toString());
    }

    @Test
    public void testWholeRealLiteralsAreIntegers() throws Exception {
        // Number literals are REAL in the interpreter; a whole number is an integer
        assertEquals("8", new OperatorExpression(new RealValue(1), "<<", new RealValue(3)).evaluate().toString());
        assertEquals("16", new OperatorExpression(new RealValue(256), ">>", new RealValue(4)).evaluate().toString());
    }

    @Test
    public void testWholeLongValuesAreIntegers() throws Exception {
        assertEquals("8", new OperatorExpression(new LongValue(1L), "<<", new IntegerValue(3)).evaluate().toString());
        assertEquals("16", new OperatorExpression(new LongValue(256L), ">>", new LongValue(4L)).evaluate().toString());
    }

    @Test
    public void testWholeBcdValuesAreIntegers() throws Exception {
        assertEquals("12", new OperatorExpression(new BcdValue("3.0"), "<<", new IntegerValue(2)).evaluate().toString());
        assertEquals("16", new OperatorExpression(new BcdValue("256"), ">>", new IntegerValue(4)).evaluate().toString());
    }

    @Test
    public void testFractionalRealOperandIsRejected() {
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new RealValue(3.9), "<<", new IntegerValue(2)).evaluate());
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new IntegerValue(256), ">>", new RealValue(4.5)).evaluate());
    }

    @Test
    public void testFractionalBcdOperandIsRejected() {
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new BcdValue("3.9"), "<<", new IntegerValue(2)).evaluate());
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new IntegerValue(256), ">>", new BcdValue("0.5")).evaluate());
    }

    @Test
    public void testOperandOutsideIntegerRangeIsRejected() {
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new RealValue(3000000000.0), "<<", new IntegerValue(1)).evaluate());
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new LongValue(4294967296L), ">>", new IntegerValue(1)).evaluate());
    }

    @Test
    public void testStringAndBooleanOperandsAreRejected() {
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new StringValue("1"), "<<", new IntegerValue(1)).evaluate());
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new IntegerValue(1), "<<", new StringValue("1")).evaluate());
        assertThrows(SyntaxErrorException.class,
            () -> new OperatorExpression(new BooleanValue(true), ">>", new IntegerValue(1)).evaluate());
    }
}
