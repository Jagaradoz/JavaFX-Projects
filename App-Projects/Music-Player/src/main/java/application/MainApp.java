package application;

import java.util.Objects;
import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;
import javafx.application.Application;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Get music player page (.FXML).
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MusicPlayerPage.fxml")));
        Scene scene = new Scene(root);

        // Get image icon.
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));

        stage.setTitle("Music Player");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        // Center the window on the screen.
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    public static void main(String[] args) {
        launch();
    }
}

