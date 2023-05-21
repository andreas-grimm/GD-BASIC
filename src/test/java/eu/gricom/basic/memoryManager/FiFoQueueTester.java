package eu.gricom.basic.memoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.error.EmptyStackException;
import eu.gricom.basic.variableTypes.StringValue;
import org.junit.jupiter.api.Test;

public class FiFoQueueTester {

    @Test
    public void testFiFoQueue() {
        FiFoQueue oFiFo = new FiFoQueue();

        oFiFo.push(new StringValue("TestValue"));
        oFiFo.push(new IntegerValue(999));

        try {
            StringValue strResult = (StringValue) oFiFo.pop();
            assertTrue(strResult.toString().matches("TestValue"));

            IntegerValue oResult = (IntegerValue) oFiFo.pop();
            assertEquals(oResult.toInt(), 999);
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFiFoWithException() {
        FiFoQueue oFiFo = new FiFoQueue();

        oFiFo.reset();

        assertThrows(EmptyStackException.class, () -> {
            oFiFo.pop();
        });
    }
}
