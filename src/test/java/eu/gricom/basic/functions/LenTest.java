package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import org.junit.jupiter.api.Test;

public class LenTest {
    @Test
    public void testLen() {
        try {
            StringValue oValue = new StringValue("abcd");

            IntegerValue oResult = (IntegerValue) Len.execute(oValue);

            assertEquals(4, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLenEmptyParam() {

        try {
            StringValue oValue = new StringValue("");

            IntegerValue oResult = (IntegerValue) Len.execute(oValue);

            assertEquals(0, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLenWithException() {
        try {
            RealValue oValue = new RealValue(-1);

            assertThrows(RuntimeException.class, () -> {
                Len.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
