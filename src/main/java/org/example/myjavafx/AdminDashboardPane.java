package org.example.myjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
// Other imports...

public class AdminDashboardPane {
    private LibrarySystem librarySystem;
    private BorderPane pane;

    private String currentUsername;

    private TableView<Book> bookTableView;

    private TableView<Category> categoryTableView;

    private TableView<User> userTableView;

    private TableView<User> loanTableView;


    public BorderPane getPane() {
        return pane;
    }


    public AdminDashboardPane(LibrarySystem librarySystem, Stage primaryStage) {
        this.librarySystem = librarySystem;
        pane = new BorderPane();
        TabPane tabPane = new TabPane();
        this.bookTableView = setupBookTableView();
        this.categoryTableView = setupCategoryTableView();
        this.userTableView = setupUserTableView();
        this.loanTableView = setupUserTableView();


        // Tabs for different administrative functionalities
        Tab bookManagementTab = new Tab("Book Management", createBookManagementPane());
        Tab categoryManagementTab = new Tab("Category Management", createCategoryManagementPane());
        Tab userManagementTab = new Tab("User Management", createUserManagementPane());
        Tab loanManagementTab = new Tab("Loan Management", createLoanManagementPane());

        // Make tabs unclosable
        bookManagementTab.setClosable(false);
        categoryManagementTab.setClosable(false);
        userManagementTab.setClosable(false);
        loanManagementTab.setClosable(false);

        // Add tabs to tab pane
        tabPane.getTabs().addAll(bookManagementTab, categoryManagementTab, userManagementTab, loanManagementTab);
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(event -> {
            Scene initialScene = BookApp.createInitialScene(primaryStage, librarySystem); // Adjust this call as needed
            primaryStage.setScene(initialScene);
        });

        // Create an HBox to hold the logout button and align it to the right
        HBox topRightButtons = new HBox(logoutBtn);
        topRightButtons.setAlignment(Pos.TOP_RIGHT);
        topRightButtons.setPadding(new Insets(10));
        pane.setTop(topRightButtons);
        // Set tab pane to the center of the border pane
        pane.setCenter(tabPane);
    }



    private BorderPane createBookManagementPane() {
        BorderPane pane = new BorderPane();

        // TextFields for all book details
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField publisherField = new TextField();
        publisherField.setPromptText("Publisher");
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        TextField yearField = new TextField();
        yearField.setPromptText("Year");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField copiesField = new TextField();
        copiesField.setPromptText("Copies");

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.setItems(FXCollections.observableArrayList(librarySystem.getAllCategories()));
        categoryComboBox.setPromptText("Filter by Category");

// Set the cell factory to display only the name property of the Category
        categoryComboBox.setCellFactory(lv -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty ? null : category.getName());
            }
        });

// Also set a custom converter to display the name of the Category in the ComboBox button cell when selected
        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category == null ? null : category.getName();
            }

            @Override
            public Category fromString(String string) {
                // This is not needed in your case because you are not converting back from a string to a Category
                return null;
            }
        });

        // Buttons for CRUD operations
        Button addButton = new Button("Add");
        //HBox buttonBar = new HBox(10, addButton);
        HBox buttonBar = new HBox(10, categoryComboBox);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 0, 10, 0));

       // HBox inputFields = new HBox(10, titleField, authorField, publisherField, isbnField, yearField, categoryField, copiesField, categoryComboBox);
        HBox inputFields = new HBox(10, titleField, authorField, publisherField, isbnField, yearField, categoryField, copiesField, addButton);
        inputFields.setAlignment(Pos.CENTER);
        inputFields.setPadding(new Insets(10, 0, 10, 0));

        // Setup TableView
        TableView<Book> bookTableView = setupBookTableView();
        bookTableView.setItems(FXCollections.observableArrayList()); // Initially empty

        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                List<Book> filteredBooks = librarySystem.getBooksByCategory(newSelection.getName());
                bookTableView.setItems(FXCollections.observableArrayList(filteredBooks));
            } else {
                bookTableView.setItems(FXCollections.observableArrayList(librarySystem.getBooks())); // Show all if no selection
            }
        });


        // Container for filter and add operations

        // Action Handlers
        addButton.setOnAction(e -> addBook(titleField, authorField, publisherField, isbnField, yearField, categoryField, copiesField, bookTableView));

        // Layout setup
        VBox topContainer = new VBox(10, inputFields, buttonBar);
        pane.setTop(topContainer);
        pane.setCenter(bookTableView);

        return pane;
    }


    private void addBook(TextField titleField, TextField authorField, TextField publisherField, TextField isbnField, TextField yearField, TextField categoryField, TextField copiesField, TableView<Book> bookTableView) {
        try {
            Book book = new Book(
                    titleField.getText(),
                    authorField.getText(),
                    publisherField.getText(),
                    isbnField.getText(),
                    Integer.parseInt(yearField.getText()),
                    categoryField.getText(),
                    Integer.parseInt(copiesField.getText())
            );

            boolean added = librarySystem.addBookBoolean(book);
            if (added) {
                librarySystem.addBook(book);
                bookTableView.getItems().add(book);
            } else {
                // Show error message.
                showAlert(Alert.AlertType.ERROR, "Add Book Error", "A book with the ISBN already exists or non existent category.");
            }
            clearTextFields(titleField, authorField, publisherField, isbnField, yearField, categoryField, copiesField);
        } catch (NumberFormatException e) {
            System.out.println("Year and Copies must be integers.");
        }
    }



    private TableView<Book> setupBookTableView() {
        TableView<Book> bookTableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> publisherColumn = new TableColumn<>("Publisher");
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Book, Integer> copiesColumn = new TableColumn<>("Copies");
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("copies"));

        // Add columns to the TableView
        bookTableView.getColumns().add(titleColumn);
        bookTableView.getColumns().add(authorColumn);
        bookTableView.getColumns().add(publisherColumn);
        bookTableView.getColumns().add(isbnColumn);
        bookTableView.getColumns().add(yearColumn);
        bookTableView.getColumns().add(categoryColumn);
        bookTableView.getColumns().add(copiesColumn);

        // Set the column resize policy to constrain the columns to the TableView's width
        bookTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Populate the TableView with books from the library system
        // This assumes you have a method in LibrarySystem to get all books as a List
        bookTableView.setItems(FXCollections.observableArrayList(librarySystem.getBooks()));




        //TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
        //actionCol.setCellFactory(param -> new ButtonCell(librarySystem)); // Simplified


        boolean actionsColumnExists = bookTableView.getColumns().stream()
                .anyMatch(column -> "Actions".equals(column.getText()));

        if (!actionsColumnExists) {
            TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
            actionCol.setCellFactory(param -> new ButtonCell(librarySystem));
            bookTableView.getColumns().add(actionCol);
        }

        bookTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bookTableView.setItems(FXCollections.observableArrayList(librarySystem.getBooks()));
        return bookTableView;
    }





    private void clearTextFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private BorderPane createCategoryManagementPane() {
        BorderPane pane = new BorderPane();

        // TextFields for all book details
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        HBox inputFields = new HBox(10, nameField);
        inputFields.setAlignment(Pos.CENTER);
        inputFields.setPadding(new Insets(10, 0, 10, 0));

        // Setup TableView
        TableView<Category> categoryTableView = setupCategoryTableView();

        // Buttons for CRUD operations
        Button addButton = new Button("Add");
        HBox buttonBar = new HBox(10, addButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 0, 10, 0));

        // Action Handlers
        addButton.setOnAction(e -> addCategory(nameField, categoryTableView));

        // Layout setup
        VBox topContainer = new VBox(10, inputFields, buttonBar);
        pane.setTop(topContainer);
        pane.setCenter(categoryTableView);

        return pane;
    }

/*
    private void addCategory(TextField nameField, TableView<Category> categoryTableView) {
        Category category = new Category(
                nameField.getText()
        );
        librarySystem.addCategory(category);
        categoryTableView.getItems().add(category);
        clearTextFields(nameField);
    }

 */

    private void addCategory(TextField nameField, TableView<Category> categoryTableView) {
        String categoryName = nameField.getText().trim();
        if (categoryName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Category name cannot be empty.");
            return;
        }

        Category category = new Category(categoryName);
        boolean added = librarySystem.addCategoryBoolean(category);
        if (added) {
            librarySystem.addCategory(category);
            categoryTableView.getItems().add(category);
        } else {
            // Show error message
            showAlert(Alert.AlertType.ERROR, "Add Category Error", "A category with the name \"" + categoryName + "\" already exists.");
        }
        clearTextFields(nameField);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header
        alert.setContentText(content);
        alert.showAndWait();
    }


    private TableView<Category> setupCategoryTableView() {
        TableView<Category> categoryTableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Category, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        // Add columns to the TableView
        categoryTableView.getColumns().add(nameColumn);


        // Set the column resize policy to constrain the columns to the TableView's width
        categoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Populate the TableView with books from the library system
        // This assumes you have a method in LibrarySystem to get all books as a List
        categoryTableView.setItems(FXCollections.observableArrayList(librarySystem.getAllCategories()));




        //TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
        //actionCol.setCellFactory(param -> new ButtonCell(librarySystem)); // Simplified


        boolean actionsColumnExists = categoryTableView.getColumns().stream()
                .anyMatch(column -> "Actions".equals(column.getText()));

        if (!actionsColumnExists) {
            TableColumn<Category, Void> actionCol = new TableColumn<>("Actions");
            actionCol.setCellFactory(param -> new ButtonCellCat(librarySystem));
            categoryTableView.getColumns().add(actionCol);
        }

        categoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        categoryTableView.setItems(FXCollections.observableArrayList(librarySystem.getAllCategories()));
        return categoryTableView;
    }




    private BorderPane createUserManagementPane() {
        BorderPane pane = new BorderPane();

        // TextFields for all book details
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField surnameField = new TextField();
        surnameField.setPromptText("Surname");
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        HBox inputFields = new HBox(10, usernameField, passwordField, nameField, surnameField, idField, emailField);
        inputFields.setAlignment(Pos.CENTER);
        inputFields.setPadding(new Insets(10, 0, 10, 0));

        // Setup TableView
        TableView<User> userTableView = setupUserTableView();

        // Buttons for CRUD operations
        Button addButton = new Button("Add");
        HBox buttonBar = new HBox(10, addButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 0, 10, 0));

        // Action Handlers
        addButton.setOnAction(e -> addUser(usernameField, passwordField, nameField, surnameField, idField, emailField, userTableView));

        // Layout setup
        VBox topContainer = new VBox(10, inputFields, buttonBar);
        pane.setTop(topContainer);
        pane.setCenter(userTableView);

        return pane;
    }


    private void addUser(TextField usernameField, TextField passwordField, TextField nameField, TextField surnameField, TextField idField, TextField emailField, TableView<User> userTableView) {
        if (usernameField == null || passwordField == null || nameField == null || idField == null || emailField == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Category name cannot be empty.");
            return;
        }
        User user = new User(
                usernameField.getText(),
                passwordField.getText(),
                nameField.getText(),
                surnameField.getText(),
                idField.getText(),
                emailField.getText()
                );

        boolean added = librarySystem.addUserBoolean(user);
        if (added) {
            librarySystem.addUser(user);
            userTableView.getItems().add(user);
        } else {
            // Show error message
            showAlert(Alert.AlertType.ERROR, "Add User Error", "A user with the some data already exists.");
        }

            clearTextFields(usernameField, passwordField, nameField, surnameField, idField, emailField);
    }





    private TableView<User> setupUserTableView() {
        TableView<User> userTableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<User, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));


        // Add columns to the TableView
        userTableView.getColumns().add(usernameColumn);
        userTableView.getColumns().add(passwordColumn);
        userTableView.getColumns().add(nameColumn);
        userTableView.getColumns().add(surnameColumn);
        userTableView.getColumns().add(idColumn);
        userTableView.getColumns().add(emailColumn);

        // Set the column resize policy to constrain the columns to the TableView's width
        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Populate the TableView with books from the library system
        // This assumes you have a method in LibrarySystem to get all books as a List
        userTableView.setItems(FXCollections.observableArrayList(librarySystem.getUsers()));




        //TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
        //actionCol.setCellFactory(param -> new ButtonCell(librarySystem)); // Simplified


        boolean actionsColumnExists = userTableView.getColumns().stream()
                .anyMatch(column -> "Actions".equals(column.getText()));

        if (!actionsColumnExists) {
            TableColumn<User, Void> actionCol = new TableColumn<>("Actions");
            actionCol.setCellFactory(param -> new ButtonCellUser(librarySystem));
            userTableView.getColumns().add(actionCol);
        }

        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTableView.setItems(FXCollections.observableArrayList(librarySystem.getUsers()));
        return userTableView;
    }

    private BorderPane createLoanManagementPane() {
        BorderPane pane = new BorderPane();

        // TextFields for all book details
        TextField userField = new TextField();
        userField.setPromptText("User");
        TextField bookField = new TextField();
        bookField.setPromptText("Book");
        TextField loanDateField = new TextField();
        loanDateField.setPromptText("Loan date");
        TextField returnDateField = new TextField();
        returnDateField.setPromptText("Return date");

        HBox inputFields = new HBox(10, userField, bookField, loanDateField, returnDateField);
        inputFields.setAlignment(Pos.CENTER);
        inputFields.setPadding(new Insets(10, 0, 10, 0));

        // Setup TableView
        TableView<Loan> loanTableView = setupLoanTableView();

        // Buttons for CRUD operations

        //Button addButton = new Button("Add");
        //HBox buttonBar = new HBox(10, addButton);
        //buttonBar.setAlignment(Pos.CENTER);
        //buttonBar.setPadding(new Insets(10, 0, 10, 0));

        // Action Handlers
        //addButton.setOnAction(e -> addBook(titleField, authorField, publisherField, isbnField, yearField, categoryField, copiesField, bookTableView));


        // Layout setup
        //VBox topContainer = new VBox(10, inputFields, buttonBar);
        //pane.setTop(topContainer);
        pane.setCenter(loanTableView);
        boolean actionsColumnExists = loanTableView.getColumns().stream()
                .anyMatch(column -> "Actions".equals(column.getText()));

        if (!actionsColumnExists) {
            TableColumn<Loan, Void> actionCol = new TableColumn<>("Actions");
            actionCol.setCellFactory(param -> new ButtonCellLoan(librarySystem));
            loanTableView.getColumns().add(actionCol);
        }

        return pane;
    }




/*
    private TableView<Loan> setupLoanTableView() {
        TableView<Loan> loanTableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Loan, String> userColumn = new TableColumn<>("User");
        //userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        userColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));

        TableColumn<Loan, String> bookColumn = new TableColumn<>("Book");
        //bookColumn.setCellValueFactory(new PropertyValueFactory<>("book"));
        bookColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Loan date");
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loan date"));

        TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Return date");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("return date"));


        // Add columns to the TableView
        loanTableView.getColumns().add(userColumn);
        loanTableView.getColumns().add(bookColumn);
        loanTableView.getColumns().add(loanDateColumn);
        loanTableView.getColumns().add(returnDateColumn);

        // Set the column resize policy to constrain the columns to the TableView's width
        loanTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Populate the TableView with books from the library system
        // This assumes you have a method in LibrarySystem to get all books as a List
        loanTableView.setItems(FXCollections.observableArrayList(librarySystem.getLoans()));




        //TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
        //actionCol.setCellFactory(param -> new ButtonCell(librarySystem)); // Simplified


        boolean actionsColumnExists = loanTableView.getColumns().stream()
                .anyMatch(column -> "Actions".equals(column.getText()));

        if (!actionsColumnExists) {
            TableColumn<Loan, Void> actionCol = new TableColumn<>("Actions");
            actionCol.setCellFactory(param -> new ButtonCellLoan(librarySystem));
            loanTableView.getColumns().add(actionCol);
        }

        loanTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loanTableView.setItems(FXCollections.observableArrayList(librarySystem.getLoans()));
        return loanTableView;
    }

 */
private TableView<Loan> setupLoanTableView() {
    TableView<Loan> loanTableView = new TableView<>();

    TableColumn<Loan, String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));

    TableColumn<Loan, String> bookColumn = new TableColumn<>("Book");
    bookColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));

    // Adjusted setup for loanDateColumn
    TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Loan Date");
    loanDateColumn.setCellValueFactory(cellData -> {
        java.util.Date loanDate = cellData.getValue().getLoanDate(); // Adjust if your method name is different
        String formattedDate = formatDate(loanDate);
        return new SimpleStringProperty(formattedDate);
    });

    // Adjusted setup for returnDateColumn
    TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Return Date");
    returnDateColumn.setCellValueFactory(cellData -> {
        java.util.Date returnDate = cellData.getValue().getReturnDate(); // Adjust if your method name is different
        String formattedDate = formatDate(returnDate);
        return new SimpleStringProperty(formattedDate);
    });

    loanTableView.getColumns().addAll(userColumn, bookColumn, loanDateColumn, returnDateColumn);
    loanTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    loanTableView.setItems(FXCollections.observableArrayList(librarySystem.getLoans()));

    return loanTableView;
}

    // Utility method for date formatting
    private String formatDate(java.util.Date date) {
        if (date == null) return "";
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

}