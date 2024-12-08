package org.example.myjavafx;//package com.example.org.example.myjavafx.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String name;
    private List<Book> books;

    public Category(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books); // Return a copy of the list
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    // Methods to manage books
    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        }
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    // Additional methods can be added as needed
}

