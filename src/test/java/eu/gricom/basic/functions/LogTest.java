package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class LogTest {

    @Test
    public void testExp() {

        try {
            RealValue oValue = new RealValue(2.718281828459045);

            RealValue oResult = (RealValue) Log.execute(oValue);

            assertEquals(1.0, oResult.toReal());

            oValue = new RealValue(1.0);

            oResult = (RealValue) Log.execute(oValue);

            assertEquals(0.0, oResult.toReal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAtnWithException() {

        try {
            StringValue oValue = new StringValue("1");

            assertThrows(RuntimeException.class, () -> {
                Cos.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
