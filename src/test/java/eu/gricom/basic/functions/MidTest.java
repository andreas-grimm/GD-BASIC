package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class MidTest {
    @Test
    public void testMid() {
        try {
            StringValue oValue = new StringValue("abcde");
            IntegerValue oStart = new IntegerValue(1);
            IntegerValue oEnd = new IntegerValue(3);

            StringValue oResult = (StringValue) Mid.execute(oValue, oStart, oEnd);

            assertEquals("bcd", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMidEmptyParam() {

        try {
            StringValue oValue = new StringValue("");
            IntegerValue oStart = new IntegerValue(1);
            IntegerValue oEnd = new IntegerValue(3);

            assertThrows(RuntimeException.class, () -> {
                Mid.execute(oValue, oStart, oEnd);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMidWithException() {
        try {
            RealValue oValue = new RealValue(-1);
            IntegerValue oStart = new IntegerValue(1);
            IntegerValue oEnd = new IntegerValue(3);

            assertThrows(RuntimeException.class, () -> {
                Mid.execute(oValue, oStart, oEnd);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
