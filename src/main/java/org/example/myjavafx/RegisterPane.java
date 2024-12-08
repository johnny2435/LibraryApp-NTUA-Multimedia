package org.example.myjavafx;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class RegisterPane {
    private VBox pane;
    private Label messageLabel; // Label for displaying success/error messages

    private LibrarySystem librarySystem;

    public RegisterPane(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;
        pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.setSpacing(8);

        messageLabel = new Label(); // Initialize the message label

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField idField = new TextField();
        TextField emailField = new TextField();

        gridPane.add(new Label("Username:"), 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(new Label("Password:"), 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(new Label("Name:"), 0, 2);
        gridPane.add(nameField, 1, 2);
        gridPane.add(new Label("Surname:"), 0, 3);
        gridPane.add(surnameField, 1, 3);
        gridPane.add(new Label("ID:"), 0, 4);
        gridPane.add(idField, 1, 4);
        gridPane.add(new Label("Email:"), 0, 5);
        gridPane.add(emailField, 1, 5);

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String surname = surnameField.getText();
            String id = idField.getText();
            String email = emailField.getText();

            if (isUnique(email, id)) {
                User newUser = new User(username, password, name, surname, id, email);
                librarySystem.addUser(newUser);

                // Success message
                messageLabel.setText("Registration successful!");
                messageLabel.setStyle("-fx-text-fill: green;"); // Set text color to green

                // Clear the fields after successful registration
                usernameField.clear();
                passwordField.clear();
                nameField.clear();
                surnameField.clear();
                idField.clear();
                emailField.clear();
            } else {
                // Error message
                messageLabel.setText("Error: Email or ID already in use.");
                messageLabel.setStyle("-fx-text-fill: red;"); // Set text color to red
            }
        });

        pane.getChildren().addAll(new Label("Register New User"), gridPane, registerButton, messageLabel);
    }

    private boolean isUnique(String email, String id) {
        List<User> existingUsers = librarySystem.getUsers();
        for (User user : existingUsers) {
            if (user.getEmail().equals(email) || user.getId().equals(id)) {
                return false; // Email or ID already exists
            }
        }
        return true; // Email and ID are unique
    }

    public VBox getPane() {
        return pane;
    }
}
