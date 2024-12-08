package org.example.myjavafx;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BookApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            LibrarySystem librarySystem = new LibrarySystem();
            TabPane tabPane = new TabPane();
            librarySystem.addCategory(new Category("drama"));
            librarySystem.addUser(new User("medialab", "medialab_2024", "", "", "admin", "admin_email"));
            Book book = new Book("Life", "Alex", "Me", "10989", 2023, "drama", 5);

            librarySystem.addBook(book);


            Tab booksTab = new Tab("Top Books", new TopBooksPane(librarySystem).getPane());
            Tab registerTab = new Tab("Register", new RegisterPane(librarySystem).getPane());
            Tab loginTab = new Tab("Login", new LoginPane(librarySystem, primaryStage).getPane());

            booksTab.setClosable(false);
            registerTab.setClosable(false);
            loginTab.setClosable(false);

            tabPane.getTabs().addAll(booksTab, registerTab, loginTab);

            //Scene scene = new Scene(tabPane, 800, 600);
            //primaryStage.setTitle("Book Application");
            //primaryStage.setScene(scene);
            //primaryStage.show();
            primaryStage.setScene(createInitialScene(primaryStage, librarySystem));
            primaryStage.setTitle("Book Application");
            primaryStage.show();
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    librarySystem.checkAndDeleteExpiredLoans();
                } catch (Exception e) {
                    e.printStackTrace(); // Log or handle exception appropriately
                }
            }, 0, 1, TimeUnit.DAYS);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    librarySystem.serializeDataOnShutdown();
                } catch (IOException e) {
                    System.err.println("Error serializing data on shutdown: " + e.getMessage());
                    e.printStackTrace();
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Scene createInitialScene(Stage primaryStage, LibrarySystem librarySystem) {
        TabPane tabPane = new TabPane();

        Tab booksTab = new Tab("Top Books", new TopBooksPane(librarySystem).getPane());
        Tab registerTab = new Tab("Register", new RegisterPane(librarySystem).getPane());
        Tab loginTab = new Tab("Login", new LoginPane(librarySystem, primaryStage).getPane());

        booksTab.setClosable(false);
        registerTab.setClosable(false);
        loginTab.setClosable(false);

        tabPane.getTabs().addAll(booksTab, registerTab, loginTab);

        return new Scene(tabPane, 800, 600);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
