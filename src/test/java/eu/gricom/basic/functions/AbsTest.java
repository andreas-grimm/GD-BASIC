package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.LongValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbsTest {

    @Test
    public void testAbsForInteger() {

        try {
            IntegerValue oValue = new IntegerValue(-1);

            IntegerValue oResult = (IntegerValue) Abs.execute(oValue);

            assertEquals(oResult.toInt(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsForReal() {

        try {
            RealValue oValue = new RealValue(-1.0);

            RealValue oResult = (RealValue) Abs.execute(oValue);

            assertEquals(oResult.toReal(), 1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsForLong() {

        try {
            LongValue oValue = new LongValue(-1);

            LongValue oResult = (LongValue) Abs.execute(oValue);

            assertEquals(oResult.toLong(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsWithException() {

        try {
            StringValue oValue = new StringValue("-1");

            assertThrows(RuntimeException.class, () -> {
                Abs.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
