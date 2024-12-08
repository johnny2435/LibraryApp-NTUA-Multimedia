package org.example.myjavafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPane {
    private VBox pane;
    private LibrarySystem librarySystem;
    private Stage primaryStage; // Assume this is the primary stage of your application

    public LoginPane(LibrarySystem librarySystem, Stage primaryStage) {
        this.librarySystem = librarySystem; // Initialize with an instance of LibrarySystem
        this.primaryStage = primaryStage; // Initialize with the primary stage of your application
        pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.setSpacing(8);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        gridPane.add(new Label("Username:"), 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(new Label("Password:"), 0, 1);
        gridPane.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (validateLogin(username, password)) {
                // Successful login
                if(username.equals("medialab")) {
                    Platform.runLater(() -> {
                        // Redirect to User Dashboard
                        redirectToAdminDashboard();
                    });
                }
                else{
                Platform.runLater(() -> {
                    // Redirect to User Dashboard
                    redirectToUserDashboard(username);
                });
                }
            } else {
                // Login failed
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        });

        pane.getChildren().addAll(new Label("User Login"), gridPane, loginButton);
    }

    private boolean validateLogin(String username, String password) {
        return librarySystem.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    private void redirectToUserDashboard(String username) {
        UserDashboardPane userDashboardPane = new UserDashboardPane(librarySystem, username, primaryStage);
        Scene dashboardScene = new Scene(userDashboardPane.getPane(), 800, 600);

        primaryStage.setScene(dashboardScene);
        primaryStage.setTitle("User Dashboard");
        primaryStage.show();
    }

    private void redirectToAdminDashboard() {
        AdminDashboardPane adminDashboardPane = new AdminDashboardPane(librarySystem, primaryStage);
        Scene dashboardScene = new Scene(adminDashboardPane.getPane(), 800, 600);

        primaryStage.setScene(dashboardScene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getPane() {
        return pane;
    }
}


