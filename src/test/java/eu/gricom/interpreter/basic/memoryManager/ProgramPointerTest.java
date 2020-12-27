package eu.gricom.interpreter.basic.memoryManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramPointerTest {
    ProgramPointer oProgramPointer = new ProgramPointer();

    @Test
    public void testCurrentStatement() {
        ProgramPointer oProgramPointer = new ProgramPointer();

        oProgramPointer.setCurrentStatement(100);

        int iResult = oProgramPointer.getCurrentStatement();
        assertEquals(iResult, 100);

        oProgramPointer.calcNextStatement();
        iResult = oProgramPointer.getCurrentStatement();
        assertEquals(iResult, 101);
    }
}
