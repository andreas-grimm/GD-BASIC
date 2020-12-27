package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IfThenStatementTest {

    @Test
    public void testEvaluate() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LabelStatement oLabelStatement = new LabelStatement();

        oLabelStatement.putLabelStatement("TestCase", 5);
        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, '=', oLeftValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, "TestCase");

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 5);
        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testNegativeEvaluate() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LabelStatement oLabelStatement = new LabelStatement();

        oLabelStatement.putLabelStatement("TestCase", 5);
        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);
        RealValue oRightValue = new RealValue(1);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, '=', oRightValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, "TestCase");

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 4);
        } catch (Exception eException) {
            fail();
        }
    }
}
