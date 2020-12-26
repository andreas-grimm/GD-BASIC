package eu.gricom.interpreter.basic.variableTypes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanValueTest {

    @Test
    public void testToString() {
        BooleanValue oValue = new BooleanValue(true);

        String strResult = oValue.toString();
        assertTrue(strResult.matches("True"));
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
}

