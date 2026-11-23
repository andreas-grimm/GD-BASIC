package eu.gricom.basic.statements;

import static org.junit.jupiter.api.Assertions.assertFalse;
import eu.gricom.basic.variabletypes.BooleanValue;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.variabletypes.BcdValue;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.LongValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import eu.gricom.basic.variabletypes.Value;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the unary minus (-A is the value of A times -1, and the type is kept).
 */
public class UnaryMinusTest {

    // ---------------------------------------------------------------- positive

    @Test
    public void testIntegerStaysInteger() throws Exception {
        Value oResult = UnaryOperatorExpression.evaluateUnaryMinus(new IntegerValue(5));
        assertTrue(oResult instanceof IntegerValue);
        assertEquals("-5", oResult.toString());
    }

    @Test
    public void testRealStaysReal() throws Exception {
        Value oResult = UnaryOperatorExpression.evaluateUnaryMinus(new RealValue(2.5));
        assertTrue(oResult instanceof RealValue);
        assertEquals(-2.5, oResult.toReal(), 0.0);
    }

    @Test
    public void testLongStaysLong() throws Exception {
        Value oResult = UnaryOperatorExpression.evaluateUnaryMinus(new LongValue(7L));
        assertTrue(oResult instanceof LongValue);
        assertEquals(-7.0, oResult.toReal(), 0.0);
    }

    @Test
    public void testBcdStaysBcd() throws Exception {
        Value oResult = UnaryOperatorExpression.evaluateUnaryMinus(new BcdValue(new BigDecimal("1.25")));
        assertTrue(oResult instanceof BcdValue);
        assertEquals(0, new BigDecimal("-1.25").compareTo(new BigDecimal(oResult.toString())));
    }

    @Test
    public void testNegativeIntegerBecomesPositive() throws Exception {
        assertEquals(5.0, UnaryOperatorExpression.evaluateUnaryMinus(new IntegerValue(-5)).toReal(), 0.0);
    }

    @Test
    public void testZeroStaysZero() throws Exception {
        assertEquals(0.0, UnaryOperatorExpression.evaluateUnaryMinus(new IntegerValue(0)).toReal(), 0.0);
    }

    // ---------------------------------------------------------------- negative

    @Test
    public void testStringIsRefused() {
        SyntaxErrorException oException = assertThrows(SyntaxErrorException.class,
                () -> UnaryOperatorExpression.evaluateUnaryMinus(new StringValue("x")));
        assertEquals("Cannot apply unary minus to a string: x", oException.getMessage());
    }

    @Test
    public void testNumericLookingStringIsStillRefused() {
        assertThrows(SyntaxErrorException.class,
                () -> UnaryOperatorExpression.evaluateUnaryMinus(new StringValue("5")));
    }

    @Test
    public void testNullIsRefused() {
        assertThrows(SyntaxErrorException.class, () -> UnaryOperatorExpression.evaluateUnaryMinus(null));
    }

    // ---------------------------------------------------------------- booleans swap TRUE and FALSE

    @Test
    public void testTrueBecomesFalse() throws Exception {
        Value oResult = UnaryOperatorExpression.evaluateUnaryMinus(new BooleanValue(true));
        assertTrue(oResult instanceof BooleanValue);
        assertFalse(((BooleanValue) oResult).toBoolean());
    }

    @Test
    public void testFalseBecomesTrue() throws Exception {
        Value oResult = UnaryOperatorExpression.evaluateUnaryMinus(new BooleanValue(false));
        assertTrue(oResult instanceof BooleanValue);
        assertTrue(((BooleanValue) oResult).toBoolean());
    }

    @Test
    public void testDoubleMinusOnBooleanGivesOriginal() throws Exception {
        Value oOnce = UnaryOperatorExpression.evaluateUnaryMinus(new BooleanValue(true));
        Value oTwice = UnaryOperatorExpression.evaluateUnaryMinus(oOnce);
        assertTrue(((BooleanValue) oTwice).toBoolean());
    }
}
