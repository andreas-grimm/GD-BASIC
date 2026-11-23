package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import org.junit.jupiter.api.Test;

public class ValTest {

    @Test
    public void testVal() {

        try {
            StringValue oValue = new StringValue("-1.5");

            RealValue oResult = (RealValue) Val.execute(oValue);

            assertEquals(-1.5, oResult.toReal());

            oValue = new StringValue("0.0");

            oResult = (RealValue) Val.execute(oValue);

            assertEquals(0.0, oResult.toReal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Positive: the number at the start of the text is returned (BASIC rule for VAL).
    @Test
    public void testValReadsLeadingNumber() throws Exception {
        assertEquals(12.0, ((RealValue) Val.execute(new StringValue("12abc"))).toReal());
        assertEquals(3.14, ((RealValue) Val.execute(new StringValue("3.14xyz"))).toReal());
        assertEquals(42.0, ((RealValue) Val.execute(new StringValue("   42"))).toReal());
        assertEquals(-7.0, ((RealValue) Val.execute(new StringValue("-7"))).toReal());
        assertEquals(0.5, ((RealValue) Val.execute(new StringValue(".5"))).toReal());
        assertEquals(1000.0, ((RealValue) Val.execute(new StringValue("1e3"))).toReal());
    }

    // Positive: text without a leading number gives 0 and does not stop the program.
    @Test
    public void testValWithoutNumberGivesZero() throws Exception {
        assertEquals(0.0, ((RealValue) Val.execute(new StringValue("abc"))).toReal());
        assertEquals(0.0, ((RealValue) Val.execute(new StringValue(""))).toReal());
        assertEquals(0.0, ((RealValue) Val.execute(new StringValue("   "))).toReal());
        assertEquals(0.0, ((RealValue) Val.execute(new StringValue("-"))).toReal());
    }

    @Test
    public void testValWithException() {

        try {
            RealValue oValue = new RealValue(-1);

            assertThrows(RuntimeException.class, () -> {
                Val.execute(oValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
