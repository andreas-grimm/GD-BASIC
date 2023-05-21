package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class LeftTest {
    @Test
    public void testLeft() {
        try {
            StringValue oValue = new StringValue("abcd");
            IntegerValue oLength = new IntegerValue(2);

            StringValue oResult = (StringValue) Left.execute(oValue, oLength);

            assertEquals("ab", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLeftEmptyParam() {

        try {
            StringValue oValue = new StringValue("");
            IntegerValue oLength = new IntegerValue(2);

            assertThrows(RuntimeException.class, () -> {
                Left.execute(oValue, oLength);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLeftWithException() {
        try {
            RealValue oValue = new RealValue(-1);
            IntegerValue oLength = new IntegerValue(2);

            assertThrows(RuntimeException.class, () -> {
                Left.execute(oValue, oLength);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
