package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CallTest.java
 *
 * Description: Unit tests for the Call class.
 * Since Call.execute performs real HTTP requests, we will test validation
 * and error handling with invalid/non-existent URLs.
 * In a full environment, a mock server could be used.
 */
public class CallTest {

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
    public void testExecute_With404Response_ThrowsRuntimeException() {
        // Using a known URL that returns 404
        Value oURL = new StringValue("https://httpbin.org/status/404");
        Value oPayload = new StringValue("{}");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Call.execute(oURL, oPayload);
        });
        assertTrue(exception.getMessage().contains("status code: 404"));
    }

    @Test
    public void testExecute_With200Response_ReturnsPayload() throws Exception {
        // Using httpbin.org to test a successful POST request
        Value oURL = new StringValue("https://httpbin.org/post");
        String payloadJson = "{\"test\":\"value\"}";
        Value oPayload = new StringValue(payloadJson);

        Value result = Call.execute(oURL, oPayload);

        assertNotNull(result);
        assertTrue(result instanceof StringValue);
        // httpbin.org/post returns the JSON with "data" field containing the payload
        // The payload string might be escaped in the response, let's check for the key/value
        String responseBody = result.toString();
        // [DEBUG_LOG] Response body: [value here]
        assertTrue(responseBody.contains("test") && responseBody.contains("value"), 
                "Response should contain the payload data. Response: " + responseBody);
    }
}
