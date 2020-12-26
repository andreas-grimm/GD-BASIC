package eu.gricom.interpreter.basic.helper;

import eu.gricom.interpreter.basic.variableTypes.RealValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemoryManagementTest {
    MemoryManagement oMemoryManagement = new MemoryManagement();

    @Test
    @Order(1)
    public void testPutLabelStatement() {
        oMemoryManagement.putLabelStatement("TestCase", 5);
    }

    @Test
    @Order(2)
    public void testContainsKey() {
        boolean bResult;
        oMemoryManagement.putLabelStatement("TestCase", 5);

        MemoryManagement oNewMemoryManagement = new MemoryManagement();

        bResult = oNewMemoryManagement.containsLabelKey("TestCase");
        assertTrue(bResult);

        bResult = oNewMemoryManagement.containsLabelKey("TestCase2");
        assertFalse(bResult);
    }

    @Test
    @Order(3)
    public void testGetLabelStatement() {
        MemoryManagement oNewMemoryManagement = new MemoryManagement();
        oMemoryManagement.putLabelStatement("TestCase", 5);

        int iResult = oNewMemoryManagement.getLabelStatement("TestCase");
        assertEquals(iResult, 5);
    }

    @Test
    @Order(4)
    public void testCurrentStatement() {
        MemoryManagement oMemoryManagement = new MemoryManagement();

        oMemoryManagement.setCurrentStatement(100);

        int iResult = oMemoryManagement.getCurrentStatement();
        assertEquals(iResult, 100);

        oMemoryManagement.nextStatement();
        iResult = oMemoryManagement.getCurrentStatement();
        assertEquals(iResult, 101);
    }

    @Test
    @Order(5)
    public void testVariableStorage() {
        MemoryManagement oMemoryManagement = new MemoryManagement();

        oMemoryManagement.putMap("Number", 999);
        oMemoryManagement.putMap("String", "TestValue");

        RealValue oResult = (RealValue) oMemoryManagement.getMap("Number");
        assertEquals(oResult.toReal(), 999);

        StringValue strResult = (StringValue) oMemoryManagement.getMap("String");
        assertTrue(strResult.toString().matches("TestValue"));
    }
}
