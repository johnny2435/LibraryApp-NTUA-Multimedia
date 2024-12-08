package org.example.myjavafx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int year;
    private String category;
    private int copies;
    private int totalRatings; // Total number of ratings
    private List<String> comments; // List of comments
    private int rating; // Sum of ratings

    // Constructor
    public Book(String title, String author, String publisher, String isbn, int year, String category, int copies) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.year = year;
        this.category = category;
        this.copies = copies;
        this.totalRatings = 0;
        this.comments = new ArrayList<>();
        this.rating = 0;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public String getCategory() {
        return category;
    }

    public int getCopies() {
        return copies;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public List<String> getComments() {
        return comments;
    }

    public int getRating() {
        return rating;
    }


    public double getAverageRating() {
        if (totalRatings==0)
            return 0;
        return (double) rating/totalRatings;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Additional utility methods can be added as needed
}