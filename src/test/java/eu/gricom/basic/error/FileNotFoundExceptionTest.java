package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FileNotFoundExceptionTest.java
 * <p>
 * Unit tests for the FileNotFoundException class.
 * <p>
 * This test class provides comprehensive coverage of the FileNotFoundException, which is thrown
 * when an attempt is made to access a file that is not registered with the FileManager or cannot
 * be accessed for any reason.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FileNotFoundExceptionTest {

    /**
     * Test: FileNotFoundException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        FileNotFoundException oException = new FileNotFoundException("File not found");
        assertNotNull(oException);
    }

    /**
     * Test: FileNotFoundException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "File with ID 123 not found";
        FileNotFoundException oException = new FileNotFoundException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: FileNotFoundException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        FileNotFoundException oException = new FileNotFoundException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: FileNotFoundException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(FileNotFoundException.class, () -> {
            throw new FileNotFoundException("test.txt not found");
        });
    }

    /**
     * Test: FileNotFoundException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        FileNotFoundException oException1 = new FileNotFoundException("Error 1");
        FileNotFoundException oException2 = new FileNotFoundException("Error 2");
        FileNotFoundException oException3 = new FileNotFoundException("Error 3");

        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: FileNotFoundException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new FileNotFoundException("Test");
        });
    }

    /**
     * Test: FileNotFoundException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        FileNotFoundException oException = new FileNotFoundException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
