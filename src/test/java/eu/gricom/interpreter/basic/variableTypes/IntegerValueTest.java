package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.DivideByZeroException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegerValueTest {

    @Test
    public void testToString() {IntegerValue oNumberValue = new IntegerValue(999);

        String strResult = oNumberValue.toString();
        assertTrue(strResult.matches("999"));
    }

    @Test
    public void testToNumber() {IntegerValue oNumberValue = new IntegerValue(999);

        double dResult = oNumberValue.toReal();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        IntegerValue oNumberValue = new IntegerValue(999);
        IntegerValue oNewValue = (IntegerValue) oNumberValue.evaluate();

        assertEquals(oNumberValue, oNewValue);
    }

    @Test
    public void testEquals() {
        try {
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(1);

            BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testNotEqual() {
        try {
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(2);

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
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(2);

            IntegerValue oResultValue = (IntegerValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.toInt() == 3);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testPlusTypeException() {
        IntegerValue oFirstValue = new IntegerValue(1);
        StringValue oSecondValue = new StringValue("2");


        assertThrows(SyntaxErrorException.class, () -> {
            IntegerValue oResultValue = (IntegerValue) oFirstValue.plus(oSecondValue);
        });
    }

    @Test
    public void testMinus() {
        try {
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(2);

            IntegerValue oResultValue = (IntegerValue) oFirstValue.minus(oSecondValue);
            assertTrue(oResultValue.toInt() == -1);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMinusTypeException() {
        IntegerValue oFirstValue = new IntegerValue(1);
        StringValue oSecondValue = new StringValue("2");


        assertThrows(SyntaxErrorException.class, () -> {
            IntegerValue oResultValue = (IntegerValue) oFirstValue.minus(oSecondValue);
        });
    }

    @Test
    public void testMultiply() {
        try {
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(2);

            IntegerValue oResultValue = (IntegerValue) oFirstValue.multiply(oSecondValue);
            assertTrue(oResultValue.toInt() == 2);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMultiplyTypeException() {
        IntegerValue oFirstValue = new IntegerValue(1);
        StringValue oSecondValue = new StringValue("2");


        assertThrows(SyntaxErrorException.class, () -> {
            IntegerValue oResultValue = (IntegerValue) oFirstValue.multiply(oSecondValue);
        });
    }

    @Test
    public void testDivide() {
        try {
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(2);

            IntegerValue oResultValue = (IntegerValue) oFirstValue.divide(oSecondValue);
            assertTrue(oResultValue.toInt() == 0);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testDivideTypeException() {
        IntegerValue oFirstValue = new IntegerValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            IntegerValue oResultValue = (IntegerValue) oFirstValue.divide(oSecondValue);
        });
    }

    @Test
    public void testDivideByZero() {
        IntegerValue oFirstValue = new IntegerValue(1);
        IntegerValue oSecondValue = new IntegerValue(0);

        assertThrows(DivideByZeroException.class, () -> {
            IntegerValue oResultValue = (IntegerValue) oFirstValue.divide(oSecondValue);
        });
    }

    @Test
    public void testPower() {
        try {
            IntegerValue oFirstValue = new IntegerValue(2);
            IntegerValue oSecondValue = new IntegerValue(2);

            IntegerValue oResultValue = (IntegerValue)oFirstValue.power(oSecondValue);
            assertTrue(oResultValue.toInt() == 4);

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testPowerTypeException() {
        IntegerValue oFirstValue = new IntegerValue(2);
        StringValue oSecondValue = new StringValue("2");


        assertThrows(SyntaxErrorException.class, () -> {
            IntegerValue oResultValue = (IntegerValue) oFirstValue.power(oSecondValue);
        });
    }

    @Test
    public void testSmallerThan() {
        try {
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSmallerThanTypeException() {
        IntegerValue oFirstValue = new IntegerValue(1);
        StringValue oSecondValue = new StringValue("2");


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
        });
    }

    @Test
    public void testSmallerEqualThan() {
        try {
            IntegerValue oFirstValue = new IntegerValue(1);
            IntegerValue oSecondValue = new IntegerValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSmallerEqualThanTypeException() {
        IntegerValue oFirstValue = new IntegerValue(1);
        StringValue oSecondValue = new StringValue("2");


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
        });
    }

    @Test
    public void testLargerThan() {
        try {
            IntegerValue oFirstValue = new IntegerValue(2);
            IntegerValue oSecondValue = new IntegerValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testLargerThanTypeException() {
        IntegerValue oFirstValue = new IntegerValue(2);
        StringValue oSecondValue = new StringValue("1");


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
        });
    }

    @Test
    public void testLargerEqualThan() {
        try {
            IntegerValue oFirstValue = new IntegerValue(2);
            IntegerValue oSecondValue = new IntegerValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testLargerEqualThanTypeException() {
        IntegerValue oFirstValue = new IntegerValue(2);
        StringValue oSecondValue = new StringValue("1");


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
        });
    }
}
