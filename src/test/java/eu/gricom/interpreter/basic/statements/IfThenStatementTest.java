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
        LineNumberXRef _oLineNumberObject = new LineNumberXRef();

        _oLineNumberObject.putLineNumber(6, 1);
        _oLineNumberObject.putStatementNumber(1,6);

        _oLineNumberObject.putLineNumber(7, 2);
        _oLineNumberObject.putStatementNumber(2,7);

        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, "==", oLeftValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, 6,5);

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
        LineNumberXRef _oLineNumberObject = new LineNumberXRef();

        _oLineNumberObject.putLineNumber(5, 1);
        _oLineNumberObject.putStatementNumber(1,6);
        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);
        RealValue oRightValue = new RealValue(1);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, "==", oRightValue);

            IfThenStatement oStatement = new IfThenStatement(oExpression, 4,6);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 7);
        } catch (Exception eException) {
            fail();
        }
    }}
