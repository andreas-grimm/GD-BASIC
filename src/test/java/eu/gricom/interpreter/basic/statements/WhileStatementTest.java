package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.EmptyStackException;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.RealValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WhileStatementTest {

    @Test
    public void testWhileLoop() throws EmptyStackException {
        Stack oStack = new Stack();

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

            WhileStatement oStatement = new WhileStatement( 1, oExpression, 6);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 4);
        } catch (Exception eException) {
            System.out.println(eException.getMessage());
            fail();
        }

        oStack.pop();
    }

    @Test
    public void testEndWhileLoop() throws EmptyStackException {
        Stack oStack = new Stack();

        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef _oLineNumberObject = new LineNumberXRef();

        _oLineNumberObject.putLineNumber(6, 1);
        _oLineNumberObject.putStatementNumber(1,6);

        _oLineNumberObject.putLineNumber(7, 2);
        _oLineNumberObject.putStatementNumber(2,7);

        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, ">", oLeftValue);

            WhileStatement oStatement = new WhileStatement( 1, oExpression, 6);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 7);
        } catch (Exception eException) {
            System.out.println(eException.getMessage());
            fail();
        }
    }
}
