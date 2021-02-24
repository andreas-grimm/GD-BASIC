package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class CosTest {

    @Test
    public void testCos() {

        try {
            RealValue oValue = new RealValue(-1.0);

            RealValue oResult = (RealValue) Cos.execute(oValue);

            assertEquals(0.5403023058681398, oResult.toReal());

            oValue = new RealValue(0.0);

            oResult = (RealValue) Cos.execute(oValue);

            assertEquals(1.0, oResult.toReal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAtnWithException() {

        try {
            StringValue oValue = new StringValue("-1");

            assertThrows(RuntimeException.class, () -> {
                Cos.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
