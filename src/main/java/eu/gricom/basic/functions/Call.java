package eu.gricom.basic.functions;

import eu.gricom.basic.error.RuntimeException;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Call.java
 * <p>
 * Description: The Call class implements the BASIC CALL function, which calls an external system API and returns the outcome
 * to the calling function.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class Call {

    /**
     * The standard timeout for the HTTP call in seconds.
     */
    private static final int TIMEOUT_SECONDS = 10;

    /**
     * Private Constructor.
     */
    private Call() {
    }

    /**
     * Performs an HTTP POST request to the specified URL with a payload.
     * This method implements the BASIC CALL function, facilitating integration
     * with external systems via their APIs.
     *
     * <p>The method uses the standard Java HttpClient to perform a POST request.
     * It expects both the URL and the payload to be provided as StringValue objects.
     *
     * <p>Validation:
     * <ul>
     *     <li>Checks if the provided URL is a StringValue.</li>
     *     <li>Checks if the provided payload is a StringValue.</li>
     *     <li>The URL must be a valid URI.</li>
     * </ul>
     *
     * <p>Execution Flow:
     * <ol>
     *     <li>Builds an HttpRequest with the POST method, including the payload in the body.</li>
     *     <li>Sets the "Content-Type" header to "application/json" by default.</li>
     *     <li>Sets a connection and request timeout of 10 seconds.</li>
     *     <li>Sends the request synchronously using a shared HttpClient instance.</li>
     *     <li>Receives the HttpResponse.</li>
     * </ol>
     *
     * <p>Error Handling:
     * <ul>
     *     <li>If the HTTP response status code is not in the 2xx range (e.g., 404, 500),
     *         a RuntimeException is thrown containing the status code.</li>
     *     <li>Standard Java networking exceptions are propagated up the call stack.</li>
     * </ul>
     *
     * @param oURL     formatted URL for the call to an external system: string as a source
     * @param oPayload payload string to send to the external system
     * @return Value the response body from the external system as a StringValue
     * @throws Exception if a validation error occurs, or if the API call fails or returns a non-2xx status code
     */
    public static Value execute(final Value oURL, final Value oPayload) throws Exception {
        // Validate inputs
        if (!(oURL instanceof StringValue)) {
            throw new RuntimeException("URL of CALL directed not of type STRING");
        }
        if (!(oPayload instanceof StringValue)) {
            throw new RuntimeException("Payload of CALL directed not of type STRING");
        }

        String strURL = oURL.toString();
        String strPayload = oPayload.toString();

        try {
            // Create the HTTP client
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .build();

            // Build the POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(strURL))
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(strPayload))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            // Check if status code is in the 2xx range (200-299)
            if (statusCode < 200 || statusCode >= 300) {
                throw new RuntimeException("API call failed with status code: " + statusCode + ". Response: " + response.body());
            }

            // Return the response body as a StringValue
            return new StringValue(response.body());

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid URL format: " + strURL);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("HTTP call was interrupted: " + e.getMessage());
        } catch (Exception e) {
            // Re-throw if it's already a RuntimeException, otherwise wrap it
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error during API call: " + e.getMessage());
        }
    }
}
