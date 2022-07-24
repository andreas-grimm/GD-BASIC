package eu.gricom.basic.variableTypes;

import eu.gricom.basic.error.DivideByZeroException;
import eu.gricom.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class LongValueTest {

    @Test
    public void testToString() {
        LongValue oNumberValue = new LongValue(999);

        String strResult = oNumberValue.toString();
        assertTrue(strResult.matches("999"));
    }

    @Test
    public void testToNumber() {
        LongValue oNumberValue = new LongValue(999);

        double dResult = oNumberValue.toReal();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        LongValue oNumberValue = new LongValue(999);
        LongValue oNewValue = (LongValue) oNumberValue.evaluate();

        assertEquals(oNumberValue, oNewValue);
    }

    @Test
    public void testEquals() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(1);

            BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testNotEqual() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            BooleanValue oResult = (BooleanValue) oFirstValue.notEqual(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPlus() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.toLong() == 3);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPlusTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.plus(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testMinus() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.minus(oSecondValue);
            assertTrue(oResultValue.toLong() == -1);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testMinusTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.minus(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testMultiply() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.multiply(oSecondValue);
            assertTrue(oResultValue.toLong() == 2);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testMultiplyTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.multiply(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testDivide() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.divide(oSecondValue);
            assertTrue(oResultValue.toLong() == 0);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            fail();
        }
    }

    @Test
    public void testDivideTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.divide(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testDivideByZero() {
        LongValue oFirstValue = new LongValue(1);
        LongValue oSecondValue = new LongValue(0);

        assertThrows(DivideByZeroException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.divide(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testModulo() {
        try {
            LongValue oFirstValue = new LongValue(3);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.modulo(oSecondValue);
            assertTrue(oResultValue.toLong() == 1);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            fail();
        }
    }

    @Test
    public void testPower() {
        try {
            LongValue oFirstValue = new LongValue(2);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.power(oSecondValue);
            assertTrue(oResultValue.toLong() == 4);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPowerTypeException() {
        LongValue oFirstValue = new LongValue(2);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.power(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testSmallerThan() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testSmallerThanTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testSmallerEqualThan() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testSmallerEqualThanTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testLargerThan() {
        try {
            LongValue oFirstValue = new LongValue(2);
            LongValue oSecondValue = new LongValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testLargerThanTypeException() {
        LongValue oFirstValue = new LongValue(2);
        RealValue oSecondValue = new RealValue(1);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testLargerEqualThan() {
        try {
            LongValue oFirstValue = new LongValue(2);
            LongValue oSecondValue = new LongValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testLargerEqualThanTypeException() {
        LongValue oFirstValue = new LongValue(2);
        RealValue oSecondValue = new RealValue(1);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }
}
