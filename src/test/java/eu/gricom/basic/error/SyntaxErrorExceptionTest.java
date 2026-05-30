package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SyntaxErrorExceptionTest.java
 * <p>
 * Unit tests for the SyntaxErrorException class.
 * <p>
 * This test class provides comprehensive coverage of the SyntaxErrorException, which is thrown
 * when the parser or interpreter encounters invalid BASIC syntax. This includes unrecognised
 * keywords, malformed expressions, and structural errors.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class SyntaxErrorExceptionTest {

    /**
     * Test: SyntaxErrorException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        SyntaxErrorException oException = new SyntaxErrorException("Syntax error");
        assertNotNull(oException);
    }

    /**
     * Test: SyntaxErrorException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "Invalid IF statement at line 50";
        SyntaxErrorException oException = new SyntaxErrorException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: SyntaxErrorException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        SyntaxErrorException oException = new SyntaxErrorException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: SyntaxErrorException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(SyntaxErrorException.class, () -> {
            throw new SyntaxErrorException("Unrecognized keyword");
        });
    }

    /**
     * Test: SyntaxErrorException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        SyntaxErrorException oException1 = new SyntaxErrorException("Error 1");
        SyntaxErrorException oException2 = new SyntaxErrorException("Error 2");
        SyntaxErrorException oException3 = new SyntaxErrorException("Error 3");

        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: SyntaxErrorException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new SyntaxErrorException("Test");
        });
    }

    /**
     * Test: SyntaxErrorException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        SyntaxErrorException oException = new SyntaxErrorException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
