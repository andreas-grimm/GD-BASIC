package eu.gricom.interpreter.basic.statements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberValueTest {

    @Test
    public void testToString() {
        NumberValue oNumberValue = new NumberValue(999);

        String strResult = oNumberValue.toString();
        assertTrue(strResult.matches("999.0"));
    }

    @Test
    public void testToNumber() {
        NumberValue oNumberValue = new NumberValue(999);

        double dResult = oNumberValue.toNumber();
        assertTrue(dResult == 999);
    }

    @Test
    public void testEvaluate() {
        NumberValue oNumberValue = new NumberValue(999);
        NumberValue oNewValue = (NumberValue) oNumberValue.evaluate();

        assertTrue(oNumberValue.equals(oNewValue));
    }
}
