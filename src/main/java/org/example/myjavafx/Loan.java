package org.example.myjavafx;//package com.example.org.example.myjavafx.library;

import org.example.myjavafx.Book;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

public class Loan implements Serializable {
    private User user;
    private Book book;
    private Date loanDate;
    private Date returnDate;

    // Constructor
    public Loan(User user, Book book, Date loanDate) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.returnDate = calculateReturnDate(loanDate);
    }

    // Method to calculate return date
    private Date calculateReturnDate(Date loanDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loanDate);
        calendar.add(Calendar.DAY_OF_MONTH, 5); // Adds 5 days to the loan date
        return calendar.getTime();
    }

    // Getters
    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
        this.returnDate = calculateReturnDate(loanDate);
    }

    // Other relevant methods can be added as needed
}
