package eu.gricom.basic.statements;

import eu.gricom.basic.memorymanager.ProgramPointer;
import eu.gricom.basic.memorymanager.Stack;
import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.memorymanager.LineNumberXRef;
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

    /**
     * Regression test: statement index 0 is a valid GOSUB destination (the first statement of a program).
     * <p>
     * Before the fix, GosubStatement treated 0 as "not found" and threw "GOSUB [unknown]".
     */
    @Test
    public void testEvaluateToFirstStatement() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();
        Stack oStack = new Stack();

        // Line 9100 is the first statement of the program: statement index 0
        oLineNumberObject.putLineNumber(9100, 0);
        oLineNumberObject.putStatementNumber(0, 0);
        // The calling statement (token 10) sits at statement index 5 and must be pushed as the return point
        oLineNumberObject.putStatementNumber(10, 5);

        try {
            oProgramPointer.setCurrentStatement(7);

            GosubStatement oStatement = new GosubStatement(10, "9100");
            oStatement.execute();

            assertEquals(0, oProgramPointer.getCurrentStatement());
            assertEquals(5, ((IntegerValue) oStack.pop()).toInt());

        } catch (Exception eException) {
            fail("GOSUB to the first statement must not fail: " + eException.getMessage());
        }
    }
}

