package eu.gricom.basic.statements;

import eu.gricom.basic.memoryManager.ProgramPointer;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.memoryManager.Stack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReturnStatementTest {
    @Test
    public void testExecute() {
        Stack oStack = new Stack();
        ProgramPointer oProgramPointer = new ProgramPointer();
        LineNumberXRef oLineNumber = new LineNumberXRef();

        oLineNumber.putStatementNumber(10, 10);
        oLineNumber.putLineNumber(10, 10);

        oLineNumber.putStatementNumber(15, 15);
        oLineNumber.putLineNumber(15, 15);

        oStack.push(new IntegerValue(10));
        oProgramPointer.setCurrentStatement(20);

        ReturnStatement oReturnStatement = new ReturnStatement(30);

        try {
            oReturnStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(15, oProgramPointer.getCurrentStatement());
    }

    @Test
    public void testExecuteFails() {
        Stack oStack = new Stack();
        ProgramPointer oProgramPointer = new ProgramPointer();

        oProgramPointer.setCurrentStatement(20);

        ReturnStatement oReturnStatement = new ReturnStatement(5);

        assertThrows(EmptyStackException.class, () -> {
            oReturnStatement.execute();
        });
    }
}
