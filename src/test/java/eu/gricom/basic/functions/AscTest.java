package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AscTest {
    @Test
    public void testAsc() {
        try {
            StringValue oValue = new StringValue("a");

            IntegerValue oResult = (IntegerValue) Asc.execute(oValue);

            assertEquals(97, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAscEmptyParam() {

        try {
            StringValue oValue = new StringValue("");

            assertThrows(RuntimeException.class, () -> {
                Asc.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAscWithException() {
        try {
            RealValue oValue = new RealValue(-1);

            assertThrows(RuntimeException.class, () -> {
                Asc.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
