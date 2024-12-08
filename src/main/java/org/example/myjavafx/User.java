package org.example.myjavafx;//package com.example.org.example.myjavafx.library;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String id;
    private String email;

    public User(String username, String password, String name, String surname, String id, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.email = email;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password; // In real-world applications, do not return plain passwords
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password; // Consider encryption or hashing for password
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

