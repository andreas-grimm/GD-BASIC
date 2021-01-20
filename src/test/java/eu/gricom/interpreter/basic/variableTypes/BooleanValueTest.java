package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanValueTest {

    @Test
    public void testToString() {
        BooleanValue oValue = new BooleanValue(true);

        String strResult = oValue.toString();
        assertTrue(strResult.matches("True"));
    }

    @Test
    public void testFromNumber() {
        BooleanValue oValue = new BooleanValue(1.0);

        String strResult = oValue.toString();
        assertTrue(strResult.matches("True"));

        oValue = new BooleanValue(0.0);

        strResult = oValue.toString();
        assertTrue(strResult.matches("False"));

        oValue = new BooleanValue(1);

        strResult = oValue.toString();
        assertTrue(strResult.matches("True"));

        oValue = new BooleanValue(0);

        strResult = oValue.toString();
        assertTrue(strResult.matches("False"));
    }

    @Test
    public void testToNumber() {
        BooleanValue oValue = new BooleanValue(true);

        double dResult = oValue.toReal();
        assertEquals(dResult, 1);
    }

    @Test
    public void testEvaluate() {
        BooleanValue oValue = new BooleanValue(true);
        BooleanValue oNewValue = (BooleanValue) oValue.evaluate();

        assertEquals(oValue, oNewValue);
    }

    @Test
    public void testIsTrue() {
        BooleanValue oValue = new BooleanValue(true);

        assertTrue(oValue.isTrue());
    }

    @Test
    public void testEquals() {
        try {
            BooleanValue oFirstValue = new BooleanValue(true);
            BooleanValue oSecondValue = new BooleanValue(true);

            BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testEqualsTypeException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        RealValue oSecondValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResult = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
        });
    }

    @Test
    public void testNotEqual() {
        try {
            BooleanValue oFirstValue = new BooleanValue(true);
            BooleanValue oSecondValue = new BooleanValue(false);

            BooleanValue oResult = (BooleanValue) oFirstValue.notEqual(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testPlus() {
        try {
            BooleanValue oFirstValue = new BooleanValue(true);
            BooleanValue oSecondValue = new BooleanValue(false);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.plus(oFirstValue);
            assertTrue(oResultValue.isTrue());

            oResultValue = (BooleanValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.isTrue());

            oResultValue = (BooleanValue) oSecondValue.plus(oFirstValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMinus() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.minus(oSecondValue);
        });
    }

    @Test
    public void testMultiply() {
        try {
            BooleanValue oFirstValue = new BooleanValue(true);
            BooleanValue oSecondValue = new BooleanValue(false);


            BooleanValue oResultValue = (BooleanValue) oSecondValue.multiply(oFirstValue);
            assertFalse(oResultValue.isTrue());

            oResultValue = (BooleanValue) oFirstValue.multiply(oSecondValue);
            assertTrue(oResultValue.isTrue());
        }  catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testDivide() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.minus(oSecondValue);
        });
    }

    @Test
    public void testPower() {
        try {
            BooleanValue oFirstValue = new BooleanValue(true);
            BooleanValue oSecondValue = new BooleanValue(false);

            BooleanValue oResult = (BooleanValue) oFirstValue.power(oSecondValue);
            assertTrue(oResult.isTrue());

            oResult = (BooleanValue) oFirstValue.power(oFirstValue);
            assertTrue(!oResult.isTrue());
        }  catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testPowerTypeException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        RealValue oSecondValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.power(oSecondValue);
        });
    }

    @Test
    public void testSmallerThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
        });
    }

    @Test
    public void testSmallerEqualThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(false);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
        });
    }

    @Test
    public void testLargerThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResult = (BooleanValue) oFirstValue.largerThan(oSecondValue);
        });
    }

    @Test
    public void testLargerEqualThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResult = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
        });
    }
}
