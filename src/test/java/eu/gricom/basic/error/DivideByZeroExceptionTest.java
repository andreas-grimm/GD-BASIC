package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DivideByZeroExceptionTest.java
 * <p>
 * Unit tests for the DivideByZeroException class.
 * <p>
 * This test class provides comprehensive coverage of the DivideByZeroException, which is thrown
 * when a division operation encounters a zero divisor. The exception prevents undefined mathematical
 * behaviour and propagates to the main class for error handling and program termination.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DivideByZeroExceptionTest {

    /**
     * Test: DivideByZeroException can be instantiated with a message.
     * <p>
     * Given: An error message
     * When: DivideByZeroException is instantiated
     * Then: Exception is created successfully
     * <p>
     * Purpose: Verifies basic exception instantiation
     */
    @Test
    public void testInstantiateWithMessage() {
        // Execute: Create exception with message
        DivideByZeroException oException = new DivideByZeroException("Division by zero");

        // Verify: Exception is not null
        assertNotNull(oException);
    }

    /**
     * Test: DivideByZeroException stores and retrieves the error message.
     * <p>
     * Given: An error message
     * When: DivideByZeroException is created and getMessage() is called
     * Then: Returns the original message
     * <p>
     * Purpose: Verifies message preservation
     */
    @Test
    public void testRetrievesMessage() {
        // Setup: Create exception with specific message
        String strMessage = "Cannot divide by zero at line 100";
        DivideByZeroException oException = new DivideByZeroException(strMessage);

        // Execute and Verify: Message is preserved
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: DivideByZeroException is an instance of Exception.
     * <p>
     * Given: A DivideByZeroException instance
     * When: Checked with instanceof
     * Then: Returns true for Exception type
     * <p>
     * Purpose: Verifies proper exception hierarchy
     */
    @Test
    public void testIsInstanceOfException() {
        // Setup: Create exception
        DivideByZeroException oException = new DivideByZeroException("Test");

        // Verify: Is instance of Exception
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: DivideByZeroException can be thrown and caught.
     * <p>
     * Given: A method that throws DivideByZeroException
     * When: The exception is thrown and caught
     * Then: Exception is caught properly with correct message
     * <p>
     * Purpose: Verifies exception throwing and catching mechanism
     */
    @Test
    public void testCanBeThrownAndCaught() {
        // Execute and Verify: Exception can be thrown and caught
        assertThrows(DivideByZeroException.class, () -> {
            throw new DivideByZeroException("Test division by zero");
        });
    }

    /**
     * Test: DivideByZeroException with multiple instances and different messages.
     * <p>
     * Given: Multiple DivideByZeroException instances with different messages
     * When: Each exception is created
     * Then: Each stores its own unique message
     * <p>
     * Purpose: Verifies message independence across instances
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        // Setup: Create multiple exceptions
        DivideByZeroException oException1 = new DivideByZeroException("Error 1");
        DivideByZeroException oException2 = new DivideByZeroException("Error 2");
        DivideByZeroException oException3 = new DivideByZeroException("Error 3");

        // Verify: Each has its own message
        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: DivideByZeroException can be caught as generic Exception.
     * <p>
     * Given: A DivideByZeroException is thrown
     * When: Caught as generic Exception type
     * Then: Is caught successfully
     * <p>
     * Purpose: Verifies polymorphic exception handling
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        // Execute and Verify: Exception can be caught as generic Exception
        assertThrows(Exception.class, () -> {
            throw new DivideByZeroException("Test");
        });
    }

    /**
     * Test: DivideByZeroException toString() returns non-null value.
     * <p>
     * Given: A DivideByZeroException instance
     * When: toString() is called
     * Then: Returns a non-null string representation
     * <p>
     * Purpose: Verifies toString() works correctly
     */
    @Test
    public void testToStringReturnsNonNull() {
        // Setup: Create exception
        DivideByZeroException oException = new DivideByZeroException("Test message");

        // Execute and Verify: toString() returns non-null
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
