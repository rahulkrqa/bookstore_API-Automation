package com.bookStoreAPI.basePagesPOJO; // Defines the package location

import java.util.Objects; // For equals/hashCode methods

// Represents a user of the bookstore system
public class UserPOJO {
    private int id; // Unique user ID
    private String email; // UserPOJO email (serves as username)
    private String password; // UserPOJO password

    // Default constructor (needed for frameworks/serialization)
    public UserPOJO() {} // No-args constructor

    // Main constructor to create a UserPOJO with id, email, and password
    public UserPOJO(int id, String email, String password) {
        this.id = id; // Set user id
        this.email = email; // Set email
        this.password = password; // Set password
    }

    // Getter for user ID
    public int getId() { return id; }

    // Setter for user ID
    public void setId(int id) { this.id = id; }

    // Getter for user email
    public String getEmail() { return email; }

    // Setter for user email
    public void setEmail(String email) { this.email = email; }

    // Getter for user password
    public String getPassword() { return password; }

    // Setter for user password
    public void setPassword(String password) { this.password = password; }

    // For debug and logging
    @Override
    public String toString() {
        return "UserPOJO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + "[PROTECTED]" + '\'' + // Don't print real password
                '}';
    }

    // For comparing users in collections or assertions (optional, but good for tests)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPOJO)) return false;
        UserPOJO userPOJO = (UserPOJO) o;
        return id == userPOJO.id &&
                Objects.equals(email, userPOJO.email) &&
                Objects.equals(password, userPOJO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
