package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GosubStatementTest {

    @Test
    public void testEvaluate() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        // setting the line number object for the actual jump:
        // Jump to line "5" -> which is token no. 1 -> statement no. 5
        oLineNumberObject.putLineNumber(5, 1);
        oLineNumberObject.putStatementNumber(1, 5);

        // this here is used to reference the current token number, which is translated into a statement and then
        // pushed onto the stack...
        oLineNumberObject.putStatementNumber(10, 5);

        try {
            GosubStatement oStatement = new GosubStatement(10, "5");

            oStatement.execute();
            int iTokenNumber = ((IntegerValue) oStack.pop()).toInt();

            assertEquals(5, iTokenNumber);
            assertEquals(5, oProgramPointer.getCurrentStatement());

        } catch (Exception eException) {
            fail();
        }
    }
}

