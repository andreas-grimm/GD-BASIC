package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IfThenStatementTest {

    @Test
    public void testEvaluateJasic() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LabelStatement oLabelStatement = new LabelStatement();

        oLabelStatement.putLabelStatement("TestCase", 5);
        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, "==", oLeftValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, "TestCase");

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 5);
        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testNegativeEvaluateJasic() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LabelStatement oLabelStatement = new LabelStatement();

        oLabelStatement.putLabelStatement("TestCase", 5);
        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);
        RealValue oRightValue = new RealValue(1);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, "==", oRightValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, "TestCase");

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 4);
        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testEvaluateBasic() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();

        oLineNumberObject.putLineNumber(6, 1);
        oLineNumberObject.putStatementNumber(1, 6);

        oLineNumberObject.putLineNumber(7, 2);
        oLineNumberObject.putStatementNumber(2, 7);

        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, "==", oLeftValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, 6, 0, 5, 0);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 4);
        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testNegativeEvaluateBasic() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();

        oLineNumberObject.putLineNumber(5, 1);
        oLineNumberObject.putStatementNumber(1, 6);
        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);
        RealValue oRightValue = new RealValue(1);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, "==", oRightValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, 4, 0, 6, 0);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 7);
        } catch (Exception eException) {
            fail();
        }
    }
}
