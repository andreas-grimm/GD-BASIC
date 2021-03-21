package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class RightTest {
    @Test
    public void testRight() {
        try {
            StringValue oValue = new StringValue("abcde");
            IntegerValue oLength = new IntegerValue(2);

            StringValue oResult = (StringValue) Right.execute(oValue, oLength);

            assertEquals("de", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRightEmptyParam() {

        try {
            StringValue oValue = new StringValue("");
            IntegerValue oLength = new IntegerValue(2);

            assertThrows(RuntimeException.class, () -> {
                Right.execute(oValue, oLength);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRightWithException() {
        try {
            RealValue oValue = new RealValue(-1);
            IntegerValue oLength = new IntegerValue(2);

            assertThrows(RuntimeException.class, () -> {
                Right.execute(oValue, oLength);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
