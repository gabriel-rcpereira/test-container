package com.grcp.testcontainer;

import com.grcp.demo.testcontainer.repository.CompanyRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

public class CompanyStep extends ApplicationTestContext {

    @Autowired
    private CompanyRepository companyRepository;

    private String api;
    private Response response;
    private ValidatableResponse validatableResponse;

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    @Given("an api {string}")
    public void the_url(String api) {
        this.api = api;
    }

    @When("client sends a get request")
    public void the_client_sends_get_request() {
        this.response = given().contentType(ContentType.JSON)
                .when()
                    .get(this.api);
    }

    @When("client sends a post request with body {string}")
    public void the_client_sends_post_request(String bodyFile) throws URISyntaxException, IOException {
        var url = getClass().getClassLoader().getResource(String.format("request/%s.json", bodyFile));
        var body = Files.readAllBytes(Paths.get(url.toURI()));

        this.response = given().contentType(ContentType.JSON)
                .when().body(body).post(this.api);
    }

    @Then("client receives status code of {int}")
    public void the_client_receives_status_code_of(int statusCode) {
        this.validatableResponse = this.response.then().statusCode(statusCode);
    }

    @And("client receives a response body equals to {string}")
    public void the_response_body_equals_to(String bodyFile) throws IOException, URISyntaxException {
        var url = getClass().getClassLoader().getResource(String.format("response/%s.json", bodyFile));
        var bytes = Files.readAllBytes(Paths.get(url.toURI()));
        this.validatableResponse.body(MyMatcher.equalToIgnoringWhiteSpaces(new String(bytes)));
    }

    @And("clean up company created")
    public void clean_up_company_created() {
        this.companyRepository.deleteAll();
    }

    static class MyMatcher extends TypeSafeMatcher<String> {

        private final String toBeStriped;

        public MyMatcher(String string) {
            this.toBeStriped = string;
        }

        @Override
        protected boolean matchesSafely(String s) {
            return this.strip(this.toBeStriped).equals(this.strip(s));
        }

        @Override
        public void describeTo(Description description) {

        }

        public static MyMatcher equalToIgnoringWhiteSpaces(String s) {
            return new MyMatcher(s);
        }

        private String strip(String s) {
            return s.replaceAll("\\r+|\\n+", "").replaceAll("\\s+", "");
        }
    }
}
