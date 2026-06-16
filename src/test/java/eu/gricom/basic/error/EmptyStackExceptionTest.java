package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EmptyStackExceptionTest.java
 * <p>
 * Unit tests for the EmptyStackException class.
 * <p>
 * This test class provides comprehensive coverage of the EmptyStackException, which is thrown
 * when an attempt is made to pop or access elements from an empty stack, causing a stack underflow.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class EmptyStackExceptionTest {

    /**
     * Test: EmptyStackException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        EmptyStackException oException = new EmptyStackException("Stack is empty");
        assertNotNull(oException);
    }

    /**
     * Test: EmptyStackException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "Cannot pop from empty stack";
        EmptyStackException oException = new EmptyStackException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: EmptyStackException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        EmptyStackException oException = new EmptyStackException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: EmptyStackException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(EmptyStackException.class, () -> {
            throw new EmptyStackException("Stack underflow");
        });
    }

    /**
     * Test: EmptyStackException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        EmptyStackException oException1 = new EmptyStackException("Error 1");
        EmptyStackException oException2 = new EmptyStackException("Error 2");
        EmptyStackException oException3 = new EmptyStackException("Error 3");

        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: EmptyStackException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new EmptyStackException("Test");
        });
    }

    /**
     * Test: EmptyStackException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        EmptyStackException oException = new EmptyStackException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
