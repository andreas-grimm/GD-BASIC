package eu.gricom.interpreter.basic.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileHandlerTest {

    @Test
    public void testReadFile() throws Exception {
        String strReadText = FileHandler.readFile("src/test/resources/testReadFile.txt");
        assertTrue(strReadText.matches("This text needs to be read correctly...\n"));
    }
}
