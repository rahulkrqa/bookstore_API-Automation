package com.bookStoreAPI.utilsPages; // Package declaration

import com.bookStoreAPI.utility.ApiConstants; // Import API constants
import io.restassured.http.ContentType; // For JSON content type
import io.restassured.response.Response; // For HTTP responses

import static io.restassured.RestAssured.given; // Static import for given()

// Allure/Extent reporting


/**
 * RestUtil provides static helpers for making REST API calls (POST, GET, PUT, DELETE)
 * and logs each request and response to both Allure and Extent reports.
 */
public class RestUtil {

    // POST request with token and body
    public static Response post(String endpoint, Object body, String token) {
        AllureReportUtil.logStep("POST " + endpoint);
        AllureReportUtil.addTextAttachment("POST Request Body", body.toString());
        ExtentReportUtil.step("INFO", "POST " + endpoint + " | Body: " + body);

        Response response = given()
                .baseUri(ApiConstants.BASE_URI)
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(body)
                .when()
                .post(endpoint);

        AllureReportUtil.addTextAttachment("POST Response", response.getBody().asString());
        ExtentReportUtil.step("INFO", "POST Response: " + response.getBody().asString());

        return response;
    }

    // GET request with token
    public static Response get(String endpoint, String token) {
        AllureReportUtil.logStep("GET " + endpoint);
        ExtentReportUtil.step("INFO", "GET " + endpoint);

        Response response = given()
                .baseUri(ApiConstants.BASE_URI)
                .auth().oauth2(token)
                .when()
                .get(endpoint);

        AllureReportUtil.addTextAttachment("GET Response", response.getBody().asString());
        ExtentReportUtil.step("INFO", "GET Response: " + response.getBody().asString());

        return response;
    }

    // PUT request with token and body
    public static Response put(String endpoint, Object body, String token) {
        AllureReportUtil.logStep("PUT " + endpoint);
        AllureReportUtil.addTextAttachment("PUT Request Body", body.toString());
        ExtentReportUtil.step("INFO", "PUT " + endpoint + " | Body: " + body);

        Response response = given()
                .baseUri(ApiConstants.BASE_URI)
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(body)
                .when()
                .put(endpoint);

        AllureReportUtil.addTextAttachment("PUT Response", response.getBody().asString());
        ExtentReportUtil.step("INFO", "PUT Response: " + response.getBody().asString());

        return response;
    }

    // DELETE request with token
    public static Response delete(String endpoint, String token) {
        AllureReportUtil.logStep("DELETE " + endpoint);
        ExtentReportUtil.step("INFO", "DELETE " + endpoint);

        Response response = given()
                .baseUri(ApiConstants.BASE_URI)
                .auth().oauth2(token)
                .when()
                .delete(endpoint);

        AllureReportUtil.addTextAttachment("DELETE Response", response.getBody().asString());
        ExtentReportUtil.step("INFO", "DELETE Response: " + response.getBody().asString());

        return response;
    }
}
