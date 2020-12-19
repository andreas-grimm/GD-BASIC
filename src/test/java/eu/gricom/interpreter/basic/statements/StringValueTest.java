package eu.gricom.interpreter.basic.statements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        double dResult = oStringValue.toNumber();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        StringValue oStringValue = new StringValue("TestValue");
        StringValue oNewValue = (StringValue) oStringValue.evaluate();

        assertEquals(oStringValue, oNewValue);
    }
}
