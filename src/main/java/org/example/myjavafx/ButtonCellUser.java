package org.example.myjavafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class ButtonCellUser extends TableCell<User, Void> {
    private final HBox buttonsContainer = new HBox(10);
    private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");

    private LibrarySystem librarySystem;

    public ButtonCellUser(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.getChildren().addAll(editButton, deleteButton);

        // Directly handle the action event of the edit button
        this.editButton.setOnAction(event -> {
            User user = getTableView().getItems().get(getIndex());
            handleEditAction(user);
        });

        deleteButton.setOnAction(event -> {
            User user = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(user);
            // Optionally, show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Removed");
            alert.setHeaderText(null);
            alert.setContentText("The user has been successfully removed.");
            alert.showAndWait();
            this.librarySystem.deleteUser(user.getUsername()); // Assuming you have a method to remove a user
        });
    }

    // Method to handle edit action
    private void handleEditAction(User user) {
        User updatedUser = showEditUserDialog(user); // Show the dialog to edit user details
        if (updatedUser != null) {
            int index = getTableView().getItems().indexOf(user);
            if (index != -1) {
                getTableView().getItems().set(index, updatedUser); // Update the list
                librarySystem.modifyUser(user.getUsername(), updatedUser); // Update the library system
                getTableView().refresh();
            }
        }
    }


    private User showEditUserDialog(User user) {
        // Create the custom dialog.
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit user details:");

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the user fields to edit
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField(user.getUsername());
        TextField passwordField = new TextField(user.getPassword());
        TextField nameField = new TextField(user.getName());
        TextField surnameField = new TextField(user.getSurname());
        TextField idField = new TextField(String.valueOf(user.getId()));
        TextField emailField = new TextField(user.getEmail());

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Name:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("Surname:"), 0, 3);
        grid.add(surnameField, 1, 3);
        grid.add(new Label("Id:"), 0, 4);
        grid.add(idField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a user when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                    return new User(usernameField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(),
                             idField.getText(), emailField.getText());
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        return result.orElse(null);
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(buttonsContainer);
        }
    }
}

