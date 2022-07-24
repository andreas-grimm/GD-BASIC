package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class ValTest {

    @Test
    public void testVal() {

        try {
            StringValue oValue = new StringValue("-1.5");

            RealValue oResult = (RealValue) Val.execute(oValue);

            assertEquals(-1.5, oResult.toReal());

            oValue = new StringValue("0.0");

            oResult = (RealValue) Val.execute(oValue);

            assertEquals(0.0, oResult.toReal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValWithException() {

        try {
            RealValue oValue = new RealValue(-1);

            assertThrows(RuntimeException.class, () -> {
                Val.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
