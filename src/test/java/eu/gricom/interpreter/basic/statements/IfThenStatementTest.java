package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.MemoryManagement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IfThenStatementTest {

    @Test
    public void testEvaluate() {
        MemoryManagement oMemoryManagement = new MemoryManagement();
        oMemoryManagement.putLabelStatement("TestCase", 5);
        oMemoryManagement.setCurrentStatement(4);

        NumberValue oLeftValue = new NumberValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, '=', oLeftValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, "TestCase");

            oStatement.execute();

            int iNewLabel = oMemoryManagement.getCurrentStatement();

            assertEquals(iNewLabel, 5);
        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testNegativeEvaluate() {
        MemoryManagement oMemoryManagement = new MemoryManagement();
        oMemoryManagement.putLabelStatement("TestCase", 5);
        oMemoryManagement.setCurrentStatement(4);

        NumberValue oLeftValue = new NumberValue(2);
        NumberValue oRightValue = new NumberValue(1);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, '=', oRightValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, "TestCase");

            oStatement.execute();

            int iNewLabel = oMemoryManagement.getCurrentStatement();

            assertEquals(iNewLabel, 4);
        } catch (Exception eException) {
            fail();
        }
    }
}
