package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.error.RuntimeException;
import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VariableManagementTest {

    @Test
    @Order(1)
    public void testVariableStorage() throws SyntaxErrorException, RuntimeException {
        VariableManagement oVariableManagement = new VariableManagement();

        oVariableManagement.putMap("Integer%", 999);
        oVariableManagement.putMap("String$", "TestValue");

        IntegerValue oResult = null;
        try {
            oResult = (IntegerValue) oVariableManagement.getMap("Integer%");
            assertEquals(oResult.toInt(), 999);

            StringValue strResult = (StringValue) oVariableManagement.getMap("String$");
            assertTrue(strResult.toString().matches("TestValue"));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
