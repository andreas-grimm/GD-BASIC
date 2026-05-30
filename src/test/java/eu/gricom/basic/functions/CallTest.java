package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * CallTest.java
 *
 * Description: Unit tests for the Call class.
 * Since Call.execute performs real HTTP requests, we will test validation
 * and error handling with invalid/non-existent URLs.
 * In a full environment, a mock server could be used.
 *
 * Note: Tests that make external HTTP requests include retry logic to handle
 * transient network failures and service unavailability (e.g., 503 errors).
 */
public class CallTest {

    /**
     * Helper method to retry an operation up to 4 times with exponential backoff.
     * Used for tests making real HTTP requests to external services.
     * Retries on transient failures (503, timeouts) and network errors.
     */
    private <T> T retryWithBackoff(RetryableOperation<T> operation, String operationName) throws Exception {
        int maxRetries = 4;
        long initialDelayMs = 500;
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return operation.execute();
            } catch (Exception e) {
                lastException = e;
                String errorMsg = e.getMessage();

                // Check if this is a transient error worth retrying
                boolean isTransient = errorMsg != null && (
                    errorMsg.contains("503") ||           // Service Unavailable
                    errorMsg.contains("timeout") ||       // Network timeout
                    errorMsg.contains("Connection") ||    // Connection issues
                    errorMsg.contains("503 Service")      // Service temporarily unavailable
                );

                if (attempt == maxRetries || !isTransient) {
                    // If it's the last attempt or not a transient error, throw
                    if (!isTransient && attempt < maxRetries) {
                        throw e;  // Non-transient error, fail fast
                    }
                    throw new Exception("Failed after " + maxRetries + " attempts on " + operationName + ": " + errorMsg, e);
                }

                // Wait before retrying with exponential backoff
                long delayMs = initialDelayMs * (long) Math.pow(2, attempt - 1);
                Thread.sleep(delayMs);
            }
        }

        throw new Exception("Retry exhausted for " + operationName, lastException);
    }

    /**
     * Functional interface for retry-able operations.
     */
    @FunctionalInterface
    private interface RetryableOperation<T> {
        T execute() throws Exception;
    }

    @Test
    public void testExecute_WithNonStringURL_ThrowsRuntimeException() {
        Value oURL = new eu.gricom.basic.variableTypes.IntegerValue(123);
        Value oPayload = new StringValue("{}");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Call.execute(oURL, oPayload);
        });
        assertEquals("URL of CALL directed not of type STRING", exception.getMessage());
    }

    @Test
    public void testExecute_WithNonStringPayload_ThrowsRuntimeException() {
        Value oURL = new StringValue("http://localhost");
        Value oPayload = new eu.gricom.basic.variableTypes.IntegerValue(123);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Call.execute(oURL, oPayload);
        });
        assertEquals("Payload of CALL directed not of type STRING", exception.getMessage());
    }

    @Test
    public void testExecute_WithInvalidURL_ThrowsRuntimeException() {
        Value oURL = new StringValue("not-a-url");
        Value oPayload = new StringValue("{}");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Call.execute(oURL, oPayload);
        });
        assertTrue(exception.getMessage().contains("Invalid URL format") || exception.getMessage().contains("Error during API call"));
    }

    @Test
    public void testExecute_WithNonExistentDomain_ThrowsRuntimeException() {
        Value oURL = new StringValue("http://this-domain-should-not-exist-12345.com");
        Value oPayload = new StringValue("{}");

        assertThrows(RuntimeException.class, () -> {
            Call.execute(oURL, oPayload);
        });
    }

    @Test
    public void testExecute_With404Response_ThrowsRuntimeException() throws Exception {
        // Using a known URL that returns 404 with retry logic for transient failures
        // Skips test if external service is unavailable or network issues occur
        try {
            retryWithBackoff(() -> {
                Value oURL = new StringValue("https://httpbin.org/status/404");
                Value oPayload = new StringValue("{}");

                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    Call.execute(oURL, oPayload);
                });
                assertTrue(exception.getMessage().contains("status code: 404"),
                        "Expected 404 error, got: " + exception.getMessage());
                return null;
            }, "404 status code test");
        } catch (Exception e) {
            // Skip test if external service is unavailable or network issues occur
            String msg = e.getMessage();
            boolean isNetworkIssue = msg != null && (msg.contains("503") || msg.contains("timeout") ||
                    msg.contains("Connection") || msg.contains("Failed after 4 attempts"));
            assumeTrue(!isNetworkIssue, "httpbin.org service unavailable or network issues, skipping external API test");
            throw e;
        }
    }

    @Test
    public void testExecute_With200Response_ReturnsPayload() throws Exception {
        // Using httpbin.org to test a successful POST request with retry logic
        // Skips test if external service is unavailable or network issues occur
        try {
            retryWithBackoff(() -> {
                Value oURL = new StringValue("https://httpbin.org/post");
                String payloadJson = "{\"test\":\"value\"}";
                Value oPayload = new StringValue(payloadJson);

                Value result = Call.execute(oURL, oPayload);

                assertNotNull(result);
                assertTrue(result instanceof StringValue);
                // httpbin.org/post returns the JSON with "data" field containing the payload
                // The payload string might be escaped in the response, let's check for the key/value
                String responseBody = result.toString();
                // Response body contains test data
                assertTrue(responseBody.contains("test") || responseBody.contains("value"),
                        "Response should contain the payload data. Response: " + responseBody);
                return null;
            }, "successful POST request test");
        } catch (Exception e) {
            // Skip test if external service is unavailable or network issues occur
            String msg = e.getMessage();
            boolean isNetworkIssue = msg != null && (msg.contains("503") || msg.contains("timeout") ||
                    msg.contains("Connection") || msg.contains("Failed after 4 attempts"));
            assumeTrue(!isNetworkIssue, "httpbin.org service unavailable or network issues, skipping external API test");
            throw e;
        }
    }
}
