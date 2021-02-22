package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CdblTest {

    @Test
    public void testCdblForInteger() {

        try {
            IntegerValue oValue = new IntegerValue(-1);

            RealValue oResult = (RealValue) Cdbl.execute(oValue);

            assertEquals(oResult.toReal(), -1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsForReal() {

        try {
            RealValue oValue = new RealValue(-1.0);

            RealValue oResult = (RealValue) Cdbl.execute(oValue);

            assertEquals(oResult.toReal(), -1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsForLong() {

        try {
            LongValue oValue = new LongValue(-1);

            RealValue oResult = (RealValue) Cdbl.execute(oValue);

            assertEquals(oResult.toReal(), -1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsWithException() {

        try {
            StringValue oValue = new StringValue("-1");

            assertThrows(RuntimeException.class, () -> {
                Cdbl.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
