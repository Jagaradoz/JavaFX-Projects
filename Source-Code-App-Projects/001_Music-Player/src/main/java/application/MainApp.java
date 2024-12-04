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
        // GETS MUSIC PLAYER PAGE (.FXML)
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MusicPlayerPage.fxml")));
        Scene scene = new Scene(root);

        // GETS IMAGE ICON
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));

        stage.setTitle("Music Player");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        // CENTERS THE WINDOW ON THE SCREEN
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    public static void main(String[] args) {
        launch();
    }
}

