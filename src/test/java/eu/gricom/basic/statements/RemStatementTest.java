package eu.gricom.basic.statements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemStatementTest {
    @Test
    public void testExecute() {
        RemStatement oRemStatement = new RemStatement(5, "Test");

        oRemStatement.execute();

        assertTrue(true);
    }
}
