package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class SqrTest {

    @Test
    public void testSqr() {

        try {
            RealValue oValue = new RealValue(4.0);

            RealValue oResult = (RealValue) Sqr.execute(oValue);

            assertEquals(2.0, oResult.toReal());

            oValue = new RealValue(0.0);

            oResult = (RealValue) Sqr.execute(oValue);

            assertEquals(0.0, oResult.toReal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSqrWithNegValueException() {

        try {
            RealValue oValue = new RealValue(-4.0);

            RealValue oResult = (RealValue) Sqr.execute(oValue);

            assertEquals("NaN", oResult.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSqrWithException() {

        try {
            StringValue oValue = new StringValue("4.0");

            assertThrows(RuntimeException.class, () -> {
                Sqr.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
