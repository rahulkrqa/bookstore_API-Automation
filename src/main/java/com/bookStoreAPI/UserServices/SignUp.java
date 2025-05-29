package com.bookStoreAPI.UserServices; // Package declaration

import com.bookStoreAPI.basePagesPOJO.UserPOJO; // Import UserPOJO model class
import com.bookStoreAPI.utility.ApiConstants; // Import API endpoint constants
import com.bookStoreAPI.utilsPages.ApiRequestUtil; // Import utility for API requests
import io.restassured.response.Response; // Import RestAssured's Response class

/**
 * SignUp handles user registration (sign-up) via API.
 */
public class SignUp {

    // Registers a new userPOJO by sending a POST request to the signup endpoint, with the userPOJO object as payload.
    public static Response signUp(UserPOJO userPOJO) {
        return ApiRequestUtil.postRequest(userPOJO, ApiConstants.SIGNUP_ENDPOINT); // Calls utility to perform POST /signup
    }
}
