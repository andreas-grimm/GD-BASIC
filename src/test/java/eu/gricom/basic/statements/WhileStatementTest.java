package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.memoryManager.Stack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WhileStatementTest {

    @Test
    public void testWhileLoop() throws EmptyStackException {
        Stack oStack = new Stack();

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

            WhileStatement oStatement = new WhileStatement(1, oExpression, 6);

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
        LineNumberXRef oLineNumberObject = new LineNumberXRef();

        oLineNumberObject.putLineNumber(6, 1);
        oLineNumberObject.putStatementNumber(1, 6);

        oLineNumberObject.putLineNumber(7, 2);
        oLineNumberObject.putStatementNumber(2, 7);

        oProgramPointer.setCurrentStatement(4);

        RealValue oLeftValue = new RealValue(2);

        try {
            OperatorExpression oExpression = new OperatorExpression(oLeftValue, ">", oLeftValue);

            WhileStatement oStatement = new WhileStatement(1, oExpression, 6);

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 7);
        } catch (Exception eException) {
            System.out.println(eException.getMessage());
            fail();
        }
    }
}
