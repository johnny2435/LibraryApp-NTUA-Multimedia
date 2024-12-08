package org.example.myjavafx;//package com.example.org.example.myjavafx.library;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class LibrarySystem {
    private List<User> users;
    private static List<Book> books;
    private List<Category> categories;
    private List<Loan> loans;


    public LibrarySystem() throws ClassNotFoundException, IOException {
        // Initialize lists
        // Load data from files using org.example.myjavafx.DataStorage class
        loadData();
    }

    private void loadData() throws ClassNotFoundException, IOException {
        // Implement loading data from serialized files
        try {
            File directory = new File("medialab");
            if (!directory.exists()){
                directory.mkdirs(); // This will create the directory if it doesn't exist
            }

            // Check if .ser files exist
            File usersFile = new File("medialab/users.ser");
            File booksFile = new File("medialab/books.ser");
            File categoriesFile = new File("medialab/categories.ser");
            File loansFile = new File("medialab/loans.ser");

            if (!usersFile.exists() || !booksFile.exists() || !categoriesFile.exists() || !loansFile.exists()) {

                // If any of the files do not exist, initialize data structures with default data
                users = new ArrayList<>();
                books = new ArrayList<>();
                categories = new ArrayList<>();
                loans = new ArrayList<>();

                // Add default data to users, books, categories, and loans if needed

                // Serialize and write the data structures to .ser files
                serializeUsers();
                serializeBooks();
                serializeCategories();
                serializeLoans();
            } else {
                // If files exist, load data from .ser files
                this.users = (List<User>) DataStorage.deserializeObject("users.ser");
                this.books = (List<Book>) DataStorage.deserializeObject("books.ser");
                this.categories = (List<Category>) DataStorage.deserializeObject("categories.ser");
                this.loans = (List<Loan>) DataStorage.deserializeObject("loans.ser");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void addBook(Book book) {
        boolean categoryExists = categories.stream()
                .anyMatch(category -> category.getName().equals(book.getCategory()));

        if (!categoryExists) {
            System.out.println("The category of the book does not exist.");
            return; // Category does not exist, so the book cannot be added
        }
        for (Book existingBook : books) {
            if (existingBook.getIsbn().equals(book.getIsbn())) {
                // Found a user with matching username, ID, or email; do not add the new user
                System.out.println("A book with the same ISBN already exists.");
                return; // Exit the method early
            }
        }
        books.add(book);
        //serializeBooks();
    }

    public boolean addBookBoolean(Book book) {
        boolean categoryExists = categories.stream()
                .anyMatch(category -> category.getName().equals(book.getCategory()));

        if (!categoryExists) {
            System.out.println("The category of the book does not exist.");
            return false; // Category does not exist, so the book cannot be added
        }
        for (Book existingBook : books) {
            if (existingBook.getIsbn().equals(book.getIsbn())) {
                // Found a user with matching username, ID, or email; do not add the new user
                System.out.println("A book with the same ISBN already exists.");
                return false; // Exit the method early
            }
        }
        //serializeBooks();
        return true;
    }

    public void deleteBook(String isbn) {
        books.removeIf(book -> book.getIsbn().equals(isbn));
        loans.removeIf(loan -> loan.getBook().getIsbn().equals(isbn));
        //serializeBooks();
        //serializeLoans();
    }

    public void modifyBook(String isbn, Book newBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                books.set(i, newBook);
                break;
            }
        }
        //serializeBooks();
    }
/*
    public void addUser(User user) {
        users.add(user);
        serializeUsers();
    }


 */

    public void addUser(User user) {
        // Iterate over the existing users to check for duplicates
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername()) ||
                    existingUser.getId().equals(user.getId()) ||
                    existingUser.getEmail().equals(user.getEmail())) {
                // Found a user with matching username, ID, or email; do not add the new user
                System.out.println("A user with the same username, ID, or email already exists.");
                return; // Exit the method early
            }
        }

        // No matching user found; proceed to add the new user
        users.add(user);
        //serializeUsers();
    }

    public boolean addUserBoolean(User user) {
        // Iterate over the existing users to check for duplicates
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername()) ||
                    existingUser.getId().equals(user.getId()) ||
                    existingUser.getEmail().equals(user.getEmail())) {
                // Found a user with matching username, ID, or email; do not add the new user
                System.out.println("A user with the same username, ID, or email already exists.");
                return false; // Exit the method early
            }
        }
        return true;
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        loans.removeIf(loan -> loan.getUser().getUsername().equals(username));
        //serializeUsers();
        //serializeLoans();
    }

    public List<User> getUsers() {
        return users;
    }

    public void modifyUser(String username, User newUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                users.set(i, newUser);
                break;
            }
        }
        //serializeUsers();
    }

    public List<Book> getBooks() {
        return books;
    }

/*
    public void addCategory(Category category) {
        categories.add(category);
        serializeCategories();
    }


 */

    public void addCategory(Category newCategory) {
        // Check if a category with the same name already exists
        boolean categoryExists = false;
        for (Category existingCategory : categories) {
            if (existingCategory.getName().equals(newCategory.getName())) {
                categoryExists = true;
                break;
            }
        }

        // Only add the new category if it doesn't already exist
        if (!categoryExists) {
            categories.add(newCategory);
            //serializeCategories();
        }
    }

    public boolean addCategoryBoolean(Category newCategory) {
        for (Category existingCategory : categories) {
            if (existingCategory.getName().equals(newCategory.getName())) {
                return false; // Category exists, so do not add
            }
        }
        return true; // Category was added
    }


    public void deleteCategory(String categoryName) {
        categories.removeIf(category -> category.getName().equals(categoryName));
        books.removeIf(book -> book.getCategory().equals(categoryName));
        loans.removeIf(loan -> loan.getBook().getCategory().equals(categoryName));
        //serializeCategories();
        //serializeBooks();
        //serializeLoans();
    }

    public void modifyCategory(String oldName, String newName) {
        for (Category category : categories) {
            if (category.getName().equals(oldName)) {
                category.setName(newName);
                // Update the category name in books as well
                for (Book book : books) {
                    if (book.getCategory().equals(oldName)) {
                        book.setCategory(newName);
                    }
                }
                break;
            }
        }
        //serializeCategories();
        //serializeBooks();
    }
/*
    public void createLoan(String username, String isbn) {
        User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        Book book = books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);

        if (user != null && book != null && book.getCopies() > 0) {
            Loan loan = new Loan(user, book, new Date());
            loans.add(loan);
            book.setCopies(book.getCopies() - 1); // Decrement available copies
            serializeLoans();
            serializeBooks();
        }
    }

 */
public boolean createLoan(String username, String isbn) {
    User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    Book book = books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);

    // Check if the user already has two or more books borrowed
    long numberOfBooksBorrowed = loans.stream()
            .filter(loan -> loan.getUser().getUsername().equals(username))
            .count();

    if (numberOfBooksBorrowed >= 2) {
        System.out.println("This user has already borrowed two books and cannot borrow another one.");
        return false; // Return 0 to indicate failure
    }

    if (user != null && book != null && book.getCopies() > 0) {
        Loan loan = new Loan(user, book, new Date());
        loans.add(loan);
        book.setCopies(book.getCopies() - 1); // Decrement available copies
        //serializeLoans();
        //serializeBooks();
        return true; // Return 1 to indicate success
    }

    return false; // Return 0 to indicate failure if any condition is not met
}



    public void returnLoan(String isbn, String username, Integer rating, String comment) {
        for (Loan loan : loans) {
            if (loan.getBook().getIsbn().equals(isbn) && loan.getUser().getUsername().equals(username)) {
                Book returnedBook = loan.getBook();

                // Increment available copies
                returnedBook.setCopies(returnedBook.getCopies() + 1);

                // Add rating and comment if provided
                if (rating != null) {
                    returnedBook.setTotalRatings(returnedBook.getTotalRatings() + 1); // Increment total ratings
                    returnedBook.setRating(returnedBook.getRating() + rating); // Add rating to total
                }
                if (comment != null) {
                    returnedBook.getComments().add(comment);
                }

                // Remove the loan
                loans.remove(loan);

                // Serialize data
                //serializeLoans();
                //serializeBooks();

                break;
            }
        }
    }

    public void deleteLoan(String isbn, String username) {
        for (Loan loan : loans) {
            if (loan.getBook().getIsbn().equals(isbn) && loan.getUser().getUsername().equals(username)) {
                Book returnedBook = loan.getBook();

                // Increment available copies
                returnedBook.setCopies(returnedBook.getCopies() + 1);
                // Remove the loan
                loans.remove(loan);
                // Serialize data
                //serializeLoans();
                //serializeBooks();
                break;
            }
        }
    }

    public void checkAndDeleteExpiredLoans() {
        LocalDate today = LocalDate.now(); // Get today's date
        Iterator<Loan> iterator = loans.iterator(); // Use iterator to safely remove items within the loop

        while (iterator.hasNext()) {
            Loan loan = iterator.next();
            LocalDate loanDate = convertToLocalDateViaInstant(loan.getLoanDate()); // Assuming loan.getLoanDate() returns a Date object
            long daysBetween = ChronoUnit.DAYS.between(loanDate, today);

            if (daysBetween > 5) { // Check if more than 5 days have passed
                iterator.remove(); // Remove the expired loan
                // Optionally, update book copies if needed
                Book returnedBook = loan.getBook();
                returnedBook.setCopies(returnedBook.getCopies() + 1);
            }
        }

        // After removing expired loans, you may want to serialize the updated lists
        //serializeLoans();
        //serializeBooks(); // If you update book copies
    }

    // Helper method to convert java.util.Date to LocalDate
    private LocalDate convertToLocalDateViaInstant(java.util.Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }
    public List<Loan> getLoansByUser(String username) {
        // Check if the username is not null to avoid NullPointerException
        if (username == null) {
            return Collections.emptyList(); // Return an empty list if username is null
        }

        // Filter the list of loans to find those that belong to the specified user
        List<Loan> userLoans = loans.stream()
                .filter(loan -> username.equals(loan.getUser().getUsername()))
                .collect(Collectors.toList());

        // Return the list of loans for the user
        return userLoans;
    }

    public List<Loan> getLoans() {
        return loans;
    }


    public List<Book> searchBook(String title, String author, Integer year) {
        // Create a list to store matching books
        List<Book> matchingBooks = new ArrayList<>();

        // Iterate through the list of books
        for (Book book : books) {
            // Check if the book matches the given criteria
            if ((title == null || book.getTitle().equalsIgnoreCase(title)) &&
                    (author == null || book.getAuthor().equalsIgnoreCase(author)) &&
                    (year == null || book.getYear()==year)) {
                // If the book matches, add it to the list of matching books
                matchingBooks.add(book);
            }
        }

        // Return the list of matching books
        return matchingBooks;
    }

    private int getNumberOfBooksBorrowed(User user) {
        // Count the number of books borrowed by the user
        int count = 0;
        for (Loan loan : loans) {
            if (loan.getUser().equals(user)) {
                count++;
            }
        }
        return count;
    }

    public List<Book> getBooksByCategory(String categoryName) {
        // Check if the categoryName is not null to avoid NullPointerException
        if (categoryName == null) {
            return Collections.emptyList(); // Return an empty list if categoryName is null
        }

        // Filter the list of books to find those that belong to the specified category
        List<Book> booksInCategory = books.stream()
                .filter(book -> categoryName.equals(book.getCategory()))
                .collect(Collectors.toList());

        // Return the list of books in the specified category
        return booksInCategory;
    }


    public List<Book> getTopFiveBooks() {
        // Ensure there's a non-null and non-empty list of books
        if (books == null || books.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if no books are available
        }

        // Sort the books by average rating in descending order
        List<Book> sortedBooks = books.stream()
                .sorted(Comparator.comparingDouble(Book::getAverageRating).reversed())
                .collect(Collectors.toList());

        // Return the top 5 or fewer books
        return sortedBooks.stream().limit(5).collect(Collectors.toList());
    }

    // Function to add a comment to a book's comments
    public void addCommentToBook(Book book, String comment) {
        List<String> comments = book.getComments(); // Retrieve the current list of comments
        comments.add(comment); // Add the new comment
        book.setComments(comments); // Update the book's comments
        //serializeBooks();
    }


    // Function to increase the book's total ratings and increment the rating by one
    public void increaseBookRatings(Book book, int increaseBy) {
        book.setTotalRatings(book.getTotalRatings() + 1); // Increase total ratings by the specified amount
        book.setRating(book.getRating() + increaseBy); // Increment the rating by one
        //serializeBooks();
    }


    public List<Category> getAllCategories() {
        return categories;
    }

    // Serialization methods
    private void serializeBooks() {
        try {
            DataStorage.serializeObject(books, "books.ser");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception, possibly log it or notify the user
        }
    }

    private void serializeUsers() {
        try {
            DataStorage.serializeObject(users, "users.ser");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void serializeLoans() {
        try {
            DataStorage.serializeObject(loans, "loans.ser");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void serializeCategories() {
        try {
            DataStorage.serializeObject(categories, "categories.ser");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void serializeDataOnShutdown() throws IOException {
        serializeBooks();
        serializeUsers();
        serializeLoans();
        serializeCategories();
        // Add any additional serialization calls here
    }


}
