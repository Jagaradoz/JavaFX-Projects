module org.example.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens application to javafx.fxml;
    opens controllers to javafx.fxml;

    exports application;
    exports controllers;
}