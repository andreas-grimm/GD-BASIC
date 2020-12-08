package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.MemoryManagement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssignStatementTest {
    MemoryManagement oMemoryManagement = new MemoryManagement();

    @Test
    public void testExecute() {
        try {
            NumberValue oNumberValue = new NumberValue(1.0);
            AssignStatement oAssignmentStatement = new AssignStatement("Test", oNumberValue);
            oAssignmentStatement.execute();

            assertTrue(oAssignmentStatement.content().contains("ASSIGN [Test:= 1.0]"));
            NumberValue oTestValue = (NumberValue) oMemoryManagement.getMap("Test");
        } catch (Exception eException) {
            System.err.println(eException.getMessage());
        }
    }
}