package eu.gricom.basic.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OutOfDataExceptionTest.java
 * <p>
 * Unit tests for the OutOfDataException class.
 * <p>
 * This test class provides comprehensive coverage of the OutOfDataException, which is thrown
 * when a READ statement or input operation encounters the end of input data before sufficient
 * data is available.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class OutOfDataExceptionTest {

    /**
     * Test: OutOfDataException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        OutOfDataException oException = new OutOfDataException("Out of data");
        assertNotNull(oException);
    }

    /**
     * Test: OutOfDataException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "No more data available to read";
        OutOfDataException oException = new OutOfDataException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: OutOfDataException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        OutOfDataException oException = new OutOfDataException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: OutOfDataException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(OutOfDataException.class, () -> {
            throw new OutOfDataException("Insufficient data");
        });
    }

    /**
     * Test: OutOfDataException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        OutOfDataException oException1 = new OutOfDataException("Error 1");
        OutOfDataException oException2 = new OutOfDataException("Error 2");
        OutOfDataException oException3 = new OutOfDataException("Error 3");

        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: OutOfDataException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new OutOfDataException("Test");
        });
    }

    /**
     * Test: OutOfDataException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        OutOfDataException oException = new OutOfDataException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
