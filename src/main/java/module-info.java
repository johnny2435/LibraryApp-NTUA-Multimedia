module org.example.myjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.myjavafx to javafx.fxml;
    exports org.example.myjavafx;
}