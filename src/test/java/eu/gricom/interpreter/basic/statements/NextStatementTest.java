package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.EmptyStackException;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
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
