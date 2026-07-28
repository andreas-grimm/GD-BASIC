package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FileAlreadyExistsExceptionTest.java
 * <p>
 * Unit tests for the FileAlreadyExistsException class.
 * <p>
 * This test class provides comprehensive coverage of the FileAlreadyExistsException, which is thrown
 * when an attempt is made to save a BASIC program to a file that already exists and would be overwritten.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FileAlreadyExistsExceptionTest {

    /**
     * Test: FileAlreadyExistsException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        FileAlreadyExistsException oException = new FileAlreadyExistsException("File already exists");
        assertNotNull(oException);
    }

    /**
     * Test: FileAlreadyExistsException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "File already exists: output.bas";
        FileAlreadyExistsException oException = new FileAlreadyExistsException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: FileAlreadyExistsException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        FileAlreadyExistsException oException = new FileAlreadyExistsException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: FileAlreadyExistsException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(FileAlreadyExistsException.class, () -> {
            throw new FileAlreadyExistsException("File conflict");
        });
    }

    /**
     * Test: FileAlreadyExistsException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        FileAlreadyExistsException oException1 = new FileAlreadyExistsException("File 1 exists");
        FileAlreadyExistsException oException2 = new FileAlreadyExistsException("File 2 exists");
        FileAlreadyExistsException oException3 = new FileAlreadyExistsException("File 3 exists");

        assertEquals("File 1 exists", oException1.getMessage());
        assertEquals("File 2 exists", oException2.getMessage());
        assertEquals("File 3 exists", oException3.getMessage());
    }

    /**
     * Test: FileAlreadyExistsException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new FileAlreadyExistsException("File exists");
        });
    }

    /**
     * Test: FileAlreadyExistsException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        FileAlreadyExistsException oException = new FileAlreadyExistsException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }

    /**
     * Test: FileAlreadyExistsException with descriptive error messages.
     */
    @Test
    public void testWithDescriptiveMessages() {
        String[] astrMessages = {
            "File already exists: /home/user/program.bas",
            "Cannot overwrite existing file",
            "The file output.bas already exists in the file system"
        };

        for (String strMessage : astrMessages) {
            FileAlreadyExistsException oException = new FileAlreadyExistsException(strMessage);
            assertEquals(strMessage, oException.getMessage());
        }
    }

    /**
     * Test: FileAlreadyExistsException for write operation protection.
     */
    @Test
    public void testPreventAccidentalOverwrite() {
        String strFilePath = "important_program.bas";
        String strErrorMsg = "File already exists: " + strFilePath;

        FileAlreadyExistsException oException = new FileAlreadyExistsException(strErrorMsg);
        assertTrue(oException.getMessage().contains(strFilePath));
    }
}