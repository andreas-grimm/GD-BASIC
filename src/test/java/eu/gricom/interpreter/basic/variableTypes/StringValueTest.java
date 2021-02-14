package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.DivideByZeroException;
import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringValueTest {

    @Test
    public void testToString() {
        StringValue oStringValue = new StringValue("TestValue");
        String strResult;

        strResult = oStringValue.toString();
        assertEquals(strResult, "TestValue");
    }

    @Test
    public void testToNumber() {
        StringValue oStringValue = new StringValue("999");

        double dResult = oStringValue.toReal();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        StringValue oStringValue = new StringValue("TestValue");
        StringValue oNewValue = (StringValue) oStringValue.evaluate();

        assertEquals(oStringValue, oNewValue);
    }

    @Test
    public void testEquals() {
        try {
            StringValue oFirstValue = new StringValue("1");
            StringValue oSecondValue = new StringValue("1");

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
            StringValue oFirstValue = new StringValue("1");
            StringValue oSecondValue = new StringValue("2");

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
            StringValue oFirstValue = new StringValue("a");
            StringValue oSecondValue = new StringValue("b");

            StringValue oResultValue = (StringValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.toString().matches("ab"));

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMinus() {
        StringValue oFirstValue = new StringValue("a");
        StringValue oSecondValue = new StringValue("b");

        assertThrows(SyntaxErrorException.class, () -> {
            StringValue oResultValue = (StringValue) oFirstValue.minus(oSecondValue);
        });
    }

    @Test
    public void testMultiply() {
        StringValue oFirstValue = new StringValue("a");
        StringValue oSecondValue = new StringValue("b");

        assertThrows(SyntaxErrorException.class, () -> {
            StringValue oResultValue = (StringValue) oFirstValue.multiply(oSecondValue);
        });
    }

    @Test
    public void testDivide() {
        StringValue oFirstValue = new StringValue("a");
        StringValue oSecondValue = new StringValue("b");

        assertThrows(SyntaxErrorException.class, () -> {
            StringValue oResultValue = (StringValue) oFirstValue.divide(oSecondValue);
        });
    }

    @Test
    public void testPower() {
        StringValue oFirstValue = new StringValue("a");
        StringValue oSecondValue = new StringValue("b");

        assertThrows(SyntaxErrorException.class, () -> {
            StringValue oResultValue = (StringValue) oFirstValue.power(oSecondValue);
        });
    }

    @Test
    public void testSmallerThan() {
        try {
            StringValue oFirstValue = new StringValue("a");
            StringValue oSecondValue = new StringValue("b");

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSmallerThanTypeException() {
        StringValue oFirstValue = new StringValue("a");
        IntegerValue oSecondValue = new IntegerValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
        });
    }

    @Test
    public void testSmallerEqualThan() {
        try {
            StringValue oFirstValue = new StringValue("a");
            StringValue oSecondValue = new StringValue("b");

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSmallerEqualThanTypeException() {
        StringValue oFirstValue = new StringValue("a");
        IntegerValue oSecondValue = new IntegerValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
        });
    }

    @Test
    public void testLargerThan() {
        try {
            StringValue oFirstValue = new StringValue("b");
            StringValue oSecondValue = new StringValue("a");

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testLargerThanTypeException() {
        StringValue oFirstValue = new StringValue("a");
        IntegerValue oSecondValue = new IntegerValue(1);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
        });
    }

    @Test
    public void testLargerEqualThan() {
        try {
            StringValue oFirstValue = new StringValue("b");
            StringValue oSecondValue = new StringValue("a");

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testLargerEqualThanTypeException() {
        StringValue oFirstValue = new StringValue("a");
        IntegerValue oSecondValue = new IntegerValue(1);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
        });
    }

    @Test
    public void testProcessSquareBrackets() {
        StringValue oValue = new StringValue("abcdef");

        try {
            assertEquals("b", oValue.process("a$[1]").toString());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessSquareBracketsOutOfBorder() {
        StringValue oValue = new StringValue("abcdef");

        assertThrows(RuntimeException.class, () -> {
            oValue.process("a$[6]").toString();
        });
    }
}
