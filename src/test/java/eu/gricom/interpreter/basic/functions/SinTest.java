package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class SinTest {

    @Test
    public void testSin() {

        try {
            RealValue oValue = new RealValue(1.0);

            RealValue oResult = (RealValue) Sin.execute(oValue);

            assertEquals(0.8414709848078965, oResult.toReal());

            oValue = new RealValue(0.0);

            oResult = (RealValue) Sin.execute(oValue);

            assertEquals(0.0, oResult.toReal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAtnWithException() {

        try {
            StringValue oValue = new StringValue("-1");

            assertThrows(RuntimeException.class, () -> {
                Sin.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
