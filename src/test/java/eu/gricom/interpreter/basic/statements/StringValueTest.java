package eu.gricom.interpreter.basic.statements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringValueTest {

    @Test
    public void testToString() {
        StringValue oStringValue = new StringValue("TestValue");
        String strResult;

        strResult = oStringValue.toString();
        assertTrue(strResult.matches("TestValue"));
    }

    @Test
    public void testToNumber() {
        StringValue oStringValue = new StringValue("999");

        double dResult = oStringValue.toNumber();
        assertTrue(dResult == 999);
    }

    @Test
    public void testEvaluate() {
        StringValue oStringValue = new StringValue("TestValue");
        StringValue oNewValue = (StringValue) oStringValue.evaluate();

        assertTrue(oStringValue.equals(oNewValue));
    }
}
