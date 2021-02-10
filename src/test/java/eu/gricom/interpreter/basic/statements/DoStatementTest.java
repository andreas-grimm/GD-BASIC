package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.memoryManager.Stack;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoStatementTest {
    @Test
    public void testExecute() {
        Stack oStack = new Stack();

        DoStatement oDoStatement = new DoStatement(10);

        try {
            oDoStatement.execute();

            assertEquals(10, ((IntegerValue) oStack.pop()).toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
