package eu.gricom.basic.statements;

import eu.gricom.basic.variabletypes.IntegerValue;
import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.memorymanager.ProgramPointer;
import eu.gricom.basic.memorymanager.Stack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NextStatementTest {
    @Test
    public void testExecute() {
        Stack oStack = new Stack();
        ProgramPointer oProgramPointer = new ProgramPointer();

        oStack.push(new IntegerValue(10));
        oProgramPointer.setCurrentStatement(20);

        NextStatement oNextStatement = new NextStatement(5);

        try {
            oNextStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(10, oProgramPointer.getCurrentStatement());
    }

    @Test
    public void testExecuteJumpsToStatementZero() throws Exception {
        // Statement 0 is the first statement of a program: a loop starting there must be jumpable (bug fixed in Phase 6)
        Stack oStack = new Stack();
        ProgramPointer oProgramPointer = new ProgramPointer();

        oStack.push(new IntegerValue(0));
        oProgramPointer.setCurrentStatement(20);

        NextStatement oNextStatement = new NextStatement(5);
        oNextStatement.execute();

        assertEquals(0, oProgramPointer.getCurrentStatement());
    }

    @Test
    public void testExecuteFails() {
        Stack oStack = new Stack();
        ProgramPointer oProgramPointer = new ProgramPointer();

        oProgramPointer.setCurrentStatement(20);

        NextStatement oNextStatement = new NextStatement(5);

        assertThrows(EmptyStackException.class, () -> {
            oNextStatement.execute();
        });
    }

}
