package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.MemoryManagement;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IfThenStatementTest {

    @Test
    public void testEvaluate() {
        MemoryManagement oMemoryManagement = new MemoryManagement();
        oMemoryManagement.putLabelStatement("TestCase", 5);
        oMemoryManagement.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

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

        RealValue oLeftValue = new RealValue(2);
        RealValue oRightValue = new RealValue(1);

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
