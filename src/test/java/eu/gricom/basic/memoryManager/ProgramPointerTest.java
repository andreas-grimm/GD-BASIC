package eu.gricom.basic.memoryManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramPointerTest {
    private final transient ProgramPointer _oProgramPointer = new ProgramPointer();

    @BeforeEach
    public void setUp() {
        _oProgramPointer.setCurrentStatement(0);
    }

    @Test
    public void testCurrentStatement() {

        _oProgramPointer.setCurrentStatement(100);

        int iResult = _oProgramPointer.getCurrentStatement();
        assertEquals(iResult, 100);

        _oProgramPointer.calcNextStatement();
        iResult = _oProgramPointer.getCurrentStatement();
        assertEquals(iResult, 101);
    }
}
