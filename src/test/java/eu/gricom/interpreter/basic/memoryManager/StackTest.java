package eu.gricom.interpreter.basic.memoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.gricom.interpreter.basic.error.EmptyStackException;
import eu.gricom.interpreter.basic.variableTypes.IntegerValue;
import eu.gricom.interpreter.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class StackTest {

    @Test
    public void testStack() {
        Stack oStack = new Stack();

        oStack.push(new StringValue("TestValue"));
        oStack.push(new IntegerValue(999));

        try {
            IntegerValue oResult = (IntegerValue) oStack.pop();
            assertEquals(oResult.toInt(), 999);

            StringValue strResult = (StringValue) oStack.pop();
            assertTrue(strResult.toString().matches("TestValue"));
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStackWithException() {
        Stack oStack = new Stack();

        assertThrows(EmptyStackException.class, () -> {
            oStack.pop();
        });
    }
}
