package eu.gricom.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.variabletypes.RealValue;
import eu.gricom.basic.variabletypes.StringValue;
import org.junit.jupiter.api.Test;

public class InstrTest {
    @Test
    public void testInstr() {
        try {
            StringValue oValue = new StringValue("abcde");
            StringValue oSearch = new StringValue("bc");

            IntegerValue oResult = (IntegerValue) Instr.execute(oValue, oSearch);

            // one-based position (Phase 7): "bc" is at position 2 of "abcde"
            assertEquals(2, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstrFail() {
        try {
            StringValue oValue = new StringValue("abcde");
            StringValue oSearch = new StringValue("fg");

            IntegerValue oResult = (IntegerValue) Instr.execute(oValue, oSearch);

            // 0 when the search text is not found (Phase 7, documented)
            assertEquals(0, oResult.toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstrEmptyParam() {

        try {
            StringValue oValue = new StringValue("");
            StringValue oSearch = new StringValue("fg");

            assertThrows(RuntimeException.class, () -> {
                Instr.execute(oValue, oSearch);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstrWithException() {
        try {
            RealValue oValue = new RealValue(-1);
            StringValue oSearch = new StringValue("fg");

            assertThrows(RuntimeException.class, () -> {
                Instr.execute(oValue, oSearch);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
