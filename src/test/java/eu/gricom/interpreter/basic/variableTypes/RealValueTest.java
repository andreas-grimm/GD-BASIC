package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.DivideByZeroException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RealValueTest {

    @Test
    public void testToString() {RealValue oNumberValue = new RealValue(999);

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
    public void testPlus() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.toReal() == 3.0);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMinus() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.minus(oSecondValue);
            assertTrue(oResultValue.toReal() == -1.0);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMultiply() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.multiply(oSecondValue);
            assertTrue(oResultValue.toReal() == 2.0);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testDivide() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue) oFirstValue.divide(oSecondValue);
            assertTrue(oResultValue.toReal() == 0.5);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testDivideByZero() {
        RealValue oFirstValue = new RealValue(1);
        RealValue oSecondValue = new RealValue(0);

        assertThrows(DivideByZeroException.class, () -> {
            RealValue oResultValue = (RealValue) oFirstValue.divide(oSecondValue);
        });
    }

    @Test
    public void testPower() {
        try {
            RealValue oFirstValue = new RealValue(2);
            RealValue oSecondValue = new RealValue(2);

            RealValue oResultValue = (RealValue)oFirstValue.power(oSecondValue);
            assertTrue(oResultValue.toInt() == 4);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testPowerTypeException() {
        RealValue oFirstValue = new RealValue(2);
        IntegerValue oSecondValue = new IntegerValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            RealValue oResultValue = (RealValue) oFirstValue.power(oSecondValue);
        });
    }

    @Test
    public void testSmallerThan() {
        try {
            RealValue oFirstValue = new RealValue(1);
            RealValue oSecondValue = new RealValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSmallerThanTypeException() {
        RealValue oFirstValue = new RealValue(1);
        IntegerValue oSecondValue = new IntegerValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
        });
    }

    @Test
    public void testLargerThan() {
        try {
            RealValue oFirstValue = new RealValue(2);
            RealValue oSecondValue = new RealValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testLargerThanTypeException() {
        RealValue oFirstValue = new RealValue(2);
        IntegerValue oSecondValue = new IntegerValue(1);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
        });
    }
}
