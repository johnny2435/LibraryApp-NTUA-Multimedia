package org.example.myjavafx;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class UserDashboardPane {
    private LibrarySystem librarySystem;
    private BorderPane pane;
    private String currentUsername;

    public BorderPane getPane() {
        return pane;
    }

    private void refreshLoansPane() {
        BorderPane loansPane = createLoansPane(); // Assumes createLoansPane is modified to build the pane dynamically
        pane.setCenter(loansPane); // Assuming 'pane' is the main BorderPane of your UserDashboardPane
        TabPane tabPane = (TabPane) pane.getCenter(); // Assumes the TabPane is directly set in the center of 'pane'
        tabPane.getTabs().get(1).setContent(loansPane); // Replace '1' with the actual index of the "My Loans" tab
    }

    public UserDashboardPane(LibrarySystem librarySystem, String currentUsername, Stage primaryStage) {
        this.librarySystem = librarySystem;
        this.currentUsername = currentUsername;
        pane = new BorderPane();
        TabPane tabPane = new TabPane();

        // Tabs for different functionalities
        Tab searchTab = new Tab("Book Search", createBookSearchPane());
        Tab loansTab = new Tab("My Loans", createLoansPane());

        // Make tabs unclosable
        searchTab.setClosable(false);
        loansTab.setClosable(false);

        // Add tabs to tab pane
        tabPane.getTabs().addAll(searchTab, loansTab);
        pane.setCenter(tabPane);
        // Create a logout button
        Button logoutBtn = new Button("Logout");
        // Set action for logout button
        logoutBtn.setOnAction(event -> {
            Scene initialScene = BookApp.createInitialScene(primaryStage, librarySystem); // Adjust this call as needed
            primaryStage.setScene(initialScene);
        });

        // Create an HBox to hold the logout button and align it to the right
        HBox topRightButtons = new HBox(logoutBtn);
        topRightButtons.setAlignment(Pos.TOP_RIGHT);
        topRightButtons.setPadding(new Insets(10));
        pane.setTop(topRightButtons);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab == searchTab) {
                searchTab.setContent(createBookSearchPane()); // Refreshes the search tab
            } else if (newTab == loansTab) {
                loansTab.setContent(createLoansPane()); // Refreshes the loans tab
            }
        });

        // Set tab pane to the center of the border pane
        pane.setCenter(tabPane);
        this.pane = pane;
    }

    private BorderPane createBookSearchPane() {
        BorderPane searchPane = new BorderPane();
        //String currentUsername = "a";

        // Create a GridPane for form inputs
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20, 150, 10, 10));

        // Title field
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        form.add(new Label("Title:"), 0, 0);
        form.add(titleField, 1, 0);

        // Author field
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        form.add(new Label("Author:"), 0, 1);
        form.add(authorField, 1, 1);

        // Year field
        TextField yearField = new TextField();
        yearField.setPromptText("Year");
        form.add(new Label("Year:"), 0, 2);
        form.add(yearField, 1, 2);

        // Search button
        Button searchBtn = new Button("Search");
        form.add(searchBtn, 1, 3);
        GridPane.setHalignment(searchBtn, HPos.RIGHT);

        VBox resultsBox = new VBox();
        resultsBox.setPadding(new Insets(10));
        // Placeholder for search results
        resultsBox.getChildren().add(new Label("Search results will appear here..."));
        searchPane.setCenter(resultsBox);

        searchBtn.setOnAction(event -> {
            // Clear previous results
            resultsBox.getChildren().clear();

            // Convert year from String to Integer if possible
            Integer year = null;
            try {
                year = yearField.getText().isEmpty() ? null : Integer.parseInt(yearField.getText());
            } catch (NumberFormatException e) {
                resultsBox.getChildren().add(new Label("Invalid year format. Please enter a valid year or leave it blank."));
                return; // Exit the event handler if the year format is invalid
            }

            // Fetch books based on title, author, and year
            List<Book> books = librarySystem.searchBook(titleField.getText().isEmpty() ? null : titleField.getText(),
                    authorField.getText().isEmpty() ? null : authorField.getText(),
                    year);

            // Display the results
            if (books.isEmpty()) {
                resultsBox.getChildren().add(new Label("No books found matching the criteria."));
            } else {
                for (Book book : books) {
                    String bookDetails = String.format("%s by %s (Year: %d) - ISBN: %s, Avg. Rating: %.2f, Ratings: %d",
                            book.getTitle(),
                            book.getAuthor(),
                            book.getYear(),
                            book.getIsbn(),
                            book.getAverageRating(),
                            book.getTotalRatings());
                    Label bookLabel = new Label(bookDetails);

                    HBox bookEntry = new HBox(10);
                    bookEntry.setPadding(new Insets(5));

                    Button infoBtn = new Button("Info");
                    infoBtn.setOnAction(e -> {
                        // Create an Alert to show book information comprehensively
                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                        infoAlert.setTitle("Detailed Book Information");
                        infoAlert.setHeaderText(book.getTitle() + " by " + book.getAuthor());

                        // Building the book information string
                        String bookInfo = String.format(
                                "Title: %s\n" +
                                        "Author: %s\n" +
                                        "Publisher: %s\n" +
                                        "ISBN: %s\n" +
                                        "Year: %d\n" +
                                        "Category: %s\n" +
                                        "Copies: %d\n" +
                                        "Total Ratings: %d\n" +
                                        "Average Rating: %.2f\n" +
                                        "Comments: %s",
                                book.getTitle(),
                                book.getAuthor(),
                                book.getPublisher(),
                                book.getIsbn(),
                                book.getYear(),
                                book.getCategory(),
                                book.getCopies(),
                                book.getTotalRatings(),
                                book.getAverageRating(),
                                String.join(", ", book.getComments()) // Joining comments into a single string
                        );
                        infoAlert.setContentText(bookInfo);
                        infoAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); // Adjust the dialog size to ensure all content is visible
                        infoAlert.showAndWait();
                    });

                    //infoBtn.setOnAction(e -> showBookInfo(book));
                    /*
                    Button borrowBtn = new Button("Borrow");
                    borrowBtn.setOnAction(e -> {
                        if(librarySystem.createLoan(currentUsername, book.getIsbn())) {
                            refreshLoansPane(); // Refresh the loans pane after borrowing a book
                        }
                        else{
                            // Print a message when borrowing fails
                            resultsBox.getChildren().add(new Label("Failed to borrow the book."));

                        }
                    });
                    */
                    Button borrowBtn = new Button("Borrow");
                    borrowBtn.setOnAction(e -> {
                        boolean success = librarySystem.createLoan(currentUsername, book.getIsbn());
                        if(success) {
                            // Optionally, update UI elements that reflect the user's current borrowed books
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Book Borrowed Successfully");
                            alert.setHeaderText(null);
                            alert.setContentText("You have successfully borrowed the book.");
                            alert.showAndWait();
                        }
                        else {
                            // Show an error message directly in the results box or as an alert
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Borrowing Failed");
                            alert.setHeaderText(null);
                            alert.setContentText("Failed to borrow the book. Please try again.");
                            alert.showAndWait();
                        }
                    });


                    bookEntry.getChildren().addAll(bookLabel, infoBtn, borrowBtn);
                    resultsBox.getChildren().add(bookEntry);
                }
            }
        });

        // Add form to the searchPane
        searchPane.setTop(form);

        return searchPane;
    }


    private BorderPane createLoansPane() {
        BorderPane loansPane = new BorderPane();
        VBox loansList = new VBox(10);
        loansList.setPadding(new Insets(10));

        //String currentUsername = "a"; // Dynamically set this based on the logged-in user

        List<Loan> userLoans = librarySystem.getLoansByUser(currentUsername);

        if (userLoans.isEmpty()) {
            loansList.getChildren().add(new Label("No current loans."));
        } else {
            for (Loan loan : userLoans) {
                HBox loanEntry = new HBox(10);
                loanEntry.setPadding(new Insets(5));

                String loanDetails = String.format("Title: %s (ISBN: %s), Loan Date: %s,\n Due Date: %s",
                        loan.getBook().getTitle(),
                        loan.getBook().getIsbn(),
                        loan.getLoanDate().toString(),
                        loan.getReturnDate().toString()
                );

                Label loanLabel = new Label(loanDetails);
//                Button returnBtn = new Button("Return");

                TextField commentField = new TextField();
                commentField.setPromptText("Add a comment");

                ComboBox<Integer> ratingComboBox = new ComboBox<>();
                ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);
                ratingComboBox.setPromptText("Rate");

                Button submitCommentAndRating = new Button("Submit Feedback");
                submitCommentAndRating.setOnAction(e -> {
                    String comment = commentField.getText();
                    Integer rating = ratingComboBox.getValue();
                    if (comment != null && !comment.isEmpty() && rating != null) {
                        librarySystem.addCommentToBook(loan.getBook(), comment);
                        librarySystem.increaseBookRatings(loan.getBook(), rating);
                        // Optionally, refresh the loans pane or provide feedback to the user
                        // Display success message
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Feedback Submitted");
                        alert.setHeaderText(null);
                        alert.setContentText("Your feedback has been successfully submitted. Thank you!");
                        alert.showAndWait();

                        // Disable further submissions
                        commentField.setDisable(true);
                        ratingComboBox.setDisable(true);
                        submitCommentAndRating.setDisable(true);
                    }
                });



                loanEntry.getChildren().addAll(loanLabel, commentField, ratingComboBox, submitCommentAndRating);
                loansList.getChildren().add(loanEntry);
            }
        }

        loansPane.setCenter(loansList);

        return loansPane;
    }

}