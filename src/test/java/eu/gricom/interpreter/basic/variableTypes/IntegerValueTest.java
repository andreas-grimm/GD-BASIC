package eu.gricom.interpreter.basic.variableTypes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegerValueTest {

    @Test
    public void testToString() {RealValue oNumberValue = new RealValue(999);

        String strResult = oNumberValue.toString();
        assertTrue(strResult.matches("999.0"));
    }

    @Test
    public void testToNumber() {RealValue oNumberValue = new RealValue(999);

        double dResult = oNumberValue.toReal();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        RealValue oNumberValue = new RealValue(999);
        RealValue oNewValue = (RealValue) oNumberValue.evaluate();

        assertEquals(oNumberValue, oNewValue);
    }
}
