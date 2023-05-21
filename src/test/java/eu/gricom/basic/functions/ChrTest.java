package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChrTest {
    @Test
    public void testChr() {
        try {
            IntegerValue oValue = new IntegerValue(97);

            StringValue oResult = (StringValue) Chr.execute(oValue);

            assertEquals("a", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChrWithException() {
        try {
            StringValue oValue = new StringValue("-1");

            assertThrows(RuntimeException.class, () -> {
                Chr.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
