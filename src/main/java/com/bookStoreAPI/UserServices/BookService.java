package com.bookStoreAPI.UserServices; // Package declaration

import com.bookStoreAPI.basePagesPOJO.BookPojo; // Import BookPojo model
import com.bookStoreAPI.utility.ApiConstants; // Import API constants
import com.bookStoreAPI.utilsPages.RestUtil; // Import HTTP request utility
import io.restassured.response.Response; // Import RestAssured's Response class

/**
 * BookService handles all CRUD operations for BookPojo resources via API.
 * Each method corresponds to a RESTful endpoint.
 */
public class BookService {

    // Create a new book (POST /books/)
    public static Response createBook(BookPojo book, String token) {
        return RestUtil.post(ApiConstants.BOOKS, book, token); // Calls RestUtil for POST request
    }

    // Fetch all books (GET /books/)
    public static Response getAllBooks(String token) {
        return RestUtil.get(ApiConstants.BOOKS, token); // Calls RestUtil for GET request
    }

    // Fetch a book by its ID (GET /books/{id})
    public static Response getBookById(int id, String token) {
        return RestUtil.get(ApiConstants.BOOKS + id, token); // Calls RestUtil for GET request with ID
    }

    // Update a book by its ID (PUT /books/{id})
    public static Response updateBook(int id, BookPojo book, String token) {
        return RestUtil.put(ApiConstants.BOOKS + id, book, token); // Calls RestUtil for PUT request
    }

    // Delete a book by its ID (DELETE /books/{id})
    public static Response deleteBook(int id, String token) {
        return RestUtil.delete(ApiConstants.BOOKS + id, token); // Calls RestUtil for DELETE request
    }
}
