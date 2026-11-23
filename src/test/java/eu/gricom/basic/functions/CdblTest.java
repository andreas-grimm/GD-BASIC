package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.LongValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
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
