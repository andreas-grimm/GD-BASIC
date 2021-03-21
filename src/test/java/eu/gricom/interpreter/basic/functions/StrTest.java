package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class StrTest {

    @Test
    public void testStr() {

        try {
            RealValue oRealValue = new RealValue(1.0);

            StringValue oResult = (StringValue) Str.execute(oRealValue);

            assertEquals("1.0", oResult.toString());

            IntegerValue oIntegerValue = new IntegerValue(5);

            oResult = (StringValue) Str.execute(oIntegerValue);

            assertEquals("5", oResult.toString());

            LongValue oLongValue = new LongValue(5L);

            oResult = (StringValue) Str.execute(oLongValue);

            assertEquals("5", oResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStrWithException() {

        try {
            StringValue oValue = new StringValue("-1");

            assertThrows(RuntimeException.class, () -> {
                Str.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
