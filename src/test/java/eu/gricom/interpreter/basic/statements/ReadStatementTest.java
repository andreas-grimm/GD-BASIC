package eu.gricom.interpreter.basic.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.gricom.interpreter.basic.error.OutOfDataException;
import eu.gricom.interpreter.basic.memoryManager.FiFoQueue;
import eu.gricom.interpreter.basic.memoryManager.VariableManagement;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ReadStatementTest {
    private final transient VariableManagement _oVariableManagement = new VariableManagement();

    @Test
    public void testExecuteSingleValue() {
        FiFoQueue oFiFo = new FiFoQueue();
        List<String> astrVariables = new ArrayList<>();

        oFiFo.push(new StringValue("TestValue"));

        try {
            astrVariables.add("A$");

            ReadStatement oReadStatement = new ReadStatement(1, astrVariables);
            oReadStatement.execute();

            StringValue oString = (StringValue) _oVariableManagement.getMap("A$");

            assertTrue(oString.toString().contains("TestValue"));
        } catch (Exception eException) {
            System.err.println(eException.getMessage());
        }
    }

    @Test
    public void testExecuteDoubleValue() {
        FiFoQueue oFiFo = new FiFoQueue();
        List<String> astrVariables = new ArrayList<>();

        oFiFo.push(new StringValue("TestValue"));
        oFiFo.push(new IntegerValue(999));

        try {
            astrVariables.add("A$");
            astrVariables.add("A#");

            ReadStatement oReadStatement = new ReadStatement(1, astrVariables);
            oReadStatement.execute();

            StringValue oString = (StringValue) _oVariableManagement.getMap("A$");
            IntegerValue oInteger = (IntegerValue) _oVariableManagement.getMap("A#");

            assertTrue(oString.toString().contains("TestValue"));
            assertEquals(999, oInteger.toInt());
        } catch (Exception eException) {
            System.err.println(eException.getMessage());
        }
    }

    @Test
    public void testExecuteWithException() {
        FiFoQueue oFiFo = new FiFoQueue();
        List<String> astrVariables = new ArrayList<>();

        oFiFo.push(new StringValue("TestValue"));

        astrVariables.add("A$");
        astrVariables.add("B$");

        ReadStatement oReadStatement = new ReadStatement(1, astrVariables);

        assertThrows(OutOfDataException.class, () -> {
            oReadStatement.execute();
        });
    }
}
