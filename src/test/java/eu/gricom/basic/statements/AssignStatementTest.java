package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.memoryManager.VariableManagement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssignStatementTest {
    private final transient VariableManagement _oVariableManagement = new VariableManagement();

    @Test
    public void testExecute() {
        try {
            RealValue oNumberValue = new RealValue(1.0);
            AssignStatement oAssignmentStatement = new AssignStatement("Test#", oNumberValue);
            oAssignmentStatement.execute();

            assertTrue(oAssignmentStatement.content().contains("ASSIGN [Test#:= 1.0]"));
            RealValue oTestValue = (RealValue) _oVariableManagement.getMap("Test#");
        } catch (Exception eException) {
            System.err.println(eException.getMessage());
        }
    }
}
