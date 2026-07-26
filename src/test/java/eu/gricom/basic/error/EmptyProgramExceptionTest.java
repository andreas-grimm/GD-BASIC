package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EmptyProgramExceptionTest.java
 * <p>
 * Unit tests for the EmptyProgramException class.
 * <p>
 * This test class provides comprehensive coverage of the EmptyProgramException, which is thrown
 * when an attempt is made to load a BASIC program from a file that exists but is empty.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class EmptyProgramExceptionTest {

    /**
     * Test: EmptyProgramException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        EmptyProgramException oException = new EmptyProgramException("Program file is empty");
        assertNotNull(oException);
    }

    /**
     * Test: EmptyProgramException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "Program file is empty: test.bas";
        EmptyProgramException oException = new EmptyProgramException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: EmptyProgramException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        EmptyProgramException oException = new EmptyProgramException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: EmptyProgramException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(EmptyProgramException.class, () -> {
            throw new EmptyProgramException("empty.bas");
        });
    }

    /**
     * Test: EmptyProgramException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        EmptyProgramException oException1 = new EmptyProgramException("File 1 is empty");
        EmptyProgramException oException2 = new EmptyProgramException("File 2 is empty");
        EmptyProgramException oException3 = new EmptyProgramException("File 3 is empty");

        assertEquals("File 1 is empty", oException1.getMessage());
        assertEquals("File 2 is empty", oException2.getMessage());
        assertEquals("File 3 is empty", oException3.getMessage());
    }

    /**
     * Test: EmptyProgramException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new EmptyProgramException("Empty file");
        });
    }

    /**
     * Test: EmptyProgramException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        EmptyProgramException oException = new EmptyProgramException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }

    /**
     * Test: EmptyProgramException with descriptive error messages.
     */
    @Test
    public void testWithDescriptiveMessages() {
        String[] astrMessages = {
            "Program file is empty: /home/user/program.bas",
            "Cannot load empty file",
            "The file program.bas contains no BASIC statements"
        };

        for (String strMessage : astrMessages) {
            EmptyProgramException oException = new EmptyProgramException(strMessage);
            assertEquals(strMessage, oException.getMessage());
        }
    }
}