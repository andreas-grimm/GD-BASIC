package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.SyntaxErrorException;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.memoryManager.VariableManagement;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UntilStatementTest {

    @Test
    public void testEndLoop() throws SyntaxErrorException {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef _oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        oStack.push(new IntegerValue(5));
        _oLineNumberObject.putStatementNumber(5,5);

        oProgramPointer.setCurrentStatement(10);

        RealValue oValue = new RealValue(10);

        try {
            OperatorExpression oExpression = new OperatorExpression(oValue, "==", oValue);

            UntilStatement oStatement = new UntilStatement(20, oExpression);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 10);
        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testContinueLoop() throws SyntaxErrorException {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef _oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        oStack.push(new IntegerValue(5));
        _oLineNumberObject.putStatementNumber(5,5);

        oProgramPointer.setCurrentStatement(10);

        RealValue oValue = new RealValue(10);

        try {
            OperatorExpression oExpression = new OperatorExpression(oValue, ">", oValue);

            UntilStatement oStatement = new UntilStatement(20, oExpression);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 5);
        } catch (Exception eException) {
            fail();
        }
    }
}
