package com.bookstore.stepdefs;

import com.bookStoreAPI.basePagesPOJO.BookPojo;
import com.bookStoreAPI.basePagesPOJO.UserPOJO;
import com.bookStoreAPI.UserServices.BookService;
import com.bookStoreAPI.UserServices.Login;
import com.bookStoreAPI.UserServices.SignUp;
import com.bookStoreAPI.utilsPages.ExtentReportUtil;
import com.bookStoreAPI.utilsPages.AllureReportUtil;
import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static org.junit.Assert.*;

public class BookManagementSteps {

    private Response response; // Stores API response
    private BookPojo book; // Stores BookPojo object for request/response
    private int createdBookId; // Stores ID of created book
    private String accessToken; // Auth token
    private String email; // Email for user
    private String password; // Password for user

    @Given("a user signs up and logs in successfully")
    @Step("UserPOJO signs up and logs in successfully")
    public void signUpAndLogin() {
        email = "bookflow_user_" + System.currentTimeMillis() + "@mail.com";
        password = "BookPojo@123";
        UserPOJO userPOJO = new UserPOJO((int)(System.currentTimeMillis() % 100000), email, password);

        Response signUpResp = SignUp.signUp(userPOJO);
        assertNotNull("SignUp response is null", signUpResp);
        assertEquals(200, signUpResp.getStatusCode());
        ExtentReportUtil.logValidation("UserPOJO Signup",200, signUpResp.getStatusCode(), "UserPOJO created successfully", signUpResp.getBody().asString(), true);
        AllureReportUtil.logStep("UserPOJO signed up: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("SignUp Response", signUpResp.getBody().asString());

        Response loginResp = Login.login(userPOJO);
        assertNotNull("Login response is null", loginResp);
        assertEquals(200, loginResp.getStatusCode());
        accessToken = JsonPath.from(loginResp.getBody().asString()).getString("access_token");
        assertNotNull("Access token should not be null after login", accessToken);
        ExtentReportUtil.logValidation("UserPOJO Login", 200, loginResp.getStatusCode(), "Login successful", loginResp.getBody().asString(), true);
        AllureReportUtil.logStep("UserPOJO logged in: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Login Response", loginResp.getBody().asString());
    }

    @Given("a book payload with name {string}, author {string}, year {int}, and summary {string} is prepared")
    @Step("Prepare book payload: {0}, {1}, {2}, {3}")
    public void prepareBookPayload(String name, String author, int year, String summary) {
        book = new BookPojo(name, author, year, summary);
        ExtentReportUtil.step("INFO", "Prepared book payload: " + book);
        AllureReportUtil.logStep("Prepared book payload: " + book);
        AllureReportUtil.addTextAttachment("BookPojo Payload", book.toString());
    }

    @Given("a book payload missing name with id {int}, author {string}, year {int}, and summary {string} is prepared")
    @Step("Prepare book payload with missing name")
    public void prepareBookPayloadMissingName(int id, String author, int year, String summary) {
        // Simulate missing name field
        book = new BookPojo(null, author, year, summary);
        ExtentReportUtil.step("INFO", "Prepared book payload with missing name: " + book);
        AllureReportUtil.logStep("Prepared book payload with missing name: " + book);
        AllureReportUtil.addTextAttachment("BookPojo Payload Missing Name", book.toString());
    }

    @When("user sends a request to create a new book")
    @Step("Send request to create a new book")
    public void createBookRequest() {
        assertNotNull("BookPojo payload must be prepared before create request", book);
        assertNotNull("Access token must be available before create request", accessToken);
        response = BookService.createBook(book, accessToken);
        String body = response != null ? response.getBody().asString() : "";
        int status = response != null ? response.getStatusCode() : -1;
        ExtentReportUtil.step("INFO", "BookPojo creation request response:\nStatus: " + status + "\nBody: " + body);
        AllureReportUtil.logStep("BookPojo creation request sent.");
        AllureReportUtil.addTextAttachment("BookPojo Creation Response", body);
        if (status == 200 && body != null && !body.trim().isEmpty()) {
            try {
                createdBookId = JsonPath.from(body).getInt("id");
            } catch (Exception e) {
                createdBookId = -1;
            }
        }
    }

    // For "is200": Cucumber may see this as a separate step from {int}
    @Then("validate book creation response code is200 and response contains book details")
    @Step("Validate book creation response code is 200 and response contains book details")
    public void validate_book_creation_response_code_is200_and_response_contains_book_details() {
        validateCreateBookResponse(200);
    }

    @Then("validate book creation response code is {int} and response contains book details")
    @Step("Validate book creation response")
    public void validateCreateBookResponse(int expectedCode) {
        assertNotNull("No response from create book", response);
        int actualCode = response.getStatusCode();
        String body = response.getBody().asString();
        String name = null;
        try {
            name = JsonPath.from(body).getString("name");
        } catch (Exception e) {
            name = "";
        }
        boolean passed = actualCode == expectedCode && book != null && name.equals(book.getName());
        ExtentReportUtil.logValidation("BookPojo Creation", expectedCode, actualCode, book != null ? book.getName() : "", name, passed);
        AllureReportUtil.logStatusStep(passed ? "PASS" : "FAIL", "BookPojo creation: Expected code " + expectedCode + ", got " + actualCode + "; Name: " + name);
        AllureReportUtil.addTextAttachment("BookPojo Creation Validation Response", body);
        assertEquals(expectedCode, actualCode);
        if (book != null) assertEquals(book.getName(), name);
    }

    @Then("validate book creation fails with code {int} and error message contains {string}")
    @Step("Validate book creation failure response")
    public void validateBookCreationFail(int expectedCode, String expectedError) {
        assertNotNull("No response for book creation failure", response);
        int actualCode = response.getStatusCode();
        String body = response.getBody().asString();
        boolean messageFound = body != null && body.toLowerCase().contains(expectedError.toLowerCase());
        ExtentReportUtil.logValidation("BookPojo Creation Fail", expectedCode, actualCode, expectedError, body, actualCode == expectedCode && messageFound);
        AllureReportUtil.logStatusStep((actualCode == expectedCode && messageFound) ? "PASS" : "FAIL",
                "BookPojo creation fail: Expected code " + expectedCode + ", got " + actualCode + "; Expected error: " + expectedError);
        AllureReportUtil.addTextAttachment("BookPojo Creation Failure Response", body);
        assertEquals(expectedCode, actualCode);
        assertTrue("Error message not found in response", messageFound);
    }

    @When("user fetches all books")
    @Step("Fetch all books")
    public void user_fetches_all_books() {
        assertNotNull("Access token must be available before fetch all", accessToken);
        response = BookService.getAllBooks(accessToken);
        ExtentReportUtil.step("INFO", "Fetch all books Response: " + (response != null ? response.getBody().asString() : ""));
        AllureReportUtil.logStep("Fetched all books");
        AllureReportUtil.addTextAttachment("Fetch All Books Response", response != null ? response.getBody().asString() : "");
    }

    @Then("verify response code is {int} and list contains the book name {string}")
    @Step("Validate fetch all books response")
    public void verify_response_code_is_and_list_contains_the_book_name(int expectedCode, String expectedBookName) {
        assertNotNull("No response for fetch all books", response);
        int actualCode = response.getStatusCode();
        String body = response.getBody().asString();
        boolean containsBook = body != null && body.contains(expectedBookName);
        ExtentReportUtil.logValidation("Fetch All Books", expectedCode, actualCode, expectedBookName, body, actualCode == expectedCode && containsBook);
        AllureReportUtil.logStatusStep((actualCode == expectedCode && containsBook) ? "PASS" : "FAIL",
                "Fetch all books: Expected code " + expectedCode + ", got " + actualCode + "; Check for book name: " + expectedBookName);
        AllureReportUtil.addTextAttachment("Fetch All Books Validation Response", body);
        assertEquals(expectedCode, actualCode);
        assertTrue("BookPojo name not found in list", containsBook);
    }

    @When("user fetches book by invalid ID")
    @Step("Fetch book by invalid ID")
    public void user_fetches_book_by_invalid_id() {
        assertNotNull("Access token must be available before fetch by invalid ID", accessToken);
        response = BookService.getBookById(-1, accessToken);
        ExtentReportUtil.step("INFO", "Fetching book by invalid ID:-1 Response: " + (response != null ? response.getBody().asString() : ""));
        AllureReportUtil.logStep("Fetching book by invalid ID: -1");
        AllureReportUtil.addTextAttachment("Fetch BookPojo By Invalid ID Response", response != null ? response.getBody().asString() : "");
    }

    @Then("validate not found response with code {int} and message contains {string}")
    @Step("Validate fetch book by invalid ID response")
    public void validate_not_found_response_with_code_and_message_contains(int expectedCode, String expectedMsg) {
        assertNotNull("No response for fetch by invalid ID", response);
        int actualCode = response.getStatusCode();
        String body = response.getBody().asString();
        boolean containsMsg = body != null && body.toLowerCase().contains(expectedMsg.toLowerCase());
        ExtentReportUtil.logValidation("Fetch BookPojo by Invalid ID", expectedCode, actualCode, expectedMsg, body, actualCode == expectedCode && containsMsg);
        AllureReportUtil.logStatusStep((actualCode == expectedCode && containsMsg) ? "PASS" : "FAIL",
                "Fetch by invalid ID: Expected code " + expectedCode + ", got " + actualCode + "; Expected message: " + expectedMsg);
        AllureReportUtil.addTextAttachment("Fetch BookPojo By Invalid ID Validation Response", body);
        assertEquals(expectedCode, actualCode);
        assertTrue("Expected message not found", containsMsg);
    }

    @When("user fetches book by valid ID")
    @Step("Fetch book by valid ID")
    public void fetchBookByValidId() {
        assertTrue("BookPojo must be created before fetching by valid ID", createdBookId > 0);
        assertNotNull("Access token must be available before fetch by valid ID", accessToken);
        response = BookService.getBookById(createdBookId, accessToken);
        ExtentReportUtil.step("INFO", "Fetching book by valid ID: " + createdBookId + " Response Body: " + (response != null ? response.getBody().asString() : ""));
        AllureReportUtil.logStep("Fetching book by ID: " + createdBookId);
        AllureReportUtil.addTextAttachment("Fetch BookPojo By ID Response", response != null ? response.getBody().asString() : "");
    }

    @Then("verify single book fetch response code is {int} and book name is {string}")
    @Step("Validate single book fetch response")
    public void validateSingleBookResponse(int expectedCode, String expectedName) {
        assertNotNull("No response for fetch by valid ID", response);
        int actualCode = response.getStatusCode();
        String name = "";
        try {
            name = JsonPath.from(response.getBody().asString()).getString("name");
        } catch (Exception e) {
            name = "";
        }
        boolean passed = actualCode == expectedCode && expectedName.equals(name);
        ExtentReportUtil.logValidation("Fetch BookPojo by ID", expectedCode, actualCode, expectedName, name, passed);
        AllureReportUtil.logStatusStep(passed ? "PASS" : "FAIL", "Fetch by ID: Expected code " + expectedCode + ", got " + actualCode + "; Name: " + name);
        AllureReportUtil.addTextAttachment("Fetch BookPojo By ID Validation Response", response.getBody().asString());
        assertEquals(expectedCode, actualCode);
        assertEquals(expectedName, name);
    }
}
