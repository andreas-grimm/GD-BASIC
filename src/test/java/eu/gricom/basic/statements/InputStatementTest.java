package eu.gricom.basic.statements;

import eu.gricom.basic.helper.ConsoleInput;
import eu.gricom.basic.memoryManager.VariableManagement;
import eu.gricom.basic.variableTypes.RealValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * InputStatementTest.java
 * <p>
 * Description: Tests INPUT reading from standard input. Two INPUT statements in a row must both get their data (bug
 * fixed in Phase 6: each INPUT had its own reader, and the first reader kept the second line in its buffer).
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class InputStatementTest {

    private final InputStream _oOriginalIn = System.in;

    @AfterEach
    public void restoreInput() {
        System.setIn(_oOriginalIn);
    }

    @Test
    public void testTwoInputStatementsReadTwoLines() throws Exception {
        System.setIn(new ByteArrayInputStream("3\n7\n".getBytes()));

        new InputStatement("A").execute();
        new InputStatement("B").execute();

        VariableManagement oVariables = new VariableManagement();
        assertEquals(3.0, ((RealValue) oVariables.getMap("A")).toReal(), 0.0);
        assertEquals(7.0, ((RealValue) oVariables.getMap("B")).toReal(), 0.0);
    }

    @Test
    public void testInputWithoutMoreDataIsReported() {
        System.setIn(new ByteArrayInputStream("".getBytes()));
        ConsoleInput.getReader();

        assertThrows(Exception.class, () -> new InputStatement("C").execute());
    }
}
