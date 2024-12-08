package org.example.myjavafx;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.stream.Collectors;

public class TopBooksPane {
    private VBox pane;
    private LibrarySystem librarySystem;
    public TopBooksPane(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem; // Initialize with an instance of LibrarySystem
        pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.setSpacing(8);

        ListView<String> booksListView = new ListView<>();

        // Use the getTopFiveBooks method to get the top 5 books
        List<Book> topBooks = librarySystem.getTopFiveBooks();

        // Convert the top books to a list of strings for display
        List<String> bookDetails = topBooks.stream()
                .map(book -> book.getTitle() + " by " + book.getAuthor() +
                        " (ISBN: " + book.getIsbn() + "), Avg. Rating: " +
                        String.format("%.2f", book.getAverageRating()) +
                        ", Rated by: " + book.getTotalRatings() + " users")
                .collect(Collectors.toList());

        booksListView.getItems().addAll(bookDetails);

        pane.getChildren().addAll(new Label("Top 5 Books"), booksListView);
    }

    public VBox getPane() {
        return pane;
    }
}
