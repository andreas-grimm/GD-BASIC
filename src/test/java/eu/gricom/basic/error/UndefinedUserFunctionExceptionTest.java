package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UndefinedUserFunctionExceptionTest.java
 * <p>
 * Unit tests for the UndefinedUserFunctionException class.
 * <p>
 * This test class provides comprehensive coverage of the UndefinedUserFunctionException, which is thrown
 * when a user-defined function is called but has not been defined in the program. This exception helps
 * catch function definition errors and missing function implementations.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class UndefinedUserFunctionExceptionTest {

    /**
     * Test: UndefinedUserFunctionException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        UndefinedUserFunctionException oException = new UndefinedUserFunctionException("Function not defined");
        assertNotNull(oException);
    }

    /**
     * Test: UndefinedUserFunctionException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "Function FOO is not defined";
        UndefinedUserFunctionException oException = new UndefinedUserFunctionException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: UndefinedUserFunctionException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        UndefinedUserFunctionException oException = new UndefinedUserFunctionException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: UndefinedUserFunctionException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(UndefinedUserFunctionException.class, () -> {
            throw new UndefinedUserFunctionException("Function MY_FUNC not found");
        });
    }

    /**
     * Test: UndefinedUserFunctionException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        UndefinedUserFunctionException oException1 = new UndefinedUserFunctionException("Error 1");
        UndefinedUserFunctionException oException2 = new UndefinedUserFunctionException("Error 2");
        UndefinedUserFunctionException oException3 = new UndefinedUserFunctionException("Error 3");

        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: UndefinedUserFunctionException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new UndefinedUserFunctionException("Test");
        });
    }

    /**
     * Test: UndefinedUserFunctionException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        UndefinedUserFunctionException oException = new UndefinedUserFunctionException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
