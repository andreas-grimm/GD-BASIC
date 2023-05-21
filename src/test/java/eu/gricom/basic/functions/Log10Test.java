package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class Log10Test {

    @Test
    public void testExp() {

        try {
            RealValue oValue = new RealValue(100);

            RealValue oResult = (RealValue) Log10.execute(oValue);

            assertEquals(2.0, oResult.toReal());

            oValue = new RealValue(1.0);

            oResult = (RealValue) Log10.execute(oValue);

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
