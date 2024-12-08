package org.example.myjavafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class ButtonCell extends TableCell<Book, Void> {
    private final HBox buttonsContainer = new HBox(10);
    private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");

    private LibrarySystem librarySystem;

    public ButtonCell(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.getChildren().addAll(editButton, deleteButton);

        // Directly handle the action event of the edit button
        this.editButton.setOnAction(event -> {
            Book book = getTableView().getItems().get(getIndex());
            handleEditAction(book);
        });

        deleteButton.setOnAction(event -> {
            Book book = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(book);
            // Optionally, show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book Removed");
            alert.setHeaderText(null);
            alert.setContentText("The book has been successfully removed.");
            alert.showAndWait();
            this.librarySystem.deleteBook(book.getIsbn()); // Assuming you have a method to remove a book
        });
    }

    // Method to handle edit action
    private void handleEditAction(Book book) {
        Book updatedBook = showEditBookDialog(book); // Show the dialog to edit book details
        if (updatedBook != null) {
            int index = getTableView().getItems().indexOf(book);
            if (index != -1) {
                getTableView().getItems().set(index, updatedBook); // Update the list
                librarySystem.modifyBook(book.getIsbn(), updatedBook); // Update the library system
                getTableView().refresh();
            }
        }
    }


    private Book showEditBookDialog(Book book) {
        // Create the custom dialog.
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");
        dialog.setHeaderText("Edit book details:");

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the book fields to edit
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField publisherField = new TextField(book.getPublisher());
        TextField isbnField = new TextField(book.getIsbn());
        isbnField.setEditable(false); // ISBN should not be editable
        TextField yearField = new TextField(String.valueOf(book.getYear()));
        TextField categoryField = new TextField(book.getCategory());
        TextField copiesField = new TextField(String.valueOf(book.getCopies()));

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Publisher:"), 0, 2);
        grid.add(publisherField, 1, 2);
        grid.add(new Label("ISBN:"), 0, 3);
        grid.add(isbnField, 1, 3);
        grid.add(new Label("Year:"), 0, 4);
        grid.add(yearField, 1, 4);
        grid.add(new Label("Category:"), 0, 5);
        grid.add(categoryField, 1, 5);
        grid.add(new Label("Copies:"), 0, 6);
        grid.add(copiesField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a book when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    System.out.println("OH");
                    return new Book(titleField.getText(), authorField.getText(), publisherField.getText(), book.getIsbn(),
                            Integer.parseInt(yearField.getText()), categoryField.getText(), Integer.parseInt(copiesField.getText()));
                } catch (NumberFormatException e) {
                    // Handle number format exception for year and copies
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Year and Copies must be valid integers.");
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        System.out.println("YES");
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
