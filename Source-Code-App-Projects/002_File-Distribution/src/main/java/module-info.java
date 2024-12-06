module org.example.testfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens application to javafx.fxml;
    opens controllers to javafx.fxml;

    exports application;
    exports controllers;
}