package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BasicRuntimeExceptionTest.java
 * <p>
 * Unit tests for the RuntimeException class.
 * <p>
 * This test class provides comprehensive coverage of the RuntimeException, which is thrown
 * when the interpreter encounters errors during program execution. This includes broken navigation
 * links between BASIC line numbers, invalid array index access, and other runtime errors.
 * <p>
 * Note: Class is named BasicRuntimeExceptionTest to avoid conflict with Java's built-in RuntimeException.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BasicRuntimeExceptionTest {

    /**
     * Test: RuntimeException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        RuntimeException oException = new RuntimeException("Runtime error");
        assertNotNull(oException);
    }

    /**
     * Test: RuntimeException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "Broken navigation link at line 500";
        RuntimeException oException = new RuntimeException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: RuntimeException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        RuntimeException oException = new RuntimeException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: RuntimeException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Program execution error");
        });
    }

    /**
     * Test: RuntimeException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        RuntimeException oException1 = new RuntimeException("Error 1");
        RuntimeException oException2 = new RuntimeException("Error 2");
        RuntimeException oException3 = new RuntimeException("Error 3");

        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: RuntimeException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new RuntimeException("Test");
        });
    }

    /**
     * Test: RuntimeException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        RuntimeException oException = new RuntimeException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
