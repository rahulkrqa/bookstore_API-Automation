package com.bookstore.stepdefs; // Step definitions package

import com.bookStoreAPI.basePagesPOJO.UserPOJO; // UserPOJO model import
import com.bookStoreAPI.UserServices.Login; // Login service import
import com.bookStoreAPI.UserServices.SignUp; // SignUp service import
import com.bookStoreAPI.utilsPages.ExtentReportUtil; // ExtentReportUtil import
import com.bookStoreAPI.utilsPages.AllureReportUtil; // AllureReportUtil import
import io.cucumber.java.en.*; // Cucumber step annotations import
import io.qameta.allure.Step; // Allure Step annotation import
import io.restassured.path.json.JsonPath; // JSON parsing import
import io.restassured.response.Response; // Response class import

import java.util.Random; // For password generation
import java.util.UUID;   // For unique email/ID

import static org.junit.Assert.*; // JUnit assertions

public class AuthSteps { // Class for user authentication step definitions

    private Response response; // Stores API response
    private int uniqueId; // Unique user id for each scenario
    private String uniqueUsername; // Unique email for each scenario
    private String password; // Password for the user

    // Utility method: Generate a truly unique email using UUID
    private String generateRandomUsername() {
        return "user_" + UUID.randomUUID().toString().replace("-", "") + "@mail.com";
    }

    // Utility method: Generate a truly unique user ID using UUID hash
    private int generateRandomId() {
        return UUID.randomUUID().toString().hashCode();
    }

    // Utility method: Generate a random password
    private String generateRandomPassword() {
        return "Pwd@" + new Random().nextInt(99999);
    }

    // Step: Attempt login with a user that has not signed up yet
    @When("user tried to login with noSignUpUser credentials into book store system")
    @Step("Attempt login with a user that has not signed up")
    public void loginWithoutSignup() {
        UserPOJO userPOJO = new UserPOJO(generateRandomId(), generateRandomUsername(), generateRandomPassword());
        AllureReportUtil.logStep("Login attempt with non-existent userPOJO: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Login attempt with non-existent userPOJO: " + userPOJO.getEmail());
        response = Login.login(userPOJO);
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString());
    }

    // Step: Attempt login with missing password parameter
    @When("user tried to login with missingParam credentials into book store system")
    @Step("Attempt login with missing parameter credentials")
    public void user_tried_to_login_with_missing_param_credentials_into_book_store_system() {
        UserPOJO userPOJO = new UserPOJO(generateRandomId(), generateRandomUsername(), null);
        AllureReportUtil.logStep("Login attempt with missing password for: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Login attempt with missing password for: " + userPOJO.getEmail());
        response = Login.login(userPOJO);
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString());
    }

    // Step: Validate login response status and message
    @Then("verify the login response code is {int} and message contains {string}")
    @Step("Validate login response code and error message")
    public void validateLogin(int expectedCode, String expectedMsg) {
        int actualCode = response.getStatusCode();
        String body = response.getBody().asString();

        boolean is2xx = expectedCode /100 == 2;
        boolean is4xx5xx = expectedCode / 100 >=4;

        try {
            if (is2xx && actualCode == expectedCode) {
                String token = JsonPath.from(body).getString("access_token");
                assertNotNull("Expected token but was null", token);
                ExtentReportUtil.step("PASS", "[PASS] Token found in response: " + token);
                AllureReportUtil.logStatusStep("PASS", "Token found in response: " + token);
            } else if (is4xx5xx && actualCode == expectedCode) {
                String actualMsg = JsonPath.from(body).getString("detail");
                assertTrue("Message mismatch: Expected to contain '" + expectedMsg + "', but got: '" + actualMsg + "'", actualMsg.toLowerCase().contains(expectedMsg.toLowerCase()));
                ExtentReportUtil.step("PASS", "[PASS] Error message matched: " + actualMsg);
                AllureReportUtil.logStatusStep("PASS", "Error message matched: " + actualMsg);
            } else {
                ExtentReportUtil.step("FAIL", "Unexpected status. Expected: " + expectedCode + ", Actual: " + actualCode);
                AllureReportUtil.logStatusStep("FAIL", "Unexpected status. Expected: " + expectedCode + ", Actual: " + actualCode);
                fail("Unexpected response. Expected: " + expectedCode + ", Actual: " + actualCode + ", Body: " + body);
            }
        } catch (Exception e) {
            ExtentReportUtil.step("FAIL", "Exception: " + e.getMessage() + "\nExpected: " + expectedCode + ", Actual: " + actualCode + ", Body: " + body);
            AllureReportUtil.logStatusStep("FAIL", "Exception: " + e.getMessage());
            throw e;
        }
        AllureReportUtil.addTextAttachment("Login Validation Response", body);
    }

    // Step: Sign up with a unique email and password
    @When("user signs up with a unique email and password")
    @Step("UserPOJO sign up with unique credentials")
    public void userSignsUp() {
        uniqueId = generateRandomId();
        uniqueUsername = generateRandomUsername();
        password = generateRandomPassword();
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password);
        AllureReportUtil.logStep("Sign up attempt: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up attempt: " + userPOJO.getEmail());
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString());
    }

    // Step: Validate sign up response and message
    @Then("verify user signup response code is {int} and message contains {string}")
    @Step("Validate signup response code and message")
    public void validateSignup(int expectedCode, String expectedMsg) {
        int actualCode = response.getStatusCode();
        String body = response.getBody().asString();

        try {
            assertEquals(expectedCode, actualCode);
            assertTrue(body.toLowerCase().contains(expectedMsg.toLowerCase()));
            ExtentReportUtil.step("PASS", "Signup response validated: " + body);
            AllureReportUtil.logStatusStep("PASS", "Signup response validated: " + body);
        } catch (Exception e) {
            ExtentReportUtil.step("FAIL", "Signup validation failed: " + e.getMessage());
            AllureReportUtil.logStatusStep("FAIL", "Signup validation failed: " + e.getMessage());
            throw e;
        }
        AllureReportUtil.addTextAttachment("Signup Validation Response", body);
    }

    // Step: Sign up to the book store as the new user with email and password
    @Given("Sign up to the book store as the new user with email and password")
    @Step("Sign up to the book store as the new user with email and password")
    public void sign_up_to_the_book_store_as_the_new_user_with_email_and_password() {
        uniqueId = generateRandomId();
        uniqueUsername = generateRandomUsername();
        password = generateRandomPassword();
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password);
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Sign up as new userPOJO: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up as new userPOJO: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString());
    }

    // Step: Do the sign up with valid credentials
    @When("do the sign up with valid credentials")
    @Step("Do the sign up with valid credentials")
    public void do_the_sign_up_with_valid_credentials() {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password);
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Sign up with valid credentials: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up with valid credentials: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString());
    }

    // Step: Login with valid credentials
    @When("user tried to login with valid credentials into book store system")
    @Step("Login with valid credentials")
    public void user_tried_to_login_with_valid_credentials_into_book_store_system() {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password);
        response = Login.login(userPOJO);
        AllureReportUtil.logStep("Login with valid credentials: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Login with valid credentials: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString());
    }

    // Step: Prepare a unique user with email prefix and password
    @Given("I prepare a unique user with email prefix {string} and password {string}")
    @Step("Prepare a unique user with email prefix and password")
    public void i_prepare_a_unique_user_with_email_prefix_and_password(String prefix, String pwd) {
        uniqueId = generateRandomId();
        uniqueUsername = prefix + "_" + UUID.randomUUID().toString().replace("-", "") + "@test.com";
        password = pwd;
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password);
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Prepared userPOJO: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Prepared userPOJO: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response", response.getBody().asString());
    }

    // Step: Login using the same email prefix and password
    @When("user tries to login using the same email prefix {string} and password {string}")
    @Step("Login with specific email prefix and password")
    public void user_tries_to_login_using_the_same_email_prefix_and_password(String prefix, String pwd) {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, pwd);
        response = Login.login(userPOJO);
        AllureReportUtil.logStep("Login with: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Login with: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Login Request", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Login Response", response.getBody().asString());
    }

    // Step: Validate signup response code and message (used in scenario outline)
    @Then("validate signup response code is {int} and message contains {string}")
    @Step("Validate signup response code and message")
    public void validate_signup_response_code_is_and_message_contains(Integer expectedCode, String expectedMsg) {
        int actualCode = response.getStatusCode();
        String body = response.getBody().asString();
        assertEquals(expectedCode.intValue(), actualCode);
        assertTrue(body.toLowerCase().contains(expectedMsg.toLowerCase()));
        ExtentReportUtil.step("PASS", "Signup response validated: " + body);
        AllureReportUtil.logStatusStep("PASS", "Signup response validated: " + body);
        AllureReportUtil.addTextAttachment("Signup Validation Response", body);
    }

    // Step: Do the sign up with old credentials (for negative scenario)
    @When("do the sign up with old credentials")
    @Step("Do the sign up with old credentials")
    public void do_the_sign_up_with_old_credentials() {
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, password);
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
        String newPassword = generateRandomPassword();
        UserPOJO userPOJO = new UserPOJO(uniqueId, uniqueUsername, newPassword);
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
        int newId = generateRandomId();
        UserPOJO userPOJO = new UserPOJO(newId, uniqueUsername, pwd);
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
        String newEmail = prefix + "_" + UUID.randomUUID().toString().replace("-", "") + "@test.com";
        UserPOJO userPOJO = new UserPOJO(uniqueId, newEmail, pwd);
        response = SignUp.signUp(userPOJO);
        AllureReportUtil.logStep("Sign up with same ID, new email: " + userPOJO.getEmail());
        ExtentReportUtil.step("INFO", "Sign up with same ID, new email: " + userPOJO.getEmail());
        AllureReportUtil.addTextAttachment("Signup Request (same ID, new email)", userPOJO.toString());
        AllureReportUtil.addTextAttachment("Signup Response (same ID, new email)", response.getBody().asString());
    }
}
