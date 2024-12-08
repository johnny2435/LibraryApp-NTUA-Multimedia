
package org.example.myjavafx;//package com.example.org.example.myjavafx.library;

import java.io.IOException;
import java.util.List;
public class library {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        LibrarySystem librarySystem = new LibrarySystem();
        // Initialize the system, load data, and provide an interface for user/admin actions

        // Example: Add a new book
        librarySystem.addCategory(new Category("tea"));
        librarySystem.addCategory(new Category("drama"));

        // Before exiting, save all changes
        // org.example.myjavafx.DataStorage.serializeObject(librarySystem.getBooks(), "books.ser");
        // Similar for other lists like users, categories, loans

        List<Category> allCategories = librarySystem.getAllCategories();

        // Print all categories
        System.out.println("All Categories:");
        for (Category category : allCategories) {
            System.out.println(category.getName());
        }

    }
}

