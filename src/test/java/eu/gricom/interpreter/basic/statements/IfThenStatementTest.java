package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.MemoryManagement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IfThenStatementTest {

    @Test
    public void testEvaluate() {
        MemoryManagement oMemoryManagement = new MemoryManagement();
        oMemoryManagement.putLabelStatement("TestCase", 5);
        oMemoryManagement.setCurrentStatement(4);

        int iNewLabel = oMemoryManagement.getCurrentStatement();

        NumberValue oLeftValue = new NumberValue(2);
        NumberValue oRightValue = new NumberValue(1);

        try {
            iNewLabel = oMemoryManagement.getCurrentStatement();

            OperatorExpression oExpression = new OperatorExpression(oLeftValue, '=', oLeftValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, "TestCase");

            oStatement.execute();

            iNewLabel = oMemoryManagement.getCurrentStatement();

            assertTrue(iNewLabel == 5);
        } catch (Exception eException) {
            assertTrue(false);
        }
    }
}
