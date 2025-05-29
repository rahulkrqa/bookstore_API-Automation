package com.bookStoreAPI.basePagesPOJO; // Package declaration

import java.util.Objects; // For equals/hashCode if needed

// BookPojo class represents a BookPojo entity in the application //
public class BookPojo {
    private int id; // BookPojo ID (unique identifier)
    private String name; // BookPojo name/title
    private String author; // BookPojo author
    private int published_year; // Year the book was published
    private String book_summary; // Short summary or description

    // Constructor for BookPojo (auto-generates ID if not set)
    public BookPojo(String name, String author, int published_year, String book_summary) {
        this.id = (int) (System.currentTimeMillis() % 1000); // Generate a pseudo-unique ID
        this.name = name;
        this.author = author;
        this.published_year = published_year;
        this.book_summary = book_summary;
    }

    // Optional constructor to allow explicit ID setting
    public BookPojo(int id, String name, String author, int published_year, String book_summary) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.published_year = published_year;
        this.book_summary = book_summary;
    }

    // Setter for book ID (needed for updates)
    public void setId(int id) { this.id = id; }

    // Getter for book ID
    public int getId() { return id; }

    // Getter for book name/title
    public String getName() { return name; }

    // Getter for book author
    public String getAuthor() { return author; }

    // Getter for published year
    public int getPublishedYear() { return published_year; }

    // Getter for book summary/description
    public String getBookSummary() { return book_summary; }

    // String representation for debugging/logging
    @Override
    public String toString() {
        return "BookPojo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", published_year=" + published_year +
                ", book_summary='" + book_summary + '\'' +
                '}';
    }

    // Equality based on all fields (optional but useful for testing)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookPojo)) return false;
        BookPojo book = (BookPojo) o;
        return id == book.id &&
                published_year == book.published_year &&
                Objects.equals(name, book.name) &&
                Objects.equals(author, book.author) &&
                Objects.equals(book_summary, book.book_summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, published_year, book_summary);
    }
}
