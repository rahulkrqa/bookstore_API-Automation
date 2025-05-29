package com.bookStoreAPI.UserServices; // Package declaration

import com.bookStoreAPI.basePagesPOJO.UserPOJO; // Import UserPOJO model
import com.bookStoreAPI.utility.ApiConstants; // Import API constants
import com.bookStoreAPI.utilsPages.ApiRequestUtil; // Import utility for API requests
import io.restassured.response.Response; // Import RestAssured's Response class

/**
 * Login handles the login operation for users via API.
 */
public class Login {

    // Logs in a userPOJO by sending a POST request to the login endpoint, with the userPOJO object as payload.
    public static Response login(UserPOJO userPOJO) {
        return ApiRequestUtil.postRequest(userPOJO, ApiConstants.LOGIN_ENDPOINT); // Calls utility to perform POST /login
    }
}
