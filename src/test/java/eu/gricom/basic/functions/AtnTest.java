package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtnTest {

    @Test
    public void testAtn() {

        try {
            RealValue oValue = new RealValue(-1.0);

            RealValue oResult = (RealValue) Atn.execute(oValue);

            assertEquals(-0.7853981633974483, oResult.toReal());

            oValue = new RealValue(0.0);

            oResult = (RealValue) Atn.execute(oValue);

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
                Atn.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
