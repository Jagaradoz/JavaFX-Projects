module org.example.snakegame {
    requires javafx.controls;
    requires javafx.fxml;

    opens application to javafx.fxml;
    opens controllers to javafx.fxml;

    exports application;
    exports controllers;
}