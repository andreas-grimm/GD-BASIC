package eu.gricom.interpreter.basic.functions;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
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
