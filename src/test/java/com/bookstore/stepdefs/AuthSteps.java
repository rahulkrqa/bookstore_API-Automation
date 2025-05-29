package com.bookstore.stepdefs; // Package for step definitions

import com.bookStoreAPI.basePagesPOJO.UserPOJO; // Import UserPOJO model
import com.bookStoreAPI.UserServices.Login;
import com.bookStoreAPI.UserServices.SignUp; // Import SignUp
import com.bookStoreAPI.utilsPages.ExtentReportUtil; // Import ExtentReportUtil
import com.bookStoreAPI.utilsPages.AllureReportUtil; // Import AllureReportUtil
import io.cucumber.java.en.*; // Import Cucumber step annotations
import io.qameta.allure.Step; // Import Allure Step annotation
import io.restassured.path.json.JsonPath; // Import JsonPath for JSON parsing
import io.restassured.response.Response; // Import Response class

import java.util.*; // Import utility classes

import static org.junit.Assert.*; // Import assertions

public class AuthSteps { // Step definition class

    private Response response; // Holds the API response
    private int uniqueId; // Unique user id
    private String uniqueUsername; // Unique username/email
    private String password; // UserPOJO password

    // Step: Attempt login with a user that has not signed up yet
    @When("user tried to login with noSignUpUser credentials into book store system")
    @Step("Attempt login with a user that has not signed up")
    public void loginWithoutSignup() {
        UserPOJO userPOJO = new UserPOJO(generateRandomId(), generateRandomUsername(), generateRandomPassword()); // Create userPOJO with random values
        AllureReportUtil.logStep("Login attempt with non-existent userPOJO: " + userPOJO.getEmail()); // Allure logging
        ExtentReportUtil.step("INFO", "Login attempt with non-existent userPOJO: " + userPOJO.getEmail()); // Extent logging
        response = Login.login(userPOJO); // Perform login
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString()); // Attach request to Allure
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString()); // Attach response to Allure
    }

    // Step: Attempt login with missing password parameter
    @When("user tried to login with missingParam credentials into book store system")
    @Step("Attempt login with missing parameter credentials")
    public void user_tried_to_login_with_missing_param_credentials_into_book_store_system() {
        UserPOJO userPOJO = new UserPOJO(generateRandomId(), generateRandomUsername(), null); // Create userPOJO with missing password
        AllureReportUtil.logStep("Login attempt with missing password for: " + userPOJO.getEmail()); // Allure logging
        ExtentReportUtil.step("INFO", "Login attempt with missing password for: " + userPOJO.getEmail()); // Extent logging
        response = Login.login(userPOJO); // Perform login
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString()); // Attach request
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString()); // Attach response
    }

    // Step: Validate login response status and message
    @Then("verify the login response code is {int} and message contains {string}")
    @Step("Validate login response code and error message")
    public void validateLogin(int expectedCode, String expectedMsg) {
        int actualCode = response.getStatusCode(); // Get status code
        String body = response.getBody().asString(); // Get response body

        boolean is2xx = expectedCode / 100 == 2; // Check for2xx code
        boolean is4xx5xx = expectedCode /100 >= 4; // Check for 4xx/5xx code

        try {
            if (is2xx && actualCode == expectedCode) { // Positive scenario
                String token = JsonPath.from(body).getString("access_token"); // Extract token
                assertNotNull("Expected token but was null", token); // Assert token present
                ExtentReportUtil.step("PASS", "[PASS] Token found in response: " + token); // Extent reporting
                AllureReportUtil.logStatusStep("PASS", "Token found in response: " + token); // Allure reporting
            } else if (is4xx5xx && actualCode == expectedCode) { // Negative/error scenario
                String actualMsg = JsonPath.from(body).getString("detail"); // Extract error message
                assertTrue("Message mismatch: Expected to contain '" + expectedMsg + "', but got: '" + actualMsg + "'", actualMsg.toLowerCase().contains(expectedMsg.toLowerCase())); // Assert error message
                ExtentReportUtil.step("PASS", "[PASS] Error message matched: " + actualMsg); // Extent reporting
                AllureReportUtil.logStatusStep("PASS", "Error message matched: " + actualMsg); // Allure reporting
            } else { // Unexpected status
                ExtentReportUtil.step("FAIL", "Unexpected status. Expected: " + expectedCode + ", Actual: " + actualCode); // Extent reporting
                AllureReportUtil.logStatusStep("FAIL", "Unexpected status. Expected: " + expectedCode + ", Actual: " + actualCode); // Allure reporting
                fail("Unexpected response. Expected: " + expectedCode + ", Actual: " + actualCode + ", Body: " + body); // Fail test
            }
        } catch (Exception e) {
            ExtentReportUtil.step("FAIL", "Exception: " + e.getMessage() + "\nExpected: " + expectedCode + ", Actual: " + actualCode + ", Body: " + body); // Extent error
            AllureReportUtil.logStatusStep("FAIL", "Exception: " + e.getMessage()); // Allure error
            throw e; // Rethrow
        }
        AllureReportUtil.addTextAttachment("Login Validation Response", body); // Attach validation to Allure
    }

    // Step: Sign up with a unique email and password
    @When("user signs up with a unique email and password")
    @Step("UserPOJO sign up with unique credentials")
    public void userSignsUp() {
        uniqueId = generateRandomId(); // Generate unique ID
        uniqueUsername = generateRandomUsername(); // Generate unique email
        password = generateRandomPassword(); // Generate password
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password); // Create userPOJO
        AllureReportUtil.logStep("Sign up attempt: " + userPOJO.getEmail()); // Allure logging
        ExtentReportUtil.step("INFO", "Sign up attempt: " + userPOJO.getEmail()); // Extent logging
        response = SignUp.signUp(userPOJO); // Perform sign up
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString()); // Allure: attach signup request
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString()); // Allure: attach signup response
    }

    // Step: Validate sign up response and message
    @Then("verify user signup response code is {int} and message contains {string}")
    @Step("Validate signup response code and message")
    public void validateSignup(int expectedCode, String expectedMsg) {
        int actualCode = response.getStatusCode(); // Get status code
        String body = response.getBody().asString(); // Get response body

        try {
            assertEquals(expectedCode, actualCode); // Assert status code
            assertTrue(body.toLowerCase().contains(expectedMsg.toLowerCase())); // Assert message content
            ExtentReportUtil.step("PASS", "Signup response validated: " + body); // Extent pass
            AllureReportUtil.logStatusStep("PASS", "Signup response validated: " + body); // Allure pass
        } catch (Exception e) {
            ExtentReportUtil.step("FAIL", "Signup validation failed: " + e.getMessage()); // Extent fail
            AllureReportUtil.logStatusStep("FAIL", "Signup validation failed: " + e.getMessage()); // Allure fail
            throw e; // Rethrow
        }
        AllureReportUtil.addTextAttachment("Signup Validation Response", body); // Allure: attach validation
    }

    // Step: Sign up to the book store as the new user with email and password
    @Given("Sign up to the book store as the new user with email and password")
    @Step("Sign up to the book store as the new user with email and password")
    public void sign_up_to_the_book_store_as_the_new_user_with_email_and_password() {
        uniqueId = generateRandomId(); // Generate unique ID
        uniqueUsername = generateRandomUsername(); // Generate unique email
        password = generateRandomPassword(); // Generate password
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password); // Create userPOJO
        response = SignUp.signUp(userPOJO); // Sign up userPOJO
        AllureReportUtil.logStep("Sign up as new userPOJO: " + userPOJO.getEmail()); // Allure log
        ExtentReportUtil.step("INFO", "Sign up as new userPOJO: " + userPOJO.getEmail()); // Extent log
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString()); // Attach signup request
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString()); // Attach signup response
    }

    // Step: Do the sign up with valid credentials
    @When("do the sign up with valid credentials")
    @Step("Do the sign up with valid credentials")
    public void do_the_sign_up_with_valid_credentials() {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password); // Use stored credentials
        response = SignUp.signUp(userPOJO); // Sign up userPOJO
        AllureReportUtil.logStep("Sign up with valid credentials: " + userPOJO.getEmail()); // Allure log
        ExtentReportUtil.step("INFO", "Sign up with valid credentials: " + userPOJO.getEmail()); // Extent log
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString()); // Attach signup request
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString()); // Attach signup response
    }

    // Step: Login with valid credentials
    @When("user tried to login with valid credentials into book store system")
    @Step("Login with valid credentials")
    public void user_tried_to_login_with_valid_credentials_into_book_store_system() {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password); // Use stored credentials
        response = Login.login(userPOJO); // Login userPOJO
        AllureReportUtil.logStep("Login with valid credentials: " + userPOJO.getEmail()); // Allure log
        ExtentReportUtil.step("INFO", "Login with valid credentials: " + userPOJO.getEmail()); // Extent log
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString()); // Attach login request
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString()); // Attach login response
    }

    // Step: Prepare a unique user with email prefix and password
    @Given("I prepare a unique user with email prefix {string} and password {string}")
    @Step("Prepare a unique user with email prefix and password")
    public void i_prepare_a_unique_user_with_email_prefix_and_password(String prefix, String pwd) {
        uniqueId = generateRandomId(); // Generate unique ID
        uniqueUsername = prefix + "_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com"; // Custom email
        password = pwd; // Use provided password
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password); // Create userPOJO
        response = SignUp.signUp(userPOJO); // Sign up userPOJO
        AllureReportUtil.logStep("Prepared userPOJO: " + userPOJO.getEmail()); // Allure log
        ExtentReportUtil.step("INFO", "Prepared userPOJO: " + userPOJO.getEmail()); // Extent log
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString()); // Attach signup request
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString()); // Attach signup response
    }

    // Step: Login using the same email prefix and password
    @When("user tries to login using the same email prefix {string} and password {string}")
    @Step("Login with specific email prefix and password")
    public void user_tries_to_login_using_the_same_email_prefix_and_password(String prefix, String pwd) {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, pwd); // Use stored email and provided password
        response = Login.login(userPOJO); // Login userPOJO
        AllureReportUtil.logStep("Login with: " + userPOJO.getEmail()); // Allure log
        ExtentReportUtil.step("INFO", "Login with: " + userPOJO.getEmail()); // Extent log
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString()); // Attach login request
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString()); // Attach login response
    }

    // Step: Validate signup response code and message (used in scenario outline)
    @Then("validate signup response code is {int} and message contains {string}")
    @Step("Validate signup response code and message")
    public void validate_signup_response_code_is_and_message_contains(Integer expectedCode, String expectedMsg) {
        int actualCode = response.getStatusCode(); // Get status code
        String body = response.getBody().asString(); // Get response body
        assertEquals(expectedCode.intValue(), actualCode); // Assert status code
        assertTrue(body.toLowerCase().contains(expectedMsg.toLowerCase())); // Assert message
        ExtentReportUtil.step("PASS", "Signup response validated: " + body); // Extent pass
        AllureReportUtil.logStatusStep("PASS", "Signup response validated: " + body); // Allure pass
        AllureReportUtil.addTextAttachment("Signup Validation Response", body); // Attach validation to Allure
    }

    // Step: Do the sign up with old credentials (for negative scenario)
    @When("do the sign up with old credentials")
    @Step("Do the sign up with old credentials")
    public void do_the_sign_up_with_old_credentials() {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password); // Use the same credentials
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Sign up with old credentials: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up with old credentials: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request (old)", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response (old)", response.getBody().asString());
    }

    // Step: Do the sign up with newPasswordOnly credentials (for negative scenario)
    @When("do the sign up with newPasswordOnly credentials")
    @Step("Do the sign up with newPasswordOnly credentials")
    public void do_the_sign_up_with_newPasswordOnly_credentials() {
        String newPassword = generateRandomPassword(); // New password
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, newPassword); // Same email/ID, new password
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Sign up with new password only: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up with new password only: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request (newPasswordOnly)", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response (newPasswordOnly)", response.getBody().asString());
    }

    // Step: I sign up with the same email and a new ID using prefix (for duplicate email check)
    @When("I sign up with the same email and a new ID using prefix {string} and password {string}")
    @Step("Sign up with same email and new ID")
    public void i_sign_up_with_the_same_email_and_a_new_id_using_prefix_and_password(String prefix, String pwd) {
        int newId = generateRandomId(); // New unique ID
        UserPOJO userPOJO = new UserPOJO(newId, uniqueUsername, pwd); // Same email, new ID
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Sign up with same email, new ID: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up with same email, new ID: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request (same email, new ID)", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response (same email, new ID)", response.getBody().asString());
    }

    // Step: I sign up with the same ID and a new email using prefix (for duplicate ID check)
    @When("I sign up with the same ID and a new email using prefix {string} and password {string}")
    @Step("Sign up with same ID and new email")
    public void i_sign_up_with_the_same_id_and_a_new_email_using_prefix_and_password(String prefix, String pwd) {
        String newEmail = prefix + "_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        UserPOJO userPOJO = new UserPOJO(uniqueId, newEmail, pwd); // Same ID, new email
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Sign up with same ID, new email: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up with same ID, new email: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request (same ID, new email)", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response (same ID, new email)", response.getBody().asString());
    }

    // Utility: Generate random username/email
    private String generateRandomUsername() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com"; // Unique user email
    }

    // Utility: Generate random password
    private String generateRandomPassword() {
        return "Pwd@" + new Random().nextInt(99999); // Random password
    }

    // Utility: Generate random user ID
    private int generateRandomId() {
        return (int)(System.currentTimeMillis() % 100000); // Unique integer ID
    }
}
