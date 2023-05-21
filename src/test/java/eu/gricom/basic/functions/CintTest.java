package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CintTest {

    @Test
    public void testCintForInteger() {

        try {
            IntegerValue oValue = new IntegerValue(-1);

            IntegerValue oResult = (IntegerValue) Cint.execute(oValue);

            assertEquals(-1, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCintForReal() {

        try {
            RealValue oValue = new RealValue(-1.0);

            IntegerValue oResult = (IntegerValue) Cint.execute(oValue);

            assertEquals(-1, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCintForLong() {

        try {
            LongValue oValue = new LongValue(-1);

            IntegerValue oResult = (IntegerValue) Cint.execute(oValue);

            assertEquals(-1, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCintWithException() {

        try {
            StringValue oValue = new StringValue("-1");

            assertThrows(RuntimeException.class, () -> {
                Cint.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
