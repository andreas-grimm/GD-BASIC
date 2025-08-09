package eu.gricom.basic.memoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FileManagerTest {

    @Test
    public void testFileOpen() {
        FileManager oFileManager = new FileManager();
        /*

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
        */
    }

    @Test
    public void testFileClose() {
        FileManager oFileManager = new FileManager();
        /*

        assertThrows(EmptyStackException.class, () -> {
            oStack.pop();
        });
        */
    }
}
