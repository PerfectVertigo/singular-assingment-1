package steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.filter.log.RequestLoggingFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import models.requests.RegisterRequest;
import utils.Assertions;
import utils.PropertiesLoader;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RegisterSteps {
    private Response response;
    private String BASE_URL;
    private String lastRequestLog;
    private RegisterRequest requestPayload;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setBaseUrl() {
        BASE_URL = PropertiesLoader.getRequiredProperty("api.base.url");
    }

    @Given("the request payload:")
    public void theRequestPayload(RegisterRequest payload) {
        this.requestPayload = payload;
    }

    @When("I send a POST request to {string}")
    public void callRegisterEndpoint(String endpoint) {
        Map<String, Object> params = mapper.convertValue(
                requestPayload,
                new TypeReference<>() {}
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream captor = new PrintStream(baos, true, StandardCharsets.UTF_8);

        response = given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .queryParams(params)
                .filter(new RequestLoggingFilter(captor))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        lastRequestLog = baos.toString(StandardCharsets.UTF_8);
    }

    @Then("the response status code should be {int}")
    public void thenResponseCodeIs(int expectedStatus) {
        Assertions.verifyHttpStatus(lastRequestLog, response, expectedStatus);
    }

    @Then("the JSON response field {string} should be {string}")
    public void thenJsonFieldIs(String fieldName, String expectedValue) {
        Assertions.verifyJsonField(fieldName, lastRequestLog, response, expectedValue);
    }
}
