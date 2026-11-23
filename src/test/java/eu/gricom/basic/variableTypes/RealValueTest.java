package eu.gricom.basic.variabletypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RealValueTest {

    @Test
    public void testToString() {
        RealValue oNumberValue = new RealValue(999);

        String strResult = oNumberValue.toString();
        assertTrue(strResult.matches("999.0"));
    }

    @Test
    public void testToReal() {
        RealValue oRealValue = new RealValue(999);

        double dResult = oRealValue.toReal();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        RealValue oNumberValue = new RealValue(999);
        RealValue oNewValue = (RealValue) oNumberValue.evaluate();

        assertEquals(oNumberValue, oNewValue);
    }

    @Test
    public void testEquals() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(1);

            BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testNotEqual() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            BooleanValue oResult = (BooleanValue) oFirstValue.notEqual(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPlus() throws Exception {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.toReal() == 3.0);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testMinus() throws Exception {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.minus(oSecondValue);
            assertTrue(oResultValue.toReal() == -1.0);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testMultiply() throws Exception {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.multiply(oSecondValue);
            assertTrue(oResultValue.toReal() == 2.0);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testDivide() throws Exception {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.divide(oSecondValue);
            assertTrue(oResultValue.toReal() == 0.5);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            fail();
        }
    }

    @Test
    public void testDivideByZero() throws Exception {
        RealValue oFirstValue = new RealValue(1);
        RealValue oSecondValue = new RealValue(0);

        assertThrows(DivideByZeroException.class, () -> {
            RealValue oResultValue = (RealValue) oFirstValue.divide(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testModulo() throws Exception {
        try {
            RealValue oFirstValue = new RealValue(3);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.modulo(oSecondValue);
            assertTrue(oResultValue.toReal() == 1);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            fail();
        }
    }

    @Test
    public void testPower() throws Exception {
        try {
            RealValue oFirstValue = new RealValue(2);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.power(oSecondValue);
            assertTrue(oResultValue.toInt() == 4);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPowerWithIntegerGivesReal() throws Exception {
        // REAL with INTEGER gives a REAL result (design rule, Phase 7): 2.0 ^ 2 = 4.0
        RealValue oFirstValue = new RealValue(2);
        IntegerValue oSecondValue = new IntegerValue(2);

        Value oResult = oFirstValue.power(oSecondValue);

        assertTrue(oResult instanceof RealValue);
        assertEquals(4.0, oResult.toReal(), 0.0);
    }

    @Test
    public void testPowerWithStringIsRejected() {
        RealValue oFirstValue = new RealValue(2);

        assertThrows(SyntaxErrorException.class, () -> oFirstValue.power(new StringValue("2")));
    }

    @Test
    public void testSmallerThan() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testSmallerThanWithIntegerGivesBoolean() throws Exception {
        // REAL compared with INTEGER compares the numbers (Phase 7 fix)
        RealValue oFirstValue = new RealValue(1);
        IntegerValue oSecondValue = new IntegerValue(2);

        BooleanValue oResult = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testSmallerThanWithStringIsRejected() {
        RealValue oFirstValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () -> oFirstValue.smallerThan(new StringValue("1")));
    }

    @Test
    public void testSmallerEqualThan() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testSmallerEqualThanWithIntegerGivesBoolean() throws Exception {
        // REAL compared with INTEGER compares the numbers (Phase 7 fix)
        RealValue oFirstValue = new RealValue(1);
        IntegerValue oSecondValue = new IntegerValue(2);

        BooleanValue oResult = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testSmallerEqualThanWithStringIsRejected() {
        RealValue oFirstValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () -> oFirstValue.smallerEqualThan(new StringValue("1")));
    }

    @Test
    public void testLargerThan() {
        try {
            RealValue oFirstValue = new RealValue(2);
            RealValue oSecondValue = new RealValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testLargerThanWithIntegerGivesBoolean() throws Exception {
        // REAL compared with INTEGER compares the numbers (Phase 7 fix)
        RealValue oFirstValue = new RealValue(2);
        IntegerValue oSecondValue = new IntegerValue(1);

        BooleanValue oResult = (BooleanValue) oFirstValue.largerThan(oSecondValue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testLargerThanWithStringIsRejected() {
        RealValue oFirstValue = new RealValue(2);

        assertThrows(SyntaxErrorException.class, () -> oFirstValue.largerThan(new StringValue("1")));
    }

    @Test
    public void testLargerEqualThan() {
        try {
            RealValue oFirstValue = new RealValue(2);
            RealValue oSecondValue = new RealValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testLargerEqualThanWithIntegerGivesBoolean() throws Exception {
        // REAL compared with INTEGER compares the numbers (Phase 7 fix)
        RealValue oFirstValue = new RealValue(2);
        IntegerValue oSecondValue = new IntegerValue(1);

        BooleanValue oResult = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testLargerEqualThanWithStringIsRejected() {
        RealValue oFirstValue = new RealValue(2);

        assertThrows(SyntaxErrorException.class, () -> oFirstValue.largerEqualThan(new StringValue("1")));
    }
}
