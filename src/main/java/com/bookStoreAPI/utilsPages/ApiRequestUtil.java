package com.bookStoreAPI.utilsPages; // Package declaration

import com.bookStoreAPI.utility.ApiConstants; // Import API constants
import io.restassured.response.Response; // Import RestAssured's Response class

import static io.restassured.RestAssured.given; // Import RestAssured's given() method

/**
 * ApiRequestUtil provides utility methods for sending HTTP requests with common setup.
 * Currently supports POST requests; can be extended for other HTTP methods.
 */
public class ApiRequestUtil {

    /**
     * Sends a POST request to the given endpoint with the provided request body.
     * Uses basePagesPOJO URI and content type from ApiConstants.
     *
     * @param body     The request payload (POJO or Map)
     * @param endpoint The API endpoint (e.g., "/signup" or "/login")
     * @return Response from the API
     */
    public static Response postRequest(Object body, String endpoint) {
        return given() // Start building the request
                .baseUri(ApiConstants.BASE_URI) // Set the basePagesPOJO URI
                .contentType(ApiConstants.CONTENT_TYPE) // Set the content type
                .body(body) // Set the request body (serialized to JSON)
                .when() // Indicate the request is ready to be sent
                .post(endpoint); // Send POST request to the endpoint
    }
}
