package eu.gricom.basic.memoryManager;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VariableManagementTest {

    @Test
    @Order(1)
    public void testVariableStorage() throws SyntaxErrorException, RuntimeException {
        VariableManagement oVariableManagement = new VariableManagement();

        oVariableManagement.putMap("Integer%", 999);
        oVariableManagement.putMap("String$", "TestValue");

        try {
            IntegerValue oResult = (IntegerValue) oVariableManagement.getMap("Integer%");
            assertEquals(oResult.toInt(), 999);

            StringValue strResult = (StringValue) oVariableManagement.getMap("String$");
            assertTrue(strResult.toString().matches("TestValue"));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void testHashSuffixThrowsSyntaxErrorInPutMapValue() {
        VariableManagement vm = new VariableManagement();

        SyntaxErrorException exception = assertThrows(
            SyntaxErrorException.class,
            () -> vm.putMap("badVar#", new RealValue(1.0))
        );
        assertTrue(exception.getMessage().contains("#"));
        assertTrue(exception.getMessage().contains("unsupported"));
    }

    @Test
    @Order(3)
    public void testHashSuffixThrowsSyntaxErrorInPutMapDouble() {
        VariableManagement vm = new VariableManagement();

        SyntaxErrorException exception = assertThrows(
            SyntaxErrorException.class,
            () -> vm.putMap("myVar#", 3.14)
        );
        assertTrue(exception.getMessage().contains("#"));
    }

    @Test
    @Order(4)
    public void testUntypedVariableStoredAsReal() throws SyntaxErrorException, RuntimeException {
        VariableManagement vm = new VariableManagement();

        vm.putMap("x", new RealValue(3.14159));
        Value result = vm.getMap("x");
        assertNotNull(result);
        if (result != null) {
            assertEquals(3.14159, result.toReal(), 0.00001);
        }
    }

    @Test
    @Order(5)
    public void testPutMapDoubleAcceptsUntyped() throws SyntaxErrorException, RuntimeException {
        VariableManagement vm = new VariableManagement();

        // Should NOT throw exception
        vm.putMap("pi", 3.14159);
        Value result = vm.getMap("pi");
        assertNotNull(result);
        if (result != null) {
            assertEquals(3.14159, result.toReal(), 0.00001);
        }
    }

    @Test
    @Order(6)
    public void testPutMapDoubleAcceptsExclamation() throws SyntaxErrorException, RuntimeException {
        VariableManagement vm = new VariableManagement();

        // Should NOT throw exception
        vm.putMap("e!", 2.71828);
        Value result = vm.getMap("e!");
        assertNotNull(result);
        if (result != null) {
            assertEquals(2.71828, result.toReal(), 0.00001);
        }
    }

    @Test
    @Order(7)
    public void testPutMapDoubleRejectsTypedVariables() {
        VariableManagement vm = new VariableManagement();

        // String type
        SyntaxErrorException ex1 = assertThrows(SyntaxErrorException.class,
            () -> vm.putMap("x$", 3.14));
        assertTrue(ex1.getMessage().contains("cannot store double"));

        // Integer type
        SyntaxErrorException ex2 = assertThrows(SyntaxErrorException.class,
            () -> vm.putMap("x%", 3.14));
        assertTrue(ex2.getMessage().contains("cannot store double"));
    }
}
