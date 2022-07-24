package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.Stack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UntilStatementTest {

    @Test
    public void testEndLoop() throws SyntaxErrorException {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        oStack.push(new IntegerValue(5));
        oLineNumberObject.putStatementNumber(5, 5);

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
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        oStack.push(new IntegerValue(5));
        oLineNumberObject.putStatementNumber(5, 5);

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
