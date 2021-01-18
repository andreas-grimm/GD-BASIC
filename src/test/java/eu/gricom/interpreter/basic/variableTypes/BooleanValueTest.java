package eu.gricom.interpreter.basic.variableTypes;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testLargerThanTypeException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        RealValue oSecondValue = new RealValue(1.0);

        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
        });
    }
}

