package com.bookStoreAPI.utility; // Package declaration

/**
 * ApiConstants holds all the constant values related to API endpoints,
 * common headers, and basePagesPOJO URIs used throughout the framework.
 */
public final class ApiConstants { // Final to avoid inheritance

    // The basePagesPOJO URI for the BookPojo Store API (can be externalized for different environments)
    public static final String BASE_URI = "http://127.0.0.1:8000";

    // The default Content-Type header for API requests
    public static final String CONTENT_TYPE = "application/json";

    // Endpoint for user signup
    public static final String SIGNUP_ENDPOINT = "/signup";

    // Endpoint for user login
    public static final String LOGIN_ENDPOINT = "/login";

    // Endpoint for books resource
    public static final String BOOKS = "/books/";

    // Private constructor to prevent instantiation
    private ApiConstants() {
        // Utility class: prevent instantiation
    }
}
