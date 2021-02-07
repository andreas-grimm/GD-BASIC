package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.error.EmptyStackException;
import eu.gricom.interpreter.basic.memoryManager.LineNumberXRef;
import eu.gricom.interpreter.basic.memoryManager.ProgramPointer;
import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReturnTest {
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
