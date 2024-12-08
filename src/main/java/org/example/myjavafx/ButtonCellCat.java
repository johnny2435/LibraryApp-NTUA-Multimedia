package org.example.myjavafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class ButtonCellCat extends TableCell<Category, Void> {
    private final HBox buttonsContainer = new HBox(10);
    private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");

    private LibrarySystem librarySystem;

    public ButtonCellCat(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.getChildren().addAll(editButton, deleteButton);

        // Directly handle the action event of the edit button
        this.editButton.setOnAction(event -> {
            Category category = getTableView().getItems().get(getIndex());
            handleEditAction(category);
        });

        deleteButton.setOnAction(event -> {
            Category category = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(category);
            // Optionally, show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Category Removed");
            alert.setHeaderText(null);
            alert.setContentText("The category has been successfully removed.");
            alert.showAndWait();
            this.librarySystem.deleteCategory(category.getName()); // Assuming you have a method to remove a book
        });
    }

    // Method to handle edit action
    private void handleEditAction(Category category) {
        Category updatedCategory = showEditCategoryDialog(category); // Show the dialog to edit book details
        if (updatedCategory != null) {
            int index = getTableView().getItems().indexOf(category);
            if (index != -1) {
                getTableView().getItems().set(index, updatedCategory); // Update the list
                librarySystem.modifyCategory(category.getName(), updatedCategory.getName()); // Update the library system
                getTableView().refresh();
            }
        }
    }


    private Category showEditCategoryDialog(Category category) {
        // Create the custom dialog.
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Edit Category");
        dialog.setHeaderText("Edit category details:");

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the book fields to edit
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(category.getName());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a book when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                    return new Category(nameField.getText());
            }
            return null;
        });

        Optional<Category> result = dialog.showAndWait();
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
