package org.example.myjavafx;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class ButtonCellLoan extends TableCell<Loan, Void> {
    private final HBox buttonsContainer = new HBox(10);
    //private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");

    private LibrarySystem librarySystem;

    public ButtonCellLoan(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.getChildren().addAll(deleteButton);

        // Directly handle the action event of the edit button
       // this.editButton.setOnAction(event -> {
       //     Book book = getTableView().getItems().get(getIndex());
       //     handleEditAction(book);
        //});

        deleteButton.setOnAction(event -> {
            Loan loan = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(loan);
            // Optionally, show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Loan Removed");
            alert.setHeaderText(null);
            alert.setContentText("The loan has been successfully removed.");
            alert.showAndWait();
            this.librarySystem.deleteLoan(loan.getBook().getIsbn(), loan.getUser().getUsername()); // Assuming you have a method to remove a book
        });
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

