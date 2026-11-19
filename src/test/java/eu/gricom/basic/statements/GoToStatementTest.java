package eu.gricom.basic.statements;

import eu.gricom.basic.error.SyntaxErrorException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.ProgramPointer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoToStatementTest {

    @Test
    public void testEvaluateJasic() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LabelStatement oLabelStatement = new LabelStatement();

        oLabelStatement.putLabelStatement("TestCase", 5);

        try {
            GotoStatement oStatement = new GotoStatement("TestCase");

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 5);

        } catch (Exception eException) {
            fail();
        }
    }

    @Test
    public void testEvaluateBasic() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();

        oLineNumberObject.putLineNumber(5, 1);
        oLineNumberObject.putStatementNumber(1, 6);

        try {
            GotoStatement oStatement = new GotoStatement("5");

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(6, iNewLabel);

        } catch (Exception eException) {
            fail();
        }
    }

    /**
     * Regression test: statement index 0 is a valid GOTO destination (the first statement of a program).
     * <p>
     * Before the fix, GotoStatement treated 0 as "not found" and threw "GOTO [unknown]".
     */
    @Test
    public void testEvaluateBasicToFirstStatement() {
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumberObject = new LineNumberXRef();

        // Line 9100 is the first statement of the program: statement index 0
        oLineNumberObject.putLineNumber(9100, 0);
        oLineNumberObject.putStatementNumber(0, 0);

        try {
            oProgramPointer.setCurrentStatement(7);

            GotoStatement oStatement = new GotoStatement("9100");
            oStatement.execute();

            assertEquals(0, oProgramPointer.getCurrentStatement());

        } catch (Exception eException) {
            fail("GOTO to the first statement must not fail: " + eException.getMessage());
        }
    }

    /**
     * A GOTO to a line that does not exist must still report "GOTO [unknown]".
     */
    @Test
    public void testEvaluateBasicUnknownLineThrows() {
        GotoStatement oStatement = new GotoStatement("9999");

        SyntaxErrorException eException = assertThrows(SyntaxErrorException.class, oStatement::execute);

        assertEquals("GOTO [unknown]: Target: 9999", eException.getMessage());
    }
}
