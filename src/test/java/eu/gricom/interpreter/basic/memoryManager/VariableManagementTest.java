package eu.gricom.interpreter.basic.memoryManager;

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
    public void testVariableStorage() {
        VariableManagement oVariableManagement = new VariableManagement();

        oVariableManagement.putMap("Number", 999);
        oVariableManagement.putMap("String", "TestValue");

        RealValue oResult = (RealValue) oVariableManagement.getMap("Number");
        assertEquals(oResult.toReal(), 999);

        StringValue strResult = (StringValue) oVariableManagement.getMap("String");
        assertTrue(strResult.toString().matches("TestValue"));
    }
}
