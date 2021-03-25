package eu.gricom.interpreter.basic.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class InstrTest {
    @Test
    public void testInstr() {
        try {
            StringValue oValue = new StringValue("abcde");
            StringValue oSearch = new StringValue("bc");

            IntegerValue oResult = (IntegerValue) Instr.execute(oValue, oSearch);

            assertEquals(1, oResult.toInt());
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

            assertEquals(-1, oResult.toInt());
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
