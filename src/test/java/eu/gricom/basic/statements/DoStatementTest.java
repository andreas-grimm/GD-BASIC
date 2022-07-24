package eu.gricom.basic.statements;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.memoryManager.Stack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoStatementTest {
    @Test
    public void testExecute() {
        Stack oStack = new Stack();

        DoStatement oDoStatement = new DoStatement(10);

        try {
            oDoStatement.execute();

            Assertions.assertEquals(10, ((IntegerValue) oStack.pop()).toInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
