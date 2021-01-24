package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import org.junit.jupiter.api.Test;

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
        LineNumberStatement _oLineNumberObject = new LineNumberStatement();

        _oLineNumberObject.putLineNumber(5, 1);
        _oLineNumberObject.putStatementNumber(1,6);

        try {
            GotoStatement oStatement = new GotoStatement("5");

            oStatement.execute();

            int iNewLabel = oProgramPointer.getCurrentStatement();

            assertEquals(iNewLabel, 6);

        } catch (Exception eException) {
            fail();
        }
    }
}

