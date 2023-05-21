package eu.gricom.basic.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileHandlerTest {

    @Test
    public void testReadFile() throws Exception {
        String strReadText = FileHandler.readFile("src/test/basic/testReadFile.txt");
        assertTrue(strReadText.matches("This text needs to be read correctly...\n"));
    }
}
