package eu.gricom.interpreter.basic.variableTypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

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
            fail();
        }
    }

    @Test
    public void testEqualsTypeException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        RealValue oSecondValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            System.out.println(oResultValue.toString());
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
            fail();
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
            fail();
        }
    }

    @Test
    public void testMinus() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.minus(oSecondValue);
            System.out.println(oResultValue.toString());
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
            fail();
        }
    }

    @Test
    public void testDivide() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.minus(oSecondValue);
            System.out.println(oResultValue.toString());
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
            fail();
        }
    }

    @Test
    public void testPowerTypeException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        RealValue oSecondValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.power(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testSmallerThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testSmallerEqualThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(false);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testLargerThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testLargerEqualThan() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }
}
