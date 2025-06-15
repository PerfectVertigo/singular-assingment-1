package utils;

import io.restassured.response.Response;
import static org.assertj.core.api.Assertions.assertThat;

public final class Assertions {
    private Assertions() {}

    /**
     * Verifies HTTP status code using AssertJ, embedding request/response in description.
     *
     * @param requestLog the log of the request
     * @param response the response object
     * @param expectedStatusCode the expected HTTP status code
     */
    public static void verifyHttpStatus(String requestLog, Response response, int expectedStatusCode) {
        int actualStatus = response.getStatusCode();
        String headers = response.getHeaders().toString();
        String body = response.getBody().asPrettyString();

        assertThat(actualStatus)
                .as("=== REQUEST ===%n%s%n%n=== RESPONSE ===%nHeaders: %s%n%s", requestLog, headers, body)
                .isEqualTo(expectedStatusCode);
    }

    /**
     * Verifies that a specific JSON field in the response matches the expected value.
     *
     * @param fieldName the name of the JSON field to verify
     * @param requestLog the log of the request
     * @param response the response object
     * @param expectedText the expected value for the JSON field
     */
    public static void verifyJsonField(String fieldName, String requestLog, Response response, String expectedText) {
        String body = response.getBody().asPrettyString();
        Object raw = response.jsonPath().get(fieldName);
        String actualText = raw == null ? "null" : String.valueOf(raw);

        assertThat(actualText)
                .as("JSON field '%s' did not match%n=== REQUEST ===%n%s%n%n=== RESPONSE BODY ===%n%s", fieldName, requestLog, body)
                .isEqualTo(expectedText);
    }
}
