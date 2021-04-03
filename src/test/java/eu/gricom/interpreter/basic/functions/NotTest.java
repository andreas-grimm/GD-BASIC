package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.BooleanValue;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.LongValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class NotTest {

    @Test
    public void testNotForInteger() {

        try {
            IntegerValue oValue = new IntegerValue(0);
            IntegerValue oResult = (IntegerValue) Not.execute(oValue);

            assertEquals(oResult.toInt(), 1);

            oValue = new IntegerValue(1);
            oResult = (IntegerValue) Not.execute(oValue);

            assertEquals(oResult.toInt(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsForReal() {

        try {
            RealValue oValue = new RealValue(0.0);
            RealValue oResult = (RealValue) Not.execute(oValue);

            assertEquals(oResult.toReal(), 1.0);

            oValue = new RealValue(1.0);
            oResult = (RealValue) Not.execute(oValue);

            assertEquals(oResult.toReal(), 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsForLong() {

        try {
            LongValue oValue = new LongValue(0);
            LongValue oResult = (LongValue) Not.execute(oValue);

            assertEquals(oResult.toLong(), 1);

            oValue = new LongValue(1);
            oResult = (LongValue) Not.execute(oValue);

            assertEquals(oResult.toLong(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbsForBoolean() {

        try {
            BooleanValue oValue = new BooleanValue(true);
            BooleanValue oResult = (BooleanValue) Not.execute(oValue);

            assertEquals(oResult.isTrue(), false);

            oValue = new BooleanValue(false);
            oResult = (BooleanValue) Not.execute(oValue);

            assertEquals(oResult.isTrue(), true);
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
